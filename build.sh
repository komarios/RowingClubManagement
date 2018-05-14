#!/bin/bash

# ----------------------------------------------------------------
# 
#    Batch file for running the build scripts on the prob02_lineqs
#    package. Requires libraries ant.jar, jaxp.jar and JAXP
#    compatible XML parser in classpath and file build.xml in the
#    etc directory. Default target is 'compile'.
#
#    Usage: build [target]
#           build clean
#
#    target     - Builds specified target. If target is omitted,
#                 uses default target 'compile'
#
#    clean      - Deletes the entire build directory.
#
#    
#    Author:    Rick Hall
#    Revision:  $Id: build.sh,v 1.6 2003/03/04 02:24:18 rphall Exp $
#
# ----------------------------------------------------------------

CP=lib/ant-1.3.jar
CP=$CP:lib/ejb-api-3.0.jar
CP=$CP:lib/javax.mail-api-1.6.1.jar
CP=$CP:lib/commons-logging-1.1.0.jboss.jar
CP=$CP:lib/jbosssx.jar
CP=$CP:lib/jboss-ejb-api_3.1_spec.jar
CP=$CP:$JBOSS_DIST/common/lib/jboss-servlet-api_3.0_spec.jar
CP=$CP:$JBOSS_DIST/common/lib/jboss-jsp-api_2.2_spec.jar
CP=$CP:lib/jaas.jar
CP=$CP:lib/jboss-jaas.jar
CP=$CP:lib/jaxp.jar
CP=$CP:lib/parser.jar
CP=$CP:lib/ant-1.3-optional.jar
CP=$CP:lib/junit.jar
CP=$CP:lib/log4j.jar
CP=$CP:lib/struts.jar
CP=$CP:lib/commons-collections.jar
CP=$CP:lib/axis.jar
CP=$CP:lib/commons-discovery.jar
CP=$CP:lib/commons-logging.jar
CP=$CP:lib/jaxrpc.jar
CP=$CP:lib/saaj.jar
CP=$CP:lib/wsdl4j.jar
CP=$CP:lib/xalan-j241.jar
CP=$CP:lib/xercesImpl.jar
CP=$CP:lib/xmlParserAPIs.jar
CP=$CP:$JAVA_HOME/lib/tools.jar
CP=$CP:$JBOSS_DIST/lib/ext/mail.jar
CP=$CP:$JBOSS_DIST/lib/ext/activation.jar

# Deprecated; see JBoss 2.2.2 or 2.4.4 path below
#CP=$CP:$J2EE_HOME/lib/j2ee.jar

# JBoss 2.2.2 with Tomcat 3.2.3
#CP=$CP:$JBOSS_DIST/client/ejb.jar
#CP=$CP:$JBOSS_DIST/../tomcat/common/lib/servlet.jar

# JBoss 2.4.4 with Catalina 4.0.1
CP=$CP:$JBOSS_DIST/client/jboss-j2ee.jar
CP=$CP:$JBOSS_DIST/../catalina/common/lib/servlet.jar

BUILDFILE=etc/build.xml

${JAVA_HOME}/bin/java -classpath ${CP}:${CLASSPATH} org.apache.tools.ant.Main \
 -buildfile $BUILDFILE $@

