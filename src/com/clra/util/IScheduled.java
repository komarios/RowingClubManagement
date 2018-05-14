/*
 * Copyright (c) Carnegie Lake Rowing Association 2002. All rights reserved.
 * Distributed under the GPL license. See doc/COPYING.
 * $RCSfile: IScheduled.java,v $
 * $Date: 2003/02/26 03:38:45 $
 * $Revision: 1.3 $
 */

package com.clra.util;

import java.util.Calendar;

/**
 * Declares a read-only interface for schedulable events.
 *
 * @version $Revision: 1.3 $ $Date: 2003/02/26 03:38:45 $
 * @author <a href="mailto:rphall@pluto.njcc.com">Rick Hall</a>
 */
public interface IScheduled {

  /** Returns the start date and time of an event as a Calendar object */
  public Calendar getStart();

  /**
   * Returns the duration of an event, in milliseconds.
   * (Sigh... Yes, milliseconds are ridiculous precision. But they are
   * the most convenient unit for durations, since millisecond durations
   * are easy to add to Date and Calendar instances).
   * @return a non-negative duration in milliseconds.
   */
  public Long getDuration();

} // IScheduled

/*
 * $Log: IScheduled.java,v $
 * Revision 1.3  2003/02/26 03:38:45  rphall
 * Added copyright and GPL license
 *
 * Revision 1.2  2002/02/18 18:05:29  rphall
 * Ran dos2unix to remove ^M (carriage return) from end of lines
 *
 * Revision 1.1.1.1  2002/01/03 21:57:28  rphall
 * Initial load, 5th try, Jan-03-2002 4:57 PM
 *
 * Revision 1.1  2001/12/31 16:11:32  rphall
 * *** empty log message ***
 *
 */

