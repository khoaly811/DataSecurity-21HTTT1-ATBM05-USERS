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
import dto.Dangky;
import dto.Nhansu;
import dto.Sinhvien;
import dto.Hocphan;

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
    private void onAddClick_DANGKY() {
        System.out.println("Added");
    }

    @FXML
    private void onUpdateClick_DANGKY() {
        System.out.println("Updated");
    }

    @FXML
    private void onDeleteClick_DANGKY() {
        System.out.println("Delete");
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
        dangkyList = FXCollections.observableArrayList();
        loadDangkyFromDatabase();
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
            System.out.println("nhan beo 1");
            cst.execute();
            System.out.println("nhan beo 2");
            rs = (ResultSet) cst.getObject(1);
            System.out.println("Nhan beo 3");
            while (rs.next()) {
                Dangky dk = new Dangky();
                Sinhvien sv = new Sinhvien();
                Hocphan hp = new Hocphan();
                Nhansu ns = new Nhansu();

                String tensv = rs.getString("HOTEN_SV");
                System.out.println("TEN SINH VIEN: " + tensv);
                sv.setHOTEN(tensv);
                dk.setSinhvien(sv);

                String tengv = rs.getString("HOTEN_GV");
                System.out.println("TEN GIANG VIEN: " + tengv);
                ns.setHOTEN(tengv);
                dk.setNhansu(ns);

                String tenhp = rs.getString("TENHP");
                System.out.println("TEN HOC PHAN: " + tenhp);
                hp.setTENHP(tenhp);
                dk.setHocphan(hp);

                dk.setHK((rs.getInt("HK")));
                dk.setNAM((rs.getInt("NAM")));
                dk.setMACT((rs.getString("MACT")));
                dk.setDIEMTH((rs.getFloat("DIEMTH")));
                dk.setDIEMQT((rs.getFloat("DIEMQT")));
                dk.setDIEMCK((rs.getFloat("DIEMCK")));
                dk.setDIEMTK((rs.getFloat("DIEMTK")));

                dangkyList.add(dk);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            
        }

        // Set the loaded users to the table view
        dangkyTableView.setItems(dangkyList);
    }
}