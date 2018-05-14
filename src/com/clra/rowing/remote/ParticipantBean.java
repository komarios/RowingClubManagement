/*
 * Copyright (c) Carnegie Lake Rowing Association 2002. All rights reserved.
 * Distributed under the GPL license. See doc/COPYING.
 * $RCSfile: ParticipantBean.java,v $
 * $Date: 2003/02/26 03:38:45 $
 * $Revision: 1.5 $
 */

package com.clra.rowing.remote;

import com.clra.member.MemberSnapshot;
import com.clra.rowing.Attendance;
import com.clra.rowing.Configuration;
import com.clra.rowing.IBoating;
import com.clra.rowing.IParticipant;
import com.clra.rowing.IRowingSession;
import com.clra.rowing.ParticipantSnapshot;
import com.clra.rowing.ParticipantStateException;
import com.clra.rowing.RowingDBRead;
import com.clra.rowing.RowingException;
import com.clra.rowing.RowingSessionStateException;
import com.clra.rowing.RowingSessionLevel;
import com.clra.rowing.RowingSessionState;
import com.clra.rowing.RowingSessionType;
import com.clra.rowing.RowingSessionSnapshot;
import com.clra.rowing.RowingUtils;
import com.clra.rowing.SeatPreference;
import com.clra.rowing.SeatSnapshot;
import com.clra.util.DBConfiguration;
import com.clra.util.ErrorUtils;
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
import org.apache.log4j.Category;

/**
 * @version $Revision: 1.5 $ $Date: 2003/02/26 03:38:45 $
 * @author <a href="mailto:rphall@pluto.njcc.com">Rick Hall</a>
 */
public class ParticipantBean implements EntityBean {

  private final static String base = ParticipantBean.class.getName();
  private final static Category theLog = Category.getInstance( base );

  /*
   * Indicates a NULL int when storing an int to the database.
   * @see com.clra.rowing.RowingDBRead#NULL_INT
   */
  public final static int NULL_INT = RowingDBRead.NULL_INT;

  private transient boolean _isDirty = true;
  private Integer _rowingId = null;
  private Integer _memberId = null;
  private Integer _participantId = null;
  private SeatPreference _seatPreference = null;
  private Integer _replacesId = null;
  private Integer _initialSeatId = null;
  private Integer _finalSeatId = null;
  private Attendance _attendance = null;

  private EntityContext context;

  /** Returns a snapshot of a participant */
  public ParticipantSnapshot getData() {
    ParticipantSnapshot retVal = new ParticipantSnapshot( this._memberId,
      this._rowingId, this._participantId, this._seatPreference,
      this._replacesId, this._initialSeatId, this._finalSeatId,
      this._attendance );
    return retVal;
  }

  /**
   * Returns the primary key of a participant. The id is immutable
   * after a participant is created.
   */
  public Integer getParticipantId() {
    return this._participantId;
  }

  /**
   * Returns the primary key of the member that the participant represents.
   * The id is immutable after a participant is created.
   */
  public Integer getMemberId() {
    return this._memberId;
  }

  /**
   * Returns the primary key of the rowing session to which the participant
   * belongs. The id is immutable after a participant is created.
   */
  public Integer getRowingId() {
    return this._rowingId;
  }

  /**
   * Returns the seat preference of a participant. The seat preference
   * is immutable after a participant is created. A signed-up participant
   * is created with a seat preference; an extra participant is created
   * without a seat preference; and a substitute participant is created
   * without a seat preference (but seating algorithms should assign the
   * substitute the same initial seat as the original participant).
   * @return a non-null SeatPreference if the participant signed up
   * in advance for the rowing session, or null if the participant
   * joined the rowing session as an 'Extra' or as a substitute for
   * another, signed-up participant.
   */
  public SeatPreference getSeatPreference() {
    return this._seatPreference;
  }

  /**
   * Sets the seat preference of a participant.
   * @param a non-null seat preference
   * @exception ParticipantStateException if a participant has already
   * been assigned an initial or final seating for a rowing session, or
   * if attendance has already been marked for the participant.
   * @exception RowingException if a participant is not a signed-up
   * participant. (An 'extra' or a 'substitute' participant can not be
   * converted into a signed-up participant by setting a seat preference.)
   */
  public void setSeatPreference( SeatPreference seatPreference )
      throws ParticipantStateException, RowingException {

    // Preconditions
    if ( seatPreference == null ) {
      throw new IllegalArgumentException( "null seat preference" );
    }
    if ( this.getSeatPreference() == null ) {
      String msg =
        "preference not allowed for an 'extra' or 'substitute' participant";
      throw new RowingException( msg );
    }
    // FIXME use getInitialSeat() for OO consistency
    // FIXME use getFinalSeat() for OO consistency
    if ( this._initialSeatId != null || this._finalSeatId != null ) {
      String msg = "preference can not be set after seat assignment";
      throw new ParticipantStateException( msg );
    }
    if ( this._attendance != null ) {
      String msg = "preference can not be set after attendance";
      throw new ParticipantStateException( msg );
    }

    this._seatPreference = seatPreference;
    this._isDirty = true;

    return;
  } // setSeatPreference(SeatPreference)

  /**
   * Returns the participant for whom this participant is substituting.
   * The substitutee is immutable after a participant is created. For a
   * signed-up or an extra participant, the substitutee is null. For a
   * substitute participant, the substitutee is non-null.
   * @return a non-null IParticipant if this participant is a substitute,
   * or null if this participant signed up or joined as an 'Extra'.
   */
  public IParticipant getSubstitutedParticipant() {

    IParticipant retVal = null;
    if ( this._replacesId != null ) {
      // retVal = RowingUtils.findParticipantByPrimaryId( this._replacesId );
      throw new RuntimeException( "not yet implemented" );
    }

    return retVal;
  } // getSubstitutedParticipant()

  /**
   * Assigns a preliminary seating to a participant before a rowing session
   * starts.
   * @exception ParticipantStateException if the participant
   * has not signed up for the rowing session.
   * @exception RowingException if the assignment conflicts with the
   * business rules for updating a boating; for example, if the
   * rowing session to which the boating belongs is not locked.
   */
  public void setInitialSeat( SeatSnapshot seat, IBoating boating )
      throws ParticipantStateException, RowingException {

    if ( this._seatPreference == null ) {
      throw new ParticipantStateException( "not signed up" );
    }

    this._isDirty = true;
    // FIXME : SchedulingManager to make initial seating assignment
    throw new RowingException( "business rules not yet implemented" );

  } // setInitialSeat(SeatSnapshot,IBoating)

  /**
   * Returns the initial seat assignment of this participant, or null
   * if an initial seating was not made.
   */
  public SeatSnapshot getInitialSeat() {
    
    // FIXME : RowingDBRead to look up initialSeatSnapshot
    throw new RuntimeException( "not yet implemented" );

  } // getInitialSeat()

  /**
   * Assigns a final seating to a participant at the start of a rowing
   * session, after attendance has been taken.
   *
   * @param seat the final seating of a participant, or <tt>null</tt> if
   * a participant is not boated for the session.
   * @exception ParticipantStateException if the state of the participant
   * conflicts with the seating assignment; for example, the participant
   * is not present for the rowing session.
   * @exception RowingException if the assignment conflicts with the
   * business rules for updating a boating; for example, if the
   * rowing session to which the boating belongs is not in the BOATING2 state.
   */
  public void setFinalSeat( SeatSnapshot seat, IBoating boating )
      throws ParticipantStateException, RowingException {

    if ( !Attendance.PRESENT.equals( this._attendance ) ) {
      throw new ParticipantStateException( "not signed up" );
    }

    this._isDirty = true;
    // FIXME : BoatingManager to make final seating assignment
    throw new RowingException( "business rules not yet implemented" );

  } // setFinalSeat(SeatSnapshot,IBoating)

  /**
   * Returns the final seat assignment of this participant, or null
   * if a final seating was not made.
   */
  public SeatSnapshot getFinalSeat() {
    
    // FIXME : RowingDBRead to look up finalSeatSnapshot
    throw new RuntimeException( "not yet implemented" );

  } // getFinalSeat()

  /**
   * Marks the attendance of a participant at a rowing session.
   * @exception ParticipantStateException if the instance data of the
   * participant conflicts with marking attendance; for example, if the
   * participant has already been assigned a final seating assignment, or
   * if an "extra" participant is marked absent.
   * @exception RowingException if marking attendance conflicts with the
   * business rules for updating a rowing session; for example, if the
   * rowing session is not in the BOATING1 state.
   */
  public void setAttendance( Attendance attend, IRowingSession session )
      throws ParticipantStateException, RowingException {

    // Preconditions
    if ( attend == null ) {
      throw new IllegalArgumentException( "null attendance" );
    }
    if ( session == null ) {
      throw new IllegalArgumentException( "null rowing session" );
    }

    // Participant-specific business rules
    if ( attend.equals( Attendance.ABSENT ) ) {
      if ( this._seatPreference == null ) {
        String msg = "extra or substitute can not be marked absent";
        throw new ParticipantStateException( msg );
      }
      if ( this._initialSeatId == null ) {

        // A null initial seating assignment means participant was
        // not expected to be present.
        //
        // FIXME move this business logic to AttendanceMgr
        //
        String msg = "participant was not expected at rowing session";
        throw new ParticipantStateException( msg );
      }
      if ( this._finalSeatId != null ) {
        String msg = "final seating assignment conflicts with absence";
        throw new ParticipantStateException( msg );
      }
      String msg = "business rules not yet implemented";
      throw new RowingException( msg );
    }
    else if ( attend.equals( Attendance.PRESENT ) ) {
      String msg = "business rules not yet implemented";
      throw new RowingException( msg );
    }
    else {
      throw new Error( "design error" );
    }

    //this._isDirty = true;
    //throw new RuntimeException( "not yet implemented" );

  } // setAttendance(Attendance,IRowingSession)

  /**
   * Returns the attendance of a participant at a rowing session, or null
   * if attendance has not been marked.
   */
  public Attendance getAttendance() {
    return this._attendance;
  }

  /**
   * @param member the member who is signing up.
   * @param session the session for which the member is signing up.
   * @param preferred the seating requested by the member for the
   * rowing session.
   * @exception CreateException if the rowing session is not OPEN.
   */
  public Integer ejbCreate( Integer memberId, Integer rowingId,
      SeatPreference preferred ) throws CreateException {

    // Precondition
    if ( memberId == null ) {
      throw new CreateException( "null member" );
    }
    if ( rowingId == null ) {
      throw new CreateException( "null rowing session" );
    }
    if ( preferred == null ) {
      throw new CreateException( "null seat preference" );
    }

    if ( theLog.isDebugEnabled() ) {
      String msg = "Creating participant: mid == " + memberId
        + ", rid == " + rowingId + ", sp == " + preferred.getName();
      theLog.debug( msg );
    }

    Integer retVal = createWithSessionConstraint( memberId, rowingId,
      preferred, RowingSessionState.OPEN );

    return retVal;
  } // ejbCreate(Integer,Integer,SeatPreference)

  public void ejbPostCreate(Integer mId,Integer rId, SeatPreference sp ) {
  }

  /**
   * Creates an "extra" participant; that is, a participant who is
   * present at a rowing session without signing up and who isn't
   * substituting for another member. The participant is constructed in
   * the PRESENT state.
   * @param member the member who is signing up.
   * @param rowing the session for which the member is signing up.
   * @exception CreateException if the rowing session is not in the
   * BOATING1 state (during which attendance if marked).
   */
  public Integer ejbCreate(MemberSnapshot member, RowingSessionSnapshot rowing)
      throws RemoteException, CreateException {

    // Preconditions
    if ( member == null ) {
      throw new CreateException( "null member" );
    }
    if ( rowing == null ) {
      throw new CreateException( "null rowing session" );
    }
    if ( !RowingSessionState.BOATING1.equals( rowing.getState() ) ) {
      String msg  = "invalid rowing state == " + rowing.getState().getName();
      throw new CreateException( msg );
    }
    // FIXME check Regular/Full vs LTR/LTR

    Integer memberId = member.getId();
    Integer rowingId = rowing.getId();

    Integer retVal = createWithSessionConstraint( memberId, rowingId,
      null, RowingSessionState.BOATING1 );

    return retVal;
  } // ejbCreate(Integer,Integer)

  public void ejbPostCreate(MemberSnapshot ms, RowingSessionSnapshot rss) {
  }

  /**
   * Creates a "substitute" participant; that is, a participant who is
   * present at a rowing session as a substitute for another, signed-up
   * participant. The substitute is constructed in the PRESENT state,
   * and the signed-up participant transitions to the ABSENT state.
   * @param member the member who is signing up.
   * @param replaces the participant who is being replaced.
   * @exception CreateException if the rowing session is not in the
   * BOATING1 state (during which attendance if marked).
   */
  public Integer ejbCreate(MemberSnapshot member, ParticipantSnapshot replaces)
      throws CreateException, RemoteException {

    // Preconditions
    if ( member == null ) {
      throw new CreateException( "null member" );
    }
    if ( replaces == null ) {
      throw new CreateException( "null participant" );
    }

    Integer memberId = member.getId();
    Integer rowingId = replaces.getRowingId();
    Integer retVal = null;

    // try {
    //   Mark replaced participant as ABSENT
    //   Create replacement
    // }
    // catch ( Something ) {
    //   Main cause is rowing state != BOATING1 ??
    //   If so, no transaction, no rollback required ??
    // }
    throw new CreateException( "not yet implemented" );

  } // ejbCreate(MemberSnapshot,ParticipantSnapshot)

  public void ejbPostCreate(MemberSnapshot ms, ParticipantSnapshot ps) {
  }

  private Integer createWithSessionConstraint( Integer memberId,
    Integer rowingId, SeatPreference sp, RowingSessionState st )
      throws CreateException {

    Integer retVal = null;
    try {

      this._memberId = memberId;
      this._rowingId = rowingId;
      this._participantId = nextId();
      this._seatPreference = sp;

      // FIXME add constraint on RowingSessionLevel

      insertRowWithSessionConstraints( memberId, rowingId,
        this._participantId, this._seatPreference, st );

      this._isDirty = false;

      retVal = this._participantId;

      if ( theLog.isInfoEnabled() ) {
        theLog.debug( "ejbCreate: retVal == " + retVal );
        String msg = "ejbCreate: "
          + this._participantId + ", " + this._rowingId  + ", "
          + this._memberId + ", '" + this._seatPreference.getName() + "', '"
          + st.getName() + "'";
        theLog.info( msg );
      }

    }
    catch (CreateException x) {
      String msg = ErrorUtils.createDbgMsg( "ejbCreate", x );
      throw x;
    }
    catch (Exception ex) {
      String msg = ErrorUtils.createDbgMsg( "ejbCreate", ex );
      theLog.error( msg, ex );
      throw new EJBException( msg );
    }

    return retVal;
  } // createWithSessionConstraint(..)

  public Integer ejbFindByPrimaryKey( Integer primaryKey )
      throws FinderException {

    // Precondition
    if ( primaryKey == null ) {
      throw new FinderException( "null primaryKey" );
    }

    boolean hasRow = false;
    try {
      hasRow = selectByPrimaryKey(primaryKey);
      if ( theLog.isDebugEnabled() ) {
        String msg = "ejbFindByPrimaryKey: " + primaryKey + " " + hasRow;
        theLog.debug( msg );
      }
    }
    catch (Exception ex) {
      String msg = ErrorUtils.createDbgMsg( "ejbFindByPrimaryKey", ex );
      theLog.error( msg, ex );
      throw new EJBException(msg);
    }

    if (!hasRow) {
      String msg = "Row for id " + primaryKey + " not found.";
      throw new ObjectNotFoundException( msg );
    }

    return primaryKey;
  } // ejbFindByPrimaryKey(Integer)

  public Collection ejbFindByMemberIdRowingId(Integer memberId,Integer rowingId)
      throws FinderException {

    // Preconditions
    if ( memberId == null ) {
      throw new FinderException( "null memberId" );
    }
    if ( rowingId == null ) {
      throw new FinderException( "null rowingId" );
    }

    Collection retVal = null;
    try {
      retVal = selectByMemberIdRowingId( memberId, rowingId );
      if ( theLog.isDebugEnabled() ) {
        String msg = "ejbFindInDateRange: size == " + retVal.size();
        theLog.debug( msg );
      }
    }
    catch (Exception ex) {
      String msg = ErrorUtils.createDbgMsg( "ejbFindByMemberIdRowingId", ex );
      theLog.error( msg, ex );
      throw new EJBException(msg);
    }

    return retVal;
  } // ejbFindByMemberIdRowingId(Integer,Integer)

  public Collection ejbFindByRowingId(Integer rowingId) throws FinderException {

    // Preconditions
    if ( rowingId == null ) {
      throw new FinderException( "null rowingId" );
    }

    Collection retVal = null;
    try {
      retVal = selectByRowingId( rowingId );
      if ( theLog.isDebugEnabled() ) {
        String msg = "ejbFindInDateRange: size == " + retVal.size();
        theLog.debug( msg );
      }
    }
    catch (Exception ex) {
      String msg = ErrorUtils.createDbgMsg( "ejbFindByRowingId", ex );
      theLog.error( msg, ex );
      throw new EJBException(msg);
    }

    return retVal;
  } // ejbFindByMemberIdRowingId(Integer,Integer)

  /**
   * Returns a collection of rowing sessions that fall within the
   * inclusive date range.
   */
  public Collection ejbFindAll() throws FinderException {

    Collection retVal = null;
    try {
      retVal = selectAll();
      if ( theLog.isDebugEnabled() ) {
        String msg = "ejbFindAll: size == " + retVal.size();
        theLog.debug( msg );
      }
    }
    catch (Exception ex) {
      String msg = ErrorUtils.createDbgMsg( "findAll", ex );
      theLog.error( msg, ex );
      throw new EJBException(msg);
    }

    return retVal;
  } // ejbFindAll()

  public void ejbRemove() {

    try {
      Integer tmpId = this.getParticipantId();
      deleteRow( tmpId );
      if ( theLog.isInfoEnabled() ) {
        theLog.info( "ejbRemove: row deleted, participantId == " + tmpId );
      }
    }
    catch (Exception ex) {
      String msg = ErrorUtils.createDbgMsg( "ejbRemove", ex );
      theLog.error( msg, ex );
      throw new EJBException(msg);
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
    this._participantId = (Integer)context.getPrimaryKey();
    if ( theLog.isDebugEnabled() ) {
      theLog.debug( "ejbActivate: participantId == " + this._participantId );
    }
  }

  public void ejbPassivate() {
    this._participantId = null;
    if ( theLog.isDebugEnabled() ) {
      theLog.debug( "ejbPassivate: participantId nulled" );
    }
  }

  public void ejbLoad() {

    try {
      loadRow();
      if ( theLog.isDebugEnabled() ) {
        theLog.debug( "ejbLoad: row loaded for #"  + this._participantId );
      }
      _isDirty = false;
    }
    catch (Exception ex) {
      String msg = ErrorUtils.createDbgMsg( "ejbLoad", ex );
      theLog.error( msg, ex );
      throw new EJBException(msg);
    }

  } // ejbLoad()
  
  public void ejbStore() {

    try {
      if ( _isDirty ) {
        storeRow();
        if ( theLog.isDebugEnabled() ) {
          theLog.debug( "ejbStore: row stored for #"  + this._participantId );
        }
        _isDirty = false;
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

  private boolean selectByPrimaryKey(Integer primaryKey) 
    throws SQLException {

    Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    boolean retVal = false;
    try {
      conn = DBConfiguration.getConnection();
      stmt = conn.prepareStatement(
          Configuration.SQL_PARTICIPANT_01,
          ResultSet.TYPE_FORWARD_ONLY,
          ResultSet.CONCUR_READ_ONLY);

      stmt.setInt( 1, primaryKey.intValue() );

      rs = stmt.executeQuery();
      // FIXME cache instance data and set isDirty=false
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
  } // selectByPrimaryKey(Integer)

  private Collection selectByMemberIdRowingId( Integer memberId,
    Integer rowingId) throws SQLException {

    Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    Collection retVal = new ArrayList();
    try {
      conn = DBConfiguration.getConnection();
      stmt = conn.prepareStatement(
          Configuration.SQL_PARTICIPANT_03,
          ResultSet.TYPE_FORWARD_ONLY,
          ResultSet.CONCUR_READ_ONLY);

      stmt.setInt( 1, memberId.intValue() );
      stmt.setInt( 2, rowingId.intValue() );

      rs = stmt.executeQuery();

      int rowCount = 0;
      while ( rs.next() ) {
        Integer id = new Integer( rs.getInt("participant_id") );
        retVal.add(id);
        ++rowCount;
      }

      if ( theLog.isDebugEnabled() ) {
        String msg = "selectByMemberIdRowingId: rowCount == " + rowCount;
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
  } // selectByMemberIdRowingId(Integer,Integer)

  private Collection selectByRowingId( Integer rowingId ) throws SQLException {

    Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    Collection retVal = new ArrayList();
    try {
      conn = DBConfiguration.getConnection();
      stmt = conn.prepareStatement(
          Configuration.SQL_PARTICIPANT_02,
          ResultSet.TYPE_FORWARD_ONLY,
          ResultSet.CONCUR_READ_ONLY);

      stmt.setInt( 1, rowingId.intValue() );

      rs = stmt.executeQuery();

      int rowCount = 0;
      while ( rs.next() ) {
        Integer id = new Integer( rs.getInt("participant_id") );
        retVal.add(id);
        ++rowCount;
      }

      if ( theLog.isDebugEnabled() ) {
        String msg = "selectByRowingId: rowCount == " + rowCount;
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
  } // selectByRowingId(Integer)

  private Collection selectAll() throws SQLException {

    Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    Collection retVal = new ArrayList();
    try {
      conn = DBConfiguration.getConnection();
      stmt = conn.prepareStatement(
          Configuration.SQL_PARTICIPANT_04,
          ResultSet.TYPE_FORWARD_ONLY,
          ResultSet.CONCUR_READ_ONLY);

      rs = stmt.executeQuery();

      int rowCount = 0;
      while ( rs.next() ) {
        Integer id = new Integer( rs.getInt("participant_id") );
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

  private void insertRowWithSessionConstraints( Integer memberId,
    Integer rowingId, Integer participantId, SeatPreference seatPreference,
      RowingSessionState rsState ) throws CreateException, SQLException {

    // Precondition
    try {
      RowingSessionSnapshot rss = RowingDBRead.loadRowingSession( rowingId );
      if ( !rsState.equals(rss.getState()) ) {
        String msg = "invalid session state == " + rss.getState().getName()
            + " != " + rsState.getName();
        throw new CreateException( msg );
      }
    }
    catch( RowingException x ) {
      String msg = ErrorUtils.createDbgMsg( "insertRowWith...", x );
      theLog.error( msg );
      throw new CreateException( msg );
    }

    Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    int rowCount = 0;
    try {

      conn = DBConfiguration.getConnection();
      stmt = conn.prepareStatement(
          Configuration.SQL_PARTICIPANT_05,
          ResultSet.TYPE_FORWARD_ONLY,
          ResultSet.CONCUR_READ_ONLY);

      stmt.setInt( 1, memberId.intValue()      );
      stmt.setInt( 2, participantId.intValue() );

      if ( seatPreference != null ) {
        stmt.setString(  3, seatPreference.getName() );
      }
      else {
        stmt.setNull( 3, Types.CHAR );
      }

      stmt.setInt(    4, rowingId.intValue() );
      // stmt.setString( 5, rsState.getName()   );

      if ( theLog.isDebugEnabled() ) {
        theLog.debug( "SQL == " + Configuration.SQL_PARTICIPANT_05 );
        theLog.debug( "arg 1 == " + memberId.intValue() );
        theLog.debug( "arg 2 == " + participantId.intValue() );
        theLog.debug( "arg 3 == " + seatPreference );
        theLog.debug( "arg 4 == " + rowingId.intValue() );
        // theLog.debug( "arg 5 == " + rsState.getName() );
      }

      rowCount = stmt.executeUpdate();

      if ( theLog.isDebugEnabled() ) {
        theLog.debug( "insertRow: participantId " + participantId );
        theLog.debug( "insertRow: rowCount " + rowCount );
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

    /*
     * Under rare conditions, when a participant is inserted at the same
     * same time a rowing session is being promoted, the INSERT statement
     * could fail quietly.
    */
try {
  theLog.debug( "trying to load new participant..." );
  ParticipantSnapshot _ps = RowingDBRead.loadParticipant( participantId );
  theLog.debug( "...loaded " + participantId );
} catch (Exception _x) {
  String _msg = ErrorUtils.createDbgMsg( "...load failed", _x );
  theLog.debug( _msg );
}
    if ( rowCount != 1 ) {
      String msg = "participant #" + participantId + " was not added";
      throw new CreateException( msg );
    }

    return;
  } // insertRowWithSessionConstraints(..)

  private void deleteRow(Integer id) throws RemoveException, SQLException {

    Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    int rowCount = 0;
    try {
      conn = DBConfiguration.getConnection();
      stmt = conn.prepareStatement(
          Configuration.SQL_PARTICIPANT_06,
          ResultSet.TYPE_FORWARD_ONLY,
          ResultSet.CONCUR_READ_ONLY);

      stmt.setInt( 1, id.intValue() );

      rowCount = stmt.executeUpdate();

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

    if ( rowCount != 1 ) {
      String msg = "row #" + id + " was not deleted";
      throw new RemoveException( msg );
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
     * speed is critical, and where it is unlikely that a ParticipantBean
     * is already in memory, it is best to use the RowingDBRead code to
     * directly load a (read-only) snapshot from the database.
     */
    ParticipantSnapshot snapshot =
        RowingDBRead.loadParticipant( this._participantId );

    this._rowingId = snapshot.getRowingId();
    this._memberId = snapshot.getMemberId();
    this._participantId = snapshot.getParticipantId();
    this._seatPreference = snapshot.getSeatPreference();
    this._replacesId = snapshot.getReplacesId();
    this._initialSeatId = snapshot.getInitialSeatId();
    this._finalSeatId = snapshot.getFinalSeatId();
    this._attendance = snapshot.getAttendance();

    return;
  } // loadRow()

  private void storeRow() throws SQLException {

    Connection conn = null;
    PreparedStatement stmt = null;
    int rowCount = 0;
    try {
      conn = DBConfiguration.getConnection();
      stmt = conn.prepareStatement(
          Configuration.SQL_PARTICIPANT_07,
          ResultSet.TYPE_FORWARD_ONLY,
          ResultSet.CONCUR_READ_ONLY);

      if ( this._seatPreference != null ) {
        stmt.setString( 1, this._seatPreference.getName() );
      }
      else {
        stmt.setNull( 1, Types.CHAR );
      }

      if ( this._initialSeatId != null ) {
        stmt.setInt( 2, this._initialSeatId.intValue() );
      }
      else {
        stmt.setNull( 2, Types.INTEGER );
      }

      if ( this._finalSeatId != null ) {
        stmt.setInt( 3, this._finalSeatId.intValue() );
      }
      else {
        stmt.setNull( 3, Types.INTEGER );
      }

      if ( this._attendance != null ) {
        stmt.setString( 4, this._attendance.getName() );
      }
      else {
        stmt.setNull( 4, Types.CHAR );
      }

      stmt.setInt( 5, this._participantId.intValue() );

      rowCount = stmt.executeUpdate();

      if ( theLog.isDebugEnabled() ) {
        theLog.debug( "storeRow: rowCount == " + rowCount );
        String msg = "storeRow: " + this._participantId
          + ", " + this._initialSeatId
          + ", " + this._finalSeatId + ", "
          + ( this._attendance == null ? null : this._attendance.getName() );
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
      String msg = "failed to store participant ==  " + this._participantId;
      theLog.error( msg );
      throw new EJBException( msg );
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
          Configuration.SQL_PARTICIPANT_08,
          ResultSet.TYPE_FORWARD_ONLY,
          ResultSet.CONCUR_READ_ONLY);

      if ( theLog.isDebugEnabled() ) {
        String msg =
          "nextId update: SQL == " + Configuration.SQL_PARTICIPANT_08;
        theLog.debug( msg );
      }

      if ( !DBConfiguration.isOracle() ) {

        boolean moreResults = stmt.execute();

        stmt = conn.prepareStatement(
            Configuration.SQL_PARTICIPANT_08A,
            ResultSet.TYPE_FORWARD_ONLY,
            ResultSet.CONCUR_READ_ONLY);

        if ( theLog.isDebugEnabled() ) {
          String msg =
            "nextId select: SQL == " + Configuration.SQL_PARTICIPANT_08A;
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

} // ParticipantBean

/*
 * $Log: ParticipantBean.java,v $
 * Revision 1.5  2003/02/26 03:38:45  rphall
 * Added copyright and GPL license
 *
 * Revision 1.4  2003/02/19 22:09:13  rphall
 * Removed gratuitous use of CLRA acronym
 *
 * Revision 1.3  2003/02/16 00:47:27  rphall
 * Removed dead code
 *
 */

