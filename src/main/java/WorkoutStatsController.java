import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import javax.xml.crypto.Data;
import java.sql.*;
import java.util.Optional;

public class WorkoutStatsController {
    @FXML private Label title;
    @FXML private TextArea statsArea;

    @FXML
    public void initialize() {
        Session session = Session.getInstance();
        DatabaseDriver databaseDriver = session.getDatabaseDriver();

        Optional<MemberStats> stats = Optional.empty();

        try {
            databaseDriver.connect();
            stats = databaseDriver.getMemberStatsByID(session.getUserID());
            databaseDriver.disconnect();
        } catch (SQLException e) {
            statsArea.setText("Error loading stats.");
            e.printStackTrace();
        }

        if (stats.isPresent()) {
            statsArea.setText(String.format("""
                Total Workouts: %d
                Total Time Spent: %d minutes
                Average Duration: %.2f minutes
                """, stats.get().getTotal_workouts(), stats.get().getTotal_minutes(), stats.get().getAvg_duration()));
        } else {
            statsArea.setText("No workout data found.");
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
