/*
 * Copyright (c) Carnegie Lake Rowing Association 2002. All rights reserved.
 * Distributed under the GPL license. See doc/COPYING.
 * $RCSfile: ValidateMemberMailingAddress.java,v $
 * $Date: 2003/02/26 03:38:46 $
 * $Revision: 1.4 $
 */

package com.clra.web;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;

/**
 * Checks whether a mailing address seems valid.
 *
 * @version $Id: ValidateMemberMailingAddress.java,v 1.4 2003/02/26 03:38:46 rphall Exp $
 * @author <a href="mailto:rphall@pluto.njcc.com">Rick Hall</a>
 */
class ValidateMemberMailingAddress extends ValidateMember {

  public final static String PROPERTY_MAILINGADDR = "address";

  ValidateMemberMailingAddress( MemberInfoForm f ) {
    super( f );
  }

  /**
   * Checks whether a mailing address seems valid.
   */
  void validate( ActionErrors errors ) {
    if ( errors == null ) {
      throw new IllegalArgumentException( "null action errors" );
    }

    String messageKey = "validate.member.address.street1";
    String value = this.form.getStreet1();
    validateRequiredValue( PROPERTY_MAILINGADDR, messageKey, value, errors );

    messageKey = "validate.member.address.city";
    value = this.form.getCity();
    validateRequiredValue( PROPERTY_MAILINGADDR, messageKey, value, errors );

    messageKey = "validate.member.address.state";
    value = this.form.getState();
    validateRequiredValue( PROPERTY_MAILINGADDR, messageKey, value, errors );

    messageKey = "validate.member.address.zip";
    value = this.form.getZip();
    validateRequiredValue( PROPERTY_MAILINGADDR, messageKey, value, errors );
    if ( value != null && value.trim().length() > 0 ) {
      validateZipCodeDetails( value, errors );
    }
  }

  private void validateZipCodeDetails( String value, ActionErrors errors ) {

    value = value.trim();

    if ( value.length() == 5 ) {

      boolean isOK = true;
      for ( int i=0; i<5; i++ ) {
        char c = value.charAt(i);
        isOK = Character.isDigit(c);
        if ( !isOK ) {
          break;
        }
      }
      if ( !isOK ) {
        ActionError ae = new ActionError(
          "validate.member.address.zipValue", value );
        errors.add( PROPERTY_MAILINGADDR, ae );
      }

    }
    else if ( value.length() == 10 ) {

      boolean isOK = true;
      for ( int i=0; i<5; i++ ) {
        char c = value.charAt(i);
        isOK = Character.isDigit(c);
        if ( !isOK ) {
          break;
        }
      }

      if ( isOK ) {
        isOK = value.charAt(5) == '-';
        if ( !isOK ) {
          ActionError ae = new ActionError(
            "validate.member.address.zipValue", value );
          errors.add( PROPERTY_MAILINGADDR, ae );
        }
      }

      if ( isOK ) {
        for ( int i=6; i<10; i++ ) {
          char c = value.charAt(i);
          isOK = Character.isDigit(c);
          if ( !isOK ) {
            break;
          }
        }
      }

      if ( !isOK ) {
        ActionError ae = new ActionError(
          "validate.member.address.zipValue", value );
        errors.add( PROPERTY_MAILINGADDR, ae );
      }

    }
    else {
      ActionError ae = new ActionError(
        "validate.member.address.zipValue", value );
      errors.add( PROPERTY_MAILINGADDR, ae );
    }

    return;
  } // validateZipCodeDetails(String,ActionErrors)

} // ValidateMemberMailingAddress

/*
 * $Log: ValidateMemberMailingAddress.java,v $
 * Revision 1.4  2003/02/26 03:38:46  rphall
 * Added copyright and GPL license
 *
 * Revision 1.3  2003/02/19 03:27:35  rphall
 * Fixed bug with null memberId during MemberEdit
 *
 * Revision 1.2  2003/02/18 04:37:39  rphall
 * Working for MEMBEREDIT, ADMINEDIT, ADMINCREATE
 *
 * Revision 1.1  2003/02/11 21:13:15  rphall
 * Separate class for specific validation task; stubbed implementation
 *
 */

