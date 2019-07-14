package minesweeper;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("board.fxml"));
        primaryStage.getIcons().add(new Image(Main.class.getResourceAsStream("/images/icon.png")));
        primaryStage.setTitle("Minesweeper v0.96");
        primaryStage.setScene(new Scene(root,344,420));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
