import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class MemberProfileViewController {
    @FXML private Label title;
    @FXML private Label name;
    @FXML private Label gender;
    @FXML private Label date_of_birth;
    @FXML private Label height;
    @FXML private Label weight;
    @FXML private Label bio;
    @FXML private Label email;
    @FXML private Label total_workouts;
    @FXML private Label total_minutes;
    @FXML private Label avg_duration;

    private Member member;
    private MemberStats stats;

    @FXML
    public void initialize()
    {
       updateProfile();
    }

    public void setMember(Member member) {
        this.member = member;
        updateProfile();
    }

    public void setStats(MemberStats stats) {
        this.stats = stats;
        updateProfile();
    }

    private void updateProfile() {
        if (member != null) {
            name.setText("Name: " + member.getFirst_name() + " " + member.getLast_name());
            gender.setText("Gender: " + member.getGender());
            date_of_birth.setText("Date of Birth: " + member.getDate_of_birth().toString());
            height.setText("Height: " + member.getHeight() + " cm");
            weight.setText("Weight: " + member.getWeight() + " kg");
            bio.setText("Bio: " + member.getBio());
            email.setText("Email: " + member.getEmail());
        }
        if (stats != null) {
            total_workouts.setText("Total Workouts: " + stats.getTotal_workouts());
            total_minutes.setText("Total Workout Time: " + stats.getTotal_minutes() + " minutes");
            avg_duration.setText("Average Workout Duration: " + stats.getAvg_duration() + " minutes");
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