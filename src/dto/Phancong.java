package dto;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.ObjectProperty;
//doi ten file

public class Phancong {
    private final SimpleStringProperty MAGV = new SimpleStringProperty();
    private final SimpleIntegerProperty MAHP = new SimpleIntegerProperty();
    private final SimpleIntegerProperty HK = new SimpleIntegerProperty();
    private final SimpleIntegerProperty NAM = new SimpleIntegerProperty();
    private final SimpleStringProperty MACT = new SimpleStringProperty();
    private final ObjectProperty<Nhansu> nhansuProperty = new SimpleObjectProperty<>();
    private final ObjectProperty<Hocphan> hocphanProperty = new SimpleObjectProperty<>();

    public Phancong(){

    }
    
    public Phancong(String MAGV, int MAHP, int HK, int NAM, String MACT){
        this.MAGV.set(MAGV);
        this.MAHP.set(MAHP);
        this.HK.set(HK);
        this.NAM.set(NAM);
        this.MACT.set(MACT);
    }

    public Nhansu getNhansu() {
        return nhansuProperty.get();
    }

    public void setNhansu(Nhansu nhansu) {
        this.nhansuProperty.set(nhansu);
    }

    public ObjectProperty<Nhansu> nhansuProperty() {
        return nhansuProperty;
    }
    public Hocphan getHocphan() {
        return hocphanProperty.get();
    }

    public void setHocphan(Hocphan hocphan) {
        this.hocphanProperty.set(hocphan);
    }

    public ObjectProperty<Hocphan> hocphanProperty() {
        return hocphanProperty;
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

    public int getMAHP(){
        return MAHP.get();
    }
    public void setMAHP(int MAHP){
        this.MAHP.set(MAHP);
    }
    public SimpleIntegerProperty MAHPproperty(){
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
