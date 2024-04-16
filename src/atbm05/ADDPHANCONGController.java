package atbm05;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import oracle.jdbc.OracleTypes;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.Action;

import DataAccessLayer.DataAccessLayer;
import dto.*;

public class ADDPHANCONGController {
    @FXML
    private TableView<KHmo> addphancongTableView;

    @FXML
    private TableColumn<KHmo, String> TENHP;

    @FXML
    private TableColumn<KHmo, String> HK;

    @FXML
    private TableColumn<KHmo, String> NGAYBD;

    @FXML
    private TableColumn<KHmo, String> NAM;

    @FXML
    private TableColumn<KHmo, String> SOTC;
    @FXML
    private TableColumn<KHmo, String> STLT;
    @FXML
    private TableColumn<KHmo, String> STTH;
    @FXML
    private TableColumn<KHmo, String> SOSVTD;
    @FXML
    private TableColumn<KHmo, String> TENDV;
    @FXML
    private TableColumn<KHmo, String> MAHP;

    @FXML
    private Button updatePC;


    @FXML
    private void onAddClick_PHANCONG() {
        System.out.println("Added");
    }

    @FXML
    private void profileButtonclick() {

    }

    @FXML
    private TextField giaovienDisplay;



    private ObservableList<KHmo> addphancongList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        TENHP.setCellValueFactory(cellData -> cellData.getValue().getHocphan().TENHPproperty());
        HK.setCellValueFactory(cellData -> cellData.getValue().HOCKYproperty().asString());
        NGAYBD.setCellValueFactory(cellData -> cellData.getValue().NGAYBDproperty().asString());
        NAM.setCellValueFactory(cellData -> cellData.getValue().NAMproperty().asString());
        SOTC.setCellValueFactory(cellData -> cellData.getValue().getHocphan().SOTCproperty().asString());
        STLT.setCellValueFactory(cellData -> cellData.getValue().getHocphan().SOTIETLTproperty().asString());
        STTH.setCellValueFactory(cellData -> cellData.getValue().getHocphan().SOTIETTHproperty().asString());
        SOSVTD.setCellValueFactory(cellData -> cellData.getValue().getHocphan().SOSVTOIDAproperty().asString());
        TENDV.setCellValueFactory(cellData -> cellData.getValue().getDonvi().TENDVproperty());
        MAHP.setCellValueFactory(cellData -> cellData.getValue().MAHPproperty());
        loadPhancongFromDatabase();

        // KHONG XAI O DAY
        //Add listener to the TableView selection model
        // addphancongTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
        //     if (newSelection != null) {
        //         // Display the selected row's information in the corresponding TextFields
        //         giaovienDisplay.setText(newSelection.getNhansu().getHOTEN());
        //     }
        // });
    }

    private void loadPhancongFromDatabase() {
        DataAccessLayer dal = null;
        Connection conn = null;
        CallableStatement cst = null;
        ResultSet rs = null;

        try {
            dal = DataAccessLayer.getInstance("your_username", "your_password");
            conn = dal.connect();
            cst = conn.prepareCall("{CALL C##QLK.SP_VIEW_ADD_PHANCONG(?)}");
            cst.registerOutParameter(1, OracleTypes.CURSOR);
            cst.execute();
            rs = (ResultSet) cst.getObject(1);
            while (rs.next()) {
                Hocphan hp = new Hocphan();
                Donvi dv = new Donvi();
                KHmo kh = new KHmo();
                String tenhp = rs.getString("TENHP");
                hp.setTENHP(tenhp);
                kh.setHOCKY((rs.getInt("HK")));
                kh.setNAM((rs.getInt("NAM")));
                kh.setNGAYBD((rs.getDate("NGAYBD").toLocalDate()));
                kh.setMACT((rs.getString("MACT")));
                int sotc = rs.getInt("SOTC");
                int stlt = rs.getInt("STLT");
                int stth = rs.getInt("STTH");
                int sosvtd = rs.getInt("SOSVTD");
                hp.setSOTC(sotc);
                hp.setSOTIETLT(stlt);
                hp.setSOTIETTH(stth);
                hp.setSOSVTOIDA(sosvtd);
                String tendv = rs.getString("TENDV");
                dv.setTENDV(tendv);
                kh.setMAHP((rs.getString("MAHP")));
                kh.setHocphan(hp);
                kh.setDonvi(dv);
                addphancongList.add(kh);
            
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());

        }

        addphancongTableView.setItems(addphancongList);
    }
    @FXML
    private void addPhancongClick(ActionEvent event){
        System.out.println("na beo 1");
        String INP_HOTEN = giaovienDisplay.getText().trim();
        System.out.println("na beo 1+");
        KHmo selectedPhancong = addphancongTableView.getSelectionModel().getSelectedItem();
        String TENHP = selectedPhancong.getHocphan().getTENHP();
        String MACT_OLD = selectedPhancong.getMACT();
        int HK_OLD = selectedPhancong.getHOCKY();
        int NAM_OLD = selectedPhancong.getNAM();
        System.out.println("na beo 2");
        System.out.println(TENHP);
        System.out.println(MACT_OLD);
        System.out.println(HK_OLD);
        System.out.println(NAM_OLD);
        DataAccessLayer dal = null;
        Connection conn = null;
        CallableStatement cst = null;
        try {
            dal = DataAccessLayer.getInstance("your_username", "your_password");
            conn = dal.connect();
            cst = conn.prepareCall("{CALL C##QLK.SP_INSERT_PHANCONG(?,?,?,?,?)}");
            cst.setString(1, INP_HOTEN);
            cst.setString(2, TENHP);
            cst.setInt(3, HK_OLD);
            cst.setInt(4, NAM_OLD);
            cst.setString(5, MACT_OLD);
            int rowsAffected = cst.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Insert successfully.");
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Thêm thành công!");
                alert.showAndWait();

            } else{
                System.out.println("Insert unsuccessfully.");
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Lỗi!");
                alert.showAndWait();

            }
        }catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            //showAlert(Alert.AlertType.ERROR, "Error", "Failed to add user: " + e.getMessage());

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
        addphancongList.clear();
        loadPhancongFromDatabase();
        addphancongTableView.refresh();
    }
}
