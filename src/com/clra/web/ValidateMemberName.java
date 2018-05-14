/*
 * Copyright (c) Carnegie Lake Rowing Association 2002. All rights reserved.
 * Distributed under the GPL license. See doc/COPYING.
 * $RCSfile: ValidateMemberName.java,v $
 * $Date: 2003/02/26 03:38:46 $
 * $Revision: 1.5 $
 */

package com.clra.web;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;

/**
 * Checks whether a member's name is complete.
 *
 * @version $Id: ValidateMemberName.java,v 1.5 2003/02/26 03:38:46 rphall Exp $
 * @author <a href="mailto:rphall@pluto.njcc.com">Rick Hall</a>
 */
class ValidateMemberName extends ValidateMember {

  public final static String PROPERTY_MEMBERNAME = "accountYear";

  ValidateMemberName( MemberInfoForm f ) {
    super( f );
  }

  /**
   * Checks whether a member's name is complete.
   */
  void validate( ActionErrors errors ) {
    if ( errors == null ) {
      throw new IllegalArgumentException( "null action errors" );
    }

    String messageKey = "validate.member.name.first";
    String value = this.form.getFirstName();
    validateRequiredValue( PROPERTY_MEMBERNAME, messageKey, value, errors );

    messageKey = "validate.member.name.last";
    value = this.form.getLastName();
    validateRequiredValue( PROPERTY_MEMBERNAME, messageKey, value, errors );

  }

} // ValidateMemberName

/*
 * $Log: ValidateMemberName.java,v $
 * Revision 1.5  2003/02/26 03:38:46  rphall
 * Added copyright and GPL license
 *
 * Revision 1.4  2003/02/19 22:38:40  rphall
 * Removed gratuitous use of CLRA acronym
 *
 * Revision 1.3  2003/02/19 03:28:35  rphall
 * Changed final property member to static
 *
 * Revision 1.2  2003/02/18 04:37:39  rphall
 * Working for MEMBEREDIT, ADMINEDIT, ADMINCREATE
 *
 * Revision 1.1  2003/02/11 21:13:15  rphall
 * Separate class for specific validation task; stubbed implementation
 *
 */

