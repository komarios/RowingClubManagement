/*
 * Copyright (c) Carnegie Lake Rowing Association 2002. All rights reserved.
 * Distributed under the GPL license. See doc/COPYING.
 * $RCSfile: MemberException.java,v $
 * $Date: 2003/02/26 03:38:45 $
 * $Revision: 1.3 $
 */

package com.clra.member;

/**
 * Indicates an error during retrieval or storage of Member data.
 *
 * @version $Id: MemberException.java,v 1.3 2003/02/26 03:38:45 rphall Exp $
 * @author <a href="mailto:rphall@pluto.njcc.com">Rick Hall</a>
 */
public class MemberException extends Exception {

  public MemberException() {}
  public MemberException(String msg) { super(msg); }

} // MemberException

/*
 * $Log: MemberException.java,v $
 * Revision 1.3  2003/02/26 03:38:45  rphall
 * Added copyright and GPL license
 *
 * Revision 1.2  2002/02/18 18:03:19  rphall
 * Ran dos2unix to remove ^M (carriage return) from end of lines
 *
 * Revision 1.1.1.1  2002/01/03 21:57:28  rphall
 * Initial load, 5th try, Jan-03-2002 4:57 PM
 *
 * Revision 1.1  2001/11/18 17:01:35  rphall
 * Exception that indicate error processing member data
 *
 */

