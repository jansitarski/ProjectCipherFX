package pl.pjatk.project;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainMenu extends Application {


    public BorderPane root;
    public ImageView coverArt;
    public VBox buttons;
    public Button btnText;
    public Button btnFile;

    public MainMenu() throws Exception {
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Main Menu");
        Parent root = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
        Shared.genCopyright((BorderPane) root);
        Shared.setIcon(primaryStage);
        Scene menuScene = new Scene(root, 600, 500);
        Shared.setStyling(menuScene);
        primaryStage.setScene(menuScene);
        primaryStage.show();
        primaryStage.setOnCloseRequest(e -> Platform.exit());

    }

    public void textStart(ActionEvent actionEvent) {
            Stage textStage = new Stage();
            TextMenu textMenu = new TextMenu();
            try {
                textMenu.start(textStage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    public void fileStart(ActionEvent actionEvent) {
        Stage fileStage = new Stage();
        FileMenu fileMenu = new FileMenu();
        try {
            fileMenu.start(fileStage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
