/*
 * Copyright (c) Carnegie Lake Rowing Association 2002. All rights reserved.
 * Distributed under the GPL license. See doc/COPYING.
 * $RCSfile: ExportUsers.java,v $
 * $Date: 2003/02/26 03:38:47 $
 * $Revision: 1.3 $
 */

package test.clra.member;

import com.clra.member.MemberException;
import com.clra.member.Configuration;
import com.clra.util.DBConfiguration;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

/**
 * This utility exports account_name and account_passwd of the Member table
 * to standard out in a form suitable for a JAAS users.properties file.
 *
 * @version $Revision: 1.3 $ $Date: 2003/02/26 03:38:47 $
 * @author <a href="mailto:rphall@pluto.njcc.com">Rick Hall</a>
 */
public final class ExportUsers {

  /** All utilities are static */
  private ExportUsers() {}

  public static void main( String[] args ) {
    try {
      printMemberTable();
    }
    catch( Exception x ) {
      x.printStackTrace();
    }
  }

  /** Prints the Member table to standard out */
  public static void printMemberTable() throws MemberException {

    Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    try {
      conn = DBConfiguration.getConnection();
      stmt = conn.prepareStatement(
          Configuration.SQL_01,
          ResultSet.TYPE_FORWARD_ONLY,
          ResultSet.CONCUR_READ_ONLY);
      rs = stmt.executeQuery();
      while ( rs.next() ) {
        printRow(System.out,rs);
      }
    }
    catch(SQLException x) {
      String msg = "SQLException: " + x.getMessage();
      throw new MemberException( msg );
    }
    finally {
      DBConfiguration.closeSQLResultSet( rs );
      DBConfiguration.closeSQLStatement( stmt );
      DBConfiguration.closeSQLConnection( conn );
    }
    conn = null;
    stmt = null;
    rs = null;

    return;
  } // findAllMembersByLastName()

  public static
  void printRow(PrintStream out, ResultSet rs) throws SQLException {

    final String account_name       = rs.getString( "account_name"      );
    final String account_passwd     = rs.getString( "account_passwd"    );

    out.println( account_name + "=" + account_passwd );

  } // mapResultSetToMemberSnapshot(ResultSet)

} // ExportUsers

/*
 * $Log: ExportUsers.java,v $
 * Revision 1.3  2003/02/26 03:38:47  rphall
 * Added copyright and GPL license
 *
 * Revision 1.2  2002/02/18 18:07:42  rphall
 * Ran dos2unix to remove ^M (carriage return) from end of lines
 *
 * Revision 1.1.1.1  2002/01/03 21:57:28  rphall
 * Initial load, 5th try, Jan-03-2002 4:57 PM
 *
 * Revision 1.1  2002/01/02 14:13:34  rphall
 * Batch file to export account_name,account_passwd for users.properties
 *
 */

