#!/bin/sh
time java -classpath lib/jaxrpc.jar:lib/axis.jar:lib/xmlParserAPIs.jar:lib/log4j.jar:lib/junit.jar:lib/commons-logging.jar:lib/commons-discovery.jar:lib/xercesImpl.jar:lib/saaj.jar:dist/classes test.clra.xml.security.Test_DBSecurityProvider
