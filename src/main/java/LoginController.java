import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.sql.*;

public class LoginController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label statusLabel;

    @FXML
    private void handleLogin() {
        String user = usernameField.getText();
        String pass = passwordField.getText();

        try (Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/group29", "group29", "C1mbI9G3")) {
            String sql = "SELECT * FROM users WHERE username=? AND password=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, user);
            stmt.setString(2, pass);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                statusLabel.setText("Login successful!");
                // Transition to dashboard or another scene
            } else {
                statusLabel.setText("Invalid login.");
            }

        } catch (SQLException e) {
            statusLabel.setText("Error connecting to DB.");
            e.printStackTrace();
        }
    }
}