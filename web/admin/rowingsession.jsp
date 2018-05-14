<%
/*
 * Copyright (c) Carnegie Lake Rowing Association 2002. All rights reserved.
 * Distributed under the GPL license. See doc/COPYING.
 * $RCSfile: rowingsession.jsp,v $
 * $Date: 2003/02/26 03:38:47 $
 * $Revision: 1.3 $
 */
%>

<%@ page language= "java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<jsp:useBean id="rowinglevels" class="com.clra.web.RowingSessionLevelBean" />
<jsp:useBean id="rowingstates" class="com.clra.web.RowingSessionStateBean" />
<jsp:useBean id="rowingtypes" class="com.clra.web.RowingSessionTypeBean" />
<jsp:useBean id="years"   class="com.clra.web.CalendarBean$Years" />
<jsp:useBean id="months"  class="com.clra.web.CalendarBean$Months" />
<jsp:useBean id="days"    class="com.clra.web.CalendarBean$Days" />
<jsp:useBean id="hours"   class="com.clra.web.CalendarBean$Hours" />
<jsp:useBean id="minutes" class="com.clra.web.CalendarBean$Minutes" />
<jsp:useBean id="ampm"    class="com.clra.web.CalendarBean$AmPm" />

<jsp:useBean id="rowingsessionForm"
             scope="request" type="com.clra.web.RowingSessionForm" />

<bean:define id="formAction" name="rowingsessionForm" property="action"
             scope="request" type="java.lang.String"/>
<bean:define id="CREATE" value="<%=com.clra.web.RowingSessionForm.CREATE%>" />
<bean:define id="EDIT" value="<%=com.clra.web.RowingSessionForm.EDIT%>" />
<bean:define id="PUBLISH" value="<%=com.clra.web.RowingSessionForm.PUBLISH%>" />
<bean:define id="LOCK" value="<%=com.clra.web.RowingSessionForm.LOCK%>" />
<bean:define id="VIEW" value="<%=com.clra.web.RowingSessionForm.VIEW%>" />
<bean:define id="SESSIONCANCEL"
                  value="<%=com.clra.web.RowingSessionForm.SESSIONCANCEL%>" />
<bean:define id="DELETE" value="<%=com.clra.web.RowingSessionForm.DELETE%>" />

<html:html locale="false">
<head>
<html:base/>

<!-- Title -->
<% if ( formAction.equalsIgnoreCase(SESSIONCANCEL) ) { %>
  <title><bean:message key="ars.title.sessioncancel"/></title>
<% } else if ( formAction.equalsIgnoreCase(CREATE) ) { %>
  <title><bean:message key="ars.title.create"/></title>
<% } else if ( formAction.equalsIgnoreCase(DELETE) ) { %>
  <title><bean:message key="ars.title.delete"/></title>
<% } else if ( formAction.equalsIgnoreCase(EDIT) ) { %>
  <title><bean:message key="ars.title.edit"/></title>
<% } else if ( formAction.equalsIgnoreCase(PUBLISH) ) { %>
  <title><bean:message key="ars.title.publish"/></title>
<% } else if ( formAction.equalsIgnoreCase(LOCK) ) { %>
  <title><bean:message key="ars.title.lock"/></title>
<% } else if ( formAction.equalsIgnoreCase(VIEW) ) { %>
  <title><bean:message key="ars.title.view"/></title>
<% } %>
<!-- End Title -->

<link rel="stylesheet" type="text/css" href="../stylesheet.css" title="Style">
</head>

<body bgcolor="#ffffff" text="#333333" link="#003366" alink="#3399cc" vlink="#3399cc" topmargin="0" leftmargin="8" marginheight="1" marginwidth="8">

<logic:notPresent name="org.apache.struts.action.MESSAGE" scope="application">
  <font color="red">
    ERROR:  Application resources not loaded -- check servlet container
    logs for error messages.
  </font>
</logic:notPresent>

<p><FONT class=header16><bean:message key="ars.pagetitle"/></FONT></p> 

<!-- Subtitle -->
<p><FONT class=header16>
<% if ( formAction.equalsIgnoreCase(SESSIONCANCEL) ) { %>
  <pagesubtitle><bean:message key="ars.pagesubtitle.sessioncancel"/></pagesubtitle>
<% } else if ( formAction.equalsIgnoreCase(CREATE) ) { %>
  <pagesubtitle><bean:message key="ars.pagesubtitle.create"/></pagesubtitle>
<% } else if ( formAction.equalsIgnoreCase(DELETE) ) { %>
  <pagesubtitle><bean:message key="ars.pagesubtitle.delete"/></pagesubtitle>
<% } else if ( formAction.equalsIgnoreCase(EDIT) ) { %>
  <pagesubtitle><bean:message key="ars.pagesubtitle.edit"/></pagesubtitle>
<% } else if ( formAction.equalsIgnoreCase(PUBLISH) ) { %>
  <pagesubtitle><bean:message key="ars.pagesubtitle.publish"/></pagesubtitle>
<% } else if ( formAction.equalsIgnoreCase(LOCK) ) { %>
  <pagesubtitle><bean:message key="ars.pagesubtitle.lock"/></pagesubtitle>
<% } else if ( formAction.equalsIgnoreCase(VIEW) ) { %>
  <pagesubtitle><bean:message key="ars.pagesubtitle.view"/></pagesubtitle>
<% } %>
</FONT></p>
<!-- End Subtitle -->

<!-- Short Description -->
<p>
<% if ( formAction.equalsIgnoreCase(SESSIONCANCEL) ) { %>
  <shortdesc><bean:message key="ars.shortdesc.sessioncancel"/></shortdesc>
<% } else if ( formAction.equalsIgnoreCase(CREATE) ) { %>
  <shortdesc><bean:message key="ars.shortdesc.create"/></shortdesc>
<% } else if ( formAction.equalsIgnoreCase(DELETE) ) { %>
  <shortdesc><bean:message key="ars.shortdesc.delete"/></shortdesc>
<% } else if ( formAction.equalsIgnoreCase(EDIT) ) { %>
  <shortdesc><bean:message key="ars.shortdesc.edit"/></shortdesc>
<% } else if ( formAction.equalsIgnoreCase(PUBLISH) ) { %>
  <shortdesc><bean:message key="ars.shortdesc.publish"/></shortdesc>
<% } else if ( formAction.equalsIgnoreCase(LOCK) ) { %>
  <shortdesc><bean:message key="ars.shortdesc.lock"/></shortdesc>
<% } else if ( formAction.equalsIgnoreCase(VIEW) ) { %>
  <shortdesc><bean:message key="ars.shortdesc.view"/></shortdesc>
<% } %>
</p>
<!-- End Short Description -->

<p>
<html:form action="/saveRowingSession">
<html:hidden property="action"/>

<!-- Header Row -->
<table cellpadding=0 cellspacing=0 border=0 width=632>
<tr>
<td width=125>
    <img src="images/spacer.gif" width=1 height=18></td>
<td bgcolor=ffcc66 width=1>
    <img src="images/spacer.gif" width=1 height=18></td>
<td bgcolor=ffcc66 width=85>
    <img src="images/spacer.gif" width=1 height=18></td>
<td bgcolor=ffcc66 width=10>
    <img src="images/spacer.gif" width=1 height=18></td>
<td bgcolor=ffcc66 width=95>
    <img src="images/spacer.gif" width=1 height=18></td>
<td bgcolor=ffcc66 width=190>
    <img src="images/spacer.gif" width=1 height=18></td>
<td bgcolor=ffcc66 width=1>
    <img src="images/spacer.gif" width=1 height=18></td>
<td width=125>
    <img src="images/spacer.gif" width=1 height=18></td>
</tr>
<!-- End Header Row -->

<% if ( !formAction.equalsIgnoreCase(CREATE)) { %>
<tr>
<td width=125><img src="images/spacer.gif" width=1 height=1></td>
<td bgcolor=ffcc66 align=center><BR></td>
<td align=right><b><bean:message key="form.rowingsessionid"/></b></td>
<td><br></td>
<td colspan=2>
<% if ( !formAction.equalsIgnoreCase(CREATE)) { %>
  <bean:write name="rowingsessionForm" property="rowingId"
             scope="request" filter="true"/>
  <html:hidden property="rowingId"/>
<% } %>
</td>
<td bgcolor=ffcc66><BR></td>
<td width=125><img src="images/spacer.gif" width=1 height=1></td>
</tr>

<tr>
<td width=125><img src="images/spacer.gif" width=1 height=1></td>
<td colspan=6 bgcolor=ffcc66><img src="images/spacer.gif" width=1 height=1></td>
<td width=125><img src="images/spacer.gif" width=1 height=1></td>
</tr>
<% } %>

<tr>
<td width=125><img src="images/spacer.gif" width=1 height=1></td>
<td bgcolor=ffcc66 align=center><BR></td>
<td align=right><b><bean:message key="table.label.date"/></b></td>
<td><br></td>
<td colspan=2>
<%
if ( formAction.equalsIgnoreCase(CREATE)
        || formAction.equalsIgnoreCase(EDIT) ) {
%>
  <html:select property="month">
  <html:options collection="months"
                property="value" labelProperty="label" />
  </html:select>
  <html:select property="day">
  <html:options collection="days"
                property="value" labelProperty="label" />
  </html:select>
  <html:select property="year">
  <html:options collection="years"
                property="value" labelProperty="label" />
  </html:select>
<% } else { %>
  <bean:write name="rowingsessionForm" property="date"
             scope="request" filter="true"/>
  <html:hidden property="date"/>
<% } %>
</td>
<td bgcolor=ffcc66><BR></td>
<td width=125><img src="images/spacer.gif" width=1 height=1></td>
</tr>

<tr>
<td width=125><img src="images/spacer.gif" width=1 height=1></td>
<td colspan=6 bgcolor=ffcc66><img src="images/spacer.gif" width=1 height=1></td>
<td width=125><img src="images/spacer.gif" width=1 height=1></td>
</tr>

<tr>
<td width=125><img src="images/spacer.gif" width=1 height=1></td>
<td bgcolor=ffcc66 align=center><BR></td>
<td align=right><b><bean:message key="table.label.time"/></b></td>
<td><br></td>
<td colspan=2>
<%
if ( formAction.equalsIgnoreCase(CREATE)
        || formAction.equalsIgnoreCase(EDIT) ) {
%>
  <html:select property="hour">
  <html:options collection="hours"
                property="value" labelProperty="label" />
  </html:select>
  <html:select property="minute">
  <html:options collection="minutes"
                property="value" labelProperty="label" />
  </html:select>
  <html:select property="amPm">
  <html:options collection="ampm"
                property="value" labelProperty="label" />
  </html:select>
<% } else { %>
  <bean:write name="rowingsessionForm" property="time"
             scope="request" filter="true"/>
  <html:hidden property="time"/>
<% } %>
</td>
<td bgcolor=ffcc66><BR></td>
<td width=125><img src="images/spacer.gif" width=1 height=1></td>
</tr>

<tr>
<td width=125><img src="images/spacer.gif" width=1 height=1></td>
<td colspan=6 bgcolor=ffcc66><img src="images/spacer.gif" width=1 height=1></td>
<td width=125><img src="images/spacer.gif" width=1 height=1></td>
</tr>

<%
if ( !formAction.equalsIgnoreCase(CREATE)
        && !formAction.equalsIgnoreCase(EDIT) ) {
%>
<tr>
<td width=125><img src="images/spacer.gif" width=1 height=1></td>
<td bgcolor=ffcc66 align=center><BR></td>
<td align=right><b><bean:message key="table.label.status"/></b></td>
<td><br></td>
<td colspan=2>
  <bean:write name="rowingsessionForm" property="state"
             scope="request" filter="true"/>
  <html:hidden property="rowingId"/>
</td>
<td bgcolor=ffcc66><BR></td>
<td width=125><img src="images/spacer.gif" width=1 height=1></td>
</tr>

<tr>
<td width=125><img src="images/spacer.gif" width=1 height=1></td>
<td colspan=6 bgcolor=ffcc66><img src="images/spacer.gif" width=1 height=1></td>
<td width=125><img src="images/spacer.gif" width=1 height=1></td>
</tr>
<% } %>

<tr>
<td width=125><img src="images/spacer.gif" width=1 height=1></td>
<td bgcolor=ffcc66 align=center><BR></td>
<td align=right><b><bean:message key="table.label.level"/></b></td>
<td><br></td>
<td>
<%
if ( formAction.equalsIgnoreCase(CREATE)
        || formAction.equalsIgnoreCase(EDIT) ) {
%>
  <html:select property="level">
  <html:options collection="rowinglevels"
                property="value" labelProperty="label" />
  </html:select>
<% } else { %>
  <bean:write name="rowingsessionForm" property="level"
             scope="request" filter="true"/>
  <html:hidden property="level"/>
<% } %>
</td>
<td>&nbsp;</td>
<td bgcolor=ffcc66><BR></td>
<td width=125><img src="images/spacer.gif" width=1 height=1></td>
</tr>

<tr>
<td width=125><img src="images/spacer.gif" width=1 height=1></td>
<td colspan=6 bgcolor=ffcc66><img src="images/spacer.gif" width=1 height=1></td>
<td width=125><img src="images/spacer.gif" width=1 height=1></td>
</tr>

<tr>
<td width=125><img src="images/spacer.gif" width=1 height=1></td>
<td bgcolor=ffcc66 align=center><BR></td>
<td align=right><b><bean:message key="table.label.type"/></b></td>
<td><br></td>
<td>
<%
if ( formAction.equalsIgnoreCase(CREATE)
        || formAction.equalsIgnoreCase(EDIT) ) {
%>
  <html:select property="type">
  <html:options collection="rowingtypes"
                property="value" labelProperty="label" />
  </html:select>
<% } else { %>
  <bean:write name="rowingsessionForm" property="type"
             scope="request" filter="true"/>
  <html:hidden property="type"/>
<% } %>
</td>
<td>&nbsp;</td>
<td bgcolor=ffcc66><BR></td>
<td width=125><img src="images/spacer.gif" width=1 height=1></td>
</tr>

<!-- Spacer row -->
<tr>
<td width=125><img src="images/spacer.gif" width=1 height=4></td>
<td bgcolor=ffcc66 width=1><img src="images/spacer.gif" width=1 height=4></td>
<td bgcolor=ffcc66 width=85><img src="images/spacer.gif" width=1 height=4></td>
<td bgcolor=ffcc66 width=10><img src="images/spacer.gif" width=1 height=4></td>
<td bgcolor=ffcc66 width=95><img src="images/spacer.gif" width=1 height=4></td>
<td bgcolor=ffcc66 width=190><img src="images/spacer.gif" width=1 height=4></td>
<td bgcolor=ffcc66 width=1><img src="images/spacer.gif" width=1 height=4></td>
<td width=125><img src="images/spacer.gif" width=1 height=4></td>
</tr>
<!-- End spacer row -->

<tr>
<td width=125><img src="images/spacer.gif" width=1 height=1></td>
<td bgcolor=ffcc66 align=center height=18><BR></td>
<td bgcolor=ffcc66 align=center>&nbsp;</td>
<td bgcolor=ffcc66><br></td>
<td bgcolor=ffcc66 align=center height=18>
<% if ( formAction.equalsIgnoreCase(SESSIONCANCEL) ) { %>
    <html:submit styleClass="button"><bean:message key="form.cancelsession"/></html:submit>
<% } else if ( formAction.equalsIgnoreCase(CREATE) ) { %>
    <%-- html:link page="/saveRowingSession.do?action=Create" --%>
    <html:submit styleClass="button"><bean:message key="table.label.create"/></html:submit>
<% } else if ( formAction.equalsIgnoreCase(DELETE) ) { %>
    <html:submit styleClass="button"><bean:message key="table.label.delete"/></html:submit>
<% } else if ( formAction.equalsIgnoreCase(EDIT) ) { %>
    <html:submit styleClass="button"><bean:message key="form.apply"/></html:submit>
<% } else if ( formAction.equalsIgnoreCase(PUBLISH) ) { %>
    <html:submit styleClass="button"><bean:message key="table.label.publish"/></html:submit>
<% } else if ( formAction.equalsIgnoreCase(LOCK) ) { %>
    <html:submit styleClass="button"><bean:message key="table.label.lock"/></html:submit>
<% } else if ( formAction.equalsIgnoreCase(VIEW) ) { %>
    &nbsp;
<% } %>
</td>
<td bgcolor=ffcc66 align=center height=18>
<% if ( formAction.equalsIgnoreCase(VIEW) ) { %>
    <html:cancel styleClass="button"><bean:message key="form.done"/></html:cancel>
<% } else if ( formAction.equalsIgnoreCase(SESSIONCANCEL) ) { %>
    <html:cancel styleClass="button"><bean:message key="form.donotcancelsession"/></html:cancel>
<% } else { %>
    <html:cancel styleClass="button"><bean:message key="form.cancel"/></html:cancel>
<% } %>
</td>
<td bgcolor=ffcc66 align=center height=18><BR></td>
<td width=125><img src="images/spacer.gif" width=1 height=1></td>
</tr>

<!-- Footer Row -->
<tr>
<td width=125><img src="images/spacer.gif" width=1 height=9></td>
<td bgcolor=ffcc66 width=1><img src="images/spacer.gif" width=1 height=9></td>
<td bgcolor=ffcc66 width=85><img src="images/spacer.gif" width=1 height=9></td>
<td bgcolor=ffcc66 width=10><img src="images/spacer.gif" width=1 height=9></td>
<td bgcolor=ffcc66 width=95><img src="images/spacer.gif" width=1 height=9></td>
<td bgcolor=ffcc66 width=190><img src="images/spacer.gif" width=1 height=9></td>
<td bgcolor=ffcc66 width=1><img src="images/spacer.gif" width=1 height=9></td>
<td width=125><img src="images/spacer.gif" width=1 height=9></td>
</tr>
</table>
<!-- End Footer Row -->

</html:form>

<p>

<!-- SESSION table -->
<table cellpadding=0 cellspacing=0 border=0 width=647>

<!-- NAV ROW -->
<tr>
    <td width=1 bgcolor=ffcc66><img src="images/spacer.gif" width=1 height=18></td>
    <td width=215  bgcolor=ffcc66 align=left>&nbsp;&nbsp;<b>Starboard</b></td>
    <td width=215  bgcolor=ffcc66 align=left>&nbsp;&nbsp;<b>Port</b></td>
    <td width=215  bgcolor=ffcc66 align=left>&nbsp;&nbsp;<b>Coxswain</b></td>
    <td width=1 bgcolor=ffcc66><img src="images/spacer.gif" width=1 height=18></td>
</tr>
<!-- END NAV ROW -->

<%
 java.util.Iterator iterStarbd = rowingsessionForm.getStarboards();
 java.util.Iterator iterPort = rowingsessionForm.getPorts();
 java.util.Iterator iterCox = rowingsessionForm.getCoxswains();

 boolean anotherStarbd = iterStarbd.hasNext();
 boolean anotherPort = iterPort.hasNext();
 boolean anotherCox = iterCox.hasNext();
 boolean anotherRow = anotherStarbd || anotherPort || anotherCox ;

 int rowCount = 0;

 while( anotherRow ) {
   ++rowCount;

   String starbd = "&nbsp;";
   if ( anotherStarbd ) {
     Object o = iterStarbd.next();
     com.clra.web.Participant2View p2v = (com.clra.web.Participant2View) o;
     starbd = com.clra.web.ParticipationForm.formatParticipantAsString( p2v );
   }

   String port = "&nbsp;";
   if ( anotherPort ) {
     Object o = iterPort.next();
     com.clra.web.Participant2View p2v = (com.clra.web.Participant2View) o;
     port = com.clra.web.ParticipationForm.formatParticipantAsString( p2v );
   }

   String cox = "&nbsp;";
   if ( anotherCox ) {
     Object o = iterCox.next();
     com.clra.web.Participant2View p2v = (com.clra.web.Participant2View) o;
     cox = com.clra.web.ParticipationForm.formatParticipantAsString( p2v );
   }

   anotherStarbd = iterStarbd.hasNext();
   anotherPort = iterPort.hasNext();
   anotherCox = iterCox.hasNext();
   anotherRow = anotherStarbd || anotherPort || anotherCox ;

%>
<!-- participant row -->
<tr>
<td bgcolor=ffcc66 align=center><BR></td>
<td>&nbsp;&nbsp;<%=starbd%></td>
<td>&nbsp;&nbsp;<%=port%></td>
<td>&nbsp;&nbsp;<%=cox%></td>
<td bgcolor=ffcc66><BR></td>
</tr>
<tr>
<td colspan=5 bgcolor=ffcc66>
  <img src="images/spacer.gif" width=1 height=1></td>
</tr>
<!-- end participant row -->
<%
 } /* while */
 if ( rowCount == 0 ) {
%>
<tr>
<td bgcolor=ffcc66 align=center><BR></td>
<td>&nbsp;&nbsp;</td>
<td>&nbsp;&nbsp;</td>
<td>&nbsp;&nbsp;</td>
<td bgcolor=ffcc66><BR></td>
</tr>
<tr>
<td colspan=5 bgcolor=ffcc66>
  <img src="images/spacer.gif" width=1 height=1></td>
</tr>
<% } /* if */ %>

<!-- footer -->
<tr>
<td colspan=5 bgcolor=ffcc66>
  <img src="images/spacer.gif" width=1 height=17></td>
</tr>
<!-- end footer -->

</table>
<!-- END SESSION table -->

</body>
</html:html>

