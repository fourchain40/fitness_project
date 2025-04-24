import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class MainMenuTrainerController {
    @FXML private Label title;

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

    @FXML TableView<Member> memberTableView;
    @FXML private TableColumn<Member, String> memberNameCol;
    @FXML private TableColumn<Member, String> memberGenderCol;
    @FXML private TableColumn<Member, String> memberDOBCol;
    @FXML private TableColumn<Member, String> memberBioCol;

    @FXML
    public void handleProfile() throws Exception
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/myProfileTrainer.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) title.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("My Profile");
        stage.show();
    }

    @FXML
    public void handleLogOut() throws Exception
    {
        Session session = Session.getInstance();
        session.setUserID(0);
        session.setRole("");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) title.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Gym Tracker Login");
        stage.show();
    }

    @FXML
    public void handleEdit() throws Exception
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/editProfileTrainer.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) title.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Main Menu");
        stage.show();
    }

    @FXML
    public void handleCreateWorkoutPlan() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/createWorkoutPlan.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) title.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Create Workout Plan");
        stage.show();
    }

    @FXML
    public void handleGroups() {
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
    @FXML
    public void handleNewChallenge() throws Exception
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/createChallenge.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) title.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Create Challenge");
        stage.show();
    }

    @FXML
    public void handleMembers() throws Exception {
        Session session = Session.getInstance();
        DatabaseDriver databaseDriver = session.getDatabaseDriver();

        List<Member> members = new ArrayList<>();

        try {
            databaseDriver.connect();
            members = databaseDriver.getAllPublicMembers();
            databaseDriver.disconnect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        ObservableList<Member> memberData = FXCollections.observableArrayList(members);

        memberNameCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getFirst_name()));
        memberGenderCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getGender()));
        memberDOBCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getDate_of_birth().toString()));
        memberBioCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getBio()));

        memberTableView.setItems(memberData);
    }

    public void memberTableViewHandler(MouseEvent event) {
        if (event.getClickCount() == 2) {
            Session session = Session.getInstance();
            DatabaseDriver databaseDriver = session.getDatabaseDriver();
            Member member = memberTableView.getSelectionModel().getSelectedItem();
            Optional<MemberStats> statsOpt;
            try {
                databaseDriver.connect();
                statsOpt = databaseDriver.getMemberStatsByID(member.getMember_id());
                databaseDriver.disconnect();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            MemberStats stats = new MemberStats(0, 0, 0.0);
            if (statsOpt.isPresent()) {
                stats = statsOpt.get();
            }
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/memberProfileView.fxml"));
                Parent root = loader.load();
                MemberProfileViewController controller = loader.getController();
                controller.setMember(member);
                controller.setStats(stats);
                Stage stage = (Stage) title.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setTitle("Member Profile");
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
    }
}

