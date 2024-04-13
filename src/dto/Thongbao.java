package dto;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.sql.Time;
//doi ten file

//DTO for THONGBAO table
public class Thongbao {
    private final SimpleStringProperty MATB = new SimpleStringProperty();
    private final SimpleStringProperty NOIDUNG= new SimpleStringProperty();
    private final SimpleObjectProperty<Time> THOIGIAN = new SimpleObjectProperty<>();

    public Thongbao(){

    }

    public Thongbao(String MATB, String NOIDUNG,Time THOIGIAN){
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

    public Time getTHOIGIAN(){
        return THOIGIAN.get();
    }

    public void setTHOIGIAN(Time THOIGIAN){
        this.THOIGIAN.set(THOIGIAN);
    }

    public SimpleObjectProperty<Time> THOIGIANproperty(){
        return THOIGIAN;
    }
}
