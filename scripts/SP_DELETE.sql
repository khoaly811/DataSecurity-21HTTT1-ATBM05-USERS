ALTER PLUGGABLE DATABASE ATBM2024 OPEN READ WRITE;
SET SERVEROUTPUT ON;
--CREATE OR REPLACE PROCEDURE SP_DROP_USERS_LIKE(
--    pattern IN VARCHAR2
--)
--AS
--    CURSOR user_cursor IS
--        SELECT USERNAME
--        FROM ALL_USERS
--        WHERE USERNAME LIKE pattern;
--
--    user_name VARCHAR2(100);
--BEGIN
--    FOR user_rec IN user_cursor LOOP
--        user_name := user_rec.USERNAME;
--        EXECUTE IMMEDIATE 'DROP USER ' || user_name || ' CASCADE';
--        DBMS_OUTPUT.PUT_LINE('User ' || user_name || ' dropped successfully.');
--    END LOOP;
--EXCEPTION
--    WHEN OTHERS THEN
--        RAISE_APPLICATION_ERROR(-20001, 'Error dropping user: ' || SQLERRM);
--END;
--/
--BEGIN
--    SP_DROP_USERS_LIKE('NV%');
--    SP_DROP_USERS_LIKE('SV%');
--END;
--/

CREATE OR REPLACE PROCEDURE SP_GRANT_EXECUTE(
    INP_PROC IN VARCHAR2,
    INP_ROLE IN VARCHAR2
)
AS
    COUNT_ROLES INT;
BEGIN
    SELECT COUNT(*) INTO COUNT_ROLES FROM ROLE_TAB_PRIVS WHERE ROLE = INP_ROLE;

    IF COUNT_ROLES > 0 THEN
        EXECUTE IMMEDIATE 'GRANT EXECUTE ON ' || INP_PROC || ' TO ' || INP_ROLE;
        DBMS_OUTPUT.PUT_LINE('GRANT EXECUTE THANH CONG CHO ' || INP_ROLE);
    ELSE
        DBMS_OUTPUT.PUT_LINE('Role ' || INP_ROLE || ' KHONG CO DUOC EXECUTE ' || INP_PROC);
    END IF;
EXCEPTION
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('Error: ' || SQLERRM);
END;
/
BEGIN
    SP_GRANT_EXECUTE('SP_VIEW_TABLE', 'COMBINED_ROLE');
    SP_GRANT_EXECUTE('SP_ALL_UPDATE_NHANSU', 'COMBINED_ROLE');
END;
/
-- TONG HOP SP DELETE
CREATE OR REPLACE PROCEDURE SP_DELETE_NHANSU(
    
    INP_MANV IN CHAR
)AUTHID CURRENT_USER AS
BEGIN
    DELETE FROM C##QLK.NHANSU ns WHERE ns.MANV = INP_MANV;
    COMMIT;
END;
/
BEGIN
    SP_GRANT_EXECUTE('SP_DELETE_NHANSU', 'TKHOA');
    SP_GRANT_EXECUTE('SP_DELETE_NHANSU', 'TDV');
    SP_GRANT_EXECUTE('SP_DELETE_NHANSU', 'GV');
    SP_GRANT_EXECUTE('SP_DELETE_NHANSU', 'GVU');
    SP_GRANT_EXECUTE('SP_DELETE_NHANSU', 'NVCB');
    SP_GRANT_EXECUTE('SP_DELETE_NHANSU', 'SV');
END;
/
CREATE OR REPLACE PROCEDURE SP_DELETE_PHANCONG(
    OLD_TENGV IN VARCHAR2,
    INP_TENHP IN VARCHAR2,
    INP_HK IN NUMBER,
    INP_NAM IN NUMBER,
    INP_MACT IN VARCHAR2
)
AUTHID CURRENT_USER
AS
    V_MAGV_OLD VARCHAR2(20);
    V_MAHP INT;
BEGIN
    SELECT MANV INTO V_MAGV_OLD FROM C##QLK.NHANSU WHERE HOTEN = OLD_TENGV;
    SELECT MAHP INTO V_MAHP FROM C##QLK.HOCPHAN WHERE TENHP = INP_TENHP;
    
    DELETE FROM C##QLK.PHANCONG pc WHERE 
        pc.MAGV = V_MAGV_OLD AND
        pc.MAHP = V_MAHP AND
        pc.HK = INP_HK AND
        pc.NAM = INP_NAM AND
        pc.MACT = INP_MACT;
        COMMIT;
END;
/


BEGIN
    SP_GRANT_EXECUTE('SP_DELETE_PHANCONG', 'TKHOA');
    SP_GRANT_EXECUTE('SP_DELETE_PHANCONG', 'TDV');
    SP_GRANT_EXECUTE('SP_DELETE_PHANCONG', 'GV');
    SP_GRANT_EXECUTE('SP_DELETE_PHANCONG', 'GVU');
    SP_GRANT_EXECUTE('SP_DELETE_PHANCONG', 'NVCB');
    SP_GRANT_EXECUTE('SP_DELETE_PHANCONG', 'SV');
END;
/
--begin
--    SP_DELETE_PHANCONG('Tran Huu Chien','HOC PHAN 4',3,2022,'CQ');
--    end;
--    /
    select * from PHANCONG;
--    DELETE FROM C##QLK.PHANCONG pc WHERE 
--        pc.MAGV = 'Chien91' AND
--        pc.MAHP = 33 AND
--        pc.HK = 3 AND
--        pc.NAM = 2022 AND
--        pc.MACT = 'CQ';
--        rollback;
CREATE OR REPLACE PROCEDURE SP_DELETE_DANGKY(
    INP_SV IN CHAR,
    INP_MAGV IN CHAR,
    INP_MAHP IN CHAR,
    INP_HK IN NUMBER,
    INP_NAM IN NUMBER,
    INP_MACT IN CHAR
)AUTHID CURRENT_USER AS
BEGIN

    DELETE FROM C##QLK.DANGKY pc WHERE pc.MAGV = INP_MAGV and
    pc.MAHP = INP_MAHP and
    pc.HK = INP_HK and
    pc.NAM = INP_NAM and
    pc.MASV = INP_SV and
    pc.MACT = INP_MACT;
   

END;
/

BEGIN
    SP_GRANT_EXECUTE('SP_DELETE_DANGKY', 'TKHOA');
    SP_GRANT_EXECUTE('SP_DELETE_DANGKY', 'TDV');
    SP_GRANT_EXECUTE('SP_DELETE_DANGKY', 'GV');
    SP_GRANT_EXECUTE('SP_DELETE_DANGKY', 'GVU');
    SP_GRANT_EXECUTE('SP_DELETE_DANGKY', 'NVCB');
    SP_GRANT_EXECUTE('SP_DELETE_DANGKY', 'SV');
END;
/

