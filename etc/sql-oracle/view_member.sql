/*
 * $Id: view_member.sql,v 1.1 2002/01/30 16:28:40 rphall Exp $
 */

SELECT member_id, name_last, name_first FROM Member ORDER BY name_last;
exit;

/*
 * $Log: view_member.sql,v $
 * Revision 1.1  2002/01/30 16:28:40  rphall
 * Moved from etc/sql to etc/sql-oracle
 *
 * Revision 1.2  2002/01/18 21:40:48  rphall
 * Changed capitalization for consistency with mySQL
 *
 * Revision 1.1  2002/01/13 21:10:38  rphall
 * Moved Oracle script from etc/sql to etc/sql-oracle
 *
 * Revision 1.2  2001/11/17 02:28:04  rphall
 * Added CVS Id and Log tags
 *
 */

