<%
/*
 * Copyright (c) Carnegie Lake Rowing Association 2002. All rights reserved.
 * Distributed under the GPL license. See doc/COPYING.
 * $RCSfile: memberlist.jsp,v $
 * $Date: 2003/03/13 17:46:42 $
 * $Revision: 1.16 $
 */
%>

<%@ page language= "java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/clra.tld" prefix="clra" %>


<%@ page import= "com.clra.member.Address" %>
<%@ page import= "com.clra.member.MemberName" %>
<%@ page import= "com.clra.member.Telephone" %>
<%@ page import= "com.clra.rowing.RowingSessionState" %>
<%@ page import= "com.clra.web.FormattedDate" %>
<%@ page import= "com.clra.web.MemberSet" %>
<%@ page import= "com.clra.web.MemberView" %>
<%@ page import= "com.clra.web.MonthViewSelectorTag" %>
<%@ page import= "com.clra.web.NameSelectorTag" %>
<%@ page import= "java.util.Date" %>
<%@ page import= "java.util.Iterator" %>
<%@ page import= "java.util.Map" %>


<html:html locale="false">

<head>
<title>LakerList</title>

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
  Integer group = NameSelectorTag.groupFromContext(pageContext);
  NameSelectorTag.resetGroupInContexts(pageContext,group);
%>


<!-- table to set text boundaries same as list boundaries -->
<a name="textTable"/>
<table cellpadding=0 cellspacing=0 border=0 width=647>

<!-- menu options -->

<tr>
<td width=1 height=12>&nbsp;</td>
<td width=322 align=left>&nbsp;&nbsp;</td>
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

  <html:link page="/help/memberlisthelp.jsp">
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
<td colspan=3 width=322 align=left>
  <p><FONT class=header16>
  Carnegie Lake Rowing Association
  </FONT></p></td>
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
  LakerList
</FONT>
<td width=1>&nbsp;</td>
</tr>
<!-- end row for page subtitle -->


<!-- text spacer -->
<tr><td colspan=5 height=16>&nbsp;</td></tr>
<!-- end text spacer -->


<!-- row for short description -->
<tr>
<td width=1>&nbsp;</td>
<td colspan=3>
Our Membership Directory
</td>
<td width=1>&nbsp;</td>
</tr>
<!-- end row for short description -->


<!-- text spacer -->
<tr><td colspan=5 height=16>&nbsp;</td></tr>
<!-- end text spacer -->


<!-- row for long description -->
<tr>
<td width=1>&nbsp;</td>
<td colspan=3>
<p>This directory and the information herein are for official club use only. This directory is provided to the members for their convenience and should be used only as a reference to contact other club members for efficiency of club operations. Use of this directory and the information herein by any persons for mailings and/or other solicitations without written authorization by an officer of Carnegie Lake Rowing Association, Inc, is prohibited. The members of Carnegie Lake Rowing Association, Inc. have given the club permission to use this information for official club use only and not as an invitation for unwanted mail and/or other solicitations. Please respect the wishes and privacy of our members.</p>

<p>Questions about this policy should be sent to
<a href="mailto:info@clra.com">info@clra.com</a>.</p>
<p>Please check your information for accuracy.&nbsp;Use the "Manage your account" link on the Welcome page to make corrections.</p>

<p></p>

<p><small>TELEPHONE: E = Evening / D = Daytime / M = Mobile </small></p>
<p><small>YEAR:  LTR = Learn-To-Row / GREEN = Novice </small></p>
</td>
<td width=1>&nbsp;</td>
</tr>
<!-- end row for long description -->


<!-- text spacer -->
<tr><td colspan=5 height=8>&nbsp;</td></tr>
<!-- end text spacer -->


<!-- START nameSelector -->
<tr>
<td width=1>&nbsp;</td>
<td colspan=3>
  <%-- FIXME hard-coded text --%>
  <FONT class=header16>NAME</FONT>&nbsp;
  <b><FONT class=textred>[</FONT>
  <clra:nameSelector currentGroup="<%=group%>"/>
  <FONT class=textred>]</FONT></b>
</td>
<td width=1>&nbsp;</td>
</tr>
<!-- end nameSelector -->


<!-- text spacer -->
<tr><td colspan=5 height=8>&nbsp;</td></tr>
<!-- end text spacer -->


</table>
<!-- end table to set text boundaries same as list boundaries -->


<!-- member table -->
<a name="memberTable"/>
<table cellpadding=0 cellspacing=0 border=0 width=647>

  <!-- Header of member table -->
  <tr>
  <td width=1 bgcolor=ffcc66>
       <img src="images/spacer.gif" width=1 height=1></td>
  <td width=200 bgcolor=ffcc66>
       <img src="images/spacer.gif" width=1 height=1></td>
  <td width=1 bgcolor=ffcc66>
       <img src="images/spacer.gif" width=1 height=1></td>
  <td width=105 bgcolor=ffcc66>
       <img src="images/spacer.gif" width=1 height=1></td>
  <td width=1 bgcolor=ffcc66>
       <img src="images/spacer.gif" width=1 height=1></td>
  <td width=218 bgcolor=ffcc66>
       <img src="images/spacer.gif" width=1 height=1></td>
  <td width=1 bgcolor=ffcc66>
       <img src="images/spacer.gif" width=1 height=1></td>
  <td width=115 bgcolor=ffcc66>
       <img src="images/spacer.gif" width=1 height=1></td>
  <td width=1 bgcolor=ffcc66>
       <img src="images/spacer.gif" width=1 height=1></td>
  <td width=40  bgcolor=ffcc66>
       <img src="images/spacer.gif" width=1 height=1></td>
  <td width=1 bgcolor=ffcc66>
       <img src="images/spacer.gif" width=1 height=1></td>
  </tr>

  <tr>
  <td width=1 bgcolor=ffcc66>
       <br></td>
  <td width=200 bgcolor=ffcc66 height="18" align=center>
    <strong>NAME / EMAIL</strong>
  </td>
  <td width=1 bgcolor=ffcc66><img src="images/spacer.gif" width=1></td>
  <td width=105 align="center" bgcolor=ffcc66>
    <strong>TELEPHONE</strong><br>
  </td>
  <td width=1 bgcolor=ffcc66><br></td>
  <td width=218 align=center bgcolor=ffcc66>
  <strong>ADDRESS</strong>
  </td>
  <td width=1 bgcolor=ffcc66><br></td>
  <td width=155 align=center bgcolor=ffcc66>
  <strong>USER NAME</strong>
  </td>
  <td width=1 bgcolor=ffcc66><br></td>
  <td width="40" align="center" bgcolor=ffcc66>
    <b>YR</b></td>
  <td width=1 bgcolor=ffcc66>
       <br></td>
  </tr>
  <!-- End of header of member table -->


  <!-- Member iteration -->
  <jsp:useBean id="members" class="com.clra.web.MemberSet">
    <jsp:setProperty name="members" property="group" value="<%=group%>"/>
  </jsp:useBean>

  <% int rowCount = 0; %>
  <logic:iterate id="member" name="members" property="iterator"
    type="com.clra.web.MemberView">

  <!-- Member row -->
  <%
  boolean isMember =
      member.getAccountTypeStr().equalsIgnoreCase("FULL")
   || member.getAccountTypeStr().equalsIgnoreCase("NOVICE")
   || member.getAccountTypeStr().equalsIgnoreCase("LTR");

  if ( isMember ) {
    ++rowCount;
  %>

    <jsp:useBean id="rowFlag" class="java.lang.Object"/>

    <% if (rowCount%2 != 0) { %>
    <!-- Odd numbered rows have a white background -->
    <tr>
    <% } else { %>
    <!-- Even numbered rows have a shaded background -->
    <tr bgcolor=fff8cc>
    <% } /* if rowCount */ %>

    <td width=1 bgcolor=ffcc66><br></td>

    <td width=200>&nbsp;
    <%
    MemberName name = member.getMemberName();
    if ( member.hasEmail() ) {
    %>
      <a href="mailto:<%=member.getEmail()%>">
    <% } /*if hasEmail()*/ %>

    <%=member.getMemberName().getFirstName()%>

    <% if ( name.hasMiddleName() ) { %>
      &nbsp;<%=member.getMemberName().getMiddleName()%>
    <% } %>

    &nbsp;<%=member.getMemberName().getLastName()%>

    <% if ( name.hasSuffix() ) { %>
      &nbsp;<%=member.getMemberName().getSuffix()%>
    <% } %>

    <% if ( member.hasEmail() ) { %>
      </a>
      <br>&nbsp;<font color="#008040">&nbsp;<%=member.getEmail()%></font>
    <% } /*if hasEmail()*/ %>

    </td>

    <td width=1 bgcolor=ffcc66><br></td>

    <td width=105 align=center>
      <%
      Map map = member.getTelephoneNumbers();
      Telephone evening = (Telephone) map.get( Telephone.EVENING );
      Telephone day = (Telephone) map.get( Telephone.DAY );
      Telephone other = (Telephone) map.get( Telephone.OTHER );
      %>
      <% if ( evening != null ) { %>
        <em>E</em>&nbsp;<%=member.getEveningPhoneStr()%>
      <% } if ( day != null ) { %>
        <br><em>D</em>&nbsp;<%=member.getDayPhoneStr()%>
      <% } if ( other != null ) { %>
        <br><em>M</em>&nbsp;<%=member.getOtherPhoneStr()%>
      <% } %>
    </td>

    <td width=1 bgcolor=ffcc66><br></td>

    <td width=218>
      <%
      Address address = member.getAddress();
      String street1 = address.getStreet1();
      String street2 = address.getStreet2();
      String city = address.getCity();
      String state = address.getState();
      String zip = address.getZip();
      %>

      <% if ( street1 != null ) { %>
        &nbsp;<%=street1%><br>&nbsp;&nbsp;
      <% } if ( street2 != null ) { %>
        <%=street2%><br>&nbsp;&nbsp;
      <% } if ( city != null ) { %>
        <%=city%>&nbsp;
      <% } if ( state != null ) { %>
        <%=state%><br>&nbsp;&nbsp;
      <% } if ( zip != null ) { %>
        <%=zip%>
      <% } %>
    </td>

    <td width=1 bgcolor=ffcc66><br></td>

    <td width=115 align="center">&nbsp;
    <%=member.getAccountName()%>
    </td>

    <td width=1 bgcolor=ffcc66><br></td>

    <%
      String status = member.getAccountTypeStr();
      if ( status.equalsIgnoreCase("NOVICE") ) {
    %>
    <td width="40" bgcolor="#99FF99" align="center">
    <% } else { %>
    <td width="40" align="center">
    <% }
      Date accountYear = member.getAccountYear();
      int year = accountYear.getYear();
      if ( year > 99 ) {
        year -= 100;
      }
      String prettyYear = "" + year;
      if ( year < 10 ) {
        prettyYear = "0" + year;
      }
      %>
      <% if ( status.equalsIgnoreCase("LTR") ) { %>
        LTR
      <% } else { %>
        <%=prettyYear%>
      <% } %>
    </td>

    <td width=1 bgcolor=ffcc66><br></td>

    </tr>
    <tr>
      <td colspan=11 width=1 height=1 bgcolor=ffcc66>
           <img src="images/spacer.gif" width=1 height=1></td>
    </tr>
  <% } /* end if isMember */ %>
  <!-- End of Member row -->

  </logic:iterate>
  <!-- End of Member iteration -->


  <!-- Empty row (if iteration returned no rows) -->
  <logic:notPresent name="rowFlag">
    <tr>
    <td width=1 bgcolor=ffcc66><br></td>
    <td width=200 height="18" align=center>&nbsp;</td>
    <td width=1 bgcolor=ffcc66><br></td>
    <td width=105 align="center">&nbsp;</td>
    <td width=1 bgcolor=ffcc66><br></td>
    <td width=263 align=center>&nbsp;</td>
    <td width=1 bgcolor=ffcc66><br></td>
    <td width="40" align="center">&nbsp;</td>
    <td width=1 bgcolor=ffcc66><br></td>
    </tr>
  </logic:notPresent>
  <!-- End empty row -->


  <!-- Footer -->
  <tr>
  <td width=1 bgcolor=ffcc66><br></td>
  <td width=200 bgcolor=ffcc66 height="18" align=center>
    <strong>NAME / EMAIL</strong>
  </td>
  <td width=1 bgcolor=ffcc66><br></td>
  <td width=105 align="center" bgcolor=ffcc66>
    <strong>TELEPHONE</strong><br></td>
  <td width=1 bgcolor=ffcc66><br></td>
  <td width=263 align=center bgcolor=ffcc66>
    <strong>ADDRESS</strong></td>
  <td width=1 bgcolor=ffcc66><br></td>
  <td width=155 align=center bgcolor=ffcc66>
    <strong>USER NAME</strong></td>
  <td width=1 bgcolor=ffcc66><br></td>
  <td width="40" align="center" bgcolor=ffcc66>
    <b>YR</b></td>
  <td width=1 bgcolor=ffcc66><br></td>
  </tr>
  <!-- End of footer -->


</table>
<!-- end member table -->


<table border="0" cellpadding="0" cellspacing="0" width=647>
  <tr>
  <td><p align="center">&nbsp;</p>
  <p align="center"><small><small>
  This web page was last updated on November 05, 2001.<br>
  © Copyright 1999 Carnegie Lake Rowing Association. All Rights Reserved.</small></small></p>
  </td>
  </tr>
</table>

</body>
</html:html>

