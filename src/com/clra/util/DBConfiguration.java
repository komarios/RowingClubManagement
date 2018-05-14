/*
 * Copyright (c) Carnegie Lake Rowing Association 2002. All rights reserved.
 * Distributed under the GPL license. See doc/COPYING.
 * $RCSfile: DBConfiguration.java,v $
 * $Date: 2003/02/26 03:38:45 $
 * $Revision: 1.5 $
 */

package com.clra.util;

import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.Properties;
import org.apache.log4j.Category;
import org.apache.log4j.helpers.Loader;

/**
 * A collection of configurable properties used by this package.
 * @version $Id: DBConfiguration.java,v 1.5 2003/02/26 03:38:45 rphall Exp $
 * @author <a href="mailto:rphall@pluto.njcc.com">Rick Hall</a>
 */
public class DBConfiguration {

  /** All methods are static */
  private DBConfiguration() {}

  private final static String base = DBConfiguration.class.getName();

  private final static Category theLog = Category.getInstance( base );

  public final static String PN_DBTYPE = "clra.dbtype";

  public final static String PN_PREFIX_DBDRIVER = "clra.dbdriver.";

  public final static String PN_DBURL = "clra.dburl";

  public final static String PN_DBUSER = "clra.dbuser";

  public final static String PN_DBPASSWORD = "clra.dbpassword";

  private final static String DEFAULTS_FILE =
          "com/clra/util/util.properties";

  private final static Properties defaultProperties = new Properties();
  static {
    InputStream is = null;
    try {
      URL url = Loader.getResource( DEFAULTS_FILE, DBConfiguration.class );
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

  private static String DEFAULT_DBTYPE = null;
  static {
    String tmp = defaultProperties.getProperty( PN_DBTYPE );
    if ( tmp == null || tmp.trim().length() == 0 ) {
      String msg = "invalid or missing value for '" + PN_DBTYPE + "'";
      theLog.error(msg);
    }
    else {
      DEFAULT_DBTYPE = tmp.trim();
    }
    theLog.info( "DEFAULT_DBTYPE == '" + DEFAULT_DBTYPE + "'" );
  } // static

  public static String DBTYPE = null;
  static {
    DBTYPE = System.getProperty( PN_DBTYPE );
    if ( DBTYPE == null || DBTYPE.trim().length() == 0 ) {
      DBTYPE = DEFAULT_DBTYPE;
    }
    if ( DBTYPE == null || DBTYPE.trim().length() == 0 ) {
      String msg = "no property for '" + PN_DBTYPE + "'";
      theLog.fatal( msg );
      throw new IllegalStateException( msg );
    }
    DBTYPE = DBTYPE.trim();
    theLog.info( "DBTYPE == '" + DBTYPE + "'" );
  } // static

  public static boolean isOracle() {
    return DBConfiguration.DBTYPE.equalsIgnoreCase("oracle");
  }

  private static String DEFAULT_DBDRIVER = null;
  static {
    if ( DEFAULT_DBTYPE != null ) {
      final String PN_DBDRIVER = PN_PREFIX_DBDRIVER + DEFAULT_DBTYPE;
      String tmp = defaultProperties.getProperty( PN_DBDRIVER );
      if ( tmp == null || tmp.trim().length() == 0 ) {
        String msg = "invalid or missing value for '" + PN_DBDRIVER + "'";
        theLog.error(msg);
      }
      else {
        DEFAULT_DBDRIVER = tmp.trim();
      }
    }
    else {
      String msg = "no default DBDRIVER for DBConfiguration query";
      theLog.error(msg);
    }
    theLog.info( "DEFAULT_DBDRIVER == '" + DEFAULT_DBDRIVER + "'" );
  } // static

  public static String DBDRIVER = null;
  static {
    final String PN_DBDRIVER = PN_PREFIX_DBDRIVER + DBTYPE;
    DBDRIVER = System.getProperty( PN_DBDRIVER );
    if ( DBDRIVER == null || DBDRIVER.trim().length() == 0 ) {
      DBDRIVER = DEFAULT_DBDRIVER;
    }
    if ( DBDRIVER == null || DBDRIVER.trim().length() == 0 ) {
      String msg = "no property for '" + PN_DBDRIVER + "'";
      theLog.fatal( msg );
      throw new IllegalStateException( msg );
    }
    theLog.info( "DBDRIVER == '" + DBDRIVER + "'" );
  } // static

  private static String DEFAULT_DBURL = null;
  static {
    if ( DEFAULT_DBTYPE != null ) {
      String tmp = defaultProperties.getProperty( PN_DBURL );
      if ( tmp == null || tmp.trim().length() == 0 ) {
        String msg = "invalid or missing value for '" + PN_DBURL + "'";
        theLog.error(msg);
      }
      else {
        DEFAULT_DBURL = tmp.trim();
      }
    }
    else {
      String msg = "no default DBURL for DBConfiguration query";
      theLog.error(msg);
    }
    theLog.info( "DEFAULT_DBURL == '" + DEFAULT_DBURL + "'" );
  } // static

  public static String DBURL = null;
  static {
    DBURL = System.getProperty( PN_DBURL );
    if ( DBURL == null || DBURL.trim().length() == 0 ) {
      DBURL = DEFAULT_DBURL;
    }
    if ( DBURL == null || DBURL.trim().length() == 0 ) {
      String msg = "no property for '" + PN_DBURL + "'";
      theLog.fatal( msg );
      throw new IllegalStateException( msg );
    }
    theLog.info( "DB URL == '" + DBURL + "'" );
  } // static

  private static String DEFAULT_DBUSER = null;
  static {
    if ( DEFAULT_DBTYPE != null ) {
      String tmp = defaultProperties.getProperty( PN_DBUSER );
      if ( tmp == null || tmp.trim().length() == 0 ) {
        String msg = "invalid or missing value for '" + PN_DBUSER + "'";
        theLog.error(msg);
      }
      else {
        DEFAULT_DBUSER = tmp.trim();
      }
    }
    else {
      String msg = "no default DBUSER for DBConfiguration query";
      theLog.error(msg);
    }
    theLog.info( "DEFAULT_DBUSER == '" + DEFAULT_DBUSER + "'" );
  } // static

  public static String DBUSER = null;
  static {
    DBUSER = System.getProperty( PN_DBUSER );
    if ( DBUSER == null || DBUSER.trim().length() == 0 ) {
      DBUSER = DEFAULT_DBUSER;
    }
    if ( DBUSER == null || DBUSER.trim().length() == 0 ) {
      String msg = "no property for '" + PN_DBUSER + "'";
      theLog.fatal( msg );
      throw new IllegalStateException( msg );
    }
    theLog.info( "DBUSER == '" + DBUSER + "'" );
  } // static

  private static String DEFAULT_DBPASSWORD = null;
  static {
    if ( DEFAULT_DBTYPE != null ) {
      String tmp = defaultProperties.getProperty( PN_DBPASSWORD );
      if ( tmp == null || tmp.trim().length() == 0 ) {
        String msg = "invalid or missing value for '" + PN_DBPASSWORD + "'";
        theLog.error(msg);
      }
      else {
        DEFAULT_DBPASSWORD = tmp.trim();
      }
    }
    else {
      String msg = "no default DBPASSWORD for DBConfiguration query";
      theLog.error(msg);
    }
    theLog.info( "DEFAULT_DBPASSWORD == '" + DEFAULT_DBPASSWORD + "'" );
  } // static

  public static String DBPASSWORD = null;
  static {
    DBPASSWORD = System.getProperty( PN_DBPASSWORD );
    if ( DBPASSWORD == null || DBPASSWORD.trim().length() == 0 ) {
      DBPASSWORD = DEFAULT_DBPASSWORD;
    }
    if ( DBPASSWORD == null || DBPASSWORD.trim().length() == 0 ) {
      String msg = "no property for '" + PN_DBPASSWORD + "'";
      theLog.fatal( msg );
      throw new IllegalStateException( msg );
    }
    theLog.info( "DBPASSWORD == '" + DBPASSWORD + "'" );
  } // static

  // Register the DB driver used by this class
  static {
    try {
      Class driverClass = Class.forName( DBConfiguration.DBDRIVER );
      Driver driver = (Driver) driverClass.newInstance();
      DriverManager.registerDriver( driver );
    }
    catch(ClassNotFoundException e) {
      String msg  = "unable to find '" + DBConfiguration.DBDRIVER + "'";
      theLog.fatal( msg );
      throw new IllegalStateException(msg);
    }
    catch(IllegalAccessException e) {
      String msg  = "unable to instantiate '" + DBConfiguration.DBDRIVER + "'";
      theLog.fatal( msg );
      throw new IllegalStateException(msg);
    }
    catch(InstantiationException e) {
      String msg  = "unable to instantiate '" + DBConfiguration.DBDRIVER + "'";
      theLog.fatal( msg );
      throw new IllegalStateException(msg);
    }
    catch(SQLException e) {
      String msg  = "unable to register '" + DBConfiguration.DBDRIVER + "'";
      theLog.fatal( msg );
      throw new IllegalStateException(msg);
    }
  } // static

  /** Gets a SQL connection */
  public static Connection getConnection() throws SQLException {

    final String url = DBConfiguration.DBURL;
    final String usr = DBConfiguration.DBUSER;
    final String pwd = DBConfiguration.DBPASSWORD;
    Connection conn = null;
    try {
      conn = DriverManager.getConnection( url, usr, pwd );
    }
    catch( SQLException x ) {
      String msg = "unable to get SQL connection for '" + url
          + "', " + usr + "'," + pwd + "'";
      theLog.debug( msg );
      throw x;
    }
    
    return conn;
  } // getConnection()

  /** Closes SQL result set */
  public static void closeSQLResultSet( ResultSet rs ) {
    if (rs != null) {
        try {
          rs.close();
          rs = null;
        }
        catch(Exception x) {
          theLog.error( x.getMessage(), x );
        }
      }
    } // closeSQLStatement(Statement)

  /** Closes SQL statement */
  public static void closeSQLStatement( Statement stmt ) {
    if (stmt != null) {
        try {
          stmt.close();
          stmt = null;
        }
        catch(Exception x) {
          theLog.error( x.getMessage(), x );
        }
      }
    } // closeSQLStatement(Statement)

  /** Closes SQL connection */
  public static void closeSQLConnection( Connection conn ) {
      if (conn != null) {
        try {
          conn.close();
          conn = null;
        }
        catch(Exception x) {
          theLog.error( x.getMessage(), x );
        }
      }

    return;
  } // closeSQLConnection(Connection)

  /**
   * Converts an int value to an Integer object. Per JDBC, a
   * zero-valued int is converted to null.
   */
  public static Integer convertIntToIntegerOrNull( int i ) {
    Integer retVal = null;
    if ( i != 0 ) {
      retVal = new Integer( i );
    }
    return retVal;
  }

} // DBConfiguration

/*
 * $Log: DBConfiguration.java,v $
 * Revision 1.5  2003/02/26 03:38:45  rphall
 * Added copyright and GPL license
 *
 * Revision 1.4  2003/02/21 05:03:39  rphall
 * Parameterized deployment-specific properties
 *
 * Revision 1.3  2003/02/19 22:09:16  rphall
 * Removed gratuitous use of CLRA acronym
 *
 * Revision 1.2  2002/01/30 15:13:40  rphall
 * Changed 'memberset' prefix to 'clra'
 * Changed default DB to mySQL
 */

