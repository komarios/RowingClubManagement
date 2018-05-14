/*
 * Copyright (c) Carnegie Lake Rowing Association 2002. All rights reserved.
 * Distributed under the GPL license. See doc/COPYING.
 * $RCSfile: EditParticipationAction.java,v $
 * $Date: 2003/02/26 03:38:46 $
 * $Revision: 1.4 $
 */

package com.clra.web;

import com.clra.rowing.ParticipantSnapshot;
import com.clra.rowing.RowingSessionSnapshot;
import com.clra.rowing.RowingDBRead;
import java.io.IOException;
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
 * information needed to create, edit, or view participation in a rowing
 * session. See the related workflow manager, <tt>SaveParticipationAction</tt>,
 * which pulls information from the input form and invokes the business logic
 * that does the actual work of creating, editing, or viewing rowing-session
 * participation.
 *
 * @author <a href="mailto:rphall@pluto.njcc.com">Rick Hall</a>
 * @version $Revision: 1.4 $ $Date: 2003/02/26 03:38:46 $
 * @see ParticipationForm
 * @see SaveParticipationAction
 */

public final class EditParticipationAction extends Action {

  private final static String base = EditParticipationAction.class.getName();
  private final static Category theLog = Category.getInstance( base );

  // Some local aliases, to make code lines shorter
  private final static String CREATE   = ParticipationForm.CREATE;
  private final static String EDIT     = ParticipationForm.EDIT;
  private final static String VIEW     = ParticipationForm.VIEW;
  private final static String MEMBER   = Constants.USER_KEY;
  private final static String ROWINGID = Constants.ROWINGSESSION_KEY;
  private final static String PARTICIPANTID = Constants.PARTICIPANT_KEY;

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

    // Extract the workflow action
    HttpSession session = request.getSession();
    String action = request.getParameter("action");
    if (action == null) {
      theLog.error( "null action" );
      // FIXME add error message
      retVal = mapping.findForward("failure");
    }

    Integer memberId = null;
    Integer rowingId = null;
    Integer participantId = null;

    // Extract participant id if the participant isn't being created
    if ( retVal == null && action.equalsIgnoreCase(EDIT) ) {

      String strId = request.getParameter(PARTICIPANTID);
      try {
        participantId = new Integer( strId );
      }
      catch( Exception x ) {
        theLog.error( "bad participantId", x );
        // FIXME append error messages
        retVal = mapping.findForward("failure");
      }
    
      if ( theLog.isDebugEnabled() ) {
        String msg = "Processing action/participantId"
            + action + "/" + participantId;
        theLog.debug( msg );
      }
    }

    // Otherwise use rowing id and member_id to create/find a participant
    else if ( retVal == null ) {

      String strId = request.getParameter(ROWINGID);
      try {
        rowingId = new Integer( strId );
      }
      catch( Exception x ) {
        theLog.error( "bad rowingId", x );
        // FIXME append error messages
        retVal = mapping.findForward("failure");
      }

      Object o = request.getSession().getAttribute( MEMBER );
      try {
        memberId = ((MemberView) o).getId();
      }
      catch( Exception x ) {
        theLog.error( "bad memberView", x );
        // FIXME append error messages
        retVal = mapping.findForward("failure");
      }

      if ( theLog.isDebugEnabled() ) {
        String msg = "Processing action/memberId/rowingId"
            + action + "/" + memberId + "/" + rowingId;;
        theLog.debug( msg );
      }
    }

    // Get snapshots of the current participant and rowing session
    ParticipantSnapshot ps = null;
    RowingSessionSnapshot rss = null;
    if ( retVal == null ) {
      try {
        if ( participantId != null ) {
          // FIXME assert action == EDIT
          ps = RowingDBRead.loadParticipant(participantId);
          memberId = ps.getMemberId();
          rowingId = ps.getRowingId();
        }
        else {
          ps = RowingDBRead.loadParticipant(memberId,rowingId);
          if ( ps != null ) {
            // FIXME assert action == VIEW
            participantId = ps.getParticipantId();
          }
          else {
            // FIXME assert action == CREATE
          }
        }
        rss = RowingDBRead.loadRowingSession(rowingId);
      }
      catch( Exception x ) {
        String msg = "action/memberId/rowingId/participantId == "
            + action + "/" + memberId + "/" + rowingId + "/" + participantId;
        theLog.debug( msg );
        // FIXME append error messages
        theLog.error( msg, x );
        rowingId = null;
        participantId = null;
        retVal = mapping.findForward("failure");
      }
    }

    // (Re-)Store the participant and rowing id as a session attribute
    if ( participantId == null ) {
      session.removeAttribute( PARTICIPANTID );
    }
    else {
      session.setAttribute( PARTICIPANTID, participantId );
    }
    if ( rowingId == null ) {
      session.removeAttribute( ROWINGID );
    }
    else {
      session.setAttribute( ROWINGID, rowingId );
    }

    // Get the participants signed up for the session
    Collection participants = null;
    if ( retVal == null ) {
      try {
        participants =
          RowingDBRead.findParticipant2SnapshotsForRowingSession(rowingId);
      }
      catch( Exception x ) {
        String msg = "unable to load participants for rowingId == " + rowingId;
        theLog.error( msg, x );
        // FIXME append error messages
        retVal = mapping.findForward("failure");
      }
    }

    // Populate the participation form
    if ( retVal == null ) {

      if (form == null) {
        form = new ParticipationForm();
        if ("request".equals(mapping.getScope())) {
          request.setAttribute(mapping.getAttribute(), form);
        }
        else {
          session.setAttribute(mapping.getAttribute(), form);
        }
      } // if form == null

      ParticipationForm subform = (ParticipationForm) form;

      try {

        subform.setAction(action);
        subform.setMemberId( memberId );
        subform.setRowingId( rowingId );
        subform.setParticipantId( participantId );

        subform.setDateTimeFromDate( rss.getDate() );
        subform.setState( rss.getState().getName() );
        subform.setLevel( rss.getLevel().getName() );
        subform.setType( rss.getType().getName() );

        String strSeatPreference = null;
        if ( ps != null && ps.getSeatPreference() != null ) {
          strSeatPreference = ps.getSeatPreference().getName();
        }
        subform.setSeatPreference( strSeatPreference );

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


} // EditParticipationAction

/*
 * $Log: EditParticipationAction.java,v $
 * Revision 1.4  2003/02/26 03:38:46  rphall
 * Added copyright and GPL license
 *
 * Revision 1.3  2002/03/24 01:52:30  rphall
 * Corrected comment
 *
 * Revision 1.2  2002/02/18 18:05:47  rphall
 * Ran dos2unix to remove ^M (carriage return) from end of lines
 *
 * Revision 1.1.1.1  2002/01/03 21:57:28  rphall
 * Initial load, 5th try, Jan-03-2002 4:57 PM
 *
 * Revision 1.3  2001/12/15 05:24:37  rphall
 * Fixed bug in handling View requests
 *
 * Revision 1.2  2001/12/15 02:30:52  rphall
 * Checkpt: compiles, starting debugging
 *
 */

