/*
 * Copyright (c) Carnegie Lake Rowing Association 2002. All rights reserved.
 * Distributed under the GPL license. See doc/COPYING.
 * $RCSfile: DBSecurityProvider.java,v $
 * $Date: 2003/03/05 03:04:00 $
 * $Revision: 1.3 $
 */

package com.clra.xml.security;

import com.clra.member.MemberDBRead;
import com.clra.member.MemberRole;
import com.clra.member.MemberSnapshot;

import javax.ejb.NoSuchEntityException;

import org.apache.axis.MessageContext;
import org.apache.axis.components.logger.LogFactory;
import org.apache.axis.security.AuthenticatedUser;
import org.apache.axis.security.SecurityProvider;
import org.apache.commons.logging.Log;

/**
 * An implementation of SecurityProvider which authentiates a user against
 * a JAAS-like table of users and roles in a database.
 *
 * @version $Revision: 1.3 $  ($Date: 2003/03/05 03:04:00 $)
 * @author <a href="mailto:rphall@pluto.njcc.com">Rick Hall</a>
 */
public class DBSecurityProvider implements SecurityProvider {

    protected static Log log =
        LogFactory.getLog(DBSecurityProvider.class.getName());

    /** Authenticate a user from a username/password pair.
     *
     * @param username the user name to check
     * @param password the password to check
     * @return an AuthenticatedUser or null
     */
    public AuthenticatedUser authenticate(MessageContext msgContext) {

        AuthenticatedUser retVal = null;

        String username = msgContext.getUsername();
        String password = msgContext.getPassword();
        if ( log.isDebugEnabled() ) {
            log.debug( "username == '" + username + "'" );
            log.debug( "password == '" + password + "'" );
        }

        MemberSnapshot ms = null;
        if ( username == null || username.trim().length() == 0 ) {
            log.debug( "invalid user == '" + username + "'" );
        }
        else {

            username = username.trim();
            try {
                ms = MemberDBRead.findMemberByAccountName(username);
            }
            catch( NoSuchEntityException x ) {
                // No such user
                log.debug( "unknown user == '" + username + "'" );
            }
            catch( Throwable t ) {
                String msg = "unexpected: " + t.getClass().getName()
                    + t.getMessage();
                log.error( msg );
                throw new Error( msg );
            }

        }

        if ( ms != null ) {
            if ( password == null || password.trim().length() == 0 ) {
                log.debug( "invalid user == '" + password + "'" );
            }
            else {

            password = password.trim();
            if ( !password.equals(ms.getAccountPassword()) ) {
                log.debug( "incorrect password == '" + password + "'" );
            }

            retVal = new DBAuthenticatedUser(
                ms.getId(), ms.getAccountName(), ms.getMemberRoles() );

            }
        }

        return retVal;
    } // authenticate(MessageContext)

    /**
     * See if a user matches a principal name.  The name might be a user
     * or a group.
     *
     * @return true if the user matches the passed name
     */
    public boolean userMatches(AuthenticatedUser user, String principal) {

        if ( log.isDebugEnabled() ) {
            log.debug( "user == '" + user + "'" );
            log.debug( "principal == '" + principal + "'" );
        }

        boolean retVal = false;
        if ( principal != null ) {

            MemberRole[] memberRoles = new MemberRole[0];
            if ( user instanceof DBAuthenticatedUser ) {
                memberRoles = ((DBAuthenticatedUser)user).getAccountRoles();
            }
            else if ( user!=null && user.getName()!=null ) {
                String username = user.getName().trim();
                try {
                    MemberSnapshot ms =
                        MemberDBRead.findMemberByAccountName(username);
                    memberRoles = ms.getMemberRoles();
                }
                catch( NoSuchEntityException x ) {
                    // No such user
                    log.debug( "unknown user == '" + username + "'" );
                }
                catch( Throwable t ) {
                    String msg = "unexpected: " + t.getClass().getName()
                        + t.getMessage();
                    log.error( msg );
                    throw new Error( msg );
                }
            }

            for ( int i=0; !retVal && i<memberRoles.length; i++ ) {
                String role = memberRoles[i].getRole();
                retVal = role.compareToIgnoreCase(principal) == 0;
            }

        } // if principal

        return retVal;
    } // userMatches(AuthenticatedUser,String)

} // DBSecurityProvider

/*
 * $Log: DBSecurityProvider.java,v $
 * Revision 1.3  2003/03/05 03:04:00  rphall
 * Removed System.out.println statements
 *
 * Revision 1.2  2003/03/05 02:58:43  rphall
 * Fixed a bug in userMatches(..)
 *
 * Revision 1.1  2003/03/05 01:21:03  rphall
 * Added security to SOAP service
 *
 */

