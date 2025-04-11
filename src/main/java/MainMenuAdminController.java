import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;



public class MainMenuAdminController {
    @FXML private Label title;

    @FXML
    public void handleProfile() throws Exception
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/myProfileAdmin.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) title.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("My Profile");
        stage.show();
    }
}
