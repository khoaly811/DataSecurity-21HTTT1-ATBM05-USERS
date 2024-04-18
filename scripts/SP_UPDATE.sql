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

-- TONG HOP SP UDPATE
CREATE OR REPLACE PROCEDURE SP_ALL_UPDATE_NHANSU(
    INP_HOTEN IN VARCHAR2,
    INP_PHAI IN VARCHAR2,
    INP_NGSINH IN DATE,
    INP_PHUCAP IN NUMBER,
    INP_DT IN VARCHAR2,
    INP_MANV IN VARCHAR2,
    INP_TENDV IN VARCHAR2
) AUTHID CURRENT_USER AS
    V_ROLE VARCHAR2(30);
    V_MADV VARCHAR2(10);
BEGIN
    SELECT * INTO V_ROLE FROM SESSION_ROLES;
    IF v_role = 'TKHOA' THEN
    SELECT MADV INTO V_MADV FROM C##QLK.DONVI WHERE TENDV = INP_TENDV AND ROWNUM = 1;
        UPDATE C##QLK.NHANSU
        SET HOTEN = INP_HOTEN,
            PHAI = INP_PHAI,
            NGSINH = INP_NGSINH,
            PHUCAP = INP_PHUCAP,
            DT = INP_DT,
            MADV = V_MADV
        WHERE MANV = INP_MANV;
        COMMIT;
    ELSIF v_role IN ('GV', 'TDV', 'GVU', 'NVCB') THEN
        UPDATE C##QLK.NHANSU
        SET DT = INP_DT;
        COMMIT;
    ELSE
        RAISE_APPLICATION_ERROR(-20001, 'Xin loi! Ban khong co quyen');
    END IF;
END;
/
BEGIN
    SP_GRANT_EXECUTE('SP_ALL_UPDATE_NHANSU', 'TKHOA');
    SP_GRANT_EXECUTE('SP_ALL_UPDATE_NHANSU', 'TDV');
    SP_GRANT_EXECUTE('SP_ALL_UPDATE_NHANSU', 'GV');
    SP_GRANT_EXECUTE('SP_ALL_UPDATE_NHANSU', 'GVU');
    SP_GRANT_EXECUTE('SP_ALL_UPDATE_NHANSU', 'NVCB');
    SP_GRANT_EXECUTE('SP_ALL_UPDATE_NHANSU', 'SV');
END;
/

CREATE OR REPLACE PROCEDURE SP_ALL_UPDATE_DANGKY(
    INP_HOTEN IN VARCHAR2,
    INP_HOTENGV IN VARCHAR2,
    INP_HOCPHAN IN VARCHAR2,
    INP_HK INT,
    INP_NAM INT,
    INP_MACT VARCHAR2,
    INP_DIEMTH IN NUMBER,
    INP_DIEMQT in NUMBER,
    INP_DIEMCK IN NUMBER,
    INP_DIEMTK IN NUMBER
) AUTHID CURRENT_USER AS
    V_ROLE VARCHAR2(30);
    V_MADV VARCHAR2(10);
    V_MASV VARCHAR2(25);
    V_MAGV VARCHAR2(20);
    V_MAHP INT;
BEGIN
    SELECT * INTO V_ROLE FROM SESSION_ROLES;
    IF v_role = 'GV' THEN
    
    select MASV into V_MASV from C##QLK.SINHVIEN sv where sv.HOTEN = INP_HOTEN;
    select MANV into V_MAGV from C##QLK.NHANSU gv where gv.HOTEN = INP_HOTENGV;
    select MAHP into V_MAHP from C##QLK.HOCPHAN hp where hp.TENHP = INP_HOCPHAN;
    
    update C##QLK.DANGKY set DIEMTH = INP_DIEMTH,
    DIEMQT = INP_DIEMQT,
    DIEMCK = INP_DIEMCK,
    DIEMTK = INP_DIEMTK
    WHERE MASV = V_MASV and MAGV = V_MAGV and MAHP = V_MAHP and HK = INP_HK and NAM = INP_NAM and MACT = INP_MACT;
        COMMIT;
    ELSE
        RAISE_APPLICATION_ERROR(-20001, 'Xin loi! Ban khong co quyen');
    END IF;
END;
/
BEGIN
    SP_GRANT_EXECUTE('SP_ALL_UPDATE_DANGKY', 'TKHOA');
    SP_GRANT_EXECUTE('SP_ALL_UPDATE_DANGKY', 'TDV');
    SP_GRANT_EXECUTE('SP_ALL_UPDATE_DANGKY', 'GV');
    SP_GRANT_EXECUTE('SP_ALL_UPDATE_DANGKY', 'GVU');
    SP_GRANT_EXECUTE('SP_ALL_UPDATE_DANGKY', 'NVCB');
    SP_GRANT_EXECUTE('SP_ALL_UPDATE_DANGKY', 'SV');
END;
/
CREATE OR REPLACE PROCEDURE SP_ALL_UPDATE_PHANCONG(
    INP_TENGV IN VARCHAR2,
    INP_TENHP IN VARCHAR2,
    INP_HK IN NUMBER,
    INP_NAM IN NUMBER,
    INP_MACT IN VARCHAR2,
    OLD_TENGV IN VARCHAR2,
    OLD_TENHP IN VARCHAR2,
    OLD_HK IN NUMBER,
    OLD_NAM IN NUMBER,
    OLD_MACT IN VARCHAR2
) AUTHID CURRENT_USER AS
    V_MAGV VARCHAR2(20);
    V_MAGV_OLD VARCHAR2(20);
    V_MAHP_OLD VARCHAR2(20);
    V_MAHP INT;
    V_ROLE VARCHAR2(20);
BEGIN
    SELECT * INTO V_ROLE FROM SESSION_ROLES;
    IF V_ROLE = 'TKHOA' OR V_ROLE = 'TDV' OR V_ROLE = 'GVU' THEN
    SELECT MANV INTO V_MAGV FROM C##QLK.NHANSU WHERE HOTEN = INP_TENGV;
    SELECT MANV INTO V_MAGV_OLD FROM C##QLK.NHANSU WHERE HOTEN = OLD_TENGV;
    SELECT MAHP INTO V_MAHP FROM C##QLK.HOCPHAN WHERE TENHP = INP_TENHP;
    SELECT MAHP INTO V_MAHP_OLD FROM C##QLK.HOCPHAN WHERE TENHP = OLD_TENHP;
    UPDATE C##QLK.PHANCONG SET
    MAGV = V_MAGV,
    MAHP = V_MAHP,
    HK = INP_HK,
    NAM = INP_NAM,
    MACT = INP_MACT
    WHERE MAGV = V_MAGV_OLD AND MAHP = V_MAHP_OLD AND HK = OLD_HK AND NAM = OLD_NAM AND MACT = OLD_MACT;
    
    COMMIT;
    ELSE
        RAISE_APPLICATION_ERROR(-20001, 'Xin loi! Ban khong co quyen');
    END IF;
END;
/
BEGIN
    SP_GRANT_EXECUTE('SP_ALL_UPDATE_PHANCONG', 'TKHOA');
    SP_GRANT_EXECUTE('SP_ALL_UPDATE_PHANCONG', 'TDV');
    SP_GRANT_EXECUTE('SP_ALL_UPDATE_PHANCONG', 'GV');
    SP_GRANT_EXECUTE('SP_ALL_UPDATE_PHANCONG', 'GVU');
    SP_GRANT_EXECUTE('SP_ALL_UPDATE_PHANCONG', 'NVCB');
    SP_GRANT_EXECUTE('SP_ALL_UPDATE_PHANCONG', 'SV');
END;
/
BEGIN
    C##QLK.SP_ALL_UPDATE_PHANCONG('HOC PHAN 4', 4,2023, 'CLC');
END;
/
CREATE OR REPLACE PROCEDURE SP_ALL_UPDATE_HOCPHAN(
    INP_TENHP IN VARCHAR2,
    INP_SOTC IN INT,
    INP_STLT IN INT,
    INP_STTH IN INT,
    INP_SOSVTD IN INT,
    INP_TENDV IN VARCHAR2
) AS
    V_MAHP INT;
    V_MADV VARCHAR2(10);
    V_ROLE VARCHAR2(15);
BEGIN
    SELECT * INTO V_ROLE FROM SESSION_ROLES;
    IF V_ROLE = 'GVU' THEN
    SELECT MAHP INTO V_MAHP FROM C##QLK.HOCPHAN WHERE INP_TENHP = TENHP;
    SELECT MADV INTO V_MADV FROM C##QLK.DONVI WHERE INP_TENDV = TENDV;
    UPDATE C##QLK.HOCPHAN SET
    TENHP = INP_TENHP,
    SOTC = INP_SOTC,
    STLT = INP_STLT,
    STTH = INP_STTH,
    SOSVTD = INP_SOSVTD,
    MADV = V_MADV
    WHERE MAHP = V_MAHP;
    COMMIT;
    ELSE
        RAISE_APPLICATION_ERROR(-20001, 'Xin loi! Ban khong co quyen');
    END IF;
END;
/
BEGIN
    SP_GRANT_EXECUTE('SP_ALL_UPDATE_HOCPHAN', 'TKHOA');
    SP_GRANT_EXECUTE('SP_ALL_UPDATE_HOCPHAN', 'TDV');
    SP_GRANT_EXECUTE('SP_ALL_UPDATE_HOCPHAN', 'GV');
    SP_GRANT_EXECUTE('SP_ALL_UPDATE_HOCPHAN', 'GVU');
    SP_GRANT_EXECUTE('SP_ALL_UPDATE_HOCPHAN', 'NVCB');
    SP_GRANT_EXECUTE('SP_ALL_UPDATE_HOCPHAN', 'SV');
END;
/
CREATE OR REPLACE PROCEDURE SP_ALL_UPDATE_KHMO(
    INP_TENHP IN VARCHAR2,
    INP_HK IN INT,
    INP_NAM IN INT,
    INP_MACT IN VARCHAR2,
    OLD_TENHP IN VARCHAR2,
    OLD_HK IN INT,
    OLD_NAM IN INT,
    OLD_MACT IN VARCHAR2
) AS
    V_MAHP INT;
    V_MAHP_OLD INT;
    V_ROLE VARCHAR2(15);
BEGIN
    SELECT * INTO V_ROLE FROM SESSION_ROLES;
    IF V_ROLE = 'GVU' THEN
    SELECT MAHP INTO V_MAHP FROM C##QLK.HOCPHAN WHERE TENHP = INP_TENHP;
    SELECT MAHP INTO V_MAHP_OLD FROM C##QLK.HOCPHAN WHERE TENHP = OLD_TENHP;
    UPDATE C##QLK.KHMO SET
    MAHP = V_MAHP,
    HK = INP_HK,
    NAM = INP_NAM,
    MACT = INP_MACT
    WHERE MAHP = V_MAHP_OLD AND HK= OLD_HK AND NAM = OLD_NAM AND MACT=OLD_MACT;
    COMMIT;
    ELSE
        RAISE_APPLICATION_ERROR(-20001, 'Xin loi! Ban khong co quyen');
    END IF;
END;
/
SELECT TENHP, KH.HK, KH.NAM, NGAYBD, KH.MACT, KH.MAHP
        FROM C##QLK.KHMO KH JOIN C##QLK.HOCPHAN HP ON KH.MAHP = HP.MAHP;
BEGIN
    SP_GRANT_EXECUTE('SP_ALL_UPDATE_KHMO', 'TKHOA');
    SP_GRANT_EXECUTE('SP_ALL_UPDATE_KHMO', 'TDV');
    SP_GRANT_EXECUTE('SP_ALL_UPDATE_KHMO', 'GV');
    SP_GRANT_EXECUTE('SP_ALL_UPDATE_KHMO', 'GVU');
    SP_GRANT_EXECUTE('SP_ALL_UPDATE_KHMO', 'NVCB');
    SP_GRANT_EXECUTE('SP_ALL_UPDATE_KHMO', 'SV');
END;
/
CREATE OR REPLACE PROCEDURE SP_ALL_UPDATE_SINHVIEN(
    INP_HOTEN IN VARCHAR2,
    INP_PHAI IN VARCHAR2,
    INP_NGSINH IN DATE,
    INP_DCHI IN NUMBER,
    INP_DT IN VARCHAR2,
    INP_MACT IN VARCHAR2,
    INP_MANGANH IN VARCHAR2,
    INP_SOTCTL IN NUMBER,
    INP_DTBTL IN NUMBER
) AUTHID CURRENT_USER AS

    V_MASV VARCHAR2(25);
    V_ROLE VARCHAR2(30);
BEGIN
    SELECT * INTO V_ROLE FROM SESSION_ROLES;
    IF v_role = 'GVU' THEN
    SELECT MASV INTO V_MASV FROM C##QLK.SINHVIEN WHERE HOTEN = INP_HOTEN;
        UPDATE C##QLK.SINHVIEN
        SET HOTEN = INP_HOTEN,
            PHAI = INP_PHAI,
            NGSINH = INP_NGSINH,
            DCHI = INP_DCHI,
            DT = INP_DT,
            MACT = INP_MACT,
            MANGANH = INP_MANGANH,
            SOTCTL = INP_SOTCTL,
            DTBTL = INP_DTBTL
        WHERE MASV = V_MASV;
        COMMIT;
    ELSIF v_role IN ('SV') THEN
        UPDATE C##QLK.SINHVIEN
        SET DT = INP_DT, DCHI = INP_DCHI;
        COMMIT;
    ELSE
        RAISE_APPLICATION_ERROR(-20001, 'Xin loi! Ban khong co quyen');
    END IF;
END;
/
BEGIN
    SP_GRANT_EXECUTE('SP_ALL_UPDATE_SINHVIEN', 'TKHOA');
    SP_GRANT_EXECUTE('SP_ALL_UPDATE_SINHVIEN', 'TDV');
    SP_GRANT_EXECUTE('SP_ALL_UPDATE_SINHVIEN', 'GV');
    SP_GRANT_EXECUTE('SP_ALL_UPDATE_SINHVIEN', 'GVU');
    SP_GRANT_EXECUTE('SP_ALL_UPDATE_SINHVIEN', 'NVCB');
    SP_GRANT_EXECUTE('SP_ALL_UPDATE_SINHVIEN', 'SV');
END;
/