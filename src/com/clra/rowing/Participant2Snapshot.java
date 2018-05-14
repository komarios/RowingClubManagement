/*
 * Copyright (c) Carnegie Lake Rowing Association 2002. All rights reserved.
 * Distributed under the GPL license. See doc/COPYING.
 * $RCSfile: Participant2Snapshot.java,v $
 * $Date: 2003/02/26 03:38:45 $
 * $Revision: 1.3 $
 */

package com.clra.rowing;

import com.clra.member.MemberName;

/**
 * Read-only information about a member's participation in rowing session.
 * Extends ParticipantSnapshot by caching the name of a participant.
 *
 * @version $Revision: 1.3 $ $Date: 2003/02/26 03:38:45 $
 * @author <a href="mailto:rphall@pluto.njcc.com">Rick Hall</a>
 */
public class Participant2Snapshot extends ParticipantSnapshot {

  private final MemberName memberName;

  public Participant2Snapshot( Integer memberId, MemberName memberName,
    Integer rowingId, Integer participantId,
    SeatPreference preference, Integer replacesId,
    Integer initialSeatId, Integer finalSeatId, Attendance attendance ) {

    super( memberId, rowingId, participantId, preference, replacesId,
            initialSeatId, finalSeatId, attendance );

    // Enforce preconditions
    if ( memberName == null ) {
       throw new IllegalArgumentException( "null memberName" );
    }

    this.memberName = memberName;

  } // ctor(..)

  public Participant2Snapshot( ParticipantSnapshot ps, MemberName memberName ) {

    this ( ps.getMemberId(), memberName, ps.getRowingId(),
      ps.getParticipantId(), ps.getSeatPreference(), ps.getReplacesId(),
      ps.getInitialSeatId(), ps.getFinalSeatId(), ps.getAttendance() );

  }

  /** Return the name of the associated member */
  public MemberName getMemberName() {
    return this.memberName;
  }

  /** Return a bare-naked snapshot */
  public ParticipantSnapshot getData() {
    return new ParticipantSnapshot( getMemberId(), getRowingId(),
      getParticipantId(), getSeatPreference(), getReplacesId(),
      getInitialSeatId(), getFinalSeatId(), getAttendance() );
  }

} // Participant2Snapshot

/*
 * $Log: Participant2Snapshot.java,v $
 * Revision 1.3  2003/02/26 03:38:45  rphall
 * Added copyright and GPL license
 *
 * Revision 1.2  2002/02/18 18:04:40  rphall
 * Ran dos2unix to remove ^M (carriage return) from end of lines
 *
 * Revision 1.1.1.1  2002/01/03 21:57:28  rphall
 * Initial load, 5th try, Jan-03-2002 4:57 PM
 *
 * Revision 1.2  2001/12/31 14:32:58  rphall
 * Renamed 'member.Name' to 'member.MemberName'
 *
 * Revision 1.1  2001/12/15 02:21:49  rphall
 * Participant data plus member name
 *
 */

