/*
 * Copyright (c) Carnegie Lake Rowing Association 2002. All rights reserved.
 * Distributed under the GPL license. See doc/COPYING.
 * $RCSfile: RowingSessionSnapshot.java,v $
 * $Date: 2003/02/26 03:38:45 $
 * $Revision: 1.3 $
 */

package com.clra.rowing;

import com.clra.util.ISerializableComparator;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.Date;
import javax.ejb.EJBObject;
import javax.ejb.RemoveException;

/**
 * A snapshot of the data held by rowing session. If more than one property
 * of a rowing session needs to be read or written, it may be more efficient
 * to read or write all properties, in order to minimize network transit
 * times.
 *
 * @version $Revision: 1.3 $ $Date: 2003/02/26 03:38:45 $
 * @author <a href="mailto:rphall@pluto.njcc.com">Rick Hall</a>
 */
public class RowingSessionSnapshot implements Comparable, Serializable {

  private Integer id;
  private Date date;
  private RowingSessionState state;
  private RowingSessionLevel level;
  private RowingSessionType type;
  private int hashCode;

  public RowingSessionSnapshot( Integer id, RowingSessionState st,
    Date dt, RowingSessionLevel lv, RowingSessionType tp ) {

    // Preconditions
    if ( id == null ) {
      throw new IllegalArgumentException( "null id" );
    }
    if ( st == null ) {
      throw new IllegalArgumentException( "null state" );
    }
    if ( dt == null ) {
      throw new IllegalArgumentException( "null date" );
    }
    if ( lv == null ) {
      throw new IllegalArgumentException( "null level" );
    }
    if ( tp == null ) {
      throw new IllegalArgumentException( "null type" );
    }

    this.id = id;
    this.state = st;
    this.date = dt;
    this.level = lv;
    this.type = tp;
    this.hashCode = id.hashCode();
  }

  /**
   * Returns the primary key of a rowing session. The id is immutable
   * after a rowing session is created.
   */
  public Integer getId() {
    return this.id;
  }

  /**
   * Returns the state of a rowing session. The state of a rowing
   * session can not be set directly. It is changed as a side-effect
   * of other operations on a rowing session.
   */
  public RowingSessionState getState() {
    return this.state;
  }

  /** Returns the date (and time) of a rowing session */
  public Date getDate() {
    return this.date;
  }

  /** Returns the level of a rowing session */
  public RowingSessionLevel getLevel() {
    return this.level;
  }

  /** Returns the type of a rowing session */
  public RowingSessionType getType() {
    return this.type;
  }

  public int compareTo( Object o ) {
    return DefaultRowingSessionComparator.staticCompare( this, o );
  }

  public int hashCode() {
    return this.hashCode;
  }

} // RowingSessionSnapshot

/*
 * $Log: RowingSessionSnapshot.java,v $
 * Revision 1.3  2003/02/26 03:38:45  rphall
 * Added copyright and GPL license
 *
 * Revision 1.2  2002/02/18 18:04:57  rphall
 * Ran dos2unix to remove ^M (carriage return) from end of lines
 *
 * Revision 1.1.1.1  2002/01/03 21:57:28  rphall
 * Initial load, 5th try, Jan-03-2002 4:57 PM
 *
 * Revision 1.1  2001/12/06 21:22:45  rphall
 * Read-only snapshot of rowing-session data
 *
 */

