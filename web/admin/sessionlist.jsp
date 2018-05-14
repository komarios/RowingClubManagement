<%
/*
 * Copyright (c) Carnegie Lake Rowing Association 2002. All rights reserved.
 * Distributed under the GPL license. See doc/COPYING.
 * $RCSfile: sessionlist.jsp,v $
 * $Date: 2003/02/26 03:38:47 $
 * $Revision: 1.3 $
 */
%>

<%@ page language= "java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/clra.tld" prefix="clra" %>

<%@ page import= "com.clra.web.FormattedDate" %>
<%@ page import= "com.clra.web.MonthViewSelectorTag" %>
<%@ page import= "com.clra.web.YearViewSelectorTag" %>
<%@ page import= "com.clra.rowing.RowingSessionState" %>

<html:html locale="false">
<head>
<title><bean:message key="arsl.title"/></title>
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

<jsp:useBean id="thisDate" class="com.clra.web.FormattedDate" />
<%
  // FIXME put this stuff in a bean
  Integer year  = YearViewSelectorTag.yearFromContext(pageContext);
  Integer month = MonthViewSelectorTag.monthFromContext(pageContext);

  YearViewSelectorTag.resetYearInContexts(pageContext,year);
  MonthViewSelectorTag.resetMonthInContexts(pageContext,month);

  String strMonthYear =
      new FormattedDate( "MMMM, yyyy", month, year ).getValue();
%>

<!-- table to set text boundaries same as list boundaries -->
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
  <html:link page="/restricted/membermenu.jsp">
    <bean:message key="page.menu"/></html:link>
  &nbsp;&nbsp;
  <html:link page="/help/sessionlisthelp.jsp">
    <bean:message key="page.help"/></html:link>
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
  <p><FONT class=header16><bean:message key="arsl.pagetitle"/></FONT></p></td>
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
<FONT class=header16><bean:message key="arsl.pagesubtitle"/></FONT>
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
<bean:message key="arsl.shortdesc" arg0='<%=strMonthYear%>'/>
</td>
<td width=1>&nbsp;</td>
</td>
</tr>
<!-- end row for short description -->

<!-- text spacer -->
<tr><td colspan=5 height=16>&nbsp;</td></tr>
<!-- end text spacer -->

<!-- START monthViewSelector -->
<tr>
<td width=1>&nbsp;</td>
<td colspan=3>
  <FONT class=header16>ROWING SESSIONS</FONT>&nbsp;
  <b><FONT class=textred>[</FONT>
  <clra:monthViewSelector currentMonth="<%=month%>">
    month
  </clra:monthViewSelector>
  <FONT class=textred>]</FONT></b>
  &nbsp;&nbsp;
  <b><FONT class=textred>[</FONT>
  <clra:yearViewSelector currentYear="<%=year%>">
    year
  </clra:yearViewSelector>
  <FONT class=textred>]</FONT></b>
</td>
<td width=1>&nbsp;</td>
</td>
</tr>
<!-- END monthViewSelector -->

<!-- text spacer -->
<tr><td colspan=5 height=8>&nbsp;</td></tr>
<!-- end text spacer -->

</table>
<!-- end table to set text boundaries same as list boundaries -->

<!-- SESSION table -->
<a name="sessionTable"/>
<table cellpadding=0 cellspacing=0 border=0 width=647>

<!-- HEADER OF SESSION table -->
<tr>
  <td width=1 bgcolor=ffcc66>
      <img src="images/spacer.gif" width=1 height=18></td>
  <td width=87  bgcolor=ffcc66 align=center><b>&nbsp;</b></td>
  <td width=67  bgcolor=ffcc66 align=left><b>&nbsp;</b></td>
  <td width=138 bgcolor=ffcc66 align=left><b>&nbsp;</b></td>
  <td width=68  bgcolor=ffcc66 align=left><b>&nbsp;</b></td>
  <td width=61  bgcolor=ffcc66 align=left><b>&nbsp;</b></td>
  <td width=44  bgcolor=ffcc66 align=left><b>&nbsp;</b></td>
  <td width=180 bgcolor=ffcc66 align=left colspan=3>
      <html:link page="/editRowingSession.do?action=Create" >
      <b><bean:message key="arsl.addSession"/></b></html:link></td>
  <td width=1 bgcolor=ffcc66>
      <img src="images/spacer.gif" width=1 height=18></td>
</tr>
<tr>
  <td width=1 bgcolor=ffcc66>
      <img src="images/spacer.gif" width=1 height=18></td>
  <td width=87  bgcolor=ffcc66 align=center>
      <b><bean:message key="table.label.status"/></b></td>
  <td width=67  bgcolor=ffcc66 align=left>
      <b><bean:message key="table.label.date"/></b></td>
  <td width=138  bgcolor=ffcc66 align=left>
      <b><bean:message key="table.label.session"/></b></td>
  <td width=68  bgcolor=ffcc66 align=left>
      <b><bean:message key="table.label.level"/></b></td>
  <td width=61  bgcolor=ffcc66 align=left>
      <b><bean:message key="table.label.type"/></b></td>
  <td width=44  bgcolor=ffcc66 align=left>&nbsp;</td>
  <td width=180 bgcolor=ffcc66 align=left colspan=3>
      <b><bean:message key="table.label.action"/></b></td>
  <td width=1 bgcolor=ffcc66>
      <img src="images/spacer.gif" width=1 height=18></td>
</tr>
<!-- END HEADER -->

<!-- SESSION ITERATION -->

<jsp:useBean id="sessions" class="com.clra.web.SessionSet">
  <jsp:setProperty name="sessions" property="month" value="<%=month%>"/>
  <jsp:setProperty name="sessions" property="year" value="<%=year%>"/>
</jsp:useBean>

<logic:iterate id="sv" name="sessions" property="iterator"
  type="com.clra.web.SessionView">

<!-- Session row -->
<jsp:useBean id="rowFlag" class="java.lang.Object"/>
  <%
  String rowDate =
      new FormattedDate( "MM/dd/yy", sv.getDate() ).getValue();
  String dayHour =
      new FormattedDate( "EEEE h:mm a", sv.getDate() ).getValue();
  %>
  <% if ( sv.getDate().compareTo(thisDate.getDate()) < 0 ) { %>
    <!-- FIXME remove logic by defining a bgColor(sv) function -->
    <!-- This session has past, so the row is shaded -->
    <tr bgcolor=fff8cc>
    <td width=1 bgcolor=ffcc66>
        <img src="images/spacer.gif" width=1 height=18></td>
  <% } else { %>
    <!-- This session is upcoming, so the row is unshaded -->
    <tr>
    <td width=1 bgcolor=ffcc66>
        <img src="images/spacer.gif" width=1 height=18></td>
  <% } %>
  <td align=center><%=sv.getState()%></td>
  <td><%= rowDate %></td>
  <td><%= dayHour %></td>
  <td><%=sv.getLevel()%></td>
  <td><%=sv.getType()%></td>
  <td>&nbsp;</td>

  <!-- FIXME remove logic by defining a promote(sv) function -->
  <!-- state == '<%=sv.getState()%>' -->
  <% if ( sv.getState().equals(RowingSessionState.NAME_TENATIVE) ) { %>
    <td width=60 align=center>
      <html:link page="/editRowingSession.do?action=Publish"
        paramId="rowingId" paramName="sv" paramProperty="id" >
        <bean:message key="table.label.publish"/></html:link></td>
  <% } else if ( sv.getState().equals(RowingSessionState.NAME_OPEN) ) { %>
    <td width=60 align=center>
      <html:link page="/editRowingSession.do?action=Lock"
        paramId="rowingId" paramName="sv" paramProperty="id" >
        <bean:message key="table.label.lock"/></html:link></td>
  <%-- } else if ( sv.getState().equals(RowingSessionState.NAME_LOCKED) ) { %>
    <td width=60 align=center>
      <html:link page="/editRowingSession.do?action=Unlock"
        paramId="rowingId" paramName="sv" paramProperty="id" >
        <bean:message key="table.label.unlock"/></html:link></td> --%>
  <% } else { %>
    <td width=60 align=center>&nbsp;</td>
  <% } /* edit */ %>

  <!-- FIXME remove logic by defining a edit(sv) function -->
  <% if ( sv.getState().equals(RowingSessionState.NAME_TENATIVE) ) { %>
    <td width=60 align=center>
      <html:link page="/editRowingSession.do?action=Edit"
        paramId="rowingId" paramName="sv" paramProperty="id" >
        <bean:message key="table.label.edit"/></html:link></td>
  <% } else { %>
    <td width=60 align=center>
      <html:link page="/editRowingSession.do?action=View"
        paramId="rowingId" paramName="sv" paramProperty="id" >
        <bean:message key="table.label.view"/></html:link></td>
  <% } /* edit */ %>

  <!-- FIXME remove logic by defining a deleteCancel(sv) function -->
  <% if ( sv.getState().equals(RowingSessionState.NAME_TENATIVE) ) { %>
    <td width=60 align=center>
      <html:link page="/editRowingSession.do?action=Delete"
        paramId="rowingId" paramName="sv" paramProperty="id" >
        <bean:message key="table.label.delete"/></html:link></td>
  <% } else if ( sv.getState().equals(RowingSessionState.NAME_OPEN)
          || sv.getState().equals(RowingSessionState.NAME_LOCKED)
          || sv.getState().equals(RowingSessionState.NAME_BOATING1)
          || sv.getState().equals(RowingSessionState.NAME_BOATING2)) {
  %>
    <td width=60 align=center>
      <html:link page="/editRowingSession.do?action=SessionCancel"
        paramId="rowingId" paramName="sv" paramProperty="id" >
        <bean:message key="table.label.cancel"/></html:link></td>
  <% } else { %>
    <td width=60 align=center>&nbsp;</td>
  <% } /* deleteCancel */ %>

  <td width=1 bgcolor=ffcc66>
      <img src="images/spacer.gif" width=1 height=18></td>
  </tr>

  <tr>
  <td colspan=11 bgcolor=ffcc66>
    <img src="images/spacer.gif" width=1 height=1></td>
  </tr>
<!-- End of Session row -->

</logic:iterate>
<!-- END SESSION ITERATION -->

<!-- EMPTY ROW (if iteration returned no rows) -->
<logic:notPresent name="rowFlag">
<tr>
  <td width=1 bgcolor=ffcc66>
      <img src="images/spacer.gif" width=1 height=18></td>
  <td width=87 ><b>&nbsp;</b></td>
  <td width=67 ><b>&nbsp;</b></td>
  <td width=138><b>&nbsp;</b></td>
  <td width=68 ><b>&nbsp;</b></td>
  <td width=61 ><b>&nbsp;</b></td>
  <td width=44 ><b>&nbsp;</b></td>
  <td width=60 ><b>&nbsp;</b></td>
  <td width=60 ><b>&nbsp;</b></td>
  <td width=60 ><b>&nbsp;</b></td>
  <td width=1 bgcolor=ffcc66>
      <img src="images/spacer.gif" width=1 height=18></td>
</tr>
</logic:notPresent>
<!-- END EMPTY ROW ) -->

<!-- CLOSING HEADER -->
<tr>
  <td width=1 bgcolor=ffcc66>
      <img src="images/spacer.gif" width=1 height=18></td>
  <td width=87  bgcolor=ffcc66 align=center>
      <b><bean:message key="table.label.status"/></b></td>
  <td width=67  bgcolor=ffcc66 align=left>
      <b><bean:message key="table.label.date"/></b></td>
  <td width=138  bgcolor=ffcc66 align=left>
      <b><bean:message key="table.label.session"/></b></td>
  <td width=68  bgcolor=ffcc66 align=left>
      <b><bean:message key="table.label.level"/></b></td>
  <td width=61  bgcolor=ffcc66 align=left>
      <b><bean:message key="table.label.type"/></b></td>
  <td width=44  bgcolor=ffcc66 align=left>&nbsp;</td>
  <td width=180 bgcolor=ffcc66 align=left colspan=3>
      <b><bean:message key="table.label.action"/></b></td>
  <td width=1 bgcolor=ffcc66>
      <img src="images/spacer.gif" width=1 height=18></td>
</tr>
<tr>
  <td width=1 bgcolor=ffcc66>
      <img src="images/spacer.gif" width=1 height=18></td>
  <td width=87  bgcolor=ffcc66 align=center><b>&nbsp;</b></td>
  <td width=67  bgcolor=ffcc66 align=left><b>&nbsp;</b></td>
  <td width=138 bgcolor=ffcc66 align=left><b>&nbsp;</b></td>
  <td width=68  bgcolor=ffcc66 align=left><b>&nbsp;</b></td>
  <td width=61  bgcolor=ffcc66 align=left><b>&nbsp;</b></td>
  <td width=44  bgcolor=ffcc66 align=left><b>&nbsp;</b></td>
  <td width=180 bgcolor=ffcc66 align=left colspan=3>
      <html:link page="/editRowingSession.do?action=Create" >
      <b><bean:message key="arsl.addSession"/></b></html:link></td>
  <td width=1 bgcolor=ffcc66>
      <img src="images/spacer.gif" width=1 height=18></td>
</tr>
<!-- END CLOSING HEADER -->

</table>
<!-- END SESSION table -->

</body>
</html:html>

