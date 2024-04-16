package atbm05;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
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
        System.out.println("Added");
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

        try {
            dal = DataAccessLayer.getInstance("your_username", "your_password");
            conn = dal.connect();
            cst = conn.prepareCall("{CALL C##QLK.SP_VIEW_ADD_DANGKY(?)}");
            cst.registerOutParameter(1, OracleTypes.CURSOR);
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
            System.out.println(e.getMessage());
            
        }

        // Set the loaded users to the table view
        adddangkyTableView.setItems(adddangkyList);
    }

    @FXML
    private void deleteDKClick(ActionEvent event) {
    }

    @FXML
    private void updateDKClick(ActionEvent event) {
    
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