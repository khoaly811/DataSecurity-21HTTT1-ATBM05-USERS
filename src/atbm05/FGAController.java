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
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Button;
import javafx.scene.control.Alert.AlertType;
import DataAccessLayer.DataAccessLayer;
import dto.DBA_FGA_AUDIT_TRAIL;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class FGAController {
    @FXML
    private TableView<DBA_FGA_AUDIT_TRAIL> FGATableView;

    @FXML
    private TableColumn<DBA_FGA_AUDIT_TRAIL, String> DB_USER;

    @FXML
    private TableColumn<DBA_FGA_AUDIT_TRAIL, String> EXTENDED_TIMESTAMP;

    @FXML
    private TableColumn<DBA_FGA_AUDIT_TRAIL, String> OBJECT_NAME;

    @FXML
    private TableColumn<DBA_FGA_AUDIT_TRAIL, String> STATEMENT_TYPE;

    @FXML
    private TableColumn<DBA_FGA_AUDIT_TRAIL, String> SQL_TEXT;


    private ObservableList<DBA_FGA_AUDIT_TRAIL> FGAList = FXCollections.observableArrayList();
    @FXML
    private TabPane tabPane;

    @FXML
    private Tab fgaTab;
    @FXML
    public void initialize() {
 
        DB_USER.setCellValueFactory(cellData -> cellData.getValue().DB_USERproperty());
        OBJECT_NAME.setCellValueFactory(cellData -> cellData.getValue().OBJECT_NAMEproperty());
        STATEMENT_TYPE.setCellValueFactory(cellData -> cellData.getValue().STATEMENT_TYPEproperty());
        SQL_TEXT.setCellValueFactory(cellData -> cellData.getValue().SQL_TEXTproperty());
        EXTENDED_TIMESTAMP.setCellValueFactory(cellData -> cellData.getValue().EXTENDED_TIMESTAMPproperty().asString());
        FGAList = FXCollections.observableArrayList();
        loadDBA_FGA_AUDIT_TRAILFromDatabase();
    }

    private void loadDBA_FGA_AUDIT_TRAILFromDatabase() {
        DataAccessLayer dal = null;
        Connection conn = null;
        CallableStatement cst = null;
        ResultSet rs = null;

        try {
            dal = DataAccessLayer.getInstance("your_username", "your_password");
            conn = dal.connect();
            cst = conn.prepareCall("{CALL C##QLK.SP_VIEW_FGA(?)}");
            cst.registerOutParameter(1, OracleTypes.CURSOR);
            cst.execute();
            rs = (ResultSet) cst.getObject(1);
            while (rs.next()) {
                DBA_FGA_AUDIT_TRAIL tb = new DBA_FGA_AUDIT_TRAIL();

                tb.setDB_USER((rs.getString("DB_USER")));
                tb.setOBJECT_NAME((rs.getString("OBJECT_NAME")));
                tb.setSTATEMENT_TYPE((rs.getString("STATEMENT_TYPE")));
                tb.setSQL_TEXT((rs.getString("SQL_TEXT")));
                tb.setEXTENDED_TIMESTAMP((rs.getTimestamp("EXTENDED_TIMESTAMP")));

                FGAList.add(tb);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        FGATableView.setItems(FGAList);
    }
    @FXML
    private void refreshTable(ActionEvent event) {
        FGAList.clear();
        loadDBA_FGA_AUDIT_TRAILFromDatabase();
        FGATableView.refresh();
    }

    @FXML
    private void onFGAmoney(ActionEvent event) {
     
        DataAccessLayer dal = null;
        Connection conn = null;
        CallableStatement cst = null;
        try {
            dal = DataAccessLayer.getInstance("", "");
            conn = dal.connect();
            cst = conn.prepareCall("{CALL dbms_fga.enable_policy('C##QLK', 'NHANSU', 'FGA_PHUCAP_AUDIT')}");
            
         
             

            int rowsAffected = cst.executeUpdate();
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Kích hoạt thành công!");
            alert.showAndWait();
            

        } catch (SQLException e) {
            System.out.println("Failed to enble PHUCAP: " + e.getMessage());
            //showAlert(Alert.AlertType.ERROR, "Error", "Failed to Update user: " + e.getMessage());
            
        }
    }
    @FXML
    private void offFGAmoney(ActionEvent event) {
        DataAccessLayer dal = null;
        Connection conn = null;
        CallableStatement cst = null;
        try {
            dal = DataAccessLayer.getInstance("", "");
            conn = dal.connect();
            cst = conn.prepareCall("{CALL dbms_fga.disable_policy('C##QLK', 'NHANSU', 'FGA_PHUCAP_AUDIT')}");
            
         
             

            int rowsAffected = cst.executeUpdate();
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Hủy thành công!");
            alert.showAndWait();
            

        } catch (SQLException e) {
            System.out.println("Failed to dis-enble PHUCAP: " + e.getMessage());
            //showAlert(Alert.AlertType.ERROR, "Error", "Failed to Update user: " + e.getMessage());
            
        }
        
    }

    @FXML
    private void offFGApoint(ActionEvent event) {
        DataAccessLayer dal = null;
        Connection conn = null;
        CallableStatement cst = null;
        try {
            dal = DataAccessLayer.getInstance("", "");
            conn = dal.connect();
            cst = conn.prepareCall("{CALL dbms_fga.disable_policy('C##QLK', 'DANGKY', 'UPDATE_DIEM_AUDIT')}");
            
         
             

            int rowsAffected = cst.executeUpdate();
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Hủy thành công!");
            alert.showAndWait();
            

        } catch (SQLException e) {
            System.out.println("Failed to dis-enble PHUCAP: " + e.getMessage());
            //showAlert(Alert.AlertType.ERROR, "Error", "Failed to Update user: " + e.getMessage());
            
        }
        
    }

    @FXML
    private void onFGApoint(ActionEvent event) {
     
        DataAccessLayer dal = null;
        Connection conn = null;
        CallableStatement cst = null;
        try {
            dal = DataAccessLayer.getInstance("", "");
            conn = dal.connect();
            cst = conn.prepareCall("{CALL dbms_fga.enable_policy('C##QLK', 'DANGKY', 'UPDATE_DIEM_AUDIT')}");
            
         
             

            int rowsAffected = cst.executeUpdate();
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Kích hoạt thành công!");
            alert.showAndWait();
            

        } catch (SQLException e) {
            System.out.println("Failed to enble PHUCAP: " + e.getMessage());
            //showAlert(Alert.AlertType.ERROR, "Error", "Failed to Update user: " + e.getMessage());
            
        }
    }
       
}
