#!/bin/bash

# ----------------------------------------------------------------
#
#    Batch file for exporting the Member table
#
#    Tested under JDK 1.3 on Linux kernel 2.4.2
#
#    Usage: ./test-clra.sh
#
#    Author:    Rick Hall
#    Revision:  $Revision: 1.1 $ $Date: 2002/01/30 14:02:12 $
#
# ----------------------------------------------------------------

CP=../dist/classes
CP=$CP:../lib/log4j.jar
CP=$CP:../lib/junit.jar
CP=$CP:../lib/struts.jar
CP=$CP:../lib/commons-collections.jar
CP=$CP:$JBOSS_DIST/lib/ext/oracle_jdbc_classes111.jar
CP=$CP:$JBOSS_DIST/client/jboss-j2ee.jar
CP=$CP:$JBOSS_DIST/client/jaas.jar
CP=$CP:$JBOSS_DIST/client/jbossmq-client.jar
CP=$CP:$JBOSS_DIST/client/jbosssx-client.jar
CP=$CP:$JBOSS_DIST/client/jboss-client.jar
CP=$CP:$JBOSS_DIST/client/jnp-client.jar

${JAVA_HOME}/bin/java -classpath ${CP} test.clra.member.ExportRoles

