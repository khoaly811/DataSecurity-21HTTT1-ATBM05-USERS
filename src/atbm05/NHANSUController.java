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
import dto.Nhansu;

public class NHANSUController {
    @FXML
    private TableView<Nhansu> nhansuTableView;

    @FXML
    private TableColumn<Nhansu, String> MANV;

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
    private TableColumn<Nhansu, String> MADV;

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

    private ObservableList<Nhansu> nhansuList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Initialize table columns
        MANV.setCellValueFactory(cellData -> cellData.getValue().MANVproperty());
        HOTEN.setCellValueFactory(cellData -> cellData.getValue().HOTENproperty());
        PHAI.setCellValueFactory(cellData -> cellData.getValue().PHAIproperty());
        NGSINH.setCellValueFactory(cellData -> cellData.getValue().NGSINHproperty().asString());
        PHUCAP.setCellValueFactory(cellData -> cellData.getValue().PHUCAPproperty().asString());
        DT.setCellValueFactory(cellData -> cellData.getValue().DTproperty());
        VAITRO.setCellValueFactory(cellData -> cellData.getValue().VAITROproperty());
        MADV.setCellValueFactory(cellData -> cellData.getValue().MADVproperty());
        // searchRoleField.textProperty().addListener((observable, oldValue, newValue) -> {
        //     searchRoles(newValue);
        // });
        // searchUserField.textProperty().addListener((observable, oldValue, newValue) -> {
        //     searchUsers(newValue);
        // });
        nhansuList = FXCollections.observableArrayList();
        loadNhansuFromDatabase();
    }

    private void loadNhansuFromDatabase() {
        DataAccessLayer dal = null;
        Connection conn = null;
        CallableStatement cst = null;
        ResultSet rs = null;

        try {
            dal = DataAccessLayer.getInstance("your_username", "your_password");
            conn = dal.connect();
            cst = conn.prepareCall("{CALL C##QLK.SP_VIEW_TABLE(?,?)}");
            cst.setString(1, "NHANSU");
            cst.registerOutParameter(2, OracleTypes.CURSOR);
            System.out.println("nhan beo 1");
            cst.execute();
            System.out.println("nhan beo 2");
            rs = (ResultSet) cst.getObject(2);
            System.out.println("Nhan beo 3");
            while (rs.next()) {
                Nhansu ns = new Nhansu();
                ns.setMANV(rs.getString("MANV"));
                ns.setHOTEN((rs.getString("HOTEN")));
                ns.setPHAI((rs.getString("PHAI")));
                ns.setNGSINH((rs.getDate("NGSINH").toLocalDate()));
                ns.setPHUCAP((rs.getInt("PHUCAP")));
                ns.setDT((rs.getString("DT")));
                ns.setVAITRO((rs.getString("VAITRO")));
                ns.setMADV((rs.getString("MADV")));

                nhansuList.add(ns);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            
        }

        // Set the loaded users to the table view
        nhansuTableView.setItems(nhansuList);
    }
}
