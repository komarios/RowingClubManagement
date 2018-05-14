/*
 * Copyright (c) Carnegie Lake Rowing Association 2002. All rights reserved.
 * Distributed under the GPL license. See doc/COPYING.
 * $RCSfile: LogonForm.java,v $
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

import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Category;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * Form bean for the user profile page.  This form has the following fields,
 * with default values in square brackets:
 * <ul>
 * <li><b>password</b> - Entered password value
 * <li><b>username</b> - Entered username value
 * </ul>
 * From the struts documentation.
 *
 * @author Craig R. McClanahan
 *         -- original author
 * @author <a href="mailto:rphall@pluto.njcc.com">Rick Hall</a>
 *         -- adapted to CLRA
 * @version $Revision: 1.3 $ $Date: 2003/02/26 03:38:46 $
 */
public final class LogonForm extends ActionForm {

  /** The password */
  private String password = null;

  /** The username */
  private String username = null;

  /** Return the password */
  public String getPassword() {
    return (this.password);
  }

  /**
   * Set the password.
   *
   * @param password The new password
   */
  public void setPassword(String password) {
    this.password = password;
  }

  /** Return the username */
  public String getUsername() {
    return (this.username);
  }

  /**
   * Set the username.
   *
   * @param username The new username
   */
  public void setUsername(String username) {
    this.username = username;
  }

  /**
   * Reset all properties to their default values.
   *
   * @param mapping The mapping used to select this instance
   * @param request The servlet request we are processing
   */
  public void reset(ActionMapping mapping, HttpServletRequest request) {
    this.password = null;
    this.username = null;
  }

  /**
   * Validate the properties that have been set from this HTTP request,
   * and return an <code>ActionErrors</code> object that encapsulates any
   * validation errors that have been found.  If no errors are found, return
   * <code>null</code> or an <code>ActionErrors</code> object with no
   * recorded error messages.
   *
   * @param mapping The mapping used to select this instance
   * @param request The servlet request we are processing
   */
  public ActionErrors validate(ActionMapping mapping,
    HttpServletRequest request) {

System.out.println( "LogonForm.validate..." );

    ActionErrors errors = new ActionErrors();
    if ((username == null) || (username.length() < 1)) {
      errors.add("username", new ActionError("error.username.required"));
    }
    if ((password == null) || (password.length() < 1)) {
      errors.add("password", new ActionError("error.password.required"));
    }

    return errors;
  } // validate(ActionMapping,HttpServletRequest)

} // LogonForm

/*
 * $Log: LogonForm.java,v $
 * Revision 1.3  2003/02/26 03:38:46  rphall
 * Added copyright and GPL license
 *
 * Revision 1.2  2002/02/18 18:06:16  rphall
 * Ran dos2unix to remove ^M (carriage return) from end of lines
 *
 * Revision 1.1.1.1  2002/01/03 21:57:28  rphall
 * Initial load, 5th try, Jan-03-2002 4:57 PM
 *
 * Revision 1.1  2001/11/23 19:40:02  rphall
 * Major revision
 *
 */

