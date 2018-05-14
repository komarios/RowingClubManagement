/*
 * Copyright (c) Carnegie Lake Rowing Association 2002. All rights reserved.
 * Distributed under the GPL license. See doc/COPYING.
 * $RCSfile: EnrollmentSet.java,v $
 * $Date: 2003/02/26 03:38:46 $
 * $Revision: 1.3 $
 */

package com.clra.web;

import com.clra.rowing.EnrollmentSnapshot;
import com.clra.rowing.RowingDBRead;
import com.clra.util.ISerializableComparator;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import javax.servlet.jsp.JspException;
import org.apache.log4j.Category;

/**
 * A JSP bean that provides ordered lists of EnrollmentViews. This class
 * is a thin wrapper around calls to RowingUtils. In the future, it might
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
 * @version $Revision: 1.3 $ $Date: 2003/02/26 03:38:46 $
 * @author <a href="mailto:rphall@pluto.njcc.com">Rick Hall</a>
 */
public class EnrollmentSet implements IEventList {

  private final static String base = EnrollmentSet.class.getName();
  private final static Category theLog = Category.getInstance( base );

  /** The primary key of the member to whom this enrollment applies */
  private Integer memberId = null;

  /** Returns the primary key of the member to whom this enrollment applies */
  public Integer getMemberId() {
    if ( this.memberId == null ) throw new IllegalStateException();
    return this.memberId;
  }

  /** Sets the member to whom this enrollment applies */
  public void setMemberId( Integer memberId ) {
    if ( memberId == null ) {
      throw new IllegalStateException( "null member id" );
    }
    this.memberId = memberId;
  }

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

  /** Creates an invalid EnrollmentSet, but required by Bean contract */
  public EnrollmentSet() throws WebException {
    this.calendar = new GregorianCalendar();
    this.calendar.set( Calendar.DATE, 1 );
  }

  /**
   * Prepares a restricted iterator for the specified month of the
   * current year.
   * @param memberId the member to whom this enrollment applies
   */
  public EnrollmentSet( Integer memberId, Integer month )
      throws WebException {
    this();
    setMemberId( memberId );
    setMonth( month );
  }

  /** Prepares a restricted iterator for the specified month and year */
  public EnrollmentSet( Integer memberId, Integer month, Integer year )
      throws WebException {
    this();
    setMemberId( memberId );
    setMonth( month );
    setYear( year );
  }

  /** Returns an iterator constructed to the current setting */
  public Iterator getIterator() throws WebException {

    // Preconditions
    if ( this.memberId == null ) {
      theLog.error( "null memberId" );
      throw new IllegalStateException( "null memberId" );
    }
    final int id = this.getMemberId().intValue();

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
            theLog.debug( "restricted iteration" );
            FormattedDate fd =
              new FormattedDate("EEEE', 'MM/dd/yy', 'h:mm:ss.SSS a");
            fd.setDate( start );
            theLog.debug( "SessionSet.start == " + fd.getValue() );
            fd.setDate( finish );
            theLog.debug( "SessionSet.finish== " + fd.getValue() );
          } catch( JspException x ) {}
        } // if isDebugEnabled

        c = RowingDBRead.findAllEnrollmentSnapshotsInRange(id,start,finish);
      }
      else {
        if ( theLog.isDebugEnabled() ) {
          theLog.debug( "unrestricted iteration" );
        }
        c = RowingDBRead.findAllEnrollmentSnapshots( id );
      }

      final Iterator cIter = c.iterator();

      retVal = new Iterator() {
        public boolean hasNext() {
          return cIter.hasNext();
        }
        public Object next() {
          EnrollmentSnapshot es = (EnrollmentSnapshot) cIter.next();
          return new EnrollmentView( es );
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

} // EnrollmentSet

/*
 * $Log: EnrollmentSet.java,v $
 * Revision 1.3  2003/02/26 03:38:46  rphall
 * Added copyright and GPL license
 *
 * Revision 1.2  2002/01/30 15:17:30  rphall
 * Fixed a bug for day in month > 28
 *
 * Revision 1.4  2002/01/30 01:30:23  rphall
 * Fixed bug for day in month > 28
 *
 * Revision 1.3  2002/01/01 03:40:33  rphall
 * Moved getName() from MemberName to MemberView
 *
 * Revision 1.2  2001/12/13 21:19:40  rphall
 * More logging
 *
 * Revision 1.1  2001/12/13 01:30:21  rphall
 * Enrollment business and web objects
 *
 */

