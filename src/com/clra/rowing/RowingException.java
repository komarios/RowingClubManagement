/*
 * Copyright (c) Carnegie Lake Rowing Association 2002. All rights reserved.
 * Distributed under the GPL license. See doc/COPYING.
 * $RCSfile: RowingException.java,v $
 * $Date: 2003/02/26 03:38:45 $
 * $Revision: 1.3 $
 */

package com.clra.rowing;

/**
 * Indicates a violation of business rules related to the rowing package.</p>

 * <p> Note that an invalid transistion within a entity state diagram is
 * indicated by an entity-specific exception, such as BoatingStateException
 * or RowingSessionStateException.
 *
 * @version $Id: RowingException.java,v 1.3 2003/02/26 03:38:45 rphall Exp $
 * @author <a href="mailto:rphall@pluto.njcc.com">Rick Hall</a>
 */
public class RowingException extends Exception {

  public RowingException() {}
  public RowingException(String msg) { super(msg); }

} // RowingException

/*
 * $Log: RowingException.java,v $
 * Revision 1.3  2003/02/26 03:38:45  rphall
 * Added copyright and GPL license
 *
 * Revision 1.2  2002/02/18 18:04:52  rphall
 * Ran dos2unix to remove ^M (carriage return) from end of lines
 *
 * Revision 1.1.1.1  2002/01/03 21:57:28  rphall
 * Initial load, 5th try, Jan-03-2002 4:57 PM
 *
 * Revision 1.2  2001/12/17 00:40:59  rphall
 * Documentation
 *
 * Revision 1.1  2001/11/18 17:07:08  rphall
 * Checkpt before major revision of rowing package
 *
 */

