/*
 * Copyright (c) Carnegie Lake Rowing Association 2002. All rights reserved.
 * Distributed under the GPL license. See doc/COPYING.
 * $RCSfile: ValidationException.java,v $
 * $Date: 2003/02/26 03:38:45 $
 * $Revision: 1.5 $
 */

package com.clra.util;

/**
 * Indicates data has failed validation rules. The accessor <tt>getType()</tt>
 * indicates what type of data has failed validation.
 *
 * @version $Id: ValidationException.java,v 1.5 2003/02/26 03:38:45 rphall Exp $
 * @author <a href="mailto:rphall@pluto.njcc.com">Rick Hall</a>
 */
public class ValidationException extends Exception {

  public final static int UNKNOWN = -1;
  public final static int MEMBER_ID = 0;
  public final static int MEMBER_ACCOUNT_NAME = 1;
  public final static int MEMBER_ACCOUNT_PASSWORD = 2;
  public final static int MEMBER_ACCOUNT_TYPE = 3;
  public final static int MEMBER_NAME = 4;
  public final static int MEMBER_NAME_FIRST = 5;
  public final static int MEMBER_NAME_LAST = 6;
  public final static int MEMBER_EMAIL = 7;
  public final static int MEMBER_TELEPHONE_NUMBER = 8;
  public final static int MEMBER_TELEPHONE_COLLECTION = 9;
  public final static int MEMBER_ADDRESS = 10;
  public final static int MEMBER_ADDRESS_STREET1 = 11;
  public final static int MEMBER_ADDRESS_STREET2 = 12;
  public final static int MEMBER_ADDRESS_CITY = 13;
  public final static int MEMBER_ADDRESS_STATE = 14;
  public final static int MEMBER_ADDRESS_ZIP = 15;
  public final static int MEMBER_ACCOUNT_YEAR = 16;
  public final static int MEMBER_BIRTH = 17;

  private final static int LOWER = UNKNOWN;
  private final static int UPPER = MEMBER_BIRTH;

  private final int type;

  public ValidationException( String devMsg ) {
    this( UNKNOWN, devMsg );
  }

  public ValidationException( int type ) {
    super();
    this.type = type;
    if ( type < LOWER || type > UPPER ) {
      throw new IllegalArgumentException( "invalid type == " + type );
    }
  } // ctor(int)

  public ValidationException( int type, String devMsg ) {
    super( devMsg );
    this.type = type;
    if ( type < LOWER || type > UPPER ) {
      throw new IllegalArgumentException( "invalid type == " + type );
    }
  } // ctor(int,String)

  public int getType() {
    return this.type;
  }

} // ValidationException

/*
 * $Log: ValidationException.java,v $
 * Revision 1.5  2003/02/26 03:38:45  rphall
 * Added copyright and GPL license
 *
 * Revision 1.4  2003/02/19 22:30:31  rphall
 * Removed gratuitous use of CLRA acronym
 *
 * Revision 1.3  2003/02/18 04:24:50  rphall
 * Added constructor with String parameter
 */

