<%
/*
 * Copyright (c) Carnegie Lake Rowing Association 2002. All rights reserved.
 * Distributed under the GPL license. See doc/COPYING.
 * $RCSfile: welcome.jsp,v $
 * $Date: 2003/02/26 03:38:47 $
 * $Revision: 1.3 $
 */
%>

<%@ page language= "java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/clra.tld" prefix="clra" %>
<%@ page import="javax.servlet.*" %>

<html:html locale="false">
<head>
<title>Welcome to CLRA</title>

<html:base/>
<link rel="stylesheet" type="text/css" href="stylesheet.css" title="Style">

</head>

<body bgcolor="#ffffff" text="#333333" link="#003366" alink="#3399cc" vlink="#3399cc" topmargin="0" leftmargin="8" marginheight="1" marginwidth="8">

<logic:notPresent name="org.apache.struts.action.MESSAGE" scope="application">
  <font color="red">
    ERROR:  Application resources not loaded -- check servlet container
    logs for error messages.
  </font>
</logic:notPresent>

  <% String firstname = (String)request.getAttribute("firstname");
  %>

  <p><FONT color=red class=header16>Welcome <%=firstname%></FONT>
  <p>Thanks for submitting membership application! Your application will be processed shortly. 
  <p>Wish to know more about CLRA? Click:   
  <html:link href="http://marsogames.world/clra-test">CLRA</html:link>

</body>
</html:html>
