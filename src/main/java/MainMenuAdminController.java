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

public class MainMenuAdminController {
    @FXML private Label title;
    @FXML private Label name;
    @FXML private Label email;

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

    @FXML
    public void initialize()
    {
        Session session = Session.getInstance();
        DatabaseDriver databaseDriver = session.getDatabaseDriver();

        Administrator admin;

        try {
            databaseDriver.connect();
            admin = databaseDriver.getAdminByID(session.getUserID());
            databaseDriver.disconnect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        name.setText("Name: " + admin.getFirst_name() + " " + admin.getLast_name());
        email.setText("Email: " + admin.getEmail());
    }

    @FXML
    public void handleEdit() throws Exception
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/editProfileAdmin.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) title.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Main Menu");
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
    public void handleMembers() throws Exception {
        Session session = Session.getInstance();
        DatabaseDriver databaseDriver = session.getDatabaseDriver();

        List<Member> members = new ArrayList<>();

        try {
            databaseDriver.connect();
            members = databaseDriver.getAllMembers();
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
            Member member = memberTableView.getSelectionModel().getSelectedItem();
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/adminEditMember.fxml"));
                Parent root = loader.load();
                adminEditMemberController controller = loader.getController();
                controller.setMember(member);
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
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/adminEditTrainer.fxml"));
                Parent root = loader.load();
                adminEditTrainerController controller = loader.getController();
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

}
