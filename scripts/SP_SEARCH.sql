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
-- TONG HOP PROC SEARCH
CREATE OR REPLACE PROCEDURE SP_SEARCH_NHANSU(
    RESULT_CURSOR OUT SYS_REFCURSOR,
    INP_HOTEN VARCHAR2
)
AS
    TEMP_COUNT INT;
    QUERY_STRING VARCHAR2(1000);
    SEARCH_PATTERN VARCHAR2(255);
BEGIN
    SELECT COUNT(*) INTO TEMP_COUNT FROM ALL_TABLES WHERE TABLE_NAME = 'NHANSU';
    
    IF (TEMP_COUNT = 0) THEN
        RAISE_APPLICATION_ERROR(-20000, 'THONG TIN KHONG TON TAI!');
    ELSE
        -- Construct the search pattern for partial match
        SEARCH_PATTERN := '%' || INP_HOTEN || '%';
        
        QUERY_STRING := 'SELECT HOTEN, PHAI, NGSINH, PHUCAP, DT, VAITRO, TENDV, MANV 
                 FROM C##QLK.NHANSU NS 
                 JOIN C##QLK.DONVI DV ON NS.MADV = DV.MADV 
                 WHERE HOTEN LIKE ''%' || INP_HOTEN || '%''';

        
        OPEN RESULT_CURSOR FOR QUERY_STRING;
    END IF;
END;
/

BEGIN
    SP_GRANT_EXECUTE('SP_SEARCH_NHANSU', 'TKHOA');
    SP_GRANT_EXECUTE('SP_SEARCH_NHANSU', 'TDV');
    SP_GRANT_EXECUTE('SP_SEARCH_NHANSU', 'GV');
    SP_GRANT_EXECUTE('SP_SEARCH_NHANSU', 'GVU');
    SP_GRANT_EXECUTE('SP_SEARCH_NHANSU', 'NVCB');
    SP_GRANT_EXECUTE('SP_SEARCH_NHANSU', 'SV');
END;
/

CREATE OR REPLACE PROCEDURE SP_SEARCH_PHANCONG(
    RESULT_CURSOR OUT SYS_REFCURSOR,
    INP_HOTEN VARCHAR2
)
AS
    TEMP_COUNT INT;
    QUERY_STRING VARCHAR2(1000);
    SEARCH_PATTERN VARCHAR2(255);
BEGIN
    SELECT COUNT(*) INTO TEMP_COUNT FROM ALL_TABLES WHERE TABLE_NAME = 'PHANCONG';
    
    IF (TEMP_COUNT = 0) THEN
        RAISE_APPLICATION_ERROR(-20000, 'THONG TIN KHONG TON TAI!');
    ELSE
        -- Construct the search pattern for partial match
        SEARCH_PATTERN := '%' || INP_HOTEN || '%';
        
        QUERY_STRING := 'SELECT HOTEN, TENHP, HK, NAM, MACT, PC.MAHP, MAGV
        FROM C##QLK.PHANCONG PC JOIN C##QLK.NHANSU NS ON PC.MAGV = NS.MANV
        JOIN C##QLK.HOCPHAN HP ON HP.MAHP = PC.MAHP
                         WHERE HOTEN LIKE ''%' || INP_HOTEN || '%''';
        
        OPEN RESULT_CURSOR FOR QUERY_STRING;
    END IF;
END;
/

BEGIN
    SP_GRANT_EXECUTE('SP_SEARCH_PHANCONG', 'TKHOA');
    SP_GRANT_EXECUTE('SP_SEARCH_PHANCONG', 'TDV');
    SP_GRANT_EXECUTE('SP_SEARCH_PHANCONG', 'GV');
    SP_GRANT_EXECUTE('SP_SEARCH_PHANCONG', 'GVU');
    SP_GRANT_EXECUTE('SP_SEARCH_PHANCONG', 'NVCB');
    SP_GRANT_EXECUTE('SP_SEARCH_PHANCONG', 'SV');
END;
/
--
--SELECT HOTEN, PHAI, NGSINH, PHUCAP, DT, VAITRO, TENDV, MANV 
--                 FROM C##QLK.NHANSU NS 
--                 JOIN C##QLK.DONVI DV ON NS.MADV = DV.MADV 
--                 WHERE HOTEN LIKE '%Chau%';

CREATE OR REPLACE PROCEDURE SP_SEARCH_SINHVIEN(
    RESULT_CURSOR OUT SYS_REFCURSOR,
    INP_HOTEN VARCHAR2
)
AS
    TEMP_COUNT INT;
    QUERY_STRING VARCHAR2(1000);
    SEARCH_PATTERN VARCHAR2(255);
BEGIN
    SELECT COUNT(*) INTO TEMP_COUNT FROM ALL_TABLES WHERE TABLE_NAME = 'SINHVIEN';
    
    IF (TEMP_COUNT = 0) THEN
        RAISE_APPLICATION_ERROR(-20000, 'THONG TIN KHONG TON TAI!');
    ELSE
        -- Construct the search pattern for partial match
        SEARCH_PATTERN := '%' || INP_HOTEN || '%';
        
        QUERY_STRING := 'SELECT HOTEN, MACT, MANGANH, PHAI, NGSINH, DCHI, DT, SOTCTL, DTBTL, MASV
        FROM C##QLK.SINHVIEN
                         WHERE HOTEN LIKE ''%' || INP_HOTEN || '%''';
        
        OPEN RESULT_CURSOR FOR QUERY_STRING;
    END IF;
END;
/

BEGIN
    SP_GRANT_EXECUTE('SP_SEARCH_SINHVIEN', 'TKHOA');
    SP_GRANT_EXECUTE('SP_SEARCH_SINHVIEN', 'TDV');
    SP_GRANT_EXECUTE('SP_SEARCH_SINHVIEN', 'GV');
    SP_GRANT_EXECUTE('SP_SEARCH_SINHVIEN', 'GVU');
    SP_GRANT_EXECUTE('SP_SEARCH_SINHVIEN', 'NVCB');
    SP_GRANT_EXECUTE('SP_SEARCH_SINHVIEN', 'SV');
END;
/