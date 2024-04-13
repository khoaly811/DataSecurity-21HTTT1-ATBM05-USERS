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

    @FXML
    private void deleteHPClick(ActionEvent event) {
        System.out.println("Delete");
        Nhansu selectedNhanSu = phancongTableView.getSelectionModel().getSelectedItem();

        if (selectedNhanSu != null) {
            String MANV = selectedNhanSu.getMANV();
            
            DataAccessLayer dal = null;
            Connection conn = null;
            CallableStatement cst = null;
            
            try {
                dal = DataAccessLayer.getInstance("", "");
                conn = dal.connect();
                cst = conn.prepareCall("{CALL C##QLK.SP_DELETE_HOCPHAN(?)}");
                cst.setString(1, MANV);
                
                int rowsAffected = cst.executeUpdate();
                
                if (rowsAffected > 0) {
                    System.out.println("Deleted successfully.");
                    // You can show a success message here if needed
                } else {
                    System.out.println("No rows deleted.");
                    // You can show a message indicating that no rows were deleted if needed
                }
            } catch (SQLException e) {
                System.out.println("Failed to delete: " + e.getMessage());
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete user: " + e.getMessage());
            } finally {
                // Close resources in a finally block to ensure they are always closed
                try {
                    if (cst != null) cst.close();
                    if (conn != null) conn.close();
                } catch (SQLException e) {
                    System.out.println("Error closing resources: " + e.getMessage());
                }
            }
        } else {
            System.out.println("No row selected.");
        }
    }

    @FXML
    private void updateHPClick(ActionEvent event) {
        String INP_HOTEN = hotenDisplay.getText().trim();
        String INP_PHAI = phaiDisplay.getText().trim();
        LocalDate INP_NGSINH = LocalDate.parse(ngsinhDisplay.getText().trim()); // Assuming your date format is parseable
        int INP_PHUCAP = Integer.parseInt(phucapDisplay.getText().trim());
        String INP_DT = dienthoaiDisplay.getText().trim();

        DataAccessLayer dal = null;
        Connection conn = null;
        CallableStatement cst = null;
        ResultSet rs = null;

        try {
            dal = DataAccessLayer.getInstance("your_username", "your_password");
            conn = dal.connect();
            cst = conn.prepareCall("{CALL C##QLK.SP_ALL_UPDATE_HOCPHAN(?,?,?,?,?)}");
            cst.setString(1, INP_HOTEN);
            cst.setString(2, INP_PHAI);
            cst.setDate(3, java.sql.Date.valueOf(INP_NGSINH));
            cst.setInt(4, INP_PHUCAP);
            cst.setString(5, INP_DT);
            cst.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            // Close resources
            try {
                if (rs != null) rs.close();
                if (cst != null) cst.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }


    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
