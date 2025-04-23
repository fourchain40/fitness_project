import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.beans.property.SimpleStringProperty;
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
    private TableView<Group> groupTable;
    @FXML
    private TableColumn<Group, String> nameCol;
    @FXML
    private TableColumn<Group, String> membersCol;
    @FXML
    private TableColumn<Group, String> challengesCol;
    @FXML
    private TableColumn<Group, Void> actionCol;

    @FXML
    public void initialize() {
        Session session = Session.getInstance();
        DatabaseDriver databaseDriver = session.getDatabaseDriver();
            List<Group> groups = new ArrayList<Group>();

        try {
            databaseDriver.connect();
            groups = databaseDriver.getGroupsByMemberID(session.getUserID());
            databaseDriver.disconnect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        ObservableList<Group> data = FXCollections.observableArrayList(groups);

            nameCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getName()));
            membersCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getMembersList()));
            challengesCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getChallengesList()));
            actionCol.setCellFactory(col -> new TableCell<>() {
            private final Button deleteBtn = new Button("Leave Group");

            {
                deleteBtn.setOnAction(e -> {
                   Group group = getTableView().getItems().get(getIndex());
                    handleDelete(group);
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

    private void handleDelete(Group group) {
        Session session = Session.getInstance();
        DatabaseDriver databaseDriver = session.getDatabaseDriver();

        try {
            databaseDriver.connect();
            databaseDriver.deleteMemberfromGroup(group.getId(), session.getUserID());
            databaseDriver.commit();
            databaseDriver.disconnect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        groupTable.getItems().remove(group);
    }

}