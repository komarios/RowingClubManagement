/*
 * Copyright (c) Carnegie Lake Rowing Association 2002. All rights reserved.
 * Distributed under the GPL license. See doc/COPYING.
 * $RCSfile: IEvent.java,v $
 * $Date: 2003/02/26 03:38:45 $
 * $Revision: 1.3 $
 */

package com.clra.util;

import java.util.Calendar;

/**
 * Declares a read-write interface for schedulable events.
 *
 * @version $Revision: 1.3 $ $Date: 2003/02/26 03:38:45 $
 * @author <a href="mailto:rphall@pluto.njcc.com">Rick Hall</a>
 */
public interface IEvent extends IScheduled {

  /** Sets the start date and time of an event as a Calendar object */
  public void setStart( Calendar start );

  /**
   * Sets the duration of an event, in milliseconds.
   * (Sigh... Yes, milliseconds are ridiculous precision. But they are
   * the most convenient unit for durations, since millisecond durations
   * are easy to add to Date and Calendar instances).
   * @parameter duration a non-negative duration in milliseconds.
   */
  public void setDuration( Long duration );

} // IEvent

/*
 * $Log: IEvent.java,v $
 * Revision 1.3  2003/02/26 03:38:45  rphall
 * Added copyright and GPL license
 *
 * Revision 1.2  2002/02/18 18:05:21  rphall
 * Ran dos2unix to remove ^M (carriage return) from end of lines
 *
 * Revision 1.1.1.1  2002/01/03 21:57:28  rphall
 * Initial load, 5th try, Jan-03-2002 4:57 PM
 *
 * Revision 1.1  2001/12/31 16:11:32  rphall
 * *** empty log message ***
 *
 * Revision 1.1  2001/12/09 05:59:36  rphall
 * An element of IEventList.java
 *
 */

