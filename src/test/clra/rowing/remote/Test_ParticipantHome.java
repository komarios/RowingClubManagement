/*
 * Copyright (c) Carnegie Lake Rowing Association 2002. All rights reserved.
 * Distributed under the GPL license. See doc/COPYING.
 * $RCSfile: Test_ParticipantHome.java,v $
 * $Date: 2003/03/08 21:08:13 $
 * $Revision: 1.6 $
 */

package test.clra.rowing.remote;

import com.clra.member.MemberSnapshot;
import com.clra.rowing.IParticipant;
import com.clra.rowing.IParticipantHome;
import com.clra.rowing.ParticipantSnapshot;
import com.clra.rowing.RowingDBRead;
import com.clra.rowing.RowingSessionSnapshot;
import com.clra.rowing.SeatPreference;
import com.clra.util.ErrorUtils;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Set;
import javax.naming.InitialContext;
import javax.rmi.PortableRemoteObject;
import junit.framework.TestCase;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Category;
import test.clra.util.Database;
import test.clra.util.Expected;

/**
 * This test depends on certain rows in the Participant table of the
 * database. The script $CLRA_HOME/bin/setup.sh (and the SQL scripts
 * it calls in the $CLRA_HOME/etc/sql directory) is the best way to
 * prepare the database for testing.<p>
 *
 * This test -- if it runs to completion -- is designed to leave the
 * database unchanged.
 *
 * @author <a href="mailto:rphall@pluto.njcc.com">Rick Hall</a>
 * @version $Default:$ $Date: 2003/03/08 21:08:13 $
 */
public class Test_ParticipantHome extends TestCase {

  // The logger used by this class
  private final static Category theLog =
    Category.getInstance(Test_ParticipantHome.class);

  /** A full member of the club */
  private MemberSnapshot msFull = null;

  /** A LTR participant in the club */
  private MemberSnapshot msLTR = null;

  /** A regular rowing session */
  private RowingSessionSnapshot rssRegular = null;

  /** An LTR rowing session */
  private RowingSessionSnapshot rssLTR = null;

  /** A signed-up, full participant */
  private ParticipantSnapshot psFull = null;

  /** A signed-up, LTR participant */
  private ParticipantSnapshot psLTR = null;

  public void setUp() {
    Database.assertValid();
    createSnapshots();
  }

  private void createSnapshots() {
    try {
      // FIXME doesn't really get Full
      for ( int i=Expected.PARTICIPANT_MIN().intValue();
                i<Expected.PARTICIPANT_COUNT().intValue()
                    + Expected.PARTICIPANT_MIN().intValue();
                i++ ) {
        try {
          // These tests recycle participants, so expected #id's may not exist
          psFull = RowingDBRead.loadParticipant( new Integer(i) );
          rssRegular = RowingDBRead.loadRowingSession( psFull.getRowingId() );
          String st = rssRegular.getState().getName().trim();
          if ( !st.equalsIgnoreCase( "OPEN" ) ) {
            rssRegular = null;
          }
        } catch( Exception x ) {
          theLog.debug( "skipping participant id == " + i );
          psFull = null;
          rssRegular = null;
        }
        if ( psFull != null && rssRegular != null ) {
          break;
        }
      }
    }
    catch( Exception x ) {
      String msg = ErrorUtils.createDbgMsg( "createSnapshots", x );
      theLog.error( msg, x );
      throw new IllegalStateException( msg );
    }
  } // createSnapshots()

  public void tearDown() {
    Database.assertValid();
  }

  public Test_ParticipantHome( String name ) {
    super( name );
  }

  public void testFindByPrimaryKey() {
    
    IParticipant p = null;
    IParticipantHome home = null;
    try {

      theLog.debug(
        "testFindByPrimaryKey: looking up Participant home" );
      home = lookupHome();
      p = home.findByPrimaryKey( psFull.getParticipantId() );
      assertTrue( p.getParticipantId().equals( psFull.getParticipantId() ) );
      assertTrue( p.getMemberId().equals( psFull.getMemberId() ) );
      assertTrue( p.getRowingId().equals( psFull.getRowingId() ) );

    }
    catch( Exception x ) {
      String msg = ErrorUtils.createDbgMsg( "testFindByPrimaryKey", x );
      theLog.error( msg );
      fail( msg );
    }

  } // testFindByPrimaryKey()

  public void testRemoveAndCreate1() {

    IParticipant p = null;
    IParticipantHome home = null;
    try {

      final Integer MID = psFull.getMemberId();
      final Integer RID = psFull.getRowingId();
      final Integer PID = psFull.getParticipantId();
      final SeatPreference SP = psFull.getSeatPreference();

      theLog.debug(
        "testRemoveAndCreate1: looking up Participant home" );
      home = lookupHome();

      theLog.debug(
        "testRemoveAndCreate1: finding " + PID );
      p = home.findByPrimaryKey( PID );

      theLog.debug(
        "testRemoveAndCreate1: removing " + PID );
      p.remove();

      theLog.debug(
        "testRemoveAndCreate1: verifying removal of " + PID );
      ParticipantSnapshot ps = RowingDBRead.loadParticipant( MID, RID );
      assertTrue( ps == null );

      theLog.debug(
        "testRemoveAndCreate1: creating clone of " + PID );
      p = home.create( MID, RID, SP );

      theLog.debug(
        "testRemoveAndCreate1: verifying creation of " + PID );
      ps = RowingDBRead.loadParticipant( MID, RID );
      assertTrue( ps != null );

    }
    catch( Exception x ) {
      String msg = ErrorUtils.createDbgMsg( "testRemove", x );
      theLog.error( msg, x );
      fail( msg );
    }

  }

  public static IParticipantHome lookupHome() {

    IParticipantHome home = null;
    try {
      InitialContext jndiContext = new InitialContext();
      Object ref  = jndiContext.lookup( Configuration.PARTICIPANT_HOME() );
      home = (IParticipantHome)
          PortableRemoteObject.narrow (ref, IParticipantHome.class);
    }
    catch( Exception x ) {
      theLog.fatal( x.getMessage(), x );
      String msg = "unable to find ParticipantHome at '"
          + Configuration.PARTICIPANT_HOME() + "'";
      fail( msg );
    }

    return home;
  } // lookupHome()

  public static void remove( IParticipant p ) {

    if ( p != null ) {
      try {
        p.remove();
      }
      catch( Exception x ) {
        theLog.fatal( x.getMessage(), x );
      }
    }

  } // remove(IParticipant)

} // Test_ParticipantHome

/*
 * $Log: Test_ParticipantHome.java,v $
 * Revision 1.6  2003/03/08 21:08:13  rphall
 * Changed name of Database.assert() method to assertValid()
 *
 * Revision 1.5  2003/02/26 03:38:47  rphall
 * Added copyright and GPL license
 *
 * Revision 1.4  2003/02/19 22:49:45  rphall
 * Removed gratuitous use of CLRA acronym
 *
 * Revision 1.3  2003/02/16 00:52:58  rphall
 * Fixed bug when min participant count is not zero
 *
 * Revision 1.2  2002/02/18 18:08:15  rphall
 * Ran dos2unix to remove ^M (carriage return) from end of lines
 *
 * Revision 1.1.1.1  2002/01/03 21:57:28  rphall
 * Initial load, 5th try, Jan-03-2002 4:57 PM
 *
 * Revision 1.2  2001/12/18 16:46:31  rphall
 * Made test a bit more repeatable (w/o reinitializing DB)
 *
 * Revision 1.1  2001/12/18 13:34:57  rphall
 * Participant tests
 *
 */

