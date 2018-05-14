/*
 * Copyright (c) Carnegie Lake Rowing Association 2002. All rights reserved.
 * Distributed under the GPL license. See doc/COPYING.
 * $RCSfile: DataUtils.java,v $
 * $Date: 2003/03/02 15:30:56 $
 * $Revision: 1.7 $
 */

package com.clra.web;

import com.clra.util.ValidationException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;

/**
 * Collections of data constants. These constants could be stored
 * in the application database, but currently they are maintained
 * separately.
 *
 * @version $Revision: 1.7 $ $Date: 2003/03/02 15:30:56 $
 * @author <a href="mailto:rphall@pluto.njcc.com">Rick Hall</a>
 */
public class DataUtils implements Serializable {

  private DataUtils() {}

  private static int FIRST_YEAR = 1985;

  private static String[][] stateLabelValues = {
    { Text.getMessage( "data.states.AL" ), "AL" },
    { Text.getMessage( "data.states.AK" ), "AK" },
    { Text.getMessage( "data.states.AZ" ), "AZ" },
    { Text.getMessage( "data.states.AR" ), "AR" },
    { Text.getMessage( "data.states.CA" ), "CA" },
    { Text.getMessage( "data.states.CO" ), "CO" },
    { Text.getMessage( "data.states.CT" ), "CT" },
    { Text.getMessage( "data.states.DE" ), "DE" },
    { Text.getMessage( "data.states.DC" ), "DC" },
    { Text.getMessage( "data.states.FL" ), "FL" },
    { Text.getMessage( "data.states.GA" ), "GA" },
    { Text.getMessage( "data.states.HI" ), "HI" },
    { Text.getMessage( "data.states.ID" ), "ID" },
    { Text.getMessage( "data.states.IL" ), "IL" },
    { Text.getMessage( "data.states.IN" ), "IN" },
    { Text.getMessage( "data.states.IA" ), "IA" },
    { Text.getMessage( "data.states.KS" ), "KS" },
    { Text.getMessage( "data.states.KY" ), "KY" },
    { Text.getMessage( "data.states.LA" ), "LA" },
    { Text.getMessage( "data.states.ME" ), "ME" },
    { Text.getMessage( "data.states.MD" ), "MD" },
    { Text.getMessage( "data.states.MA" ), "MA" },
    { Text.getMessage( "data.states.MI" ), "MI" },
    { Text.getMessage( "data.states.MN" ), "MN" },
    { Text.getMessage( "data.states.MS" ), "MS" },
    { Text.getMessage( "data.states.MO" ), "MO" },
    { Text.getMessage( "data.states.MT" ), "MT" },
    { Text.getMessage( "data.states.NE" ), "NE" },
    { Text.getMessage( "data.states.NV" ), "NV" },
    { Text.getMessage( "data.states.NH" ), "NH" },
    { Text.getMessage( "data.states.NJ" ), "NJ" },
    { Text.getMessage( "data.states.NM" ), "NM" },
    { Text.getMessage( "data.states.NY" ), "NY" },
    { Text.getMessage( "data.states.NC" ), "NC" },
    { Text.getMessage( "data.states.ND" ), "ND" },
    { Text.getMessage( "data.states.OH" ), "OH" },
    { Text.getMessage( "data.states.OK" ), "OK" },
    { Text.getMessage( "data.states.OR" ), "OR" },
    { Text.getMessage( "data.states.PA" ), "PA" },
    { Text.getMessage( "data.states.RI" ), "RI" },
    { Text.getMessage( "data.states.SC" ), "SC" },
    { Text.getMessage( "data.states.SD" ), "SD" },
    { Text.getMessage( "data.states.TN" ), "TN" },
    { Text.getMessage( "data.states.TX" ), "TX" },
    { Text.getMessage( "data.states.UT" ), "UT" },
    { Text.getMessage( "data.states.VT" ), "VT" },
    { Text.getMessage( "data.states.VA" ), "VA" },
    { Text.getMessage( "data.states.WA" ), "WA" },
    { Text.getMessage( "data.states.WV" ), "WV" },
    { Text.getMessage( "data.states.WI" ), "WI" },
    { Text.getMessage( "data.states.WY" ), "WY" }
  };

  private static String[][] accountTypeLabelValues = {
    { Text.getMessage( "data.accountType.FULL" ), "FULL" },
    { Text.getMessage( "data.accountType.NOVICE" ), "NOVICE" },
    { Text.getMessage( "data.accountType.LTR" ), "LTR" },
    { Text.getMessage( "data.accountType.ALUMNI" ), "ALUMNI" },
    { Text.getMessage( "data.accountType.LTR-ALUM" ), "LTR-ALUM" },
    { Text.getMessage( "data.accountType.CONTRACT" ), "CONTRACT" },
    { Text.getMessage( "data.accountType.DUPLICAT" ), "DUPLICAT" }
  };

  private static String[][] roleLabelValues = {
    { Text.getMessage( "data.role.MEMBER" ), "MEMBER" },
    { Text.getMessage( "data.role.MEMBERMGR" ), "MEMBERMGR" },
    { Text.getMessage( "data.role.COACH" ), "COACH" },
    { Text.getMessage( "data.role.CAPTAIN" ), "CAPTAIN" },
    { Text.getMessage( "data.role.SESSIONMGR" ), "SESSIONMGR" },
    { Text.getMessage( "data.role.TREASURER" ), "TREASURER" },
    { Text.getMessage( "data.role.INACTIVE" ), "INACTIVE" }
  };

  public static ArrayList accountYearList() {
    final int thisYear = new GregorianCalendar().get(Calendar.YEAR);
    ArrayList retVal = new ArrayList();
    for ( int i = FIRST_YEAR; i <= thisYear; i++ ) {
      String label = "" + i;
      LabelValueBean lvb = new LabelValueBean( label, label );
      retVal.add( lvb );
    }
    return retVal;
  }

  public static ArrayList stateList() {
    ArrayList retVal = new ArrayList();
    for ( int i=0; i<stateLabelValues.length; i++ ) {
      LabelValueBean lvb =
        new LabelValueBean( stateLabelValues[i][0], stateLabelValues[i][1] );
      retVal.add( lvb );
    }
    return retVal;
  }

  public static ArrayList accountTypeList() {
    ArrayList retVal = new ArrayList();
    for ( int i=0; i<accountTypeLabelValues.length; i++ ) {
      LabelValueBean lvb = new LabelValueBean(
        accountTypeLabelValues[i][0], accountTypeLabelValues[i][1] );
      retVal.add( lvb );
    }
    return retVal;
  }

  public static ArrayList roleList() {
    ArrayList retVal = new ArrayList();
    for ( int i=0; i<roleLabelValues.length; i++ ) {
      LabelValueBean lvb =
        new LabelValueBean( roleLabelValues[i][0], roleLabelValues[i][1] );
      retVal.add( lvb );
    }
    return retVal;
  }

  public static class AccountYears implements Iterator {
    private Iterator iterator = accountYearList().iterator();
    public boolean hasNext() { return iterator.hasNext(); }
    public Object next() { return iterator.next(); }
    public void remove() { throw new UnsupportedOperationException(); }
  }

  public static class States implements Iterator {
    private Iterator iterator = stateList().iterator();
    public boolean hasNext() { return iterator.hasNext(); }
    public Object next() { return iterator.next(); }
    public void remove() { throw new UnsupportedOperationException(); }
  }

  public static class AccountTypes implements Iterator {
    private Iterator iterator = accountTypeList().iterator();
    public boolean hasNext() { return iterator.hasNext(); }
    public Object next() { return iterator.next(); }
    public void remove() { throw new UnsupportedOperationException(); }
  }

  public static class Roles implements Iterator {
    private Iterator iterator = roleList().iterator();
    public boolean hasNext() { return iterator.hasNext(); }
    public Object next() { return iterator.next(); }
    public void remove() { throw new UnsupportedOperationException(); }
  }

  public static void requiredStringValue( String name, String value )
    throws ValidationException {

    // Precondition: non-null, non-blank name for string
    if ( name == null || name.trim().length() == 0 ) {
      throw new Error( "null or blank string name" );
    }

    // Test the string value
    if ( value == null || value.trim().length() == 0 ) {
      String msg = "null or blank " + name;
      throw new ValidationException( msg );
    }

    return;
  } // requiredString(String,String)

  public static String validateString( String str, String[] allowed )
    throws ValidationException {

    if ( str == null ) {
      throw new IllegalArgumentException( "null string" );
    }
    str = str.trim();

    String retVal = null;
    for ( int i=0; i<allowed.length; i++ ) {
      if ( allowed[i].equalsIgnoreCase(str) ) {
        retVal = allowed[i];
        break;
      }
    }

    if ( retVal == null ) {
      throw new ValidationException( "invalid value == '" +str+ "'" );
    }

    return retVal;
  } // validateString(String,String[])

} // DataUtils

/*
 * $Log: DataUtils.java,v $
 * Revision 1.7  2003/03/02 15:30:56  rphall
 * Moved hard-coded text from DataUtils to clra.properties
 *
 * Revision 1.6  2003/02/26 03:38:46  rphall
 * Added copyright and GPL license
 *
 * Revision 1.5  2003/02/21 04:59:54  rphall
 * Shortened DB value for 'DUPLICATE'
 *
 * Revision 1.4  2003/02/20 04:47:01  rphall
 * Removed gratuitous use of CLRA acronym
 *
 * Revision 1.3  2003/02/19 03:19:13  rphall
 * Added 'contractor' notation to Coach role
 *
 * Revision 1.2  2003/02/18 04:26:30  rphall
 * Added general validation methods
 *
 * Revision 1.1  2003/02/10 05:35:31  rphall
 * Data constants
 *
 */

