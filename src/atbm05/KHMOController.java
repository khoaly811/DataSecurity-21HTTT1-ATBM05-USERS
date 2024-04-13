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
        
    }

    @FXML
    private void updateKHMOClick(ActionEvent event) {
        
    }


    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}   
