package atbm05;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import oracle.jdbc.OracleTypes;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


import DataAccessLayer.DataAccessLayer;
import dto.*;

public class ADDDANGKYController {
    @FXML
    private TableView<KHmo> adddangkyTableView;

    @FXML
    private TableColumn<KHmo, String> TENHP;

    @FXML
    private TableColumn<KHmo, String> NGAYBD;

    @FXML
    private TableColumn<KHmo, String> MADV;

    @FXML
    private TableColumn<KHmo, String> HK;

    @FXML
    private TableColumn<KHmo, String> NAM;

    @FXML
    private TableColumn<KHmo, String> MACT;

    @FXML
    private TableColumn<KHmo, String> SOTC;
    @FXML
    private TableColumn<KHmo, String> STLT;
    @FXML
    private TableColumn<KHmo, String> STTH;
    @FXML
    private TableColumn<KHmo, String> SOSVTD;


    @FXML
    private void onAddClick_DANGKY() {
       
    }

    private ObservableList<KHmo> adddangkyList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Initialize table columns
        TENHP.setCellValueFactory(cellData -> cellData.getValue().getHocphan().TENHPproperty());
        HK.setCellValueFactory(cellData -> cellData.getValue().HOCKYproperty().asString());
        NAM.setCellValueFactory(cellData -> cellData.getValue().NAMproperty().asString());
        NGAYBD.setCellValueFactory(cellData -> cellData.getValue().NGAYBDproperty().asString());
        MACT.setCellValueFactory(cellData -> cellData.getValue().MACTproperty());
        MADV.setCellValueFactory(cellData->cellData.getValue().getHocphan().MADVproperty());
        SOTC.setCellValueFactory(cellData->cellData.getValue().getHocphan().SOTCproperty().asString());
        STLT.setCellValueFactory(cellData->cellData.getValue().getHocphan().SOTIETLTproperty().asString());
        STTH.setCellValueFactory(cellData->cellData.getValue().getHocphan().SOTIETTHproperty().asString());
        SOSVTD.setCellValueFactory(cellData->cellData.getValue().getHocphan().SOSVTOIDAproperty().asString());
        adddangkyList = FXCollections.observableArrayList();
        loadDangkyFromDatabase();
        // KHONG CAN VI HE THONG SE TU LAY MA SINH VIEN DANG KY
        // khmoTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
        //     if (newSelection != null) {
                
        //     }
        // });
    }

    private void loadDangkyFromDatabase() {
        DataAccessLayer dal = null;
        Connection conn = null;
        CallableStatement cst = null;
        ResultSet rs = null;
        String role = null;
        PreparedStatement pst = null;

        try {
            dal = DataAccessLayer.getInstance("your_username", "your_password");
            conn = dal.connect();
            pst = conn.prepareStatement("SELECT * FROM SESSION_ROLES");
            rs = pst.executeQuery();
            if (rs.next()) {
                role = rs.getString("ROLE");
            }   
            if ("SV".equals(role)){
            cst = conn.prepareCall("{CALL C##QLK.SP_SV_VIEW_ADD_DANGKY(?)}");
            cst.registerOutParameter(1, OracleTypes.CURSOR);
            }
            else{
                cst = conn.prepareCall("{CALL C##QLK.SP_VIEW_ADD_DANGKY(?)}");
                cst.registerOutParameter(1, OracleTypes.CURSOR);
            }
            cst.execute();
            rs = (ResultSet) cst.getObject(1);
            while (rs.next()) {
                KHmo kh = new KHmo();
                Hocphan hp = new Hocphan();
                kh.setMACT((rs.getString("MACT")));
                kh.setHOCKY((rs.getInt("HK")));
                kh.setNAM((rs.getInt("NAM")));
                kh.setNGAYBD((rs.getDate("NGAYBD").toLocalDate()));
                String tenhp = rs.getString("TENHP");
                hp.setTENHP(tenhp);
                int sotc = rs.getInt("SOTC");
                hp.setSOTC(sotc);
                int stlt = rs.getInt("STLT");
                hp.setSOTIETLT(stlt);
                int stth = rs.getInt("STTH");
                hp.setSOTIETLT(stth);
                int sosvtd = rs.getInt("SOSVTD");
                hp.setSOSVTOIDA(sosvtd);
                String madv = rs.getString("MADV");
                hp.setMADV(madv);
                kh.setHocphan(hp);
                adddangkyList.add(kh);
            }
        } catch (SQLException e) {
           
            
        }

        // Set the loaded users to the table view
        adddangkyTableView.setItems(adddangkyList);
    }

    @FXML
    private void addDangkyClick(ActionEvent event){
        KHmo selectedDangky = adddangkyTableView.getSelectionModel().getSelectedItem();
        if (selectedDangky != null){
            String TENHP = selectedDangky.getHocphan().getTENHP();
            int HK = selectedDangky.getHOCKY();
            int NAM = selectedDangky.getNAM();
            String MACT = selectedDangky.getMACT();
            DataAccessLayer dal = null;
            Connection conn = null;
            CallableStatement cst = null;
            try {
                dal = DataAccessLayer.getInstance("your_username", "your_password");
                conn = dal.connect();
                cst = conn.prepareCall("{CALL C##QLK.SP_INSERT_DANGKY(?,?,?,?)}");
                cst.setString(1, TENHP);
                cst.setInt(2, HK);
                cst.setInt(3, NAM);
                cst.setString(4, MACT);
                int rowsAffected = cst.executeUpdate();
                if (rowsAffected > 0) {
                    // Show success message
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Thêm thành công!");
                } else {
                    // Show error message
                    showAlert(Alert.AlertType.ERROR, "Error", "Lỗi!");
                }
            } catch (SQLException e) {
                System.out.println("Error: " + e.getMessage());
                // Show error message
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to add user: " + e.getMessage());
            }
        } else {
            // Show error message indicating no item is selected
            showAlert(Alert.AlertType.ERROR, "Error", "No item selected!");
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
        adddangkyList.clear();
        loadDangkyFromDatabase();
        adddangkyTableView.refresh();
    }
}