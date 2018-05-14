/*
 * Copyright (c) Carnegie Lake Rowing Association 2002. All rights reserved.
 * Distributed under the GPL license. See doc/COPYING.
 * $RCSfile: AllTestsLog4jConfig.java,v $
 * $Date: 2003/02/26 03:38:47 $
 * $Revision: 1.3 $
 */

package test.clra.rowing;

import junit.framework.Test;
import junit.framework.TestSuite;
import test.Log4jConfigurator;

/**
 * @version $Revision: 1.3 $ $Date: 2003/02/26 03:38:47 $
 */
public class AllTestsLog4jConfig {

  public static Test suite() {

    // System.out.println("building rowing.AllTestsLog4jConfig ...");
    TestSuite suite = new TestSuite(
        "All tests for test.clra.rowing plus log4j configuration" );

    // System.out.println("adding Log4jConfigurator ...");
    suite.addTest( new TestSuite(Log4jConfigurator.class) );

    // System.out.println("adding rowing.AllTests ...");
    suite.addTest( AllTests.suite() );

    // System.out.println("finished building rowing.AlltestsLog4jConfig");
    return suite;
  }

} // AllTestsLog4jConfig

/*
 * $Log: AllTestsLog4jConfig.java,v $
 * Revision 1.3  2003/02/26 03:38:47  rphall
 * Added copyright and GPL license
 *
 * Revision 1.2  2002/02/18 18:08:24  rphall
 * Ran dos2unix to remove ^M (carriage return) from end of lines
 *
 * Revision 1.1.1.1  2002/01/03 21:57:28  rphall
 * Initial load, 5th try, Jan-03-2002 4:57 PM
 *
 * Revision 1.1  2001/11/30 11:43:41  rphall
 * First working tests
 *
 */

