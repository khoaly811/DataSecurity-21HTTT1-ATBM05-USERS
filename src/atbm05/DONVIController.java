package atbm05;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import oracle.jdbc.OracleTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import DataAccessLayer.DataAccessLayer;
import dto.Donvi;
import dto.Nhansu;

public class DONVIController {
    @FXML
    private TableView<Donvi> donviTableView;

    @FXML
    private TableColumn<Donvi, String> MADV;
    @FXML 
    private TableColumn<Donvi, String> TENDV;

    @FXML 
    private TableColumn<Donvi, String> HOTEN_TDV;

    @FXML
    private void onAddClick_DONVI() {
        System.out.println("Added");
    }

    @FXML
    private TextField madvDisplay;

    @FXML
    private TextField tendvDisplay;

    @FXML
    private TextField truongdvDisPlay;

    @FXML
    private TextField searchDV;

    @FXML
    private Button updateDV;

    private ObservableList<Donvi> donviList = FXCollections.observableArrayList();

    public void initialize(){
        MADV.setCellValueFactory(cellData -> cellData.getValue().MADVproperty());
        TENDV.setCellValueFactory(cellData -> cellData.getValue().TENDVproperty());
        HOTEN_TDV.setCellValueFactory(cellData -> cellData.getValue().getNhansu().HOTENproperty());
        donviList = FXCollections.observableArrayList();
        loadDonviFromDatabase();

        donviTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if(newSelection != null){
                madvDisplay.setText(newSelection.getMADV());
                tendvDisplay.setText(newSelection.getTENDV());
                truongdvDisPlay.setText(newSelection.getNhansu().getHOTEN());
            }
        });

        searchDV.textProperty().addListener((observable, oldValue, newValue) -> {
            searchDonvi(newValue);
        });
    }

    private void loadDonviFromDatabase() {
        DataAccessLayer dal = null;
        Connection conn = null;
        CallableStatement cst = null;
        ResultSet rs = null;

        try {
            dal = DataAccessLayer.getInstance("your_username", "your_password");
            conn = dal.connect();
            cst = conn.prepareCall("{CALL C##QLK.SP_VIEW_DONVI(?)}");
            cst.registerOutParameter(1, OracleTypes.CURSOR);
            System.out.println("nhan beo 1");
            cst.execute();
            System.out.println("nhan beo 2");
            rs = (ResultSet) cst.getObject(1);
            System.out.println("Nhan beo 3");
            while (rs.next()) {
                Donvi dv = new Donvi();
                Nhansu ns = new Nhansu();

                dv.setMADV(rs.getString("MDV"));
                dv.setTENDV(rs.getString("TENDV"));

                String tentdv = rs.getString("HOTEN");
                System.out.println("TEN TRUONG DON VI: " + tentdv);
                ns.setHOTEN(tentdv);
                dv.setNhansu(ns);

                donviList.add(dv);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            
        }
        
        donviTableView.setItems(donviList);
    }

    // @FXML
    // private void deleteDVClick(ActionEvent event) {
       
    // }

    
    @FXML
    private void updateDVClick(ActionEvent event) {
        String INP_MADV = madvDisplay.getText().trim();
        String INP_TENDV = tendvDisplay.getText().trim();
        String INP_TRUONGDV = truongdvDisPlay.getText().trim();

        DataAccessLayer dal = null;
        Connection conn = null;
        CallableStatement cst = null;

        try {
            dal = DataAccessLayer.getInstance("your_username", "your_password");
            conn = dal.connect();
            System.out.println("khoa beo 1");
            cst = conn.prepareCall("{CALL C##QLK.SP_ALL_UPDATE_DONVI(?,?,?)}");
            cst.setString(1, INP_TENDV);
            cst.setString(2, INP_TRUONGDV);
            cst.setString(3, INP_MADV);

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

    @FXML 
    private void insertDVClick(ActionEvent event) {
        String INP_MADV = madvDisplay.getText().trim();
        String INP_TENDV = tendvDisplay.getText().trim();
        String INP_TRUONGDV = truongdvDisPlay.getText().trim();

        DataAccessLayer dal = null;
        Connection conn = null;
        CallableStatement cst = null;

        try {
            dal = DataAccessLayer.getInstance("your_username", "your_password");
            conn = dal.connect();
            System.out.println("khoa beo 1");
            cst = conn.prepareCall("{CALL C##QLK.SP_INSERT_DONVI(?,?,?)}");
            cst.setString(1, INP_MADV);
            cst.setString(2, INP_TENDV);
            cst.setString(3, INP_TRUONGDV);

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

    private void searchDonvi(String searchText) {
        DataAccessLayer dal = null;
        Connection conn = null;
        CallableStatement cst = null;
        ResultSet rs = null;
        List<Donvi> donviList = new ArrayList<>();

        try {
            dal = DataAccessLayer.getInstance("your_username", "your_password");
            conn = dal.connect();

            // Construct the wildcard pattern for partial matching

            cst = conn.prepareCall("{CALL C##QLK.SP_SEARCH_DONVI(?, ?)}");
            cst.registerOutParameter(1, OracleTypes.CURSOR);
            cst.setString(2, searchText);
            cst.execute();

            rs = (ResultSet) cst.getObject(1);

            while (rs.next()) {
                Donvi dv = new Donvi();
                dv.setMADV(rs.getString("MADV"));
                dv.setTENDV(rs.getString("TENDV"));
                dv.setTRUONGDV(rs.getString("TRUONGDV"));
                
                donviList.add(dv);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        // Set the loaded users to the table view
        donviTableView.setItems(FXCollections.observableArrayList(donviList));
    }

    // private void showAlert(Alert.AlertType alertType, String title, String message) {
    //     Alert alert = new Alert(alertType);
    //     alert.setTitle(title);
    //     alert.setHeaderText(null);
    //     alert.setContentText(message);
    //     alert.showAndWait();
    // }


}
