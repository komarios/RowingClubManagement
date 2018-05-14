/*
 * Copyright (c) Carnegie Lake Rowing Association 2002. All rights reserved.
 * Distributed under the GPL license. See doc/COPYING.
 * $RCSfile: MemberUtils.java,v $
 * $Date: 2003/02/26 03:38:45 $
 * $Revision: 1.3 $
 */

package com.clra.member;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.Locale;
import java.util.Vector;
import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.EJBHome;
import javax.ejb.EJBObject;
import javax.ejb.FinderException;
import javax.ejb.Handle;
import javax.ejb.RemoveException;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;
import org.apache.log4j.Category;

/**
 * @author <a href="mailto:donaldzhu@sympatico.ca">Angela Yue</a>
 * @version $Revision: 1.3 $ $Date: 2003/02/26 03:38:45 $
 */
public final class MemberUtils {

  private final static String base = MemberUtils.class.getName();
  private final static Category theLog = Category.getInstance( base );

  private static IMemberHome _homeMember = null;

  private static IMemberHome lookupMemberEJBHome()
    throws NamingException {

    IMemberHome retVal = null;

    InitialContext jndiContext = new InitialContext();
    Object ref  = jndiContext.lookup( Configuration.MEMBER_HOME() );
    retVal = (IMemberHome)
        PortableRemoteObject.narrow (ref, IMemberHome.class);

    return retVal;
  } // lookupMemberEJBHome()

  public static IMemberHome getMemberEJBHome()
    throws NamingException {

    if ( _homeMember == null ) {
      _homeMember = lookupMemberEJBHome();
    }
    return _homeMember;
  }

  public static IMember findMemberEJB( Integer memberId )
    throws RemoteException, FinderException, NamingException {

    IMember retVal = null;
    try {
      retVal = getMemberEJBHome().findByPrimaryKey( memberId );
    }
    catch( RemoteException x ) {
      theLog.error( "MemberUtils.findMemberEJB: " + x.getMessage(), x );
      _homeMember = null;
      throw x;
    }
    catch( FinderException x ) {
      theLog.error( "MemberUtils.findMemberEJB: " + x.getMessage(), x );
      throw x;
    }
    catch( EJBException x ) {
      theLog.error( "MemberUtils.findMemberEJB: " + x.getMessage(), x );
      throw x;
    }

    return retVal;
  } // findMemberEJB(Integer)

} // MemberUtils

/*
 * Log:$
 */

