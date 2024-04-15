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

-- TONG HOP SP UDPATE
CREATE OR REPLACE PROCEDURE SP_ALL_UPDATE_NHANSU(
    INP_HOTEN IN VARCHAR2,
    INP_PHAI IN VARCHAR2,
    INP_NGSINH IN DATE,
    INP_PHUCAP IN NUMBER,
    INP_DT IN VARCHAR2,
    INP_MANV IN VARCHAR2
) AUTHID CURRENT_USER AS
    V_ROLE VARCHAR2(30);
BEGIN
    SELECT * INTO V_ROLE FROM SESSION_ROLES;
    IF v_role = 'TKHOA' THEN
        UPDATE C##QLK.NHANSU
        SET HOTEN = INP_HOTEN,
            PHAI = INP_PHAI,
            NGSINH = INP_NGSINH,
            PHUCAP = INP_PHUCAP,
            DT = INP_DT
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
