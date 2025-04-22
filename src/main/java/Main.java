import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import javax.xml.crypto.Data;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        String postgresURL = "jdbc:postgresql://bastion.cs.virginia.edu:5432/group29";
        Properties postgresProps = new Properties();
        postgresProps.setProperty("user", "group29");
        postgresProps.setProperty("password", "C1mbI9G3");
        DatabaseDriver databaseDriver = new DatabaseDriver(postgresURL, postgresProps);

        Session session = Session.getInstance();
        session.setDatabaseDriver(databaseDriver);

        Parent root = FXMLLoader.load(getClass().getResource("/login.fxml"));
        stage.setTitle("Gym Tracker Login");
        stage.setScene(new Scene(root));
        stage.show();
    }
}