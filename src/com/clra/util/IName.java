/*
 * Copyright (c) Carnegie Lake Rowing Association 2002. All rights reserved.
 * Distributed under the GPL license. See doc/COPYING.
 * $RCSfile: IName.java,v $
 * $Date: 2003/02/26 03:38:45 $
 * $Revision: 1.3 $
 */

package com.clra.util;

/**
 * Declares a read-write interface for named objects. 
 *
 * @version $Revision: 1.3 $ $Date: 2003/02/26 03:38:45 $
 * @author <a href="mailto:rphall@pluto.njcc.com">Rick Hall</a>
 */
public interface IName extends INamed {

  /**
   * Sets the name of an object. Implementations should ensure the passed
   * value is not null and is not blank after trimmming. (Only trimmed
   * values should be used.)
   * @param name a non-blank, non-empty value.
   */
  public void setName( String name );

} // IName

/*
 * $Log: IName.java,v $
 * Revision 1.3  2003/02/26 03:38:45  rphall
 * Added copyright and GPL license
 *
 * Revision 1.2  2002/02/18 18:05:24  rphall
 * Ran dos2unix to remove ^M (carriage return) from end of lines
 *
 * Revision 1.1.1.1  2002/01/03 21:57:28  rphall
 * Initial load, 5th try, Jan-03-2002 4:57 PM
 *
 * Revision 1.2  2002/01/01 03:40:33  rphall
 * Moved getName() from MemberName to MemberView
 *
 * Revision 1.1  2001/12/31 16:11:32  rphall
 * *** empty log message ***
 *
 * Revision 1.1  2001/12/09 05:59:36  rphall
 * An element of INameList.java
 *
 */

