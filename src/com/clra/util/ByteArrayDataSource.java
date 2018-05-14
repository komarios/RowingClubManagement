/*
 * Copyright (c) Carnegie Lake Rowing Association 2002. All rights reserved.
 * Distributed under the GPL license. See doc/COPYING.
 * $RCSfile: ByteArrayDataSource.java,v $
 * $Date: 2003/02/26 03:38:45 $
 * $Revision: 1.3 $
 */

package com.clra.util;


import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Date;

import javax.activation.DataSource;


/**
 * Used to create a DataSource for the mail message.
 * @see MailHelper
 */
class ByteArrayDataSource implements DataSource {
    private byte[] data; // data for mail message
    private String type; // content type/mime type

   /**
    * Create a DataSource from a String
    * @param data is the contents of the mail message
    * @param type is the mime-type such as text/html
    */
    ByteArrayDataSource(String data, String type) {
        try {
           this.data = data.getBytes("UTF-8");
        } catch (UnsupportedEncodingException uex) { }
        this.type = type;
    }

    //DataSource interface methods

    public InputStream getInputStream() throws IOException {
        if (data == null)
            throw new IOException("no data");
        return new ByteArrayInputStream(data);
    }

    public OutputStream getOutputStream() throws IOException {
        throw new IOException("cannot do this");
    }

    public String getContentType() {
        return type;
    }

    public String getName() {
        return "dummy";
    }
}


