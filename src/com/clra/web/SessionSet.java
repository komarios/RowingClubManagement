/*
 * Copyright (c) Carnegie Lake Rowing Association 2002. All rights reserved.
 * Distributed under the GPL license. See doc/COPYING.
 * $RCSfile: SessionSet.java,v $
 * $Date: 2003/02/26 03:38:46 $
 * $Revision: 1.3 $
 */

package com.clra.web;

import com.clra.rowing.RowingDBRead;
import com.clra.rowing.RowingSessionSnapshot;
import com.clra.util.ISerializableComparator;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import javax.servlet.jsp.JspException;
import org.apache.log4j.Category;

/**
 * A JSP bean that provides ordered lists of SessionViews. This class
 * is a thin wrapper around calls to RowingDBRead. In the future, it might
 * add further value by caching and sorting views on the client side.<p>
 *
 * FIXME: this class is basically a list, not a set. A set hides duplicates,
 * whereas a list does not. Rename this class.<p>
 *
 * In the current implementation, a list is created on the fly whenever
 * a JSP is displayed, and the list is tossed after the page is written.
 * For lists of 60 or so items, a page will update in 2 - 3 seconds when
 * the app and web servers are lightly loaded. This is adequate performance,
 * since most lists will be restricted by month, and therefore will contain
 * roughly 30 items.<p>
 *
 * See revision 1.10 and 1.11 for a flawed implementation of
 * caching and sorting. See ../bugs/SEVERITY3_slow_sessionlist_2001.12.05a.txt
 * for an analysis of the resulting bugs.<p>
 *
 * @version $Revision: 1.3 $ $Date: 2003/02/26 03:38:46 $
 * @author <a href="mailto:rphall@pluto.njcc.com">Rick Hall</a>
 */
public class SessionSet implements IEventList {

  private final static String base = SessionSet.class.getName();
  private final static Category theLog = Category.getInstance( base );

  /** Holds the restriction flag */
  private Boolean restricted = new Boolean(true);

  /** Holds the value of the month and year for restricted iterations */
  private final Calendar calendar;

  /**
   * Returns a flag that indicates whether an iterator will be restricted
   * to events that are scheduled to start within a particular month
   * and year. 
   */
  public Boolean getRestricted() {
    return this.restricted;
  }

  /**
   * Sets whether an iterator will be restricted to events that start
   * within a particular month and year. A null values does not change
   * the current restriction.
   */
  public void setRestricted( Boolean restricted ) {
    if ( restricted != null ) {
      this.restricted = restricted;
    }
  }

  /** Returns the month used by restricted iterations */
  public Integer getMonth() {
    return new Integer( this.calendar.get( Calendar.MONTH ) );
  }

  /** Sets the month used by restricted iterations */
  public void setMonth( Integer month ) {
    if ( month.intValue() < 0 || month.intValue() > 11 ) {
      throw new IllegalArgumentException( "bad month == " + month );
    }
    this.calendar.set( Calendar.MONTH, month.intValue() );
  }

  /** Returns the year used by restricted iterations */
  public Integer getYear() {
    return new Integer( this.calendar.get( Calendar.YEAR ) );
  }

  /** Sets the year used by restricted iterations */
  public void setYear( Integer year ) {
    if ( year.intValue() < 1000 || year.intValue() > 9999 ) {
      String msg = "bad year == " + year + "; years must be four digits";
      throw new IllegalArgumentException( "bad year == " + year );
    }
    this.calendar.set( Calendar.YEAR, year.intValue() );
  }

  /** Stubbed method that returns null */
  public ISerializableComparator getComparator() { return null; }

  /** Stubbed method that does nothing */
  public void setComparator( ISerializableComparator comparator ) { }

  /** Prepares a restricted iterator for the current month and year */
  public SessionSet() throws WebException {
    this.calendar = new GregorianCalendar();
    this.calendar.set( Calendar.DATE, 1 );
  }

  /**
   * Prepares a restricted iterator for the specified month of the
   * current year
   */
  public SessionSet( Integer month ) throws WebException {
    this();
    setMonth( month );
  }

  /** Prepares a restricted iterator for the specified month and year */
  public SessionSet( Integer month, Integer year ) throws WebException {
    this();
    setMonth( month );
    setYear( year );
  }

  /** Returns an iterator constructed to the current setting */
  public Iterator getIterator() throws WebException {

    Iterator retVal = null;
    try {
      Collection c = null;
      if ( restricted.booleanValue() ) {

        final int m = getMonth().intValue();
        final int y = getYear().intValue();
        final Date start  = WebUtils.minimumDateInMonth( m, y ); 
        final Date finish = WebUtils.maximumDateInMonth( m, y );

        if ( theLog.isDebugEnabled() ) {
          try {
            FormattedDate fd =
              new FormattedDate("EEEE', 'MM/dd/yy', 'h:mm:ss.SSS a");
            fd.setDate( start );
            theLog.debug( "SessionSet.start == " + fd.getValue() );
            fd.setDate( finish );
            theLog.debug( "SessionSet.finish== " + fd.getValue() );
          } catch( JspException x ) {}
        } // if isDebugEnabled

        c = RowingDBRead.findAllRowingSessionSnapshotsInRange(start,finish);
      }
      else {
        c = RowingDBRead.findAllRowingSessionSnapshots();
      }

      final Iterator cIter = c.iterator();

      retVal = new Iterator() {
        public boolean hasNext() {
          return cIter.hasNext();
        }
        public Object next() {
          RowingSessionSnapshot rss = (RowingSessionSnapshot) cIter.next();
          return new SessionView( rss );
        }
        public void remove() {
          throw new UnsupportedOperationException( "remove not supported" );
        }
      }; // new Iterator()

    }
    catch( Exception x ) {
      String msg = "SessionSet.getIterator: " + x.getClass().getName()
        + ": " + x.getMessage();
      theLog.error( msg, x );
      throw new WebException( msg );
    }

    return retVal;
  } // getIterator()

} // SessionSet

/*
 * $Log: SessionSet.java,v $
 * Revision 1.3  2003/02/26 03:38:46  rphall
 * Added copyright and GPL license
 *
 * Revision 1.2  2002/01/30 15:17:30  rphall
 * Fixed a bug for day in month > 28
 *
 * Revision 1.19  2002/01/30 01:30:23  rphall
 * Fixed bug for day in month > 28
 *
 * Revision 1.18  2002/01/01 18:48:07  rphall
 * Removed unnecessary import statements
 *
 * Revision 1.17  2002/01/01 03:40:33  rphall
 * Moved getName() from MemberName to MemberView
 *
 * Revision 1.16  2001/12/13 01:30:21  rphall
 * Enrollment business and web objects
 *
 * Revision 1.15  2001/12/11 23:47:32  rphall
 * Documentation
 *
 * Revision 1.14  2001/12/09 20:29:19  rphall
 * Fixed date range bug
 *
 * Revision 1.13  2001/12/09 05:58:32  rphall
 * Implements IEventViewList
 *
 * Revision 1.12  2001/12/07 21:25:16  rphall
 * Removed (now unused) client-side caching
 *
 * Revision 1.11  2001/12/07 14:44:46  rphall
 * Hacked removal of caching
 *
 * Revision 1.10  2001/12/07 01:07:24  rphall
 * Checkpt: before debugging slow perf
 *
 * Revision 1.9  2001/12/06 21:26:48  rphall
 * Checkpt
 *
 * Revision 1.8  2001/12/06 04:54:35  rphall
 * Deprecated (until client-side caching is fixed)
 *
 * Revision 1.7  2001/12/05 19:53:01  rphall
 * New implementation using EJB's -- but not final
 *
 * Revision 1.6  2001/12/05 03:54:01  rphall
 * New implementation based on RowingSessionList
 *
 * Revision 1.5  2001/11/30 11:38:00  rphall
 * First working version, RowingSession entity bean
 *
 * Revision 1.4  2001/11/27 06:01:47  rphall
 * Trim strings returned from DB
 *
 * Revision 1.2  2001/11/24 22:52:29  rphall
 * Moved to a bean-friendly design. Exposed ctor, new caching scheme.
 *
 * Revision 1.1  2001/11/23 19:40:02  rphall
 * Major revision
 *
 * Revision 1.2  2001/11/18 17:07:08  rphall
 * Checkpt before major revision of rowing package
 *
 */

