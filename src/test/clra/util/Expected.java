/*
 * Copyright (c) Carnegie Lake Rowing Association 2002. All rights reserved.
 * Distributed under the GPL license. See doc/COPYING.
 * $RCSfile: Expected.java,v $
 * $Date: 2003/02/26 03:38:47 $
 * $Revision: 1.7 $
 */

package test.clra.util;

import java.io.InputStream;
import java.net.URL;
import java.util.Properties;
import org.apache.log4j.Category;
import org.apache.log4j.helpers.Loader;

/**
 * A collection of configurable properties used by this package.
 *
 * @version $Revision: 1.7 $ $Date: 2003/02/26 03:38:47 $
 * @author <a href="mailto:rphall@pluto.njcc.com">Rick Hall</a>
 */
public class Expected {

  /** All methods are static */
  private Expected() {}

  private final static String base = Expected.class.getName();
  private final static Category theLog = Category.getInstance( base );

  public final static String PN_ROWINGSESSION_FIRST =
          "database.rowingsession.first";

  public final static String PN_ROWINGSESSION_LAST =
          "database.rowingsession.last";

  public final static String PN_ROWINGSESSION_COUNT =
          "database.rowingsession.count";

  public final static String PN_ROWINGSESSION_OCT_COUNT =
          "database.rowingsession.oct.count";

  public final static String PN_PARTICIPANT_FIRST =
          "database.participant.first";

  public final static String PN_PARTICIPANT_LAST =
          "database.participant.last";

  public final static String PN_PARTICIPANT_COUNT =
          "database.participant.count";

  public final static String PN_PARTICIPANT_MIN =
          "database.participant.min";

  public final static String PN_MEMBER_COUNT =
          "database.member.count";

  public final static String PN_MEMBER_MIN =
          "database.member.min";

  private final static String DEFAULTS_FILE =
          "test/clra/util/expected.properties";

  private final static Properties defaultProperties = new Properties();
  static {
    InputStream is = null;
    try {
      URL url = Loader.getResource( DEFAULTS_FILE, Expected.class );
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

  private static Integer _ROWINGSESSION_FIRST = null;
  static {
    try {
      String tmp = defaultProperties.getProperty( PN_ROWINGSESSION_FIRST );
      _ROWINGSESSION_FIRST = new Integer( tmp.trim() );
    }
    catch( Exception x ) {
      theLog.error(x.getMessage(),x);
    }
    theLog.info( "ROWINGSESSION_FIRST == '" + _ROWINGSESSION_FIRST + "'" );
  } // static

  public static Integer ROWINGSESSION_FIRST() {
    return _ROWINGSESSION_FIRST;
  }

  private static Integer _ROWINGSESSION_LAST = null;
  static {
    try {
      String tmp = defaultProperties.getProperty( PN_ROWINGSESSION_LAST );
      _ROWINGSESSION_LAST = new Integer( tmp.trim() );
    }
    catch( Exception x ) {
      theLog.error(x.getMessage(),x);
    }
    theLog.info( "ROWINGSESSION_LAST == '" + _ROWINGSESSION_LAST + "'" );
  } // static

  public static Integer ROWINGSESSION_LAST() {
    return _ROWINGSESSION_LAST;
  }

  private static Integer _ROWINGSESSION_COUNT = null;
  static {
    try {
      String tmp = defaultProperties.getProperty( PN_ROWINGSESSION_COUNT );
      _ROWINGSESSION_COUNT = new Integer( tmp.trim() );
    }
    catch( Exception x ) {
      theLog.error(x.getMessage(),x);
    }
    theLog.info( "ROWINGSESSION_COUNT == '" + _ROWINGSESSION_COUNT + "'" );
  } // static

  public static Integer ROWINGSESSION_COUNT() {
    return _ROWINGSESSION_COUNT;
  }

  private static Integer _ROWINGSESSION_OCT_COUNT = null;
  static {
    try {
      String tmp = defaultProperties.getProperty( PN_ROWINGSESSION_OCT_COUNT );
      _ROWINGSESSION_OCT_COUNT = new Integer( tmp.trim() );
    }
    catch( Exception x ) {
      theLog.error(x.getMessage(),x);
    }
    theLog.info( "ROWINGSESSION_OCT_COUNT == '" + _ROWINGSESSION_OCT_COUNT + "'" );
  } // static

  public static Integer ROWINGSESSION_OCT_COUNT() {
    return _ROWINGSESSION_OCT_COUNT;
  }

  private static Integer _PARTICIPANT_FIRST = null;
  static {
    try {
      String tmp = defaultProperties.getProperty( PN_PARTICIPANT_FIRST );
      _PARTICIPANT_FIRST = new Integer( tmp.trim() );
    }
    catch( Exception x ) {
      theLog.error(x.getMessage(),x);
    }
    theLog.info( "PARTICIPANT_FIRST == '" + _PARTICIPANT_FIRST + "'" );
  } // static

  public static Integer PARTICIPANT_FIRST() {
    return _PARTICIPANT_FIRST;
  }

  private static Integer _PARTICIPANT_LAST = null;
  static {
    try {
      String tmp = defaultProperties.getProperty( PN_PARTICIPANT_LAST );
      _PARTICIPANT_LAST = new Integer( tmp.trim() );
    }
    catch( Exception x ) {
      theLog.error(x.getMessage(),x);
    }
    theLog.info( "PARTICIPANT_LAST == '" + _PARTICIPANT_LAST + "'" );
  } // static

  public static Integer PARTICIPANT_LAST() {
    return _PARTICIPANT_LAST;
  }

  private static Integer _PARTICIPANT_COUNT = null;
  static {
    try {
      String tmp = defaultProperties.getProperty( PN_PARTICIPANT_COUNT );
      _PARTICIPANT_COUNT = new Integer( tmp.trim() );
    }
    catch( Exception x ) {
      theLog.error(x.getMessage(),x);
    }
    theLog.info( "PARTICIPANT_COUNT == '" + _PARTICIPANT_COUNT + "'" );
  } // static

  public static Integer PARTICIPANT_COUNT() {
    return _PARTICIPANT_COUNT;
  }

  private static Integer _PARTICIPANT_MIN = null;
  static {
    try {
      String tmp = defaultProperties.getProperty( PN_PARTICIPANT_MIN );
      _PARTICIPANT_MIN = new Integer( tmp.trim() );
    }
    catch( Exception x ) {
      theLog.error(x.getMessage(),x);
    }
    theLog.info( "PARTICIPANT_MIN == '" + _PARTICIPANT_MIN + "'" );
  } // static

  public static Integer PARTICIPANT_MIN() {
    return _PARTICIPANT_MIN;
  }

  private static Integer _MEMBER_COUNT = null;
  static {
    try {
      String tmp = defaultProperties.getProperty( PN_MEMBER_COUNT );
      _MEMBER_COUNT = new Integer( tmp.trim() );
    }
    catch( Exception x ) {
      theLog.error(x.getMessage(),x);
    }
    theLog.info( "MEMBER_COUNT == '" + _MEMBER_COUNT + "'" );
  } // static

  public static Integer MEMBER_COUNT() {
    return _MEMBER_COUNT;
  }

  private static Integer _MEMBER_MIN = null;
  static {
    try {
      String tmp = defaultProperties.getProperty( PN_MEMBER_MIN );
      _MEMBER_MIN = new Integer( tmp.trim() );
    }
    catch( Exception x ) {
      theLog.error(x.getMessage(),x);
    }
    theLog.info( "MEMBER_MIN == '" + _MEMBER_MIN + "'" );
  } // static

  public static Integer MEMBER_MIN() {
    return _MEMBER_MIN;
  }

} // Expected

/*
 * $Log: Expected.java,v $
 * Revision 1.7  2003/02/26 03:38:47  rphall
 * Added copyright and GPL license
 *
 * Revision 1.6  2003/02/19 22:09:21  rphall
 * Removed gratuitous use of CLRA acronym
 *
 * Revision 1.5  2003/02/15 04:31:42  rphall
 * Changes connected to major revision of MemberBean
 */

