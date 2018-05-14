/*
 * Copyright (c) Carnegie Lake Rowing Association 2002. All rights reserved.
 * Distributed under the GPL license. See doc/COPYING.
 * $RCSfile: MemberInfoForm.java,v $
 * $Date: 2003/03/02 15:32:06 $
 * $Revision: 1.16 $
 */

package com.clra.web;

import com.clra.member.AccountType;
import com.clra.member.Address;
import com.clra.member.Email;
import com.clra.member.MemberName;
import com.clra.member.MemberRole;
import com.clra.member.Telephone;
import com.clra.util.ValidationException;

import java.text.SimpleDateFormat;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Category;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * Form bean for the user profile page.
 * 
 * @author <a href="mailto:jmstone@nerc.com">Jan Stone</a>
 * @author <a href="mailto:rphall@pluto.njcc.com">Rick Hall</a>
 */
public class MemberInfoForm extends ActionForm  {

  private final  static String base = MemberInfoForm.class.getName();
  private final  static Category theLog = Category.getInstance( base );

  /**
   * Indicates the purpose of this workflow is to edit an
   * existing member with restrictions (by a member of the club)
   */
  public final static String FUNCTION_MEMBEREDIT = "MemberEdit";

  /**
   * Indicates the purpose of this workflow is to create
   * a new member.
   */
  public final static String FUNCTION_ADMINCREATE = "AdminCreate";

  /**
   * Indicates the purpose of this workflow is to edit an
   * existing member without restriction (by an admin of the club)
   */
  public final static String FUNCTION_ADMINEDIT = "AdminEdit";

  public static String[] validFunctions() {
    return new String[] {
      FUNCTION_MEMBEREDIT, FUNCTION_ADMINCREATE, FUNCTION_ADMINEDIT
    };
  }

  private String function = null; // required

  private String first  = null; // required
  private String middle = null; // optional
  private String last   = null; // required
  private String suffix = null; // optional

  private Integer id = null;                 // optional during creation
  private String accountName = null;         // required
  private String accountNameOriginal = null; // workflow flag
  private String accountPassword = null;     // required
  private String confirmPassword = null;     // required

  private Calendar birthDate  = null; // optional

  private int accountYear = -1;        // required
  private String strAccountType = null; // required

  private Set accountRoles = new HashSet(); // optional (but typical)

  private String emailAddr = null;  // optional

  private String phoneEveningAreaCode = null; // required
  private String phoneEveningExchange = null; // required
  private String phoneEveningLocal    = null; // required
  private String phoneEveningExt      = null; // optional

  private String phoneDayAreaCode = null; // optional
  private String phoneDayExchange = null; // optional
  private String phoneDayLocal    = null; // optional
  private String phoneDayExt      = null; // optional

  private String phoneOtherAreaCode = null; // optional
  private String phoneOtherExchange = null; // optional
  private String phoneOtherLocal    = null; // optional
  private String phoneOtherExt      = null; // optional

  private String street1 = null; // required
  private String street2 = null; // optional
  private String city    = null; // required
  private String state   = null; // required
  private String zip     = null; // required

  public String getFunction() {
    return this.function;
  }

  public void setFunction(String f) {
    this.function = f == null ? null : f ;
  }

  public boolean isAdminCreate() {
    return FUNCTION_ADMINCREATE.equalsIgnoreCase( this.function ) ;
  }

  public boolean isAdminEdit() {
    return FUNCTION_ADMINEDIT.equalsIgnoreCase( this.function ) ;
  }

  public boolean isMemberEdit() {
    return FUNCTION_MEMBEREDIT.equalsIgnoreCase( this.function ) ;
  }

  public boolean isRestricted() {
    return !( this.isAdminCreate() || this.isAdminEdit() );
  }

  public String getFirstName() {
    return this.first;
  }

  public void setFirstName(String nm) {
    this.first = nm == null ? null : nm.trim() ;
  }

  public boolean hasMiddleName() {
    return this.middle != null;
  }

  public String getMiddleName() {
    return this.middle;
  }

  public void setMiddleName(String nm ) {
    this.middle = nm == null ? null : nm.trim() ;
  }

  public String getLastName() {
    return this.last;
  }

  public void setLastName(String nm) {
    this.last = nm == null ? null : nm.trim() ;
  }

  public boolean hasSuffix() {
    return this.suffix != null;
  }

  public String getSuffix() {
    return this.suffix;
  }

  public void setSuffix(String s) {
    this.suffix = s == null ? null : s.trim() ;
  }

  public String getFullName() {

    StringBuffer sb = new StringBuffer();
    sb.append( getFirstName() );
    sb.append( " " );
    if ( hasMiddleName() ) {
      sb.append( getMiddleName() );
      sb.append( " " );
    }
    sb.append( getLastName() );
    if ( hasSuffix() ) {
      sb.append( " " );
      sb.append( getSuffix() );
    }

    String retVal = new String(sb);

    return retVal;
  } // getFullName()

  public Integer getId() {
    if ( this.id == null
          && !getFunction().equalsIgnoreCase(FUNCTION_ADMINCREATE) ) {
      throw new IllegalStateException( "null id && function != ADMINCREATE" );
    }
    return this.id;
  }

  public void setId( Integer I ) {
    this.id = I;
  }

  public String getAccountNameOriginal() {
    return this.accountNameOriginal;
  }

  public void setAccountNameOriginal( String str ) {
    this.accountNameOriginal = str == null ? null : str.trim() ;
  } 

  public String getAccountName() {
    return this.accountName;
  }

  public void setAccountName( String str ) {
    this.accountName = str == null ? null : str.trim() ;
  } 

  public String getAccountPassword() {
    return this.accountPassword;
  }

  public void setAccountPassword( String str ) {
    this.accountPassword = str == null ? null : str.trim() ;
  } 

  public String getConfirmPassword() {
    return this.confirmPassword;
  }

  public void setConfirmPassword( String str ) {
    this.confirmPassword = str == null ? null : str.trim() ;
  }

  public boolean getHasKnownBirthDate() {
    return birthDate != null;
  }

  public void setBirthDate( Date d ) {
    if ( d == null ) {
      this.birthDate = null;
    } else {
      if ( this.birthDate == null ) {
        this.birthDate = Calendar.getInstance();
      }
      this.birthDate.setTime( d );
    }
  }

  public int getBirthMonthInt() {
    int retVal = -1;
    if ( this.getHasKnownBirthDate() ) {
      retVal = this.birthDate.get(Calendar.MONTH) + 1;
    }
    return retVal;
  }

  public int getBirthDayInt() {
    int retVal = -1;
    if ( this.getHasKnownBirthDate() ) {
      retVal = this.birthDate.get(Calendar.DAY_OF_MONTH);
    }
    return retVal;
  }

  public int getBirthYearInt() {
    int retVal = -1;
    if ( this.getHasKnownBirthDate() ) {
      retVal = this.birthDate.get(Calendar.YEAR);
    }
    return retVal;
  }

  public String getBirthMonth() {
    String retVal = "";
    if ( this.getHasKnownBirthDate() ) {
      retVal = "" + this.getBirthMonthInt();
    }
    return retVal;
  }

  public String getBirthDay() {
    String retVal = "";
    if ( this.getHasKnownBirthDate() ) {
      retVal = "" + getBirthDayInt();
    }
    return retVal;
  }

  public String getBirthYear() {
    String retVal = "";
    if ( this.getHasKnownBirthDate() ) {
      retVal = "" + getBirthYearInt();
    }
    return retVal;
  }

  public void setBirthMonth( String s )  {
    if ( s != null && s.trim().length() > 0 ) {
      final int m = Integer.parseInt(s.trim()) - 1;
      if ( 0 < m && m < 13 ) {
        if ( this.birthDate == null ) {
          this.birthDate = Calendar.getInstance();
        }
        this.birthDate.set(Calendar.MONTH,m);
      }
    }
  }

  public void setBirthDay( String s ) {
    if ( s != null && s.trim().length() > 0 ) {
      final int d = Integer.parseInt(s.trim());
      if ( 0 < d && d < 32 ) {
        if ( this.birthDate == null ) {
          this.birthDate = Calendar.getInstance();
        }
        this.birthDate.set(Calendar.DAY_OF_MONTH,d);
      }
    }
  }

  public void setBirthYear( String s ) {
    if ( s != null && s.trim().length() > 0 ) {
      final int y = Integer.parseInt(s.trim());
      if ( 1900 < y && y < 2032 ) {
        if ( this.birthDate == null ) {
          this.birthDate = Calendar.getInstance();
        }
        this.birthDate.set(Calendar.YEAR,y);
      }
    }
  }

  public int getAccountYear() {
    return this.accountYear;
  }

  public void setAccountYear( int s ) {
    this.accountYear = s;
  } 
  
  public String getAccountTypeStr() {
    return this.strAccountType;
  }

  public void setAccountTypeStr( String str ) {
    this.strAccountType = str == null ? null : str.trim() ;
  }

  public MemberRole[] getMemberRoles() {
    MemberRole[] retVal =
      (MemberRole[]) accountRoles.toArray(new MemberRole[0]);
    return retVal;
  }

  public void setMemberRoles( MemberRole[] roles ) {
    if ( roles == null ) {
      throw new IllegalArgumentException( "null role array" );
    }
    accountRoles = new HashSet();
    for ( int i=0; i<roles.length; i++ ) {
      if ( roles[i] == null ) {
        throw new IllegalArgumentException( "null role" );
      }
      accountRoles.add( roles[i] );
    }
    return;
  } // setMemberRoles(MemberRole[])

  public boolean isCaptain() {
    return accountRoles.contains( MemberRole.CAPTAIN );
  }

  public void setCaptain( boolean isCaptain ) {
    if ( isCaptain ) {
      accountRoles.add( MemberRole.CAPTAIN );
    } else {
      accountRoles.remove( MemberRole.CAPTAIN );
    }
    return;
  } // setCaptain(boolean)

  public boolean isCoach() {
    return accountRoles.contains( MemberRole.COACH );
  }

  public void setCoach( boolean isCoach ) {
    if ( isCoach ) {
      accountRoles.add( MemberRole.COACH );
    } else {
      accountRoles.remove( MemberRole.COACH );
    }
    return;
  } // setCoach(boolean)

  public boolean isMember() {
    return accountRoles.contains( MemberRole.MEMBER );
  }

  public void setMember( boolean isMember ) {
    if ( isMember ) {
      accountRoles.add( MemberRole.MEMBER );
    } else {
      accountRoles.remove( MemberRole.MEMBER );
    }
    return;
  } // setMember(boolean)

  public boolean isMemberManager() {
    return accountRoles.contains( MemberRole.MEMBERMGR );
  }

  public void setMemberManager( boolean isMemberManager ) {
    if ( isMemberManager ) {
      accountRoles.add( MemberRole.MEMBERMGR );
    } else {
      accountRoles.remove( MemberRole.MEMBERMGR );
    }
    return;
  } // setMemberManager(boolean)

  public boolean isSessionManager() {
    return accountRoles.contains( MemberRole.SESSIONMGR );
  }

  public void setSessionManager( boolean isSessionManager ) {
    if ( isSessionManager ) {
      accountRoles.add( MemberRole.SESSIONMGR );
    } else {
      accountRoles.remove( MemberRole.SESSIONMGR );
    }
    return;
  } // setSessionManager(boolean)

  public boolean isTreasurer() {
    return accountRoles.contains( MemberRole.TREASURER );
  }

  public void setTreasurer( boolean isTreasurer ) {
    if ( isTreasurer ) {
      accountRoles.add( MemberRole.TREASURER );
    } else {
      accountRoles.remove( MemberRole.TREASURER );
    }
    return;
  } // setTreasurer(boolean)

  public boolean hasEmail() {
    return (this.emailAddr != null);
  }

  public String getEmail() {
    return this.emailAddr;
  }

  public void setEmail( String str) {
    this.emailAddr = str == null ? null : str.trim() ;
    if ( this.emailAddr != null
      && this.emailAddr.trim().length() == 0 ) {
        this.emailAddr = null;
    }
  } 

  public String getPhoneEveningAreaCode() {
    return this.phoneEveningAreaCode;
  }

  public String getPhoneEveningExchange() {
    return this.phoneEveningExchange;
  }

  public String getPhoneEveningLocal() {
    return this.phoneEveningLocal;
  }

  public String getPhoneEveningExt() {
    return this.phoneEveningExt;
  }

  public String getPhoneDayAreaCode() {
    return this.phoneDayAreaCode;
  }    

  public String getPhoneDayExchange() {
    return this.phoneDayExchange;
  }    

  public String getPhoneDayLocal() {
    return this.phoneDayLocal;
  }    

  public String getPhoneDayExt() {
    return this.phoneDayExt;
  }    

  public String getPhoneOtherAreaCode() {
    return this.phoneOtherAreaCode;
  } 

  public String getPhoneOtherExchange() {
    return this.phoneOtherExchange;
  } 

  public String getPhoneOtherLocal() {
    return this.phoneOtherLocal;
  } 

  public String getPhoneOtherExt() {
    return this.phoneOtherExt;
  } 

  public void setPhoneEveningAreaCode( String s ) {
    this.phoneEveningAreaCode = s == null ? null : s.trim() ;
  }

  public void setPhoneEveningExchange( String s ) {
    this.phoneEveningExchange = s == null ? null : s.trim() ;
  }

  public void setPhoneEveningLocal( String s ) {
    this.phoneEveningLocal = s == null ? null : s.trim() ;
  }

  public void setPhoneEveningExt( String s ) {
    this.phoneEveningExt = s == null ? null : s.trim() ;
    if ( this.phoneEveningExt != null
      && this.phoneEveningExt.trim().length() == 0 ) {
        this.phoneEveningExt = null;
    }
  }

  public void setPhoneDayAreaCode( String s ) {
    this.phoneDayAreaCode = s == null ? null : s.trim() ;
  } 

  public void setPhoneDayExchange( String s ) {
    this.phoneDayExchange = s == null ? null : s.trim() ;
  } 

  public void setPhoneDayLocal( String s ) {
    this.phoneDayLocal = s == null ? null : s.trim() ;
  } 

  public void setPhoneDayExt( String s ) {
    this.phoneDayExt = s == null ? null : s.trim() ;
    if ( this.phoneDayExt != null
      && this.phoneDayExt.trim().length() == 0 ) {
        this.phoneDayExt = null;
    }
  } 

  public void setPhoneOtherAreaCode( String s ) {
    this.phoneOtherAreaCode = s == null ? null : s.trim() ;
  }

  public void setPhoneOtherExchange( String s ) {
    this.phoneOtherExchange = s == null ? null : s.trim() ;
  }

  public void setPhoneOtherLocal( String s ) {
    this.phoneOtherLocal = s == null ? null : s.trim() ;
  }

  public void setPhoneOtherExt( String s ) {
    this.phoneOtherExt = s == null ? null : s.trim() ;
    if ( this.phoneOtherExt != null
      && this.phoneOtherExt.trim().length() == 0 ) {
        this.phoneOtherExt = null;
    }
  }

  public String getStreet1() {
    return this.street1;
  }

  public String getStreet2() {
    return this.street2;
  }

  public String getCity() {
    return this.city;
  }

  public String getState() {
    return this.state;
  }

  public String getZip() {
    return this.zip;
  }

  public void setAddress( Address address ) {
    this.street1 = address.getStreet1();
    this.street2 = address.getStreet2();
    this.city = address.getCity();
    this.state = address.getState();
    this.zip = address.getZip();
  }

  public void setStreet1( String s ) {
    this.street1 = s == null ? null : s.trim() ;
  }

  public void setStreet2( String s ) {
    this.street2 = s == null ? null : s.trim() ;
  }

  public void setCity( String s ) {
    this.city = s == null ? null : s.trim() ;
  }

  public void setState( String s ) {
    this.state = s == null ? null : s.trim() ;
  }

  public void setZip( String s ) {
    this.zip = s == null ? null : s.trim() ;
  }

  // Validate
  public ActionErrors validate(ActionMapping mapping,
    HttpServletRequest request) {

    ActionErrors errors = new ActionErrors();

    new ValidateMemberName(this).validate(errors);
    new ValidateMemberMailingAddress(this).validate(errors);
    new ValidateMemberPhoneNumbers(this).validate(errors);
    new ValidateMemberEmail(this).validate(errors);
    new ValidateMemberAccountName(this).validate(errors);
    new ValidateMemberOptionalInfo(this).validate(errors);

    if ( !isRestricted() ) {

      new ValidateMemberPassword(this).validate(errors);
      new ValidateMemberAccountYear(this).validate(errors);
      new ValidateMemberRoleAndStatus(this).validate(errors);

    }

    return errors;
  } // validate

} // MemberInfoForm

/*
 * $Log: MemberInfoForm.java,v $
 * Revision 1.16  2003/03/02 15:32:06  rphall
 * Removed unused fields and operations
 *
 * Revision 1.15  2003/02/26 03:38:46  rphall
 * Added copyright and GPL license
 *
 * Revision 1.14  2003/02/24 13:29:54  rphall
 * Added handling for when accountName changes
 *
 * Revision 1.13  2003/02/24 12:09:47  rphall
 * Fixed bug in setEmail(String)
 *
 * Revision 1.12  2003/02/21 19:14:26  rphall
 * Added validation of user name during AdminEdit
 *
 * Revision 1.11  2003/02/21 15:09:16  rphall
 * More fixes to blank extension bug
 *
 * Revision 1.10  2003/02/20 16:29:29  rphall
 * Fixed bug with null birthdate
 *
 * Revision 1.9  2003/02/20 04:51:27  rphall
 * Added properties for Roles
 *
 * Revision 1.8  2003/02/19 03:20:34  rphall
 * Added isAdminCreate(), isAdminEdit() and isMemberEdit() tests
 *
 * Revision 1.7  2003/02/18 04:29:59  rphall
 * Major revision; working for MEMBEREDIT, ADMINEDIT, ADMINCREATE
 *
 */

