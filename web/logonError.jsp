<%
/*
 * Copyright (c) Carnegie Lake Rowing Association 2002. All rights reserved.
 * Distributed under the GPL license. See doc/COPYING.
 * $RCSfile: logonError.jsp,v $
 * $Date: 2003/02/26 03:38:47 $
 * $Revision: 1.2 $
 */
%>

<%@ page language= "java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html:html locale="false">
<head>
<title>
  <%-- FIXME hard-coded text --%>
  Logged off
</title>
<link rel="stylesheet" type="text/css" href="stylesheet.css" title="Style">
<html:base/>
</head>

<body bgcolor="#ffffff" text="#333333" link="#003366" alink="#3399cc" vlink="#3399cc" topmargin="0" leftmargin="8" marginheight="1" marginwidth="8">

<html:errors/>

<logic:notPresent name="org.apache.struts.action.MESSAGE" scope="application">
  <font color="red">
    ERROR:  Application resources not loaded -- check servlet container
    logs for error messages.
  </font>
</logic:notPresent>

<!-- table to set text boundaries -->
<a name="textTable"/>
<table cellpadding=0 cellspacing=0 border=0 width=647>

<!-- menu options -->
<tr><td colspan=5 height=1>
      <img src="images/spacer.gif" width=1 height=1></td></tr>
<tr>
<td width=1 height=97>&nbsp;</td>
<td width=245 height=97 align=left valign=bottom>
  &nbsp;&nbsp;
</td>
<td width=156 height=97 align=center>
  <html:link href="http://marsogames.world/clra-test">
    <img src="images/clralogo.gif"
         width="156" height="97"
         alt="Carnegie Lake Rowing Association"
         align="center"/>
  </html:link>
</td>
<td width=245 height=97 align=right valign=bottom>
  &nbsp;&nbsp;
</td>
<td width=1 height=97>&nbsp;</td>
</tr>
<tr><td colspan=5 height=1 bgcolor=ffcc66>
      <img src="images/spacer.gif" width=1 height=1></td></tr>
<tr><td colspan=5 height=8>
      <img src="images/spacer.gif" width=1 height=1></td></tr>
<!-- end menu options -->

<!-- row for pagetitle and help -->
<tr>
<td width=1>&nbsp;</td>
<td colspan=3 align=center>
  <p>
  <FONT class=header16>
  <%-- bean:message key="membermenu.pagetitle"/ --%>
  <%-- FIXME hard-coded text --%>
  Carnegie Lake Rowing Association
  </FONT>
  </p>
</td>
<td width=1>&nbsp;</td>
</tr>
<!-- end row for pagetitle and help -->

<!-- text spacer -->
<tr><td colspan=5 height=16>&nbsp;</td></tr>
<!-- end text spacer -->

<!-- row for page subtitle -->
<tr>
<td width=1>&nbsp;</td>
<td colspan=3>
  <FONT class=header16>
  <%-- bean:message key="membermenu.pagesubtitle"/ --%>
  <%-- FIXME hard-coded text --%>
  Logged out
  </FONT>
<td width=1>&nbsp;</td>
</td>
</tr>
<!-- end row for page subtitle -->

<!-- text spacer -->
<tr><td colspan=5 height=16>&nbsp;</td></tr>
<!-- end text spacer -->

<!-- row for long description -->
<tr>
<td width=1>&nbsp;</td>
<td colspan=3>
<%-- FIXME hard-coded text --%>
<logic:present name="accountNameModified" scope="request">
<p><font color="red">Your account name has been changed.</font></p>
<p>Please log in again under your new account name by clicking this link:</p>
</logic:present>
<logic:notPresent name="accountNameModified" scope="request">
<p><font color="red">Your user name or password is incorrect.</font></p>
<p>Please try again by clicking this link:</p>
</logic:notPresent>
<p align=center>
  <html:link page="/restricted/membermenu.jsp">Log in</html:link>
</p>

</td>
<td width=1>&nbsp;</td>
</td>
</tr>
<!-- end row for long description -->

</table>
<!-- end table to set text boundaries same as list boundaries -->

</body>
</html:html>

