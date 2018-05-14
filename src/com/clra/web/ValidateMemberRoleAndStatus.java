/*
 * Copyright (c) Carnegie Lake Rowing Association 2002. All rights reserved.
 * Distributed under the GPL license. See doc/COPYING.
 * $RCSfile: ValidateMemberRoleAndStatus.java,v $
 * $Date: 2003/02/26 03:38:46 $
 * $Revision: 1.4 $
 */

package com.clra.web;

import com.clra.member.AccountType;
import com.clra.member.MemberRole;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;

/**
 * Checks whether a member's role and status are valid and consistent.
 *
 * @version $Id: ValidateMemberRoleAndStatus.java,v 1.4 2003/02/26 03:38:46 rphall Exp $
 * @author <a href="mailto:rphall@pluto.njcc.com">Rick Hall</a>
 */
class ValidateMemberRoleAndStatus extends ValidateMember {

  public final static String PROPERTY_TYPE_ROLES = "accountTypeAndRoles";

  ValidateMemberRoleAndStatus( MemberInfoForm f ) {
    super( f );
  }

  /**
   * Checks whether a member's roles and account-type are valid and consistent.
   */
  void validate( ActionErrors errors ) {

    if ( errors == null ) {
      throw new IllegalArgumentException( "null action errors" );
    }
    
    String messageKey = "validate.member.accountType.missing";
    String strAccountType = this.form.getAccountTypeStr();
    validateRequiredValue(PROPERTY_TYPE_ROLES,messageKey,strAccountType,errors);

    AccountType accountType = null;
    if ( strAccountType != null && strAccountType.trim().length() > 0 ) {
      try {
        accountType = new AccountType( strAccountType );
      }
      catch ( IllegalArgumentException x ) {
        messageKey = "validate.member.accountType.invalid";
        ActionError ae = new ActionError( messageKey, strAccountType );
        errors.add( PROPERTY_TYPE_ROLES, ae );
      }
    }

    MemberRole[] memberRoles = this.form.getMemberRoles();
    if ( memberRoles.length > 0 && accountType != null
      && AccountType.DUPLICATE.equals(accountType) ) {

        messageKey = "validate.member.accountType.duplicateWithRoles";
        ActionError ae = new ActionError( messageKey );
        errors.add( PROPERTY_TYPE_ROLES, ae );

    } // if duplicate

    return;
  } // validate(ActionErrors)

} // ValidateMemberRoleAndStatus

/*
 * $Log: ValidateMemberRoleAndStatus.java,v $
 * Revision 1.4  2003/02/26 03:38:46  rphall
 * Added copyright and GPL license
 *
 * Revision 1.3  2003/02/21 19:16:06  rphall
 * First non-stubbed impl: checks that DUPLICATE accounts have no roles
 *
 * Revision 1.2  2003/02/20 04:06:09  rphall
 * Stubbed versions
 *
 * Revision 1.1  2003/02/11 21:13:15  rphall
 * Separate class for specific validation task; stubbed implementation
 *
 */

