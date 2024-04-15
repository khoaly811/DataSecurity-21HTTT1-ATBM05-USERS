package atbm05;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import oracle.jdbc.OracleTypes;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import DataAccessLayer.DataAccessLayer;
import dto.Dangky;
import dto.Nhansu;
import dto.Sinhvien;
import dto.Hocphan;

public class KETQUADANGKYController {
    @FXML
    private TableView<Dangky> dangkyTableView;

    @FXML
    private TableColumn<Dangky, String> HOTENSV;

    @FXML
    private TableColumn<Dangky, String> HOTENGV;

    @FXML
    private TableColumn<Dangky, String> TENHP;

    @FXML
    private TableColumn<Dangky, String> HK;

    @FXML
    private TableColumn<Dangky, String> NAM;

    @FXML
    private TableColumn<Dangky, String> MACT;

    @FXML
    private TableColumn<Dangky, String> DIEMTH;
    @FXML
    private TableColumn<Dangky, String> DIEMQT;
    @FXML
    private TableColumn<Dangky, String> DIEMCK;
    @FXML
    private TableColumn<Dangky, String> DIEMTK;
    @FXML
    private TableColumn<Dangky, String> MASV;
    @FXML
    private TableColumn<Dangky, String> MAGV;

    @FXML
    private TextField hotensvDisplay;
    @FXML
    private TextField hotengvDisplay;
    @FXML
    private TextField tenhocphanDisplay;
    @FXML
    private TextField hkDisplay;
    @FXML
    private TextField namDisplay;
    @FXML
    private TextField diemthDisplay;
    @FXML
    private TextField diemqtDisplay;
    @FXML
    private TextField diemckDisplay;
    @FXML
    private TextField diemtkDisplay;
    @FXML
    private TextField mactDisplay;
    @FXML
    private TextField masvDisplay;
    @FXML
    private TextField magvDisplay;

    @FXML
    private void onAddClick_DANGKY() {
        System.out.println("Added");
    }

    private ObservableList<Dangky> dangkyList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Initialize table columns
        HOTENSV.setCellValueFactory(cellData -> cellData.getValue().getSinhvien().HOTENproperty());
        HOTENGV.setCellValueFactory(cellData -> cellData.getValue().getNhansu().HOTENproperty());
        TENHP.setCellValueFactory(cellData -> cellData.getValue().getHocphan().TENHPproperty());
        HK.setCellValueFactory(cellData -> cellData.getValue().HKproperty().asString());
        NAM.setCellValueFactory(cellData -> cellData.getValue().NAMproperty().asString());
        DIEMTH.setCellValueFactory(cellData -> cellData.getValue().DIEMTHproperty().asString());
        DIEMQT.setCellValueFactory(cellData -> cellData.getValue().DIEMQTproperty().asString());
        DIEMCK.setCellValueFactory(cellData -> cellData.getValue().DIEMCKproperty().asString());
        DIEMTK.setCellValueFactory(cellData -> cellData.getValue().DIEMTKproperty().asString());
        MASV.setCellValueFactory(cellData -> cellData.getValue().MASVproperty());
        MAGV.setCellValueFactory(cellData -> cellData.getValue().MAGVproperty());
        MACT.setCellValueFactory(cellData -> cellData.getValue().MACTproperty());
        dangkyList = FXCollections.observableArrayList();
        loadDangkyFromDatabase();

        dangkyTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                hotensvDisplay.setText(newSelection.getSinhvien().getHOTEN());
                hotengvDisplay.setText(newSelection.getNhansu().getHOTEN());
                tenhocphanDisplay.setText(newSelection.getHocphan().getTENHP());
                mactDisplay.setText(newSelection.getMACT());
                hkDisplay.setText(String.valueOf(newSelection.getHK()));
                namDisplay.setText(String.valueOf(newSelection.getNAM()));
                diemthDisplay.setText(String.valueOf(newSelection.getDIEMTH()));
                diemqtDisplay.setText(String.valueOf(newSelection.getDIEMQT()));
                diemckDisplay.setText(String.valueOf(newSelection.getDIEMCK()));
                diemtkDisplay.setText(String.valueOf(newSelection.getDIEMTK()));
                mactDisplay.setText(String.valueOf(newSelection.getMACT()));
                masvDisplay.setText(newSelection.getSinhvien().getMASV());
                magvDisplay.setText(newSelection.getNhansu().getMANV());
                
            }
        });
    }

    private void loadDangkyFromDatabase() {
        DataAccessLayer dal = null;
        Connection conn = null;
        CallableStatement cst = null;
        ResultSet rs = null;

        try {
            dal = DataAccessLayer.getInstance("your_username", "your_password");
            conn = dal.connect();
            cst = conn.prepareCall("{CALL C##QLK.SP_VIEW_DANGKY(?)}");
            cst.registerOutParameter(1, OracleTypes.CURSOR);
            System.out.println("nhan beo 1");
            cst.execute();
            System.out.println("nhan beo 2");
            rs = (ResultSet) cst.getObject(1);
            System.out.println("Nhan beo 3");
            while (rs.next()) {
                Dangky dk = new Dangky();
                Sinhvien sv = new Sinhvien();
                Hocphan hp = new Hocphan();
                Nhansu ns = new Nhansu();

                String tensv = rs.getString("HOTEN_SV");
                //System.out.println("TEN SINH VIEN: " + tensv);
                sv.setHOTEN(tensv);
                dk.setSinhvien(sv);

                String tengv = rs.getString("HOTEN_GV");
                //System.out.println("TEN GIANG VIEN: " + tengv);
                ns.setHOTEN(tengv);
                dk.setNhansu(ns);

                String tenhp = rs.getString("TENHP");
                //System.out.println("TEN HOC PHAN: " + tenhp);
                hp.setTENHP(tenhp);
                dk.setHocphan(hp);

                dk.setHK((rs.getInt("HK")));
                dk.setNAM((rs.getInt("NAM")));
                dk.setMACT((rs.getString("MACT")));
                dk.setDIEMTH((rs.getFloat("DIEMTH")));
                dk.setDIEMQT((rs.getFloat("DIEMQT")));
                dk.setDIEMCK((rs.getFloat("DIEMCK")));
                dk.setDIEMTK((rs.getFloat("DIEMTK")));

                dangkyList.add(dk);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            
        }

        // Set the loaded users to the table view
        dangkyTableView.setItems(dangkyList);
    }

    @FXML
    private void deleteDKClick(ActionEvent event) {
    }

    @FXML
    private void updateDKClick(ActionEvent event) {
    
    }


    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}