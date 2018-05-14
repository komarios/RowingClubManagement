/*
 * Copyright (c) Carnegie Lake Rowing Association 2002. All rights reserved.
 * Distributed under the GPL license. See doc/COPYING.
 * $RCSfile: Configuration.java,v $
 * $Date: 2003/02/26 03:38:45 $
 * $Revision: 1.12 $
 */

package com.clra.member;

import com.clra.util.DBConfiguration;
import com.clra.util.ConfigurationException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;
import org.apache.log4j.Category;
import org.apache.log4j.helpers.Loader;

/**
 * A collection of configurable properties used by this package.
 * @version $Id: Configuration.java,v 1.12 2003/02/26 03:38:45 rphall Exp $
 * @author <a href="mailto:rphall@pluto.njcc.com">Rick Hall</a>
 */
public class Configuration {

  /** All methods are static */
  private Configuration() {}

  /** A hard-coded JAAS LoginContext == "clra" */
  public final static String LOGIN_CONTEXT = "clra";

  private final static String base = Configuration.class.getName();

  private final static Category theLog = Category.getInstance( base );

  /** Session SQL property-name prefix */
  public final static String PN_PREFIX_SQL_SESSION = "memberset.sql.";

  /** SQL date format spec */
  public final static String PN_SQL_DATE_FORMAT =
          PN_PREFIX_SQL_SESSION + "dateformat." + DBConfiguration.DBTYPE;

  public final static String PN_PREFIX_SQL_01 = "memberset.sql.01.";

  public final static String PN_SQL_01 =
          PN_PREFIX_SQL_01 + DBConfiguration.DBTYPE;

  public final static String PN_PREFIX_SQL_02 = "memberset.sql.02.";

  public final static String PN_SQL_02 =
          PN_PREFIX_SQL_02 + DBConfiguration.DBTYPE;

  public final static String PN_PREFIX_SQL_03 = "memberset.sql.03.";

  public final static String PN_SQL_03 =
          PN_PREFIX_SQL_03 + DBConfiguration.DBTYPE;

  public final static String PN_PREFIX_SQL_04 = "memberset.sql.04.";

  public final static String PN_SQL_04 =
          PN_PREFIX_SQL_04 + DBConfiguration.DBTYPE;

  public final static String PN_PREFIX_SQL_05 = "memberset.sql.05.";

  public final static String PN_SQL_05 =
          PN_PREFIX_SQL_05 + DBConfiguration.DBTYPE;

  public final static String PN_PREFIX_SQL_06 = "memberset.sql.06.";

  public final static String PN_SQL_06 =
          PN_PREFIX_SQL_06 + DBConfiguration.DBTYPE;

  public final static String PN_PREFIX_SQL_07 = "memberset.sql.07.";

  public final static String PN_SQL_07 =
          PN_PREFIX_SQL_07 + DBConfiguration.DBTYPE;

  public final static String PN_PREFIX_SQL_08 = "memberset.sql.08.";

  public final static String PN_SQL_08 =
          PN_PREFIX_SQL_08 + DBConfiguration.DBTYPE;

  public final static String PN_PREFIX_SQL_081 = "memberset.sql.081.";

  public final static String PN_SQL_081 =
          PN_PREFIX_SQL_081 + DBConfiguration.DBTYPE;

  public final static String PN_PREFIX_SQL_09 = "memberset.sql.09.";

  public final static String PN_SQL_09 =
          PN_PREFIX_SQL_09 + DBConfiguration.DBTYPE;
          
  public final static String PN_PREFIX_SQL_10 = "memberset.sql.10.";

  public final static String PN_SQL_10 =
          PN_PREFIX_SQL_10 + DBConfiguration.DBTYPE;
          
  public final static String PN_PREFIX_SQL_11 = "memberset.sql.11.";

  public final static String PN_SQL_11 =
          PN_PREFIX_SQL_11 + DBConfiguration.DBTYPE;
          
  public final static String PN_PREFIX_SQL_12 = "memberset.sql.12.";

  public final static String PN_SQL_12 =
          PN_PREFIX_SQL_12 + DBConfiguration.DBTYPE;
          
  public final static String PN_PREFIX_SQL_15 = "memberset.sql.15.";

  public final static String PN_SQL_15 =
          PN_PREFIX_SQL_15 + DBConfiguration.DBTYPE;
          
  public final static String PN_PREFIX_SQL_15A = "memberset.sql.15a.";

  public final static String PN_SQL_15A =
          PN_PREFIX_SQL_15A + DBConfiguration.DBTYPE;
          
  public final static String PN_PREFIX_SQL_16 = "memberset.sql.16.";

  public final static String PN_SQL_16 =
          PN_PREFIX_SQL_16 + DBConfiguration.DBTYPE;
          
  public final static String PN_PREFIX_SQL_17 = "memberset.sql.17.";

  public final static String PN_SQL_17 =
          PN_PREFIX_SQL_17 + DBConfiguration.DBTYPE;
          
  public final static String PN_PREFIX_SQL_18 = "memberset.sql.18.";

  public final static String PN_SQL_18 =
          PN_PREFIX_SQL_18 + DBConfiguration.DBTYPE;

  public final static String PN_MEMBER_HOME = "member.home";

  private final static String DEFAULTS_FILE =
          "com/clra/member/member.properties";

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

  private static String DEFAULT_SQL_05 = null;
  static {
    String tmp = defaultProperties.getProperty( PN_SQL_05 );
    if ( tmp == null || tmp.trim().length() == 0 ) {
      String msg = "invalid or missing value for '" + PN_SQL_05 + "'";
      theLog.error(msg);
    }
    else {
      DEFAULT_SQL_05 = tmp.trim();
    }
    theLog.info( "DEFAULT_SQL_05 == '" + DEFAULT_SQL_05 + "'" );
  } // static

  public static String SQL_05 = null;
  static {
    SQL_05 = System.getProperty( PN_SQL_05 );
    if ( SQL_05 == null || SQL_05.trim().length() == 0 ) {
      SQL_05 = DEFAULT_SQL_05;
    }
    if ( SQL_05 == null || SQL_05.trim().length() == 0 ) {
      String msg = "no property for '" + PN_SQL_05 + "'";
      theLog.fatal( msg );
      throw new IllegalStateException( msg );
    }
    theLog.info( "SQL_05 == '" + SQL_05 + "'" );
  } // static

  private static String DEFAULT_SQL_06 = null;
  public static String SQL_06 = null;
  static {
    DEFAULT_SQL_06 = defaultProperties.getProperty( PN_SQL_06 );
    theLog.info( "DEFAULT_SQL_06 == '" + DEFAULT_SQL_06 + "'" );
    SQL_06 = System.getProperty( PN_SQL_06, DEFAULT_SQL_06 );
    if ( SQL_06 == null || SQL_06.trim().length() == 0 ) {
      String msg = "no property for '" + PN_SQL_06 + "'";
      theLog.fatal( msg );
      throw new IllegalStateException( msg );
    }
    SQL_06 = SQL_06.trim();
  } // static

  private static String DEFAULT_SQL_07 = null;
  public static String SQL_07 = null;
  static {
    DEFAULT_SQL_07 = defaultProperties.getProperty( PN_SQL_07 );
    theLog.info( "DEFAULT_SQL_07 == '" + DEFAULT_SQL_07 + "'" );
    SQL_07 = System.getProperty( PN_SQL_07, DEFAULT_SQL_07 );
    if ( SQL_07 == null || SQL_07.trim().length() == 0 ) {
      String msg = "no property for '" + PN_SQL_07 + "'";
      theLog.fatal( msg );
      throw new IllegalStateException( msg );
    }
    SQL_07 = SQL_07.trim();
  } // static

  private static String DEFAULT_SQL_08 = null;
  public static String SQL_08 = null;
  static {
    DEFAULT_SQL_08 = defaultProperties.getProperty( PN_SQL_08 );
    theLog.info( "DEFAULT_SQL_08 == '" + DEFAULT_SQL_08 + "'" );
    SQL_08 = System.getProperty( PN_SQL_08, DEFAULT_SQL_08 );
    if ( SQL_08 == null || SQL_08.trim().length() == 0 ) {
      String msg = "no property for '" + PN_SQL_08 + "'";
      theLog.fatal( msg );
      throw new IllegalStateException( msg );
    }
    SQL_08 = SQL_08.trim();
  } // static

  private static String DEFAULT_SQL_081 = null;
  public static String SQL_081 = null;
  static {
    DEFAULT_SQL_081 = defaultProperties.getProperty( PN_SQL_081 );
    theLog.info( "DEFAULT_SQL_081 == '" + DEFAULT_SQL_081 + "'" );
    SQL_081 = System.getProperty( PN_SQL_081, DEFAULT_SQL_081 );
    if ( SQL_081 == null || SQL_081.trim().length() == 0 ) {
      String msg = "no property for '" + PN_SQL_081 + "'";
      theLog.fatal( msg );
      throw new IllegalStateException( msg );
    }
    SQL_081 = SQL_081.trim();
  } // static

  private static String DEFAULT_SQL_09 = null;
  public static String SQL_09 = null;
  static {
    DEFAULT_SQL_09 = defaultProperties.getProperty( PN_SQL_09 );
    theLog.info( "DEFAULT_SQL_09 == '" + DEFAULT_SQL_09 + "'" );
    SQL_09 = System.getProperty( PN_SQL_09, DEFAULT_SQL_09 );
    if ( SQL_09 == null || SQL_09.trim().length() == 0 ) {
      String msg = "no property for '" + PN_SQL_09 + "'";
      theLog.fatal( msg );
      throw new IllegalStateException( msg );
    }
    SQL_09 = SQL_09.trim();
  } // static

  private static String DEFAULT_SQL_10 = null;
  public static String SQL_10 = null;
  static {
    DEFAULT_SQL_10 = defaultProperties.getProperty( PN_SQL_10 );
    theLog.info( "DEFAULT_SQL_10 == '" + DEFAULT_SQL_10 + "'" );
    SQL_10 = System.getProperty( PN_SQL_10, DEFAULT_SQL_10 );
    if ( SQL_10 == null || SQL_10.trim().length() == 0 ) {
      String msg = "no property for '" + PN_SQL_10 + "'";
      theLog.fatal( msg );
      throw new IllegalStateException( msg );
    }
    SQL_10 = SQL_10.trim();
  } // static

  private static String DEFAULT_SQL_11 = null;
  public static String SQL_11 = null;
  static {
    DEFAULT_SQL_11 = defaultProperties.getProperty( PN_SQL_11 );
    theLog.info( "DEFAULT_SQL_11 == '" + DEFAULT_SQL_11 + "'" );
    SQL_11 = System.getProperty( PN_SQL_11, DEFAULT_SQL_11 );
    if ( SQL_11 == null || SQL_11.trim().length() == 0 ) {
      String msg = "no property for '" + PN_SQL_11 + "'";
      theLog.fatal( msg );
      throw new IllegalStateException( msg );
    }
    SQL_11 = SQL_11.trim();
  } // static

  private static String DEFAULT_SQL_12 = null;
  public static String SQL_12 = null;
  static {
    DEFAULT_SQL_12 = defaultProperties.getProperty( PN_SQL_12 );
    theLog.info( "DEFAULT_SQL_12 == '" + DEFAULT_SQL_12 + "'" );
    SQL_12 = System.getProperty( PN_SQL_12, DEFAULT_SQL_12 );
    if ( SQL_12 == null || SQL_12.trim().length() == 0 ) {
      String msg = "no property for '" + PN_SQL_12 + "'";
      theLog.fatal( msg );
      throw new IllegalStateException( msg );
    }
    SQL_12 = SQL_12.trim();
  } // static

  private static String DEFAULT_SQL_15 = null;
  public static String SQL_15 = null;
  static {
    DEFAULT_SQL_15 = defaultProperties.getProperty( PN_SQL_15 );
    theLog.info( "DEFAULT_SQL_15 == '" + DEFAULT_SQL_15 + "'" );
    SQL_15 = System.getProperty( PN_SQL_15, DEFAULT_SQL_15 );
    if ( SQL_15 == null || SQL_15.trim().length() == 0 ) {
      String msg = "no property for '" + PN_SQL_15 + "'";
      theLog.fatal( msg );
      throw new IllegalStateException( msg );
    }
    SQL_15 = SQL_15.trim();
  } // static

  private static String DEFAULT_SQL_15A = null;
  public static String SQL_15A = null;
  static {
    DEFAULT_SQL_15A = defaultProperties.getProperty( PN_SQL_15A );
    theLog.info( "DEFAULT_SQL_15A == '" + DEFAULT_SQL_15A + "'" );
    SQL_15A = System.getProperty( PN_SQL_15A, DEFAULT_SQL_15A );
    if ( SQL_15A == null || SQL_15A.trim().length() == 0 ) {
      String msg = "no property for '" + PN_SQL_15A + "'";
      theLog.fatal( msg );
      throw new IllegalStateException( msg );
    }
    SQL_15A = SQL_15A.trim();
  } // static

  private static String DEFAULT_SQL_16 = null;
  public static String SQL_16 = null;
  static {
    DEFAULT_SQL_16 = defaultProperties.getProperty( PN_SQL_16 );
    theLog.info( "DEFAULT_SQL_16 == '" + DEFAULT_SQL_16 + "'" );
    SQL_16 = System.getProperty( PN_SQL_16, DEFAULT_SQL_16 );
    if ( SQL_16 == null || SQL_16.trim().length() == 0 ) {
      String msg = "no property for '" + PN_SQL_16 + "'";
      theLog.fatal( msg );
      throw new IllegalStateException( msg );
    }
    SQL_16 = SQL_16.trim();
  } // static

  private static String DEFAULT_SQL_17 = null;
  public static String SQL_17 = null;
  static {
    DEFAULT_SQL_17 = defaultProperties.getProperty( PN_SQL_17 );
    theLog.info( "DEFAULT_SQL_17 == '" + DEFAULT_SQL_17 + "'" );
    SQL_17 = System.getProperty( PN_SQL_17, DEFAULT_SQL_17 );
    if ( SQL_17 == null || SQL_17.trim().length() == 0 ) {
      String msg = "no property for '" + PN_SQL_17 + "'";
      theLog.fatal( msg );
      throw new IllegalStateException( msg );
    }
    SQL_17 = SQL_17.trim();
  } // static

  private static String DEFAULT_SQL_18 = null;
  static {
    String tmp = defaultProperties.getProperty( PN_SQL_18 );
    if ( tmp == null || tmp.trim().length() == 0 ) {
      String msg = "invalid or missing value for '" + PN_SQL_18 + "'";
      theLog.error(msg);
    }
    else {
      DEFAULT_SQL_18 = tmp.trim();
    }
    theLog.info( "DEFAULT_SQL_18 == '" + DEFAULT_SQL_18 + "'" );
  } // static

  public static String SQL_18 = null;
  static {
    SQL_18 = System.getProperty( PN_SQL_18 );
    if ( SQL_18 == null || SQL_18.trim().length() == 0 ) {
      SQL_18 = DEFAULT_SQL_18;
    }
    if ( SQL_18 == null || SQL_18.trim().length() == 0 ) {
      String msg = "no property for '" + PN_SQL_18 + "'";
      theLog.fatal( msg );
      throw new IllegalStateException( msg );
    }
    theLog.info( "SQL_18 == '" + SQL_18 + "'" );
  } // static

  // method to look up home for MemberEJB
  private static String _DEFAULT_MEMBER_HOME = null;
  private static String _MEMBER_HOME = null;
  static {
    _DEFAULT_MEMBER_HOME = getDefaultProperty( PN_MEMBER_HOME );
    theLog.info(
        "DEFAULT_MEMBER_HOME == '" + _DEFAULT_MEMBER_HOME + "'");
    _MEMBER_HOME = getProperty(
        PN_MEMBER_HOME, _DEFAULT_MEMBER_HOME );
    theLog.info( "MEMBER_HOME == '" + _MEMBER_HOME + "'" );
  } // static

  public static String MEMBER_HOME() { return _MEMBER_HOME; }

} // Configuration

/*
 * $Log: Configuration.java,v $
 * Revision 1.12  2003/02/26 03:38:45  rphall
 * Added copyright and GPL license
 *
 * Revision 1.11  2003/02/21 19:12:52  rphall
 * Added SQL for checking duplicate user names during editing
 *
 * Revision 1.10  2003/02/19 22:08:34  rphall
 * Removed gratuitous use of CLRA acronym
 *
 * Revision 1.9  2003/02/19 12:58:37  rphall
 * Added SQL to insert/delete MemberRole rows
 *
 * Revision 1.8  2003/02/16 00:40:29  rphall
 * Fixed bug with SQL for nextId updates
 *
 * Revision 1.7  2003/02/15 04:31:41  rphall
 * Changes connected to major revision of MemberBean
 *
 */

