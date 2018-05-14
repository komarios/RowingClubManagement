/*
 * Copyright (c) Carnegie Lake Rowing Association 2002. All rights reserved.
 * Distributed under the GPL license. See doc/COPYING.
 * $RCSfile: MemberView.java,v $
 * $Date: 2003/03/13 04:51:50 $
 * $Revision: 1.11 $
 */

package com.clra.web;

import com.clra.member.Address;
import com.clra.member.MemberException;
import com.clra.member.MemberSnapshot;
import com.clra.member.MemberName;
import com.clra.member.Telephone;
import com.clra.util.DBConfiguration;
import com.clra.util.INamed;
import com.clra.util.ValidationException;
import java.io.Serializable;
import java.util.Date;
import java.util.Map;
import java.util.Calendar;
import java.text.*;

/**
 * Read-only information about a member. This is a thin wrapper around
 * MemberSnapshot. The class exists so that some logic can be pulled
 * out of the memberlist.jsp screen.
 *
 * @version $Id: MemberView.java,v 1.11 2003/03/13 04:51:50 rphall Exp $
 * @author <a href="mailto:rphall@pluto.njcc.com">Rick Hall</a>
 */
public class MemberView implements INamed, Comparable, Serializable {

  private MemberNameFormat memberNameFormat = MemberNameFormat.FIRSTLAST;
  private MemberSnapshot snapshot = null;

  /** Produces an invalid MemberView. Used only during deserialization */
  public MemberView() {}

  public MemberView( MemberSnapshot snapshot ) {
    if ( snapshot == null ) {
      throw new IllegalArgumentException( "null snapshot" );
    }
    this.snapshot = snapshot;
  }

  /** Returns the format used by <tt>getName()</tt> */
  public MemberNameFormat getMemberNameFormat() {
    return this.memberNameFormat;
  }

  /** Sets the format used by <tt>getName()</tt> */
  public void setMemberNameFormat( MemberNameFormat memberNameFormat ) {
    if ( memberNameFormat == null ) {
      throw new IllegalArgumentException( "null member-name format" );
    }
    this.memberNameFormat = memberNameFormat;
  }

  /**
   * Returns a String presentation of a member's name, formatted
   * according to <tt>getMemberNameFormat()</tt>.
   */
  public String getName() {
    return this.getMemberNameFormat().format(this.snapshot.getMemberName());
  }

  public Integer getId() {
    return this.snapshot.getId();
  }

  public String getAccountName() {
    return this.snapshot.getAccountName();
  }

  public String getAccountPassword() {
    return this.snapshot.getAccountPassword();
  }

  public String getAccountTypeStr() {
    return this.snapshot.getAccountType().toString();
  }

  public MemberName getMemberName() {
    return this.snapshot.getMemberName();
  }

  public boolean hasEmail() {
    return this.snapshot.getEmail() != null;
  }

  public String getEmail() {
    return hasEmail() ? this.snapshot.getEmail().toString() : "" ;
  }

  public Map getTelephoneNumbers() {
    return this.snapshot.getTelephoneNumbers();
  }

  public String getEveningPhoneStr() {
    Map phones = getTelephoneNumbers();
    Telephone phone = (Telephone) phones.get( Telephone.EVENING );
    String retVal = "";
    return phoneToString(phone);
  }

  public String getDayPhoneStr() {
    Map phones = getTelephoneNumbers();
    Telephone phone = (Telephone) phones.get( Telephone.DAY );
    return phoneToString(phone);
  }

  public String getOtherPhoneStr() {
    Map phones = getTelephoneNumbers();
    Telephone phone = (Telephone) phones.get( Telephone.OTHER );
    return phoneToString(phone);
  }

  private static String phoneToString( Telephone phone ) {
    String retVal = "";
    if ( phone != null ) {
      StringBuffer sb = new StringBuffer(25);
      sb.append( phone.getAreaCode() );
      sb.append( "&nbsp;" );
      sb.append( phone.getExchange() );
      sb.append( "-" );
      sb.append( phone.getLocal() );
      if ( phone.getExtension() != null
        && phone.getExtension().trim().length() > 0 ) {
        sb.append( " " );
        sb.append( "<em>x</em>&nbsp;" );
        sb.append( phone.getExtension().trim() );
      }
      retVal = new String( sb );
    }
    return retVal;
  }

  public Address getAddress() {
    return this.snapshot.getAddress();
  }

  public Date getAccountYear() {
    return this.snapshot.getAccountDate();
  }

  public int getAccountYear4() {
    return 1900 + this.snapshot.getAccountDate().getYear();
  }

  public boolean hasKnownBirthDate() {
    return this.snapshot.getBirthDate() != null;
  }

  public Date getBirthDate() {
    return this.snapshot.getBirthDate();
  }

  public boolean hasNoRoles() {
    return this.snapshot.getMemberRoles().length == 0;
  }

  public boolean hasRole(String role) {
	  return this.snapshot.hasRole(role);
  }

  /** Two members are equal iff their id's are equal. */
  public boolean equals( Object o ) {

    boolean retVal = false;
    if ( o instanceof MemberView ) {
      retVal = this.snapshot.equals( ((MemberView) o).snapshot );
    }

    return retVal;
  } // equals(Object)

  /** Member objects are hashed by id's */
  public int hashCode() {
    return this.snapshot.hashCode();
  }

  /**
   * Defines a natural ordering for members by lastname, firstname, middlename
   * and suffix.<p>
   *
   * Note: this class has a natural ordering that is inconsistent with equals.
   * Equality is defined by member id's, not by member names.
   *
   * @param o A member object.
   * @exception ClassCastException if o is not a member object.
   */
  public int compareTo( Object o ) throws ClassCastException {
    // Precondition
    if ( !(o instanceof MemberView) ) {
      throw new ClassCastException( "not a member object" );
    }

    MemberView other = (MemberView) o;
    int retVal = this.snapshot.compareTo( other.snapshot );

    return retVal;
  } // compareTo(Object)

} // MemberView

/*
 * $Log: MemberView.java,v $
 * Revision 1.11  2003/03/13 04:51:50  rphall
 * Simplified phone format
 *
 * Revision 1.10  2003/02/26 03:38:46  rphall
 * Added copyright and GPL license
 *
 * Revision 1.9  2003/02/22 20:47:57  rphall
 * Added various (hokey) getXxxPhoneStr()
 *
 * Revision 1.8  2003/02/21 19:21:01  rphall
 * Fixed bug in getEmail()
 *
 * Revision 1.7  2003/02/20 16:32:30  rphall
 * Add hasNoRoles() method
 *
 * Revision 1.6  2003/02/20 04:53:24  rphall
 * Removed gratuitous use of CLRA acronym
 *
 * Revision 1.5  2003/02/16 00:49:51  rphall
 * Removed obsolete comments
 *
 * Revision 1.4  2002/05/31 14:06:04  jmstone
 * Additions for handling dates
 *
 * Revision 1.3  2002/03/15 13:19:08  timguinther
 * update to display (or not) rowing admin link based on member role
 *
 * Revision 1.2  2002/02/18 18:06:33  rphall
 * Ran dos2unix to remove ^M (carriage return) from end of lines
 *
 * Revision 1.1.1.1  2002/01/03 21:57:28  rphall
 * Initial load, 5th try, Jan-03-2002 4:57 PM
 *
 * Revision 1.4  2002/01/01 03:40:33  rphall
 * Moved getName() from MemberName to MemberView
 *
 * Revision 1.3  2001/12/31 14:32:58  rphall
 * Renamed 'member.Name' to 'member.MemberName'
 *
 * Revision 1.2  2001/12/12 04:10:19  rphall
 * Fixed build problems
 *
 * Revision 1.1  2001/12/11 23:46:33  rphall
 * Moved from rowing/member package to web package
 *
 * Revision 1.4  2001/11/28 11:55:15  rphall
 * Made Serializable
 *
 * Revision 1.3  2001/11/18 17:04:06  rphall
 * Import of DBConfiguration and ValidationException
 *
 * Revision 1.2  2001/11/10 19:09:04  rphall
 * Fixed compareTo(String)
 *
 * Revision 1.1  2001/11/10 16:17:34  rphall
 * First compilable version
 *
 */

