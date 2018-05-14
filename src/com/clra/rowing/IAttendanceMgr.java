/*
 * Copyright (c) Carnegie Lake Rowing Association 2002. All rights reserved.
 * Distributed under the GPL license. See doc/COPYING.
 * $RCSfile: IAttendanceMgr.java,v $
 * $Date: 2003/02/26 03:38:45 $
 * $Revision: 1.3 $
 */

package com.clra.rowing;

import com.clra.util.ValidationException;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.Map;
import javax.ejb.EJBObject;

/**
 * Manages attendance for a RowingSession.
 *
 * @version $Id: IAttendanceMgr.java,v 1.3 2003/02/26 03:38:45 rphall Exp $
 * @author <a href="mailto:rphall@pluto.njcc.com">Rick Hall</a>
 */
public interface IAttendanceMgr extends EJBObject {

  void markParticipantAbsent( ParticipantSnapshot participant, String note )
    throws RemoteException, RowingException, RowingSessionStateException;

  String getParticipantStatus( ParticipantSnapshot participant )
    throws RemoteException, RowingException;

  void startFinalBoatings()
    throws RemoteException, RowingSessionStateException;

  void startFinalBoating( BoatingView boating )
    throws RemoteException, RowingException, RowingSessionStateException,
    BoatingStateException;

  SeatSnapshot setFinalBoating( BoatingView boating,
    ParticipantSnapshot participant, int seatnum )
      throws RemoteException, RowingException, RowingSessionStateException,
      BoatingStateException;

  void removeFinalBoating( SeatSnapshot seat )
    throws RemoteException, RowingException, RowingSessionStateException,
    BoatingStateException;

  void finishFinalBoating( BoatingView boating )
    throws RemoteException, RowingException, RowingSessionStateException,
    BoatingStateException;

  void finishFinalBoatings()
    throws RemoteException, RowingSessionStateException;

  void closeBoating( BoatingView boating )
    throws RemoteException, RowingException, RowingSessionStateException;

  void closeBoatings()
    throws RemoteException, RowingSessionStateException;

  void cancelRowingSession( String note )
    throws RemoteException, RowingException;

} // IAttendanceMgr

/*
 * $Log: IAttendanceMgr.java,v $
 * Revision 1.3  2003/02/26 03:38:45  rphall
 * Added copyright and GPL license
 *
 * Revision 1.2  2002/02/18 18:04:11  rphall
 * Ran dos2unix to remove ^M (carriage return) from end of lines
 *
 * Revision 1.1.1.1  2002/01/03 21:57:28  rphall
 * Initial load, 5th try, Jan-03-2002 4:57 PM
 *
 * Revision 1.4  2001/12/16 22:08:44  rphall
 * Renamed 'Seat' to 'SeatSnapshot'
 *
 * Revision 1.3  2001/12/13 21:16:12  rphall
 * Fixed View dependence
 *
 * Revision 1.2  2001/12/11 23:39:36  rphall
 * Moved MemberView, MemberSet from rowing to web package
 *
 * Revision 1.1  2001/12/01 18:13:23  rphall
 * Moved process operations to manager
 *
 */

