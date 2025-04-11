import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.sql.*;

public class MyProfileController {
    @FXML private Label title;
    @FXML private Label name;
    @FXML private Label gender;
    @FXML private Label date_of_birth;
    @FXML private Label height;
    @FXML private Label weight;
    @FXML private Label bio;

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
                        String gender_c = rs.getString("gender");
                        gender.setText("Gender: " + gender_c);
                        Date date = rs.getDate("date_of_birth");
                        String datec = date.toString();
                        date_of_birth.setText("Date of Birth: " + datec);
                        int heightc = rs.getInt("height");
                        int weightc = rs.getInt("weight");
                        height.setText("Height: " + heightc + " cm");
                        weight.setText("Weight: " + weightc + " kg");
                        String bioc = rs.getString("bio");
                        bio.setText("Bio: " + bioc);
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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/mainMenu.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) title.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Main Menu");
        stage.show();
    }
}
