package atbm05;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import oracle.jdbc.OracleTypes;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import DataAccessLayer.DataAccessLayer;
import dto.Donvi;
import dto.Nhansu;

public class DONVIController {
    @FXML
    private TableView<Donvi> donviTableView;

    @FXML 
    private TableColumn<Donvi, String> TENDV;

    @FXML 
    private TableColumn<Donvi, String> HOTEN_TDV;

    @FXML
    private void onAddClick_DONVI() {
        System.out.println("Added");
    }

    @FXML
    private void onUpdateClick_DONVI() {
        System.out.println("Updated");
    }

    @FXML
    private void onDeleteClick_DONVI() {
        System.out.println("Delete");
    }

    @FXML
    private TextField tendvDisplay;

    @FXML
    private TextField truongdvDisPlay;

    private ObservableList<Donvi> donviList = FXCollections.observableArrayList();

    public void initialize(){
        TENDV.setCellValueFactory(cellData -> cellData.getValue().TENDVproperty());
        HOTEN_TDV.setCellValueFactory(cellData -> cellData.getValue().getNhansu().HOTENproperty());
        donviList = FXCollections.observableArrayList();
        loadDonviFromDatabase();

        donviTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if(newSelection != null){
                tendvDisplay.setText(newSelection.getTENDV());
                truongdvDisPlay.setText(newSelection.getNhansu().getHOTEN());
            }
        });
    }

    private void loadDonviFromDatabase() {
        DataAccessLayer dal = null;
        Connection conn = null;
        CallableStatement cst = null;
        ResultSet rs = null;

        try {
            dal = DataAccessLayer.getInstance("your_username", "your_password");
            conn = dal.connect();
            cst = conn.prepareCall("{CALL C##QLK.SP_VIEW_DONVI(?)}");
            cst.registerOutParameter(1, OracleTypes.CURSOR);
            System.out.println("nhan beo 1");
            cst.execute();
            System.out.println("nhan beo 2");
            rs = (ResultSet) cst.getObject(1);
            System.out.println("Nhan beo 3");
            while (rs.next()) {
                Donvi dv = new Donvi();
                Nhansu ns = new Nhansu();

                dv.setTENDV(rs.getString("TENDV"));

                String tentdv = rs.getString("HOTEN_TDV");
                System.out.println("TEN TRUONG DON VI: " + tentdv);
                ns.setHOTEN(tentdv);
                dv.setNhansu(ns);

                donviList.add(dv);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            
        }
        
        donviTableView.setItems(donviList);
    }
}
