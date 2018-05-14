/*
 * Copyright (c) Carnegie Lake Rowing Association 2002. All rights reserved.
 * Distributed under the GPL license. See doc/COPYING.
 * $RCSfile: Configuration.java,v $
 * $Date: 2003/02/26 03:38:45 $
 * $Revision: 1.4 $
 */

package com.clra.rowing;

import com.clra.util.ConfigurationException;
import com.clra.util.DBConfiguration;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;
import org.apache.log4j.Category;
import org.apache.log4j.helpers.Loader;

/**
 * A collection of configurable properties used by this package.
 * @version $Id: Configuration.java,v 1.4 2003/02/26 03:38:45 rphall Exp $
 * @author <a href="mailto:rphall@pluto.njcc.com">Rick Hall</a>
 */
public class Configuration {

  /** All methods are static */
  private Configuration() {}

  private final static String base = Configuration.class.getName();
  private final static Category theLog = Category.getInstance( base );

  /** Property that holds the JNDI location of the RowingSession factory */
  public final static String PN_ROWINGSESSION_HOME =
          "rowingsession.home";

  /** Property that holds the JNDI location of the Participant factory */
  public final static String PN_PARTICIPANT_HOME =
          "participant.home";

  /** Session SQL property-name prefix */
  public final static String PN_PREFIX_SQL_SESSION = "sessionset.sql.";

  /** Participant SQL property-name prefix */
  public final static String PN_PREFIX_SQL_PARTICIPANT = "participant.sql.";

  /** Enrollment SQL property-name prefix */
  public final static String PN_PREFIX_SQL_ENROLLMENT = "enrollment.sql.";

  /** SQL date format spec */
  public final static String PN_SQL_DATE_FORMAT =
          PN_PREFIX_SQL_SESSION + "dateformat." + DBConfiguration.DBTYPE;

  /** All session id's */
  public final static String PN_SQL_SESSION_01 =
          PN_PREFIX_SQL_SESSION + "01." + DBConfiguration.DBTYPE;

  /** All sessions */
  public final static String PN_SQL_SESSION_01A =
          PN_PREFIX_SQL_SESSION + "01a." + DBConfiguration.DBTYPE;

  /** Specific Session by id */
  public final static String PN_SQL_SESSION_02 =
          PN_PREFIX_SQL_SESSION + "02." + DBConfiguration.DBTYPE;

  /** Sessions within a date range */
  public final static String PN_SQL_SESSION_03 =
          PN_PREFIX_SQL_SESSION + "03." + DBConfiguration.DBTYPE;

  /** Inserts a session */
  public final static String PN_SQL_SESSION_04 =
          PN_PREFIX_SQL_SESSION + "04." + DBConfiguration.DBTYPE;

  /** Deletes a session */
  public final static String PN_SQL_SESSION_05 =
          PN_PREFIX_SQL_SESSION + "05." + DBConfiguration.DBTYPE;

  /** Loads a session */
  public final static String PN_SQL_SESSION_06 =
          PN_PREFIX_SQL_SESSION + "06." + DBConfiguration.DBTYPE;

  /** Stores a session */
  public final static String PN_SQL_SESSION_07 =
          PN_PREFIX_SQL_SESSION + "07." + DBConfiguration.DBTYPE;

  /** Updates the next id for a rowing session */
  public final static String PN_SQL_SESSION_08 =
          PN_PREFIX_SQL_SESSION + "08." + DBConfiguration.DBTYPE;

  /** Selects the next id for a rowing session */
  public final static String PN_SQL_SESSION_08A =
          PN_PREFIX_SQL_SESSION + "08a." + DBConfiguration.DBTYPE;

  /** Participant identified by participant_id */
  public final static String PN_SQL_PARTICIPANT_01 =
          PN_PREFIX_SQL_PARTICIPANT + "01." + DBConfiguration.DBTYPE;

  /** Participants (and member names) associated with a rowing session */
  public final static String PN_SQL_PARTICIPANT_02 =
          PN_PREFIX_SQL_PARTICIPANT + "02." + DBConfiguration.DBTYPE;

  /** Participant identified by memberId and rowingId */
  public final static String PN_SQL_PARTICIPANT_03 =
          PN_PREFIX_SQL_PARTICIPANT + "03." + DBConfiguration.DBTYPE;

  /** Id's of all participants */
  public final static String PN_SQL_PARTICIPANT_04 =
          PN_PREFIX_SQL_PARTICIPANT + "04." + DBConfiguration.DBTYPE;

  /** Inserts a participant constrained by rowing state */
  public final static String PN_SQL_PARTICIPANT_05 =
          PN_PREFIX_SQL_PARTICIPANT + "05." + DBConfiguration.DBTYPE;

  /** Deletes a participant */
  public final static String PN_SQL_PARTICIPANT_06 =
          PN_PREFIX_SQL_PARTICIPANT + "06." + DBConfiguration.DBTYPE;

  /** Stores a participant */
  public final static String PN_SQL_PARTICIPANT_07 =
          PN_PREFIX_SQL_PARTICIPANT + "07." + DBConfiguration.DBTYPE;

  /** Updates the next id for a participant */
  public final static String PN_SQL_PARTICIPANT_08 =
          PN_PREFIX_SQL_PARTICIPANT + "08." + DBConfiguration.DBTYPE;

  /** Selects the next id for a participant */
  public final static String PN_SQL_PARTICIPANT_08A =
          PN_PREFIX_SQL_PARTICIPANT + "08a." + DBConfiguration.DBTYPE;

  /** Inserts non-null participation data into temporary enrollment table */
  public final static String PN_SQL_ENROLLMENT_01 =
          PN_PREFIX_SQL_ENROLLMENT + "01." + DBConfiguration.DBTYPE;

  /** Inserts null participation data into temporary enrollment table */
  public final static String PN_SQL_ENROLLMENT_02 =
          PN_PREFIX_SQL_ENROLLMENT + "02." + DBConfiguration.DBTYPE;

  /** Name of file that holds default values for this package */
  private final static String DEFAULTS_FILE =
          "com/clra/rowing/rowing.properties";

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


  private static String _DEFAULT_ROWINGSESSION_HOME = null;
  private static String _ROWINGSESSION_HOME = null;
  static {
    _DEFAULT_ROWINGSESSION_HOME = getDefaultProperty( PN_ROWINGSESSION_HOME );
    theLog.info(
        "DEFAULT_ROWINGSESSION_HOME == '" + _DEFAULT_ROWINGSESSION_HOME + "'");
    _ROWINGSESSION_HOME = getProperty(
        PN_ROWINGSESSION_HOME, _DEFAULT_ROWINGSESSION_HOME );
    theLog.info( "ROWINGSESSION_HOME == '" + _ROWINGSESSION_HOME + "'" );
  } // static

  public static String ROWINGSESSION_HOME() { return _ROWINGSESSION_HOME; }

  private static String _DEFAULT_PARTICIPANT_HOME = null;
  private static String _PARTICIPANT_HOME = null;
  static {
    _DEFAULT_PARTICIPANT_HOME =
        getDefaultProperty( PN_PARTICIPANT_HOME );
    theLog.info( "DEFAULT_PARTICIPANT_HOME == '"
        + _DEFAULT_PARTICIPANT_HOME + "'");
    _PARTICIPANT_HOME = getProperty(
        PN_PARTICIPANT_HOME, _DEFAULT_PARTICIPANT_HOME );
    theLog.info("PARTICIPANT_HOME == '" + _PARTICIPANT_HOME + "'");
  } // static

  public static String PARTICIPANT_HOME() {
    return _PARTICIPANT_HOME;
  }

  /** SQL that selects up all sessions */
  private static String DEFAULT_SQL_DATE_FORMAT = null;
  public static String SQL_DATE_FORMAT = null;
  static {
    DEFAULT_SQL_DATE_FORMAT = getDefaultProperty( PN_SQL_DATE_FORMAT );
    theLog.info("DEFAULT_SQL_DATE_FORMAT == '" + DEFAULT_SQL_DATE_FORMAT + "'");
    SQL_DATE_FORMAT = getProperty(PN_SQL_DATE_FORMAT, DEFAULT_SQL_DATE_FORMAT);
    theLog.info( "SQL_DATE_FORMAT == '" + SQL_DATE_FORMAT + "'" );
  } // static

  /** SQL that selects all session id's */
  private static String DEFAULT_SQL_SESSION_01 = null;
  public static String SQL_SESSION_01 = null;
  static {
    DEFAULT_SQL_SESSION_01 = getDefaultProperty( PN_SQL_SESSION_01 );
    theLog.info( "DEFAULT_SQL_SESSION_01 == '" + DEFAULT_SQL_SESSION_01 + "'" );
    SQL_SESSION_01 = getProperty( PN_SQL_SESSION_01, DEFAULT_SQL_SESSION_01 );
    theLog.info( "SQL_SESSION_01 == '" + SQL_SESSION_01 + "'" );
  } // static

  /** SQL that selects all sessions */
  private static String DEFAULT_SQL_SESSION_01A = null;
  public static String SQL_SESSION_01A = null;
  static {
    DEFAULT_SQL_SESSION_01A = getDefaultProperty( PN_SQL_SESSION_01A );
    theLog.info("DEFAULT_SQL_SESSION_01A == '" + DEFAULT_SQL_SESSION_01A + "'");
    SQL_SESSION_01A = getProperty( PN_SQL_SESSION_01A,DEFAULT_SQL_SESSION_01A );
    theLog.info( "SQL_SESSION_01A == '" + SQL_SESSION_01A + "'" );
  } // static

  /** SQL that selects a session by id */
  private static String DEFAULT_SQL_SESSION_02 = null;
  public static String SQL_SESSION_02 = null;
  static {
    DEFAULT_SQL_SESSION_02 = getDefaultProperty( PN_SQL_SESSION_02 );
    theLog.info( "DEFAULT_SQL_SESSION_02 == '" + DEFAULT_SQL_SESSION_02 + "'" );
    SQL_SESSION_02 = getProperty( PN_SQL_SESSION_02, DEFAULT_SQL_SESSION_02 );
    theLog.info( "SQL_SESSION_02 == '" + SQL_SESSION_02 + "'" );
  } // static

  /** SQL that selects sessions within an inclusive date range */
  private static String DEFAULT_SQL_SESSION_03 = null;
  public static String SQL_SESSION_03 = null;
  static {
    DEFAULT_SQL_SESSION_03 = getDefaultProperty( PN_SQL_SESSION_03 );
    theLog.info( "DEFAULT_SQL_SESSION_03 == '" + DEFAULT_SQL_SESSION_03 + "'" );
    SQL_SESSION_03 = getProperty( PN_SQL_SESSION_03, DEFAULT_SQL_SESSION_03 );
    theLog.info( "SQL_SESSION_03 == '" + SQL_SESSION_03 + "'" );
  } // static

  /** SQL that inserts a rowing session */
  private static String DEFAULT_SQL_SESSION_04 = null;
  public static String SQL_SESSION_04 = null;
  static {
    DEFAULT_SQL_SESSION_04 = getDefaultProperty( PN_SQL_SESSION_04 );
    theLog.info( "DEFAULT_SQL_SESSION_04 == '" + DEFAULT_SQL_SESSION_04 + "'" );
    SQL_SESSION_04 = getProperty( PN_SQL_SESSION_04, DEFAULT_SQL_SESSION_04 );
    theLog.info( "SQL_SESSION_04 == '" + SQL_SESSION_04 + "'" );
  } // static

  /** SQL that deletes a session */
  private static String DEFAULT_SQL_SESSION_05 = null;
  public static String SQL_SESSION_05 = null;
  static {
    DEFAULT_SQL_SESSION_05 = getDefaultProperty( PN_SQL_SESSION_05 );
    theLog.info( "DEFAULT_SQL_SESSION_05 == '" + DEFAULT_SQL_SESSION_05 + "'" );
    SQL_SESSION_05 = getProperty( PN_SQL_SESSION_05, DEFAULT_SQL_SESSION_05 );
    theLog.info( "SQL_SESSION_05 == '" + SQL_SESSION_05 + "'" );
  } // static

  /** SQL that loads a session */
  private static String DEFAULT_SQL_SESSION_06 = null;
  public static String SQL_SESSION_06 = null;
  static {
    DEFAULT_SQL_SESSION_06 = getDefaultProperty( PN_SQL_SESSION_06 );
    theLog.info( "DEFAULT_SQL_SESSION_06 == '" + DEFAULT_SQL_SESSION_06 + "'" );
    SQL_SESSION_06 = getProperty( PN_SQL_SESSION_06, DEFAULT_SQL_SESSION_06 );
    theLog.info( "SQL_SESSION_06 == '" + SQL_SESSION_06 + "'" );
  } // static

  /** SQL that stores a session */
  private static String DEFAULT_SQL_SESSION_07 = null;
  public static String SQL_SESSION_07 = null;
  static {
    DEFAULT_SQL_SESSION_07 = getDefaultProperty( PN_SQL_SESSION_07 );
    theLog.info( "DEFAULT_SQL_SESSION_07 == '" + DEFAULT_SQL_SESSION_07 + "'" );
    SQL_SESSION_07 = getProperty( PN_SQL_SESSION_07, DEFAULT_SQL_SESSION_07 );
    theLog.info( "SQL_SESSION_07 == '" + SQL_SESSION_07 + "'" );
  } // static

  /** Updates the next id for a rowing session */
  private static String DEFAULT_SQL_SESSION_08 = null;
  public static String SQL_SESSION_08 = null;
  static {
    DEFAULT_SQL_SESSION_08 = getDefaultProperty( PN_SQL_SESSION_08 );
    theLog.info( "DEFAULT_SQL_SESSION_08 == '" + DEFAULT_SQL_SESSION_08 + "'" );
    SQL_SESSION_08 = getProperty( PN_SQL_SESSION_08, DEFAULT_SQL_SESSION_08 );
    theLog.info( "SQL_SESSION_08 == '" + SQL_SESSION_08 + "'" );
  } // static

  /** Selects the next id for a rowing session */
  private static String DEFAULT_SQL_SESSION_08A = null;
  public static String SQL_SESSION_08A = null;
  static {
    DEFAULT_SQL_SESSION_08A = getDefaultProperty( PN_SQL_SESSION_08A );
    theLog.info("DEFAULT_SQL_SESSION_08A == '" + DEFAULT_SQL_SESSION_08A + "'");
    SQL_SESSION_08A = getProperty( PN_SQL_SESSION_08A, DEFAULT_SQL_SESSION_08A);
    theLog.info( "SQL_SESSION_08A == '" + SQL_SESSION_08A + "'" );
  } // static

  /** Loads a participant identified by participant_id */
  private static String DEFAULT_SQL_PARTICIPANT_01 = null;
  public static String SQL_PARTICIPANT_01 = null;
  static {
    DEFAULT_SQL_PARTICIPANT_01 = getDefaultProperty( PN_SQL_PARTICIPANT_01 );
    theLog.info(
      "DEFAULT_SQL_PARTICIPANT_01 == '" + DEFAULT_SQL_PARTICIPANT_01 + "'" );
    SQL_PARTICIPANT_01 =
      getProperty( PN_SQL_PARTICIPANT_01, DEFAULT_SQL_PARTICIPANT_01 );
    theLog.info( "SQL_PARTICIPANT_01 == '" + SQL_PARTICIPANT_01 + "'" );
  } // static

  /** Loads a participants (and their member names) in a rowing session */
  private static String DEFAULT_SQL_PARTICIPANT_02 = null;
  public static String SQL_PARTICIPANT_02 = null;
  static {
    DEFAULT_SQL_PARTICIPANT_02 = getDefaultProperty( PN_SQL_PARTICIPANT_02 );
    theLog.info(
      "DEFAULT_SQL_PARTICIPANT_02 == '" + DEFAULT_SQL_PARTICIPANT_02 + "'" );
    SQL_PARTICIPANT_02 =
      getProperty( PN_SQL_PARTICIPANT_02, DEFAULT_SQL_PARTICIPANT_02 );
    theLog.info( "SQL_PARTICIPANT_02 == '" + SQL_PARTICIPANT_02 + "'" );
  } // static

  /** Loads a participant (possibly null) identified by member and rowing id */
  private static String DEFAULT_SQL_PARTICIPANT_03 = null;
  public static String SQL_PARTICIPANT_03 = null;
  static {
    DEFAULT_SQL_PARTICIPANT_03 = getDefaultProperty( PN_SQL_PARTICIPANT_03 );
    theLog.info(
      "DEFAULT_SQL_PARTICIPANT_03 == '" + DEFAULT_SQL_PARTICIPANT_03 + "'" );
    SQL_PARTICIPANT_03 =
      getProperty( PN_SQL_PARTICIPANT_03, DEFAULT_SQL_PARTICIPANT_03 );
    theLog.info( "SQL_PARTICIPANT_03 == '" + SQL_PARTICIPANT_03 + "'" );
  } // static

  /** Selects id's of all participants */
  private static String DEFAULT_SQL_PARTICIPANT_04 = null;
  public static String SQL_PARTICIPANT_04 = null;
  static {
    DEFAULT_SQL_PARTICIPANT_04 = getDefaultProperty( PN_SQL_PARTICIPANT_04 );
    theLog.info(
      "DEFAULT_SQL_PARTICIPANT_04 == '" + DEFAULT_SQL_PARTICIPANT_04 + "'" );
    SQL_PARTICIPANT_04 =
      getProperty( PN_SQL_PARTICIPANT_04, DEFAULT_SQL_PARTICIPANT_04 );
    theLog.info( "SQL_PARTICIPANT_04 == '" + SQL_PARTICIPANT_04 + "'" );
  } // static

  /** Inserts a participant constrained by rowing state */
  private static String DEFAULT_SQL_PARTICIPANT_05 = null;
  public static String SQL_PARTICIPANT_05 = null;
  static {
    DEFAULT_SQL_PARTICIPANT_05 = getDefaultProperty( PN_SQL_PARTICIPANT_05 );
    theLog.info(
      "DEFAULT_SQL_PARTICIPANT_05 == '" + DEFAULT_SQL_PARTICIPANT_05 + "'" );
    SQL_PARTICIPANT_05 =
      getProperty( PN_SQL_PARTICIPANT_05, DEFAULT_SQL_PARTICIPANT_05 );
    theLog.info( "SQL_PARTICIPANT_05 == '" + SQL_PARTICIPANT_05 + "'" );
  } // static

  /** Deletes a participant */
  private static String DEFAULT_SQL_PARTICIPANT_06 = null;
  public static String SQL_PARTICIPANT_06 = null;
  static {
    DEFAULT_SQL_PARTICIPANT_06 = getDefaultProperty( PN_SQL_PARTICIPANT_06 );
    theLog.info(
      "DEFAULT_SQL_PARTICIPANT_06 == '" + DEFAULT_SQL_PARTICIPANT_06 + "'" );
    SQL_PARTICIPANT_06 =
      getProperty( PN_SQL_PARTICIPANT_06, DEFAULT_SQL_PARTICIPANT_06 );
    theLog.info( "SQL_PARTICIPANT_06 == '" + SQL_PARTICIPANT_06 + "'" );
  } // static

  /** Stores a participant */
  private static String DEFAULT_SQL_PARTICIPANT_07 = null;
  public static String SQL_PARTICIPANT_07 = null;
  static {
    DEFAULT_SQL_PARTICIPANT_07 = getDefaultProperty( PN_SQL_PARTICIPANT_07 );
    theLog.info(
      "DEFAULT_SQL_PARTICIPANT_07 == '" + DEFAULT_SQL_PARTICIPANT_07 + "'" );
    SQL_PARTICIPANT_07 =
      getProperty( PN_SQL_PARTICIPANT_07, DEFAULT_SQL_PARTICIPANT_07 );
    theLog.info( "SQL_PARTICIPANT_07 == '" + SQL_PARTICIPANT_07 + "'" );
  } // static

  /** Updates the next id for a participant */
  private static String DEFAULT_SQL_PARTICIPANT_08 = null;
  public static String SQL_PARTICIPANT_08 = null;
  static {
    DEFAULT_SQL_PARTICIPANT_08 = getDefaultProperty( PN_SQL_PARTICIPANT_08 );
    theLog.info(
      "DEFAULT_SQL_PARTICIPANT_08 == '" + DEFAULT_SQL_PARTICIPANT_08 + "'" );
    SQL_PARTICIPANT_08 =
      getProperty( PN_SQL_PARTICIPANT_08, DEFAULT_SQL_PARTICIPANT_08 );
    theLog.info( "SQL_PARTICIPANT_08 == '" + SQL_PARTICIPANT_08 + "'" );
  } // static

  /** Selects the next id for a participant */
  private static String DEFAULT_SQL_PARTICIPANT_08A = null;
  public static String SQL_PARTICIPANT_08A = null;
  static {
    DEFAULT_SQL_PARTICIPANT_08A = getDefaultProperty( PN_SQL_PARTICIPANT_08A );
    theLog.info(
      "DEFAULT_SQL_PARTICIPANT_08A == '" + DEFAULT_SQL_PARTICIPANT_08A + "'" );
    SQL_PARTICIPANT_08A =
      getProperty( PN_SQL_PARTICIPANT_08A, DEFAULT_SQL_PARTICIPANT_08A );
    theLog.info( "SQL_PARTICIPANT_08A == '" + SQL_PARTICIPANT_08A + "'" );
  } // static

  /** Inserts non-null participation data into temporary enrollment table */
  private static String DEFAULT_SQL_ENROLLMENT_01 = null;
  public static String SQL_ENROLLMENT_01 = null;
  static {
    DEFAULT_SQL_ENROLLMENT_01 = getDefaultProperty( PN_SQL_ENROLLMENT_01 );
    theLog.info(
      "DEFAULT_SQL_ENROLLMENT_01 == '" + DEFAULT_SQL_ENROLLMENT_01 + "'" );
    SQL_ENROLLMENT_01 =
      getProperty( PN_SQL_ENROLLMENT_01, DEFAULT_SQL_ENROLLMENT_01 );
    theLog.info( "SQL_ENROLLMENT_01 == '" + SQL_ENROLLMENT_01 + "'" );
  } // static

  /** Inserts null participation data into temporary enrollment table */
  private static String DEFAULT_SQL_ENROLLMENT_02 = null;
  public static String SQL_ENROLLMENT_02 = null;
  static {
    DEFAULT_SQL_ENROLLMENT_02 = getDefaultProperty( PN_SQL_ENROLLMENT_02 );
    theLog.info(
      "DEFAULT_SQL_ENROLLMENT_02 == '" + DEFAULT_SQL_ENROLLMENT_02 + "'" );
    SQL_ENROLLMENT_02 =
      getProperty( PN_SQL_ENROLLMENT_02, DEFAULT_SQL_ENROLLMENT_02 );
    theLog.info( "SQL_ENROLLMENT_02 == '" + SQL_ENROLLMENT_02 + "'" );
  } // static

} // Configuration

/*
 * $Log: Configuration.java,v $
 * Revision 1.4  2003/02/26 03:38:45  rphall
 * Added copyright and GPL license
 *
 * Revision 1.3  2003/02/19 22:09:15  rphall
 * Removed gratuitous use of CLRA acronym
 *
 */

