/*
 * Copyright (c) Carnegie Lake Rowing Association 2002. All rights reserved.
 * Distributed under the GPL license. See doc/COPYING.
 * $RCSfile: ValidateMember.java,v $
 * $Date: 2003/02/26 03:38:46 $
 * $Revision: 1.3 $
 */

package com.clra.web;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionError;
import java.util.ArrayList;
import java.util.List;

/**
 * Base class for validation checks of MemberInfoForm.
 *
 * @version $Id: ValidateMember.java,v 1.3 2003/02/26 03:38:46 rphall Exp $
 * @author <a href="mailto:rphall@pluto.njcc.com">Rick Hall</a>
 */
abstract class ValidateMember {

  /** The form that is being validated */
  protected final MemberInfoForm form;

  ValidateMember( MemberInfoForm f ) {
    this.form = f;
    if ( f == null ) {
      throw new IllegalArgumentException( "null form" );
    }
  }

  /**
   * Subclasses must define this method to check whether data contained in
   * a form is valid within some validation context.
   * @param errors a non-null ActionErrors object to which subclasses should
   * add error messages (if the form is invalid).
   */
  abstract void validate( ActionErrors errors );

  protected final void validateRequiredValue( String PROPERTY,
    String messageKey, String value, ActionErrors errors ) {

    if ( value == null || value.trim().length() == 0 ) {
      ActionError ae = new ActionError(messageKey);
      errors.add( PROPERTY, ae );
    }

    return;
  } // validateRequiredValue(String,String,ActionErrors)

} // ValidateMember

/*
 * $Log: ValidateMember.java,v $
 * Revision 1.3  2003/02/26 03:38:46  rphall
 * Added copyright and GPL license
 *
 * Revision 1.2  2003/02/18 04:37:39  rphall
 * Working for MEMBEREDIT, ADMINEDIT, ADMINCREATE
 *
 * Revision 1.1  2003/02/11 21:13:15  rphall
 * Separate class for specific validation task; stubbed implementation
 *
 */

