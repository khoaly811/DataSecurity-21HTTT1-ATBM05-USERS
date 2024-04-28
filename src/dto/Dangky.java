package dto;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.ObjectProperty;

//doi ten file
public class Dangky {
    private final SimpleStringProperty MASV = new SimpleStringProperty();
    private final SimpleStringProperty MAGV = new SimpleStringProperty();
    private final SimpleIntegerProperty MAHP = new SimpleIntegerProperty();
    private final SimpleIntegerProperty HK = new SimpleIntegerProperty();
    private final SimpleIntegerProperty NAM = new SimpleIntegerProperty();
    private final SimpleStringProperty MACT = new SimpleStringProperty();
    private final SimpleIntegerProperty DIEMTH = new SimpleIntegerProperty();
    private final SimpleIntegerProperty DIEMQT = new SimpleIntegerProperty();
    private final SimpleIntegerProperty DIEMCK = new SimpleIntegerProperty();
    private final SimpleIntegerProperty DIEMTK = new SimpleIntegerProperty();
    private final ObjectProperty<Sinhvien> sinhvienProperty = new SimpleObjectProperty<>();
    private final ObjectProperty<Nhansu> nhansuProperty = new SimpleObjectProperty<>();
    private final ObjectProperty<Hocphan> hocphanProperty = new SimpleObjectProperty<>();

    public Dangky(){

    }
    
    public Dangky(String MASV, String MAGV, int MAHP, int HK, int NAM, String MACT, int DIEMTH, int DIEMQT, int DIEMCK, int DIEMTK){
        this.MASV.set(MASV);
        this.MAGV.set(MAGV);
        this.MAHP.set(MAHP);
        this.HK.set(HK);
        this.NAM.set(NAM);
        this.MACT.set(MACT);
        this.DIEMTH.set(DIEMTH);
        this.DIEMQT.set(DIEMQT);
        this.DIEMCK.set(DIEMCK);
        this.DIEMTK.set(DIEMTK);
    }
    public Sinhvien getSinhvien() {
        return sinhvienProperty.get();
    }

    public void setSinhvien(Sinhvien sinhvien) {
        this.sinhvienProperty.set(sinhvien);
    }

    public ObjectProperty<Sinhvien> sinhvienProperty() {
        return sinhvienProperty;
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
    public String getMASV(){
        return MASV.get();
    }
    public void setMASV(String MASV){
        this.MASV.set(MASV);
    }
    public SimpleStringProperty MASVproperty(){
        return MASV;
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
    public int getDIEMTH() {
        return DIEMTH.get();
    }
    
    public void setDIEMTH(int DIEMTH) {
        this.DIEMTH.set(DIEMTH);
    }

    public SimpleIntegerProperty DIEMTHproperty(){
        return DIEMTH;
    }
    public int getDIEMQT() {
        return DIEMQT.get();
    }
    
    public void setDIEMQT(int DIEMQT) {
        this.DIEMQT.set(DIEMQT);
    }

    public SimpleIntegerProperty DIEMQTproperty(){
        return DIEMQT;
    }
    public int getDIEMCK() {
        return DIEMCK.get();
    }
    
    public void setDIEMCK(int DIEMCK) {
        this.DIEMCK.set(DIEMCK);
    }

    public SimpleIntegerProperty DIEMCKproperty(){
        return DIEMCK;
    }
    public int getDIEMTK() {
        return DIEMTK.get();
    }
    
    public void setDIEMTK(int DIEMTK) {
        this.DIEMTK.set(DIEMTK);
    }

    public SimpleIntegerProperty DIEMTKproperty(){
        return DIEMTK;
    }
}
