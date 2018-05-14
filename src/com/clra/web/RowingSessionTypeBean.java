/*
 * Copyright (c) Carnegie Lake Rowing Association 2002. All rights reserved.
 * Distributed under the GPL license. See doc/COPYING.
 * $RCSfile: RowingSessionTypeBean.java,v $
 * $Date: 2003/02/26 03:38:46 $
 * $Revision: 1.3 $
 */

package com.clra.web;

import com.clra.rowing.RowingSessionType;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Represents the type of a rowing session.
 *
 * @version $Id: RowingSessionTypeBean.java,v 1.3 2003/02/26 03:38:46 rphall Exp $
 * @author <a href="mailto:rphall@pluto.njcc.com">Rick Hall</a>
 */
public class RowingSessionTypeBean implements Iterator, Serializable {

  private final static ArrayList list = new ArrayList();
  static {

    String label = Text.getMessage( "rowingtype.practice" );
    LabelValueBean lvb =
      new LabelValueBean( label, RowingSessionType.PRACTICE.getName() );
    list.add( lvb );

    label = Text.getMessage( "rowingtype.regatta" );
    lvb = new LabelValueBean( label, RowingSessionType.REGATTA.getName() );
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

} // RowingSessionTypeBean

/*
 * $Log: RowingSessionTypeBean.java,v $
 * Revision 1.3  2003/02/26 03:38:46  rphall
 * Added copyright and GPL license
 *
 * Revision 1.2  2002/02/18 18:07:10  rphall
 * Ran dos2unix to remove ^M (carriage return) from end of lines
 *
 * Revision 1.1.1.1  2002/01/03 21:57:28  rphall
 * Initial load, 5th try, Jan-03-2002 4:57 PM
 *
 * Revision 1.1  2001/12/01 14:06:05  rphall
 * JSP bean
 *
 */

