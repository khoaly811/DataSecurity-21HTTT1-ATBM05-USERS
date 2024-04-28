package atbm05;
import java.sql.Timestamp;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
// import javafx.scene.control.TextField;
import oracle.jdbc.OracleTypes;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import DataAccessLayer.DataAccessLayer;
import dto.DBA_AUDIT_TRAIL;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class STDAController {
    @FXML
    private TableView<DBA_AUDIT_TRAIL> STDATableView;

    @FXML
    private TableColumn<DBA_AUDIT_TRAIL, String> USERNAME;

    @FXML
    private TableColumn<DBA_AUDIT_TRAIL, String> TIMESTAMP;

    @FXML
    private TableColumn<DBA_AUDIT_TRAIL, String> OBJ_NAME;

    @FXML
    private TableColumn<DBA_AUDIT_TRAIL, String> ACTION_NAME;




    private ObservableList<DBA_AUDIT_TRAIL> STDAList = FXCollections.observableArrayList();
    @FXML
    private TabPane tabPane;

    @FXML
    private Tab fgaTab;
    @FXML
    public void initialize() {
 
        USERNAME.setCellValueFactory(cellData -> cellData.getValue().USERNAMEproperty());
        OBJ_NAME.setCellValueFactory(cellData -> cellData.getValue().OBJ_NAMEproperty());
        ACTION_NAME.setCellValueFactory(cellData -> cellData.getValue().ACTION_NAMEproperty());
        TIMESTAMP.setCellValueFactory(cellData -> cellData.getValue().TIMESTAMPproperty().asString());
        STDAList = FXCollections.observableArrayList();
        loadDBA_AUDIT_TRAILFromDatabase();
    }

    private void loadDBA_AUDIT_TRAILFromDatabase() {
        DataAccessLayer dal = null;
        Connection conn = null;
        CallableStatement cst = null;
        ResultSet rs = null;

        try {
            dal = DataAccessLayer.getInstance("your_username", "your_password");
            conn = dal.connect();
            cst = conn.prepareCall("{CALL C##QLK.SP_VIEW_STDA(?)}");
            cst.registerOutParameter(1, OracleTypes.CURSOR);
            cst.execute();
            rs = (ResultSet) cst.getObject(1);
            while (rs.next()) {
                DBA_AUDIT_TRAIL tb = new DBA_AUDIT_TRAIL();

                tb.setUSERNAME((rs.getString("USERNAME")));
                tb.setOBJ_NAME((rs.getString("OBJ_NAME")));
                tb.setACTION_NAME((rs.getString("ACTION_NAME")));
                tb.setTIMESTAMP((rs.getTimestamp("TIMESTAMP")));

                STDAList.add(tb);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        STDATableView.setItems(STDAList);
    }
    @FXML
    private void refreshTable(ActionEvent event) {
        STDAList.clear();
        loadDBA_AUDIT_TRAILFromDatabase();
        STDATableView.refresh();
    }
       
}
