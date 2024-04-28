package dto;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.sql.Timestamp;
//doi ten file

//DTO for DBA_AUDIT_TRAIL table
public class DBA_AUDIT_TRAIL {
    private final SimpleStringProperty USERNAME = new SimpleStringProperty();
    private final SimpleStringProperty OBJ_NAME= new SimpleStringProperty();
    private final SimpleStringProperty ACTION_NAME= new SimpleStringProperty();
    private final SimpleObjectProperty<Timestamp> TIMESTAMP = new SimpleObjectProperty<>();

    public DBA_AUDIT_TRAIL(){

    }

    public DBA_AUDIT_TRAIL(String USERNAME, String ACTION_NAME, String OBJ_NAME,Timestamp TIMESTAMP){
        this.USERNAME.set(USERNAME);
        this.OBJ_NAME.set(OBJ_NAME);
        this.ACTION_NAME.set(ACTION_NAME);
        this.TIMESTAMP.set(TIMESTAMP);
    }
    // Getters and setters
    public String getUSERNAME(){
        return USERNAME.get();
    }

    public void setUSERNAME(String USERNAME){
        this.USERNAME.set(USERNAME);
    }

    public SimpleStringProperty USERNAMEproperty(){
        return USERNAME;
    }

    public String getOBJ_NAME(){
        return OBJ_NAME.get();
    }

    public void setOBJ_NAME(String OBJ_NAME){
        this.OBJ_NAME.set(OBJ_NAME);
    }

    public SimpleStringProperty OBJ_NAMEproperty(){
        return OBJ_NAME;
    }

    public Timestamp getTIMESTAMP(){
        return TIMESTAMP.get();
    }

    public void setTIMESTAMP(Timestamp TIMESTAMP){
        this.TIMESTAMP.set(TIMESTAMP);
    }

    public SimpleObjectProperty<Timestamp> TIMESTAMPproperty(){
        return TIMESTAMP;
    }

    public String getACTION_NAME(){
        return ACTION_NAME.get();
    }

    public void setACTION_NAME(String ACTION_NAME){
        this.ACTION_NAME.set(ACTION_NAME);
    }

    public SimpleStringProperty ACTION_NAMEproperty(){
        return ACTION_NAME;
    }


}
