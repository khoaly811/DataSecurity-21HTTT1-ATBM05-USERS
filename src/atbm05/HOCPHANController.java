package atbm05;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
// import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import oracle.jdbc.OracleTypes;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import DataAccessLayer.DataAccessLayer;
import dto.Hocphan;
import dto.Donvi;

public class HOCPHANController {
    @FXML
    private TableView<Hocphan> hocphanTableView;

    @FXML
    private TableColumn<Hocphan, String> TENHP;

    @FXML
    private TableColumn<Hocphan, String> SOTC;

    @FXML
    private TableColumn<Hocphan, String> SOTIETLT;

    @FXML
    private TableColumn<Hocphan, String> SOTIETTH;

    @FXML
    private TableColumn<Hocphan, String> SOSVTOIDA;

    @FXML
    private TableColumn<Hocphan, String> TENDV;

    @FXML
    private void onAddClick_HOCPHAN() {
        System.out.println("Added");
    }

    @FXML
    private void onUpdateClick_HOCPHAN() {
        System.out.println("Updated");
    }

    @FXML
    private void onDeleteClick_HOCPHAN() {
        System.out.println("Delete");
    }

    @FXML
    private TextField tenhpDisplay;

    @FXML
    private TextField sotcDisPlay;

    @FXML
    private TextField sotietltDisplay;

    @FXML 
    private TextField sotietthDisplay;

    @FXML
    private TextField sosvtoidaDisplay;

    @FXML
    private TextField tendvDisplay;

    private ObservableList<Hocphan> hocphanList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        TENHP.setCellValueFactory(cellData -> cellData.getValue().TENHPproperty());
        SOTC.setCellValueFactory(cellData -> cellData.getValue().SOTCproperty().asString());
        SOTIETLT.setCellValueFactory(cellData -> cellData.getValue().SOTIETLTproperty().asString());
        SOTIETTH.setCellValueFactory(cellData -> cellData.getValue().SOTIETTHproperty().asString());
        SOSVTOIDA.setCellValueFactory(cellData -> cellData.getValue().SOSVTOIDAproperty().asString());
        TENDV.setCellValueFactory(cellData -> cellData.getValue().getDonvi().TENDVproperty());
        hocphanList = FXCollections.observableArrayList();
        loadHocphanFromDatabase();

        hocphanTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if(newSelection != null) {
                tenhpDisplay.setText(newSelection.getTENHP());
                sotcDisPlay.setText(String.valueOf(newSelection.getSOTC()));
                sotietltDisplay.setText(String.valueOf(newSelection.getSOTIETLT()));
                sotietthDisplay.setText(String.valueOf(newSelection.getSOTIETTH()));
                sosvtoidaDisplay.setText(String.valueOf(newSelection.getSOSVTOIDA()));
                tendvDisplay.setText(newSelection.getDonvi().getTENDV());
            }
        });
    }

    private void loadHocphanFromDatabase() {
        DataAccessLayer dal = null;
        Connection conn = null;
        CallableStatement cst = null;
        ResultSet rs = null;

        try {
            dal = DataAccessLayer.getInstance("your_username", "your_password");
            conn = dal.connect();
            cst = conn.prepareCall("{CALL C##QLK.SP_VIEW_HOCPHAN(?)}");
            cst.registerOutParameter(1, OracleTypes.CURSOR);
            System.out.println("nhan beo 1");
            cst.execute();
            System.out.println("nhan beo 2");
            rs = (ResultSet) cst.getObject(1);
            System.out.println("Nhan beo 3");
            while (rs.next()) {
                Hocphan hp = new Hocphan();
                Donvi dv = new Donvi();

                hp.setTENHP(rs.getString("TENHP"));
                hp.setSOTC(rs.getInt("SOTC"));
                hp.setSOTIETLT(rs.getInt("SOTIETLT"));
                hp.setSOTIETTH(rs.getInt("SOTIETTH"));
                hp.setSOSVTOIDA(rs.getInt("SOSVTOIDA"));

                String tendv = rs.getString("TENDV");
                System.out.println("TENDV là: " + tendv);
                dv.setTENDV(tendv);
                hp.setDonvi(dv);

                hocphanList.add(hp);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        hocphanTableView.setItems(hocphanList);
    }
}