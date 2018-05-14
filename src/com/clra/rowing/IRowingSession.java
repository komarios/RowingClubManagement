/*
 * Copyright (c) Carnegie Lake Rowing Association 2002. All rights reserved.
 * Distributed under the GPL license. See doc/COPYING.
 * $RCSfile: IRowingSession.java,v $
 * $Date: 2003/02/26 03:38:45 $
 * $Revision: 1.5 $
 */

package com.clra.rowing;

import com.clra.util.ISerializableComparator;
import java.rmi.RemoteException;
import java.util.Date;
import javax.ejb.EJBObject;
import javax.ejb.RemoveException;

/**
 * Represents a rowing session.
 *
 * @version $Revision: 1.5 $ $Date: 2003/02/26 03:38:45 $
 * @author <a href="mailto:rphall@pluto.njcc.com">Rick Hall</a>
 */
public interface IRowingSession extends EJBObject {

  /**
   * Returns the natural Comparator for rowing sessions, in which rowing
   * sessions are compared by date, state, type, level, and id, with that
   * respective weighting.
   */
  ISerializableComparator getNaturalComparator() throws RemoteException;

  /** Returns a snapshot of a rowing session */
  RowingSessionSnapshot getData() throws RemoteException;

  /**
   * Sets the date, level and type of a rowing session if the rowing
   * session is TENATIVE. The id and state properties are not set.
   */
  void setData( RowingSessionSnapshot data )
    throws RemoteException, RowingSessionStateException;

  /**
   * Returns the primary key of a rowing session. The id is immutable
   * after a rowing session is created.
   */
  Integer getId() throws RemoteException;

  /**
   * Returns the state of a rowing session. The state of a rowing
   * session can not be set directly. It is changed as a side-effect
   * of other operations on a rowing session.
   */
  RowingSessionState getState() throws RemoteException;

  /**
   * Publishes a rowing session. Only TENATIVE sessions may be published.
   * The state of a published rowing session becomes OPEN.
   * @exception RowingSessionStateException if a non-tenative rowing session is
   * published.
   */
  void publish() throws RemoteException, RowingSessionStateException;

  /**
   * Locks a rowing session. Only OPEN sessions may be locked.
   * The state of a locked rowing session becomes LOCKED.
   * @exception RowingSessionStateException if a non-open rowing session is
   * locked.
   */
  void lock() throws RemoteException, RowingSessionStateException;

  /**
   * Cancels a rowing session. A TENTATIVE session may not be cancelled
   * (but a TENATIVE session may be deleted). The state of a cancelled
   * state becomes CANCELLED.
   * @exception RowingSessionStateException if a tenative rowing session is
   * cancelled.
   */
  void cancel() throws RemoteException, RowingSessionStateException;

  /**
   * Deletes a rowing session. Only a TENATIVE session may be deleted.
   * A deleted session is removed from the database.<p>
   *
   * This is a safe version of the standard EJBObject.remove()
   * operation. It checks that the session is tenative before removing it.
   * <strong>Application code should always delete, rather than remove, rowing
   * sessions.</strong> (The remove operation is required for testing.)<p>
   *
   * @exception RowingSessionStateException if a non-tenative rowing session is
   * deleted.
   * @see javax.ebj.EJBObject.remove()
   */
  void delete()
    throws RemoteException, RemoveException, RowingSessionStateException;

  /** Returns the date (and time) of a rowing session */
  Date getDate() throws RemoteException;

  /**
   * Edits the date (and time) of a rowing session. Editing is allowed only
   * for TENATIVE sessions.
   * @exception RowingSessionStateException if the edited session is not
   * in the TENATIVE state.
   * @see RowingSessionState
   */
  void setDate( Date date )
    throws RemoteException, RowingSessionStateException;

  /** Returns the level of a rowing session */
  RowingSessionLevel getLevel() throws RemoteException;

  /**
   * Edits the level of a rowing session. Editing is allowed only for
   * TENATIVE sessions.
   */
  void setLevel( RowingSessionLevel level )
    throws RemoteException, RowingSessionStateException;

  /** Returns the type of a rowing session */
  RowingSessionType getType()
    throws RemoteException;

  /**
   * Edits the type of a rowing session. Editing is allowed only for
   * TENATIVE sessions.
   */
  void setType( RowingSessionType type )
    throws RemoteException, RowingSessionStateException;

} // IRowingSession

/*
 * $Log: IRowingSession.java,v $
 * Revision 1.5  2003/02/26 03:38:45  rphall
 * Added copyright and GPL license
 *
 * Revision 1.4  2003/02/19 22:30:12  rphall
 * Removed gratuitous use of CLRA acronym
 *
 */

