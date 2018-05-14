/*
 * Copyright (c) Carnegie Lake Rowing Association 2002. All rights reserved.
 * Distributed under the GPL license. See doc/COPYING.
 * $RCSfile: IMember.java,v $
 * $Date: 2003/02/26 03:38:45 $
 * $Revision: 1.6 $
 */

package com.clra.member;

import java.rmi.RemoteException;
import java.util.Date;
import java.util.Map;
import javax.ejb.EJBObject;

/**
 * @author <a href="mailto:donaldzhu@sympatico.ca">Angela Yue</a>
 * @author <a href="mailto:rphall@pluto.njcc.com">Rick Hall</a>
 * @version $Revision: 1.6 $ $Date: 2003/02/26 03:38:45 $
 */

public interface IMember extends EJBObject {

  public void setData(MemberSnapshot data) throws RemoteException;
  public void setAccountName(String name) throws RemoteException;
  public void setAccountPassword(String password) throws RemoteException;
  public void setAccountType(AccountType type) throws RemoteException;
  public void setMemberName(MemberName name) throws RemoteException;
  public void setEmail(Email email) throws RemoteException;
  public void setTelephones( Map telephones ) throws RemoteException;
  public void setEveningTelephone(Telephone phone) throws RemoteException;
  public void setDayTelephone(Telephone phone) throws RemoteException;
  public void setOtherTelephone(Telephone phone) throws RemoteException;
  public void setAddress(Address address) throws RemoteException;
  public void setAccountYear(Date year) throws RemoteException;
  public void setBirthDate(Date birthDate) throws RemoteException;
  public void setMemberRoles(MemberRole[] memberRoles) throws RemoteException;

  public Integer getId() throws RemoteException;
  public MemberSnapshot getData() throws RemoteException;
  public String getAccountName() throws RemoteException;
  public String getAccountPassword() throws RemoteException;
  public AccountType getAccountType() throws RemoteException;
  public MemberName getMemberName() throws RemoteException;
  public Email getEmail() throws RemoteException;
  public Map getTelephones() throws RemoteException;
  public Telephone getEveningTelephone() throws RemoteException;
  public Telephone getDayTelephone() throws RemoteException;
  public Telephone getOtherTelephone() throws RemoteException;
  public Address getAddress() throws RemoteException;
  public Date getAccountYear() throws RemoteException;
  public Date getBirthDate() throws RemoteException;
  public MemberRole[] getMemberRoles() throws RemoteException;

} // IMember

/*
 * $Log: IMember.java,v $
 * Revision 1.6  2003/02/26 03:38:45  rphall
 * Added copyright and GPL license
 *
 * Revision 1.5  2003/02/20 04:25:48  rphall
 * Rename get/setClraYear() to get/setAccountYear()
 *
 * Revision 1.4  2003/02/18 04:17:42  rphall
 * Added get/setTelephones
 *
 * Revision 1.3  2003/02/15 04:31:41  rphall
 * Changes connected to major revision of MemberBean
 *
 */

