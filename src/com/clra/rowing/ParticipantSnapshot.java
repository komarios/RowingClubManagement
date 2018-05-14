/*
 * Copyright (c) Carnegie Lake Rowing Association 2002. All rights reserved.
 * Distributed under the GPL license. See doc/COPYING.
 * $RCSfile: ParticipantSnapshot.java,v $
 * $Date: 2003/02/26 03:38:45 $
 * $Revision: 1.3 $
 */

package com.clra.rowing;

import com.clra.member.MemberSnapshot;

/**
 * Read-only information about a member's participation in rowing session.
 *
 * @version $Revision: 1.3 $ $Date: 2003/02/26 03:38:45 $
 * @author <a href="mailto:rphall@pluto.njcc.com">Rick Hall</a>
 */
public class ParticipantSnapshot {

  private final Integer rowingId;
  private final Integer memberId;
  private final Integer participantId;
  private final SeatPreference preference;
  private final Integer replacesId;
  private final Integer initialSeatId;
  private final Integer finalSeatId;
  private final Attendance attendance;

  public ParticipantSnapshot( Integer memberId, Integer rowingId,
    Integer participantId, SeatPreference preference,
    Integer replacesId, Integer initialSeatId,
    Integer finalSeatId, Attendance attendance ) {

    // Preconditions
    if ( rowingId == null ) {
       throw new IllegalArgumentException( "null session" );
    }
    if ( memberId == null ) {
       throw new IllegalArgumentException( "null member" );
    }
    if ( participantId == null ) {
       throw new IllegalArgumentException( "null participant id" );
    }

    this.memberId = memberId;
    this.rowingId = rowingId;
    this.participantId = participantId;
    this.preference = preference;
    this.replacesId = replacesId;
    this.initialSeatId = initialSeatId;
    this.finalSeatId = finalSeatId;
    this.attendance = attendance;

  } // ctor(..)

  /**
   * Return the primary key of the member associated with this participant.
   */
  public Integer getMemberId() {
    return this.memberId;
  }

  /**
   * Returns the primary key of the rowing session a participant has joined.
   */
  public Integer getRowingId() {
    return this.rowingId;
  }

  /** Returns the primary key of a participant instance */
  public Integer getParticipantId() {
    return this.participantId;
  }

  /**
   * Returns the requested seating position of a signed-up participant,
   * or null if a participant is a substitute or an extra.
   */
  public SeatPreference getSeatPreference() {
    return this.preference;
  }

  /**
   * Returns the participant id of the signed-up, but absent, participant
   * for whom this member is substituting, or null if this member is not a
   * substitute.
   */
  public Integer getReplacesId() {
    return this.replacesId;
  }

  /**
   * Returns the initial seat assigned to a participant, or null if
   * a participant hasn't been assigned an initial boating.</p><p>
   *
   * Note that seat id is not the same as seat number. A seat id is a primary
   * key for a triplet value composed of a rowing_id (identifies a rowing
   * session), a boat_id (identifies a particular sweep or scull), and
   * a seat number (identifies a position within a sweep or scull).
   */
  public Integer getInitialSeatId() {
    return this.initialSeatId;
  }

  /**
   * Returns the final seat assigned to a participant, or null if
   * a participant has not been assigned to a final boating.</p><p>
   *
   * See <tt>getInitialSeatId()</tt> for a note on the distinction between
   * seat id and seat number.
   */
  public Integer getFinalSeatId() {
    return this.finalSeatId;
  }

  /**
   * Returns the attendance of a participant at a rowing session,
   * or null if a participant has not been assigned an attendance.
   */
  public Attendance getAttendance() {
    return this.attendance;
  }

} // ParticipantSnapshot

/*
 * $Log: ParticipantSnapshot.java,v $
 * Revision 1.3  2003/02/26 03:38:45  rphall
 * Added copyright and GPL license
 *
 * Revision 1.2  2002/02/18 18:04:43  rphall
 * Ran dos2unix to remove ^M (carriage return) from end of lines
 *
 * Revision 1.1.1.1  2002/01/03 21:57:28  rphall
 * Initial load, 5th try, Jan-03-2002 4:57 PM
 *
 * Revision 1.3  2001/12/13 21:22:54  rphall
 * Fixed access control on getXxx()
 *
 * Revision 1.2  2001/12/13 01:30:21  rphall
 * Enrollment business and web objects
 *
 * Revision 1.1  2001/12/12 04:49:43  rphall
 * Compiles, but not yet tested
 *
 * Revision 1.1  2001/11/23 18:23:16  rphall
 * Read-only view of participant data
 *
 * Revision 1.2  2001/11/18 18:15:01  rphall
 * Fixed compilation problems
 *
 * Revision 1.1  2001/11/18 17:07:07  rphall
 * Checkpt before major revision of rowing package
 *
 */

