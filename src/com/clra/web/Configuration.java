/*
 * Copyright (c) Carnegie Lake Rowing Association 2002. All rights reserved.
 * Distributed under the GPL license. See doc/COPYING.
 * $RCSfile: Configuration.java,v $
 * $Date: 2003/03/04 02:35:32 $
 * $Revision: 1.6 $
 */

package com.clra.web;

import com.clra.util.ConfigurationException;
import com.clra.util.DBConfiguration;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;
import org.apache.log4j.Category;
import org.apache.log4j.helpers.Loader;

/**
 * A collection of configurable properties used by this package.
 * @version $Id: Configuration.java,v 1.6 2003/03/04 02:35:32 rphall Exp $
 * @author <a href="mailto:rphall@pluto.njcc.com">Rick Hall</a>
 */
public class Configuration {

  /** All methods are static */
  private Configuration() {}

  private final static String base = Configuration.class.getName();

  private final static Category theLog = Category.getInstance( base );

  /** Name of the property that holds the default Member key */
  public final static String PN_KEY_MEMBER = "membertag.key";

  /** Name of the property that holds the default MemberTag property */
  public final static String PN_MEMBER_PROPERTY = "membertag.property";

  /** Name of the property that holds the value for the assn's first year */
  public final static String PN_ACCOUNT_FIRSTYEAR =
    "validate.member.account.firstYear";

  /** Name of the property that holds the value of the SOAP server URL */
  public final static String PN_SOAP_SERVER_URL = "membership.soap.url";

  /** Boat SQL property-name prefix */
  public final static String PN_PREFIX_SQL_BOAT = "boatset.sql.";

  /** All Boats */
  public final static String PN_SQL_BOAT_01 =
          PN_PREFIX_SQL_BOAT + "01." + DBConfiguration.DBTYPE;

  /** Specific Boat by id */
  public final static String PN_SQL_BOAT_02 =
          PN_PREFIX_SQL_BOAT + "02." + DBConfiguration.DBTYPE;

  /** Specific Boat by name */
  public final static String PN_SQL_BOAT_03 =
          PN_PREFIX_SQL_BOAT + "03." + DBConfiguration.DBTYPE;

  /** Oarset SQL property-name prefix */
  public final static String PN_PREFIX_SQL_OARSET = "oarsetset.sql.";

  /** All Oarsets */
  public final static String PN_SQL_OARSET_01 =
          PN_PREFIX_SQL_OARSET + "01." + DBConfiguration.DBTYPE;

  /** Specific Oarset by id */
  public final static String PN_SQL_OARSET_02 =
          PN_PREFIX_SQL_OARSET + "02." + DBConfiguration.DBTYPE;

  /** Specific Oarset by name */
  public final static String PN_SQL_OARSET_03 =
          PN_PREFIX_SQL_OARSET + "03." + DBConfiguration.DBTYPE;

  /** Name of file that holds default values for this package */
  private final static String DEFAULTS_FILE =
          "com/clra/web/web.properties";

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

  /** Default Member key */
  private static String DEFAULT_KEY_MEMBER = null;
  public static String KEY_MEMBER = null;
  static {
    DEFAULT_KEY_MEMBER = getDefaultProperty( PN_KEY_MEMBER );
    theLog.info( "DEFAULT_KEY_MEMBER == '" + DEFAULT_KEY_MEMBER + "'" );
    KEY_MEMBER = getProperty( PN_KEY_MEMBER, DEFAULT_KEY_MEMBER );
    theLog.info( "KEY_MEMBER == '" + KEY_MEMBER + "'" );
  } // static

  /** Default MemberTag property */
  private static String DEFAULT_MEMBER_PROPERTY = null;
  public static String MEMBER_PROPERTY = null;
  static {
    DEFAULT_MEMBER_PROPERTY = getDefaultProperty( PN_MEMBER_PROPERTY );
    theLog.info("DEFAULT_MEMBER_PROPERTY == '" + DEFAULT_MEMBER_PROPERTY + "'");
    MEMBER_PROPERTY = getProperty(PN_MEMBER_PROPERTY, DEFAULT_MEMBER_PROPERTY );
    theLog.info( "MEMBER_PROPERTY == '" + MEMBER_PROPERTY + "'" );
  } // static

  /** The rowing association's first year */
  public static String DEFAULT_ACCOUNT_FIRSTYEAR = null;
  public static String ACCOUNT_FIRSTYEAR = null;
  static {
    DEFAULT_ACCOUNT_FIRSTYEAR = getDefaultProperty( PN_ACCOUNT_FIRSTYEAR );
    theLog.info("DEFAULT_ACCT_FIRSTYEAR == '" +DEFAULT_ACCOUNT_FIRSTYEAR+ "'");
    ACCOUNT_FIRSTYEAR =
      getProperty(PN_ACCOUNT_FIRSTYEAR, DEFAULT_ACCOUNT_FIRSTYEAR );
    theLog.info( "ACCT_FIRSTYEAR == '" + ACCOUNT_FIRSTYEAR + "'" );
  } // static

  /** The URL of the membership SOAP server */
  public static String DEFAULT_SOAP_SERVER_URL = null;
  public static String SOAP_SERVER_URL = null;
  static {
    DEFAULT_SOAP_SERVER_URL = getDefaultProperty( PN_SOAP_SERVER_URL );
    theLog.info("DEFAULT_ACCT_FIRSTYEAR == '" +DEFAULT_SOAP_SERVER_URL+ "'");
    SOAP_SERVER_URL =
      getProperty(PN_SOAP_SERVER_URL, DEFAULT_SOAP_SERVER_URL );
    theLog.info( "ACCT_FIRSTYEAR == '" + SOAP_SERVER_URL + "'" );
  } // static

  /** Default SQL that selects up all boats */
  private static String DEFAULT_SQL_BOAT_01 = null;
  static {
    DEFAULT_SQL_BOAT_01 = getDefaultProperty( PN_SQL_BOAT_01 );
    theLog.info( "DEFAULT_SQL_BOAT_01 == '" + DEFAULT_SQL_BOAT_01 + "'" );
  } // static

  /** SQL that selects all boats */
  public static String SQL_BOAT_01 = null;
  static {
    SQL_BOAT_01 = getProperty( PN_SQL_BOAT_01, DEFAULT_SQL_BOAT_01 );
    theLog.info( "SQL_BOAT_01 == '" + SQL_BOAT_01 + "'" );
  } // static

  /** Default SQL that selects a boat by id */
  private static String DEFAULT_SQL_BOAT_02 = null;
  static {
    DEFAULT_SQL_BOAT_02 = getDefaultProperty( PN_SQL_BOAT_02 );
    theLog.info( "DEFAULT_SQL_BOAT_02 == '" + DEFAULT_SQL_BOAT_02 + "'" );
  } // static

  /** SQL that selects a boat by id */
  public static String SQL_BOAT_02 = null;
  static {
    SQL_BOAT_02 = getProperty( PN_SQL_BOAT_02, DEFAULT_SQL_BOAT_02 );
    theLog.info( "SQL_BOAT_02 == '" + SQL_BOAT_02 + "'" );
  } // static

  /** Default SQL that selects a boat by name */
  private static String DEFAULT_SQL_BOAT_03 = null;
  static {
    DEFAULT_SQL_BOAT_03 = getDefaultProperty( PN_SQL_BOAT_03 );
    theLog.info( "DEFAULT_SQL_BOAT_03 == '" + DEFAULT_SQL_BOAT_03 + "'" );
  } // static

  /** SQL that selects a boat by name */
  public static String SQL_BOAT_03 = null;
  static {
    SQL_BOAT_03 = getProperty( PN_SQL_BOAT_02, DEFAULT_SQL_BOAT_03 );
    theLog.info( "SQL_BOAT_03 == '" + SQL_BOAT_03 + "'" );
  } // static

  /** Default SQL that selects up all oarsets */
  private static String DEFAULT_SQL_OARSET_01 = null;
  static {
    DEFAULT_SQL_OARSET_01 = getDefaultProperty( PN_SQL_OARSET_01 );
    theLog.info( "DEFAULT_SQL_OARSET_01 == '" + DEFAULT_SQL_OARSET_01 + "'" );
  } // static

  /** SQL that selects all oarsets */
  public static String SQL_OARSET_01 = null;
  static {
    SQL_OARSET_01 = getProperty( PN_SQL_OARSET_01, DEFAULT_SQL_OARSET_01 );
    theLog.info( "SQL_OARSET_01 == '" + SQL_OARSET_01 + "'" );
  } // static

  /** Default SQL that selects a oarset by id */
  private static String DEFAULT_SQL_OARSET_02 = null;
  static {
    DEFAULT_SQL_OARSET_02 = getDefaultProperty( PN_SQL_OARSET_02 );
    theLog.info( "DEFAULT_SQL_OARSET_02 == '" + DEFAULT_SQL_OARSET_02 + "'" );
  } // static

  /** SQL that selects a oarset by id */
  public static String SQL_OARSET_02 = null;
  static {
    SQL_OARSET_02 = getProperty( PN_SQL_OARSET_02, DEFAULT_SQL_OARSET_02 );
    theLog.info( "SQL_OARSET_02 == '" + SQL_OARSET_02 + "'" );
  } // static

  /** Default SQL that selects a oarset by name */
  private static String DEFAULT_SQL_OARSET_03 = null;
  static {
    DEFAULT_SQL_OARSET_03 = getDefaultProperty( PN_SQL_OARSET_03 );
    theLog.info( "DEFAULT_SQL_OARSET_03 == '" + DEFAULT_SQL_OARSET_03 + "'" );
  } // static

  /** SQL that selects a oarset by name */
  public static String SQL_OARSET_03 = null;
  static {
    SQL_OARSET_03 = getProperty( PN_SQL_OARSET_02, DEFAULT_SQL_OARSET_03 );
    theLog.info( "SQL_OARSET_03 == '" + SQL_OARSET_03 + "'" );
  } // static

} // Configuration

/*
 * $Log: Configuration.java,v $
 * Revision 1.6  2003/03/04 02:35:32  rphall
 * Add property for membership SOAP url
 *
 * Revision 1.5  2003/02/26 03:38:46  rphall
 * Added copyright and GPL license
 *
 * Revision 1.4  2003/02/19 22:09:17  rphall
 * Removed gratuitous use of CLRA acronym
 *
 * Revision 1.3  2003/02/19 20:50:31  rphall
 * Moved validation values to Configuration class and web.properties file
 */

