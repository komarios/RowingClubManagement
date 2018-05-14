/*
 * Copyright (c) Carnegie Lake Rowing Association 2002. All rights reserved.
 * Distributed under the GPL license. See doc/COPYING.
 * $RCSfile: AllTests.java,v $
 * $Date: 2003/03/04 02:31:34 $
 * $Revision: 1.6 $
 */

package test.clra;

import junit.framework.Test;
import junit.framework.TestSuite;
import org.apache.log4j.Category;

/**
 * @version $Revision: 1.6 $ $Date: 2003/03/04 02:31:34 $
 */
public class AllTests {

  // The general logger used by most methods
  private final static String baseCat = AllTests.class.getName();
  private final static Category theLog = Category.getInstance( baseCat );

  public static Test suite() {

    theLog.debug("building clra.Alltests ...");
    TestSuite suite = new TestSuite("All tests for test.clra");

    theLog.debug("adding rowing.AllTests ...");
    suite.addTest( test.clra.rowing.AllTests.suite() );

    theLog.debug("adding member.AllTests ...");
    suite.addTest( test.clra.member.AllTests.suite() );

    theLog.debug("adding member.remote.AllTests ...");
    suite.addTest( test.clra.member.remote.AllTests.suite() );

    theLog.debug("adding rowing.remote.AllTests ...");
    suite.addTest( test.clra.rowing.remote.AllTests.suite() );

    theLog.debug("adding web.AllTests ...");
    suite.addTest( test.clra.web.AllTests.suite() );

    theLog.debug("finished building clra.Alltests");
    return suite;
  }

} // AllTests

/*
 * $Log: AllTests.java,v $
 * Revision 1.6  2003/03/04 02:31:34  rphall
 * Added JUnit tests for MembershipServlet
 *
 * Revision 1.5  2003/02/26 03:38:46  rphall
 * Added copyright and GPL license
 *
 * Revision 1.4  2003/02/19 03:33:40  rphall
 * Add tests from member package
 *
 * Revision 1.3  2003/02/15 04:31:42  rphall
 * Changes connected to major revision of MemberBean
 *
 * Revision 1.2  2002/02/18 18:07:45  rphall
 * Ran dos2unix to remove ^M (carriage return) from end of lines
 *
 * Revision 1.1.1.1  2002/01/03 21:57:28  rphall
 * Initial load, 5th try, Jan-03-2002 4:57 PM
 *
 * Revision 1.1  2001/11/30 11:43:41  rphall
 * First working tests
 *
 */

