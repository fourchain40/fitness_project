import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class CreateGroupController {
    @FXML
    public Label title;
    public CheckBox join_group;

    @FXML
    public void initialize() {
        join_group.setSelected(true);
    }

    @FXML
    public void handleCreate() throws Exception {
        //actual code to handle adding a group
        handleBack();
    }
    @FXML
    public void handleBack() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/myGroups.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) title.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("My Groups");
        stage.show();
    }
}
