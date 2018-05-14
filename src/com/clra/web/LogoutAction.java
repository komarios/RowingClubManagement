/*
 * Copyright (c) Carnegie Lake Rowing Association 2002. All rights reserved.
 * Distributed under the GPL license. See doc/COPYING.
 * $RCSfile: LogoutAction.java,v $
 * $Date: 2003/02/26 03:38:46 $
 * $Revision: 1.2 $
 */

/*
 * Copyright (c) 1999-2001 The Apache Software Foundation.  All rights
 * reserved.
 *
 * The Apache Software License, Version 1.1 (see licenses)
*/
package com.clra.web;

import java.io.IOException;
import java.util.Locale;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Category;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionServlet;
import org.apache.struts.util.MessageResources;

/**
 * Implementation of <strong>Action</strong> that processes a
 * user logoff. From the struts documentation example.
 *
 * @author Craig R. McClanahan
 *         -- original author
 * @author <a href="mailto:rphall@pluto.njcc.com">Rick Hall</a>
 *         -- adapted to CLRA
 * @version $Revision: 1.2 $ $Date: 2003/02/26 03:38:46 $
 */
public final class LogoutAction extends Action {

  private final static String base = LogoutAction.class.getName();
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

System.out.println( "LogoutAction.perform..." );

    // Extract attributes we will need
    Locale locale = getLocale(request);
    MessageResources messages = getResources();
    HttpSession session = request.getSession();

    // Process this user logoff
    session.removeAttribute(Constants.USER_KEY);
    session.invalidate();

    // Forward control to the specified success URI
    return (mapping.findForward("success"));

  }

}

/*
 * $Log: LogoutAction.java,v $
 * Revision 1.2  2003/02/26 03:38:46  rphall
 * Added copyright and GPL license
 *
 * Revision 1.1  2002/02/23 01:01:24  rphall
 * Working
 *
 * Revision 1.2  2002/02/18 18:06:10  rphall
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

