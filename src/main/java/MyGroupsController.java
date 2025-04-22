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

public class MyGroupsController {
    @FXML
    private Label title;
    @FXML
    private TableView<GroupEntry> groupTable;
    @FXML
    private TableColumn<GroupEntry, String> nameCol;
    @FXML
    private TableColumn<GroupEntry, String> membersCol;
    @FXML
    private TableColumn<GroupEntry, String> challengesCol;


    @FXML
    public void initialize() {
        ObservableList<GroupEntry> data = FXCollections.observableArrayList();

            int memberId = Session.getInstance().getUserID();

            ArrayList<String> g1_members = new ArrayList<String>();
            g1_members.add("Alice");
            g1_members.add("Bob");
            ArrayList<String> g1_challenges = new ArrayList<String>();
            g1_challenges.add("TestChallenge");
            Group g1 = new Group(1, "TestGroup1", 1, null, g1_members, g1_challenges);

            ArrayList<String> g2_members = new ArrayList<String>();
            g2_members.add("Alice");
            g2_members.add("Charlie");
            ArrayList<String> g2_challenges = new ArrayList<String>();
            g2_challenges.add("TestChallenge2");
            Group g2 = new Group(2, "TestGroup2", 3, null, g2_members, g2_challenges);

            ArrayList<Group> groups = new ArrayList<Group>();
            groups.add(g1);
            groups.add(g2);

            for(Group g: groups)
            {
                data.add(new GroupEntry(g.getName(), g.getMembers(), g.getChallenges()));
            }

            nameCol.setCellValueFactory(cell -> cell.getValue().nameProperty());
            membersCol.setCellValueFactory(cell -> cell.getValue().membersProperty());
            challengesCol.setCellValueFactory(cell -> cell.getValue().challengesProperty());
        groupTable.setItems(data);
    }

    @FXML
    public void handleBack() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/challengeMenu.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) title.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Challenge Menu");
        stage.show();
    }

    @FXML
    public void handleCreate() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/createGroup.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) title.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Create New Group");
        stage.show();
    }
}