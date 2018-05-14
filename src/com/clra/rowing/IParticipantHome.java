/*
 * Copyright (c) Carnegie Lake Rowing Association 2002. All rights reserved.
 * Distributed under the GPL license. See doc/COPYING.
 * $RCSfile: IParticipantHome.java,v $
 * $Date: 2003/02/26 03:38:45 $
 * $Revision: 1.4 $
 */

package com.clra.rowing;

import com.clra.member.MemberSnapshot;
import java.util.Collection;
import java.rmi.RemoteException;
import javax.ejb.EJBHome;
import javax.ejb.CreateException;
import javax.ejb.FinderException;

/**
 * Factory class for IParticipant instances.
 *
 * @version $Id: IParticipantHome.java,v 1.4 2003/02/26 03:38:45 rphall Exp $
 * @author <a href="mailto:rphall@pluto.njcc.com">Rick Hall</a>
 */
public interface IParticipantHome extends EJBHome {

  /**
   * @param memberId the primary key of the member who is signing up.
   * @param rowingId the primary key of the session for which the member
   * is signing up.
   * @param preferred the seating requested by the member for the
   * rowing session.
   * @exception CreateException if the rowing session is not OPEN.
   */
  IParticipant create( Integer memberId, Integer rowingId,
    SeatPreference preferred ) throws CreateException, RemoteException;

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
  IParticipant create( MemberSnapshot member, RowingSessionSnapshot rowing )
      throws CreateException, RemoteException;

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
  IParticipant create( MemberSnapshot member, ParticipantSnapshot replaces )
      throws CreateException, RemoteException;

  IParticipant findByPrimaryKey( Integer participantId )
      throws FinderException, RemoteException;

  /**
   * The memberId and rowingId of a participant form an alternate key
   * to the Participant table, so the collection that is returned has
   * at most one IParticipant element.
   */
  Collection findByMemberIdRowingId( Integer memberId, Integer rowingId )
      throws FinderException, RemoteException;

  Collection findByRowingId( Integer rowingId )
      throws FinderException, RemoteException;

  Collection findAll()
      throws FinderException, RemoteException;

} // IParticipantHome

/*
 * $Log: IParticipantHome.java,v $
 * Revision 1.4  2003/02/26 03:38:45  rphall
 * Added copyright and GPL license
 *
 * Revision 1.3  2003/02/19 22:09:16  rphall
 * Removed gratuitous use of CLRA acronym
 *
 */

