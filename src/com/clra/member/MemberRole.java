/*
 * Copyright (c) Carnegie Lake Rowing Association 2002. All rights reserved.
 * Distributed under the GPL license. See doc/COPYING.
 * $RCSfile: MemberRole.java,v $
 * $Date: 2003/03/01 00:45:14 $
 * $Revision: 1.8 $
 */

package com.clra.member;

import java.io.Serializable;

/**
 * Encapsulates information about the role of a member within the club.
 * A member's roles determine his or her access rights. For example, a
 * treasurer or a member-manager may update the account information of other
 * members, but they can not create or edit rowing sessions. Conversely,
 * a captain or a coach may create and edit rowing sessions, but they can
 * not update the account information of other members. If a member has no
 * roles, the member has no access rights to the website.
 * @version $Revision: 1.8 $ $Date: 2003/03/01 00:45:14 $
 * @author <a href="mailto:rphall@pluto.njcc.com">Rick Hall</a>
 */
public class MemberRole implements Serializable {

  /** The default group for member roles */
  public final static String ROLEGROUP_ROLES = "Roles"; // case set by JAAS

  /** Designates the  default role for active members */
  public final static String ROLE_MEMBER = "MEMBER";

  /** Designates a member who can assign boatings */
  public final static String ROLE_COACH = "COACH";

  /** Designates a member who is in charge of club operations */
  public final static String ROLE_CAPTAIN = "CAPTAIN";

  /** Designates a member who takes attendance */
  public final static String ROLE_SESSIONMGR = "SESSIONMGR";

  /** Designates a member who is in charge of invoicing */
  public final static String ROLE_TREASURER = "TREASURER";

  /** Designates a member who assigns roles to other members */
  public final static String ROLE_MEMBERMGR = "MEMBERMGR";

  /** The default role for members */
  public final static MemberRole MEMBER =
      new MemberRole( ROLE_MEMBER, ROLEGROUP_ROLES );

  /** The role of a member who can assign boatings */
  public final static MemberRole COACH =
      new MemberRole( ROLE_COACH, ROLEGROUP_ROLES );

  /** The role of a member who is in charge of club operations */
  public final static MemberRole CAPTAIN =
      new MemberRole( ROLE_CAPTAIN, ROLEGROUP_ROLES );

  /** The role of a member who takes attendance */
  public final static MemberRole SESSIONMGR =
      new MemberRole( ROLE_SESSIONMGR, ROLEGROUP_ROLES );

  /** The role of a member who is in charge of invoicing */
  public final static MemberRole TREASURER =
      new MemberRole( ROLE_TREASURER, ROLEGROUP_ROLES );

  /** The role of a member who assigns roles to other members */
  public final static MemberRole MEMBERMGR =
      new MemberRole( ROLE_MEMBERMGR, ROLEGROUP_ROLES );

  public final static String[] ALLOWED_ROLES() {
    return new String[] {
      ROLE_MEMBER,
      ROLE_COACH,
      ROLE_CAPTAIN,
      ROLE_SESSIONMGR,
      ROLE_TREASURER,
      ROLE_MEMBERMGR
    };
  }

  public final static String[] ALLOWED_ROLEGROUPS() {
    return new String[] {
      ROLEGROUP_ROLES
    };
  }

  /** The role of the Member */
  private final String role;

  /** The JAAS-related group of the role */
  private final String roleGroup;

  /** Creates a role in the default role group */
  public MemberRole( String role ) {
    this( role, ROLEGROUP_ROLES );
  }

  /** Creates a role in the specified role group */
  public MemberRole( String role, String roleGroup ) {
    // Preconditions
    if ( role == null ) {
      throw new IllegalArgumentException( "null role" );
    }
    if ( roleGroup == null ) {
      throw new IllegalArgumentException( "null role group" );
    }
    role = role.trim().toUpperCase();

    // Must be case-sensitive comparison for roleGroup
    roleGroup = roleGroup.trim();

    boolean isOK = false;
    String[] ALLOWED = ALLOWED_ROLES();
    for ( int i=0; i<ALLOWED.length; i++ ) {
      if ( ALLOWED[i].equals(role) ) {
        isOK = true;
        break;
      }
    }
    if ( !isOK ) {
      throw new IllegalArgumentException( "invalid role == " + role );
    }

    isOK = false;
    ALLOWED = ALLOWED_ROLEGROUPS();
    for ( int i=0; i<ALLOWED.length; i++ ) {
      // Must be case-sensitive comparison
      if ( ALLOWED[i].equals(roleGroup) ) {
        isOK = true;
        break;
      }
    }
    if ( !isOK ) {
      throw new IllegalArgumentException( "invalid roleGroup == " + roleGroup );
    }

    this.role = role;
    this.roleGroup = roleGroup;

  } // ctor(String,String)

  public String getRole() {
    return this.role;
  }

  public String getRoleGroup() {
    return this.roleGroup;
  }

  public String toString() {
    return this.role;
  }

  /** For now, string comparison assume the default group */
  public boolean equals( Object o ) {
    boolean retVal;
    if ( o == null ) {
      retVal = false;
    }
    else if ( o instanceof MemberRole ) {
      MemberRole that = (MemberRole) o;
      retVal = this.role.equalsIgnoreCase( that.role );
      retVal = retVal && this.roleGroup.equalsIgnoreCase( that.roleGroup );
    }
    else {
      retVal = false;
    }

    return retVal;
  } // equals(Object)

  public int hashCode() {
    int retVal = this.role.trim().toUpperCase().hashCode();
    retVal += this.roleGroup.trim().toUpperCase().hashCode();
    return retVal;
  }

} // MemberRole

/*
 * $Log: MemberRole.java,v $
 * Revision 1.8  2003/03/01 00:45:14  rphall
 * Removed no-param default constructor
 *
 * Revision 1.7  2003/02/28 14:05:50  rphall
 * Added default constructor so that class could be used as a Java bean
 *
 * Revision 1.6  2003/02/26 03:38:45  rphall
 * Added copyright and GPL license
 *
 * Revision 1.5  2003/02/21 05:01:17  rphall
 * Made RoleGroup case-sensitive
 *
 * Revision 1.4  2003/02/19 22:26:39  rphall
 * Removed gratuitous use of CLRA acronym
 *
 * Revision 1.3  2003/02/16 00:43:57  rphall
 * Fixed bug in constructor
 *
 * Revision 1.2  2003/02/15 04:31:42  rphall
 * Changes connected to major revision of MemberBean
 *
 */

