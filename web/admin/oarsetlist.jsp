<%
/*
 * Copyright (c) Carnegie Lake Rowing Association 2002. All rights reserved.
 * Distributed under the GPL license. See doc/COPYING.
 * $RCSfile: oarsetlist.jsp,v $
 * $Date: 2003/02/26 03:38:47 $
 * $Revision: 1.3 $
 */
%>

<%@ page language= "java" %>
<%@ page import= "com.clra.web.OarsetSet" %>
<%@ page import= "com.clra.rowing.OarsetView" %>
<%@ page import= "java.util.Iterator" %>
<%@ page import= "java.util.Map" %>

<HTML>

<HEAD>
<TITLE></TITLE>
<link rel="stylesheet" type="text/css" href="../stylesheet.css" title="Style">
</HEAD>

<body bgcolor="#ffffff" text="#333333" link="#003366" alink="#3399cc" vlink="#3399cc" topmargin="0" leftmargin="8" marginheight="1" marginwidth="8">
<p>
<FONT class=header16>CLRA Administration</FONT>
</p> 

<p> <FONT class=header16>Set up oarsets</FONT> &nbsp; </p> 

<p>This page shows the oarsets used by the club.</p>

<p>You may add a new oarset by clicking on "Add".<p>

<p>You may modify the status of a oarset, or attach a note to it, by clicking on that oarset.
</p>

<p><FONT class=header16>OARSETS</FONT></p>

<!-- OARSET TABLE -->
<TABLE cellpadding=0 cellspacing=0 border=0 width=647>

<!-- HEADER OF OARSET TABLE -->
<TR>
    <TD width=1 bgcolor=ffcc66><img src="images/spacer.gif" width=1 height=18></TD>
    <TD width=107  bgcolor=ffcc66 align=center><b>Status</b></TD>
    <TD width=165  bgcolor=ffcc66 align=left><b>Name</b></TD>
    <TD width=68  bgcolor=ffcc66 align=center><b>Size</b></TD>
    <TD width=81  bgcolor=ffcc66 align=left><b>Type</b></TD>
    <TD width=224 bgcolor=ffcc66 align=left><b>Notes</b></TD>
    <TD width=1 bgcolor=ffcc66><img src="images/spacer.gif" width=1 height=18></TD>
</TR>
<!-- END HEADER -->

    <!-- OARSET ITERATION -->
    <%
    OarsetSet list = OarsetSet.findAllActiveOarsets();
    Iterator iter = list.iterator();
    %>

    <!-- Oarset row -->
    <%
    while ( iter.hasNext() ) {
      OarsetView bv = (OarsetView) iter.next();
    %>
      <tr>
      <td bgcolor=ffcc66 align=center><br></td>
      <td align=center>&nbsp;</td>
      <td><a href=#><%=bv.getName()%></a></td>
      <td align=center><%=bv.getSize()%></td>
      <td><%=bv.getType()%></td>
      <td>&nbsp;</td>
      <td bgcolor=ffcc66><br></td>
      </tr>

      <tr>
      <td colspan=7 bgcolor=ffcc66>
        <img src="images/spacer.gif" width=1 height=1></td>
      </tr>
    <% } /*while*/ %>
    <!-- End of Oarset row -->

<!-- BUTTONS -->
<TR>
<TD colspan=4 bgcolor=ffcc66 align=center height=38>
<!--
  <img src="images/spacer.gif" width=1 height=38></TD>
 -->
  <b>&nbsp;&nbsp;&nbsp;<a href="Zzz">Logout</a>&nbsp;&nbsp;&nbsp;
  </b></TD>
<TD colspan=4 bgcolor=ffcc66 align=center height=38>
  <b>&nbsp;&nbsp;&nbsp;<a href="Zzz">Return to My Account</a>&nbsp;&nbsp;&nbsp;
  </b></TD>
</TR>

<TR>
<TD colspan=8 bgcolor=ffcc66><img src="images/spacer.gif" width=1 height=1></TD>
</TR>
<!-- END BUTTONS -->

</TABLE>
<!-- END OARSET TABLE -->

</BODY>
</HTML>
