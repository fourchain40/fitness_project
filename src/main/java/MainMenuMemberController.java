import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;



public class MainMenuMemberController {
    @FXML private Label title;

    @FXML
    public void handleProfile() throws Exception
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/myProfileMember.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) title.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("My Profile");
        stage.show();
    }

    @FXML
    public void handleLog() throws Exception
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/logWorkout.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) title.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Log Workout");
        stage.show();
    }

    @FXML
    public void handleLogOut() throws Exception
    {
        Session session = Session.getInstance();
        session.setUserID(0);
        session.setRole("");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) title.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Gym Tracker Login");
        stage.show();
    }

    @FXML
    public void handleStats() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/workoutStats.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) title.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Workout Statistics");
        stage.show();
    }

}
