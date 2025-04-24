import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.*;

public class adminEditMemberController {
    @FXML private Label title;
    @FXML private TextField fname_field;
    @FXML private TextField lname_field;
    @FXML private DatePicker dob_picker;
    @FXML private TextField bio_field;
    @FXML private TextField height_field;
    @FXML private TextField weight_field;
    @FXML private TextField email_field;
    @FXML private TextField password_field;
    @FXML private RadioButton female;
    @FXML private RadioButton male;
    @FXML private RadioButton other;
    @FXML private CheckBox public_visibility;

    private Member member;

    public void setMember(Member member) {
        this.member = member;
        updateProfile();
    }

    @FXML
    public void initialize() {
       updateProfile();
    }

    private void updateProfile() {
        if (member != null) {
            lname_field.setText(member.getLast_name());
            fname_field.setText(member.getFirst_name());
            height_field.setText(String.valueOf(member.getHeight()));
            weight_field.setText(String.valueOf(member.getWeight()));
            bio_field.setText(member.getBio());
            email_field.setText(member.getEmail());
            password_field.setText(member.getPassword());
            ToggleGroup gender = new ToggleGroup();
            female.setToggleGroup(gender);
            male.setToggleGroup(gender);
            other.setToggleGroup(gender);
            if (member.getGender().equals("M")) {
                male.setSelected(true);
            } else if (member.getGender().equals("F")) {
                female.setSelected(true);
            } else {
                other.setSelected(true);
            }
            dob_picker.setValue(member.getDate_of_birth());
            if (member.isPublic_visibility()) {
                public_visibility.setSelected(true);
            }
        }
    }

    @FXML
    public void handleEdit() throws Exception {
        Session session = Session.getInstance();
        DatabaseDriver databaseDriver = session.getDatabaseDriver();

        String gender_char = "O";
        if (female.isSelected())
            gender_char = "F";
        else if (male.isSelected())
            gender_char = "M";

        Member newMember = new Member(
                member.getMember_id(),
                fname_field.getText(),
                lname_field.getText(),
                email_field.getText(),
                password_field.getText(),
                gender_char,
                dob_picker.getValue(),
                Integer.parseInt(height_field.getText()),
                Integer.parseInt(weight_field.getText()),
                bio_field.getText(),
                false
        );

        if (public_visibility.isSelected()) {
            newMember.setPublic_visibility(true);
        }

        try {
            databaseDriver.connect();
            databaseDriver.adminUpdateMember(newMember);
            databaseDriver.commit();
            databaseDriver.disconnect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        handleBack();
    }

    @FXML
    public void handleDelete() throws Exception {
        Session session = Session.getInstance();
        DatabaseDriver databaseDriver = session.getDatabaseDriver();

        try {
            databaseDriver.connect();
            databaseDriver.deleteMemberByID(member.getMember_id());
            databaseDriver.commit();
            databaseDriver.disconnect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        handleBack();
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
