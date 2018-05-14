/*
 * Copyright (c) Carnegie Lake Rowing Association 2002. All rights reserved.
 * Distributed under the GPL license. See doc/COPYING.
 * $RCSfile: ParticipationForm.java,v $
 * $Date: 2003/02/26 03:38:46 $
 * $Revision: 1.4 $
 */

package com.clra.web;

import com.clra.member.MemberName;
import com.clra.rowing.Participant2Snapshot;
import com.clra.rowing.SeatPreference;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Category;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * Form bean for the session signup screen.  A session signup screen contains
 * the same information as a rowing session screen, plus additional fields.
 * Here is a list of additions or differences, with default values in square
 * brackets:
 * <ul>
 *
 * <li><b>action</b>
 * - The maintenance action that is being performed is restricted to
 * Create, Promote, Edit, View, Cancel or Delete. [Create]</li>
 *
 * <li><b>memberId</b>
 * - The primary key of the member signing up for the session. [REQUIRED]</li>
 *
 * <li><b>rowingId</b>
 * - The primary key of the rowing session. [REQUIRED]</li>
 *
 * <li><b>participantId</b>
 * - The read-only id of the participant. [REQUIRED on Edit]</li>
 *
 * <li><b>seatPreference</b>
 * - The requested seating for this participant. Null is valid only
 * if the participant is new or is to be removed.
 * Other allowed values are <tt>SeatPreference.NAME_STARBOARD</tt>,
 * <tt>SeatPreference.NAME_PORT</tt>, and so on.
 * [REQUIRED, null has significance]</li>
 *
 * <li><b>starboards</b>
 * - A read-only iterator of "starboard" participants. Items are returned
 * as String values for display. [REQUIRED]</li>
 *
 * <li><b>ports</b>
 * - A read-only iterator of "port" participants. Items are returned
 * as String values for display. [REQUIRED]</li>
 *
 * <li><b>coxswains</b>
 * - A read-only iterator of "cox" participants. Items are returned
 * as String values for display. [REQUIRED]</li>
 * </ul>
 *
 * @author <a href="mailto:rphall@pluto.njcc.com">Rick Hall</a>
 * @version $Revision: 1.4 $ $Date: 2003/02/26 03:38:46 $
 */
public final class ParticipationForm extends RowingSessionForm  {

  private final static String base = ParticipationForm.class.getName();
  private final static Category theLog = Category.getInstance( base );

  /** The id of the member. Non-null for valid forms. */
  private Integer memberId = null;

  /** The id of the participant. Null for new signups, non-null otherwise. */
  private Integer participantId = null;

  /**
   * The seating preference of the participant. Allowed values are null
   * (new or to be deleted), SeatPreference.NAME_STARBOARD,
   * SeatPreference.NAME_PORT, and so on.
  */
  private String strSeatPreference = null;

  /**
   * A read-only collection of participants in the session, sorted by name.
   * Items in the collection are instances of Participant2View.
   */
  private Collection participants = null;

  /** Return the persistent id of the member */
  public Integer getMemberId() {
    return this.memberId;
  }

  /** Set the member */
  void setMemberId( Integer memberId ) {
    this.memberId = memberId;
  }

  /**
   * Return the primary key of the participant. Null for new participants;
   * non-null otherwise.
   */
  public Integer getParticipantId() {
    return this.participantId;
  }

  /** Set the primary key of the participant. */
  void setParticipantId( Integer participantId ) {
    this.participantId = participantId;
  }

  /**
   * Return the seating preference of a participant. Null is valid only
   * if the participant is new or is to be removed. Other allowed values are
   * <tt>SeatPreference.NAME_STARBOARD</tt>, <tt>SeatPreference.NAME_PORT</tt>,
   * and so on.
   */
  public String getSeatPreference() {
    return this.strSeatPreference;
  }

  /**
   * Set the seating preference of a participant. Null is valid only if
   * the participant is new or is to be removed. Other allowed values are
   * <tt>SeatPreference.NAME_STARBOARD</tt>, <tt>SeatPreference.NAME_PORT</tt>,
   * and so on.
   */
  public void setSeatPreference( String preference ) {
    this.strSeatPreference = preference;
  }

  /**
   * Sets the (name-sorted) collection of other participants in this
   * rowing session. Items in the collection should be instances of
   * Participant2View.
   */
  void setParticipants( Collection participants ) {
    if ( participants == null ) {
      throw new IllegalArgumentException( "null participant collection" );
    }
    this.participants = participants;
  }

  /**
   * Returns a read-only iterator of "starboard" participants.
   * Items in the iteration are Strings, formatted for display on a screen.
   */
  public Iterator getStarboards() {
    Iterator retVal = selectStarboards( this.participants );
    return retVal;
  }

  /**
   * Returns a read-only iterator of "starboard" participants from a
   * collection of participants. Items in the iteration are Strings,
   * formatted for display on a screen.
   * @param participants a collection of Participant2Snapshot objects.
   */
  static Iterator selectStarboards( Collection participants ) {
    // Precondition
    if ( participants == null ) {
      throw new IllegalArgumentException( "null participant collection" );
    }

    final Iterator iter = participants.iterator();

    Participant2View tmp = null;
    while ( iter.hasNext() && tmp == null ) {
      Object o = iter.next();
      if ( !(o instanceof Participant2Snapshot) ) {
        String msg = "participants include invalid " + o.getClass().getName();
        throw new IllegalArgumentException( msg );
      }
      Participant2Snapshot p2s = (Participant2Snapshot) o;
      Participant2View p2v = new Participant2View( p2s );
      if ( isStarboard(p2v) ) {
        tmp = p2v;
      }
    }
    final Participant2View first = tmp;

    return new Iterator() {
      Participant2View nextStarboard = first;
      public boolean hasNext() {
        return nextStarboard != null;
      }
      public Object next() {

        if ( nextStarboard == null ) {
          throw new NoSuchElementException();
        }
        Object retVal = nextStarboard;

        nextStarboard = null;
        while ( iter.hasNext() && nextStarboard == null ) {
          Participant2Snapshot p2s = (Participant2Snapshot) iter.next();
          Participant2View p2v = new Participant2View( p2s );
          if ( isStarboard(p2v) ) {
            nextStarboard = p2v;
          }
        }

        return retVal;
      }
      public void remove() {
        throw new UnsupportedOperationException( "remove not supported" );
      }
    };
  } // selectStarboards(Collection)

  /**
   * Returns a read-only iterator of "port" participants.
   * Items in the iteration are Strings, formatted for display on a screen.
   */
  public Iterator getPorts() {
    Iterator retVal = selectPorts( this.participants );
    return retVal;
  }

  /**
   * Returns a read-only iterator of "port" participants from a
   * collection of participants. Items in the iteration are Strings,
   * formatted for display on a screen.
   * @param participants a collection of Participant2Snapshot objects.
   */
  static Iterator selectPorts( Collection participants ) {
    // Precondition
    if ( participants == null ) {
      throw new IllegalArgumentException( "null participant collection" );
    }

    final Iterator iter = participants.iterator();

    Participant2View tmp = null;
    while ( iter.hasNext() && tmp == null ) {
      Object o = iter.next();
      if ( !(o instanceof Participant2Snapshot) ) {
        String msg = "participants include invalid " + o.getClass().getName();
        throw new IllegalArgumentException( msg );
      }
      Participant2Snapshot p2s = (Participant2Snapshot) o;
      Participant2View p2v = new Participant2View( p2s );
      if ( isPort(p2v) ) {
        tmp = p2v;
      }
    }
    final Participant2View first = tmp;

    return new Iterator() {
      Participant2View nextPort = first;
      public boolean hasNext() {
        return nextPort != null;
      }
      public Object next() {

        if ( nextPort == null ) {
          throw new NoSuchElementException();
        }
        Object retVal = nextPort;

        nextPort = null;
        while ( iter.hasNext() && nextPort == null ) {
          Participant2Snapshot p2s = (Participant2Snapshot) iter.next();
          Participant2View p2v = new Participant2View( p2s );
          if ( isPort(p2v) ) {
            nextPort = p2v;
          }
        }

        return retVal;
      }
      public void remove() {
        throw new UnsupportedOperationException( "remove not supported" );
      }
    };
  } // selectPorts(Collection)

  /**
   * Returns a read-only iterator of "coxswain" participants.
   * Items in the iteration are Strings, formatted for display on a screen.
   */
  public Iterator getCoxswains() {
    Iterator retVal = selectCoxswains( this.participants );
    return retVal;
  }

  /**
   * Returns a read-only iterator of "coxswain" participants from a
   * collection of participants. Items in the iteration are Strings,
   * formatted for display on a screen.
   * @param participants a collection of Participant2Snapshot objects.
   */
  static Iterator selectCoxswains( Collection participants ) {
    // Precondition
    if ( participants == null ) {
      throw new IllegalArgumentException( "null participant collection" );
    }

    final Iterator iter = participants.iterator();

    Participant2View tmp = null;
    while ( iter.hasNext() && tmp == null ) {
      Object o = iter.next();
      if ( !(o instanceof Participant2Snapshot) ) {
        String msg = "participants include invalid " + o.getClass().getName();
        throw new IllegalArgumentException( msg );
      }
      Participant2Snapshot p2s = (Participant2Snapshot) o;
      Participant2View p2v = new Participant2View( p2s );
      if ( isCox(p2v) ) {
        tmp = p2v;
      }
    }
    final Participant2View first = tmp;

    return new Iterator() {
      Participant2View nextCox = first;
      public boolean hasNext() {
        return nextCox != null;
      }
      public Object next() {

        if ( nextCox == null ) {
          throw new NoSuchElementException();
        }
        Object retVal = nextCox;

        nextCox = null;
        while ( iter.hasNext() && nextCox == null ) {
          Participant2Snapshot p2s = (Participant2Snapshot) iter.next();
          Participant2View p2v = new Participant2View( p2s );
          if ( isCox(p2v) ) {
            nextCox = p2v;
          }
        }

        return retVal;
      }
      public void remove() {
        throw new UnsupportedOperationException( "remove not supported" );
      }
    };
  } // selectCoxswains(Collection)

  /**
   * Reset all properties to their default values.
   *
   * @param mapping The mapping used to select this instance
   * @param request The servlet request we are processing
  */
  public void reset(ActionMapping mapping, HttpServletRequest request) {

    // FIXME reset to request values

  }

  /**
   * Validate the properties that have been set from this HTTP request,
   * and return an <code>ActionErrors</code> object that encapsulates any
   * validation errors that have been found.  If no errors are found, return
   * <code>null</code> or an <code>ActionErrors</code> object with no
   * recorded error messages.
   *
   * @param mapping The mapping used to select this instance
   * @param request The servlet request we are processing
  */
  public ActionErrors validate(ActionMapping mapping,
                           HttpServletRequest request) {

    ActionErrors errors = new ActionErrors();

    return (errors);
  }

  /**
   * A utility that creates a String representation of participant.
   */
  public static String formatParticipantAsString( Participant2View p2 ) {
    if ( p2 == null ) {
      throw new IllegalArgumentException( "null participant" );
    }

    String sp = p2.getSeatPreference();

    String extra = "";
    if ( SeatPreference.NAME_PORT_THEN_STARBOARD.equalsIgnoreCase(sp) ) {
      extra = SeatPreference.NAME_PORT_THEN_STARBOARD;
    }
    else if ( SeatPreference.NAME_STARBOARD_THEN_PORT.equalsIgnoreCase(sp) ) {
      extra = SeatPreference.NAME_STARBOARD_THEN_PORT;
    }

    MemberName n = p2.getMemberName();
    String[] params = {
      n.getFirstName(),
      ( n.hasMiddleName() ? n.getMiddleName().trim() : "" ),
      n.getLastName(),
      ( n.hasSuffix() ? n.getSuffix().trim() : "" ),
      extra
    };

    String retVal = Text.getMessage( "participant.format", params );

    return retVal;
  } // formatParticipantAsString(Participant2View)

  /** Checks whether a participant prefers a PORT seat */
  private static boolean isStarboard( Participant2View p2 ) {

    boolean retVal = false;
    if ( p2 != null && p2.getSeatPreference() != null ) {

      String pref = p2.getSeatPreference().trim();
      retVal = SeatPreference.NAME_STARBOARD_THEN_PORT.equals( pref );
      retVal = retVal || SeatPreference.NAME_STARBOARD.equals( pref );
    }

    return retVal;
  } // isStarboard(Participant2View)

  /** Checks whether a participant prefers a STARBOARD seat */
  private static boolean isPort( Participant2View p2 ) {

    boolean retVal = false;
    if ( p2 != null && p2.getSeatPreference() != null ) {

      String pref = p2.getSeatPreference().trim();
      retVal = SeatPreference.NAME_PORT_THEN_STARBOARD.equals( pref );
      retVal = retVal || SeatPreference.NAME_PORT.equals( pref );
    }

    return retVal;
  } // isPort(Participant2View)

  /** Checks whether a participant prefers to COX */
  private static boolean isCox( Participant2View p2 ) {

    boolean retVal = false;
    if ( p2 != null && p2.getSeatPreference() != null ) {

      String pref = p2.getSeatPreference().trim();
      retVal = SeatPreference.NAME_COX.equals( pref );
    }

    return retVal;
  } // isPort(Participant2View)

} // ParticipationForm

/*
 * $Log: ParticipationForm.java,v $
 * Revision 1.4  2003/02/26 03:38:46  rphall
 * Added copyright and GPL license
 *
 * Revision 1.3  2002/03/24 01:54:41  rphall
 * Broke collection filters into components for reuse
 *
 * Revision 1.2  2002/01/30 15:24:38  rphall
 * Removed System.out.println debugging
 *
 * Revision 1.6  2002/01/24 18:32:13  rphall
 * Removed println; changed whitespace
 *
 * Revision 1.5  2001/12/31 14:34:30  rphall
 * Renamed 'member.Name' to 'member.MemberName'
 *
 * Revision 1.4  2001/12/15 08:21:56  rphall
 * Beginning to work
 *
 * Revision 1.3  2001/12/15 02:30:14  rphall
 * New impl, extended from RowingSessionForm
 *
 * Revision 1.2  2001/12/15 00:22:41  rphall
 * Checkpt
 *
 * Revision 1.1  2001/11/28 12:19:28  rphall
 * Started struts framework for managing member participation
 *
 */

