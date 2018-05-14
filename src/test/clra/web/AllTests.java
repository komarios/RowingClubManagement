/*
 * Copyright (c) Carnegie Lake Rowing Association 2002. All rights reserved.
 * Distributed under the GPL license. See doc/COPYING.
 * $RCSfile: AllTests.java,v $
 * $Date: 2003/03/04 02:18:44 $
 * $Revision: 1.1 $
 */

package test.clra.web;

import junit.framework.Test;
import junit.framework.TestSuite;
import org.apache.log4j.Category;

/**
 * @version $Revision: 1.1 $ $Date: 2003/03/04 02:18:44 $
 */
public class AllTests {

  // The general logger used by most methods
  private final static String baseCat = AllTests.class.getName();
  private final static Category theLog = Category.getInstance( baseCat );

  public static Test suite() {

    theLog.debug("building web.AllTests ...");
    TestSuite suite = new TestSuite("All tests for test.clra.web");

    theLog.debug("adding web.Test_MembershipServlet ...");
    suite.addTest( new TestSuite(Test_MembershipServlet.class) );

    theLog.debug("finished building web.Alltests");
    return suite;
  }

} // AllTests

/*
 * $Log: AllTests.java,v $
 * Revision 1.1  2003/03/04 02:18:44  rphall
 * Test com/clra/web functionality
 *
 */

