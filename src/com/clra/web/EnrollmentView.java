/*
 * Copyright (c) Carnegie Lake Rowing Association 2002. All rights reserved.
 * Distributed under the GPL license. See doc/COPYING.
 * $RCSfile: EnrollmentView.java,v $
 * $Date: 2003/02/26 03:38:46 $
 * $Revision: 1.3 $
 */

package com.clra.web;

import com.clra.rowing.EnrollmentSnapshot;
import com.clra.rowing.ParticipantSnapshot;
import com.clra.rowing.RowingSessionSnapshot;
import com.clra.rowing.RowingSessionState;
import java.io.Serializable;
import java.util.Date;

/**
 * Read-only information about enrollment of a member in a rowing session.
 * A thin wrapper around EnrollmentSnapshot. Adds value by defining
 * a natural order for presenting enrollment snapshots.
 *
 * @version $Revision: 1.3 $ $Date: 2003/02/26 03:38:46 $
 * @author <a href="mailto:rphall@pluto.njcc.com">Rick Hall</a>
 */
public class EnrollmentView implements Comparable, Serializable {

  /**
   * Primary key of associated member.
   * Don't use this attribute directly. Use the public getMemberId()
   * accessor, which validates the construction of this instance.
   */
  private Integer memberId = null;

  /**
   * View of associated rowing session.
   * Don't use this attribute directly. Use the public getSessionView()
   * accessor, which validates the construction of this instance.
   */
  private SessionView session = null;

  /**
   * View of associated participant.
   * As a matter of style, use the public getParticipantView() accessor,
   * not this attribute directly.
   */
  private ParticipantView participant = null;

  private int hashCode;

  /** Produces an invalid EnrollmentView. Used only by JSP's */
  public EnrollmentView() {}

  /**
   * @param es a non-null EnrollmentSnapshot
   */
  public EnrollmentView( EnrollmentSnapshot es ) {
    setData( es );
  }

  public void setData( EnrollmentSnapshot es ) {
    // Precondition
    if ( es == null ) {
      throw new IllegalArgumentException( "null enrollment" );
    }

    this.memberId = es.getMemberId();
    this.hashCode = this.memberId.hashCode();

    this.session = new SessionView( es.getRowingSessionSnapshot() );
    this.hashCode += this.session.hashCode();

    if ( es.getParticipantSnapshot() != null ) {
      this.participant = new ParticipantView( es.getParticipantSnapshot() );
      this.hashCode += this.participant.hashCode();
    }
    else {
      this.participant = null;
    }

  } // setData(EnrollmentSnapshot)

  public EnrollmentSnapshot getData() {
    // Preconditions
    if ( memberId == null || session == null ) {
      throw new IllegalStateException( "null memberId or session" );
    }

    EnrollmentSnapshot retVal;
    if ( getParticipantView() == null ) {
      retVal = new EnrollmentSnapshot(
        getMemberId(), getSessionView().getData(), null );
    }
    else {
      retVal = new EnrollmentSnapshot( getMemberId(),
        getSessionView().getData(), getParticipantView().getData() );
    }

    return retVal;
  } // getData();

  public Integer getMemberId() {
    if ( this.memberId == null ) {
      throw new IllegalStateException( "null memberId" );
    }
    return this.memberId;
  }

  public SessionView getSessionView() {
    if ( this.session == null ) {
      throw new IllegalStateException( "null session" );
    }
    return this.session;
  }

  /** May be null */
  public ParticipantView getParticipantView() {
    return this.participant;
  }

  /**
   * Enrollment status is a derived value. It does not correspond to the
   * state of any persistent entity. Rather, it reflects the state of
   * several entities, in a somewhat arbitrary manner.  It conveys useful
   * information to the user, but because multiple entity states can map
   * to a single enrollment status, it should not be used except for
   * presentation.</p><p>
   *
   * FIXME: even though this algorithm is intended for just
   * presenatation, it is closely related to business rules for invoicing
   * members and for balancing "bumps" among members. When those rules are
   * coded, this method should be rewritten in terms of those algorithms.
   *
   * @return a non-null, but possibly blank String.
   */
  public String getEnrollmentStatus() {

    String retVal = "";
    RowingSessionState sessionState = session.getData().getState();
    if ( sessionState.equals( RowingSessionState.CANCELLED ) ) {
      // FIXME remove hard-coded presentation
      retVal = "Cancelled";
    }
    else if ( sessionState.equals( RowingSessionState.CLOSED )
      || sessionState.equals( RowingSessionState.INVOICING )
      || sessionState.equals( RowingSessionState.COMPLETE ) ) {

      if ( participant != null && participant.getFinalSeatId() != null ) {
        retVal = "Boated";
      }
      else {
        // This is a "bumped", "substituted" or "noshow" state. However,
        // at this stage of development, DB test scripts are not sophisticated.
        // As a result, a lot of "bumped" states would be present
        // on prototype screens, which would be disconcerting to
        // acceptance testers. So this presentation value is blank
        // until DB scripts improve (and boating and attendance
        // modules allow final boatings to be marked!).
        //retVal = "Bumped";

        // FIXME remove hard-coded presentation
        retVal = "";
      }
    }
    else if ( sessionState.equals( RowingSessionState.BOATING2 )
      || sessionState.equals( RowingSessionState.BOATING1 ) ) {

      if ( participant != null && participant.getInitialSeatId() != null ) {
        retVal = "Boated";
      }
      else {
        // This is a "not-scheduled" " However, for the same
        // reasons as above, test scripts create a lot of "not-scheduled"
        // entries, which would be disconcerting for acceptance
        // testers. So this presentation value is blank
        // until DB scripts improve (and the boating 
        // module allows initial boatings to be assigned!).
        //retVal = "Not scheduled";

        // FIXME remove hard-coded presentation
        retVal="";
      }
    }
    else if ( sessionState.equals( RowingSessionState.LOCKED )
      || sessionState.equals( RowingSessionState.OPEN ) ) {

      retVal = participant == null ? "" : participant.getSeatPreference() ;
    }
    else {
      // This is probably an error state, but no exception should
      // thrown since this is just a presentation operation
      // FIXME add logging as a diagnostic

      // FIXME remove hard-coded presentation
      retVal="";
    }

    return retVal;
  } // getEnrollmentStatus()

  /** Two enrollments are equal iff their components are equal. */
  public boolean equals( Object o ) {

    boolean retVal = false;
    if ( o instanceof EnrollmentView ) {

      EnrollmentView ev = (EnrollmentView) o;
      retVal = this.getMemberId().equals( ev.getMemberId() );
      retVal = retVal && this.getSessionView().equals( ev.getSessionView() );
      if ( this.getParticipantView() != null ) {
        ParticipantView pv = this.getParticipantView();
        retVal = retVal && pv.equals( ev.getParticipantView() );
      }
      else if ( ev.getParticipantView() != null ) {
        retVal = false;
      }

    } // if EnrollmentView

    return retVal;
  } // equals(Object)

  /** Member objects are hashed by id's */
  public int hashCode() {
    return hashCode;
  }

  /**
   * Defines a natural ordering for enrollments by using the natural order
   * of MemberId, then SessionView, then by id of ParticipantView.
   *
   * @param o an EnrollmentView
   * @exception ClassCastException if o is not a enrollment view.
   */
  public int compareTo( Object o ) throws ClassCastException {

    EnrollmentView other = (EnrollmentView) o;

    int retVal = this.getMemberId().compareTo( other.getMemberId() );

    if ( retVal == 0 ) {
      retVal = this.getSessionView().compareTo( other.getSessionView() );
    }

    if ( retVal == 0 && this.getParticipantView() != null ) {
      if ( other.getParticipantView() == null ) {
        retVal = 1;
      }
      else {
        Integer thisId =
          this.getParticipantView().getData().getParticipantId();
        Integer otherId =
          other.getParticipantView().getData().getParticipantId();
        retVal = thisId.compareTo( otherId );
      }
    }
    else if ( retVal == 0 && this.getParticipantView() == null ) {
      if ( other.getParticipantView() != null ) {
        retVal = -1;
      }
    }

    return retVal;
  } // compareTo(Object)

} // EnrollmentView

/*
 * $Log: EnrollmentView.java,v $
 * Revision 1.3  2003/02/26 03:38:46  rphall
 * Added copyright and GPL license
 *
 * Revision 1.2  2002/02/18 18:05:56  rphall
 * Ran dos2unix to remove ^M (carriage return) from end of lines
 *
 * Revision 1.1.1.1  2002/01/03 21:57:28  rphall
 * Initial load, 5th try, Jan-03-2002 4:57 PM
 *
 * Revision 1.4  2001/12/13 21:21:15  rphall
 * Fixed equals(..) and compareTo(..); added getEnrollmentStatus()
 *
 * Revision 1.3  2001/12/13 01:30:21  rphall
 * Enrollment business and web objects
 *
 * Revision 1.2  2001/12/12 04:10:19  rphall
 * Fixed build problems
 *
 * Revision 1.1  2001/12/11 09:16:11  rphall
 * Checkpt
 *
 */

