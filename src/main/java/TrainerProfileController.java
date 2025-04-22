import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.sql.*;

public class TrainerProfileController {
    @FXML private Label title;
    @FXML private Label name;
    @FXML private Label gender;
    @FXML private Label date_of_birth;
    @FXML private Label years_of_experience;
    @FXML private Label specialization;
    @FXML private Label bio;

    @FXML
    public void initialize()
    {
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

        name.setText("Name: " + trainer.getFirst_name() + " " + trainer.getLast_name());
        gender.setText("Gender: " + trainer.getGender());
        date_of_birth.setText("Date of Birth: " + trainer.getDate_of_birth().toString());
        years_of_experience.setText("Experience: " + trainer.getYears_of_experience() + "years");
        specialization.setText("Specialization: " + trainer.getSpecialization());
        bio.setText("Bio: " + trainer.getBio());
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
