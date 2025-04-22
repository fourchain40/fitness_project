import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.*;

public class WorkoutHistoryController {
    @FXML private TableView<WorkoutEntry> workoutTable;
    @FXML private TableColumn<WorkoutEntry, String> dateCol;
    @FXML private TableColumn<WorkoutEntry, Integer> durationCol;
    @FXML private TableColumn<WorkoutEntry, String> notesCol;
    @FXML private TableColumn<WorkoutEntry, String> planCol;

    @FXML private TableColumn<WorkoutEntry, Void> actionCol;


    @FXML
    public void initialize() {
        ObservableList<WorkoutEntry> data = FXCollections.observableArrayList();

        try (Connection conn = DriverManager.getConnection(
                "jdbc:postgresql://bastion.cs.virginia.edu:5432/group29", "group29", "C1mbI9G3")) {

            int memberId = Session.getInstance().getUserID();

            String sql = """
                SELECT wl.workout_date, wl.duration_minutes, wl.notes,
                       wp.plan_name
                FROM WorkoutLog wl
                LEFT JOIN WorkoutPlan wp ON wl.plan_id = wp.plan_id
                WHERE wl.member_id = ?
                ORDER BY wl.workout_date DESC
            """;

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, memberId);
                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    data.add(new WorkoutEntry(
                            rs.getDate("workout_date").toString(),
                            rs.getInt("duration_minutes"),
                            rs.getString("notes"),
                            rs.getString("plan_name")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        dateCol.setCellValueFactory(cell -> cell.getValue().workoutDateProperty());
        durationCol.setCellValueFactory(cell -> cell.getValue().durationProperty().asObject());
        notesCol.setCellValueFactory(cell -> cell.getValue().notesProperty());
        planCol.setCellValueFactory(cell -> cell.getValue().planNameProperty());
        actionCol.setCellFactory(col -> new TableCell<>() {
            private final Button deleteBtn = new Button("Delete");

            {
                deleteBtn.setOnAction(e -> {
                    WorkoutEntry entry = getTableView().getItems().get(getIndex());
                    handleDelete(entry);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(deleteBtn);
                }
            }
        });
        workoutTable.setItems(data);
    }

    @FXML
    public void handleBack() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/mainMenuMember.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) workoutTable.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Main Menu");
        stage.show();
    }
    private void handleDelete(WorkoutEntry entry) {
        try (Connection conn = DriverManager.getConnection(
                "jdbc:postgresql://bastion.cs.virginia.edu:5432/group29", "group29", "C1mbI9G3")) {

            String sql = """
            DELETE FROM WorkoutLog
            WHERE member_id = ? AND workout_date = ? AND duration_minutes = ? AND notes = ?
            """;
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, Session.getInstance().getUserID());
            stmt.setDate(2, Date.valueOf(entry.getWorkoutDate()));
            stmt.setInt(3, entry.getDuration());
            stmt.setString(4, entry.getNotes());
            stmt.executeUpdate();

            workoutTable.getItems().remove(entry);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
