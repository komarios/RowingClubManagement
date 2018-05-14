/*
 * Copyright (c) Carnegie Lake Rowing Association 2002. All rights reserved.
 * Distributed under the GPL license. See doc/COPYING.
 * $RCSfile: MemberNameFormat.java,v $
 * $Date: 2003/02/26 03:38:46 $
 * $Revision: 1.4 $
 */

package com.clra.web;

import com.clra.member.MemberName;
import java.io.Serializable;

/**
 * Formats the name of member according to a few different patterns.
 * @version $Revision: 1.4 $ $Date: 2003/02/26 03:38:46 $
 * @author <a href="mailto:rphall@pluto.njcc.com">Rick Hall</a>
 */
public abstract class MemberNameFormat implements Serializable {

  protected MemberNameFormat() {}

  /**
   * Returns a non-null, non-blank, trimmed String representing the name
   * of a member. This method is a template. Subclasses should implement
   * <tt>protectedFormat(MemberName)</tt> to define new formatting behaviour.
   * @param memberName a non-null MemberName (enforced)
   * @return a non-null, non-blank String (enforced)
   */
  public final String format( MemberName memberName ) {
    preConditions(memberName);
    String retVal = protectedFormat(memberName).trim();
    postConditions(retVal);
    return retVal;
  }

  private final static void preConditions( MemberName memberName ) {
    if ( memberName == null ) {
      throw new IllegalArgumentException( "null member name" );
    }
  }

  private final static void postConditions( String retVal ) {
    if ( retVal == null || retVal.length() == 0 ) {
      throw new IllegalStateException( "invalid return value" );
    }
  }

  /**
   * Subclasses must implement this method to define formatting behavior.
   * Subclasses must return a non-null, non-blank String. They do not
   * have to check that the member name is valid; that check has already
   * been performed. Nor do they have to trim the return value; that task
   * will be automatically performed before the return value is passed back.
   */
  protected abstract String protectedFormat( MemberName memberName );

  /**
   * Returns a string of the form:<pre>
   *   First
   * </pre>
   */
  public final static MemberNameFormat FIRSTONLY =

    new MemberNameFormat() {

      protected String protectedFormat( MemberName memberName ) {
        return memberName.getFirstName();
      }

    }; // FIRSTONLY

  /**
   * Returns a string of the form:<pre>
   *   First [Middle] Last [Suffix]
   * </pre>
   */
  public final static MemberNameFormat FIRSTLAST =

    new MemberNameFormat() {

      protected String protectedFormat( MemberName memberName ) {
        StringBuffer sb = new StringBuffer();
        String test = memberName.getFirstName();
        sb.append( test );
        test = memberName.getMiddleName();
        if ( test != null ) {
          sb.append( " " + test );
        }
        test = memberName.getLastName();
        sb.append( " " + test );
        test = memberName.getSuffix();
        if ( test != null ) {
          sb.append( " " + test );
        }
        String retVal = new String( sb );

        return retVal;
      } // protectedFormat(MemberName)

    }; // FIRSTLAST

  /**
   * Returns a string of the form:<pre>
   *   Last, First [Middle] [Suffix]
   * </pre>
   */
  public final static MemberNameFormat LASTFIRST =

    new MemberNameFormat() {

      protected String protectedFormat( MemberName memberName ) {
        StringBuffer sb = new StringBuffer();
        String test = memberName.getLastName();
        sb.append( test );
        test = memberName.getFirstName();
        sb.append( ", " + test );
        test = memberName.getMiddleName();
        if ( test != null ) {
          sb.append( " " + test );
        }
        test = memberName.getSuffix();
        if ( test != null ) {
          sb.append( " " + test );
        }
        String retVal = new String( sb );

        return retVal;
      } // protectedFormat(MemberName)

    }; // LASTFIRST

} // MemberNameFormat

/*
 * $Log: MemberNameFormat.java,v $
 * Revision 1.4  2003/02/26 03:38:46  rphall
 * Added copyright and GPL license
 *
 * Revision 1.3  2003/02/19 22:38:39  rphall
 * Removed gratuitous use of CLRA acronym
 *
 * Revision 1.2  2002/02/18 18:06:19  rphall
 * Ran dos2unix to remove ^M (carriage return) from end of lines
 */

