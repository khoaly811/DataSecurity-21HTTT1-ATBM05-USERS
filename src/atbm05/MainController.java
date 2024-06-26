package atbm05;

import java.sql.Connection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import DataAccessLayer.DataAccessLayer;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;

public class MainController {
    @FXML
    private void onClickAdd() {
    }

    @FXML
    private Tab fgaTab;

    @FXML
    private Tab fgaTab1;

    @FXML
    private Tab nsTab;

    @FXML
    private Tab pcTab;

    @FXML
    private Tab kqTab;

    @FXML
    private Tab dvTab;


    @FXML
    private void initialize() {
            
        // Get the current user
        Boolean currentUser = getCurrentUser();
        Boolean currentUserSV = getCurrentUserSV(); // Implement this method to get the current user
        System.out.println(currentUser);
        // Hide the FGA tab if the user is not "C##QLK"
        if (!currentUser) {
            fgaTab.setDisable(true);
            fgaTab.setContent(null);
            // fgaTab.setInvisible(true);
            fgaTab.getTabPane().getTabs().remove(fgaTab);
            fgaTab1.getTabPane().getTabs().remove(fgaTab1);
        }

        if (currentUserSV) {
           
            nsTab.getTabPane().getTabs().remove(nsTab);
            pcTab.getTabPane().getTabs().remove(pcTab);
            kqTab.getTabPane().getTabs().remove(kqTab);
            dvTab.getTabPane().getTabs().remove(dvTab);
        }
    }

    // Implement this method to get the current user
    private Boolean getCurrentUser() {
        String grantedRole = null;
        DataAccessLayer dal = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        Connection conn = null;
        try {

            dal = DataAccessLayer.getInstance("your_username", "your_password");
            conn = dal.connect();

            pst = conn
                    .prepareStatement(
                            "select user from dual");

            rs = pst.executeQuery();

            if (rs.next()) {
                grantedRole = rs.getString("USER");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());

        }
        if ("C##QLK".equals(grantedRole)) {
            return true;
        } else {
            return false;
        }
    }

    private Boolean getCurrentUserSV() {
        String grantedRole = null;
        DataAccessLayer dal = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        Connection conn = null;
        try {

            dal = DataAccessLayer.getInstance("your_username", "your_password");
            conn = dal.connect();

            pst = conn
                    .prepareStatement(
                            "select ROLE from SESSION_ROLES WHERE ROWNUM = 1");

            rs = pst.executeQuery();

            if (rs.next()) {
                grantedRole = rs.getString("ROLE");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());

        }
        if ("SV".equals(grantedRole)) {
            return true;
        } else {
            return false;
        }
    }
}