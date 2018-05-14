/*
 * Copyright (c) Carnegie Lake Rowing Association 2002. All rights reserved.
 * Distributed under the GPL license. See doc/COPYING.
 * $RCSfile: SaveRowingSessionAction.java,v $
 * $Date: 2003/02/26 03:38:46 $
 * $Revision: 1.4 $
 */

package com.clra.web;

import com.clra.rowing.IRowingSession;
import com.clra.rowing.RowingException;
import com.clra.rowing.RowingSessionLevel;
import com.clra.rowing.RowingSessionState;
import com.clra.rowing.RowingSessionStateException;
import com.clra.rowing.RowingSessionType;
import com.clra.rowing.RowingUtils;
import java.io.IOException;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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

/**
 * A workflow manager that pulls data about a rowing session from
 * an input form. The data is used to invoke business logic that
 * creates, edits, publishes, views, deletes or cancels a rowing session.
 * See the related workflow manager, <tt>EditRowingSessionAction</tt>, which
 * initializes an input form with data from a rowing session.
 *
 * @author <a href="mailto:rphall@pluto.njcc.com>Rick Hall</a>
 * @version $Revision: 1.4 $ $Date: 2003/02/26 03:38:46 $
 */
public final class SaveRowingSessionAction extends Action {

  private final static String base = SaveRowingSessionAction.class.getName();
  private final static Category theLog = Category.getInstance( base );

  // Some local aliases, to make code lines shorter
  private final static String CREATE        = RowingSessionForm.CREATE;
  private final static String EDIT          = RowingSessionForm.EDIT;
  private final static String PUBLISH       = RowingSessionForm.PUBLISH;
  private final static String LOCK          = RowingSessionForm.LOCK;
  private final static String VIEW          = RowingSessionForm.VIEW;
  private final static String SESSIONCANCEL = RowingSessionForm.SESSIONCANCEL;
  private final static String DELETE        = RowingSessionForm.DELETE;
  private final static String CANCEL        = RowingSessionForm.CANCEL;
  private final static String ROWINGID      = Constants.ROWINGSESSION_KEY;

  // Used to format debug and log messages
  private final static SimpleDateFormat dateFormat =
    new SimpleDateFormat("MM/dd/yy h:mm a");

  /**
   * Handle the workflow step in data about a rowing session is pulled
   * from an input form. The data is used to invoke business logic that
   * creates, edits, publishes, views, deletes or cancels a rowing session.
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

    // A null return value indicates that processing should continue
    ActionForward retVal = null;

    // Extract the workflow
    HttpSession session = request.getSession();
    String workflow = request.getParameter("action");
    if (workflow == null) {
      // The workflow is cancelled by default
      theLog.info( "null workflow: cancelled 'SaveRowingSession'" );
      // FIXME add status message
      retVal = mapping.findForward("success");
    }
    else if ( isCancelled(request) ) {
      theLog.info("'" + workflow + "' workflow: cancelled 'SaveRowingSession'");
      // FIXME add status message
      retVal = mapping.findForward("success");
    }
    else {
      theLog.info( "proceeding with 'SaveRowingSession'" );
      if ( retVal != null ) { throw new Error( "design error" ); }
    }

    // Extract the form action
    RowingSessionForm subform = (RowingSessionForm) form;
    if ( theLog.isDebugEnabled() ) {
      StringBuffer sb = new StringBuffer();
      sb.append( "RowingSessionForm: action == " + subform.getAction() );
      sb.append( ", rowingId == " + subform.getRowingId() );
      sb.append( ", state == " + subform.getState() );
      sb.append( ", date == "
        + dateFormat.format(subform.getDateTimeAsDateObject()) );
      sb.append( ", level == " + subform.getLevel() );
      sb.append( ", type == " + subform.getType() );
      theLog.debug( new String(sb) );
    }
    String formAction = subform.getAction();
    if ( retVal == null && formAction == null ) {
      theLog.error( "null formAction" );
      // FIXME add error message
      retVal = mapping.findForward("failure");
    }

    // Perform the action specified by the form
    if ( retVal == null && formAction != null ) {

      // Trim the (non-null) form action
      formAction = formAction.trim();

      // Get the id of the targetted rowing session
      Integer rowingId = (Integer) session.getAttribute(ROWINGID);
      if ( rowingId == null && !CREATE.equalsIgnoreCase(formAction) ) {
        String msg = "Missing rowing id for formAction '" + formAction + "'";
        theLog.error( msg );
        // response.sendError(HttpServletResponse.SC_BAD_REQUEST,
        //                messages.getMessage("error.norowingId"));
        // FIXME note that continuing with retVal==null won't work here
        // return null;
        //
        // FIXME append error messages
        retVal = mapping.findForward("failure");
      }

      if ( theLog.isDebugEnabled() ) {
        String msg = "Processing formAction/rowingId"
                      + formAction + "/" + rowingId;
        theLog.debug( msg );
      }

      // All required validations were done by the form itself

      try {

        if ( CREATE.equalsIgnoreCase(formAction) ) {
          rowingId = createSession( subform );
          // FIXME append status messages
        }
        else if ( VIEW.equalsIgnoreCase(formAction) ) {
          // Nothing to do
        }
        else { // (not CREATE, not VIEW)

          IRowingSession irs = RowingUtils.findRowingSession( rowingId );

          if ( EDIT.equalsIgnoreCase(formAction) ) {
            editSession( irs, subform );
          }
          else if ( PUBLISH.equalsIgnoreCase(formAction) ) {
            irs.publish();
          }
          else if ( SESSIONCANCEL.equalsIgnoreCase(formAction) ) {
            irs.cancel();
          }
          else if ( DELETE.equalsIgnoreCase(formAction) ) {
            irs.delete();
          }
          else if ( LOCK.equalsIgnoreCase(formAction) ) {
            irs.lock();
          }
          else { // unknown
            throw new Error( "design error: formAction == " + formAction );
          }

          // FIXME append status messages

        } // end else (not CREATE, not VIEW)

        // FIXME et the month attribute so that relevant data is displayed
        // MonthViewSelectorTag.resetMonthInContexts( request, month );
        // FIXME set the year attribute

        // Processing is complete.
        if ( theLog.isDebugEnabled() ) {
          String msg = "Forwarding to 'success' page:" + rowingId;
          theLog.debug( msg );
        }
        retVal = mapping.findForward("success");

      }
      catch( Exception x ) {
        theLog.error( x.getMessage(), x );
        // FIXME append error messages
        retVal = mapping.findForward("failure");
      }

    } // End: Perform the action specified by the form

    // Remove the obsolete form bean and rowingId
    if (mapping.getAttribute() != null) {
      if ("request".equals(mapping.getScope())) {
          request.removeAttribute(mapping.getAttribute());
      }
      else {
          session.removeAttribute(mapping.getAttribute());
      }
    }
    session.removeAttribute(ROWINGID);

    // Forward control to the appropriate page
    if ( retVal == null ) {
      String msg = "null retVal for workflow/formAction == '" + workflow
        + "'/'" + formAction + "'";
      throw new Error( msg );
    }
    if ( theLog.isDebugEnabled() ) {
      theLog.debug( "Forwarding to " + retVal.toString() );
    }

    return retVal;
  } // perform

  private static Integer createSession( RowingSessionForm form )
    throws RemoteException, CreateException, NamingException, RowingException {

    Date date = form.getCalendar().getTime();

    String s = form.getLevel();
    RowingSessionLevel level = RowingSessionLevel.getLevel( s );

    s = form.getType();
    RowingSessionType type = RowingSessionType.getType( s );

    IRowingSession irs = RowingUtils.createRowingSession( date, level, type );
    Integer retVal = irs.getId();
    if ( theLog.isInfoEnabled() ) {
      String msg = "rowing session created: id == " + retVal
        + ", date == '" + dateFormat.format( date )
        + "', level == " + level.getName() + ", type == " + type.getName();
      theLog.info( msg );
    }

    return retVal;
  } // createSession(RowingSessionForm)

  private static void editSession( IRowingSession irs, RowingSessionForm form )
    throws RemoteException, RowingSessionStateException, RowingException { 

    Date date = form.getCalendar().getTime();
    irs.setDate( date );

    String s = form.getLevel();
    RowingSessionLevel level = RowingSessionLevel.getLevel( s );
    irs.setLevel( level );

    s = form.getType();
    RowingSessionType type = RowingSessionType.getType( s );
    irs.setType( type );

    if ( theLog.isInfoEnabled() ) {
      String msg = "rowing session edited: id == " + irs.getId()
        + ", date == '" + dateFormat.format( date )
        + "', level == " + level.getName() + ", type == " + type.getName();
      theLog.info( msg );
    }

    return;
  } // editSession(Integer,RowingSessionForm)

} // SaveRowingSessionAction

/*
 * $Log: SaveRowingSessionAction.java,v $
 * Revision 1.4  2003/02/26 03:38:46  rphall
 * Added copyright and GPL license
 *
 * Revision 1.3  2002/03/24 01:51:28  rphall
 * Locking and participant changes
 *
 * Revision 1.2  2002/02/18 18:07:16  rphall
 * Ran dos2unix to remove ^M (carriage return) from end of lines
 *
 * Revision 1.1.1.1  2002/01/03 21:57:28  rphall
 * Initial load, 5th try, Jan-03-2002 4:57 PM
 *
 * Revision 1.10  2001/12/17 00:41:45  rphall
 * Documentation and whitespace
 *
 * Revision 1.9  2001/12/15 02:27:04  rphall
 * Removed unnecessary import statements
 *
 * Revision 1.8  2001/12/10 21:44:06  rphall
 * Fixed cancellation bug
 *
 * Revision 1.7  2001/12/09 20:28:50  rphall
 * Added fixme related to workflow
 *
 * Revision 1.6  2001/12/04 02:47:13  rphall
 * Added logging. Fixed processing logic. Beginning to work.
 * Some documentation changes.  Some whitespace tinkering.
 *
 * Revision 1.5  2001/12/03 17:52:24  rphall
 * Checkpt: before revision to workflow/formAction logic
 *
 * Revision 1.3  2001/12/03 02:29:26  rphall
 * Checkpt: some stubbing remains
 *
 * Revision 1.2  2001/12/02 22:09:44  rphall
 * Checkpt: started replacing stub with valid implementation
 *
 */

