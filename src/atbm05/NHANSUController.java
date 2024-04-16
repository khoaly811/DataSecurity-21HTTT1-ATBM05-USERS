package atbm05;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import oracle.jdbc.OracleTypes;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import DataAccessLayer.DataAccessLayer;
import dto.Donvi;
import dto.Nhansu;

public class NHANSUController {
    @FXML
    private TableView<Nhansu> nhansuTableView;

    @FXML
    private TableColumn<Nhansu, String> HOTEN;

    @FXML
    private TableColumn<Nhansu, String> PHAI;

    @FXML
    private TableColumn<Nhansu, String> NGSINH;

    @FXML
    private TableColumn<Nhansu, String> PHUCAP;

    @FXML
    private TableColumn<Nhansu, String> DT;

    @FXML
    private TableColumn<Nhansu, String> VAITRO;

    @FXML
    private TableColumn<Nhansu, String> TENDV;

    @FXML
    private TableColumn<Nhansu, String> MANV;

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

    @FXML
    private TextField hotenDisplay;

    @FXML
    private TextField phaiDisplay;

    @FXML
    private TextField phucapDisplay;

    @FXML
    private TextField ngsinhDisplay;

    @FXML
    private TextField dienthoaiDisplay;

    // @FXML
    // private TextField tendvDisplay;

    @FXML
    private TextField manvDisplay;

    @FXML
    private Button updateNS;

    @FXML
    private Button profileButton;

    @FXML
    private TextField searchNS;

    @FXML
    private TextField vaitroDisplay;

    private ObservableList<Nhansu> nhansuList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        HOTEN.setCellValueFactory(cellData -> cellData.getValue().HOTENproperty());
        PHAI.setCellValueFactory(cellData -> cellData.getValue().PHAIproperty());
        NGSINH.setCellValueFactory(cellData -> cellData.getValue().NGSINHproperty().asString());
        PHUCAP.setCellValueFactory(cellData -> cellData.getValue().PHUCAPproperty().asString());
        DT.setCellValueFactory(cellData -> cellData.getValue().DTproperty());
        VAITRO.setCellValueFactory(cellData -> cellData.getValue().VAITROproperty());
        TENDV.setCellValueFactory(cellData -> cellData.getValue().getDonvi().TENDVproperty());
        MANV.setCellValueFactory(cellData -> cellData.getValue().MANVproperty());
        nhansuList = FXCollections.observableArrayList();
        loadNhansuFromDatabase();
        populateChoiceBox();

        // Add listener to the TableView selection model
        nhansuTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                hotenDisplay.setText(newSelection.getHOTEN());
                phaiDisplay.setText(newSelection.getPHAI());
                ngsinhDisplay.setText(newSelection.getNGSINH().toString());
                phucapDisplay.setText(String.valueOf(newSelection.getPHUCAP()));
                dienthoaiDisplay.setText(newSelection.getDT());
                // tendvDisplay.setText(newSelection.getDonvi().getTENDV());
                tendvDisplayDrop.getSelectionModel().select(newSelection.getDonvi().getTENDV());
                manvDisplay.setText(newSelection.getMANV());
                vaitroDisplay.setText(newSelection.getVAITRO());
            }
        });

        searchNS.textProperty().addListener((observable, oldValue, newValue) -> {
            searchNhanSu(newValue);
        });

    }

    private void loadNhansuFromDatabase() {
        DataAccessLayer dal = null;
        Connection conn = null;
        CallableStatement cst = null;
        ResultSet rs = null;

        try {
            dal = DataAccessLayer.getInstance("your_username", "your_password");
            conn = dal.connect();
            cst = conn.prepareCall("{CALL C##QLK.SP_VIEW_NHANSU(?)}");
            cst.registerOutParameter(1, OracleTypes.CURSOR);
            System.out.println("minh beo 1");
            cst.execute();
            System.out.println("minh beo 2");
            rs = (ResultSet) cst.getObject(1);
            System.out.println("minh beo 3");
            while (rs.next()) {
                Nhansu ns = new Nhansu();
                ns.setHOTEN((rs.getString("HOTEN")));
                ns.setPHAI((rs.getString("PHAI")));
                ns.setNGSINH((rs.getDate("NGSINH").toLocalDate()));
                ns.setPHUCAP((rs.getInt("PHUCAP")));
                ns.setDT((rs.getString("DT")));
                ns.setVAITRO((rs.getString("VAITRO")));
                String tendv = rs.getString("TENDV");
                ns.setMANV((rs.getString("MANV")));
                // System.out.println("TENDV là: " + tendv);
                Donvi dv = new Donvi();
                dv.setTENDV(tendv);
                ns.setDonvi(dv);
                nhansuList.add(ns);

            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());

        }

        // Set the loaded users to the table view
        nhansuTableView.setItems(nhansuList);
    }

    @FXML
    private void updateNSClick(ActionEvent event) {
        String INP_HOTEN = hotenDisplay.getText().trim();
        String INP_PHAI = phaiDisplay.getText().trim();
        LocalDate INP_NGSINH = LocalDate.parse(ngsinhDisplay.getText().trim()); // Assuming your date format is
                                                                                // parseable
        int INP_PHUCAP = Integer.parseInt(phucapDisplay.getText().trim());
        String INP_DT = dienthoaiDisplay.getText().trim();
        String INP_MANV = manvDisplay.getText().trim();
        String INP_TENDV = ((String) tendvDisplayDrop.getValue()).trim();
        System.out.println(INP_TENDV);

        DataAccessLayer dal = null;
        Connection conn = null;
        CallableStatement cst = null;

        try {
            dal = DataAccessLayer.getInstance("your_username", "your_password");
            conn = dal.connect();
            System.out.println("khoa beo 1");
            cst = conn.prepareCall("{CALL C##QLK.SP_ALL_UPDATE_NHANSU(?,?,?,?,?,?,?)}");
            cst.setString(1, INP_HOTEN);
            cst.setString(2, INP_PHAI);
            cst.setDate(3, java.sql.Date.valueOf(INP_NGSINH));
            cst.setInt(4, INP_PHUCAP);
            cst.setString(5, INP_DT);
            cst.setString(6, INP_MANV);
            cst.setString(7, INP_TENDV);
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

            if (!"TKHOA".equals(grantedRole) || !("GV".equals(grantedRole) || !"GVU".equals(grantedRole) 
            || !"SV".equals(grantedRole) || !"TDV".equals(grantedRole) 
            || !"NVCB".equals(grantedRole)) || grantedRole == null) {
                System.out.println("No privileges (no grant).");
                // Show an error alert
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Lỗi: không có quyền!");
                alert.showAndWait();
          
            }else
            if (rowsAffected > 0) {
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

    }
    }
    @FXML
    private void deleteNSClick(ActionEvent event) {
        System.out.println("Delete");
        Nhansu selectedNhanSu = nhansuTableView.getSelectionModel().getSelectedItem();

        if (selectedNhanSu != null) {
            String MANV = selectedNhanSu.getMANV();

            DataAccessLayer dal = null;
            Connection conn = null;
            CallableStatement cst = null;

            try {
                dal = DataAccessLayer.getInstance("", "");
                conn = dal.connect();
                cst = conn.prepareCall("{CALL C##QLK.SP_DELETE_NHANSU(?)}");
                cst.setString(1, MANV);

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
                    System.out.println("Role: " + grantedRole);
                } else {
                    System.out.println("No role found for the current user.");
                }

                if (!"TKHOA".equals(grantedRole)|| grantedRole == null) {
                    System.out.println("No privileges (no grant).");
                    // Show an error alert
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Lỗi");
                    alert.setHeaderText(null);
                    alert.setContentText("Lỗi: không có quyền!");
                    alert.showAndWait();
              
                }else
                if (rowsAffected > 0) {
                    System.out.println("Insert successfully.");
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Success");
                    alert.setHeaderText(null);
                    alert.setContentText("Xóa thành công!");
                    alert.showAndWait();

                } else{
                    System.out.println("Delete unsuccessfully.");
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Lỗi!");
                    alert.showAndWait();

                }
            } catch (SQLException e) {
                System.out.println("Failed to delete: " + e.getMessage());
                //showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete user: " + e.getMessage());
                if (e.getMessage().contains("ORA-01031")) {
                    System.out.println("No privileges (no grant).");
                    // Show an error alert
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Lỗi!");
                    alert.showAndWait();
                }
            } 
        } else {
            System.out.println("No row selected.");
        }

    }

    @FXML
    private void insertNSClick(ActionEvent event) {
        String INP_HOTEN = hotenDisplay.getText().trim();
        String INP_PHAI = phaiDisplay.getText().trim();
        LocalDate INP_NGSINH = null;
        String ngsinhText = ngsinhDisplay.getText(); // Get the text from ngsinhDisplay
        if (ngsinhText != null && !ngsinhText.isEmpty()) { // Assuming your date format is
            INP_NGSINH = LocalDate.parse(ngsinhText.trim());
        } // parseable

        int INP_PHUCAP = 0; // Default value is 0
        String phucapText = phucapDisplay.getText(); // Get the text from phucapDisplay
        if (phucapText != null && !phucapText.isEmpty()) { // Check if the text is not null and not empty

            INP_PHUCAP = Integer.parseInt(phucapText.trim());
        }

        String INP_DT = dienthoaiDisplay.getText().trim();
        String INP_VAITRO = vaitroDisplay.getText().trim();
        String INP_TENDV = ((String) tendvDisplayDrop.getValue()).trim();
        DataAccessLayer dal = null;
        Connection conn = null;
        CallableStatement cst = null;
        if (INP_HOTEN == null || INP_PHAI == null || INP_NGSINH == null || INP_DT == null || INP_VAITRO == null
                || INP_TENDV == null) {
            System.out.println("Nhap thieu");
            // Show an error alert
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Vui lòng nhập đủ!");
            alert.showAndWait();
        } else {
            try {
                System.out.println(INP_TENDV);
                dal = DataAccessLayer.getInstance("your_username", "your_password");
                conn = dal.connect();
                cst = conn.prepareCall("{CALL C##QLK.SP_INSERT_NHANSU(?,?,?,?,?,?,?)}");
                cst.setString(1, INP_HOTEN);
                cst.setString(2, INP_PHAI);
                cst.setDate(3, java.sql.Date.valueOf(INP_NGSINH));
                cst.setInt(4, INP_PHUCAP);
                cst.setString(5, INP_DT);
                cst.setString(6, INP_VAITRO);
                cst.setString(7, INP_TENDV);
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

                if (!"TKHOA".equals(role) || role == null) {
                    System.out.println("No privileges (no grant).");
                    // Show an error alert
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Lỗi: không có quyền!");
                    alert.showAndWait();
              
                }else
                if (rowsAffected > 0) {
                    System.out.println("Insert successfully.");
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Success");
                    alert.setHeaderText(null);
                    alert.setContentText("Thêm thành công!");
                    alert.showAndWait();

                } else{
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Lỗi!");
                    alert.showAndWait();

                }
            } catch (SQLException e) {
                System.out.println("Error: " + e.getMessage());
                //showAlert(Alert.AlertType.ERROR, "Error", "Failed to add user: " + e.getMessage());
                System.out.println("Insert unsuccessfully.");
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Lỗi!");
                    alert.showAndWait();
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

    private void searchNhanSu(String searchText) {
        DataAccessLayer dal = null;
        Connection conn = null;
        CallableStatement cst = null;
        ResultSet rs = null;
        List<Nhansu> nhansuList = new ArrayList<>();

        try {
            dal = DataAccessLayer.getInstance("your_username", "your_password");
            conn = dal.connect();

            // Construct the wildcard pattern for partial matching

            cst = conn.prepareCall("{CALL C##QLK.SP_SEARCH_NHANSU(?, ?)}");
            cst.registerOutParameter(1, OracleTypes.CURSOR);
            cst.setString(2, searchText);
            cst.execute();

            rs = (ResultSet) cst.getObject(1);

            while (rs.next()) {
                Nhansu ns = new Nhansu();
                ns.setHOTEN(rs.getString("HOTEN"));
                ns.setPHAI(rs.getString("PHAI"));
                ns.setNGSINH(rs.getDate("NGSINH").toLocalDate());
                ns.setPHUCAP(rs.getInt("PHUCAP"));
                ns.setDT(rs.getString("DT"));
                ns.setVAITRO(rs.getString("VAITRO"));
                String tendv = rs.getString("TENDV");
                ns.setMANV(rs.getString("MANV"));

                Donvi dv = new Donvi();
                dv.setTENDV(tendv);
                ns.setDonvi(dv);

                nhansuList.add(ns);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        // Set the loaded users to the table view
        nhansuTableView.setItems(FXCollections.observableArrayList(nhansuList));
    }

    @FXML
    private ChoiceBox tendvDisplayDrop;

    private List<String> loadDistinctTENDVFromDatabase() {
        List<String> tendvList = new ArrayList<>();

        DataAccessLayer dal = null;
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            dal = DataAccessLayer.getInstance("your_username", "your_password");
            conn = dal.connect();

            // SQL query to retrieve distinct TENDV values from DONVI table
            String sql = "SELECT DISTINCT TENDV FROM C##QLK.DONVI";

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
    private void refreshTable(ActionEvent event) {
        nhansuList.clear();
        loadNhansuFromDatabase();
        nhansuTableView.refresh();
    }
}