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
import oracle.jdbc.OracleTypes;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import DataAccessLayer.DataAccessLayer;
import dto.*;

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
    private TableColumn<Dangky, String> MASV;
    @FXML
    private TableColumn<Dangky, String> MAGV;

    @FXML
    private TextField hotensvDisplay;
    @FXML
    private TextField hotengvDisplay;
    @FXML
    private TextField tenhocphanDisplay;
    @FXML
    private TextField hkDisplay;
    @FXML
    private TextField namDisplay;
    @FXML
    private TextField diemthDisplay;
    @FXML
    private TextField diemqtDisplay;
    @FXML
    private TextField diemckDisplay;
    @FXML
    private TextField diemtkDisplay;
    @FXML
    private TextField mactDisplay;
    @FXML
    private TextField masvDisplay;
    @FXML
    private TextField magvDisplay;
    @FXML
    private TextField searchKQDK;
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
        MASV.setCellValueFactory(cellData -> cellData.getValue().MASVproperty());
        MAGV.setCellValueFactory(cellData -> cellData.getValue().MAGVproperty());
        MACT.setCellValueFactory(cellData -> cellData.getValue().MACTproperty());
        dangkyList = FXCollections.observableArrayList();
        loadDangkyFromDatabase();

        dangkyTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                hotensvDisplay.setText(newSelection.getSinhvien().getHOTEN());
                hotengvDisplay.setText(newSelection.getNhansu().getHOTEN());
                tenhocphanDisplay.setText(newSelection.getHocphan().getTENHP());
                mactDisplay.setText(newSelection.getMACT());
                hkDisplay.setText(String.valueOf(newSelection.getHK()));
                namDisplay.setText(String.valueOf(newSelection.getNAM()));
                diemthDisplay.setText(String.valueOf(newSelection.getDIEMTH()));
                diemqtDisplay.setText(String.valueOf(newSelection.getDIEMQT()));
                diemckDisplay.setText(String.valueOf(newSelection.getDIEMCK()));
                diemtkDisplay.setText(String.valueOf(newSelection.getDIEMTK()));
                mactDisplay.setText(String.valueOf(newSelection.getMACT()));
                
            }
        });

        searchKQDK.textProperty().addListener((observable, oldValue, newValue) -> {
            searchDangKy(newValue);
        });

        
    }


    private void searchDangKy(String searchText) {
        DataAccessLayer dal = null;
        Connection conn = null;
        CallableStatement cst = null;
        ResultSet rs = null;
        List<Dangky>dangkyList  = new ArrayList<>();
        try {
            dal = DataAccessLayer.getInstance("your_username", "your_password");
            conn = dal.connect();

            // Construct the wildcard pattern for partial matching

            cst = conn.prepareCall("{CALL C##QLK.SP_SEARCH_DANGKY(?, ?)}");
            cst.registerOutParameter(1, OracleTypes.CURSOR);
            cst.setString(2, searchText);
            cst.execute();
            rs = (ResultSet) cst.getObject(1);
            while (rs.next()) {
                Dangky dk = new Dangky();
                Sinhvien sv = new Sinhvien();
                Hocphan hp = new Hocphan();
                Nhansu ns = new Nhansu();

                String tensv = rs.getString("HOTEN_SV");
                sv.setHOTEN(tensv);
                dk.setSinhvien(sv);

                String tengv = rs.getString("HOTEN_GV");
                ns.setHOTEN(tengv);
                dk.setNhansu(ns);

                String tenhp = rs.getString("TENHP");
                hp.setTENHP(tenhp);
                dk.setHocphan(hp);

                dk.setHK((rs.getInt("HK")));
                dk.setNAM((rs.getInt("NAM")));
                dk.setMACT((rs.getString("MACT")));
                dk.setDIEMTH((rs.getInt("DIEMTH")));
                dk.setDIEMQT((rs.getInt("DIEMQT")));
                dk.setDIEMCK((rs.getInt("DIEMCK")));
                dk.setDIEMTK((rs.getInt("DIEMTK")));

                dangkyList.add(dk);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            
        }

        // Set the loaded users to the table view
        dangkyTableView.setItems(FXCollections.observableArrayList(dangkyList));
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
            cst.execute();
            rs = (ResultSet) cst.getObject(1);
            while (rs.next()) {
                Dangky dk = new Dangky();
                Sinhvien sv = new Sinhvien();
                Hocphan hp = new Hocphan();
                Nhansu ns = new Nhansu();

                String tensv = rs.getString("HOTEN_SV");
                //System.out.println("TEN SINH VIEN: " + tensv);
                sv.setHOTEN(tensv);
                dk.setSinhvien(sv);

                String tengv = rs.getString("HOTEN_GV");
                //System.out.println("TEN GIANG VIEN: " + tengv);
                ns.setHOTEN(tengv);
                dk.setNhansu(ns);

                String tenhp = rs.getString("TENHP");
                //System.out.println("TEN HOC PHAN: " + tenhp);
                hp.setTENHP(tenhp);
                dk.setHocphan(hp);

                dk.setHK((rs.getInt("HK")));
                dk.setNAM((rs.getInt("NAM")));
                dk.setMACT((rs.getString("MACT")));
                dk.setDIEMTH((rs.getInt("DIEMTH")));
                dk.setDIEMQT((rs.getInt("DIEMQT")));
                dk.setDIEMCK((rs.getInt("DIEMCK")));
                dk.setDIEMTK((rs.getInt("DIEMTK")));

                dangkyList.add(dk);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            
        }

        // Set the loaded users to the table view
        dangkyTableView.setItems(dangkyList);
    }
    @FXML
    private void insertDKClick(ActionEvent event){
        String HOTENSV = hotensvDisplay.getText().trim();
        String HOTENGV = hotengvDisplay.getText().trim();
        String TENHP = tenhocphanDisplay.getText().trim();
        int HK = 0;
        String hkText=hkDisplay.getText();
        if (hkText != null && hkText.isEmpty()){
            HK=Integer.parseInt(hkText.trim());
        }
        int NAM=0;
        String namText=namDisplay.getText();
        if (namText != null && namText.isEmpty()){
            NAM=Integer.parseInt(namText.trim());
        }
        String MACT = mactDisplay.getText().trim();
        Integer DIEMTH=0;
        String thText=diemthDisplay.getText();
        if (thText != null && thText.isEmpty()){
            DIEMTH=Integer.parseInt(thText.trim());
        }
        Integer DIEMQT=0;
        String qtText=diemqtDisplay.getText();
        if (qtText != null && qtText.isEmpty()){
            DIEMQT=Integer.parseInt(qtText.trim());
        }
        Integer DIEMCK=0;
        String ckText=diemckDisplay.getText();
        if (ckText != null && ckText.isEmpty()){
            DIEMCK=Integer.parseInt(ckText.trim());
        }
        Integer DIEMTK=0;
        String tkText=diemtkDisplay.getText();
        if (tkText != null && tkText.isEmpty()){
            DIEMTK=Integer.parseInt(tkText.trim());
        }
        DataAccessLayer dal = null;
        Connection conn = null;
        CallableStatement cst = null;
        try {
            dal = DataAccessLayer.getInstance("your_username", "your_password");
            conn = dal.connect();
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
            System.out.println("test 1");
            if ("GVU".equals(role)){
                cst = conn.prepareCall("{CALL C##QLK.SP_GVU_INSERT_DANGKY(?,?,?,?,?,?,?,?,?,?)}");
                cst.setString(1, HOTENSV);
                cst.setString(2, TENHP);
                cst.setString(3, HOTENGV);
                cst.setInt(4, HK);
                cst.setInt(5, NAM);
                cst.setString(6, MACT);
                cst.setInt(7, DIEMTH);
                cst.setInt(8, DIEMQT);
                cst.setInt(9, DIEMCK);
                cst.setInt(10, DIEMTK);
                System.out.println("test 2");
                
            }
            int rowsAffected = cst.executeUpdate();
            System.out.println("test 3");
            if (rs.next()) {
                role = rs.getString("ROLE");
            }

            if (role != null) {
                System.out.println("Role: " + role);
            } else {
                System.out.println("No role found for the current user.");
            }

            if ("TKHOA".equals(role) || "SV".equals(role) || "TDV".equals(role) ||
            "GV".equals(role) || "NVCB".equals(role) || role == null) {
                System.out.println("No privileges (no grant).");
                System.out.println("khoa");
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
    @FXML
    private void deleteDKClick(ActionEvent event) {
    
        Dangky selectedDangky = dangkyTableView.getSelectionModel().getSelectedItem();
        String TENSV_OLD = selectedDangky.getSinhvien().getHOTEN();
        String TENGV_OLD = selectedDangky.getNhansu().getHOTEN();
        String TENHP_OLD = selectedDangky.getHocphan().getTENHP();
        String MACT_OLD = selectedDangky.getMACT();
        int HK_OLD = selectedDangky.getHK();
        int NAM_OLD = selectedDangky.getNAM();

        DataAccessLayer dal = null;
         Connection conn = null;
         CallableStatement cst = null;
        try {

             dal = DataAccessLayer.getInstance("", "");
             conn = dal.connect();
             int rowCountBefore = getRowCount(conn);
             cst = conn.prepareCall("{CALL C##QLK.SP_DELETE_DANGKY(?,?,?,?,?,?)}");
             cst.setString(1, TENSV_OLD);
             cst.setString(2, TENGV_OLD);
             cst.setString(3, TENHP_OLD);
             cst.setInt(4, HK_OLD);
             cst.setInt(5, NAM_OLD);
             cst.setString(6, MACT_OLD);
   
             cst.executeUpdate();
             int rowCountAfter = getRowCount(conn);
             
             if (rowCountAfter > rowCountBefore) {
                // Show success message
                showAlert(Alert.AlertType.INFORMATION, "Success", "Thêm thành công!");
            } else {
                // Show error message
                showAlert(Alert.AlertType.ERROR, "Error", "Lỗi! SDGN");
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

 private int getRowCount(Connection conn) throws SQLException {
    int rowCount = 0;
    try (Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM C##QLK.DANGKY")) {
        if (rs.next()) {
            rowCount = rs.getInt(1);
        }
    }
    return rowCount;
}

    @FXML
    private void updateDKClick(ActionEvent event) {
        Dangky selectedDangky = dangkyTableView.getSelectionModel().getSelectedItem();
        String TENSV_OLD = selectedDangky.getSinhvien().getHOTEN();
        String TENGV_OLD = selectedDangky.getNhansu().getHOTEN();
        String TENHP_OLD = selectedDangky.getHocphan().getTENHP();
        String MACT_OLD = selectedDangky.getMACT();
        int HK_OLD = selectedDangky.getHK();
        int NAM_OLD = selectedDangky.getNAM();
        // String hotensv = hotensvDisplay.getText().trim();
        // String hotengv = hotengvDisplay.getText().trim();
        // String tenhocphan = tenhocphanDisplay.getText().trim();
        // String mact = mactDisplay.getText().trim();
        // Integer hkStr = Integer.parseInt(hkDisplay.getText().trim());
        // Integer namStr = Integer.parseInt(namDisplay.getText().trim());
        Integer diemthStr = Integer.parseInt(diemthDisplay.getText().trim());
        Integer diemqtStr = Integer.parseInt(diemqtDisplay.getText().trim());
        Integer diemckStr = Integer.parseInt(diemckDisplay.getText().trim());
        Integer diemtkStr = Integer.parseInt(diemtkDisplay.getText().trim());
        
        

        DataAccessLayer dal = null;
         Connection conn = null;
         CallableStatement cst = null;
        try {

             dal = DataAccessLayer.getInstance("", "");
             conn = dal.connect();
             cst = conn.prepareCall("{CALL C##QLK.SP_ALL_UPDATE_DANGKY(?,?,?,?,?,?,?,?,?,?)}");
             cst.setString(1, TENSV_OLD);
             cst.setString(2, TENGV_OLD);
             cst.setString(3, TENHP_OLD);
             cst.setInt(4, HK_OLD);
             cst.setInt(5, NAM_OLD);
             cst.setString(6, MACT_OLD);

             cst.setInt(7,diemthStr);
             cst.setInt(8, diemqtStr);
             cst.setInt(9, diemckStr);
             cst.setInt(10, diemtkStr);
   
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


    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    @FXML
    private void refreshTable(ActionEvent event) {
        dangkyList.clear();
        loadDangkyFromDatabase();
        dangkyTableView.refresh();
    }
}