CREATE OR REPLACE FUNCTION F_CS_NHANSU(
    P_SCHEMA IN VARCHAR2,
    P_OBJECT IN VARCHAR2
)
RETURN VARCHAR2
AS
    USERNAME VARCHAR2(130);
    USERROLE NUMBER;
    PREDICATE   VARCHAR2(1000); 
BEGIN
    USERNAME := SYS_CONTEXT('USERENV', 'SESSION_USER');
    
    IF USERNAME = 'C##QLK' THEN
        RETURN '';
    ELSE
        SELECT COUNT(*) INTO USERROLE FROM DBA_ROLE_PRIVS WHERE GRANTEE = USERNAME AND (GRANTED_ROLE = 'TKHOA' OR GRANTED_ROLE = 'GVU'  OR GRANTED_ROLE = 'SV');

        IF USERROLE > 0 THEN
            RETURN '';
        ELSE
            SELECT COUNT(*) INTO USERROLE FROM DBA_ROLE_PRIVS WHERE GRANTEE = USERNAME AND (GRANTED_ROLE = 'NVCB' OR GRANTED_ROLE = 'GV' );

            IF USERROLE > 0 THEN
                PREDICATE := 'UPPER(MANV) = ''' || USERNAME || '''';
            ELSE
                 SELECT COUNT(*) INTO USERROLE FROM DBA_ROLE_PRIVS WHERE GRANTEE = USERNAME AND (GRANTED_ROLE = 'TDV');
                 IF USERROLE > 0 THEN
                   PREDICATE := 'MADV = (select MADV from DONVI where upper(TRGDV) = ''' || USERNAME || ''')';
                 ELSE
                    PREDICATE := '1=0'; 
                END IF;
            END IF;
        END IF;
    END IF;

    RETURN PREDICATE;
END;
/

BEGIN
    DBMS_RLS.ADD_POLICY(
        OBJECT_SCHEMA    => 'C##QLK',
        OBJECT_NAME      => 'NHANSU',
        policy_name      => 'NHANSU_POLICY',
        function_schema  => 'C##QLK',
        policy_function  => 'F_CS_NHANSU',
        statement_types  => 'SELECT, UPDATE, INSERT, DELETE',
        update_check     => TRUE,
        enable           => TRUE);
END;
/
CREATE OR REPLACE FUNCTION F_CS_NHANSU_4(
    P_SCHEMA IN VARCHAR2,
    P_OBJECT IN VARCHAR2
)
RETURN VARCHAR2
AS
    USERNAME VARCHAR2(130);
    USERROLE NUMBER;
    PREDICATE   VARCHAR2(1000); 
BEGIN
    USERNAME := SYS_CONTEXT('USERENV', 'SESSION_USER');
    
    IF USERNAME = 'C##QLK' THEN
        RETURN '';
    ELSE
        SELECT COUNT(*) INTO USERROLE FROM DBA_ROLE_PRIVS WHERE GRANTEE = USERNAME AND (GRANTED_ROLE = 'GVU' OR GRANTED_ROLE = 'TDV' );
        IF USERROLE > 0 THEN
            PREDICATE := 'UPPER(MANV) = ''' || USERNAME || '''';
        ELSE
            PREDICATE := '1=1'; 
        END IF;
    END IF;

    RETURN PREDICATE;
END;
/
BEGIN
    DBMS_RLS.ADD_POLICY(
        OBJECT_SCHEMA    => 'C##QLK',
        OBJECT_NAME      => 'NHANSU',
        policy_name      => 'NHANSU_POLICY_4',
        function_schema  => 'C##QLK',
        policy_function  => 'F_CS_NHANSU_4',
        statement_types  => 'UPDATE',
        update_check     => TRUE,
        enable           => TRUE);
END;
/
CREATE OR REPLACE FUNCTION F_CS_NHANSU_2(
    P_SCHEMA IN VARCHAR2,
    P_OBJECT IN VARCHAR2
)
RETURN VARCHAR2
AS
    USERNAME VARCHAR2(130);
    USERROLE NUMBER;
    PREDICATE VARCHAR2(1000); 
BEGIN
    USERNAME := SYS_CONTEXT('USERENV', 'SESSION_USER');
    
    IF USERNAME = 'C##QLK' THEN
        RETURN '';
    ELSE
        SELECT COUNT(*) INTO USERROLE FROM DBA_ROLE_PRIVS WHERE GRANTEE = USERNAME AND GRANTED_ROLE = 'TDV';

        IF USERROLE > 0 THEN
            PREDICATE := 'UPPER(MANV) = ''' || USERNAME || '''';
        ELSE
            PREDICATE := '';
        END IF;
    END IF;

    RETURN PREDICATE;
END;
/


BEGIN
    DBMS_RLS.ADD_POLICY(
        OBJECT_SCHEMA         => 'C##QLK',
        OBJECT_NAME           => 'NHANSU',
        POLICY_NAME           => 'NHANSU_POLICY_2',
        FUNCTION_SCHEMA       => 'C##QLK',
        POLICY_FUNCTION       => 'F_CS_NHANSU_2',
        STATEMENT_TYPES       => 'SELECT',
        SEC_RELEVANT_COLS     => 'PHUCAP', 
        SEC_RELEVANT_COLS_OPT => DBMS_RLS.ALL_ROWS,
        UPDATE_CHECK          => TRUE,
        ENABLE                => TRUE);
END;
/
CREATE OR REPLACE FUNCTION F_CS_NHANSU_3(
    P_SCHEMA IN VARCHAR2,
    P_OBJECT IN VARCHAR2
)
RETURN VARCHAR2
AS
    USERNAME VARCHAR2(130);
    USERROLE NUMBER;
    PREDICATE VARCHAR2(1000); 
BEGIN
    USERNAME := SYS_CONTEXT('USERENV', 'SESSION_USER');
    
    IF USERNAME = 'C##QLK' THEN
        RETURN '';
    ELSE
        SELECT COUNT(*) INTO USERROLE FROM DBA_ROLE_PRIVS WHERE GRANTEE = USERNAME AND GRANTED_ROLE = 'GVU';

        IF USERROLE > 0 THEN
            PREDICATE := 'UPPER(MANV) = ''' || USERNAME || '''';
        ELSE
            PREDICATE := '';
        END IF;
    END IF;

    RETURN PREDICATE;
END;
/
select * from nhansu;

BEGIN
    DBMS_RLS.ADD_POLICY(
        OBJECT_SCHEMA         => 'C##QLK',
        OBJECT_NAME           => 'NHANSU',
        POLICY_NAME           => 'NHANSU_POLICY_3',
        FUNCTION_SCHEMA       => 'C##QLK',
        POLICY_FUNCTION       => 'F_CS_NHANSU_3',
        STATEMENT_TYPES       => 'SELECT',
        SEC_RELEVANT_COLS     => 'PHUCAP, PHAI, NGSINH, DT', 
        SEC_RELEVANT_COLS_OPT => DBMS_RLS.ALL_ROWS,
        UPDATE_CHECK          => TRUE,
        ENABLE                => TRUE);
END;
/

CREATE OR REPLACE FUNCTION F_CS_NHANSU_5(
    P_SCHEMA IN VARCHAR2,
    P_OBJECT IN VARCHAR2
)
RETURN VARCHAR2
AS
    USERNAME VARCHAR2(130);
    USERROLE NUMBER;
    PREDICATE VARCHAR2(1000); 
BEGIN
    USERNAME := SYS_CONTEXT('USERENV', 'SESSION_USER');
    
    IF USERNAME = 'C##QLK' THEN
        RETURN '';
    ELSE
        SELECT COUNT(*) INTO USERROLE FROM DBA_ROLE_PRIVS WHERE GRANTEE = USERNAME AND GRANTED_ROLE = 'SV';

        IF USERROLE > 0 THEN
            PREDICATE := '1=0';
        ELSE
            PREDICATE := '';
        END IF;
    END IF;

    RETURN PREDICATE;
END;
/
select * from nhansu;

BEGIN
    DBMS_RLS.ADD_POLICY(
        OBJECT_SCHEMA         => 'C##QLK',
        OBJECT_NAME           => 'NHANSU',
        POLICY_NAME           => 'NHANSU_POLICY_5',
        FUNCTION_SCHEMA       => 'C##QLK',
        POLICY_FUNCTION       => 'F_CS_NHANSU_5',
        STATEMENT_TYPES       => 'SELECT',
        SEC_RELEVANT_COLS     => 'PHUCAP, PHAI, NGSINH, DT,VAITRO,MADV', 
        SEC_RELEVANT_COLS_OPT => DBMS_RLS.ALL_ROWS,
        UPDATE_CHECK          => TRUE,
        ENABLE                => TRUE);
END;
/


SELECT * FROM C##QLK.NHANSU where MADV = (select MADV from DONVI where upper(TRGDV) ='TUAN101');
GRANT SELECT ON NHANSU TO NVCB,GV,GVU,TDV,SV;
GRANT UPDATE(DT) ON NHANSU TO NVCB,GV,GVU,TDV;
GRANT SELECT, UPDATE, INSERT, DELETE ON NHANSU TO TKHOA;
COMMIT;

