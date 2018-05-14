/*
 * Copyright (c) Carnegie Lake Rowing Association 2002. All rights reserved.
 * Distributed under the GPL license. See doc/COPYING.
 * $RCSfile: Configuration.java,v $
 * $Date: 2003/02/26 03:38:46 $
 * $Revision: 1.6 $
 */

package com.clra.visitor;

import com.clra.util.ConfigurationException;
import com.clra.util.DBConfiguration;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;
import org.apache.log4j.Category;
import org.apache.log4j.helpers.Loader;

/**
 * A collection of configurable properties used by this package.
 *
 * @author <a href="mailto:donaldzhu@sympatico.ca">Angela Yue</a>
 * @version $Revision: 1.6 $ $Date: 2003/02/26 03:38:46 $
 */

public class Configuration {

  /** All methods are static */
  private Configuration() {}

  private final static String base = Configuration.class.getName();
  private final static Category theLog = Category.getInstance( base );

  /** Session SQL property-name prefix */
  public final static String PN_PREFIX_SQL_SESSION = "visitor.sql.";

  /** SQL date format spec */
  public final static String PN_SQL_DATE_FORMAT =
          PN_PREFIX_SQL_SESSION + "dateformat." + DBConfiguration.DBTYPE;

  public final static String PN_PREFIX_SQL_01 = "visitor.sql.01.";

  public final static String PN_SQL_01 =
          PN_PREFIX_SQL_01 + DBConfiguration.DBTYPE;

  public final static String PN_PREFIX_SQL_02 = "visitor.sql.02.";

  public final static String PN_SQL_02 =
          PN_PREFIX_SQL_02 + DBConfiguration.DBTYPE;

  public final static String PN_PREFIX_SQL_03 = "visitor.sql.03.";

  public final static String PN_SQL_03 =
          PN_PREFIX_SQL_03 + DBConfiguration.DBTYPE;

  public final static String PN_PREFIX_SQL_04 = "visitor.sql.04.";

  public final static String PN_SQL_04 =
          PN_PREFIX_SQL_04 + DBConfiguration.DBTYPE;

  public final static String PN_PREFIX_SQL_05 = "visitor.sql.05.";

  public final static String PN_SQL_05 =
          PN_PREFIX_SQL_05 + DBConfiguration.DBTYPE;          

  public final static String PN_PREFIX_SQL_06 = "visitor.sql.06.";

  public final static String PN_SQL_06 =
          PN_PREFIX_SQL_06 + DBConfiguration.DBTYPE;          

  /** Property that holds the JNDI location of the ApplyMemberShip factory */
  public final static String PN_APPLYMEMBERSHIP_HOME =
          "applymembership.home";

  /** Name of file that holds default values for this package */
  private final static String DEFAULTS_FILE =
          "com/clra/visitor/visitor.properties";

  /** Default properties for this package */
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

  /** Utility that looks up a default property by name */
  private static String getDefaultProperty( String PN ) {
    // Precondition
    if ( PN == null || PN.trim().length()== 0 ) {
      throw new IllegalArgumentException( "invalid property name" );
    }

    String retVal = defaultProperties.getProperty( PN );
    if ( retVal == null || retVal.trim().length() == 0 ) {
      String msg = "invalid or missing value for '" + PN + "'";
      theLog.error(msg);
      retVal = null;
    }
    else {
      retVal = retVal.trim();
    }

    return retVal;
  } // getDefaultProperty(String)

  /** Utility that looks up a System property or assigns a default value */
  private static String getProperty( String PN, String DEFAULT ) {
    // Preconditions
    //theLog.debug( "PN == '" + PN + "'" );
    //theLog.debug( "DEFAULT == '" + DEFAULT + "'" );
    if ( PN == null || PN.trim().length() == 0 ) {
      throw new IllegalArgumentException( "invalid property name" );
    }
    if ( DEFAULT != null && DEFAULT.trim().length() == 0 ) {
      DEFAULT = null;
    }
      
    String retVal = System.getProperty( PN );
    if ( retVal == null || retVal.trim().length() == 0 ) {
      retVal = DEFAULT;
    }
    if ( retVal == null || retVal.trim().length() == 0 ) {
      String msg = "no property for '" + PN + "'";
      theLog.fatal( msg );
      throw new IllegalStateException( msg );
    }
    retVal = retVal.trim();

    return retVal;
  } // getProperty(String,String)

  private static String DEFAULT_SQL_DATE_FORMAT = null;
  public static String SQL_DATE_FORMAT = null;
  static {
    DEFAULT_SQL_DATE_FORMAT = getDefaultProperty( PN_SQL_DATE_FORMAT );
    theLog.info("DEFAULT_SQL_DATE_FORMAT == '" + DEFAULT_SQL_DATE_FORMAT + "'");
    SQL_DATE_FORMAT = getProperty(PN_SQL_DATE_FORMAT, DEFAULT_SQL_DATE_FORMAT);
    theLog.info( "SQL_DATE_FORMAT == '" + SQL_DATE_FORMAT + "'" );
  } // static

  private static String DEFAULT_SQL_01 = null;
  static {
    String tmp = defaultProperties.getProperty( PN_SQL_01 );
    if ( tmp == null || tmp.trim().length() == 0 ) {
      String msg = "invalid or missing value for '" + PN_SQL_01 + "'";
      theLog.error(msg);
    }
    else {
      DEFAULT_SQL_01 = tmp.trim();
    }
    theLog.info( "DEFAULT_SQL_01 == '" + DEFAULT_SQL_01 + "'" );
  } // static

  public static String SQL_01 = null;
  static {
    SQL_01 = System.getProperty( PN_SQL_01 );
    if ( SQL_01 == null || SQL_01.trim().length() == 0 ) {
      SQL_01 = DEFAULT_SQL_01;
    }
    if ( SQL_01 == null || SQL_01.trim().length() == 0 ) {
      String msg = "no property for '" + PN_SQL_01 + "'";
      theLog.fatal( msg );
      throw new IllegalStateException( msg );
    }
    theLog.info( "SQL_01 == '" + SQL_01 + "'" );
  } // static

  public static String SQL_02 = null;
  static {
    String tmp = defaultProperties.getProperty( PN_SQL_02 );
    if ( tmp != null ) {
      tmp = tmp.trim();
    }
    theLog.debug( "DEFAULT_SQL_02 == " + tmp );
    SQL_02 = System.getProperty( PN_SQL_02, tmp );
    if ( SQL_02 == null || SQL_02.trim().length() == 0 ) {
      String msg = "no property for '" + PN_SQL_02 + "'";
      theLog.fatal( msg );
      throw new IllegalStateException( msg );
    }
    theLog.info( "SQL_02 == '" + SQL_02 + "'" );
  } // static

  public static String SQL_03 = null;
  static {
    String tmp = defaultProperties.getProperty( PN_SQL_03 );
    if ( tmp != null ) {
      tmp = tmp.trim();
    }
    theLog.debug( "DEFAULT_SQL_03 == " + tmp );
    SQL_03 = System.getProperty( PN_SQL_03, tmp );
    if ( SQL_03 == null || SQL_03.trim().length() == 0 ) {
      String msg = "no property for '" + PN_SQL_03 + "'";
      theLog.fatal( msg );
      throw new IllegalStateException( msg );
    }
    theLog.info( "SQL_03 == '" + SQL_03 + "'" );
  } // static

  public static String SQL_04 = null;
  static {
    String tmp = defaultProperties.getProperty( PN_SQL_04 );
    if ( tmp != null ) {
      tmp = tmp.trim();
    }
    theLog.debug( "DEFAULT_SQL_04 == " + tmp );
    SQL_04 = System.getProperty( PN_SQL_04, tmp );
    if ( SQL_04 == null || SQL_04.trim().length() == 0 ) {
      String msg = "no property for '" + PN_SQL_04 + "'";
      theLog.fatal( msg );
      throw new IllegalStateException( msg );
    }
    theLog.info( "SQL_04 == '" + SQL_04 + "'" );
  } // static

  public static String SQL_05 = null;
  static {
    String tmp = defaultProperties.getProperty( PN_SQL_05 );
    if ( tmp != null ) {
      tmp = tmp.trim();
    }
    theLog.debug( "DEFAULT_SQL_05 == " + tmp );
    SQL_05 = System.getProperty( PN_SQL_05, tmp );
    if ( SQL_05 == null || SQL_05.trim().length() == 0 ) {
      String msg = "no property for '" + PN_SQL_05 + "'";
      theLog.fatal( msg );
      throw new IllegalStateException( msg );
    }
    theLog.info( "SQL_05 == '" + SQL_05 + "'" );
  } // static

  public static String SQL_06 = null;
  static {
    String tmp = defaultProperties.getProperty( PN_SQL_06 );
    if ( tmp != null ) {
      tmp = tmp.trim();
    }
    theLog.debug( "DEFAULT_SQL_06 == " + tmp );
    SQL_06 = System.getProperty( PN_SQL_06, tmp );
    if ( SQL_06 == null || SQL_06.trim().length() == 0 ) {
      String msg = "no property for '" + PN_SQL_06 + "'";
      theLog.fatal( msg );
      throw new IllegalStateException( msg );
    }
    theLog.info( "SQL_06 == '" + SQL_06 + "'" );
  } // static

  private static String _DEFAULT_APPLYMEMBERSHIP_HOME = null;
  private static String _APPLYMEMBERSHIP_HOME = null;
  static {
    _DEFAULT_APPLYMEMBERSHIP_HOME = getDefaultProperty( PN_APPLYMEMBERSHIP_HOME );
    theLog.info(
        "DEFAULT_APPLYMEMBERSHIP_HOME == '" + _DEFAULT_APPLYMEMBERSHIP_HOME + "'");
    _APPLYMEMBERSHIP_HOME = getProperty(
        PN_APPLYMEMBERSHIP_HOME, _DEFAULT_APPLYMEMBERSHIP_HOME );
    theLog.info( "APPLYMEMBERSHIP_HOME == '" + _APPLYMEMBERSHIP_HOME + "'" );
  } // static

  public static String APPLYMEMBERSHIP_HOME() { return _APPLYMEMBERSHIP_HOME; }

} // Configuration

/*
 * $Log: Configuration.java,v $
 * Revision 1.6  2003/02/26 03:38:46  rphall
 * Added copyright and GPL license
 *
 * Revision 1.5  2003/02/19 22:09:17  rphall
 * Removed gratuitous use of CLRA acronym
 *
 */

