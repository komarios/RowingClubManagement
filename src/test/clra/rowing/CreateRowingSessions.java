/*
 * Copyright (c) Carnegie Lake Rowing Association 2002. All rights reserved.
 * Distributed under the GPL license. See doc/COPYING.
 * $RCSfile: CreateRowingSessions.java,v $
 * $Date: 2003/02/26 03:38:47 $
 * $Revision: 1.4 $
 */

package test.clra.rowing;

import com.clra.rowing.RowingSessionLevel;
import com.clra.rowing.RowingSessionSnapshot;
import com.clra.rowing.RowingSessionState;
import com.clra.rowing.RowingSessionType;

import java.io.PrintStream;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.text.SimpleDateFormat;

/**
 * This utility creates Oracle or mySQL INSERT statements to populate the
 * RowingSession table. The class is meant to run from a command-line and
 * its output may be directed to a file. This utility is meant mainly to
 * generate test data. It has only one adjustable parameter -- a command-line
 * argument specifying whether Oracle or mySQL is the target database.<p>
 *
 * RowingSessions are generated through December 31 of the current year,
 * starting from September 1 of the preceding year. There are four seasons:
 * winter, spring, summer, and fall. See the Season interface and the
 * Winter, Spring, Summer and Autumn implementations.<p>
 *
 * @version $Revision: 1.4 $ $Date: 2003/02/26 03:38:47 $
 * @author <a href="mailto:rphall@pluto.njcc.com">Rick Hall</a>
 */
public final class CreateRowingSessions {

  /** All operations are static */
  private CreateRowingSessions() {}

  /** Used to format a Java date to a string compatible with mySQL */
  private final static SimpleDateFormat DATE_FORMAT =
    new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );

  /** Used to generate random RowingSessionState values */
  private final static RowingSessionState[] arrRSS = {
    RowingSessionState.TENATIVE,
    RowingSessionState.OPEN,
    RowingSessionState.LOCKED,
    RowingSessionState.BOATING1,
    RowingSessionState.BOATING2,
    RowingSessionState.COMPLETE,
    RowingSessionState.INVOICING,
    RowingSessionState.CLOSED,
    RowingSessionState.CANCELLED
  };

  /** Easter 2002 */
  public final static Calendar EASTER() {
    Calendar retVal = new GregorianCalendar();
    retVal.set( Calendar.YEAR, 2002 );
    retVal.set( Calendar.MONTH, Calendar.MARCH );
    retVal.set( Calendar.DAY_OF_MONTH, 31 );
    return retVal;
  }

  /** Memorial Day 2002 */
  public final static Calendar MEMORIALDAY() {
    Calendar retVal = new GregorianCalendar();
    retVal.set( Calendar.YEAR, 2002 );
    retVal.set( Calendar.MONTH, Calendar.MAY );
    retVal.set( Calendar.DAY_OF_MONTH, 27 );
    return retVal;
  }

  /** July 4th 2002 */
  public final static Calendar JULY4TH() {
    Calendar retVal = new GregorianCalendar();
    retVal.set( Calendar.YEAR, 2002 );
    retVal.set( Calendar.MONTH, Calendar.JULY );
    retVal.set( Calendar.DAY_OF_MONTH, 4 );
    return retVal;
  }

  /** Labor Day 2002 */
  public final static Calendar LABORDAY() {
    Calendar retVal = new GregorianCalendar();
    retVal.set( Calendar.YEAR, 2002 );
    retVal.set( Calendar.MONTH, Calendar.SEPTEMBER );
    retVal.set( Calendar.DAY_OF_MONTH, 2 );
    return retVal;
  }

  /** Holidays (no rowing) */
  public final static Calendar[] holidays() {
    Calendar[] retVal = new Calendar[] {
      EASTER(), MEMORIALDAY(), JULY4TH(), LABORDAY() };
    return retVal;
  }

  /** Tests if a date is a holiday */
  public final static boolean isHoliday( Calendar date ) {
    if ( date == null ) {
      throw new IllegalArgumentException( "null date" );
    }
    boolean retVal = false;
    Calendar[] hdays = holidays();
    for ( int i=0; i<hdays.length && !retVal; i++ ) {
      final int hMonth = hdays[i].get( Calendar.MONTH );
      final int hDay = hdays[i].get( Calendar.DAY_OF_MONTH );
      retVal = hMonth == date.get( Calendar.MONTH );
      retVal = retVal && hDay == date.get( Calendar.DAY_OF_MONTH );
    }
    return retVal;
  }

  /**
   * Command-line entry point. Takes one optional, case-insensitive argument:
   * "mysql" (without the quotation marks). If this parameter is present,
   * INSERT statements are printed in a format compatible with MySQL. If this
   * parameter is absent or misspelled, INSERT statements are printed in a
   * format compatible with Oracle.
   */
  public static void main( String[] args ) {

    boolean isOracle = true;
    if ( args.length > 0 && args[0].equalsIgnoreCase("mysql") ) {
      isOracle = false;
    }

    Season season = new SeasonImpl();

    Calendar date = new GregorianCalendar();
    final int thisYear = date.get( Calendar.YEAR );
    /*
    date.set( Calendar.YEAR, thisYear - 1 );
    date.set( Calendar.MONTH, Calendar.SEPTEMBER );
    date.set( Calendar.DAY_OF_MONTH, 1 );
    */
    date.set( Calendar.YEAR, thisYear );
    date.set( Calendar.MONTH, Calendar.MARCH );
    date.set( Calendar.DAY_OF_MONTH, 20 );

    // Note CVS keywords are broken apart to avoid expansion in source code
    System.out.println( "-- $" + "Revision:" + "$" );
    System.out.println( "-- $" + "Date:" + "$" );
    System.out.println( "-- Generated " + (new Date()).toString() );
    System.out.println();
    System.out.println( "DELETE FROM RowingSession;" );
    System.out.println();

    while ( date.get( Calendar.YEAR) < thisYear + 1 ) {

      if ( !isHoliday(date) ) {

        RowingSessionSnapshot[] arrRS = season.createRowingSessions( date );
        for ( int i=0; i<arrRS.length; i++ ) {
          printRowingSession( isOracle, arrRS[i], System.out );
        }

        date.add( Calendar.DATE, 1 );

      } // if

    } // while

    return;
  } // main

  /** Returns a random RowingSessionState value */
  public static RowingSessionState randomRowingSessionState() {
    int idx =  (int) (arrRSS.length * Math.random());
    RowingSessionState retVal = arrRSS[ idx ];
    return retVal;
  }

  /** Prints an INSERT statement for a rowing session */
  public static void printRowingSession(
      boolean isOracle, RowingSessionSnapshot rs, PrintStream pw) {

    StringBuffer sb = new StringBuffer();
    sb.append( "'" + DATE_FORMAT.format( rs.getDate() ) + "', " );
    String strDate = new String( sb );

    if ( isOracle ) {
      sb.delete( 0, sb.length() );
      sb.append( "TO_DATE( " + strDate + "'YYYY-MM-DD HH24:MI:SS' ), " );
      strDate = new String( sb );
      sb.delete( 0, sb.length() );
    }
    else {
      sb.delete( 0, sb.length() );
      sb.append( "UPDATE ClraSequence SET id = LAST_INSERT_ID(id + 1) " );
      sb.append( "WHERE name = 'RowingSession'; ");
    }
    sb.append( "INSERT INTO RowingSession( rowing_id, rowing_date, " );
    sb.append( "rowing_level, rowing_type, rowing_state ) " );

    if ( isOracle ) {
      sb.append( "VALUES ( clra_rowing.NEXTVAL, " );
    }
    else {
      sb.append( "VALUES ( LAST_INSERT_ID(), " );
    }
    sb.append( strDate );
    sb.append( "'" + rs.getLevel().getName() + "', " );
    sb.append( "'" + rs.getType().getName() + "', " );
    sb.append( "'" + rs.getState().getName() + "' ); " );

   pw.println( new String(sb) );

   return;
  } // printRowingSession(boolean,RowingSessionSnapshot,PrintWriter)

  /** Declares an operation to return rowing sessions given a calendar date */
  public static interface Season {

    /** A place-holder id for rowing sessions */
    public final static Integer PLACEHOLDER_ID = new Integer( -1 );

    /**
     * Returns an array of rowing session for the given date, or throws
     * an IllegalArgumentException if the date is null or out of season.
     */
    public RowingSessionSnapshot[] createRowingSessions( Calendar date );
  }

  /**
   * A convenience class that delegates work to the Winter, Spring, Summer
   * or Autumn seasons depending on date.
   */
  public static class SeasonImpl implements Season {

    private final static Winter winter = new Winter();
    private final static Spring spring = new Spring();
    private final static Summer summer = new Summer();
    private final static Autumn autumn = new Autumn();

    public RowingSessionSnapshot[] createRowingSessions( Calendar date ) {
      if ( date == null ) {
        throw new IllegalArgumentException( "null calendar date" );
      }

      RowingSessionSnapshot[] retVal = null;
      int month = date.get( Calendar.MONTH );
      switch( month ) {

        case Calendar.DECEMBER:
        case Calendar.UNDECIMBER:
        case Calendar.JANUARY:
        case Calendar.FEBRUARY:
        case Calendar.MARCH:
          retVal = winter.createRowingSessions( date );
          break;

        case Calendar.APRIL:
        case Calendar.MAY:
          retVal = spring.createRowingSessions( date );
          break;

        case Calendar.JUNE:
        case Calendar.JULY:
        case Calendar.AUGUST:
          retVal = summer.createRowingSessions( date );
          break;

        case Calendar.SEPTEMBER:
        case Calendar.OCTOBER:
        case Calendar.NOVEMBER:
          retVal = autumn.createRowingSessions( date );
          break;

        default:
          throw new IllegalStateException( "illegal month == " + month);

      } // switch month

      return retVal;
    } // createRowingSessions(Calendar)

  } // SeasonImpl

  /**
   * Implements the <strong>Winter</strong> season from December 1 through
   * March 31.<br>
   * Monday - Thursday, 7:00 - 8:30 pm.<br>
   * Tuesday, Friday, 5:45 - 7:00 am.<br>
   * Sunday 6:00 - 7:30 pm.<p>
   */
  public static class Winter implements Season {

    public RowingSessionSnapshot[] createRowingSessions(Calendar date) {

      RowingSessionSnapshot[] retVal = null;
      int dow = date.get( Calendar.DAY_OF_WEEK );
      switch ( dow ) {

        case Calendar.MONDAY:
        case Calendar.WEDNESDAY:
        case Calendar.THURSDAY: {
          /*
          date.set( Calendar.HOUR_OF_DAY, 19 );
          date.set( Calendar.MINUTE, 0 );
          RowingSessionSnapshot rs = new RowingSessionSnapshot(
              Season.PLACEHOLDER_ID,
              randomRowingSessionState(),
              date.getTime(),
              RowingSessionLevel.REGULAR,
              RowingSessionType.PRACTICE
            );
          retVal = new RowingSessionSnapshot[] { rs };
          */
          retVal = new RowingSessionSnapshot[0];
          break;
        }

        case Calendar.TUESDAY: {
          /*
          date.set( Calendar.HOUR_OF_DAY, 5 );
          date.set( Calendar.MINUTE, 45 );
          RowingSessionSnapshot rs1 = new RowingSessionSnapshot(
              Season.PLACEHOLDER_ID,
              randomRowingSessionState(),
              date.getTime(),
              RowingSessionLevel.REGULAR,
              RowingSessionType.PRACTICE
            );
          date.set( Calendar.HOUR_OF_DAY, 19 );
          date.set( Calendar.MINUTE, 0 );
          RowingSessionSnapshot rs2 = new RowingSessionSnapshot(
              Season.PLACEHOLDER_ID,
              randomRowingSessionState(),
              date.getTime(),
              RowingSessionLevel.REGULAR,
              RowingSessionType.PRACTICE
            );
          retVal = new RowingSessionSnapshot[] { rs1, rs2 };
          */
          retVal = new RowingSessionSnapshot[0];
          break;
        }

        case Calendar.FRIDAY: {
          /*
          date.set( Calendar.HOUR_OF_DAY, 5 );
          date.set( Calendar.MINUTE, 45 );
          RowingSessionSnapshot rs = new RowingSessionSnapshot(
              Season.PLACEHOLDER_ID,
              randomRowingSessionState(),
              date.getTime(),
              RowingSessionLevel.REGULAR,
              RowingSessionType.PRACTICE
            );
          retVal = new RowingSessionSnapshot[] { rs };
          */
          retVal = new RowingSessionSnapshot[0];
          break;
        }

        case Calendar.SATURDAY: {
          retVal = new RowingSessionSnapshot[0];
          break;
        }

        case Calendar.SUNDAY: {
          /*
          date.set( Calendar.HOUR_OF_DAY, 18 );
          date.set( Calendar.MINUTE, 0 );
          RowingSessionSnapshot rs = new RowingSessionSnapshot(
              Season.PLACEHOLDER_ID,
              randomRowingSessionState(),
              date.getTime(),
              RowingSessionLevel.REGULAR,
              RowingSessionType.PRACTICE
            );
          retVal = new RowingSessionSnapshot[] { rs };
          */
          retVal = new RowingSessionSnapshot[0];
          break;
        }

        default:
          throw new IllegalStateException( "invalid day of week == " + dow );

      } // switch dow

      return retVal;

    } // createRowingSessions(Calendar)

  } // Winter

  /**
   * Implements the <strong>Spring</strong> season from April 1 through
   * May 31.<br>
   * Monday - Friday, 5:45 - 7:00 am.<br>
   * Sunday 1:00 - 3:00 pm.<p>
   */
  public static class Spring implements Season {

    public RowingSessionSnapshot[] createRowingSessions(Calendar date) {

      RowingSessionSnapshot[] retVal = null;
      int dow = date.get( Calendar.DAY_OF_WEEK );
      switch ( dow ) {

        case Calendar.MONDAY:
        case Calendar.TUESDAY:
        case Calendar.WEDNESDAY:
        case Calendar.THURSDAY:
        case Calendar.FRIDAY: {
          date.set( Calendar.HOUR_OF_DAY, 5 );
          date.set( Calendar.MINUTE, 45 );
          RowingSessionSnapshot rs = new RowingSessionSnapshot(
              Season.PLACEHOLDER_ID,
              /* randomRowingSessionState() */ RowingSessionState.OPEN,
              date.getTime(),
              RowingSessionLevel.REGULAR,
              RowingSessionType.PRACTICE
            );
          retVal = new RowingSessionSnapshot[] { rs };
          break;
        }

        case Calendar.SATURDAY: {
          retVal = new RowingSessionSnapshot[0];
          break;
        }

        case Calendar.SUNDAY: {
          date.set( Calendar.HOUR_OF_DAY, 13 );
          date.set( Calendar.MINUTE, 0 );
          RowingSessionSnapshot rs1 = new RowingSessionSnapshot(
              Season.PLACEHOLDER_ID,
              /* randomRowingSessionState() */ RowingSessionState.OPEN,
              date.getTime(),
              RowingSessionLevel.REGULAR,
              RowingSessionType.PRACTICE
            );
          date.set( Calendar.HOUR_OF_DAY, 15 );
          date.set( Calendar.MINUTE, 0 );
          RowingSessionSnapshot rs2 = new RowingSessionSnapshot(
              Season.PLACEHOLDER_ID,
              /* randomRowingSessionState() */ RowingSessionState.OPEN,
              date.getTime(),
              RowingSessionLevel.REGULAR,
              RowingSessionType.PRACTICE
            );
          retVal = new RowingSessionSnapshot[] { rs1, rs2 };
          break;
        }

        default:
          throw new IllegalStateException( "invalid day of week == " + dow );

      } // switch dow

      return retVal;

    } // createRowingSessions(Calendar)

  } // Spring

  /**
   * Implements the <strong>Summer</strong> season from June 1 through
   * August 31.<br>
   * Monday - Friday, 7:00 - 8:30 pm.<br>
   * Sunday 1:00 - 3:00 pm.<p>
   */
  public static class Summer implements Season {

    public RowingSessionSnapshot[] createRowingSessions(Calendar date) {

      RowingSessionSnapshot[] retVal = null;
      int dow = date.get( Calendar.DAY_OF_WEEK );
      switch ( dow ) {

        case Calendar.MONDAY:
        case Calendar.TUESDAY:
        case Calendar.WEDNESDAY:
        case Calendar.THURSDAY:
        case Calendar.FRIDAY: {
          date.set( Calendar.HOUR_OF_DAY, 19 );
          date.set( Calendar.MINUTE, 0 );
          RowingSessionSnapshot rs = new RowingSessionSnapshot(
              Season.PLACEHOLDER_ID,
              /* randomRowingSessionState() */ RowingSessionState.OPEN,
              date.getTime(),
              RowingSessionLevel.REGULAR,
              RowingSessionType.PRACTICE
            );
          retVal = new RowingSessionSnapshot[] { rs };
          break;
        }

        case Calendar.SATURDAY: {
          retVal = new RowingSessionSnapshot[0];
          break;
        }

        case Calendar.SUNDAY: {
          date.set( Calendar.HOUR_OF_DAY, 15 );
          date.set( Calendar.MINUTE, 0 );
          RowingSessionSnapshot rs1 = new RowingSessionSnapshot(
              Season.PLACEHOLDER_ID,
              /* randomRowingSessionState() */ RowingSessionState.OPEN,
              date.getTime(),
              RowingSessionLevel.REGULAR,
              RowingSessionType.PRACTICE
            );
          date.set( Calendar.HOUR_OF_DAY, 17 );
          date.set( Calendar.MINUTE, 0 );
          RowingSessionSnapshot rs2 = new RowingSessionSnapshot(
              Season.PLACEHOLDER_ID,
              /* randomRowingSessionState() */ RowingSessionState.OPEN,
              date.getTime(),
              RowingSessionLevel.REGULAR,
              RowingSessionType.PRACTICE
            );
          retVal = new RowingSessionSnapshot[] { rs1, rs2 };
          break;
        }

        default:
          throw new IllegalStateException( "invalid day of week == " + dow );

      } // switch dow

      return retVal;

    } // createRowingSessions(Calendar)

  } // Summer

  /**
   * Implements the <strong>Autumn</strong> season from December 1 through
   * <strong>Autumn</strong>: from September 1 through November 30.<br>
   * Monday, Wednesday, Thursday, 5:45 - 7:00 am (REGULAR)<br>
   * Tuesday, Friday, 5:45 - 7:00 am (Learn-To-Row)<br>
   * Sunday 1:00 - 3:00 pm (REGULAR)<br>
   * Sunday 3:00 - 5:00 pm (Learn-To-Row)<p>
   */
  public static class Autumn implements Season {

    public RowingSessionSnapshot[] createRowingSessions(Calendar date) {

      RowingSessionSnapshot[] retVal = null;
      int dow = date.get( Calendar.DAY_OF_WEEK );
      switch ( dow ) {

        case Calendar.MONDAY:
        case Calendar.WEDNESDAY:
        case Calendar.THURSDAY: {
          date.set( Calendar.HOUR_OF_DAY, 5 );
          date.set( Calendar.MINUTE, 45 );
          RowingSessionSnapshot rs = new RowingSessionSnapshot(
              Season.PLACEHOLDER_ID,
              /* randomRowingSessionState() */ RowingSessionState.TENATIVE,
              date.getTime(),
              RowingSessionLevel.REGULAR,
              RowingSessionType.PRACTICE
            );
          retVal = new RowingSessionSnapshot[] { rs };
          break;
        }

        case Calendar.TUESDAY:
        case Calendar.FRIDAY: {
          date.set( Calendar.HOUR_OF_DAY, 5 );
          date.set( Calendar.MINUTE, 45 );
          RowingSessionSnapshot rs = new RowingSessionSnapshot(
              Season.PLACEHOLDER_ID,
              /* randomRowingSessionState() */ RowingSessionState.TENATIVE,
              date.getTime(),
              RowingSessionLevel.LTR,
              RowingSessionType.PRACTICE
            );
          retVal = new RowingSessionSnapshot[] { rs };
          break;
        }

        case Calendar.SATURDAY: {
          retVal = new RowingSessionSnapshot[0];
          break;
        }

        case Calendar.SUNDAY: {
          date.set( Calendar.HOUR_OF_DAY, 13 );
          date.set( Calendar.MINUTE, 0 );
          RowingSessionSnapshot rs1 = new RowingSessionSnapshot(
              Season.PLACEHOLDER_ID,
              /* randomRowingSessionState() */ RowingSessionState.TENATIVE,
              date.getTime(),
              RowingSessionLevel.REGULAR,
              RowingSessionType.PRACTICE
            );
          date.set( Calendar.HOUR_OF_DAY, 15 );
          date.set( Calendar.MINUTE, 0 );
          RowingSessionSnapshot rs2 = new RowingSessionSnapshot(
              Season.PLACEHOLDER_ID,
              /* randomRowingSessionState() */ RowingSessionState.TENATIVE,
              date.getTime(),
              RowingSessionLevel.LTR,
              RowingSessionType.PRACTICE
            );
          retVal = new RowingSessionSnapshot[] { rs1, rs2 };
          break;
        }

        default:
          throw new IllegalStateException( "invalid day of week == " + dow );

      } // switch dow

      return retVal;

    } // createRowingSessions(Calendar)

  } // Autumn

} // CreateRowingSessions

/*
 * $Log: CreateRowingSessions.java,v $
 * Revision 1.4  2003/02/26 03:38:47  rphall
 * Added copyright and GPL license
 *
 * Revision 1.3  2003/02/19 22:09:20  rphall
 * Removed gratuitous use of CLRA acronym
 *
 * Revision 1.2  2002/03/17 05:31:57  rphall
 * Adjusted for 2002 season
 */

