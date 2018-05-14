<%
/*
 * Copyright (c) Carnegie Lake Rowing Association 2002. All rights reserved.
 * Distributed under the GPL license. See doc/COPYING.
 * $RCSfile: addmember.jsp,v $
 * $Date: 2003/02/26 03:38:47 $
 * $Revision: 1.5 $
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
  Laker Online Activity Center
</title>
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
  Laker Activity Center
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
Welcome to the online Activity Center for the Carnegie Lake Rowing Association. 
</p>

<p>
<%-- FIXME hard-coded text --%>
To apply the membership of the club, please enter your information.
</p>
<p>
You must fill in the coloum marked by <Font color=red>*</Font>.

</td>
<td width=1>&nbsp;</td>
</td>
</tr>
<!-- end row for long description -->

</table>
<!-- end table to set text boundaries same as list boundaries -->

<FONT class=header16 color=red>
<p>
<html:errors/>
</p>
</FONT>


<html:form action="/applymembership">
<table border=0 width=647>

  <tr>
    <td align="right">
      <Font color=red>* </Font><bean:message key="member.name_last"/>
    </td>
    <td align="left">
      <input type="text" name="name_last">
    </td>
  </tr>

  <tr>
    <td align="right">
      <Font color=red>* </Font><bean:message key="member.name_first"/>
    </td>
    <td align="left">
      <input type="text" name="name_first">
    </td>
  </tr>

  <tr>
    <td align="right">
      <bean:message key="member.name_middle"/>
    </td>
    <td align="left">
      <input type="text" name="name_middle">
    </td>
  </tr>

  <tr>
    <td align="right">
      <bean:message key="member.name_suffix"/>
    </td>
    <td align="left">
      <input type="text" name="name_suffix">
    </td>
  </tr>

  <tr>
    <td align="right">
      <Font color=red>* </Font><bean:message key="member.password"/>
    </td>
    <td align="left">
      <input type="password" name="password">
    </td>
  </tr>

  <tr>
    <td align="right">
      <Font color=red>* </Font><bean:message key="member.confirm_password"/>
    </td>
    <td align="left">
      <input type="password" name="confirm_password">
    </td>
  </tr>

  <tr>
    <td align="right">
      <Font color=red>* </Font><bean:message key="member.email"/>
    </td>
    <td align="left">
      <input type="text" name="email">
    </td>
  </tr>

  <tr>
    <td align="right">
      <Font color=red>* </Font><bean:message key="member.tel_evening"/>
    </td>
    <td>
      <table border=0>
        <tr>
          <td align="left">
            (<input type="text" maxlength=3 size=3 maxsize=3 name="tel_evening_areacode">)
          </td>
          <td>
            <input type="text" maxlength=3 size=3 maxsize=3 name="tel_evening_exchange">-
          </td>
          <td>
            <input type="text" maxlength=4 size=4 maxsize=4 name="tel_evening_local">ext
          </td>
          <td>
            <input type="text" maxlength=4 size=4 maxsize=4 name="tel_evening_ext">
          </td>
        </tr>
      </table>
    </td>
  </tr>

  <tr>
    <td align="right">
      <bean:message key="member.tel_day"/>
    </td>
    <td>
      <table border=0>
        <tr>
          <td align="left">
            (<input type="text" maxlength=3 size=3 maxsize=3 name="tel_day_areacode">)
          </td>
          <td>
            <input type="text" maxlength=3 size=3 maxsize=3 name="tel_day_exchange">-
          </td>
          <td>
            <input type="text" maxlength=4 size=4 maxsize=4 name="tel_day_local">ext
          </td>
          <td>
            <input type="text" maxlength=4 size=4 maxsize=4 name="tel_day_ext">
          </td>
        </tr>
      </table>
    </td>
  </tr>

  <tr>
    <td align="right">
      <bean:message key="member.tel_other"/>
    </td>
    <td>
      <table border=0>
        <tr>
          <td align="left">
            (<input type="text" maxlength=3 size=3 maxsize=3 name="tel_other_areacode">)
          </td>
          <td>
            <input type="text" maxlength=3 size=3 maxsize=3 name="tel_other_exchange">-
          </td>
          <td>
            <input type="text" maxlength=4 size=4 maxsize=4 name="tel_other_local">ext
          </td>
          <td>
            <input type="text" maxlength=4 size=4 maxsize=4 name="tel_other_ext">
          </td>
        </tr>
      </table>
    </td>
  </tr>

  <tr>
    <td align="right">
      <Font color=red>* </Font><bean:message key="member.address_street1"/>
    </td>
    <td align="left">
      <input type="text" name="address_street1">
    </td>
  </tr>

  <tr>
    <td align="right">
      <bean:message key="member.address_street2"/>
    </td>
    <td align="left">
      <input type="text" name="address_street2">
    </td>
  </tr>

  <tr>
    <td align="right">
      <Font color=red>* </Font><bean:message key="member.address_city"/>
    </td>
    <td align="left">
      <input type="text" name="address_city">
    </td>
  </tr>

  <tr>
    <td align="right">
      <Font color=red>* </Font><bean:message key="member.address_state"/>
    </td>
    <td align="left">
      <select name="address_state">
      <option value=AK>Alaska</option>
      <option value=AL>Alabama</option>
      <option value=AZ>Arizona</option>
      <option value=AR>Arkansas</option>
      <option value=CA>California</option>
      <option value=CO>Colorado</option>
      <option value=CT>Connecticut</option>
      <option value=DE>Delaware</option>
      <option value=DC>District of Columbia</option>
      <option value=FL>Florida</option>
      <option value=GA>Georgia</option>
      <option value=GU>Guam</option>
      <option value=HI>Hawaii</option>
      <option value=ID>Idaho</option>
      <option value=IL>Illinois</option>
      <option value=IN>Indiana</option>
      <option value=IA>Iowa</option>
      <option value=KS>Kansas</option>
      <option value=KY>Kentucky</option>
      <option value=LA>Louisiana</option>
      <option value=ME>Maine</option>
      <option value=MD>Maryland</option>
      <option value=MA>Massachusettes</option>
      <option value=MI>Michigan</option>
      <option value=MN>Minnesota</option>
      <option value=MS>Mississippi</option>
      <option value=MO>Missouri</option>
      <option value=MT>Montana</option>
      <option value=NB>Nebraska</option>
      <option value=NV>Nevada</option>
      <option value=NH>New Hampshire</option>
      <option value=NJ>New Jersey</option>
      <option value=NM>New Mexico</option>
      <option value=NY>New York</option>
      <option value=NC>North Carolina</option>
      <option value=ND>North Dakota</option>
      <option value=OH>Ohio</option>
      <option value=OK>Oklahoma</option>
      <option value=OR>Oregon</option>
      <option value=PA>Pennsylvania</option>
      <option value=PR>Puerto Rico</option>
      <option value=RI>Rhode Island</option>
      <option value=SC>South Carolina</option>
      <option value=SD>South Dakota</option>
      <option value=TN>Tennessee</option>
      <option value=UT>Utah</option>
      <option value=VT>Vermont</option>
      <option value=VA>Virginia</option>
      <option value=VI>Virgin Islands</option>
      <option value=WA>Washington</option>
      <option value=WV>West Virgina</option>
      <option value=WI>Wisconsin</option>
      <option value=WY>Wyoming</option></select>
    </td>
  </tr>

  <tr>
    <td align="right">
      <Font color=red>* </Font><bean:message key="member.address_zip"/>
    </td>
    <td align="left">
      <input type="text" name="address_zip">
    </td>
  </tr>

  <tr>
    <td align="right">
      <Font color=red>* </Font><bean:message key="member.experience_year"/>
    </td>
    <td align="left">
      <select name="experience_year">
      <option value=0>0</option>
      <option value=1>1</option>
      <option value=2>2</option>
      <option value=3+>3+</option></select>
    </td>
  </tr>

  <tr>
    <td align="right">
      <Font color=red>* </Font><bean:message key="member.recent_year"/>
    </td>
    <td align="left">
      <select name="recent_year">
      <option value=never>never</option>
      <option value="1 year ago">1 year ago</option>
      <option value="2 years ago">2 years ago</option>
      <option value="3-5 years ago">3-5 years ago</option>
      <option value="5-10 years ago">5-10 years ago</option>
      <option value="10+ years ago">10+ years ago</option></select>
    </td>
  </tr>

  <tr>
    <td align="right">
      <bean:message key="member.birthday"/>
    </td>
    <td align="left">
      <select name="birthday_day">
      <option value=1>1</option>
      <option value=2>2</option>
      <option value=3>3</option>
      <option value=4>4</option>
      <option value=5>5</option>
      <option value=6>6</option>
      <option value=7>7</option>
      <option value=8>8</option>
      <option value=9>9</option>
      <option value=10>10</option>
      <option value=11>11</option>
      <option value=12>12</option>
      <option value=13>13</option>
      <option value=14>14</option>
      <option value=15>15</option>
      <option value=16>16</option>
      <option value=17>17</option>
      <option value=18>18</option>
      <option value=19>19</option>
      <option value=20>20</option>
      <option value=21>21</option>
      <option value=22>22</option>
      <option value=23>23</option>
      <option value=24>24</option>
      <option value=25>25</option>
      <option value=26>26</option>
      <option value=27>27</option>
      <option value=28>28</option>
      <option value=29>29</option>
      <option value=30>30</option>
      <option value=31>31</option></select>
      <select name="birthday_month">
      <option value=1>January</option>
      <option value=2>February</option>
      <option value=3>March</option>
      <option value=4>April</option>
      <option value=5>May</option>
      <option value=6>June</option>
      <option value=7>July</option>
      <option value=8>August</option>
      <option value=9>September</option>
      <option value=10>October</option>
      <option value=11>November</option>
      <option value=12>December</option></select>
      <select name="birthday_year">
      <option value=1920>1920</option>
      <option value=1921>1921</option>
      <option value=1922>1922</option>
      <option value=1923>1923</option>
      <option value=1924>1924</option>
      <option value=1925>1925</option>
      <option value=1926>1926</option>
      <option value=1927>1927</option>
      <option value=1928>1928</option>
      <option value=1929>1929</option>
      <option value=1930>1930</option>
      <option value=1931>1931</option>
      <option value=1932>1932</option>
      <option value=1933>1933</option>
      <option value=1934>1934</option>
      <option value=1935>1935</option>
      <option value=1936>1936</option>
      <option value=1937>1937</option>
      <option value=1938>1938</option>
      <option value=1939>1939</option>
      <option value=1940>1940</option>
      <option value=1941>1941</option>
      <option value=1942>1942</option>
      <option value=1943>1943</option>
      <option value=1944>1944</option>
      <option value=1945>1945</option>
      <option value=1946>1946</option>
      <option value=1947>1947</option>
      <option value=1948>1948</option>
      <option value=1949>1949</option>
      <option value=1950>1950</option>
      <option value=1951>1951</option>
      <option value=1952>1952</option>
      <option value=1953>1953</option>
      <option value=1954>1954</option>
      <option value=1955>1955</option>
      <option value=1956>1956</option>
      <option value=1957>1957</option>
      <option value=1958>1958</option>
      <option value=1959>1959</option>
      <option value=1960>1960</option>
      <option value=1961>1961</option>
      <option value=1962>1962</option>
      <option value=1963>1963</option>
      <option value=1964>1964</option>
      <option value=1965>1965</option>
      <option value=1966>1966</option>
      <option value=1967>1967</option>
      <option value=1968>1968</option>
      <option value=1969>1969</option>
      <option value=1970>1970</option>
      <option value=1971>1971</option>
      <option value=1972>1972</option>
      <option value=1973>1973</option>
      <option value=1974>1974</option>
      <option value=1975>1975</option>
      <option value=1976>1976</option>
      <option value=1977>1977</option>
      <option value=1978>1978</option>
      <option value=1979>1979</option>
      <option value=1980>1980</option>
      <option value=1981>1981</option>
      <option value=1982>1982</option>
      <option value=1983>1983</option>
      <option value=1984>1984</option>
      <option value=1985>1985</option>
      <option value=1986>1986</option>
      <option value=1987>1987</option>
      <option value=1988>1988</option>
      <option value=1989>1989</option>
      </select>
    </td>
  </tr>

  <tr>
    <td align="right">
      <Font color=red>* </Font><bean:message key="member.sex"/>
    </td>
    <td align="left">
      <select name="sex">
      <option value=F>Female</option>
      <option value=M>Male</option></select>
    </td>
  </tr>

  <tr>
    <td align="right">
      <input type="submit" value="Register"> <!-- FIXME hard-coded text -->
    </td>
    <td align="left">
      <input type="reset" value="Clear"> <!-- FIXME hard-coded text -->
    </td>
  </tr>

</table>

</html:form>

</body>
</html:html>
