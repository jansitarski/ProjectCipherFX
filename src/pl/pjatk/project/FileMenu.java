package pl.pjatk.project;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.File;

public class FileMenu extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage fileStage) {
        fileStage.setTitle("File Encryption");
        Button btnEncrypt = new Button("Encrypt");
        Button btnDecrypt = new Button("Decrypt");
        Button btnFileChooser = new Button("Choose File");
        AnchorPane root = new AnchorPane();
        GridPane text = new GridPane();
        GridPane radio = new GridPane();
        HBox buttons = new HBox();
        TextField fileChosen = new TextField();
        ToggleGroup groupCrypt = new ToggleGroup();
        FileChooser fileChooser = new FileChooser();

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setHgrow(Priority.NEVER);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setHgrow(Priority.ALWAYS);
        text.getColumnConstraints().addAll(col1,col2);
        text.add(new Label("File Directory:"), 0, 0);
        text.add(fileChosen, 1, 0);
        text.add(btnFileChooser, 2, 0);
        text.setAlignment(Pos.CENTER);
        text.setVgap(5);
        text.setHgap(5);

        AnchorPane.setTopAnchor(text, 50.0);
        AnchorPane.setLeftAnchor(text, 30.0);
        AnchorPane.setRightAnchor(text, 30.0);

        RadioButton caesarCipherRadio = new RadioButton("Caesar");
        caesarCipherRadio.setToggleGroup(groupCrypt);
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 25);
        Spinner<Integer> caesarSpinner = new Spinner<Integer>();
        caesarSpinner.setValueFactory(valueFactory);
        RadioButton AESCipherRadio = new RadioButton("AES");
        AESCipherRadio.setToggleGroup(groupCrypt);
        TextField secretKeyFieldAES = new TextField();
        RadioButton DESCipherRadio = new RadioButton("DES");
        DESCipherRadio.setToggleGroup(groupCrypt);
        TextField secretKeyFieldDES = new TextField();

        radio.setVgap(5);
        radio.setHgap(5);
        radio.setAlignment(Pos.CENTER_LEFT);
        radio.add(caesarCipherRadio, 0, 0);
        radio.add(caesarSpinner, 1, 0);
        radio.add(AESCipherRadio, 0, 1);
        radio.add(secretKeyFieldAES, 1, 1);
        radio.add(DESCipherRadio, 0, 2);
        radio.add(secretKeyFieldDES, 1, 2);

        AnchorPane.setBottomAnchor(radio, 200.0);
        AnchorPane.setLeftAnchor(radio, 30.0);

        buttons.getChildren().addAll(btnEncrypt,btnDecrypt);
        buttons.setAlignment(Pos.CENTER);
        buttons.setSpacing(5);
        btnEncrypt.setMaxWidth(400.0);
        btnDecrypt.setMaxWidth(100.0);
        HBox.setHgrow(btnEncrypt,Priority.ALWAYS);
        HBox.setHgrow(btnDecrypt,Priority.ALWAYS);
        btnDecrypt.setDisable(true);
        btnEncrypt.setDisable(true);

        AnchorPane.setBottomAnchor(buttons, 50.0);
        AnchorPane.setLeftAnchor(buttons, 30.0);
        AnchorPane.setRightAnchor(buttons, 30.0);

        root.getChildren().addAll(text, radio, buttons);
        Scene scene = new Scene(root, 600, 500);
        fileStage.setScene(scene);
        fileStage.show();

        btnFileChooser.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                File selectedFile = fileChooser.showOpenDialog(fileStage);
                    fileChosen.setText(selectedFile.getAbsolutePath());
                    btnEncrypt.setDisable(false);
                    btnDecrypt.setDisable(false);
            }
        });
    }
}
