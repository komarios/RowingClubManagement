/*
 * Copyright (c) Carnegie Lake Rowing Association 2002. All rights reserved.
 * Distributed under the GPL license. See doc/COPYING.
 * $RCSfile: IMemberNameList.java,v $
 * $Date: 2003/02/26 03:38:46 $
 * $Revision: 1.3 $
 */

package com.clra.web;

/**
 * Declares JSP attributes and bean properties for alphabetized lists of
 * members. Views that implement this interface may be controlled via components
 * such as NameSelector. This interface is an extension of INameList.<p>
 *
 * For Java Server Pages, this interface declares one attribute (in addition
 * to the attributes declared by INameList) that should
 * be set and accessed within the scope of a request or session:<ul>
 * <li><strong>membernameformat</strong>: the format with which member names
 * are presented and sorted. [MemberNameFormat.FIRSTLAST]</li>
 * </ul>
 *
 * For beans, this interface declares one property (in addition to the
 * properties declared by INameList):<ul>
 * <li><strong>memberNameFormat</strong>: read/write MemberNameFormat</li>
 * </ul>
 *
 * @version $Revision: 1.3 $ $Date: 2003/02/26 03:38:46 $
 * @author <a href="mailto:rphall@pluto.njcc.com">Rick Hall</a>
 */
public interface IMemberNameList extends INameList {

  /**
   * Names an attribute within the scope of an HTTP request or session
   * that determines how member names are presented and sorted. The
   * value of this attribute should be an instance of MemberNameFormat.
   * A null value indicates the default, <tt>MemberNameFormat.FIRSTLAST</tt>.
   */
  public final static String AN_MEMBERNAMEFORMAT = "membernameformat";

  /**
   * Returns a String that indicates whether an iterator will be sorted
   * by first or last name.
   * @return <tt>FIRST</tt> or <tt>LAST</tt>.
   */
  public MemberNameFormat getMemberNameFormat();

  /**
   * Sets whether an iterator will be sorted by first or last name. The
   * argument can not be null. The argument is trimmed of any leading or
   * trailing whitespace, then compared in a case-insensitive manner to
   * <tt>FIRST</tt> and <tt>LAST</tt>. If the normalized value matches
   * either, then that value is set. Otherwise an IllegalArgumentException
   * is thrown.
   */
  public void setMemberNameFormat( MemberNameFormat memberNameFormat );

} // IMemberNameList

/*
 * $Log: IMemberNameList.java,v $
 * Revision 1.3  2003/02/26 03:38:46  rphall
 * Added copyright and GPL license
 *
 * Revision 1.2  2002/02/18 18:06:03  rphall
 * Ran dos2unix to remove ^M (carriage return) from end of lines
 *
 * Revision 1.1.1.1  2002/01/03 21:57:28  rphall
 * Initial load, 5th try, Jan-03-2002 4:57 PM
 *
 * Revision 1.1  2002/01/01 18:54:07  rphall
 * Selects first or last name
 *
 */

