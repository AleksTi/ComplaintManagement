package ru.yandex.sashanc;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.log4j.Logger;

public class Main extends Application {
    private static final Logger logger = Logger.getLogger(Main.class);

    @Override
    public void start(Stage primaryStage) throws Exception{
        logger.info("Started start()");
        System.out.println("Started start()");
        logger.info(getClass().getResource("/views/sample.fxml").getPath());
        Parent root = FXMLLoader.load(getClass().getResource("/views/sample.fxml"));
        primaryStage.setTitle("Complaint Managment");
        primaryStage.setScene(new Scene(root, 900, 600));
        primaryStage.show();
        logger.info("Finished start()");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
