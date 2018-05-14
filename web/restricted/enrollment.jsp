<%
/*
 * Copyright (c) Carnegie Lake Rowing Association 2002. All rights reserved.
 * Distributed under the GPL license. See doc/COPYING.
 * $RCSfile: enrollment.jsp,v $
 * $Date: 2003/03/13 17:46:40 $
 * $Revision: 1.5 $
 */
%>

<%@ page language= "java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/clra.tld" prefix="clra" %>

<jsp:useBean id="rowingId" scope="session" type="java.lang.Integer" />
<jsp:useBean id="participationForm"
             scope="request" type="com.clra.web.ParticipationForm" />

<bean:define id="sessionDate" name="participationForm" property="date" type="java.lang.String" />

<bean:define id="sessionDateTime" name="participationForm" property="dateTime" type="java.lang.String" />

<bean:define id="formAction" name="participationForm" property="action"
             scope="request" type="java.lang.String"/>

<bean:define id="CREATE" value="<%=com.clra.web.ParticipationForm.CREATE%>" />
<bean:define id="EDIT" value="<%=com.clra.web.ParticipationForm.EDIT%>" />
<bean:define id="VIEW" value="<%=com.clra.web.ParticipationForm.VIEW%>" />

<bean:define id="STARBOARD"
      value="<%=com.clra.rowing.SeatPreference.NAME_STARBOARD%>" />
<bean:define id="PORT"
      value="<%=com.clra.rowing.SeatPreference.NAME_PORT%>" />
<bean:define id="SP"
      value="<%=com.clra.rowing.SeatPreference.NAME_STARBOARD_THEN_PORT%>" />
<bean:define id="PS"
      value="<%=com.clra.rowing.SeatPreference.NAME_PORT_THEN_STARBOARD%>" />
<bean:define id="COX"
      value="<%=com.clra.rowing.SeatPreference.NAME_COX%>" />

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

<!-- table to set text boundaries same as list boundaries -->
<a name="textTable"/>
<table cellpadding=0 cellspacing=0 border=0 width=647>

<%-- Remove help link for now: request params may get screwed up ...
<!-- menu options -->
<tr>
<td width=1 height=12>&nbsp;</td>
<td width=322 align=left>
  &nbsp;&nbsp;
</td>
<td width=1>&nbsp;</td>
<td width=322 align=right>
  <html:link page="/restricted/enrollmentlist.jsp">
    <bean:message key="page.back"/></html:link>
  &nbsp;&nbsp;
  <html:link page="/help/enrollmenthelp.jsp">
    <bean:message key="page.help"/></html:link>
</td>
<td width=1>&nbsp;</td>
</tr>
<tr><td colspan=5 height=1 bgcolor=ffcc66>
      <img src="images/spacer.gif" width=1 height=1></td></tr>
<tr><td colspan=5 height=8>
      <img src="images/spacer.gif" width=1 height=1></td></tr>
<!-- end menu options -->
 end remove help link --%>

<%-- row for pagetitle and help --%>
<tr>
<td width=1>&nbsp;</td>
<td width=322 align=left>
  <%-- FIXME hardcoded text --%>
  <p><FONT class=header16><clra:member/>'s Laker Account</FONT></p></td>
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
<FONT class=header16>
<bean:message key="mps.pagesubtitle1" arg0="<%=sessionDate%>" />
</FONT>
<td width=1>&nbsp;</td>
</td>
</tr>

<%-- text spacer --%>
<tr><td colspan=5 height=16>&nbsp;</td></tr>

<%-- row for short description --%>
<tr>
<td width=1>&nbsp;</td>
<td colspan=3>
<jsp:useBean id="thisDate" class="com.clra.web.FormattedDate" />
<bean:message key="mps.shortdesc"
  arg0="<%=thisDate.getValue()%>" arg1="<%=sessionDate%>" />
</td>
<td width=1>&nbsp;</td>
</td>
</tr>

<%-- text spacer --%>
<tr><td colspan=5 height=16>&nbsp;</td></tr>

<%-- row for long description --%>
<tr>
<td width=1>&nbsp;</td>
<td colspan=3>
<bean:message key="mps.longdesc"/>
</td>
<td width=1>&nbsp;</td>
</td>
</tr>

<%-- text spacer --%>
<tr><td colspan=5 height=16>&nbsp;</td></tr>

<%-- row for form title --%>
<tr>
<td width=1>&nbsp;</td>
<td colspan=3>
<FONT class=header16>
<bean:message key="mps.formtitle1" arg0="<%=sessionDateTime%>" />
</FONT>
</td>
<td width=1>&nbsp;</td>
</td>
</tr>

<%-- text spacer --%>
<tr><td colspan=5 height=16>&nbsp;</td></tr>

<%-- end table to set text boundaries same as list boundaries --%>
</table>

<!-- ROLE form/table -->
<html:form action="/saveParticipation">
<html:hidden property="action"/>

<table cellpadding=0 cellspacing=0 border=0 width=647>
<!-- <FONT class=textform> -->

<!-- NAV ROW -->
<tr>
<td width=125><img src="images/spacer.gif" width=1 height=1></td>
<td width=1   bgcolor=ffcc66><img src="images/spacer.gif" width=1 height=1></td>
<td width=40  bgcolor=ffcc66><img src="images/spacer.gif" width=1 height=1></td>
<td width=105 bgcolor=ffcc66><img src="images/spacer.gif" width=1 height=1></td>
<td width=105 bgcolor=ffcc66><img src="images/spacer.gif" width=1 height=1></td>
<td width=105 bgcolor=ffcc66><img src="images/spacer.gif" width=1 height=1></td>
<td width=40  bgcolor=ffcc66><img src="images/spacer.gif" width=1 height=1></td>
<td width=1   bgcolor=ffcc66><img src="images/spacer.gif" width=1 height=1></td>
<td width=125><img src="images/spacer.gif" width=1 height=1></td>
</tr>

<tr>
<td></td>
<td bgcolor=ffcc66>
  <img src="images/spacer.gif" width=1 height=18></td>
<td  bgcolor=ffcc66 align=center>&nbsp;</td>
<td  bgcolor=ffcc66 align=left>&nbsp;</td>
<td colspan=3 bgcolor=ffcc66 align=left>&nbsp;</td>
<td bgcolor=ffcc66>
    <img src="images/spacer.gif" width=1 height=18></td>
<td></td>
</tr>
<!-- END NAV ROW -->

<!-- ROLE: NOT SIGNED UP -->
<tr>
<td></td>
<td bgcolor=ffcc66><img src="images/spacer.gif" width=1 height=18></td>
<td align=center>
  <%-- input type=radio name="role" value="no_attend" checked --%>
  <% if ( !formAction.equalsIgnoreCase(EDIT) ) { %>
  <html:radio property="seatPreference" value="" disabled="true" />
  <% } else { %>
  <html:radio property="seatPreference" value="" />
  <% } %>
</td>
<td>
  <% if ( !formAction.equalsIgnoreCase(EDIT) ) { %>
    Not signed up.
  <% } else { %>
    Cancel enrollment.
  <% } %>
</td>
<td colspan=3>&nbsp;</td>
<td bgcolor=ffcc66><img src="images/spacer.gif" width=1 height=18></td>
<td></td>
</tr>

<tr>
<td></td>
<td colspan=7 bgcolor=ffcc66><img src="images/spacer.gif" height=1></td>
<td></td>
</tr>
<!-- END NOT SIGNED UP -->

<!-- ROLE: STARBOARD -->
<tr>
<td></td>
<td bgcolor=ffcc66><img src="images/spacer.gif" width=1 height=18></td>
<td align=center>
  <%-- input type=radio name="role" value="starboard" --%>
  <% if ( !formAction.equalsIgnoreCase(VIEW) ) { %>
  <html:radio property="seatPreference" value="<%=STARBOARD%>" />
  <% } else { %>
  <html:radio property="seatPreference" value="<%=STARBOARD%>" disabled="true"/>
  <% } %>
</td>
<td>Starboard</td>
<td colspan=3></td>
<td bgcolor=ffcc66><img src="images/spacer.gif" width=1 height=18></td>
<td></td>
</tr>

<tr>
<td></td>
<td colspan=7 bgcolor=ffcc66><img src="images/spacer.gif" height=1></td>
<td></td>
</tr>
<!-- END STARBOARD -->

<!-- ROLE: STARBOARD/PORT -->
<tr>
<td></td>
<td bgcolor=ffcc66><img src="images/spacer.gif" width=1 height=18></td>
<td align=center>
  <%-- input type=radio name="role" value="S/P" --%>
  <% if ( !formAction.equalsIgnoreCase(VIEW) ) { %>
  <html:radio property="seatPreference" value="<%=SP%>" />
  <% } else { %>
  <html:radio property="seatPreference" value="<%=SP%>" disabled="true" />
  <% } %>
</td>
<td>Starboard / P</td>
<td colspan=3>Prefer starboard, but will row port.</td>
<td bgcolor=ffcc66><img src="images/spacer.gif" width=1 height=18></td>
<td></td>
</tr>

<tr>
<td></td>
<td colspan=7 bgcolor=ffcc66><img src="images/spacer.gif" height=1></td>
<td></td>
</tr>
<!-- END STARBOARD/PORT -->

<!-- ROLE: PORT -->
<tr>
<td></td>
<td bgcolor=ffcc66><img src="images/spacer.gif" width=1 height=18></td>
<td align=center>
  <%-- input type=radio name="role" value="port" --%>
  <% if ( !formAction.equalsIgnoreCase(VIEW) ) { %>
  <html:radio property="seatPreference" value="<%=PORT%>" />
  <% } else { %>
  <html:radio property="seatPreference" value="<%=PORT%>" disabled="true" />
  <% } %>
</td>
<td>Port</td>
<td colspan=3></td>
<td bgcolor=ffcc66><img src="images/spacer.gif" width=1 height=18></td>
<td></td>
</tr>

<tr>
<td></td>
<td colspan=7 bgcolor=ffcc66><img src="images/spacer.gif" height=1></td>
<td></td>
</tr>
<!-- END PORT -->

<!-- ROLE: PORT/STARBOARD -->
<tr>
<td></td>
<td bgcolor=ffcc66><img src="images/spacer.gif" width=1 height=18></td>
<td align=center>
  <%-- input type=radio name="role" value="P/S" --%>
  <% if ( !formAction.equalsIgnoreCase(VIEW) ) { %>
  <html:radio property="seatPreference" value="<%=PS%>" />
  <% } else { %>
  <html:radio property="seatPreference" value="<%=PS%>" disabled="true" />
  <% } %>
</td>
<td>Port / S</td>
<td colspan=3>Prefer port, but will row starboard.</td>
<td bgcolor=ffcc66><img src="images/spacer.gif" width=1 height=18></td>
<td></td>
</tr>

<tr>
<td></td>
<td colspan=7 bgcolor=ffcc66><img src="images/spacer.gif" height=1></td>
<td></td>
</tr>
<!-- END PORT/STARBOARD -->

<!-- ROLE: COXSWAIN -->
<tr>
<td></td>
<td bgcolor=ffcc66><img src="images/spacer.gif" width=1 height=18></td>
<td align=center>
  <%-- input type=radio name="role" value="cox" --%>
  <% if ( !formAction.equalsIgnoreCase(VIEW) ) { %>
  <html:radio property="seatPreference" value="<%=COX%>" />
  <% } else { %>
  <html:radio property="seatPreference" value="<%=COX%>" disabled="true" />
  <% } %>
</td>
<td>Coxswain</td>
<td colspan=3>&nbsp;</td>
<td bgcolor=ffcc66><img src="images/spacer.gif" width=1 height=18></td>
<td></td>
</tr>

<tr>
<td></td>
<td colspan=7 bgcolor=ffcc66><img src="images/spacer.gif" height=1></td>
<td></td>
</tr>
<!-- END COXSWAIN -->

<!-- ROLE: BUTTONS -->
<tr>
<td width=125></td>
<td width=1  bgcolor=ffcc66>
    <img src="images/spacer.gif" width=1 height=18></td>
<td width=40 bgcolor=ffcc66 align=center>&nbsp;</td>
<% if ( formAction.equalsIgnoreCase(VIEW) ) { %>
<td width=315 colspan=3 bgcolor=ffcc66 align=center height=18>
    <html:cancel styleClass="button">
      <bean:message key="form.done"/></html:cancel>
</td>
<% } else { %>
<td width=155 bgcolor=ffcc66 align=center height=18>
<% if ( formAction.equalsIgnoreCase(CREATE) ) { %>
    <html:submit styleClass="button">
      <bean:message key="mps.submit.signup"/></html:submit>
<% } else if ( formAction.equalsIgnoreCase(EDIT) ) { %>
    <html:submit styleClass="button">
      <bean:message key="form.submit.update"/></html:submit>
<% } %>
</td>
<td width=5 bgcolor=ffcc66 align=center height=18>&nbsp;</td>
<td width=155 bgcolor=ffcc66 align=center height=18>
<% if ( formAction.equalsIgnoreCase(CREATE) ) { %>
    <html:cancel styleClass="button">
      <bean:message key="mps.cancel.signup"/></html:cancel>
<% } else if ( formAction.equalsIgnoreCase(EDIT) ) { %>
    <html:submit styleClass="button">
      <bean:message key="form.cancel.update"/></html:submit>
<%
   } /* else EDIT  */
   } /* else !VIEW */
%>
<td width=40 bgcolor=ffcc66 align=center>&nbsp;</td>
<td width=1 bgcolor=ffcc66>
    <img src="images/spacer.gif" width=1 height=18></td>
<td width=125></td>
</tr>

<tr>
<td width=125><img src="images/spacer.gif" width=1 height=1></td>
<td bgcolor=ffcc66 width=1><img src="images/spacer.gif" width=1 height=1></td>
<td bgcolor=ffcc66 width=40><img src="images/spacer.gif" width=1 height=1></td>
<td bgcolor=ffcc66 width=105><img src="images/spacer.gif" width=1 height=1></td>
<td bgcolor=ffcc66 width=105><img src="images/spacer.gif" width=1 height=1></td>
<td bgcolor=ffcc66 width=105><img src="images/spacer.gif" width=1 height=1></td>
<td bgcolor=ffcc66 width=40><img src="images/spacer.gif" width=1 height=1></td>
<td bgcolor=ffcc66 width=1><img src="images/spacer.gif" width=1 height=1></td>
<td width=125><img src="images/spacer.gif" width=1 height=1></td>
</tr>

<!-- tr>
<td width=125></td>
<td colspan=7 bgcolor=ffcc66><img src="images/spacer.gif" width=1 height=1></td>
<td width=125></td>
</TR -->
<!-- END BUTTONS -->


</table>
<!-- END ROW table -->

</html:form>

<P>
<P>

<p>
<FONT class=header16>
<bean:message key="mps.tabletitle" arg0="<%=sessionDate%>" />
</FONT>
</p>

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
 java.util.Iterator iterStarbd = participationForm.getStarboards();
 java.util.Iterator iterPort = participationForm.getPorts();
 java.util.Iterator iterCox = participationForm.getCoxswains();

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

