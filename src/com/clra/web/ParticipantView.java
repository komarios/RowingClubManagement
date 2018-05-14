/*
 * Copyright (c) Carnegie Lake Rowing Association 2002. All rights reserved.
 * Distributed under the GPL license. See doc/COPYING.
 * $RCSfile: ParticipantView.java,v $
 * $Date: 2003/02/26 03:38:46 $
 * $Revision: 1.3 $
 */

package com.clra.web;

import com.clra.rowing.ParticipantSnapshot;
import com.clra.rowing.SeatPreference;

/**
 * Read-only information about a member's participation in rowing session.
 * A thin-wrapper around ParticipantSnapshot,with some String properties
 * useful in JSP's.
 *
 * @version $Revision: 1.3 $ $Date: 2003/02/26 03:38:46 $
 * @author <a href="mailto:rphall@pluto.njcc.com">Rick Hall</a>
 */
public class ParticipantView {

  private ParticipantSnapshot data;

  /**
   * Constructs an invalid instance. Use this constructor only for JSP beans
   * and immediately set valid values via setValuesFromRowingSession(..).
   */
  public ParticipantView() {
    this.data = null;
  }

  public ParticipantView( ParticipantSnapshot data ) {
    setData( data );
  }

  public void setData( ParticipantSnapshot data ) {
    if ( data == null ) {
      throw new IllegalArgumentException( "null participant snapshot" );
    }
    this.data = data;
  }

  public ParticipantSnapshot getData() {
    if ( data == null ) {
      throw new IllegalStateException( "null participant data" );
    }
    return this.data;
  }

  /**
   * Return the primary key of the member associated with this participant.
   */
  Integer getMemberId() {
    return this.getData().getMemberId();
  }

  /**
   * Returns the primary key of the rowing session a participant has joined.
   */
  Integer getRowingId() {
    return this.getData().getRowingId();
  }

  /** Returns the primary key of a participant instance */
  public Integer getParticipantId() {
    return this.getData().getParticipantId();
  }

  /**
   * Returns the requested seating position of a signed-up participant,
   * or blank if a participant is a substitute or an extra.
   */
  String getSeatPreference() {
    String retVal = "";
    if ( this.getData().getSeatPreference() != null ) {
      retVal = this.getData().getSeatPreference().getName();
    }
    return retVal;
  }

  /**
   * Returns the participant id of the signed-up, but absent, participant
   * for whom this member is substituting, or null if this member is not a
   * substitute.
   */
  Integer getReplacesId() {
    return this.getData().getReplacesId();
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
  Integer getInitialSeatId() {
    return this.getData().getInitialSeatId();
  }

  /**
   * Returns the final seat assigned to a participant, or null if
   * a participant has not been assigned to a final boating.</p><p>
   *
   * See <tt>getInitialSeatId()</tt> for a note on the distinction between
   * seat id and seat number.
   */
  Integer getFinalSeatId() {
    return this.getData().getFinalSeatId();
  }

  /**
   * Returns the attendance of a participant at a rowing session,
   * or blank if a participant has not been assigned an attendance.
   */
  String getAttendance() {
    String retVal = "";
    if ( this.getData().getAttendance() != null ) {
      retVal = this.getData().getAttendance().getName();
    }
    return retVal;
  } // getAttendance()

} // ParticipantView

/*
 * $Log: ParticipantView.java,v $
 * Revision 1.3  2003/02/26 03:38:46  rphall
 * Added copyright and GPL license
 *
 * Revision 1.2  2002/02/18 18:06:50  rphall
 * Ran dos2unix to remove ^M (carriage return) from end of lines
 *
 * Revision 1.1.1.1  2002/01/03 21:57:28  rphall
 * Initial load, 5th try, Jan-03-2002 4:57 PM
 *
 * Revision 1.2  2001/12/13 21:23:50  rphall
 * Changed return values to Strings, Integers
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

