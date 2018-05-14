/*
 * Copyright (c) Carnegie Lake Rowing Association 2002. All rights reserved.
 * Distributed under the GPL license. See doc/COPYING.
 * $RCSfile: RowingSessionForm.java,v $
 * $Date: 2003/02/26 03:38:46 $
 * $Revision: 1.5 $
 */

package com.clra.web;

import com.clra.rowing.RowingSessionLevel;
import com.clra.rowing.RowingSessionState;
import com.clra.rowing.RowingSessionType;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Category;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * Form bean for the rowing session page.  This form has the following fields,
 * with default values in square brackets:
 * <ul>
 * <li><b>action</b> - The maintenance action that is being performed:
 * Create, Edit, Publish, Lock, View, Cancel or Delete. [REQUIRED]</li>
 * <li><b>id</b> - The rowing_id of the rowing session.
 * [REQUIRED except on Create, IMMUTABLE otherwise]</li>
 * <li><b>datetime</b> - The date (and time) for this rowing session.
 * [REQUIRED on Create and Edit, IMMUTABLE otherwise]</li>
 * <li><b>level</b> - The level (LTR, REGULAR) of this rowing session.
 * [REQUIRED on Create and Edit, IMMUTABLE otherwise]</li>
 * <li><b>type</b> - The type (COMPETITION,PRACTICE) of this rowing session.
 * [REQUIRED on Create and Edit, IMMUTABLE otherwise]</li>
 * <li><b>state</b> - The state (TENATIVE, OPEN, LOCKED, BOATING1, BOATING2,
 * COMPLETE, INVOICING, CLOSED) of this rowing session.
 * [REQUIRED on Create and Edit, IMMUTABLE otherwise]</li>
 * </ul>
 *
 * @author <a href="mailto:rphall@pluto.njcc.com">Rick Hall</a>
 * @version $Revision: 1.5 $ $Date: 2003/02/26 03:38:46 $
 */
public class RowingSessionForm extends ActionForm  {

  private final static String base = RowingSessionForm.class.getName();
  private final static Category theLog = Category.getInstance( base );

  /** Name of the action that creates a rowing session */
  public final static String CREATE  = "Create";

  /** Name of the action that publishes a rowing session */
  public final static String PUBLISH = "Publish";

  /** Name of the action that edits a rowing session */
  public final static String EDIT    = "Edit";

  /** Name of the action that locks a rowing session */
  public final static String LOCK = "Lock";

  /** Name of the action that displays a rowing session */
  public final static String VIEW    = "View";

  /** Name of the action that cancels a rowing session */
  public final static String SESSIONCANCEL  = "SessionCancel";

  /** Name of the action that deletes a rowing session */
  public final static String DELETE  = "Delete";

  /** Name of the action that cancels action by the form */
  public final static String CANCEL  = "Cancel";

  private final static String spec1 = Text.getMessage( "dateformat.date1" );
  private final static SimpleDateFormat sdf1 = new SimpleDateFormat(spec1);

  private final static String spec2 = Text.getMessage( "dateformat.time1" );
  private final static SimpleDateFormat sdf2 = new SimpleDateFormat(spec2);

  private final static String spec3 = Text.getMessage( "dateformat.long1" );
  private final static SimpleDateFormat sdf3 = new SimpleDateFormat(spec3);

  /** The maintenance action that is being performed. */
  private String action = CREATE;

  /** The id of the rowing session */
  private Integer rowingId = null;

  /** Date (and time) of the rowing session */
  private Calendar calendar = tomorrowMorning();

  /** A collection of participants enrolled in the rowing session */
  private Collection participants = new ArrayList();

  /**
   * The level of the rowing session.
   * @see com.clra.rowing.RowingSessionLevel
  */
  private String level = RowingSessionLevel.NAME_REGULAR;

  /**
   * The rowing session type.
   * @see com.clra.rowing.RowingSessionType
   */
  private String type = RowingSessionType.NAME_PRACTICE;

  /**
   * The rowing session type.
   * @see com.clra.rowing.RowingSessionState
   */
  private String state = RowingSessionState.NAME_NEW;

  /** Return the maintenance action */
  public String getAction() {
    return this.action;
  }

  /** Set the maintenance action.  */
  public void setAction(String action) {
    this.action = action;
  }

  /** Return the persistent id of the rowing session */
  public Integer getRowingId() {
    return this.rowingId;
  }

  /** Set the persistent id of the rowing session */
  void setRowingId( Integer rowingId ) {
    this.rowingId = rowingId;
  }

  /** Return the state of the rowing session */
  public String getState() {
    return this.state;
  }

  /** Set the state of the rowing session */
  public void setState( String state ) {
    this.state = state;
  }

  /** Returns the formatted date of the rowing session */
  public String getDate() {
    return sdf1.format( this.calendar.getTime() );
  }

  /** Returns the formatted time of the rowing session */
  public String getTime() {
    return sdf2.format( this.calendar.getTime() );
  }

  /** Returns the formatted date and time of the rowing session */
  public String getDateTime() {
    return sdf3.format( this.calendar.getTime() );
  }

  /** Returns the date and time in a Date object */
  public Date getDateTimeAsDateObject() {
    return this.calendar.getTime();
  }

  /** Sets the date and time from a Date object */
  public void setDateTimeFromDate( Date date ) {
    if ( date == null ) {
      throw new IllegalArgumentException( "null date" );
    }
    // Bug workaround
    date = new Date( date.getTime() );
    // End workaround
    this.calendar.set( Calendar.YEAR, date.getYear() + 1900 );
    this.calendar.set( Calendar.MONTH, date.getMonth() );
    this.calendar.set( Calendar.DATE, date.getDate() );
    this.calendar.set( Calendar.HOUR_OF_DAY, date.getHours() );
    this.calendar.set( Calendar.MINUTE, date.getMinutes() );
  }

  /** Returns the date and time in a Calendar object */
  public Calendar getCalendar() {
    return (Calendar) this.calendar.clone();
  }

  /** Sets the date and time from a Calendar object */
  public void setCalendar( Calendar calendar ) {
    if ( calendar == null ) {
      throw new IllegalArgumentException( "null calendar" );
    }
    this.calendar = (Calendar) calendar.clone();
  }

  /** Return the year. Range: 2001 - ... */
  public int getYear() {
    return this.calendar.get( Calendar.YEAR );
  }

  /** Sets the year. Range: 2001 - ... */
  public void setYear( int year ) {
    this.calendar.set( Calendar.YEAR, year );
  }

  /** Return the month. Range: 0 - 11 */
  public int getMonth() {
    return this.calendar.get( Calendar.MONTH );
  }

  /** Sets the month. Range: 0 - 11 */
  public void setMonth( int month ) {
    this.calendar.set( Calendar.MONTH, month );
  }

  /** Return the day. Range: 1 - 31 */
  public int getDay() {
    return this.calendar.get( Calendar.DATE );
  }

  /** Sets the day. Range: 1 - 31 */
  public void setDay( int day ) {
    this.calendar.set( Calendar.DATE, day );
  }

  /** Return the hour. Range: 1 - 12 */
  public int getHour() {
    return this.calendar.get( Calendar.HOUR );
  }

  /** Sets the hour. Range: 1 - 12 */
  public void setHour( int hour ) {
    this.calendar.set( Calendar.HOUR, hour );
  }

  /** Return the minute */
  public int getMinute() {
    return this.calendar.get( Calendar.MINUTE );
  }

  /** Sets the minute */
  public void setMinute( int minute ) {
    this.calendar.set( Calendar.MINUTE, minute );
  }

  /** Return am/pm. Range: Calendar.AM or Calendar.PM */
  public int getAmPm() {
    return this.calendar.get( Calendar.AM_PM );
  }

  /** Sets am/pm Range: Calendar.AM or Calendar.PM */
  public void setAmPm( int ampm ) {
    this.calendar.set( Calendar.AM_PM, ampm );
  }

  /** Return the level of the rowing session */
  public String getLevel() {
    return this.level;
  }

  /** Set the level of the rowing session */
  public void setLevel( String level ) {
    this.level = level;
  }

  /** Return the type of the rowing session */
  public String getType() {
    return this.type;
  }

  /** Set the type of the rowing session */
  public void setType( String type ) {
    this.type = type;
  }

  /**
   * Reset all properties to their default values.
   *
   * @param mapping The mapping used to select this instance
   * @param request The servlet request we are processing
   */
  public void reset(ActionMapping mapping, HttpServletRequest request) {

    // FIXME reset to request values
    this.action = CREATE;
    this.rowingId = null;
    this.calendar = tomorrowMorning();
    this.level = RowingSessionLevel.NAME_REGULAR;
    this.type = RowingSessionType.NAME_PRACTICE;
    this.state = RowingSessionState.NAME_NEW;

  }


  /**
   * Validate the properties that have been set from this HTTP request,
   * and return an <code>ActionErrors</code> object that encapsulates any
   * validation errors that have been found.  If no errors are found, return
   * <code>null</code> or an <code>ActionErrors</code> object with no
   * recorded error messages.
   *
   * <p>Restrictions:<ul>
   *
   * <li>A NEW session may be created. No other action is permitted.</li>
   * <li>After creation, a session is considered TENATIVE. A TENATIVE
   * sessions. The Create action is not permitted on any other session
   * state.</li>
   * <li>The Edit and Delete actions are permitted only for TENATIVE rowing
   * sessions.</li>
   * <li>The Promote and Cancel actions are not permitted for CLOSED rowing
   * sessions.</li>
   * <li>Only the Edit action is permitted on CANCELLED rowing sessions.</li>
   * <ul>
   *
   * @param mapping The mapping used to select this instance
   * @param request The servlet request we are processing
   */
  public ActionErrors validate(ActionMapping mapping,
                           HttpServletRequest request) {

    ActionErrors errors = new ActionErrors();

    return (errors);
  }

  /** Package utility to calculate 5:45 AM tomorrow morning */
  static Calendar tomorrowMorning() {
    GregorianCalendar calendar = new GregorianCalendar();
    calendar.add(Calendar.DATE,1);
    calendar.set(Calendar.HOUR,5);
    calendar.set(Calendar.MINUTE,45);
    calendar.set(Calendar.AM_PM,Calendar.AM);
    return calendar;
  }

  /** Package utility to calculate 7:00 PM tomorrow evening */
  static Calendar tomorrowEvening() {
    GregorianCalendar calendar = new GregorianCalendar();
    calendar.add(Calendar.DATE,1);
    calendar.set(Calendar.HOUR,19);
    calendar.set(Calendar.MINUTE,0);
    calendar.set(Calendar.AM_PM,Calendar.PM);
    return calendar;
  }

  /**
   * Sets the (name-sorted) collection of other participants in this
   * rowing session. Items in the collection should be instances of
   * Participant2View.
   */
  void setParticipants( Collection participants ) {
    if ( participants == null ) {
      throw new IllegalArgumentException( "null participant collection" );
    }
    this.participants = participants;
  }

  /**
   * Returns a read-only iterator of "starboard" participants.
   * Items in the iteration are Strings, formatted for display on a screen.
   */
  public Iterator getStarboards() {
    Iterator retVal = ParticipationForm.selectStarboards( this.participants );
    return retVal;
  }

  /**
   * Returns a read-only iterator of "port" participants.
   * Items in the iteration are Strings, formatted for display on a screen.
   */
  public Iterator getPorts() {
    Iterator retVal = ParticipationForm.selectPorts( this.participants );
    return retVal;
  }

  /**
   * Returns a read-only iterator of "coxswain" participants.
   * Items in the iteration are Strings, formatted for display on a screen.
   */
  public Iterator getCoxswains() {
    Iterator retVal = ParticipationForm.selectCoxswains( this.participants );
    return retVal;
  }

} // RowingSessionForm

/*
 * $Log: RowingSessionForm.java,v $
 * Revision 1.5  2003/02/26 03:38:46  rphall
 * Added copyright and GPL license
 *
 * Revision 1.4  2002/03/24 01:51:28  rphall
 * Locking and participant changes
 *
 * Revision 1.3  2002/02/24 18:17:10  rphall
 * Fixed bug #514881
 *
 * Revision 1.2  2002/02/18 18:07:02  rphall
 * Ran dos2unix to remove ^M (carriage return) from end of lines
 *
 * Revision 1.1.1.1  2002/01/03 21:57:28  rphall
 * Initial load, 5th try, Jan-03-2002 4:57 PM
 *
 * Revision 1.6  2001/12/15 02:27:38  rphall
 * Removed final, made extensible
 *
 * Revision 1.5  2001/12/09 22:12:56  rphall
 * Fixed day/hour bug
 *
 * Revision 1.4  2001/12/04 02:43:36  rphall
 * Added a logging.
 * Distinguished CANCEL from SESSIONCANCEL.
 * Added getDateTimeAsDateObject(), getCalendar() and setters.
 *
 * Revision 1.3  2001/12/02 22:07:17  rphall
 * Replaced stubs with valid methods
 *
 * Revision 1.2  2001/12/01 14:07:18  rphall
 * Major changes
 *
 * Revision 1.1  2001/11/28 12:18:39  rphall
 * Started struts framework for managing rowing sessions
 *
 */

