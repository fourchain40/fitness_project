import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.*;
import java.time.LocalDate;
public class CreateWorkoutPlanController {
    @FXML private TextField emailField;
    @FXML private TextField planNameField;
    @FXML private DatePicker startDatePicker;
    @FXML private DatePicker endDatePicker;
    @FXML private TextArea descriptionArea;
    @FXML private Label statusLabel;

    @FXML
    private void handleAssignPlan() {
        String email = emailField.getText().trim();
        String planName = planNameField.getText().trim();
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();
        String description = descriptionArea.getText().trim();

        if (email.isEmpty() || planName.isEmpty() || startDate == null || endDate == null || description.isEmpty()) {
            statusLabel.setText("Please fill all fields.");
            return;
        }

        try (Connection conn = DriverManager.getConnection(
                "jdbc:postgresql://bastion.cs.virginia.edu:5432/group29", "group29", "C1mbI9G3")) {

            // Get member_id by email
            PreparedStatement getMember = conn.prepareStatement("SELECT member_id FROM member WHERE email = ?");
            getMember.setString(1, email);
            ResultSet rs = getMember.executeQuery();

            if (!rs.next()) {
                statusLabel.setText("Member not found.");
                return;
            }

            int memberId = rs.getInt("member_id");
            int trainerId = Session.getInstance().getUserID();

            // Insert workout plan
            PreparedStatement insertPlan = conn.prepareStatement(
                    "INSERT INTO WorkoutPlan (member_id, trainer_id, plan_name, start_date, end_date, description) " +
                            "VALUES (?, ?, ?, ?, ?, ?)");
            insertPlan.setInt(1, memberId);
            insertPlan.setInt(2, trainerId);
            insertPlan.setString(3, planName);
            insertPlan.setDate(4, Date.valueOf(startDate));
            insertPlan.setDate(5, Date.valueOf(endDate));
            insertPlan.setString(6, description);
            insertPlan.executeUpdate();

            statusLabel.setText("Workout plan assigned successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            statusLabel.setText("Error assigning workout plan.");
        }
    }

    @FXML
    private void handleReturnToMenu() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/mainMenuTrainer.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) emailField.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Main Menu");
        stage.show();
    }
}
