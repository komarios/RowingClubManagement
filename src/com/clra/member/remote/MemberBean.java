/*
 * Copyright (c) Carnegie Lake Rowing Association 2002. All rights reserved.
 * Distributed under the GPL license. See doc/COPYING.
 * $RCSfile: MemberBean.java,v $
 * $Date: 2003/02/26 03:38:45 $
 * $Revision: 1.11 $
 */

package com.clra.member.remote;

import com.clra.member.Configuration;
import com.clra.util.DBConfiguration;
import com.clra.util.ISerializableComparator;
import com.clra.util.ErrorUtils;
import com.clra.member.AccountType;
import com.clra.member.Address;
import com.clra.member.Email;
import com.clra.member.MemberDBRead;
import com.clra.member.MemberException;
import com.clra.member.MemberName;
import com.clra.member.MemberRole;
import com.clra.member.MemberSnapshot;
import com.clra.member.Telephone;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.text.SimpleDateFormat;
import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.EntityBean;
import javax.ejb.EntityContext;
import javax.ejb.FinderException;
import javax.ejb.NoSuchEntityException;
import javax.ejb.ObjectNotFoundException;
import javax.ejb.RemoveException;
import org.apache.log4j.Category;

/**
 * @author <a href="mailto:donaldzhu@sympatico.ca">Angela Yue</a>
 * @author <a href="mailto:rphall@pluto.njcc.com">Rick Hall</a>
 * @version $Revision: 1.11 $ $Date: 2003/02/26 03:38:45 $
 */

public class MemberBean implements EntityBean {

  private final static String base = MemberBean.class.getName();
  private final static Category theLog = Category.getInstance( base );

  private transient boolean isDirty = false;

  private EntityContext context = null;

  private Integer m_memberId = null;
  private String m_accountName = null;
  private String m_accountPassword = null;
  private AccountType m_accountType = null;
  private MemberName m_memberName = null;
  private Email m_email = null;
  private Telephone m_eveningPhone = null;
  private Telephone m_dayPhone = null;
  private Telephone m_otherPhone = null;
  private Address m_address = null;
  private Date m_accountYear = null;
  private Date m_birthDate = null;
  private Set m_roles = null;

  public final static SimpleDateFormat dateFormat =
          new SimpleDateFormat( Configuration.SQL_DATE_FORMAT );

  /** Set the context (and primary key) of this instance */
  public void setEntityContext(EntityContext ctx) {

    this.context = ctx;
    if ( theLog.isDebugEnabled() ) {
      theLog.debug( "setEntityContext: context set" );
      theLog.debug( "setEntityContext: id == " + this.m_memberId ); 
    }

    return;
  } // setEntityContext(EntityContext)

  /** Unset the context (and primary key) of this instance */
  public void unsetEntityContext() {

    this.context = null;
    if ( theLog.isDebugEnabled() ) {
      theLog.debug( "unsetEntityContext: id nulled" );
      theLog.debug( "unsetEntityContext: context nulled" );
    }

    return;
  } // unsetEntityContext()

  /**
   * Sets all properties of a member except the member id. The member id
   * of the specified snapshot must match the member id of this bean.
   */
  public void setData(MemberSnapshot data) {
    _setData(data);
  }

  /**
   * A private method outside the watchful eye of the EJB container. Used
   * to implement both <code>setData</code> and <code>loadRows()</code>.
   */
  private void _setData( MemberSnapshot data ) {

    if ( !m_memberId.equals(data.getId()) ) {
      String msg = "memberId == '" +m_memberId+ "' != '" +data.getId()+ "'";
      throw new IllegalArgumentException( msg );
    }

    setAccountName( data.getAccountName() );
    setAccountPassword( data.getAccountPassword() );
    setAccountType( data.getAccountType() );
    setMemberName( data.getMemberName() );
    setEmail( data.getEmail() );

    Map phones = data.getTelephoneNumbers();
    Telephone evening = (Telephone) phones.get( Telephone.EVENING );
    setEveningTelephone( evening );

    Telephone day = (Telephone) phones.get( Telephone.DAY );
    setDayTelephone( day );

    Telephone other = (Telephone) phones.get( Telephone.OTHER );
    setOtherTelephone( other );

    setAddress( data.getAddress() );
    setAccountYear( data.getAccountDate() );
    setBirthDate( data.getBirthDate() );
    setMemberRoles( data.getMemberRoles() );

    return;
  } // setData( MemberSnapshot )

  public void setAccountName(String accountName) {
    if ( accountName == null || accountName.trim().length() == 0 ) {
      String msg = "invalid account name == '" +accountName+ "'";
      throw new IllegalArgumentException( msg );
    }
    if ( m_accountName == null || !m_accountName.equals(accountName) ) {
      this.isDirty = true;
      m_accountName = accountName;
    }
    return;
  } // setAccountName(String)

  public void setAccountPassword(String password) {
    if ( password == null || password.trim().length() == 0 ) {
      String msg = "invalid password == '" +password+ "'";
      throw new IllegalArgumentException( msg );
    }
    if ( m_accountPassword == null || !m_accountPassword.equals(password) ) {
      this.isDirty = true;
      m_accountPassword = password;
    }
    return;
  } // setAccountPassword(String)

  public void setAccountType(AccountType type ) {
    if ( type == null ) {
      throw new IllegalArgumentException( "null account type" );
    }
    if ( m_accountType == null || !m_accountType.equals(type) ) {
      this.isDirty = true;
      m_accountType = type;
    }
    return;
  } // setAccountType(AccountType)

  public void setMemberName(MemberName name) {
    if ( name == null ) {
      throw new IllegalArgumentException( "null member name" );
    }
    if ( m_memberName == null || !m_memberName.equals(name) ) {
      this.isDirty = true;
      m_memberName = name;
    }
    return;
  } // setMemberName(MemberName)

  public void setEmail(Email email) {
    if ( m_email != null && !m_email.equals(email) ) {
      this.isDirty = true;
      m_email = email;
    }
    else if ( m_email == null && email != null ) {
      this.isDirty = true;
      m_email = email;
    }
    return;
  } // setEmail(Email)

  public void setTelephones( Map telephones ) {

    if ( telephones == null ) {
      throw new IllegalArgumentException( "null telephone map" );
    }

    Telephone phone = (Telephone) telephones.get( Telephone.EVENING );
    if ( phone == null ) {
      String msg = "null evening phone";
      theLog.error( msg );
      throw new IllegalArgumentException( msg );
    }
    if ( m_eveningPhone == null || !m_eveningPhone.equals(phone) ) {
      this.isDirty = true;
      m_eveningPhone = phone;
    }

    phone = (Telephone) telephones.get( Telephone.DAY );
    if ( m_dayPhone != null && !m_dayPhone.equals(phone) ) {
      this.isDirty = true;
      m_dayPhone = phone;
    }
    else if ( m_dayPhone == null && phone != null ) {
      this.isDirty = true;
      m_dayPhone = phone;
    }

    phone = (Telephone) telephones.get( Telephone.OTHER );
    if ( m_otherPhone != null && !m_otherPhone.equals(phone) ) {
      this.isDirty = true;
      m_otherPhone = phone;
    }
    else if ( m_otherPhone == null && phone != null ) {
      this.isDirty = true;
      m_otherPhone = phone;
    }

    return;
  } // setTelephones(Map)

  public void setEveningTelephone(Telephone phone) {
    if ( phone == null ) {
      throw new IllegalArgumentException( "null evening phone" );
    }
    if ( m_eveningPhone == null || !m_eveningPhone.equals(phone) ) {
      this.isDirty = true;
      m_eveningPhone = phone;
    }
    return;
  } // setEveningTelephone(Telephone)

  public void setDayTelephone(Telephone phone) {
    if ( m_dayPhone != null && !m_dayPhone.equals(phone) ) {
      this.isDirty = true;
      m_dayPhone = phone;
    }
    else if ( m_dayPhone == null && phone != null ) {
      this.isDirty = true;
      m_dayPhone = phone;
    }
    return;
  } // setDayTelephone(Telephone)

  public void setOtherTelephone(Telephone phone) {
    if ( m_otherPhone != null && !m_otherPhone.equals(phone) ) {
      this.isDirty = true;
      m_otherPhone = phone;
    }
    else if ( m_otherPhone == null && phone != null ) {
      this.isDirty = true;
      m_otherPhone = phone;
    }
    return;
  } // setOtherTelephone(Telephone)

  public void setAddress(Address address) {
    if ( address == null ) {
      throw new IllegalArgumentException( "null mailing address" );
    }
    if ( m_address == null || !m_address.equals( address ) ) {
      this.isDirty = true;
      m_address = address;
    }
    return;
  } // setAddress(Address)

  public void setAccountYear(Date year) {
    if ( year == null ) {
      throw new IllegalArgumentException( "null year" );
    }
    if ( m_accountYear == null || !m_accountYear.equals( year ) ) {
      this.isDirty = true;
      m_accountYear = year;
    }
    return;
  } // setAccountYear(Date)

  public void setBirthDate(Date date) {
    if ( m_birthDate != null && !m_birthDate.equals(date) ) {
      this.isDirty = true;
      m_birthDate = date;
    }
    else if ( date != null ) {
      this.isDirty = true;
      m_birthDate = date;
    }
    return;
  } // setBirthDate(Date)

  public void setMemberRoles( MemberRole[] roles ) {
    if ( roles == null ) {
      throw new IllegalArgumentException( "null roles" );
    }
    boolean isOutOfDate = false;
    if ( m_roles == null ) {
      isOutOfDate = true;
    }
    else if ( m_roles.size() != roles.length ) {
      isOutOfDate = true;
    }
    else {
      for ( int i=0; i<roles.length; i++ ) {
        if ( roles[i] != null && !m_roles.contains(roles[i]) ) {
          isOutOfDate = true;
          break;
        }
        else if ( roles[i] == null ) {
          isOutOfDate = true;
          break;
        }
      } // for
    }

    if ( isOutOfDate ) {
      this.isDirty = true;
      m_roles = new HashSet( roles.length );
      for ( int i=0; i<roles.length; i++ ) {
        if ( roles[i] != null ) {
          m_roles.add( roles[i] );
        }
      } // for
    }

    return;
  } // setMemberRoles(MemberRole[])

  public Integer getId() {
    return m_memberId;
  }

  public MemberSnapshot getData() {

    Map phones = new HashMap();
    phones.put( Telephone.EVENING, m_eveningPhone );
    if ( m_dayPhone != null ) {
      phones.put( Telephone.DAY, m_dayPhone );
    }
    if ( m_otherPhone != null ) {
      phones.put( Telephone.OTHER, m_otherPhone );
    }

    MemberRole[] roles = (MemberRole[]) m_roles.toArray( new MemberRole[0] );

    MemberSnapshot retVal = new MemberSnapshot( m_memberId,
      m_accountName, m_accountPassword, m_accountType, m_memberName,
      m_email, phones, m_address, m_accountYear, m_birthDate, roles );

    return retVal;
  } // getData()

  public String getAccountName() {
    return m_accountName;
  }

  public String getAccountPassword() {
    return m_accountPassword;
  }

  public AccountType getAccountType() {
    return m_accountType;
  }

  public MemberName getMemberName() {
    return m_memberName;
  }

  public Email getEmail() {
    return m_email;
  }

  public Map getTelephones() {

    if ( m_eveningPhone == null ) {
      throw new IllegalStateException( "null evening phone" );
    }

    Map retVal = new HashMap();
    retVal.put( Telephone.EVENING, m_eveningPhone );
    if ( m_dayPhone != null ) {
      retVal.put( Telephone.DAY, m_dayPhone );
    }
    if ( m_otherPhone != null ) {
      retVal.put( Telephone.OTHER, m_otherPhone );
    }

    return retVal;
  } // getTelephones()

  public Telephone getEveningTelephone() {
    return m_eveningPhone;
  }

  public Telephone getDayTelephone() {
    return m_dayPhone;
  }

  public Telephone getOtherTelephone() {
    return m_otherPhone;
  }

  public Address getAddress() {
    return m_address;
  }

  public Date getAccountYear() {
    return m_accountYear;
  }

  public Date getBirthDate() {
    return m_birthDate;
  }

  public MemberRole[] getMemberRoles() {
    MemberRole[] retVal = (MemberRole[]) m_roles.toArray( new MemberRole[0] );
    return retVal;
  }

  public Integer ejbCreate(
                  String accountName,
                  String accountPassword,
                  AccountType accountType,
                  MemberName memberName,
                  Email email,
                  Telephone evening,
                  Telephone day,
                  Telephone other,
                  Address address,
                  Date accountYear,
                  Date birthDate,
                  MemberRole[] roles
                ) throws CreateException {

    try {

      m_memberId = nextId();

      setAccountName( accountName );
      setAccountPassword( accountPassword );
      setAccountType( accountType );
      setMemberName( memberName );
      setEmail( email );
      setEveningTelephone( evening );
      setDayTelephone( day );
      setOtherTelephone( other );
      setAddress( address );
      setAccountYear( accountYear );
      setBirthDate( birthDate );
      setMemberRoles( roles );

      insertRows();

      // FIXME shouldn't isDirty be set false here?
      //this.isDirty = false;
      // ENDFIXME

      if ( theLog.isDebugEnabled() ) {
        String msg = "ejbCreate: " + m_memberId + ", '" + accountName
          + "', '" + memberName.getLastName()
          + "', '" + memberName.getFirstName() + "'";
        theLog.debug( msg );
      }

    }
    catch (Exception ex) {
      theLog.error( "ejbCreate", ex );
      throw new EJBException( "ejbCreate: " + ex.getMessage() );
    }

    return m_memberId;
  } // ejbCreate

  public void ejbPostCreate( String accountName, String accountPassword,
      AccountType accountType, MemberName memberName, Email email,
      Telephone evening, Telephone day, Telephone other, Address address,
      Date accountYear, Date birthDate, MemberRole[] roles ) {
  }

  public Integer ejbFindByPrimaryKey(final Integer primaryKey)
    throws FinderException {

    // Precondition
    if ( primaryKey == null ) {
      throw new FinderException( "null primaryKey" );
    }

    Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    boolean hasRow = false;
    try {
      conn = DBConfiguration.getConnection();
      stmt = conn.prepareStatement(Configuration.SQL_08,
                                   ResultSet.TYPE_FORWARD_ONLY,
                                   ResultSet.CONCUR_READ_ONLY);

      stmt.setInt( 1, primaryKey.intValue() );

      rs = stmt.executeQuery();
      hasRow = rs.next();
      if ( theLog.isDebugEnabled() ) {
        theLog.debug( "selectByPrimaryKey: hasRow == " + hasRow );
      }

    } catch (Exception ex) {
        theLog.debug("Exception in ejbFindByPrimaryKey: " + ex.getMessage());
    }
    finally {
      DBConfiguration.closeSQLResultSet( rs );
      DBConfiguration.closeSQLStatement( stmt );
      DBConfiguration.closeSQLConnection( conn );
      rs = null;
      stmt = null;
      conn = null;
    }

    if (!hasRow) {
      String msg = "Row for primaryKey '" + primaryKey + "' not found.";
      throw new ObjectNotFoundException( msg );
    }

    return primaryKey;
  } // ejbFindByPrimaryKey(Integer)

  public Integer ejbFindByAccountName(String name)
    throws FinderException {

    Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    boolean retVal = false;
    Integer primaryKey = null;

    try {
      conn = DBConfiguration.getConnection();
      stmt = conn.prepareStatement(Configuration.SQL_081,
                                   ResultSet.TYPE_FORWARD_ONLY,
                                   ResultSet.CONCUR_READ_ONLY);

      stmt.setString( 1, name );

      rs = stmt.executeQuery();
      retVal = rs.next();
      primaryKey = new Integer(rs.getInt(1));

      if ( theLog.isDebugEnabled() ) {
        theLog.debug( "selectByPrimaryKey: retVal == " + retVal );
      }

    } catch (Exception ex) {
        theLog.debug("Exception in ejbFindByPrimaryKey: " + ex);
    }
    finally {
      DBConfiguration.closeSQLResultSet( rs );
      DBConfiguration.closeSQLStatement( stmt );
      DBConfiguration.closeSQLConnection( conn );
      rs = null;
      stmt = null;
      conn = null;
    }

    if (!retVal) {
      String msg = "Row for primary '" + primaryKey + "' not found.";
      throw new ObjectNotFoundException( msg );
    }

    m_memberId = primaryKey;
    theLog.info("findByAccountName m_memberId = " + m_memberId );

    return primaryKey;
  } // ejbFindByAccountName(String)

  public Collection ejbFindAll()
    throws FinderException {

    Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    Collection retVal = new ArrayList();
    try {
      conn = DBConfiguration.getConnection();
      stmt = conn.prepareStatement(Configuration.SQL_09,
                                   ResultSet.TYPE_FORWARD_ONLY,
                                   ResultSet.CONCUR_READ_ONLY);

      rs = stmt.executeQuery();

      int rowCount = 0;
      while ( rs.next() ) {
        Integer m_memberId = new Integer( rs.getInt(1) );
        retVal.add(m_memberId);
        ++rowCount;
      }

      if ( theLog.isDebugEnabled() ) {
        String msg = "selectAll: rowCount == " + rowCount;
        theLog.debug( msg );
      }

    } catch (Exception ex) {
        theLog.debug("Exception in ejbFindAll: " + ex);
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
  } // ejbFindAll()

  /** @see delete() */
  public void ejbRemove() {

    Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;

    try {
      conn = DBConfiguration.getConnection();
      stmt = conn.prepareStatement(Configuration.SQL_10,
                                   ResultSet.TYPE_FORWARD_ONLY,
                                   ResultSet.CONCUR_READ_ONLY);

      stmt.setInt( 1, m_memberId.intValue());

      rs = stmt.executeQuery();

      deleteRoles(); // delete old roles

      if ( theLog.isDebugEnabled() ) {
        theLog.debug( "deleteRow: row " + m_memberId );
      }

    } catch (Exception ex) {
        theLog.debug("Exception in ejbRemove: " + ex);
    }

    finally {
      DBConfiguration.closeSQLResultSet( rs );
      DBConfiguration.closeSQLStatement( stmt );
      DBConfiguration.closeSQLConnection( conn );
      rs = null;
      stmt = null;
      conn = null;
    }

    return;
  } // ejbRemove()

  public void ejbActivate() {
    this.m_memberId = (Integer) this.context.getPrimaryKey();
    if ( theLog.isDebugEnabled() ) {
      theLog.debug( "ejbActivate: m_memberId == " + m_memberId );
    }
  }

  public void ejbPassivate() {
    this.m_memberId = null;
    if ( theLog.isDebugEnabled() ) {
      theLog.debug( "MemberBean.ejbPassivate: m_memberId == " + m_memberId );
    }
  }

  public void ejbLoad() {

    this.m_memberId = (Integer) this.context.getPrimaryKey();
    if ( theLog.isDebugEnabled() ) {
      theLog.debug( "ejbLoad: m_memberId == " + m_memberId );
    }

    try {
      loadRows();
      if ( theLog.isDebugEnabled() ) {
        theLog.debug( "ejbLoad: row loaded" );
      }
      isDirty = false;
    }
    catch (Exception ex) {
      theLog.error( "ejbLoad", ex );
      throw new EJBException("ejbLoad: " + ex.getMessage());
    }

  } // ejbLoad()

  public void ejbStore() {

    try {
      if ( isDirty ) {
        storeRows();
        if ( theLog.isDebugEnabled() ) {
            theLog.debug( "ejbStore: row stored for #"  + m_memberId );
        }
        isDirty = false;
      }
      else {
        if ( theLog.isDebugEnabled() ) {
          theLog.debug( "ejbStore: row skipped" );
        }
      }
    }
    catch (Exception ex) {
      String msg = ErrorUtils.createDbgMsg( "ejbStore", ex );
      theLog.error( msg, ex );
      throw new EJBException(msg);
    }

  } // ejbStore()

  /** Inserts a single Member row, and possibly multiple MemberRole rows */
  private void insertRows() throws SQLException, CreateException {

    Connection conn = null;
    PreparedStatement stmt = null;
    int rowCount = 0;
    try {

      conn = DBConfiguration.getConnection();
      stmt = conn.prepareStatement(Configuration.SQL_12);

      stmt.setInt(1, m_memberId.intValue() );
      stmt.setString(2, m_accountName );
      stmt.setString(3, m_accountPassword );
      stmt.setString(4, m_accountType.toString() );
      stmt.setString(5, m_memberName.getLastName() );
      stmt.setString(6, m_memberName.getFirstName() );

      int paramInd = 7;
      final String nmiddle = m_memberName.getMiddleName();
      if ( nmiddle != null && nmiddle.length() > 0 ) {
          stmt.setString(paramInd, nmiddle);
      } else {
          stmt.setNull(paramInd, Types.VARCHAR);
      }
      paramInd++;

      final String nsuffix = m_memberName.getSuffix();
      if ( nsuffix != null && nsuffix .length() > 0 ) {
          stmt.setString(paramInd, nsuffix );
      } else {
          stmt.setNull(paramInd, Types.VARCHAR);
      }
      paramInd++;

      final String email = m_email == null ? null : m_email.toString();
      if ( email != null && email .length() > 0 ) {
          stmt.setString(paramInd, email );
      } else {
          stmt.setNull(paramInd, Types.VARCHAR);
      }
      paramInd++;

      final String tel_evening = toDataString(m_eveningPhone);
      stmt.setString(paramInd, tel_evening );
      paramInd++;

      final String tel_day =
          m_dayPhone == null ? null : toDataString(m_dayPhone);
      if ( tel_day != null ) {
          stmt.setString(paramInd, tel_day );
      } else {
          stmt.setNull(paramInd, Types.VARCHAR);
      }
      paramInd++;

      final String tel_other =
            m_otherPhone == null ? null : toDataString(m_otherPhone);
      if ( tel_other != null ) {
          stmt.setString(paramInd, tel_other );
      } else {
          stmt.setNull(paramInd, Types.VARCHAR);
      }
      paramInd++;

      final String addr_str1 = m_address.getStreet1();
      stmt.setString(paramInd, addr_str1 );
      paramInd++;

      final String addr_str2 = m_address.getStreet2();
      if ( addr_str2 != null && addr_str2 .length() > 0 ) {
          stmt.setString(paramInd, addr_str2 );
      } else {
          stmt.setNull(paramInd, Types.VARCHAR);
      }
      paramInd++;

      final String addr_city = m_address.getCity();
      stmt.setString(paramInd, addr_city );
      paramInd++;

      final String addr_state = m_address.getState();
      stmt.setString(paramInd, addr_state );
      paramInd++;

      final String addr_zip = m_address.getZip();
      stmt.setString(paramInd, addr_zip );
      paramInd++;

      final int year = m_accountYear.getYear() + 1900;
      stmt.setInt(paramInd, year);
      paramInd++;

      if ( m_birthDate != null ) {
          String s = dateFormat.format( m_birthDate );
          stmt.setString(paramInd, s );
      } else {
          stmt.setNull(paramInd, Types.VARCHAR);
      }

      rowCount = stmt.executeUpdate();

      if ( theLog.isDebugEnabled() ) {
        theLog.debug("insertRow count: " + rowCount);
        theLog.debug("insertRow memberId: " + m_memberId);
        theLog.debug("insertRow accountName: " + m_accountName);
        theLog.debug("insertRow accountPassword: " + m_accountPassword);
        theLog.debug("insertRow status: " + m_accountType.toString());
        theLog.debug("insertRow lname: '" +m_memberName.getLastName()+ "'");
        theLog.debug("insertRow fname: '" +m_memberName.getFirstName()+ "'");
        theLog.debug("insertRow tel_evening: " + tel_evening);
        theLog.debug("insertRow addr_str1: " + addr_str1);
        theLog.debug("insertRow addr_city: " + addr_city);
        theLog.debug("insertRow addr_state: " + addr_state);
        theLog.debug("insertRow addr_zip: " + addr_zip);
        theLog.debug("insertRow year: " + year);
      }

      deleteRoles(); // delete old roles
      insertRoles(); // insert current roles

    }
    finally {
      DBConfiguration.closeSQLStatement( stmt );
      stmt = null;
      DBConfiguration.closeSQLConnection( conn );
      conn = null;
    }

    if ( rowCount != 1 ) {
      String msg = "failed to insert member ==  " + m_memberId;
      theLog.error( msg );
      throw new CreateException( msg );
    }

    return;
  } // insertRows

  /** Load a single Member row, and possibly multiple MemberRole rows */
  private void loadRows() throws MemberException, SQLException {

    /*
     * Implementation note: the translation from DB row to snapshot
     * to bean looks inefficient, but it is not the bottleneck in
     * loading bean. The bottleneck is the EJB algorithm for finding
     * an id, then loading a row, then storing the result back to
     * the DB. That means at least two DB calls (assuming the storage
     * step is skipped by checking an isDirty flag). The DB calls
     * completely dominate performance.
     *
     * On the other hand, returning a row as a snapshot allows the
     * MemberDBRead code to be reused directly elsewhere. Where
     * speed is critical, and where it is unlikely that a MemberBean
     * is already in memory, it is best to use the MemberDBRead code to
     * directly load a (read-only) snapshot from the database.
     */

    MemberSnapshot data = MemberDBRead.loadMember( m_memberId );
    _setData( data );

    return;
  } // loadRows()

  /** Stores a single Member row, and possibly multiple MemberRole rows */
  private void storeRows() throws SQLException {

    Connection conn = null;
    PreparedStatement stmt = null;
    int rowCount = 0;
    try {

      conn = DBConfiguration.getConnection();
      stmt = conn.prepareStatement(Configuration.SQL_11);

      stmt.setInt(    19, m_memberId.intValue() );
      stmt.setString( 17, m_accountName );
      stmt.setString(  1, m_accountPassword );
      stmt.setString( 18, m_accountType.toString() );
      stmt.setString(  2, m_memberName.getLastName() );
      stmt.setString(  3, m_memberName.getFirstName() );

      final String nmiddle = m_memberName.getMiddleName();
      if (nmiddle != null && nmiddle.length() > 0 ) {
          stmt.setString( 4, nmiddle);
      } else {
          stmt.setNull( 4, Types.VARCHAR);
      }

      final String nsuffix = m_memberName.getSuffix();
      if ( nsuffix != null && nsuffix .length() > 0 ) {
          stmt.setString( 5, nsuffix );
      } else {
          stmt.setNull( 5, Types.VARCHAR);
      }

      final String email = m_email == null ? null : m_email.toString();
      if ( email != null && email .length() > 0 ) {
          stmt.setString( 6, email );
      } else {
          stmt.setNull( 6, Types.VARCHAR);
      }

      stmt.setString( 7, toDataString(m_eveningPhone) );

      if ( m_dayPhone != null ) {
        stmt.setString( 8, toDataString(m_dayPhone) );
      }
      else {
        stmt.setNull( 8, Types.VARCHAR );
      }

      if ( m_otherPhone != null ) {
        stmt.setString( 9, toDataString(m_otherPhone) );
      }
      else {
        stmt.setNull( 9, Types.VARCHAR );
      }

      stmt.setString(10, m_address.getStreet1() );

      String addrstr2 = m_address.getStreet2();
      if ( addrstr2 != null && addrstr2.trim().length() > 0) {
        stmt.setString( 11, addrstr2 );
      }
      else {
        stmt.setNull( 11, Types.VARCHAR );
      }

      stmt.setString( 12, m_address.getCity() );
      stmt.setString( 13, m_address.getState() );
      stmt.setString( 14, m_address.getZip() );
      stmt.setInt( 15, 1900 + m_accountYear.getYear() );

      if ( m_birthDate != null ) {
        String s = dateFormat.format( m_birthDate );
        stmt.setString( 16, s );
      }
      else {
        stmt.setNull( 16, Types.VARCHAR );
      }

      rowCount = stmt.executeUpdate();

      if ( theLog.isDebugEnabled() ) {
        theLog.debug( "storeRows: rowCount == " + m_memberId );
        theLog.debug( "storeRows: memberId: " + m_memberId);
        theLog.debug( "storeRows: accountName: " + m_accountName);
        theLog.debug( "storeRows: accountPassword: " + m_accountPassword);
        theLog.debug( "storeRows: status: " + m_accountType.toString());
        theLog.debug( "storeRows: lname: '" +m_memberName.getLastName()+ "'");
        theLog.debug( "storeRows: fname: '" +m_memberName.getFirstName()+ "'");
        theLog.debug( "storeRows: tel_evening: " + m_eveningPhone.toString());
        theLog.debug( "storeRows: addr_str1: '" + m_address.getStreet1() + "'");
        theLog.debug( "storeRows: addr_city: '" + m_address.getCity() + "'");
        theLog.debug( "storeRows: addr_state: '" + m_address.getState() + "'");
        theLog.debug( "storeRows: addr_zip: '" + m_address.getZip() + "'");
        theLog.debug( "storeRows: year: " + (1900 + m_accountYear.getYear()) );
      }

      deleteRoles(); // delete old roles
      insertRoles(); // insert current roles

    }
    finally {
      DBConfiguration.closeSQLStatement( stmt );
      stmt = null;
      DBConfiguration.closeSQLConnection( conn );
      conn = null;
    }

    if ( rowCount != 1 ) {
      String msg = "failed to store member ==  " + m_memberId;
      theLog.error( msg );
      throw new EJBException( msg );
    }

    return;
  } // storeRows()

  /** Inserts (possibly multiple) MemberRole rows */
  private void insertRoles() throws SQLException {

    Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;

    try {
      conn = DBConfiguration.getConnection();
      stmt = conn.prepareStatement(Configuration.SQL_16);

      MemberRole[] roles = this.getMemberRoles();
      for ( int i=0; i<roles.length; i++ ) {

        stmt.setInt( 1, m_memberId.intValue() );
        stmt.setString( 2, roles[i].getRole() );
        stmt.setString( 3, roles[i].getRoleGroup() );

        int count = stmt.executeUpdate();

        if ( theLog.isDebugEnabled() ) {
          theLog.debug("insertRole count: " + count);
          theLog.debug("insertRole member_id: " + m_memberId);
          theLog.debug("insertRole role: " + roles[i].getRole() );
          theLog.debug("insertRole roleGroup: " + roles[i].getRoleGroup() );
        }

      } // for

    }
    finally {
      DBConfiguration.closeSQLResultSet( rs );
      rs = null;
      DBConfiguration.closeSQLStatement( stmt );
      stmt = null;
      DBConfiguration.closeSQLConnection( conn );
      conn = null;
    }

    return;
  } // insertRoles()

  /** Deletes (possibly multiple) MemberRole rows */
  private void deleteRoles() throws SQLException {

    Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;

    try {
      conn = DBConfiguration.getConnection();
      stmt = conn.prepareStatement(Configuration.SQL_17);

      stmt.setInt( 1, m_memberId.intValue() );

      //rs = stmt.executeQuery();
      int count = stmt.executeUpdate();

      if ( theLog.isDebugEnabled() ) {
        theLog.debug("deleteRole member_id: " + m_memberId);
      }

    }
    finally {
      DBConfiguration.closeSQLResultSet( rs );
      rs = null;
      DBConfiguration.closeSQLStatement( stmt );
      stmt = null;
      DBConfiguration.closeSQLConnection( conn );
      conn = null;
    }

    return;
  } // deleteRoles()

  /** A utility that gets (and reserves) the next id for a member */
  public static Integer nextId() throws SQLException, CreateException {

    Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    Integer retVal = null;
    try {
      conn = DBConfiguration.getConnection();
      stmt = conn.prepareStatement(
          Configuration.SQL_15,
          ResultSet.TYPE_FORWARD_ONLY,
          ResultSet.CONCUR_READ_ONLY);

      if ( theLog.isDebugEnabled() ) {
        String msg =
          "nextId update: SQL == " + Configuration.SQL_15;
        theLog.debug( msg );
      }

      if ( !DBConfiguration.isOracle() ) {

        /*
         * There is a problem in this block of code because it is not
         * locked in a transaction. If two different threads execute
         * this block simultaneously, the MySQL sequence value will
         * have been incremented twice, and both threads will use the
         * same value in the code section that follows this block.
         *
         * As a practical matter, during normal operation, rowing
         * sessions are created infrequently, and this block of code
         * is executed by one thread at a time. This code works because
         * this is a low-load application.
         */

        boolean moreResults = stmt.execute();

        stmt = conn.prepareStatement(
            Configuration.SQL_15A,
            ResultSet.TYPE_FORWARD_ONLY,
            ResultSet.CONCUR_READ_ONLY);

        if ( theLog.isDebugEnabled() ) {
          String msg =
            "nextId select: SQL == " + Configuration.SQL_15A;
          theLog.debug( msg );
        }

      } // if !isOracle

      rs = stmt.executeQuery();

      int rowCount = 0;
      while ( rs.next() ) {
        retVal = new Integer( rs.getInt(1) );
        ++rowCount;
      }
      if ( rowCount != 1 ) {
        String msg =  "unable to get next id: rowCount == " + rowCount;
        throw new CreateException( msg );
      }

      if ( theLog.isDebugEnabled() ) {
        String msg = "nextId: retVal == " + retVal.intValue();
        theLog.debug( msg );
      }

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
  } // nextId()

  private static String toDataString( Telephone phone ) {

    if ( phone == null ) {
      throw new IllegalArgumentException( "null phone" );
    }

    StringBuffer sb = new StringBuffer();
    sb.append( phone.getAreaCode() ); 
    sb.append( " " );
    sb.append( phone.getExchange() );
    sb.append( "-" );
    sb.append( phone.getLocal() );
    String ext = phone.getExtension();
    if ( ext != null && ext.trim().length() > 0 ) {
      sb.append( " ext. " );
      sb.append( ext.trim() );
    }
    String retVal = new String(sb);

    return retVal;
  } // toDataString(Telephone)

} // MemberBean

/*
 * $Log: MemberBean.java,v $
 * Revision 1.11  2003/02/26 03:38:45  rphall
 * Added copyright and GPL license
 *
 * Revision 1.10  2003/02/21 14:30:55  rphall
 * Fixed bug with blank extensions
 *
 * Revision 1.9  2003/02/21 05:02:33  rphall
 * Fixed bug where accountType, accountName weren't stored
 *
 * Revision 1.8  2003/02/20 04:58:03  rphall
 * Added insert/deleteRoles
 *
 */

