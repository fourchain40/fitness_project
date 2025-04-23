import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class CreateChallengeController {
    @FXML
    public Label title;

    @FXML
    public void handleBack() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/challengeMenu.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) title.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("My Groups");
        stage.show();
    }

    @FXML
    public void handleSubmit() throws IOException {
        //SQL for actually creating a new challenge
        handleBack();
    }


}
