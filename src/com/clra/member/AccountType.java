/*
 * Copyright (c) Carnegie Lake Rowing Association 2002. All rights reserved.
 * Distributed under the GPL license. See doc/COPYING.
 * $RCSfile: AccountType.java,v $
 * $Date: 2003/03/01 00:45:14 $
 * $Revision: 1.6 $
 */

package com.clra.member;

import java.io.Serializable;

/**
 * Encapsulates information about the type of account of a member.
 * The type of an account is used in several ways:<ul>
 * <li> By the treasurer, as a part of accounting</li>
 * <li> By the member-manager and others, when boatings are created</li>
 * <li> By social chairs and others, when contacting past and present
 * membership of the club.</li></ul>
 * 
 * @version $Revision: 1.6 $ $Date: 2003/03/01 00:45:14 $
 * @author <a href="mailto:rphall@pluto.njcc.com">Rick Hall</a>
 */
public class AccountType implements Serializable {

  /** Designates a current account of an experienced member */
  public final static String TYPE_FULL = "FULL";

  /**
   * Designates a current account of a member with less than
   * a year's experience.
   */
  public final static String TYPE_NOVICE = "NOVICE";

  /** Designates a current account of a Learn-To-Row participant */
  public final static String TYPE_LTR = "LTR";

  /** Designates the account of a former novice or experienced member */
  public final static String TYPE_ALUMNI = "ALUMNI";

  /** Designates the account of a former LTR participant */
  public final static String TYPE_LTR_ALUM = "LTR-ALUM";

  /** Designates the account of a paid contractor */
  public final static String TYPE_CONTRACTOR = "CONTRACT";

  /** Designates a duplicate account */
  public final static String TYPE_DUPLICATE = "DUPLICAT";

  public final static AccountType FULL =
    new AccountType( TYPE_FULL );

  public final static AccountType NOVICE =
    new AccountType( TYPE_NOVICE );

  public final static AccountType LTR =
    new AccountType( TYPE_LTR );

  public final static AccountType ALUMNI =
    new AccountType( TYPE_ALUMNI );

  public final static AccountType LTR_ALUM =
    new AccountType( TYPE_LTR_ALUM );

  public final static AccountType CONTRACTOR =
    new AccountType( TYPE_CONTRACTOR );

  public final static AccountType DUPLICATE =
    new AccountType( TYPE_DUPLICATE );

  public final static String[] ALLOWED_TYPES() {
    return new String[] {
        TYPE_FULL,
        TYPE_NOVICE,
        TYPE_LTR,
        TYPE_ALUMNI,
        TYPE_LTR_ALUM,
        TYPE_CONTRACTOR,
        TYPE_DUPLICATE 
    };
  }

  /** The type of the Member */
  private final String accountType;

  public AccountType( String type ) {
    // Preconditions
    if ( type == null ) {
      throw new IllegalArgumentException( "null type" );
    } 
    type = type.toUpperCase().trim();
    boolean isOK = false;
    String[] ALLOWED = ALLOWED_TYPES();
    for ( int i=0; i<ALLOWED.length; i++ ) {
      if ( ALLOWED[i].equals(type) ) {
        isOK = true;
        break;
      }
    }
    if ( !isOK ) {
      String msg = "invalid account type == '" + type + "'";
      throw new IllegalArgumentException( msg );
    }

    this.accountType = type;

  } // ctor(String,String)

  public String getAccountType() {
    return this.accountType;
  }

  public String toString() {
    return this.accountType;
  }

  public boolean equals( Object o ) {
    boolean retVal;
    if ( o == null ) {
      retVal = false;
    }
    else if ( o instanceof AccountType ) {
      AccountType that = (AccountType) o;
      retVal = this.accountType.equalsIgnoreCase( that.accountType );
    }
    else if ( o instanceof String ) {
      retVal = this.accountType.equalsIgnoreCase( o.toString().trim() );
    }
    else {
      retVal = false;
    }

    return retVal;
  } // equals(Object)

  public int hashCode() {
    return this.accountType.trim().toUpperCase().hashCode();
  }

} // AccountType

/*
 * $Log: AccountType.java,v $
 * Revision 1.6  2003/03/01 00:45:14  rphall
 * Removed no-param default constructor
 *
 * Revision 1.5  2003/02/28 14:05:48  rphall
 * Added default constructor so that class could be used as a Java bean
 *
 * Revision 1.4  2003/02/26 03:38:45  rphall
 * Added copyright and GPL license
 *
 * Revision 1.3  2003/02/21 04:59:54  rphall
 * Shortened DB value for 'DUPLICATE'
 *
 * Revision 1.2  2003/02/19 22:23:01  rphall
 * Removed gratuitous use of CLRA acronym
 *
 * Revision 1.1  2003/02/19 03:41:39  rphall
 * Working with unit tests
 *
 */

