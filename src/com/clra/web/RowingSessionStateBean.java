/*
 * Copyright (c) Carnegie Lake Rowing Association 2002. All rights reserved.
 * Distributed under the GPL license. See doc/COPYING.
 * $RCSfile: RowingSessionStateBean.java,v $
 * $Date: 2003/02/26 03:38:46 $
 * $Revision: 1.3 $
 */

package com.clra.web;

import com.clra.rowing.RowingSessionState;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Represents the state of a rowing session.
 *
 * @version $Id: RowingSessionStateBean.java,v 1.3 2003/02/26 03:38:46 rphall Exp $
 * @author <a href="mailto:rphall@pluto.njcc.com">Rick Hall</a>
 */
public class RowingSessionStateBean implements Iterator, Serializable {

  private final static ArrayList list = new ArrayList();
  static {

    String label = Text.getMessage( "rowingstate.tenative" );
    LabelValueBean lvb =
      new LabelValueBean( label, RowingSessionState.TENATIVE.getName() );
    list.add( lvb );

    label = Text.getMessage( "rowingstate.open" );
    lvb = new LabelValueBean( label, RowingSessionState.OPEN.getName() );
    list.add( lvb );

    label = Text.getMessage( "rowingstate.locked" );
    lvb = new LabelValueBean( label, RowingSessionState.LOCKED.getName() );
    list.add( lvb );

    label = Text.getMessage( "rowingstate.boating1" );
    lvb = new LabelValueBean( label, RowingSessionState.BOATING1.getName() );
    list.add( lvb );

    label = Text.getMessage( "rowingstate.boating2" );
    lvb = new LabelValueBean( label, RowingSessionState.BOATING2.getName() );
    list.add( lvb );

    label = Text.getMessage( "rowingstate.complete" );
    lvb = new LabelValueBean( label, RowingSessionState.COMPLETE.getName() );
    list.add( lvb );

    label = Text.getMessage( "rowingstate.invoicing" );
    lvb = new LabelValueBean( label, RowingSessionState.INVOICING.getName() );
    list.add( lvb );

    label = Text.getMessage( "rowingstate.closed" );
    lvb = new LabelValueBean( label, RowingSessionState.CLOSED.getName() );
    list.add( lvb );

    label = Text.getMessage( "rowingstate.cancelled" );
    lvb = new LabelValueBean( label, RowingSessionState.CANCELLED.getName() );
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

} // RowingSessionStateBean

/*
 * $Log: RowingSessionStateBean.java,v $
 * Revision 1.3  2003/02/26 03:38:46  rphall
 * Added copyright and GPL license
 *
 * Revision 1.2  2002/02/18 18:07:07  rphall
 * Ran dos2unix to remove ^M (carriage return) from end of lines
 *
 * Revision 1.1.1.1  2002/01/03 21:57:28  rphall
 * Initial load, 5th try, Jan-03-2002 4:57 PM
 *
 * Revision 1.1  2001/12/01 14:06:05  rphall
 * JSP bean
 *
 */

