package dto;
import javafx.beans.property.SimpleStringProperty;


public class DonVi {
    private final SimpleStringProperty MADV = new SimpleStringProperty();
    private final SimpleStringProperty TENDV = new SimpleStringProperty();;
    private final SimpleStringProperty TRUONGDV = new SimpleStringProperty();;
    
    public DonVi(){

    }

    public DonVi(String MADV, String TENDV, String TRUONGDV){
        this.MADV.set(MADV);
        this.TENDV.set(TENDV);
        this.TRUONGDV.set(TRUONGDV);
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