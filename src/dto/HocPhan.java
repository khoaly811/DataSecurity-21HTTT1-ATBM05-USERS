package dto;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class HocPhan {
    private final SimpleStringProperty MAHP = new SimpleStringProperty();
    private final SimpleStringProperty TENHP = new SimpleStringProperty();
    private final SimpleIntegerProperty SOTC = new SimpleIntegerProperty();
    private final SimpleIntegerProperty SOTIETLT = new SimpleIntegerProperty();
    private final SimpleIntegerProperty SOTIETTH = new SimpleIntegerProperty();
    private final SimpleIntegerProperty SOSVTOIDA = new SimpleIntegerProperty();
    private final SimpleStringProperty MADV = new SimpleStringProperty();
    
    public HocPhan() {

    }

    public HocPhan(String MAHP, String TENHP, int SOTC, int SOTIETLT, int SOTIETTH, int SOSVTOIDA, String MADV){
        this.MAHP.set(MAHP);
        this.TENHP.set(TENHP);
        this.SOTC.set(SOTC);
        this.SOTIETLT.set(SOTIETLT);
        this.SOTIETTH.set(SOTIETTH);
        this.SOSVTOIDA.set(SOSVTOIDA);
        this.MADV.set(MADV);
    }

    // Getters and setters
   public String getMAHP(){
        return MAHP.get();
    }

    public void setMAHP(String MAHP){
        this.MAHP.set(MAHP);
    }

    public SimpleStringProperty MAHPproperty(){
        return MAHP;
    }

    public String getTENHP(){
        return TENHP.get();
    }

    public void setTENHP(String TENHP){
        this.TENHP.set(TENHP);
    }

    public SimpleStringProperty TENHPproperty(){
        return TENHP;
    }

    public int getSOTC() {
        return SOTC.get();
    }
    
    public void setSOTC(int SOTC) {
        this.SOTC.set(SOTC);
    }

    public SimpleIntegerProperty SOTCproperty(){
        return SOTC;
    }

    public int getSOTIETLT() {
        return SOTIETLT.get();
    }
    
    public void setSOTIETLT(int SOTIETLT) {
        this.SOTIETLT.set(SOTIETLT);
    }

    public SimpleIntegerProperty SOTIETLTproperty(){
        return SOTIETLT;
    }

     public int getSOTIETTH() {
        return SOTIETTH.get();
    }
    
    public void setSOTIETT(int SOTIETTH) {
        this.SOTIETTH.set(SOTIETTH);
    }

    public SimpleIntegerProperty SOTIETTHproperty(){
        return SOTIETTH;
    }

     public int getSOSVTOIDA() {
        return SOSVTOIDA.get();
    }
    
    public void setSOTCTL(int SOSVTOIDA) {
        this.SOSVTOIDA.set(SOSVTOIDA);
    }

    public SimpleIntegerProperty SOSVTOIDAproperty(){
        return SOSVTOIDA;
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