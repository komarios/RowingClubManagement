/*
 * Copyright (c) Carnegie Lake Rowing Association 2002. All rights reserved.
 * Based on Apache Axis org.apache.axis.handlers.DBAuthenticationHandler
 * Distributed under the GPL license. See doc/COPYING.
 * $RCSfile: DBAuthenticationHandler.java,v $
 * $Date: 2003/03/05 01:21:03 $
 * $Revision: 1.1 $
 */

package com.clra.xml.security;

import org.apache.axis.AxisFault;
import org.apache.axis.MessageContext;
import org.apache.axis.handlers.BasicHandler;
import org.apache.axis.security.AuthenticatedUser;
import org.apache.axis.security.SecurityProvider;
import org.apache.axis.utils.Messages;

import org.apache.axis.components.logger.LogFactory;
import org.apache.commons.logging.Log;

/**
 * An minor rewrite of the Axis SimpleAuthenticationHandler, which checks
 * to see if a user specified in the MessageContext is allowed to continue.
 * This code differs from the Axis code by using the DBSecurityProvider.
 *
 * @version $Revision: 1.1 $  ($Date: 2003/03/05 01:21:03 $)
 * @author Doug Davis (dug@us.ibm.com)
 * @author Sam Ruby (rubys@us.ibm.com)
 * @author <a href="mailto:rphall@pluto.njcc.com">Rick Hall</a>
 */
public class DBAuthenticationHandler extends BasicHandler {

    protected static Log log =
        LogFactory.getLog(DBAuthenticationHandler.class.getName());

    /**
     * Authenticate the user and password from the msgContext
     */
    public void invoke(MessageContext msgContext) throws AxisFault {
        if (log.isDebugEnabled()) {
            log.debug("Enter: DBAuthenticationHandler::invoke");
        }

        SecurityProvider provider =
            (SecurityProvider)msgContext.getProperty("securityProvider");
        if (provider == null) {
            provider = new DBSecurityProvider();
            msgContext.setProperty("securityProvider", provider);
        }

        if (provider != null) {
            String  userID = msgContext.getUsername();
            if (log.isDebugEnabled()) {
                log.debug( Messages.getMessage("user00", userID) );
            }

            // in order to authenticate, the user must exist
            if ( userID == null || userID.equals(""))
                throw new AxisFault( "Server.Unauthenticated",
                    Messages.getMessage("cantAuth00", userID),
                    null, null );

            String passwd = msgContext.getPassword();
            if (log.isDebugEnabled()) {
                log.debug( Messages.getMessage("password00", passwd) );
            }

            AuthenticatedUser authUser = provider.authenticate(msgContext);

            // if a password is defined, then it must match
            if ( authUser == null)
                throw new AxisFault( "Server.Unauthenticated",
                    Messages.getMessage("cantAuth01", userID),
                    null, null );

            if (log.isDebugEnabled()) {
                log.debug( Messages.getMessage("auth00", userID) );
            }

            msgContext.setProperty(MessageContext.AUTHUSER, authUser);
        }

        if (log.isDebugEnabled()) {
            log.debug("Exit: DBAuthenticationHandler::invoke");
        }
    }

} // DBAuthenticationHandler

/*
 * $Log: DBAuthenticationHandler.java,v $
 * Revision 1.1  2003/03/05 01:21:03  rphall
 * Added security to SOAP service
 *
 */

