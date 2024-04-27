package atbm05;
import java.sql.Timestamp;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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
            cst.execute();
            rs = (ResultSet) cst.getObject(1);
            while (rs.next()) {
                Thongbao tb = new Thongbao();

                tb.setNOIDUNG((rs.getString("NOIDUNG")));
                tb.setTHOIGIAN((rs.getTimestamp("THOIGIAN")));

                thongbaoList.add(tb);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        thongbaoTableView.setItems(thongbaoList);
    }
    @FXML
    private void refreshTable(ActionEvent event) {
        thongbaoList.clear();
        loadThongbaoFromDatabase();
        thongbaoTableView.refresh();
    }
       
}
