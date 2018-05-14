/*
 * Copyright (c) Carnegie Lake Rowing Association 2002. All rights reserved.
 * Distributed under the GPL license. See doc/COPYING.
 * $RCSfile: YearViewSelectorTag.java,v $
 * $Date: 2003/02/26 03:38:46 $
 * $Revision: 1.3 $
 */

package com.clra.web;

import java.io.IOException;
import java.io.Serializable;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.Iterator;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpUtils;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;
import org.apache.log4j.Category;
import org.apache.struts.util.MessageResources;
import org.apache.struts.util.ResponseUtils;

/**
 * Stuff.
 *
 * @author <a mailto:"rphall@pluto.njcc.com">Rick Hall</a>
 * @version $Revision: 1.3 $ $Date: 2003/02/26 03:38:46 $
 */
public class YearViewSelectorTag extends TagSupport {

  private final static String base = YearViewSelectorTag.class.getName();
  private final static Category theLog = Category.getInstance( base );

  /** The message resources for this package */
  protected static MessageResources messages =
    MessageResources.getMessageResources("com.clra.web.clra");

  /** The context-relative URI */
  protected String page = null;

  /** The currently selected year (4 digits) */
  private Integer currentYear = null;

  /** Return the context-relative URI */
  public String getPage() {
    return (this.page);
  }

  /** Set the context-relative URI */
  public void setPage(String page) {
    this.page = page;
  }

  /** Return the currently selected year (4 digits, or null) */
  public Integer getCurrentYear() {
    return this.currentYear;
  }

  /** Set the currently selected year (4 digits, or null) */
  public void setCurrentYear( Integer currentYear ) throws JspException {
    if ( currentYear != null ) {
      if ( !hasFourDigits( currentYear ) ) {
        String msg = messages.getMessage(
            "yearselector.badcurrentYear", currentYear );
        throw new JspException(msg);
      }
    }
    this.currentYear = currentYear;
  }

  /**
   * A slightly mis-named utility that checks if an Integer is
   * <strong>positive</strong> and has four digits.
   */
  public static boolean hasFourDigits( Integer year ) {
    if ( year == null ) {
      throw new IllegalArgumentException( "null Integer" );
    }
    return hasFourDigits( year.intValue() );
  }

  /**
   * A slightly mis-named utility that checks if an int is
   * <strong>positive</strong> and has four digits.
   */
  public static boolean hasFourDigits( int year ) {
    boolean retVal = 999 < year && year < 10000;
    return retVal;
  }

  /**
   * A utility which determines what year should be currently selected
   * based on the page context. The algorithm is:<ol>
   * <li>Check for a request parameter named IEventList.AN_YEAR. If
   * found, and it is a valid Integer (4 digits), use it.</li>
   * <li>Check for a request attribute named IEventList.AN_YEAR. If
   * found, and it is valid, use it.</li>
   * <li>Check for a session attribute named IEventList.AN_YEAR. If
   * found, and it is valid, use it.</li>
   * <li>If a valid year hasn't been determined yet, return the current
   * calendar year</li>
   * </ol>
   */
  public static Integer yearFromContext( PageContext context ) {

    final String NAME = IEventList.AN_YEAR;
    Integer retVal = null;

    // Try request parameter first
    try {
      String s = context.getRequest().getParameter(NAME);
      if ( s != null ) {
        retVal = Integer.valueOf(s);
        if ( !hasFourDigits( retVal ) ) {
          throw new IllegalArgumentException( "bad year == " + retVal );
        }
      }
    }
    catch( Exception x ) {
      String msg = "requestParameter: " + NAME + "; "
          + x.getClass().getName() + ": " + x.getMessage();
      theLog.error( msg, x );
    }

    // Try request attribute second
    try {
      if ( retVal == null ) {
        Object o = context.getRequest().getAttribute(NAME);
        if ( o != null && o instanceof Integer ) {
          retVal = (Integer) o;
          if ( !hasFourDigits( retVal ) ) {
            throw new IllegalArgumentException( "bad year == " + retVal );
          } // if invalid Integer
        } // if o
      } // if null retVal
    } // try
    catch( Exception x ) {
      String msg = "requestAttribute: " + NAME + "; "
          + x.getClass().getName() + ": " + x.getMessage();
      theLog.error( msg, x );
    }
    
    // Try session attribute third
    try {
      if ( retVal == null ) {
        Object o = context.getSession().getAttribute(NAME);
        if ( o != null && o instanceof Integer ) {
          retVal = (Integer) o;
          if ( !hasFourDigits( retVal ) ) {
            throw new IllegalArgumentException( "bad year == " + retVal );
          } // if valid Integer
        } // if o
      } // if null retVal
    } // try
    catch( Exception x ) {
      String msg = "sessionAttribute: " + NAME + "; "
          + x.getClass().getName() + ": " + x.getMessage();
      theLog.error( msg, x );
    }

    // Use the current year as a fallback
    if ( retVal == null ) {
      int year = new GregorianCalendar().get( Calendar.YEAR );
      retVal = new Integer( year );
    }

    return retVal;
  } // yearFromContext(PageContext)

  /**
   * A utility which resets request and session attributes so that only
   * the session attribute AN_YEAR holds the currently selected year.
   * The algorithm is:<ol>
   * <li>Check for a request attribute named IEventList.AN_YEAR. If
   * found, remove it.</li>
   * <li>Check for a session attribute named IEventList.AN_YEAR. If
   * found, reset to the specified value; otherwise create and set it.</li>
   * </ol>
   * If the specified year is invalid (not 4 digits),
   * no action is taken and an error is logged.
   */
  public static void resetYearInContexts(PageContext context, Integer year) {

    // Check the context and year.
    // If either is invalid, log an error and null both as a flag.
    if ( context == null || year == null || !hasFourDigits(year) ) {
      theLog.error( "invalid context/year == " + context + "/" + year );
      context = null;
      year = null;
    }

    final String NAME = IEventList.AN_YEAR;

    // Reset the request attribute
    if ( context != null ) {
      context.getRequest().removeAttribute(NAME);
    }

    // Set the session attribute
    if ( context != null ) {
      context.getSession().setAttribute(NAME,year);
    }

    return;
  } // resetYearInContexts(PageContext,Integer)

  /** Form a query string from current parameter and the specified year */
  protected String createQueryString( int year, Hashtable queryParams ) {

    // FIXME consolidate code with MonthViewSelectorTag.createQueryString(..)

    // Preconditions
    if ( !hasFourDigits(year) ) {
      throw new IllegalArgumentException( "invalid year == " + year );
    }
    if ( queryParams == null ) {
      throw new IllegalArgumentException( "null queryParams" );
    }

    StringBuffer sb = new StringBuffer();
    sb.append( "?" );

    // Overwrite existing query params related to this control
    queryParams.put(
      IEventList.AN_ISRESTRICTED, new Boolean(true).toString() );
    queryParams.put(
      IEventList.AN_YEAR, new Integer(year).toString() );
    
    // Form the query string
    boolean isFirst = true;
    Iterator keys = queryParams.keySet().iterator();
    while ( keys.hasNext() ) {

      if ( isFirst ) {
        isFirst = false;
      }
      else {
        sb.append( "&" );
      }

      String key = (String) keys.next();
      Object value = queryParams.get( key );
      if ( value instanceof String ) {
        sb.append( key + "=" + value );
      }
      else {
        String[] values = (String[]) value;
        for ( int i=0; i<values.length; i++ ) {
          if ( i > 0 ) {
            sb.append( "&" );
          }
          sb.append( key + "=" + values[i] );
        } // for String[]
      } // else String[]

    } // while keys

    return sb.toString();
  } // createQueryString(int,Hashtable)

  /** Create a link from the specified page */
  protected String createLink( int year,
    String contextPath, String page ) throws JspException {

    // Preconditions
    if ( contextPath == null ) {
      throw new IllegalArgumentException( "null contextPath" );
    }
    if ( page == null ) {
      throw new IllegalArgumentException( "null page" );
    }

    StringBuffer sb = new StringBuffer( contextPath );
    sb.append(page);

    Hashtable queryParams;
    String tmp = sb.toString();
    int idx = tmp.indexOf("?");
    if ( idx > -1 && idx < page.length()-1 ) {
      String s = tmp.substring( idx+1 );
      queryParams = HttpUtils.parseQueryString(s);
    }
    else {
      queryParams = new Hashtable();
    }
    String queryString = createQueryString( year, queryParams );

    String url;
    if ( idx < 0 ) {
      url = tmp + "?";
    }
    else {
      url = sb.substring( 0, idx );
    }
    url = url + queryString;

    return url;
  } // createLink(int,String,String)

  /** Creates a link from the currentYear request */
  protected String createLink( int year, HttpServletRequest request ) {

    StringBuffer url = HttpUtils.getRequestURL( request );

    String s = request.getQueryString();
    Hashtable queryParams;
    if ( s != null ) {
      queryParams = HttpUtils.parseQueryString(s);
    }
    else {
      queryParams = new Hashtable();
    }

    String queryString = createQueryString( year, queryParams );
    url.append( queryString );

    return url.toString();
  } // createLink(int,HttpServletRequest)

  protected void doStartLink( int year ) throws JspException {

    // Create a link
    HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
    String url = null;
    if ( this.page != null ) {
      // Use the specified page
      String contextPath = request.getContextPath();
      url = createLink( year, contextPath, page );
    }
    else {
      // Link back to the currentYear page by default
      url = createLink( year, request );
    }

    // Generate the hyperlink start element
    HttpServletResponse response =
      (HttpServletResponse) pageContext.getResponse();
    StringBuffer results = new StringBuffer("<a href=\"");
    results.append( response.encodeURL(url) );
    results.append("\">");

    // Print this element to our output writer
    JspWriter writer = pageContext.getOut();
    try {
      writer.print(results.toString());
    } catch (IOException e) {
      throw new JspException
          (messages.getMessage("yearselector.io", e.toString()));
    }

    return;
  } // doStartLink(int)

  protected void doEndLink() throws JspException {

    // Print the ending element to our output writer
    JspWriter writer = pageContext.getOut();
    try {
      writer.print("</a>");
    } catch (IOException e) {
      throw new JspException
          (messages.getMessage("yearselector.io", e.toString()));
    }

    return;
  } // doEndLink()

  /** Writes a Calendar-based year (4 digits) as user-friendly text */
  protected void doYearText( int year ) throws JspException {

    JspWriter writer = pageContext.getOut();
    try {
      writer.print("" + year );
    } catch (IOException e) {
      throw new JspException(
        messages.getMessage("yearselector.io", e.toString()) );
    }

    return;
  } // doYearText(int)

  /** Writes the text that separates year links */
  protected void doYearLeadingSeparationText(int unused) throws JspException{

    JspWriter writer = pageContext.getOut();
    try {
      writer.print( "&nbsp;" );
    } catch (IOException e) {
      throw new JspException(
        messages.getMessage("yearselector.io", e.toString()) );
    }

    return;
  } // doYearLeadingSeparationText(int)

  /** Writes the text that separates year links */
  protected void doYearTrailingSeparationText(int unused) throws JspException{

    JspWriter writer = pageContext.getOut();
    try {
      writer.print( "&nbsp;" );
    } catch (IOException e) {
      throw new JspException(
        messages.getMessage("yearselector.io", e.toString()) );
    }

    return;
  } // doYearTrailingSeparationText(int)

  /** Write a year link */
  protected void doYearLink( int year ) throws JspException {

    doYearLeadingSeparationText( year );
    if ( currentYear == null ) {
      doStartLink( year );
    }
    else if ( currentYear != null && year != currentYear.intValue() ) {
      doStartLink( year );
    }
    doYearText( year );
    if ( currentYear == null ) {
      doEndLink();
    }
    else if ( currentYear != null && year != currentYear.intValue() ) {
      doEndLink();
    }
    doYearTrailingSeparationText( year );

    return;
  } // doYearLink()

  /** Writes several year links */
  protected void doYearLinks() throws JspException {

    // Preconditions
    if ( currentYear == null ) {
      throw new IllegalStateException( "current year is null" );
    }
    final int cy = currentYear.intValue();

    for ( int year=cy-1; year<cy+2; year++ ) {
      doYearLink( year );
    }

    return;
  } // doYearLinks(int)

  /** Render the beginning of the selector */
  public int doStartTag() throws JspException {
    return (EVAL_BODY_INCLUDE);
  }

  /** Render the end of the selector */
  public int doEndTag() throws JspException {
    doYearLinks();
    return (EVAL_PAGE);
  }

  /**
   * Release any acquired resources.
   */
  public void release() {
    super.release();
    this.page = null;
    return;
  }

} // YearViewSelector

/*
 * $Log: YearViewSelectorTag.java,v $
 * Revision 1.3  2003/02/26 03:38:46  rphall
 * Added copyright and GPL license
 *
 * Revision 1.2  2002/02/18 18:07:33  rphall
 * Ran dos2unix to remove ^M (carriage return) from end of lines
 *
 * Revision 1.1.1.1  2002/01/03 21:57:28  rphall
 * Initial load, 5th try, Jan-03-2002 4:57 PM
 *
 * Revision 1.2  2002/01/01 03:40:33  rphall
 * Moved getName() from MemberName to MemberView
 *
 * Revision 1.1  2001/12/14 00:47:33  rphall
 * YearViewSelectorTag JSP tag controls year display
 *
 */


