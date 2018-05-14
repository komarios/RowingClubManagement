/*
 * Copyright (c) Carnegie Lake Rowing Association 2002. All rights reserved.
 * Distributed under the GPL license. See doc/COPYING.
 * $RCSfile: MemberName.java,v $
 * $Date: 2003/02/26 03:38:45 $
 * $Revision: 1.6 $
 */

package com.clra.member;

import java.io.Serializable;

/**
 * Encapsulates information about the name of a member.
 * @version $Revision: 1.6 $ $Date: 2003/02/26 03:38:45 $
 * @author <a href="mailto:rphall@pluto.njcc.com">Rick Hall</a>
 */
public class MemberName implements Serializable {

  private String first = null;
  private String middle = null;
  private String last = null;
  private String suffix = null;

  /**
   * Produces an invalid MemberName instance.
   * Used only during deserialization.
   */
  public MemberName() {}

  /**
   * @param first the non-null, non-blank first name of a member.
   * @param last the non-null, non-blank last name of a member.
   */
  public MemberName( String first, String last ) {
    this(first,null,last,null);
  }

  /**
   * @param first the non-null, non-blank first name of a member.
   * @param middle the middle name of a member, possibly blank or null.
   * @param last the non-null, non-blank last name of a member.
   */
  public MemberName( String first, String middle, String last ) {
    this(first,middle,last,null);
  }

  /**
   * @param first the non-null, non-blank first name of a member.
   * @param middle the middle name of a member, possibly blank or null.
   * @param last the non-null, non-blank last name of a member.
   * @param suffix the suffix of a member's name, possibly blank or null.
   */
  public MemberName(String first, String middle, String last, String suffix) {
    // Preconditions
    if ( first == null || first.trim().length() == 0 ) {
      throw new IllegalArgumentException( "invalid first name" );
    }
    if ( last == null || last.trim().length() == 0 ) {
      throw new IllegalArgumentException( "invalid last name" );
    }

    this.first = first.trim();
    this.last = last.trim();

    if ( middle == null || middle.trim().length() == 0 ) {
      this.middle = null;
    }
    else {
      this.middle = middle.trim();
    }

    if ( suffix == null || suffix.trim().length() == 0 ) {
      this.suffix = null;
    }
    else {
      this.suffix = suffix.trim();
    }

  } // ctor(String,String,String,String)

  public String getFirstName() {
    return this.first;
  }

  public void setFirstName( String first ) {
    if ( first == null || first.trim().length() == 0 ) {
      throw new IllegalArgumentException( "invalid first name" );
    }
    this.first = first.trim();
  }

  public boolean hasMiddleName() {
    return this.middle != null;
  }

  public String getMiddleName() {
    return this.middle;
  }

  public void setMiddleName( String middle ) {
    if ( middle == null || middle.trim().length() == 0 ) {
      this.middle = null;
    }
    else {
      this.middle = middle.trim();
    }
  }

  public String getLastName() {
    return this.last;
  }

  public void setLastName( String last ) {
    if ( last == null || last.trim().length() == 0 ) {
      throw new IllegalArgumentException( "invalid last name" );
    }
    this.last = last.trim();
  }

  public boolean hasSuffix() {
    return this.suffix != null;
  }

  public String getSuffix() {
    return this.suffix;
  }

  public void setSuffix( String suffix ) {
    if ( suffix == null || suffix.trim().length() == 0 ) {
      this.middle = null;
    }
    else {
      this.suffix = suffix.trim();
    }
  }

  public boolean equals( Object o ) {

    boolean retVal = false;
    if ( o instanceof MemberName ) {
      MemberName that = (MemberName) o;
      retVal = this.getLastName().equalsIgnoreCase( that.getLastName() );
      retVal = retVal &&
                this.getFirstName().equalsIgnoreCase( that.getFirstName() );
      if ( retVal && !this.hasMiddleName() && that.hasMiddleName() ) {
        retVal = false;
      }
      else if ( retVal && this.hasMiddleName() && !that.hasMiddleName() ) {
        retVal = false;
      }
      else if ( retVal && !this.hasMiddleName() && !that.hasMiddleName() ) {
        // no change
      }
      else if ( retVal && this.hasMiddleName() && that.hasMiddleName() ) {
        retVal = this.getMiddleName().equalsIgnoreCase( that.getMiddleName() );
      }
      if ( retVal && !this.hasSuffix() && that.hasSuffix() ) {
        retVal = false;
      }
      else if ( retVal && this.hasSuffix() && !that.hasSuffix() ) {
        retVal = false;
      }
      else if ( retVal && !this.hasSuffix() && !that.hasSuffix() ) {
        // no change
      }
      else if ( retVal && this.hasSuffix() && that.hasSuffix() ) {
        retVal = this.getSuffix().equalsIgnoreCase( that.getSuffix() );
      }
    }

    return retVal;
  } // equals(Object)

  public int hashCode() {
    int retVal = this.getFirstName().hashCode() + this.getLastName().hashCode();
    return retVal;
  }

  public String toString() {

    StringBuffer sb = new StringBuffer();
    sb.append( this.getLastName() );
    sb.append( ", " );
    sb.append( this.getFirstName() );
    if ( this.hasMiddleName() ) {
    sb.append( " " );
      sb.append( this.getMiddleName() );
    }
    if ( this.hasSuffix() ) {
      sb.append( " " );
      sb.append( this.getSuffix() );
    }
    String retVal = new String(sb);

    return retVal;
  } // toString()

} // MemberName

/*
 * $Log: MemberName.java,v $
 * Revision 1.6  2003/02/26 03:38:45  rphall
 * Added copyright and GPL license
 *
 * Revision 1.5  2003/02/19 22:26:38  rphall
 * Removed gratuitous use of CLRA acronym
 *
 * Revision 1.4  2003/02/16 00:41:44  rphall
 * Added 'equals', 'hashCode' and 'toString' methods
 *
 */

