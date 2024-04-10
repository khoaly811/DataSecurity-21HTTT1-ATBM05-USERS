package dto;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import java.time.LocalDate;

// DTO for KHMO table
public class KHmo {
    private final SimpleStringProperty MAHP = new SimpleStringProperty();
    private final SimpleIntegerProperty HOCKY = new SimpleIntegerProperty();
    private final SimpleIntegerProperty NAM = new SimpleIntegerProperty();
    private final SimpleStringProperty MACT = new SimpleStringProperty();
    private final SimpleObjectProperty<LocalDate> NGAYBD = new SimpleObjectProperty<>();
    
    public KHmo(){

    }

    public KHmo(String MAHP, int HOCKY, int NAM, String MACT, LocalDate NGAYBD){
        this.MAHP.set(MAHP);
        this.HOCKY.set(HOCKY);
        this.NAM.set(NAM);
        this.MACT.set(MACT);
        this.NGAYBD.set(NGAYBD);
    }

    // Getters and setters
    public String getMAHP(){
        return MAHP.get();
    }

    public void setMAHP(String MAHP){
        this.MAHP.set(MAHP);
    }

    public SimpleStringProperty MAHPproperty(){
        return MAHP;
    }

    public int getHOCKY(){
        return HOCKY.get();
    }

    public void setHOCKY(int HOCKY){
        this.HOCKY.set(HOCKY);
    }

    public SimpleIntegerProperty HOCKYproperty(){
        return HOCKY;
    }

    public int getNAM(){
        return NAM.get();
    }

    public void setNAM(int NAM){
        this.NAM.set(NAM);
    }

    public SimpleIntegerProperty NAMproperty(){
        return NAM;
    }

    public String getMACT(){
        return MACT.get();
    }

    public void setMACT(String MACT){
        this.MACT.set(MACT);
    }

    public SimpleStringProperty MACTproperty(){
        return MACT;
    }

    public LocalDate getNGAYBD(){
        return NGAYBD.get();
    }

    public void setNGAYBD(LocalDate NGAYBD){
        this.NGAYBD.set(NGAYBD);
    }

    public SimpleObjectProperty<LocalDate> NGAYBDproperty(){
        return NGAYBD;
    }
}