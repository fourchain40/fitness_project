import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.w3c.dom.Text;

import java.sql.SQLException;

public class CreateTrainerController {
    @FXML private Label title;
    @FXML private Label errorLabel;
    @FXML private TextField fname_field;
    @FXML private TextField lname_field;
    @FXML private DatePicker dob_picker;
    @FXML private TextField bio_field;
    @FXML private RadioButton female;
    @FXML private RadioButton male;
    @FXML private RadioButton other;
    @FXML private TextField email_field;
    @FXML private TextField password_field;
    @FXML private TextField spec_field;
    @FXML private TextField exp_field;

    @FXML
    private void handleCreate() throws Exception {
        Session session = Session.getInstance();
        DatabaseDriver databaseDriver = session.getDatabaseDriver();

        Boolean isEmailAvailable;
        try {
            databaseDriver.connect();
            isEmailAvailable = databaseDriver.isTrainerEmailAvailable(email_field.getText());
            databaseDriver.disconnect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if (!isEmailAvailable) {
            errorLabel.setText("Email address already in use!");
            return;
        }

        if (password_field.getText().isEmpty() || email_field.getText().isEmpty() || fname_field.getText().isEmpty()
                || lname_field.getText().isEmpty() || dob_picker.getValue() == null || bio_field.getText().isEmpty()
                || spec_field.getText().isEmpty() || exp_field.getText().isEmpty()) {
            errorLabel.setText("Please fill out all fields!");
            return;
        }

        String gender_char = "O";
        if (female.isSelected())
            gender_char = "F";
        else if (male.isSelected())
            gender_char = "M";

        Trainer trainer = new Trainer(
                fname_field.getText(),
                lname_field.getText(),
                dob_picker.getValue(),
                gender_char,
                email_field.getText(),
                password_field.getText(),
                Integer.parseInt(exp_field.getText()),
                bio_field.getText(),
                spec_field.getText()

        );

        try {
            databaseDriver.connect();
            databaseDriver.addTrainer(trainer);
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