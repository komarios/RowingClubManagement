/*
 * Copyright (c) Carnegie Lake Rowing Association 2002. All rights reserved.
 * Distributed under the GPL license. See doc/COPYING.
 * $RCSfile: RowingSessionBean.java,v $
 * $Date: 2003/02/26 03:38:45 $
 * $Revision: 1.8 $
 */

package com.clra.rowing.remote;

import com.clra.rowing.Configuration;
import com.clra.rowing.DefaultRowingSessionComparator;
import com.clra.rowing.RowingDBRead;
import com.clra.rowing.RowingException;
import com.clra.rowing.RowingSessionStateException;
import com.clra.rowing.RowingSessionLevel;
import com.clra.rowing.RowingSessionState;
import com.clra.rowing.RowingSessionType;
import com.clra.rowing.RowingSessionSnapshot;
import com.clra.util.DBConfiguration;
import com.clra.util.ISerializableComparator;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import org.apache.log4j.Category;

/**
 * @version $Revision: 1.8 $ $Date: 2003/02/26 03:38:45 $
 * @author <a href="mailto:rphall@pluto.njcc.com">Rick Hall</a>
 */
public class RowingSessionBean implements EntityBean {

  private final static String base = RowingSessionBean.class.getName();
  private final static Category theLog = Category.getInstance( base );

  private transient boolean isDirty = true;
  private Integer id = null;
  private RowingSessionState state = null;
  private Date date = null;
  private RowingSessionLevel level = null;
  private RowingSessionType type = null;

  private EntityContext context;

  /**
   * Returns the natural Comparator for rowing sessions, in which rowing
   * sessions are compared by date, state, type, level, and id, with that
   * respective weighting.
   */
  public ISerializableComparator getNaturalComparator() {
    return new DefaultRowingSessionComparator();
  }

  /** Returns a snapshot of a rowing session */
  public RowingSessionSnapshot getData() throws RemoteException {
    RowingSessionSnapshot retVal = new RowingSessionSnapshot( this.id,
      this.state, this.date, this.level, this.type );
    return retVal;
  }

  /**
   * Sets the date, level and type of a rowing session if the rowing
   * session is TENATIVE. The id and state properties are not set.
   */
  public void setData(RowingSessionSnapshot data)
    throws RowingSessionStateException {
    if ( this.state != RowingSessionState.TENATIVE ) {
      String msg = "Can not edit data when state == '"
          + this.state.getName() + "'";
      throw new RowingSessionStateException( msg );
    }
    if ( !this.date.equals( data.getDate() ) ) {
      this.isDirty = true;
      this.date = data.getDate();
    }
    if ( !this.level.equals( data.getLevel() ) ) {
      this.isDirty = true;
      this.level = data.getLevel();
    }
    if ( !this.type.equals( data.getType() ) ) {
      this.isDirty = true;
      this.type = data.getType();
    }
  }

  /**
   * Returns the primary key of a rowing session. The id is immutable
   * after a rowing session is created.
   */
  public Integer getId() {
    return this.id;
  }

  /**
   * Returns the state of a rowing session. The state of a rowing
   * session can not be set directly. It is changed as a side-effect
   * of other operations on a rowing session.
   */
  public RowingSessionState getState() {
    return this.state;
  }

  /**
   * Publishes a rowing session. Only TENATIVE sessions may be published.
   * The state of a published rowing session becomes OPEN.
   * @exception RowingSessionStateException if a non-tenative rowing session is
   * published.
   */
  public void publish() throws RowingSessionStateException {
    if ( getState() != RowingSessionState.TENATIVE ) {
      throw new RowingSessionStateException( "state is " +  getState() );
    }
    if ( !this.state.equals( RowingSessionState.OPEN ) ) {
      // This block is here in case preconditions change
      this.isDirty = true;
      this.state = RowingSessionState.OPEN;
    }
  } // publish()

  /**
   * Locks a rowing session. Only OPEN sessions may be locked.
   * The state of a locked rowing session becomes LOCKED.
   * @exception RowingSessionStateException if a non-open rowing session is
   * locked.
   */
  public void lock() throws RowingSessionStateException {
    if ( getState() != RowingSessionState.OPEN ) {
      throw new RowingSessionStateException( "state is " +  getState() );
    }
    if ( !this.state.equals( RowingSessionState.LOCKED ) ) {
      // This block is here in case preconditions change
      this.isDirty = true;
      this.state = RowingSessionState.LOCKED;
    }
  } // lock()

  /**
   * Cancels a rowing session. The state of a cancelled state becomes
   * CANCELLED.<p>
   *
   * A TENTATIVE session may not be cancelled (but it may be deleted).
   * A COMPLETE, INVOICING or CLOSED session may not be cancelled.
   *
   * @exception RowingSessionStateException if a tenative, complete, invoicing
   * or closed session is cancelled.
   */
  public void cancel() throws RowingSessionStateException {
    if ( getState() == RowingSessionState.TENATIVE
        || getState() == RowingSessionState.COMPLETE
        || getState() == RowingSessionState.INVOICING
        || getState() == RowingSessionState.CLOSED ) {
      throw new RowingSessionStateException( "state is " +  getState() );
    }
    if ( !this.state.equals( RowingSessionState.CANCELLED ) ) {
      this.isDirty = true;
      this.state = RowingSessionState.CANCELLED;
    }
  } // cancel()

  /**
   * Deletes a rowing session. Only a TENATIVE session may be deleted.
   * A deleted session is removed from the database.<p>
   *
   * This is a safe version of the standard EJBObject.remove()
   * operation. It checks that the session is tenative before removing it.
   * <strong>Application code should always delete, rather than remove, rowing
   * sessions.</strong> The remove operation, however, is required for testing
   * purposes.<p>
   *
   * @exception RowingSessionStateException if a non-tenative rowing session is
   * deleted.
   * @see com.clra.rowing.IRowingSession.remove()
   */
  public void delete()
    throws RemoteException, RemoveException, RowingSessionStateException {
    if ( getState() != RowingSessionState.TENATIVE ) {
      throw new RowingSessionStateException( "state is " +  getState() );
    }
    context.getEJBObject().remove();
  } // delete();

  /** Returns the date (and time) of a rowing session */
  public Date getDate() {
    return this.date;
  }

  /**
   * Edits the date (and time) of a rowing session. Editing is allowed only
   * for TENATIVE sessions.
   * @exception RowingSessionStateException if the edited session is not
   * in the TENATIVE state.
   * @see RowingSessionState
   */
  public void setDate( Date date ) throws RowingSessionStateException {
    // Precondition
    if ( date == null ) {
      throw new IllegalArgumentException( "null date" );
    }
    if ( this.state != RowingSessionState.TENATIVE ) {
      String msg = "Can not edit date when state == '"
          + this.state.getName() + "'";
      throw new RowingSessionStateException( msg );
    }
    if ( !this.date.equals( date ) ) {
      this.isDirty = true;
      this.date = date;
    }
  }

  /** Returns the level of a rowing session */
  public RowingSessionLevel getLevel() throws RemoteException {
    return this.level;
  }

  /**
   * Edits the level of a rowing session. Editing is allowed only for
   * TENATIVE sessions.
   */
  public void setLevel( RowingSessionLevel level )
    throws RowingSessionStateException {
    // Precondition
    if ( level == null ) {
      throw new IllegalArgumentException( "null level" );
    }
    if ( this.state != RowingSessionState.TENATIVE ) {
      String msg = "Can not edit level when state == '"
          + this.state.getName() + "'";
      throw new RowingSessionStateException( msg );
    }
    if ( !this.level.equals( level ) ) {
      this.isDirty = true;
      this.level = level;
    }
  }

  /** Returns the type of a rowing session */
  public RowingSessionType getType() throws RemoteException {
    return this.type;
  }

  /**
   * Edits the type of a rowing session. Editing is allowed only for
   * TENATIVE sessions.
   */
  public void setType( RowingSessionType type )
    throws RowingSessionStateException {
    // Precondition
    if ( type == null ) {
      throw new IllegalArgumentException( "null type" );
    }
    if ( this.state != RowingSessionState.TENATIVE ) {
      String msg = "Can not edit type when state == '"
          + this.state.getName() + "'";
      throw new RowingSessionStateException( msg );
    }
    if ( !this.type.equals( type ) ) {
      this.isDirty = true;
      this.type = type;
    }
  }

  public Integer ejbCreate( Date date, RowingSessionLevel level,
    RowingSessionType type ) throws CreateException {

    // Preconditions
    if ( date == null ) {
      throw new CreateException( "null date" );
    }
    if ( level == null ) {
      throw new CreateException( "null level" );
    }
    if ( type == null ) {
      throw new CreateException( "null type" );
    }

    try {

      this.id = nextId();
      this.state = RowingSessionState.TENATIVE;
      this.date = date;
      this.level = level;
      this.type = type;

      insertRow( this.id, this.state, this.date, this.level, this.type );

      // FIXME shouldn't isDirty be set false here?
      //this.isDirty = false;
      // ENDFIXME

      if ( theLog.isDebugEnabled() ) {
        String msg = "ejbCreate: " + id + ", '"
          + RowingDBRead.dateFormat.format( date ) + "', " + level.getName()
          + ", " + type.getName() + ", " + state.getName();
        theLog.debug( msg );
      }

    }
    catch (Exception ex) {
      throw new EJBException( "ejbCreate: " + ex.getMessage() );
    }

    return this.id;
  } // ejbCreate(Date,RowingSessionLevel,RowingSessionType)

  public Integer ejbFindByPrimaryKey(Integer primaryKey) 
    throws FinderException {

    // Precondition
    if ( primaryKey == null ) {
      throw new FinderException( "null primaryKey" );
    }

    boolean hasRow = false;
    try {
      hasRow = selectByPrimaryKey(primaryKey);
      if ( theLog.isDebugEnabled() ) {
        String msg = "ejbFindByPrimaryKey: " + id + " " + hasRow;
        theLog.debug( msg );
      }

    }
    catch (Exception ex) {
      throw new EJBException("ejbFindByPrimaryKey: " + ex.getMessage());
    }

    if (!hasRow) {
      String msg = "Row for id " + primaryKey + " not found.";
      throw new ObjectNotFoundException( msg );
    }

    return primaryKey;
  } // ejbFindByPrimaryKey(Integer)

  /**
   * Returns a collection of rowing sessions that fall within the
   * inclusive date range.
   */
  public Collection ejbFindInDateRange( Date start, Date finish )
    throws FinderException {

    // Preconditions
    if ( start == null || finish == null ) {
      throw new FinderException( "null date" );
    }
    if ( start.compareTo(finish) > 0 ) {
      throw new FinderException( "start > finish" );
    }

    Collection result = null;
    try {
      result = selectInDateRange( start, finish );
      if ( theLog.isDebugEnabled() ) {
        String msg = "ejbFindInDateRange: size == " + result.size();
        theLog.debug( msg );
      }
    }
    catch (Exception ex) {
      throw new EJBException("ejbFindInDateRange: " + ex.getMessage());
    }

    return result;
  } // ejbFindInDateRange(Date,Date)

  /**
   * Returns a collection of rowing sessions that fall within the
   * inclusive date range.
   */
  public Collection ejbFindAll()
    throws FinderException {

    Collection result = null;
    try {
      result = selectAll();
      if ( theLog.isDebugEnabled() ) {
        String msg = "ejbFindAll: size == " + result.size();
        theLog.debug( msg );
      }
    }
    catch (Exception ex) {
      throw new EJBException("ejbFindAll: " + ex.getMessage());
    }

    return result;
  } // ejbFindAll()

  /** @see delete() */
  public void ejbRemove() {

    try {
      Integer tmpId = this.getId();
      deleteRow(id);
      if ( !RowingSessionState.TENATIVE.equals(this.getState()) ) {
        theLog.warn( "ejbRemove: row deleted, id == " + tmpId );
      }
      else if ( theLog.isDebugEnabled() ) {
        theLog.debug( "ejbRemove: row deleted, id == " + tmpId );
      }
    }
    catch (Exception ex) {
      throw new EJBException("ejbRemove: " + ex.getMessage());
    }

  } // ejbRemove()

  public void setEntityContext(EntityContext context) {

    this.context = context;
    if ( theLog.isDebugEnabled() ) {
      theLog.debug( "setEntityContext: context set" );
      theLog.debug( "setEntityContext: id == " + this.id );
    }

    return;
  } // setEntityContext(EntityContext)

  public void unsetEntityContext() {

    this.context = null;
    if ( theLog.isDebugEnabled() ) {
      theLog.debug( "unsetEntityContext: id nulled" );
      theLog.debug( "unsetEntityContext: context nulled" );
    }

    return;
  } // unsetEntityContext()

  public void ejbActivate() {
    this.id = (Integer)context.getPrimaryKey();
    if ( theLog.isDebugEnabled() ) {
      theLog.debug( "ejbActivate: id == " + this.id );
    }
  }

  public void ejbPassivate() {
    this.id = null;
    if ( theLog.isDebugEnabled() ) {
      theLog.debug( "ejbPassivate: id == " + this.id );
    }
  }

  public void ejbLoad() {

    try {
      loadRow();
      if ( theLog.isDebugEnabled() ) {
        theLog.debug( "ejbLoad: row loaded" );
      }
      isDirty = false;
    }
    catch (Exception ex) {
      throw new EJBException("ejbLoad: " + ex.getMessage());
    }

  } // ejbLoad()
  
  public void ejbStore() {

    try {
      if ( isDirty ) {
        storeRow();
        if ( theLog.isDebugEnabled() ) {
          theLog.debug( "ejbStore: row stored" );
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
      throw new EJBException("ejbStore: " + ex.getMessage());
    }

  } // ejbStore()

  public void ejbPostCreate( Date date, RowingSessionLevel level,
    RowingSessionType type ) {}

  private boolean selectByPrimaryKey(Integer primaryKey) 
    throws SQLException {

    Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    boolean retVal = false;
    try {
      conn = DBConfiguration.getConnection();
      stmt = conn.prepareStatement(
          Configuration.SQL_SESSION_02,
          ResultSet.TYPE_FORWARD_ONLY,
          ResultSet.CONCUR_READ_ONLY);

      stmt.setInt( 1, primaryKey.intValue() );

      rs = stmt.executeQuery();
      retVal = rs.next();
      if ( theLog.isDebugEnabled() ) {
        theLog.debug( "selectByPrimaryKey: retVal == " + retVal );
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
  } // selectByPrimaryKey(String)

  private Collection selectAll() 
    throws SQLException {

    Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    Collection retVal = new ArrayList();
    try {
      conn = DBConfiguration.getConnection();
      stmt = conn.prepareStatement(
          Configuration.SQL_SESSION_01,
          ResultSet.TYPE_FORWARD_ONLY,
          ResultSet.CONCUR_READ_ONLY);

      rs = stmt.executeQuery();

      int rowCount = 0;
      while ( rs.next() ) {
        Integer id = new Integer( rs.getInt(1) );
        retVal.add(id);
        ++rowCount;
      }

      if ( theLog.isDebugEnabled() ) {
        String msg = "selectAll: rowCount == " + rowCount;
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
  } // selectAll()

  private Collection selectInDateRange(Date start, Date finish) 
      throws SQLException {

    Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    Collection retVal = new ArrayList();
    try {
      conn = DBConfiguration.getConnection();
      stmt = conn.prepareStatement(
          Configuration.SQL_SESSION_03,
          ResultSet.TYPE_FORWARD_ONLY,
          ResultSet.CONCUR_READ_ONLY);

      String strStart  = RowingDBRead.dateFormat.format( start );
      String strFinish = RowingDBRead.dateFormat.format( finish );

      stmt.setString( 1, strStart  );
      stmt.setString( 2, strFinish );

      rs = stmt.executeQuery();

      int rowCount = 0;
      while ( rs.next() ) {
        Integer id = new Integer( rs.getInt(1) );
        retVal.add(id);
        ++rowCount;
      }

      if ( theLog.isDebugEnabled() ) {
        String msg = "selectInDateRange:  rowCount == " + rowCount;
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
  } // selectInDateRange(Date,Date)

  private void insertRow( Integer id, RowingSessionState state, Date date,
    RowingSessionLevel level, RowingSessionType type ) throws SQLException {

    Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    try {
      conn = DBConfiguration.getConnection();
      stmt = conn.prepareStatement(
          Configuration.SQL_SESSION_04,
          ResultSet.TYPE_FORWARD_ONLY,
          ResultSet.CONCUR_READ_ONLY);

      String strDate = RowingDBRead.dateFormat.format( date );

      stmt.setInt(     1, id.intValue()   );
      stmt.setString(  2, strDate         );
      stmt.setString(  3, level.getName() );
      stmt.setString(  4, type.getName()  );
      stmt.setString(  5, state.getName() );

      //rs = stmt.executeQuery();
      stmt.executeUpdate();

      if ( theLog.isDebugEnabled() ) {
        theLog.debug( "insertRow: row " + id );
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

    return;
  } // insertRow(..)

  private void deleteRow(Integer id) throws SQLException {

    Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    try {
      conn = DBConfiguration.getConnection();
      stmt = conn.prepareStatement(
          Configuration.SQL_SESSION_05,
          ResultSet.TYPE_FORWARD_ONLY,
          ResultSet.CONCUR_READ_ONLY);

      stmt.setInt( 1, id.intValue()   );

      rs = stmt.executeQuery();

      if ( theLog.isDebugEnabled() ) {
        theLog.debug( "deleteRow: row " + id );
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

    return;
  } // deleteRow(Integer)

  private void loadRow()
    throws SQLException, RowingException, NoSuchEntityException {

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
     * RowingDBRead code to be reused directly elsewhere. Where
     * speed is critical, and where it is unlikely that a RowingSessionBean
     * is already in memory, it is best to use the RowingDBRead code to
     * directly load a (read-only) snapshot from the database.
     */
    RowingSessionSnapshot snapshot = RowingDBRead.loadRowingSession( this.id );
    this.id    = snapshot.getId();
    this.state = snapshot.getState();
    this.date  = snapshot.getDate();
    this.level = snapshot.getLevel();
    this.type  = snapshot.getType();

  } // loadRow()

  private void storeRow() throws SQLException {

    Connection conn = null;
    PreparedStatement stmt = null;
    int rowCount = 0;
    try {
      conn = DBConfiguration.getConnection();
      stmt = conn.prepareStatement(
          Configuration.SQL_SESSION_07,
          ResultSet.TYPE_FORWARD_ONLY,
          ResultSet.CONCUR_READ_ONLY);

      String strDate = RowingDBRead.dateFormat.format( this.date );

      stmt.setString(  1, strDate              );
      stmt.setString(  2, this.level.getName() );
      stmt.setString(  3, this.type.getName()  );
      stmt.setString(  4, this.state.getName() );
      stmt.setInt(     5, this.id.intValue()   );

      rowCount = stmt.executeUpdate();

      if ( theLog.isDebugEnabled() ) {
        theLog.debug( "storeRow: rowCount == " + rowCount );
        String msg = "storeRow: " + id.intValue() + ", '" + strDate + "', "
          + level.getName() + ", " + type.getName() + ", " + state.getName();
        theLog.debug( msg );
      }

    }
    finally {
      DBConfiguration.closeSQLStatement( stmt );
      DBConfiguration.closeSQLConnection( conn );
      stmt = null;
      conn = null;
    }

    if ( rowCount != 1 ) {
      throw new EJBException( "failed to store row ==  " + this.id );
    }

    return;
  } // storeRow()

  /** A utility that gets (and reserves) the next id for a rowing session */
  public static Integer nextId() throws SQLException, CreateException {

    Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    Integer retVal = null;
    try {
      conn = DBConfiguration.getConnection();
      stmt = conn.prepareStatement(
          Configuration.SQL_SESSION_08,
          ResultSet.TYPE_FORWARD_ONLY,
          ResultSet.CONCUR_READ_ONLY);

      if ( theLog.isDebugEnabled() ) {
        String msg =
          "nextId update: SQL == " + Configuration.SQL_SESSION_08;
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
            Configuration.SQL_SESSION_08A,
            ResultSet.TYPE_FORWARD_ONLY,
            ResultSet.CONCUR_READ_ONLY);

        if ( theLog.isDebugEnabled() ) {
          String msg =
            "nextId select: SQL == " + Configuration.SQL_SESSION_08A;
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

} // RowingSessionBean

/*
 * $Log: RowingSessionBean.java,v $
 * Revision 1.8  2003/02/26 03:38:45  rphall
 * Added copyright and GPL license
 *
 * Revision 1.7  2003/02/19 22:09:14  rphall
 * Removed gratuitous use of CLRA acronym
 *
 * Revision 1.6  2003/02/16 00:48:53  rphall
 * Rolled back 'fix' for where primary key is set
 *
 * Revision 1.5  2003/02/15 04:31:42  rphall
 * Changes connected to major revision of MemberBean
 *
 * Revision 1.4  2003/02/11 01:33:03  rphall
 * Added comment about code block that is not thread safe
 *
 */

