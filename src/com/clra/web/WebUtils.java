/*
 * Copyright (c) Carnegie Lake Rowing Association 2002. All rights reserved.
 * Distributed under the GPL license. See doc/COPYING.
 * $RCSfile: WebUtils.java,v $
 * $Date: 2003/02/26 03:38:46 $
 * $Revision: 1.3 $
 */

package com.clra.web;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import org.apache.log4j.Category;

/**
 * Some utilities related to presenting information to the user.
 *
 * @version $Revision: 1.3 $ $Date: 2003/02/26 03:38:46 $
 * @author <a href="mailto:rphall@pluto.njcc.com">Rick Hall</a>
 */
public class WebUtils {

  private final static String base = WebUtils.class.getName();
  private final static Category theLog = Category.getInstance( base );

  /**
   * Returns the first (or nearly first) Date in a month.</p><p>
   *
   * FIXME:
   * The Java Calender class and Oracle date functions seem to disagree
   * about how to treat times around 12 midnight, which leads to some
   * fuzziness around the beginning and end of a month.
   */
  public static Date minimumDateInMonth( int month, int year ) {

    Calendar calendar = new GregorianCalendar();
    calendar.set( Calendar.YEAR, year );
    calendar.set( Calendar.MONTH, month );
    calendar.set( Calendar.DATE, calendar.getMinimum( Calendar.DATE ) );
    calendar.set( Calendar.HOUR_OF_DAY,
        calendar.getMinimum( Calendar.HOUR_OF_DAY ) );
    calendar.set( Calendar.MINUTE, calendar.getMinimum( Calendar.MINUTE ) );
    calendar.set( Calendar.SECOND, calendar.getMinimum( Calendar.SECOND ) );
    calendar.set( Calendar.MILLISECOND,
        calendar.getMinimum( Calendar.MILLISECOND ) );

    return calendar.getTime();
  } // minimumDate(int,int)

  /**
   * Returns the last (or nearly last) Date in (or nearly in) a month.</p><p>
   *
   * FIXME:
   * The Java Calender class and Oracle date functions seem to disagree
   * about how to treat times around 12 midnight, which leads to some
   * fuzziness around the beginning and end of a month.
   */
  public static Date maximumDateInMonth( int month, int year ) {

    Calendar calendar = new GregorianCalendar();
    calendar.set( Calendar.YEAR, year );
    calendar.set( Calendar.MONTH, month );
    calendar.set( Calendar.DATE, calendar.getMaximum( Calendar.DATE ) );
    calendar.set( Calendar.HOUR_OF_DAY,
        calendar.getMaximum( Calendar.HOUR_OF_DAY ) );
    calendar.set( Calendar.MINUTE, calendar.getMaximum( Calendar.MINUTE ) );
    calendar.set( Calendar.SECOND, calendar.getMaximum( Calendar.SECOND ) );
    calendar.set( Calendar.MILLISECOND,
        calendar.getMaximum( Calendar.MILLISECOND ) );

    return calendar.getTime();
  } // maximumDate(int,int)

} // WebUtils

/*
 * $Log: WebUtils.java,v $
 * Revision 1.3  2003/02/26 03:38:46  rphall
 * Added copyright and GPL license
 *
 * Revision 1.2  2002/02/18 18:07:29  rphall
 * Ran dos2unix to remove ^M (carriage return) from end of lines
 *
 * Revision 1.1.1.1  2002/01/03 21:57:28  rphall
 * Initial load, 5th try, Jan-03-2002 4:57 PM
 *
 * Revision 1.1  2001/12/13 01:30:21  rphall
 * Enrollment business and web objects
 *
 */

