package dto;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleObjectProperty;
import java.time.LocalDate;

//DTO for THONGBAO table
public class ThongBao {
    private final SimpleStringProperty MATB = new SimpleStringProperty();
    private final SimpleStringProperty NOIDUNG= new SimpleStringProperty();
    private final SimpleObjectProperty<LocalDate> THOIGIAN = new SimpleObjectProperty<>();

    public ThongBao(){

    }

    public ThongBao(String MATB, String NOIDUNG, LocalDate THOIGIAN){
        this.MATB.set(MATB);
        this.NOIDUNG.set(NOIDUNG);
        this.THOIGIAN.set(THOIGIAN);
    }
    // Getters and setters
    public String getMATB(){
        return MATB.get();
    }

    public void setMATB(String MATB){
        this.MATB.set(MATB);
    }

    public SimpleStringProperty MATBproperty(){
        return MATB;
    }

    public String getNOIDUNG(){
        return NOIDUNG.get();
    }

    public void setNOIDUNG(String NOIDUNG){
        this.NOIDUNG.set(NOIDUNG);
    }

    public SimpleStringProperty NOIDUNGproperty(){
        return NOIDUNG;
    }

    public LocalDate getTHOIGIAN(){
        return THOIGIAN.get();
    }

    public void setTHOIGIAN(LocalDate THOIGIAN){
        this.THOIGIAN.set(THOIGIAN);
    }

    public SimpleObjectProperty<LocalDate> THOIGIANproperty(){
        return THOIGIAN;
    }
}