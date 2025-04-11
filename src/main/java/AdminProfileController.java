import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.sql.*;

public class AdminProfileController {
    @FXML private Label title;
    @FXML private Label name;

    @FXML
    public void initialize()
    {

        try (Connection conn = DriverManager.getConnection(
                "jdbc:postgresql://bastion.cs.virginia.edu:5432/group29", "group29", "C1mbI9G3")) {
            Session session = Session.getInstance();
            String role = session.getRole();
            String role_id = "member_id";
            int id = session.getUserID();
            if(role.equals("Trainer"))
            {
                role_id = "trainer_id";
            }
            else if(role.equals("Administrator"))
            {
                role_id = "admin_id";
            }
            String sql = "SELECT * FROM " + role + " WHERE " + role_id + "=?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, id);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    String first_name = rs.getString("first_name");
                    String last_name = rs.getString("last_name");
                    name.setText("Name: " + first_name + " " + last_name);
                }
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

    }
    @FXML
    public void handleBack() throws Exception
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/mainMenuAdmin.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) title.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Main Menu");
        stage.show();
    }
}
