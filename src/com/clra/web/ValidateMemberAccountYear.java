/*
 * Copyright (c) Carnegie Lake Rowing Association 2002. All rights reserved.
 * Distributed under the GPL license. See doc/COPYING.
 * $RCSfile: ValidateMemberAccountYear.java,v $
 * $Date: 2003/02/26 03:38:46 $
 * $Revision: 1.3 $
 */

package com.clra.web;

import java.util.Calendar;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;

/**
 * Checks whether the year a member joined the rowing association is reasonable.
 *
 * @version $Id: ValidateMemberAccountYear.java,v 1.3 2003/02/26 03:38:46 rphall Exp $
 * @author <a href="mailto:rphall@pluto.njcc.com">Rick Hall</a>
 */
class ValidateMemberAccountYear extends ValidateMember {

  public final static String PROPERTY_ACCOUNTYEAR = "accountYear";

  public final int FIRST_YEAR =
    Integer.parseInt( Configuration.ACCOUNT_FIRSTYEAR );

  // This static definition means the app has to be restarted at least
  // once per year. Seems eminently reasonable.
  public final int THIS_YEAR = Calendar.getInstance().get( Calendar.YEAR );

  public final String STR_THIS_YEAR = "" + THIS_YEAR;

  ValidateMemberAccountYear( MemberInfoForm f ) {
    super( f );
  }

  /**
   * Checks whether the year a member joined the rowing association
   * is reasonable.
   */
  void validate( ActionErrors errors ) {
    if ( errors == null ) {
      throw new IllegalArgumentException( "null action errors" );
    }
    final int accountYear = form.getAccountYear();
    if ( accountYear < FIRST_YEAR ) {
      ActionError ae =
        new ActionError( "validate.member.accountYear.lowYear" );
      errors.add( PROPERTY_ACCOUNTYEAR, ae );
    }
    else if ( THIS_YEAR < accountYear ) {
      String strThisYear = "" + STR_THIS_YEAR;
      ActionError ae =
        new ActionError( "validate.member.accountYear.lowYear", strThisYear );
      errors.add( PROPERTY_ACCOUNTYEAR, ae );
    }
    return;
  } // validate(ActionErrors)

} // ValidateMemberAccountYear

/*
 * $Log: ValidateMemberAccountYear.java,v $
 * Revision 1.3  2003/02/26 03:38:46  rphall
 * Added copyright and GPL license
 *
 * Revision 1.2  2003/02/20 04:35:13  rphall
 * Removed gratuitous use of CLRA acronym
 *
 * Revision 1.1  2003/02/19 20:45:11  rphall
 * Moved to ValidateMemberAccountYear
 *
 * Revision 1.2  2003/02/19 03:24:22  rphall
 * Implemented validation
 *
 * Revision 1.1  2003/02/11 21:13:15  rphall
 * Separate class for specific validation task; stubbed implementation
 *
 */

