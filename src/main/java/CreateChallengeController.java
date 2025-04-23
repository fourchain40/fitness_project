import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateChallengeController {
    @FXML
    public Label title;

    @FXML private ComboBox<String> groupComboBox;
    private Map<String, Integer> groupMap = new HashMap<>();

    public void initialize()
    {
        Session session = Session.getInstance();
        DatabaseDriver databaseDriver = session.getDatabaseDriver();

        List<Group> groups = new ArrayList<>();

        try {
            databaseDriver.connect();
            groups = databaseDriver.getGroupsByMemberID(session.getUserID());
            databaseDriver.disconnect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        for (Group group : groups) {
            String groupName = group.getName();
            int groupId = group.getId();
            groupComboBox.getItems().add(groupName);
            groupMap.put(groupName, groupId);
        }
    }

    @FXML
    public void handleBack() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/challengeMenu.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) title.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("My Groups");
        stage.show();
    }

    @FXML
    public void handleSubmit() throws IOException {
        //SQL for actually creating a new challenge
        handleBack();
    }


}
