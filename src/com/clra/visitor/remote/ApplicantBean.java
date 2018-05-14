/*
 * Copyright (c) Carnegie Lake Rowing Association 2002. All rights reserved.
 * Distributed under the GPL license. See doc/COPYING.
 * $RCSfile: ApplicantBean.java,v $
 * $Date: 2003/02/26 03:38:46 $
 * $Revision: 1.6 $
 */

package com.clra.visitor.remote;

import com.clra.visitor.Configuration;
import com.clra.util.DBConfiguration;
import com.clra.util.ISerializableComparator;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.EntityBean;
import javax.ejb.EntityContext;
import javax.ejb.FinderException;
import javax.ejb.NoSuchEntityException;
import javax.ejb.ObjectNotFoundException;
import javax.ejb.RemoveException;
import java.text.SimpleDateFormat;
import org.apache.log4j.Category;

/**
 * @author <a href="mailto:donaldzhu@sympatico.ca">Angela Yue</a>
 * @version $Revision: 1.6 $ $Date: 2003/02/26 03:38:46 $
 */


public class ApplicantBean implements EntityBean {

  private final static String base = ApplicantBean.class.getName();
  private final static Category theLog = Category.getInstance( base );

  public final static SimpleDateFormat dateFormat =
          new SimpleDateFormat( Configuration.SQL_DATE_FORMAT );

  private transient boolean isDirty = true;
  private Date date = null;
  private String email_addr = null;

  private EntityContext context;

  public String ejbCreate(String nlast,
                  String nfirst,
                  String nmiddle,
                  String nsuffix,
                  String mail,
                  String tel_evening,
                  String tel_day,
                  String tel_other,
                  String addr_str1,
                  String addr_str2,
                  String addr_city,
                  String addr_state,
                  String addr_zip,
                  String experience_year,
                  String recent_year,
                  Date birth,
                  String sex,
                  Date apply_date,
                  String status
                ) throws CreateException, java.sql.SQLException {

    email_addr = mail;
    Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    try {
      conn = DBConfiguration.getConnection();

      stmt = conn.prepareStatement(Configuration.SQL_03);

      stmt.setString(1, nlast);
      stmt.setString(2, nfirst);

      int paramInd = 3;
      if (nmiddle != null) {
          stmt.setString(paramInd, nmiddle);
      } else
          stmt.setNull(paramInd, Types.VARCHAR);
      paramInd++;

      if (nsuffix != null) {
          stmt.setString(paramInd, nsuffix);
      } else
          stmt.setNull(paramInd, Types.VARCHAR);
      paramInd++;

      stmt.setString(paramInd, mail);
      paramInd++;

      stmt.setString(paramInd, tel_evening);
      paramInd++;

      if (tel_day != null) {
          stmt.setString(paramInd, tel_day);
      } else
          stmt.setNull(paramInd, Types.VARCHAR);
      paramInd++;

      if (tel_other != null) {
          stmt.setString(paramInd, tel_other);
      } else
          stmt.setNull(paramInd, Types.VARCHAR);
      paramInd++;

      stmt.setString(paramInd, addr_str1);
      paramInd++;

      if (addr_str2 != null) {
          stmt.setString(paramInd, addr_str2);
      } else
          stmt.setNull(paramInd, Types.VARCHAR);
      paramInd++;

      stmt.setString(paramInd, addr_city);
      paramInd++;

      stmt.setString(paramInd, addr_state);
      paramInd++;

      stmt.setString(paramInd, addr_zip);
      paramInd++;

      stmt.setString(paramInd, experience_year);
      paramInd++;

      stmt.setString(paramInd, recent_year);
      paramInd++;

      if (birth != null) {
          String s = dateFormat.format( birth );
          stmt.setString(paramInd, s);
      } else
          stmt.setNull(paramInd, Types.VARCHAR);
      paramInd++;

      stmt.setString(paramInd, sex);
      paramInd++;
      
      stmt.setString(paramInd, status);

      stmt.executeUpdate();

      if ( theLog.isDebugEnabled() ) {
        theLog.info( "insertRow: row " + mail );
      }

    } catch (Exception ex) {
       theLog.error("Exception in ejbCreate: " + ex);
       
       if (ex instanceof java.sql.SQLException)
         throw new java.sql.SQLException(ex.toString());
    }
    finally {
      DBConfiguration.closeSQLResultSet( rs );
      DBConfiguration.closeSQLStatement( stmt );
      DBConfiguration.closeSQLConnection( conn );
      rs = null;
      stmt = null;
      conn = null;
    }

    return mail;
  } // ejbCreate

  // email is the primaryKey the table
  public String ejbFindByPrimaryKey(String primaryKey) 
    throws FinderException {

    // Precondition
    if ( primaryKey == null ) {
      throw new FinderException( "null primaryKey" );
    }

    Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    boolean retVal = false;
    try {
      conn = DBConfiguration.getConnection();
      stmt = conn.prepareStatement(Configuration.SQL_01);

      stmt.setString( 1, primaryKey );

      rs = stmt.executeQuery();
      retVal = rs.next();
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
      String msg = "Row for id " + primaryKey + " not found.";
      throw new ObjectNotFoundException( msg );
    }

    return primaryKey;
  } // ejbFindByPrimaryKey(String)

  public Collection ejbFindAll()
    throws FinderException {

    Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    Collection retVal = new ArrayList();
    try {
      conn = DBConfiguration.getConnection();
      stmt = conn.prepareStatement(Configuration.SQL_02);

      rs = stmt.executeQuery();

      int rowCount = 0;
      while ( rs.next() ) {
        String id = rs.getString(1);
        retVal.add(id);
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
      stmt = conn.prepareStatement(Configuration.SQL_04);

      stmt.setString( 1, email_addr);

      rs = stmt.executeQuery();

      if ( theLog.isDebugEnabled() ) {
        theLog.debug( "deleteRow: row " + email_addr );
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

  } // ejbRemove()

  public void setEntityContext(EntityContext context) {

    this.context = context;
    if ( theLog.isDebugEnabled() ) {
      theLog.debug( "setEntityContext: context set" );
    }

  } // setEntityContext(EntityContext)

  public void unsetEntityContext() {

    this.context = null;
    if ( theLog.isDebugEnabled() ) {
      theLog.debug( "unsetEntityContext: context nulled" );
    }

  } // unsetEntityContext()

  public void ejbActivate() {
    email_addr = (String)context.getPrimaryKey();
    if ( theLog.isDebugEnabled() ) {
      theLog.debug( "ejbActivate: id == " + email_addr );
    }
  }

  public void ejbPassivate() {
    email_addr = null;
    if ( theLog.isDebugEnabled() ) {
      theLog.debug( "ejbPassivate: id nulled" );
    }
  }

  public void ejbLoad() {

  } // ejbLoad()
  
  public void ejbStore() {

  } // ejbStore()

  public void ejbPostCreate(String nlast,
                  String nfirst,
                  String nmiddle,
                  String nsuffix,
                  String mail,
                  String tel_evening,
                  String tel_day,
                  String tel_other,
                  String addr_str1,
                  String addr_str2,
                  String addr_city,
                  String addr_state,
                  String addr_zip,
                  String experience_year,
                  String recent_year,
                  Date birth,
                  String sex,
                  Date apply_date,
                  String status) {}

} // ApplicantBean

