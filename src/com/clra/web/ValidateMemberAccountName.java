/*
 * Copyright (c) Carnegie Lake Rowing Association 2002. All rights reserved.
 * Distributed under the GPL license. See doc/COPYING.
 * $RCSfile: ValidateMemberAccountName.java,v $
 * $Date: 2003/02/26 03:38:46 $
 * $Revision: 1.3 $
 */

package com.clra.web;

import com.clra.member.MemberDBRead;
import com.clra.member.MemberSnapshot;
import com.clra.util.DBConfiguration;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;

/**
 * Checks whether a member's account name is valid and unique.
 *
 * @version $Id: ValidateMemberAccountName.java,v 1.3 2003/02/26 03:38:46 rphall Exp $
 * @author <a href="mailto:rphall@pluto.njcc.com">Rick Hall</a>
 */
class ValidateMemberAccountName extends ValidateMember {

  public final static String PROPERTY_ACCOUNTNAME = "accountname";

  ValidateMemberAccountName( MemberInfoForm f ) {
    super( f );
  }

  /**
   * Checks whether a member's account name is valid and unique.
   */
  void validate( ActionErrors errors ) {

    if ( errors == null ) {
      throw new IllegalArgumentException( "null action errors" );
    }

    String messageKey = "validate.member.accountName.missing";
    String accountName = this.form.getAccountName();
    validateRequiredValue(PROPERTY_ACCOUNTNAME,messageKey,accountName,errors);

    if ( accountName != null && accountName.trim().length() > 0 ) {

      boolean isAvailable = false;
      try {
        Integer memberId = this.form.getId();
        if ( memberId != null ) {
          // Member should already exist in DB, so exclude matches
          // on accountName if the memberId also matches.
          int intMemberId = memberId.intValue();
          isAvailable = isAccountNameAvailable(accountName,intMemberId);
        }
        else {
          // Member should not exist in DB, so any match on accountName
          // indicates a duplicate
          isAvailable = isAccountNameAvailable(accountName);
        }
      }
      catch ( Throwable t ) {
        messageKey = "validate.member.accountName.unexpected";
        ActionError ae = new ActionError(
          messageKey, t.getClass().getName(), t.getMessage() );
        errors.add( ActionErrors.GLOBAL_ERROR, ae );
      } // end catch

      if ( !isAvailable ) {
        messageKey = "validate.member.accountName.duplicate";
        ActionError ae = new ActionError( messageKey, accountName );
        errors.add( PROPERTY_ACCOUNTNAME, ae );
      }
      else {
        messageKey = "validate.member.accountName.ascii";
        validateRestrictedAscii(
          PROPERTY_ACCOUNTNAME, messageKey, accountName, errors );
      }

    } // end accountName not missing

    return;
  } // validate(ActionErrors)

  protected final void validateRestrictedAscii( String PROPERTY,
    String messageKey, String value, ActionErrors errors ) {

    if ( value == null ) {
      throw new IllegalArgumentException( "null value" );
    }

    boolean isValid = true;
    for ( int i=0; isValid && i<value.length(); i++ ) {
      char c = value.charAt(i);
      isValid = ( 'a' <= c && c <= 'z' )
             || ( 'A' <= c && c <= 'Z' )
             || ( '0' <= c && c <= '9' )
             || c == '-' || c == '_' || c == '.' ;
    }

    if ( !isValid ) {
      ActionError ae = new ActionError(messageKey,value);
      errors.add( PROPERTY, ae );
    }

    return;
  } // validateRequiredValue(String,String,ActionErrors)

  /**
   * Checks whether any member uses the given account name. If no
   * such member is found, returns true.</p>
   * <p>
   * Implementation note: Unlike MemberDBRead.findMemberByAccountName(String),
   * this method does not throw an exception when a match is not found.
   * Throwing and then catching an exception is inefficient, and should
   * be used only when exceptions are NOT expected. Here, no-match exceptions
   * are expected, which is why the MemberDBRead method is not used.
   */
  private static boolean isAccountNameAvailable(
    String accountName, int intMemberId ) throws SQLException {

    Connection conn = null;
    PreparedStatement stmt = null;

    boolean retVal = false;
    try {
      conn = DBConfiguration.getConnection();
      stmt = conn.prepareStatement(
          com.clra.member.Configuration.SQL_18,
          ResultSet.TYPE_FORWARD_ONLY,
          ResultSet.CONCUR_READ_ONLY);
      stmt.setString( 1, accountName );
      stmt.setInt( 2, intMemberId );
      retVal = isAccountNameAvailable(stmt);
    }
    finally {
      DBConfiguration.closeSQLStatement( stmt );
      stmt = null;
      DBConfiguration.closeSQLConnection( conn );
      conn = null;
    }

    return retVal;
  } // isAccountNameAvailable(String,int)

  private static boolean isAccountNameAvailable( String accountName )
    throws SQLException {

    Connection conn = null;
    PreparedStatement stmt = null;

    boolean retVal = false;
    try {
      conn = DBConfiguration.getConnection();
      stmt = conn.prepareStatement(
          com.clra.member.Configuration.SQL_05,
          ResultSet.TYPE_FORWARD_ONLY,
          ResultSet.CONCUR_READ_ONLY);
      stmt.setString( 1, accountName );
      retVal = isAccountNameAvailable(stmt);
    }
    finally {
      DBConfiguration.closeSQLStatement( stmt );
      stmt = null;
      DBConfiguration.closeSQLConnection( conn );
      conn = null;
    }

    return retVal;
  } // isAccountNameAvailable(String)

  private static boolean isAccountNameAvailable( PreparedStatement stmt )
    throws SQLException {

    ResultSet rs = null;

    boolean retVal = true;
    try {
      rs = stmt.executeQuery();
      while ( rs.next() && retVal ) {
        retVal = false;
      }
    }
    finally {
      DBConfiguration.closeSQLResultSet( rs );
      rs = null;
    }

    return retVal;
  } // isAccountNameAvailable(String)

} // ValidateMemberAccountName

/*
 * $Log: ValidateMemberAccountName.java,v $
 * Revision 1.3  2003/02/26 03:38:46  rphall
 * Added copyright and GPL license
 *
 * Revision 1.2  2003/02/21 19:18:42  rphall
 * Added SQL for checking duplicate user names during editing
 *
 * Revision 1.1  2003/02/21 05:08:43  rphall
 * Moved ValidateMemberUserName to ValidateMemberAccountName
 *
 * Revision 1.1  2003/02/11 21:13:15  rphall
 * Separate class for specific validation task; stubbed implementation
 *
 */

