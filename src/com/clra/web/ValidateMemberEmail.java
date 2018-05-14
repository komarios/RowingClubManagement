/*
 * Copyright (c) Carnegie Lake Rowing Association 2002. All rights reserved.
 * Distributed under the GPL license. See doc/COPYING.
 * $RCSfile: ValidateMemberEmail.java,v $
 * $Date: 2003/02/26 03:38:46 $
 * $Revision: 1.3 $
 */

package com.clra.web;

import com.clra.member.Email;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;

/**
 * Checks whether an email address seems valid.
 *
 * @version $Id: ValidateMemberEmail.java,v 1.3 2003/02/26 03:38:46 rphall Exp $
 * @author <a href="mailto:rphall@pluto.njcc.com">Rick Hall</a>
 */
class ValidateMemberEmail extends ValidateMember {

  public final static String PROPERTY_EMAIL = "email";

  ValidateMemberEmail( MemberInfoForm f ) {
    super( f );
  }

  /**
   * Checks whether an email address seems valid.
   */
  void validate( ActionErrors errors ) {
    if ( errors == null ) {
      throw new IllegalArgumentException( "null action errors" );
    }

    String strEmail = form.getEmail();

    if ( strEmail != null
      && strEmail.trim().length() > 0 && !Email.isValidEmail( strEmail ) ) {

      ActionError ae = new ActionError( "validate.member.email.invalid" );
      errors.add( PROPERTY_EMAIL, ae );

    }

    return;
  } // validate(ActionErrors)

} // ValidateMemberEmail

/*
 * $Log: ValidateMemberEmail.java,v $
 * Revision 1.3  2003/02/26 03:38:46  rphall
 * Added copyright and GPL license
 *
 * Revision 1.2  2003/02/19 03:25:25  rphall
 * Fixed bug in validation
 *
 * Revision 1.1  2003/02/11 21:13:15  rphall
 * Separate class for specific validation task; stubbed implementation
 *
 */

