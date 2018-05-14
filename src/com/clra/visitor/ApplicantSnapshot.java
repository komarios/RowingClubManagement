/*
 * Copyright (c) Carnegie Lake Rowing Association 2002. All rights reserved.
 * Distributed under the GPL license. See doc/COPYING.
 * $RCSfile: ApplicantSnapshot.java,v $
 * $Date: 2003/02/26 03:38:46 $
 * $Revision: 1.4 $
 */

package com.clra.visitor;

import com.clra.util.ISerializableComparator;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.Date;
import javax.ejb.EJBObject;
import javax.ejb.RemoveException;

/**
 * @author <a href="mailto:donaldzhu@sympatico.ca">Angela Yue</a>
 */
public class ApplicantSnapshot implements Comparable, Serializable {

  private String lastname = null;
  private String firstname = null;
  private String middlename = null;
  private String suffix = null;
  private String email = null;
  private String eveningtel = null;
  private String daytel = null;
  private String othertel = null;
  private String addrstr1 = null;
  private String addrstr2 = null;
  private String city = null;
  private String state = null;
  private String zip = null;
  private String experienceyear = null;
  private String recentyear = null;
  private Date birthday = null;
  private String sex = null;
  private Date applydate = null;
  private String status = null;
  private int hashCode;

  public ApplicantSnapshot(String nlast,
                  String nfirst,
                  String nmiddle,
                  String nsuffix,
                  String mail,
                  String tel_evening,
                  String tel_day,
                  String tel_other,
                  String addr_str1,
                  String addr_str2,
                  String addr_city,
                  String addr_state,
                  String addr_zip,
                  String experience_year,
                  String recent_year,
                  Date birth,
                  String sex,
                  Date apply_date,
                  String status) {

    this.lastname = nlast;
    this.firstname = nfirst;
    this.middlename = nmiddle;
    this.suffix = nsuffix;
    this.email = mail;
    this.eveningtel = tel_evening;
    this.daytel = tel_day;
    this.othertel = tel_other;
    this.addrstr1 = addr_str1;
    this.addrstr2 = addr_str2;
    this.city = addr_city;
    this.state = addr_state;
    this.zip = addr_zip;
    this.experienceyear = experience_year;
    this.recentyear = recent_year;
    this.birthday = birth;
    this.sex = sex;
    this.applydate = apply_date;
    this.status = status;
    this.hashCode = mail.hashCode();
  }

  public String getFirstname() {
  	return firstname;
  }
  
  public String getLastname() {
  	return lastname;
  }
  
  public String getMiddlename() {
  	return middlename;
  }
  
  public String getSuffix() {
  	return suffix;
  }
  
  public String getEveningtel() {
  	return eveningtel;
  }
  
  public String getDaytel() {
  	return daytel;
  }
  
  public String getOthertel() {
  	return othertel;
  }
  
  public String getEmail() {
  	return email;
  }
  
  public Date getBirthday() {
  	return birthday;
  }
  
  public String getAddrstr1() {
  	return addrstr1;
  }
  
  public String getAddrstr2() {
  	return addrstr2;
  }
  
  public String getCity() {
  	return city;
  }
  
  public String getState() {
  	return state;
  }
  
  public String getZip() {
  	return zip;
  }

  public String getExperienceyear() {
  	return experienceyear;
  }

  public String getRecentyear() {
  	return recentyear;
  }

  public String getSex() {
  	return sex;
  }

  public Date getApplydate() {
  	return applydate;
  }

  public String getStatus() {
  	return status;
  }

  public int hashCode() {
    return this.hashCode;
  }
  
  public int compareTo( Object o ) {
    return 0;
  }  

} // ApplicantSnapshot

