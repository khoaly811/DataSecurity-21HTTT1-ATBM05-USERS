package atbm05;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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

public class DANGKYController {
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
        dangkyList = FXCollections.observableArrayList();
        loadDangkyFromDatabase();
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
                System.out.println("TEN SINH VIEN: " + tensv);
                sv.setHOTEN(tensv);
                dk.setSinhvien(sv);

                String tengv = rs.getString("HOTEN_GV");
                System.out.println("TEN GIANG VIEN: " + tengv);
                ns.setHOTEN(tengv);
                dk.setNhansu(ns);

                String tenhp = rs.getString("TENHP");
                System.out.println("TEN HOC PHAN: " + tenhp);
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
                cst = conn.prepareCall("{CALL C##QLK.SP_DELETE_DANGKY(?)}");
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
    private void updateDKClick(ActionEvent event) {
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
            cst = conn.prepareCall("{CALL C##QLK.SP_ALL_UPDATE_DANGKY(?,?,?,?,?)}");
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