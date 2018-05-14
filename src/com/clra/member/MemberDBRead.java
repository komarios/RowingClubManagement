/*
 * Copyright (c) Carnegie Lake Rowing Association 2002. All rights reserved.
 * Distributed under the GPL license. See doc/COPYING.
 * $RCSfile: MemberDBRead.java,v $
 * $Date: 2003/03/10 05:29:28 $
 * $Revision: 1.11 $
 */

package com.clra.member;

import com.clra.member.Address;
import com.clra.member.Configuration;
import com.clra.member.MemberException;
import com.clra.member.MemberName;
import com.clra.member.MemberSnapshot;
import com.clra.member.Telephone;
import com.clra.util.DBConfiguration;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.ejb.EJBException;
import javax.ejb.NoSuchEntityException;
import org.apache.log4j.Category;

/**
 * This utility class defines common routines for reading snapshots
 * from the database. In cases where read-only lists are presented
 * to a user, these routines are faster than their ejbFind counterparts.
 * However, data should never be written directly back to the database,
 * otherwise in-memory caches maintained by EJB's will be out of synch
 * and data will be corrupted.<p>
 *
 * @version $Revision: 1.11 $ $Date: 2003/03/10 05:29:28 $
 * @author <a href="mailto:rphall@pluto.njcc.com">Rick Hall</a>
 */
public final class MemberDBRead {

  private final static String base = MemberDBRead.class.getName();
  private final static Category theLog = Category.getInstance( base );

  /** All utilities are static */
  private MemberDBRead() {}

  /** The format used to get a date out of the data base */
  private final static SimpleDateFormat sdfDBDate =
          new SimpleDateFormat( "MM/dd/yyyy" );

  /** The format used to get a time out of the data base */
  private final static SimpleDateFormat sdfDBTime =
          new SimpleDateFormat( "hh:mm a" );

  // /** The format used to convert a date/time String to a Date */
  // private final static SimpleDateFormat sdfConvert =
  //         new SimpleDateFormat( "MM/dd/yyyy hh:mm a" );

  /**
   * Finds all active members of the CLRA. Members are sorted by
   * lastname-firstname.
   */
  public static Collection findAllMembersByLastName()
    throws MemberException {

    ArrayList list = new ArrayList();

    Connection conn = null;
    PreparedStatement stmt = null;
    Collection retVal = null;
    try {
      conn = DBConfiguration.getConnection();
      stmt = conn.prepareStatement(
          Configuration.SQL_01,
          ResultSet.TYPE_FORWARD_ONLY,
          ResultSet.CONCUR_READ_ONLY);
      retVal = loadMemberCollection( stmt );
    }
    catch(SQLException x) {
      String msg = "SQLException: " + x.getMessage();
      theLog.fatal( msg, x );
      throw new MemberException( msg );
    }
    finally {
      DBConfiguration.closeSQLStatement( stmt );
      DBConfiguration.closeSQLConnection( conn );
    }
    conn = null;
    stmt = null;

    return retVal;
  } // findAllMembersByLastName()

  /**
   * Finds all active members with last names greater than
   * or equal to the specified String. Selection is case-insensitive.
   * Members are sorted by lastname-firstname.
   */
  public static Collection findMembersWithLastNamesGTE( String lower )
    throws MemberException {

    ArrayList list = new ArrayList();

    Connection conn = null;
    PreparedStatement stmt = null;
    Collection retVal = null;
    try {
      conn = DBConfiguration.getConnection();
      stmt = conn.prepareStatement(
          Configuration.SQL_02,
          ResultSet.TYPE_FORWARD_ONLY,
          ResultSet.CONCUR_READ_ONLY);
      stmt.setString( 1, lower );
      retVal = loadMemberCollection( stmt );
    }
    catch(SQLException x) {
      String msg = "SQLException: " + x.getMessage();
      theLog.fatal( msg, x );
      throw new MemberException( msg );
    }
    finally {
      DBConfiguration.closeSQLStatement( stmt );
      DBConfiguration.closeSQLConnection( conn );
    }
    conn = null;
    stmt = null;

    return retVal;
  } // findMembersWithLastNamesGTE(String)

  /**
   * Finds all active members with last names less than
   * the specified String. Selection is case-insensitive. Members are
   * sorted by lastname-firstname.
   */
  public static Collection findMembersWithLastNamesLT( String upper )
    throws MemberException {

    ArrayList list = new ArrayList();

    Connection conn = null;
    PreparedStatement stmt = null;
    Collection retVal = null;
    try {
      conn = DBConfiguration.getConnection();
      stmt = conn.prepareStatement(
          Configuration.SQL_03,
          ResultSet.TYPE_FORWARD_ONLY,
          ResultSet.CONCUR_READ_ONLY);
      stmt.setString( 1, upper );
      retVal = loadMemberCollection( stmt );
    }
    catch(SQLException x) {
      String msg = "SQLException: " + x.getMessage();
      theLog.fatal( msg, x );
      throw new MemberException( msg );
    }
    finally {
      DBConfiguration.closeSQLStatement( stmt );
      DBConfiguration.closeSQLConnection( conn );
    }
    conn = null;
    stmt = null;

    return retVal;
  } // findMembersWithLastNamesLT(String)

  /**
   * Finds all active members with last names greater than
   * or equal to the specified lower bound, and less than the specified
   * upper bound. Selection is case-insensitive. Members are sorted by
   * lastname-firstname.
   */
  public static
  Collection findMembersWithLastNamesInRange( String lower, String upper )
    throws MemberException {

    ArrayList list = new ArrayList();

    Connection conn = null;
    PreparedStatement stmt = null;
    Collection retVal = null;
    try {
      conn = DBConfiguration.getConnection();
      stmt = conn.prepareStatement(
          Configuration.SQL_04,
          ResultSet.TYPE_FORWARD_ONLY,
          ResultSet.CONCUR_READ_ONLY);
      stmt.setString( 1, lower );
      stmt.setString( 2, upper );
      retVal = loadMemberCollection( stmt );
    }
    catch(SQLException x) {
      String msg = "SQLException: " + x.getMessage();
      theLog.fatal( msg, x );
      throw new MemberException( msg );
    }
    finally {
      DBConfiguration.closeSQLStatement( stmt );
      DBConfiguration.closeSQLConnection( conn );
    }
    conn = null;
    stmt = null;

    return retVal;
  } // findMembersWithLastNamesInRange(String,String)

  /**
   * Finds the member that has the given account name. Account names
   * are unique, so this "finder" returns at most one member.
   */
  public static MemberSnapshot findMemberByAccountName( String accountName )
    throws MemberException, NoSuchEntityException {

    Connection conn = null;
    PreparedStatement stmt = null;

    MemberSnapshot retVal = null;
    try {
      conn = DBConfiguration.getConnection();
      stmt = conn.prepareStatement(
          Configuration.SQL_05,
          ResultSet.TYPE_FORWARD_ONLY,
          ResultSet.CONCUR_READ_ONLY);
      stmt.setString( 1, accountName );
      retVal = loadMember( stmt );
    }
    catch(SQLException x) {
      String msg = "SQLException for accountName == '" + accountName + "': "
          + x.getMessage();
      theLog.fatal( msg, x );
      throw new MemberException( msg );
    }
    finally {
      DBConfiguration.closeSQLStatement( stmt );
      DBConfiguration.closeSQLConnection( conn );
    }
    conn = null;
    stmt = null;

    return retVal;
  } // findMemberByAccountName(String)

  /**
   * An alias for ${link #loadMember(Integer) loadMember(Integer)}.
   * @param a non-null key for an entry in the Member table.
   */
  public static MemberSnapshot findMemberByMemberId( Integer memberId )
    throws MemberException, NoSuchEntityException {
    return loadMember(memberId);
  }

  /**
   * Finds the member that has the given member id. Member id's
   * are unique, so this "finder" returns at most one snapshot.
   */
  public static MemberSnapshot loadMember( Integer memberId )
    throws MemberException, NoSuchEntityException {

    Connection conn = null;
    PreparedStatement stmt = null;

    MemberSnapshot retVal = null;
    try {
      conn = DBConfiguration.getConnection();
      stmt = conn.prepareStatement(
          Configuration.SQL_08,
          ResultSet.TYPE_FORWARD_ONLY,
          ResultSet.CONCUR_READ_ONLY);
      stmt.setInt( 1, memberId.intValue() );
      retVal = loadMember( stmt );
    }
    catch(SQLException x) {
      String msg = "SQLException for memberId == '" + memberId + "': "
          + x.getMessage();
      theLog.fatal( msg, x );
      throw new MemberException( msg );
    }
    finally {
      DBConfiguration.closeSQLStatement( stmt );
      DBConfiguration.closeSQLConnection( conn );
    }
    conn = null;
    stmt = null;

    return retVal;
  } // loadMember(Integer)

  static MemberSnapshot loadMember( PreparedStatement stmt )
    throws SQLException, MemberException, NoSuchEntityException, EJBException {

    // Precondition
    if ( stmt == null ) {
      throw new IllegalArgumentException( "null stmt" );
    }

    MemberSnapshot retVal = null;
    int rowCount = 0;
    Iterator iter = loadMemberCollection( stmt ).iterator();
    while ( iter.hasNext() && rowCount < 2 ) {
      ++rowCount;
      retVal = (MemberSnapshot) iter.next();
    }

    // Postconditions
    if ( rowCount == 0 ) {
      throw new NoSuchEntityException( "no results matching statement" );
    }
    else if ( rowCount > 1 ) {
      throw new EJBException( "2 or more results matching statement" );
    }

    return retVal;
} // loadMember(PreparedStatement);

public static Collection loadMemberCollection( PreparedStatement stmt )
    throws SQLException, MemberException, NoSuchEntityException, EJBException
{

    // Precondition
    if ( stmt == null ) {
      throw new IllegalArgumentException( "null stmt" );
    }

    final Collection retVal = new ArrayList();

    ResultSet rs = null;
    try {
      rs = stmt.executeQuery();
      while ( rs.next() ) {
        MemberSnapshot ms = mapResultSetToMemberSnapshot(rs);
        ms.setMemberRoles(loadMemberRoles(ms.getId().intValue()));
        retVal.add( ms );
      } // while
    }
    finally {
      DBConfiguration.closeSQLResultSet( rs );
      rs = null;
    }

    return retVal;
} // loadMemberCollection(PreparedStatement)

public static MemberRole[] loadMemberRoles(int member_id)
    throws MemberException {

    Connection conn = null;
    PreparedStatement stmt = null;
    Set set = null;
    try {
      conn = DBConfiguration.getConnection();
      stmt = conn.prepareStatement(
          Configuration.SQL_07,
          ResultSet.TYPE_FORWARD_ONLY,
          ResultSet.CONCUR_READ_ONLY);
      stmt.setInt( 1, (int)member_id  );
      set = loadMemberRolesSet( stmt );
    }
    catch(SQLException x) {
      String msg = "SQLException: " + x.getMessage();
      theLog.fatal( msg, x );
      throw new MemberException( msg );
    }
    finally {
      DBConfiguration.closeSQLStatement( stmt );
      DBConfiguration.closeSQLConnection( conn );
      conn = null;
      stmt = null;
    }

    MemberRole[] retVal = (MemberRole[]) set.toArray( new MemberRole[0] );

    return retVal;
} // loadMemberRoles

public static HashSet loadMemberRolesSet( PreparedStatement stmt )
    throws SQLException, MemberException, NoSuchEntityException, EJBException
{
    // Precondition
    if ( stmt == null ) {
      throw new IllegalArgumentException( "null stmt" );
    }

    final HashSet retVal = new HashSet();

    ResultSet rs = null;
    try {
    rs = stmt.executeQuery();
    while ( rs.next() ) {
      String strRole = rs.getString( "role" );
      MemberRole role = new MemberRole(strRole);
      retVal.add( role );
    } // while
    }
    finally {
      DBConfiguration.closeSQLResultSet( rs );
      rs = null;
    }

    return retVal;
  } // loadMemberRolesSet(PreparedStatement)

public static
  MemberSnapshot mapResultSetToMemberSnapshot(ResultSet rs)
    throws SQLException, MemberException {

    final int member_id             = rs.getInt(    "member_id"         );
    final int clra_year             = rs.getInt(    "clra_year"         );
    final Date birthday             = rs.getDate(   "birthday"          );
    final String account_name       = rs.getString( "account_name"      );
    final String account_passwd     = rs.getString( "account_passwd"    );
    final String clra_status        = rs.getString( "clra_status"       );
    final String name_last          = rs.getString( "name_last"         );
    final String name_first         = rs.getString( "name_first"        );
    final String name_middle        = rs.getString( "name_middle"       );
    final String name_suffix        = rs.getString( "name_suffix"       );
    final String strEmail           = rs.getString( "email"             );
    final String telephone_evening  = rs.getString( "telephone_evening" );
    final String telephone_day      = rs.getString( "telephone_day"     );
    final String telephone_other    = rs.getString( "telephone_other"   );
    final String address_street1    = rs.getString( "address_street1"   );
    final String address_street2    = rs.getString( "address_street2"   );
    final String address_city       = rs.getString( "address_city"      );
    final String address_state      = rs.getString( "address_state"     );
    final String address_zip        = rs.getString( "address_zip"       );

    if ( theLog.isDebugEnabled() ) {
      theLog.debug( member_id + ", " + name_last + ", " + name_first );
    }

    MemberSnapshot retVal = null;
    try {

      Integer id = new Integer( member_id );

      AccountType accountType = new AccountType( clra_status );

      MemberName memberName =
          new MemberName(name_first, name_middle, name_last, name_suffix);

      Email email = null;
      if ( strEmail != null && strEmail.trim().length() > 0 ) {
        email = new Email(strEmail);
      }

      Map telephoneNumbers = new HashMap();
      if (telephone_evening!=null && telephone_evening.trim().length()!=0) {
        Telephone telephone = new Telephone(telephone_evening);
        telephoneNumbers.put( Telephone.EVENING, telephone );
      }
      if ( telephone_day != null && telephone_day.trim().length() != 0 ) {
        Telephone telephone = new Telephone(telephone_day);
        telephoneNumbers.put( Telephone.DAY, telephone );
      }
      if ( telephone_other!=null && telephone_other.trim().length()!=0 ) {
        Telephone telephone = new Telephone(telephone_other);
        telephoneNumbers.put( Telephone.OTHER, telephone );
      }

      Address address = new Address( address_street1, address_street2,
              address_city, address_state, address_zip );

      final int JANUARY = 0;
      Calendar c = new GregorianCalendar();
      c.set( clra_year, JANUARY, 1);
      Date accountYear = c.getTime();

      retVal = new MemberSnapshot( id, account_name, account_passwd,
          accountType, memberName, email, telephoneNumbers, address,
          accountYear, birthday );

    }
    catch( IllegalArgumentException x ) {
      String msg = "invalid data for id == " +member_id+ ": " + x.getMessage();
      theLog.error( msg, x );
      throw new MemberException( msg );
    }

    return retVal;
  } // mapResultSetToMemberSnapshot(ResultSet)

} // MemberDBRead

/*
 * $Log: MemberDBRead.java,v $
 * Revision 1.11  2003/03/10 05:29:28  rphall
 * Fixed bug caused by new exception message
 *
 * Revision 1.10  2003/03/10 05:25:36  rphall
 * Added id to exception message
 *
 * Revision 1.9  2003/02/26 03:38:45  rphall
 * Added copyright and GPL license
 *
 * Revision 1.8  2003/02/20 04:44:38  rphall
 * Switched from String values to Object values: Email, MemberRole, etc
 *
 * Revision 1.7  2003/02/19 22:08:36  rphall
 * Removed gratuitous use of CLRA acronym
 *
 * Revision 1.6  2003/02/18 04:19:14  rphall
 * Removed ValidationException
 *
 * Revision 1.5  2003/02/15 04:31:41  rphall
 * Changes connected to major revision of MemberBean
 *
 */

