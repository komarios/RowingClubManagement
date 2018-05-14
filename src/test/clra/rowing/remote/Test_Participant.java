/*
 * Copyright (c) Carnegie Lake Rowing Association 2002. All rights reserved.
 * Distributed under the GPL license. See doc/COPYING.
 * $RCSfile: Test_Participant.java,v $
 * $Date: 2003/02/26 03:38:47 $
 * $Revision: 1.3 $
 */

package test.clra.rowing.remote;

import com.clra.rowing.IParticipant;
import com.clra.rowing.IParticipantHome;
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
public class Test_Participant extends TestCase {

  // The logger used by this class
  private final static Category theLog =
    Category.getInstance(Test_Participant.class);

  // The rowingsession and date used during tests
  private IParticipant rowingsession = null;
  private Date today = null;

  public Test_Participant( String name ) {
    super( name );
  }

/* FIXME
  public void setUp() {

    this.today = new Date();

    IParticipant rs = null;
    IParticipantHome home = null;
    try {

      home = Test_ParticipantHome.lookupHome();
      rs = home.create( this.today,
          ParticipantLevel.LTR, ParticipantType.PRACTICE );

    }
    catch( Exception x ) {
      theLog.fatal( x.getMessage(), x );
      fail( x.getMessage() );
    }

    this.rowingsession = rs;

    return;
  } // setUp()

  public void tearDown() {

    Test_ParticipantHome.remove( this.rowingsession );
    this.rowingsession = null;
    this.today = null;

    return;
  } // tearDown()

  public void testSetup() {

    IParticipant rs = this.rowingsession;
    try {
      assertTrue( rs.getDate().equals(today) );
      assertTrue(
        rs.getState().getName().equals(ParticipantState.TENATIVE.getName()));
      assertTrue(
        rs.getLevel().getName().equals(ParticipantLevel.LTR.getName() ));
      assertTrue(
        rs.getType().getName().equals(ParticipantType.PRACTICE.getName()));
    }
    catch( RemoteException x ) {
      theLog.error( x.getMessage(), x );
      fail( x.getMessage() );
    }

  } // testSetup()
*/
} // Test_Participant

/*
 * $Log: Test_Participant.java,v $
 * Revision 1.3  2003/02/26 03:38:47  rphall
 * Added copyright and GPL license
 *
 * Revision 1.2  2002/02/18 18:08:11  rphall
 * Ran dos2unix to remove ^M (carriage return) from end of lines
 *
 * Revision 1.1.1.1  2002/01/03 21:57:28  rphall
 * Initial load, 5th try, Jan-03-2002 4:57 PM
 *
 * Revision 1.1  2001/12/18 13:34:57  rphall
 * Participant tests
 *
 */

