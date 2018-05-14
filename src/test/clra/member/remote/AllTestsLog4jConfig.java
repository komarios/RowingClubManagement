/*
 * Copyright (c) Carnegie Lake Rowing Association 2002. All rights reserved.
 * Distributed under the GPL license. See doc/COPYING.
 * $RCSfile: AllTestsLog4jConfig.java,v $
 * $Date: 2003/03/04 02:32:46 $
 * $Revision: 1.3 $
 */

package test.clra.member.remote;

import junit.framework.Test;
import junit.framework.TestSuite;
import test.Log4jConfigurator;

/**
 * @version $Revision: 1.3 $ $Date: 2003/03/04 02:32:46 $
 */
public class AllTestsLog4jConfig {

  public static Test suite() {

    //System.out.println("building member.remote.AllTestsLog4jConfig ...");
    TestSuite suite = new TestSuite(
        "All tests for test.clra.member.remote plus log4j configuration" );

    //System.out.println("adding Log4jConfigurator ...");
    suite.addTest( new TestSuite(Log4jConfigurator.class) );

    //System.out.println("adding member.remote.AllTests ...");
    suite.addTest( AllTests.suite() );

    //System.out.println("finished building member.remote.AlltestsLog4jConfig");
    return suite;
  }

} // AllTestsLog4jConfig

/*
 * $Log: AllTestsLog4jConfig.java,v $
 * Revision 1.3  2003/03/04 02:32:46  rphall
 * Fixed comments
 *
 * Revision 1.2  2003/02/26 03:38:47  rphall
 * Added copyright and GPL license
 *
 * Revision 1.1  2003/02/19 03:41:40  rphall
 * Working with unit tests
 *
 */

