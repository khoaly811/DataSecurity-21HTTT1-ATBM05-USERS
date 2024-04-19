package atbm05;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Button;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import oracle.jdbc.OracleTypes;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
// import java.time.LocalDate;

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
   

    // @FXML
    // private TextField tenhpDisplay;
    @FXML 
    private ChoiceBox tenhpDisplayDrop;

    @FXML
    private TextField hockyDisplay;

    @FXML
    private TextField namDisplay;

    @FXML
    private TextField chuongtrinhDisplay;

    @FXML
    private TextField ngaybdDisplay;

    @FXML 
    private TextField searchKHMO;

    @FXML
    private Button updateKHMO;

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
        populateChoiceBox();

        khmoTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                tenhpDisplayDrop.getSelectionModel().select(newSelection.getHocphan().getTENHP());
                hockyDisplay.setText(String.valueOf(newSelection.getHOCKY()));
                namDisplay.setText(String.valueOf(newSelection.getNAM()));
                chuongtrinhDisplay.setText(newSelection.getMACT());
                ngaybdDisplay.setText(newSelection.getNGAYBD().toString());
            }
        });

        searchKHMO.textProperty().addListener((observable, oldValue, newValue) -> {
            searchKhmo(newValue);
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
                kh.setHOCKY(rs.getInt("HK"));
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

    // @FXML
    // private void deleteKHMOClick(ActionEvent event) {
        
    // }
    

    

    private List<String> loadDistinctTENHPFromDatabase(){
        List<String> tenhpList = new ArrayList<>();

        DataAccessLayer dal = null;
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            dal = DataAccessLayer.getInstance("your_username", "your_password");
            conn = dal.connect();

            String sql = "SELECT TENHP FROM C##QLK.HOCPHAN";

            pst = conn.prepareStatement(sql);

            rs = pst.executeQuery();

            while (rs.next()) {
                String tenhp = rs.getString("TENHP");
                tenhpList.add(tenhp);
            }

        } catch (SQLException e) {
            System.out.println("Error Message: " + e.getMessage());
            e.printStackTrace();

        }

        return tenhpList;
    }

    private void populateChoiceBox() {
        List<String> tenhpList = loadDistinctTENHPFromDatabase();

        // Clear existing items in ChoiceBox
        tenhpDisplayDrop.getItems().clear();

        // Populate ChoiceBox with distinct TENDV values
        tenhpDisplayDrop.getItems().addAll(tenhpList);

        // Set the first item as the default selected item (Optional)
        if (!tenhpList.isEmpty()) {
            tenhpDisplayDrop.getSelectionModel().selectFirst();
        }
    }

    @FXML
    private void updateKHMOClick(ActionEvent event) {

        String INP_TENHP = ((String) tenhpDisplayDrop.getValue()).trim();
        int INP_HK = Integer.parseInt(hockyDisplay.getText().trim());
        int INP_NAM = Integer.parseInt(namDisplay.getText().trim());
        String INP_MACT = chuongtrinhDisplay.getText().trim();

        KHmo selectedKhmo = khmoTableView.getSelectionModel().getSelectedItem();
        String TENHP_OLD = selectedKhmo.getHocphan().getTENHP();
        int HK_OLD = selectedKhmo.getHOCKY();
        int NAM_OLD = selectedKhmo.getNAM();
        String MACT_OLD = selectedKhmo.getMACT();

        // System.out.println("TENHP: " + TENHP_OLD);
        // System.out.println("HOCKY: " + HK_OLD);
        // System.out.println("NAM: " + NAM_OLD);
        // System.out.println("MACT: " + MACT_OLD);

        
        
        DataAccessLayer dal = null;
        Connection conn = null;
        CallableStatement cst = null;
        try {
            dal = DataAccessLayer.getInstance("", "");
            conn = dal.connect();
            cst = conn.prepareCall("{CALL C##QLK.SP_ALL_UPDATE_KHMO(?,?,?,?,?,?,?,?)}");
            
            cst.setString(1, INP_TENHP);
            cst.setInt(2, INP_HK);
            cst.setInt(3, INP_NAM);
            cst.setString(4, INP_MACT);
            cst.setString(5, TENHP_OLD);
            cst.setInt(6, HK_OLD);
            cst.setInt(7, NAM_OLD);
            cst.setString(8, MACT_OLD);
             

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

            } else{
                System.out.println("Update unsuccessfully.");
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Lỗi!");
                alert.showAndWait();

            }

        } catch (SQLException e) {
            System.out.println("Failed to Delete: " + e.getMessage());
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


    // private void showAlert(Alert.AlertType alertType, String title, String message) {
    //     Alert alert = new Alert(alertType);
    //     alert.setTitle(title);
    //     alert.setHeaderText(null);
    //     alert.setContentText(message);
    //     alert.showAndWait();
    // }
    
    @FXML
    private void insertKHmoClick(ActionEvent event) {
        String INP_TENHP = ((String) tenhpDisplayDrop.getValue()).trim();
        int INP_HK = 0;
        String hkText = hockyDisplay.getText();
        if (hkText != null && !hkText.isEmpty()) {
            INP_HK = Integer.parseInt(hockyDisplay.getText().trim());
        }

        int INP_NAM = 0;
        String namText = namDisplay.getText();
        if (namText != null && !namText.isEmpty()){
            INP_NAM = Integer.parseInt(namDisplay.getText().trim());
        }
        String INP_MACT = chuongtrinhDisplay.getText().trim();

        DataAccessLayer dal = null;
        Connection conn = null;
        CallableStatement cst = null;

        if (INP_TENHP == null ||  INP_MACT == null){
            System.out.println("Nhap thieu");
            // Show an error alert
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Vui lòng nhập đủ!");
            alert.showAndWait();
        } else {
            try {
                System.out.println(INP_TENHP);
                dal = DataAccessLayer.getInstance("your_username", "your_password");
                conn = dal.connect();
                cst = conn.prepareCall("{CALL C##QLK.SP_INSERT_KHMO(?,?,?,?)}");
                cst.setString(1, INP_TENHP);
                cst.setInt(2, INP_HK);
                cst.setInt(3, INP_NAM);
                cst.setString(4, INP_MACT);

                int rowsAffected = cst.executeUpdate();

                String role = null;
                PreparedStatement pst = null;
                ResultSet rs = null;
                pst = conn
                        .prepareStatement(
                                "SELECT * FROM SESSION_ROLES");

                rs = pst.executeQuery();

                if (rs.next()) {
                    role = rs.getString("ROLE");
                }

                if (role != null) {
                    System.out.println("Role: " + role);
                } else {
                    System.out.println("No role found for the current user.");
                }
                if (!"GVU".equals(role) || role == null) {
                    System.out.println("No privileges (no grant).");
                    // Show an error alert
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Lỗi: không có quyền!");
                    alert.showAndWait();

                } else if (rowsAffected > 0) {
                    System.out.println("Insert successfully.");
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Success");
                    alert.setHeaderText(null);
                    alert.setContentText("Thêm thành công!");
                    alert.showAndWait();

                } else {
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Lỗi!");
                    alert.showAndWait();

                }
            } catch (SQLException e) {
                System.out.println("Error: " + e.getMessage());
                // showAlert(Alert.AlertType.ERROR, "Error", "Failed to add user: " +
                // e.getMessage());
                System.out.println("Insert unsuccessfully.");
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Lỗi!");
                alert.showAndWait();
            }
        }
    } 

    private void searchKhmo(String searchText){
        DataAccessLayer dal = null;
        Connection conn = null;
        CallableStatement cst = null;
        ResultSet rs = null;
        List<KHmo> khmoList = new ArrayList<>();

        try {
            dal = DataAccessLayer.getInstance("your_username", "your_password");
            conn = dal.connect();

            // Construct the wildcard pattern for partial matching

            cst = conn.prepareCall("{CALL C##QLK.SP_SEARCH_KHMO(?, ?)}");
            cst.registerOutParameter(1, OracleTypes.CURSOR);
            cst.setString(2, searchText);
            cst.execute();

            rs = (ResultSet) cst.getObject(1);

            while (rs.next()) {
                KHmo kh = new KHmo();
                Hocphan hp = new Hocphan();

                String tenhp = rs.getString("TENHP");
                hp.setTENHP(tenhp);
                kh.setHocphan(hp);
                kh.setHOCKY(rs.getInt("HK"));
                kh.setNAM(rs.getInt("NAM"));
                kh.setMACT(rs.getString("MACT"));
                kh.setNGAYBD(rs.getDate("NGAYBD").toLocalDate());
                khmoList.add(kh);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        khmoTableView.setItems(FXCollections.observableArrayList(khmoList));
    }

    @FXML
    private void refreshTable(ActionEvent event) {
        khmoList.clear();
        loadKHmoFromDatabase();
        khmoTableView.refresh();
    }
}   
