/*
 * Copyright (c) Carnegie Lake Rowing Association 2002. All rights reserved.
 * Distributed under the GPL license. See doc/COPYING.
 * $RCSfile: INameList.java,v $
 * $Date: 2003/02/26 03:38:46 $
 * $Revision: 1.3 $
 */

package com.clra.web;

import com.clra.util.ISerializableComparator;
import java.util.Iterator;

/**
 * Declares JSP attributes and bean properties for alphabetized lists of
 * stuff. Views that implement this interface may be controlled via components
 * such as NameSelector.<p>
 *
 * For Java Server Pages, this interface declares three attributes that should
 * be set and accessed within the scope of a request or session:<ul>
 * <li><strong>restricted</strong>: is the view restricted to a range of
 * items with names that start with a particular group of letters?
 * [true,false]</li>
 * <li><strong>group</strong>: if the view is restricted, the group of letters
 * with the name of an item should begin.
 * <li><strong>comparator</strong>: specifies a bean that implements
 * <tt>java.util.Comparator</tt> which can compare instances of
 * INamed. The current implementation of NameSelector does not
 * use the value of this attribute.</li>
 * </ul>
 *
 * <p>The Integer value specified by <tt>group</tt> refers to predefined
 * groups:<ol>
 * <li> ABC  - names less than 'D'</li>
 * <li> DEF  - names between 'D' and 'F'</li>
 * <li> GHI  - names between 'G' and 'I'</li>
 * <li> JKL  - names between 'J' and 'L'</li>
 * <li> MNO  - names between 'M' and 'O'</li>
 * <li> PQRS - names between 'P' and 'S'</li>
 * <li> TUV  - names between 'T' and 'V'</li>
 * <li> WXYZ - names greater than or equal to 'W'</li>
 * </ol>
 * Note that the first group, <tt>ABC</tt>, may contain names like "3Com",
 * whereas the last group, <tt>WXYZ</tt>, may contain names like "Ëtvos".
 * Also note that the group index is indexed from '1', not '0'.</p>
 *
 * For beans, this interface declares five properties:<ul>
 * <li><strong>restricted</strong>: read/write Boolean</li>
 * <li><strong>group</strong>: read/write Integer</li>
 * <li><strong>comparator</strong>: read/write java.util.Comparator, compares
 * instances of INamed</li>
 * <li><strong>iterator</strong>: read-only iterator of INamed
 * instances</li>
 * </ul>
 *
 * @version $Revision: 1.3 $ $Date: 2003/02/26 03:38:46 $
 * @author <a href="mailto:rphall@pluto.njcc.com">Rick Hall</a>
 */
public interface INameList {

  /**
   * Names an attribute within the scope of an HTTP request or session
   * whose Boolean value indicates whether a list should be restricted to
   * events that are scheduled to start within a particular month and year.
   * A blank, null or missing value should default to non-restricted.
   */
  public final static String AN_ISRESTRICTED = "namerestricted";

  /**
   * Names an attribute within the scope of an HTTP request or session
   * whose char[] value indicates the letters that with which a item
   * name should begin within a restricted list.
   */
  public final static String AN_GROUP = "namegroup";

  /**
   * Names an attribute within the scope of an HTTP request or session
   * that holds a bean which implements <tt>java.util.Comparator</tt>.
   * The comparator must be able to operate on instances of
   * <tt>INamed</tt>. Current implementations ignore this
   * attribute, but it is reserved for future enhancements.
   */
  public final static String AN_COMPARATOR = "namecomparator";

  /**
   * Returns an iterator over a collection of INamed beans.
   * The behavior of the iterator is specified by setting 
   * <tt>restricted</tt>, <tt>month</tt>, <tt>year</tt>, and
   * <tt>comparator</tt> properties before the iterator is requested.
   */
  public Iterator getIterator() throws WebException;

  /**
   * Returns a flag that indicates whether an iterator will be restricted
   * to events that are scheduled to start within a particular month
   * and year. A null value indicates behavior that is
   * implementation-specific.
   */
  public Boolean getRestricted();

  /**
   * Sets whether an iterator will be restricted to events that start
   * within a particular month and year. A null value specifies
   * implementation-specific behavior.
   */
  public void setRestricted( Boolean restricted );

  /**
   * Returns the group of letters to which an iteration is restricted
   * (if it is restricted). If the return value is non-null, it must
   * be in the range 1 - 8, inclusive.
   */
  public Integer getGroup();

  /**
   * Sets the group to which an iteration is restricted (if it is
   * restricted). If the input value is non-null, it must be in the
   * range 1 - 8, inclusive.
   */
  public void setGroup( Integer group );

  /**
   * Returns the sort-order that an iterator will use. Valid comparators
   * must be able to compare instances of INamed. A null comparator
   * indicates the natural sort order of the events will be used.
   */
  public ISerializableComparator getComparator();

  /**
   * Sets the sort-order that an iterator will use. Valid comparators
   * must be able to compare instances of INamed. A null comparator
   * indicates the natural sort order of the events will be used.
   */
  public void setComparator( ISerializableComparator comparator );

} // INameList

/*
 * $Log: INameList.java,v $
 * Revision 1.3  2003/02/26 03:38:46  rphall
 * Added copyright and GPL license
 *
 * Revision 1.2  2002/02/18 18:06:05  rphall
 * Ran dos2unix to remove ^M (carriage return) from end of lines
 *
 * Revision 1.1.1.1  2002/01/03 21:57:28  rphall
 * Initial load, 5th try, Jan-03-2002 4:57 PM
 *
 * Revision 1.2  2002/01/01 03:40:33  rphall
 * Moved getName() from MemberName to MemberView
 *
 * Revision 1.1  2002/01/01 00:52:22  rphall
 * *** empty log message ***
 *
 */

