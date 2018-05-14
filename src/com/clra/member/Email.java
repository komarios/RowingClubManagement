/*
 * Copyright (c) Carnegie Lake Rowing Association 2002. All rights reserved.
 * Distributed under the GPL license. See doc/COPYING.
 * $RCSfile: Email.java,v $
 * $Date: 2003/03/01 00:45:14 $
 * $Revision: 1.5 $
 */

package com.clra.member;

import java.io.Serializable;

/**
 * Encapsulates the email address of a member. This class performs
 * rough validation of email addresses, in the spirit of RFC 821, but not
 * to the letter of that specification. An email address is considered valid
 * if it is of the form:<pre>
 *      "local" "@" "domain"
 * </pre>
 * where the "local" part can be any combination of [a-zA-Z0-9.] that does not
 * start with a digit or dot nor ends with a dot. (To be rigorous, the local
 * part should exclude doubled dot sequences, and it should allow all escaped
 * ASCII characters.) The "domain" part can be any combination of [-a-zA-Z09.]
 * that does not start with a digit, hyphen or dot, and that does not end with
 * a hyphen or dot. (To be rigorous, the domain part should exclude doubled
 * dot or hyphen sequences, and it should allow dotted numeric addresses.)</p>
 * 
 * @version $Id: Email.java,v 1.5 2003/03/01 00:45:14 rphall Exp $
 * @author <a href="mailto:rphall@pluto.njcc.com">Rick Hall</a>
 */
public class Email implements Serializable {

  public static boolean isValidStart( char c ) {
    boolean retVal =   ( 'a' <= c && c <= 'z' );
    retVal = retVal || ( 'A' <= c && c <= 'Z' );

    return retVal;
  }

  public static boolean isValidEnd( char c ) {
    boolean retVal = isValidStart( c ); 
    retVal = retVal || ( '0' <= c && c <= '9' );

    return retVal;
  }

  public static boolean isValidLocalChar( char c ) {
    boolean retVal = isValidEnd( c ); 
    retVal = retVal || c == '.' || c == '_' || c == '-' ;

    return retVal;
  }

  public static boolean isValidDomainChar( char c ) {
    boolean retVal = isValidEnd( c ); 
    retVal = retVal || c == '.' || c == '-' ;

    return retVal;
  }

  public static boolean isValidEmail( String s ) {

    // Not null, and no leading or trailing whitespace
    boolean retVal = s != null;
    retVal = retVal && s.trim().length() == s.length();

    // An '@' sign in the middle
    if ( retVal ) {

      final int idxAtSign = s.indexOf( '@' );
      retVal = ( 0 < idxAtSign ) && ( idxAtSign < s.length() - 1 );

      // A valid local part
      final int idxLastLocal = idxAtSign - 1;
      retVal = retVal && isValidStart( s.charAt(0) );
      retVal = retVal && isValidEnd( s.charAt(idxLastLocal) );
      for ( int i=1; retVal && i<idxLastLocal; i++ ) {
        retVal = isValidLocalChar( s.charAt(i) );
      }

      // A valid domain
      final int idxFirstDomain = idxAtSign + 1;
      retVal = retVal && isValidStart( s.charAt(idxFirstDomain) );
      retVal = retVal && isValidEnd( s.charAt(s.length()-1) );
      for ( int i=idxFirstDomain+1; retVal && i<s.length()-1; i++ ) {
        retVal = isValidDomainChar( s.charAt(i) );
      }

    } // if s non-null

    return retVal;
  } // isValidEmail

  private final String email;

  public Email( String s ) {

    this.email = s;

    if ( s == null ) {
      throw new IllegalArgumentException( "null email address" );
    }
    s = s.trim();
    if ( !isValidEmail(s) ) {
      throw new IllegalArgumentException( "invalid email == '" +s+ "'" );
    }

  } // ctor

  public String toString() {
    return this.email;
  }

  public boolean equals( Object o ) {
    boolean retVal;
    if ( o == null ) {
      retVal = false;
    }
    else if ( o instanceof String ) {
      String s = ((String)o).trim();
      retVal = this.email.equalsIgnoreCase(s);
    }
    else if ( o instanceof Email ) {
      Email that = (Email) o;
      retVal = this.email.equalsIgnoreCase(that.email);
    }
    else {
      retVal = false;
    }

    return retVal;
  } // equals(Object)

  public int hashCode() {
    int retVal = this.email.toUpperCase().hashCode();
    return retVal;
  }

} // Email

/*
 * $Log: Email.java,v $
 * Revision 1.5  2003/03/01 00:45:14  rphall
 * Removed no-param default constructor
 *
 * Revision 1.4  2003/02/28 14:05:49  rphall
 * Added default constructor so that class could be used as a Java bean
 *
 * Revision 1.3  2003/02/26 03:38:45  rphall
 * Added copyright and GPL license
 *
 * Revision 1.2  2003/02/19 22:23:05  rphall
 * Removed gratuitous use of CLRA acronym
 *
 * Revision 1.1  2003/02/19 03:41:40  rphall
 * Working with unit tests
 *
 */

