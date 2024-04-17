--select * from HOCPHAN;
--select * from KHMO;
--select * from SINHVIEN;
--select * from Nhansu;
--select * from ALL_POLICIES WHERE OBJECT_OWNER  = 'C##QLK';
--select * from PHANCONG;
--select * from DONVI;
--select MAHP  from HOCPHAN join DONVI on DONVI.MADV = HOCPHAN.MADV where DONVI.TENDV = 'Van phong khoa';


CREATE OR REPLACE FUNCTION F_CS_HOCPHAN(
    P_SCHEMA IN VARCHAR2,
    P_OBJECT IN VARCHAR2
)
RETURN VARCHAR2
AS
    USERNAME VARCHAR2(130);
    USERROLE VARCHAR2(130);
    PREDICATE   VARCHAR2(1000); 
BEGIN
    USERNAME := SYS_CONTEXT('USERENV', 'SESSION_USER');
    
    IF USERNAME = 'C##QLK' THEN
        RETURN '';
    ELSE
        SELECT COUNT(*) INTO USERROLE FROM DBA_ROLE_PRIVS WHERE GRANTEE = USERNAME AND (GRANTED_ROLE = 'TKHOA' or GRANTED_ROLE = 'NVCB' or GRANTED_ROLE = 'GV' 
        or GRANTED_ROLE = 'TDV' or GRANTED_ROLE = 'GVU'  or GRANTED_ROLE = 'SV');

        IF USERROLE > 0 THEN
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
        OBJECT_SCHEMA    => 'C##QLK',
        OBJECT_NAME      => 'HOCPHAN',
        policy_name      => 'HOCPHAN_POLICY',
        function_schema  => 'C##QLK',
        policy_function  => 'F_CS_HOCPHAN',
        statement_types  => 'SELECT, UPDATE, INSERT, DELETE',
        update_check     => TRUE,
        enable           => TRUE);
END;
/
grant select on HOCPHAN to NVCB,GV,GVU,TDV,TKHOA,SV;
grant insert, update on HOCPHAN to GVU;
commit;


CREATE OR REPLACE FUNCTION F_CS_KHMO(
    P_SCHEMA IN VARCHAR2,
    P_OBJECT IN VARCHAR2
)
RETURN VARCHAR2
AS
    USERNAME VARCHAR2(130);
    USERROLE NUMBER;
    PREDICATE   VARCHAR2(1000);
    MACTSV VARCHAR2(50);
BEGIN
    USERNAME := SYS_CONTEXT('USERENV', 'SESSION_USER');
    
    IF USERNAME = 'C##QLK' THEN
        RETURN '';
    ELSE
        SELECT COUNT(*) INTO USERROLE FROM DBA_ROLE_PRIVS WHERE GRANTEE = USERNAME AND (GRANTED_ROLE = 'TKHOA' or GRANTED_ROLE = 'NVCB' or GRANTED_ROLE = 'GV' 
        or GRANTED_ROLE = 'TDV' or GRANTED_ROLE = 'GVU');

        IF USERROLE > 0 THEN
            PREDICATE := '1=1';
        ELSE
            SELECT COUNT(*) INTO USERROLE FROM DBA_ROLE_PRIVS WHERE GRANTEE = USERNAME AND (GRANTED_ROLE = 'SV');

            IF USERROLE > 0 THEN
            select mact into MACTSV from C##QLK.SINHVIEN where upper(MASV) = USERNAME;
                IF MACTSV = 'CQ' then
                  PREDICATE := 'MACT = ''CQ''';
                ELSIF MACTSV = 'CLC' then
                  PREDICATE := 'MACT = ''CLC''';
                  ELSIF MACTSV = 'CTTT' then
                  PREDICATE := 'MACT = ''CTTT''';
                  ELSIF MACTSV = 'VP' then
                  PREDICATE := 'MACT = ''VP''';                  
                END IF;
            ELSE
                PREDICATE := '1=0'; 
            END IF;
        END IF;
    END IF;

    RETURN PREDICATE;
END;
/

BEGIN
    DBMS_RLS.ADD_POLICY(
        OBJECT_SCHEMA    => 'C##QLK',
        OBJECT_NAME      => 'KHMO',
        policy_name      => 'KHMO_POLICY',
        function_schema  => 'C##QLK',
        policy_function  => 'F_CS_KHMO',
        statement_types  => 'SELECT, UPDATE, INSERT, DELETE',
        update_check     => TRUE,
        enable           => TRUE);
END;
/
grant select on KHMO to NVCB,GV,GVU,TDV,TKHOA,SV;
grant insert, update on KHMO to GVU;