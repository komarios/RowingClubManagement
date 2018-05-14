/*
 * Copyright (c) Carnegie Lake Rowing Association 2002. All rights reserved.
 * Distributed under the GPL license. See doc/COPYING.
 * $RCSfile: CreateParticipants.java,v $
 * $Date: 2003/03/08 21:11:09 $
 * $Revision: 1.5 $
 */

package test.clra.rowing;

import com.clra.member.AccountType;
import com.clra.member.MemberDBRead;
import com.clra.member.MemberSnapshot;
import com.clra.rowing.ParticipantSnapshot;
import com.clra.rowing.RowingDBRead;
import com.clra.rowing.RowingSessionLevel;
import com.clra.rowing.RowingSessionSnapshot;
import com.clra.rowing.SeatPreference;
import com.clra.util.DBConfiguration;
import com.clra.util.ErrorUtils;

import java.io.PrintStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Category;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.helpers.Loader;

/**
 * This utility creates Oracle or mySQL INSERT statements to populate the
 * Participant table. The class is meant to run from a command-line and
 * its output may be directed to a file. This utility is meant mainly to
 * generate test data.<p>
 *
 * Participants are generated from the Member table for every entry in the
 * RowingSession table, except for rowing sessions that are marked 'TENATIVE'.
 * Participants are matched by type to rowing sessions: FULL members are placed
 * in REGULAR sessions, and LTR participants are placed in LTR sessions. A
 * member is placed at most once as participant in a rowing session. About
 * 30 participants (enough for four 8's, four coxswains, and two extras) are
 * are created per rowing session.<p>
 *
 * Unlike the <tt>CreateRowingSessions</tt> utility, the <strong>main</strong>
 * entry point of this utility does NOT take any parameters. The target
 * database, as well as the database from which Member and RowingSession data,
 * is drawn, is specified by the com/clra/util/util.properties file.<p>
 *
 * @version $Revision: 1.5 $ $Date: 2003/03/08 21:11:09 $
 * @author <a href="mailto:rphall@pluto.njcc.com">Rick Hall</a>
 */
public final class CreateParticipants {

  private final static String _LOG4J_URL = "test/log4j.properties";
  private static boolean isConfigured = false;
  private static URL url = null;

  /** Initialize logging. See test/log4j.properties for settings */
  static {

    // If not configured, check whether the log4.configuration file of this
    // class exists. If so, use it.
    if ( !isConfigured ) {
      url = Loader.getResource( _LOG4J_URL, CreateParticipants.class );
      if ( url != null ) {
        System.err.println("log4j URL == " + url);
        PropertyConfigurator.configure( url );
        System.err.println("Finished static configuration of log4j");
        isConfigured = true;
      }
      else {
        System.err.println("No static configuration of log4j");
      }
    }

  }

  /** A placeholder for a paricipant id */
  private final static Integer FAKE_PARTICIPANT_ID = new Integer( -1 );

  /**
   * Approximate number of participants per rowing session. The actual
   * number may less, because routine that randomly generates participants
   * may generate duplicates.
   */
  private static final int APPROX_PARTICIPANT_NUMBER = 30;

  /** An array of all members */
  private static MemberSnapshot[] arrMember = null;
  static {
    try {
      Collection c = MemberDBRead.findAllMembersByLastName();
      MemberSnapshot[] tmp = new MemberSnapshot[0];
      arrMember = (MemberSnapshot[]) c.toArray( tmp );
    }
    catch( Throwable t ) {
      String msg = ErrorUtils.createDbgMsg( "arrRowing init", t );
      throw new IllegalStateException( msg );
    }
  } // static

  /** An array of all rowing sessions */
  private static RowingSessionSnapshot[] arrRowing = null;
  static {
    try {
      Collection c = RowingDBRead.findAllRowingSessionSnapshots();
      RowingSessionSnapshot[] tmp = new RowingSessionSnapshot[0];
      arrRowing = (RowingSessionSnapshot[]) c.toArray( tmp );
    }
    catch( Throwable t ) {
      String msg = ErrorUtils.createDbgMsg( "arrRowing init", t );
      throw new IllegalStateException( msg );
    }
  } // static

  /** Used to generate random SeatPreference values */
  private final static SeatPreference[] arrSP = {
    SeatPreference.COX,
    SeatPreference.STARBOARD,
    SeatPreference.PORT,
    SeatPreference.STARBOARD_THEN_PORT,
    SeatPreference.PORT_THEN_STARBOARD
  };

  /** All public operations are static */
  private CreateParticipants() {}

  /**
   * Command-line entry point. Takes no arguments. The target database,
   * as well as the database from which Member and RowingSession data
   * are drawn, is specified by the <tt>dbtype</tt> entry in the
   * <tt>com/clra/util/util.properties</tt> file.
   */
  public static void main( String[] args ) {

    // Preconditions
    if ( arrMember == null || arrRowing == null ) {
      throw new IllegalStateException( "invalid invariant" );
    }
    if ( arrMember.length == 0 || arrRowing.length == 0 ) {
      throw new IllegalStateException( "invalid invariant" );
    }

    boolean isOracle = true;
    if ( new String("mysql").equalsIgnoreCase(DBConfiguration.DBTYPE) ) {
      isOracle = false;
    }

    // Note CVS keywords are broken apart to avoid expansion in source code
    System.out.println( "-- $" + "Revision:" + "$" );
    System.out.println( "-- $" + "Date:" + "$" );
    System.out.println( "-- Generated " + (new Date()).toString() );
    System.out.println();
    System.out.println( "DELETE FROM Participant;" );
    System.out.println();

    // Iterate over rowing sessions
    for ( int i=0; i<arrRowing.length; i++ ) {

      // Determine the appropriate AccountType for participants
      AccountType tmp = null;
      RowingSessionLevel rsLvl = arrRowing[i].getLevel();
      if ( rsLvl.equals(RowingSessionLevel.REGULAR) ) {
        tmp = AccountType.FULL;
      }
      else if ( rsLvl.equals(RowingSessionLevel.LTR) ) {
        tmp = AccountType.LTR;
      }
      else {
        String msg = "unexpected rowing level == '" + rsLvl.getName() + "'";
        throw new IllegalStateException( msg );
      }
      final AccountType accountType = tmp;

      // Use a set to prevent duplicate member-participants.
      // (Note Member class defines "equals", but Participant does not.)
      Set setMP = new HashSet();

      // Add to the MP set, up to APPROX_PARTICIPANT_NUMBER, if possible
      final int GUARD = APPROX_PARTICIPANT_NUMBER * 2;
      for ( int j=0, g=0; j<APPROX_PARTICIPANT_NUMBER && g<GUARD; g++ ) {
        int idx = (int)(arrMember.length * Math.random());
        MemberSnapshot ms = arrMember[ idx ];
        if ( accountType.equals(ms.getAccountType()) ) {
          setMP.add( ms );
          ++j;
        }
      } // for j,g

      // Create Participants from MP set and write them to the standard output
      Iterator iter = setMP.iterator();
      while ( iter.hasNext() ) {
        MemberSnapshot ms = (MemberSnapshot) iter.next();
        SeatPreference sp = randomSeatPreference();
        ParticipantSnapshot ps = new ParticipantSnapshot(
            // memberId
            ms.getId(),
            // rowingId
            arrRowing[i].getId(),
            // participantId (ignored during printing)
            FAKE_PARTICIPANT_ID,
            // seatPreference
            sp, 
            // replacesId (for substitutions; not used, yet)
            null,
            // initialSeatId (not used, yet)
            null,
            // finalSeatId (not used, yet)
            null,
            // attendance ( not used, yet)
            null);
        printParticipant( isOracle, ps, System.out );
      } // while

    } // for i

    return;
  } // main

  /** Returns a random SeatPreference value */
  public static SeatPreference randomSeatPreference() {

    SeatPreference retVal = null;

    int idx = (int)(arrSP.length * Math.random());
    if ( idx == 0 ) {
      // Index 0 is the coxswain.
      // Coxing isn't a frequent preference.
      // Roll the dice again (reduces coxing to 0.2 * 0.2 == 1 in 25)
      idx = (int)(arrSP.length * Math.random());
    }
    retVal = arrSP[ idx ];

    return retVal;
  }

  /** Prints an INSERT statement for a participant */
  public static void printParticipant(
      boolean isOracle, ParticipantSnapshot ps, PrintStream pw) {

    StringBuffer sb = new StringBuffer();

    if ( !isOracle ) {
      sb.append( "UPDATE ClraSequence SET id = LAST_INSERT_ID(id + 1) " );
      sb.append( "WHERE name = 'Participant'; ");
    }
    sb.append( "INSERT INTO Participant( " );
    sb.append( "participant_id, rowing_id, member_id, requested) " );

    if ( isOracle ) {
      sb.append( "VALUES ( clra_participant.NEXTVAL, " );
    }
    else {
      sb.append( "VALUES ( LAST_INSERT_ID(), " );
    }
    sb.append( "'" + ps.getRowingId().intValue() + "', " );
    sb.append( "'" + ps.getMemberId().intValue() + "', " );
    sb.append( "'" + ps.getSeatPreference().getName() + "' ); " );

    pw.println( new String(sb) );

    return;
  } // printRowingSession(boolean,RowingSessionSnapshot,PrintWriter)

} // CreateParticipants

/*
 * $Log: CreateParticipants.java,v $
 * Revision 1.5  2003/03/08 21:11:09  rphall
 * Removed unused (and ambiguous) import statements
 *
 * Revision 1.4  2003/02/26 03:38:47  rphall
 * Added copyright and GPL license
 *
 * Revision 1.3  2003/02/20 05:03:20  rphall
 * Imported AccountType
 *
 * Revision 1.2  2003/02/19 22:09:19  rphall
 * Removed gratuitous use of CLRA acronym
 *
 * Revision 1.1  2002/01/30 16:35:34  rphall
 * Script to create Participant data based on Member and RowingSession tables
 */

