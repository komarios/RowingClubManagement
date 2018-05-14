/*
 * Copyright (c) Carnegie Lake Rowing Association 2002. All rights reserved.
 * Distributed under the GPL license. See doc/COPYING.
 * $RCSfile: INamed.java,v $
 * $Date: 2003/02/26 03:38:45 $
 * $Revision: 1.3 $
 */

package com.clra.util;

/**
 * Declares a read-only interface for named objects.
 * Named objects should guarantee that <tt>getName()</tt> returns a
 * non-null and non-blank value (otherwise it doesn't make much sense
 * to declare an object as 'Named'). Implementations should also trim
 * names (i.e. remove leading and trailing whitespace) since whitespace
 * doesn't make much sense in a name.
 *
 * @version $Revision: 1.3 $ $Date: 2003/02/26 03:38:45 $
 * @author <a href="mailto:rphall@pluto.njcc.com">Rick Hall</a>
 */
public interface INamed {

  /**
   * Returns the name of an object.
   * @return a non-null, non-blank, trimmed value.
   * much sense to declare an object as 'named').
   */
  public String getName();

} // INamed

/*
 * $Log: INamed.java,v $
 * Revision 1.3  2003/02/26 03:38:45  rphall
 * Added copyright and GPL license
 *
 * Revision 1.2  2002/02/18 18:05:26  rphall
 * Ran dos2unix to remove ^M (carriage return) from end of lines
 *
 * Revision 1.1.1.1  2002/01/03 21:57:28  rphall
 * Initial load, 5th try, Jan-03-2002 4:57 PM
 *
 * Revision 1.1  2001/12/31 16:11:32  rphall
 * *** empty log message ***
 *
 */

