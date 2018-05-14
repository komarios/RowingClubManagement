/*
 * Copyright (c) Carnegie Lake Rowing Association 2002. All rights reserved.
 * Distributed under the GPL license. See doc/COPYING.
 * $RCSfile: ApplyMemberShipAction.java,v $
 * $Date: 2003/02/26 03:38:46 $
 * $Revision: 1.7 $
 */

package com.clra.web;

import com.clra.visitor.IApplicantHome;
import com.clra.visitor.Configuration;
import com.clra.util.MailHelper;
import java.io.IOException;
import java.util.Collection;
import java.util.Calendar;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletResponse;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;
import javax.ejb.ObjectNotFoundException;
import org.apache.log4j.Category;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author <a href="mailto:donaldzhu@sympatico.ca">Angela Yue</a>
 * @version $Revision: 1.7 $ $Date: 2003/02/26 03:38:46 $
 */

public final class ApplyMemberShipAction extends Action {

  private final static String base = ApplyMemberShipAction.class.getName();
  private final static Category theLog = Category.getInstance( base );

  public ActionForward perform( ActionMapping mapping, ActionForm form,
    HttpServletRequest request, HttpServletResponse response )
      throws IOException, ServletException {

    // A null return value indicates that processing should continue
    ActionForward retVal = null;

    ApplyMemberShipForm regform = (ApplyMemberShipForm)form;

    IApplicantHome mHome = null;
    int id = -1;

    try {
      InitialContext jndiContext = new InitialContext();
      Object ref  = jndiContext.lookup( Configuration.APPLYMEMBERSHIP_HOME() );
      mHome = (IApplicantHome)
        PortableRemoteObject.narrow (ref, IApplicantHome.class);
    } catch (Exception ex) {
      theLog.error("Exception in perform: " + ex);
      retVal = mapping.findForward("failure");
      return retVal;
    }

    String birthday = regform.getBirthday_day() + "-" + regform.getBirthday_month() + "-" + regform.getBirthday_year();
    theLog.info("birthday = " + birthday);

    Calendar c = Calendar.getInstance();
    c.set(regform.getBirthday_year(), regform.getBirthday_month() - 1, regform.getBirthday_day());

    theLog.info("last name = '" + regform.getName_last() + "'");
    theLog.info("first name = " + regform.getName_first());
    theLog.info("middle name = " + regform.getName_middle());
    theLog.info("suffix name = " + regform.getName_suffix());
    theLog.info("tel evening = " + regform.getTel_evening());
    theLog.info("tel day = " + regform.getTel_day());
    theLog.info("street1 = " + regform.getAddress_street1());
    theLog.info("city = " + regform.getAddress_city());
    theLog.info("experience = " + regform.getExperience_year());
    theLog.info("recent = " + regform.getRecent_year());

    try {
        mHome.create(regform.getName_last(),
                     regform.getName_first(), regform.getName_middle(),
                     regform.getName_suffix(), regform.getEmail(),
                     regform.getTel_evening(), regform.getTel_day(),
                     regform.getTel_other(), regform.getAddress_street1(),
                     regform.getAddress_street2(), regform.getAddress_city(),
                     regform.getAddress_state(), regform.getAddress_zip(),
                     regform.getExperience_year(), regform.getRecent_year(), 
                     c.getTime(), regform.getSex(), null, "waiting");

        MailHelper mh = new MailHelper();
        String content = "<html><body>Apply for Membership";

        // just a test, the following link should be changed
        content = "<a href=\"http:\\\\marsogame.world\\clra-test\" target=_blank>CLRA</a></body></html>";

        mh.createAndSendMail("Apply for Membership", content, null);

        content = "<html><body>Your application is received. Thanks! <p> To learn more about the club, click " +
                  "<a href=\"http:\\\\marsogames.world\\clra-test\" target=_blank>CLRA</a></body></html>";
                  
        mh.createAndSendMail(regform.getEmail(), "Confirmation", content, null);

        request.setAttribute("firstname", regform.getName_first());
        retVal = mapping.findForward("success");
    } catch (Exception ex) {
        theLog.error("Exception : " + ex);
        retVal = mapping.findForward("failure");
    }

    return retVal;
  } // perform


} // MemberRegisterAction

/*
 * $Log: ApplyMemberShipAction.java,v $
 * Revision 1.7  2003/02/26 03:38:46  rphall
 * Added copyright and GPL license
 *
 * Revision 1.6  2003/02/19 22:30:31  rphall
 * Removed gratuitous use of CLRA acronym
 *
 */

