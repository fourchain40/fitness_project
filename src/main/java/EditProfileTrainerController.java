import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.SQLException;

public class EditProfileTrainerController {
    @FXML
    private Label title;
    @FXML private TextField fname_field;
    @FXML private TextField lname_field;
    @FXML private DatePicker dob_picker;
    @FXML private TextField bio_field;
    @FXML private TextField years_of_experience_field;
    @FXML private TextField specialty_field;
    @FXML private RadioButton female;
    @FXML private RadioButton male;
    @FXML private RadioButton other;

    public void initialize() {
        Session session = Session.getInstance();
        DatabaseDriver databaseDriver = session.getDatabaseDriver();

        Trainer trainer;

        try {
            databaseDriver.connect();
            trainer = databaseDriver.getTrainerByID(session.getUserID());
            databaseDriver.disconnect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        lname_field.setText(trainer.getLast_name());
        fname_field.setText(trainer.getFirst_name());
        years_of_experience_field.setText(String.valueOf(trainer.getYears_of_experience()));
        specialty_field.setText(String.valueOf(trainer.getSpecialization()));
        bio_field.setText(trainer.getBio());
        ToggleGroup gender = new ToggleGroup();
        female.setToggleGroup(gender);
        male.setToggleGroup(gender);
        other.setToggleGroup(gender);
        if (trainer.getGender().equals("M")) {
            male.setSelected(true);
        } else if (trainer.getGender().equals("F")) {
            female.setSelected(true);
        } else {
            other.setSelected(true);
        }
        dob_picker.setValue(trainer.getDate_of_birth());
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

        Trainer trainer = new Trainer(
                session.getUserID(),
                fname_field.getText(),
                lname_field.getText(),
                dob_picker.getValue(),
                gender_char,
                null,
                null,
                Integer.parseInt(years_of_experience_field.getText()),
                bio_field.getText(),
                specialty_field.getText()
        );

        try {
            databaseDriver.connect();
            databaseDriver.updateTrainer(trainer);
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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/mainMenuTrainer.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) title.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Main Menu");
        stage.show();
    }
}
