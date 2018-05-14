/*
 * Copyright (c) Carnegie Lake Rowing Association 2002. All rights reserved.
 * Distributed under the GPL license. See doc/COPYING.
 * $RCSfile: Test_IMemberXmlRpc.java,v $
 * $Date: 2003/03/05 03:02:17 $
 * $Revision: 1.3 $
 */

package test.clra.xml;

import com.clra.member.Email;
import com.clra.xml.IMemberXmlRpc;
import com.clra.xml.IMemberXmlRpcService;
import com.clra.xml.IMemberXmlRpcServiceLocator;
import com.clra.xml.MemberSoapBindingStub;
import com.clra.xml.beans.AddressBean;
import com.clra.xml.beans.MemberBean;
import com.clra.xml.beans.MemberNameBean;
import junit.framework.TestCase;
import org.apache.axis.AxisEngine;
import org.apache.axis.MessageContext;
import org.apache.log4j.Category;
import test.clra.xml.security.Test_DBSecurityProvider;

/**
 * Tests the IMemberXmlRpc SOAP interface.</p>
 * <p>
 * Note: this test is extremely slow. For this reason, it should be run
 * by itself from the command-line; it should not be included in the automated
 * JUnit tests that are run as a part of an ANT build.</p>
 * <p>
 * Here are some durations of the test when the number of members was
 * restricted on the server-side:
 * <pre>
 *  10 members      0 min 44 seconds (older code)
 *  20 members      1 min 20 seconds (  "    "  )
 *  40 members      1 min 47 seconds (  "    "  )
 *  80 members      3 min 28 seconds (  "    "  )
 * 160 members      7 min 11 seconds (older code)
 * 405 members     12 min 32 seconds (more recent code)
 * </pre>
 * These durations were measured with multi-refs turned off, a time savings
 * of about 30 sec for 20 members. There was less parsing required for the
 * the more recent code compared to the older code; for example, telephone
 * numbers were stored as Strings instead of TelephoneBeans, and there was
 * no password field, in the recent code.</p>
 * <p>
 * Most of this delay is on the client-side. For example, if one sends sends
 * a raw XML message to the server, and receives a raw XML message in return,
 * the total processing time drops dramatically. The server-side processing is
 * still active in this scenario, but the client-side processing is not:
 * <pre>
 * 405 members      0 min 12 seconds (older code)
 * </pre></p>
 * <p>
 * Xalan can be used for simple XSL processing of the raw XML response. This
 * is the approached used by the com.clra.web.MembershipServlet class. Xalan
 * processing by itself adds about 10-20 seconds to the overall time, so the
 * total round trip should be about 30 seconds, which is comparable to the
 * time required for JSP processing of a 405-member list. Actual times are
 * closer to 45 seconds; see Test_MembershipServlet. It is not clear what
 * accounts for the extra 15 seconds in the actual versus estimated times.</p>
 *
 * @version $Default:$ $Date: 2003/03/05 03:02:17 $
 * @author <a href="mailto:rphall@pluto.njcc.com>Rick Hall</a>
 */
public class Test_IMemberXmlRpc extends TestCase {

  // The logger used by this class
  private final Category theLog =
    Category.getInstance(Test_IMemberXmlRpc.class);

  private IMemberXmlRpcServiceLocator locator = null;

  public static void main( String[] args ) {

    new test.Log4jConfigurator("");

    Test_IMemberXmlRpc test = new Test_IMemberXmlRpc( "command-line test" );
    test.setUp();
    test.testWithInvalidCredentials();
    test.testWithValidCredentials();
    test.tearDown();

    return;
  } // main

  public Test_IMemberXmlRpc( String name ) {
    super( name );
  }

  public void setUp() {
    this.locator = new IMemberXmlRpcServiceLocator();
  }

  public void tearDown() {
    this.locator = null;
  }

  public void testWithInvalidCredentials() {

    String username = null;
    String password = null;
    _expectedFailure( username, password );

    username = "  ";
    _expectedFailure( username, password );

    username = Test_DBSecurityProvider.getInvalidUsername();
    _expectedFailure( username, password );

    username = null;
    password = "  ";
    _expectedFailure( username, password );

    username = "  ";
    _expectedFailure( username, password );

    username = Test_DBSecurityProvider.getInvalidUsername();
    _expectedFailure( username, password );

    username = null;
    password = Test_DBSecurityProvider.getInvalidPassword();
    _expectedFailure( username, password );

    username = "  ";
    _expectedFailure( username, password );

    username = Test_DBSecurityProvider.getInvalidUsername();
    _expectedFailure( username, password );

    return;
  } // testWithInvalidCredentials()

  public void testWithValidCredentials() {

    String username = Test_DBSecurityProvider.getValidUsername();
    String password = Test_DBSecurityProvider.getValidPassword();

    MemberBean[] memberBeans = null;
    try {
      memberBeans = _findAllMembers( username, password );
    }
    catch( Exception x ) {
      String s = "unexpected: " +x.getClass().getName()+ ": " +x.getMessage();
      theLog.error( s );
      fail( s );
    }
    assertTrue( memberBeans != null );
    assertTrue( memberBeans.length > 0 );

    for ( int i=0; i<memberBeans.length; i++ ) {

      MemberBean mb = memberBeans[i];
      assertTrue( mb.getId() != null && mb.getId().intValue() > 0 );
      assertTrue( mb.getAccountName() != null
          && mb.getAccountName().trim().length() > 0 );
      assertTrue( mb.getAccountType() != null
          && mb.getAccountType().trim().length() > 0 );

      MemberNameBean mnb = mb.getMemberNameBean();
      assertTrue( mnb != null );
      assertTrue( mnb.getFirstName() != null
        && mnb.getFirstName().trim().length() > 0 );
      assertTrue( mnb.getLastName() != null
        && mnb.getLastName().trim().length() > 0 );

      AddressBean ab = mb.getAddressBean();
      assertTrue( ab != null );
      assertTrue( ab.getStreet1() != null
        && ab.getStreet1().trim().length() > 0 );
      assertTrue( ab.getCity() != null
        && ab.getCity().trim().length() > 0 );
      assertTrue( ab.getState() != null
        && ab.getState().trim().length() > 0 );
      assertTrue( ab.getZip() != null
        && ab.getZip().trim().length() > 0 );

      String phone = mb.getEveningTelephone();
      assertTrue( phone != null && phone.trim().length() > 0 );

      String strEmail = mb.getEmail();
      assertTrue( strEmail == null || Email.isValidEmail(strEmail) );

    } // iterate memberBeans

    return;
  } // testWithValidCredentials()

  private void _expectedFailure( String username, String password ) {

    MemberBean[] memberBeans = null;
    try {
      memberBeans = _findAllMembers( username, password );
    }
    catch( Exception x ) {
      // Expected
      String msg = "" + username + "/" + password + ": ";
      msg += x.getClass().getName() + ": " + x.getMessage();
      theLog.debug( msg );
    }
    assertTrue( memberBeans == null );

    return;
  } // _expectedFailure(String,String)

  private MemberBean[] _findAllMembers(String username, String password)
    throws javax.xml.rpc.ServiceException, java.rmi.RemoteException {

    theLog.debug( "username == '" + username + "'" );
    theLog.debug( "password == '" + password + "'" );

    IMemberXmlRpc memberXmlRpc = locator.getMember();

    assertTrue( memberXmlRpc instanceof MemberSoapBindingStub );
    MemberSoapBindingStub stub = (MemberSoapBindingStub) memberXmlRpc;
    stub.setUsername( username );
    stub.setPassword( password );

    MemberBean[] retVal = memberXmlRpc.findAllMembers();

    return retVal;
  } // _findAllMembers(String,String)

} // Test_IMemberXmlRpc

/*
 * $Log: Test_IMemberXmlRpc.java,v $
 * Revision 1.3  2003/03/05 03:02:17  rphall
 * Fixed minor bugs
 *
 * Revision 1.2  2003/03/05 01:21:02  rphall
 * Added security to SOAP service
 *
 * Revision 1.1  2003/03/04 14:00:27  rphall
 * Test for SOAP/XML functionality
 *
 */

