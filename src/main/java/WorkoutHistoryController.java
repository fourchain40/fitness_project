import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class WorkoutHistoryController {
    @FXML private TableView<Workout> workoutTable;
    @FXML private TableColumn<Workout, String> dateCol;
    @FXML private TableColumn<Workout, String> durationCol;
    @FXML private TableColumn<Workout, String> notesCol;
    @FXML private TableColumn<Workout, String> planCol;

    @FXML private TableColumn<Workout, Void> actionCol;


    @FXML
    public void initialize() {
        Session session = Session.getInstance();
        DatabaseDriver databaseDriver = session.getDatabaseDriver();

        List<Workout> workouts = new ArrayList<>();

        try {
            databaseDriver.connect();
            workouts = databaseDriver.getWorkoutHistoryByID(session.getUserID());
            databaseDriver.disconnect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        ObservableList<Workout> data = FXCollections.observableArrayList(workouts);

        dateCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getWorkout_date().toString()));
        durationCol.setCellValueFactory(cell -> new SimpleStringProperty(String.valueOf(cell.getValue().getDuration_minutes())));
        notesCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getNotes()));
        planCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getPlan_name()));
        actionCol.setCellFactory(col -> new TableCell<>() {
            private final Button deleteBtn = new Button("Delete");

            {
                deleteBtn.setOnAction(e -> {
                    Workout workout = getTableView().getItems().get(getIndex());
                    handleDelete(workout);
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
    private void handleDelete(Workout workout) {
        Session session = Session.getInstance();
        DatabaseDriver databaseDriver = session.getDatabaseDriver();

        try {
            databaseDriver.connect();
            databaseDriver.deleteWorkoutByID(workout.getLog_id());
            databaseDriver.commit();
            databaseDriver.disconnect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        workoutTable.getItems().remove(workout);
    }
}
