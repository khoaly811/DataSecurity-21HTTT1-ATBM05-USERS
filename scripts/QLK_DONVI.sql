CREATE OR REPLACE FUNCTION  F_CS_DONVI(
    P_SCHEMA IN VARCHAR2,
    P_OBJECT IN VARCHAR2
)
RETURN VARCHAR2
AS
    USERNAME VARCHAR(130);
    USERROLE NUMBER;
    PREDICATE VARCHAR2(1000);
BEGIN
    USERNAME := SYS_CONTEXT('USERENV', 'SESSION_USER');
    
    IF USERNAME = 'C##QLK' THEN
        RETURN '';
    ELSE
        SELECT COUNT(*) INTO USERROLE FROM DBA_ROLE_PRIVS WHERE GRANTEE = USERNAME AND (GRANTED_ROLE = 'NVCB' OR GRANTED_ROLE = 'GV'
        OR GRANTED_ROLE = 'GVU' OR GRANTED_ROLE = 'TDV' OR GRANTED_ROLE = 'TKHOA' OR GRANTED_ROLE = 'SV');
        
        IF USERROLE >0 THEN
            PREDICATE := '1=1';
        ELSE 
            PREDICATE := '1=0';
        END IF;
    END IF;
    RETURN PREDICATE;
END;
/
BEGIN
    DBMS_RLS.ADD_POLICY(
        OBJECT_SCHEMA => 'C##QLK',
        OBJECT_NAME => 'DONVI',
        POLICY_NAME => 'DONVI_POLICY',
        FUNCTION_SCHEMA => 'C##QLK',
        POLICY_FUNCTION => 'F_CS_DONVI',
        STATEMENT_TYPES => 'SELECT, INSERT, UPDATE',
        UPDATE_CHECK => TRUE,
        ENABLE => TRUE);
END;
/
GRANT INSERT, UPDATE ON C##QLK.DONVI TO GVU;
GRANT SELECT ON C##QLK.DONVI TO NVCB, GVU, GV, TDV, SV;
GRANT SELECT ON C##QLK.DONVI TO TKHOA;
