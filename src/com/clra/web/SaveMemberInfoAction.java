/*
 * Copyright (c) Carnegie Lake Rowing Association 2002. All rights reserved.
 * Distributed under the GPL license. See doc/COPYING.
 * $RCSfile: SaveMemberInfoAction.java,v $
 * $Date: 2003/02/26 03:38:46 $
 * $Revision: 1.11 $
 */

package com.clra.web;

import com.clra.web.MemberView;
import com.clra.web.MemberTag;
import com.clra.member.Address;
import com.clra.member.AccountType;
import com.clra.member.Email;
import com.clra.member.MemberName;
import com.clra.member.MemberRole;
import com.clra.member.IMember;
import com.clra.member.IMemberHome;
import com.clra.member.MemberUtils;
import com.clra.member.MemberSnapshot;
import com.clra.member.Telephone;
import com.clra.util.ErrorUtils;
import com.clra.util.ValidationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.ejb.CreateException;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Category;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionServlet;

/**
 * A workflow manager that pulls a member's personal information from
 * an input form. The data is used to invoke business logic that
 * maintains the member information in a database.
 * See the related workflow manager, <tt>MemberInfoAction</tt>, which
 * initializes an input form with data for a member.
 *
 * @author <a href="mailto:jmstone@nerc.com"> Jan Stone </a>
 * @see MemberInfoForm
 * @see EditMemberInfoAction
 */
public final class SaveMemberInfoAction extends Action {

  private final static String   base   = SaveMemberInfoAction.class.getName();
  private final static Category theLog = Category.getInstance( base );

  /**
   * Handle the workflow step in which a form is popluated with date from
   * a member's data.
   */
  public ActionForward perform( ActionMapping mapping, ActionForm form,
    HttpServletRequest request, HttpServletResponse response )
      throws IOException, ServletException {

    theLog.info( "Invoked" );

    // A null return value indicates that processing should continue
    ActionForward retVal = null;
    
    // Get the form, the form function, and the submission parameter
    final MemberInfoForm theForm = (MemberInfoForm)(form);
    final String function = theForm.getFunction();
    final String submission = request.getParameter("submit");
    if (submission == null) {
      theLog.debug("submission = " + submission);
      if ( theForm.isAdminEdit() ) {
        retVal = mapping.findForward("browse");
      } else {
        retVal = mapping.findForward("success");
      }
    }

    Integer    memberId = null;
    MemberName memberName = null;
    Address    memberAddress = null;
    Map        memberPhones = null;
    Email      memberEmail = null;
    String     memberEmailString = null;
    Date       memberBirthDate = null;

    String       accountName = null;
    String       accountNameOriginal = null;
    boolean      isAccountNameModified = false;
    String       accountPassword = null;
    AccountType  accountType = null;
    Date         accountDate = null;
    MemberRole[] accountRoles = null;

    // Retrieve the (relevant) form data
    if ( retVal == null ) {

      memberId = theForm.getId(); // may be null iff ADMINCREATE

      memberPhones = new HashMap();

      String  ac = theForm.getPhoneEveningAreaCode();
      String ech = theForm.getPhoneEveningExchange();
      String lcl = theForm.getPhoneEveningLocal();
      String ext = theForm.getPhoneEveningExt();
      Telephone phone = Telephone.createTelephone(ac,ech,lcl,ext);
      memberPhones.put( Telephone.EVENING, phone );

      ac  = theForm.getPhoneDayAreaCode();
      ech = theForm.getPhoneDayExchange();
      lcl = theForm.getPhoneDayLocal();
      ext = theForm.getPhoneDayExt();
      phone = Telephone.createTelephone(ac,ech,lcl,ext);
      if ( phone != null ) {
        memberPhones.put( Telephone.DAY, phone );
      }

      ac  = theForm.getPhoneOtherAreaCode();
      ech = theForm.getPhoneOtherExchange();
      lcl = theForm.getPhoneOtherLocal();
      ext = theForm.getPhoneOtherExt();
      phone = Telephone.createTelephone(ac,ech,lcl,ext);
      if ( phone != null ) {
        memberPhones.put( Telephone.OTHER, phone );
      }

      memberName = new MemberName( theForm.getFirstName(),
        theForm.getMiddleName(), theForm.getLastName(), theForm.getSuffix() );

      memberAddress = new Address( theForm.getStreet1(), theForm.getStreet2(),
        theForm.getCity(), theForm.getState(), theForm.getZip() );

      int memberBirthMonth = theForm.getBirthMonthInt();
      int memberBirthDay   = theForm.getBirthDayInt();
      int memberBirthYear  = theForm.getBirthYearInt();
      Calendar c = Calendar.getInstance();
      c.set(memberBirthYear, memberBirthMonth-1, memberBirthDay);
      memberBirthDate = c.getTime();

      memberEmailString = theForm.getEmail();
      if ( memberEmailString != null ) {
          memberEmail = new Email( memberEmailString );
      }

      accountName = theForm.getAccountName();
      accountNameOriginal = theForm.getAccountNameOriginal();
      isAccountNameModified =
        !accountName.equalsIgnoreCase(accountNameOriginal);

      if ( theForm.isAdminEdit() || theForm.isAdminCreate() ) {

        accountPassword = theForm.getAccountPassword();
        String strAccountType = theForm.getAccountTypeStr();
        accountType = new AccountType( strAccountType );
        accountRoles = theForm.getMemberRoles();

        int accountYear = theForm.getAccountYear();
        Calendar accountCalendar = Calendar.getInstance();
        accountCalendar.set( accountYear, 0, 1 );
        accountDate = accountCalendar.getTime();

      } // if ADMINEDIT or ADMINCREATE

    } // if retVal == null

    try {

      // Edit a restricted set of data for an existing member
      if ( retVal == null && theForm.isMemberEdit() ) {

        IMemberHome mh = MemberUtils.getMemberEJBHome();
        IMember m = mh.findByPrimaryKey(memberId);

        MemberSnapshot ms = m.getData();

        MemberSnapshot update = new MemberSnapshot(
            memberId, accountName, ms.getAccountPassword(), ms.getAccountType(),
            memberName, memberEmail, memberPhones, memberAddress,
            ms.getAccountDate(), ms.getBirthDate(), ms.getMemberRoles());
        m.setData( update );

        retVal = mapping.findForward("success");
        if ( isAccountNameModified ) {
          request.setAttribute( "accountNameModified", new Boolean(true) );
          retVal = mapping.findForward("logout");
        }

      } // end if MEMBEREDIT

      // Edit all data for an existing member
      else if ( retVal == null && theForm.isAdminEdit() ) {

        IMemberHome mh = MemberUtils.getMemberEJBHome();
        IMember m = mh.findByPrimaryKey(memberId);
        MemberSnapshot ms = new MemberSnapshot(
            memberId, accountName, accountPassword, accountType,
            memberName, memberEmail, memberPhones, memberAddress,
            accountDate, memberBirthDate, accountRoles );
        m.setData( ms );

        retVal = mapping.findForward("browse");

      } // end ADMINEDIT

      // Create a new member
      else if ( retVal == null && theForm.isAdminCreate() ) {

        Telephone evening = (Telephone)memberPhones.get(Telephone.EVENING);
        Telephone day = (Telephone) memberPhones.get( Telephone.DAY );
        Telephone other = (Telephone) memberPhones.get( Telephone.OTHER );

        IMemberHome mh = MemberUtils.getMemberEJBHome();
        IMember m = mh.create( accountName, accountPassword, accountType,
          memberName, memberEmail, evening, day, other, memberAddress,
          accountDate, memberBirthDate, accountRoles );

        retVal = mapping.findForward("success");

      } // end if ADMINCREATE

      // Handle fall-through caused by invalid function values
      else if ( retVal == null ) {
        String msg = "invalid function == '" +function+ "'";
        theLog.error( msg );
        throw new ServletException( msg );
      } // end fall-through

    }
    catch( ServletException x ) {
      theLog.debug( x.getMessage() );
      throw x;
    }
    catch( Throwable t ) {
      theLog.error( t );
      throw new ServletException( t );
    }

    // Remove the form bean if processing is complete
    if ( retVal != null && mapping.getAttribute() != null ) {

      if ("request".equals(mapping.getScope())) {
        request.removeAttribute(mapping.getAttribute());
      }
      else {
        HttpSession session = request.getSession();
        session.removeAttribute(mapping.getAttribute());
      }

    }

    return retVal;
  } // End perform
 
} // SaveMemberInfoAction

/*
 * $Log: SaveMemberInfoAction.java,v $
 * Revision 1.11  2003/02/26 03:38:46  rphall
 * Added copyright and GPL license
 *
 * Revision 1.10  2003/02/24 14:19:04  rphall
 * Fixed workflow when accountName changes
 *
 * Revision 1.9  2003/02/24 13:29:54  rphall
 * Added handling for when accountName changes
 *
 * Revision 1.8  2003/02/20 04:55:03  rphall
 * Changes rippled through from MemberSnapshot, MemberInfoForm
 *
 * Revision 1.7  2003/02/19 03:22:56  rphall
 * Fixed bug with null memberId during MemberEdit
 *
 * Revision 1.6  2003/02/18 04:28:26  rphall
 * Major revision; working for MEMBEREDIT, ADMINEDIT, ADMINCREATE
 *
 * Revision 1.5  2003/02/15 04:31:42  rphall
 * Changes connected to major revision of MemberBean
 *
 * Revision 1.4  2003/02/10 05:20:31  rphall
 * Fixed bug when day/other phone is null; cleaned up debugging
 *
 */

