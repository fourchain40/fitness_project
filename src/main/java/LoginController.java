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
                String role_id = "member_id";
                if(role.equals("Trainer"))
                    role_id = "trainer_id";
                else if(role.equals("Administrator"))
                    role_id = "admin_id";
                String sql = "SELECT * FROM " + role + " WHERE email=? AND password=?";
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setString(1, user);
                    stmt.setString(2, pass);
                    ResultSet rs = stmt.executeQuery();

                    if (rs.next()) {
                        authenticated = true;
                        System.out.println("Login successful as " + role);

                        //Set up Session with appropriate user id
                        int id = rs.getInt(role_id);

                        Session session = Session.getInstance();
                        session.setUserID(id);
                        session.setRole(role);

                        // Load the workout screen
                        //FXMLLoader loader = new FXMLLoader(getClass().getResource("/logWorkout.fxml"));
                        if(role.equals("Member"))
                        {FXMLLoader loader = new FXMLLoader(getClass().getResource("/mainMenuMember.fxml"));
                        Parent root = loader.load();
                        Stage stage = (Stage) usernameField.getScene().getWindow();
                        stage.setScene(new Scene(root));
                        //stage.setTitle("Log Workout");
                        stage.setTitle("Main Menu");
                        stage.show();}
                        else if(role.equals("Trainer"))
                        {FXMLLoader loader = new FXMLLoader(getClass().getResource("/mainMenuTrainer.fxml"));
                            Parent root = loader.load();
                            Stage stage = (Stage) usernameField.getScene().getWindow();
                            stage.setScene(new Scene(root));
                            //stage.setTitle("Log Workout");
                            stage.setTitle("Main Menu");
                            stage.show();}
                        else if(role.equals("Administrator"))
                        {FXMLLoader loader = new FXMLLoader(getClass().getResource("/mainMenuAdmin.fxml"));
                            Parent root = loader.load();
                            Stage stage = (Stage) usernameField.getScene().getWindow();
                            stage.setScene(new Scene(root));
                            //stage.setTitle("Log Workout");
                            stage.setTitle("Main Menu");
                            stage.show();}



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