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

import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import static pl.pjatk.project.TextCrypto.*;

public class FileMenu extends Application {
    private SecretKey secretKey;
    private File encryptedFile;
    private File selectedFile;

    private final Button btnEncrypt = new Button("Encrypt");
    private final Button btnDecrypt = new Button("Decrypt");
    private final Button btnFileChooser = new Button("Choose File");
    private final AnchorPane root = new AnchorPane();
    private final GridPane text = new GridPane();
    private final GridPane radio = new GridPane();
    private final HBox buttons = new HBox();
    private final TextField fileChosen = new TextField();
    private final ToggleGroup groupCrypt = new ToggleGroup();
    private final FileChooser fileChooser = new FileChooser();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage fileStage) {
        fileStage.setTitle("File Encryption");

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
        Spinner<Integer> caesarSpinner = new Spinner<>();
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
                selectedFile = fileChooser.showOpenDialog(fileStage);
                fileChosen.setText(selectedFile.getAbsolutePath());
                btnEncrypt.setDisable(false);
                btnDecrypt.setDisable(false);
            }
        });

        btnEncrypt.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (AESCipherRadio.isSelected()) {
                    try {
                        if (secretKeyFieldAES.getText().isBlank()) {

                            //Generates SecretKey and decodes it for UI print.
                            secretKey = KeyGenerator.getInstance("AES").generateKey();
                            secretKeyFieldAES.setText(decodeKey(secretKey));

                        } else {

                            //If key exists, imports it form UI and encodes it.
                            secretKey = encodeKeyAES(secretKeyFieldAES.getText());

                        }
                        FileCrypto encrypt = new FileCrypto(secretKey, "AES/CBC/PKCS5Padding");
                        encryptedFile = new File(selectedFile.toString() + ".enc");
                        encrypt.encrypt(new FileInputStream(selectedFile), new FileOutputStream(encryptedFile));
                    } catch (Exception o) {
                        o.printStackTrace();
                    }
                }

                //Caesar encrypt.

                if (caesarCipherRadio.isSelected()) {
                    FileCrypto encrypt = new FileCrypto();
                    try {
                        encrypt.encryptCaesar(selectedFile, (int) caesarSpinner.getValue());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                //DES encrypt

                if (DESCipherRadio.isSelected()) {
                    try {
                        if (secretKeyFieldDES.getText().isBlank()) {

                            //Generates SecretKey and decodes it for UI print.
                            secretKey = KeyGenerator.getInstance("DES").generateKey();
                            secretKeyFieldDES.setText(decodeKey(secretKey));

                        } else {

                            //If key exists, imports it form UI and encodes it.
                            secretKey = encodeKeyDES(secretKeyFieldDES.getText());

                        }
                        FileCrypto encryptDES = new FileCrypto(secretKey, "DES/CBC/PKCS5Padding");
                        encryptedFile = new File(selectedFile.toString() + ".des");
                        encryptDES.encrypt(new FileInputStream(selectedFile), new FileOutputStream(encryptedFile));
                    } catch (Exception o) {
                        o.printStackTrace();
                    }
                }
            }
        });

        btnDecrypt.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                //AES decrypt

                if (AESCipherRadio.isSelected()) {

                    //Imports key from UI and encodes.
                    secretKey = encodeKeyAES(secretKeyFieldAES.getText());

                    try {
                        FileCrypto decrypt = new FileCrypto(secretKey, "AES/CBC/PKCS5Padding");
                        encryptedFile = new File(selectedFile.toString().substring(0, selectedFile.toString().length() - 4));
                        decrypt.decrypt(new FileInputStream(selectedFile), new FileOutputStream(encryptedFile));
                    } catch (Exception o) {
                        o.printStackTrace();
                    }
                }

                //Caesar decrypt.

                if (caesarCipherRadio.isSelected()) {
                    FileCrypto decrypt = new FileCrypto();
                    try {
                        decrypt.decryptCaesar(selectedFile, (int) caesarSpinner.getValue());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                //DES decrypt

                if (DESCipherRadio.isSelected()) {

                    //Imports key from UI and encodes.
                    secretKey = encodeKeyDES(secretKeyFieldDES.getText());
                    try {
                        FileCrypto decryptDES = new FileCrypto(secretKey, "DES/CBC/PKCS5Padding");
                        encryptedFile = new File(selectedFile.toString().substring(0, selectedFile.toString().length() - 4));
                        decryptDES.decryptDES(new FileInputStream(selectedFile), new FileOutputStream(encryptedFile));
                    } catch (IOException | InvalidAlgorithmParameterException | InvalidKeyException | NoSuchPaddingException | NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
