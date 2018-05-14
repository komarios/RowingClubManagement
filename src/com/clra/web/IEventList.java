/*
 * Copyright (c) Carnegie Lake Rowing Association 2002. All rights reserved.
 * Distributed under the GPL license. See doc/COPYING.
 * $RCSfile: IEventList.java,v $
 * $Date: 2003/02/26 03:38:46 $
 * $Revision: 1.3 $
 */

package com.clra.web;

import com.clra.util.ISerializableComparator;
import java.util.Iterator;

/**
 * Declares JSP attributes and bean properties for sorted lists of scheduled
 * events. Views that implement this interface may be controlled via components
 * such as MonthViewSelector and YearViewSelector.<p>
 *
 * For Java Server Pages, this interface declares four attributes that should
 * be set and accessed within the scope of a request or session:<ul>
 * <li><strong>restricted</strong>: is the view restricted to a range of
 * dates within a particular month and year? [true,false]</li>
 * <li><strong>month</strong>: if the view is restricted, the month to which
 * it is restricted. A number between 0 (January) and 11 (December).</li>
 * <li><strong>year</strong>: if the view is restricted, the year to which
 * it is restricted. A four-digit year; e.g. 2002.</li>
 * <li><strong>comparator</strong>: specifies a bean that implements
 * <tt>java.util.Comparator</tt> which can compare instances of IScheduled.
 * The current implementation of MonthViewSelector does not use the value
 * of this attribute.</li>
 * </ul>
 *
 * For beans, this interface declares five properties:<ul>
 * <li><strong>restricted</strong>: read/write Boolean</li>
 * <li><strong>month</strong>: read/write Integer, 0 - 11</li>
 * <li><strong>year</strong>: read/write Integer, four digits</li>
 * <li><strong>comparator</strong>: read/write java.util.Comparator, compares
 * instances of IScheduled</li>
 * <li><strong>iterator</strong>: read-only iterator of IScheduled
 * instances</li>
 * </ul>
 *
 * @version $Revision: 1.3 $ $Date: 2003/02/26 03:38:46 $
 * @author <a href="mailto:rphall@pluto.njcc.com">Rick Hall</a>
 */
public interface IEventList {

  /**
   * Names an attribute within the scope of an HTTP request or session
   * whose Boolean value indicates whether a list should be restricted to
   * events that are scheduled to start within a particular month and year.
   */
  public final static String AN_ISRESTRICTED = "eventrestricted";

  /**
   * Names an attribute within the scope of an HTTP request or session
   * whose Integer value indicates the month to which a list should be
   * restricted. Valid values are 0 (for January) through 11 (for December).
   */
  public final static String AN_MONTH = "eventmonth";

  /**
   * Names an attribute within the scope of an HTTP request or session
   * whose Integer value indicates the year to which a list should be
   * restricted. Values values must have four digits; e.g. "2002".
   */
  public final static String AN_YEAR = "eventyear";

  /**
   * Names an attribute within the scope of an HTTP request or session
   * that holds a bean which implements <tt>java.util.Comparator</tt>.
   * The comparator must be able to operate on instances of <tt>IScheduled</tt>.
   * Current implementations ignore this attribute, but it is reserved for
   * future enhancements.
   */
  public final static String AN_COMPARATOR = "eventcomparator";

  /**
   * Returns an iterator over a collection of IScheduled beans.
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
   * Returns the month to which an iteration is restricted (if it is
   * restricted). Valid values are 0 (for January) through 11 (for December)
   * plus <tt>null</tt> (for the current calendar month).
   */
  public Integer getMonth();

  /**
   * Sets the month to which an iteration is restricted (if it is
   * restricted). Valid values are 0 (for January) through 11 (for December)
   * plus <tt>null</tt> (for the current calendar month).
   */
  public void setMonth( Integer month );

  /**
   * Returns the year to which an iteration is restricted (if it is
   * restricted). Valid values have four digits (e.g. "2002") or are
   * <tt>null</tt> (for the current calendar year).
   */
  public Integer getYear();

  /**
   * Sets the year to which an iteration is restricted (if it is
   * restricted). Valid values have four digits (e.g. "2002") or are
   * <tt>null</tt> (for the current calendar year).
   */
  public void setYear( Integer year );

  /**
   * Returns the sort-order that an iterator will use. Valid comparators
   * must be able to compare instances of IScheduled. A null comparator
   * indicates the natural sort order of the events will be used.
   */
  public ISerializableComparator getComparator();

  /**
   * Sets the sort-order that an iterator will use. Valid comparators
   * must be able to compare instances of IScheduled. A null comparator
   * indicates the natural sort order of the events will be used.
   */
  public void setComparator( ISerializableComparator comparator );

} // IEventList

/*
 * $Log: IEventList.java,v $
 * Revision 1.3  2003/02/26 03:38:46  rphall
 * Added copyright and GPL license
 *
 * Revision 1.2  2002/02/18 18:06:00  rphall
 * Ran dos2unix to remove ^M (carriage return) from end of lines
 *
 * Revision 1.1.1.1  2002/01/03 21:57:28  rphall
 * Initial load, 5th try, Jan-03-2002 4:57 PM
 *
 * Revision 1.1  2001/12/31 15:10:39  rphall
 * Renamed 'IEventViewList' to 'IEventList'
 *
 * Revision 1.3  2001/12/31 14:36:17  rphall
 * Prefaced HTTP attribute names by 'event'
 *
 * Revision 1.2  2001/12/09 05:57:58  rphall
 * Fixed setYear
 *
 * Revision 1.1  2001/12/08 23:23:57  rphall
 * Declares attributes and properties for lists of events
 *
 */

