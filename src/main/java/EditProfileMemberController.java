import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import javax.xml.crypto.Data;
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
    @FXML private CheckBox public_visibility;

    @FXML
    public void initialize() {
        Session session = Session.getInstance();
        DatabaseDriver databaseDriver = session.getDatabaseDriver();

        Member member;

        try {
            databaseDriver.connect();
            member = databaseDriver.getMemberByID(session.getUserID());
            databaseDriver.disconnect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        lname_field.setText(member.getLast_name());
        fname_field.setText(member.getFirst_name());
        height_field.setText(String.valueOf(member.getHeight()));
        weight_field.setText(String.valueOf(member.getWeight()));
        bio_field.setText(member.getBio());
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

    @FXML
    public void handleEdit() throws Exception {
        Session session = Session.getInstance();
        DatabaseDriver databaseDriver = session.getDatabaseDriver();

        String gender_char = "O";
        if (female.isSelected())
            gender_char = "F";
        else if (male.isSelected())
            gender_char = "M";

        Member member = new Member(
                session.getUserID(),
                fname_field.getText(),
                lname_field.getText(),
                null,
                null,
                gender_char,
                dob_picker.getValue(),
                Integer.parseInt(height_field.getText()),
                Integer.parseInt(weight_field.getText()),
                bio_field.getText(),
                false
        );

        if (public_visibility.isSelected()) {
            member.setPublic_visibility(true);
        }

        try {
            databaseDriver.connect();
            databaseDriver.updateMember(member);
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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/mainMenuMember.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) title.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Main Menu");
        stage.show();
    }
}
