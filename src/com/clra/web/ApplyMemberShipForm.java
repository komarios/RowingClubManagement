/*
 * Copyright (c) Carnegie Lake Rowing Association 2002. All rights reserved.
 * Distributed under the GPL license. See doc/COPYING.
 * $RCSfile: ApplyMemberShipForm.java,v $
 * $Date: 2003/02/26 03:38:46 $
 * $Revision: 1.4 $
 */

package com.clra.web;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Category;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * @author <a href="mailto:donaldzhu@sympatico.ca">Angela Yue</a>
 * @version $Revision: 1.4 $ $Date: 2003/02/26 03:38:46 $
 */

public final class ApplyMemberShipForm extends ActionForm  {

    private final static String base = ApplyMemberShipForm.class.getName();
    private final static Category theLog = Category.getInstance( base );

    private String name_last = null;
    private String name_first = null;
    private String name_middle = null;
    private String name_suffix = null;
    private String email = null;
    private String password = null;
    private String confirm_password = null;
    private String tel_evening = null;
    private String tel_day = null;
    private String tel_other = null;
    private String tel_evening_areacode = null;
    private String tel_evening_exchange = null;
    private String tel_evening_local = null;
    private String tel_evening_ext = null;
    private String tel_day_areacode = null;
    private String tel_day_exchange = null;
    private String tel_day_local = null;
    private String tel_day_ext = null;
    private String tel_other_areacode = null;
    private String tel_other_exchange = null;
    private String tel_other_local = null;
    private String tel_other_ext = null;
    private String address_street1 = null;
    private String address_street2 = null;
    private String address_city = null;
    private String address_state = null;
    private String address_zip = null;
    private String experience_year = null;
    private String recent_year = null;
    private int birthday_day = -1;
    private int birthday_month = -1;
    private int birthday_year = -1;
    private String sex = null;

    public String getName_last() {
      return (this.name_last);
    }

    public void setName_last(String name_last) {
        this.name_last = name_last;
    }

    public String getName_first() {
      return (this.name_first);
    }

    public void setName_first(String name_first) {
        this.name_first = name_first;
    }

    public String getName_middle() {
      if (this.name_middle.length() == 0)
        return null;
      else
        return (this.name_middle);
    }

    public void setName_middle(String name_middle) {
        this.name_middle = name_middle;
    }

    public String getName_suffix() {
      if (this.name_suffix.length() == 0)
        return null;
      else
	  return (this.name_suffix);
    }

    public void setName_suffix(String name_suffix) {
        this.name_suffix = name_suffix;
    }

    public String getPassword() {
	return (this.password);
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirm_password() {
	return (this.confirm_password);
    }

    public void setConfirm_password(String password) {
        this.confirm_password = password;
    }

    public String getEmail() {
      if (this.email.length() == 0)
        return null;
      else
	  return (this.email);
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTel_evening_areacode() {
      return (this.tel_evening_areacode);
    }

    public void setTel_evening_areacode(String code) {
        this.tel_evening_areacode = code;
    }

    public String getTel_evening_exchange() {
      return (this.tel_evening_exchange);
    }

    public void setTel_evening_exchange(String code) {
        this.tel_evening_exchange = code;
    }

    public String getTel_evening_local() {
      return (this.tel_evening_local);
    }

    public void setTel_evening_local(String code) {
        this.tel_evening_local = code;
    }

    public String getTel_evening_ext() {
      return (this.tel_evening_ext);
    }

    public String getTel_day_areacode() {
      return (this.tel_day_areacode);
    }

    public void setTel_day_areacode(String code) {
        this.tel_day_areacode = code;
    }

    public String getTel_day_exchange() {
      return (this.tel_day_exchange);
    }

    public void setTel_day_exchange(String code) {
        this.tel_day_exchange = code;
    }

    public String getTel_day_local() {
      return (this.tel_day_local);
    }

    public void setTel_day_local(String code) {
        this.tel_day_local = code;
    }

    public String getTel_day_ext() {
      return (this.tel_day_ext);
    }

    public void setTel_day_ext(String code) {
        this.tel_day_ext = code;
    }

    public String getTel_other_areacode() {
      return (this.tel_other_areacode);
    }

    public void setTel_other_areacode(String code) {
        this.tel_other_areacode = code;
    }

    public String getTel_other_exchange() {
      return (this.tel_other_exchange);
    }

    public void setTel_other_exchange(String code) {
        this.tel_other_exchange = code;
    }

    public String getTel_other_local() {
      return (this.tel_other_local);
    }

    public void setTel_other_local(String code) {
        this.tel_other_local = code;
    }

    public String getTel_other_ext() {
      return (this.tel_other_ext);
    }

    public String getTel_evening() {
      this.tel_evening = "(" + this.tel_evening_areacode + ")" + this.tel_evening_exchange
                         + "-" + this.tel_evening_local;
      if (this.tel_evening_ext != null && this.tel_evening_ext.length() > 0)
         this.tel_evening = this.tel_evening + "Ext." + this.tel_evening_ext;
	return (this.tel_evening);
    }

    public String getTel_day() {
      if (this.tel_day_areacode == null || this.tel_day_areacode.length() == 0)
        return null;
        
      this.tel_day = "(" + this.tel_day_areacode + ")" + this.tel_day_exchange
                         + "-" + this.tel_day_local;
      if (this.tel_day_ext != null && this.tel_day_ext.length() > 0)
         this.tel_day = this.tel_day + "Ext." + this.tel_day_ext;
	return (this.tel_day);
    }

    public String getTel_other() {
      if (this.tel_other_areacode == null || this.tel_other_areacode.length() == 0)
        return null;

      this.tel_other = "(" + this.tel_other_areacode + ")" + this.tel_other_exchange
                         + "-" + this.tel_other_local;
      if (this.tel_other_ext != null && this.tel_other_ext.length() > 0)
         this.tel_other = this.tel_other + "Ext." + this.tel_other_ext;
	return (this.tel_other);
    }

    public String getAddress_street1() {
	return (this.address_street1);
    }

    public void setAddress_street1(String address) {
        this.address_street1 = address;
    }

    public String getAddress_street2() {
      if (this.address_street2.length() == 0)
        return null;
      else
	  return (this.address_street2);
    }

    public void setAddress_street2(String address) {
        this.address_street2 = address;
    }

    public String getAddress_city() {
	return (this.address_city);
    }

    public void setAddress_city(String city) {
        this.address_city = city;
    }

    public String getAddress_state() {
	return (this.address_state);
    }

    public void setAddress_state(String state) {
        this.address_state = state;
    }

    public String getAddress_zip() {
	return (this.address_zip);
    }

    public void setAddress_zip(String zip) {
        this.address_zip = zip;
    }

    public int getBirthday_day() {
	return (this.birthday_day);
    }

    public void setBirthday_day(int day) {
        this.birthday_day = day;
    }

    public int getBirthday_month() {
	return (this.birthday_month);
    }

    public void setBirthday_month(int month) {
        this.birthday_month = month;
    }

    public int getBirthday_year() {
	return (this.birthday_year);
    }

    public void setBirthday_year(int year) {
        this.birthday_year = year;
    }

    public String getExperience_year() {
	return (this.experience_year);
    }

    public void setExperience_year(String year) {
        this.experience_year = year;
    }

    public String getRecent_year() {
	return (this.recent_year);
    }

    public void setRecent_year(String year) {
        this.recent_year = year;
    }

    public String getSex() {
	return (this.sex);
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

  /**
   * Validate the properties that have been set from this HTTP request,
   * and return an <code>ActionErrors</code> object that encapsulates any
   * validation errors that have been found.  If no errors are found, return
   * <code>null</code> or an <code>ActionErrors</code> object with no
   * recorded error messages.
   *
   * @param mapping The mapping used to select this instance
   * @param request The servlet request we are processing
  */
  public ActionErrors validate(ActionMapping mapping,
                           HttpServletRequest request) {

    ActionErrors errors = new ActionErrors();

    if (name_last.length() == 0)
        errors.add("name_last",
                   new ActionError("error.name_last"));

    if (name_first.length() == 0)
        errors.add("name_first",
                   new ActionError("error.name_first"));

    if (email.length() == 0)
        errors.add("email",
                   new ActionError("error.email.format"));

    int phonenumber = tel_evening_areacode.length() + tel_evening_exchange.length() 
                    + tel_evening_local.length();
    if (phonenumber == 0)
        errors.add("tel_evening", new ActionError("error.tel_evening"));
    else if (phonenumber != 10)
        errors.add("tel_evening",
                   new ActionError("error.tel_format"));

    phonenumber = tel_day_areacode.length() + tel_day_exchange.length() 
                    + tel_day_local.length();
    if (phonenumber != 10 && phonenumber != 0)
        errors.add("tel_day",
                   new ActionError("error.tel_day"));

    phonenumber = tel_other_areacode.length() + tel_other_exchange.length() 
                    + tel_other_local.length();
    if (phonenumber != 10 && phonenumber != 0)
        errors.add("tel_other",
                   new ActionError("error.tel_other"));

    if (address_street1.length() == 0)
        errors.add("addr_street1",
                   new ActionError("error.addr_street1"));

    if (address_city.length() == 0)
        errors.add("addr_city",
                   new ActionError("error.addr_city"));

    if (address_zip.length() == 0)
        errors.add("addr_zip",
                   new ActionError("error.addr_zip"));

    if (experience_year.length() == 0)
        errors.add("experience_year",
                   new ActionError("error.experience_year"));

    if (recent_year.length() == 0)
        errors.add("recent_year",
                   new ActionError("error.recent_year"));

    if (sex.length() == 0)
        errors.add("sex",
                   new ActionError("error.sex"));

    if (!password.equals(confirm_password))
        errors.add("confirm_password",
                   new ActionError("error.password.match"));

    if (email.length() != 0 && email.indexOf('@') <0)
        errors.add("email_format",
                   new ActionError("error.email.format"));
                   
    // check birthday
    switch (this.birthday_month) {
    	case 4:
    	case 6:
    	case 9:
    	case 11: if (this.birthday_day > 30)
    	           errors.add("birthday", new ActionError("error.birthday"));
    	         break;
    	case 2: if (this.birthday_year % 4 == 0) {
    		  if (this.birthday_day > 29)
    		    errors.add("birthday", new ActionError("error.birthday"));
    	        } else
    	          if (this.birthday_day > 28)
    	            errors.add("birthday", new ActionError("error.birthday"));
    	        break;
    }

    return (errors);
  }

} // MemberRegisterForm

