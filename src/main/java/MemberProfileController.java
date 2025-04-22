import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.sql.*;

public class MemberProfileController {
    @FXML private Label title;
    @FXML private Label name;
    @FXML private Label gender;
    @FXML private Label date_of_birth;
    @FXML private Label height;
    @FXML private Label weight;
    @FXML private Label bio;

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
    public void handleBack() throws Exception
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/mainMenuMember.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) title.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Main Menu");
        stage.show();
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
}
