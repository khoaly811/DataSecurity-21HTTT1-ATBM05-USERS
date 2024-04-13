package atbm05;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import oracle.jdbc.OracleTypes;

import java.sql.CallableStatement;
import java.sql.Connection;
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
                System.out.println("TEN HOC PHAN: " + tenhp);
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
        System.out.println("Delete");
        Nhansu selectedNhanSu = phancongTableView.getSelectionModel().getSelectedItem();

        if (selectedNhanSu != null) {
            String MANV = selectedNhanSu.getMANV();
            
            DataAccessLayer dal = null;
            Connection conn = null;
            CallableStatement cst = null;
            
            try {
                dal = DataAccessLayer.getInstance("", "");
                conn = dal.connect();
                cst = conn.prepareCall("{CALL C##QLK.SP_DELETE_PHANCONG(?)}");
                cst.setString(1, MANV);
                
                int rowsAffected = cst.executeUpdate();
                
                if (rowsAffected > 0) {
                    System.out.println("Deleted successfully.");
                    // You can show a success message here if needed
                } else {
                    System.out.println("No rows deleted.");
                    // You can show a message indicating that no rows were deleted if needed
                }
            } catch (SQLException e) {
                System.out.println("Failed to delete: " + e.getMessage());
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete user: " + e.getMessage());
            } finally {
                // Close resources in a finally block to ensure they are always closed
                try {
                    if (cst != null) cst.close();
                    if (conn != null) conn.close();
                } catch (SQLException e) {
                    System.out.println("Error closing resources: " + e.getMessage());
                }
            }
        } else {
            System.out.println("No row selected.");
        }
    }

    @FXML
    private void updatePCClick(ActionEvent event) {
        String INP_HOTEN = hotenDisplay.getText().trim();
        String INP_PHAI = phaiDisplay.getText().trim();
        LocalDate INP_NGSINH = LocalDate.parse(ngsinhDisplay.getText().trim()); // Assuming your date format is parseable
        int INP_PHUCAP = Integer.parseInt(phucapDisplay.getText().trim());
        String INP_DT = dienthoaiDisplay.getText().trim();

        DataAccessLayer dal = null;
        Connection conn = null;
        CallableStatement cst = null;
        ResultSet rs = null;

        try {
            dal = DataAccessLayer.getInstance("your_username", "your_password");
            conn = dal.connect();
            cst = conn.prepareCall("{CALL C##QLK.SP_ALL_UPDATE_PHANCONG(?,?,?,?,?)}");
            cst.setString(1, INP_HOTEN);
            cst.setString(2, INP_PHAI);
            cst.setDate(3, java.sql.Date.valueOf(INP_NGSINH));
            cst.setInt(4, INP_PHUCAP);
            cst.setString(5, INP_DT);
            cst.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            // Close resources
            try {
                if (rs != null) rs.close();
                if (cst != null) cst.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
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
}
