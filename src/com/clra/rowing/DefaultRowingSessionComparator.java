/*
 * Copyright (c) Carnegie Lake Rowing Association 2002. All rights reserved.
 * Distributed under the GPL license. See doc/COPYING.
 * $RCSfile: DefaultRowingSessionComparator.java,v $
 * $Date: 2003/02/26 03:38:45 $
 * $Revision: 1.3 $
 */

package com.clra.rowing;

import com.clra.util.ISerializableComparator;
import java.rmi.RemoteException;
import org.apache.log4j.Category;

/**
 * Compares a rowing session with another rowing session for order.
 * @version $Revision: 1.3 $ $Date: 2003/02/26 03:38:45 $
 * @author <a href="mailto:rphall@pluto.njcc.com">Rick Hall</a>
 */
public class DefaultRowingSessionComparator implements ISerializableComparator {

  private final static String base =
    DefaultRowingSessionComparator.class.getName();
  private final static Category theLog = Category.getInstance( base );

  /**
   * Compares a rowing session with another rowing session for order.
   * Rowing sessions are compared by date, state, type, level, and id, in that
   * order.<p>
   *
   * The current implementation compares IRowingSession and
   * RowingSessionSnapshot instances..<p>
   *
   * @return a negative integer, zero, or a positive integer as the first
   * rowing session is less than, equal to, or greater than the second
   * rowing session.
   * @exception ClassCastException if either object is not a rowing session.
   * @exception IllegalStateException if either rowing session throws a
   * RemoteException.
   */
  public static int staticCompare( Object o1, Object o2 ) {

    String msg = null;
    RowingSessionSnapshot rs1 = null;
    RowingSessionSnapshot rs2 = null;

    try {
      if ( o1 instanceof IRowingSession ) {
        rs1 = ((IRowingSession)o1).getData();
      }
      else if ( o1 instanceof RowingSessionSnapshot ) {
        rs1 = (RowingSessionSnapshot) o1;
      }
      else {
        msg = "o1 is not a rowing session";
      }

      if ( o2 instanceof IRowingSession ) {
        rs2 = ((IRowingSession)o2).getData();
      }
      else if ( o2 instanceof RowingSessionSnapshot ) {
        rs2 = (RowingSessionSnapshot) o2;
      }
      else {
        String tmp = "o2 is not a rowing session";
        msg = msg == null ? tmp : msg + "; " + tmp ;
      }
    }
    catch( RemoteException x ) {
      String xMsg = "DefaultRowingSessionComparator.compareTo: "
        + x.getClass().getName() + ": " + x.getMessage();
      theLog.error( xMsg, x );
      throw new IllegalStateException( xMsg );
    }

    // Enforce preconditions
    if ( msg != null ) {
      throw new ClassCastException( msg );
    }

    int retVal = rs1.getDate().compareTo( rs2.getDate() );
    if ( retVal == 0 ) {
      retVal = rs1.getState().compareTo( rs2.getState() );
    }
    if ( retVal == 0 ) {
      retVal = rs1.getType().compareTo( rs2.getType() );
    }
    if ( retVal == 0 ) {
      retVal = rs1.getLevel().compareTo( rs2.getLevel() );
    }
    if ( retVal == 0 ) {
      retVal = rs1.getId().compareTo( rs2.getId() );
    }

    return retVal;
  } // compare(Object,Object)

  /** @see staticCompare(Object,Object) */
  public int compare( Object o1, Object o2 ) {
    return staticCompare( o1, o2 );
  }

} // DefaultRowingSessionComparator

/*
 * $Log: DefaultRowingSessionComparator.java,v $
 * Revision 1.3  2003/02/26 03:38:45  rphall
 * Added copyright and GPL license
 *
 * Revision 1.2  2002/02/18 18:03:58  rphall
 * Ran dos2unix to remove ^M (carriage return) from end of lines
 *
 * Revision 1.1.1.1  2002/01/03 21:57:28  rphall
 * Initial load, 5th try, Jan-03-2002 4:57 PM
 *
 * Revision 1.2  2001/12/07 01:07:24  rphall
 * Checkpt: before debugging slow perf
 *
 * Revision 1.1  2001/12/06 21:21:35  rphall
 * Comparator for default ordering
 *
 */

