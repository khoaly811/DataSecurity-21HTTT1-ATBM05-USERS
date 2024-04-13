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
        
    }

    @FXML
    private void updatePCClick(ActionEvent event) {
       
    }


    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
