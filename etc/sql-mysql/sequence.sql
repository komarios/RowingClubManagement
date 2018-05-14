-- $Id: sequence.sql,v 1.1 2002/01/30 16:31:02 rphall Exp $

DROP TABLE IF EXISTS ClraSequence ;

CREATE TABLE ClraSequence(
  id    integer      not null,
  name  varchar(13)  not null
);

INSERT INTO ClraSequence VALUES (10000,'Boat');
INSERT INTO ClraSequence VALUES (10000,'Oarset');
INSERT INTO ClraSequence VALUES (10000,'Member');
INSERT INTO ClraSequence VALUES (10000,'RowingSession');
INSERT INTO ClraSequence VALUES (10000,'Participant');
INSERT INTO ClraSequence VALUES (10000,'Seat');

