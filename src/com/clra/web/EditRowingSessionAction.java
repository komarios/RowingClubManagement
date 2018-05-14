/*
 * Copyright (c) Carnegie Lake Rowing Association 2002. All rights reserved.
 * Distributed under the GPL license. See doc/COPYING.
 * $RCSfile: EditRowingSessionAction.java,v $
 * $Date: 2003/02/26 03:38:46 $
 * $Revision: 1.4 $
 */

package com.clra.web;

import com.clra.rowing.IRowingSession;
import com.clra.rowing.RowingDBRead;
import com.clra.rowing.RowingSessionSnapshot;
import com.clra.rowing.RowingUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
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
 * A workflow manager that sets up an input form which queries a user for
 * information needed to create, edit, publish, view, delete or
 * cancel a rowing session. See the related workflow manager,
 * <tt>SaveRowingSessionAction</tt>, which pulls information from the
 * input form and invokes the business logic that does the actual work
 * of creating, editing, publishing, viewing, deleting or cancelling a
 * rowing session.
 *
 * @author <a href="mailto:rphall@pluto.njcc.com">Rick Hall</a>
 * @version $Revision: 1.4 $ $Date: 2003/02/26 03:38:46 $
 * @see RowingSessionForm
 * @see SaveRowingSessionAction
 */

public final class EditRowingSessionAction extends Action {

  private final static String base = EditRowingSessionAction.class.getName();
  private final static Category theLog = Category.getInstance( base );

  // Some local aliases, to make code lines shorter
  private final static String CREATE   = RowingSessionForm.CREATE;
  private final static String ROWINGID = Constants.ROWINGSESSION_KEY;

  /**
   * Handle the workflow step in which a form is popluated with date from
   * a rowing session data.
   *
   * @param mapping The ActionMapping used to select this instance
   * @param actionForm The optional ActionForm bean for this request (if any)
   * @param request The HTTP request we are processing
   * @param response The HTTP response we are creating
   *
   * @exception IOException if an input/output error occurs
   * @exception ServletException if a servlet exception occurs
   */
  public ActionForward perform( ActionMapping mapping, ActionForm form,
    HttpServletRequest request, HttpServletResponse response )
      throws IOException, ServletException {

    // A null return value indicates that processing should continue
    ActionForward retVal = null;

    /*
     * Implementation note: there's a confusing overuse of the word
     * "action" in two different contexts:
     *
     *  -- the query parameter "action" which can take two allowed values:
     * "Submit" and "Cancel".
     *
     *  -- the form property "action" which can take six allowed values:
     * CREATE, EDIT, PUBLISH, VIEW, CANCEL and DELETE.
     *
     * What can make this particularly confusing is that the form action can
     * specify CANCEL, which means "mark a rowing session as cancelled",
     * whereas when the query action specifies "Cancel", then the
     * workflow should be cancelled without modifying or creating a
     * rowing session.
     */

    // Extract the workflow action
    HttpSession session = request.getSession();
    String action = request.getParameter("action");
    if (action == null) {
      theLog.error( "null action" );
      // FIXME add error message
      retVal = mapping.findForward("failure");
    }

    // Extract rowing id if the rowing session isn't being created
    Integer rowingId = null;
    if ( retVal == null && !action.equalsIgnoreCase(CREATE) ) {
      String strRowingId = request.getParameter(ROWINGID);
      try {
        rowingId = new Integer( strRowingId );
      }
      catch( Exception x ) {
        theLog.error( "bad rowingId", x );
        // FIXME append error messages
        retVal = mapping.findForward("failure");
      }
    }

    if ( theLog.isDebugEnabled() ) {
      String msg = "Processing action/rowingId" + action + "/" + rowingId;
      theLog.debug( msg );
    }

    // Find the rowing session or set up default properties
    RowingSessionSnapshot snapshot = null;
    if ( retVal == null ) {
      try {
        if (action.equals(CREATE)) {
          snapshot = RowingUtils.createRowingSessionDefaults();
        } else {
          IRowingSession rowingsession = null;
          rowingsession = RowingUtils.findRowingSession( rowingId );
          snapshot = rowingsession.getData();
        }
      }
      catch( Exception x ) {
        String msg = "action/rowingId == " + action + "/" + rowingId;
        // FIXME append error messages
        theLog.error( msg, x );
        rowingId = null;
        retVal = mapping.findForward("failure");
      }
    }

    // (Re-)Store the rowing id as a session attribute
    if ( rowingId == null ) {
      session.removeAttribute( ROWINGID );
    }
    else {
      session.setAttribute( ROWINGID, rowingId );
    }

    // Get the participants signed up for the session (empty by default)
    Collection participants = new ArrayList();
    if ( rowingId != null && retVal == null ) {
      try {
        participants =
          RowingDBRead.findParticipant2SnapshotsForRowingSession(rowingId);
      }
      catch( Exception x ) {
        String msg = "unable to load participants for rowingId == " + rowingId;
        theLog.warn( msg, x );
      }
    }

    // Populate the rowing session form
    if ( retVal == null ) {

      if (form == null) {
        String msg = " Creating new RowingSessionForm bean under key "
            + mapping.getAttribute();
        form = new RowingSessionForm();
        if ("request".equals(mapping.getScope())) {
          theLog.info( msg + " in 'request' scope.");
          request.setAttribute(mapping.getAttribute(), form);
        }
        else {
          theLog.info( msg + " in 'session' scope.");
          session.setAttribute(mapping.getAttribute(), form);
        }
      } // if form == null

      RowingSessionForm subform = (RowingSessionForm) form;
      subform.setAction(action);

      if ( theLog.isDebugEnabled() ) {
        theLog.debug(" Populating form from " + snapshot);
      }

      try {
        if ( theLog.isDebugEnabled() ) {
          String msg = "setting action == " + action;
          theLog.debug( msg );
          msg = "setting rowingId == " + snapshot.getId();
          theLog.debug( msg );
          msg = new FormattedDate("MM/dd/yy h:mm a",snapshot.getDate())
                .getValue();
          theLog.debug( msg );
          msg = "setting state == " + snapshot.getState().getName();
          theLog.debug( msg );
          msg = "setting level == " + snapshot.getLevel().getName();
          theLog.debug( msg );
          msg = "setting type == " + snapshot.getType().getName();
        }
        subform.setAction(action);
        subform.setRowingId( snapshot.getId() );
        subform.setDateTimeFromDate( snapshot.getDate() );
        subform.setState( snapshot.getState().getName() );
        subform.setLevel( snapshot.getLevel().getName() );
        subform.setType( snapshot.getType().getName() );

        subform.setParticipants( participants );

        // Processing is complete.
        if ( theLog.isDebugEnabled() ) {
          String msg = "Forwarding to 'success' page:" + rowingId;
          theLog.debug( msg );
        }
        retVal = mapping.findForward("success");

      }
      catch (Throwable t) {
        String msg = Text.getMessage( "rsf.populate", t.getClass().getName() );
        theLog.error( msg, t );
        throw new ServletException(msg,t);
      }

    } // End: Populate the rowing session form

    // Forward control to the appropriate page
    if ( retVal == null ) {
      throw new Error( "design error" );
    }
    if ( theLog.isDebugEnabled() ) {
      theLog.debug( "Forwarding to " + retVal.toString() );
    }

    return retVal;
  } // perform


} // EditRowingSessionAction

/*
 * $Log: EditRowingSessionAction.java,v $
 * Revision 1.4  2003/02/26 03:38:46  rphall
 * Added copyright and GPL license
 *
 * Revision 1.3  2002/03/24 01:51:28  rphall
 * Locking and participant changes
 *
 * Revision 1.2  2002/02/18 18:05:51  rphall
 * Ran dos2unix to remove ^M (carriage return) from end of lines
 *
 * Revision 1.1.1.1  2002/01/03 21:57:28  rphall
 * Initial load, 5th try, Jan-03-2002 4:57 PM
 *
 * Revision 1.6  2001/12/15 02:27:04  rphall
 * Removed unnecessary import statements
 *
 */

