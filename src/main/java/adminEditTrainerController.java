import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.*;

public class adminEditTrainerController {
    @FXML private Label title;
    @FXML private TextField fname_field;
    @FXML private TextField lname_field;
    @FXML private DatePicker dob_picker;
    @FXML private TextField bio_field;
    @FXML private TextField spec_field;
    @FXML private TextField exp_field;
    @FXML private TextField email_field;
    @FXML private TextField password_field;
    @FXML private RadioButton female;
    @FXML private RadioButton male;
    @FXML private RadioButton other;

    private Trainer trainer;

    public void setTrainer(Trainer trainer) {
        this.trainer = trainer;
        updateProfile();
    }

    @FXML
    public void initialize() {
        updateProfile();
    }

    private void updateProfile() {
        if (trainer != null) {
            lname_field.setText(trainer.getLast_name());
            fname_field.setText(trainer.getFirst_name());
            exp_field.setText(String.valueOf(trainer.getYears_of_experience()));
            bio_field.setText(trainer.getBio());
            spec_field.setText(trainer.getSpecialization());
            email_field.setText(trainer.getEmail());
            password_field.setText(trainer.getPassword());
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

        Trainer newTrainer = new Trainer(
                trainer.getTrainer_id(),
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
            databaseDriver.adminUpdateTrainer(newTrainer);
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
            databaseDriver.deleteTrainerByID(trainer.getTrainer_id());
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
