/*
 * Copyright (c) Carnegie Lake Rowing Association 2002. All rights reserved.
 * Distributed under the GPL license. See doc/COPYING.
 * $RCSfile: ValidateMemberPhoneNumbers.java,v $
 * $Date: 2003/02/26 03:38:46 $
 * $Revision: 1.4 $
 */

package com.clra.web;

import com.clra.member.Telephone;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;

/**
 * Checks whether the member has included at least an evening phone number
 * and whether all included phone numbers seem reasonable.
 *
 * @version $Id: ValidateMemberPhoneNumbers.java,v 1.4 2003/02/26 03:38:46 rphall Exp $
 * @author <a href="mailto:rphall@pluto.njcc.com">Rick Hall</a>
 */
class ValidateMemberPhoneNumbers extends ValidateMember {

  public final static String PROPERTY_PHONE = "phone";

  ValidateMemberPhoneNumbers( MemberInfoForm f ) {
    super( f );
  }

  /**
   * Checks whether the member has included at least an evening phone number
   * and whether all included phone numbers seem reasonable.
   */
  void validate( ActionErrors errors ) {
    if ( errors == null ) {
      throw new IllegalArgumentException( "null action errors" );
    }

    ActionError ae = null;

    // Evening phone
    String areacode  = this.form.getPhoneEveningAreaCode();
    String exchange  = this.form.getPhoneEveningExchange();
    String local     = this.form.getPhoneEveningLocal();
    String extension = this.form.getPhoneEveningExt();
    if ( isPhoneCompletelyBlank(areacode,exchange,local,extension) ) {
      ae = new ActionError( "validate.member.phone.evening.missing" );
      errors.add( PROPERTY_PHONE, ae );
    }
    else {
      if ( !Telephone.isValidAreaCode(areacode) ) {
        ae = new ActionError("validate.member.phone.evening.areacode",areacode);
        errors.add( PROPERTY_PHONE, ae );
      }
      if ( !Telephone.isValidExchange(exchange) ) {
        ae = new ActionError("validate.member.phone.evening.exchange",exchange);
        errors.add( PROPERTY_PHONE, ae );
      }
      if ( !Telephone.isValidLocal(local) ) {
        ae = new ActionError("validate.member.phone.evening.local",local);
        errors.add( PROPERTY_PHONE, ae );
      }
      if ( !Telephone.isValidExtension(extension) ) {
        ae=new ActionError("validate.member.phone.evening.extension",extension);
        errors.add( PROPERTY_PHONE, ae );
      }
    }

    // Day phone
    areacode  = this.form.getPhoneDayAreaCode();
    exchange  = this.form.getPhoneDayExchange();
    local     = this.form.getPhoneDayLocal();
    extension = this.form.getPhoneDayExt();
    if ( !isPhoneCompletelyBlank(areacode,exchange,local,extension) ) {
      if ( !Telephone.isValidAreaCode(areacode) ) {
        ae = new ActionError("validate.member.phone.day.areacode",areacode);
        errors.add( PROPERTY_PHONE, ae );
      }
      if ( !Telephone.isValidExchange(exchange) ) {
        ae = new ActionError("validate.member.phone.day.exchange",exchange);
        errors.add( PROPERTY_PHONE, ae );
      }
      if ( !Telephone.isValidLocal(local) ) {
        ae = new ActionError("validate.member.phone.day.local",local);
        errors.add( PROPERTY_PHONE, ae );
      }
      if ( !Telephone.isValidExtension(extension) ) {
        ae=new ActionError("validate.member.phone.day.extension",extension);
        errors.add( PROPERTY_PHONE, ae );
      }
    }

    // Other phone
    areacode  = this.form.getPhoneOtherAreaCode();
    exchange  = this.form.getPhoneOtherExchange();
    local     = this.form.getPhoneOtherLocal();
    extension = this.form.getPhoneOtherExt();
    if ( !isPhoneCompletelyBlank(areacode,exchange,local,extension) ) {
      if ( !Telephone.isValidAreaCode(areacode) ) {
        ae = new ActionError("validate.member.phone.other.areacode",areacode);
        errors.add( PROPERTY_PHONE, ae );
      }
      if ( !Telephone.isValidExchange(exchange) ) {
        ae = new ActionError("validate.member.phone.other.exchange",exchange);
        errors.add( PROPERTY_PHONE, ae );
      }
      if ( !Telephone.isValidLocal(local) ) {
        ae = new ActionError("validate.member.phone.other.local",local);
        errors.add( PROPERTY_PHONE, ae );
      }
      if ( !Telephone.isValidExtension(extension) ) {
        ae=new ActionError("validate.member.phone.other.extension",extension);
        errors.add( PROPERTY_PHONE, ae );
      }
    }

  }

  private static boolean
  isPhoneCompletelyBlank(String s0, String s1, String s2, String s3 ) {

    boolean retVal = true;

    retVal = retVal && ( s0 == null || s0.trim().length() == 0 );
    retVal = retVal && ( s1 == null || s1.trim().length() == 0 );
    retVal = retVal && ( s2 == null || s2.trim().length() == 0 );
    retVal = retVal && ( s3 == null || s3.trim().length() == 0 );

    return retVal;
  } // isPhoneCompletelyBlank(..)

} // ValidateMemberPhoneNumbers

/*
 * $Log: ValidateMemberPhoneNumbers.java,v $
 * Revision 1.4  2003/02/26 03:38:46  rphall
 * Added copyright and GPL license
 *
 * Revision 1.3  2003/02/21 15:09:17  rphall
 * More fixes to blank extension bug
 *
 * Revision 1.2  2003/02/19 03:32:21  rphall
 * Implemented validation; removed stubbing
 *
 * Revision 1.1  2003/02/11 21:13:15  rphall
 * Separate class for specific validation task; stubbed implementation
 *
 */

