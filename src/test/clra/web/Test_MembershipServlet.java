/*
 * Copyright (c) Carnegie Lake Rowing Association 2002. All rights reserved.
 * Distributed under the GPL license. See doc/COPYING.
 * $RCSfile: Test_MembershipServlet.java,v $
 * $Date: 2003/03/05 14:04:57 $
 * $Revision: 1.2 $
 */

package test.clra.web;

import com.clra.web.MembershipServlet;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Date;
import junit.framework.TestCase;
import org.apache.log4j.Category;
import test.clra.xml.security.Test_DBSecurityProvider;

/**
 * Tests MembershipServlet (and Axis performance) by writing SOAP
 * in a variety of formats to temporary files.
 * @version $Default:$ $Date: 2003/03/05 14:04:57 $
 */
public class Test_MembershipServlet extends TestCase {

  private final static String base = MembershipServlet.class.getName();
  private final static Category theLog = Category.getInstance( base );

  public static String FILE_NAME_PREFIX = "testMembershipServlet";
  public static String FILE_NAME_SUFFIX = ".tmp";

  private String invalidUsername = null;
  private String invalidPassword = null;
  private String validUsername   = null;
  private String validPassword   = null;

  public Test_MembershipServlet( String str ) {
    super(str);
  }

  public static void main( String[] args ) {

    new test.Log4jConfigurator("");

    Test_MembershipServlet test =
      new Test_MembershipServlet("command-line test");
    test.setUp();

    test.testInvalidCredentials();

    Date start = new Date();
    test.testWriteFindAllSoapXmlResponse();
    Date finish = new Date();
    long durationMillis = finish.getTime() - start.getTime();
    long durationSecs = durationMillis / 1000;
    theLog.info("writeFindAllSoapXmlResponse: " +durationSecs+ " secs");

    start = new Date();
    test.testWriteFindAllCsvResponse();
    finish = new Date();
    durationMillis = finish.getTime() - start.getTime();
    durationSecs = durationMillis / 1000;
    theLog.info("writeFindAllCsvResponse: " +durationSecs+ " secs");

    start = new Date();
    test.testWriteFindAllSimplifiedXmlResponse();
    finish = new Date();
    durationMillis = finish.getTime() - start.getTime();
    durationSecs = durationMillis / 1000;
    theLog.info("writeFindAllSimplifiedXmlResponse: " +durationSecs+ " secs");

    test.tearDown();

    return;
  } // main

  public void setUp() {
    this.invalidUsername = Test_DBSecurityProvider.getInvalidUsername();
    this.invalidPassword = Test_DBSecurityProvider.getInvalidPassword();
    this.validUsername   = Test_DBSecurityProvider.getValidUsername();
    this.validPassword   = Test_DBSecurityProvider.getValidPassword();
  }

  public void tearDown() {
    this.invalidUsername = null;
    this.invalidPassword = null;
    this.validUsername   = null;
    this.validPassword   = null;
  }

  public void testInvalidCredentials() {
    String name = this.invalidUsername;
    String pass = this.invalidPassword;
    try {
      _writeFindAllSoapXmlResponse( name, pass );
      // fail( "failed to detect invalid credentials" );
    }
    catch( Exception x ) {
      // Expected
    }
  }

  public void testWriteFindAllSoapXmlResponse() {
    String name = this.validUsername;
    String pass = this.validPassword;
    try {
      _writeFindAllSoapXmlResponse( name, pass );
    }
    catch( Exception x ) {
      theLog.error( x );
      fail( x.getClass().getName() + ": " + x.getMessage() );
    }
  }

  private void _writeFindAllSoapXmlResponse( String name, String pass )
  throws Exception {

    File file = null;
    FileOutputStream fos = null;
    PrintWriter pw = null;

    try {
      file = File.createTempFile( FILE_NAME_PREFIX, FILE_NAME_SUFFIX );
      String fileName = file.getAbsolutePath();
      fos = new FileOutputStream(file);
      pw = new PrintWriter(fos);
      MembershipServlet.writeFindAllSoapXmlResponse(name,pass,pw);
      theLog.info( "wrote results of writeFindAllSoapXmlResponse to '"
        + fileName + "'" );
    }
    finally {
      if ( pw != null ) {
        try { pw.close(); } catch(Exception x) {}
      }
      if ( fos != null ) {
        try { fos.close(); } catch(Exception x) {}
      }
    }

    return;
  } // testWriteFindAllSoapXmlResponse();

  public void testWriteFindAllCsvResponse() {

    File file = null;
    FileOutputStream fos = null;
    PrintWriter pw = null;
    String name = this.validUsername;
    String pass = this.validPassword;

    try {
      file = File.createTempFile( FILE_NAME_PREFIX, FILE_NAME_SUFFIX );
      String fileName = file.getAbsolutePath();
      fos = new FileOutputStream(file);
      pw = new PrintWriter(fos);
      MembershipServlet.writeFindAllCsvResponse(name,pass,pw);
      theLog.info( "wrote results of writeFindAllSoapXmlResponse to '"
        + fileName + "'" );
    }
    catch( Exception x ) {
      theLog.error( x );
      fail( x.getClass().getName() + ": " + x.getMessage() );
    }
    finally {
      if ( pw != null ) {
        try { pw.close(); } catch(Exception x) {}
      }
      if ( fos != null ) {
        try { fos.close(); } catch(Exception x) {}
      }
    }

    return;
  } // testWriteFindAllSoapXmlResponse();

  public void testWriteFindAllSimplifiedXmlResponse() {

    File file = null;
    FileOutputStream fos = null;
    PrintWriter pw = null;
    String name = this.validUsername;
    String pass = this.validPassword;

    try {
      file = File.createTempFile( FILE_NAME_PREFIX, FILE_NAME_SUFFIX );
      String fileName = file.getAbsolutePath();
      fos = new FileOutputStream(file);
      pw = new PrintWriter(fos);
      MembershipServlet.writeFindAllSimplifiedXmlResponse(name,pass,pw);
      theLog.info( "wrote results of writeFindAllSimplifiedXmlResponse to '"
        + fileName + "'" );
    }
    catch( Exception x ) {
      theLog.error( x );
      fail( x.getClass().getName() + ": " + x.getMessage() );
    }
    finally {
      if ( pw != null ) {
        try { pw.close(); } catch(Exception x) {}
      }
      if ( fos != null ) {
        try { fos.close(); } catch(Exception x) {}
      }
    }

    return;
  } // testWriteFindAllSoapXmlResponse();


} // Test_MembershipServlet

/*
 * $Log: Test_MembershipServlet.java,v $
 * Revision 1.2  2003/03/05 14:04:57  rphall
 * Added username/password to SOAP testing
 *
 * Revision 1.1  2003/03/04 02:18:44  rphall
 * Test com/clra/web functionality
 *
 */

