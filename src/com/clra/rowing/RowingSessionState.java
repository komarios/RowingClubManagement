/*
 * Copyright (c) Carnegie Lake Rowing Association 2002. All rights reserved.
 * Distributed under the GPL license. See doc/COPYING.
 * $RCSfile: RowingSessionState.java,v $
 * $Date: 2003/02/26 03:38:45 $
 * $Revision: 1.3 $
 */

package com.clra.rowing;

import com.clra.util.ValidationException;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.Map;
import javax.ejb.EJBObject;

/**
 * Represents the state of a rowing session.
 *
 * @version $Id: RowingSessionState.java,v 1.3 2003/02/26 03:38:45 rphall Exp $
 * @author <a href="mailto:rphall@pluto.njcc.com">Rick Hall</a>
 */
public class RowingSessionState implements Comparable, Serializable {

  public final static String NAME_NEW = "NEW";

  public final static String NAME_TENATIVE = "TENATIVE";

  public final static String NAME_OPEN = "OPEN";

  public final static String NAME_LOCKED = "LOCKED";

  /** Initial boatings */
  public final static String NAME_BOATING1 = "BOATING1";

  /** Final boatings */
  public final static String NAME_BOATING2 = "BOATING2";

  /** Attendance. (Combine with BOATING2?) */
  public final static String NAME_COMPLETE = "COMPLETE";

  public final static String NAME_INVOICING = "INVOICING";

  public final static String NAME_CLOSED = "CLOSED";

  public final static String NAME_CANCELLED = "CANCELLED";

  public final static RowingSessionState NEW =
      new RowingSessionState( NAME_NEW );

  public final static RowingSessionState TENATIVE =
      new RowingSessionState( NAME_TENATIVE );

  public final static RowingSessionState OPEN =
      new RowingSessionState( NAME_OPEN );

  public final static RowingSessionState LOCKED =
      new RowingSessionState( NAME_LOCKED );

  public final static RowingSessionState BOATING1 =
      new RowingSessionState( NAME_BOATING1 );

  public final static RowingSessionState BOATING2 =
      new RowingSessionState( NAME_BOATING2 );

  public final static RowingSessionState COMPLETE =
      new RowingSessionState( NAME_COMPLETE );

  public final static RowingSessionState INVOICING =
      new RowingSessionState( NAME_INVOICING );

  public final static RowingSessionState CLOSED =
      new RowingSessionState( NAME_CLOSED );

  public final static RowingSessionState CANCELLED =
      new RowingSessionState( NAME_CANCELLED );

  private final String name;

  protected RowingSessionState( String name ) {

    // Assign blank final
    this.name = name;

    // Enforce preconditions
  }

  public static RowingSessionState getState( String name )
    throws RowingException {

    // Preconditions
    if ( name == null || name.trim().length() == 0 ) {
      throw new RowingException( "invalid level name" );
    }

    RowingSessionState retVal = null;
    name = name.trim();
    if ( name.equalsIgnoreCase( NAME_TENATIVE ) ) {
      retVal = TENATIVE;
    }
    else if ( name.equalsIgnoreCase( NAME_OPEN ) ) {
      retVal = OPEN;
    }
    else if ( name.equalsIgnoreCase( NAME_LOCKED ) ) {
      retVal = LOCKED;
    }
    else if ( name.equalsIgnoreCase( NAME_BOATING1 ) ) {
      retVal = BOATING1;
    }
    else if ( name.equalsIgnoreCase( NAME_BOATING2 ) ) {
      retVal = BOATING2;
    }
    else if ( name.equalsIgnoreCase( NAME_COMPLETE ) ) {
      retVal = COMPLETE;
    }
    else if ( name.equalsIgnoreCase( NAME_INVOICING ) ) {
      retVal = INVOICING;
    }
    else if ( name.equalsIgnoreCase( NAME_CLOSED ) ) {
      retVal = CLOSED;
    }
    else if ( name.equalsIgnoreCase( NAME_CANCELLED ) ) {
      retVal = CANCELLED;
    }
    else {
      throw new RowingException( "invalid name == '" + name + "'" );
    }

    return retVal;
  } // getState(String)

  public String getName() {
    return name;
  }

  public String toString() {
    return name;
  }

  public boolean equals( Object o ) {

    boolean retVal = false;
    if ( o instanceof RowingSessionState ) {
      retVal = getName().equalsIgnoreCase( ((RowingSessionState)o).getName() );
    }

    return retVal;
  } // equals(Object)

  public int hashCode() {
    return getName().toUpperCase().hashCode();
  }

  /**
   * Compares a level to other levels and valid level names. The default
   * order is described in the documentation for
   * <tt>DefaultRowingSessionStateComparator</tt>
   * @param o a non-null RowingSessionLevel or a String NAME of a level
   * @see DefaultRowingSessionStateComparator
   */
  public int compareTo( Object o ) {
    return DefaultRowingSessionStateComparator.staticCompare( this, o );
  }

} // RowingSessionState

/*
 * $Log: RowingSessionState.java,v $
 * Revision 1.3  2003/02/26 03:38:45  rphall
 * Added copyright and GPL license
 *
 * Revision 1.2  2002/02/18 18:04:59  rphall
 * Ran dos2unix to remove ^M (carriage return) from end of lines
 *
 * Revision 1.1.1.1  2002/01/03 21:57:28  rphall
 * Initial load, 5th try, Jan-03-2002 4:57 PM
 *
 * Revision 1.9  2001/12/18 13:31:07  rphall
 * Whitespace
 *
 * Revision 1.8  2001/12/07 01:07:24  rphall
 * Checkpt: before debugging slow perf
 *
 * Revision 1.7  2001/12/06 21:26:48  rphall
 * Checkpt
 *
 * Revision 1.6  2001/12/06 04:47:20  rphall
 * Added equals(Object) and hashCode()
 *
 * Revision 1.5  2001/12/01 14:05:17  rphall
 * Changed states to BOATING1 and BOATING2
 *
 * Revision 1.4  2001/11/30 11:38:00  rphall
 * First working version, RowingSession entity bean
 *
 * Revision 1.3  2001/11/29 15:42:54  rphall
 * Added static utility that looks up instance by name
 *
 * Revision 1.2  2001/11/28 11:56:16  rphall
 * New states
 *
 * Revision 1.1  2001/11/23 18:34:08  rphall
 * Major revision.
 *
 */

