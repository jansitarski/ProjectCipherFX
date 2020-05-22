package pl.pjatk.project;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class MainMenu extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        StackPane root = new StackPane();
        Scene menuScene = new Scene(root, 600, 500);
        Button btn = new Button("Text");
        primaryStage.setScene(menuScene);
        root.getChildren().add(btn);
        primaryStage.show();
        primaryStage.setOnCloseRequest(e-> Platform.exit());

        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Stage textStage = new Stage();
                TextMenu textMenu = new TextMenu();
                try {
                    textMenu.start(textStage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }
}
