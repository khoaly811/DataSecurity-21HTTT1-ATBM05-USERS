package atbm05;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.event.ActionEvent;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;

import java.io.IOException;
import java.sql.SQLException;
import DataAccessLayer.DataAccessLayer;

public class LoginController {
    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;


    @FXML
    private void loginButtonAction(ActionEvent event) throws SQLException, IOException {
        String username = usernameField.getText();
        String password = passwordField.getText();

        Alert.AlertType popUp = Alert.AlertType.INFORMATION;
        String title = "Login Successfully";
        String msg = "Welcome " + username + "!";
        boolean flag = false;
        
        try {
            DataAccessLayer.getInstance(username, password).connect();
            flag = true;
        } catch (SQLException e) {
            popUp = Alert.AlertType.ERROR;
            title = "Login failed";
            msg = "Invalid username or password!";
        } finally {
            showAlert(popUp, title, msg);
        } 
        
        if (flag == true) {
            ((Stage)(((Button)event.getSource()).getScene().getWindow())).close();
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("Main.fxml"));
            Stage stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            Scene scene1 = new Scene(fxmlLoader.load());
            stage.setScene(scene1);
            stage.centerOnScreen();
            stage.show();
        }
    } 

    private void showAlert(Alert.AlertType alertType, String title, String message) throws SQLException {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
};