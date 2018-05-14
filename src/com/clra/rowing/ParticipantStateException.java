/*
 * Copyright (c) Carnegie Lake Rowing Association 2002. All rights reserved.
 * Distributed under the GPL license. See doc/COPYING.
 * $RCSfile: ParticipantStateException.java,v $
 * $Date: 2003/02/26 03:38:45 $
 * $Revision: 1.3 $
 */

package com.clra.rowing;

/**
 * Indicates an error during retrieval or storage of Participant data.
 *
 * @version $Id: ParticipantStateException.java,v 1.3 2003/02/26 03:38:45 rphall Exp $
 * @author <a href="mailto:rphall@pluto.njcc.com">Rick Hall</a>
 */
public class ParticipantStateException extends Exception {

  public ParticipantStateException() {}
  public ParticipantStateException(String msg) { super(msg); }

} // ParticipantStateException

/*
 * $Log: ParticipantStateException.java,v $
 * Revision 1.3  2003/02/26 03:38:45  rphall
 * Added copyright and GPL license
 *
 * Revision 1.2  2002/02/18 18:04:46  rphall
 * Ran dos2unix to remove ^M (carriage return) from end of lines
 *
 * Revision 1.1.1.1  2002/01/03 21:57:28  rphall
 * Initial load, 5th try, Jan-03-2002 4:57 PM
 *
 * Revision 1.1  2001/11/18 18:16:03  rphall
 * Fixed compilation problems
 *
 */

