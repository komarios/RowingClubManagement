/*
 * Copyright (c) Carnegie Lake Rowing Association 2002. All rights reserved.
 * Distributed under the GPL license. See doc/COPYING.
 * $RCSfile: IParticipant.java,v $
 * $Date: 2003/02/26 03:38:45 $
 * $Revision: 1.4 $
 */

package com.clra.rowing;

import java.rmi.RemoteException;
import java.util.Date;
import java.util.Map;
import javax.ejb.EJBObject;

/**
 * Defines operations that modify a member's participation in rowing session.
 *
 * @version $Id: IParticipant.java,v 1.4 2003/02/26 03:38:45 rphall Exp $
 * @author <a href="mailto:rphall@pluto.njcc.com">Rick Hall</a>
 */
public interface IParticipant extends EJBObject {

  /** Returns a snapshot of a participant */
  ParticipantSnapshot getData() throws RemoteException;

  /** Returns the primary key of a participant */
  Integer getParticipantId() throws RemoteException;

  /**
   * Returns the primary key of the rowing session to which the participant
   * belongs.
   */
  Integer getRowingId() throws RemoteException;

  /** Returns the primary key of the member that the participant represents */
  Integer getMemberId() throws RemoteException;

  /**
   * Returns the seat preference of a participant.
   * @return a non-null SeatPreference if the participant signed up
   * in advance for the rowing session, or null if the participant
   * joined the rowing session as an 'Extra' or as a substitute for
   * another, signed-up participant.
   */
  SeatPreference getSeatPreference() throws RemoteException;

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
  void setSeatPreference( SeatPreference seatPreference )
      throws RemoteException, ParticipantStateException, RowingException;

  /**
   * Returns the participant for whom this participant is substituting.
   * @return a non-null IParticipant if this participant is a substitute,
   * or null if this participant signed up or joined as an 'Extra'.
   */
  IParticipant getSubstitutedParticipant() throws RemoteException;

  /**
   * Assigns a preliminary seating to a participant before a rowing session
   * starts.
   * @exception ParticipantStateException if the participant
   * has not signed up for the rowing session.
   * @exception RowingException if the assignment conflicts with the
   * business rules for updating a boating; for example, if the
   * rowing session to which the boating belongs is not locked.
   */
  void setInitialSeat( SeatSnapshot seat, IBoating boating )
      throws RemoteException, ParticipantStateException, RowingException;

  /**
   * Returns the initial seat assignment of this participant, or null
   * if an initial seating was not made.
   */
  SeatSnapshot getInitialSeat() throws RemoteException;

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
  void setFinalSeat( SeatSnapshot seat, IBoating boating )
      throws RemoteException, ParticipantStateException, RowingException;

  /**
   * Returns the final seat assignment of this participant, or null
   * if a final seating was not made.
   */
  SeatSnapshot getFinalSeat() throws RemoteException;

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
  void setAttendance( Attendance attendance, IRowingSession session )
      throws RemoteException, ParticipantStateException, RowingException;

  /**
   * Returns the attendance of a participant at a rowing session, or null
   * if attendance has not been marked.
   */
  Attendance getAttendance() throws RemoteException;

} // IParticipant

/*
 * $Log: IParticipant.java,v $
 * Revision 1.4  2003/02/26 03:38:45  rphall
 * Added copyright and GPL license
 *
 * Revision 1.3  2003/02/19 22:09:15  rphall
 * Removed gratuitous use of CLRA acronym
 *
 */

