/*
 * Copyright (c) Carnegie Lake Rowing Association 2002. All rights reserved.
 * Distributed under the GPL license. See doc/COPYING.
 * $RCSfile: Database.java,v $
 * $Date: 2003/03/08 21:08:13 $
 * $Revision: 1.5 $
 */

package test.clra.util;

import com.clra.rowing.Configuration;
import com.clra.util.DBConfiguration;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.log4j.Category;

/**
 * Verifies the database is configured properly for unit testing. The
 * database may be re-initialized by running the $CLRA_HOME/bin/setup.sh
 * script.<p>
 *
 * @version $Revision: 1.5 $ $Date: 2003/03/08 21:08:13 $
 * @author <a href="mailto:rphall@pluto.njcc.com">Rick Hall</a>
 */
public class Database {

  private final static String base = Database.class.getName();
  private final static Category theLog = Category.getInstance( base );

  /** All methods are static */
  private Database() {}

  public static void assertValid() {

    Connection conn = null;
    try {

      conn = DBConfiguration.getConnection();

      assertRowingSessionCount( conn );
      assertRowingSessionOctoberCount( conn );

    }

    catch( Exception x ) {
      theLog.fatal( Database.class.getName(), x );
      throw new IllegalStateException( x.getMessage() );
    }

    finally {
      DBConfiguration.closeSQLConnection( conn );
    }
    conn = null;

  } // assertValid()

  private static void assertRowingSessionCount( Connection conn )
    throws Exception {

    ResultSet rs = null;
    PreparedStatement stmt = null;
    int rowCount = 0;
    try {

      stmt = conn.prepareStatement( Configuration.SQL_SESSION_01 );
      rs = stmt.executeQuery();

      while ( rs.next() ) {
        Integer id = new Integer( rs.getInt(1) );
        ++rowCount;
      }

    }
    finally {
      DBConfiguration.closeSQLResultSet( rs );
      DBConfiguration.closeSQLStatement( stmt );
    }

    if ( rowCount != Expected.ROWINGSESSION_COUNT().intValue() ) {
      String msg = "RowingSession: unexpected rowCount == " + rowCount;
      throw new IllegalStateException( msg );
    }

  } // assertRowingSessionCount(..)

  private static void assertRowingSessionOctoberCount( Connection conn )
    throws Exception {

    ResultSet rs = null;
    PreparedStatement stmt = null;
    int rowCount = 0;
    try {

      // String strOct01 = "09/30/2001 11:59 PM";
      // String strOct31 = "10/31/2001 11:59 PM";
      String strOct01 = "2001-09-30 23:59:59";
      String strOct31 = "2001-10-31 23:59:59";

      stmt = conn.prepareStatement( Configuration.SQL_SESSION_03 );
      stmt.setString( 1, strOct01 );
      stmt.setString( 2, strOct31 );
      rs = stmt.executeQuery();

      while ( rs.next() ) {
        Integer id = new Integer( rs.getInt(1) );
        ++rowCount;
      }

    }
    finally {
      DBConfiguration.closeSQLResultSet( rs );
      DBConfiguration.closeSQLStatement( stmt );
    }

    if ( rowCount != Expected.ROWINGSESSION_OCT_COUNT().intValue() ) {
      String msg = "RowingSessionOctober: unexpected rowCount == " + rowCount;
      throw new IllegalStateException( msg );
    }

  } // assertRowingSessionOctoberCount(..)

} // Database

/*
 * $Log: Database.java,v $
 * Revision 1.5  2003/03/08 21:08:13  rphall
 * Changed name of Database.assert() method to assertValid()
 *
 * Revision 1.4  2003/02/26 03:38:47  rphall
 * Added copyright and GPL license
 *
 * Revision 1.3  2003/02/19 22:38:55  rphall
 * Removed gratuitous use of CLRA acronym
 *
 * Revision 1.2  2002/01/30 15:51:27  rphall
 * Changed date formats
 */

