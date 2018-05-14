/*
 * Copyright (c) Carnegie Lake Rowing Association 2002. All rights reserved.
 * Distributed under the GPL license. See doc/COPYING.
 * $RCSfile: SeatSnapshot.java,v $
 * $Date: 2003/02/26 03:38:45 $
 * $Revision: 1.4 $
 */

package com.clra.rowing;

/**
 * Information about a seat assignment in a boating for a rowing session.
 *
 * @version $Id: SeatSnapshot.java,v 1.4 2003/02/26 03:38:45 rphall Exp $
 * @author <a href="mailto:rphall@pluto.njcc.com">Rick Hall</a>
 */
public class SeatSnapshot {

  private final int seatId;
  private final BoatingView boating;
  private final int seatNumber;

  public SeatSnapshot( int seatId, BoatingView boating, int seatNumber ) {
    this.seatId = seatId;
    this.boating = boating;
    this.seatNumber = seatNumber;
  }

  public int getSeatId() {
    return this.seatId;
  }

  public BoatingView getBoating() {
    return this.boating;
  }

  public int getSeatNumber() {
    return this.seatNumber;
  }

} // SeatSnapshot

/*
 * $Log: SeatSnapshot.java,v $
 * Revision 1.4  2003/02/26 03:38:45  rphall
 * Added copyright and GPL license
 *
 * Revision 1.3  2003/02/19 22:09:16  rphall
 * Removed gratuitous use of CLRA acronym
 *
 * Revision 1.2  2002/02/18 18:05:11  rphall
 * Ran dos2unix to remove ^M (carriage return) from end of lines
 */

