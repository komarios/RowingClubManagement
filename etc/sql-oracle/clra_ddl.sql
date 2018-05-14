/*
 * $Id: clra_ddl.sql,v 1.10 2003/03/13 00:25:21 rphall Exp $
 */

DROP TABLE MemberRole CASCADE CONSTRAINTS;

CREATE TABLE MemberRole (
       member_id            INTEGER NOT NULL,
       Role                 VARCHAR(18) NOT NULL
                                   CHECK (Role IN ('INACTIVE', 'MEMBER', 'COACH', 'CAPTAIN', 'SESSIONMGR', 'TREASURER', 'MEMBERMGR')),
       RoleGroup            VARCHAR(18) NOT NULL
                                   CHECK (RoleGroup IN ('Roles', 'CallerPrincipal'))
);


ALTER TABLE MemberRole
       ADD  ( PRIMARY KEY (member_id, Role, RoleGroup) ) ;


DROP TABLE SessionCoach CASCADE CONSTRAINTS;

CREATE TABLE SessionCoach (
       rowing_id            INTEGER NOT NULL,
       member_id            INTEGER NOT NULL
);


ALTER TABLE SessionCoach
       ADD  ( PRIMARY KEY (rowing_id, member_id) ) ;


DROP TABLE Seat CASCADE CONSTRAINTS;

CREATE TABLE Seat (
       seat_id              INTEGER NOT NULL,
       rowing_id            INTEGER NOT NULL,
       boat_id              INTEGER NOT NULL,
       seat_number          INTEGER NOT NULL
                                   CHECK (seat_number BETWEEN 1 AND 8)
);


ALTER TABLE Seat
       ADD  ( PRIMARY KEY (seat_id) ) ;


CREATE UNIQUE INDEX seat_rowing_boat_seatnum_uk ON Seat
(
       rowing_id                      DESC,
       boat_id                        ASC,
       seat_number                    ASC
);


DROP TABLE Participant CASCADE CONSTRAINTS;

CREATE TABLE Participant (
       participant_id       INTEGER NOT NULL,
       rowing_id            INTEGER NOT NULL,
       member_id            INTEGER NOT NULL,
       requested            VARCHAR(10) NULL
                                   CHECK (requested IN ('PORT', 'STARBOARD', 'P(S)', 'S(P)', 'COX')),
       initial_seat         INTEGER NULL,
       final_seat           INTEGER NULL,
       replaces_id             INTEGER NULL,
       attendance           VARCHAR(8) NULL
                                   CHECK (attendance IN ('PRESENT', 'NOSHOW', 'ABSENT'))
);


ALTER TABLE Participant
       ADD  ( PRIMARY KEY (participant_id) ) ;


CREATE UNIQUE INDEX participant_rowing_member_uk ON Participant
(
       rowing_id                      DESC,
       member_id                      ASC
);


DROP TABLE Member CASCADE CONSTRAINTS;

CREATE TABLE Member (
       member_id            INTEGER NOT NULL,
       account_name         VARCHAR(20) NOT NULL,
       account_passwd       VARCHAR(20) NOT NULL,
       clra_status          VARCHAR(8) NOT NULL
                                   CHECK (clra_status IN ('LTR', 'NOVICE', 'FULL', 'CONTRACT', 'ALUMNI', 'LTR-ALUM', 'DUPLICAT' )),
       name_last            VARCHAR(20) NOT NULL,
       name_first           VARCHAR(20) NOT NULL,
       name_middle          VARCHAR(20) NULL,
       name_suffix          VARCHAR(20) NULL,
       email                VARCHAR(40) NULL,
       telephone_evening    VARCHAR(25) NOT NULL,
       telephone_day        VARCHAR(25) NULL,
       telephone_other      VARCHAR(25) NULL,
       address_street1      VARCHAR(40) NOT NULL,
       address_street2      VARCHAR(40) NULL,
       address_city         VARCHAR(20) NOT NULL,
       address_state        CHAR(2) NOT NULL,
       address_zip          VARCHAR(10) NOT NULL,
       clra_year            INTEGER NOT NULL,
       birthday             DATE NULL
);

CREATE UNIQUE INDEX member_account_uk ON Member
(
       UPPER(account_name)  ASC
);


ALTER TABLE Member
       ADD  ( PRIMARY KEY (member_id) ) ;


DROP TABLE Boating CASCADE CONSTRAINTS;

CREATE TABLE Boating (
       rowing_id            INTEGER NOT NULL,
       boat_id              INTEGER NOT NULL,
       oarset_id            INTEGER NOT NULL,
       boating_state        VARCHAR(11) NOT NULL
                                   CHECK (boating_state IN ('WEATHER', 'NOSHOW', 'COMPLETED', 'PRELIM', 'EQUIPMENT'))
);


ALTER TABLE Boating
       ADD  ( PRIMARY KEY (rowing_id, boat_id) ) ;


DROP TABLE Boat CASCADE CONSTRAINTS;

CREATE TABLE Boat (
       boat_id              INTEGER NOT NULL,
       boat_name            VARCHAR(20) NOT NULL,
       boat_size            INTEGER NOT NULL
                                   CHECK (boat_size BETWEEN 1 AND 8),
       boat_type            VARCHAR(5) NOT NULL
                                   CHECK (boat_type IN ('SCULL', 'SWEEP'))
);


CREATE UNIQUE INDEX boat_name_uk ON Boat
(
       boat_name                 ASC
);


ALTER TABLE Boat
       ADD  ( PRIMARY KEY (boat_id) ) ;


DROP TABLE Oarset CASCADE CONSTRAINTS;

CREATE TABLE Oarset (
       oarset_id            INTEGER NOT NULL,
       oarset_name          VARCHAR(20) NOT NULL,
       oarset_size          INTEGER NOT NULL
                                   CHECK (oarset_size BETWEEN 1 AND 8),
       oarset_type          VARCHAR(5) NOT NULL
                                   CHECK (oarset_type IN ('SCULL', 'SWEEP'))
);


CREATE UNIQUE INDEX oarset_name_uk ON Oarset
(
       oarset_name                 ASC
);


ALTER TABLE Oarset
       ADD  ( PRIMARY KEY (oarset_id) ) ;


DROP TABLE RowingSession CASCADE CONSTRAINTS;

CREATE TABLE RowingSession (
       rowing_id            INTEGER NOT NULL,
       rowing_date          DATE NOT NULL,
       rowing_level         VARCHAR(7) NOT NULL
                                   CHECK (rowing_level IN ('REGULAR', 'LTR')),
       rowing_type          VARCHAR(8) NOT NULL
                                   CHECK (rowing_type IN ('PRACTICE', 'REGATTA')),
       rowing_state         VARCHAR(9) NOT NULL
                                   CHECK (rowing_state IN ('TENATIVE', 'OPEN', 'LOCKED', 'BOATING1', 'BOATING2', 'COMPLETE', 'INVOICING', 'CLOSED', 'CANCELLED'))
);


ALTER TABLE RowingSession
       ADD  ( PRIMARY KEY (rowing_id) ) ;


ALTER TABLE MemberRole
       ADD  ( FOREIGN KEY (member_id)
                             REFERENCES Member ) ;


ALTER TABLE SessionCoach
       ADD  ( FOREIGN KEY (member_id)
                             REFERENCES Member ) ;


ALTER TABLE SessionCoach
       ADD  ( FOREIGN KEY (rowing_id)
                             REFERENCES RowingSession ) ;


ALTER TABLE Seat
       ADD  ( FOREIGN KEY (rowing_id, boat_id)
                             REFERENCES Boating ) ;


ALTER TABLE Participant
       ADD  ( FOREIGN KEY (final_seat)
                             REFERENCES Seat ) ;


ALTER TABLE Participant
       ADD  ( FOREIGN KEY (initial_seat)
                             REFERENCES Seat ) ;


ALTER TABLE Participant
       ADD  ( FOREIGN KEY (rowing_id)
                             REFERENCES RowingSession ) ;


ALTER TABLE Participant
       ADD  ( FOREIGN KEY (member_id)
                             REFERENCES Member ) ;


ALTER TABLE Participant
       ADD  ( FOREIGN KEY (replaces_id)
                             REFERENCES Participant ) ;


ALTER TABLE Boating
       ADD  ( FOREIGN KEY (oarset_id)
                             REFERENCES Oarset ) ;


ALTER TABLE Boating
       ADD  ( FOREIGN KEY (boat_id)
                             REFERENCES Boat ) ;


ALTER TABLE Boating
       ADD  ( FOREIGN KEY (rowing_id)
                             REFERENCES RowingSession ) ;

-- ALTER TABLE Member CACHE;

-- ALTER TABLE MemberRole CACHE;

-- ALTER TABLE RowingSession CACHE;

-- ALTER TABLE Boat CACHE;

-- ALTER TABLE Oarset CACHE;


DROP TABLE Applicant;

CREATE TABLE Applicant (
       name_last            VARCHAR(20) NOT NULL,
       name_first           VARCHAR(20) NOT NULL,
       name_middle          VARCHAR(20) NULL,
       name_suffix          VARCHAR(20) NULL,
       email                VARCHAR(40) NULL,
       telephone_evening    VARCHAR(25) NOT NULL,
       telephone_day        VARCHAR(25) NULL,
       telephone_other      VARCHAR(25) NULL,
       address_street1      VARCHAR(40) NOT NULL,
       address_street2      VARCHAR(40) NULL,
       address_city         VARCHAR(20) NOT NULL,
       address_state        CHAR(2) NOT NULL,
       address_zip          VARCHAR(10) NOT NULL,
       experience_year      VARCHAR(5) NOT NULL,
       recent_year          VARCHAR(20) NOT NULL,
       birthday             DATE NULL,
       sex                  CHAR(1) NOT NULL,
       apply_date           DATE NOT NULL,
       status               VARCHAR(10) NOT NULL
);


ALTER TABLE Applicant
       ADD  ( PRIMARY KEY (email) ) ;


create trigger tI_MemberRole after INSERT on MemberRole for each row
-- ERwin Builtin Thu Nov 15 17:25:30 2001
-- INSERT trigger on MemberRole 
declare numrows INTEGER;
begin
    /* ERwin Builtin Thu Nov 15 17:25:30 2001 */
    /* Member R/41 MemberRole ON CHILD INSERT RESTRICT */
    select count(*) into numrows
      from Member
      where
        /* %JoinFKPK(:%New,Member," = "," and") */
        :new.member_id = Member.member_id;
    if (
      /* %NotnullFK(:%New," is not null and") */
      
      numrows = 0
    )
    then
      raise_application_error(
        -20002,
        'Cannot INSERT MemberRole because Member does not exist.'
      );
    end if;


-- ERwin Builtin Thu Nov 15 17:25:30 2001
end;
/

create trigger tU_MemberRole after UPDATE on MemberRole for each row
-- ERwin Builtin Thu Nov 15 17:25:30 2001
-- UPDATE trigger on MemberRole 
declare numrows INTEGER;
begin
  /* ERwin Builtin Thu Nov 15 17:25:30 2001 */
  /* Member R/41 MemberRole ON CHILD UPDATE RESTRICT */
  select count(*) into numrows
    from Member
    where
      /* %JoinFKPK(:%New,Member," = "," and") */
      :new.member_id = Member.member_id;
  if (
    /* %NotnullFK(:%New," is not null and") */
    
    numrows = 0
  )
  then
    raise_application_error(
      -20007,
      'Cannot UPDATE MemberRole because Member does not exist.'
    );
  end if;


-- ERwin Builtin Thu Nov 15 17:25:30 2001
end;
/

create trigger tI_SessionCoach after INSERT on SessionCoach for each row
-- ERwin Builtin Thu Nov 15 17:25:30 2001
-- INSERT trigger on SessionCoach 
declare numrows INTEGER;
begin
    /* ERwin Builtin Thu Nov 15 17:25:30 2001 */
    /* Member may be SessionCoach ON CHILD INSERT RESTRICT */
    select count(*) into numrows
      from Member
      where
        /* %JoinFKPK(:%New,Member," = "," and") */
        :new.member_id = Member.member_id;
    if (
      /* %NotnullFK(:%New," is not null and") */
      
      numrows = 0
    )
    then
      raise_application_error(
        -20002,
        'Cannot INSERT SessionCoach because Member does not exist.'
      );
    end if;

    /* ERwin Builtin Thu Nov 15 17:25:30 2001 */
    /* RowingSession must have SessionCoach ON CHILD INSERT RESTRICT */
    select count(*) into numrows
      from RowingSession
      where
        /* %JoinFKPK(:%New,RowingSession," = "," and") */
        :new.rowing_id = RowingSession.rowing_id;
    if (
      /* %NotnullFK(:%New," is not null and") */
      
      numrows = 0
    )
    then
      raise_application_error(
        -20002,
        'Cannot INSERT SessionCoach because RowingSession does not exist.'
      );
    end if;


-- ERwin Builtin Thu Nov 15 17:25:30 2001
end;
/

create trigger tU_SessionCoach after UPDATE on SessionCoach for each row
-- ERwin Builtin Thu Nov 15 17:25:30 2001
-- UPDATE trigger on SessionCoach 
declare numrows INTEGER;
begin
  /* ERwin Builtin Thu Nov 15 17:25:30 2001 */
  /* Member may be SessionCoach ON CHILD UPDATE RESTRICT */
  select count(*) into numrows
    from Member
    where
      /* %JoinFKPK(:%New,Member," = "," and") */
      :new.member_id = Member.member_id;
  if (
    /* %NotnullFK(:%New," is not null and") */
    
    numrows = 0
  )
  then
    raise_application_error(
      -20007,
      'Cannot UPDATE SessionCoach because Member does not exist.'
    );
  end if;

  /* ERwin Builtin Thu Nov 15 17:25:30 2001 */
  /* RowingSession must have SessionCoach ON CHILD UPDATE RESTRICT */
  select count(*) into numrows
    from RowingSession
    where
      /* %JoinFKPK(:%New,RowingSession," = "," and") */
      :new.rowing_id = RowingSession.rowing_id;
  if (
    /* %NotnullFK(:%New," is not null and") */
    
    numrows = 0
  )
  then
    raise_application_error(
      -20007,
      'Cannot UPDATE SessionCoach because RowingSession does not exist.'
    );
  end if;


-- ERwin Builtin Thu Nov 15 17:25:30 2001
end;
/

create trigger tI_Seat after INSERT on Seat for each row
-- ERwin Builtin Thu Nov 15 17:25:30 2001
-- INSERT trigger on Seat 
declare numrows INTEGER;
begin
    /* ERwin Builtin Thu Nov 15 17:25:30 2001 */
    /* Boating R/30 Seat ON CHILD INSERT RESTRICT */
    select count(*) into numrows
      from Boating
      where
        /* %JoinFKPK(:%New,Boating," = "," and") */
        :new.rowing_id = Boating.rowing_id and
        :new.boat_id = Boating.boat_id;
    if (
      /* %NotnullFK(:%New," is not null and") */
      
      numrows = 0
    )
    then
      raise_application_error(
        -20002,
        'Cannot INSERT Seat because Boating does not exist.'
      );
    end if;


-- ERwin Builtin Thu Nov 15 17:25:30 2001
end;
/

create trigger tU_Seat after UPDATE on Seat for each row
-- ERwin Builtin Thu Nov 15 17:25:30 2001
-- UPDATE trigger on Seat 
declare numrows INTEGER;
begin
  /* ERwin Builtin Thu Nov 15 17:25:30 2001 */
  /* Boating R/30 Seat ON CHILD UPDATE RESTRICT */
  select count(*) into numrows
    from Boating
    where
      /* %JoinFKPK(:%New,Boating," = "," and") */
      :new.rowing_id = Boating.rowing_id and
      :new.boat_id = Boating.boat_id;
  if (
    /* %NotnullFK(:%New," is not null and") */
    
    numrows = 0
  )
  then
    raise_application_error(
      -20007,
      'Cannot UPDATE Seat because Boating does not exist.'
    );
  end if;


-- ERwin Builtin Thu Nov 15 17:25:30 2001
end;
/

create trigger tD_Seat after DELETE on Seat for each row
-- ERwin Builtin Fri Nov 16 22:26:34 2001
-- DELETE trigger on Seat 
declare numrows INTEGER;
begin
    /* ERwin Builtin Fri Nov 16 22:26:34 2001 */
    /* Seat initially_assigned_to Participant ON PARENT DELETE SET NULL */
    update Participant
      set
        /* %SetFK(Participant,NULL) */
        Participant.initial_seat = NULL
      where
        /* %JoinFKPK(Participant,:%Old," = "," and") */
        Participant.initial_seat = :old.SEAT_ID;


    /* ERwin Builtin Fri Nov 16 22:26:34 2001 */
    /* Seat finally_assigned_to Participant ON PARENT DELETE SET NULL */
    update Participant
      set
        /* %SetFK(Participant,NULL) */
        Participant.final_seat = NULL
      where
        /* %JoinFKPK(Participant,:%Old," = "," and") */
        Participant.final_seat = :old.SEAT_ID;


-- ERwin Builtin Fri Nov 16 22:26:34 2001
end;
/

/*
 * FIXME
 * This RI check produces ORA-04091 (trigger may not see mutating table)
 *
create trigger tD_Participant after DELETE on Participant for each row
declare numrows INTEGER;
begin
    select count(*) into numrows
      from Participant
      where
        Participant.replaces_id = :old.participant_id;
    if (numrows > 0)
    then
      raise_application_error(
        -20001,
        'Cannot DELETE replaced Participant because substitute exists.'
      );
    end if;


end;
/
 *
 * END FIXME
*/

create trigger tI_Participant after INSERT on Participant for each row
-- ERwin Builtin Thu Nov 15 17:25:30 2001
-- INSERT trigger on Participant 
declare numrows INTEGER;
begin
    /* ERwin Builtin Thu Nov 15 17:25:30 2001 */
    /* RowingSession R/22 Participant ON CHILD INSERT RESTRICT */
    select count(*) into numrows
      from RowingSession
      where
        /* %JoinFKPK(:%New,RowingSession," = "," and") */
        :new.rowing_id = RowingSession.rowing_id;
    if (
      /* %NotnullFK(:%New," is not null and") */
      
      numrows = 0
    )
    then
      raise_application_error(
        -20002,
        'Cannot INSERT Participant because RowingSession does not exist.'
      );
    end if;

    /* ERwin Builtin Thu Nov 15 17:25:30 2001 */
    /* Member may be Participant ON CHILD INSERT RESTRICT */
    select count(*) into numrows
      from Member
      where
        /* %JoinFKPK(:%New,Member," = "," and") */
        :new.member_id = Member.member_id;
    if (
      /* %NotnullFK(:%New," is not null and") */
      
      numrows = 0
    )
    then
      raise_application_error(
        -20002,
        'Cannot INSERT Participant because Member does not exist.'
      );
    end if;

    /* ERwin Builtin Sat Nov 17 17:22:18 2001 */
    /* Participant replaces Participant ON CHILD INSERT RESTRICT */
    /*
     * FIXME
     * This RI check produces ORA-04091 (trigger may not see mutating table)
     *
    select count(*) into numrows
      from Participant
      where
        :new.replaces_id = Participant.participant_id;
    if (
      :new.replaces_id is not null and
      numrows = 0
    )
    then
      raise_application_error(
        -20002,
        'Cannot INSERT substitute because replaced Participant does not exist.'
      );
    end if;
     *
     * END FIXME
    */


-- ERwin Builtin Thu Nov 15 17:25:30 2001
end;
/

create trigger tU_Participant after UPDATE on Participant for each row
-- ERwin Builtin Thu Nov 15 17:25:30 2001
-- UPDATE trigger on Participant 
declare numrows INTEGER;
begin
  /* ERwin Builtin Thu Nov 15 17:25:30 2001 */
  /* RowingSession R/22 Participant ON CHILD UPDATE RESTRICT */
  select count(*) into numrows
    from RowingSession
    where
      /* %JoinFKPK(:%New,RowingSession," = "," and") */
      :new.rowing_id = RowingSession.rowing_id;
  if (
    /* %NotnullFK(:%New," is not null and") */
    
    numrows = 0
  )
  then
    raise_application_error(
      -20007,
      'Cannot UPDATE Participant because RowingSession does not exist.'
    );
  end if;

  /* ERwin Builtin Thu Nov 15 17:25:30 2001 */
  /* Member may be Participant ON CHILD UPDATE RESTRICT */
  select count(*) into numrows
    from Member
    where
      /* %JoinFKPK(:%New,Member," = "," and") */
      :new.member_id = Member.member_id;
  if (
    /* %NotnullFK(:%New," is not null and") */
    
    numrows = 0
  )
  then
    raise_application_error(
      -20007,
      'Cannot UPDATE Participant because Member does not exist.'
    );
  end if;

  /* ERwin Builtin Sat Nov 17 17:22:18 2001 */
  /*
   * Participant.replaced is_replaced_by Participant.substitute
   * ON PARENT UPDATE RESTRICT
  */
  /*
   * FIXME
   * This RI check produces ORA-04091 (trigger may not see mutating table)
   *
  if
    :old.participant_id <> :new.participant_id
  then
    select count(*) into numrows
      from Participant
      where
        Participant.replaces_id = :old.participant_id;
    if (numrows > 0)
    then 
      raise_application_error(
        -20005,
        'Cannot UPDATE replaced Participant because substitute exists.'
      );
    end if;
  end if;
   *
   * END FIXME
  */

  /* ERwin Builtin Sat Nov 17 17:22:18 2001 */
  /*
   * Participant.replaced is_replaced_by Participant.substitute
   * ON CHILD UPDATE RESTRICT
  */
  /*
   * FIXME
   * This RI check produces ORA-04091 (trigger may not see mutating table)
   *
  select count(*) into numrows
    from Participant
    where
      :new.replaces_id = Participant.participant_id;
  if (
    :new.replaces_id is not null and
    numrows = 0
  )
  then
    raise_application_error(
      -20007,
      'Cannot UPDATE substitute because replaced Participant does not exist.'
    );
  end if;
   *
   * END FIXME
  */


-- ERwin Builtin Thu Nov 15 17:25:30 2001
end;
/

create trigger tD_Member after DELETE on Member for each row
-- ERwin Builtin Thu Nov 15 17:25:30 2001
-- DELETE trigger on Member 
declare numrows INTEGER;
begin
    /* ERwin Builtin Thu Nov 15 17:25:30 2001 */
    /* Member R/41 MemberRole ON PARENT DELETE RESTRICT */
    select count(*) into numrows
      from MemberRole
      where
        /*  %JoinFKPK(MemberRole,:%Old," = "," and") */
        MemberRole.member_id = :old.member_id;
    if (numrows > 0)
    then
      raise_application_error(
        -20001,
        'Cannot DELETE Member because MemberRole exists.'
      );
    end if;

    /* ERwin Builtin Thu Nov 15 17:25:30 2001 */
    /* Member may be SessionCoach ON PARENT DELETE RESTRICT */
    select count(*) into numrows
      from SessionCoach
      where
        /*  %JoinFKPK(SessionCoach,:%Old," = "," and") */
        SessionCoach.member_id = :old.member_id;
    if (numrows > 0)
    then
      raise_application_error(
        -20001,
        'Cannot DELETE Member because SessionCoach exists.'
      );
    end if;

    /* ERwin Builtin Thu Nov 15 17:25:30 2001 */
    /* Member may be Participant ON PARENT DELETE RESTRICT */
    select count(*) into numrows
      from Participant
      where
        /*  %JoinFKPK(Participant,:%Old," = "," and") */
        Participant.member_id = :old.member_id;
    if (numrows > 0)
    then
      raise_application_error(
        -20001,
        'Cannot DELETE Member because Participant exists.'
      );
    end if;


-- ERwin Builtin Thu Nov 15 17:25:30 2001
end;
/

create trigger tU_Member after UPDATE on Member for each row
-- ERwin Builtin Thu Nov 15 17:25:30 2001
-- UPDATE trigger on Member 
declare numrows INTEGER;
begin
  /* ERwin Builtin Thu Nov 15 17:25:30 2001 */
  /* Member R/41 MemberRole ON PARENT UPDATE RESTRICT */
  if
    /* %JoinPKPK(:%Old,:%New," <> "," or ") */
    :old.member_id <> :new.member_id
  then
    select count(*) into numrows
      from MemberRole
      where
        /*  %JoinFKPK(MemberRole,:%Old," = "," and") */
        MemberRole.member_id = :old.member_id;
    if (numrows > 0)
    then 
      raise_application_error(
        -20005,
        'Cannot UPDATE Member because MemberRole exists.'
      );
    end if;
  end if;

  /* ERwin Builtin Thu Nov 15 17:25:30 2001 */
  /* Member may be SessionCoach ON PARENT UPDATE RESTRICT */
  if
    /* %JoinPKPK(:%Old,:%New," <> "," or ") */
    :old.member_id <> :new.member_id
  then
    select count(*) into numrows
      from SessionCoach
      where
        /*  %JoinFKPK(SessionCoach,:%Old," = "," and") */
        SessionCoach.member_id = :old.member_id;
    if (numrows > 0)
    then 
      raise_application_error(
        -20005,
        'Cannot UPDATE Member because SessionCoach exists.'
      );
    end if;
  end if;

  /* ERwin Builtin Thu Nov 15 17:25:30 2001 */
  /* Member may be Participant ON PARENT UPDATE RESTRICT */
  if
    /* %JoinPKPK(:%Old,:%New," <> "," or ") */
    :old.member_id <> :new.member_id
  then
    select count(*) into numrows
      from Participant
      where
        /*  %JoinFKPK(Participant,:%Old," = "," and") */
        Participant.member_id = :old.member_id;
    if (numrows > 0)
    then 
      raise_application_error(
        -20005,
        'Cannot UPDATE Member because Participant exists.'
      );
    end if;
  end if;


-- ERwin Builtin Thu Nov 15 17:25:30 2001
end;
/

create trigger tD_Boating after DELETE on Boating for each row
-- ERwin Builtin Thu Nov 15 17:25:30 2001
-- DELETE trigger on Boating 
declare numrows INTEGER;
begin
    /* ERwin Builtin Thu Nov 15 17:25:30 2001 */
    /* Boating R/30 Seat ON PARENT DELETE RESTRICT */
    select count(*) into numrows
      from Seat
      where
        /*  %JoinFKPK(Seat,:%Old," = "," and") */
        Seat.rowing_id = :old.rowing_id and
        Seat.boat_id = :old.boat_id;
    if (numrows > 0)
    then
      raise_application_error(
        -20001,
        'Cannot DELETE Boating because Seat exists.'
      );
    end if;


-- ERwin Builtin Thu Nov 15 17:25:30 2001
end;
/

create trigger tI_Boating after INSERT on Boating for each row
-- ERwin Builtin Thu Nov 15 17:25:30 2001
-- INSERT trigger on Boating 
declare numrows INTEGER;
begin
    /* ERwin Builtin Thu Nov 15 17:25:30 2001 */
    /* Oarset R/15 Boating ON CHILD INSERT RESTRICT */
    select count(*) into numrows
      from Oarset
      where
        /* %JoinFKPK(:%New,Oarset," = "," and") */
        :new.oarset_id = Oarset.oarset_id;
    if (
      /* %NotnullFK(:%New," is not null and") */
      
      numrows = 0
    )
    then
      raise_application_error(
        -20002,
        'Cannot INSERT Boating because Oarset does not exist.'
      );
    end if;

    /* ERwin Builtin Thu Nov 15 17:25:30 2001 */
    /* Boat R/14 Boating ON CHILD INSERT RESTRICT */
    select count(*) into numrows
      from Boat
      where
        /* %JoinFKPK(:%New,Boat," = "," and") */
        :new.boat_id = Boat.boat_id;
    if (
      /* %NotnullFK(:%New," is not null and") */
      
      numrows = 0
    )
    then
      raise_application_error(
        -20002,
        'Cannot INSERT Boating because Boat does not exist.'
      );
    end if;

    /* ERwin Builtin Thu Nov 15 17:25:30 2001 */
    /* RowingSession has Boating ON CHILD INSERT RESTRICT */
    select count(*) into numrows
      from RowingSession
      where
        /* %JoinFKPK(:%New,RowingSession," = "," and") */
        :new.rowing_id = RowingSession.rowing_id;
    if (
      /* %NotnullFK(:%New," is not null and") */
      
      numrows = 0
    )
    then
      raise_application_error(
        -20002,
        'Cannot INSERT Boating because RowingSession does not exist.'
      );
    end if;


-- ERwin Builtin Thu Nov 15 17:25:30 2001
end;
/

create trigger tU_Boating after UPDATE on Boating for each row
-- ERwin Builtin Thu Nov 15 17:25:30 2001
-- UPDATE trigger on Boating 
declare numrows INTEGER;
begin
  /* ERwin Builtin Thu Nov 15 17:25:30 2001 */
  /* Boating R/30 Seat ON PARENT UPDATE RESTRICT */
  if
    /* %JoinPKPK(:%Old,:%New," <> "," or ") */
    :old.rowing_id <> :new.rowing_id or 
    :old.boat_id <> :new.boat_id
  then
    select count(*) into numrows
      from Seat
      where
        /*  %JoinFKPK(Seat,:%Old," = "," and") */
        Seat.rowing_id = :old.rowing_id and
        Seat.boat_id = :old.boat_id;
    if (numrows > 0)
    then 
      raise_application_error(
        -20005,
        'Cannot UPDATE Boating because Seat exists.'
      );
    end if;
  end if;

  /* ERwin Builtin Thu Nov 15 17:25:30 2001 */
  /* Oarset R/15 Boating ON CHILD UPDATE RESTRICT */
  select count(*) into numrows
    from Oarset
    where
      /* %JoinFKPK(:%New,Oarset," = "," and") */
      :new.oarset_id = Oarset.oarset_id;
  if (
    /* %NotnullFK(:%New," is not null and") */
    
    numrows = 0
  )
  then
    raise_application_error(
      -20007,
      'Cannot UPDATE Boating because Oarset does not exist.'
    );
  end if;

  /* ERwin Builtin Thu Nov 15 17:25:30 2001 */
  /* Boat R/14 Boating ON CHILD UPDATE RESTRICT */
  select count(*) into numrows
    from Boat
    where
      /* %JoinFKPK(:%New,Boat," = "," and") */
      :new.boat_id = Boat.boat_id;
  if (
    /* %NotnullFK(:%New," is not null and") */
    
    numrows = 0
  )
  then
    raise_application_error(
      -20007,
      'Cannot UPDATE Boating because Boat does not exist.'
    );
  end if;

  /* ERwin Builtin Thu Nov 15 17:25:30 2001 */
  /* RowingSession has Boating ON CHILD UPDATE RESTRICT */
  select count(*) into numrows
    from RowingSession
    where
      /* %JoinFKPK(:%New,RowingSession," = "," and") */
      :new.rowing_id = RowingSession.rowing_id;
  if (
    /* %NotnullFK(:%New," is not null and") */
    
    numrows = 0
  )
  then
    raise_application_error(
      -20007,
      'Cannot UPDATE Boating because RowingSession does not exist.'
    );
  end if;


-- ERwin Builtin Thu Nov 15 17:25:30 2001
end;
/

create trigger tD_Boat after DELETE on Boat for each row
-- ERwin Builtin Thu Nov 15 17:25:30 2001
-- DELETE trigger on Boat 
declare numrows INTEGER;
begin
    /* ERwin Builtin Thu Nov 15 17:25:30 2001 */
    /* Boat R/14 Boating ON PARENT DELETE RESTRICT */
    select count(*) into numrows
      from Boating
      where
        /*  %JoinFKPK(Boating,:%Old," = "," and") */
        Boating.boat_id = :old.boat_id;
    if (numrows > 0)
    then
      raise_application_error(
        -20001,
        'Cannot DELETE Boat because Boating exists.'
      );
    end if;


-- ERwin Builtin Thu Nov 15 17:25:30 2001
end;
/

create trigger tU_Boat after UPDATE on Boat for each row
-- ERwin Builtin Thu Nov 15 17:25:30 2001
-- UPDATE trigger on Boat 
declare numrows INTEGER;
begin
  /* ERwin Builtin Thu Nov 15 17:25:30 2001 */
  /* Boat R/14 Boating ON PARENT UPDATE RESTRICT */
  if
    /* %JoinPKPK(:%Old,:%New," <> "," or ") */
    :old.boat_id <> :new.boat_id
  then
    select count(*) into numrows
      from Boating
      where
        /*  %JoinFKPK(Boating,:%Old," = "," and") */
        Boating.boat_id = :old.boat_id;
    if (numrows > 0)
    then 
      raise_application_error(
        -20005,
        'Cannot UPDATE Boat because Boating exists.'
      );
    end if;
  end if;


-- ERwin Builtin Thu Nov 15 17:25:30 2001
end;
/

create trigger tD_Oarset after DELETE on Oarset for each row
-- ERwin Builtin Thu Nov 15 17:25:30 2001
-- DELETE trigger on Oarset 
declare numrows INTEGER;
begin
    /* ERwin Builtin Thu Nov 15 17:25:30 2001 */
    /* Oarset R/15 Boating ON PARENT DELETE RESTRICT */
    select count(*) into numrows
      from Boating
      where
        /*  %JoinFKPK(Boating,:%Old," = "," and") */
        Boating.oarset_id = :old.oarset_id;
    if (numrows > 0)
    then
      raise_application_error(
        -20001,
        'Cannot DELETE Oarset because Boating exists.'
      );
    end if;


-- ERwin Builtin Thu Nov 15 17:25:30 2001
end;
/

create trigger tU_Oarset after UPDATE on Oarset for each row
-- ERwin Builtin Thu Nov 15 17:25:30 2001
-- UPDATE trigger on Oarset 
declare numrows INTEGER;
begin
  /* ERwin Builtin Thu Nov 15 17:25:30 2001 */
  /* Oarset R/15 Boating ON PARENT UPDATE RESTRICT */
  if
    /* %JoinPKPK(:%Old,:%New," <> "," or ") */
    :old.oarset_id <> :new.oarset_id
  then
    select count(*) into numrows
      from Boating
      where
        /*  %JoinFKPK(Boating,:%Old," = "," and") */
        Boating.oarset_id = :old.oarset_id;
    if (numrows > 0)
    then 
      raise_application_error(
        -20005,
        'Cannot UPDATE Oarset because Boating exists.'
      );
    end if;
  end if;


-- ERwin Builtin Thu Nov 15 17:25:30 2001
end;
/

create trigger tD_RowingSession after DELETE on RowingSession for each row
-- ERwin Builtin Thu Nov 15 17:25:30 2001
-- DELETE trigger on RowingSession 
declare numrows INTEGER;
begin
    /* ERwin Builtin Thu Nov 15 17:25:30 2001 */
    /* RowingSession must have SessionCoach ON PARENT DELETE RESTRICT */
    select count(*) into numrows
      from SessionCoach
      where
        /*  %JoinFKPK(SessionCoach,:%Old," = "," and") */
        SessionCoach.rowing_id = :old.rowing_id;
    if (numrows > 0)
    then
      raise_application_error(
        -20001,
        'Cannot DELETE RowingSession because SessionCoach exists.'
      );
    end if;

    /* ERwin Builtin Thu Nov 15 17:25:30 2001 */
    /* RowingSession R/22 Participant ON PARENT DELETE RESTRICT */
    select count(*) into numrows
      from Participant
      where
        /*  %JoinFKPK(Participant,:%Old," = "," and") */
        Participant.rowing_id = :old.rowing_id;
    if (numrows > 0)
    then
      raise_application_error(
        -20001,
        'Cannot DELETE RowingSession because Participant exists.'
      );
    end if;

    /* ERwin Builtin Thu Nov 15 17:25:30 2001 */
    /* RowingSession has Boating ON PARENT DELETE RESTRICT */
    select count(*) into numrows
      from Boating
      where
        /*  %JoinFKPK(Boating,:%Old," = "," and") */
        Boating.rowing_id = :old.rowing_id;
    if (numrows > 0)
    then
      raise_application_error(
        -20001,
        'Cannot DELETE RowingSession because Boating exists.'
      );
    end if;


-- ERwin Builtin Thu Nov 15 17:25:30 2001
end;
/

create trigger tU_RowingSession after UPDATE on RowingSession for each row
-- ERwin Builtin Thu Nov 15 17:25:30 2001
-- UPDATE trigger on RowingSession 
declare numrows INTEGER;
begin
  /* ERwin Builtin Thu Nov 15 17:25:30 2001 */
  /* RowingSession must have SessionCoach ON PARENT UPDATE RESTRICT */
  if
    /* %JoinPKPK(:%Old,:%New," <> "," or ") */
    :old.rowing_id <> :new.rowing_id
  then
    select count(*) into numrows
      from SessionCoach
      where
        /*  %JoinFKPK(SessionCoach,:%Old," = "," and") */
        SessionCoach.rowing_id = :old.rowing_id;
    if (numrows > 0)
    then 
      raise_application_error(
        -20005,
        'Cannot UPDATE RowingSession because SessionCoach exists.'
      );
    end if;
  end if;

  /* ERwin Builtin Thu Nov 15 17:25:30 2001 */
  /* RowingSession R/22 Participant ON PARENT UPDATE RESTRICT */
  if
    /* %JoinPKPK(:%Old,:%New," <> "," or ") */
    :old.rowing_id <> :new.rowing_id
  then
    select count(*) into numrows
      from Participant
      where
        /*  %JoinFKPK(Participant,:%Old," = "," and") */
        Participant.rowing_id = :old.rowing_id;
    if (numrows > 0)
    then 
      raise_application_error(
        -20005,
        'Cannot UPDATE RowingSession because Participant exists.'
      );
    end if;
  end if;

  /* ERwin Builtin Thu Nov 15 17:25:30 2001 */
  /* RowingSession has Boating ON PARENT UPDATE RESTRICT */
  if
    /* %JoinPKPK(:%Old,:%New," <> "," or ") */
    :old.rowing_id <> :new.rowing_id
  then
    select count(*) into numrows
      from Boating
      where
        /*  %JoinFKPK(Boating,:%Old," = "," and") */
        Boating.rowing_id = :old.rowing_id;
    if (numrows > 0)
    then 
      raise_application_error(
        -20005,
        'Cannot UPDATE RowingSession because Boating exists.'
      );
    end if;
  end if;


-- ERwin Builtin Thu Nov 15 17:25:30 2001
end;
/

/*
 * $Log: clra_ddl.sql,v $
 * Revision 1.10  2003/03/13 00:25:21  rphall
 * Added DUPLICAT as a valid value for clra_status
 *
 * Revision 1.9  2002/10/06 20:49:12  rphall
 * Added apply_date to Applicant table
 *
 * Revision 1.8  2002/08/14 14:22:36  huayue
 * modify definition of Applicant
 *
 * Revision 1.7  2002/08/08 17:46:31  huayue
 * add information for Applicant table.
 *
 * Revision 1.6  2002/03/31 00:07:11  rphall
 * Added 'NOVICE' and 'LTR-ALUM' as allowed Member.clra-status values
 *
 * Revision 1.5  2002/03/24 02:01:40  rphall
 * Added 'ALUMNI' to list of valid Member.clra_status values.
 *
 * Revision 1.4  2002/03/17 10:59:50  rphall
 * Removed vestigial TempEnrollment table
 *
 * Revision 1.3  2002/03/17 09:25:48  rphall
 * Added 'INACTIVE' role to list of valid MemberRole.role values.
 *
 * Revision 1.2  2002/02/20 13:40:47  rphall
 * Changed 'ROLES' to 'Roles' in check constraint on MemberRole.rolegroup
 * Changed member_account_uk to function index on UPPER(Member.account_name)
 *
 * Revision 1.1  2002/01/30 16:28:40  rphall
 * Moved from etc/sql to etc/sql-oracle
 *
 * Revision 1.4  2002/01/29 17:47:46  rphall
 * Added TempEnrollment table
 *
 * Revision 1.3  2002/01/25 02:17:52  rphall
 * Fixed syntax in Member table
 *
 * Revision 1.2  2002/01/18 21:36:17  rphall
 * Changed data types for compatibility with MySQL
 *
 * Revision 1.1  2002/01/13 21:10:38  rphall
 * Moved Oracle script from etc/sql to etc/sql-oracle
 *
 * Revision 1.17  2001/11/28 11:44:51  rphall
 * Removed table caching. Revised allowed rowing states
 *
 * Revision 1.16  2001/11/17 23:36:41  rphall
 * Played with RI triggers to avoid ORA-04091. RI is largely disabled for 'replaces_id' fishhook
 *
 * Revision 1.15  2001/11/17 21:47:50  rphall
 * Added fishhook to Participant for substitutions.
 *
 * Revision 1.14  2001/11/17 20:35:47  rphall
 * Added primary key to Participant table (for EJB compatiblity).
 * Changed (rowing_id,member_id) to UNIQUE index on Participant table.
 * Fixed typo.
 *
 * Revision 1.13  2001/11/17 04:26:22  rphall
 * Removed Seat.member_id as foreign key into Participant.
 * Removed associated triggers ON CHILD INSERT/UPDATE for Seating.member_id.
 * Shortened name Participant.requested_position to Participant.requested.
 * Shortened Participant.initial_seating to initial_seat; ditto final.
 * Added Participant.initial_seat, Participant.final_seat as FK into Seat.
 * Added associated triggers ON PARENT DELETE SET NULL.
 * Shorted Participant.participant_state to Participant.attendance.
 * Changed valid values of attendance to 'PRESENT', 'NOSHOW', 'ABSENT'.
 *
 * Revision 1.12  2001/11/17 03:00:50  rphall
 * Changed primary key on Seat to just seat_id.
 * Created unique index on Seat from rowing_id, boat_id and seat_number.
 * Changed name of Participant.preliminary_seating_id to initial_seating.
 * Changed allowed participate_state values to match latest state diagram.
 * Changed Member.clra_status from CHAR(4) to CHAR(8).
 * Changed allowed value of Member.clra_status from PAID to CONTRACT.
 *
 * Revision 1.11  2001/11/17 02:12:24  rphall
 * Added CVS Id and Log tags
 *
 */

