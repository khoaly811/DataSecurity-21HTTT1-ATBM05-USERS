package atbm05;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
// import javafx.scene.control.TextField;
import oracle.jdbc.OracleTypes;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import DataAccessLayer.DataAccessLayer;
import dto.Thongbao;

public class THONGBAOController {
    @FXML
    private TableView<Thongbao> thongbaoTableView;

    @FXML
    private TableColumn<Thongbao, String> NOIDUNG;

    @FXML
    private TableColumn<Thongbao, String> THOIGIAN;

    @FXML
    private void onAddClick_THONGBAO() {
        System.out.println("Added");
    }

    @FXML
    private void onUpdateClick_THONGBAO() {
        System.out.println("Updated");
    }

    @FXML
    private void onDeleteClick_THONGBAO() {
        System.out.println("Delete");
    }

    // @FXML
    // private TextField noidungDisplay;

    // @FXML
    // private TextField thoigianDisplay;

    private ObservableList<Thongbao> thongbaoList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        NOIDUNG.setCellValueFactory(cellData -> cellData.getValue().NOIDUNGproperty());
        THOIGIAN.setCellValueFactory(cellData -> cellData.getValue().THOIGIANproperty().asString());
        thongbaoList = FXCollections.observableArrayList();
        loadThongbaoFromDatabase();

        // thongbaoTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
        //     if (newSelection != null) {
        //         noidungDisplay.setText(newSelection.getNOIDUNG());
        //         thoigianDisplay.setText(newSelection.getTHOIGIAN().toString());
        //     }
        // });
    }

    private void loadThongbaoFromDatabase() {
        DataAccessLayer dal = null;
        Connection conn = null;
        CallableStatement cst = null;
        ResultSet rs = null;

        try {
            dal = DataAccessLayer.getInstance("your_username", "your_password");
            conn = dal.connect();
            cst = conn.prepareCall("{CALL C##QLK.SP_VIEW_THONGBAO(?)}");
            cst.registerOutParameter(1, OracleTypes.CURSOR);
            System.out.println("nhan beo 1");
            cst.execute();
            System.out.println("nhan beo 2");
            rs = (ResultSet) cst.getObject(1);
            System.out.println("Nhan beo 3");
            while (rs.next()) {
                Thongbao tb = new Thongbao();

                tb.setNOIDUNG((rs.getString("NOIDUNG")));
                tb.setTHOIGIAN((rs.getTime("THOIGIAN")));

                thongbaoList.add(tb);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        thongbaoTableView.setItems(thongbaoList);
    }
}
