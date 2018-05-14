/*
 * Copyright (c) Carnegie Lake Rowing Association 2002. All rights reserved.
 * Distributed under the GPL license. See doc/COPYING.
 * $RCSfile: Telephone.java,v $
 * $Date: 2003/02/26 03:38:45 $
 * $Revision: 1.13 $
 */

package com.clra.member;

import java.io.Serializable;

/**
 * Encapsulates telephone information of a member.
 * @version $Id: Telephone.java,v 1.13 2003/02/26 03:38:45 rphall Exp $
 * @author <a href="mailto:rphall@pluto.njcc.com">Rick Hall</a>
 */
public class Telephone implements Serializable {

  public final static String EVENING = "evening";

  public final static String DAY = "day";

  public final static String OTHER = "other";

  private String areaCode = null;
  private String exchange = null;
  private String local = null;
  private String extension = null;

  // character indices
  private int ac1;  // starting position of area code
  private int xch0; // expected position of space before exchange
  private int loc1; // expected start of local number
  private int xt0;  // expected position of space before optional extension

  public static boolean isValidAreaCode( String areaCode ) {
    boolean retVal = isValidComponent(areaCode);
    retVal = retVal && areaCode.length() == 3;
    return retVal;
  }

  public static boolean isValidExchange( String exchange ) {
    return isValidAreaCode(exchange);
  }

  public static boolean isValidLocal( String local ) {
    boolean retVal = isValidComponent(local);
    retVal = retVal && local.length() == 4;
    return retVal;
  }

  public static boolean isValidExtension( String extension ) {
    boolean retVal;
    if ( extension == null ) {
      retVal = true;
    }
    else {
      retVal = isValidComponent(extension);
      retVal = retVal && extension.length() > 0;
    }
    return retVal;
  }

  private static boolean isValidComponent( String number ) {

    boolean retVal = number != null
        && number.trim().length() == number.length()
        && number.length() <= 5;

    for ( int i=0; retVal && i<number.length(); i++ ) {
      retVal = Character.isDigit( number.charAt(i) );
    }

    return retVal;
  } // isValidLocal(String)

  /** Produces an invalid instance. Used only during deserialization. */
  public Telephone() {}

  public Telephone( String ac, String ech, String lcl, String ext) {

    this.areaCode  =  ac == null ? null :  ac.trim();
    this.exchange  = ech == null ? null : ech.trim();
    this.local     = lcl == null ? null : lcl.trim();

    this.extension = ext == null ? null : ext.trim();
    if ( this.extension != null && this.extension.length() == 0 ) {
      this.extension = null;
    }

    validateComponents();

  } // ctor(String,String,String,String)

  /**
   * @param number a string of the form "(123) 456-7890 Ext. 12345"
   * or "(123)-456-7890" where<ul>
   * <li><tt>123</tt> is the area code, OPTIONALLY enclosed in parentheses</li>
   * <li><tt>456</tt> is the exchange</li>
   * <li><tt>7890</tt> is the local number, and <tt>12345</tt> is an optional
   * extension.</li></ul>
   * The area code, exchange and local number are required, so the minimal
   * length of a valid number is 12 (or 14) including optional parentheses, a
   * space or dash, and a second dash.
   */
  public Telephone( String number ) {

    // Precondition
    if ( number == null || number.length() < 12 ) {
      throw new IllegalArgumentException( "invalid number == " + number );
    }

    // Character indices of components in string [(]ddd[)] ddd-dddd[ {d}*]
    number = number.trim();
    if ( number.charAt(0) == '(' && number.charAt(4) == ')' ) {
      ac1 = 1;       // start of area code
      xch0 = 5;      // position before exchange
    }
    else {    
      ac1 = 0;       // start of area code
      xch0 = 3;      // position before exchange
    }
    loc1 = xch0+5;   // start of local number
    xt0  = loc1 + 4; // position before optional extension

    this.areaCode = parseAreaCode( number, ac1 );
    this.exchange = parseExchange( number, xch0 );
    this.local = parseLocal( number, loc1 );

    this.extension = parseExtension( number, xt0 );
    if ( this.extension != null && this.extension.trim().length() == 0 ) {
      this.extension = null;
    }

    validateComponents();
    if ( this.extension == null && number.length() > xt0 ) {
      String msg = "unable to parse extension from '" + number + "'";
      throw new IllegalArgumentException( msg );
    }

  } // ctor(String)

  private void validateComponents() {

    if ( !isValidAreaCode(this.areaCode) ) {
      throw new IllegalArgumentException( "null or invalid area code" );
    }

    if ( !isValidExchange(this.exchange) ) {
      throw new IllegalArgumentException( "null or invalid exchange" );
    }

    if ( !isValidLocal(this.local) ) {
      throw new IllegalArgumentException( "null or invalid local number" );
    }

  } // validateComponents()

  /** Creates a valid telephone number or returns null */
  public static Telephone createTelephone( String areaCode,
    String exchange, String local, String extension ) {
    Telephone retVal = null;
    if ( isValidAreaCode(areaCode)
         && isValidExchange(exchange) && isValidLocal(local) ) {
      retVal = new Telephone(areaCode,exchange,local,extension);
    }
    return retVal;
  } // createTelephone(String,String,String,String)

  public String getAreaCode()  { return this.areaCode;  }
  public String getExchange()  { return this.exchange;  }
  public String getLocal()     { return this.local;     }
  public String getExtension() { return this.extension; }

  /** @return a 3-digit number starting at index 'ac1', or null on error */
  private static String parseAreaCode( String number, int ac1 ) {
    String retVal = null;
    if ( Character.isDigit( number.charAt(ac1) )
           && Character.isDigit( number.charAt(ac1+1) )
           && Character.isDigit( number.charAt(ac1+2) ) ) {
        retVal = number.substring( ac1, ac1+3 );
    }

    return retVal;
  } // parseAreaCode(String)

  /** @return a 3-digit number, or null on error */
  private static String parseExchange( String number, int xch0 ) {
    String retVal = null;
    if ( (number.charAt(xch0) == ' ' && number.charAt(xch0+4) == '-')
        || (number.charAt(xch0) == '-' && number.charAt(xch0+4) == '-') ) {
      if ( Character.isDigit( number.charAt(xch0+1) ) 
           && Character.isDigit( number.charAt(xch0+2) )
           && Character.isDigit( number.charAt(xch0+3) ) ) {
        retVal = number.substring( xch0+1, xch0+4 );
      }
    }

    return retVal;
  } // parseExchange(String)

  /** @return a 4-digit number, or null on error */
  private static String parseLocal( String number, int loc1 ) {
    String retVal = null;
    if ( Character.isDigit( number.charAt(loc1) )
         && Character.isDigit( number.charAt(loc1+1) )
         && Character.isDigit( number.charAt(loc1+2) )
         && Character.isDigit( number.charAt(loc1+3) ) ) {
      retVal = number.substring( loc1, loc1+4 );
    }

    return retVal;
  } // parseLocal(String)

  /** @return a numerical extension or null */
  private static String parseExtension( String number, int xt0 ) {
    String retVal = null;
    
    // Optional extension is " Ext. " plus at least one digit
    if ( number.length() > xt0+6 ) {
      String nxt = number.substring(xt0, xt0+5);
      if ( nxt.compareTo(" ext.") == 0 || nxt.compareTo(" Ext.") == 0
        || nxt.compareTo(" EXT.") == 0 ) {
        try {
          int test = Integer.parseInt( number.substring(xt0+6) );
          retVal = number.substring(xt0+6);
        }
        catch( NumberFormatException ignored ) {}
      }
    }

    return retVal;
  } // parseExtension(String)

  public String toString() {
    StringBuffer sb = new StringBuffer(25);
    sb.append( "(" );
    sb.append( this.areaCode );
    sb.append( ") " );
    sb.append( this.exchange );
    sb.append( "-" );
    sb.append( this.local );
    if ( this.extension != null && this.extension.trim().length() > 0 ) {
      sb.append( " Ext. " );
      sb.append( this.extension.trim() );
    }
    String retVal = new String( sb );

    return retVal;
  } // toString()

  public boolean equals( Object o ) {
    boolean retVal;
    if ( o == null ) {
      retVal = false;
    }
    else if ( o instanceof Telephone ) {
      Telephone that = (Telephone) o;
      retVal = this.areaCode.equals(that.areaCode);
      retVal = retVal && this.exchange.equals(that.exchange);
      retVal = retVal && this.local.equals(that.local);
      if ( this.extension != null ) {
        retVal = retVal && this.extension.equals(that.extension);
      }
      else {
        retVal = retVal && that.extension == null;
      }
    }
    else if ( o instanceof String ) {
      retVal = false;
      try {
        Telephone other = new Telephone( (String)o );
        retVal = this.equals(other);
      } catch( Exception x ) {
        // don't care
      }
    }
    else {
      retVal = false;
    }

    return retVal;
  } // equals(Object)

  public int hashCode() {
    int retVal = this.local.hashCode(); // good enough
    return retVal;
  }

} // Telephone

/*
 * $Log: Telephone.java,v $
 * Revision 1.13  2003/02/26 03:38:45  rphall
 * Added copyright and GPL license
 *
 * Revision 1.12  2003/02/21 15:08:33  rphall
 * More fixes to blank extension bug
 *
 * Revision 1.11  2003/02/21 14:30:37  rphall
 * Fixed bug with blank extensions
 *
 * Revision 1.10  2003/02/19 22:26:39  rphall
 * Removed gratuitous use of CLRA acronym
 *
 * Revision 1.9  2003/02/19 03:16:56  rphall
 * Added validation for extension
 *
 * Revision 1.8  2003/02/18 04:21:34  rphall
 * Added validation methods
 *
 * Revision 1.7  2003/02/15 04:31:42  rphall
 * Changes connected to major revision of MemberBean
 *
 * Revision 1.6  2003/02/10 05:10:51  rphall
 * Added parsing for '-' between area code and exchange
 *
 */

