package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.logging.Logger;

public class Main extends Application {

    public static Logger log = Logger.getLogger(Main.class.getName());
    public static Stage formInfo;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../appearance/main.fxml"));
        primaryStage.setTitle("Logical calculator");
        primaryStage.setResizable(false);
        primaryStage.getIcons().add(new Image(getClass().getResource("icon.png").toExternalForm()));
        primaryStage.setScene(new Scene(root));
        primaryStage.setOnCloseRequest(event -> log.info("Завершення роботи програми"));
        primaryStage.show();

        Parent root2 = FXMLLoader.load(getClass().getResource("../appearance/formInfo.fxml"));
        formInfo = new Stage();
        formInfo.setTitle("Truth table");
        formInfo.setResizable(false);
        formInfo.getIcons().add(new Image(getClass().getResource("icon2.png").toExternalForm()));
        formInfo.setOnCloseRequest(event -> log.info("Закриття таблиці істиності"));
        formInfo.setScene(new Scene(root2));

    }


    public static void main(String[] args){ launch(args); }
}
