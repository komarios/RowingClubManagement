/*
 * Copyright (c) Carnegie Lake Rowing Association 2002. All rights reserved.
 * Distributed under the GPL license. See doc/COPYING.
 * $RCSfile: MembershipServlet.java,v $
 * $Date: 2003/03/05 13:55:13 $
 * $Revision: 1.2 $
 */

package com.clra.web;

import com.clra.web.MemberView;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringBufferInputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.net.URL;
import javax.ejb.NoSuchEntityException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.axis.encoding.Base64;
import org.apache.log4j.Category;
import org.apache.log4j.helpers.Loader;

/**
 * Converts a soap response to CSV or beautified XML message.
 * The only valid request parameters are:<ul>
 * <li><code>format</code>.
 * Specifies what action to perform.<br>
 * The only valid values for this parameter are:<ul>
 * <li><code>CSV</code><br>
 * Converts a SOAP message to comma-separarted-value format.</li>
 * <li><code>XML</code><br>
 * Converts a SOAP message to a simplified XML format.</li></ul></p>
 * <p>
 * If the <code>format</code> parameter is omitted, the output format
 * defaults to beautified XML.</p>
 *
 * @version $Revision: 1.2 $
 */
public class MembershipServlet extends HttpServlet {

  private final static String base = MembershipServlet.class.getName();
  private final static Category theLog = Category.getInstance( base );

  /** Socket timeout (milliseconds) */
  private final static int timeoutMillis = 60000;

  /** Default buffer size */
  private final static int BUFSIZE = 100*1024;

  /** The name of the "format" request parameter */
  public final static String RPN_FORMAT = "format";

  /** The "CSV" value for the "format" request parameter */
  public final static String RPV_FORMAT_CSV = "CSV";
   
  /** The "XML" value for the "format" request parameter */
  public final static String RPV_FORMAT_XML = "XML";

  /** The file that holds the pre-built SOAP "findAll" request headers */
  public final static String FILE_FINDALL_HEADERS =
      "com/clra/web/findAll-request-headers.http";

  /** The file that holds the pre-built SOAP "findAll" request content */
  public final static String FILE_FINDALL_CONTENT =
      "com/clra/web/findAll-request-content.http";

  /** The file that holds the CVS stylesheet */
  public final static String FILE_CSV_STYLESHEET =
      "com/clra/web/csv.xsl";
   
  /** The file that holds the simplified-XML stylesheet */
  public final static String FILE_XML_STYLESHEET =
      "com/clra/web/memberXml.xsl";

  /** The pre-built "findAll" request headers */
  private final static String requestHeaders =
    loadStringFromFile( FILE_FINDALL_HEADERS );

  /** The pre-built "findAll" request message */
  private final static String requestContent =
    loadStringFromFile( FILE_FINDALL_CONTENT );

  /** The csv stylesheet */
  private final static String csvStylesheet =
    loadStringFromFile( FILE_CSV_STYLESHEET );

  /** The simplified-XML stylesheet */
  private final static String xmlStylesheet =
    loadStringFromFile( FILE_XML_STYLESHEET );

  private static String loadStringFromFile( final String strFile ) {

    String retVal = null;
    try {
      URL url = Loader.getResource( strFile, MembershipServlet.class );
      retVal = loadStringFromInputStream( url.openStream() );
      if ( theLog.isDebugEnabled() ) {
        final int total = retVal.length();
        theLog.debug( "read " + strFile + " bytes from " + strFile );
        theLog.debug( "'" + strFile + "' contents == '" + retVal + "'" );
      }
    }
    catch( Exception x ){
      String msg = "unable to load request message from '" + strFile + "'";
      theLog.fatal(msg,x);
      throw new IllegalStateException( msg );
    }

    return retVal;
  } // loadStringFromFile(String)

  // Closes input stream after string is loaded
  private static String loadStringFromInputStream( InputStream is )
    throws IOException {
    StringWriter out = new StringWriter( BUFSIZE );
    writeFromInputStreamToWriter( is, out );
    return out.toString();
  }

  /** An implementation-specific exception that indicates a SOAP problem */
  public static class MembershipSoapException extends Exception {
    private MembershipSoapException( String msg ) { super(msg); }
  };

  /** Initializes the servlet */
  public void init(ServletConfig config) throws ServletException {
    super.init(config);
  }

  /** Destroys the servlet */
  public void destroy() {
  }

  private static Socket getFindAllResponseSocket(
    String username, String password ) throws IOException,
      MalformedURLException, SocketException, UnknownHostException,
        NoSuchEntityException {

    username = username == null ? null : username.trim() ;
    password = password == null ? null : password.trim() ;

    Socket retVal = null;

    // Create the request message
    String requestMessage = null;
    if ( username != null && password != null ) {
      String authInfo = username + ":" + password;
      String encoded = Base64.encode( authInfo.getBytes() );
      StringBuffer sb = new StringBuffer();
      sb.append( requestHeaders );
      sb.append( "Authorization: Basic " );
      sb.append( encoded );
      sb.append( "\r\n\r\n" );
      sb.append( requestContent );
      requestMessage = new String(sb);
    } else {
      StringBuffer sb = new StringBuffer();
      sb.append( requestHeaders );
      sb.append( "\r\n" );
      sb.append( requestContent );
      requestMessage = new String(sb);
    }

    // Send the SOAP request
    final String strURL = Configuration.SOAP_SERVER_URL;
    URL url = new URL( strURL );
    retVal = new Socket( url.getHost(), url.getPort() );
    retVal.setSoTimeout( timeoutMillis );
    OutputStream os = retVal.getOutputStream();
    final byte[] buf = requestMessage.getBytes();
    final int total = buf.length;
    os.write( buf, 0, total );
    theLog.debug( "wrote " + total + " bytes to " + strURL );

    return retVal;
  } // getFindAllResponseSocket()

  // Assumes buf != null && buf.length > 3
  private static boolean foundCRNL_CRNL( char[] buf ) {
    return ( buf[0] == '\r' )
        && ( buf[1] == '\n' )
        && ( buf[2] == '\r' )
        && ( buf[3] == '\n' );
  }

  private static void readUntilEndOfHttpHeaders( InputStream is )
    throws IOException, MembershipSoapException {

    final int  EOS = -1;

    char[] buf = new char[4]; // initialized to all zero's

    int b = is.read();
    while ( b != EOS ) {

      buf[0] = buf[1];
      buf[1] = buf[2];
      buf[2] = buf[3];
      buf[3] = (char) b;
      if ( foundCRNL_CRNL(buf) ) {
        break;
      }

      b = is.read();
    } // while !EOS

    if ( b == EOS ) {
      throw new MembershipSoapException( "did not find end of HTTP headers" );
    }

    return;
  } // readUntilEndOfHttpHeaders(InputStream)

  /**
   * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
   * methods. The current implementation uses a pre-built SOAP message
   * stored in the text file specified by <code>FINDALL-REQUEST</code>
   * to retrieve a SOAP message, and then uses XSL stylesheets to
   * to simplify the SOAP XML.
   *
   * @param request servlet request
   * @param response servlet response
   */
  protected void processRequest(
    HttpServletRequest request, HttpServletResponse response)
      throws ServletException, java.io.IOException {

    // Determine how to format the response
    String strFormat = request.getParameter( RPN_FORMAT );
    if ( strFormat == null ) {
      theLog.debug( "null format defaults to XML" );
      strFormat = RPV_FORMAT_XML;
    }

    // Set the response content-type and get the appropriate stylesheet
    String xsl = null;
    if ( RPV_FORMAT_CSV.equalsIgnoreCase(strFormat) ) {
      theLog.debug( RPV_FORMAT_CSV );
      response.setContentType( "text/plain" );
      xsl = csvStylesheet;
    }
    else if (RPV_FORMAT_XML.equalsIgnoreCase(strFormat) ) {
      theLog.debug( RPV_FORMAT_XML );
      response.setContentType( "text/plain" );
      xsl = xmlStylesheet;
    }
    else {
      String msg = "ERROR: unexpected format == '" + strFormat + "'";
      theLog.error( msg );
      throw new ServletException( msg );
    }

    try {
      MemberView mv = MemberTag.getMemberFromAuthenticatedUser(request);
      final String username = mv.getAccountName();
      final String password = mv.getAccountPassword();

      PrintWriter out = response.getWriter();
      writeResponse( username, password, xsl, out );
    }
    catch( Exception x ) {
      theLog.error( x );
      throw new ServletException( x );
    }

    return;
  } // processRequest(HttpServletRequest,HttpServletResponse)

  /** Delegates to the <code>processRequest</code> method */
  protected void doGet(
    HttpServletRequest request, HttpServletResponse response)
      throws ServletException, java.io.IOException {
    processRequest(request, response);
  }

  /** Delegates to the <code>processRequest</code> method */
  protected void doPost(
    HttpServletRequest request, HttpServletResponse response)
      throws ServletException, java.io.IOException {
    processRequest(request, response);
  }

  /**
   * Returns a short description of the servlet.
   */
  public String getServletInfo() {
    return "Reformats a SOAP message to CSV or simplified XML";
  }

  /**
   * Writes the entire SOAP response for <code>Member.findAllMembers</code>
   * to the specified PrintWriter. Used for JUnit testing.
   */
  public static void
  writeFindAllSoapXmlResponse(String username,String password,PrintWriter out)
  throws Exception {
    writeResponse(username,password,null,out);
  } // getFindAllSoapXmlResponse()

  /**
   * Writes the CSV-formatted response for <code>Member.findAllMembers</code>
   * to the specified PrintWriter. Used for JUnit testing.
   */
  public static void
  writeFindAllCsvResponse(String username,String password,PrintWriter out)
  throws Exception {
    writeResponse(username,password,csvStylesheet,out);
  }

  /**
   * Returns a simplified XML-formatted response for
   * <code>Member.findAllMembers</code>. Used for JUnit testing.
   */
  public static void
  writeFindAllSimplifiedXmlResponse(String name,String pass,PrintWriter out)
  throws Exception {
    writeResponse(name,pass,xmlStylesheet,out);
  }

  // Does NOT close the Writer.
  private static void
  writeResponse(String username, String password, String xsl, PrintWriter out)
  throws TransformerConfigurationException, TransformerException, IOException,
  MembershipSoapException {

    // Precondition
    if ( out == null ) {
      throw new IllegalArgumentException( "null PrintWriter" );
    }

    Socket s = null;
    InputStream soapStream = null;

    try {

      s = getFindAllResponseSocket(username,password);
      soapStream = s.getInputStream();

      if ( xsl == null ) {
        writeFromInputStreamToWriter( soapStream, out );
      }
      else {
        readUntilEndOfHttpHeaders( soapStream );
        Source xmlSource = new StreamSource( soapStream );
        Source xslSource =
          new StreamSource( new StringBufferInputStream(xsl) );
        TransformerFactory f = TransformerFactory.newInstance();
        Transformer t = f.newTransformer( xslSource );
        t.transform( xmlSource, new StreamResult(out) );
      }
      out.flush();

    }
    finally {
      if( s != null ) {
        try { s.close(); } catch( Exception x ) {}
      }
    }

    return;
  } // getFindAllFormattedResponse(String)

  // Closes input stream after string is loaded.
  // Does NOT close the Writer.
  private static void writeFromInputStreamToWriter(InputStream is, Writer out)
  throws IOException {

    InputStreamReader isr = null;
    try {
      isr = new InputStreamReader( is );
      char[] cbuf = new char[BUFSIZE];
      int numRead = isr.read( cbuf, 0, cbuf.length );
      while ( numRead > 0 ) {
        out.write( cbuf, 0, numRead );
        numRead = isr.read( cbuf, 0, cbuf.length );
      }
      out.flush();
    }
    finally {
      if ( isr != null ) {
        try { isr.close(); } catch( Exception x ) {}
      }
      if ( is != null ) {
        try { is.close(); } catch( Exception x ) {}
        is = null;
      }
    }

    return;
  } // readFromInputStreamToOutputWriter(InputStream,Writer)

} // MembershipServlet

/*
 * $Log: MembershipServlet.java,v $
 * Revision 1.2  2003/03/05 13:55:13  rphall
 * Added username/password to SOAP call
 *
 * Revision 1.1  2003/03/04 02:16:33  rphall
 * Moved MembershipServlet and *.xsl files to com/clra/web
 *
 * Revision 1.1  2003/03/02 17:31:43  rphall
 * Converts a soap response to CSV or beautified XML message
 *
 */

