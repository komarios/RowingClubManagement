/*
 * Copyright (c) Carnegie Lake Rowing Association 2002. All rights reserved.
 * Distributed under the GPL license. See doc/COPYING.
 * $RCSfile: SessionView.java,v $
 * $Date: 2003/02/26 03:38:46 $
 * $Revision: 1.3 $
 */

package com.clra.web;

import com.clra.rowing.DefaultRowingSessionComparator;
import com.clra.rowing.RowingSessionLevel;
import com.clra.rowing.RowingSessionSnapshot;
import com.clra.rowing.RowingSessionState;
import com.clra.rowing.RowingSessionType;
import java.rmi.RemoteException;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.io.Serializable;
import org.apache.log4j.Category;

/**
 * Read-only information about a session. A thin-wrapper around
 * RowingSessionSnapshot, with some String properties useful
 * in JSP's.
 *
 * @version $Id: SessionView.java,v 1.3 2003/02/26 03:38:46 rphall Exp $
 * @author <a href="mailto:rphall@pluto.njcc.com">Rick Hall</a>
 */
public class SessionView implements Comparable, Serializable {

  private final static String base = SessionView.class.getName();
  private final static Category theLog = Category.getInstance( base );

  private RowingSessionSnapshot data;

  private String dateFormatSpec = "EEEE MM/dd/yyyy 'at' hh:mm am";

  /**
   * Constructs an invalid instance. Use this constructor only for JSP beans
   * and immediately set valid values via setValuesFromRowingSession(..).
   */
  public SessionView() {
    this.data = null;
  }

  public SessionView( RowingSessionSnapshot rss ) {
    setData( rss );
  }

  /** Deprecated */
  public void setData( RowingSessionSnapshot rss ) {
    if ( rss == null ) {
      throw new IllegalArgumentException( "null rowing snapshot" );
    }
    this.data = rss;
  }

  public RowingSessionSnapshot getData() {
    return this.data;
  }

  public Integer getId() {
    return this.getData().getId();
  }

  public Date getDate() {
    return this.getData().getDate();
  }

  /**
   * Specifies how the date should be formatted as a string.
   * The SimpleDateFormat class spells out how formats are specified.
   * @see java.text.SimpleDateFormat
   */
  public void setDateFormatSpec( String spec ) {
    if ( spec == null || spec.trim().length() == 0 ) {
      throw new IllegalArgumentException( "invalid spec == '" + spec + "'");
    }
    this.dateFormatSpec = spec;
  }

  /**
   * Returns the specification for the rowing date is formatted as a string.
   * The SimpleDateFormat class spells out how formats are specified.
   * @see java.text.SimpleDateFormat
   */
  public String getDateFormatSpec() {
    if ( this.dateFormatSpec== null
            || this.dateFormatSpec.trim().length() == 0) {
      String msg = "invalid spec == '" + this.dateFormatSpec + "'";
      throw new IllegalStateException( msg );
    }
    return this.dateFormatSpec;
  }

  /** Returns the date as a formatted string, using the current format */
  public String getDateAsString() {
    SimpleDateFormat sdf =  new SimpleDateFormat( this.getDateFormatSpec() );
    String retVal = sdf.format( this.getDate() );
    return retVal;
  }

  public String getLevel() {
    return this.getData().getLevel().getName();
  }

  public String getType() {
    return this.getData().getType().getName();
  }

  public String getState() {
    return this.getData().getState().getName();
  }

  /** Two sessions are equal iff they their natural comparator returns equal */
  public boolean equals( Object o ) {

    boolean retVal = false;
    if ( o instanceof SessionView ) {
      retVal = this.compareTo( o ) == 0;
    }

    return retVal;
  } // equals(Object)

  /** Session objects are hashed by id's */
  public int hashCode() {
    return this.getData().hashCode();
  }

  /**
   * Orders views by the natural comparator for rowing sessions.
   *
   * @param o A session object.
   * @exception ClassCastException if o is not a session object.
   */
  public int compareTo( Object o ) throws ClassCastException {

    if ( !(o instanceof SessionView) ) {
      throw new ClassCastException( "not a session object" );
    }
    int retVal = DefaultRowingSessionComparator.staticCompare(
      this.getData(), ((SessionView)o).getData() );

    return retVal;
  } // compareTo(Object)

} // SessionView

/*
 * $Log: SessionView.java,v $
 * Revision 1.3  2003/02/26 03:38:46  rphall
 * Added copyright and GPL license
 *
 * Revision 1.2  2002/02/18 18:07:20  rphall
 * Ran dos2unix to remove ^M (carriage return) from end of lines
 *
 * Revision 1.1.1.1  2002/01/03 21:57:28  rphall
 * Initial load, 5th try, Jan-03-2002 4:57 PM
 *
 * Revision 1.8  2001/12/15 02:28:17  rphall
 * Added date formatting
 *
 * Revision 1.7  2001/12/13 21:25:13  rphall
 * Remove EJB-based ctor's; check snapshot isn't null on setData
 *
 * Revision 1.6  2001/12/13 01:30:21  rphall
 * Enrollment business and web objects
 *
 * Revision 1.5  2001/12/07 01:07:24  rphall
 * Checkpt: before debugging slow perf
 *
 * Revision 1.4  2001/12/06 21:26:48  rphall
 * Checkpt
 *
 * Revision 1.3  2001/12/06 04:56:47  rphall
 * Cleaned up constructors & properties
 *
 * Revision 1.2  2001/11/28 12:09:26  rphall
 * Made Serializable
 *
 * Revision 1.1  2001/11/23 19:40:02  rphall
 * Major revision
 *
 * Revision 1.2  2001/11/18 17:07:08  rphall
 * Checkpt before major revision of rowing package
 *
 */

