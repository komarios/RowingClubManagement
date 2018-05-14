/*
 * Copyright (c) Carnegie Lake Rowing Association 2002. All rights reserved.
 * Distributed under the GPL license. See doc/COPYING.
 * $RCSfile: Authentication.java,v $
 * $Date: 2003/02/26 03:38:45 $
 * $Revision: 1.4 $
 */

package com.clra.member;

import java.security.Principal;
import javax.security.auth.Subject;
import java.io.IOException;
import java.io.Serializable;
import java.util.Iterator;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import org.apache.log4j.Category;
import org.jboss.security.SecurityAssociation;

/**
 * Authenticates a user during login, and releases the authentication
 * during logout.<p>
 *
 * Note: This class is NOT secure, because it stores passwords in plain
 * text. This isn't an issue unless the rowing association starts
 * doing e-commerce. The plain-text issue is important when web objects
 * use Authentication instances, because they typically stick them into
 * HttpSessions, where they can be deserialized to any old location.<p>
 *
 * @version $Id: Authentication.java,v 1.4 2003/02/26 03:38:45 rphall Exp $
 * @author <a href="mailto:rphall@pluto.njcc.com">Rick Hall</a>
 */
public class Authentication implements CallbackHandler, Serializable {

  private final static String base = Authentication.class.getName();
  private final static Category theLog = Category.getInstance( base );

  private final String user;
  private final String password;
  private LoginContext ctx = null; // Should be transient?

  public Authentication( String user, String password ) {

    theLog.debug( "user == '" + user + "'" );
    theLog.debug( "password == '" + password + "'" );

    // Assign blank finals
    this.user = user;
    this.password = password;

    // Enforce preconditions
    if ( user == null || user.trim().length() == 0 ) {
      throw new IllegalArgumentException( "invalid user" );
    }
    if ( password == null || password.trim().length() == 0 ) {
      throw new IllegalArgumentException( "invalid password" );
    }

  } // ctor(String,String)

  public void login() throws LoginException {

    if ( theLog.isDebugEnabled() ) {
      theLog.debug( "login '" + this.user + "'" );
      Principal p = SecurityAssociation.getPrincipal();
      String msg = ( p==null ? null : p.getName() );
      theLog.debug( this.user + ": preSAPrincip == '" + msg + "'" );
      Object c = SecurityAssociation.getCredential();
      msg = ( c==null ? null : c.toString() );
      theLog.debug( this.user + ": preSACredent == '" + msg + "'" );
    }
    if ( this.ctx == null ) {
      this.ctx = new LoginContext( Configuration.LOGIN_CONTEXT, this );
    }
    this.ctx.login();

    if ( theLog.isDebugEnabled() ) {
      theLog.debug( "login '" + this.user + "'" );
      Principal p = SecurityAssociation.getPrincipal();
      String msg = ( p==null ? null : p.getName() );
      theLog.debug( this.user + ": postSAPrincip == '" + msg + "'" );
      Object c = SecurityAssociation.getCredential();
      msg = ( c==null ? null : c.toString() );
      theLog.debug( this.user + ": postSACredent == '" + msg + "'" );
    }
    if ( theLog.isDebugEnabled() ) {
      Subject s = this.ctx.getSubject();
      theLog.debug( this.user + ": Subject readonly == " + s.isReadOnly() );
      theLog.debug( this.user + ": Subject == '" + s.toString() + "'" );
      Iterator iter = s.getPrincipals().iterator();
      while( iter.hasNext() ) {
        Principal p = (Principal) iter.next();
        theLog.debug( this.user + ": Principal == '" + p.getName() + "'" );
      }
      iter = s.getPublicCredentials().iterator();
      while( iter.hasNext() ) {
        Object c = iter.next();
        String info = c.getClass().getName() + "(" + c.toString() + ")";
        theLog.debug( this.user + ": PublicCredential == " + info );
      }
      iter = s.getPrivateCredentials().iterator();
      while( iter.hasNext() ) {
        Object c = iter.next();
        String info = c.getClass().getName() + "(" + c.toString() + ")";
        theLog.debug( this.user + ": PrivateCredential == " + info );
      }
    }

  } // login()

  public void logout() throws LoginException {
    theLog.debug( "logout '" + this.user + "'" );
    if ( this.ctx != null ) {
      this.ctx.logout();
    }
    this.ctx = null;
  } // logout()

  protected void finalize() {
    try {
      logout();
    }
    catch( Exception x ) {
      this.ctx = null;
    }
  } // finalize()

  public void handle( Callback[] callbacks )
    throws IOException, UnsupportedCallbackException {

    for (int i = 0; i < callbacks.length; i++) {

      if (callbacks[i] instanceof NameCallback) {
        theLog.debug( "name Callback for '" + this.user + "'" );
        NameCallback nc = (NameCallback)callbacks[i];
        nc.setName( this.user );
      }
      else if (callbacks[i] instanceof PasswordCallback) {
        theLog.debug( "password Callback for '" + this.user + "'" );
        PasswordCallback pc = (PasswordCallback)callbacks[i];
        pc.setPassword( this.password.toCharArray() );
      }
      else {
        theLog.debug( "unexpected Callback for '" + this.user + "'" );
        String msg = "Unrecognized callback == " + callbacks[i].toString();
        throw new UnsupportedCallbackException( callbacks[i], msg );
      }

    } // for

    return;
  } // handle(Callback[])

} // Authentication

/*
 * $Log: Authentication.java,v $
 * Revision 1.4  2003/02/26 03:38:45  rphall
 * Added copyright and GPL license
 *
 * Revision 1.3  2003/02/19 22:23:02  rphall
 * Removed gratuitous use of CLRA acronym
 *
 * Revision 1.2  2002/02/18 18:03:10  rphall
 * Ran dos2unix to remove ^M (carriage return) from end of lines
 *
 * Revision 1.1.1.1  2002/01/03 21:57:28  rphall
 * Initial load, 5th try, Jan-03-2002 4:57 PM
 *
 * Revision 1.1  2001/11/28 12:14:06  rphall
 * Authenticates a user during login
 *
 */

