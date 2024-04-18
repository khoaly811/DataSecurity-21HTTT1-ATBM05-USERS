--ALTER PLUGGABLE DATABASE ATBM2024 OPEN READ WRITE;
SET SERVEROUTPUT ON;

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

-- TONG HOP SP INSERT
CREATE OR REPLACE PROCEDURE SP_INSERT_NHANSU(
    INP_HOTEN IN VARCHAR2,
    INP_PHAI IN VARCHAR2,
    INP_NGSINH IN DATE,
    INP_PHUCAP IN NUMBER,
    INP_DT IN VARCHAR2,
    INP_VAITRO IN VARCHAR2,
    INP_TENDV IN VARCHAR2
) AS
    v_manv VARCHAR2(20);
    v_last_name VARCHAR2(100);
    v_max_rownum NUMBER;
    v_madv VARCHAR2(10);
BEGIN
    v_last_name := SUBSTR(INP_HOTEN, INSTR(INP_HOTEN, ' ', -1) + 1);

    SELECT MAX(ROWNUM) + 1 INTO v_max_rownum FROM C##QLK.NHANSU;
    
    v_manv := v_last_name || v_max_rownum;
    
    -- Get MADV based on TENDV
    SELECT MADV INTO v_madv FROM C##QLK.DONVI WHERE TENDV = INP_TENDV;

    INSERT INTO C##QLK.NHANSU (MANV, HOTEN, PHAI, NGSINH, PHUCAP, DT, VAITRO, MADV)
    VALUES (v_manv, INP_HOTEN, INP_PHAI, INP_NGSINH, INP_PHUCAP, INP_DT, INP_VAITRO, v_madv);
    COMMIT;
    
    DBMS_OUTPUT.PUT_LINE('Insert successful. MANV: ' || v_manv);
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        DBMS_OUTPUT.PUT_LINE('Error: ' || SQLERRM);
END;
/
BEGIN
    C##QLK.SP_GRANT_EXECUTE('C##QLK.SP_INSERT_NHANSU', 'TKHOA');
    C##QLK.SP_GRANT_EXECUTE('C##QLK.SP_INSERT_NHANSU', 'TDV');
    C##QLK.SP_GRANT_EXECUTE('C##QLK.SP_INSERT_NHANSU', 'GV');
    C##QLK.SP_GRANT_EXECUTE('C##QLK.SP_INSERT_NHANSU', 'GVU');
    C##QLK.SP_GRANT_EXECUTE('C##QLK.SP_INSERT_NHANSU', 'NVCB');
    C##QLK.SP_GRANT_EXECUTE('C##QLK.SP_INSERT_NHANSU', 'SV');
END;
/

CREATE OR REPLACE PROCEDURE C##QLK.SP_INSERT_DONVI(
    INP_MADV IN VARCHAR2,
    INP_TENDV IN VARCHAR2,
    INP_TEN IN VARCHAR2
) AS V_MANV C##QLK.NHANSU.MANV%TYPE;
BEGIN
    SELECT MANV INTO V_MANV FROM C##QLK.NHANSU
    WHERE HOTEN = INP_TEN;
    INSERT INTO DONVI VALUES (INP_MADV, INP_TENDV, V_MANV);
    COMMIT;
    DBMS_OUTPUT.PUT_LINE('Insert successful. MANV: ' || v_manv);
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        DBMS_OUTPUT.PUT_LINE('Error: ' || SQLERRM);
END;
/
BEGIN
    C##QLK.SP_GRANT_EXECUTE('C##QLK.SP_INSERT_DONVI', 'TKHOA');
    C##QLK.SP_GRANT_EXECUTE('C##QLK.SP_INSERT_DONVI', 'TDV');
    C##QLK.SP_GRANT_EXECUTE('C##QLK.SP_INSERT_DONVI', 'GV');
    C##QLK.SP_GRANT_EXECUTE('C##QLK.SP_INSERT_DONVI', 'GVU');
    C##QLK.SP_GRANT_EXECUTE('C##QLK.SP_INSERT_DONVI', 'NVCB');
    C##QLK.SP_GRANT_EXECUTE('C##QLK.SP_INSERT_DONVI', 'SV');
END;
/

CREATE SEQUENCE hp_sequence
  START WITH 34
  INCREMENT BY 1
  NOCACHE;
  
CREATE OR REPLACE PROCEDURE C##QLK.SP_INSERT_HOCPHAN(
    INP_TENHP IN VARCHAR2,
    INP_SOTC IN NUMBER,
    INP_STLT IN NUMBER,
    INP_STTH IN NUMBER,
    INP_SOSVTD IN NUMBER,
    INP_TENDV IN VARCHAR2
) AS
    V_MAHP INTEGER;
    V_MADV VARCHAR2(10);
BEGIN
    SELECT hp_sequence.NEXTVAL INTO V_MAHP FROM dual;
    SELECT MADV INTO V_MADV FROM C##QLK.DONVI WHERE TENDV = INP_TENDV;
    INSERT INTO C##QLK.HOCPHAN (MAHP, TENHP, SOTC, STLT, STTH, SOSVTD, MADV)
    VALUES (V_MAHP, INP_TENHP, INP_SOTC, INP_STLT, INP_STTH, INP_SOSVTD, V_MADV);
    COMMIT;
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        -- Output error message
        DBMS_OUTPUT.PUT_LINE('Error: ' || SQLERRM);
END;
/
BEGIN
    C##QLK.SP_GRANT_EXECUTE('C##QLK.SP_INSERT_HOCPHAN', 'TKHOA');
    C##QLK.SP_GRANT_EXECUTE('C##QLK.SP_INSERT_HOCPHAN', 'TDV');
    C##QLK.SP_GRANT_EXECUTE('C##QLK.SP_INSERT_HOCPHAN', 'GV');
    C##QLK.SP_GRANT_EXECUTE('C##QLK.SP_INSERT_HOCPHAN', 'GVU');
    C##QLK.SP_GRANT_EXECUTE('C##QLK.SP_INSERT_HOCPHAN', 'NVCB');
    C##QLK.SP_GRANT_EXECUTE('C##QLK.SP_INSERT_HOCPHAN', 'SV');
END;
/
CREATE OR REPLACE PROCEDURE C##QLK.SP_INSERT_KHMO(
    INP_TENHP IN VARCHAR2,
    INP_HK INT,
    INP_NAM INT,
    INP_MACT VARCHAR2
) AS V_MAHP INT;
BEGIN
    SELECT MAHP INTO V_MAHP FROM HOCPHAN WHERE TENHP = INP_TENHP;
    INSERT INTO C##QLK.KHMO (MAHP, HK, NAM, MACT) VALUES
    (V_MAHP, INP_HK, INP_NAM, INP_MACT);
    COMMIT;
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        -- Output error message
        DBMS_OUTPUT.PUT_LINE('Error: ' || SQLERRM);
END;
/
BEGIN
    C##QLK.SP_GRANT_EXECUTE('C##QLK.SP_INSERT_KHMO', 'TKHOA');
    C##QLK.SP_GRANT_EXECUTE('C##QLK.SP_INSERT_KHMO', 'TDV');
    C##QLK.SP_GRANT_EXECUTE('C##QLK.SP_INSERT_KHMO', 'GV');
    C##QLK.SP_GRANT_EXECUTE('C##QLK.SP_INSERT_KHMO', 'GVU');
    C##QLK.SP_GRANT_EXECUTE('C##QLK.SP_INSERT_KHMO', 'NVCB');
    C##QLK.SP_GRANT_EXECUTE('C##QLK.SP_INSERT_KHMO', 'SV');
END;
/

CREATE OR REPLACE PROCEDURE C##QLK.SP_INSERT_DANGKY (
    INP_TENHP IN VARCHAR2,
    INP_TENGV IN VARCHAR2,
    INP_HK INT,
    INP_NAM INT,
    INP_MACT VARCHAR2
)
AS 
    V_MASV VARCHAR2(25);
    V_MAGV VARCHAR2(20);
    V_MAHP INT;
BEGIN
    SELECT UPPER(SUBSTR(USER, 1, 1)) || LOWER(SUBSTR(USER, 2)) INTO V_MASV FROM DUAL;
    SELECT MANV INTO V_MAGV FROM C##QLK.NHANSU WHERE HOTEN = INP_TENGV;
    SELECT MAHP INTO V_MAHP FROM C##QLK.HOCPHAN WHERE TENHP = INP_TENHP;
    INSERT INTO DANGKY(MASV, MAGV, MAHP,HK,NAM,MACT,DIEMTH,DIEMQT,DIEMCK,DIEMTK)
    VALUES (V_MASV,V_MAGV, V_MAHP, INP_HK, INP_NAM, INP_MACT, NULL, NULL, NULL, NULL);
    COMMIT;
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        -- Output error message
        DBMS_OUTPUT.PUT_LINE('Error: ' || SQLERRM);
END;
/
BEGIN
    C##QLK.SP_GRANT_EXECUTE('C##QLK.SP_INSERT_DANGKY', 'TKHOA');
    C##QLK.SP_GRANT_EXECUTE('C##QLK.SP_INSERT_DANGKY', 'TDV');
    C##QLK.SP_GRANT_EXECUTE('C##QLK.SP_INSERT_DANGKY', 'GV');
    C##QLK.SP_GRANT_EXECUTE('C##QLK.SP_INSERT_DANGKY', 'GVU');
    C##QLK.SP_GRANT_EXECUTE('C##QLK.SP_INSERT_DANGKY', 'NVCB');
    C##QLK.SP_GRANT_EXECUTE('C##QLK.SP_INSERT_DANGKY', 'SV');
END;
/
CREATE OR REPLACE PROCEDURE C##QLK.SP_INSERT_PHANCONG(
    INP_TENGV IN VARCHAR2,
    INP_TENHP IN VARCHAR2,
    INP_HK IN INT,
    INP_NAM IN INT,
    INP_MACT IN VARCHAR2
)
AS
    V_MAGV VARCHAR2(20);
    V_MAHP INT;
BEGIN
    SELECT MANV INTO V_MAGV FROM C##QLK.NHANSU WHERE HOTEN = INP_TENGV;
    SELECT MAHP INTO V_MAHP FROM C##QLK.HOCPHAN WHERE TENHP = INP_TENHP;
    INSERT INTO PHANCONG(MAGV,MAHP,HK,NAM,MACT)
    VALUES (V_MAGV, V_MAHP, INP_HK, INP_NAM, INP_MACT);
    COMMIT;
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        -- Output error message
        DBMS_OUTPUT.PUT_LINE('Error: ' || SQLERRM);
END;
/

BEGIN
    C##QLK.SP_GRANT_EXECUTE('C##QLK.SP_INSERT_PHANCONG', 'TKHOA');
    C##QLK.SP_GRANT_EXECUTE('C##QLK.SP_INSERT_PHANCONG', 'TDV');
    C##QLK.SP_GRANT_EXECUTE('C##QLK.SP_INSERT_PHANCONG', 'GV');
    C##QLK.SP_GRANT_EXECUTE('C##QLK.SP_INSERT_PHANCONG', 'GVU');
    C##QLK.SP_GRANT_EXECUTE('C##QLK.SP_INSERT_PHANCONG', 'NVCB');
    C##QLK.SP_GRANT_EXECUTE('C##QLK.SP_INSERT_PHANCONG', 'SV');
END;
/

CREATE OR REPLACE PROCEDURE C##QLK.SP_INSERT_SINHVIEN(
    INP_HOTEN IN VARCHAR2,
    INP_PHAI IN VARCHAR2,
    INP_NGSINH IN DATE,
    INP_DCHI IN NUMBER,
    INP_DT IN VARCHAR2,
    INP_MACT IN VARCHAR2,
    INP_MANGANH IN VARCHAR2
) AS
    V_MASV VARCHAR2(25);
    V_LAST_NAME VARCHAR2(100);
    v_max_rownum NUMBER;
BEGIN
    v_last_name := SUBSTR(INP_HOTEN, INSTR(INP_HOTEN, ' ', -1) + 1);

    SELECT MAX(ROWNUM) + 1 INTO v_max_rownum FROM C##QLK.SINHVIEN;
    
    V_MASV := v_last_name || v_max_rownum;

    INSERT INTO C##QLK.SINHVIEN(MASV, HOTEN, PHAI, NGSINH, DCHI, DT, MACT, MANGANH, SOTCTL, DTBTL)
    VALUES (V_MASV, INP_HOTEN, INP_PHAI, INP_NGSINH, INP_DCHI, INP_DT, INP_MACT, INP_MANGANH, NULL, NULL);
    COMMIT;
    
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        DBMS_OUTPUT.PUT_LINE('Error: ' || SQLERRM);
END;
/

BEGIN
    C##QLK.SP_GRANT_EXECUTE('C##QLK.SP_INSERT_SINHVIEN', 'TKHOA');
    C##QLK.SP_GRANT_EXECUTE('C##QLK.SP_INSERT_SINHVIEN', 'TDV');
    C##QLK.SP_GRANT_EXECUTE('C##QLK.SP_INSERT_SINHVIEN', 'GV');
    C##QLK.SP_GRANT_EXECUTE('C##QLK.SP_INSERT_SINHVIEN', 'GVU');
    C##QLK.SP_GRANT_EXECUTE('C##QLK.SP_INSERT_SINHVIEN', 'NVCB');
    C##QLK.SP_GRANT_EXECUTE('C##QLK.SP_INSERT_SINHVIEN', 'SV');
END;
/
