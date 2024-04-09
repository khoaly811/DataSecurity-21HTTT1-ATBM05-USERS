package dto;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Phancong {
    private final SimpleStringProperty MAGV = new SimpleStringProperty();
    private final SimpleStringProperty MAHP = new SimpleStringProperty();
    private final SimpleIntegerProperty HK = new SimpleIntegerProperty();
    private final SimpleIntegerProperty NAM = new SimpleIntegerProperty();
    private final SimpleStringProperty MACT = new SimpleStringProperty();
    

    public Phancong(){

    }
    
    public Phancong(String MAGV, String MAHP, int HK, int NAM, String MACT){
        this.MAGV.set(MAGV);
        this.MAHP.set(MAHP);
        this.HK.set(HK);
        this.NAM.set(NAM);
        this.MACT.set(MACT);
    }

    public String getMAGV(){
        return MAGV.get();
    }
    public void setMAGV(String MAGV){
        this.MAGV.set(MAGV);
    }
    public SimpleStringProperty MAGVproperty(){
        return MAGV;
    }

    public String getMAHP(){
        return MAHP.get();
    }
    public void setMAHP(String MAHP){
        this.MAHP.set(MAHP);
    }
    public SimpleStringProperty MAHPproperty(){
        return MAHP;
    }

    public int getHK(){
        return HK.get();
    }
    public void setHK(int HK){
        this.HK.set(HK);
    }
    public SimpleIntegerProperty HKproperty(){
        return HK;
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
}
