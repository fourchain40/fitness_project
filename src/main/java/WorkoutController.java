import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import javax.xml.crypto.Data;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
        Session session = Session.getInstance();
        DatabaseDriver databaseDriver = session.getDatabaseDriver();

        List<Plan> plans = new ArrayList<>();

        try {
            databaseDriver.connect();
            plans = databaseDriver.getPlansByMemberID(session.getUserID());
            databaseDriver.disconnect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        for (Plan plan : plans) {
            String planName = plan.getPlan_name();
            int planId = plan.getPlan_id();
            planComboBox.getItems().add(planName);
            planMap.put(planName, planId);
        }

        // Optional: add blank option for no plan
        planComboBox.getItems().add(0, "None");
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

        Session session = Session.getInstance();
        DatabaseDriver databaseDriver = session.getDatabaseDriver();

        int duration = 0;

        try {
            duration = Integer.parseInt(durationText);
        } catch (NumberFormatException e) {
            confirmationLabel.setText("Duration must be a number.");
        }

        Workout workout = new Workout(
                session.getUserID(),
                0,
                date,
                duration,
                notes
        );

        String selectedPlan = planComboBox.getValue();
        if (selectedPlan != null && !selectedPlan.equals("None")) {
           workout.setPlan_id(planMap.get(selectedPlan));
        }

        try {
            databaseDriver.connect();
            databaseDriver.logWorkout(workout);
            databaseDriver.commit();
            databaseDriver.disconnect();
        } catch (SQLException e) {
            confirmationLabel.setText("Error saving workout.");
            e.printStackTrace();
        }

        confirmationLabel.setText("Workout logged!");
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
