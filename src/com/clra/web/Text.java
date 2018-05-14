/*
 * Copyright (c) Carnegie Lake Rowing Association 2002. All rights reserved.
 * Distributed under the GPL license. See doc/COPYING.
 * $RCSfile: Text.java,v $
 * $Date: 2003/02/26 03:38:46 $
 * $Revision: 1.3 $
 */

package com.clra.web;

import java.io.Serializable;
import org.apache.struts.util.MessageResources;
import org.apache.struts.util.PropertyMessageResources;
import org.apache.struts.util.PropertyMessageResourcesFactory;

/**
 * A thin wrapper around the Jakarta MessageResources class. This saves very
 * GUI class from having to reproduce the same code.
 */
public final class Text implements Serializable {

  private final static String messages = "com.clra.web.clra";

  private final static MessageResources resources =
    new PropertyMessageResources(
      new PropertyMessageResourcesFactory(), messages );
  static {
    resources.setReturnNull(false);
  }

  private Text() {}

  /**
   * Returns a text message for the specified key, for the default Locale.
   *
   * @param key The message key to look up
   */
  public static String getMessage(String key) {
    return resources.getMessage(key);
  }

  /**
   * Returns a text message after parametric replacement of the specified
   * parameter placeholders.
   *
   * @param key The message key to look up
   * @param args An array of replacement parameters for placeholders
   */
  public static String getMessage(String key, Object args[]) {
    return resources.getMessage(key,args);
  }

  /**
   * Returns a text message after parametric replacement of the specified
   * parameter placeholders.
   *
   * @param key The message key to look up
   * @param arg0 The replacement for placeholder {0} in the message
   */
  public static String getMessage(String key, Object arg0) {
    return resources.getMessage(key,arg0);
  }

  /**
   * Returns a text message after parametric replacement of the specified
   * parameter placeholders.
   *
   * @param key The message key to look up
   * @param arg0 The replacement for placeholder {0} in the message
   * @param arg1 The replacement for placeholder {1} in the message
   */
  public static String getMessage(String key, Object arg0, Object arg1) {
    return resources.getMessage(key,arg0,arg1);
  }

  /**
   * Returns a text message after parametric replacement of the specified
   * parameter placeholders.
   *
   * @param key The message key to look up
   * @param arg0 The replacement for placeholder {0} in the message
   * @param arg1 The replacement for placeholder {1} in the message
   * @param arg2 The replacement for placeholder {2} in the message
   */
  public static String getMessage(String key, Object arg0, Object arg1,
    Object arg2) {
    return resources.getMessage( key, arg0, arg1, arg2 );
  }

  /**
   * Returns a text message after parametric replacement of the specified
   * parameter placeholders.
   *
   * @param key The message key to look up
   * @param arg0 The replacement for placeholder {0} in the message
   * @param arg1 The replacement for placeholder {1} in the message
   * @param arg2 The replacement for placeholder {2} in the message
   * @param arg3 The replacement for placeholder {3} in the message
   */
  public static String getMessage(String key, Object arg0, Object arg1,
                       Object arg2, Object arg3) {
    return resources.getMessage( key, arg0, arg1, arg2, arg3 );
  }

} // Text

/*
 * $Log: Text.java,v $
 * Revision 1.3  2003/02/26 03:38:46  rphall
 * Added copyright and GPL license
 *
 * Revision 1.2  2002/02/18 18:07:23  rphall
 * Ran dos2unix to remove ^M (carriage return) from end of lines
 *
 * Revision 1.1.1.1  2002/01/03 21:57:28  rphall
 * Initial load, 5th try, Jan-03-2002 4:57 PM
 *
 * Revision 1.1  2001/12/01 14:06:05  rphall
 * JSP bean
 *
 */

