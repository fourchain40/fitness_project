import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.*;

public class WorkoutStatsController {
    @FXML private Label title;
    @FXML private TextArea statsArea;

    @FXML
    public void initialize() {
        try (Connection conn = DriverManager.getConnection(
                "jdbc:postgresql://bastion.cs.virginia.edu:5432/group29", "group29", "C1mbI9G3")) {

            int memberId = Session.getInstance().getUserID();
            String sql = """
                SELECT COUNT(*) AS total_workouts,
                       SUM(duration_minutes) AS total_minutes,
                       AVG(duration_minutes) AS avg_duration
                FROM WorkoutLog
                WHERE member_id = ?
            """;

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, memberId);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    int total = rs.getInt("total_workouts");
                    int totalMinutes = rs.getInt("total_minutes");
                    double avg = rs.getDouble("avg_duration");

                    statsArea.setText(String.format("""
                        Total Workouts: %d
                        Total Time Spent: %d minutes
                        Average Duration: %.2f minutes
                    """, total, totalMinutes, avg));
                } else {
                    statsArea.setText("No workout data found.");
                }
            }
        } catch (SQLException e) {
            statsArea.setText("Error loading stats.");
            e.printStackTrace();
        }
    }

    @FXML
    public void handleBack() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/mainMenuMember.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) title.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Main Menu");
        stage.show();
    }
}
