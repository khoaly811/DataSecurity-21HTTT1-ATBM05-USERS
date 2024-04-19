package atbm05;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import oracle.jdbc.OracleTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
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
            searchSinhvien(newValue);
        });
    }


    private void searchSinhvien(String searchText) {
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
        String INP_HOTEN = hotenDisplay.getText().trim();
        String INP_PHAI = phaiDisplay.getText().trim();
        LocalDate INP_NGSINH = LocalDate.parse(ngsinhDisplay.getText().trim()); // Assuming your date format is
                                                                                // parseable
        String INP_DCHI = diachiDisplay.getText().trim();
        String INP_DT = dienthoaiDisplay.getText().trim();
        String INP_MACT = chuongtrinhDisplay.getText().trim();
        String INP_MANGANH = nganhDisplay.getText().trim();
        int INP_SOTCTL = Integer.parseInt(sotctlDisplay.getText().trim());
        Float INP_DTBTL = Float.parseFloat(diemtbtlDisplay.getText().trim());

        System.out.println(INP_HOTEN);

        DataAccessLayer dal = null;
        Connection conn = null;
        CallableStatement cst = null;

        try {
            dal = DataAccessLayer.getInstance("your_username", "your_password");
            conn = dal.connect();
            System.out.println("khoa beo 1");
            cst = conn.prepareCall("{CALL C##QLK.SP_ALL_UPDATE_SINHVIEN(?,?,?,?,?,?,?,?,?)}");
            cst.setString(1, INP_HOTEN);
            cst.setString(2, INP_PHAI);
            cst.setDate(3, java.sql.Date.valueOf(INP_NGSINH));
            cst.setString(4, INP_DCHI);
            cst.setString(5, INP_DT);
            cst.setString(6, INP_MACT);
            cst.setString(7, INP_MANGANH);
            cst.setInt(8, INP_SOTCTL);
            cst.setFloat(9, INP_DTBTL);
            int rowsAffected = cst.executeUpdate();

            String grantedRole = null;
            PreparedStatement pst = null;
            ResultSet rs = null;
            pst = conn
                    .prepareStatement(
                            "SELECT * FROM SESSION_ROLES");

            rs = pst.executeQuery();

            if (rs.next()) {
                grantedRole = rs.getString("ROLE");
            }

            if (grantedRole != null) {
                System.out.println("Granted role: " + grantedRole);
            } else {
                System.out.println("No role found for the current user.");
            }

            if ( !(!"GVU".equals(grantedRole) || !"SV".equals(grantedRole)) || grantedRole == null) {
                System.out.println("No privileges (no grant).");
                // Show an error alert
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Lỗi: không có quyền!");
                alert.showAndWait();

            } else if (rowsAffected > 0) {
                System.out.println("Update successfully.");
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Cập nhật thành công!");
                alert.showAndWait();

            } else {
                System.out.println("Update unsuccessfully.");
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Lỗi!");
                alert.showAndWait();

            }
        } catch (SQLException e) {
            System.out.println("Failed to Update: " + e.getMessage());
            // showAlert(Alert.AlertType.ERROR, "Error", "Failed to Update user: " +
            // e.getMessage());
            if (e.getMessage().contains("ORA-01031")) {
                System.out.println("No privileges (no grant).");
                // Show an error alert
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Lỗi");
                alert.showAndWait();
            }

        }
    }

    @FXML
    private void insertSVClick(ActionEvent action){
        String INP_HOTEN = hotenDisplay.getText().trim();
        String INP_PHAI = phaiDisplay.getText().trim();
        LocalDate INP_NGSINH = LocalDate.parse(ngsinhDisplay.getText().trim()); // Assuming your date format is
                                                                                // parseable
        String INP_DCHI = diachiDisplay.getText().trim();
        String INP_DT = dienthoaiDisplay.getText().trim();
        String INP_MACT = chuongtrinhDisplay.getText().trim();
        String INP_MANGANH = nganhDisplay.getText().trim();
        int INP_SOTCTL = 0;
        String sotcText = sotctlDisplay.getText();
        INP_SOTCTL = Integer.parseInt(sotcText.trim());
        Double INP_DTBTL = 0.0;
        String dtbText = diemtbtlDisplay.getText();
        INP_DTBTL = Double.parseDouble(dtbText.trim());
        System.out.println(INP_DTBTL);
        DataAccessLayer dal = null;
        Connection conn = null;
        CallableStatement cst = null;

        try {
            dal = DataAccessLayer.getInstance("your_username", "your_password");
            conn = dal.connect();
            System.out.println("khoa beo 1");
            cst = conn.prepareCall("{CALL C##QLK.SP_INSERT_SINHVIEN(?,?,?,?,?,?,?,?,?)}");
            cst.setString(1, INP_HOTEN);
            cst.setString(2, INP_PHAI);
            cst.setDate(3, java.sql.Date.valueOf(INP_NGSINH));
            cst.setString(4, INP_DCHI);
            cst.setString(5, INP_DT);
            cst.setString(6, INP_MACT);
            cst.setString(7, INP_MANGANH);
            cst.setInt(8, INP_SOTCTL);
            cst.setDouble(9, INP_DTBTL);

            int rowsAffected = cst.executeUpdate();

            String grantedRole = null;
            PreparedStatement pst = null;
            ResultSet rs = null;
            pst = conn
                    .prepareStatement(
                            "SELECT * FROM SESSION_ROLES");

            rs = pst.executeQuery();

            if (rs.next()) {
                grantedRole = rs.getString("ROLE");
            }

            if (grantedRole != null) {
                System.out.println("Granted role: " + grantedRole);
            } else {
                System.out.println("No role found for the current user.");
            }

            if (!"GVU".equals(grantedRole) || grantedRole == null) {
                System.out.println("No privileges (no grant).");
                // Show an error alert
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Lỗi: không có quyền!");
                alert.showAndWait();

            } else if (rowsAffected > 0) {
                System.out.println("Update successfully.");
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Cập nhật thành công!");
                alert.showAndWait();

            } else {
                System.out.println("Update unsuccessfully.");
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Lỗi!");
                alert.showAndWait();

            }
        } catch (SQLException e) {
            System.out.println("Failed to Update: " + e.getMessage());
            // showAlert(Alert.AlertType.ERROR, "Error", "Failed to Update user: " +
            // e.getMessage());
            if (e.getMessage().contains("ORA-01031")) {
                System.out.println("No privileges (no grant).");
                // Show an error alert
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Lỗi");
                alert.showAndWait();
            }

        }
    }
    // private void showAlert(Alert.AlertType alertType, String title, String message) {
    //     Alert alert = new Alert(alertType);
    //     alert.setTitle(title);
    //     alert.setHeaderText(null);
    //     alert.setContentText(message);
    //     alert.showAndWait();
    // }
    @FXML
    private void refreshTable(ActionEvent event) {
        sinhvienList.clear();
        loadSinhvienFromDatabase();
        sinhvienTableView.refresh();
    }
}
