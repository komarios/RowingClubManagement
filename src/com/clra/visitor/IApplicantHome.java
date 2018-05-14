/*
 * Copyright (c) Carnegie Lake Rowing Association 2002. All rights reserved.
 * Distributed under the GPL license. See doc/COPYING.
 * $RCSfile: IApplicantHome.java,v $
 * $Date: 2003/02/26 03:38:46 $
 * $Revision: 1.6 $
 */

package com.clra.visitor;

import java.util.Date;
import java.util.Collection;
import java.rmi.RemoteException;
import javax.ejb.EJBHome;
import javax.ejb.CreateException;
import javax.ejb.FinderException;

/**
 * @author <a href="mailto:donaldzhu@sympatico.ca">Angela Yue</a>
 * @version $Revision: 1.6 $ $Date: 2003/02/26 03:38:46 $
 */

public interface IApplicantHome extends EJBHome {

  IApplicant create( String name_last,
                  String name_first,
                  String name_middle,
                  String name_suffix,
                  String email,
                  String tel_evening,
                  String tel_day,
                  String tel_other,
                  String address_str1,
                  String address_str2,
                  String address_city,
                  String address_state,
                  String address_zip,
                  String experience_year,
                  String recent_year,
                  Date birthday,
                  String sex,
                  Date apply_date,
                  String status
                ) throws CreateException, RemoteException, java.sql.SQLException;

  IApplicant findByPrimaryKey( String email )
      throws FinderException, RemoteException;

  Collection findAll()
      throws FinderException, RemoteException;

} // IApplicantHome

