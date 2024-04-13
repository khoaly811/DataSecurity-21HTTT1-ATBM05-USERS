package dto;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.ObjectProperty;
import java.time.LocalDate;
//doi ten file

public class Sinhvien {
    private final SimpleStringProperty MASV = new SimpleStringProperty();
    private final SimpleStringProperty HOTEN = new SimpleStringProperty();
    private final SimpleStringProperty PHAI = new SimpleStringProperty();
    private final SimpleObjectProperty<LocalDate> NGSINH = new SimpleObjectProperty<>();
    private final SimpleStringProperty DIACHI = new SimpleStringProperty();
    private final SimpleStringProperty SDT = new SimpleStringProperty();
    private final SimpleStringProperty MACT = new SimpleStringProperty();
    private final SimpleStringProperty MANGANH = new SimpleStringProperty();
    private final ObjectProperty<Donvi> donviProperty = new SimpleObjectProperty<>();
    private final SimpleIntegerProperty SOTCTL = new SimpleIntegerProperty();
    private final SimpleFloatProperty DIEMTBTL = new SimpleFloatProperty();
    
    public Sinhvien(){

    }

    public Sinhvien(String MASV, String HOTEN, String PHAI, LocalDate NGSINH, String DIACHI, String SDT, String MACT,String MANGANH, int SOTCTL, float DIEMTBTL){
        this.MASV.set(MASV);
        this.HOTEN.set(HOTEN);
        this.PHAI.set(PHAI);
        this.NGSINH.set(NGSINH);
        this.DIACHI.set(DIACHI);
        this.SDT.set(SDT);
        this.MACT.set(MACT);
        this.MANGANH.set(MANGANH);
        this.SOTCTL.set(SOTCTL);
        this.DIEMTBTL.set(DIEMTBTL);
    }

    // Getters and setters

    public String getMASV() {
        return MASV.get();
    }

    public void setMASV(String MASV) {
        this.MASV.set(MASV);
    }

    public SimpleStringProperty MASVproperty(){
        return MASV;
    }

    public String getHOTEN() {
        return HOTEN.get();
    }

    public void setHOTEN(String HOTEN) {
        this.HOTEN.set(HOTEN);
    }
    
    public SimpleStringProperty HOTENproperty(){
        return HOTEN;
    }
    public String getPHAI() {
        return PHAI.get();
    }

    public void setPHAI(String PHAI) {
        this.PHAI.set(PHAI);
    }
    
    public SimpleStringProperty PHAIproperty(){
        return PHAI;
    }
    public LocalDate getNGSINH() {
        return NGSINH.get();
    }

    public void setNGSINH(LocalDate NGSINH) {
        this.NGSINH.set(NGSINH);
    }
    
    public SimpleObjectProperty<LocalDate> NGSINHproperty(){
        return NGSINH;
    }

    public String getDIACHI() {
        return DIACHI.get();
    }

    public void setDIACHI(String DIACHI) {
        this.DIACHI.set(DIACHI);
    }

    public SimpleStringProperty DIACHIproperty(){
        return DIACHI;
    }
    
    public String getSDT() {
        return SDT.get();
    }

    public void setSDT(String SDT) {
        this.SDT.set(SDT);
    }
    
    public SimpleStringProperty SDTproperty(){
        return SDT;
    }

    public String getMACT() {
        return MACT.get();
    }

    public void setMACT(String MACT) {
        this.MACT.set(MACT);
    }

    public SimpleStringProperty MACTproperty(){
        return MACT;
    }

    public String getMANGANH() {
        return MANGANH.get();
    }
    
    public void setMANGANH(String MANGANH) {
        this.MANGANH.set(MANGANH);
    }

    public SimpleStringProperty MANGANHproperty(){
        return MANGANH;
    }
    
    public int getSOTCTL() {
        return SOTCTL.get();
    }
    
    public void setSOTCTL(int SOTCTL) {
        this.SOTCTL.set(SOTCTL);
    }

    public SimpleIntegerProperty SOTCTLproperty(){
        return SOTCTL;
    }

    public float getDIEMTBTL() {
        return DIEMTBTL.get();
    }
    
    public void setDIEMTBTL(float DIEMTBTL) {
        this.DIEMTBTL.set(DIEMTBTL);
    }

    public SimpleFloatProperty DIEMTBTLproperty(){
        return DIEMTBTL;
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
}
