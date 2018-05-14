/*
 * $Id: sequence_participant.sql,v 1.1 2002/01/30 16:28:40 rphall Exp $
 */

DROP SEQUENCE clra_participant;

CREATE SEQUENCE clra_participant
  INCREMENT BY 1
  START WITH 10000
  NOMAXVALUE
  NOCYCLE
  CACHE 10;

/*
 * $Log: sequence_participant.sql,v $
 * Revision 1.1  2002/01/30 16:28:40  rphall
 * Moved from etc/sql to etc/sql-oracle
 *
 * Revision 1.2  2002/01/18 21:37:55  rphall
 * Changed sequence to start at 10000
 *
 * Revision 1.1  2002/01/13 21:10:38  rphall
 * Moved Oracle script from etc/sql to etc/sql-oracle
 *
 * Revision 1.1  2001/11/18 16:35:33  rphall
 * Sequence for Participant table
 *
 */

