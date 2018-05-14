/*
 * Copyright (c) Carnegie Lake Rowing Association 2002. All rights reserved.
 * Distributed under the GPL license. See doc/COPYING.
 * $RCSfile: DefaultRowingSessionLevelComparator.java,v $
 * $Date: 2003/02/26 03:38:45 $
 * $Revision: 1.3 $
 */

package com.clra.rowing;

import java.io.Serializable;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Category;

/**
 * Compares RowingSessionLevel instances as well as Strings. The default
 * order is LTR, REGULAR.
 *
 * @version $Revision: 1.3 $ $Date: 2003/02/26 03:38:45 $
 * @author <a href="mailto:rphall@pluto.njcc.com">Rick Hall</a>
 */
public class DefaultRowingSessionLevelComparator
  implements Comparator, Serializable {

  private final static String base =
    DefaultRowingSessionLevelComparator.class.getName();
  private final static Category theLog = Category.getInstance( base );

  private final static String makeKey( String s ) {
    return s.trim().toUpperCase();
  }

  private final static String makeKey( RowingSessionLevel rsl ) {
    return makeKey( rsl.getName() );
  }

  private final static Map ordinalMap = new HashMap();
  static {

    String key  = makeKey( RowingSessionLevel.NAME_REGULAR );
    Integer val = new Integer( 1 );
    ordinalMap.put( key, val );

    key = makeKey( RowingSessionLevel.NAME_LTR );
    val = new Integer( 2 );
    ordinalMap.put( key, val );

  } // static

  private final static Integer getOrdinal( String s ) {
    return (Integer) ordinalMap.get( makeKey(s) );
  }

  private final static Integer getOrdinal( RowingSessionLevel rsl ) {
    return (Integer) ordinalMap.get( makeKey(rsl) );
  }

  private final static Integer getOrdinal( Object o ) {

    Integer retVal = null;
    if ( o instanceof RowingSessionLevel ) {
      retVal = getOrdinal( ((RowingSessionLevel) o) );
    }
    else if ( o instanceof String ) {
      retVal = getOrdinal( ((String) o) );
    }

    return retVal;
  } // getOrdinal(Object)

  /**
   * Compares a level to other levels and valid level names. The default
   * order is LTR then REGULAR.
   * @param o a non-null RowingSessionLevel or a String NAME of a level
   */
  public static int staticCompare( Object o1, Object o2 ) {

    Integer rank1 = getOrdinal( o1 );
    Integer rank2 = getOrdinal( o2 );

    if ( o1 == null || o2 == null ) {
      String msg = "cannot compare '" + o1 + "' and '" + o2 + "'";
      String err = "DefaultRowingSessionLevelComparator.staticCompare: " + msg;
      theLog.error( err );
      throw new ClassCastException( msg );
    }

    return rank1.compareTo( rank2 );
  } // staticCompare(Object,Object)

  /** @see #staticCompare(Object,Object) */
  public int compare( Object o1, Object o2 ) {
    return staticCompare( o1, o2 );
  }

} // DefaultRowingSessionLevelComparator

/*
 * $Log: DefaultRowingSessionLevelComparator.java,v $
 * Revision 1.3  2003/02/26 03:38:45  rphall
 * Added copyright and GPL license
 *
 * Revision 1.2  2002/02/18 18:04:01  rphall
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

