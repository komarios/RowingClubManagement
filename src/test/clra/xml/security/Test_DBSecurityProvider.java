/*
 * Copyright (c) Carnegie Lake Rowing Association 2002. All rights reserved.
 * Distributed under the GPL license. See doc/COPYING.
 * $RCSfile: Test_DBSecurityProvider.java,v $
 * $Date: 2003/03/05 14:03:51 $
 * $Revision: 1.2 $
 */

package test.clra.xml.security;

import com.clra.member.MemberDBRead;
import com.clra.member.MemberRole;
import com.clra.member.MemberSnapshot;
import com.clra.xml.IMemberXmlRpcService;
import com.clra.xml.IMemberXmlRpcServiceLocator;
import com.clra.xml.security.DBSecurityProvider;
import com.clra.xml.security.DBAuthenticatedUser;
import junit.framework.TestCase;
import org.apache.axis.AxisEngine;
import org.apache.axis.MessageContext;
import org.apache.axis.security.AuthenticatedUser;
import org.apache.axis.security.simple.SimpleAuthenticatedUser;
import org.apache.log4j.Category;

/**
 * Tests the DBSecurityProvider.<
 *
 * @version $Default:$ $Date: 2003/03/05 14:03:51 $
 * @author <a href="mailto:rphall@pluto.njcc.com>Rick Hall</a>
 */
public class Test_DBSecurityProvider extends TestCase {

  // The logger used by this class
  private final Category theLog =
    Category.getInstance(Test_DBSecurityProvider.class);

  public static final String getValidUsername() {
    throw new RuntimeException( "not yet implemented" );
    // FIXME remove hard-coding
    // return new String( "vail95" );
  }

  public static final String getValidPassword() {
    throw new RuntimeException( "not yet implemented" );
    // FIXME remove hard-coding
    // return new String( "123456" );
  }

  public static final String getInvalidUsername() {
    throw new RuntimeException( "not yet implemented" );
    // FIXME remove hard-coding
    // return new String( "daffodil83" );
  }

  public static final String getInvalidPassword() {
    throw new RuntimeException( "not yet implemented" );
    // FIXME remove hard-coding
    // return new String( "123456" );
  }

  public static void main( String[] args ) {

    new test.Log4jConfigurator("");

    Test_DBSecurityProvider test =
      new Test_DBSecurityProvider( "command-line test" );

    test.testAuthenticate();
    test.testUserMatches();

    return;
  } // main

  public Test_DBSecurityProvider( String name ) {
    super( name );
  }

  public void testAuthenticate() {

    IMemberXmlRpcServiceLocator locator = new IMemberXmlRpcServiceLocator();
    AxisEngine engine = locator.getEngine();
    MessageContext mc = new MessageContext(engine);

    String username = null;
    String password = null;
    AuthenticatedUser au = null;
    DBSecurityProvider dbsp = new DBSecurityProvider();

    // Test with null username and null password
    assertTrue( mc.getUsername() == null );
    assertTrue( mc.getPassword() == null );
    au = dbsp.authenticate( mc );
    assertTrue( au == null );

    // Test with blank username and null password
    username = new String( "  " );
    mc.setUsername( username );
    au = dbsp.authenticate( mc );
    assertTrue( au == null );

    // Test with invalid username and null password
    username = getInvalidUsername();
    mc.setUsername( username );
    au = dbsp.authenticate( mc );
    assertTrue( au == null );

    // Test with valid username and null password
    username = getValidUsername();
    mc.setUsername( username );
    au = dbsp.authenticate( mc );
    assertTrue( au == null );

    // Test with null username and blank password
    username = null;
    password = "   ";
    mc.setUsername( username );
    mc.setPassword( password );
    au = dbsp.authenticate( mc );
    assertTrue( au == null );

    // Test with blank username and blank password
    username = new String( "  " );
    mc.setUsername( username );
    au = dbsp.authenticate( mc );
    assertTrue( au == null );

    // Test with invalid username and blank password
    username = getInvalidUsername();
    mc.setUsername( username );
    au = dbsp.authenticate( mc );
    assertTrue( au == null );

    // Test with valid username and blank password
    username = getValidUsername();
    mc.setUsername( username );
    au = dbsp.authenticate( mc );
    assertTrue( au == null );

    // Test with null username and invalid password
    username = null;
    password = getInvalidPassword();
    mc.setUsername( username );
    mc.setPassword( password );
    au = dbsp.authenticate( mc );
    assertTrue( au == null );

    // Test with blank username and invalid password
    username = new String( "  " );
    mc.setUsername( username );
    au = dbsp.authenticate( mc );
    assertTrue( au == null );

    // Test with invalid username and invalid password
    username = getInvalidUsername();
    mc.setUsername( username );
    au = dbsp.authenticate( mc );
    assertTrue( au == null );

    // Test with valid username and invalid password
    username = getValidUsername();
    mc.setUsername( username );
    au = dbsp.authenticate( mc );
    assertTrue( au == null );

    // Test with null username and valid password
    username = null;
    password = getValidPassword();
    mc.setUsername( username );
    mc.setPassword( password );
    au = dbsp.authenticate( mc );
    assertTrue( au == null );

    // Test with blank username and valid password
    username = new String( "  " );
    mc.setUsername( username );
    au = dbsp.authenticate( mc );
    assertTrue( au == null );

    // Test with invalid username and valid password
    username = getInvalidUsername();
    mc.setUsername( username );
    au = dbsp.authenticate( mc );
    assertTrue( au == null );

    // Test with valid username and valid password
    username = getValidUsername();
    mc.setUsername( username );
    au = dbsp.authenticate( mc );
    assertTrue( au != null );
    assertTrue( au instanceof DBAuthenticatedUser );
    DBAuthenticatedUser dbau = (DBAuthenticatedUser) au;
    assertTrue( dbau.getName() != null );
    assertTrue( dbau.getAccountId() != null );
    assertTrue( dbau.getAccountName() != null );
    assertTrue( dbau.getAccountRoles() != null );
    assertTrue( dbau.getAccountName().equals(dbau.getName()) );
    assertTrue( dbau.getAccountName().equalsIgnoreCase(username) );

    return;
  } // testAuthenticate()

  public void testUserMatches() {

    String username = getValidUsername();
    MemberSnapshot ms = null;
    try {
      ms = MemberDBRead.findMemberByAccountName(username);
    } catch( Throwable t ) {
      String msg = "unexpected: " + t.getClass().getName() + t.getMessage();
      theLog.error( msg, t );
      fail( msg );
    }
    MemberRole[] memberRoles = ms.getMemberRoles();

    final String[] validPrincipals = new String[ memberRoles.length ];
    for ( int i=0; i<validPrincipals.length; i++ ) {
      validPrincipals[i] = memberRoles[i].getRole();
    }

    final String[] invalidPrincipals = new String[3];
    invalidPrincipals[0] = null;
    invalidPrincipals[1] = "  ";
    invalidPrincipals[2] = new String( "asd123 !@#-_. " );

    DBSecurityProvider dbsp = new DBSecurityProvider();

    AuthenticatedUser au = null;
    for ( int i=0; i<validPrincipals.length; i++ ) {
      boolean test = dbsp.userMatches( au, validPrincipals[i] );
      assertTrue( !test );
    }
    for ( int i=0; i<invalidPrincipals.length; i++ ) {
      boolean test = dbsp.userMatches( au, invalidPrincipals[i] );
      assertTrue( !test );
    }

    au = new SimpleAuthenticatedUser( getInvalidUsername() );
    for ( int i=0; i<validPrincipals.length; i++ ) {
      boolean test = dbsp.userMatches( au, validPrincipals[i] );
      assertTrue( !test );
    }
    for ( int i=0; i<invalidPrincipals.length; i++ ) {
      boolean test = dbsp.userMatches( au, invalidPrincipals[i] );
      assertTrue( !test );
    }

    au = new SimpleAuthenticatedUser( ms.getAccountName() );
    for ( int i=0; i<validPrincipals.length; i++ ) {
      boolean test = dbsp.userMatches( au, validPrincipals[i] );
      assertTrue( test );
    }
    for ( int i=0; i<invalidPrincipals.length; i++ ) {
      boolean test = dbsp.userMatches( au, invalidPrincipals[i] );
      assertTrue( !test );
    }

    au = new DBAuthenticatedUser(
      ms.getId(), ms.getAccountName(), ms.getMemberRoles() );
    for ( int i=0; i<validPrincipals.length; i++ ) {
      boolean test = dbsp.userMatches( au, validPrincipals[i] );
      assertTrue( test );
    }
    for ( int i=0; i<invalidPrincipals.length; i++ ) {
      boolean test = dbsp.userMatches( au, invalidPrincipals[i] );
      assertTrue( !test );
    }

    return;
  } // testUserMatches()

} // Test_DBSecurityProvider

/*
 * $Log: Test_DBSecurityProvider.java,v $
 * Revision 1.2  2003/03/05 14:03:51  rphall
 * Commented out hard-code usernames/passwords
 *
 * Revision 1.1  2003/03/05 01:21:03  rphall
 * Added security to SOAP service
 *
 */

