/*
 * Copyright (c) Carnegie Lake Rowing Association 2002. All rights reserved.
 * Distributed under the GPL license. See doc/COPYING.
 * $RCSfile: RowingSessionType.java,v $
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
 * Represents the type of a rowing session.
 *
 * @version $Id: RowingSessionType.java,v 1.3 2003/02/26 03:38:45 rphall Exp $
 * @author <a href="mailto:rphall@pluto.njcc.com">Rick Hall</a>
 */
public class RowingSessionType implements Comparable, Serializable {

  public final static String NAME_PRACTICE = "PRACTICE";

  public final static String NAME_REGATTA = "REGATTA";

  public final static RowingSessionType PRACTICE =
      new RowingSessionType( NAME_PRACTICE );

  public final static RowingSessionType REGATTA =
      new RowingSessionType( NAME_REGATTA );

  private final String name;

  protected RowingSessionType( String name ) {

    // Assign blank final
    this.name = name;

    // Enforce preconditions
  }

  public static RowingSessionType getType( String name )
    throws RowingException {

    // Preconditions
    if ( name == null || name.trim().length() == 0 ) {
      throw new RowingException( "invalid level name" );
    }

    RowingSessionType retVal = null;
    name = name.trim();
    if ( name.equalsIgnoreCase( NAME_PRACTICE ) ) {
      retVal = PRACTICE;
    }
    else if ( name.equalsIgnoreCase( NAME_REGATTA ) ) {
      retVal = REGATTA;
    }
    else {
      throw new RowingException( "invalid name == '" + name + "'" );
    }

    return retVal;
  } // getType(String)

  public String getName() {
    return name;
  }

  public String toString() {
    return name;
  }

  public boolean equals( Object o ) {

    boolean retVal = false;
    if ( o instanceof RowingSessionType ) {
      retVal = getName().equalsIgnoreCase( ((RowingSessionType)o).getName() );
    }

    return retVal;
  } // equals(Object)

  public int hashCode() {
    return getName().toUpperCase().hashCode();
  }

  /**
   * Compares a level to other levels and valid level names. The default
   * order is described in the documentation for
   * <tt>DefaultRowingSessionTypeComparator</tt>
   * @param o a non-null RowingSessionLevel or a String NAME of a level
   * @see DefaultRowingSessionTypeComparator
   */
  public int compareTo( Object o ) {
    return DefaultRowingSessionTypeComparator.staticCompare( this, o );
  }

} // RowingSessionType

/*
 * $Log: RowingSessionType.java,v $
 * Revision 1.3  2003/02/26 03:38:45  rphall
 * Added copyright and GPL license
 *
 * Revision 1.2  2002/02/18 18:05:04  rphall
 * Ran dos2unix to remove ^M (carriage return) from end of lines
 *
 * Revision 1.1.1.1  2002/01/03 21:57:28  rphall
 * Initial load, 5th try, Jan-03-2002 4:57 PM
 *
 * Revision 1.7  2001/12/18 13:31:07  rphall
 * Whitespace
 *
 * Revision 1.6  2001/12/07 01:07:24  rphall
 * Checkpt: before debugging slow perf
 *
 * Revision 1.5  2001/12/06 21:26:48  rphall
 * Checkpt
 *
 * Revision 1.4  2001/12/06 04:47:20  rphall
 * Added equals(Object) and hashCode()
 *
 * Revision 1.3  2001/11/30 11:38:00  rphall
 * First working version, RowingSession entity bean
 *
 * Revision 1.2  2001/11/29 15:42:54  rphall
 * Added static utility that looks up instance by name
 *
 * Revision 1.1  2001/11/23 18:34:08  rphall
 * Major revision.
 *
 */

