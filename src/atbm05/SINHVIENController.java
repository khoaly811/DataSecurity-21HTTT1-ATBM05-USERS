package atbm05;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import oracle.jdbc.OracleTypes;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import DataAccessLayer.DataAccessLayer;
import dto.*;


public class SINHVIENController {
    @FXML
    private TableView<Sinhvien> sinhvienTableView;

    @FXML
    private TableColumn<Sinhvien, String> HOTEN;

    @FXML
    private TableColumn<Sinhvien, String> PHAI;

    @FXML
    private TableColumn<Sinhvien, String> NGSINH;

    @FXML
    private TableColumn<Sinhvien, String> DIACHI;

    @FXML
    private TableColumn<Sinhvien, String> SDT;

    @FXML
    private TableColumn<Sinhvien, String> MACT;

    @FXML
    private TableColumn<Sinhvien, String> MANGANH;

    @FXML
    private TableColumn<Sinhvien, String> SOTCTL;

    @FXML
    private TableColumn<Sinhvien, String> DIEMTBTL;

    @FXML
    private void onAddClick_SINHVIEN() {
        System.out.println("Added");
    }

    @FXML
    private void onUpdateClick_SINHVIEN() {
        System.out.println("Updated");
    }

    @FXML
    private void onDeleteClick_SINHVIEN() {
        System.out.println("Delete");
    }

    @FXML
    private TextField hotenDisplay;

    @FXML
    private TextField phaiDisplay;

    @FXML
    private TextField diachiDisplay;

    @FXML
    private TextField ngsinhDisplay;

    @FXML
    private TextField dienthoaiDisplay;

    @FXML
    private TextField chuongtrinhDisplay;

    @FXML
    private TextField nganhDisplay;

    @FXML
    private TextField sotctlDisplay;

    @FXML
    private TextField diemtbtlDisplay;

    @FXML
    private Button updateSV;

    @FXML
    private TextField searchSV;


    private ObservableList<Sinhvien> sinhvienList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        HOTEN.setCellValueFactory(cellData -> cellData.getValue().HOTENproperty());
        PHAI.setCellValueFactory(cellData -> cellData.getValue().PHAIproperty());
        NGSINH.setCellValueFactory(cellData -> cellData.getValue().NGSINHproperty().asString());
        DIACHI.setCellValueFactory(cellData -> cellData.getValue().DIACHIproperty());
        SDT.setCellValueFactory(cellData -> cellData.getValue().SDTproperty());
        MACT.setCellValueFactory(cellData -> cellData.getValue().MACTproperty());
        MANGANH.setCellValueFactory(cellData->cellData.getValue().MANGANHproperty());
        SOTCTL.setCellValueFactory(cellData -> cellData.getValue().SOTCTLproperty().asString());
        DIEMTBTL.setCellValueFactory(cellData -> cellData.getValue().DIEMTBTLproperty().asString());
        sinhvienList = FXCollections.observableArrayList();
        loadSinhvienFromDatabase();

        // Add listener to the TableView selection model
        sinhvienTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                hotenDisplay.setText(newSelection.getHOTEN());
                phaiDisplay.setText(newSelection.getPHAI());
                ngsinhDisplay.setText(newSelection.getNGSINH().toString());
                diachiDisplay.setText(newSelection.getDIACHI());
                dienthoaiDisplay.setText(newSelection.getSDT());
                chuongtrinhDisplay.setText(newSelection.getMACT());
                nganhDisplay.setText(newSelection.getMANGANH());
                sotctlDisplay.setText(String.valueOf(newSelection.getSOTCTL()));
                diemtbtlDisplay.setText(String.valueOf(newSelection.getDIEMTBTL()));
            }
        });

        searchSV.textProperty().addListener((observable, oldValue, newValue) -> {
            searcSinhvien(newValue);
        });
    }


    private void searcSinhvien(String searchText) {
        DataAccessLayer dal = null;
        Connection conn = null;
        CallableStatement cst = null;
        ResultSet rs = null;
        List<Sinhvien>  sinhvienList= new ArrayList<>();
        try {
            dal = DataAccessLayer.getInstance("your_username", "your_password");
            conn = dal.connect();

            // Construct the wildcard pattern for partial matching

            cst = conn.prepareCall("{CALL C##QLK.SP_SEARCH_SINHVIEN(?, ?)}");
            cst.registerOutParameter(1, OracleTypes.CURSOR);
            cst.setString(2, searchText);
            cst.execute();
            System.out.println("nhan beo 2");
            rs = (ResultSet) cst.getObject(1);
            System.out.println("Nhan beo 3");
            while (rs.next()) {
                Sinhvien sv = new Sinhvien();
                sv.setHOTEN(rs.getString("HOTEN"));
                sv.setPHAI(rs.getString("PHAI"));
                sv.setNGSINH(rs.getDate("NGSINH").toLocalDate());
                sv.setDIACHI((rs.getString("DCHI")));
                sv.setSDT(rs.getString("DT"));
                sv.setMACT(rs.getString("MACT"));
                sv.setMANGANH(rs.getString("MANGANH"));
                sv.setSOTCTL(rs.getInt("SOTCTL"));
                sv.setDIEMTBTL(rs.getFloat("DTBTL"));
                sinhvienList.add(sv);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        // Set the loaded users to the table view
        sinhvienTableView.setItems(FXCollections.observableArrayList(sinhvienList));

    }

    private void loadSinhvienFromDatabase() {
        DataAccessLayer dal = null;
        Connection conn = null;
        CallableStatement cst = null;
        ResultSet rs = null;

        try {
            dal = DataAccessLayer.getInstance("your_username", "your_password");
            conn = dal.connect();
            cst = conn.prepareCall("{CALL C##QLK.SP_VIEW_SINHVIEN(?)}");
            cst.registerOutParameter(1, OracleTypes.CURSOR);
            System.out.println("nhan beo 1");
            cst.execute();
            System.out.println("nhan beo 2");
            rs = (ResultSet) cst.getObject(1);
            System.out.println("Nhan beo 3");
            while (rs.next()) {
                Sinhvien sv = new Sinhvien();
                sv.setHOTEN(rs.getString("HOTEN"));
                sv.setPHAI(rs.getString("PHAI"));
                sv.setNGSINH(rs.getDate("NGSINH").toLocalDate());
                sv.setDIACHI((rs.getString("DCHI")));
                sv.setSDT(rs.getString("DT"));
                sv.setMACT(rs.getString("MACT"));
                sv.setMANGANH(rs.getString("MANGANH"));
                sv.setSOTCTL(rs.getInt("SOTCTL"));
                sv.setDIEMTBTL(rs.getFloat("DTBTL"));
                sinhvienList.add(sv);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        // Set the loaded users to the table view
        sinhvienTableView.setItems(sinhvienList);
    }
    @FXML
    private void updateSVClick(ActionEvent action){

    }
    @FXML
    private void insertSVClick(ActionEvent action){

    }
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    @FXML
    private void refreshTable(ActionEvent event) {
        sinhvienList.clear();
        loadSinhvienFromDatabase();
        sinhvienTableView.refresh();
    }
}
