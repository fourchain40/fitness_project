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
import java.util.*;


public class MainMenuMemberController {
    @FXML private Label title;
    @FXML private Label name;
    @FXML private Label gender;
    @FXML private Label date_of_birth;
    @FXML private Label height;
    @FXML private Label weight;
    @FXML private Label bio;

    @FXML private Label total_workouts;
    @FXML private Label total_minutes;
    @FXML private Label avg_duration;
    @FXML private Label stats_err;

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

    @FXML TableView<Trainer> trainerTableView;
    @FXML private TableColumn<Trainer, String> trainerNameCol;
    @FXML private TableColumn<Trainer, String> trainerGenderCol;
    @FXML private TableColumn<Trainer, String> trainerSpecCol;
    @FXML private TableColumn<Trainer, String> trainerBioCol;

    @FXML private ComboBox<String> challengeComboBox;
    private Map<String, Integer> challengeMap = new HashMap<>();

    @FXML
    public void initialize()
    {
        Session session = Session.getInstance();
        DatabaseDriver databaseDriver = session.getDatabaseDriver();

        Member member;

        try {
            databaseDriver.connect();
            member = databaseDriver.getMemberByID(session.getUserID());
            databaseDriver.disconnect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        name.setText("Name: " + member.getFirst_name() + " " + member.getLast_name());
        gender.setText("Gender: " + member.getGender());
        date_of_birth.setText("Date of Birth: " + member.getDate_of_birth().toString());
        height.setText("Height: " + member.getHeight() + " cm");
        weight.setText("Weight: " + member.getWeight() + " kg");
        bio.setText("Bio: " + member.getBio());
    }

    @FXML
    public void handleEdit() throws Exception
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/editProfileMember.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) title.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Main Menu");
        stage.show();
    }

    @FXML
    public void handleLog() throws Exception
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/logWorkout.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) title.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Log Workout");
        stage.show();
    }

    @FXML
    public void handleChallenge() throws Exception
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/challengeMenu.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) title.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Challenge Menu");
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
    public void handleStats() throws Exception {
        Session session = Session.getInstance();
        DatabaseDriver databaseDriver = session.getDatabaseDriver();

        Optional<MemberStats> stats = Optional.empty();

        try {
            databaseDriver.connect();
            stats = databaseDriver.getMemberStatsByID(session.getUserID());
            databaseDriver.disconnect();
        } catch (SQLException e) {
            stats_err.setText("Error loading stats!");
            e.printStackTrace();
        }

        if (stats.isPresent()) {
            total_workouts.setText(String.format("Total Workouts: %d", stats.get().getTotal_workouts()));
            total_minutes.setText(String.format("Total Workout Time: %d minutes", stats.get().getTotal_minutes()));
            avg_duration.setText(String.format("Average Duration: %.2f minutes", stats.get().getAvg_duration()));
        } else {
            stats_err.setText("No workout data found.");
        }
    }

    @FXML
    public void handleHistory() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/workoutHistory.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) title.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Workout History");
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

    @FXML
    public void handleTrainers() {
        Session session = Session.getInstance();
        DatabaseDriver databaseDriver = session.getDatabaseDriver();

        List<Trainer> trainers = new ArrayList<>();

        try {
            databaseDriver.connect();
            trainers = databaseDriver.getAllTrainers();
            databaseDriver.disconnect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        ObservableList<Trainer> trainerData = FXCollections.observableArrayList(trainers);

        trainerNameCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getFirst_name()));
        trainerGenderCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getGender()));
        trainerSpecCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getSpecialization()));
        trainerBioCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getBio()));

        trainerTableView.setItems(trainerData);
    }

    public void trainerTableViewHandler(MouseEvent event) {
        if (event.getClickCount() == 2) {
            Trainer trainer = trainerTableView.getSelectionModel().getSelectedItem();
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/trainerProfileView.fxml"));
                Parent root = loader.load();
                TrainerProfileViewController controller = loader.getController();
                controller.setTrainer(trainer);
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

    @FXML
    public void handleLeaderboard()
    {
        Session session = Session.getInstance();
        DatabaseDriver databaseDriver = session.getDatabaseDriver();

        List<Challenge> challenges = new ArrayList<>();

        try {
            databaseDriver.connect();
            challenges = databaseDriver.getChallengesByMemberID(session.getUserID());
            databaseDriver.disconnect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        for (Challenge challenge : challenges) {
            String challengeName = challenge.getChallenge_name();
            int challengeId = challenge.getChallenge_id();
            challengeComboBox.getItems().add(challengeName);
            challengeMap.put(challengeName, challengeId);
        }
    }

    @FXML
    public void handleLB() throws Exception {
        Session session = Session.getInstance();
        DatabaseDriver databaseDriver = session.getDatabaseDriver();
        try{
            databaseDriver.connect();
            int challenge_id = databaseDriver.getChallengeIDByName(challengeComboBox.getValue());
            databaseDriver.disconnect();
            session.setChallengeID(challenge_id);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/leaderboard.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) title.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("View Leaderboard");
            stage.show();}
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
