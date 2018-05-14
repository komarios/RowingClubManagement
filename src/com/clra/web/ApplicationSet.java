/*
 * Copyright (c) Carnegie Lake Rowing Association 2002. All rights reserved.
 * Distributed under the GPL license. See doc/COPYING.
 * $RCSfile: ApplicationSet.java,v $
 * $Date: 2003/02/26 03:38:46 $
 * $Revision: 1.2 $
 */

package com.clra.web;

import com.clra.util.ISerializableComparator;
import com.clra.visitor.ApplicantDBRead;
import com.clra.visitor.ApplicantSnapshot;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import javax.servlet.jsp.JspException;
import org.apache.log4j.Category;

/**
 * @version $Revision: 1.2 $ $Date: 2003/02/26 03:38:46 $
 * @author <a href="mailto:donaldzhu@sympatico.ca">Angela Yue</a>
 */
public class ApplicationSet implements IEventList {

  private final static String base = ApplicationSet.class.getName();
  private final static Category theLog = Category.getInstance( base );

  /** Holds the value of the month and year for restricted iterations */
  private final Calendar calendar;
  private Boolean restricted = new Boolean(true);

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

  /** Creates an invalid ApplicationSet, but required by Bean contract */
  public ApplicationSet() throws WebException {
    this.calendar = new GregorianCalendar();
    this.calendar.set( Calendar.DATE, 1 );
  }

  /**
   * Prepares a restricted iterator for the specified month of the
   * current year.
   * @param memberId the member to whom this enrollment applies
   */
  public ApplicationSet( Integer month )
      throws WebException {
    this();
    setMonth( month );
  }

  /** Prepares a restricted iterator for the specified month and year */
  public ApplicationSet( Integer month, Integer year )
      throws WebException {
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
            theLog.debug( "restricted iteration" );
            FormattedDate fd =
              new FormattedDate("EEEE', 'MM/dd/yy', 'h:mm:ss.SSS a");
            fd.setDate( start );
            theLog.debug( "SessionSet.start == " + fd.getValue() );
            fd.setDate( finish );
            theLog.debug( "SessionSet.finish== " + fd.getValue() );
          } catch( JspException x ) {}
        } // if isDebugEnabled

        c = ApplicantDBRead.findAllApplicantsInRange(start,finish);
      }
      else {
        if ( theLog.isDebugEnabled() ) {
          theLog.debug( "unrestricted iteration" );
        }
        c = ApplicantDBRead.findAllApplicants( );
      }

      final Iterator cIter = c.iterator();

      retVal = new Iterator() {
        public boolean hasNext() {
          return cIter.hasNext();
        }
        public Object next() {
          ApplicantSnapshot es = (ApplicantSnapshot) cIter.next();
          return es;
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

} // ApplicationSet

