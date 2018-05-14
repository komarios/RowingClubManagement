/*
 * Copyright (c) Carnegie Lake Rowing Association 2002. All rights reserved.
 * Distributed under the GPL license. See doc/COPYING.
 * $RCSfile: ApplicantDBRead.java,v $
 * $Date: 2003/02/26 03:38:46 $
 * $Revision: 1.3 $
 */

package com.clra.visitor;

import com.clra.util.ISerializableComparator;
import com.clra.util.DBConfiguration;
import com.clra.util.ErrorUtils;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.ejb.EJBException;
import javax.ejb.NoSuchEntityException;
import org.apache.log4j.Category;

/**
 * @version $Revision: 1.3 $ $Date: 2003/02/26 03:38:46 $
 * @author <a href="mailto:donaldzhu@sympatico.ca">Angela Yue</a>
 */
public final class ApplicantDBRead {

  private final static String base = ApplicantDBRead.class.getName();
  private final static Category theLog = Category.getInstance( base );

  /** All utilities are static */
  private ApplicantDBRead() {}

  /** Labels a null integer in the database, per JDBC */
  public final static int NULL_INT = 0;

  /** The format used to specify a date and time to the database */
  public final static SimpleDateFormat dateFormat =
          new SimpleDateFormat( Configuration.SQL_DATE_FORMAT );

  /** The format used to get a date out of the data base */
  public final static SimpleDateFormat sdfDBDate =
          new SimpleDateFormat( "yyyy-MM-dd" );

  /** The format used to get a time out of the data base */
  public final static SimpleDateFormat sdfDBTime =
          new SimpleDateFormat( "HH:mm:ss" );

  /** The format used to convert a date/time String to a Date */
  public final static SimpleDateFormat sdfConvert = dateFormat;
          /* new SimpleDateFormat( "MM/dd/yyyy hh:mm a" ); */

  /** Returns a collection of snapshots for all rowing sessions */
  public static
  Collection findAllApplicants() throws ApplicantException {

    Collection retVal = new ArrayList();

    Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    try {
      conn = DBConfiguration.getConnection();
      stmt = conn.prepareStatement(
          Configuration.SQL_05,
          ResultSet.TYPE_FORWARD_ONLY,
          ResultSet.CONCUR_READ_ONLY);
      rs = stmt.executeQuery();
      while ( rs.next() ) {
        ApplicantSnapshot ss = mapResultSetToApplicantSnapshot(rs);
        retVal.add( ss );
      } // while

    }
    catch( Exception x ) {
      String msg = ErrorUtils.createDbgMsg("findAllRowingSessionSnapshots",x);
      theLog.error( msg, x );
      throw new ApplicantException( msg );
    }
    finally {
      DBConfiguration.closeSQLResultSet( rs );
      DBConfiguration.closeSQLStatement( stmt );
      DBConfiguration.closeSQLConnection( conn );
      rs = null;
      stmt = null;
      conn = null;
    }

    return retVal;
  } // findAllApplicants()
  
  /**
   * Returns a collection of snapshots for rowing sessions within an
   * inclusive date range.
   */
  public static
  Collection findAllApplicantsInRange( Date start, Date finish )
    throws ApplicantException {

    // Preconditions
    if ( start == null || finish == null ) {
      throw new IllegalArgumentException( "null date" );
    }
    if ( start.compareTo(finish) > 0 ) {
      throw new IllegalArgumentException( "start > finish" );
    }

    Collection retVal = new ArrayList();

    Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    try {
      conn = DBConfiguration.getConnection();
      stmt = conn.prepareStatement(
          Configuration.SQL_06,
          ResultSet.TYPE_FORWARD_ONLY,
          ResultSet.CONCUR_READ_ONLY);

      String strStart  = ApplicantDBRead.dateFormat.format( start );
      String strFinish = ApplicantDBRead.dateFormat.format( finish );

      if ( theLog.isDebugEnabled() ) {
        final String prefix = "ApplicantDBRead.findAllRowing..InRange: ";
        theLog.debug( prefix + "strStart  == " + strStart );
        theLog.debug( prefix + "strFinish == " + strFinish );
      }

      stmt.setString( 1, strStart  );
      stmt.setString( 2, strFinish );

      rs = stmt.executeQuery();
      while ( rs.next() ) {
        ApplicantSnapshot ss = mapResultSetToApplicantSnapshot(rs);
        retVal.add( ss );
      } // while

    }
    catch( Exception x ) {
      String msg =
          ErrorUtils.createDbgMsg("findAllRowingSessionSnapshotsInRange",x);
      theLog.error( msg, x );
      throw new ApplicantException( msg );
    }
    finally {
      DBConfiguration.closeSQLResultSet( rs );
      DBConfiguration.closeSQLStatement( stmt );
      DBConfiguration.closeSQLConnection( conn );
      rs = null;
      stmt = null;
      conn = null;
    }

    return retVal;
  } // findAllApplicantsInRange( Date start, Date finish )  
  
  public static
  ApplicantSnapshot mapResultSetToApplicantSnapshot(ResultSet rs)
    throws SQLException, ApplicantException {

    final String lastname = rs.getString("name_last");
    final String firstname = rs.getString("name_first");
    final String middlename = rs.getString("name_middle");
    final String suffix = rs.getString("name_suffix");
    final String email = rs.getString("email");
    final String eveningtel = rs.getString("telephone_evening");
    final String daytel = rs.getString("telephone_day");
    final String othertel = rs.getString("telephone_other");
    final String addrstr1 = rs.getString("address_street1");
    final String addrstr2 = rs.getString("address_street2");
    final String city = rs.getString("address_city");
    final String state = rs.getString("address_state");
    final String zip = rs.getString("address_zip");
    final String experienceyear = rs.getString("experience_year");
    final String recentyear = rs.getString("recent_year");
    final Date birthday = rs.getDate("birthday");
    final String sex = rs.getString("sex");
    final Date applydate = rs.getDate("apply_date");
    final String status = rs.getString("status");
  
    if ( theLog.isDebugEnabled() ) {
      final String prefix = "ApplicantDBRead.mapResultToRowing: ";
    }

    ApplicantSnapshot retVal =
      new ApplicantSnapshot(lastname, firstname, middlename, suffix, email,
                            eveningtel, daytel, othertel, addrstr1, addrstr2,
                            city, state, zip, experienceyear, recentyear, birthday,
                            sex, applydate, status);

    return retVal;
  } // mapResultSetToApplicantSnapshot(ResultSet)


} // ApplicantDBRead

