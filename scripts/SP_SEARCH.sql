--ALTER PLUGGABLE DATABASE ATBM2024 OPEN READ WRITE;
SET SERVEROUTPUT ON;
--CREATE OR REPLACE PROCEDURE C##QLK.SP_DROP_USERS_LIKE(
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
--    C##QLK.SP_DROP_USERS_LIKE('NV%');
--    C##QLK.SP_DROP_USERS_LIKE('SV%');
--END;
--/

CREATE OR REPLACE PROCEDURE C##QLK.SP_GRANT_EXECUTE(
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
    C##QLK.SP_GRANT_EXECUTE('C##QLK.SP_VIEW_TABLE', 'COMBINED_ROLE');
    C##QLK.SP_GRANT_EXECUTE('C##QLK.SP_ALL_UPDATE_NHANSU', 'COMBINED_ROLE');
END;
/
-- TONG HOP PROC SEARCH
CREATE OR REPLACE PROCEDURE C##QLK.SP_SEARCH_NHANSU(
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
    C##QLK.SP_GRANT_EXECUTE('C##QLK.SP_SEARCH_NHANSU', 'TKHOA');
    C##QLK.SP_GRANT_EXECUTE('C##QLK.SP_SEARCH_NHANSU', 'TDV');
    C##QLK.SP_GRANT_EXECUTE('C##QLK.SP_SEARCH_NHANSU', 'GV');
    C##QLK.SP_GRANT_EXECUTE('C##QLK.SP_SEARCH_NHANSU', 'GVU');
    C##QLK.SP_GRANT_EXECUTE('C##QLK.SP_SEARCH_NHANSU', 'NVCB');
    C##QLK.SP_GRANT_EXECUTE('C##QLK.SP_SEARCH_NHANSU', 'SV');
END;
/

CREATE OR REPLACE PROCEDURE C##QLK.SP_SEARCH_PHANCONG(
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
    C##QLK.SP_GRANT_EXECUTE('C##QLK.SP_SEARCH_PHANCONG', 'TKHOA');
    C##QLK.SP_GRANT_EXECUTE('C##QLK.SP_SEARCH_PHANCONG', 'TDV');
    C##QLK.SP_GRANT_EXECUTE('C##QLK.SP_SEARCH_PHANCONG', 'GV');
    C##QLK.SP_GRANT_EXECUTE('C##QLK.SP_SEARCH_PHANCONG', 'GVU');
    C##QLK.SP_GRANT_EXECUTE('C##QLK.SP_SEARCH_PHANCONG', 'NVCB');
    C##QLK.SP_GRANT_EXECUTE('C##QLK.SP_SEARCH_PHANCONG', 'SV');
END;
/
--
--SELECT HOTEN, PHAI, NGSINH, PHUCAP, DT, VAITRO, TENDV, MANV 
--                 FROM C##QLK.NHANSU NS 
--                 JOIN C##QLK.DONVI DV ON NS.MADV = DV.MADV 
--                 WHERE HOTEN LIKE '%Chau%';

CREATE OR REPLACE PROCEDURE C##QLK.SP_SEARCH_SINHVIEN(
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
    C##QLK.SP_GRANT_EXECUTE('C##QLK.SP_SEARCH_SINHVIEN', 'TKHOA');
    C##QLK.SP_GRANT_EXECUTE('C##QLK.SP_SEARCH_SINHVIEN', 'TDV');
    C##QLK.SP_GRANT_EXECUTE('C##QLK.SP_SEARCH_SINHVIEN', 'GV');
    C##QLK.SP_GRANT_EXECUTE('C##QLK.SP_SEARCH_SINHVIEN', 'GVU');
    C##QLK.SP_GRANT_EXECUTE('C##QLK.SP_SEARCH_SINHVIEN', 'NVCB');
    C##QLK.SP_GRANT_EXECUTE('C##QLK.SP_SEARCH_SINHVIEN', 'SV');
END;
/
select * from C##QLK.NHANSU;


CREATE OR REPLACE PROCEDURE C##QLK.SP_SEARCH_DANGKY(
    RESULT_CURSOR OUT SYS_REFCURSOR,
    INP_HOTEN VARCHAR2
)
AS
    TEMP_COUNT INT;
    QUERY_STRING VARCHAR2(1000);
    SEARCH_PATTERN VARCHAR2(255);
BEGIN
    SELECT COUNT(*) INTO TEMP_COUNT FROM ALL_TABLES WHERE TABLE_NAME = 'DANGKY';
    
    IF (TEMP_COUNT = 0) THEN
        RAISE_APPLICATION_ERROR(-20000, 'THONG TIN KHONG TON TAI!');
    ELSE
        -- Construct the search pattern for partial match
        SEARCH_PATTERN := '%' || INP_HOTEN || '%';
        
        QUERY_STRING := 'SELECT SV.HOTEN AS HOTEN_SV, NS.HOTEN AS HOTEN_GV, HP.TENHP, HK, NAM, DK.MACT, DIEMTH, DIEMQT, DIEMCK, DIEMTK, DK.MASV, DK.MAGV
        FROM C##QLK.DANGKY DK JOIN C##QLK.SINHVIEN SV ON DK.MASV=SV.MASV JOIN
        C##QLK.NHANSU NS ON NS.MANV = DK.MAGV JOIN C##QLK.HOCPHAN HP ON
        HP.MAHP = DK.MAHP
                         WHERE NS.HOTEN LIKE ''%' || INP_HOTEN || '%'' or SV.HOTEN LIKE ''%' || INP_HOTEN || '%''';
        
        OPEN RESULT_CURSOR FOR QUERY_STRING;
    END IF;
END;
/

BEGIN
    C##QLK.SP_GRANT_EXECUTE('C##QLK.SP_SEARCH_DANGKY', 'TKHOA');
    C##QLK.SP_GRANT_EXECUTE('C##QLK.SP_SEARCH_DANGKY', 'TDV');
    C##QLK.SP_GRANT_EXECUTE('C##QLK.SP_SEARCH_DANGKY', 'GV');
    C##QLK.SP_GRANT_EXECUTE('C##QLK.SP_SEARCH_DANGKY', 'GVU');
    C##QLK.SP_GRANT_EXECUTE('C##QLK.SP_SEARCH_DANGKY', 'NVCB');
    C##QLK.SP_GRANT_EXECUTE('C##QLK.SP_SEARCH_DANGKY', 'SV');
END;
/

CREATE OR REPLACE PROCEDURE C##QLK.SP_SEARCH_HOCPHAN(
    RESULT_CURSOR OUT SYS_REFCURSOR,
    INP_TENHP VARCHAR2
)
AS
    TEMP_COUNT INT;
    QUERY_STRING VARCHAR2(1000);
    SEARCH_PATTERN VARCHAR2(255);
BEGIN
    SELECT COUNT(*) INTO TEMP_COUNT FROM ALL_TABLES WHERE TABLE_NAME = 'HOCPHAN';
    
    IF (TEMP_COUNT = 0) THEN
        RAISE_APPLICATION_ERROR(-20000, 'THONG TIN KHONG TON TAI!');
    ELSE
        -- Construct the search pattern for partial match
        SEARCH_PATTERN := '%' || INP_TENHP || '%';
        
        QUERY_STRING := 'SELECT HP.TENHP, HP.SOTC, HP.STLT, HP.STTH, HP.SOSVTD, DV.TENDV 
                         FROM C##QLK.HOCPHAN HP JOIN C##QLK.DONVI DV ON DV.MADV = HP.MADV
                         WHERE HP.TENHP LIKE ''%' || INP_TENHP  || '%''';
        
        OPEN RESULT_CURSOR FOR QUERY_STRING;
    END IF;
END;
/
BEGIN
    C##QLK.SP_GRANT_EXECUTE('C##QLK.SP_SEARCH_HOCPHAN', 'TKHOA');
    C##QLK.SP_GRANT_EXECUTE('C##QLK.SP_SEARCH_HOCPHAN', 'TDV');
    C##QLK.SP_GRANT_EXECUTE('C##QLK.SP_SEARCH_HOCPHAN', 'GV');
    C##QLK.SP_GRANT_EXECUTE('C##QLK.SP_SEARCH_HOCPHAN', 'GVU');
    C##QLK.SP_GRANT_EXECUTE('C##QLK.SP_SEARCH_HOCPHAN', 'NVCB');
    C##QLK.SP_GRANT_EXECUTE('C##QLK.SP_SEARCH_HOCPHAN', 'SV');
END;
/

CREATE OR REPLACE PROCEDURE C##QLK.SP_SEARCH_KHMO(
    RESULT_CURSOR OUT SYS_REFCURSOR,
    INP_TENHP VARCHAR2
)
AS
    TEMP_COUNT INT;
    QUERY_STRING VARCHAR2(1000);
    SEARCH_PATTERN VARCHAR2(255);
BEGIN
    SELECT COUNT(*) INTO TEMP_COUNT FROM ALL_TABLES WHERE TABLE_NAME = 'KHMO';
    
    IF (TEMP_COUNT = 0) THEN
        RAISE_APPLICATION_ERROR(-20000, 'THONG TIN KHONG TON TAI!');
    ELSE
        -- Construct the search pattern for partial match
        SEARCH_PATTERN := '%' || INP_TENHP || '%';
        
        QUERY_STRING := 'SELECT HP.TENHP, KH.HK, KH.NAM, KH.MACT, KH.NGAYBD 
                         FROM C##QLK.KHMO KH JOIN C##QLK.HOCPHAN ON KH.MAHP = HP.MAHP 
                         WHERE HP.TENHP LIKE ''%' || INP_TENHP || '%''';
        
        OPEN RESULT_CURSOR FOR QUERY_STRING;
    END IF;
END;
/
BEGIN
    C##QLK.SP_GRANT_EXECUTE('C##QLK.SP_SEARCH_KHMO', 'TKHOA');
    C##QLK.SP_GRANT_EXECUTE('C##QLK.SP_SEARCH_KHMO', 'TDV');
    C##QLK.SP_GRANT_EXECUTE('C##QLK.SP_SEARCH_KHMO', 'GV');
    C##QLK.SP_GRANT_EXECUTE('C##QLK.SP_SEARCH_KHMO', 'GVU');
    C##QLK.SP_GRANT_EXECUTE('C##QLK.SP_SEARCH_KHMO', 'NVCB');
    C##QLK.SP_GRANT_EXECUTE('C##QLK.SP_SEARCH_KHMO', 'SV');
END;
/

CREATE OR REPLACE PROCEDURE C##QLK.SP_SEARCH_DONVI(
    RESULT_CURSOR OUT SYS_REFCURSOR,
    INP_TENDV VARCHAR2
)
AS
    TEMP_COUNT INT;
    QUERY_STRING VARCHAR2(1000);
    SEARCH_PATTERN VARCHAR2(255);
BEGIN
    SELECT COUNT(*) INTO TEMP_COUNT FROM ALL_TABLES WHERE TABLE_NAME = 'DONVI';
    
    IF (TEMP_COUNT = 0) THEN
        RAISE_APPLICATION_ERROR(-20000, 'THONG TIN KHONG TON TAI!');
    ELSE
        -- Construct the search pattern for partial match
        SEARCH_PATTERN := '%' || INP_TENDV || '%';
        
        QUERY_STRING := 'SELECT DV.TENDV, NS.HOTEN FROM C##QLK.DONVI DV JOIN C##QLK.NHANSU NS ON NS.MANS = DV.TRUONGDV 
                         WHERE DV.TENDV LIKE ''%' || INP_TENDV || '%''';
        
        OPEN RESULT_CURSOR FOR QUERY_STRING;
    END IF;
END;
/
BEGIN
    C##QLK.SP_GRANT_EXECUTE('C##QLK.SP_SEARCH_DONVI', 'TKHOA');
    C##QLK.SP_GRANT_EXECUTE('C##QLK.SP_SEARCH_DONVI', 'TDV');
    C##QLK.SP_GRANT_EXECUTE('C##QLK.SP_SEARCH_DONVI', 'GV');
    C##QLK.SP_GRANT_EXECUTE('C##QLK.SP_SEARCH_DONVI', 'GVU');
    C##QLK.SP_GRANT_EXECUTE('C##QLK.SP_SEARCH_DONVI', 'NVCB');
    C##QLK.SP_GRANT_EXECUTE('C##QLK.SP_SEARCH_DONVI', 'SV');
END;
