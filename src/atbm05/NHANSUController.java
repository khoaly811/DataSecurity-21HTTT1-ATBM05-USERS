package atbm05;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import oracle.jdbc.OracleTypes;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import DataAccessLayer.DataAccessLayer;
import dto.Donvi;
import dto.Nhansu;

public class NHANSUController {
    @FXML
    private TableView<Nhansu> nhansuTableView;

    @FXML
    private TableColumn<Nhansu, String> HOTEN;

    @FXML
    private TableColumn<Nhansu, String> PHAI;

    @FXML
    private TableColumn<Nhansu, String> NGSINH;

    @FXML
    private TableColumn<Nhansu, String> PHUCAP;

    @FXML
    private TableColumn<Nhansu, String> DT;

    @FXML
    private TableColumn<Nhansu, String> VAITRO;

    @FXML
    private TableColumn<Nhansu, String> TENDV;

    @FXML
    private TableColumn<Nhansu, String> MANV;

    @FXML
    private void onAddClick_NHANSU() {
        System.out.println("Added");
    }

    @FXML
    private void onUpdateClick_NHANSU() {
        System.out.println("Updated");
    }

    @FXML
    private void onDeleteClick_NHANSU() {
        System.out.println("Delete");
    }

    @FXML
    private TextField hotenDisplay;

    @FXML
    private TextField phaiDisplay;

    @FXML
    private TextField phucapDisplay;

    @FXML
    private TextField ngsinhDisplay;

    @FXML
    private TextField dienthoaiDisplay;

    @FXML
    private TextField tendvDisplay;

    @FXML
    private Button updateNS;

    @FXML
    private Button profileButton;

    private ObservableList<Nhansu> nhansuList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        HOTEN.setCellValueFactory(cellData -> cellData.getValue().HOTENproperty());
        PHAI.setCellValueFactory(cellData -> cellData.getValue().PHAIproperty());
        NGSINH.setCellValueFactory(cellData -> cellData.getValue().NGSINHproperty().asString());
        PHUCAP.setCellValueFactory(cellData -> cellData.getValue().PHUCAPproperty().asString());
        DT.setCellValueFactory(cellData -> cellData.getValue().DTproperty());
        VAITRO.setCellValueFactory(cellData -> cellData.getValue().VAITROproperty());
        TENDV.setCellValueFactory(cellData -> cellData.getValue().getDonvi().TENDVproperty());
        MANV.setCellValueFactory(cellData -> cellData.getValue().MANVproperty());
        nhansuList = FXCollections.observableArrayList();
        loadNhansuFromDatabase();

        // Add listener to the TableView selection model
        nhansuTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                hotenDisplay.setText(newSelection.getHOTEN());
                phaiDisplay.setText(newSelection.getPHAI());
                ngsinhDisplay.setText(newSelection.getNGSINH().toString());
                phucapDisplay.setText(String.valueOf(newSelection.getPHUCAP()));
                dienthoaiDisplay.setText(newSelection.getDT());
                tendvDisplay.setText(newSelection.getDonvi().getTENDV());
            }
        });
    }

    private void loadNhansuFromDatabase() {
        DataAccessLayer dal = null;
        Connection conn = null;
        CallableStatement cst = null;
        ResultSet rs = null;

        try {
            dal = DataAccessLayer.getInstance("your_username", "your_password");
            conn = dal.connect();
            cst = conn.prepareCall("{CALL C##QLK.SP_VIEW_NHANSU(?)}");
            cst.registerOutParameter(1, OracleTypes.CURSOR);
            System.out.println("nhan beo 1");
            cst.execute();
            System.out.println("nhan beo 2");
            rs = (ResultSet) cst.getObject(1);
            System.out.println("Nhan beo 3");
            while (rs.next()) {
                Nhansu ns = new Nhansu();
                ns.setHOTEN((rs.getString("HOTEN")));
                ns.setPHAI((rs.getString("PHAI")));
                ns.setNGSINH((rs.getDate("NGSINH").toLocalDate()));
                ns.setPHUCAP((rs.getInt("PHUCAP")));
                ns.setDT((rs.getString("DT")));
                ns.setVAITRO((rs.getString("VAITRO")));
                String tendv = rs.getString("TENDV");
                ns.setMANV((rs.getString("MANV")));
                // System.out.println("TENDV là: " + tendv);
                Donvi dv = new Donvi();
                dv.setTENDV(tendv);
                ns.setDonvi(dv);
                nhansuList.add(ns);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());

        }

        // Set the loaded users to the table view
        nhansuTableView.setItems(nhansuList);
    }

    @FXML
    private void updateNSClick(ActionEvent event) {
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
            System.out.println("khoa beo 1");
            cst = conn.prepareCall("{CALL C##QLK.SP_ALL_UPDATE_NHANSU(?,?,?,?,?)}");
            cst.setString(1, INP_HOTEN);
            cst.setString(2, INP_PHAI);
            cst.setDate(3, java.sql.Date.valueOf(INP_NGSINH));
            cst.setInt(4, INP_PHUCAP);
            cst.setString(5, INP_DT);
            System.out.println("khoa beo 2");
            cst.executeUpdate();
            System.out.println("khoa beo 3");
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


    @FXML
    private void profileButtonclick(ActionEvent event) {
        System.out.println("Profile");
    }

    @FXML
    private void deleteNSClick(ActionEvent event) {
        System.out.println("Delete");
        Nhansu selectedNhanSu = nhansuTableView.getSelectionModel().getSelectedItem();

        if (selectedNhanSu != null) {
            String MANV = selectedNhanSu.getMANV();
            
            DataAccessLayer dal = null;
            Connection conn = null;
            CallableStatement cst = null;
            
            try {
                dal = DataAccessLayer.getInstance("", "");
                conn = dal.connect();
                cst = conn.prepareCall("{CALL C##QLK.SP_DELETE_NHANSU(?)}");
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

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
