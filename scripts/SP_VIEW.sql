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
-- GUI THONG TIN NHAN SU
CREATE OR REPLACE PROCEDURE SP_VIEW_NHANSU(
    RESULT_CURSOR OUT SYS_REFCURSOR
)
AS
    TEMP_COUNT INT;
    QUERY_STRING VARCHAR2(1000);
BEGIN
    SELECT COUNT(*) INTO TEMP_COUNT FROM ALL_TABLES WHERE TABLE_NAME = 'NHANSU';
    IF (TEMP_COUNT = 0) THEN
        RAISE_APPLICATION_ERROR(-20000,'THONG TIN KHONG TON TAI!');
    ELSE
        QUERY_STRING := 'SELECT HOTEN, PHAI, NGSINH, PHUCAP, DT, VAITRO, TENDV, MANV 
        FROM C##QLK.NHANSU NS LEFT OUTER JOIN C##QLK.DONVI DV ON NS.MADV = DV.MADV';
        OPEN RESULT_CURSOR FOR QUERY_STRING;
        EXECUTE IMMEDIATE QUERY_STRING;
    END IF;
END;
/
select * from C##QLK.NHANSU;

BEGIN
    SP_GRANT_EXECUTE('SP_VIEW_NHANSU', 'TKHOA');
    SP_GRANT_EXECUTE('SP_VIEW_NHANSU', 'TDV');
    SP_GRANT_EXECUTE('SP_VIEW_NHANSU', 'GV');
    SP_GRANT_EXECUTE('SP_VIEW_NHANSU', 'GVU');
    SP_GRANT_EXECUTE('SP_VIEW_NHANSU', 'NVCB');
    SP_GRANT_EXECUTE('SP_VIEW_NHANSU', 'SV');
END;
/

-- GUI THONG TIN SINH VIEN 
CREATE OR REPLACE PROCEDURE SP_VIEW_SINHVIEN(
    RESULT_CURSOR OUT SYS_REFCURSOR
)
AS
    TEMP_COUNT INT;
    QUERY_STRING VARCHAR2(1000);
BEGIN
    SELECT COUNT(*) INTO TEMP_COUNT FROM ALL_TABLES WHERE TABLE_NAME = 'SINHVIEN';
    IF (TEMP_COUNT = 0) THEN
        RAISE_APPLICATION_ERROR(-20000,'THONG TIN KHONG TON TAI!');
    ELSE
        QUERY_STRING := 'SELECT HOTEN, MACT, MANGANH, PHAI, NGSINH, DCHI, DT, SOTCTL, DTBTL, MASV
        FROM C##QLK.SINHVIEN';
        OPEN RESULT_CURSOR FOR QUERY_STRING;
    END IF;
END;
/
BEGIN
    SP_GRANT_EXECUTE('SP_VIEW_SINHVIEN', 'TKHOA');
    SP_GRANT_EXECUTE('SP_VIEW_SINHVIEN', 'TDV');
    SP_GRANT_EXECUTE('SP_VIEW_SINHVIEN', 'GV');
    SP_GRANT_EXECUTE('SP_VIEW_SINHVIEN', 'GVU');
    SP_GRANT_EXECUTE('SP_VIEW_SINHVIEN', 'NVCB');
    SP_GRANT_EXECUTE('SP_VIEW_SINHVIEN', 'SV');
END;
/
-- GUI THONG TIN DON VI
CREATE OR REPLACE PROCEDURE SP_VIEW_DONVI(
    RESULT_CURSOR OUT SYS_REFCURSOR
)
AS
    TEMP_COUNT INT;
    QUERY_STRING VARCHAR2(1000);
BEGIN
    SELECT COUNT(*) INTO TEMP_COUNT FROM ALL_TABLES WHERE TABLE_NAME = 'DONVI';
    IF (TEMP_COUNT = 0) THEN
        RAISE_APPLICATION_ERROR(-20000,'THONG TIN KHONG TON TAI!');
    ELSE
        QUERY_STRING := 'SELECT TENDV, HOTEN, MADV
        FROM C##QLK.DONVI DV JOIN C##QLK.NHANSU NS ON DV.TRGDV = NS.MANV';
        OPEN RESULT_CURSOR FOR QUERY_STRING;
    END IF;
END;
/
BEGIN
    SP_GRANT_EXECUTE('SP_VIEW_DONVI', 'TKHOA');
    SP_GRANT_EXECUTE('SP_VIEW_DONVI', 'TDV');
    SP_GRANT_EXECUTE('SP_VIEW_DONVI', 'GV');
    SP_GRANT_EXECUTE('SP_VIEW_DONVI', 'GVU');
    SP_GRANT_EXECUTE('SP_VIEW_DONVI', 'NVCB');
    SP_GRANT_EXECUTE('SP_VIEW_DONVI', 'SV');
END;
/

-- GUI DANH SACH HOC PHAN
CREATE OR REPLACE PROCEDURE SP_VIEW_HOCPHAN(
    RESULT_CURSOR OUT SYS_REFCURSOR
)
AS
    TEMP_COUNT INT;
    QUERY_STRING VARCHAR2(1000);
BEGIN
    SELECT COUNT(*) INTO TEMP_COUNT FROM ALL_TABLES WHERE TABLE_NAME = 'HOCPHAN';
    IF (TEMP_COUNT = 0) THEN
        RAISE_APPLICATION_ERROR(-20000,'THONG TIN KHONG TON TAI!');
    ELSE
        QUERY_STRING := 'SELECT TENHP, SOTC, STLT, STTH, SOSVTD, TENDV, MAHP
        FROM C##QLK.HOCPHAN HP JOIN C##QLK.DONVI DV ON HP.MADV = DV.MADV';
        OPEN RESULT_CURSOR FOR QUERY_STRING;
    END IF;
END;
/
BEGIN
    SP_GRANT_EXECUTE('SP_VIEW_HOCPHAN', 'TKHOA');
    SP_GRANT_EXECUTE('SP_VIEW_HOCPHAN', 'TDV');
    SP_GRANT_EXECUTE('SP_VIEW_HOCPHAN', 'GV');
    SP_GRANT_EXECUTE('SP_VIEW_HOCPHAN', 'GVU');
    SP_GRANT_EXECUTE('SP_VIEW_HOCPHAN', 'NVCB');
    SP_GRANT_EXECUTE('SP_VIEW_HOCPHAN', 'SV');
END;
/
-- GUI KE HOACH MO HOC PHAN
CREATE OR REPLACE PROCEDURE SP_VIEW_KHMO(
    RESULT_CURSOR OUT SYS_REFCURSOR
)
AS
    TEMP_COUNT INT;
    QUERY_STRING VARCHAR2(1000);
BEGIN
    SELECT COUNT(*) INTO TEMP_COUNT FROM ALL_TABLES WHERE TABLE_NAME = 'KHMO';
    IF (TEMP_COUNT = 0) THEN
        RAISE_APPLICATION_ERROR(-20000,'THONG TIN KHONG TON TAI!');
    ELSE
        QUERY_STRING := 'SELECT TENHP, HK, NAM, NGAYBD, MACT, MAHP
        FROM C##QLK.KHMO KH JOIN C##QLK.HOCPHAN HP ON KH.MAHP = HP.MAHP';
        OPEN RESULT_CURSOR FOR QUERY_STRING;
    END IF;
END;
/
BEGIN
    SP_GRANT_EXECUTE('SP_VIEW_KHMO', 'TKHOA');
    SP_GRANT_EXECUTE('SP_VIEW_KHMO', 'TDV');
    SP_GRANT_EXECUTE('SP_VIEW_KHMO', 'GV');
    SP_GRANT_EXECUTE('SP_VIEW_KHMO', 'GVU');
    SP_GRANT_EXECUTE('SP_VIEW_KHMO', 'NVCB');
    SP_GRANT_EXECUTE('SP_VIEW_KHMO', 'SV');
END;
/
-- GUI DANH SACH PHAN CONG
CREATE OR REPLACE PROCEDURE SP_VIEW_PHANCONG(
    RESULT_CURSOR OUT SYS_REFCURSOR
)
AS
    TEMP_COUNT INT;
    QUERY_STRING VARCHAR2(1000);
BEGIN
    SELECT COUNT(*) INTO TEMP_COUNT FROM ALL_TABLES WHERE TABLE_NAME = 'PHANCONG';
    IF (TEMP_COUNT = 0) THEN
        RAISE_APPLICATION_ERROR(-20000,'THONG TIN KHONG TON TAI!');
    ELSE
        QUERY_STRING := 'SELECT HOTEN, TENHP, HK, NAM, MACT, PC.MAHP, MAGV
        FROM C##QLK.PHANCONG PC JOIN C##QLK.NHANSU NS ON PC.MAGV = NS.MANV
        JOIN C##QLK.HOCPHAN HP ON HP.MAHP = PC.MAHP';
        OPEN RESULT_CURSOR FOR QUERY_STRING;
    END IF;
END;
/

BEGIN
    SP_GRANT_EXECUTE('SP_VIEW_PHANCONG', 'TKHOA');
    SP_GRANT_EXECUTE('SP_VIEW_PHANCONG', 'TDV');
    SP_GRANT_EXECUTE('SP_VIEW_PHANCONG', 'GV');
    SP_GRANT_EXECUTE('SP_VIEW_PHANCONG', 'GVU');
    SP_GRANT_EXECUTE('SP_VIEW_PHANCONG', 'NVCB');
    SP_GRANT_EXECUTE('SP_VIEW_PHANCONG', 'SV');
END;
/

-- GUI DANH SACH DANG KY HOC PHAN
CREATE OR REPLACE PROCEDURE SP_VIEW_DANGKY(
    RESULT_CURSOR OUT SYS_REFCURSOR
)
AS
    TEMP_COUNT INT;
    QUERY_STRING VARCHAR2(1000);
BEGIN
    SELECT COUNT(*) INTO TEMP_COUNT FROM ALL_TABLES WHERE TABLE_NAME = 'DANGKY';
    IF (TEMP_COUNT = 0) THEN
        RAISE_APPLICATION_ERROR(-20000,'THONG TIN KHONG TON TAI!');
    ELSE
        QUERY_STRING := 'SELECT SV.HOTEN AS HOTEN_SV, NS.HOTEN AS HOTEN_GV, HP.TENHP, HK, NAM, DK.MACT, DIEMTH, DIEMQT, DIEMCK, DIEMTK, DK.MASV, DK.MAGV
        FROM C##QLK.DANGKY DK JOIN C##QLK.SINHVIEN SV ON DK.MASV=SV.MASV JOIN
        C##QLK.NHANSU NS ON NS.MANV = DK.MAGV JOIN C##QLK.HOCPHAN HP ON
        HP.MAHP = DK.MAHP';
        OPEN RESULT_CURSOR FOR QUERY_STRING;
    END IF;
END;
/

BEGIN
    SP_GRANT_EXECUTE('SP_VIEW_DANGKY', 'TKHOA');
    SP_GRANT_EXECUTE('SP_VIEW_DANGKY', 'TDV');
    SP_GRANT_EXECUTE('SP_VIEW_DANGKY', 'GV');
    SP_GRANT_EXECUTE('SP_VIEW_DANGKY', 'GVU');
    SP_GRANT_EXECUTE('SP_VIEW_DANGKY', 'NVCB');
    SP_GRANT_EXECUTE('SP_VIEW_DANGKY', 'SV');
END;
/
-- GUI BUTTON THONG BAO
CREATE OR REPLACE PROCEDURE SP_VIEW_THONGBAO(
    RESULT_CURSOR OUT SYS_REFCURSOR
)
AS
    TEMP_COUNT INT;
    QUERY_STRING VARCHAR2(1000);
BEGIN
    SELECT COUNT(*) INTO TEMP_COUNT FROM ALL_TABLES WHERE TABLE_NAME = 'THONGBAO';
    IF (TEMP_COUNT = 0) THEN
        RAISE_APPLICATION_ERROR(-20000,'THONG TIN KHONG TON TAI!');
    ELSE
        QUERY_STRING := 'SELECT NOIDUNG, THOIGIAN, MATB FROM C##QLK.THONGBAO';
        OPEN RESULT_CURSOR FOR QUERY_STRING;
    END IF;
END;
/
BEGIN
    SP_GRANT_EXECUTE('SP_VIEW_THONGBAO', 'TKHOA');
    SP_GRANT_EXECUTE('SP_VIEW_THONGBAO', 'TDV');
    SP_GRANT_EXECUTE('SP_VIEW_THONGBAO', 'GV');
    SP_GRANT_EXECUTE('SP_VIEW_THONGBAO', 'GVU');
    SP_GRANT_EXECUTE('SP_VIEW_THONGBAO', 'NVCB');
    SP_GRANT_EXECUTE('SP_VIEW_THONGBAO', 'SV');
END;
/
