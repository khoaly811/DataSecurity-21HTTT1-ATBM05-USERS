package dto;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

//doi ten file

public class Donvi {
    private final SimpleStringProperty MADV = new SimpleStringProperty();
    private final SimpleStringProperty TENDV = new SimpleStringProperty();;
    private final SimpleStringProperty TRUONGDV = new SimpleStringProperty();;
    private final ObjectProperty<Nhansu> nhansuProperty = new SimpleObjectProperty<>();

    public Donvi(){

    }

    public Donvi(String MADV, String TENDV, String TRUONGDV){
        this.MADV.set(MADV);
        this.TENDV.set(TENDV);
        this.TRUONGDV.set(TRUONGDV);
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

    // Getters and setters
    public String getMADV(){
        return MADV.get();
    }
    public void setMADV(String MADV){
        this.MADV.set(MADV);
    }
    public SimpleStringProperty MADVproperty(){
        return MADV;
    }

    public String getTENDV() {
        return TENDV.get();
    }

    public void setTENDV(String TENDV) {
        this.TENDV.set(TENDV);
    }

    public SimpleStringProperty TENDVproperty(){
        return TENDV;
    }
    
    public String getTRUONGDV() {
        return TRUONGDV.get();
    }

    public void setTRUONGDV(String TRUONGDV) {
        this.TRUONGDV.set(TRUONGDV);
    }

    public SimpleStringProperty TRUONGDVproperty(){
        return TRUONGDV;
    }
}