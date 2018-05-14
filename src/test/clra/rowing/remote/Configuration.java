/*
 * Copyright (c) Carnegie Lake Rowing Association 2002. All rights reserved.
 * Distributed under the GPL license. See doc/COPYING.
 * $RCSfile: Configuration.java,v $
 * $Date: 2003/02/26 03:38:47 $
 * $Revision: 1.4 $
 */

package test.clra.rowing.remote;

import java.io.InputStream;
import java.net.URL;
import java.util.Properties;
import org.apache.log4j.Category;
import org.apache.log4j.helpers.Loader;

/**
 * A collection of configurable properties used by this package.
 *
 * @version $Revision: 1.4 $ $Date: 2003/02/26 03:38:47 $
 * @author <a href="mailto:rphall@pluto.njcc.com">Rick Hall</a>
 */
public class Configuration {

  /** All methods are static */
  private Configuration() {}

  private final static String base = Configuration.class.getName();
  private final static Category theLog = Category.getInstance( base );

  public final static String PN_ROWINGSESSION_HOME =
          "rowingsession.home";

  public final static String PN_PARTICIPANT_HOME =
          "participant.home";

  private final static String DEFAULTS_FILE =
          "test/clra/rowing/remote/remote.properties";

  private final static Properties defaultProperties = new Properties();
  static {
    InputStream is = null;
    try {
      URL url = Loader.getResource( DEFAULTS_FILE, Configuration.class );
      is = url.openStream();
      defaultProperties.load( is );
      if ( theLog.isDebugEnabled() ) {
        theLog.debug( "loaded properties from '" + DEFAULTS_FILE + "'" );
        java.util.Enumeration _e = defaultProperties.propertyNames();
        while ( _e.hasMoreElements() ) {
          theLog.debug( "property: " + _e.nextElement() );
        }
      }
    }
    catch( Exception x ){
      String msg = "unable to load default properties from '"
              + DEFAULTS_FILE + "'";
      theLog.fatal(msg,x);
      throw new IllegalStateException( msg );
    }
    finally {
      if ( is != null ) {
        try { is.close(); } catch( Exception x ) {}
        is = null;
      }
    } // finally
  } // static

  private static String _ROWINGSESSION_HOME = null;
  static {
    try {
      String tmp = defaultProperties.getProperty( PN_ROWINGSESSION_HOME );
      _ROWINGSESSION_HOME = tmp.trim();
    }
    catch( Exception x ) {
      theLog.error(x.getMessage(),x);
      throw new IllegalStateException( x.getMessage() );
    }
    theLog.info( "ROWINGSESSION_HOME == '" + _ROWINGSESSION_HOME + "'" );
    if ( _ROWINGSESSION_HOME.length() == 0 ) {
      String msg =  "invalid '" + PN_ROWINGSESSION_HOME + "'";
      throw new IllegalArgumentException( msg );
    }
  } // static

  public static String ROWINGSESSION_HOME() {
    return _ROWINGSESSION_HOME;
  }

  private static String _PARTICIPANT_HOME = null;
  static {
    try {
      String tmp = defaultProperties.getProperty( PN_PARTICIPANT_HOME );
      _PARTICIPANT_HOME = tmp.trim();
    }
    catch( Exception x ) {
      theLog.error(x.getMessage(),x);
      throw new IllegalStateException( x.getMessage() );
    }
    theLog.info( "PARTICIPANT_HOME == '" + _PARTICIPANT_HOME + "'" );
    if ( _PARTICIPANT_HOME.length() == 0 ) {
      String msg =  "invalid '" + PN_PARTICIPANT_HOME + "'";
      throw new IllegalArgumentException( msg );
    }
  } // static

  public static String PARTICIPANT_HOME() {
    return _PARTICIPANT_HOME;
  }

} // Configuration

/*
 * $Log: Configuration.java,v $
 * Revision 1.4  2003/02/26 03:38:47  rphall
 * Added copyright and GPL license
 *
 * Revision 1.3  2003/02/19 22:09:19  rphall
 * Removed gratuitous use of CLRA acronym
 *
 * Revision 1.2  2002/02/18 18:07:56  rphall
 * Ran dos2unix to remove ^M (carriage return) from end of lines
 */

