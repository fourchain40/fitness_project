import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.Optional;

public class LoginController {
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label statusLabel;

    @FXML
    private void handleLogin() {
        String user = usernameField.getText();
        String pass = passwordField.getText();

        Session session = Session.getInstance();
        DatabaseDriver databaseDriver = session.getDatabaseDriver();
        try {
            databaseDriver.connect();
            Optional userObj;
            if ((userObj = databaseDriver.memberAuthenticated(user, pass)).isPresent()) {
                databaseDriver.disconnect();
                session.setUserID(((Member) userObj.get()).getMember_id());
                session.setRole("Member");
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/mainMenuMember.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) usernameField.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setTitle("Main Menu");
                stage.show();
            } else if ((userObj = databaseDriver.trainerAuthenticated(user, pass)).isPresent()) {
                databaseDriver.disconnect();
                session.setUserID(((Trainer) userObj.get()).getTrainer_id());
                session.setRole("Trainer");
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/mainMenuTrainer.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) usernameField.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setTitle("Main Menu");
                stage.show();
            } else if ((userObj = databaseDriver.adminAuthenticated(user, pass)).isPresent()) {
                databaseDriver.disconnect();
                session.setUserID(((Administrator) userObj.get()).getAdmin_id());
                session.setRole("Administrator");
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/mainMenuAdmin.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) usernameField.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setTitle("Main Menu");
                stage.show();
            } else {
                statusLabel.setText("Invalid credentials.");
            }
            databaseDriver.disconnect();
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void handleCreateAccount() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/createProfile.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) statusLabel.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Create Member Profile");
        stage.show();
    }

    @FXML
    private void handleCreateTrainerAccount() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/createTrainer.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) statusLabel.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Create Trainer Profile");
        stage.show();
    }
}
