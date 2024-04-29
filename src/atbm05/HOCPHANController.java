package atbm05;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Alert.AlertType;
// import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import oracle.jdbc.OracleTypes;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
// import java.sql.Statement;

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
    private TextField tenhpDisplay;

    @FXML
    private TextField sotcDisplay;

    @FXML
    private TextField sotietltDisplay;

    @FXML 
    private TextField sotietthDisplay;

    @FXML
    private TextField sosvtoidaDisplay;

    @FXML
    private ChoiceBox tendvDisplayDrop;

    @FXML
    private Button updateHP;

    @FXML
    private TextField searchHP;

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
        populateChoiceBox();

        hocphanTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if(newSelection != null) {
                tenhpDisplay.setText(newSelection.getTENHP());
                sotcDisplay.setText(String.valueOf(newSelection.getSOTC()));
                sotietltDisplay.setText(String.valueOf(newSelection.getSOTIETLT()));
                sotietthDisplay.setText(String.valueOf(newSelection.getSOTIETTH()));
                sosvtoidaDisplay.setText(String.valueOf(newSelection.getSOSVTOIDA()));
                tendvDisplayDrop.getSelectionModel().select(newSelection.getDonvi().getTENDV());
            }
        });

        searchHP.textProperty().addListener((observable, oldValue, newValue) -> {
            searchHocphan(newValue);
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
            cst.execute();
            rs = (ResultSet) cst.getObject(1);
            while (rs.next()) {
                Hocphan hp = new Hocphan();
                Donvi dv = new Donvi();

                hp.setTENHP(rs.getString("TENHP"));
                hp.setSOTC(rs.getInt("SOTC"));
                hp.setSOTIETLT(rs.getInt("STLT"));
                hp.setSOTIETTH(rs.getInt("STTH"));
                hp.setSOSVTOIDA(rs.getInt("SOSVTD"));

                String tendv = rs.getString("TENDV");
                dv.setTENDV(tendv);
                hp.setDonvi(dv);

                hocphanList.add(hp);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        hocphanTableView.setItems(hocphanList);
    }

    private List<String> loadDistinctTENDVFromDatabase(){
        List<String> tendvList = new ArrayList<>();

        DataAccessLayer dal = null;
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            dal = DataAccessLayer.getInstance("your_username", "your_password");
            conn = dal.connect();

            String sql = "SELECT TENDV FROM C##QLK.DONVI";

            pst = conn.prepareStatement(sql);

            rs = pst.executeQuery();

            while (rs.next()) {
                String tendv = rs.getString("TENDV");
                tendvList.add(tendv);
            }

        } catch (SQLException e) {
            System.out.println("Error Message: " + e.getMessage());
            e.printStackTrace();

        }

        return tendvList;
    }

    private void populateChoiceBox() {
        List<String> tendvList = loadDistinctTENDVFromDatabase();

        // Clear existing items in ChoiceBox
        tendvDisplayDrop.getItems().clear();

        // Populate ChoiceBox with distinct TENDV values
        tendvDisplayDrop.getItems().addAll(tendvList);

        // Set the first item as the default selected item (Optional)
        if (!tendvList.isEmpty()) {
            tendvDisplayDrop.getSelectionModel().selectFirst();
        }
    }


    @FXML
    private void updateHPClick(ActionEvent event) {
        Hocphan selectedHocphan = hocphanTableView.getSelectionModel().getSelectedItem();
        String TENHP_OLD = selectedHocphan.getTENHP();
        String TENHP = tenhpDisplay.getText().trim();
        int SOTC = Integer.parseInt(sotcDisplay.getText().trim());
        int STLT = Integer.parseInt(sotietltDisplay.getText().trim());
        int STTH = Integer.parseInt(sotietthDisplay.getText().trim());
        int SOSVTD = Integer.parseInt(sosvtoidaDisplay.getText().trim());
        String TENDV = ((String) tendvDisplayDrop.getValue()).trim(); 
        DataAccessLayer dal = null;
        Connection conn = null;
        CallableStatement cst = null;

        try {

            dal = DataAccessLayer.getInstance("", "");
            conn = dal.connect();
            cst = conn.prepareCall("{CALL C##QLK.SP_ALL_UPDATE_HOCPHAN(?,?,?,?,?,?,?)}");
            cst.setString(1, TENHP_OLD);
            cst.setString(2, TENHP);
            cst.setInt(3, SOTC);
            cst.setInt(4, STLT);
            cst.setInt(5, STTH);
            cst.setInt(6, SOSVTD);
            cst.setString(7, TENDV);

            int rowsAffected = cst.executeUpdate();

            String grantedRole = null;
            PreparedStatement pst = null;
            ResultSet rs = null;
            pst = conn
                    .prepareStatement(
                            "SELECT * FROM SESSION_ROLES");

            rs = pst.executeQuery();

            if (rs.next()) {
                grantedRole = rs.getString("ROLE");
            }

            if (grantedRole != null) {
                System.out.println("Granted role: " + grantedRole);
            } else {
                System.out.println("No role found for the current user.");
            }

            if ("SV".equals(grantedRole) || "NVCB".equals(grantedRole) || "GV".equals(grantedRole) ||
            "TKHOA".equals(grantedRole) || "TDV".equals(grantedRole) || grantedRole == null) {
                System.out.println("No privileges (no grant).");
                // Show an error alert
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Lỗi: không có quyền!");
                alert.showAndWait();

            } else if (rowsAffected > 0) {
                System.out.println("Update successfully.");
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Cập nhật thành công!");
                alert.showAndWait();

            } else{
                System.out.println("Update unsuccessfully.");
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Lỗi!");
                alert.showAndWait();

            }

        } catch (SQLException e) {
            System.out.println("Failed to Update: " + e.getMessage());
            //showAlert(Alert.AlertType.ERROR, "Error", "Failed to Update user: " + e.getMessage());
            if (e.getMessage().contains("ORA-01031")) {
                System.out.println("No privileges (no grant).");
                // Show an error alert
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Lỗi");
                alert.showAndWait();
            }
            else{
                System.out.println("Unexpected error");
                // Show an error alert
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Lỗi khi chạy !!!");
                alert.showAndWait();
            }
        }
    }


    // private void showAlert(Alert.AlertType alertType, String title, String message) {
    //     Alert alert = new Alert(alertType);
    //     alert.setTitle(title);
    //     alert.setHeaderText(null);
    //     alert.setContentText(message);
    //     alert.showAndWait();
    // }

    @FXML 
    private void insertHPClick(ActionEvent event) {
        String INP_TENHP = tenhpDisplay.getText().trim();
        int INP_SOTC = 0;
        String sotcText = sotcDisplay.getText();
        if (sotcText != null && !sotcText.isEmpty()) {
            INP_SOTC = Integer.parseInt(sotcText.trim());
        }
        int INP_SOTIETLT = 0;
        String sotietltText = sotietltDisplay.getText();
        if (sotietltText != null && !sotietltText.isEmpty()) {
            INP_SOTIETLT = Integer.parseInt(sotietltText.trim());
        }
        int INP_SOTIETTH = 0;
        String sotietthText = sotietthDisplay.getText();
        if (sotietthText != null && !sotietthText.isEmpty()) {
            INP_SOTIETTH = Integer.parseInt(sotietthText.trim());
        }
        int INP_SOSVTOIDA = 0;
        String sosvtoidaText = sosvtoidaDisplay.getText();
        if (sosvtoidaText != null && !sosvtoidaText.isEmpty()) {
            INP_SOSVTOIDA = Integer.parseInt(sosvtoidaText.trim());
        }
        String INP_TENDV = ((String) tendvDisplayDrop.getValue()).trim();

        DataAccessLayer dal = null;
        Connection conn = null;
        CallableStatement cst = null;
        if (INP_TENHP == null ||  INP_TENDV == null) {
            System.out.println("Nhap thieu");
            // Show an error alert
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Vui lòng nhập đủ!");
            alert.showAndWait();
        } else {
            try {
                dal = DataAccessLayer.getInstance("your_username", "your_password");
                conn = dal.connect();
                cst = conn.prepareCall("{CALL C##QLK.SP_INSERT_HOCPHAN(?,?,?,?,?,?)}");
                cst.setString(1,INP_TENHP);
                cst.setInt(2, INP_SOTC);
                cst.setInt(3, INP_SOTIETLT);
                cst.setInt(4, INP_SOTIETTH);
                cst.setInt(5, INP_SOSVTOIDA);
                cst.setString(6, INP_TENDV);

                int rowsAffected = cst.executeUpdate();

                String role = null;
                PreparedStatement pst = null;
                ResultSet rs = null;
                pst = conn
                        .prepareStatement(
                                "SELECT * FROM SESSION_ROLES");

                rs = pst.executeQuery();

                if (rs.next()) {
                    role = rs.getString("ROLE");
                }

                if (role != null) {
                    System.out.println("Role: " + role);
                } else {
                    System.out.println("No role found for the current user.");
                }

                if (!"GVU".equals(role) || role == null) {
                    System.out.println("No privileges (no grant).");
                    // Show an error alert
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Lỗi: không có quyền!");
                    alert.showAndWait();

                } else if (rowsAffected > 0) {
                    System.out.println("Insert successfully.");
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Success");
                    alert.setHeaderText(null);
                    alert.setContentText("Thêm thành công!");
                    alert.showAndWait();

                } else {
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Lỗi!");
                    alert.showAndWait();

                }
            } catch (SQLException e) {
                System.out.println("Error: " + e.getMessage());
                // showAlert(Alert.AlertType.ERROR, "Error", "Failed to add user: " +
                // e.getMessage());
                System.out.println("Insert unsuccessfully.");
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Lỗi!");
                alert.showAndWait();
            }
        }

    }

    private void searchHocphan(String searchText) {
        DataAccessLayer dal = null;
        Connection conn = null;
        CallableStatement cst = null;
        ResultSet rs = null;
        List<Hocphan> hocphanList = new ArrayList<>();

        try {
            dal = DataAccessLayer.getInstance("your_username", "your_password");
            conn = dal.connect();

            // Construct the wildcard pattern for partial matching

            cst = conn.prepareCall("{CALL C##QLK.SP_SEARCH_HOCPHAN(?, ?)}");
            cst.registerOutParameter(1, OracleTypes.CURSOR);
            cst.setString(2, searchText);
            cst.execute();

            rs = (ResultSet) cst.getObject(1);

            while (rs.next()) {
                Hocphan hp = new Hocphan();
                Donvi dv = new Donvi();

                hp.setTENHP(rs.getString("TENHP"));
                hp.setSOTC(rs.getInt("SOTC"));
                hp.setSOTIETLT(rs.getInt("STLT"));
                hp.setSOTIETTH(rs.getInt("STTH"));
                hp.setSOSVTOIDA(rs.getInt("SOSVTD"));

                String tendv = rs.getString("TENDV");
                dv.setTENDV(tendv);
                hp.setDonvi(dv);

                hocphanList.add(hp);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        hocphanTableView.setItems(FXCollections.observableArrayList(hocphanList));
    }
    @FXML
    private void refreshTable(ActionEvent event) {
        hocphanList.clear();
        loadHocphanFromDatabase();
        hocphanTableView.refresh();
    }
}
