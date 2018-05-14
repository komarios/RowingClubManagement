/*
 * Copyright (c) Carnegie Lake Rowing Association 2002. All rights reserved.
 * Distributed under the GPL license. See doc/COPYING.
 * $RCSfile: MailerAppException.java,v $
 * $Date: 2003/02/26 03:38:45 $
 * $Revision: 1.3 $
 */

package com.clra.util;

/**
 * MailerAppException is an exception that extends the standrad
 * Exception. This is thrown by the mailer component when there is some
 * failure while sending the mail
 */

public class MailerAppException extends Exception {

    /**
     * Default constructor. Takes no arguments
     */
    public MailerAppException() {}

    /**
     * Constructor
     * @param str    a string that explains what the exception condition is
     */
    public MailerAppException(String str) {
        super(str);
    }
}

