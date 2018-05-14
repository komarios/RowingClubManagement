/*
 * Copyright (c) Carnegie Lake Rowing Association 2002. All rights reserved.
 * Distributed under the GPL license. See doc/COPYING.
 * $RCSfile: LabelValueBean.java,v $
 * $Date: 2003/02/26 03:38:46 $
 * $Revision: 1.3 $
 */

/*
 * Copyright (c) 1999-2001 The Apache Software Foundation.  All rights
 * reserved.
 *
 * The Apache Software License, Version 1.1 (see licenses)
*/
package com.clra.web;

/**
 * Simple JavaBean to represent label-value pairs for use in collections
 * that are utilized by the <code>&lt;form:options&gt;</code> tag.
 *
 * @author Craig R. McClanahan
 *         -- original author
 * @author <a href="mailto:rphall@pluto.njcc.com">Rick Hall</a>
 *         -- adapted to CLRA
 * @version $Revision: 1.3 $ $Date: 2003/02/26 03:38:46 $
 */
public class LabelValueBean {

  /**
   * Construct a new LabelValueBean with the specified values.
   *
   * @param label The label to be displayed to the user
   * @param value The value to be returned to the server
   */
  public LabelValueBean(String label, String value) {
    this.label = label;
    this.value = value;
  }

  /** The label to be displayed to the user. */
  protected String label = null;

  public String getLabel() {
    return (this.label);
  }

  /** The value to be returned to the server. */
  protected String value = null;

  public String getValue() {
    return (this.value);
  }

  /**
   * Return a string representation of this object.
   */
  public String toString() {
    StringBuffer sb = new StringBuffer("LabelValueBean[");
    sb.append(this.label);
    sb.append(", ");
    sb.append(this.value);
    sb.append("]");
    return (sb.toString());
  }

} // LabelValueBean

/*
 * $Log: LabelValueBean.java,v $
 * Revision 1.3  2003/02/26 03:38:46  rphall
 * Added copyright and GPL license
 *
 * Revision 1.2  2002/02/18 18:06:07  rphall
 * Ran dos2unix to remove ^M (carriage return) from end of lines
 *
 * Revision 1.1.1.1  2002/01/03 21:57:28  rphall
 * Initial load, 5th try, Jan-03-2002 4:57 PM
 *
 * Revision 1.1  2001/12/01 14:06:05  rphall
 * JSP bean
 *
 */

