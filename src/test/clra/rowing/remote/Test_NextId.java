/*
 * Copyright (c) Carnegie Lake Rowing Association 2002. All rights reserved.
 * Distributed under the GPL license. See doc/COPYING.
 * $RCSfile: Test_NextId.java,v $
 * $Date: 2003/02/26 03:38:47 $
 * $Revision: 1.2 $
 */

package test.clra.rowing.remote;

import com.clra.rowing.Configuration;
import com.clra.rowing.remote.RowingSessionBean;
import com.clra.rowing.remote.ParticipantBean;
import com.clra.util.DBConfiguration;
import com.clra.util.ErrorUtils;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import junit.framework.TestCase;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Category;

/**
 * @version $Default:$ $Date: 2003/02/26 03:38:47 $
 */
public class Test_NextId extends TestCase {

  // The logger used by this class
  private final static Category theLog =
    Category.getInstance(Test_NextId.class);

  /** Explicitly exercises SQL, as way to debug it */
  public static void main( String[] args ) {

    new test.Log4jConfigurator("");

    try {
      Integer id = ParticipantBean.nextId();
      System.out.println( "Next participant id == " +  id );

      id = RowingSessionBean.nextId();
      System.out.println( "Next rowing-session id == " + id );
    }
    catch( Exception x ) {
      x.printStackTrace();
    }

    return;
  } // main

  public Test_NextId( String name ) {
    super( name );
  }

  public void setUp() {
  } // setUp()

  public void tearDown() {
  } // tearDown()

  public void testParticipantNextId() {

    try {
      Integer id = ParticipantBean.nextId();
      Integer id2 = ParticipantBean.nextId();
      assertTrue( id2.intValue() == id.intValue() +1 );
    }
    catch( Exception x ) {
      String msg = ErrorUtils.createDbgMsg( "testParticipantNextId", x );
      theLog.error( msg );
      fail( msg );
    }

  } // testParticipantNextId()

  public void testRowingSessionNextId() {

    try {
      Integer id = RowingSessionBean.nextId();
      Integer id2 = RowingSessionBean.nextId();
      assertTrue( id2.intValue() == id.intValue() +1 );
    }
    catch( Exception x ) {
      String msg = ErrorUtils.createDbgMsg( "testRowingSessionNextId", x );
      theLog.error( msg );
      fail( msg );
    }

  } // testRowingSessionNextId()

} // Test_NextId

/*
 * $Log: Test_NextId.java,v $
 * Revision 1.2  2003/02/26 03:38:47  rphall
 * Added copyright and GPL license
 *
 * Revision 1.1  2002/01/30 16:43:12  rphall
 * Moved to ./remote subdirectory
 *
 * Revision 1.1  2002/01/30 16:38:14  rphall
 * Tests mySQL-compatible nextId methods
 *
 * Revision 1.1  2002/01/25 01:49:44  rphall
 * Test nextId() routines
 *
 */

