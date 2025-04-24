import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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

    @FXML
    public TextField name_field;

    @FXML
    public DatePicker start_date;

    @FXML
    public DatePicker end_date;

    @FXML
    public Label confirmationLabel;

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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/mainMenuMember.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) title.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Main Menu");
        stage.show();
    }

    @FXML
    public void handleSubmit() throws IOException {
        try {
            Session session = Session.getInstance();
            DatabaseDriver databaseDriver = session.getDatabaseDriver();
            if (groupComboBox.getSelectionModel().isEmpty()||name_field.getText().isEmpty()) {
                confirmationLabel.setText("Please fill out all the fields.");
                return;
            }
            databaseDriver.connect();
            Group group = databaseDriver.getGroupByName(groupComboBox.getValue());
            int group_id = group.getId();
            Challenge challenge = new Challenge(name_field.getText(), start_date.getValue(), end_date.getValue(), session.getUserID(), group_id);
            databaseDriver.addChallenge(challenge);
            int challenge_id = databaseDriver.getChallengeIDByNameandGroup(name_field.getText(), group_id);
            databaseDriver.addMembersToChallenge(challenge_id, databaseDriver.getMembersInGroup(group));
            databaseDriver.commit();
            databaseDriver.disconnect();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }

        handleBack();
    }


}
