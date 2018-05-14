/*
 * Copyright (c) Carnegie Lake Rowing Association 2002. All rights reserved.
 * Distributed under the GPL license. See doc/COPYING.
 * $RCSfile: AllTests.java,v $
 * $Date: 2003/02/26 03:38:47 $
 * $Revision: 1.2 $
 */

package test.clra.member.remote;

import junit.framework.Test;
import junit.framework.TestSuite;
import org.apache.log4j.Category;

/**
 * @version $Revision: 1.2 $ $Date: 2003/02/26 03:38:47 $
 */
public class AllTests {

  // The general logger used by most methods
  private final static String baseCat = AllTests.class.getName();
  private final static Category theLog = Category.getInstance( baseCat );

  public static Test suite() {

    theLog.debug("building member.remote.AllTests ...");
    TestSuite suite =
            new TestSuite("All tests for test.clra.member.remote");

    theLog.debug("adding member.remote.Test_MemberEJBHome ...");
    suite.addTest( new TestSuite(Test_MemberEJBHome.class) );

    // theLog.debug("adding member.remote.Test_MemberEJB ...");
    // suite.addTest( new TestSuite(Test_MemberEJB.class) );

    theLog.debug("finished building member.remote.Alltests");
    return suite;
  }

} // AllTests

/*
 * $Log: AllTests.java,v $
 * Revision 1.2  2003/02/26 03:38:47  rphall
 * Added copyright and GPL license
 *
 * Revision 1.1  2003/02/19 03:41:40  rphall
 * Working with unit tests
 *
 */

