/*
 * Copyright (c) Carnegie Lake Rowing Association 2002. All rights reserved.
 * Distributed under the GPL license. See doc/COPYING.
 * $RCSfile: SeatPreference.java,v $
 * $Date: 2003/02/26 03:38:45 $
 * $Revision: 1.3 $
 */

package com.clra.rowing;

import java.io.Serializable;

/**
 * Defines seating preference that may be specified by a participant for
 * a rowing session.
 *
 * @version $Id: SeatPreference.java,v 1.3 2003/02/26 03:38:45 rphall Exp $
 * @author <a href="mailto:rphall@pluto.njcc.com">Rick Hall</a>
 */
public class SeatPreference implements Serializable {

  /** Indicates a member prefers a starboard rowing position */
  public final static String NAME_STARBOARD = "STARBOARD";

  /** Indicates a member prefers a port rowing position */
  public final static String NAME_PORT = "PORT";

  /**
   * Indicates a member prefers a starboard position, but will also
   * row port.
   */
  public final static String NAME_STARBOARD_THEN_PORT = "S(P)";

  /**
   * Indicates a member prefers a port position, but will also
   * row starboard.
   */
  public final static String NAME_PORT_THEN_STARBOARD = "P(S)";

  /** Indicates a member prefers to cox. */
  public final static String NAME_COX = "COX";

  /** Indicates a member prefers a starboard rowing position */
  public final static SeatPreference STARBOARD =
      new SeatPreference( NAME_STARBOARD );

  /** Indicates a member prefers a port rowing position */
  public final static SeatPreference PORT =
      new SeatPreference( NAME_PORT );

  /**
   * Indicates a member prefers a starboard position, but will also
   * row port.
   */
  public final static SeatPreference STARBOARD_THEN_PORT =
      new SeatPreference( NAME_STARBOARD_THEN_PORT );

  /**
   * Indicates a member prefers a port position, but will also
   * row starboard.
   */
  public final static SeatPreference PORT_THEN_STARBOARD =
      new SeatPreference( NAME_PORT_THEN_STARBOARD );

  /** Indicates a member prefers to cox. */
  public final static SeatPreference COX =
      new SeatPreference( NAME_COX );

  private final String name;

  protected SeatPreference( String name ) {

    // Assign blank final
    this.name = name;

    // Enforce preconditions
  }

  /**
   * SeatPreference is not a required field in a participant record, therefore
   * if a null or blank name is passed to this method, an
   * IllegalArgumentException is <strong>NOT</strong> thrown, but rather
   * a null instance is returned.
   * </p><p>
   * Note: values not defined by this class, will throw a RowingException.
   */
  public static SeatPreference getSeatPreference( String name )
    throws RowingException {

    SeatPreference retVal = null;
    if ( name != null && name.trim().length() != 0 ) {
      name = name.trim();
      if ( name.equalsIgnoreCase( NAME_STARBOARD ) ) {
        retVal = STARBOARD;
      }
      else if ( name.equalsIgnoreCase( NAME_PORT ) ) {
        retVal = PORT;
      }
      else if ( name.equalsIgnoreCase( NAME_STARBOARD_THEN_PORT ) ) {
        retVal = STARBOARD_THEN_PORT;
      }
      else if ( name.equalsIgnoreCase( NAME_PORT_THEN_STARBOARD ) ) {
        retVal = PORT_THEN_STARBOARD;
      }
      else if ( name.equalsIgnoreCase( NAME_COX ) ) {
        retVal = COX;
      }
      else {
        throw new RowingException( "invalid name == '" + name + "'" );
      }
    }

    return retVal;
  } // getSeatPreference(String)

  public String getName() {
    return name;
  }

  public String toString() {
    return name;
  }

  public int hashCode() {
    return this.getName().toUpperCase().hashCode();
  }

  public static boolean compare( String s1, String s2 ) {

    boolean retVal = false;
    if ( s1 != null ) {
      if ( s2 != null ) {
        retVal = s1.toUpperCase().trim().equals( s2.toUpperCase().trim() );
      }
    }

    return retVal;
  } // compare(String,String)

  public boolean equals( Object o ) {

    boolean retVal = false;
    if ( o instanceof SeatPreference ) {
      retVal = this.getName().equalsIgnoreCase( ((SeatPreference)o).getName() );
    }

    return retVal;
  } // equals(Object)

} // SeatPreference

/*
 * $Log: SeatPreference.java,v $
 * Revision 1.3  2003/02/26 03:38:45  rphall
 * Added copyright and GPL license
 *
 * Revision 1.2  2002/02/18 18:05:09  rphall
 * Ran dos2unix to remove ^M (carriage return) from end of lines
 *
 * Revision 1.1.1.1  2002/01/03 21:57:28  rphall
 * Initial load, 5th try, Jan-03-2002 4:57 PM
 *
 * Revision 1.7  2001/12/19 02:19:16  rphall
 * Fixed static getXxx factory method to accept blank names
 *
 * Revision 1.6  2001/12/18 16:43:48  rphall
 * Serializable
 *
 * Revision 1.5  2001/12/18 13:29:58  rphall
 * Simplified 'equals' method
 *
 * Revision 1.4  2001/12/15 02:25:03  rphall
 * Added equals, compareTo, hashCode
 *
 * Revision 1.3  2001/12/13 21:17:04  rphall
 * Fixed handling of null name in static lookup
 *
 * Revision 1.2  2001/12/13 01:30:21  rphall
 * Enrollment business and web objects
 *
 * Revision 1.1  2001/11/23 18:34:08  rphall
 * Major revision.
 *
 * Revision 1.2  2001/11/18 18:15:01  rphall
 * Fixed compilation problems
 *
 * Revision 1.1  2001/11/18 17:07:07  rphall
 * Checkpt before major revision of rowing package
 *
 */

