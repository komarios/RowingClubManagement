/*
 * $Id: test_oarset.sql,v 1.1 2002/01/30 16:28:40 rphall Exp $
 */

DELETE FROM Oarset;

INSERT INTO Oarset(oarset_id,oarset_name,oarset_size,oarset_type) values( 1, 'Yellow', '8', 'SWEEP' );

INSERT INTO Oarset(oarset_id,oarset_name,oarset_size,oarset_type) values( 2, 'Red and Blue', '8', 'SWEEP' );

INSERT INTO Oarset(oarset_id,oarset_name,oarset_size,oarset_type) values( 3, 'Unadjustable', '8', 'SWEEP' );

INSERT INTO Oarset(oarset_id,oarset_name,oarset_size,oarset_type) values( 4, 'Red and Yellow', '8', 'SWEEP' );

INSERT INTO Oarset(oarset_id,oarset_name,oarset_size,oarset_type) values( 5, 'Green and White', '8', 'SWEEP' );

/*
 * $Log: test_oarset.sql,v $
 * Revision 1.1  2002/01/30 16:28:40  rphall
 * Moved from etc/sql to etc/sql-oracle
 *
 * Revision 1.2  2002/01/18 21:38:22  rphall
 * Removed NEXTVAL for compatibility with mySQL
 *
 * Revision 1.1  2002/01/13 21:10:38  rphall
 * Moved Oracle script from etc/sql to etc/sql-oracle
 *
 * Revision 1.3  2001/11/17 02:28:04  rphall
 * Added CVS Id and Log tags
 *
 */

