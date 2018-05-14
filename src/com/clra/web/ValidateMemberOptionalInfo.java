/*
 * Copyright (c) Carnegie Lake Rowing Association 2002. All rights reserved.
 * Distributed under the GPL license. See doc/COPYING.
 * $RCSfile: ValidateMemberOptionalInfo.java,v $
 * $Date: 2003/02/26 03:38:46 $
 * $Revision: 1.3 $
 */

package com.clra.web;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;

/**
 * Checks whether optional info is plausible, if it is present.
 *
 * @version $Id: ValidateMemberOptionalInfo.java,v 1.3 2003/02/26 03:38:46 rphall Exp $
 * @author <a href="mailto:rphall@pluto.njcc.com">Rick Hall</a>
 */
class ValidateMemberOptionalInfo extends ValidateMember {

  ValidateMemberOptionalInfo( MemberInfoForm f ) {
    super( f );
  }

  /**
   * Checks whether optional info is plausible, if it is present.
   */
  void validate( ActionErrors errors ) {
    if ( errors == null ) {
      throw new IllegalArgumentException( "null action errors" );
    }
    // System.out.println( this.getClass().getName() + ": not implemented" );
  }

} // ValidateMemberOptionalInfo

/*
 * $Log: ValidateMemberOptionalInfo.java,v $
 * Revision 1.3  2003/02/26 03:38:46  rphall
 * Added copyright and GPL license
 *
 * Revision 1.2  2003/02/20 04:06:08  rphall
 * Stubbed versions
 *
 * Revision 1.1  2003/02/11 21:13:15  rphall
 * Separate class for specific validation task; stubbed implementation
 *
 */

