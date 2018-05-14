/*
 * Copyright (c) Carnegie Lake Rowing Association 2002. All rights reserved.
 * Distributed under the GPL license. See doc/COPYING.
 * $RCSfile: RowingUtils.java,v $
 * $Date: 2003/02/26 03:38:45 $
 * $Revision: 1.3 $
 */

package com.clra.rowing;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.Locale;
import java.util.Vector;
import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.EJBHome;
import javax.ejb.EJBObject;
import javax.ejb.FinderException;
import javax.ejb.Handle;
import javax.ejb.RemoveException;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;
import org.apache.log4j.Category;

/**
 * Utilities for finding and creating "base" entities: Member, RowingSession,
 * Boat, and Oarset. Other entities are created by operations on these base
 * entities.</p><p>
 *
 * These utilities are implemented by calls to EJB's. They are appropriate
 * where entities should be cached in memory, perhaps because the entities
 * will be modified shortly.</p><p>
 *
 * The class RowingDBRead defines utilities with similar signatures that
 * are implemented by directly reading from the database. These operations
 * are faster if objects are not already in memory, and if the objects
 * are not anticipated to require modification.</p>
 *
 * @author <a href="mailto:rphall@pluto.njcc.com">Rick Hall</a>
 * @version $Revision: 1.3 $ $Date: 2003/02/26 03:38:45 $
 */
public final class RowingUtils {

  private final static String base = RowingUtils.class.getName();
  private final static Category theLog = Category.getInstance( base );

  private static IRowingSessionHome _homeRowingSession = null;
  private static IParticipantHome _homeParticipant = null;

  private static IRowingSessionHome lookupRowingSessionHome()
    throws NamingException {

    IRowingSessionHome retVal = null;

    InitialContext jndiContext = new InitialContext();
    Object ref  = jndiContext.lookup( Configuration.ROWINGSESSION_HOME() );
    retVal = (IRowingSessionHome)
        PortableRemoteObject.narrow (ref, IRowingSessionHome.class);

    return retVal;
  } // lookupRowingSessionHome()

  private static IParticipantHome lookupParticipantHome()
    throws NamingException {

    IParticipantHome retVal = null;

    InitialContext jndiContext = new InitialContext();
    Object ref  = jndiContext.lookup( Configuration.PARTICIPANT_HOME() );
    retVal = (IParticipantHome)
        PortableRemoteObject.narrow (ref, IParticipantHome.class);

    return retVal;
  } // lookupParticipantHome()

  /**
   * Returns the factory for RowingSession instances.<p>
   *
   * <strong>
   * Note: this operation should be used only by this class, unit tests,
   * and the implementation class for IRowingSession
   * </strong>
   */
  public static IRowingSessionHome getRowingSessionHome()
    throws NamingException {

    if ( _homeRowingSession == null ) {
      _homeRowingSession = lookupRowingSessionHome();
    }
    return _homeRowingSession;
  }

  /**
   * Returns the factory for Participant instances.<p>
   *
   * <strong>
   * Note: this operation should be used only by this class, unit tests,
   * and the implementation class for IParticipant
   * </strong>
   */
  public static IParticipantHome getParticipantHome()
    throws NamingException {

    if ( _homeParticipant == null ) {
      _homeParticipant = lookupParticipantHome();
    }
    return _homeParticipant;
  }

  /** Returns the rowing session specified by the rowing id */
  public static IRowingSession findRowingSession( Integer rowingId )
    throws RemoteException, FinderException, NamingException {

    IRowingSession retVal = null;
    try {
      retVal = getRowingSessionHome().findByPrimaryKey( rowingId );
    }
    catch( RemoteException x ) {
      theLog.error( "RowingUtils.findRowingSession: " + x.getMessage(), x );
      // Null out the session factory; it will be recreated on next call
      _homeRowingSession = null;
      throw x;
    }
    catch( FinderException x ) {
      theLog.error( "RowingUtils.findRowingSession: " + x.getMessage(), x );
      throw x;
    }
    catch( EJBException x ) {
      theLog.error( "RowingUtils.findRowingSession: " + x.getMessage(), x );
      throw x;
    }

    return retVal;
  } // findRowingSession(Integer)

  /** Creates a rowing session */
  public static IRowingSession createRowingSession( Date date,
    RowingSessionLevel level, RowingSessionType type )
      throws RemoteException, CreateException, NamingException {

    IRowingSession retVal = null;
    try {
      retVal = getRowingSessionHome().create( date, level, type );
      // createRowingSessionList().addRowingSession( retVal );
    }
    catch( CreateException x ) {
      theLog.error( "RowingUtils.findRowingSession: " + x.getMessage(), x );
      throw x;
    }
    catch( EJBException x ) {
      theLog.error( "RowingUtils.findRowingSession: " + x.getMessage(), x );
      throw x;
    }

    return retVal;
  } // createRowingSession(..)

  /** Returns the participant specified by the participant id */
  public static IParticipant findParticipant( Integer participantId )
    throws RemoteException, FinderException, NamingException {

    IParticipant retVal = null;
    try {
      retVal = getParticipantHome().findByPrimaryKey( participantId );
    }
    catch( RemoteException x ) {
      theLog.error( "RowingUtils.findParticipant: " + x.getMessage(), x );
      // Null out the session factory; it will be recreated on next call
      _homeParticipant = null;
      throw x;
    }
    catch( FinderException x ) {
      theLog.error( "RowingUtils.findParticipant: " + x.getMessage(), x );
      throw x;
    }
    catch( EJBException x ) {
      theLog.error( "RowingUtils.findParticipant: " + x.getMessage(), x );
      throw x;
    }

    return retVal;
  } // findParticipant(Integer)

  /** Creates a participant */
  public static IParticipant createParticipant( Integer memberId,
    Integer rowingId, SeatPreference seatPreference )
      throws RemoteException, CreateException, NamingException {

    IParticipant retVal = null;
    try {
      retVal = getParticipantHome().create(memberId, rowingId, seatPreference);
    }
    catch( CreateException x ) {
      theLog.error( "RowingUtils.findParticipant: " + x.getMessage(), x );
      throw x;
    }
    catch( EJBException x ) {
      theLog.error( "RowingUtils.findParticipant: " + x.getMessage(), x );
      throw x;
    }

    return retVal;
  } // createParticipant(..)

  /**
   * Returns a "fake" rowing session, one which is neither persistent nor
   * managed by the EJB container. The primary key of the session (the session
   * id) is null.  Other session accessors return valid default values.
   */
  public static RowingSessionSnapshot createRowingSessionDefaults() {

    // Used only for construction
    final Integer FAKE_ID = new Integer( Integer.MIN_VALUE );

    // An anonymous extension of RowingSessionSnapshot that returns a null id.
    RowingSessionSnapshot retVal = new RowingSessionSnapshot(
        // Used only for construction
        FAKE_ID,
        // A non-persistent state (should be TENATIVE?)
        RowingSessionState.NEW,
        // Today's date
        new Date(),
        RowingSessionLevel.REGULAR,
        RowingSessionType.PRACTICE ) {

      // Overrides parent to return a null id
      public Integer getId() { return null; }
    };

    return retVal;
  } // createRowingSessionDefaults()

} // RowingUtils

/*
 * Log:$
 */

