/*
 * Copyright (c) Carnegie Lake Rowing Association 2002. All rights reserved.
 * Distributed under the GPL license. See doc/COPYING.
 * $RCSfile: BoatSet.java,v $
 * $Date: 2003/02/26 03:38:46 $
 * $Revision: 1.4 $
 */

package com.clra.web;

import com.clra.rowing.BoatView;
import com.clra.util.DBConfiguration;
import com.clra.util.ValidationException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.SortedSet;
import java.util.TreeSet;
import org.apache.log4j.Category;
import org.apache.log4j.helpers.Loader;

/**
 * A collection of "finder" methods that return read-only, sorted sets
 * of boats.<p>
 * FIXME: when this class changes to a "Session-like" design, make sure
 * it becomes Serializable.
 *
 * @version $Id: BoatSet.java,v 1.4 2003/02/26 03:38:46 rphall Exp $
 * @author <a href="mailto:rphall@pluto.njcc.com">Rick Hall</a>
 */
public class BoatSet implements SortedSet {

  private final static String base = BoatSet.class.getName();
  private final static Category theLog = Category.getInstance( base );

  /**
   * The current implementations return data in an in-memory collections.
   * Since the underlaying Boat database table may already be
   * cached in memory, this can lead to duplicate caching. Future
   * implementations should avoid in-memory duplicates by implementing
   * SortedSet and Iterator methods directly in SQL.<p>
   */
  private final SortedSet data = new TreeSet();

  /**
   * Finds all active boats of the CLRA. Boats are sorted by name
   * (the natural comparator for boats).
   */
  public static BoatSet findAllActiveBoats()
    throws WebException {

    Connection conn = null;
    PreparedStatement stmt = null;
    BoatSet retVal = new BoatSet();
    try {
      conn = DBConfiguration.getConnection();
      stmt = conn.prepareStatement(
          Configuration.SQL_BOAT_01,
          ResultSet.TYPE_FORWARD_ONLY,
          ResultSet.CONCUR_READ_ONLY);
      loadBoatSet( stmt, retVal.data );
    }
    catch(SQLException x) {
      String msg = "SQLException: " + x.getMessage();
      theLog.fatal( msg, x );
      throw new WebException( msg );
    }
    finally {
      DBConfiguration.closeSQLStatement( stmt );
      DBConfiguration.closeSQLConnection( conn );
    }
    conn = null;
    stmt = null;

    return retVal;
  } // findAllActiveBoats()

  /**
   * Finds the boat that has the given id. This "finder" returns at most
   * one boat.
   */
  public static BoatView findBoatById( int id )
    throws WebException {

    Connection conn = null;
    PreparedStatement stmt = null;
    BoatView retVal = null;
    try {
      conn = DBConfiguration.getConnection();
      stmt = conn.prepareStatement(
          Configuration.SQL_BOAT_02,
          ResultSet.TYPE_FORWARD_ONLY,
          ResultSet.CONCUR_READ_ONLY);
      stmt.setInt( 1, id );
      retVal = loadBoat( stmt );
    }
    catch(SQLException x) {
      String msg = "SQLException for id == '" + id + "': "
          + x.getMessage();
      theLog.fatal( msg, x );
      throw new WebException( msg );
    }
    finally {
      DBConfiguration.closeSQLStatement( stmt );
      DBConfiguration.closeSQLConnection( conn );
    }
    conn = null;
    stmt = null;

    return retVal;
  } // findBoatById(int)

  /**
   * Finds the boat that has the given name. Names
   * are unique, so this "finder" returns at most one boat.
   */
  public static BoatView findBoatByName( String name )
    throws WebException {

    Connection conn = null;
    PreparedStatement stmt = null;
    BoatView retVal = null;
    try {
      conn = DBConfiguration.getConnection();
      stmt = conn.prepareStatement(
          Configuration.SQL_BOAT_03,
          ResultSet.TYPE_FORWARD_ONLY,
          ResultSet.CONCUR_READ_ONLY);
      stmt.setString( 1, name );
      retVal = loadBoat( stmt );
    }
    catch(SQLException x) {
      String msg = "SQLException for name == '" + name + "': "
          + x.getMessage();
      theLog.fatal( msg, x );
      throw new WebException( msg );
    }
    finally {
      DBConfiguration.closeSQLStatement( stmt );
      DBConfiguration.closeSQLConnection( conn );
    }
    conn = null;
    stmt = null;

    return retVal;
  } // findBoatByName(String)

  /** BoatSet instances are created by static "finder" methods */
  private BoatSet() {}

  /**
   * Returns the comparator associated with this sorted set, or
   * <tt>null</tt> if it uses its elements' natural ordering.
   *
   * @return the comparator associated with this sorted set, or
   * 	       <tt>null</tt> if it uses its elements' natural ordering.
   */
  public Comparator comparator() {
    return this.data.comparator();
  }

  /**
   * Returns a view of the portion of this sorted set whose elements range
   * from <tt>fromElement</tt>, inclusive, to <tt>toElement</tt>, exclusive.
   *
   * @param fromElement low endpoint (inclusive) of the subSet.
   * @param toElement high endpoint (exclusive) of the subSet.
   * @return a view of the specified range within this sorted set.
   * 
   * @throws ClassCastException if <tt>fromElement</tt> and
   *         <tt>toElement</tt> are not <tt>Boat</tt> objects.
   * @throws IllegalArgumentException if <tt>fromElement</tt> is greater than
   *         <tt>toElement</tt>; or if this set is itself a subSet, headSet,
   *         or tailSet, and <tt>fromElement</tt> or <tt>toElement</tt> are
   *         not within the specified range of the subSet, headSet, or
   *         tailSet.
   * @throws NullPointerException if <tt>fromElement</tt> or
   *	       <tt>toElement</tt> is <tt>null</tt>
   */
  public SortedSet subSet(Object fromElement, Object toElement) {
    return this.data.subSet(fromElement,toElement);
  }

  /**
   * Returns a view of the portion of this sorted set whose elements are
   * strictly less than <tt>toElement</tt>.
   *
   * @param toElement high endpoint (exclusive) of the headSet.
   * @return a view of the specified initial range of this sorted set.
   * @throws ClassCastException if <tt>toElement</tt> is not compatible
   *         with this set's comparator (or, if the set has no comparator,
   *         if <tt>toElement</tt> does not implement <tt>Comparable</tt>).
   * @throws NullPointerException if <tt>toElement</tt> is <tt>null</tt>
   * @throws IllegalArgumentException if this set is itself a subSet,
   *         headSet, or tailSet, and <tt>toElement</tt> is not within the
   *         specified range of the subSet, headSet, or tailSet.
   */
  public SortedSet headSet(Object toElement) {
    return this.data.headSet(toElement);
  }

  /**
   * Returns a view of the portion of this sorted set whose elements are
   * greater than or equal to <tt>fromElement</tt>.
   *
   * @param fromElement low endpoint (inclusive) of the tailSet.
   * @return a view of the specified final range of this sorted set.
   * @throws ClassCastException if <tt>fromElement</tt> is not compatible
   *         with this set's comparator (or, if the set has no comparator,
   *         if <tt>fromElement</tt> does not implement <tt>Comparable</tt>).
   * @throws NullPointerException if <tt>fromElement</tt> is <tt>null</tt>
   * @throws IllegalArgumentException if this set is itself a subSet,
   *         headSet, or tailSet, and <tt>fromElement</tt> is not within the
   *         specified range of the subSet, headSet, or tailSet.
   */
  public SortedSet tailSet(Object fromElement) {
    return this.data.tailSet(fromElement);
  }

  /**
   * Returns the first (lowest) element currently in this sorted set.
   *
   * @return the first (lowest) element currently in this sorted set.
   * @throws    NoSuchElementException sorted set is empty.
   */
  public Object first() {
    return this.data.first();
  }

  /**
   * Returns the last (highest) element currently in this sorted set.
   *
   * @return the last (highest) element currently in this sorted set.
   * @throws    NoSuchElementException sorted set is empty.
   */
  public Object last() {
    return this.data.last();
  }

  /**
   * Returns the number of elements in this set (its cardinality).  If this
   * set contains more than <tt>Integer.MAX_VALUE</tt> elements, returns
   * <tt>Integer.MAX_VALUE</tt>.
   *
   * @return the number of elements in this set (its cardinality).
   */
  public int size() {
    return this.data.size();
  }

  /**
   * Returns <tt>true</tt> if this set contains no elements.
   *
   * @return <tt>true</tt> if this set contains no elements.
   */
  public boolean isEmpty() {
    return this.data.isEmpty();
  }

  /**
   * Returns <tt>true</tt> if this set contains the specified element.
   *
   * @param o element whose presence in this set is to be tested.
   * @return <tt>true</tt> if this set contains the specified element.
   */
  public boolean contains(Object o) {
   /*
   * formally, returns <tt>true</tt> if and only if this set contains an
   * element <code>e</code> such that <code>(o==null ? e==null :
   * o.equals(e))</code>.
   */
    return this.data.contains(o);
  }

  /**
   * Returns an iterator over the (sorted) elements in this set.
   *
   * @return an iterator over the elements in this set.
   */
  public Iterator iterator() {
    return this.data.iterator();
  }

  /**
   * Returns an array containing all of the elements in this set.
   *
   * @return an array containing all of the elements in this set.
   */
  public Object[] toArray() {
    return this.data.toArray();
  }

  /**
   * Returns an array containing all of the elements in this set whose
   * runtime type is that of the specified array.
   *
   * @param a the array into which the elements of this set are to
   *		be stored, if it is big enough; otherwise, a new array of the
   * 		same runtime type is allocated for this purpose.
   * @return an array containing the elements of this set.
   * @throws    ArrayStoreException the runtime type of a is not a supertype
   * of the runtime type of Boat.
   */
  public Object[] toArray(Object a[]) {
    return this.data.toArray(a);
  }

  // Bulk Operations

  /**
   * Returns <tt>true</tt> if this set contains all of the elements of the
   * specified collection.
   *
   * @param c collection to be checked for containment in this set.
   * @return <tt>true</tt> if this set contains all of the elements of the
   * 	       specified collection.
   */
  public boolean containsAll(Collection c) {
    return this.data.containsAll(c);
  }

  // Comparison and hashing

  /**
   * Compares the specified object with this set for equality.
   *
   * @param o Object to be compared for equality with this set.
   * @return <tt>true</tt> if the specified Object is equal to this set.
   */
  public boolean equals(Object o) {
    return this.data.equals(o);
  }

  /**
   * Returns the hash code value for this set.  The hash code of a set is
   * defined to be the sum of the hash codes of the elements in the set.
   *
   * @return the hash code value for this set.
   */
  public int hashCode() {
    return this.data.hashCode();
  }

  // Modification Operations are not supported

  /**
   * @throws UnsupportedOperationException since the <tt>add</tt> method is
   *           not supported by this set.
   */
  public boolean add(Object o) {
    throw new UnsupportedOperationException( "not supported" );
  }

  /**
   * @throws UnsupportedOperationException since the <tt>remove</tt> method is
   *         not supported by this set.
   */
  public boolean remove(Object o) {
    throw new UnsupportedOperationException( "not supported" );
  }

  /**
   * @throws UnsupportedOperationException since the <tt>addAll</tt> method is
   * 		  not supported by this set.
   */
  public boolean addAll(Collection c) {
    throw new UnsupportedOperationException( "not supported" );
  }

  /**
   * @throws UnsupportedOperationException since the <tt>retainAll</tt> method
   * 		  is not supported by this Collection.
   */
  public boolean retainAll(Collection c) {
    throw new UnsupportedOperationException( "not supported" );
  }

  /**
   * @throws UnsupportedOperationException since the <tt>removeAll</tt>
   * 		  method is not supported by this Collection.
  */
  public boolean removeAll(Collection c) {
    throw new UnsupportedOperationException( "not supported" );
  }

  /**
   * @throws UnsupportedOperationException since the <tt>clear</tt> method
   * 		  is not supported by this set.
   */
  public void clear() {
    throw new UnsupportedOperationException( "not supported" );
  }

  // UTILITIES

  /** Loads an in-memory object from a database using the specified SQL */
  private static BoatView loadBoat( PreparedStatement stmt )
    throws SQLException, WebException {

    // Initialize result set and return value to facilitate error recovery
    ResultSet rs = null;
    BoatView retVal = null;

    try {
      rs = stmt.executeQuery();
      if ( rs.next() ) {
        retVal = mapRowToBoat(rs);
      } // if
    }
    finally {
      if (rs != null) {
        try {
          rs.close();
          rs = null;
        }
        catch(Exception x) {
          theLog.error(x.getMessage(),x);
        }
      }
    } // finally

    return retVal;
  } // loadBoat(PreparedStatement)

  /** Loads an in-memory set from a database using the specified SQL */
  private static void loadBoatSet( PreparedStatement stmt, SortedSet set )
    throws SQLException, WebException {

    // Initialize result set to facilitate error recovery
    ResultSet rs = null;
    int rowIdx = -1;

    try {
      rs = stmt.executeQuery();
      while ( rs.next() ) {
        ++rowIdx;
        BoatView boat = mapRowToBoat(rs);
        set.add( boat );
      } // while
    }
    finally {
      if (rs != null) {
        try {
          rs.close();
          rs = null;
        }
        catch(Exception x) {
          theLog.error(x.getMessage(),x);
        }
      }
    } // finally
  } // loadBoatSet(PreparedStatement,SortedSet)

  /** Maps a row of SQL ResultSet to a BoatView object */
  private static BoatView mapRowToBoat( ResultSet rs )
    throws SQLException, WebException {

    final int boat_id             = rs.getInt(    "boat_id"   );
    final String boat_name        = rs.getString( "boat_name" );
    final int boat_size           = rs.getInt(    "boat_size" );
    final String boat_type        = rs.getString( "boat_type" );

    if ( theLog.isDebugEnabled() ) {
      theLog.debug( boat_id + ", " + boat_name );
    }

    BoatView retVal = null;
    try {
      retVal = new BoatView( boat_id, boat_name, boat_size, boat_type );
    }
    catch( ValidationException x ) {
      String msg = "Problem with data for boat == '" + boat_id + "', '"
          + boat_name + "': " + x.getMessage();
      theLog.fatal( msg, x );
      throw new WebException( msg );
    }

    return retVal;
  } // mapRowToBoat(ResultSet)

} // BoatSet

/*
 * $Log: BoatSet.java,v $
 * Revision 1.4  2003/02/26 03:38:46  rphall
 * Added copyright and GPL license
 *
 * Revision 1.3  2003/02/19 22:30:31  rphall
 * Removed gratuitous use of CLRA acronym
 *
 * Revision 1.2  2002/02/18 18:05:37  rphall
 * Ran dos2unix to remove ^M (carriage return) from end of lines
 */

