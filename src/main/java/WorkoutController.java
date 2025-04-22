import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class WorkoutController {
    @FXML private TextArea workoutDetails;
    @FXML private TextField durationField;
    @FXML private DatePicker workoutDate;
    @FXML private Label confirmationLabel;

    @FXML private ComboBox<String> planComboBox;
    private Map<String, Integer> planMap = new HashMap<>();  // plan name → id

    // This should eventually be set when the user logs in
    private int memberId = 1;

    @FXML
    public void initialize() {
        try (Connection conn = DriverManager.getConnection(
                "jdbc:postgresql://bastion.cs.virginia.edu:5432/group29", "group29", "C1mbI9G3")) {

            int memberId = Session.getInstance().getUserID();
            String sql = "SELECT plan_id, plan_name FROM WorkoutPlan WHERE member_id = ?";

            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, memberId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int planId = rs.getInt("plan_id");
                String planName = rs.getString("plan_name");
                planComboBox.getItems().add(planName);
                planMap.put(planName, planId);
            }

            // Optional: add blank option for no plan
            planComboBox.getItems().add(0, "None");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
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
                    "jdbc:postgresql://bastion.cs.virginia.edu:5432/group29", "group29", "C1mbI9G3");

            String sql = "INSERT INTO WorkoutLog (member_id, plan_id, workout_date, duration_minutes, notes) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, memberId);

            // Get selected plan ID or set NULL
            String selectedPlan = planComboBox.getValue();
            if (selectedPlan == null || selectedPlan.equals("None")) {
                stmt.setNull(2, Types.INTEGER);
            } else {
                stmt.setInt(2, planMap.get(selectedPlan));
            }

            stmt.setDate(3, Date.valueOf(date));
            stmt.setInt(4, duration);
            stmt.setString(5, notes);
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
