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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import DataAccessLayer.DataAccessLayer;
import dto.*;

public class PHANCONGController {
    @FXML
    private TableView<Phancong> phancongTableView;

    @FXML
    private TableColumn<Phancong, String> HOTENGV;

    @FXML
    private TableColumn<Phancong, String> TENHP;

    @FXML
    private TableColumn<Phancong, String> HK;

    @FXML
    private TableColumn<Phancong, String> NAM;

    @FXML
    private TableColumn<Phancong, String> MACT;

    @FXML
    private Button updatePC;

    @FXML
    private Button deletePC;

    @FXML
    private Button profileButton;

    @FXML
    private void onAddClick_PHANCONG() {
        System.out.println("Added");
    }

    @FXML
    private void profileButtonclick() {

    }

    @FXML
    private TextField giaovienDisplay;

    @FXML
    private TextField hocphanDisplay;

    @FXML
    private TextField hockyDisplay;

    @FXML
    private TextField namDisplay;

    @FXML
    private TextField chuongtrinhDisplay;


    private ObservableList<Phancong> phancongList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        HOTENGV.setCellValueFactory(cellData -> cellData.getValue().getNhansu().HOTENproperty());
        TENHP.setCellValueFactory(cellData -> cellData.getValue().getHocphan().TENHPproperty());
        HK.setCellValueFactory(cellData -> cellData.getValue().HKproperty().asString());
        NAM.setCellValueFactory(cellData -> cellData.getValue().NAMproperty().asString());
        MACT.setCellValueFactory(cellData -> cellData.getValue().MACTproperty());
        phancongList = FXCollections.observableArrayList();
        loadPhancongFromDatabase();

        //Add listener to the TableView selection model
        phancongTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                // Display the selected row's information in the corresponding TextFields
                giaovienDisplay.setText(newSelection.getNhansu().getHOTEN());
                hocphanDisplay.setText(newSelection.getHocphan().getTENHP());
                hockyDisplay.setText(String.valueOf(newSelection.getHK()));
                namDisplay.setText(String.valueOf(newSelection.getNAM()));
                chuongtrinhDisplay.setText(newSelection.getMACT());
            }
        });
    }

    private void loadPhancongFromDatabase() {
        DataAccessLayer dal = null;
        Connection conn = null;
        CallableStatement cst = null;
        ResultSet rs = null;

        try {
            dal = DataAccessLayer.getInstance("your_username", "your_password");
            conn = dal.connect();
            cst = conn.prepareCall("{CALL C##QLK.SP_VIEW_PHANCONG(?)}");
            cst.registerOutParameter(1, OracleTypes.CURSOR);
            cst.execute();
            rs = (ResultSet) cst.getObject(1);
            while (rs.next()) {
                Phancong pc = new Phancong();
                Nhansu ns = new Nhansu();
                Hocphan hp = new Hocphan();

                String tengv = rs.getString("HOTEN");
                //System.out.println("TEN GIANG VIEN: " + tengv);
                ns.setHOTEN(tengv);
                pc.setNhansu(ns);

                String tenhp = rs.getString("TENHP");
                //System.out.println("TEN HOC PHAN: " + tenhp);
                hp.setTENHP(tenhp);
                pc.setHocphan(hp);

                pc.setHK((rs.getInt("HK")));
                pc.setNAM((rs.getInt("NAM")));
                pc.setMACT((rs.getString("MACT")));

                phancongList.add(pc);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());

        }

        phancongTableView.setItems(phancongList);
    }

    @FXML
    private void deletePCClick(ActionEvent event) {
        
    }

    @FXML
    private void updatePCClick(ActionEvent event) {
       String TENHP = hocphanDisplay.getText().trim();
       String TENGV = giaovienDisplay.getText().trim();
       String MACT = chuongtrinhDisplay.getText().trim();
       int HK = Integer.parseInt(hockyDisplay.getText().trim());
       int NAM = Integer.parseInt(namDisplay.getText().trim());

       DataAccessLayer dal = null;
        Connection conn = null;
        CallableStatement cst = null;
       try {
            dal = DataAccessLayer.getInstance("", "");
            conn = dal.connect();
            cst = conn.prepareCall("{CALL C##QLK.SP_ALL_UPDATE_PHANCONG(?,?,?,?,?)}");
            cst.setString(1, TENGV);
            System.out.println(TENGV);
            System.out.println(TENHP);
            cst.setString(2, TENHP);
            cst.setInt(3, HK);
            cst.setInt(4, NAM);
            cst.setString(5, MACT);
  
            int rowsAffected = cst.executeUpdate();

            
            if (rowsAffected > 0) {
                System.out.println("Update successfully.");
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Cập nhật thành công!");
                alert.showAndWait();

            } else{
                System.out.println("Update unsuccessfully.");
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Lỗi!");
                alert.showAndWait();

            }
        } catch (SQLException e) {
            System.out.println("Failed to Update: " + e.getMessage());
            //showAlert(Alert.AlertType.ERROR, "Error", "Failed to Update user: " + e.getMessage());
            if (e.getMessage().contains("ORA-01031")) {
                System.out.println("No privileges (no grant).");
                // Show an error alert
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Lỗi");
                alert.showAndWait();
            }
            if (e.getErrorCode() == -20003) {
                // Display error message in an alert box
                showAlert(AlertType.ERROR, "Error", "Custom Error HP", e.getMessage());
            }
            if (e.getErrorCode() == -20002) {
                // Display error message in an alert box
                showAlert(AlertType.ERROR, "Error", "Custom Error NS", e.getMessage());
            }
            else{
                System.out.println("Unexpected error");
                // Show an error alert
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Lỗi khi chạy !!!");
                alert.showAndWait();
            }
    }
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
        phancongList.clear();
        loadPhancongFromDatabase();
        phancongTableView.refresh();
    }
    // Method to show alert
private void showAlert(AlertType alertType, String title, String headerText, String contentText) {
    Alert alert = new Alert(alertType);
    alert.setTitle(title);
    alert.setHeaderText(headerText);
    alert.setContentText(contentText);
    alert.showAndWait();
}
}
