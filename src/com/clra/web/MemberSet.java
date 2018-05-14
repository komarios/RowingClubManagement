/*
 * Copyright (c) Carnegie Lake Rowing Association 2002. All rights reserved.
 * Distributed under the GPL license. See doc/COPYING.
 * $RCSfile: MemberSet.java,v $
 * $Date: 2003/02/26 03:38:46 $
 * $Revision: 1.4 $
 */

package com.clra.web;

import com.clra.member.MemberDBRead;
import com.clra.member.MemberException;
import com.clra.member.MemberSnapshot;
import com.clra.util.ErrorUtils;
import com.clra.util.ISerializableComparator;
import java.util.Collection;
import java.util.Iterator;
import javax.servlet.jsp.JspException;
import org.apache.log4j.Category;

/**
 * A JSP bean that provides ordered lists of MemberViews. This class
 * is a thin wrapper around calls to MemberDBRead. In the future, it might
 * add further value by caching and sorting views on the client side.<p>
 *
 * FIXME: this class is basically a list, not a set. A set hides duplicates,
 * whereas a list does not. Rename this class.<p>
 *
 * FIXME: this class could be less tightly coupled to the GUI, by using
 * String properties lowerBound and upperBound. If both lowerBound and
 * upperBound are non-null, then select for lowerBound <= names < upperBound.
 * If lowerBound is null, then select for names < upperBound. If upperBound
 * is null, then select for lowerBound <= names. If both are null, throw an
 * IllegalStateException.<p>
 *
 * In the current implementation, a list is created on the fly whenever
 * a JSP is displayed, and the list is tossed after the page is written.
 * For lists of 60 or so items, a page will update in 2 - 3 seconds when
 * the app and web servers are lightly loaded. This is adequate performance,
 * since most lists will be restricted by name, and therefore will contain
 * roughly 40 (==300/8) items.<p>
 *
 * @version $Revision: 1.4 $ $Date: 2003/02/26 03:38:46 $
 * @author <a href="mailto:rphall@pluto.njcc.com">Rick Hall</a>
 */
public class MemberSet implements INameList {

  private final static String base = MemberSet.class.getName();
  private final static Category theLog = Category.getInstance( base );

  private final static Integer DEFAULT_GROUP_INDEX = new Integer(2);

  /** Holds the restriction flag */
  private Boolean restricted = new Boolean(true);

  /**
   * Holds the group index that specifies which names should be displayed.
   * By default, this group is <tt>ABC</tt> (index 2).
   */
  private Integer group = DEFAULT_GROUP_INDEX;

  /**
   * Returns a flag that indicates whether an iterator will be restricted
   * to a particular group of names. 
   */
  public Boolean getRestricted() {
    return this.restricted;
  }

  /**
   * Sets whether an iterator will be restricted to names within a
   * within a group. A null values does not change the current restriction.
   */
  public void setRestricted( Boolean restricted ) {
    if ( restricted != null ) {
      this.restricted = restricted;
    }
  }

  /** Returns the group used by restricted iterations */
  public Integer getGroup() {
    return group;
  }

  /** Sets the month used by restricted iterations */
  public void setGroup( Integer group ) {
    if ( group.intValue() < 2 || group.intValue() > 9 ) {
      throw new IllegalArgumentException( "bad group == " + group );
    }
    this.group = group;
  }

  /** Stubbed method that returns null */
  public ISerializableComparator getComparator() { return null; }

  /** Stubbed method that does nothing */
  public void setComparator( ISerializableComparator comparator ) { }

  /** Prepares a restricted iterator for the current group and year */
  public MemberSet() throws WebException {
  }

  /**
   * Prepares a restricted iterator for the specified group of the
   * current year
   */
  public MemberSet( Integer group ) throws WebException {
    setGroup( group );
  }

  /** Returns an iterator constructed to the current setting */
  public Iterator getIterator() throws WebException {

    Iterator retVal = null;
    try {

      Collection c = null;
      if ( restricted.booleanValue() ) {
        c = findMemberSnapshots( this.getGroup() );
      }
      else {
        c = MemberDBRead.findAllMembersByLastName();
      }

      final Iterator cIter = c.iterator();

      retVal = new Iterator() {
        public boolean hasNext() {
          return cIter.hasNext();
        }
        public Object next() {
          MemberSnapshot ms = (MemberSnapshot) cIter.next();
          return new MemberView( ms );
        }
        public void remove() {
          throw new UnsupportedOperationException( "remove not supported" );
        }
      }; // new Iterator()

    }
    catch( Exception x ) {
      String msg = ErrorUtils.createDbgMsg( "MemberSet.getIterator", x );
      theLog.error( msg, x );
      throw new WebException( msg );
    }

    return retVal;
  } // getIterator()

  /**
   * Returns a collection of member snapshots based on the current group.
   */
  Collection findMemberSnapshots( Integer group ) throws MemberException {

    // Preconditions
    if ( group == null || group.intValue() < 2 || group.intValue() > 9 ) {
      throw new IllegalArgumentException( "invalid group == " + group );
    }

    Collection retVal = null;

    switch( group.intValue() ) {
      case 2:
        retVal = MemberDBRead.findMembersWithLastNamesLT( "D" );
        break;
      case 9:
        retVal = MemberDBRead.findMembersWithLastNamesGTE( "W" );
        break;
      default:
        String lower = getLowerBound( group );
        String upper = getUpperBound( group );
        retVal = MemberDBRead.findMembersWithLastNamesInRange( lower, upper );
        break;
    } // switch group

    return retVal;
  } // findMemberSnapshots(Integer)

  /** Calculates the lower bound of names in a group */
  String getLowerBound( Integer group ) {

    String retVal = null;
    switch( group.intValue() ) {
      case 3: retVal = "D"; break;
      case 4: retVal = "G"; break;
      case 5: retVal = "J"; break;
      case 6: retVal = "M"; break;
      case 7: retVal = "P"; break;
      case 8: retVal = "T"; break;
      default:
        throw new IllegalArgumentException( "invalid group == " + group );
    } // switch

    return retVal;
  } // getLowerBound(Integer)

  /** Calculates the uppder bound of names in a group */
  String getUpperBound( Integer group ) {

    String retVal = null;
    switch( group.intValue() ) {
      case 3: retVal = "G"; break;
      case 4: retVal = "J"; break;
      case 5: retVal = "M"; break;
      case 6: retVal = "P"; break;
      case 7: retVal = "T"; break;
      case 8: retVal = "W"; break;
      default:
        throw new IllegalArgumentException( "invalid group == " + group );
    } // switch

    return retVal;
  } // getUpperBound(Integer)

  /**
   * Finds all active members of the CLRA. Members are sorted by
   * lastname-firstname (the natural comparator for members).<p>
   * @deprecated
   */
  public Iterator getAllActiveMembers()
    throws MemberException {

    final java.util.TreeSet data = new java.util.TreeSet();

    try {
      Iterator snapshots = MemberDBRead.findAllMembersByLastName().iterator();
      while ( snapshots.hasNext() ) {
        MemberView mv = new MemberView( (MemberSnapshot) snapshots.next() );
        data.add( mv );
      }
    }
    catch(Exception x) {
      String msg = x.getClass().getName() + ": " + x.getMessage();
      theLog.fatal( msg, x );
      throw new MemberException( msg );
    }

    return data.iterator();
  } // findAllActiveMembers()

} // MemberSet

/*
 * $Log: MemberSet.java,v $
 * Revision 1.4  2003/02/26 03:38:46  rphall
 * Added copyright and GPL license
 *
 * Revision 1.3  2002/02/24 21:15:10  rphall
 * Fixed bug #501041
 *
 * Revision 1.2  2002/02/18 18:06:26  rphall
 * Ran dos2unix to remove ^M (carriage return) from end of lines
 *
 * Revision 1.1.1.1  2002/01/03 21:57:28  rphall
 * Initial load, 5th try, Jan-03-2002 4:57 PM
 *
 * Revision 1.5  2002/01/01 18:46:49  rphall
 * Working: name selector
 *
 */

