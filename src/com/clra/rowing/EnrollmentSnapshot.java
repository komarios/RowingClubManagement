/*
 * Copyright (c) Carnegie Lake Rowing Association 2002. All rights reserved.
 * Distributed under the GPL license. See doc/COPYING.
 * $RCSfile: EnrollmentSnapshot.java,v $
 * $Date: 2003/02/26 03:38:45 $
 * $Revision: 1.3 $
 */

package com.clra.rowing;

import com.clra.member.MemberSnapshot;
import com.clra.rowing.Attendance;
import com.clra.rowing.ParticipantSnapshot;
import com.clra.rowing.RowingSessionLevel;
import com.clra.rowing.RowingSessionSnapshot;
import com.clra.rowing.RowingSessionState;
import com.clra.rowing.RowingSessionType;
import com.clra.rowing.SeatPreference;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

/**
 * Read-only information about enrollment of a member in a rowing session.
 * Unlike a Participant entity, which is non-null only if a member is signed
 * up for a rowing session, an enrollment instance is guaranteed to be non-null
 * regardless of whether a member is signed up in a rowing session.
 * An enrollment instance is a derived view that doesn't correspond to
 * a persistent object in the database.</p><p>
 *
 * An enrollment is composed of a member_id, a rowing_id, and a participant_id.
 * The member_id and rowing_id must be non-null. The participant_id is
 * non-null only if the member has signed up for the rowing session.
 * In addition to these primary keys, an enrollment object caches some
 * frequently needed data from the member, rowing session and participant
 * entities.</p><p>
 *
 * @version $Revision: 1.3 $ $Date: 2003/02/26 03:38:45 $
 * @author <a href="mailto:rphall@pluto.njcc.com">Rick Hall</a>
 */
public class EnrollmentSnapshot implements Serializable {

  /** Snapshot of rowing session (guaranteed to be non-null) */
  private RowingSessionSnapshot rowingSessionSnapshot = null;

  /** Snapshot of participation (may be null) */
  private ParticipantSnapshot participantSnapshot = null;

  /** Primary key of member (guaranteed to be non-null) */
  private Integer member_id = null;

  /** Returns the id of the rowing session associated with this enrollment */
  public Integer getRowingId() {
    return this.rowingSessionSnapshot.getId();
  }

  /** Returns the id of the member associated with this enrollment */
  public Integer getMemberId() {
    return this.member_id;
  }

  /**
   * Returns the id of the participant associated with this enrollment.
   * May be null.
   */
  public Integer getParticipantId() {
    Integer retVal = null;
    if ( participantSnapshot != null ) {
      retVal = participantSnapshot.getParticipantId();
    }
    return retVal;
  }

  /** Returns a snapshot of data about the rowing session */
  public RowingSessionSnapshot getRowingSessionSnapshot() {
    return this.rowingSessionSnapshot;
  }

  /**
   * Returns a snapshot of participant data in a rowing session.
   * May be null.
   */
  public ParticipantSnapshot getParticipantSnapshot() {
    return this.participantSnapshot;
  }

  /** Produces an invalid EnrollmentSnapshot. Used during deserialization */
  public EnrollmentSnapshot() {}

  /**
   * @param memberId a non-null primary key for a member
   * @param rs a non-null snapshot of a rowing session
   * @param ps a snapshot of a participant, possibly null. If non-null,
   * then the member and rowing id's associated with the participant
   * must match the id's of the preceding parameters.
   */
  public EnrollmentSnapshot(
    Integer memberId, RowingSessionSnapshot rs, ParticipantSnapshot ps ) {

    // Preconditions
    if ( memberId == null ) {
      throw new IllegalArgumentException( "null member id" );
    }
    if ( rs == null ) {
      throw new IllegalArgumentException( "null rowing snapshot" );
    }
    if ( ps != null && !ps.getMemberId().equals( memberId ) ) {
      String msg = "mismatch between participant and member";
      throw new IllegalArgumentException( msg );
    }
    if ( ps != null && !ps.getRowingId().equals( rs.getId() ) ) {
      String msg = "mismatch between participant and rowing session";
      throw new IllegalArgumentException( msg );
    }

    this.member_id = memberId;
    this.rowingSessionSnapshot = rs;
    this.participantSnapshot = ps;

  } // ctor(..)

} // EnrollmentSnapshot

/*
 * $Log: EnrollmentSnapshot.java,v $
 * Revision 1.3  2003/02/26 03:38:45  rphall
 * Added copyright and GPL license
 *
 * Revision 1.2  2002/01/30 14:41:52  rphall
 * Changed a comment
 *
 * Revision 1.4  2002/01/30 00:10:58  rphall
 * Change to a comment
 *
 * Revision 1.3  2001/12/13 21:22:12  rphall
 * Removed 'throws RowingException' from ctor
 *
 * Revision 1.2  2001/12/13 01:30:21  rphall
 * Enrollment business and web objects
 *
 * Revision 1.1  2001/12/12 12:26:29  rphall
 * Snapshot of a member's enrollment status in a rowing session
 *
 * Revision 1.2  2001/12/12 04:10:19  rphall
 * Fixed build problems
 *
 * Revision 1.1  2001/12/11 09:16:11  rphall
 * Checkpt
 *
 */

