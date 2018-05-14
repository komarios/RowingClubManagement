/*
 * $Id: sequence_boat.sql,v 1.1 2002/01/30 16:28:40 rphall Exp $
 */

DROP SEQUENCE clra_boat;

CREATE SEQUENCE clra_boat
  INCREMENT BY 1
  START WITH 10000
  NOMAXVALUE
  NOCYCLE
  CACHE 10;

/*
 * $Log: sequence_boat.sql,v $
 * Revision 1.1  2002/01/30 16:28:40  rphall
 * Moved from etc/sql to etc/sql-oracle
 *
 * Revision 1.2  2002/01/18 21:37:15  rphall
 * Removed NEXTVAL for compatibility with mySQL
 *
 * Revision 1.1  2002/01/13 21:10:38  rphall
 * Moved Oracle script from etc/sql to etc/sql-oracle
 *
 * Revision 1.2  2001/11/17 02:28:04  rphall
 * Added CVS Id and Log tags
 *
 */

