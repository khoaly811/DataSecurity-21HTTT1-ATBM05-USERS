package dto;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.sql.Timestamp;
//doi ten file

//DTO for DBA_FGA_AUDIT_TRAIL table
public class DBA_FGA_AUDIT_TRAIL {
    private final SimpleStringProperty DB_USER = new SimpleStringProperty();
    private final SimpleStringProperty OBJECT_NAME= new SimpleStringProperty();
    private final SimpleStringProperty STATEMENT_TYPE= new SimpleStringProperty();
    private final SimpleStringProperty SQL_TEXT= new SimpleStringProperty();
    private final SimpleObjectProperty<Timestamp> EXTENDED_TIMESTAMP = new SimpleObjectProperty<>();

    public DBA_FGA_AUDIT_TRAIL(){

    }

    public DBA_FGA_AUDIT_TRAIL(String DB_USER, String STATEMENT_TYPE, String SQL_TEXT, String OBJECT_NAME,Timestamp EXTENDED_TIMESTAMP){
        this.DB_USER.set(DB_USER);
        this.OBJECT_NAME.set(OBJECT_NAME);
        this.STATEMENT_TYPE.set(STATEMENT_TYPE);
        this.SQL_TEXT.set(SQL_TEXT);
        this.EXTENDED_TIMESTAMP.set(EXTENDED_TIMESTAMP);
    }
    // Getters and setters
    public String getDB_USER(){
        return DB_USER.get();
    }

    public void setDB_USER(String DB_USER){
        this.DB_USER.set(DB_USER);
    }

    public SimpleStringProperty DB_USERproperty(){
        return DB_USER;
    }

    public String getOBJECT_NAME(){
        return OBJECT_NAME.get();
    }

    public void setOBJECT_NAME(String OBJECT_NAME){
        this.OBJECT_NAME.set(OBJECT_NAME);
    }

    public SimpleStringProperty OBJECT_NAMEproperty(){
        return OBJECT_NAME;
    }

    public Timestamp getEXTENDED_TIMESTAMP(){
        return EXTENDED_TIMESTAMP.get();
    }

    public void setEXTENDED_TIMESTAMP(Timestamp EXTENDED_TIMESTAMP){
        this.EXTENDED_TIMESTAMP.set(EXTENDED_TIMESTAMP);
    }

    public SimpleObjectProperty<Timestamp> EXTENDED_TIMESTAMPproperty(){
        return EXTENDED_TIMESTAMP;
    }

    public String getSTATEMENT_TYPE(){
        return STATEMENT_TYPE.get();
    }

    public void setSTATEMENT_TYPE(String STATEMENT_TYPE){
        this.STATEMENT_TYPE.set(STATEMENT_TYPE);
    }

    public SimpleStringProperty STATEMENT_TYPEproperty(){
        return STATEMENT_TYPE;
    }

    public String getSQL_TEXT(){
        return SQL_TEXT.get();
    }

    public void setSQL_TEXT(String SQL_TEXT){
        this.SQL_TEXT.set(SQL_TEXT);
    }

    public SimpleStringProperty SQL_TEXTproperty(){
        return SQL_TEXT;
    }
}
