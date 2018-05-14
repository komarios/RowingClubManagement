/*
 * Copyright (c) Carnegie Lake Rowing Association 2002. All rights reserved.
 * Distributed under the GPL license. See doc/COPYING.
 * $RCSfile: AllTestsLog4jConfig.java,v $
 * $Date: 2003/03/04 02:18:44 $
 * $Revision: 1.1 $
 */

package test.clra.web;

import junit.framework.Test;
import junit.framework.TestSuite;
import test.Log4jConfigurator;

/**
 * @version $Revision: 1.1 $ $Date: 2003/03/04 02:18:44 $
 */
public class AllTestsLog4jConfig {

  public static Test suite() {

    //System.out.println("building web.AllTestsLog4jConfig ...");
    TestSuite suite = new TestSuite(
        "All tests for test.clra.web plus log4j configuration" );

    //System.out.println("adding Log4jConfigurator ...");
    suite.addTest( new TestSuite(Log4jConfigurator.class) );

    //System.out.println("adding web.AllTests ...");
    suite.addTest( AllTests.suite() );

    //System.out.println("finished building web.AlltestsLog4jConfig");
    return suite;
  }

} // AllTestsLog4jConfig

/*
 * $Log: AllTestsLog4jConfig.java,v $
 * Revision 1.1  2003/03/04 02:18:44  rphall
 * Test com/clra/web functionality
 *
 */

