/*
 * Copyright (c) Carnegie Lake Rowing Association 2002. All rights reserved.
 * Distributed under the GPL license. See doc/COPYING.
 * $RCSfile: Address.java,v $
 * $Date: 2003/02/26 03:38:45 $
 * $Revision: 1.7 $
 */

package com.clra.member;

import java.io.Serializable;

/**
 * Encapsulates address information about a member.
 * @version $Id: Address.java,v 1.7 2003/02/26 03:38:45 rphall Exp $
 * @author <a href="mailto:rphall@pluto.njcc.com">Rick Hall</a>
 */
public class Address implements Serializable {

  private String street1 = null;
  private String street2 = null;
  private String city = null;
  private String state = null;
  private String zip = null;

  /** Produces an invalid Address object. Used only during deserialization. */
  public Address() {}

  public Address( String street1, String street2,
      String city, String state, String zip ) {
    // Assign blank finals
    this.street1 = street1 == null ? null : street1.trim() ;
    this.street2 = street2 == null ? null : street2.trim() ;
    this.city = city == null ? null : city.trim() ;
    this.state = state == null ? null : state.trim() ;
    this.zip = zip == null ? null : zip.trim() ;

    // Enforce preconditions
    if ( street1 == null || this.street1.length() == 0 ) {
      throw new IllegalArgumentException( "invalid street1" );
    }
    if ( city == null || this.city.length() == 0 ) {
      throw new IllegalArgumentException( "invalid city" );
    }
    if ( state == null || this.state.length() == 0 ) {
      throw new IllegalArgumentException( "invalid state" );
    }
    if ( zip == null || this.zip.length() == 0 ) {
      throw new IllegalArgumentException( "invalid zip" );
    }

  } // ctor(..)

  public String getStreet1() { return this.street1; }

  public String getStreet2() { return this.street2; }

  public String getCity()    { return this.city;    }

  public String getState()   { return this.state;   }

  public String getZip()     { return this.zip;     }

  public boolean equals( Object o ) {

    boolean retVal;
    if ( o == null ) {
      retVal = false;
    }
    else if ( o instanceof Address ) {
      Address that = (Address) o;
      retVal = this.street1.equals(that.street1);
      if ( retVal ) {
        if ( this.street2 != null ) {
          retVal = this.street2.equals(that.street2);
        } else {
          retVal = that.street2 == null;
        }
      }
      retVal = retVal && this.city.equals(that.city);
      retVal = retVal && this.state.equals(that.state);
      retVal = retVal && this.zip.equals(that.zip);
    }
    else {
      retVal = false;
    }

    return retVal;
  } // equals(Object)

  public int hashCode() {
    int retVal = this.street1.hashCode(); // good enough
    return retVal;
  }

} // Address

/*
 * $Log: Address.java,v $
 * Revision 1.7  2003/02/26 03:38:45  rphall
 * Added copyright and GPL license
 *
 * Revision 1.6  2003/02/19 22:23:01  rphall
 * Removed gratuitous use of CLRA acronym
 *
 * Revision 1.5  2003/02/18 04:16:41  rphall
 * Removed ValidationException
 *
 * Revision 1.4  2003/02/16 00:38:44  rphall
 * Fixed bug in 'equals' method
 *
 * Revision 1.3  2003/02/15 04:31:41  rphall
 * Changes connected to major revision of MemberBean
 *
 */

