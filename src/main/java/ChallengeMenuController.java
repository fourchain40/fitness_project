import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class ChallengeMenuController {
    @FXML private Label title;
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
    public void handleGroups() throws Exception
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/myGroups.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) title.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("My Groups");
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
}
