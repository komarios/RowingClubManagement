/*
 * Copyright (c) Carnegie Lake Rowing Association 2002. All rights reserved.
 * Distributed under the GPL license. See doc/COPYING.
 * $RCSfile: LogonAction.java,v $
 * $Date: 2003/02/26 03:38:46 $
 * $Revision: 1.3 $
 */

/*
 * Copyright (c) 1999-2001 The Apache Software Foundation.  All rights
 * reserved.
 *
 * The Apache Software License, Version 1.1 (see licenses)
*/
package com.clra.web;

import com.clra.member.Authentication;
import com.clra.web.MemberView;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Locale;
import javax.servlet.RequestDispatcher;
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
import org.apache.struts.util.MessageResources;

/**
 * Implementation of <strong>Action</strong> that validates a user logon.
 * From the struts documentation example.
 *
 * @author Craig R. McClanahan
 *         -- original author
 * @author <a href="mailto:rphall@pluto.njcc.com">Rick Hall</a>
 *         -- adapted to CLRA
 * @version $Revision: 1.3 $ $Date: 2003/02/26 03:38:46 $
 */
public final class LogonAction extends Action {

  private final static String base = LogonAction.class.getName();
  private final static Category theLog = Category.getInstance( base );

  /**
   * Process the specified HTTP request, and create the corresponding HTTP
   * response (or forward to another web component that will create it).
   * Return an <code>ActionForward</code> instance describing where and how
   * control should be forwarded, or <code>null</code> if the response has
   * already been completed.
   *
   * @param mapping The ActionMapping used to select this instance
   * @param actionForm The optional ActionForm bean for this request (if any)
   * @param request The HTTP request we are processing
   * @param response The HTTP response we are creating
   *
   * @exception IOException if an input/output error occurs
   * @exception ServletException if a servlet exception occurs
   */
  public ActionForward perform(ActionMapping mapping, ActionForm form,
    HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException {

System.out.println( "LogonAction.perform..." );

    // Extract attributes we will need
    Locale locale = getLocale(request);
    MessageResources messages = getResources();

    // Validate the request parameters specified by the user
    HttpSession session = request.getSession();
    ActionErrors errors = new ActionErrors();
    String username = ((LogonForm) form).getUsername();
    String password = ((LogonForm) form).getPassword();
    Authentication authentication = null;
    try {
      authentication = new Authentication(username,password);
    }
    catch(Exception x) {
      theLog.error(x.getMessage(),x);
      errors.add(ActionErrors.GLOBAL_ERROR,
        new ActionError("error.login.exception") );
    }
    try {

      authentication.login();

      // Save our logged-in user in the session
      session.setAttribute(Constants.AUTHENTICATION_KEY, authentication);
      String msg = "LogonAction: User '" + username
        + "' logged on in session " + session.getId();
      theLog.debug( msg );
      if (servlet.getDebug() >= 1) {
        servlet.log( msg );
      }

    }
    catch(Exception x) {
      theLog.error(x.toString()); // Somewhat expected, so no stack trace
      errors.add(ActionErrors.GLOBAL_ERROR,
        new ActionError("error.login.failed") );
    }

    // Report any errors we have discovered back to the original form
    if (!errors.empty()) {
      saveErrors(request, errors);
      return (new ActionForward(mapping.getInput()));
    }

    // Remove the obsolete form bean
    if (mapping.getAttribute() != null) {
      if ("request".equals(mapping.getScope())) {
        request.removeAttribute(mapping.getAttribute());
      }
      else {
        session.removeAttribute(mapping.getAttribute());
      }
    }

    // Forward control to the specified success URI
    return (mapping.findForward("success"));

  } // perform(..)

} // ActionForward

/*
 * $Log: LogonAction.java,v $
 * Revision 1.3  2003/02/26 03:38:46  rphall
 * Added copyright and GPL license
 *
 * Revision 1.2  2002/02/18 18:06:13  rphall
 * Ran dos2unix to remove ^M (carriage return) from end of lines
 *
 * Revision 1.1.1.1  2002/01/03 21:57:28  rphall
 * Initial load, 5th try, Jan-03-2002 4:57 PM
 *
 * Revision 1.3  2001/12/11 23:38:43  rphall
 * Moved MemberView, MemberSet from member to web package
 *
 * Revision 1.2  2001/11/30 11:38:00  rphall
 * First working version, RowingSession entity bean
 *
 * Revision 1.1  2001/11/23 19:40:02  rphall
 * Major revision
 *
 */

