import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.*;
import java.time.LocalDate;

public class WorkoutController {
    @FXML private TextArea workoutDetails;
    @FXML private TextField durationField;
    @FXML private DatePicker workoutDate;
    @FXML private Label confirmationLabel;

    // This should eventually be set when the user logs in
    private int memberId = 1;

    @FXML
    private void handleLogWorkout() {
        String notes = workoutDetails.getText();
        String durationText = durationField.getText();
        LocalDate date = workoutDate.getValue();

        if (notes.isEmpty() || durationText.isEmpty() || date == null) {
            confirmationLabel.setText("Please fill out all fields.");
            return;
        }

        try {
            int duration = Integer.parseInt(durationText);

            Connection conn = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/group29", "group29", "yourpassword");

            String sql = "INSERT INTO WorkoutLog (member_id, workout_date, duration_minutes, notes) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, memberId);
            stmt.setDate(2, Date.valueOf(date));
            stmt.setInt(3, duration);
            stmt.setString(4, notes);
            stmt.executeUpdate();

            confirmationLabel.setText("Workout logged!");
            conn.close();
        } catch (NumberFormatException e) {
            confirmationLabel.setText("Duration must be a number.");
        } catch (SQLException e) {
            confirmationLabel.setText("Error saving workout.");
            e.printStackTrace();
        }
    }

    @FXML
    public void handleBack() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/mainMenuMember.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) workoutDate.getScene().getWindow(); // or any other @FXML node
        stage.setScene(new Scene(root));
        stage.setTitle("Main Menu");
        stage.show();
    }
}
