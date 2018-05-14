/*
 * Copyright (c) Carnegie Lake Rowing Association 2002. All rights reserved.
 * Distributed under the GPL license. See doc/COPYING.
 * $RCSfile: CalendarBean.java,v $
 * $Date: 2003/02/26 03:38:46 $
 * $Revision: 1.3 $
 */

package com.clra.web;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;

/**
 * Represents a date in a GUI.
 *
 * @version $Revision: 1.3 $ $Date: 2003/02/26 03:38:46 $
 * @author <a href="mailto:rphall@pluto.njcc.com">Rick Hall</a>
 */
public class CalendarBean implements Serializable {

  private static String strAM = Text.getMessage( "time.am" );
  private static String strPM = Text.getMessage( "time.pm" );

  public static ArrayList yearList() {
    return yearList( new GregorianCalendar().get(Calendar.YEAR) );
  }

  public static ArrayList yearList( int year ) {
    if ( year < 0 ) throw new IllegalArgumentException("bad year == " + year);
    ArrayList retVal = new ArrayList();
    for ( int i = year - 1; i < year + 3; i++ ) {
      String label = "" + i;
      LabelValueBean lvb = new LabelValueBean( label, label );
      retVal.add( lvb );
    }
    return retVal;
  }

  public static ArrayList monthList() {
    ArrayList retVal = new ArrayList();
    Calendar c = new GregorianCalendar();
    c.set( Calendar.DATE, 1 );
    SimpleDateFormat sdf = new SimpleDateFormat( "MMMM" );
    for ( int i=0; i<12; i++ ) {
      c.set( Calendar.MONTH, i );
      String label = sdf.format( c.getTime() );
      LabelValueBean lvb = new LabelValueBean( label, "" + i );
      retVal.add( lvb );
    }
    return retVal;
  }

  public static ArrayList dayList() {
    ArrayList retVal = new ArrayList();
    for ( int i=1; i<32; i++ ) {
      String label = "" + i;
      LabelValueBean lvb = new LabelValueBean( label, "" + i );
      retVal.add( lvb );
    }
    return retVal;
  }

  public static ArrayList hourList() {
    ArrayList retVal = new ArrayList();
    for ( int i=1; i<13; i++ ) {
      String label = "" + i;
      LabelValueBean lvb = new LabelValueBean( label, "" + i );
      retVal.add( lvb );
    }
    return retVal;
  }

  public static ArrayList minuteList() {
    ArrayList retVal = new ArrayList();
    for ( int i=0; i<4; i++ ) {
      int minute = 15*i;
      String label = "" + minute;
      if ( minute == 0 ) {
        label = "00";
      }
      LabelValueBean lvb = new LabelValueBean( label, "" + minute );
      retVal.add( lvb );
    }
    return retVal;
  }

  public static ArrayList ampmList() {

    ArrayList retVal = new ArrayList();

    LabelValueBean lvb = new LabelValueBean( strAM, "" + Calendar.AM );
    retVal.add( lvb );

    lvb = new LabelValueBean( strPM, "" + Calendar.PM );
    retVal.add( lvb );

    return retVal;
  }

  public static class Years implements Iterator {
    private Iterator iterator = yearList().iterator();
    public boolean hasNext() { return iterator.hasNext(); }
    public Object next() { return iterator.next(); }
    public void remove() { throw new UnsupportedOperationException(); }
  }

  public static class Months implements Iterator {
    private Iterator iterator = monthList().iterator();
    public boolean hasNext() { return iterator.hasNext(); }
    public Object next() { return iterator.next(); }
    public void remove() { throw new UnsupportedOperationException(); }
  }

  public static class Days implements Iterator {
    private Iterator iterator = dayList().iterator();
    public boolean hasNext() { return iterator.hasNext(); }
    public Object next() { return iterator.next(); }
    public void remove() { throw new UnsupportedOperationException(); }
  }

  public static class Hours implements Iterator {
    private Iterator iterator = hourList().iterator();
    public boolean hasNext() { return iterator.hasNext(); }
    public Object next() { return iterator.next(); }
    public void remove() { throw new UnsupportedOperationException(); }
  }

  public static class Minutes implements Iterator {
    private Iterator iterator = minuteList().iterator();
    public boolean hasNext() { return iterator.hasNext(); }
    public Object next() { return iterator.next(); }
    public void remove() { throw new UnsupportedOperationException(); }
  }

  public static class AmPm implements Iterator {
    private Iterator iterator = ampmList().iterator();
    public boolean hasNext() { return iterator.hasNext(); }
    public Object next() { return iterator.next(); }
    public void remove() { throw new UnsupportedOperationException(); }
  }

} // CalendarBean

/*
 * $Log: CalendarBean.java,v $
 * Revision 1.3  2003/02/26 03:38:46  rphall
 * Added copyright and GPL license
 *
 * Revision 1.2  2002/01/30 15:17:30  rphall
 * Fixed a bug for day in month > 28
 *
 * Revision 1.2  2002/01/30 01:30:23  rphall
 * Fixed bug for day in month > 28
 *
 * Revision 1.1  2001/12/01 14:06:05  rphall
 * JSP bean
 *
 */

