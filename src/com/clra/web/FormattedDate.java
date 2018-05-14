/*
 * Copyright (c) Carnegie Lake Rowing Association 2002. All rights reserved.
 * Distributed under the GPL license. See doc/COPYING.
 * $RCSfile: FormattedDate.java,v $
 * $Date: 2003/02/26 03:38:46 $
 * $Revision: 1.3 $
 */

package com.clra.web;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.servlet.jsp.JspException;

/**
 * A JSP bean that displays a formatted date. This class is a thin wrapper
 * around the Date and SimpleDateFormat classe. See the SimpleDateFormat
 * for how date formats are specified.<p>
 *
 * @version $Id: FormattedDate.java,v 1.3 2003/02/26 03:38:46 rphall Exp $
 * @author <a href="mailto:rphall@pluto.njcc.com>Rick Hall</a>
 * @see java.text.SimpleDateFormat
 */

public class FormattedDate implements Serializable {

  /** Default format is "Tuesday, 10/28/99" */
  public final static String DEFAULT_FORMAT = "EEEE', 'MM/dd/yy";

  /** Default date is the current system date/time */
  private Date date = new Date();

  /** Default format specification is specified by DEFAULT_FORMAT */
  private String format = DEFAULT_FORMAT;

  /**
   * This member should cache the currently applied format/date.
   * See the constructors and setters.
   */
  private String value = null;

  /** Utility that applies a format to a date */
  public static String applyFormat( String fmt, Date dt )
      throws JspException {

    // Preconditions
    if ( fmt == null || fmt.trim().length() == 0 ) {
      throw new JspException( "invalid format == '" + fmt + "'" );
    }
    if ( dt == null ) {
      throw new JspException( "null dt" );
    }

    String retVal = null;
    try {
      SimpleDateFormat sdf = new SimpleDateFormat( fmt );
      retVal = sdf.format( dt );
    }
    catch( Exception x ) {
      throw new JspException( x.toString() );
    }

    // Enforce postcondition
    if ( retVal == null ) { throw new Error( "design error" ); }

    return retVal;
  } // applyFormat(String,Date)

  // CONSTRUCTORS

  /**
   * Constructs a formatted date the current system time with
   * <tt>DEFAULT_FORMAT</tt>
   */
  public FormattedDate() throws JspException {
    this.value = applyFormat( this.format, this.date );
  }

  /**
   * Constructs a formatted date with the current system time
   * and the specified format
   */
  public FormattedDate( String format ) throws JspException {
    // Preconditions checked by applyFormat
    setFormat( format );
  }

  /**
   * Constructs a formatted date with the specified datetime
   * and <tt>DEFAULT_FORMAT</tt>
   */
  public FormattedDate( Date date ) throws JspException {
    // Preconditions checked by applyFormat
    setDate( date );
  }

  /**
   * Constructs a formatted date with the specified datetime
   * and format.
   */
  public FormattedDate( String format, Date date ) throws JspException {
    // Preconditions checked by applyFormat
    this.format = format;
    this.date = date;
    this.value = applyFormat( format, date );
  } // FormattedDate(String,Date)

  /**
   * Constructs a formatted date with the specified month (0-11), year
   * (four digits) and format.
   */
  public FormattedDate( String format, Integer month, Integer year )
    throws JspException {

    // FIXME Preconditions
    Calendar calendar = new GregorianCalendar();
    calendar.set( Calendar.YEAR, year.intValue() );
    calendar.set( Calendar.MONTH, month.intValue() );

    // Set the date in the middle of the month
    calendar.set( Calendar.DATE, 15 );

    this.format = format;
    this.date = calendar.getTime();
    this.value = applyFormat( format, date );

  } // FormattedDate(String,Date)

  // PROPERTY ACCESSORS AND MANIPULATORS

  public Date getDate() {
    return this.date;
  }

  public void setDate( Date date ) throws JspException {
    // Preconditions checked by applyFormat
    this.date = date;
    this.value = applyFormat( this.format, this.date );
  }

  public String getFormat()  {
    return this.format;
  }

  public void setFormat( String format ) throws JspException {
    // Preconditions checked by applyFormat
    this.format = format;
    this.value = applyFormat( this.format, this.date );
  }

  public String getValue() {
    return this.value;
  }

} // FormattedDate

/*
 * $Log: FormattedDate.java,v $
 * Revision 1.3  2003/02/26 03:38:46  rphall
 * Added copyright and GPL license
 *
 * Revision 1.2  2002/02/18 18:05:58  rphall
 * Ran dos2unix to remove ^M (carriage return) from end of lines
 *
 * Revision 1.1.1.1  2002/01/03 21:57:28  rphall
 * Initial load, 5th try, Jan-03-2002 4:57 PM
 *
 * Revision 1.3  2001/12/14 02:20:36  rphall
 * Added constructor taking Integer month, year
 *
 * Revision 1.2  2001/11/28 12:09:48  rphall
 * Made Serializable
 *
 * Revision 1.1  2001/11/23 19:40:02  rphall
 * Major revision
 *
 */

