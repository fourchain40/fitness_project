import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JoinGroupController {
    @FXML
    public Label title;

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
            groups = databaseDriver.getGroupsNotIn(session.getUserID());
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
    public void handleBack() throws Exception
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/MainMenuMember.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) title.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Main Menu");
        stage.show();
    }

    @FXML
    public void handleJoin() throws Exception
    {
        if (groupComboBox.getSelectionModel().isEmpty()) {
            confirmationLabel.setText("Please select a group.");
            return;
        }
       try {
           Session session = Session.getInstance();
           DatabaseDriver databaseDriver = session.getDatabaseDriver();
           databaseDriver.connect();
           Group group = databaseDriver.getGroupByName(groupComboBox.getValue());
           List<Member> members = new ArrayList<Member>();
           Member m = new Member();
           m.setMember_id(session.getUserID());
           members.add(m);
           databaseDriver.addMembersToGroup(group, members);
           databaseDriver.commit();
           databaseDriver.disconnect();
       }
        catch (SQLException e) {
        throw new RuntimeException(e);
    }
        handleBack();
    }
}
