/*
 * Copyright (c) Carnegie Lake Rowing Association 2002. All rights reserved.
 * Distributed under the GPL license. See doc/COPYING.
 * $RCSfile: ErrorUtils.java,v $
 * $Date: 2003/02/26 03:38:45 $
 * $Revision: 1.3 $
 */

package com.clra.util;

/**
 * Some common error handling tasks.
 */
public final class ErrorUtils {

  private ErrorUtils() {}

  /**
   * Creates a debugging message, suitable as a logging message. The message
   * is not suitable to display to a user, since it is too low-level.
   * @param t an error
   */
  public static String createDbgMsg( Throwable t ) {
    String retVal = "";
    if ( t != null ) {
      retVal = t.getClass().getName() + ": " + t.getMessage();
    }
    return retVal;
  }

  /**
   * Creates a debugging message, suitable as a logging message. The message
   * is not suitable to display to a user, since it is too low-level.
   * @param context A string that identifies where an error occurred
   * @param t an error
   */
  public static String createDbgMsg( String context, Throwable t ) {
    String retVal;
    if ( context != null ) {
      retVal = context + ": " + createDbgMsg(t);
    }
    else {
      retVal = createDbgMsg(t);
    }
    return retVal;
  } // createDbgMsg(String,Throwable)

  /**
   * Mark a code branch as impossible to reach (assuming a correct design).
   * @exception throws a Error if executed.
   */
  public void impossible() {
    throw new Error( "design error" );
  }

} // ErrorUtils

/*
 * $Log: ErrorUtils.java,v $
 * Revision 1.3  2003/02/26 03:38:45  rphall
 * Added copyright and GPL license
 *
 * Revision 1.2  2002/02/18 18:05:19  rphall
 * Ran dos2unix to remove ^M (carriage return) from end of lines
 *
 * Revision 1.1.1.1  2002/01/03 21:57:28  rphall
 * Initial load, 5th try, Jan-03-2002 4:57 PM
 *
 * Revision 1.1  2001/12/18 13:33:09  rphall
 * Error handling utilities
 *
 */

