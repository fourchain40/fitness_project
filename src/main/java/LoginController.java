import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class LoginController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label statusLabel;

    @FXML
    private void handleLogin() {
        String user = usernameField.getText();
        String pass = passwordField.getText();

        String[] roles = { "Member", "Trainer", "Administrator" };
        boolean authenticated = false;

        try (Connection conn = DriverManager.getConnection(
                "jdbc:postgresql://bastion.cs.virginia.edu:5432/group29", "group29", "C1mbI9G3")) {

            for (String role : roles) {
                String sql = "SELECT * FROM " + role + " WHERE email=? AND password=?";
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setString(1, user);
                    stmt.setString(2, pass);
                    ResultSet rs = stmt.executeQuery();

                    if (rs.next()) {
                        authenticated = true;
                        System.out.println("Login successful as " + role);

                        // Load the workout screen
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/logWorkout.fxml"));
                        Parent root = loader.load();
                        Stage stage = (Stage) usernameField.getScene().getWindow();
                        stage.setScene(new Scene(root));
                        stage.setTitle("Log Workout");
                        stage.show();

                        break;
                    }
                }
            }

            if (!authenticated) {
                statusLabel.setText("Invalid credentials.");
            }

        } catch (SQLException e) {
            statusLabel.setText("Error connecting to DB.");
            e.printStackTrace();
        } catch (IOException e) {
            statusLabel.setText("Failed to load next screen.");
            e.printStackTrace();
        }
    }
}