package atbm05;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;

import java.sql.CallableStatement;
import java.sql.Connection;

import dto.Nhansu;

public class NHANSUController {
    @FXML
    private TableView<Role> nhansuTableView;

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
        PHUCAP.setCellValueFactory(cellData -> cellData.getValue().PHUCAPproperty());
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
    }

    private void loadRolesFromDatabase() {
        DataAccessLayer dal = null;
        Connection conn = null;
        CallableStatement cst = null;
        ResultSet rs = null;

        try {
            dal = DataAccessLayer.getInstance("your_username", "your_password");
            conn = dal.connect();
            cst = conn.prepareCall("{CALL SP_VIEW_TABLE(?)}");
            cst.registerOutParameter(1, "NHANSU");
            cst.execute();
            rs = cst.getObject(1);
            while (rs.next()) {
                Nhansu ns = new Nhansu();
                ns.setMANV(rs.getString("MANV"));
                ns.setHOTEN((rs.getString("HOTEN")));
                ns.setPHAI((rs.getString("PHAI")));
                ns.setNGSINH((rs.getString("NGSINH")));
                ns.setPHUCAP((rs.getString("PHUCAP")));
                ns.setDT((rs.getString("DT")));
                ns.setVAITRO((rs.getString("VAITRO")));
                ns.setMADV((rs.getString("MADV")));

                nhansuList.add(ns);
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load roles from the database.");
            System.out.println(e.getMessage());
        }

        // Set the loaded users to the table view
        nhansuListTableView.setItems(nhansuList);
    }
}
