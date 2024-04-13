package atbm05;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import oracle.jdbc.OracleTypes;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import DataAccessLayer.DataAccessLayer;
import dto.KHmo;
import dto.Hocphan;

public class KHMOController {
    @FXML
    private TableView<KHmo> khmoTableView;

    @FXML
    private TableColumn<KHmo, String> TENHP;

    @FXML
    private TableColumn<KHmo, String> HK;

    @FXML
    private TableColumn<KHmo, String> NAM;

    @FXML
    private TableColumn<KHmo, String> MACT;

    @FXML
    private TableColumn<KHmo, String> NGAYBD;

    @FXML
    private void onAddClick_KHMO() {
        System.out.println("Added");
    }

   

    @FXML
    private TextField tenhpDisplay;

    @FXML
    private TextField hockyDisplay;

    @FXML
    private TextField namDisplay;

    @FXML
    private TextField chuongtrinhDisplay;

    @FXML
    private TextField ngaybdDisplay;

    private ObservableList<KHmo> khmoList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        TENHP.setCellValueFactory(cellData -> cellData.getValue().getHocphan().TENHPproperty());
        HK.setCellValueFactory(cellData -> cellData.getValue().HOCKYproperty().asString());
        NAM.setCellValueFactory(cellData -> cellData.getValue().NAMproperty().asString());
        MACT.setCellValueFactory(cellData -> cellData.getValue().MACTproperty());
        NGAYBD.setCellValueFactory(cellData -> cellData.getValue().NGAYBDproperty().asString());
        khmoList = FXCollections.observableArrayList();
        loadKHmoFromDatabase();

        khmoTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                TENHP.setText(newSelection.getHocphan().getTENHP());
                HK.setText(String.valueOf(newSelection.getHOCKY()));
                NAM.setText(String.valueOf(newSelection.getNAM()));
                MACT.setText(newSelection.getMACT());
                NGAYBD.setText(newSelection.getNGAYBD().toString());
            }
        });
    }

    private void loadKHmoFromDatabase() {
        DataAccessLayer dal = null;
        Connection conn = null;
        CallableStatement cst = null;
        ResultSet rs = null;

        try {
            dal = DataAccessLayer.getInstance("your_username", "your_password");
            conn = dal.connect();
            cst = conn.prepareCall("{CALL C##QLK.SP_VIEW_KHMO(?)}");
            cst.registerOutParameter(1, OracleTypes.CURSOR);
            System.out.println("nhan beo 1");
            cst.execute();
            System.out.println("nhan beo 2");
            rs = (ResultSet) cst.getObject(1);
            System.out.println("Nhan beo 3");
            while (rs.next()) {
                KHmo kh = new KHmo();
                Hocphan hp = new Hocphan();

                String tenhp = rs.getString("TENHP");
                hp.setTENHP(tenhp);
                kh.setHocphan(hp);
                kh.setHOCKY(rs.getInt("HOCKY"));
                kh.setNAM(rs.getInt("NAM"));
                kh.setMACT(rs.getString("MACT"));
                kh.setNGAYBD(rs.getDate("NGAYBD").toLocalDate());
                khmoList.add(kh);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        khmoTableView.setItems(khmoList);

    }

    @FXML
    private void deleteKHMOClick(ActionEvent event) {
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
                cst = conn.prepareCall("{CALL C##QLK.SP_DELETE_KHMO(?)}");
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
    private void updateKHMOClick(ActionEvent event) {
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
            cst = conn.prepareCall("{CALL C##QLK.SP_ALL_UPDATE_KHMO(?,?,?,?,?)}");
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
