import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.sql.*;
import java.time.LocalDate;

public class WorkoutController {
    @FXML private TextArea workoutDetails;
    @FXML private Label confirmationLabel;

    private int userId = 1; // Replace with actual logged-in user ID

    @FXML
    private void handleLogWorkout() {
        String details = workoutDetails.getText();
        try (Connection conn = DriverManager.getConnection("jdbc:postgresql://bastion.cs.virginia.edu:5432/group29", "group29", "C1mbI9G3")) {
            String sql = "INSERT INTO workouts (user_id, workout_date, workout_details) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            stmt.setDate(2, Date.valueOf(LocalDate.now()));
            stmt.setString(3, details);
            stmt.executeUpdate();

            confirmationLabel.setText("Workout logged!");
        } catch (SQLException e) {
            confirmationLabel.setText("Error logging workout.");
            e.printStackTrace();
        }
    }
}