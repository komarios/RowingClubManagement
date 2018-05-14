/*
 * Copyright (c) Carnegie Lake Rowing Association 2002. All rights reserved.
 * Distributed under the GPL license. See doc/COPYING.
 * $RCSfile: MemberNameFormatSelectorTag.java,v $
 * $Date: 2003/02/26 03:38:46 $
 * $Revision: 1.3 $
 */

package com.clra.web;

import com.clra.util.ErrorUtils;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
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
 * Allows the user to specify whether names should be formated according to
 * <tt>MemberNameFormat.FIRSTLAST</tt> or <tt>MemberNameFormat.LASTFIRST</tt>.
 *
 * @author <a mailto:"rphall@pluto.njcc.com">Rick Hall</a>
 * @version $Revision: 1.3 $ $Date: 2003/02/26 03:38:46 $
 */
public class MemberNameFormatSelectorTag extends TagSupport {

  private final static String base= MemberNameFormatSelectorTag.class.getName();
  private final static Category theLog = Category.getInstance( base );

  /** The message resources for this package */
  protected static MessageResources messages =
    MessageResources.getMessageResources("com.clra.web.clra");

  /** The cached value of the groups used by this selector */
  private static Map _groups = null;
  static {
    _groups = new HashMap();
    final String PN_GROUP_PREFIX = "nameformat.0";
    for ( int i=2; i<10; i++ ) {
      Integer key = new Integer( i );
      String PN = PN_GROUP_PREFIX + i;
      String lbl = messages.getMessage( PN );
      _groups.put( key, lbl );
    }
  }

  /** The groups used by this selector */
  protected static Map groups() {
    return _groups;
  }

  /** The context-relative URI */
  protected String page = null;

  /** The currently selected group (2 - 9) */
  private Integer currentGroup = null;

  /** Return the context-relative URI */
  public String getPage() {
    return (this.page);
  }

  /** Set the context-relative URI */
  public void setPage(String page) {
    this.page = page;
  }

  /** Return the currently selected group (2 - 9, or null) */
  public Integer getCurrentGroup() {
    return this.currentGroup;
  }

  /** Set the currently selected group (2 - 9, or null) */
  public void setCurrentGroup( Integer currentGroup ) throws JspException {
    if ( currentGroup != null ) {
        if ( currentGroup.intValue() < 2 || currentGroup.intValue() > 9 ) {
        String msg = messages.getMessage(
            "nameselector.badcurrentGroup", currentGroup );
        throw new JspException(msg);
      }
    }
    this.currentGroup = currentGroup;
  }

  /**
   * A utility which determines what group should be currently selected
   * based on the page context. The algorithm is:<ol>
   * <li>Check for a request parameter named INameList.AN_GROUP. If
   * found, and it is a valid Integer in the range 2 - 9, use it.</li>
   * <li>Check for a request attribute named INameList.AN_GROUP. If
   * found, and it is valid, use it.</li>
   * <li>Check for a session attribute named INameList.AN_GROUP. If
   * found, and it is valid, use it.</li>
   * <li>If a valid group hasn't been determined yet, return the index
   * of the first group, <tt>ABC</tt> (index 2).</li>
   * </ol>
   */
  public static Integer groupFromContext( PageContext context ) {

    final String NAME = INameList.AN_GROUP;
    Integer retVal = null;

    // Try request parameter first
    try {
      String s = context.getRequest().getParameter(NAME);
      if ( s != null ) {
        retVal = Integer.valueOf(s);
        if ( retVal.intValue() < 2 || retVal.intValue() > 9 ) {
          throw new IllegalArgumentException( "bad group == " + retVal );
        }
      }
    }
    catch( Exception x ) {
      String msg = ErrorUtils.createDbgMsg( "requestParameter: " + NAME, x );
      theLog.error( msg, x );
    }

    // Try request attribute second
    try {
      if ( retVal == null ) {
        Object o = context.getRequest().getAttribute(NAME);
        if ( o != null && o instanceof Integer ) {
          retVal = (Integer) o;
          if ( retVal.intValue() < 2 || retVal.intValue() > 9 ) {
            throw new IllegalArgumentException( "bad group == " + retVal );
          } // if invalid Integer
        } // if o
      } // if null retVal
    } // try
    catch( Exception x ) {
      String msg = ErrorUtils.createDbgMsg( "requestAttribute: " + NAME, x );
      theLog.error( msg, x );
    }
    
    // Try session attribute third
    try {
      if ( retVal == null ) {
        Object o = context.getSession().getAttribute(NAME);
        if ( o != null && o instanceof Integer ) {
          retVal = (Integer) o;
          if ( retVal.intValue() < 2 || retVal.intValue() > 9 ) {
            throw new IllegalArgumentException( "bad group == " + retVal );
          } // if valid Integer
        } // if o
      } // if null retVal
    } // try
    catch( Exception x ) {
      String msg = ErrorUtils.createDbgMsg( "sessionAttribute: " + NAME, x );
      theLog.error( msg, x );
    }

    // Use the first group as a fallback
    if ( retVal == null ) {
      retVal = new Integer( 2 );
    }

    return retVal;
  } // groupFromContext(PageContext)

  /**
   * A utility which resets request and session attributes so that only
   * the session attribute AN_GROUP holds the currently selected name.
   * The algorithm is:<ol>
   * <li>Check for a request attribute named INameList.AN_GROUP. If
   * found, remove it.</li>
   * <li>Check for a session attribute named INameList.AN_GROUP. If
   * found, reset to the specified value; otherwise create and set it.</li>
   * </ol>
   * If the specified name is invalid (not between 2 - 9, inclusive),
   * no action is taken and an error is logged.
   */
  public static void resetGroupInContexts(PageContext context, Integer group) {

    // Check the context and group.
    // If either is invalid, log an error and null both as a flag.
    if ( context == null || group == null
        || group.intValue() < 2 || group.intValue() > 9 ) {
      theLog.error( "invalid context/group == " + context + "/" + group );
      context = null;
      group = null;
    }

    final String NAME = INameList.AN_GROUP;

    // Reset the request attribute
    if ( context != null ) {
      context.getRequest().removeAttribute(NAME);
    }

    // Set the session attribute
    if ( context != null ) {
      context.getSession().setAttribute(NAME,group);
    }

    return;
  } // resetGroupInContexts(PageContext,Integer)

  /** Form a query string from current parameter and the specified group */
  protected String createQueryString( int group, Hashtable queryParams ) {

    // Preconditions
    if ( group < 2 || group > 9 ) {
      throw new IllegalArgumentException( "invalid group == " + group );
    }
    if ( queryParams == null ) {
      throw new IllegalArgumentException( "null queryParams" );
    }

    StringBuffer sb = new StringBuffer();
    sb.append( "?" );

    // Overwrite existing query params related to this control
    queryParams.put(
      INameList.AN_ISRESTRICTED, new Boolean(true).toString() );
    queryParams.put(
      INameList.AN_GROUP, new Integer(group).toString() );
    
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
  protected String createLink( int group,
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
    String queryString = createQueryString( group, queryParams );

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

  /** Creates a link from the currentGroup request */
  protected String createLink( int group, HttpServletRequest request ) {

    StringBuffer url = HttpUtils.getRequestURL( request );

    String s = request.getQueryString();
    Hashtable queryParams;
    if ( s != null ) {
      queryParams = HttpUtils.parseQueryString(s);
    }
    else {
      queryParams = new Hashtable();
    }

    String queryString = createQueryString( group, queryParams );
    url.append( queryString );

    return url.toString();
  } // createLink(int,HttpServletRequest)

  protected void doStartLink( int group ) throws JspException {

    // Create a link
    HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
    String url = null;
    if ( this.page != null ) {
      // Use the specified page
      String contextPath = request.getContextPath();
      url = createLink( group, contextPath, page );
    }
    else {
      // Link back to the currentGroup page by default
      url = createLink( group, request );
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
          (messages.getMessage("nameselector.io", e.toString()));
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
          (messages.getMessage("nameselector.io", e.toString()));
    }

    return;
  } // doEndLink()

  /** Writes a Integer-based group (2 - 9) as user-friendly text */
  protected void doGroupText( int group ) throws JspException {

    if ( group < 2 || group > 9 ) {
      throw new IllegalArgumentException( "bad index == " + group );
    }

    JspWriter writer = pageContext.getOut();
    try {
      String lbl = (String) groups().get( new Integer(group) );
      if ( lbl == null ) {
        throw new IllegalStateException( "null label for " + group );
      }
      writer.print( lbl );
    } catch (IOException e) {
      throw new JspException(
        messages.getMessage("nameselector.io", e.toString()) );
    }

    return;
  } // doGroupText(int)

  /** Writes the text that separates group links */
  protected void doGroupLeadingSeparationText(int unused) throws JspException{

    JspWriter writer = pageContext.getOut();
    try {
      writer.print( "&nbsp;" );
    } catch (IOException e) {
      throw new JspException(
        messages.getMessage("nameselector.io", e.toString()) );
    }

    return;
  } // doGroupLeadingSeparationText(int)

  /** Writes the text that separates group links */
  protected void doGroupTrailingSeparationText(int unused) throws JspException{

    JspWriter writer = pageContext.getOut();
    try {
      writer.print( "&nbsp;" );
    } catch (IOException e) {
      throw new JspException(
        messages.getMessage("nameselector.io", e.toString()) );
    }

    return;
  } // doGroupTrailingSeparationText(int)

  /** Write a group link */
  protected void doGroupLink( int group ) throws JspException {

    doGroupLeadingSeparationText( group );
    if ( currentGroup == null ) {
      doStartLink( group );
    }
    else if ( currentGroup != null && group != currentGroup.intValue() ) {
      doStartLink( group );
    }
    doGroupText( group );
    if ( currentGroup == null ) {
      doEndLink();
    }
    else if ( currentGroup != null && group != currentGroup.intValue() ) {
      doEndLink();
    }
    doGroupTrailingSeparationText( group );

    return;
  } // doGroupLink(int)

  /** Writes an alphabet's worth of group links */
  protected void doGroupLinks() throws JspException {

    for ( int group=2; group<10; group++ ) {
      doGroupLink( group );
    }

    return;
  } // doGroupLinks()

  /** Render the beginning of the selector */
  public int doStartTag() throws JspException {
    return (EVAL_BODY_INCLUDE);
  }

  /** Render the end of the selector */
  public int doEndTag() throws JspException {
    doGroupLinks();
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

} // NameSelector

/*
 * $Log: MemberNameFormatSelectorTag.java,v $
 * Revision 1.3  2003/02/26 03:38:46  rphall
 * Added copyright and GPL license
 *
 * Revision 1.2  2002/02/18 18:06:22  rphall
 * Ran dos2unix to remove ^M (carriage return) from end of lines
 *
 * Revision 1.1.1.1  2002/01/03 21:57:28  rphall
 * Initial load, 5th try, Jan-03-2002 4:57 PM
 *
 * Revision 1.1  2002/01/01 18:54:07  rphall
 * Selects first or last name
 *
 */

