import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.Optional;


public class MainMenuMemberController {
    @FXML private Label title;
    @FXML private Label name;
    @FXML private Label gender;
    @FXML private Label date_of_birth;
    @FXML private Label height;
    @FXML private Label weight;
    @FXML private Label bio;

    @FXML private Label total_workouts;
    @FXML private Label total_minutes;
    @FXML private Label avg_duration;
    @FXML private Label stats_err;

    @FXML
    public void initialize()
    {
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

        name.setText("Name: " + member.getFirst_name() + " " + member.getLast_name());
        gender.setText("Gender: " + member.getGender());
        date_of_birth.setText("Date of Birth: " + member.getDate_of_birth().toString());
        height.setText("Height: " + member.getHeight() + " cm");
        weight.setText("Weight: " + member.getWeight() + " kg");
        bio.setText("Bio: " + member.getBio());
    }

    @FXML
    public void handleEdit() throws Exception
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/editProfileMember.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) title.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Main Menu");
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
    public void handleChallenge() throws Exception
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/challengeMenu.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) title.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Challenge Menu");
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
        Session session = Session.getInstance();
        DatabaseDriver databaseDriver = session.getDatabaseDriver();

        Optional<MemberStats> stats = Optional.empty();

        try {
            databaseDriver.connect();
            stats = databaseDriver.getMemberStatsByID(session.getUserID());
            databaseDriver.disconnect();
        } catch (SQLException e) {
            stats_err.setText("Error loading stats!");
            e.printStackTrace();
        }

        if (stats.isPresent()) {
            total_workouts.setText(String.format("Total Workouts: %d", stats.get().getTotal_workouts()));
            total_minutes.setText(String.format("Total Workout Time: %d minutes", stats.get().getTotal_minutes()));
            avg_duration.setText(String.format("Average Duration: %.2f minutes", stats.get().getAvg_duration()));
        } else {
            stats_err.setText("No workout data found.");
        }
    }

    @FXML
    public void handleHistory() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/workoutHistory.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) title.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Workout History");
        stage.show();
    }
}
