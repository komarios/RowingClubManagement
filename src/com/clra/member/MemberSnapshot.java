/*
 * Copyright (c) Carnegie Lake Rowing Association 2002. All rights reserved.
 * Distributed under the GPL license. See doc/COPYING.
 * $RCSfile: MemberSnapshot.java,v $
 * $Date: 2003/02/26 03:38:45 $
 * $Revision: 1.8 $
 */

package com.clra.member;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Read-only information about a member.
 *
 * @version $Revision: 1.8 $ $Date: 2003/02/26 03:38:45 $
 * @author <a href="mailto:rphall@pluto.njcc.com">Rick Hall</a>
 */
public class MemberSnapshot implements Comparable, Serializable {

  private Integer id = null;
  private String accountName = null;
  private String accountPassword = null;
  private AccountType accountType = null;
  private MemberName memberName = null;
  private Email email = null;
  private Map telephoneNumbers = null;
  private Address address = null;
  private Date accountDate = null;
  private Date birthDate = null;
  private int hashCode = 0;
  private Set memberRoles = null;

  /** Produces an invalid MemberSnapshot. Used only during deserialization */
  public MemberSnapshot() {}

  /** Used only when loading from the database */
  MemberSnapshot( Integer id, String accountName, String accountPassword,
    AccountType accountType, MemberName memberName, Email email,
    Map telephoneNumbers, Address address, Date accountDate, Date birthDate) {

    this( id, accountName, accountPassword, accountType, memberName, email,
    telephoneNumbers, address, accountDate, birthDate, new MemberRole[0] );

  }

  /** Used only when loading from the database */
  void setMemberRoles( MemberRole[] roles ) {
    if ( roles == null ) {
      throw new IllegalArgumentException( "null member roles" );
    }
    this.memberRoles = new HashSet();
    for ( int i=0; i<roles.length; i++ ) {
      if ( roles[i] == null ) {
        throw new IllegalArgumentException( "null member role" );
      }
      this.memberRoles.add( roles[i] );
    }
    return;
  } // setMemberRoles(MemberRole[])

  public MemberSnapshot( Integer id, String accountName, String accountPassword,
    AccountType accountType, MemberName memberName, Email email,
    Map telephoneNumbers, Address address, Date accountDate, Date birthDate,
    MemberRole[] roles ) {

    // Preconditions
    if ( accountName == null || accountName.trim().length() == 0 ) {
      throw new IllegalArgumentException( "invalid account name" );
    }
    if ( accountPassword == null || accountPassword.trim().length() == 0 ) {
      throw new IllegalArgumentException( "invalid account password" );
    }
    if ( accountType == null ) {
      throw new IllegalArgumentException( "null account type" );
    }
    if ( telephoneNumbers == null || telephoneNumbers.isEmpty() ) {
      throw new IllegalArgumentException( "invalid telephone numbers" );
    }
    else {
      Object o = telephoneNumbers.get( Telephone.EVENING );
      if ( !(o instanceof Telephone) ) {
        throw new IllegalArgumentException( "no evening telephone number" );
      }
    }

    this.id = id;
    this.accountName = accountName.trim();
    this.accountPassword = accountPassword.trim();
    this.accountType = accountType;
    this.memberName = memberName;
    this.email = email;
    this.telephoneNumbers = telephoneNumbers;
    this.address = address;
    this.accountDate = accountDate;
    this.birthDate = birthDate;

    setMemberRoles( roles );

    this.hashCode = this.getId().hashCode();

  } // ctor(..)

  public Integer getId() {
    return this.id;
  }

  public String getAccountName() {
    return this.accountName;
  }

  public String getAccountPassword() {
    return this.accountPassword;
  }

  public AccountType getAccountType() {
    return this.accountType;
  }

  public MemberName getMemberName() {
    return this.memberName;
  }

  public boolean hasEmail() {
    return this.email != null;
  }

  public Email getEmail() {
    return this.email;
  }

  public Map getTelephoneNumbers() {
    return this.telephoneNumbers;
  }

  public Address getAddress() {
    return this.address;
  }

  public Date getAccountDate() {
    return this.accountDate;
  }

  public boolean hasKnownBirthDate() {
    return this.birthDate != null;
  }

  public Date getBirthDate() {
    return this.birthDate;
  }

  public MemberRole[] getMemberRoles() {
    return (MemberRole[]) this.memberRoles.toArray( new MemberRole[0] );
  }

  public boolean hasRole(String role) {

    boolean retVal = false;
    if ( role != null ) {
	    Iterator iter = memberRoles.iterator();
      while ( !retVal && iter.hasNext() ) {
        String strRole = ((MemberRole)iter.next()).toString();
        if (strRole.equalsIgnoreCase(role)) {
          retVal = true;
        }
      } // while
    } // if role

	  return retVal;
  } // hasRole(String)

  /** Two members are equal iff their id's are equal. */
  public boolean equals( Object o ) {

    boolean retVal = false;
    if ( o instanceof MemberSnapshot ) {
      MemberSnapshot other = (MemberSnapshot)o;
      retVal = this.getId().equals(other.getId());
    }

    return retVal;
  } // equals(Object)

  /** Member objects are hashed by id's */
  public int hashCode() {
    return hashCode;
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
    if ( !(o instanceof MemberSnapshot) ) {
      throw new ClassCastException( "not a member object" );
    }

    MemberSnapshot other = (MemberSnapshot) o;
    int retVal;

    // Compare last names (which are never null)
    String thisName = this.getMemberName().getLastName();
    String otherName = other.getMemberName().getLastName();
    retVal = thisName.compareToIgnoreCase( otherName );

    // Compare first names (which are never null)
    if ( retVal == 0 ) {
      thisName = this.getMemberName().getFirstName();
      otherName = other.getMemberName().getFirstName();
      retVal = thisName.compareToIgnoreCase( otherName );
    }

    // Compare middle names (which may be null)
    thisName = this.getMemberName().getMiddleName();
    otherName = other.getMemberName().getMiddleName();
    if ( retVal == 0 && thisName != null && otherName != null ) {
      retVal = thisName.compareToIgnoreCase( otherName );
    }
    else if ( retVal == 0 && thisName == null && otherName != null ) {
      retVal = -1;
    }
    else if ( retVal == 0 && thisName != null && otherName == null ) {
      retVal = +1;
    }

    // Compare suffices (which may be null)
    thisName = this.getMemberName().getSuffix();
    otherName = other.getMemberName().getSuffix();
    if ( retVal == 0 && thisName != null && otherName != null ) {
      retVal = thisName.compareToIgnoreCase( otherName );
    }
    else if ( retVal == 0 && thisName == null && otherName != null ) {
      retVal = -1;
    }
    else if ( retVal == 0 && thisName != null && otherName == null ) {
      retVal = +1;
    }

    return retVal;
  } // compareTo(Object)

} // MemberSnapshot

/*
 * $Log: MemberSnapshot.java,v $
 * Revision 1.8  2003/02/26 03:38:45  rphall
 * Added copyright and GPL license
 *
 * Revision 1.7  2003/02/20 04:44:37  rphall
 * Switched from String values to Object values: Email, MemberRole, etc
 *
 * Revision 1.6  2003/02/19 22:08:42  rphall
 * Removed gratuitous use of CLRA acronym
 *
 * Revision 1.5  2003/02/18 04:20:15  rphall
 * Removed ValidationException
 *
 * Revision 1.4  2003/02/15 04:31:42  rphall
 * Changes connected to major revision of MemberBean
 *
 */

