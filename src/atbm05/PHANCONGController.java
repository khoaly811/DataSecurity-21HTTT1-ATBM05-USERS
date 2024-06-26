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
import java.util.ArrayList;
import java.util.List;

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

    @FXML
    private TextField searchPC;


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
        searchPC.textProperty().addListener((observable, oldValue, newValue) -> {
            searcPhancong(newValue);
        });
    }

    private void searcPhancong(String searchText) {
        DataAccessLayer dal = null;
        Connection conn = null;
        CallableStatement cst = null;
        ResultSet rs = null;
        List<Phancong>  phancongList= new ArrayList<>();
        try {
            dal = DataAccessLayer.getInstance("your_username", "your_password");
            conn = dal.connect();

            // Construct the wildcard pattern for partial matching

            cst = conn.prepareCall("{CALL C##QLK.SP_SEARCH_PHANCONG(?, ?)}");
            cst.registerOutParameter(1, OracleTypes.CURSOR);
            cst.setString(2, searchText);
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

        phancongTableView.setItems(FXCollections.observableArrayList(phancongList));
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
    
        Phancong selectedPhancong = phancongTableView.getSelectionModel().getSelectedItem();
        String TENGV_OLD = selectedPhancong.getNhansu().getHOTEN();
        String TENHP_OLD = selectedPhancong.getHocphan().getTENHP();
        String MACT_OLD = selectedPhancong.getMACT();
        int HK_OLD = selectedPhancong.getHK();
        int NAM_OLD = selectedPhancong.getNAM();

        DataAccessLayer dal = null;
         Connection conn = null;
         CallableStatement cst = null;
        try {
             dal = DataAccessLayer.getInstance("", "");
             conn = dal.connect();
             cst = conn.prepareCall("{CALL C##QLK.SP_DELETE_PHANCONG(?,?,?,?,?)}");
         
             cst.setString(1, TENGV_OLD);
             cst.setString(2, TENHP_OLD);
             cst.setInt(3, HK_OLD);
             cst.setInt(4, NAM_OLD);
             cst.setString(5, MACT_OLD);
   
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
             
             if ("SV".equals(grantedRole) || grantedRole == null|| "NVCB".equals(grantedRole) || "GV".equals(grantedRole) || "GVU".equals(grantedRole)) {
                 System.out.println("No privileges (no grant).");
                 // Show an error alert
                 Alert alert = new Alert(AlertType.ERROR);
                 alert.setTitle("Error");
                 alert.setHeaderText(null);
                 alert.setContentText("Lỗi: không có quyền!");
                 alert.showAndWait();
 
             } else if (rowsAffected > 0) {
                 System.out.println("Delete successfully.");
                 Alert alert = new Alert(AlertType.INFORMATION);
                 alert.setTitle("Success");
                 alert.setHeaderText(null);
                 alert.setContentText("Xóa thành công!");
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
    private void updatePCClick(ActionEvent event) {
       String TENHP = hocphanDisplay.getText().trim();
       String TENGV = giaovienDisplay.getText().trim();
       String MACT = chuongtrinhDisplay.getText().trim();
       int HK = Integer.parseInt(hockyDisplay.getText().trim());
       int NAM = Integer.parseInt(namDisplay.getText().trim());
       Phancong selectedPhancong = phancongTableView.getSelectionModel().getSelectedItem();
       String TENGV_OLD = selectedPhancong.getNhansu().getHOTEN();
       String TENHP_OLD = selectedPhancong.getHocphan().getTENHP();
       String MACT_OLD = selectedPhancong.getMACT();
       int HK_OLD = selectedPhancong.getHK();
       int NAM_OLD = selectedPhancong.getNAM();
       DataAccessLayer dal = null;
       Connection conn = null;
       CallableStatement cst = null;
       try {
            dal = DataAccessLayer.getInstance("", "");
            conn = dal.connect();
            cst = conn.prepareCall("{CALL C##QLK.SP_ALL_UPDATE_PHANCONG(?,?,?,?,?,?,?,?,?,?)}");
            cst.setString(1, TENGV);
            cst.setString(2, TENHP);
            cst.setInt(3, HK);
            cst.setInt(4, NAM);
            cst.setString(5, MACT);
            cst.setString(6, TENGV_OLD);
            cst.setString(7, TENHP_OLD);
            cst.setInt(8, HK_OLD);
            cst.setInt(9, NAM_OLD);
            cst.setString(10, MACT_OLD);
  
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
            
            if ("SV".equals(grantedRole) || grantedRole == null|| "NVCB".equals(grantedRole) || "GV".equals(grantedRole)) {
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
}
