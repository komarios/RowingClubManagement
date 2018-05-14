<%
/*
 * Copyright (c) Carnegie Lake Rowing Association 2002. All rights reserved.
 * Distributed under the GPL license. See doc/COPYING.
 * $RCSfile: membermenu.jsp,v $
 * $Date: 2003/03/10 03:04:39 $
 * $Revision: 1.18 $
 */
%>

<%@ page language= "java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/clra.tld" prefix="clra" %>

<%@ page import= "com.clra.web.MemberView" %>
<%@ page import= "com.clra.web.MemberTag" %>

<%
  MemberView mv = null;
  try {
    mv = MemberTag.getMemberFromAuthenticatedUser(request);
  }
  catch ( javax.ejb.NoSuchEntityException x ) {
    session.removeAttribute( com.clra.web.Constants.USER_KEY );
    session.invalidate();
    request.setAttribute( "accountNameModified", new Boolean(true) );
    request.getRequestDispatcher( "/logonError.jsp" ).forward(request,response);
  }
%>

<html:html locale="false">
<head>
<title><bean:message key="membermenu.title"/></title>
<html:base/>
<link rel="stylesheet" type="text/css" href="../stylesheet.css" title="Style">
</head>

<body bgcolor="#ffffff" text="#333333" link="#003366" alink="#3399cc" vlink="#3399cc" topmargin="0" leftmargin="8" marginheight="1" marginwidth="8">

<logic:notPresent name="org.apache.struts.action.MESSAGE" scope="application">
  <font color="red">
    ERROR:  Application resources not loaded -- check servlet container
    logs for error messages.
  </font>
</logic:notPresent>

<%-- table to set text boundaries --%>
<a name="textTable"/>
<table cellpadding=0 cellspacing=0 border=0 width=647>

<%-- menu options --%>
<tr>
<td width=1 height=12>&nbsp;</td>

<td width=322 align=left>
  <%-- FIXME hardcoded text --%>
  <p><FONT class=header16>Welcome <clra:member/></FONT>
  &nbsp;
</p>
</td>

<td width=1>&nbsp;</td>

<td width=322 align=right>
  <html:link href="http://marsogames.world/clra-test">
    <bean:message key="page.main"/></html:link>
  &nbsp;&nbsp;

  <html:link page="/logout.do"> Logout </html:link>
  &nbsp;&nbsp;

  <html:link page="/help/membermenuhelp.jsp">
    <bean:message key="page.help"/></html:link>

</td>

<td width=1>&nbsp;</td>
</tr>

<tr><td colspan=5 height=1>
      <img src="images/spacer.gif" width=1 height=1></td></tr>
<tr><td colspan=5 height=1 bgcolor=ffcc66>
      <img src="images/spacer.gif" width=1 height=1></td></tr>
<tr><td colspan=5 height=8>
      <img src="images/spacer.gif" width=1 height=1></td></tr>
<%-- end menu options --%>

<%-- row for pagetitle and help --%>
<tr>
<td width=1>&nbsp;</td>
<td width=322 align=left>
  <p><FONT class=header16><bean:message key="membermenu.pagetitle"/></FONT></p>
</td>
<td width=1>&nbsp;</td>
<td width=322 align=right>&nbsp;</td>
<td width=1>&nbsp;</td>
</tr>

<%-- text spacer --%>
<tr><td colspan=5 height=16>&nbsp;</td></tr>

<%-- row for page subtitle --%>
<tr>
<td width=1>&nbsp;</td>
<td colspan=3>
<% if ( mv.hasNoRoles() ) { %>
<bean:message key="membermenu.notauthorized"/>
<% } else { %>
<FONT class=header16><bean:message key="membermenu.pagesubtitle"/></FONT>
<% } %>
<td width=1>&nbsp;</td>
</td>
</tr>

<%-- text spacer --%>
<tr><td colspan=5 height=16>&nbsp;</td></tr>

<%
  if ( mv.hasRole("MEMBER") ) {
    Integer memberId = mv.getId();
    request.setAttribute( "memberId", memberId );
%>

<%-- row for long description --%>
<tr>
<td width=1>&nbsp;</td>
<td colspan=3>
<p>
<bean:message key="membermenu.memberheading"/>
<ul>
    <%-- see etc/struts-config.xml for action mapping --%>
<li><html:link
    page="/editMemberInfo.do?function=MemberEdit"
    paramId="id" paramName="memberId">
    <bean:message key="membermenu.accountlink"/></html:link>
    </li>
<li><a href="memberlist.jsp">
    <bean:message key="membermenu.memberlist"/>
    </a></li>
   <%--
   } /* if MEMBER */

  boolean hasExportPrivilege = mv.hasRole("CAPTAIN")
    || mv.hasRole("MEMBERMGR") || mv.hasRole("TREASURER");
  if ( hasExportPrivilege ) {
  --%>
<li><a href="MembershipExport?format=XML">Export LakerList to XML format</a>
    &nbsp;<em>(May take up to 60 seconds)</em>
    </li>
<li><a href="MembershipExport?format=CSV">Export LakerList to CSV format</a>
    &nbsp;<em>(May take up to 60 seconds)</em>
    </li>
  <%--
  } /* if hasExportPrivilege */

  if ( mv.hasRole("MEMBER") ) {
  --%>
   </ul>
   </p>

   <p>
   <bean:message key="membermenu.signupheading"/>
   <ul>
   <li><a href="../restricted/enrollmentlist.jsp">
       <bean:message key="membermenu.signuplink"/>
       </a></li>
   </ul>
   </p>

   <%
   } /* if MEMBER */

  boolean isRowingAdmin = mv.hasRole("CAPTAIN") || mv.hasRole("COACH");
  if (isRowingAdmin) {
  %>
   <p>
   <bean:message key="membermenu.rowingadminheading"/>
   <ul>
   <li><a href="../admin/sessionlist.jsp">
       <bean:message key="membermenu.sessionedit"/>
       </a></li> 
   </ul>
  <% } /* if isRowingAdmin */

  boolean isAccountAdmin = mv.hasRole("MEMBERMGR") || mv.hasRole("TREASURER");
  if (isAccountAdmin) {
  %>
   <p>
   <bean:message key="membermenu.accountadminheading"/>
   <ul>
   <li><html:link page="/editMemberInfo.do?function=AdminCreate">
       <bean:message key="membermenu.editnewaccountslink"/></html:link></li>
   <li><a href="../admin/membermanage.jsp">
       <bean:message key="membermenu.editbrowseaccountslink"/></a></li>
   </ul>
  <% } /* if isAccountAdmin */  %>

</td>
<td width=1>&nbsp;</td>
</td>
</tr>
<%-- end row for long description --%>

</table>
<%-- end table to set text boundaries same as list boundaries --%>

</body>
</html:html>

<%--
/*
 * $Log: membermenu.jsp,v $
 * Revision 1.18  2003/03/10 03:04:39  rphall
 * Wording change
 *
 * Revision 1.17  2003/03/05 13:52:23  rphall
 * Added MEMBER as allowed role for exporting LakerList
 *
 * Revision 1.16  2003/02/26 03:38:47  rphall
 * Added copyright and GPL license
 *
 * Revision 1.15  2003/02/24 15:23:31  rphall
 * Fixed workflow bugs on login errors
 *
 * Revision 1.14  2003/02/20 16:35:44  rphall
 * Revised page if user has no roles
 *
 * Revision 1.13  2003/02/20 05:07:21  rphall
 * Cleaned up HTML
 *
 * Revision 1.12  2003/02/18 04:35:55  rphall
 * Major revision; working for MEMBEREDIT, ADMINEDIT, ADMINCREATE
 *
 * Revision 1.11  2003/02/10 12:15:08  rphall
 * Commented out search option for editing accounts
 *
 * Revision 1.10  2003/02/10 05:32:53  rphall
 * Changed comments from HTML-style to JSP-style
 *
 */
--%>

