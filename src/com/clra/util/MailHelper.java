/*
 * Copyright (c) Carnegie Lake Rowing Association 2002. All rights reserved.
 * Distributed under the GPL license. See doc/COPYING.
 * $RCSfile: MailHelper.java,v $
 * $Date: 2003/02/26 03:38:45 $
 * $Revision: 1.5 $
 */

package com.clra.util;

import java.util.Date;
import java.util.Locale;
import java.util.Properties;

import javax.naming.InitialContext;
import java.io.InputStream;
import javax.activation.DataHandler;
import java.net.URL;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.Session;
import javax.mail.Multipart;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.InternetAddress;
import org.apache.log4j.Category;
import org.apache.log4j.helpers.Loader;

/**
 * A helper class to create and send mail.
 */
public class MailHelper {


    /**
     * This method creates an email message and sends it using the
     * J2EE mail services
     * @param mailContent contains the message contents to send
     */

  private final static String base = MailHelper.class.getName();

  private final static Category theLog = Category.getInstance( base );

  private final static String PN_MAILTYPE = "clra.mail.type";

  private final static String PN_MAILSERVER = "clra.mail.server";
  
  private final static String PN_MAILADDRESS = "clra.mail.admin.address";

  private final static String DEFAULTS_FILE =
          "com/clra/util/util.properties";

  private final static Properties defaultProperties = new Properties();
  static {
    InputStream is = null;
    try {
      URL url = Loader.getResource( DEFAULTS_FILE, MailHelper.class );
      is = url.openStream();
      defaultProperties.load( is );
      if ( theLog.isDebugEnabled() ) {
        theLog.debug( "loaded properties from '" + DEFAULTS_FILE + "'" );
        java.util.Enumeration _e = defaultProperties.propertyNames();
        while ( _e.hasMoreElements() ) {
          theLog.debug( "property: " + _e.nextElement() );
        }
      }
    }
    catch( Exception x ){
      String msg = "unable to load default properties from '"
              + DEFAULTS_FILE + "'";
      theLog.fatal(msg,x);
      throw new IllegalStateException( msg );
    }
    finally {
      if ( is != null ) {
        try { is.close(); } catch( Exception x ) {}
        is = null;
      }
    } // finally
  } // static

  private static String DEFAULT_MAIL_SERVER = 
                                            defaultProperties.getProperty(PN_MAILSERVER);
  private static String DEFAULT_MAIL_TYPE = 
                                            defaultProperties.getProperty(PN_MAILTYPE);

  private static String DEFAULT_MAIL_ADDRESS = 
                                            defaultProperties.getProperty(PN_MAILADDRESS);

  public void createAndSendMail(String emailAddress,
                                String subject,
                                String mailContent,
                                Locale locale) throws MailerAppException {
        try {
            Properties props = new Properties();
            //props.put("mail.smtp.host", "smtp3.sympatico.ca");
            props.put(DEFAULT_MAIL_TYPE, DEFAULT_MAIL_SERVER);
            Session session = javax.mail.Session.getInstance(props);
            
            Message msg = new MimeMessage(session);
            msg.setFrom();
            msg.setRecipients(Message.RecipientType.TO,
                     InternetAddress.parse(emailAddress, false));
            msg.setSubject(subject);
            String contentType = "text/html";
                StringBuffer sb = new StringBuffer(mailContent);
            msg.setDataHandler(new DataHandler(
                                  new ByteArrayDataSource(sb.toString(), contentType)));
            msg.setHeader("X-Mailer", "JavaMailer");
            msg.setSentDate(new Date());
            Transport.send(msg);
        } catch (Exception e) {
            System.out.print("createAndSendMail exception : " + e);
            throw new MailerAppException("Failure while sending mail");
        }
  }                                   	
  
  public void createAndSendMail(String subject, 
                                String mailContent, 
                                Locale locale) throws MailerAppException {

        createAndSendMail(DEFAULT_MAIL_ADDRESS, subject, mailContent, locale);
  }
}

/*
 * $Log: MailHelper.java,v $
 * Revision 1.5  2003/02/26 03:38:45  rphall
 * Added copyright and GPL license
 *
 * Revision 1.4  2003/02/19 22:09:17  rphall
 * Removed gratuitous use of CLRA acronym
 *
 */

