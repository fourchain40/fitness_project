import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CreateGroupController {
    @FXML
    public Label title;
    @FXML
    public Label confirmationLabel;
    @FXML
    public TextField name_field;
    public CheckBox join_group;

    @FXML
    public void initialize() {
        join_group.setSelected(true);
    }

    @FXML
    public void handleCreate() throws Exception {
        Session session = Session.getInstance();
        DatabaseDriver databaseDriver = session.getDatabaseDriver();

        if (name_field.getText().isEmpty()) {
            confirmationLabel.setText("Please fill out all the fields.");
            return;
        }

        Group group = new Group(0, name_field.getText(), session.getUserID(), LocalDate.now(), new ArrayList<String>(), new ArrayList<String>());
        try {
            databaseDriver.connect();
            databaseDriver.addGroup(group);
            group = databaseDriver.getGroupByName(name_field.getText());
            if(join_group.isSelected()) {
                List<Member> members = new ArrayList<Member>();
                Member m = new Member();
                m.setMember_id(session.getUserID());
                members.add(m);
                databaseDriver.addMembersToGroup(group, members);
            }
            databaseDriver.commit();
            databaseDriver.disconnect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        handleBack();
    }

    @FXML
    public void handleBack() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/MainMenuMember.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) title.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Main Menu");
        stage.show();
    }
}
