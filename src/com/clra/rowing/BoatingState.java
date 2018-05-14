/*
 * Copyright (c) Carnegie Lake Rowing Association 2002. All rights reserved.
 * Distributed under the GPL license. See doc/COPYING.
 * $RCSfile: BoatingState.java,v $
 * $Date: 2003/02/26 03:38:45 $
 * $Revision: 1.3 $
 */

package com.clra.rowing;

/**
 * Represents the state of a boating.
 *
 * @version $Id: BoatingState.java,v 1.3 2003/02/26 03:38:45 rphall Exp $
 * @author <a href="mailto:rphall@pluto.njcc.com">Rick Hall</a>
 */
public class BoatingState {

  public final static String NAME_INITIAL = "INITIAL";

  public final static String NAME_FINAL = "FINAL";

  public final static String NAME_CANCELLED = "CANCELLED";

  public final static BoatingState INITIAL =
      new BoatingState( NAME_INITIAL );

  public final static BoatingState FINAL =
      new BoatingState( NAME_FINAL );

  public final static BoatingState CANCELLED =
      new BoatingState( NAME_CANCELLED );

  private final String name;

  protected BoatingState( String name ) {

    // Assign blank final
    this.name = name;

    // Enforce preconditions
  }

  public String getName() {
    return name;
  }

  public String toString() {
    return name;
  }

} // BoatingState

/*
 * $Log: BoatingState.java,v $
 * Revision 1.3  2003/02/26 03:38:45  rphall
 * Added copyright and GPL license
 *
 * Revision 1.2  2002/02/18 18:03:47  rphall
 * Ran dos2unix to remove ^M (carriage return) from end of lines
 *
 * Revision 1.1.1.1  2002/01/03 21:57:28  rphall
 * Initial load, 5th try, Jan-03-2002 4:57 PM
 *
 * Revision 1.1  2001/11/23 18:34:08  rphall
 * Major revision.
 *
 */

