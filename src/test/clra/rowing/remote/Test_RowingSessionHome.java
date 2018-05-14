/*
 * Copyright (c) Carnegie Lake Rowing Association 2002. All rights reserved.
 * Distributed under the GPL license. See doc/COPYING.
 * $RCSfile: Test_RowingSessionHome.java,v $
 * $Date: 2003/03/08 21:08:13 $
 * $Revision: 1.6 $
 */

package test.clra.rowing.remote;

import com.clra.rowing.IRowingSession;
import com.clra.rowing.IRowingSessionHome;
import com.clra.rowing.RowingSessionLevel;
import com.clra.rowing.RowingSessionState;
import com.clra.rowing.RowingSessionType;
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
 * This test depends on certain rows in the RowingSession table of the
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
public class Test_RowingSessionHome extends TestCase {

  // The logger used by this class
  private final static Category theLog =
    Category.getInstance(Test_RowingSessionHome.class);

  public void setUp() {
    Database.assertValid();
  }

  public void tearDown() {
    Database.assertValid();
  }

  public Test_RowingSessionHome( String name ) {
    super( name );
  }

  public void testCreate() {

    IRowingSession rs = null;
    IRowingSessionHome home = null;
    try {

      home = lookupHome();
      Date today = new Date();
      rs = home.create( today,
          RowingSessionLevel.LTR, RowingSessionType.PRACTICE );

      // FIXME assertTrue( rs.getDate().equals(today) );
      assertTrue(
        rs.getState().getName().equals(RowingSessionState.TENATIVE.getName()));
      assertTrue(
        rs.getLevel().getName().equals(RowingSessionLevel.LTR.getName() ));
      assertTrue(
        rs.getType().getName().equals(RowingSessionType.PRACTICE.getName()));

    }
    catch( Exception x ) {
      theLog.fatal( x.getMessage(), x );
      fail( x.getMessage() );
    }
    finally {
      remove( rs );
      rs = null;
    }

    return;
  } // testCreate()

  public void testFindByPrimaryKey() {

    IRowingSessionHome home = null;
    try {
      final Integer first = Expected.ROWINGSESSION_FIRST();
      final Integer last = Expected.ROWINGSESSION_LAST();
      home = lookupHome();
      IRowingSession irs =
        (IRowingSession) home.findByPrimaryKey( first );
      irs = (IRowingSession) home.findByPrimaryKey( last );
    }
    catch( Exception x ) {
      theLog.fatal( x.getMessage(), x );
      fail( x.getMessage() );
    }

    boolean isOK = false;
    final int nonexistent = (Expected.ROWINGSESSION_LAST().intValue())+1;
    try {
      home = lookupHome();
      IRowingSession irs =
        (IRowingSession) home.findByPrimaryKey( new Integer(nonexistent) );
      isOK = false;
    }
    catch( Exception expected ) {
      isOK = true;
    }
    if ( !isOK ) {
      fail( "Found nonexistent rowing session, id == " + nonexistent );
    }

    return;
  } // testFindAll()

  public void testFindAll() {

    IRowingSessionHome home = null;
    try {

      home = lookupHome();
      Collection c = home.findAll();
      assertTrue( c.size() == Expected.ROWINGSESSION_COUNT().intValue() );

    }
    catch( Exception x ) {
      theLog.fatal( x.getMessage(), x );
      fail( x.getMessage() );
    }

    return;
  } // testFindAll()

  public void testFindAll2() {

    IRowingSessionHome home = null;
    try {

      home = lookupHome();
      Collection c = home.findAll();
      for ( Iterator iter = c.iterator(); iter.hasNext(); ) {
        IRowingSession irs = (IRowingSession) iter.next();
        theLog.debug( "testFindAll2: IRowingSession #" + irs.getId() );
      }

    }
    catch( Exception x ) {
      theLog.fatal( x.getMessage(), x );
      fail( x.getMessage() );
    }

    return;
  } // testFindAll()

  public void testFindOctober() {

    IRowingSessionHome home = null;
    try {

      final int year = 2001;
      final int oct  = 9; // zero-based

      home = lookupHome();
      GregorianCalendar oct01 = new GregorianCalendar(year,oct-1,30,23,59,59);
      GregorianCalendar oct31 = new GregorianCalendar(year,oct,31,23,59,59);

      Collection c = home.findInDateRange( oct01.getTime(), oct31.getTime() );
      assertTrue( c.size() == Expected.ROWINGSESSION_OCT_COUNT().intValue() );

    }
    catch( Exception x ) {
      theLog.fatal( x.getMessage(), x );
      fail( x.getMessage() );
    }

    return;
  } // testFindAll()

  public static IRowingSessionHome lookupHome() {

    IRowingSessionHome home = null;
    try {
      InitialContext jndiContext = new InitialContext();
      Object ref  = jndiContext.lookup( Configuration.ROWINGSESSION_HOME() );
      home = (IRowingSessionHome)
          PortableRemoteObject.narrow (ref, IRowingSessionHome.class);
    }
    catch( Exception x ) {
      theLog.fatal( x.getMessage(), x );
      String msg = "unable to find RowingSessionHome at '"
          + Configuration.ROWINGSESSION_HOME() + "'";
      fail( msg );
    }

    return home;
  } // lookupHome()

  public static void remove( IRowingSession rs ) {

    if ( rs != null ) {
      try {
        rs.remove();
      }
      catch( Exception x ) {
        theLog.fatal( x.getMessage(), x );
      }
    }

  } // remove(IRowingSession)

} // Test_RowingSessionHome

/*
 * $Log: Test_RowingSessionHome.java,v $
 * Revision 1.6  2003/03/08 21:08:13  rphall
 * Changed name of Database.assert() method to assertValid()
 *
 * Revision 1.5  2003/02/26 03:38:47  rphall
 * Added copyright and GPL license
 *
 * Revision 1.4  2003/02/19 22:38:54  rphall
 * Removed gratuitous use of CLRA acronym
 *
 * Revision 1.3  2003/02/16 00:54:45  rphall
 * Minor bug fix
 */

