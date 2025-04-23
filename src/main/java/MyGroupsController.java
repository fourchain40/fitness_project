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
    private TableColumn<GroupEntry, Void> actionCol;

    @FXML
    public void initialize() {
        Session session = Session.getInstance();
        ObservableList<GroupEntry> data = FXCollections.observableArrayList();
        DatabaseDriver databaseDriver = session.getDatabaseDriver();
            List<Group> groups = new ArrayList<Group>();

        try {
            databaseDriver.connect();
            groups = databaseDriver.getGroupsByMemberID(session.getUserID());
            databaseDriver.disconnect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

            for(Group g: groups)
            {
                data.add(new GroupEntry(g.getName(), g.getMembers(), g.getChallenges()));
            }

            nameCol.setCellValueFactory(cell -> cell.getValue().nameProperty());
            membersCol.setCellValueFactory(cell -> cell.getValue().membersProperty());
            challengesCol.setCellValueFactory(cell -> cell.getValue().challengesProperty());
            actionCol.setCellFactory(col -> new TableCell<>() {
            private final Button deleteBtn = new Button("Delete");

            {
                deleteBtn.setOnAction(e -> {
                   GroupEntry g = getTableView().getItems().get(getIndex());
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

    @FXML
    public void handleJoin() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/joinGroup.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) title.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Join Existing Group");
        stage.show();
    }


}