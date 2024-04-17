CREATE OR REPLACE FUNCTION F_CS_PHANCONG_1(
    P_SCHEMA IN VARCHAR2,
    P_OBJECT IN VARCHAR2
)
RETURN VARCHAR2
AS
    USERNAME VARCHAR2(130);
    USERROLE NUMBER;
    PREDICATE VARCHAR2(1000);
    INP_MAHP VARCHAR2(50);
BEGIN
    USERNAME := SYS_CONTEXT('USERENV', 'SESSION_USER');
    
    IF USERNAME = 'C##QLK' THEN
        RETURN '';
    ELSE
        SELECT COUNT(*) INTO USERROLE FROM DBA_ROLE_PRIVS WHERE GRANTEE = USERNAME AND (GRANTED_ROLE = 'TKHOA');

        IF USERROLE > 0 THEN
            PREDICATE := 'MAHP IN (SELECT MAHP FROM HOCPHAN JOIN DONVI ON DONVI.MADV = HOCPHAN.MADV WHERE DONVI.TENDV = ''Van phong khoa'')';
        ELSE
             SELECT COUNT(*) INTO USERROLE FROM DBA_ROLE_PRIVS WHERE GRANTEE = USERNAME AND (GRANTED_ROLE = 'GVU');
             IF USERROLE > 0 THEN
                 PREDICATE := 'MAHP IN (SELECT MAHP FROM HOCPHAN JOIN DONVI ON DONVI.MADV = HOCPHAN.MADV WHERE DONVI.TENDV = ''Van phong khoa'')';
            ELSE
                SELECT COUNT(*) INTO USERROLE FROM DBA_ROLE_PRIVS WHERE GRANTEE = USERNAME AND (GRANTED_ROLE = 'TDV');
                IF USERROLE > 0 THEN
                    PREDICATE:= 'MAHP IN (SELECT MAHP FROM HOCPHAN JOIN DONVI ON DONVI.MADV = HOCPHAN.MADV WHERE DONVI.TRGDV = ''' || USERNAME || ''')';
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
        OBJECT_NAME      => 'PHANCONG',
        policy_name      => 'PHANCONG1_POLICY',
        function_schema  => 'C##QLK',
        policy_function  => 'F_CS_PHANCONG_1',
        statement_types  => 'UPDATE, INSERT, DELETE',
        update_check     => TRUE,
        enable           => TRUE);
END;
/

CREATE OR REPLACE FUNCTION F_CS_PHANCONG_2(
    P_SCHEMA IN VARCHAR2,
    P_OBJECT IN VARCHAR2
)
RETURN VARCHAR2
AS
    USERNAME VARCHAR2(130);
    USERROLE NUMBER;
    PREDICATE VARCHAR2(1000);
    INP_MAHP VARCHAR2(50);
BEGIN
    USERNAME := SYS_CONTEXT('USERENV', 'SESSION_USER');
    
    IF USERNAME = 'C##QLK' THEN
        RETURN '';
    ELSE
        SELECT COUNT(*) INTO USERROLE FROM DBA_ROLE_PRIVS WHERE GRANTEE = USERNAME AND (GRANTED_ROLE = 'GVU' OR GRANTED_ROLE = 'TKHOA' OR GRANTED_ROLE = 'SV');
        IF USERROLE > 0 THEN
            PREDICATE:= '1=1';
        ELSE
            SELECT COUNT(*) INTO USERROLE FROM DBA_ROLE_PRIVS WHERE GRANTEE = USERNAME AND (GRANTED_ROLE = 'GV');

            IF USERROLE > 0 THEN
                PREDICATE := 'MAGV = ''' || USERNAME || '''';
            ELSE
                SELECT COUNT(*) INTO USERROLE FROM DBA_ROLE_PRIVS WHERE GRANTEE = USERNAME AND (GRANTED_ROLE = 'TDV');
                IF USERROLE > 0 THEN
                    PREDICATE:= 'MAGV IN (SELECT MANV FROM NHANSU JOIN DONVI ON DONVI.MADV = NHANSU.MADV WHERE  DONVI.TRGDV = ''' || USERNAME || ''')';
                ELSE
                    PREDICATE := '1=0';
                END IF;
            END IF;
        END IF;
    END IF;
    RETURN PREDICATE;
END;
/
select * from nhansu;
BEGIN
    DBMS_RLS.ADD_POLICY(
        OBJECT_SCHEMA    => 'C##QLK',
        OBJECT_NAME      => 'PHANCONG',
        policy_name      => 'PHANCONG2_POLICY',
        function_schema  => 'C##QLK',
        policy_function  => 'F_CS_PHANCONG_2',
        statement_types  => 'SELECT',
        update_check     => TRUE,
        enable           => TRUE);
END;
/

CREATE OR REPLACE FUNCTION F_CS_PHANCONG_3(
    P_SCHEMA IN VARCHAR2,
    P_OBJECT IN VARCHAR2
)
RETURN VARCHAR2
AS
    USERNAME VARCHAR2(130);
    USERROLE NUMBER;
    PREDICATE VARCHAR2(1000);
    INP_MAHP VARCHAR2(50);
BEGIN
    USERNAME := SYS_CONTEXT('USERENV', 'SESSION_USER');
    
    IF USERNAME = 'C##QLK' THEN
        RETURN '';
    ELSE
        SELECT COUNT(*) INTO USERROLE FROM DBA_ROLE_PRIVS WHERE GRANTEE = USERNAME AND (GRANTED_ROLE = 'SV');
        IF USERROLE > 0 THEN
            PREDICATE:= '1=0';
        ELSE
           PREDICATE:= '1=1';
           END IF;
    END IF;
    RETURN PREDICATE;
END;
/
select * from nhansu;
BEGIN
    DBMS_RLS.ADD_POLICY(
        OBJECT_SCHEMA    => 'C##QLK',
        OBJECT_NAME      => 'PHANCONG',
        policy_name      => 'PHANCONG3_POLICY',
        function_schema  => 'C##QLK',
        policy_function  => 'F_CS_PHANCONG_3',
        statement_types  => 'SELECT',
        SEC_RELEVANT_COLS     => 'HK, NAM, MACT', 
        SEC_RELEVANT_COLS_OPT => DBMS_RLS.ALL_ROWS,
        update_check     => TRUE,
        enable           => TRUE);
END;
/

COMMIT;
GRANT SELECT, INSERT, UPDATE, DELETE ON PHANCONG TO TKHOA;
GRANT SELECT, INSERT, UPDATE, DELETE ON PHANCONG TO TDV;
GRANT SELECT, UPDATE ON PHANCONG TO GVU;
GRANT SELECT ON PHANCONG TO GV,SV;
select * from C##QLK.PHANCONG