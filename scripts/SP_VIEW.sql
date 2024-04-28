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
        FROM C##QLK.NHANSU NS JOIN C##QLK.DONVI DV ON NS.MADV = DV.MADV';
        OPEN RESULT_CURSOR FOR QUERY_STRING;
        EXECUTE IMMEDIATE QUERY_STRING;
    END IF;
END;
/

BEGIN
    SP_GRANT_EXECUTE('SP_VIEW_NHANSU', 'TKHOA');
    SP_GRANT_EXECUTE('SP_VIEW_NHANSU', 'TDV');
    SP_GRANT_EXECUTE('SP_VIEW_NHANSU', 'GV');
    SP_GRANT_EXECUTE('SP_VIEW_NHANSU', 'GVU');
    SP_GRANT_EXECUTE('SP_VIEW_NHANSU', 'NVCB');
    SP_GRANT_EXECUTE('SP_VIEW_NHANSU', 'SV');
END;
/
SELECT HOTEN, MACT, MANGANH, PHAI, NGSINH, DCHI, DT, SOTCTL, DTBTL, MASV
        FROM C##QLK.SINHVIEN;
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
        QUERY_STRING := 'SELECT HOTEN, PHAI, NGSINH, DCHI, DT, MACT, MANGANH, SOTCTL, DTBTL
        FROM C##QLK.SINHVIEN';
        OPEN RESULT_CURSOR FOR QUERY_STRING;
    END IF;
END;
/
SELECT HOTEN, PHAI, NGSINH, DCHI, DT, MACT, MANGANH, SOTCTL, DTBTL
        FROM C##QLK.SINHVIEN;
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
        QUERY_STRING := 'SELECT DV.MADV AS MDV, TENDV, HOTEN
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
        QUERY_STRING := 'SELECT TENHP, KH.HK, KH.NAM, NGAYBD, KH.MACT, KH.MAHP
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

CREATE OR REPLACE PROCEDURE SP_VIEW_ADD_PHANCONG(
    RESULT_CURSOR OUT SYS_REFCURSOR
) AS
    QUERY_STRING VARCHAR2(1000);
BEGIN
    QUERY_STRING := 'SELECT HP.TENHP, KH.HK, KH.NAM, KH.NGAYBD, KH.MACT, HP.SOTC, HP.STLT, HP.STTH, HP.SOSVTD, DV.TENDV, KH.MAHP
    FROM C##QLK.KHMO KH JOIN C##QLK.HOCPHAN HP ON HP.MAHP=KH.MAHP JOIN C##QLK.DONVI DV ON DV.MADV = HP.MADV';
    OPEN RESULT_CURSOR FOR QUERY_STRING;
END;
/
BEGIN
    SP_GRANT_EXECUTE('SP_VIEW_ADD_PHANCONG', 'TKHOA');
    SP_GRANT_EXECUTE('SP_VIEW_ADD_PHANCONG', 'TDV');
    SP_GRANT_EXECUTE('SP_VIEW_ADD_PHANCONG', 'GV');
    SP_GRANT_EXECUTE('SP_VIEW_ADD_PHANCONG', 'GVU');
    SP_GRANT_EXECUTE('SP_VIEW_ADD_PHANCONG', 'NVCB');
    SP_GRANT_EXECUTE('SP_VIEW_ADD_PHANCONG', 'SV');
END;
/

CREATE OR REPLACE PROCEDURE SP_VIEW_ADD_DANGKY(
    RESULT_CURSOR OUT SYS_REFCURSOR
) AS
    QUERY_STRING VARCHAR2(1000);
BEGIN
    QUERY_STRING := 'SELECT HP.TENHP, NS.HOTEN, KH.HK, KH.NAM, KH.NGAYBD, KH.MACT, HP.MADV, HP.SOTC, HP.STLT, HP.STTH, HP.SOSVTD
    FROM C##QLK.HOCPHAN HP JOIN C##QLK.KHMO KH ON HP.MAHP = KH.MAHP JOIN C##QLK.PHANCONG PC ON PC.MAHP = KH.MAHP
    AND PC.HK = KH.HK AND PC.NAM = KH.NAM JOIN C##QLK.NHANSU NS ON NS.MANV = PC.MAGV WHERE KH.MAHP NOT IN (SELECT DK.MAHP FROM C##QLK.DANGKY DK)';
    OPEN RESULT_CURSOR FOR QUERY_STRING;
END;
/
BEGIN
    SP_GRANT_EXECUTE('SP_VIEW_ADD_DANGKY', 'TKHOA');
    SP_GRANT_EXECUTE('SP_VIEW_ADD_DANGKY', 'TDV');
    SP_GRANT_EXECUTE('SP_VIEW_ADD_DANGKY', 'GV');
    SP_GRANT_EXECUTE('SP_VIEW_ADD_DANGKY', 'GVU');
    SP_GRANT_EXECUTE('SP_VIEW_ADD_DANGKY', 'NVCB');
    SP_GRANT_EXECUTE('SP_VIEW_ADD_DANGKY', 'SV');
END;
/



-- GUI BUTTON THONG BAO
CREATE OR REPLACE PROCEDURE SP_VIEW_FGA(
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
        QUERY_STRING := 'SELECT DB_USER, EXTENDED_TIMESTAMP, OBJECT_NAME, STATEMENT_TYPE, SQL_TEXT
FROM DBA_FGA_AUDIT_TRAIL 
ORDER BY EXTENDED_TIMESTAMP';
        OPEN RESULT_CURSOR FOR QUERY_STRING;
    END IF;
END;
/
--BEGIN
--    SP_GRANT_EXECUTE('SP_VIEW_FGA', 'TKHOA');
--    SP_GRANT_EXECUTE('SP_VIEW_FGA', 'TDV');
--    SP_GRANT_EXECUTE('SP_VIEW_FGA', 'GV');
--    SP_GRANT_EXECUTE('SP_VIEW_FGA', 'GVU');
--    SP_GRANT_EXECUTE('SP_VIEW_FGA', 'NVCB');
--    SP_GRANT_EXECUTE('SP_VIEW_FGA', 'SV');
--END;
--/

CREATE OR REPLACE PROCEDURE SP_VIEW_STDA(
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
        QUERY_STRING := 'SELECT USERNAME, TIMESTAMP, ACTION_NAME, OBJ_NAME
FROM (
    SELECT *
    FROM DBA_AUDIT_TRAIL
    ORDER BY TIMESTAMP DESC
)
WHERE ROWNUM <= 70';
        OPEN RESULT_CURSOR FOR QUERY_STRING;
    END IF;
END;
/
--BEGIN
--    SP_GRANT_EXECUTE('SP_VIEW_STDA', 'TKHOA');
--    SP_GRANT_EXECUTE('SP_VIEW_STDA', 'TDV');
--    SP_GRANT_EXECUTE('SP_VIEW_STDA', 'GV');
--    SP_GRANT_EXECUTE('SP_VIEW_STDA', 'GVU');
--    SP_GRANT_EXECUTE('SP_VIEW_STDA', 'NVCB');
--    SP_GRANT_EXECUTE('SP_VIEW_STDA', 'SV');
--END;
--/
commit;

