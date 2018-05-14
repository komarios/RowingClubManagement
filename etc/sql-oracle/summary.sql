/*
 * $Id: summary.sql,v 1.1 2002/01/30 16:28:40 rphall Exp $
 */

set term on;

select count(*) Member from Member;
select count(*) MemberRole from MemberRole;
select count(*) SessionCoach from SessionCoach;
select count(*) RowingSession from RowingSession;
select count(*) Boat from Boat;
select count(*) Oarset from Oarset;
select count(*) Boating from Boating;
select count(*) Seat from Seat;
select count(*) Participant from Participant;

/*
 * $Log: summary.sql,v $
 * Revision 1.1  2002/01/30 16:28:40  rphall
 * Moved from etc/sql to etc/sql-oracle
 *
 * Revision 1.1  2002/01/13 21:10:38  rphall
 * Moved Oracle script from etc/sql to etc/sql-oracle
 *
 * Revision 1.2  2001/11/18 16:29:53  rphall
 * Added titles to count(*) columns
 *
 * Revision 1.1  2001/11/17 02:30:10  rphall
 * Added CVS Id and Log tags
 *
 */

