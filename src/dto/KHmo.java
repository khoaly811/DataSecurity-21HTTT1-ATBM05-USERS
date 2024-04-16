package dto;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.ObjectProperty;
import java.time.LocalDate;
//doi ten file

// DTO for KHMO table
public class KHmo {
    private final SimpleIntegerProperty MAHP = new SimpleIntegerProperty();
    private final SimpleIntegerProperty HOCKY = new SimpleIntegerProperty();
    private final SimpleIntegerProperty NAM = new SimpleIntegerProperty();
    private final SimpleStringProperty MACT = new SimpleStringProperty();
    private final SimpleObjectProperty<LocalDate> NGAYBD = new SimpleObjectProperty<>();
    private final ObjectProperty<Hocphan> hocphanProperty = new SimpleObjectProperty<>();
    private final ObjectProperty<Donvi> donviProperty = new SimpleObjectProperty<>();
    private final ObjectProperty<Nhansu> nhansuProperty = new SimpleObjectProperty<>();

    public KHmo(int HOCKY, int NAM, String MACT, LocalDate NGAYBD){
        // this.MAHP.set(MAHP);
        this.HOCKY.set(HOCKY);
        this.NAM.set(NAM);
        this.MACT.set(MACT);
        this.NGAYBD.set(NGAYBD);
    }
    public KHmo(){}
    // public KHmo() {
    //     hocphanProperty.set(new Hocphan());
    //     donviProperty.set(new Donvi());
    // }
    public Hocphan getHocphan() {
        return hocphanProperty.get();
    }

    public void setHocphan(Hocphan hocphan) {
        this.hocphanProperty.set(hocphan);
    }

    public ObjectProperty<Hocphan> hocphanProperty() {
        return hocphanProperty;
    }
    public Donvi getDonvi() {
        return donviProperty.get();
    }

    public void setDonvi(Donvi donvi) {
        this.donviProperty.set(donvi);
    }

    public ObjectProperty<Donvi> donviProperty() {
        return donviProperty;
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

    public int getMAHP(){
        return MAHP.get();
    }

    public void setMAHP(int MAHP){
        this.MAHP.set(MAHP);
    }

    public SimpleIntegerProperty MAHPproperty(){
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
