/*
 * Copyright (c) Carnegie Lake Rowing Association 2002. All rights reserved.
 * Distributed under the GPL license. See doc/COPYING.
 * $RCSfile: AllTests.java,v $
 * $Date: 2003/02/26 03:38:47 $
 * $Revision: 1.3 $
 */

package test.clra.rowing.remote;

import junit.framework.Test;
import junit.framework.TestSuite;
import org.apache.log4j.Category;

/**
 * @version $Revision: 1.3 $ $Date: 2003/02/26 03:38:47 $
 */
public class AllTests {

  // The general logger used by most methods
  private final static String baseCat = AllTests.class.getName();
  private final static Category theLog = Category.getInstance( baseCat );

  public static Test suite() {

    theLog.debug("building rowing.remote.AllTests ...");
    TestSuite suite =
            new TestSuite("All tests for test.clra.rowing.remote");

    theLog.debug("adding rowing.remote.Test_RowingSessionHome ...");
    suite.addTest( new TestSuite(Test_RowingSessionHome.class) );

    theLog.debug("adding rowing.remote.Test_RowingSession ...");
    suite.addTest( new TestSuite(Test_RowingSession.class) );

    theLog.debug("adding rowing.remote.Test_ParticipantHome ...");
    suite.addTest( new TestSuite(Test_ParticipantHome.class) );

    // theLog.debug("adding rowing.remote.Test_Participant ...");
    // suite.addTest( new TestSuite(Test_Participant.class) );

    theLog.debug("finished building rowing.remote.Alltests");
    return suite;
  }

} // AllTests

/*
 * $Log: AllTests.java,v $
 * Revision 1.3  2003/02/26 03:38:47  rphall
 * Added copyright and GPL license
 *
 * Revision 1.2  2002/02/18 18:07:50  rphall
 * Ran dos2unix to remove ^M (carriage return) from end of lines
 *
 * Revision 1.1.1.1  2002/01/03 21:57:28  rphall
 * Initial load, 5th try, Jan-03-2002 4:57 PM
 *
 * Revision 1.8  2001/12/18 13:34:34  rphall
 * Participant tests
 *
 */

