/*
 * Copyright (c) Carnegie Lake Rowing Association 2002. All rights reserved.
 * Distributed under the GPL license. See doc/COPYING.
 * $RCSfile: BoatingStateException.java,v $
 * $Date: 2003/02/26 03:38:45 $
 * $Revision: 1.3 $
 */

package com.clra.rowing;

/**
 * Indicates a boating is in an inconsistent state, or that a boating
 * operation is invoked in an inappropriate state
 *
 * @version $Id: BoatingStateException.java,v 1.3 2003/02/26 03:38:45 rphall Exp $
 * @author <a href="mailto:rphall@pluto.njcc.com">Rick Hall</a>
 */
public class BoatingStateException extends Exception {

  public BoatingStateException() {}
  public BoatingStateException(String msg) { super(msg); }

} // BoatingStateException

/*
 * $Log: BoatingStateException.java,v $
 * Revision 1.3  2003/02/26 03:38:45  rphall
 * Added copyright and GPL license
 *
 * Revision 1.2  2002/02/18 18:03:50  rphall
 * Ran dos2unix to remove ^M (carriage return) from end of lines
 *
 * Revision 1.1.1.1  2002/01/03 21:57:28  rphall
 * Initial load, 5th try, Jan-03-2002 4:57 PM
 *
 * Revision 1.1  2001/11/28 12:21:31  rphall
 * More specific exception class
 *
 */

