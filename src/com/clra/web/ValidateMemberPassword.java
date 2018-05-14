/*
 * Copyright (c) Carnegie Lake Rowing Association 2002. All rights reserved.
 * Distributed under the GPL license. See doc/COPYING.
 * $RCSfile: ValidateMemberPassword.java,v $
 * $Date: 2003/02/26 03:38:46 $
 * $Revision: 1.3 $
 */

package com.clra.web;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;

/**
 * Checks whether a password matches its confirmation value.
 *
 * @version $Id: ValidateMemberPassword.java,v 1.3 2003/02/26 03:38:46 rphall Exp $
 * @author <a href="mailto:rphall@pluto.njcc.com">Rick Hall</a>
 */
class ValidateMemberPassword extends ValidateMember {

  public final static String PROPERTY_PASSWORD = "password";

  ValidateMemberPassword( MemberInfoForm f ) {
    super( f );
  }

  /**
   * Checks whether a password matches its confirmation value.
   */
  void validate( ActionErrors errors ) {
    if ( errors == null ) {
      throw new IllegalArgumentException( "null action errors" );
    }

    String messageKey = "validate.member.password.missing";
    String password = this.form.getAccountPassword();
    validateRequiredValue( PROPERTY_PASSWORD, messageKey, password, errors );

    messageKey = "validate.member.password.confirm";
    String confirm = this.form.getConfirmPassword();
    validateRequiredValue( PROPERTY_PASSWORD, messageKey, confirm, errors );

    if ( password != null && confirm != null && !password.equals(confirm) ) {
      messageKey = "validate.member.password.notconfirmed";
      ActionError ae = new ActionError( messageKey );
      errors.add( PROPERTY_PASSWORD, ae );
    }

  }

} // ValidateMemberPassword

/*
 * $Log: ValidateMemberPassword.java,v $
 * Revision 1.3  2003/02/26 03:38:46  rphall
 * Added copyright and GPL license
 *
 * Revision 1.2  2003/02/19 03:31:14  rphall
 * Implemented validation; removed stubbing
 *
 * Revision 1.1  2003/02/11 21:13:15  rphall
 * Separate class for specific validation task; stubbed implementation
 *
 */

