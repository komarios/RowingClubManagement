/*
 * Copyright (c) Carnegie Lake Rowing Association 2002. All rights reserved.
 * Distributed under the GPL license. See doc/COPYING.
 * $RCSfile: Constants.java,v $
 * $Date: 2003/02/26 03:38:46 $
 * $Revision: 1.4 $
 */

package com.clra.web;

/**
 * Manifest constants for the CLRA application.
 *
 * @version $Revision: 1.4 $ $Date: 2003/02/26 03:38:46 $
 */
public final class Constants {

  /**
   * The session scope attribute under which the Authentication object
   * for the currently logged in user is stored.
   */
  public static final String AUTHENTICATION_KEY = "authentication";

  /** The scope scope attribute under which the rowing session id is stored */
  public static final String ROWINGSESSION_KEY = "rowingId";

  /** The scope scope attribute under which the participant id is stored */
  public static final String PARTICIPANT_KEY = "participantId";

  /**
   * The session scope attribute under which the MemberView object
   * for the currently logged in user is stored.
   */
  public static final String USER_KEY = "member";

} // 

/*
 * $Log: Constants.java,v $
 * Revision 1.4  2003/02/26 03:38:46  rphall
 * Added copyright and GPL license
 *
 * Revision 1.3  2003/02/19 22:30:32  rphall
 * Removed gratuitous use of CLRA acronym
 *
 * Revision 1.2  2002/02/18 18:05:44  rphall
 * Ran dos2unix to remove ^M (carriage return) from end of lines
 */

