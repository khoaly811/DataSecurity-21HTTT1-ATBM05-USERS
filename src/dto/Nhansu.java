package dto;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleObjectProperty;
import java.time.LocalDate;

public class Nhansu {
    private final SimpleStringProperty MANV = new SimpleStringProperty();
    private final SimpleStringProperty HOTEN = new SimpleStringProperty();
    private final SimpleStringProperty PHAI = new SimpleStringProperty();
    private final SimpleObjectProperty<LocalDate> NGSINH = new SimpleObjectProperty<>();
    private final SimpleIntegerProperty PHUCAP = new SimpleIntegerProperty();
    private final SimpleStringProperty DT = new SimpleStringProperty();
    private final SimpleStringProperty VAITRO = new SimpleStringProperty();
    private final SimpleStringProperty MADV = new SimpleStringProperty();

    public Nhansu(){

    }
    public Nhansu(String MANV, String HOTEN){
        this.MANV.set(MANV);
        this.HOTEN.set(HOTEN);
    }
    public Nhansu(String MANV, String HOTEN, String PHAI, LocalDate NGSINH, int PHUCAP, String DT, String VAITRO, String MADV){
        this.MANV.set(MANV);
        this.HOTEN.set(HOTEN);
        this.PHAI.set(PHAI);
        this.NGSINH.set(NGSINH);
        this.PHUCAP.set(PHUCAP);
        this.DT.set(DT);
        this.VAITRO.set(VAITRO);
        this.MADV.set(MADV);
    }
    public String getMANV(){
        return MANV.get();
    }
    public void setMANV(String MANV){
        this.MANV.set(MANV);
    }
    public SimpleStringProperty MANVproperty(){
        return MANV;
    }

    public String getHOTEN(){
        return HOTEN.get();
    }
    public void setHOTEN(String HOTEN){
        this.HOTEN.set(HOTEN);
    }
    public SimpleStringProperty HOTENproperty(){
        return HOTEN;
    }

    public String getPHAI(){
        return PHAI.get();
    }
    public void setPHAI(String PHAI){
        this.PHAI.set(PHAI);
    }
    public SimpleStringProperty PHAIproperty(){
        return PHAI;
    }

    public LocalDate getNGSINH(){
        return NGSINH.get();
    }
    public void setNGSINH(LocalDate NGSINH){
        this.NGSINH.set(NGSINH);
    }
    public SimpleObjectProperty<LocalDate> NGSINHproperty(){
        return NGSINH;
    }
    public int getPHUCAP(){
        return PHUCAP.get();
    }
    public void setPHUCAP(int PHUCAP){
        this.PHUCAP.set(PHUCAP);
    }
    public SimpleIntegerProperty PHUCAPproperty(){
        return PHUCAP;
    }
    public String getDT(){
        return DT.get();
    }
    public void setDT(String DT){
        this.DT.set(DT);
    }
    public SimpleStringProperty DTproperty(){
        return DT;
    }
    public String getVAITRO(){
        return VAITRO.get();
    }
    public void setVAITRO(String VAITRO){
        this.VAITRO.set(VAITRO);
    }
    public SimpleStringProperty VAITROproperty(){
        return VAITRO;
    }
    public String getMADV(){
        return MADV.get();
    }
    public void setMADV(String MADV){
        this.MADV.set(MADV);
    }
    public SimpleStringProperty MADVproperty(){
        return MADV;
    }
}