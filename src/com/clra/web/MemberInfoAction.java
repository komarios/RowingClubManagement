/*
 * Copyright (c) Carnegie Lake Rowing Association 2002. All rights reserved.
 * Distributed under the GPL license. See doc/COPYING.
 * $RCSfile: MemberInfoAction.java,v $
 * $Date: 2003/02/26 03:38:46 $
 * $Revision: 1.2 $
 */

package com.clra.web;

/**
 * A workflow manager that sets up an input form 
 * for a member's personal info
 * @author <a href="mailto:jmstone@nerc.com">Jan Stone</a>
 */

import com.clra.web.MemberView;
import com.clra.web.MemberTag;
import com.clra.member.MemberName;
import com.clra.util.ErrorUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
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

public final class MemberInfoAction extends Action {

  private final static String base = MemberInfoAction.class.getName();
  private final static Category theLog = Category.getInstance( base );

  /**
   * Handle the workflow step in which a form is popluated with date from
   * a member's data.
   **/
  public ActionForward perform( ActionMapping mapping, ActionForm form,
    HttpServletRequest request, HttpServletResponse response )
      throws IOException, ServletException {

    // A null return value indicates that processing should continue
    ActionForward retVal = null;

    // Extract the workflow action
    HttpSession session = request.getSession();
    String action = request.getParameter("action");
    if (action == null) {
      theLog.error( "null action" );
      // FIXME add error message
      retVal = mapping.findForward("failure");
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

    return retVal;
  }
 
}
