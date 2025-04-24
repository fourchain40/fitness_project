import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LeaderboardController {
    @FXML
    public Label title;
    @FXML
    private TableView<LeaderboardRow> lbTable;
    @FXML
    private TableColumn<LeaderboardRow, String> nameCol;
    @FXML
    private TableColumn<LeaderboardRow, String> pointsCol;
    @FXML
    private TableColumn<LeaderboardRow, String> rankCol;
    @FXML
    public void handleBack() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/mainMenuMember.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) title.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Main Menu");
        stage.show();
    }
    public void initialize()
    {
        Session session = Session.getInstance();
        DatabaseDriver databaseDriver = session.getDatabaseDriver();
        List<LeaderboardRow> rows = new ArrayList<LeaderboardRow>();

        try {
            databaseDriver.connect();
            rows = databaseDriver.getChallengeLeaderboard(session.getChallengeID());
            databaseDriver.disconnect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        ObservableList<LeaderboardRow> data = FXCollections.observableArrayList(rows);

        nameCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getName()));
        rankCol.setCellValueFactory(cell -> new SimpleStringProperty(Integer.toString(cell.getValue().getRank())));
        pointsCol.setCellValueFactory(cell -> new SimpleStringProperty(Integer.toString(cell.getValue().getScore())));
        lbTable.setItems(data);
    }
}
