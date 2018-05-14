#!/bin/bash

# ----------------------------------------------------------------
#
#    Batch file for testing the clra application.
#
#    Tested under JDK 1.3 on Linux kernel 2.4.2
#
#    Usage: ./test-clra.sh
#
#    Author:    Rick Hall
#    Revision:  $Revision: 1.2 $ $Date: 2002/01/30 13:49:25 $
#
# ----------------------------------------------------------------

CP=../dist/classes
CP=$CP:../lib/log4j.jar
CP=$CP:../lib/junit.jar
CP=$CP:../lib/struts.jar
CP=$CP:../lib/commons-collections.jar
CP=$CP:$JBOSS_DIST/lib/ext/oracle_jdbc_classes111.jar
CP=$CP:$JBOSS_DIST/lib/ext/mm.mysql-2.0.8-bin.jar
CP=$CP:$JBOSS_DIST/client/jboss-j2ee.jar
CP=$CP:$JBOSS_DIST/client/jaas.jar
CP=$CP:$JBOSS_DIST/client/jbossmq-client.jar
CP=$CP:$JBOSS_DIST/client/jbosssx-client.jar
CP=$CP:$JBOSS_DIST/client/jboss-client.jar
CP=$CP:$JBOSS_DIST/client/jnp-client.jar

${JAVA_HOME}/bin/java -classpath ${CP} junit.textui.TestRunner test.clra.AllTestsLog4jConfig

