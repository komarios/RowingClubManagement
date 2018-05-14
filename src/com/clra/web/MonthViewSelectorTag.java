/*
 * Copyright (c) Carnegie Lake Rowing Association 2002. All rights reserved.
 * Distributed under the GPL license. See doc/COPYING.
 * $RCSfile: MonthViewSelectorTag.java,v $
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
public class MonthViewSelectorTag extends TagSupport {

  private final static String base = MonthViewSelectorTag.class.getName();
  private final static Category theLog = Category.getInstance( base );

  /** The message resources for this package */
  protected static MessageResources messages =
    MessageResources.getMessageResources("com.clra.web.clra");

  /** The context-relative URI */
  protected String page = null;

  /** The currently selected month (0 - 11) */
  private Integer currentMonth = null;

  /** Return the context-relative URI */
  public String getPage() {
    return (this.page);
  }

  /** Set the context-relative URI */
  public void setPage(String page) {
    this.page = page;
  }

  /** Return the currently selected month (0 - 11, or null) */
  public Integer getCurrentMonth() {
    return this.currentMonth;
  }

  /** Set the currently selected month (0 - 11, or null) */
  public void setCurrentMonth( Integer currentMonth ) throws JspException {
    if ( currentMonth != null ) {
        if ( currentMonth.intValue() < 0 || currentMonth.intValue() > 11 ) {
        String msg = messages.getMessage(
            "monthselector.badcurrentMonth", currentMonth );
        throw new JspException(msg);
      }
    }
    this.currentMonth = currentMonth;
  }

  /**
   * A utility which determines what month should be currently selected
   * based on the page context. The algorithm is:<ol>
   * <li>Check for a request parameter named IEventList.AN_MONTH. If
   * found, and it is a valid Integer in the range 0 - 11, use it.</li>
   * <li>Check for a request attribute named IEventList.AN_MONTH. If
   * found, and it is valid, use it.</li>
   * <li>Check for a session attribute named IEventList.AN_MONTH. If
   * found, and it is valid, use it.</li>
   * <li>If a valid month hasn't been determined yet, return the current
   * calendar month</li>
   * </ol>
   */
  public static Integer monthFromContext( PageContext context ) {

    final String NAME = IEventList.AN_MONTH;
    Integer retVal = null;

    // Try request parameter first
    try {
      String s = context.getRequest().getParameter(NAME);
      if ( s != null ) {
        retVal = Integer.valueOf(s);
        if ( retVal.intValue() < 0 || retVal.intValue() > 11 ) {
          throw new IllegalArgumentException( "bad month == " + retVal );
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
          if ( retVal.intValue() < 0 || retVal.intValue() > 11 ) {
            throw new IllegalArgumentException( "bad month == " + retVal );
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
          if ( retVal.intValue() < 0 || retVal.intValue() > 11 ) {
            throw new IllegalArgumentException( "bad month == " + retVal );
          } // if valid Integer
        } // if o
      } // if null retVal
    } // try
    catch( Exception x ) {
      String msg = "sessionAttribute: " + NAME + "; "
          + x.getClass().getName() + ": " + x.getMessage();
      theLog.error( msg, x );
    }

    // Use the current month as a fallback
    if ( retVal == null ) {
      int month = new GregorianCalendar().get( Calendar.MONTH );
      retVal = new Integer( month );
    }

    return retVal;
  } // monthFromContext(PageContext)

  /**
   * A utility which resets request and session attributes so that only
   * the session attribute AN_MONTH holds the currently selected month.
   * The algorithm is:<ol>
   * <li>Check for a request attribute named IEventList.AN_MONTH. If
   * found, remove it.</li>
   * <li>Check for a session attribute named IEventList.AN_MONTH. If
   * found, reset to the specified value; otherwise create and set it.</li>
   * </ol>
   * If the specified month is invalid (not between 0 and 11, inclusive),
   * no action is taken and an error is logged.
   */
  public static void resetMonthInContexts(PageContext context, Integer month) {

    // Check the context and month.
    // If either is invalid, log an error and null both as a flag.
    if ( context == null || month == null
        || month.intValue() < 0 || month.intValue() > 11 ) {
      theLog.error( "invalid context/month == " + context + "/" + month );
      context = null;
      month = null;
    }

    final String NAME = IEventList.AN_MONTH;

    // Reset the request attribute
    if ( context != null ) {
      context.getRequest().removeAttribute(NAME);
    }

    // Set the session attribute
    if ( context != null ) {
      context.getSession().setAttribute(NAME,month);
    }

    return;
  } // resetMonthInContexts(PageContext,Integer)

  /** Form a query string from current parameter and the specified month */
  protected String createQueryString( int month, Hashtable queryParams ) {

    // Preconditions
    if ( month < 0 || month > 11 ) {
      throw new IllegalArgumentException( "invalid month == " + month );
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
      IEventList.AN_MONTH, new Integer(month).toString() );
    
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
  protected String createLink( int month,
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
    String queryString = createQueryString( month, queryParams );

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

  /** Creates a link from the currentMonth request */
  protected String createLink( int month, HttpServletRequest request ) {

    StringBuffer url = HttpUtils.getRequestURL( request );

    String s = request.getQueryString();
    Hashtable queryParams;
    if ( s != null ) {
      queryParams = HttpUtils.parseQueryString(s);
    }
    else {
      queryParams = new Hashtable();
    }

    String queryString = createQueryString( month, queryParams );
    url.append( queryString );

    return url.toString();
  } // createLink(int,HttpServletRequest)

  protected void doStartLink( int month ) throws JspException {

    // Create a link
    HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
    String url = null;
    if ( this.page != null ) {
      // Use the specified page
      String contextPath = request.getContextPath();
      url = createLink( month, contextPath, page );
    }
    else {
      // Link back to the currentMonth page by default
      url = createLink( month, request );
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
          (messages.getMessage("monthselector.io", e.toString()));
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
          (messages.getMessage("monthselector.io", e.toString()));
    }

    return;
  } // doEndLink()

  /** Writes a Calendar-based month (0-11) as user-friendly text */
  protected void doMonthText( int month ) throws JspException {

    JspWriter writer = pageContext.getOut();
    try {
      writer.print("" + (month+1) );
    } catch (IOException e) {
      throw new JspException(
        messages.getMessage("monthselector.io", e.toString()) );
    }

    return;
  } // doMonthText(int)

  /** Writes the text that separates month links */
  protected void doMonthLeadingSeparationText(int unused) throws JspException{

    JspWriter writer = pageContext.getOut();
    try {
      writer.print( "&nbsp;" );
    } catch (IOException e) {
      throw new JspException(
        messages.getMessage("monthselector.io", e.toString()) );
    }

    return;
  } // doMonthLeadingSeparationText(int)

  /** Writes the text that separates month links */
  protected void doMonthTrailingSeparationText(int unused) throws JspException{

    JspWriter writer = pageContext.getOut();
    try {
      writer.print( "&nbsp;" );
    } catch (IOException e) {
      throw new JspException(
        messages.getMessage("monthselector.io", e.toString()) );
    }

    return;
  } // doMonthTrailingSeparationText(int)

  /** Write a month link */
  protected void doMonthLink( int month ) throws JspException {

    doMonthLeadingSeparationText( month );
    if ( currentMonth == null ) {
      doStartLink( month );
    }
    else if ( currentMonth != null && month != currentMonth.intValue() ) {
      doStartLink( month );
    }
    doMonthText( month );
    if ( currentMonth == null ) {
      doEndLink();
    }
    else if ( currentMonth != null && month != currentMonth.intValue() ) {
      doEndLink();
    }
    doMonthTrailingSeparationText( month );

    return;
  } // doMonthLink()

  /** Writes a year's worth of month links */
  protected void doMonthLinks() throws JspException {

    for ( int month=0; month<12; month++ ) {
      doMonthLink( month );
    }

    return;
  } // doMonthLinks(int)

  /** Render the beginning of the selector */
  public int doStartTag() throws JspException {
    return (EVAL_BODY_INCLUDE);
  }

  /** Render the end of the selector */
  public int doEndTag() throws JspException {
    doMonthLinks();
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

} // MonthViewSelector

/*
 * $Log: MonthViewSelectorTag.java,v $
 * Revision 1.3  2003/02/26 03:38:46  rphall
 * Added copyright and GPL license
 *
 * Revision 1.2  2002/02/18 18:06:37  rphall
 * Ran dos2unix to remove ^M (carriage return) from end of lines
 *
 * Revision 1.1.1.1  2002/01/03 21:57:28  rphall
 * Initial load, 5th try, Jan-03-2002 4:57 PM
 *
 * Revision 1.5  2002/01/01 03:40:33  rphall
 * Moved getName() from MemberName to MemberView
 *
 * Revision 1.4  2001/12/09 20:28:17  rphall
 * Added resetMonthInContexts(PageContext,Integer)
 *
 * Revision 1.3  2001/12/08 23:25:20  rphall
 * Working
 *
 * Revision 1.2  2001/12/08 18:53:22  rphall
 * Checkpt
 *
 * Revision 1.1  2001/12/08 02:41:38  rphall
 * MonthViewSelector JSP tag controls month display
 *
 */


