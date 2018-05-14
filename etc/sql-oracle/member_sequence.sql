/*
 * $Id: member_sequence.sql,v 1.1 2002/01/30 16:28:40 rphall Exp $
 */

CREATE SEQUENCE clra_member
  INCREMENT BY 1
  START WITH 1
  NOMAXVALUE
  NOCYCLE
  CACHE 10;

/*
 * $Log: member_sequence.sql,v $
 * Revision 1.1  2002/01/30 16:28:40  rphall
 * Moved from etc/sql to etc/sql-oracle
 *
 * Revision 1.1  2002/01/13 21:10:38  rphall
 * Moved Oracle script from etc/sql to etc/sql-oracle
 *
 * Revision 1.2  2001/11/17 02:28:04  rphall
 * Added CVS Id and Log tags
 *
 */

