/*
 * Copyright (c) Carnegie Lake Rowing Association 2002. All rights reserved.
 * Distributed under the GPL license. See doc/COPYING.
 * $RCSfile: IMemberHome.java,v $
 * $Date: 2003/02/26 03:38:45 $
 * $Revision: 1.6 $
 */

package com.clra.member;

import java.util.Date;
import java.util.Collection;
import java.rmi.RemoteException;
import javax.ejb.EJBHome;
import javax.ejb.CreateException;
import javax.ejb.FinderException;

/**
 * @author <a href="mailto:donaldzhu@sympatico.ca">Angela Yue</a>
 * @version $Revision: 1.6 $ $Date: 2003/02/26 03:38:45 $
 */

public interface IMemberHome extends EJBHome {

  IMember create( String account_name,
                  String account_passwd,
                  AccountType accountType,
                  MemberName memberName,
                  Email email,
                  Telephone tel_evening,
                  Telephone tel_day,
                  Telephone tel_other,
                  Address mailingAddress,
                  Date accountYear,
                  Date birthDate,
                  MemberRole[] memberRoles
                ) throws CreateException, RemoteException;

  IMember findByPrimaryKey( Integer memberId )
      throws FinderException, RemoteException;

  IMember findByAccountName( String name )
      throws FinderException, RemoteException;

  Collection findAll()
      throws FinderException, RemoteException;

} // IMemberHome

