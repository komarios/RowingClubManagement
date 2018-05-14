/*
 * $Id: test_boat.sql,v 1.1 2002/01/30 16:28:40 rphall Exp $
 */

DELETE FROM Boat;

INSERT INTO Boat(boat_id,boat_name,boat_size,boat_type) values( 1, 'North Country', '4', 'SWEEP' );

INSERT INTO Boat(boat_id,boat_name,boat_size,boat_type) values( 2, 'New Laker 4', '4', 'SWEEP' );

INSERT INTO Boat(boat_id,boat_name,boat_size,boat_type) values( 3, 'Finn', '8', 'SWEEP' );

INSERT INTO Boat(boat_id,boat_name,boat_size,boat_type) values( 4, 'WL 1', '8', 'SWEEP' );

INSERT INTO Boat(boat_id,boat_name,boat_size,boat_type) values( 5, 'WL 2', '8', 'SWEEP' );

INSERT INTO Boat(boat_id,boat_name,boat_size,boat_type) values( 6, 'Old Laker 4', '4', 'SWEEP' );

INSERT INTO Boat(boat_id,boat_name,boat_size,boat_type) values( 7, 'Unruh', '8', 'SWEEP' );

INSERT INTO Boat(boat_id,boat_name,boat_size,boat_type) values( 8, 'Finn Meislahn', '8', 'SWEEP' );

INSERT INTO Boat(boat_id,boat_name,boat_size,boat_type) values( 9, 'Scoop', '8', 'SWEEP' );

INSERT INTO Boat(boat_id,boat_name,boat_size,boat_type) values( 10, 'Nilsson', '8', 'SWEEP' );

INSERT INTO Boat(boat_id,boat_name,boat_size,boat_type) values( 11, 'Kashper', '2', 'SCULL' );

/*
 * $Log: test_boat.sql,v $
 * Revision 1.1  2002/01/30 16:28:40  rphall
 * Moved from etc/sql to etc/sql-oracle
 *
 * Revision 1.2  2002/01/18 21:38:22  rphall
 * Removed NEXTVAL for compatibility with mySQL
 *
 * Revision 1.1  2002/01/13 21:10:38  rphall
 * Moved Oracle script from etc/sql to etc/sql-oracle
 *
 * Revision 1.2  2001/11/17 02:28:04  rphall
 * Added CVS Id and Log tags
 *
 */

