/*
 * Copyright (c) Carnegie Lake Rowing Association 2002. All rights reserved.
 * Distributed under the GPL license. See doc/COPYING.
 * $RCSfile: ExportMember.java,v $
 * $Date: 2003/02/26 03:38:47 $
 * $Revision: 1.4 $
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
 * This utility exports the Member table to standard out.
 *
 * @version $Revision: 1.4 $ $Date: 2003/02/26 03:38:47 $
 * @author <a href="mailto:rphall@pluto.njcc.com">Rick Hall</a>
 */
public final class ExportMember {

  /** All utilities are static */
  private ExportMember() {}

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

    final int member_id             = rs.getInt(    "member_id"         );
    final int clra_year             = rs.getInt(    "clra_year"         );
    final Date birthday             = rs.getDate(   "birthday"          );
    final String account_name       = rs.getString( "account_name"      );
    final String account_passwd     = rs.getString( "account_passwd"    );
    final String clra_status        = rs.getString( "clra_status"       );
    final String name_last          = rs.getString( "name_last"         );
    final String name_first         = rs.getString( "name_first"        );
    final String name_middle        = rs.getString( "name_middle"       );
    final String name_suffix        = rs.getString( "name_suffix"       );
    final String email              = rs.getString( "email"             );
    final String telephone_evening  = rs.getString( "telephone_evening" );
    final String telephone_day      = rs.getString( "telephone_day"     );
    final String telephone_other    = rs.getString( "telephone_other"   );
    final String address_street1    = rs.getString( "address_street1"   );
    final String address_street2    = rs.getString( "address_street2"   );
    final String address_city       = rs.getString( "address_city"      );
    final String address_state      = rs.getString( "address_state"     );
    final String address_zip        = rs.getString( "address_zip"       );

    out.println(
      "INSERT INTO Member(member_id,account_name,account_passwd,clra_status,name_last,name_first,name_middle,name_suffix,email,telephone_evening,telephone_day,telephone_other,address_street1,address_street2,address_city,address_state,address_zip,clra_year,birthday) values("
      + member_id
      + ",'" + account_name + "','" + account_passwd + "',"
      + (clra_status == null ? "NULL," : "'" + clra_status.trim() + "'," )
      + (name_last == null ? "NULL," : "'" + name_last.trim() + "'," )
      + (name_first == null ? "NULL," : "'" + name_first.trim() + "'," )
      + (name_middle == null ? "NULL," : "'" + name_first.trim() + "'," )
      + (name_suffix == null ? "NULL," : "'" + name_first.trim() + "'," )
      + (email == null ? "NULL," : "'" + email.trim() + "'," )
      + (telephone_evening == null ? "NULL," : "'"
        + telephone_evening.trim() + "'," )
      + (telephone_day == null ? "NULL," : "'" + telephone_day.trim() + "'," )
      + (telephone_other == null ? "NULL," : "'" + telephone_other.trim()+ "',")
      + (address_street1 == null ? "NULL," : "'" + address_street1.trim()+ "',")
      + (address_street2 == null ? "NULL," : "'" + address_street2.trim()+ "',")
      + (address_city == null ? "NULL," : "'" + address_city.trim() + "'," )
      + (address_state == null ? "NULL," : "'" + address_state.trim() + "'," )
      + (address_zip == null ? "NULL," : "'" + address_zip.trim() + "'," )
      + (clra_year == 0 ? "NULL," : "" + clra_year + "," )
      + (birthday == null ? "NULL" : "'" + birthday + "'" )
      + ");" );
    out.println();

  } // mapResultSetToMemberSnapshot(ResultSet)

} // ExportMember

/*
 * $Log: ExportMember.java,v $
 * Revision 1.4  2003/02/26 03:38:47  rphall
 * Added copyright and GPL license
 *
 * Revision 1.3  2003/02/19 22:38:54  rphall
 * Removed gratuitous use of CLRA acronym
 *
 * Revision 1.2  2002/02/18 18:07:36  rphall
 * Ran dos2unix to remove ^M (carriage return) from end of lines
 */

