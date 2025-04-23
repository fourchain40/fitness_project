import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.SQLException;

public class CreateProfileController {
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
    @FXML private TextField email_field;
    @FXML private TextField password_field;

    @FXML
    private void handleCreate() throws Exception {
        Session session = Session.getInstance();
        DatabaseDriver databaseDriver = session.getDatabaseDriver();

        Boolean isEmailAvailable;
        try {
            databaseDriver.connect();
            isEmailAvailable = databaseDriver.isEmailAvailable(email_field.getText());
            databaseDriver.disconnect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if (!isEmailAvailable) {
            title.setText("Invalid email address: This email address is already in use.");
            return;
        }

        String gender_char = "O";
        if (female.isSelected())
            gender_char = "F";
        else if (male.isSelected())
            gender_char = "M";

        Member member = new Member(
                session.getUserID(),
                fname_field.getText(),
                lname_field.getText(),
                email_field.getText(),
                password_field.getText(),
                gender_char,
                dob_picker.getValue(),
                Integer.parseInt(height_field.getText()),
                Integer.parseInt(weight_field.getText()),
                bio_field.getText()
        );

        try {
            databaseDriver.connect();
            databaseDriver.addMember(member);
            databaseDriver.commit();
            databaseDriver.disconnect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        handleBack();
    };

    @FXML
    public void handleBack() throws Exception
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) title.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Gym Tracker Login");
        stage.show();
    }
}
