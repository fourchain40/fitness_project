import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.*;

public class EditProfileAdminController {
    @FXML private Label title;
    @FXML private TextField fname_field;
    @FXML private TextField lname_field;
    @FXML private TextField email_field;
    @FXML private TextField password_field;

    @FXML
    public void initialize() {
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

        lname_field.setText(admin.getLast_name());
        fname_field.setText(admin.getFirst_name());
        email_field.setText(admin.getEmail());
        password_field.setText(admin.getPassword());
    }

    @FXML
    public void handleEdit() throws Exception {
        Session session = Session.getInstance();
        DatabaseDriver databaseDriver = session.getDatabaseDriver();

        Administrator admin = new Administrator(
                session.getUserID(),
                fname_field.getText(),
                lname_field.getText(),
                email_field.getText(),
                password_field.getText()
        );

        try {
            databaseDriver.connect();
            databaseDriver.updateAdmin(admin);
            databaseDriver.commit();
            databaseDriver.disconnect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        handleBack();
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