/*
 * Copyright (c) Carnegie Lake Rowing Association 2002. All rights reserved.
 * Distributed under the GPL license. See doc/COPYING.
 * $RCSfile: BoatView.java,v $
 * $Date: 2003/02/26 03:38:45 $
 * $Revision: 1.3 $
 */

package com.clra.rowing;

import com.clra.util.DBConfiguration;
import com.clra.util.ValidationException;
import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * Read-only information about a boat.
 *
 * @version $Id: BoatView.java,v 1.3 2003/02/26 03:38:45 rphall Exp $
 * @author <a href="mailto:rphall@pluto.njcc.com">Rick Hall</a>
 */
public class BoatView implements Comparable, Serializable {

  private int id = 0;
  private String name = null;
  private int size = 0;
  private String type = null;

  private int hashCode = 0;

  public BoatView(int id, String name, int size, String type)
    throws ValidationException {

    // Assign blank finals
    this.id = id;
    this.name = name;
    this.size = size;
    this.type = type;

    this.hashCode = ( new Integer(this.getId()) ).hashCode();

    // Enforce preconditions
    /*
    if ( !isValidBoatId( id ) ) {
      throw new ValidationException( BOAT_ID );
    }
    if ( !isValidBoatName( name ) ) {
      throw new ValidationException( BOAT_ACCOUNT_NAME );
    }
    ...
    */

  } // ctor(..)

  public int getId() {
    return this.id;
  }

  public String getName() {
    return this.name;
  }

  public int getSize() {
    return this.size;
  }

  public String getType() {
    return this.type;
  }

  /** Two boats are equal iff their id's are equal. */
  public boolean equals( Object o ) {

    boolean retVal = false;
    if ( o instanceof BoatView ) {
      retVal = this.getId() == ((BoatView)o).getId();
    }

    return retVal;
  } // equals(Object)

  /** Boat objects are hashed by id's */
  public int hashCode() {
    return hashCode;
  }

  /**
   * Defines a natural ordering for boats by name.
   *
   * Note: this class has a natural ordering that is inconsistent with equals.
   * Equality is defined by boat id's, not by boat names.
   *
   * @param o A boat object.
   * @exception ClassCastException if o is not a boat object.
   */
  public int compareTo( Object o ) throws ClassCastException {
    // Precondition
    if ( !(o instanceof BoatView) ) {
      throw new ClassCastException( "not a boat object" );
    }

    BoatView other = (BoatView) o;
    int retVal;

    // Compare last names (which are never null)
    String thisName = this.getName();
    String otherName = other.getName();
    retVal = thisName.compareToIgnoreCase( otherName );

    return retVal;
  } // compareTo(Object)

} // BoatView

/*
 * $Log: BoatView.java,v $
 * Revision 1.3  2003/02/26 03:38:45  rphall
 * Added copyright and GPL license
 *
 * Revision 1.2  2002/02/18 18:03:44  rphall
 * Ran dos2unix to remove ^M (carriage return) from end of lines
 *
 * Revision 1.1.1.1  2002/01/03 21:57:28  rphall
 * Initial load, 5th try, Jan-03-2002 4:57 PM
 *
 * Revision 1.2  2001/11/28 11:53:32  rphall
 * Made Serializable
 *
 * Revision 1.1  2001/11/18 17:07:07  rphall
 * Checkpt before major revision of rowing package
 *
 */

