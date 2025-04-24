import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class TrainerProfileViewController {
    @FXML private Label title;
    @FXML private Label name;
    @FXML private Label gender;
    @FXML private Label date_of_birth;
    @FXML private Label specialization;
    @FXML private Label bio;
    @FXML private Label email;

    private Trainer trainer;

    @FXML
    public void initialize()
    {
        updateProfile();
    }

    public void setTrainer(Trainer trainer) {
        this.trainer = trainer;
        updateProfile();
    }

    private void updateProfile() {
        if (trainer != null) {
            name.setText("Name: " + trainer.getFirst_name() + " " + trainer.getLast_name());
            gender.setText("Gender: " + trainer.getGender());
            date_of_birth.setText("Date of Birth: " + trainer.getDate_of_birth().toString());
            specialization.setText("Specialization: " + trainer.getSpecialization());
            bio.setText("Bio: " + trainer.getBio());
            email.setText("Email: " + trainer.getEmail());
        }
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