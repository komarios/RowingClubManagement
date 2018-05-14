/*
 * Copyright (c) Carnegie Lake Rowing Association 2002. All rights reserved.
 * Distributed under the GPL license. See doc/COPYING.
 * $RCSfile: RowingSessionLevelBean.java,v $
 * $Date: 2003/02/26 03:38:46 $
 * $Revision: 1.3 $
 */

package com.clra.web;

import com.clra.rowing.RowingSessionLevel;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Represents the level of a rowing session.
 *
 * @version $Id: RowingSessionLevelBean.java,v 1.3 2003/02/26 03:38:46 rphall Exp $
 * @author <a href="mailto:rphall@pluto.njcc.com">Rick Hall</a>
 */
public class RowingSessionLevelBean implements Iterator, Serializable {

  private final static ArrayList list = new ArrayList();
  static {

    String label = Text.getMessage( "rowinglevel.ltr" );
    LabelValueBean lvb =
      new LabelValueBean( label, RowingSessionLevel.LTR.getName() );
    list.add( lvb );

    label = Text.getMessage( "rowinglevel.regular" );
    lvb = new LabelValueBean( label, RowingSessionLevel.REGULAR.getName() );
    list.add( lvb );

  } // static

  private final Iterator iterator = list.iterator();

  public boolean hasNext() {
    return iterator.hasNext();
  }

  public Object next() {
    return iterator.next();
  }

  /* @exception UnsupportedOperationException always thrown */
  public void remove() {
    throw new UnsupportedOperationException( "remove not supported" );
  }

} // RowingSessionLevelBean

/*
 * $Log: RowingSessionLevelBean.java,v $
 * Revision 1.3  2003/02/26 03:38:46  rphall
 * Added copyright and GPL license
 *
 * Revision 1.2  2002/02/18 18:07:05  rphall
 * Ran dos2unix to remove ^M (carriage return) from end of lines
 *
 * Revision 1.1.1.1  2002/01/03 21:57:28  rphall
 * Initial load, 5th try, Jan-03-2002 4:57 PM
 *
 * Revision 1.1  2001/12/01 14:06:05  rphall
 * JSP bean
 *
 */

