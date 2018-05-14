/*
 * Copyright (c) Carnegie Lake Rowing Association 2002. All rights reserved.
 * Distributed under the GPL license. See doc/COPYING.
 * $RCSfile: EditMemberInfoAction.java,v $
 * $Date: 2003/02/26 03:38:46 $
 * $Revision: 1.12 $
 */

package com.clra.web;

import com.clra.member.MemberDBRead;
import com.clra.member.MemberSnapshot;
import com.clra.member.MemberName;
import com.clra.member.Address;
import com.clra.member.Telephone;
import com.clra.util.ErrorUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import javax.ejb.CreateException;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Category;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;

/**
 * A workflow manager that sets up an input form that queries a user for
 * information needed to maintain the user's personal information. 
 * See the related workflow manager, <tt>SaveMemberInfoAction</tt>, 
 * which pulls information from the input form and invokes the business 
 * logic that does the actual work of editing the personal information.
 *
 * The 'function' parameter is 'Edit' or 'Manage'.
 * For 'Edit', retrieve a MemberSnapshot for the member specified by the 
 * parameter 'id'.
 * For 'Manage', present a blank form.
 *
 * @author <a href="mailto:jmstone@nerc.com"> Jan Stone </a>
 * @see MemberInfoForm
 * @see SaveMemberInfoAction
 */ 

public final class EditMemberInfoAction extends Action {

  private final static String   base   = EditMemberInfoAction.class.getName();
  private final static Category theLog = Category.getInstance( base );

  /**
   * Indicates the purpose of this workflow is to edit an
   * existing member with restrictions (by a member of the club)
   */
  public final static String FUNCTION_MEMBEREDIT =
    MemberInfoForm.FUNCTION_MEMBEREDIT;

  /**
   * Indicates the purpose of this workflow is to create
   * a new member.
   */
  public final static String FUNCTION_ADMINCREATE =
    MemberInfoForm.FUNCTION_ADMINCREATE;

  /**
   * Indicates the purpose of this workflow is to edit an
   * existing member without restriction (by an admin of the club)
   */
  public final static String FUNCTION_ADMINEDIT =
    MemberInfoForm.FUNCTION_ADMINEDIT;

  /**
   * Handle the workflow step in which a form is popluated with data from
   * the database.
   *
   * @param mapping The ActionMapping used to select this instance
   * @param actionForm The optional ActionForm bean for this request (if any)
   * @param request The HTTP request we are processing
   * @param response The HTTP response we are creating
   *
   * @exception IOException if an input/output error occurs
   * @exception ServletException if a servlet exception occurs
   **/

  public ActionForward perform( ActionMapping mapping, ActionForm form,
    HttpServletRequest request, HttpServletResponse response )
      throws IOException, ServletException {

    // A null return value indicates that processing should continue
    ActionForward retVal = null;

    String msg;

    // Extract the workflow action
    HttpSession session = request.getSession();
    String function = request.getParameter("function");

    if ( theLog.isInfoEnabled() ) {
      msg = "function = " + function;
      theLog.info( msg );
    }

    if ( function == null ) {
      theLog.error( "form's http request has null function parameter" );
      retVal = mapping.findForward("failure");
    }

    // 'AdminCreate':  return a nearly blank form with some defaults
    else if ( function.equalsIgnoreCase(FUNCTION_ADMINCREATE) ) {
      MemberInfoForm theForm = (MemberInfoForm)(form);
      theForm.setState( "NJ" );
      int thisYear = Calendar.getInstance().get(Calendar.YEAR);
      theForm.setAccountYear( thisYear );
      theForm.setMember( true );
      retVal = mapping.findForward("success");
    }

    // 'MemberEdit': restricted updates to existing database entry
    // 'AdminEdit': unrestricted updates to existing database entry
    else if ( function.equalsIgnoreCase(FUNCTION_MEMBEREDIT)
            || function.equalsIgnoreCase(FUNCTION_ADMINEDIT) ) {

      String idStr = request.getParameter("id");
      if ( theLog.isInfoEnabled() ) {
        msg = "member id = '" + idStr + "'";
        theLog.info( msg );
      }

      // Populate the member info form
      if ( form != null ) {

        MemberSnapshot ms = null;
        try {
          Integer id = new Integer( idStr );
	        ms = MemberDBRead.findMemberByMemberId( id );
        }
        catch( NumberFormatException x ) {
          theLog.error( "Invalid member id = '" + idStr + "'" );
          retVal = mapping.findForward("failure");
        }
        catch( Exception x ) {
          theLog.error( "GetMemberFromKey threw an exception", x);
          retVal = mapping.findForward("failure");  
        }

        // Return value is null if no errors have occurred so far
        if ( retVal == null ) {

          MemberInfoForm theForm = (MemberInfoForm)(form); 

          if ( theLog.isDebugEnabled() ) {
            theLog.debug("Populating Member Info form");
          }

          try {

            theForm.setFunction( function );

            MemberName nm = ms.getMemberName();
            theForm.setFirstName( nm.getFirstName() );
            theForm.setMiddleName( nm.getMiddleName() );
            theForm.setLastName( nm.getLastName() );
            theForm.setSuffix( nm.getSuffix() );

            Address address = ms.getAddress();
            theForm.setAddress( address );
            theForm.setStreet1( address.getStreet1() );
            theForm.setStreet2( address.getStreet2() );
            theForm.setCity   ( address.getCity() );
            theForm.setState  ( address.getState() );
            theForm.setZip    ( address.getZip() );

            Map phoneNumbers = ms.getTelephoneNumbers();
            Telephone phone = (Telephone)(phoneNumbers.get(Telephone.EVENING));

            if ( theLog.isDebugEnabled() ) {
              msg = "Evening phone = " + phone.toString();
              theLog.debug( msg );
            }

            theForm.setPhoneEveningAreaCode ( phone.getAreaCode() );
            theForm.setPhoneEveningExchange ( phone.getExchange() );
            theForm.setPhoneEveningLocal    ( phone.getLocal() );
            theForm.setPhoneEveningExt      ( phone.getExtension() );

            phone = (Telephone)(phoneNumbers.get(Telephone.DAY));
            if ( theLog.isDebugEnabled() ) {
              msg = "Day phone == '" +
                ( phone == null ? "null" : phone.toString() ) + "'";
              theLog.debug( msg );
            }
            if ( phone != null ) {

              theForm.setPhoneDayAreaCode ( phone.getAreaCode() );
              theForm.setPhoneDayExchange ( phone.getExchange() );
              theForm.setPhoneDayLocal    ( phone.getLocal() );
              theForm.setPhoneDayExt      ( phone.getExtension() );
            }

            phone = (Telephone)(phoneNumbers.get(Telephone.OTHER));
            if ( theLog.isDebugEnabled() ) {
              msg = "Other phone == '" +
                ( phone == null ? "null" : phone.toString() ) + "'";
              theLog.debug( msg );
            }
            if ( phone != null ) {

              theForm.setPhoneOtherAreaCode ( phone.getAreaCode() );
              theForm.setPhoneOtherExchange ( phone.getExchange() );
              theForm.setPhoneOtherLocal    ( phone.getLocal() );
              theForm.setPhoneOtherExt      ( phone.getExtension() );
            }

            if ( ms.hasEmail() ) {
              theForm.setEmail( ms.getEmail().toString() );
            }

            theForm.setId( ms.getId() );
            theForm.setAccountName( ms.getAccountName() );
            theForm.setAccountNameOriginal( ms.getAccountName() );
            theForm.setAccountPassword( ms.getAccountPassword() );
            theForm.setConfirmPassword( ms.getAccountPassword() );

            int y = 1900 + ms.getAccountDate().getYear();
            theForm.setAccountYear( y );

            theForm.setAccountTypeStr( ms.getAccountType().toString() );

            theForm.setMemberRoles( ms.getMemberRoles() );

            if ( ms.hasKnownBirthDate() ) {
              theForm.setBirthDate( ms.getBirthDate() );
            }

            if ( theLog.isInfoEnabled() ) {

              msg = "Member name = '" + theForm.getFullName() + "'";
              theLog.info( msg );

              StringBuffer sb = new StringBuffer();
              sb.append( "Evening phone = '" );
              sb.append( theForm.getPhoneEveningAreaCode().toString() );
              sb.append( " " );
              sb.append( theForm.getPhoneEveningExchange() );
              sb.append( "-" );
              sb.append( theForm.getPhoneEveningLocal() );
              sb.append( " (ext. " );
              sb.append( theForm.getPhoneEveningExt() );
              sb.append( ")" );
              msg = new String(sb);
              theLog.info( msg );

              msg = "Account year = " + theForm.getAccountYear();
              theLog.info( msg );

              msg = "street1 = " + address.getStreet1();
              theLog.info( msg );

              msg = "street2 = " + address.getStreet2();
              theLog.info( msg );

              msg = "city = " + address.getCity();
              theLog.info( msg );

              msg = "state = " + address.getState();
              theLog.info( msg );

              msg = "zip = " + address.getZip();
              theLog.info( msg );
            }

            retVal = mapping.findForward("success");

          } // end try

          catch ( Exception m ) {
            theLog.error( "Error populating form", m );
            retVal = mapping.findForward("failure");
          }

           // Processing is complete.
        } // end if ( retVal == null )

      } // end if form != null

      // Missing form
      else {
        theLog.error( "unexpected null form" );
        retVal = mapping.findForward("failure");
      }

    }  // end if AdminEdit || MemberEdit

    // Invalid 'function' parameter
    else {
      theLog.error( "unrecognized function parameter: '" + function + "'" );
      retVal = mapping.findForward("failure");
    }

    // Forward control to the appropriate page
    if ( retVal == null ) {
      throw new Error( "design error" );
    }
    if ( theLog.isDebugEnabled() ) {
      theLog.debug( "Forwarding to " + retVal.toString() );
    }

    return retVal;
  } // perform


} // EditMemberInfoAction

/*
 * $Log: EditMemberInfoAction.java,v $
 * Revision 1.12  2003/02/26 03:38:46  rphall
 * Added copyright and GPL license
 *
 * Revision 1.11  2003/02/24 13:29:54  rphall
 * Added handling for when accountName changes
 *
 * Revision 1.10  2003/02/20 16:27:41  rphall
 * Minor: removed obsolete comment
 *
 * Revision 1.9  2003/02/20 04:49:36  rphall
 * Reorganized perform method
 *
 */

