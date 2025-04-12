import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.*;

public class EditProfileMemberController {
    @FXML private Label title;
    @FXML private TextField fname_field;
    @FXML private TextField lname_field;
    @FXML private DatePicker dob_picker;
    @FXML private TextField bio_field;
    @FXML private TextField height_field;
    @FXML private TextField weight_field;
    @FXML private RadioButton female;
    @FXML private RadioButton male;
    @FXML private RadioButton other;
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
                    lname_field.setText(rs.getString("last_name"));
                    fname_field.setText(rs.getString("first_name"));
                    height_field.setText(rs.getString("height"));
                    weight_field.setText(rs.getString("weight"));
                    bio_field.setText(rs.getString("bio"));
                    ToggleGroup gender = new ToggleGroup();
                    female.setToggleGroup(gender);
                    male.setToggleGroup(gender);
                    other.setToggleGroup(gender);
                    if(rs.getString("gender").equals("M"))
                    {
                        male.setSelected(true);
                    }
                    else if(rs.getString("gender").equals("F"))
                    {
                        female.setSelected(true);
                    }
                    else{
                        other.setSelected(true);
                    }
                    dob_picker.setValue(rs.getTimestamp("date_of_birth").toLocalDateTime().toLocalDate());
                }
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @FXML
    public void handleEdit() throws Exception
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
            String sql = "UPDATE " + role + " SET first_name=?, last_name=?, gender=?, date_of_birth=?, height=?, weight=?, bio=?" + " WHERE " + role_id + "=?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(8, id);
                stmt.setString(1, fname_field.getText());
                stmt.setString(2, lname_field.getText());
                stmt.setDate(4, Date.valueOf(dob_picker.getValue()));
                stmt.setInt(5, Integer.parseInt(height_field.getText()));
                stmt.setInt(6, Integer.parseInt(weight_field.getText()));
                stmt.setString(7, bio_field.getText());

                String gender_char = "O";
                if (female.isSelected())
                    gender_char = "F";
                else if (male.isSelected())
                    gender_char = "M";
                stmt.setString(3, gender_char);
                stmt.executeUpdate();
                handleBack();
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @FXML
    public void handleBack() throws Exception
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/myProfileMember.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) title.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("My Profile");
        stage.show();
    }
}
