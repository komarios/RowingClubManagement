/*
 * Copyright (c) Carnegie Lake Rowing Association 2002. All rights reserved.
 * Distributed under the GPL license. See doc/COPYING.
 * $RCSfile: Log4jConfigurator.java,v $
 * $Date: 2003/02/26 03:38:46 $
 * $Revision: 1.3 $
 */

/*
 * $Id: Log4jConfigurator.java,v 1.3 2003/02/26 03:38:46 rphall Exp $
 */

package test;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import junit.framework.TestCase;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Category;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.helpers.Loader;

public class Log4jConfigurator extends TestCase {

  private final static String _LOG4J_URL =
      "test/log4j.properties";

  private static boolean isConfigured = false;

  private static URL url = null;

  static {

    // If not configured, check whether the log4.configuration file of this
    // class exists. If so, use it.
    if ( !isConfigured ) {
      url = Loader.getResource( _LOG4J_URL, Log4jConfigurator.class );
      if ( url != null ) {
        System.out.println("log4j URL == " + url);
        PropertyConfigurator.configure( url );
        System.out.println("Finished static configuration of log4j");
        isConfigured = true;
      }
      else {
        System.out.println("No static configuration of log4j");
      }
    }

  } // static initializer

  // The logger used by this class
  private final static Category theLog =
      Category.getInstance(Log4jConfigurator.class);

  protected void setUp() {
    
    if ( !isConfigured && url != null ) {
      PropertyConfigurator.configure( url );
      isConfigured = true;
    }
    if ( !isConfigured ) {
      BasicConfigurator.configure();
    }

  } // setup

  public Log4jConfigurator( String name ) {
    super(name);
  }

  public void testLoggingPrerequisite() {
    assertTrue( url != null );
    //assertTrue( theLog.getAllAppenders().hasMoreElements() );
    theLog.debug("Test of log4j configuration");
  }

} // Log4jConfigurator

/*
 * $Log: Log4jConfigurator.java,v $
 * Revision 1.3  2003/02/26 03:38:46  rphall
 * Added copyright and GPL license
 *
 * Revision 1.2  2002/02/18 18:08:37  rphall
 * Ran dos2unix to remove ^M (carriage return) from end of lines
 *
 * Revision 1.1.1.1  2002/01/03 21:57:28  rphall
 * Initial load, 5th try, Jan-03-2002 4:57 PM
 *
 * Revision 1.1  2001/11/30 11:43:41  rphall
 * First working tests
 *
 */

