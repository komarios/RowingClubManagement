<%
/*
 * Copyright (c) Carnegie Lake Rowing Association 2002. All rights reserved.
 * Distributed under the GPL license. See doc/COPYING.
 * $RCSfile: memberlisthelp.jsp,v $
 * $Date: 2003/02/26 03:38:47 $
 * $Revision: 1.2 $
 */
%>

<%@ page language= "java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<%@ page import= "com.clra.web.FormattedDate" %>
<%@ page import= "com.clra.rowing.RowingSessionState" %>

<html:html locale="false">
<head>
<title><bean:message key="helpmemberlist.title"/></title>
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

<!-- table to set text boundaries -->
<a name="textTable"/>
<table cellpadding=0 cellspacing=0 border=0 width=647>

<!-- menu options -->
<tr>
<td width=1 height=12>&nbsp;</td>
<td width=322 align=left>
  &nbsp;&nbsp;
</td>
<td width=1>&nbsp;</td>
<td width=322 align=right>
  <html:link page="/restricted/memberlist.jsp">
    <bean:message key="page.back"/></html:link>
</td>
<td width=1>&nbsp;</td>
</tr>
<tr><td colspan=5 height=1 bgcolor=ffcc66>
      <img src="images/spacer.gif" width=1 height=1></td></tr>
<tr><td colspan=5 height=8>
      <img src="images/spacer.gif" width=1 height=1></td></tr>
<!-- end menu options -->

<!-- row for pagetitle and help -->
<tr>
<td width=1>&nbsp;</td>
<td width=322 align=left>
  <p><FONT class=header16><bean:message key="helpmemberlist.pagetitle"/></FONT></p></td>
<td width=1>&nbsp;</td>
<td width=322 align=right>&nbsp;</td>
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
<FONT class=header16><bean:message key="helpmemberlist.pagesubtitle"/></FONT>
<td width=1>&nbsp;</td>
</td>
</tr>
<!-- end row for page subtitle -->

<!-- text spacer -->
<tr><td colspan=5 height=16>&nbsp;</td></tr>
<!-- end text spacer -->

<!-- row for short description -->
<tr>
<td width=1>&nbsp;</td>
<td colspan=3>
<p>
<jsp:useBean id="thisDate" class="com.clra.web.FormattedDate" />
<bean:message key="helpmemberlist.shortdesc" arg0='<%=thisDate.getValue()%>'/>
</p>
</td>
<td width=1>&nbsp;</td>
</td>
</tr>
<!-- end row for short description -->

<!-- text spacer -->
<tr><td colspan=5 height=16>&nbsp;</td></tr>
<!-- end text spacer -->

<!-- row for long description -->
<tr>
<td width=1>&nbsp;</td>
<td colspan=3>
<p><bean:message key="helpmemberlist.longdesc" /></p>
</td>
<td width=1>&nbsp;</td>
</td>
</tr>
<!-- end row for long description -->

</table>
<!-- end table to set text boundaries same as list boundaries -->

</body>
</html:html>

