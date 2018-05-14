/*
 * Copyright (c) Carnegie Lake Rowing Association 2002. All rights reserved.
 * Distributed under the GPL license. See doc/COPYING.
 * $RCSfile: IRowingSessionHome.java,v $
 * $Date: 2003/02/26 03:38:45 $
 * $Revision: 1.3 $
 */

package com.clra.rowing;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Date;
import javax.ejb.EJBHome;
import javax.ejb.CreateException;
import javax.ejb.FinderException;

/**
 * Factory class for IRowingSession instances.
 *
 * @version $Id: IRowingSessionHome.java,v 1.3 2003/02/26 03:38:45 rphall Exp $
 * @author <a href="mailto:rphall@pluto.njcc.com">Rick Hall</a>
 */
public interface IRowingSessionHome extends EJBHome {

  /**
   * Creates a rowing session on the specified date (and time).<p>
   *
   * Application classes should not invoke this operation directly, but
   * rather should use <tt>RowingUtils.createRowingSession</tt>, in order
   * to guarantee that any (future) optimized lists remains up-to-date.
   *
   * @param date the date (and time) on which to create the session.
   * @param level the level of the session (Regular, LTR)
   * @param type the type of the session (Practice, Regatta)
   * @see RowingUtils#
   */
  IRowingSession create( Date date, RowingSessionLevel level,
      RowingSessionType type ) throws CreateException, RemoteException;

  IRowingSession findByPrimaryKey( Integer rowingId )
      throws FinderException, RemoteException;

  Collection findInDateRange( Date start, Date finish )
      throws FinderException, RemoteException;

  Collection findAll() throws FinderException, RemoteException;

} // IRowingSessionHome

/*
 * $Log: IRowingSessionHome.java,v $
 * Revision 1.3  2003/02/26 03:38:45  rphall
 * Added copyright and GPL license
 *
 * Revision 1.2  2002/02/18 18:04:35  rphall
 * Ran dos2unix to remove ^M (carriage return) from end of lines
 *
 * Revision 1.1.1.1  2002/01/03 21:57:28  rphall
 * Initial load, 5th try, Jan-03-2002 4:57 PM
 *
 * Revision 1.5  2001/12/11 23:43:18  rphall
 * Documentation
 *
 * Revision 1.4  2001/12/05 03:54:41  rphall
 * Documentation
 *
 * Revision 1.3  2001/11/30 11:38:00  rphall
 * First working version, RowingSession entity bean
 *
 * Revision 1.2  2001/11/28 16:10:36  rphall
 * Removed findAll(), fixed name of findByPrimaryKey()
 *
 * Revision 1.1  2001/11/23 18:34:08  rphall
 * Major revision.
 *
 */

