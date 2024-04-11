package atbm05;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import oracle.jdbc.OracleTypes;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import DataAccessLayer.DataAccessLayer;
import dto.Donvi;
import dto.Sinhvien;


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
    private TableColumn<Sinhvien, String> TENDV;

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
    private TextField tendvDisplay;

    @FXML
    private TextField sotctlDisplay;

    @FXML
    private TextField diemtbtlDisplay;

    @FXML
    private Button updateNS;

    @FXML
    private Button profileButton;

    private ObservableList<Sinhvien> sinhvienList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        HOTEN.setCellValueFactory(cellData -> cellData.getValue().HOTENproperty());
        PHAI.setCellValueFactory(cellData -> cellData.getValue().PHAIproperty());
        NGSINH.setCellValueFactory(cellData -> cellData.getValue().NGSINHproperty().asString());
        DIACHI.setCellValueFactory(cellData -> cellData.getValue().DIACHIproperty());
        SDT.setCellValueFactory(cellData -> cellData.getValue().SDTproperty());
        MACT.setCellValueFactory(cellData -> cellData.getValue().MACTproperty());
        TENDV.setCellValueFactory(cellData -> cellData.getValue().getDonvi().TENDVproperty());
        SOTCTL.setCellValueFactory(cellData -> cellData.getValue().SOTCTLproperty().asString());
        DIEMTBTL.setCellValueFactory(cellData -> cellData.getValue().DIEMTBTLproperty().asString());
        sinhvienList = FXCollections.observableArrayList();
        loadSinhvienFromDatabase();

        // Add listener to the TableView selection model
        sinhvienTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                // Display the selected row's information in the corresponding TextFields
                hotenDisplay.setText(newSelection.getHOTEN());
                phaiDisplay.setText(newSelection.getPHAI());
                ngsinhDisplay.setText(newSelection.getNGSINH().toString());
                diachiDisplay.setText(newSelection.getDIACHI());
                dienthoaiDisplay.setText(newSelection.getSDT());
                chuongtrinhDisplay.setText(newSelection.getMACT());
                tendvDisplay.setText(newSelection.getDonvi().getTENDV());
                sotctlDisplay.setText(String.valueOf(newSelection.getSOTCTL()));
                diemtbtlDisplay.setText(String.valueOf(newSelection.getDIEMTBTL()));
            }
        });
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
                Donvi dv = new Donvi();
                sv.setHOTEN(rs.getString("HOTEN"));
                sv.setPHAI(rs.getString("PHAI"));
                sv.setNGSINH(rs.getDate("NGSINH").toLocalDate());
                sv.setDIACHI((rs.getString("DIACHI")));
                sv.setSDT(rs.getString("DT"));
                sv.setMACT(rs.getString("MACT"));
                
                String tendv = rs.getString("TENDV");
                System.out.println("TENDV là: " + tendv);
                dv.setTENDV(tendv);
                sv.setDonvi(dv);

                sv.setSOTCTL(rs.getInt("SOTCTL"));
                sv.setDIEMTBTL(rs.getFloat("DIEMTBTL"));
                sinhvienList.add(sv);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        // Set the loaded users to the table view
        sinhvienTableView.setItems(sinhvienList);
    }
}
