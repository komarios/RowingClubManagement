<%
/*
 * Copyright (c) Carnegie Lake Rowing Association 2002. All rights reserved.
 * Distributed under the GPL license. See doc/COPYING.
 * $RCSfile: logon.jsp,v $
 * $Date: 2003/03/13 17:46:04 $
 * $Revision: 1.9 $
 */
%>

<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html:html locale="true">
<head>
<title>
  <%-- FIXME hard-coded text --%>
  Laker Easy Signup
</title>
<html:base/>
<link rel="stylesheet" type="text/css" href="stylesheet.css" title="Style">
</head>

<body bgcolor="#ffffff" text="#333333" link="#003366" alink="#3399cc" vlink="#3399cc" topmargin="0" leftmargin="8" marginheight="1" marginwidth="8" onload="document.logon_form.j_username.focus();">

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
  Laker Easy Signup
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
<p>
<%-- FIXME hard-coded text --%>
Welcome to the easy signup site of the Carnegie Lake Rowing Association. 
</p>
<p>
<%-- FIXME hard-coded text --%>
To proceed, please enter your user name and password.
</p>

</td>
<td width=1>&nbsp;</td>
</td>
</tr>
<!-- end row for long description -->

</table>
<!-- end table to set text boundaries same as list boundaries -->

<form method="POST" name="logon_form" action="j_security_check">
<table border=0 width=647>

  <tr>
    <td align="right">
      <bean:message key="logon.prompt.username"/>
    </td>
    <td align="left">
      <input type="text" name="j_username">
    </td>
  </tr>

  <tr>
    <td align="right">
      <bean:message key="logon.prompt.password"/>
    </td>
    <td align="left">
      <input type="password" name="j_password">
    </td>
  </tr>

  <tr>
    <td align="right">
      <input type="submit" value="Log in"> <!-- FIXME hard-coded text -->
    </td>
    <td align="left">
      <input type="reset" value="Clear"> <!-- FIXME hard-coded text -->
    </td>
  </tr>

</table>

</form>

<%-- html:link page="/addmember.jsp">
<bean:message key="logon.newmember"/>
</html:link --%>

</body>
</html:html>

