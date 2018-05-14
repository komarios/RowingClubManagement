/*
 * Copyright (c) Carnegie Lake Rowing Association 2002. All rights reserved.
 * Distributed under the GPL license. See doc/COPYING.
 * $RCSfile: RowingDBRead.java,v $
 * $Date: 2003/02/26 03:38:45 $
 * $Revision: 1.3 $
 */

package com.clra.rowing;

import com.clra.member.MemberName;
import com.clra.rowing.Configuration;
import com.clra.rowing.DefaultRowingSessionComparator;
import com.clra.rowing.RowingException;
import com.clra.rowing.RowingSessionStateException;
import com.clra.rowing.RowingSessionLevel;
import com.clra.rowing.RowingSessionState;
import com.clra.rowing.RowingSessionType;
import com.clra.rowing.RowingSessionSnapshot;
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
 * This utility class defines common routines for reading snapshots
 * from the database. In cases where read-only lists are presented
 * to a user, these routines are faster than their ejbFind counterparts.
 * However, data should never be written directly back to the database,
 * otherwise in-memory caches maintained by EJB's will be out of synch
 * and data will be corrupted.  The class RowingUtils defines utilities
 * with similar signatures that are implemented by calls to EJB's. These
 * operations are appropriate if objects should be cached in memory
 * because they will be modified shortly.</p><p>
 *
 * @version $Revision: 1.3 $ $Date: 2003/02/26 03:38:45 $
 * @author <a href="mailto:rphall@pluto.njcc.com">Rick Hall</a>
 */
public final class RowingDBRead {

  private final static String base = RowingDBRead.class.getName();
  private final static Category theLog = Category.getInstance( base );

  /** All utilities are static */
  private RowingDBRead() {}

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

  public static
  RowingSessionSnapshot loadRowingSession( Integer rowingId )
    throws SQLException, RowingException, NoSuchEntityException {

    // Precondition
    if ( rowingId == null ) {
      throw new IllegalArgumentException( "null rowingId" );
    }

    RowingSessionSnapshot retVal = null;

    Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    int rowCount = 0;
    try {
      conn = DBConfiguration.getConnection();
      stmt = conn.prepareStatement(
          Configuration.SQL_SESSION_06,
          ResultSet.TYPE_FORWARD_ONLY,
          ResultSet.CONCUR_READ_ONLY);
      stmt.setInt( 1, rowingId.intValue()   );
      rs = stmt.executeQuery();
      while ( rs.next() ) {
        ++rowCount;
        retVal = mapResultSetToRowingSessionSnapshot(rs);
      } // while

    }
    finally {
      DBConfiguration.closeSQLResultSet( rs );
      DBConfiguration.closeSQLStatement( stmt );
      DBConfiguration.closeSQLConnection( conn );
      rs = null;
      stmt = null;
      conn = null;
    }

    // Postconditions
    if ( rowCount == 0 ) {
      throw new NoSuchEntityException( "no id matching " + rowingId );
    }
    else if ( rowCount > 1 ) {
      throw new EJBException( "2 or more id's matching " + rowingId );
    }

    return retVal;
  } // loadRowingSession()

  /** Returns a collection of snapshots for all rowing sessions */
  public static
  Collection findAllRowingSessionSnapshots() throws RowingException {

    Collection retVal = new ArrayList();

    Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    try {
      conn = DBConfiguration.getConnection();
      stmt = conn.prepareStatement(
          Configuration.SQL_SESSION_01A,
          ResultSet.TYPE_FORWARD_ONLY,
          ResultSet.CONCUR_READ_ONLY);
      rs = stmt.executeQuery();
      while ( rs.next() ) {
        RowingSessionSnapshot rss = mapResultSetToRowingSessionSnapshot(rs);
        retVal.add( rss );
      } // while

    }
    catch( Exception x ) {
      String msg = ErrorUtils.createDbgMsg("findAllRowingSessionSnapshots",x);
      theLog.error( msg, x );
      throw new RowingException( msg );
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
  } // findAllRowingSessions()
  
  /**
   * Returns a collection of snapshots for rowing sessions within an
   * inclusive date range.
   */
  public static
  Collection findAllRowingSessionSnapshotsInRange( Date start, Date finish )
    throws RowingException {

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
          Configuration.SQL_SESSION_03,
          ResultSet.TYPE_FORWARD_ONLY,
          ResultSet.CONCUR_READ_ONLY);

      String strStart  = RowingDBRead.dateFormat.format( start );
      String strFinish = RowingDBRead.dateFormat.format( finish );

      if ( theLog.isDebugEnabled() ) {
        final String prefix = "RowingDBRead.findAllRowing..InRange: ";
        theLog.debug( prefix + "strStart  == " + strStart );
        theLog.debug( prefix + "strFinish == " + strFinish );
      }

      stmt.setString( 1, strStart  );
      stmt.setString( 2, strFinish );

      rs = stmt.executeQuery();
      while ( rs.next() ) {
        RowingSessionSnapshot rss = mapResultSetToRowingSessionSnapshot(rs);
        retVal.add( rss );
      } // while

    }
    catch( Exception x ) {
      String msg =
          ErrorUtils.createDbgMsg("findAllRowingSessionSnapshotsInRange",x);
      theLog.error( msg, x );
      throw new RowingException( msg );
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
  } // findAllRowingSessionSnapshotsInRange( Date start, Date finish )
  
  /** Returns a collection of snapshots for all enrollments */
  public static
  Collection findAllEnrollmentSnapshots( int member_id )
    throws RowingException {

    Date start  = new Date( Long.MIN_VALUE );
    Date finish = new Date( Long.MAX_VALUE );

    return findAllEnrollmentSnapshotsInRange(member_id, start, finish);
  } // findAllEnrollmentSnapshots()
  
  /**
   * Returns a collection of snapshots for enrollments within an
   * inclusive date range.
   */
  public static Collection
  findAllEnrollmentSnapshotsInRange(
    final int member_id, final Date start, final Date finish )
    throws RowingException {

    // Preconditions
    if ( start == null || finish == null ) {
      throw new IllegalArgumentException( "null date" );
    }
    if ( start.compareTo(finish) > 0 ) {
      throw new IllegalArgumentException( "start > finish" );
    }

    final Integer memberId = new Integer( member_id );

    Collection retVal = new ArrayList();

    Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    try {

      /*
       * Implementation note: the following steps could be accomplished
       * with a single SQL statement, if UNION and sub-SELECT statements
       * are supported by the database. Unfortunately, although Oracle
       * supports UNION and sub-SELECT, mySQL does not.
       *
       * As a workaround, valid for both Oracle and mySQL, a set is used
       * to combine rowing sessions where a member is and is not a participant.
       * First all rowing sessions are selected with null participation
       * data. Then rowing session are selected where the member is a
       * participant. The second selection overwrites null enrollments
       * where appropriate.
       *
       * There's a potentially nasty sorting issue with this approach, unless
       * the database sorts the original (null-participation) data before
       * returning it. In the current implementation, both the Oracle and
       * mySQL SQL statements contain an ORDER BY clause, so the issue is
       * avoided.
       *
       * Here's the issue, for future reference. The database
       * distinguishes rowing sessions by id, not date. One can have two
       * distinct rowing session scheduled for exactly the same date and time.
       * This should only occur in case of an error. Java provides convenient
       * collection classes for sorting, but only if "equals" and "compareTo"
       * are consistent; see the documentation for the java.util.Comparator
       * interface. One can not define a *consistent* date comparator for
       * rowing sessions without potentially hiding rowing sessions that
       * (erroneously) have identical dates and times. This is an obscure
       * issue, but it is particularly ugly since it can hide bad data sets.
       */

      final Map map = new HashMap();
      final List sorting = new ArrayList();
      final String strStart  = dateFormat.format( start );
      final String strFinish = dateFormat.format( finish );

      conn = DBConfiguration.getConnection();

      // Step 1: select sessions regardless of whether a member is enrolled
      stmt = conn.prepareStatement(
          Configuration.SQL_ENROLLMENT_01,
          ResultSet.TYPE_FORWARD_ONLY,
          ResultSet.CONCUR_READ_ONLY);

      stmt.setString( 1, strStart  );
      stmt.setString( 2, strFinish );

      int rowCount = 0;
      rs = stmt.executeQuery();
      while ( rs.next() ) {
        RowingSessionSnapshot rss = mapResultSetToRowingSessionSnapshot(rs);
        EnrollmentSnapshot es = new EnrollmentSnapshot( memberId, rss, null );
        map.put( es.getRowingId(), es );
        sorting.add( es.getRowingId() );
        ++rowCount;
      } // while
      if ( theLog.isDebugEnabled() ) {
        String msg =
          "RowingUtils.findAllEnroll..InRange: result count#1 == " + rowCount;
        theLog.debug( msg );
      }

      // Step 2: non-null participation data
      stmt = conn.prepareStatement(
          Configuration.SQL_ENROLLMENT_02,
          ResultSet.TYPE_FORWARD_ONLY,
          ResultSet.CONCUR_READ_ONLY);

      stmt.setInt(    1, memberId.intValue() );
      stmt.setString( 2, strStart  );
      stmt.setString( 3, strFinish );

      rowCount = 0;
      rs = stmt.executeQuery();
      while ( rs.next() ) {
        EnrollmentSnapshot es = mapResultSetToEnrollmentSnapshot(rs);
        map.put( es.getRowingId(), es );
        ++rowCount;
      } // while
      if ( theLog.isDebugEnabled() ) {
        String msg =
          "RowingUtils.findAllEnroll..InRange: result count#2 == " + rowCount;
        theLog.debug( msg );
      }

      // Sort the enrollments into the return list
      for ( Iterator iter = sorting.iterator(); iter.hasNext(); ) {
        Object key = iter.next();
        Object val = map.get( key );
        retVal.add( val );
      }

    }
    catch( Exception x ) {
      String msg = "RowingUtils.findAllEnrollmentSnapshotsInRange: "
        + memberId + "/'" + start + "'/'" + finish + "': "
        + x.getClass().getName() + ": " + x.getMessage();
      theLog.error( msg, x );
      throw new RowingException( msg );
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
  } // findAllEnrollmentSnapshotsInRange( Date start, Date finish )

  /**
   * Returns a Participant identified by a participant id.
   * @return <strong>NEVER</null>
   * @exception NoSuchEntityException if the id does not identify
   * a participant
   */
  public static
  ParticipantSnapshot loadParticipant( Integer participantId )
    throws SQLException, RowingException, NoSuchEntityException {

    // Precondition
    if ( participantId == null ) {
      throw new IllegalArgumentException( "null participantId" );
    }

    ParticipantSnapshot retVal = null;

    Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    int rowCount = 0;
    try {
      conn = DBConfiguration.getConnection();
      stmt = conn.prepareStatement(
          Configuration.SQL_PARTICIPANT_01,
          ResultSet.TYPE_FORWARD_ONLY,
          ResultSet.CONCUR_READ_ONLY);
      stmt.setInt( 1, participantId.intValue() );
      rs = stmt.executeQuery();
      while ( rs.next() ) {
        ++rowCount;
        retVal = mapResultSetToParticipantSnapshot(rs);
      } // while

    }
    finally {
      DBConfiguration.closeSQLResultSet( rs );
      DBConfiguration.closeSQLStatement( stmt );
      DBConfiguration.closeSQLConnection( conn );
      rs = null;
      stmt = null;
      conn = null;
    }

    // Postconditions
    if ( rowCount == 0 ) {
      throw new NoSuchEntityException( "no id matching " + participantId );
    }
    else if ( rowCount > 1 ) {
      throw new EJBException( "2 or more id's matching " + participantId );
    }

    return retVal;
  } // loadParticipant()

  /**
   * Returns a Participant identified by a member and rowing id.
   * The combination member_id/rowing_id is an alternate key on
   * the Participant table. Therefore this routine returns either
   * a null Participant or a unique Participant.
   * @return possibly null
   */
  public static
  ParticipantSnapshot loadParticipant( Integer memberId, Integer rowingId  )
    throws SQLException, RowingException {

    // Precondition
    if ( memberId == null ) {
      throw new IllegalArgumentException( "null memberId" );
    }
    if ( rowingId == null ) {
      throw new IllegalArgumentException( "null rowingId" );
    }

    ParticipantSnapshot retVal = null;

    Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    int rowCount = 0;
    try {
      conn = DBConfiguration.getConnection();
      stmt = conn.prepareStatement(
          Configuration.SQL_PARTICIPANT_03,
          ResultSet.TYPE_FORWARD_ONLY,
          ResultSet.CONCUR_READ_ONLY);
      stmt.setInt( 1, memberId.intValue() );
      stmt.setInt( 2, rowingId.intValue() );
      rs = stmt.executeQuery();
      while ( rs.next() ) {
        ++rowCount;
        retVal = mapResultSetToParticipantSnapshot(rs);
      } // while

    }
    finally {
      DBConfiguration.closeSQLResultSet( rs );
      DBConfiguration.closeSQLStatement( stmt );
      DBConfiguration.closeSQLConnection( conn );
      rs = null;
      stmt = null;
      conn = null;
    }

    // Postconditions
    if ( rowCount > 1 ) {
      String msg = "2 or more id's matching " + memberId + "/" + rowingId;
      throw new EJBException( msg );
    }

    return retVal;
  } // loadParticipant()

  /**
   * Returns a collection of Participant2Snapshot instances for
   * the specified rowing session.
   * @return a non-null collection of Participant2Snapshot instances,
   * possibly empty
   */
  public static
  Collection findParticipant2SnapshotsForRowingSession( Integer rowingId )
    throws RowingException {

    // Preconditions
    if ( rowingId == null ) {
      throw new IllegalArgumentException( "null rowingId" );
    }

    Collection retVal = new ArrayList();

    Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    try {
      conn = DBConfiguration.getConnection();
      stmt = conn.prepareStatement(
          Configuration.SQL_PARTICIPANT_02,
          ResultSet.TYPE_FORWARD_ONLY,
          ResultSet.CONCUR_READ_ONLY);

      stmt.setInt(    1, rowingId.intValue() );
      rs = stmt.executeQuery();
      while ( rs.next() ) {
        Participant2Snapshot ps2 = mapResultSetToParticipant2Snapshot(rs);
        retVal.add( ps2 );
      } // while

    }
    catch( Exception x ) {
      String msg = "RowingUtils.findParticipant2SnapshotsForRowingSession #"
        + rowingId + ": "+ x.getClass().getName() + ": " + x.getMessage();
      theLog.error( msg, x );
      throw new RowingException( msg );
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
  } // findParticipant2SnapshotsForRowingSession(Integer)
  
  public static
  RowingSessionSnapshot mapResultSetToRowingSessionSnapshot(ResultSet rs)
    throws SQLException, RowingException {

    final int  rowing_id      = rs.getInt(    "rowing_id"    );
    final Date rowing_date    = rs.getDate(   "rowing_date"  );
    final Date rowing_time    = rs.getTime(   "rowing_date"  );
    final String rowing_level = rs.getString( "rowing_level" ).trim();
    final String rowing_type  = rs.getString( "rowing_type"  ).trim();
    final String rowing_state = rs.getString( "rowing_state" ).trim();

    Integer rowingId = new Integer( rowing_id );

    final String strDate = sdfDBDate.format( rowing_date );
    final String strTime = sdfDBTime.format( rowing_time );
    final StringBuffer sbDT = new StringBuffer( strDate );
    sbDT.append( " " );
    sbDT.append( strTime );
    final ParsePosition pos = new ParsePosition( 0 );
    final Date rowingDateTime = sdfConvert.parse( sbDT.toString(), pos );

    if ( theLog.isDebugEnabled() ) {
      final String prefix = "RowingDBRead.mapResultToRowing: ";
      theLog.debug( prefix + "rowing_date == '" + rowing_date + "'" );
      theLog.debug( prefix + "strDate == '" + strDate + "'" );
      theLog.debug( prefix + "strTime == '" + strTime + "'" );
      theLog.debug( prefix + "sbDT == '" + sbDT.toString() + "'" );
      theLog.debug( prefix + "rowingDateTime == '" + rowingDateTime + "'" );
    }

    RowingSessionState state = RowingSessionState.getState( rowing_state );
    RowingSessionLevel level = RowingSessionLevel.getLevel( rowing_level );
    RowingSessionType type   = RowingSessionType.getType(   rowing_type  );

    RowingSessionSnapshot retVal =
      new RowingSessionSnapshot(rowingId, state, rowingDateTime, level, type);

    if ( theLog.isDebugEnabled() ) {
      String msg = "mapResultSetToRowingSessionSnapshot: " + rowing_id + ", '"
        + sdfConvert.format( rowingDateTime ) + "', " + level.getName()
        + ", " + type.getName() + ", " + state.getName();
      theLog.debug( msg );
    }

    return retVal;
  } // mapResultSetToRowingSessionSnapshot(ResultSet)

  /**
   * Constructs an ParticipantSnapshot from the current row of a result set.
   * @param rs A result set containing the following columns:<ul>
   * <li>member_id</li>
   * <li>rowing_id</li>
   * <li>participant_id</li>
   * <li>requested</li>
   * <li>replaces_id</li>
   * <li>initial_seat</li>
   * <li>final_seat</li>
   * <li>attendance</li>
   * </ul>
   * @param rs A non-null result set containing the columns specified above.
   * @exception RowingException if the result set is exhausted.
   * @exception SQLException if some database-related error occurs
   */
  public static
  ParticipantSnapshot mapResultSetToParticipantSnapshot(ResultSet rs)
    throws SQLException, RowingException {

    final int member_id        = rs.getInt(    "member_id"      );
    final int rowing_id        = rs.getInt(    "rowing_id"      );
    final int participant_id   = rs.getInt(    "participant_id" );
    final String requested     = rs.getString( "requested"      );
    final int initial_seat     = rs.getInt(    "initial_seat"   );
 // final int initial_seat_num = rs.getInt(    ???              );
    final int final_seat       = rs.getInt(    "final_seat"     );
 // final int final_seat_num   = rs.getInt(    ???              );
    final int replaces_id      = rs.getInt(    "replaces_id"    );
    final String strAttendance = rs.getString( "attendance"     );

    // Convert int values to Integers, where 0 indicates null
    final Integer memberId =
      DBConfiguration.convertIntToIntegerOrNull( member_id );
    final Integer rowingId =
      DBConfiguration.convertIntToIntegerOrNull( rowing_id );
    final Integer participantId =
      DBConfiguration.convertIntToIntegerOrNull( participant_id );
    final Integer initialSeatId =
      DBConfiguration.convertIntToIntegerOrNull( initial_seat );
    final Integer finalSeatId =
      DBConfiguration.convertIntToIntegerOrNull( final_seat );
    final Integer replacesId =
      DBConfiguration.convertIntToIntegerOrNull( replaces_id );

    if ( theLog.isDebugEnabled() ) {
      String msg = "mapResultSetToParticipantSnapshot: " + member_id + ", "
        + rowing_id + ", " + participant_id + ", '"
        + requested + "', " + replaces_id + ", " 
        + initial_seat + ", " + final_seat + ", '"
        + strAttendance + "'";
      theLog.debug( msg );
    }

    // Convert Strings to objects
    SeatPreference preference = SeatPreference.getSeatPreference( requested );
    Attendance attendance = Attendance.getAttendance( strAttendance );

    ParticipantSnapshot retVal = null;
    if ( participant_id != NULL_INT ) {

      retVal = new ParticipantSnapshot( memberId, rowingId, participantId,
        preference, replacesId, initialSeatId, finalSeatId, attendance );
    }

    if ( theLog.isDebugEnabled() ) {
      String msg = "mapResultSetToParticipantSnapshot: " + memberId + ", "
        + rowingId + ", " + participantId + ", '"
        + requested + "', " + replacesId + ", " 
        + initialSeatId + ", " + finalSeatId + ", '"
        + strAttendance + "'";
      theLog.debug( msg );
    }

    return retVal;
  } // mapResultSetToParticipantSnapshot(ResultSet)

  /**
   * Constructs an Participant2Snapshot from the current row of a result set.
   * @param rs A result set containing the same columns as ParticipantSnapshot,
   * plus following columns:<ul>
   * <li>name_first</li>
   * <li>name_middle</li>
   * <li>name_last</li>
   * <li>name_suffix</li>
   * </ul>
   * @param rs A non-null result set containing the columns specified above.
   * @exception RowingException if the result set is exhausted.
   * @exception SQLException if some database-related error occurs
   */
  public static
  Participant2Snapshot mapResultSetToParticipant2Snapshot(ResultSet rs)
    throws SQLException, RowingException {

    // Preconditions
    if ( rs == null ) {
      throw new IllegalArgumentException( "null result set" );
    }

    ParticipantSnapshot ps    = mapResultSetToParticipantSnapshot(rs);

    final String name_last   = rs.getString( "name_last"   );
    final String name_first  = rs.getString( "name_first"  );
    final String name_middle = rs.getString( "name_middle" );
    final String name_suffix = rs.getString( "name_suffix" );
    MemberName memberName =
        new MemberName(name_first, name_middle, name_last, name_suffix);

    Participant2Snapshot retVal = new Participant2Snapshot(ps,memberName);

    if ( theLog.isDebugEnabled() ) {
      String msg = "mapResultSetToParticipantSnapshot: " 
        + ps.getMemberId() + "/" + ps.getRowingId() + "/"
        + ( ps == null ? null : ps.getParticipantId() );
      theLog.debug( msg );
    }

    return retVal;
  } // mapResultSetToParticipant(ResultSet)

  /**
   * Constructs an EnrollmentSnapshot from the current row of a result set.
   * @param rs A result set containing the following columns:<ul>
   * <li>rowing_id</li>
   * <li>rowing_date</li>
   * <li>rowing_level</li>
   * <li>rowing_type</li>
   * <li>rowing_state</li>
   * <li>participant_id</li>
   * <li>requested</li>
   * <li>initial_seat</li>
   * <li>final_seat</li>
   * <li>attendance</li>
   * <li>member_id</li>
   * </ul>
   * @param rs A non-null result set containing the columns specified above.
   * @exception RowingException if the result set is exhausted.
   * @exception SQLException if some database-related error occurs
   */
  public static
  EnrollmentSnapshot mapResultSetToEnrollmentSnapshot(ResultSet rs)
    throws SQLException, RowingException {

    // Preconditions
    if ( rs == null ) {
      throw new IllegalArgumentException( "null result set" );
    }

    final int member_id = rs.getInt( "member_id" );
    final Integer memberId = new Integer( member_id );

    RowingSessionSnapshot rss = mapResultSetToRowingSessionSnapshot(rs);
    ParticipantSnapshot ps    = mapResultSetToParticipantSnapshot(rs);
    EnrollmentSnapshot retVal = new EnrollmentSnapshot(memberId,rss,ps);

    if ( theLog.isDebugEnabled() ) {
      String msg = "mapResultSetToEnrollmentSnapshot: " 
        + member_id + "/" + rss.getId() + "/"
        + ( ps == null ? null : ps.getParticipantId() );
      theLog.debug( msg );
    }

    return retVal;
  } // mapResultSetToEnrollment(ResultSet)

} // RowingDBRead

/*
 * $Log: RowingDBRead.java,v $
 * Revision 1.3  2003/02/26 03:38:45  rphall
 * Added copyright and GPL license
 *
 * Revision 1.2  2002/01/30 15:02:47  rphall
 * Changed date formats fro compatibility with mySQL.
 * Changed findAllRowingSessionSnapshots* to a simple DB read.
 * Changed findEnrollmentSnapshots* for compatibility with mySQL.
 *
 * Revision 1.16  2002/01/30 01:28:57  rphall
 * Cleaned up some local variables
 *
 * Revision 1.15  2002/01/30 00:12:00  rphall
 * Removed SQL to select enrollments completely in the database
 *
 * Revision 1.14  2002/01/29 21:18:20  rphall
 * Added debugging
 *
 * Revision 1.13  2002/01/29 17:49:36  rphall
 * Broke up enrollment query into 4 steps for mySQL compatibility
 *
 * Revision 1.12  2002/01/24 18:38:15  rphall
 * Removed some unnecessary EJB imports.
 * Changed date formats.
 * Made "findAllRowing..InDateRange" a simple DB read.
 *
 * Revision 1.11  2002/01/20 21:16:35  rphall
 * Set sdfConvert to dateFormat
 *
 * Revision 1.10  2002/01/19 23:08:19  rphall
 * Converted findAllRowingSessionSnapshots to simple DB read
 *
 * Revision 1.9  2001/12/31 14:34:30  rphall
 * Renamed 'member.Name' to 'member.MemberName'
 *
 * Revision 1.8  2001/12/19 02:21:23  rphall
 * Changed log4j info messages to debug messages
 *
 * Revision 1.7  2001/12/18 13:36:06  rphall
 * Documentation
 *
 * Revision 1.6  2001/12/15 08:19:59  rphall
 * Documentation
 *
 * Revision 1.5  2001/12/15 05:23:44  rphall
 * Added DB routine to find Participant by member and rowing id
 *
 * Revision 1.4  2001/12/15 02:24:11  rphall
 * Added participant stuff
 *
 * Revision 1.3  2001/12/13 21:17:56  rphall
 * Fixed null handling in log message
 *
 * Revision 1.2  2001/12/13 01:30:21  rphall
 * Enrollment business and web objects
 *
 * Revision 1.1  2001/12/12 04:46:01  rphall
 * Moved from rowing/remote to rowing
 *
 * Revision 1.2  2001/12/12 04:10:19  rphall
 * Fixed build problems
 *
 * Revision 1.1  2001/12/11 16:17:01  rphall
 * Moved loadRow code to public loadRowingSession utility
 *
 */

