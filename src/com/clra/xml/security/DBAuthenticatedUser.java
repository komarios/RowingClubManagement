/*
 * Copyright (c) Carnegie Lake Rowing Association 2002. All rights reserved.
 * Based on Apache Axis org.apache.axis.security.simple.DBAuthenticatedUser
 * Distributed under the GPL license. See doc/COPYING.
 * $RCSfile: DBAuthenticatedUser.java,v $
 * $Date: 2003/03/05 02:59:31 $
 * $Revision: 1.2 $
 */

package com.clra.xml.security;

import com.clra.member.MemberRole;
import org.apache.axis.security.AuthenticatedUser;

/**
 * An implementation of AuthenticatedUser that caches a accountId as well
 * as account roles. 
 *
 * @version $Revision: 1.2 $  ($Date: 2003/03/05 02:59:31 $)
 * @author <a href="mailto:rphall@pluto.njcc.com">Rick Hall</a>
 */
public class DBAuthenticatedUser implements AuthenticatedUser {

    private final Integer accountId;
    private final String accountName;
    private final MemberRole[] accountRoles;

    public DBAuthenticatedUser(
        Integer accountId, String accountName, MemberRole[] accountRoles ) {
        this.accountId = accountId;
        this.accountName = accountName == null ? null : accountName.trim() ;
        this.accountRoles = accountRoles;

        if ( accountId == null ) {
            throw new IllegalArgumentException( "null account id" );
        }
        if ( accountName == null || accountName.trim().length() == 0 ) {
            throw new IllegalArgumentException( "invalid account name" );
        }
        if ( accountRoles == null ) {
            throw new IllegalArgumentException( "null account roles" );
        }
    }

    /**
     * Return the user's name.
     * @return a non-null, non-blank String.
     */
    public String getName() {
        return accountName;
    }

    /**
     * Return the user's account id.
     * @return a non-null Integer
     */
    public Integer getAccountId() {
        return accountId;
    }

    /**
     * Same result as <code>getName()</code>. Provided for consistency
     * with JavaBean spec.
     */
    public String getAccountName() {
        return getName();
    }
    /**
     * Return the user's roles.
     * @return a non-null, but possibly empty MemberRole array
     */
    public MemberRole[] getAccountRoles() {
        return accountRoles;
    }

    public String toString() {
      return accountName;
    }

} // DBAuthenticatedUser

/*
 * $Log: DBAuthenticatedUser.java,v $
 * Revision 1.2  2003/03/05 02:59:31  rphall
 * Overrode default method for toString()
 *
 * Revision 1.1  2003/03/05 01:21:03  rphall
 * Added security to SOAP service
 *
 */

