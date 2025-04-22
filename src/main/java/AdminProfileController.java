import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.sql.*;

public class AdminProfileController {
    @FXML private Label title;
    @FXML private Label name;

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
    }
    @FXML
    public void handleBack() throws Exception
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/mainMenuAdmin.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) title.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Main Menu");
        stage.show();
    }
}
