/*
 * $Id: setup.sql,v 1.1 2002/01/30 16:28:40 rphall Exp $
 */

set term off;

@@clra_ddl.sql;

@@sequence_member.sql;
@@sequence_rowing.sql;
@@sequence_boat.sql;
@@sequence_oarset.sql;
@@sequence_seat.sql;
@@sequence_participant.sql;

@@test_member.sql;
@@test_memberrole.sql;
@@test_rowingsession.sql;
@@test_boat.sql;
@@test_oarset.sql;
@@test_participantsignup.sql;

@@summary.sql

exit;

/*
 * $Log: setup.sql,v $
 * Revision 1.1  2002/01/30 16:28:40  rphall
 * Moved from etc/sql to etc/sql-oracle
 *
 * Revision 1.2  2002/01/18 21:39:12  rphall
 * Moved 'DROP SEQUENCE' to appropriate sequence script
 *
 * Revision 1.1  2002/01/13 21:10:38  rphall
 * Moved Oracle script from etc/sql to etc/sql-oracle
 *
 * Revision 1.5  2001/11/18 16:28:55  rphall
 * Added sequences for Seat and Participant.
 * Rearranged order of operations.
 *
 * Revision 1.4  2001/11/17 02:28:04  rphall
 * Added CVS Id and Log tags
 *
 */

