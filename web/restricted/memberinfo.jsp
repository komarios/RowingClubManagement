<%
/*
 * Copyright (c) Carnegie Lake Rowing Association 2002. All rights reserved.
 * Distributed under the GPL license. See doc/COPYING.
 * $RCSfile: memberinfo.jsp,v $
 * $Date: 2003/03/13 17:46:41 $
 * $Revision: 1.18 $
 */
%>

<%@ page language= "java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<%@ page import= "com.clra.member.MemberName" %>
<%@ page import= "com.clra.member.Address" %>
<%@ page import= "com.clra.member.Telephone" %>
<%@ page import= "com.clra.web.FormattedDate" %>
<%@ page import= "com.clra.web.MemberView" %>
<%@ page import= "com.clra.web.MemberTag" %>
<%@ page import= "com.clra.web.MonthViewSelectorTag" %>
<%@ page import= "com.clra.web.Text" %>
<%@ page import= "com.clra.web.YearViewSelectorTag" %>
<%@ page import= "java.util.Calendar" %>
<%@ page import= "java.util.Collection" %>
<%@ page import= "java.util.Date" %>
<%@ page import= "java.util.Map" %>
<%@ page import= "java.text.*" %>

<jsp:useBean id="states" class="com.clra.web.DataUtils$States" />
<jsp:useBean id="years"  class="com.clra.web.DataUtils$AccountYears" />
<jsp:useBean id="accountTypes" class="com.clra.web.DataUtils$AccountTypes" />
<jsp:useBean id="roles"  class="com.clra.web.DataUtils$Roles" />
<jsp:useBean id="memberinfoForm"
             scope="request" type="com.clra.web.MemberInfoForm" />

<html:html>

<head>
   <html:base/>
   <link rel="stylesheet" href="../stylesheet.css" type="text/css" title="Style">
   <title>
        <bean:message key="memberinfo.title"/>
        <jsp:getProperty name="memberinfoForm" property="fullName" />
    </title>
</head>

<body bgcolor="#ffffff" text="#333333" link="#003366" alink="#3399cc" vlink="#3399cc" topmargin="0" >

<logic:notPresent name="org.apache.struts.action.MESSAGE" scope="application">
  <font color="red">
    ERROR:  Application resources not loaded -- check servlet container
    logs for error messages.
  </font>
</logic:notPresent>

<%-- table to set text boundaries same as form boundaries --%>
<a name="textTable"/>
<table cellpadding=0 cellspacing=0 border=0 width=640>

<tr><td>&nbsp;</td></tr>
<tr>
<td>
    <font class=header16><bean:message key="memberinfo.bodytitle"/></font>
</td>
</tr>
<tr><td>&nbsp;</td></tr>

<tr>
<td>
    <% if ( memberinfoForm.isAdminCreate() ) { %>
    <font class=header16><bean:message key="memberinfo.bodysubtitle.admincreate"/></font>
    <% } else if ( memberinfoForm.isAdminEdit() ) { %>
    <font class=header16><bean:message key="memberinfo.bodysubtitle.adminedit"/></font>
    <% } else if ( memberinfoForm.isMemberEdit() ) { %>
    <font class=header16><bean:message key="memberinfo.bodysubtitle.memberedit"/></font>
    <% } %>
</td>
</tr>
<tr><td>&nbsp;</td></tr>

<tr>
<td>
    <% if ( memberinfoForm.isAdminCreate() ) { %>
    <bean:message key="memberinfo.shortdesc.admincreate"/>
    <% } else if ( memberinfoForm.isAdminEdit() ) { %>
    <bean:message key="memberinfo.shortdesc.adminedit"/>
    <% } else if ( memberinfoForm.isMemberEdit() ) { %>
    <bean:message key="memberinfo.shortdesc.memberedit"/>
    <% } %>
</td>
</tr>
<tr><td>&nbsp;</td></tr>

<tr>
<td>
<font color="red">
<html:errors/>
</font>
</td>
</tr>
<tr><td>&nbsp;</td></tr>

</table>
<%-- end table to set text boundaries same as form boundaries --%>

<!-- Table to set form size -->
<a name="formTable"/>
<table cellpadding=0 cellspacing=0 border=0 width=640>

<!-- MemberInfo form -->
<% String focusField = "firstName"; %>
<html:form focus="<%=focusField%>" action="/saveMemberInfo">
<html:hidden property="function"/>
<html:hidden property="id"/>
<html:hidden property="accountNameOriginal"/>

<tr>
<td width=1 height=1 bgcolor=ffcc66>
    <img src="images/spacer.gif" width=1 height=1></td>
<td width=78 height=1 bgcolor=ffcc66>
    <img src="images/spacer.gif" width=1 height=1></td>
<td width=238 height=1 bgcolor=ffcc66>
    <img src="images/spacer.gif" width=1 height=1></td>
<td width=1 height=1 bgcolor=ffcc66>
    <img src="images/spacer.gif" width=1 height=1></td>
<td width=4 height=1 bgcolor=ffcc66>
    <img src="images/spacer.gif" width=1 height=1></td>
<td width=99 height=1 bgcolor=ffcc66>
    <img src="images/spacer.gif" width=1 height=1></td>
<td width=104 height=1 bgcolor=ffcc66>
    <img src="images/spacer.gif" width=1 height=1></td>
<td width=114 height=1 bgcolor=ffcc66>
    <img src="images/spacer.gif" width=1 height=1></td>
<td width=1 height=1 bgcolor=ffcc66>
    <img src="images/spacer.gif" width=1 height=1></td>
</tr>

<tr>
<td width=1 height=1 bgcolor=ffcc66>
    <img src="images/spacer.gif" width=1 height=1></td>
<td colspan="7" bgcolor=ffcc66>
    <p class="memberinfoLabel" style="font-weight: bold;">
        &nbsp;
        <bean:message key="memberinfo.tabletitle"/>
    </p>
</td>
<td width=1 height=1 bgcolor=ffcc66>
    <img src="images/spacer.gif" width=1 height=1></td>
</tr>

<tr>
<td width=1 height=1 bgcolor=ffcc66>
    <img src="images/spacer.gif" width=1 height=1></td>
<td colspan=2 height=3 bgcolor=fff8cc>
    <img src="images/spacer.gif" width=1 height=1></td>
<td width=1 height=1 bgcolor=ffcc66>
    <img src="images/spacer.gif" width=1 height=1></td>
<td colspan=4 height=3 bgcolor=fff8cc>
    <img src="images/spacer.gif" width=1 height=1></td>
<td width=1 height=1 bgcolor=ffcc66>
    <img src="images/spacer.gif" width=1 height=1></td>
</tr>

<tr>
<td width=1 height=1 bgcolor=ffcc66>
    <img src="images/spacer.gif" width=1 height=1></td>
<td colspan=2 bgcolor=#fff8cc>
    <strong>
        <bean:message key="memberinfo.name_heading"/>
    </strong>
</td>
<td width=1 height=1 bgcolor=ffcc66>
    <img src="images/spacer.gif" width=1 height=1></td>
<td bgcolor=fff8cc>&nbsp;</td>
<td colspan=3 bgcolor=#fff8cc>
    <strong>
        &nbsp;<bean:message key="memberinfo.phone_email_heading"/>
    </strong>
</td>
<td width=1 height=1 bgcolor=ffcc66>
    <img src="images/spacer.gif" width=1 height=1></td>
</tr>

<tr>
<td width=1 height=1 bgcolor=ffcc66>
    <img src="images/spacer.gif" width=1 height=1></td>
<td align=right>
    <em><bean:message key="memberinfo.firstname_heading"/></em>&nbsp;&nbsp;
</td>
<td>
    <%-- if ( memberinfoForm.isRestricted() ) { %>
    <jsp:getProperty name="memberinfoForm" property="firstName" />
    <% } else { --%>
    <html:text property="firstName" size="20" maxlength="20" tabindex="1" />
    <%-- } --%>
</td>
<td width=1 height=1 bgcolor=ffcc66>
    <img src="images/spacer.gif" width=1 height=1></td>
<td>&nbsp;</td>
<td align=right valign=center>
    <em><bean:message key="memberinfo.eveningphone"/></em>&nbsp;&nbsp;
</td>
<td colspan=2 valign=center>
    <html:text property="phoneEveningAreaCode" size="3" maxlength="3" tabindex="10" />&nbsp;&nbsp;<html:text property="phoneEveningExchange" size="3" maxlength="3" tabindex="11" />&nbsp;-&nbsp;<html:text property="phoneEveningLocal" size="4" maxlength="4" tabindex="12" />&nbsp;<bean:message key="memberinfo.phone_ext_heading"/>&nbsp;<html:text property="phoneEveningExt" size="5" maxlength="5" tabindex="13" />
</td>
<td width=1 height=1 bgcolor=ffcc66>
    <img src="images/spacer.gif" width=1 height=1></td>
</tr>

<tr>
<td width=1 height=1 bgcolor=ffcc66>
    <img src="images/spacer.gif" width=1 height=1></td>
<td align=right>
    <em><bean:message key="memberinfo.middlename_heading"/></em>&nbsp;&nbsp;
</td>
<td>
    <%-- if ( memberinfoForm.isRestricted() ) { %>
    <jsp:getProperty name="memberinfoForm" property="middleName" />
    <% } else { --%>
    <html:text property="middleName" size="20" maxlength="20" tabindex="2" />
    <%-- } --%>
</td>
<td width=1 height=1 bgcolor=ffcc66>
    <img src="images/spacer.gif" width=1 height=1></td>
<td>&nbsp;</td>
<td align=right valign=center>
    <em><bean:message key="memberinfo.daytimephone"/></em>&nbsp;&nbsp;
</td>
<td colspan=2 valign=center>
    <html:text property="phoneDayAreaCode" size="3" maxlength="3" tabindex="14" />&nbsp;&nbsp;<html:text property="phoneDayExchange" size="3" maxlength="3" tabindex="15" />&nbsp;-&nbsp;<html:text property="phoneDayLocal" size="4" maxlength="4" tabindex="16" />&nbsp;<bean:message key="memberinfo.phone_ext_heading"/>&nbsp;<html:text property="phoneDayExt" size="5" maxlength="5" tabindex="17" />
</td>
<td width=1 height=1 bgcolor=ffcc66>
    <img src="images/spacer.gif" width=1 height=1></td>
</tr>

<tr>
<td width=1 height=1 bgcolor=ffcc66>
    <img src="images/spacer.gif" width=1 height=1></td>
<td align=right>
    <em><bean:message key="memberinfo.lastname_heading"/></em>&nbsp;&nbsp;
</td>
<td>
    <%-- if ( memberinfoForm.isRestricted() ) { %>
    <jsp:getProperty name="memberinfoForm" property="lastName" />
    <% } else { --%>
    <html:text property="lastName" size="20" maxlength="20" tabindex="3" />
    <%-- } --%>
</td>
<td width=1 height=1 bgcolor=ffcc66>
    <img src="images/spacer.gif" width=1 height=1></td>
<td>&nbsp;</td>
<td align=right valign=center>
    <em><bean:message key="memberinfo.otherphone"/></em>&nbsp;&nbsp;
</td>
<td colspan=2 valign=center>
    <html:text property="phoneOtherAreaCode" size="3" maxlength="3" tabindex="18" />&nbsp;&nbsp;<html:text property="phoneOtherExchange" size="3" maxlength="3" tabindex="19" />&nbsp;-&nbsp;<html:text property="phoneOtherLocal" size="4" maxlength="4" tabindex="20" />&nbsp;<bean:message key="memberinfo.phone_ext_heading"/>&nbsp;<html:text property="phoneOtherExt" size="5" maxlength="5" tabindex="21" />
</td>
<td width=1 height=1 bgcolor=ffcc66>
    <img src="images/spacer.gif" width=1 height=1></td>
</tr>

<tr>
<td width=1 height=1 bgcolor=ffcc66>
    <img src="images/spacer.gif" width=1 height=1></td>
<td align=right>
    <em><bean:message key="memberinfo.namesuffix_heading"/></em>&nbsp;&nbsp;
</td>
<td>
    <%-- if ( memberinfoForm.isRestricted() ) { %>
    <jsp:getProperty name="memberinfoForm" property="suffix" />
    <% } else { --%>
    <html:text property="suffix" size="20" maxlength="20" tabindex="4" />
    <%-- } --%>
</td>
<td width=1 height=1 bgcolor=ffcc66>
    <img src="images/spacer.gif" width=1 height=1></td>
<td>&nbsp;</td>
<td align=right valign=bottom>
    <em><bean:message key="memberinfo.email"/></em>&nbsp;&nbsp;
</td>
<td colspan=2>
    <%-- if ( memberinfoForm.isRestricted() ) { %>
    <jsp:getProperty name="memberinfoForm" property="email" />
    <% } else { --%>
    <html:text property="email"  size="27" maxlength="40" tabindex="22" />
    <%-- } --%>
</td>
<td width=1 height=1 bgcolor=ffcc66>
    <img src="images/spacer.gif" width=1 height=1></td>
</tr>

<tr>
<td width=1 height=1 bgcolor=ffcc66>
    <img src="images/spacer.gif" width=1 height=1></td>
<td colspan=2 height=1>&nbsp;</td>
<td width=1 height=1 bgcolor=ffcc66>
    <img src="images/spacer.gif" width=1 height=1></td>
<td colspan=4 height=1>&nbsp;</td>
<td width=1 height=1 bgcolor=ffcc66>
    <img src="images/spacer.gif" width=1 height=1></td>
</tr>

<tr>
<td width=1 height=1 bgcolor=ffcc66>
    <img src="images/spacer.gif" width=1 height=1></td>
<td colspan=2 bgcolor=#fff8cc>
    <strong>
        <bean:message key="memberinfo.mailingaddr"/>
    </strong></td>
<td width=1 height=1 bgcolor=ffcc66>
    <img src="images/spacer.gif" width=1 height=1></td>
<td bgcolor=fff8cc>&nbsp;</td>
<td colspan=3 bgcolor=#fff8cc>
    <strong>
        &nbsp;<bean:message key="memberinfo.clra_heading"/>
    </strong>
</td>
<td width=1 height=1 bgcolor=ffcc66>
    <img src="images/spacer.gif" width=1 height=1></td>
</tr>

<tr>
<td width=1 height=1 bgcolor=ffcc66>
    <img src="images/spacer.gif" width=1 height=1></td>
<td align=right>
    <em><bean:message key="memberinfo.street"/></em>&nbsp;&nbsp;
</td>
<td>
    <%-- if ( memberinfoForm.isRestricted() ) { %>
    <jsp:getProperty name="memberinfoForm" property="street1" />
    <% } else { --%>
    <html:text property="street1"  size="27" maxlength="40" tabindex="5" />
    <%-- } --%>
</td>
<td width=1 height=1 bgcolor=ffcc66>
    <img src="images/spacer.gif" width=1 height=1></td>
<td>&nbsp;</td>
<td align=right>
    <em><bean:message key="memberinfo.clra_id_heading"/></em>&nbsp;&nbsp;
</td>
<td colspan=2>
    <% if ( memberinfoForm.isAdminCreate() ) { %>
    &nbsp;
    <% } else { %>
    <jsp:getProperty name="memberinfoForm" property="id" />
    <% } %>
</td>
<td width=1 height=1 bgcolor=ffcc66>
    <img src="images/spacer.gif" width=1 height=1></td>
</tr>

<tr>
<td width=1 height=1 bgcolor=ffcc66>
    <img src="images/spacer.gif" width=1 height=1></td>
<td align=right>
    <em><bean:message key="memberinfo.street2"/></em>&nbsp;&nbsp;
</td>
<td>
    <%-- if ( memberinfoForm.isRestricted() ) { %>
    <jsp:getProperty name="memberinfoForm" property="street2" />
    <% } else { --%>
    <html:text property="street2"  size="27" maxlength="40" tabindex="6" />
    <%-- } --%>
</td>
<td width=1 height=1 bgcolor=ffcc66>
    <img src="images/spacer.gif" width=1 height=1></td>
<td>&nbsp;</td>
<td align=right>
    <em><bean:message key="memberinfo.username_heading"/></em>&nbsp;&nbsp;
</td>
<td colspan=2>
    <%-- if ( memberinfoForm.isRestricted() ) { %>
    <jsp:getProperty name="memberinfoForm" property="accountName" />
    <% } else { --%>
    <html:text property="accountName" size="20" maxlength="20" tabindex="23" />
    <%-- } --%>
</td>
<td width=1 height=1 bgcolor=ffcc66>
    <img src="images/spacer.gif" width=1 height=1></td>
</tr>

<tr>
<td width=1 height=1 bgcolor=ffcc66>
    <img src="images/spacer.gif" width=1 height=1></td>
<td align=right>
    <em><bean:message key="memberinfo.city"/></em>&nbsp;&nbsp;
</td>
<td>
    <%-- if ( memberinfoForm.isRestricted() ) { %>
    <jsp:getProperty name="memberinfoForm" property="city" />
    <% } else { --%>
    <html:text property="city"  size="20" maxlength="20" tabindex="7" />
    <%-- } --%>
</td>
<td width=1 height=1 bgcolor=ffcc66>
    <img src="images/spacer.gif" width=1 height=1></td>
<td>&nbsp;</td>
<td align=right>
    <em><bean:message key="memberinfo.password_heading"/></em>&nbsp;&nbsp;
</td>
<td colspan=2>
    <% if ( memberinfoForm.isRestricted() ) { %>
    *&nbsp;*&nbsp;*&nbsp;*&nbsp;*&nbsp;*
    <% } else { %>
    <html:password property="accountPassword" size="20" maxlength="20" tabindex="24" />
    <% } %>
</td>
<td width=1 height=1 bgcolor=ffcc66>
    <img src="images/spacer.gif" width=1 height=1></td>
</tr>

<tr>
<td width=1 height=1 bgcolor=ffcc66>
    <img src="images/spacer.gif" width=1 height=1></td>
<td align=right>
    <em><bean:message key="memberinfo.state" /></em>&nbsp;&nbsp;
</td>
<td>
    <%-- if ( memberinfoForm.isRestricted() ) { %>
    <jsp:getProperty name="memberinfoForm" property="state" />
    <% } else { --%>
    <html:select property="state" tabindex="8" >
    <html:options collection="states"
                property="value" labelProperty="label" />
    </html:select>
    <%-- } --%>
</td>
<td width=1 height=1 bgcolor=ffcc66>
    <img src="images/spacer.gif" width=1 height=1></td>
<td>&nbsp;</td>
<td align=right>
    <em><bean:message key="memberinfo.password_confirm" /></em>&nbsp;&nbsp;
</td>
<td colspan=2>
    <% if ( memberinfoForm.isRestricted() ) { %>
    *&nbsp;*&nbsp;*&nbsp;*&nbsp;*&nbsp;*
    <% } else { %>
    <html:password property="confirmPassword"  size="20" maxlength="20" tabindex="25" />
    <% } %>
</td>
<td width=1 height=1 bgcolor=ffcc66>
    <img src="images/spacer.gif" width=1 height=1></td>
</tr>

<tr>
<td width=1 height=1 bgcolor=ffcc66>
    <img src="images/spacer.gif" width=1 height=1></td>
<td align=right>
    <em><bean:message key="memberinfo.zip" /></em>&nbsp;&nbsp;
</td>
<td>
    <%-- if ( memberinfoForm.isRestricted() ) { %>
    <jsp:getProperty name="memberinfoForm" property="zip" />
    <% } else { --%>
    <html:text property="zip"  size="10" maxlength="10" tabindex="9" />
    <%-- } --%>
</td>
<td width=1 height=1 bgcolor=ffcc66>
    <img src="images/spacer.gif" width=1 height=1></td>
<td>&nbsp;</td>
<td align=right>
    <em><bean:message key="memberinfo.seniorityheading" /></em>&nbsp;&nbsp;
</td>
<td colspan=2>
    <% if ( memberinfoForm.isRestricted() ) { %>
    <jsp:getProperty name="memberinfoForm" property="accountYear" />
    <% } else { %>
    <html:select property="accountYear" tabindex="26" >
    <html:options collection="years"
                property="value" labelProperty="label" />
    </html:select>
    <% } %>
</td>
<td width=1 height=1 bgcolor=ffcc66>
    <img src="images/spacer.gif" width=1 height=1></td>
</tr>

<tr>
<td width=1 height=1 bgcolor=ffcc66>
    <img src="images/spacer.gif" width=1 height=1></td>
<td colspan=2>&nbsp;</td>
<td width=1 height=1 bgcolor=ffcc66>
    <img src="images/spacer.gif" width=1 height=1></td>
<td>&nbsp;</td>
<td align=right>
    <em><bean:message key="memberinfo.status"/></em>&nbsp;&nbsp;
</td>
<td colspan=2>
    <% if ( memberinfoForm.isRestricted() ) { %>
    <jsp:getProperty name="memberinfoForm" property="accountTypeStr" />
    <% } else { %>
    <html:select property="accountTypeStr" tabindex="27" >
    <html:options collection="accountTypes"
                property="value" labelProperty="label" />
    </html:select>
    <% } %>
</td>
<td width=1 height=1 bgcolor=ffcc66>
    <img src="images/spacer.gif" width=1 height=1></td>
</tr>

<% if ( !memberinfoForm.isRestricted() ) { %>

<tr>
<td width=1 height=1 bgcolor=ffcc66>
    <img src="images/spacer.gif" width=1 height=1></td>
<td colspan=2 align=right>&nbsp;</td>
<td width=1 height=1 bgcolor=ffcc66>
    <img src="images/spacer.gif" width=1 height=1></td>
<td>&nbsp;</td>
<td align=right>
    <em><bean:message key="memberinfo.role"/></em>&nbsp;&nbsp;
<td>
    <html:checkbox property="member" tabindex="28">
      <bean:message key="memberinfo.role.member"/></html:checkbox>
    &nbsp;
</td>
<td>
    &nbsp;
    <html:checkbox property="memberManager" tabindex="29">
      <bean:message key="memberinfo.role.memberManager"/></html:checkbox>
</td>
<td width=1 height=1 bgcolor=ffcc66>
    <img src="images/spacer.gif" width=1 height=1></td>
</tr>

<tr>
<td width=1 height=1 bgcolor=ffcc66>
    <img src="images/spacer.gif" width=1 height=1></td>
<td colspan=2 align=right>&nbsp;</td>
<td width=1 height=1 bgcolor=ffcc66>
    <img src="images/spacer.gif" width=1 height=1></td>
<td>&nbsp;</td>
<td align=right>&nbsp;</td>
<td>
    <html:checkbox property="captain" tabindex="30">
      <bean:message key="memberinfo.role.captain"/></html:checkbox>
    &nbsp;
</td>
<td>
    &nbsp;
    <html:checkbox property="treasurer" tabindex="31">
      <bean:message key="memberinfo.role.treasurer"/></html:checkbox>
</td>
<td width=1 height=1 bgcolor=ffcc66>
    <img src="images/spacer.gif" width=1 height=1></td>
</tr>

<tr>
<td width=1 height=1 bgcolor=ffcc66>
    <img src="images/spacer.gif" width=1 height=1></td>
<td colspan=2 align=right>&nbsp;</td>
<td width=1 height=1 bgcolor=ffcc66>
    <img src="images/spacer.gif" width=1 height=1></td>
<td>&nbsp;</td>
<td align=right>&nbsp;</td>
<td>
    <html:checkbox property="coach" tabindex="32">
      <bean:message key="memberinfo.role.coach"/></html:checkbox>
    &nbsp;
</td>
<td>
    &nbsp;
    <html:checkbox property="sessionManager" tabindex="33">
      <bean:message key="memberinfo.role.sessionManager"/></html:checkbox>
</td>
<td width=1 height=1 bgcolor=ffcc66>
    <img src="images/spacer.gif" width=1 height=1></td>
</tr>

<% } %>

<tr>
<td width=1 height=1 bgcolor=ffcc66>
    <img src="images/spacer.gif" width=1 height=1></td>
<td colspan=2 height=1>&nbsp;</td>
<td width=1 height=1 bgcolor=ffcc66>
    <img src="images/spacer.gif" width=1 height=1></td>
<td colspan=4 height=1>&nbsp;</td>
<td width=1 height=1 bgcolor=ffcc66>
    <img src="images/spacer.gif" width=1 height=1></td>
</tr>

<tr>
<td width=1 height=1 bgcolor=ffcc66>
    <img src="images/spacer.gif" width=1 height=1></td>
<td colspan="7" align="center" bgcolor=ffcc66>
    <%
      String strSubmit;
      String strCancel;
      if ( memberinfoForm.isAdminCreate() ) {
        strSubmit = Text.getMessage("memberinfo.submit.create");
        strCancel = Text.getMessage("memberinfo.cancel.create");
      } else {
        strSubmit = Text.getMessage("memberinfo.submit.update");
        strCancel = Text.getMessage("memberinfo.cancel.update");
      }
    %>
    <font class=header16>
      <html:submit styleClass="button" value="<%=strSubmit%>" />
    </font>
    &nbsp; &nbsp; &nbsp;
    <font class=header16>
      <html:cancel styleClass="button" value="<%=strCancel%>" />
    </font>
</td>
<td width=1 height=1 bgcolor=ffcc66>
    <img src="images/spacer.gif" width=1 height=1></td>
</tr>

<tr>
<td width=1 height=1 bgcolor=ffcc66>
    <img src="images/spacer.gif" width=1 height=1></td>
<td width=78 height=1 bgcolor=ffcc66>
    <img src="images/spacer.gif" width=1 height=1></td>
<td width=238 height=1 bgcolor=ffcc66>
    <img src="images/spacer.gif" width=1 height=1></td>
<td width=1 height=1 bgcolor=ffcc66>
    <img src="images/spacer.gif" width=1 height=1></td>
<td width=4 height=1 bgcolor=ffcc66>
    <img src="images/spacer.gif" width=1 height=1></td>
<td width=99 height=1 bgcolor=ffcc66>
    <img src="images/spacer.gif" width=1 height=1></td>
<td width=104 height=1 bgcolor=ffcc66>
    <img src="images/spacer.gif" width=1 height=1></td>
<td width=114 height=1 bgcolor=ffcc66>
    <img src="images/spacer.gif" width=1 height=1></td>
<td width=1 height=1 bgcolor=ffcc66>
    <img src="images/spacer.gif" width=1 height=1></td>
</tr>

</html:form>

</table>

</body>
</html:html>

