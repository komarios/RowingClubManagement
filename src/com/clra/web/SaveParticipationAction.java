/*
 * Copyright (c) Carnegie Lake Rowing Association 2002. All rights reserved.
 * Distributed under the GPL license. See doc/COPYING.
 * $RCSfile: SaveParticipationAction.java,v $
 * $Date: 2003/03/13 17:49:23 $
 * $Revision: 1.5 $
 */

package com.clra.web;

import com.clra.rowing.IParticipant;
import com.clra.rowing.ParticipantSnapshot;
import com.clra.rowing.RowingException;
import com.clra.rowing.RowingUtils;
import com.clra.rowing.SeatPreference;
import com.clra.util.ErrorUtils;
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
 * See the related workflow manager, <tt>EditParticipationAction</tt>, which
 * initializes an input form with data from a rowing session.
 *
 * @author <a href="mailto:rphall@pluto.njcc.com>Rick Hall</a>
 * @version $Revision: 1.5 $ $Date: 2003/03/13 17:49:23 $
 */
public final class SaveParticipationAction extends Action {

  private final static String base = SaveParticipationAction.class.getName();
  private final static Category theLog = Category.getInstance( base );

  // Some local aliases, to make code lines shorter
  private final static String CREATE = ParticipationForm.CREATE;
  private final static String EDIT   = ParticipationForm.EDIT;
  private final static String VIEW   = ParticipationForm.VIEW;
  private final static String DELETE = ParticipationForm.DELETE;
  private final static String CANCEL = ParticipationForm.CANCEL;

  private final static String MEMBER   = Constants.USER_KEY;
  private final static String ROWINGID = Constants.ROWINGSESSION_KEY;
  private final static String PARTICIPANTID = Constants.PARTICIPANT_KEY;

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
      theLog.info( "null workflow: cancelled 'SaveParticipation'" );
      // FIXME add status message
      retVal = mapping.findForward("success");
    }
    else if ( isCancelled(request) ) {
      theLog.info("'" + workflow + "' workflow: cancelled 'SaveParticipation'");
      // FIXME add status message
      retVal = mapping.findForward("success");
    }
    else {
      theLog.info( "proceeding with 'SaveParticipation'" );
      if ( retVal != null ) { throw new Error( "design error" ); }
    }

    // Extract the form action
    ParticipationForm subform = (ParticipationForm) form;
    if ( theLog.isDebugEnabled() ) {
      StringBuffer sb = new StringBuffer();
      sb.append( "ParticipationForm: action == '" + subform.getAction() + "'" );
      sb.append( ", seatPreference == '" + subform.getSeatPreference() + "'" );
      sb.append( ", rowingId == " + subform.getRowingId() );
      sb.append( ", state == '" + subform.getState() + "'" );
      sb.append( ", date == "
        + dateFormat.format(subform.getDateTimeAsDateObject()) );
      sb.append( ", level == '" + subform.getLevel() + "'" );
      sb.append( ", type == '" + subform.getType() + "'" );
      theLog.debug( new String(sb) );
    }
    String formAction = subform.getAction();
    if ( retVal == null && formAction == null ) {
      theLog.error( "null formAction" );
      // FIXME add error message
      retVal = mapping.findForward("failure");
    }

    // Get the id of the targetted member, rowing session and participant
    // FIXME : should this stuff be form data?
    Integer rowingId = null;
    Integer participantId = null;
    Integer memberId = null;
    if ( retVal == null ) {

      // Trim the (non-null) form action
      formAction = formAction.trim();

      rowingId = (Integer) session.getAttribute(ROWINGID);
      participantId = (Integer) session.getAttribute(PARTICIPANTID);
      memberId = null;
      Object o = request.getSession().getAttribute( MEMBER );
      try {
        memberId = ((MemberView) o).getId();
      }
      catch( Exception x ) {
        theLog.error( ErrorUtils.createDbgMsg( "perform", x ) ); 
      }

      if ( theLog.isDebugEnabled() ) {
        theLog.debug( "rowingId == " + rowingId );
        theLog.debug( "memberId == " + memberId );
        theLog.debug( "participantId == " + participantId );
      }

      if ( CREATE.equalsIgnoreCase(formAction) ) {
        if ( rowingId == null && memberId == null ) {
          String msg = "Missing rowingId/memberId for formAction '"
            + formAction + "'";
          theLog.error( msg );
          // response.sendError(HttpServletResponse.SC_BAD_REQUEST,
          //                messages.getMessage("error.norowingId"));
          // FIXME note that continuing with retVal==null won't work here
          // return null;
          //
          // FIXME append error messages
          retVal = mapping.findForward("failure");
        }
      }
      else {
        if ( participantId == null ) {
          String msg = "Missing participantId for formAction '"
            + formAction + "'";
          theLog.error( msg );
          // response.sendError(HttpServletResponse.SC_BAD_REQUEST,
          //                messages.getMessage("error.norowingId"));
          // FIXME note that continuing with retVal==null won't work here
          // return null;
          //
          // FIXME append error messages
          retVal = mapping.findForward("failure");
        }
      }

    } // end extract rowingId, memberId, participantId

    // Extract the seat preference
    SeatPreference sp = null;
    if ( retVal == null ) {
      String s = subform.getSeatPreference();
      try {
        sp = SeatPreference.getSeatPreference(s);
      }
      catch( Exception x ) {
        String msg = ErrorUtils.createDbgMsg( "editParticipant", x );
        theLog.error( msg, x );
        // FIXME append error messages
        retVal = mapping.findForward("failure");
      }

    }

    // Check for oddball: CREATE with null seatPreference
    if ( retVal==null && CREATE.equalsIgnoreCase(formAction) && sp==null ) {

      if ( theLog.isDebugEnabled() ) {
        String msg = "Cancelling CREATE with null seat preference";
        theLog.debug( msg );
      }

      retVal = mapping.findForward("success");
    }

    // Perform the action specified by the form
    if ( retVal == null ) {

      if ( theLog.isDebugEnabled() ) {
        String msg = "Processing formAction/rowingId/memberId/participantId: "
          + formAction + "/" + rowingId + "/" + memberId + "/" + participantId;
        theLog.debug( msg );
      }

      // All required validations were done by the form itself

      try {

        if ( CREATE.equalsIgnoreCase(formAction) ) {
          createParticipant( rowingId, memberId, sp );
          // FIXME append status messages
        }
        else if ( VIEW.equalsIgnoreCase(formAction) ) {
          // Nothing to do
        }
        else if ( EDIT.equalsIgnoreCase(formAction) ) {
          editParticipant( participantId, sp );
          // FIXME append status messages
        }
        else {
          String msg = "design error: " + formAction + "/" + rowingId
            + "/" + memberId + "/" + participantId;
          throw new Error( msg );
        }

        // Processing is complete.
        if ( theLog.isDebugEnabled() ) {
          String msg = "Forwarding to 'success' page: " + formAction
            + "/" + rowingId + "/" + memberId + "/" + participantId;
          theLog.debug( msg );
        }
        retVal = mapping.findForward("success");

      }
      catch( Exception x ) {
        String msg = ErrorUtils.createDbgMsg( "perform2", x );
        theLog.error( msg, x );
        // FIXME append error messages
        retVal = mapping.findForward("failure");
      }

    } // End: Perform the action specified by the form

    // Remove the obsolete form bean, participantId and rowingId
    if (mapping.getAttribute() != null) {
      if ("request".equals(mapping.getScope())) {
          request.removeAttribute(mapping.getAttribute());
      }
      else {
          session.removeAttribute(mapping.getAttribute());
      }
    }
    session.removeAttribute(PARTICIPANTID);
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

  private static void createParticipant( Integer rowingId, Integer memberId,
    SeatPreference sp ) throws Exception {

    if ( sp == null ) {
      String msg = "null seat preference";
      theLog.error( msg );
      throw new WebException( msg );
    }

    IParticipant ip = RowingUtils.createParticipant( memberId, rowingId, sp );

    if ( theLog.isInfoEnabled() ) {
      String msg = "participant created: id == " + ip.getParticipantId();
      theLog.info( msg );
    }

    return;
  } // createParticipant(Integer,Integer,ParticipationForm)

  private static void editParticipant( final Integer participantId,
    final SeatPreference sp ) throws Exception { 

    IParticipant ip = RowingUtils.findParticipant( participantId );

    if ( sp == null ) {
      // FIXME define a state-safe delete()
      ip.remove();
      if ( theLog.isInfoEnabled() ) {
        String msg = "rowing session removed: participantId == "
          + participantId + ", seatPreference == '" + null + "'";
        theLog.info( msg );
      }
    }
    else {
      ip.setSeatPreference( sp );
      if ( theLog.isInfoEnabled() ) {
        String msg = "rowing session edited: participantId == "
          + participantId + ", seatPreference == '" + sp.getName() + "'";
        theLog.info( msg );
      }
    }


    return;
  } // editParticipant(Integer,ParticipationForm)

} // SaveParticipationAction

/*
 * $Log: SaveParticipationAction.java,v $
 * Revision 1.5  2003/03/13 17:49:23  rphall
 * Fixed bug listed on SourceForge
 *
 * Revision 1.4  2003/02/26 03:38:46  rphall
 * Added copyright and GPL license
 *
 * Revision 1.3  2003/02/19 22:09:18  rphall
 * Removed gratuitous use of CLRA acronym
 *
 * Revision 1.2  2002/01/30 15:25:37  rphall
 * Removed System.out.println debugging
 */

