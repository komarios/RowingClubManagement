/*
 * Copyright (c) Carnegie Lake Rowing Association 2002. All rights reserved.
 * Distributed under the GPL license. See doc/COPYING.
 * $RCSfile: Test_MemberEJBHome.java,v $
 * $Date: 2003/03/08 21:07:51 $
 * $Revision: 1.8 $
 */

package test.clra.member.remote;

import com.clra.member.AccountType;
import com.clra.member.Address;
import com.clra.member.Email;
import com.clra.member.IMember;
import com.clra.member.IMemberHome;
import com.clra.member.MemberDBRead;
import com.clra.member.MemberName;
import com.clra.member.MemberRole;
import com.clra.member.MemberSnapshot;
import com.clra.member.Telephone;
import com.clra.member.Configuration;
import com.clra.util.ErrorUtils;
import java.util.Collection;
import java.util.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Set;
import java.util.Map;
import javax.naming.InitialContext;
import javax.rmi.PortableRemoteObject;
import junit.framework.TestCase;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Category;
import test.clra.util.Database;
import test.clra.util.Expected;

/**
 * This test depends on certain rows in the Member table of the
 * database. The script $CLRA_HOME/bin/setup.sh (and the SQL scripts
 * it calls in the $CLRA_HOME/etc/sql directory) is the best way to
 * prepare the database for testing.<p>
 *
 * This test -- if it runs to completion -- is designed to leave the
 * database (kind of) unchanged.
 *
 * @author <a href="mailto:donaldzhu@sympatico.ca">Angela Yue</a>
 * @version $Default:$ $Date: 2003/03/08 21:07:51 $
 */

public class Test_MemberEJBHome extends TestCase {

  // The logger used by this class
  private final static Category theLog =
    Category.getInstance(Test_MemberEJBHome.class);

  private MemberSnapshot ms = null;

  private Integer m_id = null;
  private String m_accountName = null;
  private String m_accountPassword = null;
  private AccountType m_accountType = null;
  private MemberName m_memberName = null;
  private Email m_email = null;
  private Telephone m_eveningPhone = null;
  private Telephone m_dayPhone = null;
  private Telephone m_otherPhone = null;
  private Address m_address = null;
  private Date m_accountYear = null;
  private Date m_birthDate = null;
  private MemberRole[] m_memberRoles = null;

  public void setUp() {
    Database.assertValid();
    createSnapshots();
  }

  private void createSnapshots() {

      int count = 0;

      while ( count < 5 && ms == null) {

        try {

            ++count;

            int id = (int) Math.round(
                Math.random() * Expected.MEMBER_COUNT().intValue() );
            id += Expected.MEMBER_MIN().intValue();

            ms = MemberDBRead.loadMember(new Integer(id));
            if ( ms == null ) {
                continue;
            }

            m_id = ms.getId();
            m_accountName = ms.getAccountName();
            m_accountPassword = ms.getAccountPassword();
            m_accountType = ms.getAccountType();
            m_memberName = ms.getMemberName();
            m_email = ms.getEmail();

            Map phones = ms.getTelephoneNumbers();
            m_eveningPhone = (Telephone) phones.get( Telephone.EVENING );
            m_dayPhone = (Telephone) phones.get( Telephone.DAY );
            m_otherPhone = (Telephone) phones.get( Telephone.OTHER );

            m_address = ms.getAddress();
            m_accountYear = ms.getAccountDate();
            m_birthDate = ms.getBirthDate();
            m_memberRoles = ms.getMemberRoles();

        } catch (Exception ex) {
            theLog.error( ex.getClass().getName() + ": " + ex.getMessage() );
        }

      } // while

  } // createSnapshots()

  public void tearDown() {
    Database.assertValid();
  }

  public Test_MemberEJBHome( String name ) {
    super( name );
  }

  public void testFindByPrimaryKey() {
    
    IMember m = null;
    IMemberHome home = null;
    try {

      home = lookupHome();
      m = home.findByPrimaryKey( m_id );

      assertEquals(
        "id", m.getId(), m_id );
      assertEquals(
        "accountName", m.getAccountName(), m_accountName );
      assertEquals(
        "accountPassword", m.getAccountPassword(), m_accountPassword );
      assertEquals(
        "accountType", m.getAccountType(), m_accountType );
      assertEquals(
        "memberName", m.getMemberName(), m_memberName );
      assertEquals(
        "email", m.getEmail(), m_email );
      assertEquals(
        "eveningPhone", m.getEveningTelephone(), m_eveningPhone );
      assertEquals(
        "dayPhone", m.getDayTelephone(), m_dayPhone );
      assertEquals(
        "otherPhone", m.getOtherTelephone(), m_otherPhone );
      assertEquals(
        "address", m.getAddress(), m_address );
      assertEquals(
        "accountYear", m.getAccountYear().getYear(), m_accountYear.getYear() );
      assertEquals(
        "birthDate", m.getBirthDate(), m_birthDate );

      MemberRole[] arTest = m.getMemberRoles();
      assertTrue( "null getMemberRoles", arTest != null );
      assertTrue( "null m_memberRoles", m_memberRoles != null );
      assertTrue( "memberRoles.length",
        m.getMemberRoles().length == m_memberRoles.length );
      for ( int i=0; i<m_memberRoles.length; i++ ) {
        boolean isPresent = false;
        assertTrue( "null m_memberRoles["+i+"]", m_memberRoles[i] != null );
        for ( int j=0; j<arTest.length; j++ ) {
          assertTrue( "null arTest["+j+"]", arTest[j] != null );
          if ( m_memberRoles[i].equals(arTest[j]) ) {
            isPresent = true;
            break;
          }
        } // for j
        assertTrue( "missing " + m_memberRoles[i], isPresent );
      } // for i

    }
    catch( Exception x ) {
      String msg = ErrorUtils.createDbgMsg( "testFindByPrimaryKey", x );
      theLog.error( msg );
      fail( msg );
    }

  } // testFindByPrimaryKey()

  public void testRemoveAndCreate1() {

    IMember m = null;
    IMemberHome home = null;
    try {

      final Integer MID = ms.getId();

      theLog.debug( "testRemoveAndCreate1: looking up Member home" );
      home = lookupHome();

      theLog.debug( "testRemoveAndCreate1: finding " + MID );
      m = home.findByPrimaryKey( MID );

      theLog.debug( "testRemoveAndCreate1: removing " + MID );
      m.remove();

      theLog.debug( "testRemoveAndCreate1: verifying removal of " + MID );
      MemberSnapshot ms1 = null;
      try {
        ms1 = MemberDBRead.loadMember( MID );
      }
      catch( javax.ejb.NoSuchEntityException expected ) {}
      assertTrue( ms1 == null );

      theLog.debug( "testRemoveAndCreate1: creating clone of " + MID );
      m = home.create( m_accountName, m_accountPassword, m_accountType,
            m_memberName, m_email, m_eveningPhone, m_dayPhone, m_otherPhone,
            m_address, m_accountYear, m_birthDate, m_memberRoles );

      final Integer newMID = m.getId();

      theLog.debug(
        "testRemoveAndCreate1: verifying creation of clone of '"
        + MID + "' (id == '" + newMID + "')" );
      ms1 = MemberDBRead.loadMember( newMID );
      assertTrue( ms1 != null );

      // New snapshot is a clone
      assertEquals(
        "id", ms1.getId(), newMID );
      assertEquals(
        "accountName", ms1.getAccountName(), m_accountName );
      assertEquals(
        "accountPassword", ms1.getAccountPassword(), m_accountPassword );
      assertEquals(
        "accountType", ms1.getAccountType(), m_accountType.toString() );
      assertEquals(
        "memberName", ms1.getMemberName(), m_memberName );
      assertEquals(
        "email", ms1.getEmail(), m_email.toString() );

      Map phones = ms1.getTelephoneNumbers();
      Telephone phone = (Telephone) phones.get( Telephone.EVENING );
      assertEquals( "eveningPhone", phone, m_eveningPhone );

      phone = (Telephone) phones.get( Telephone.DAY );
      assertEquals( "dayPhone", phone, m_dayPhone );

      phone = (Telephone) phones.get( Telephone.OTHER );
      assertEquals( "otherPhone", phone, m_otherPhone );

      assertEquals(
        "address", ms1.getAddress(), m_address );

      assertEquals( "accountYear",
        ms1.getAccountDate().getYear(), m_accountYear.getYear() );

      assertEquals(
        "birthDate", ms1.getBirthDate(), m_birthDate );

      MemberRole[] arTest = (MemberRole[]) ms1.getMemberRoles();
      assertTrue( "null getMemberRoles", arTest != null );
      assertTrue( "null m_memberRoles", m_memberRoles != null );
      assertTrue( "memberRoles.length",
        ms1.getMemberRoles().length == m_memberRoles.length );
      for ( int i=0; i<m_memberRoles.length; i++ ) {
        boolean isPresent = false;
        assertTrue( "null m_memberRoles["+i+"]", m_memberRoles[i] != null );
        for ( int j=0; j<arTest.length; j++ ) {
          assertTrue( "null arTest["+j+"]", arTest[j] != null );
          if ( m_memberRoles[i].equals(arTest[j]) ) {
            isPresent = true;
            break;
          }
        } // for j
        assertTrue( "missing " + m_memberRoles[i], isPresent );
      } // for i

      // Update global snapshot values
      ms = ms1;

    }
    catch( Exception x ) {
      String msg = ErrorUtils.createDbgMsg( "testRemove", x );
      theLog.error( msg, x );
      fail( msg );
    }

  }

  public static IMemberHome lookupHome() {

    IMemberHome home = null;
    try {
      InitialContext jndiContext = new InitialContext();
      Object ref  = jndiContext.lookup( Configuration.MEMBER_HOME() );
      home = (IMemberHome)
          PortableRemoteObject.narrow (ref, IMemberHome.class);
    }
    catch( Exception x ) {
      theLog.fatal( x.getMessage(), x );
      String msg = "unable to find MemberHome at '"
          + Configuration.MEMBER_HOME() + "'";
      fail( msg );
    }

    return home;
  } // lookupHome()

  public static void remove( IMember p ) {

    if ( p != null ) {
      try {
        p.remove();
      }
      catch( Exception x ) {
        theLog.fatal( x.getMessage(), x );
      }
    }

  } // remove(IMember)

} // Test_MemberEJBHome

/*
 * $Log: Test_MemberEJBHome.java,v $
 * Revision 1.8  2003/03/08 21:07:51  rphall
 * Changed name of Database.assert() method to assertValid()
 *
 * Revision 1.7  2003/02/26 03:38:47  rphall
 * Added copyright and GPL license
 *
 * Revision 1.6  2003/02/20 05:01:07  rphall
 * Changes rippled through from MemberSnapshot, MemberInfoForm
 *
 */

