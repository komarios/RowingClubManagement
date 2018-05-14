/*
 * Copyright (c) Carnegie Lake Rowing Association 2002. All rights reserved.
 * Based on Apache Axis org.apache.axis.handlers.DBAuthorizationHandler
 * Distributed under the GPL license. See doc/COPYING.
 * $RCSfile: DBAuthorizationHandler.java,v $
 * $Date: 2003/03/05 01:21:03 $
 * $Revision: 1.1 $
 */

package com.clra.xml.security;

import org.apache.axis.AxisFault;
import org.apache.axis.Handler;
import org.apache.axis.MessageContext;
import org.apache.axis.handlers.BasicHandler;
import org.apache.axis.security.AuthenticatedUser;
import org.apache.axis.security.SecurityProvider;
import org.apache.axis.utils.JavaUtils;
import org.apache.axis.utils.Messages;

import org.apache.axis.components.logger.LogFactory;
import org.apache.commons.logging.Log;

import java.util.StringTokenizer;


/**
 * A renamed, but otherwise identical, version of the Axis
 * SimpleAuthorizationHandler, which checks to see if a user specified in
 * the MessageContext is allowed to perform the requested action. The main
 * value of this class is to put the Axis code under CLRA version control,
 * in case the Axis version disappears or changes in some incompatible way
 * (which is, admittedly, very unlikely).
 *
 * @version $Revision: 1.1 $  ($Date: 2003/03/05 01:21:03 $)
 * @author Doug Davis (dug@us.ibm.com)
 * @author Sam Ruby (rubys@us.ibm.com)
 * @author <a href="mailto:rphall@pluto.njcc.com">Rick Hall</a>
 */
public class DBAuthorizationHandler extends BasicHandler {

    protected static Log log =
        LogFactory.getLog(DBAuthorizationHandler.class.getName());

    /**
     * Authorize the user and targetService from the msgContext
     */
    public void invoke(MessageContext msgContext) throws AxisFault {

        if (log.isDebugEnabled()) {
            log.debug("Enter: DBAuthorizationHandler::invoke");
        }

        boolean allowByDefault =
            JavaUtils.isTrueExplicitly(getOption("allowByDefault"));

        AuthenticatedUser user = (AuthenticatedUser)msgContext.
                                         getProperty(MessageContext.AUTHUSER);

        if (user == null)
            throw new AxisFault("Server.NoUser",
                    Messages.getMessage("needUser00"), null, null);

        String userID = user.getName();
        Handler serviceHandler = msgContext.getService();

        if (serviceHandler == null)
            throw new AxisFault(Messages.getMessage("needService00"));

        String serviceName = serviceHandler.getName();

        String allowedRoles = (String)serviceHandler.getOption("allowedRoles");
        if (allowedRoles == null) {
            if (allowByDefault) {
                if (log.isDebugEnabled()) {
                    log.debug(Messages.getMessage( "noRoles00"));
                }
            }
            else {
                if (log.isDebugEnabled()) {
                    log.debug(Messages.getMessage( "noRoles01"));
                }

                throw new AxisFault( "Server.Unauthorized",
                    Messages.getMessage("notAuth00", userID, serviceName),
                    null, null );
            }

            if (log.isDebugEnabled()) {
                log.debug("Exit: DBAuthorizationHandler::invoke");
            }
            return;
        }

        SecurityProvider provider =
            (SecurityProvider)msgContext.getProperty("securityProvider");
        if (provider == null)
            throw new AxisFault(Messages.getMessage("noSecurity00"));

        StringTokenizer st = new StringTokenizer(allowedRoles, ",");
        while (st.hasMoreTokens()) {
            String thisRole = st.nextToken();
            if (provider.userMatches(user, thisRole)) {

                if (log.isDebugEnabled()) {
                    log.debug(Messages.getMessage("auth01", 
                        userID, serviceName));
                }

                if (log.isDebugEnabled()) {
                    log.debug("Exit: DBAuthorizationHandler::invoke");
                }
                return;
            }
        }

        throw new AxisFault( "Server.Unauthorized",
            Messages.getMessage("cantAuth02", userID, serviceName),
            null, null );
    }

    /**
     * Nothing to undo
     */
    public void undo(MessageContext msgContext) {
        if (log.isDebugEnabled()) {
            log.debug("Enter: DBAuthorizationHandler::undo");
            log.debug("Exit: DBAuthorizationHandler::undo");
        }
    }

} // DBAuthorizationHandler

/*
 * $Log: DBAuthorizationHandler.java,v $
 * Revision 1.1  2003/03/05 01:21:03  rphall
 * Added security to SOAP service
 *
 */

