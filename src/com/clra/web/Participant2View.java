/*
 * Copyright (c) Carnegie Lake Rowing Association 2002. All rights reserved.
 * Distributed under the GPL license. See doc/COPYING.
 * $RCSfile: Participant2View.java,v $
 * $Date: 2003/02/26 03:38:46 $
 * $Revision: 1.3 $
 */

package com.clra.web;

import com.clra.member.MemberName;
import com.clra.rowing.ParticipantSnapshot;
import com.clra.rowing.Participant2Snapshot;

/**
 * Read-only information about a member's participation in rowing session.
 * Extends ParticipantView by caching the name of a participant.
 *
 * @version $Revision: 1.3 $ $Date: 2003/02/26 03:38:46 $
 * @author <a href="mailto:rphall@pluto.njcc.com">Rick Hall</a>
 */
public class Participant2View extends ParticipantView {

  private MemberName memberName;

  /**
   * Constructs an invalid instance. Use this constructor only for JSP beans
   * and immediately set valid values via setXxx(..).
   */
  public Participant2View() {
    this.memberName = null;
  }

  public Participant2View( ParticipantSnapshot data, MemberName memberName ) {
    super( data );
    setMemberName( memberName );
  }

  public Participant2View( Participant2Snapshot ps2 ) {
    super( ps2.getData() );
    setMemberName( ps2.getMemberName() );
  }

  public void setMemberName( MemberName memberName ) {
    if ( memberName == null ) {
      throw new IllegalArgumentException( "null member name" );
    }
    this.memberName = memberName;
  }

  public MemberName getMemberName() {
    if ( memberName == null ) {
      throw new IllegalStateException( "null member name" );
    }
    return this.memberName;
  }

} // ParticipantView

/*
 * $Log: Participant2View.java,v $
 * Revision 1.3  2003/02/26 03:38:46  rphall
 * Added copyright and GPL license
 *
 * Revision 1.2  2002/02/18 18:06:47  rphall
 * Ran dos2unix to remove ^M (carriage return) from end of lines
 *
 * Revision 1.1.1.1  2002/01/03 21:57:28  rphall
 * Initial load, 5th try, Jan-03-2002 4:57 PM
 *
 * Revision 1.2  2001/12/31 14:32:58  rphall
 * Renamed 'member.Name' to 'member.MemberName'
 *
 * Revision 1.1  2001/12/15 02:21:49  rphall
 * Participant data plus member name
 *
 */

