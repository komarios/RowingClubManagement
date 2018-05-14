/*
 * Copyright (c) Carnegie Lake Rowing Association 2002. All rights reserved.
 * Distributed under the GPL license. See doc/COPYING.
 * $RCSfile: Test_RowingSession.java,v $
 * $Date: 2003/02/26 03:38:47 $
 * $Revision: 1.3 $
 */

package test.clra.rowing.remote;

import com.clra.rowing.IRowingSession;
import com.clra.rowing.IRowingSessionHome;
import com.clra.rowing.RowingSessionLevel;
import com.clra.rowing.RowingSessionState;
import com.clra.rowing.RowingSessionType;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import junit.framework.TestCase;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Category;

/**
 * @version $Default:$ $Date: 2003/02/26 03:38:47 $
 */
public class Test_RowingSession extends TestCase {

  // The logger used by this class
  private final static Category theLog =
    Category.getInstance(Test_RowingSession.class);

  // The rowingsession and date used during tests
  private IRowingSession rowingsession = null;
  private Date today = null;

  public Test_RowingSession( String name ) {
    super( name );
  }

  public void setUp() {

    this.today = new Date();

    IRowingSession rs = null;
    IRowingSessionHome home = null;
    try {

      home = Test_RowingSessionHome.lookupHome();
      rs = home.create( this.today,
          RowingSessionLevel.LTR, RowingSessionType.PRACTICE );

    }
    catch( Exception x ) {
      theLog.fatal( x.getMessage(), x );
      fail( x.getMessage() );
    }

    this.rowingsession = rs;

    return;
  } // setUp()

  public void tearDown() {

    Test_RowingSessionHome.remove( this.rowingsession );
    this.rowingsession = null;
    this.today = null;

    return;
  } // tearDown()

  public void testSetup() {

    IRowingSession rs = this.rowingsession;
    try {
      assertTrue( rs.getDate().equals(today) );
      assertTrue(
        rs.getState().getName().equals(RowingSessionState.TENATIVE.getName()));
      assertTrue(
        rs.getLevel().getName().equals(RowingSessionLevel.LTR.getName() ));
      assertTrue(
        rs.getType().getName().equals(RowingSessionType.PRACTICE.getName()));
    }
    catch( RemoteException x ) {
      theLog.error( x.getMessage(), x );
      fail( x.getMessage() );
    }

  } // testSetup()

} // Test_RowingSession

/*
 * $Log: Test_RowingSession.java,v $
 * Revision 1.3  2003/02/26 03:38:47  rphall
 * Added copyright and GPL license
 *
 * Revision 1.2  2002/02/18 18:08:17  rphall
 * Ran dos2unix to remove ^M (carriage return) from end of lines
 *
 * Revision 1.1.1.1  2002/01/03 21:57:29  rphall
 * Initial load, 5th try, Jan-03-2002 4:57 PM
 *
 * Revision 1.2  2001/12/01 14:09:22  rphall
 * First meaningful version
 *
 * Revision 1.1  2001/11/30 11:44:30  rphall
 * First working tests
 *
 */

