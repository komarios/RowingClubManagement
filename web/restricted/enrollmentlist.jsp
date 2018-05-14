<%
/*
 * Copyright (c) Carnegie Lake Rowing Association 2002. All rights reserved.
 * Distributed under the GPL license. See doc/COPYING.
 * $RCSfile: enrollmentlist.jsp,v $
 * $Date: 2003/03/13 17:46:40 $
 * $Revision: 1.11 $
 */
%>

<%@ page language= "java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/clra.tld" prefix="clra" %>

<%@ page import= "com.clra.web.FormattedDate" %>
<%@ page import= "com.clra.web.MemberView" %>
<%@ page import= "com.clra.web.MemberTag" %>
<%@ page import= "com.clra.web.MonthViewSelectorTag" %>
<%@ page import= "com.clra.web.YearViewSelectorTag" %>
<%@ page import= "com.clra.rowing.RowingSessionState" %>
<%@ page import= "com.clra.rowing.RowingSessionType" %>
<%@ page import= "java.util.Date" %>
<%@ page import= "java.util.Calendar" %>

<html:html locale="false">
<head>

<title>
<%-- FIXME hardcoded text --%>
<clra:member/>'s Signup Sheet
</title>

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

<%-- table to set text boundaries same as list boundaries --%>

<a name="textTable"/>

<table cellpadding=0 cellspacing=0 border=0 width=647>

<%-- menu options --%>
<tr>

<td width=1 height=12>&nbsp;</td>

<td width=322 align=left>
  <%-- FIXME hardcoded text --%>
  <p><FONT class=header16><clra:member/>'s Rowing Sessions</FONT>
</td>

<td width=1>&nbsp;</td>

<td width=322 align=right>
  <html:link page="/restricted/membermenu.jsp">
    <bean:message key="page.menu"/></html:link>
  &nbsp;&nbsp;

  <html:link href="http://marsogames.world/clra-test">
    <bean:message key="page.main"/></html:link>
  &nbsp;&nbsp;

  <html:link page="/logout.do"> Logout </html:link>
  &nbsp;&nbsp;

  <html:link page="/help/enrollmentlisthelp.jsp">
    <bean:message key="page.help"/></html:link>
</td>

<td width=1>&nbsp;</td>

</tr>

<tr><td colspan=5 height=1 bgcolor=ffcc66>
      <img src="images/spacer.gif" width=1 height=1></td></tr>

<tr><td colspan=5 height=8>
      <img src="images/spacer.gif" width=1 height=1></td></tr>


<%-- rows for account name and logout --%>

<tr>
<td width=1>&nbsp;</td>
<td width=322 align=left>&nbsp;<td width=1>&nbsp;</td>
<td width=322 align=right>&nbsp;</td>
<td width=1>&nbsp;</td>
</tr>


<tr>
<td width=1>&nbsp;</td>
<td width=322 align=left>
  <%-- FIXME hardcoded text --%>
  &nbsp;
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
<FONT class=header16><bean:message key="mpl.pagesubtitle"/></FONT>
<td width=1>&nbsp;</td>
</td>
</tr>

<%-- text spacer --%>
<tr><td colspan=5 height=16>&nbsp;</td></tr>

<%-- row for short description --%>

<tr>
<td width=1>&nbsp;</td>
<td colspan=3>
<bean:message key="mpl.shortdesc"  arg0='<%=strMonthYear%>'/>
</td>
<td width=1>&nbsp;</td>
</td>
</tr>

<%-- text spacer --%>
<tr><td colspan=5 height=16>&nbsp;</td></tr>

<%-- monthViewSelector --%>
<tr>
<td width=1>&nbsp;</td>
<td colspan=3>
  <%-- FIXME hard-coded text --%>
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


<%-- text spacer --%>
<tr><td colspan=5 height=8>&nbsp;</td></tr>

</table>
<%-- end table to set text boundaries same as list boundaries --%>


<%-- enrollment table --%>
<a name="sessionTable"/>
<table cellpadding=0 cellspacing=0 border=0 width=647>

<%-- header of enrollment table --%>
<tr>
  <td width=1 bgcolor=ffcc66>
      <img src="images/spacer.gif" width=1 height=18></td>
  <td width=107 bgcolor=ffcc66 align=center>
      <b><bean:message key="table.label.enrollment"/></b></td>
  <td width=67  bgcolor=ffcc66 align=left>
      <b><bean:message key="table.label.date"/></b></td>
  <td width=138  bgcolor=ffcc66 align=left>
      <b><bean:message key="table.label.session"/></b></td>
  <td width=87  bgcolor=ffcc66 align=left>&nbsp;</td>
  <td width=61  bgcolor=ffcc66 align=left>
      <b><bean:message key="table.label.type"/></b></td>
  <td width=5   bgcolor=ffcc66 align=left>&nbsp;</td>
  <td width=60  bgcolor=ffcc66 align=left>&nbsp;</td>
  <td width=120 bgcolor=ffcc66 align=left colspan=2>
      <b><bean:message key="table.label.action"/></b></td>
  <td width=1 bgcolor=ffcc66>
      <img src="images/spacer.gif" width=1 height=18></td>
</tr>


<%-- enrollment iteration --%>
<% MemberView mv = MemberTag.getMemberFromAuthenticatedUser(request); %>

<jsp:useBean id="enrollments" class="com.clra.web.EnrollmentSet">
  <jsp:setProperty
          name="enrollments" property="memberId" value="<%=mv.getId()%>" />
  <jsp:setProperty name="enrollments" property="month" value="<%=month%>"/>
  <jsp:setProperty name="enrollments" property="year" value="<%=year%>"/>
</jsp:useBean>

<logic:iterate id="ev" name="enrollments" property="iterator"
  type="com.clra.web.EnrollmentView">

<%-- Session row --%>
  <%
  /* FIXME move this logic to beans */
  final String sessionState = ev.getSessionView().getState();
  final Date sessionDate = ev.getSessionView().getDate();

  boolean isFullMember =
      mv.getAccountTypeStr().trim().equalsIgnoreCase( "FULL" )
   || mv.getAccountTypeStr().trim().equalsIgnoreCase( "NOVICE" );

  boolean isRegSession =
      ev.getSessionView().getLevel().trim().equalsIgnoreCase( "REGULAR" );

  boolean isPast = thisDate.getDate().compareTo(sessionDate) > 0 ;

  Calendar calFH = Calendar.getInstance();
  calFH.add( Calendar.DATE, 14 );
  Date fortnightHenceDate = calFH.getTime();
  boolean isWithinNextFortnight = sessionDate.compareTo( fortnightHenceDate ) < 0;

  boolean isShown =
      (isFullMember && isRegSession) || (!isFullMember && !isRegSession);
  isShown = isShown && !sessionState.equals( RowingSessionState.NAME_TENATIVE );

  if ( isShown && isWithinNextFortnight ) {
  %>

  <jsp:useBean id="rowFlag" class="java.lang.Object"/>

  <%
  String rowDate = new FormattedDate( "MM/dd/yy", sessionDate ).getValue(); 
  String dayHour = new FormattedDate( "EEEE h:mm a", sessionDate ).getValue();

  if ( isPast ) {
  %>

    <%-- FIXME remove logic by defining a bgColor(sv) function --%>

    <%-- This session has past, so the row is shaded --%>
    <tr bgcolor=fff8cc>
    <td width=1 bgcolor=ffcc66>
        <img src="images/spacer.gif" width=1 height=18></td>

  <% } else { %>

    <%-- This session is upcoming, so the row is unshaded --%>
    <tr>
    <td width=1 bgcolor=ffcc66>
        <img src="images/spacer.gif" width=1 height=18></td>

  <% } %>

  <td align=center><%=ev.getEnrollmentStatus()%>&nbsp;</td>
  <td><%= rowDate %></td>
  <td><%= dayHour %></td>
  <td>

  <%
  if ( !isPast ) {
  if ( sessionState.equals(RowingSessionState.NAME_OPEN) ) {
  %>
    <bean:message key="rowingstate.open"/>
  <% } else if ( sessionState.equals(RowingSessionState.NAME_LOCKED) ) { %>
    <em><bean:message key="rowingstate.locked"/></em>
  <% } else if ( sessionState.equals(RowingSessionState.NAME_CANCELLED) ) { %>
    <bean:message key="rowingstate.cancelled"/>
  <% } else { %>
    &nbsp;
  <%
  } /* end else not OPEN and not LOCKED */
  } else {
  %>
    &nbsp;
  <% } /* end else isPast */ %>

  </td>
  <td>
  <%
    String _type = ev.getSessionView().getType();
    if ( RowingSessionType.NAME_PRACTICE.equals(_type) ) {
  %>
  <bean:message key="mpl.type.practice"/>
  <% } else if (RowingSessionType.NAME_REGATTA.equalsIgnoreCase(_type) ) { %>
  <bean:message key="mpl.type.regatta"/>
  <% } %>
  </td>
  <td>&nbsp;</td>
  <td width=60 align=center>&nbsp;</td>

  <%-- FIXME remove logic by defining an Edit/Signup(sv) function --%>

  <td width=60 align=center>
  <!-- sessionState == '<%=sessionState%>' -->
  <!-- locked State == '<%=RowingSessionState.NAME_LOCKED%>' -->
  <%
  if ( !isPast && sessionState.equals(RowingSessionState.NAME_OPEN)
       && ev.getParticipantView() == null ) {
  %>

      <html:link page="/editParticipation.do?action=Create"
        paramId="rowingId" paramName="ev" paramProperty="sessionView.id" >
        <bean:message key="table.label.signup"/></html:link>

  <%
  } else if ( !isPast && sessionState.equals(RowingSessionState.NAME_OPEN) ) {
  %>

      <html:link page="/editParticipation.do?action=Edit"
        paramId="participantId"
          paramName="ev" paramProperty="participantView.participantId" >
        <bean:message key="table.label.edit"/></html:link>

  <%
  } else if (!isPast && sessionState.equals(RowingSessionState.NAME_LOCKED)){
  %>

      <html:link page="/editParticipation.do?action=View"
        paramId="rowingId" paramName="ev" paramProperty="sessionView.id" >
          <bean:message key="table.label.view"/>
      </html:link>

  <% } else { %>

      <html:link page="/editParticipation.do?action=View"
        paramId="rowingId" paramName="ev" paramProperty="sessionView.id" >
          <bean:message key="table.label.view"/>
      </html:link>

  <% } /* editSignup */ %>
  </td>

  <td width=60 align=center>&nbsp;</td>

  <td width=1 bgcolor=ffcc66>
      <img src="images/spacer.gif" width=1 height=18></td>
  </tr>


  <tr>
  <td colspan=11 bgcolor=ffcc66>
    <img src="images/spacer.gif" width=1 height=1></td>
  </tr>

  <%
  } // end if isShown
  %>
<%-- End of Session row --%>

</logic:iterate>
<%-- end enrollment iteration --%>


<%-- empty row (if iteration returned no rows) --%>
<logic:notPresent name="rowFlag">

<tr>
  <td width=1 bgcolor=ffcc66>
      <img src="images/spacer.gif" width=1 height=18></td>
  <td width=107 ><b>&nbsp;</b></td>
  <td width=67 ><b>&nbsp;</b></td>
  <td width=138><b>&nbsp;</b></td>
  <td width=87 ><b>&nbsp;</b></td>
  <td width=61 ><b>&nbsp;</b></td>
  <td width=5  ><b>&nbsp;</b></td>
  <td width=60 ><b>&nbsp;</b></td>
  <td width=60 ><b>&nbsp;</b></td>
  <td width=60 ><b>&nbsp;</b></td>
  <td width=1 bgcolor=ffcc66>
      <img src="images/spacer.gif" width=1 height=18></td>
</tr>

</logic:notPresent>
<%-- end empty row ) --%>


<%-- closing header --%>
<tr>
  <td width=1 bgcolor=ffcc66>
      <img src="images/spacer.gif" width=1 height=18></td>
  <td width=107 bgcolor=ffcc66 align=center>
      <b><bean:message key="table.label.enrollment"/></b></td>
  <td width=67  bgcolor=ffcc66 align=left>
      <b><bean:message key="table.label.date"/></b></td>
  <td width=138  bgcolor=ffcc66 align=left>
      <b><bean:message key="table.label.session"/></b></td>
  <td width=87  bgcolor=ffcc66 align=left>&nbsp;</td>
  <td width=61  bgcolor=ffcc66 align=left>
      <b><bean:message key="table.label.type"/></b></td>
  <td width=5   bgcolor=ffcc66 align=left>&nbsp;</td>
  <td width=60  bgcolor=ffcc66 align=left>&nbsp;</td>
  <td width=120 bgcolor=ffcc66 align=left colspan=2>
      <b><bean:message key="table.label.action"/></b></td>
  <td width=1 bgcolor=ffcc66>
      <img src="images/spacer.gif" width=1 height=18></td>
</tr>


</table>
<%-- end enrollment table --%>

</body>

</html:html>

