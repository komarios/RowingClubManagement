/*
 * Copyright (c) Carnegie Lake Rowing Association 2002. All rights reserved.
 * Distributed under the GPL license. See doc/COPYING.
 * $RCSfile: Attendance.java,v $
 * $Date: 2003/02/26 03:38:45 $
 * $Revision: 1.3 $
 */

package com.clra.rowing;

import java.io.Serializable;

/**
 * Defines the possible attendance values for a participant in a rowing
 * session.
 *
 * @version $Id: Attendance.java,v 1.3 2003/02/26 03:38:45 rphall Exp $
 * @author <a href="mailto:rphall@pluto.njcc.com">Rick Hall</a>
 */
public class Attendance implements Serializable {

  /**
   * Indicates that a participant showed up for a practice, regardless of
   * whether participant was a signup, a substitute, or an extra.
   */
  public final static String NAME_PRESENT = "PRESENT";

  /**
   * Indicates that a signed-up participant did not show up for a practice,
   * but either was not assigned, or was assigned but found a substitute.
   */
  public final static String NAME_ABSENT = "ABSENT";

  public final static Attendance PRESENT = new Attendance( NAME_PRESENT );

  public final static Attendance ABSENT = new Attendance( NAME_ABSENT );

  private final String name;

  protected Attendance( String name ) {

    // Assign blank final
    this.name = name;

    // Enforce preconditions
  }

  /**
   * Attendance is not a required field in a participant record, therefore
   * if a null or blank name is passed to this method, an
   * IllegalArgumentException is <strong>NOT</strong> thrown,
   * but rather a null instance is returned.
   * </p><p>
   * Note: values not defined by this class, will throw a RowingException.
   */
  public static Attendance getAttendance( String name )
    throws RowingException {

    Attendance retVal = null;
    if ( name != null && name.trim().length() != 0 ) {
      name = name.trim();
      if ( name.equalsIgnoreCase( NAME_PRESENT ) ) {
        retVal = PRESENT;
      }
      else if ( name.equalsIgnoreCase( NAME_ABSENT ) ) {
        retVal = ABSENT;
      }
      else {
        throw new RowingException( "invalid name == '" + name + "'" );
      }
    }

    return retVal;
  } // getAttendance(String)

  public String getName() {
    return name;
  }

  public String toString() {
    return name;
  }

  public int hashCode() {
    return this.getName().hashCode();
  }

  public boolean equals( Object o ) {

    boolean retVal = false;
    if ( o instanceof Attendance ) {
      retVal = this.getName().equalsIgnoreCase( ((Attendance)o).getName() );
    }

    return retVal;
  } // equals(Object)

} // Attendance

/*
 * $Log: Attendance.java,v $
 * Revision 1.3  2003/02/26 03:38:45  rphall
 * Added copyright and GPL license
 *
 * Revision 1.2  2002/02/18 18:03:42  rphall
 * Ran dos2unix to remove ^M (carriage return) from end of lines
 *
 * Revision 1.1.1.1  2002/01/03 21:57:28  rphall
 * Initial load, 5th try, Jan-03-2002 4:57 PM
 *
 * Revision 1.8  2001/12/19 02:19:16  rphall
 * Fixed static getXxx factory method to accept blank names
 *
 * Revision 1.7  2001/12/18 16:43:48  rphall
 * Serializable
 *
 * Revision 1.6  2001/12/18 13:29:18  rphall
 * Added 'equals' and 'hashCode' methods
 *
 * Revision 1.5  2001/12/13 21:17:04  rphall
 * Fixed handling of null name in static lookup
 *
 * Revision 1.4  2001/12/13 01:30:21  rphall
 * Enrollment business and web objects
 *
 * Revision 1.3  2001/11/23 18:25:22  rphall
 * Simplified class to a tagging data type
 *
 * Revision 1.1  2001/11/18 17:07:07  rphall
 * Checkpt before major revision of rowing package
 *
 */

