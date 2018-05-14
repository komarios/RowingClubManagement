/*
 * Copyright (c) Carnegie Lake Rowing Association 2002. All rights reserved.
 * Distributed under the GPL license. See doc/COPYING.
 * $RCSfile: MemberTag.java,v $
 * $Date: 2003/02/26 03:38:46 $
 * $Revision: 1.6 $
 */

package com.clra.web;

import com.clra.member.MemberDBRead;
import com.clra.member.MemberException;
import com.clra.member.MemberSnapshot;
import java.io.IOException;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;

/**
 * MemberTag <b>member</b>, used to get the member who is currently
 * logged in. This class is a thin wrapper around the MemberView class.
 * <p>
 * MemberTag Lib Descriptor
 * <p><pre>
 * &lt;name&gt;memberId&lt;/name&gt;
 * &lt;tagclass&gt;com.clra.web.MemberIdTag&lt;/tagclass&gt;
 * &lt;bodycontent&gt;empty&lt;/bodycontent&gt;
 * &lt;info&gt;Gets a property of an authenticated member.&lt;/info&gt;
 * </pre>
 * Most properties are self-explanatory. Name, Address, Telephone and
 * ID values require special names. The birth date is returned as
 * "mm/dd/yyyy". If no property is specified, the default is "fullName".<ul>
 * <li> PN_ACCOUNTNAME == "accountName" == getAccountName()</li>
 * <li> PN_STREET1 == "street1" == getAddress().getStreet1()</li>
 * <li> PN_STREET2 == "street2" == getAddress().getStreet2()</li>
 * <li> PN_CITY == "city" == getAddress().getCity()</li>
 * <li> PN_STATE == "state" == getAddress().getState()</li>
 * <li> PN_ZIP == "zip" == getAddress().getZip()</li>
 * <li> PN_BIRTH == "birthDate" == getBirthDate() [mm/dd/yyyy]</li>
 * <li> PN_ACCOUNTTYPESTR == "accountTypeStr" == getAccountTypeStr()</li>
 * <li> PN_EMAIL == "email" == getEmail()</li>
 * <li> PN_MEMBERID == "memberId" == getId()</li>
 * <li> PN_FULLNAME == "fullName" == getMemberName().getName()</li>
 * <li> PN_LASTNAME == "lastName" == getMemberName().getLastName()</li>
 * <li> PN_FIRSTNAME == "firstName" == getMemberName().getFirstName()</li>
 * <li> PN_MIDDLENAME == "middleName" == getMemberName().getMiddleName()</li>
 * <li> PN_SUFFIX == "suffix" == getMemberName().getSuffix()</li>
 * <li> PN_EVENINGPHONE== "eveningPhone" == [get EVENING telephone]</li>
 * <li> PN_DAYPHONE == "dayPhone" == [get DAY telephone]</li>
 * <li> PN_OTHERPHONE == "otherPhone" == [get OTHER telephone]</li>
 * </ul>
 *
 * @version $Id: MemberTag.java,v 1.6 2003/02/26 03:38:46 rphall Exp $
 * @author <a href="mailto:rphall@pluto.njcc.com>Rick Hall</a>
 */

public class MemberTag extends TagSupport {

  public final static String PN_ACCOUNTNAME = "accountName";
  public final static String PN_STREET1 = "street1";
  public final static String PN_STREET2 = "street2";
  public final static String PN_CITY = "city";
  public final static String PN_STATE = "state";
  public final static String PN_ZIP = "zip";
  public final static String PN_BIRTH = "birthDate";
  public final static String PN_ACCOUNTTYPESTR = "accountTypeStr";
  public final static String PN_EMAIL = "email";
  public final static String PN_MEMBERID = "memberId";
  public final static String PN_FULLNAME = "fullName";
  public final static String PN_LASTNAME = "lastName";
  public final static String PN_FIRSTNAME = "firstName";
  public final static String PN_MIDDLENAME = "middleName";
  public final static String PN_SUFFIX = "suffix";
  public final static String PN_EVENINGPHONE= "eveningPhone";
  public final static String PN_DAYPHONE = "dayPhone";
  public final static String PN_OTHERPHONE = "otherPhone";

  private String _key = Configuration.KEY_MEMBER;
  private String _property = Configuration.MEMBER_PROPERTY;
  private MemberView _member = null;

  /**
   * Utility that checks for the MEMBER_ID key in the session context
   * @return member id, or null if session attribute is not set.
   */
  public static MemberView getMemberFromKey( HttpSession session, String key )
    throws JspException {

    // Preconditions
    if ( session == null ) {
      throw new JspException( "null session" );
    }
    if ( key == null || key.trim().length() == 0 ) {
      throw new JspException( "invalid key" );
    }

    MemberView retVal = (MemberView) session.getAttribute( key );
    return retVal;
  } // getMemberId(HttpSession)

  /**
   * Utility that gets the authenticated user from the request context
   * @return user account name
   * @exception JspException if the current user is not authenticated
   */
  public static MemberView getMemberFromAuthenticatedUser(
    HttpServletRequest request) throws JspException {

    // Precondition
    if ( request == null ) {
      throw new JspException( "null request" );
    }

    String user = request.getRemoteUser();
    if ( user == null || user.trim().length() == 0 ) {
      String msg = MemberTag.class.getName() + ": "
        + "INVALID: HttpServletRequest.getRemoteUser() == '" + user + "'";
      throw new JspException( msg );
    }

    MemberView retVal = null;
    try {
      MemberSnapshot ms = MemberDBRead.findMemberByAccountName(user);
      retVal = new MemberView( ms );
    }
    catch( MemberException x ) {
      throw new JspException( x.toString() );
    }

    return retVal;
  } // getMemberFromAuthenticatedUser(HttpServletRequest)

  /**
   * Utility that gets a MemberView for the specified memberId.
   * @return strMemberId a String that can be parsed to a valid
   * integer corresponding to a key in the Member table.
   * @exception JspException if the current user is not authenticated
   */
  public static MemberView getMemberFromMemberId( String strMemberId )
    throws JspException {

    MemberView retVal = null;
    try {
      Integer memberId = new Integer( strMemberId );
      retVal = getMemberFromMemberId(memberId);
    }
    catch( NumberFormatException x ) {
      throw new JspException( x.toString() );
    }

    return retVal;
  } // getMemberFromMemberId(String)
  
  
  /**
   * Utility that gets a MemberView for the specified memberId
   * @return memberId a non-null Integer corresponding to a key in the
   * Member table
   * @exception JspException if the current user is not authenticated
   */
  public static MemberView getMemberFromMemberId( Integer memberId)
    throws JspException {

    // Precondition
    if ( memberId == null ) {
      throw new JspException( "null memberId" );
    }

    MemberView retVal = null;
    try {
      MemberSnapshot ms = MemberDBRead.findMemberByMemberId(memberId);
      retVal = new MemberView( ms );
    }
    catch( MemberException x ) {
      throw new JspException( x.toString() );
    }

    return retVal;
  } // getMemberFromMemberId(Integer)
  
  /**
   * Utility that inserts member info in the session context
   */
  public static void setMemberInSession(HttpSession session,
    String key, MemberView member) throws JspException {

    // Preconditions
    if ( session == null ) {
      throw new JspException( "null session" );
    }
    if ( key == null || key.trim().length() == 0 ) {
      throw new JspException( "invalid key" );
    }
    if ( member == null ) {
      throw new JspException( "null member" );
    }

    session.setAttribute( key, member );
    return;
  } // setMemberInSession(HttpSession,String,MemberView)

  public String getKey() {
    return this._key;
  }

  public void setKey() {
    this._key = _key;
  }

  public String getProperty() {
    return this._property;
  }

  public void setProperty() {
    this._property = _property;
  }

  /**
   * Method called at end of Tag to output member property
   *
   * @return EVAL_PAGE
   */
  public final int doEndTag() throws JspException {

    // Check for named member
    String key = this.getKey();
    HttpSession session = this.pageContext.getSession();
    this._member = getMemberFromKey( session, key );

    // If the key is not found, create it and store it
    if ( this._member == null ) {
      HttpServletRequest sr = (HttpServletRequest)this.pageContext.getRequest();
      this._member = getMemberFromAuthenticatedUser( sr );
      session.setAttribute( key, this._member );
      }

    // Get the requested property
    String property = this.getProperty();
    String value = null;
    if ( property.equalsIgnoreCase( PN_ACCOUNTNAME ) ) {
      value = this._member.getAccountName();
    }
    else if ( property.equalsIgnoreCase( PN_STREET1 ) ) {
      value = this._member.getAddress().getStreet1();
    }
    else if ( property.equalsIgnoreCase( PN_STREET2 ) ) {
      value = this._member.getAddress().getStreet2();
    }
    else if ( property.equalsIgnoreCase( PN_CITY ) ) {
      value = this._member.getAddress().getCity();
    }
    else if ( property.equalsIgnoreCase( PN_STATE ) ) {
      value = this._member.getAddress().getState();
    }
    else if ( property.equalsIgnoreCase( PN_ZIP ) ) {
      value = this._member.getAddress().getZip();
    }
    else if ( property.equalsIgnoreCase( PN_BIRTH ) ) {
      value = null; // FIXME
    }
    else if ( property.equalsIgnoreCase( PN_ACCOUNTTYPESTR ) ) {
      value = this._member.getAccountTypeStr();
    }
    else if ( property.equalsIgnoreCase( PN_EMAIL ) ) {
      value = this._member.getEmail();
    }
    else if ( property.equalsIgnoreCase( PN_MEMBERID ) ) {
      value = "" + this._member.getId();
    }
    else if ( property.equalsIgnoreCase( PN_FULLNAME ) ) {
      value = this._member.getName();
    }
    else if ( property.equalsIgnoreCase( PN_LASTNAME ) ) {
      value = this._member.getMemberName().getLastName();
    }
    else if ( property.equalsIgnoreCase( PN_FIRSTNAME ) ) {
      value = this._member.getMemberName().getFirstName();
    }
    else if ( property.equalsIgnoreCase( PN_MIDDLENAME ) ) {
      value = this._member.getMemberName().getMiddleName();
    }
    else if ( property.equalsIgnoreCase( PN_SUFFIX ) ) {
      value = this._member.getMemberName().getSuffix();
    }
    else if ( property.equalsIgnoreCase( PN_EVENINGPHONE) ) {
      value = null; // FIXME
    }
    else if ( property.equalsIgnoreCase( PN_DAYPHONE ) ) {
      value = null; // FIXME
    }
    else if ( property.equalsIgnoreCase( PN_OTHERPHONE ) ) {
      value = null; // FIXME
    }

    if ( value == null ) {
      value = "";
    }

    try {
      this.pageContext.getOut().write(value);
    }
    catch( IOException x ) {
      throw new JspException( x.toString() );
    }

    return EVAL_PAGE;
  } // doEndTag()

  /**
   * Release any acquired resources.
   */
  public void release() {
    super.release();
    this._key = Configuration.KEY_MEMBER;
    this._property = Configuration.KEY_MEMBER;
    this._member = null;
  }

} // MemberIdTag

/*
 * $Log: MemberTag.java,v $
 * Revision 1.6  2003/02/26 03:38:46  rphall
 * Added copyright and GPL license
 *
 * Revision 1.5  2003/02/20 16:31:39  rphall
 * Improved diagnostic message if remoteUser is null
 *
 * Revision 1.4  2003/02/20 04:52:36  rphall
 * Removed gratuitous use of CLRA acronym
 *
 * Revision 1.3  2002/06/20 20:45:04  rphall
 * Added JSP/DB code to look up members by id
 */

