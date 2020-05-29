package pl.pjatk.project;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

import javax.crypto.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import static pl.pjatk.project.Crypto.*;

public class TextMenu extends Application {
    private SecretKey secretKeyAES;
    private SecretKey secretKeyDES;

    private final Button btn = new Button("Submit");
    private final AnchorPane root = new AnchorPane();
    private final GridPane texts = new GridPane();
    private final GridPane radio = new GridPane();
    private final TextField input = new TextField();
    private final TextField output = new TextField();
    private final ToggleGroup groupCrypt = new ToggleGroup();

    public TextMenu() {
    }

    @Override
    public void start(Stage textStage) throws Exception {

        textStage.setTitle("Text Encryption");

        texts.setVgap(5);
        texts.setHgap(5);
        texts.setAlignment(Pos.CENTER);
        //set constraints for text fields to expand
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setHgrow(Priority.NEVER);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setHgrow(Priority.ALWAYS);
        texts.getColumnConstraints().addAll(col1, col2);

        texts.add(new Label("Input"), 0, 0);
        texts.add(input, 1, 0);
        texts.add(new Label("Output"), 0, 1);
        texts.add(output, 1, 1);

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

        root.getChildren().addAll(texts, radio, btn);

        AnchorPane.setTopAnchor(radio, 250.0);
        AnchorPane.setBottomAnchor(radio, 250.0);
        AnchorPane.setLeftAnchor(radio, 30.0);

        AnchorPane.setBottomAnchor(btn, 50.0);
        AnchorPane.setLeftAnchor(btn, 30.0);
        AnchorPane.setRightAnchor(btn, 30.0);

        AnchorPane.setTopAnchor(texts, 50.0);
        AnchorPane.setLeftAnchor(texts, 30.0);
        AnchorPane.setRightAnchor(texts, 30.0);
        Scene scene = new Scene(root, 600, 500);
        textStage.setScene(scene);
        textStage.show();

        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                if (caesarCipherRadio.isSelected()) {
                    StringBuilder sb = new StringBuilder();
                    int n = (int) caesarSpinner.getValue();
                    if (!input.getText().equals("")) {
                        TextCrypto encrypt = new TextCrypto();
                        output.setText(encrypt.encryptCaesar(input.getText(), n));
                    } else {
                        TextCrypto decrypt = new TextCrypto();
                        input.setText(decrypt.decryptCaesar(output.getText(), n));
                    }
                }

                if (AESCipherRadio.isSelected()) {
                    if (!input.getText().equals("")) {
                        try {
                            if (secretKeyFieldAES.getText().equals("")) {

                                //Generates SecretKey and decodes it for UI print.

                                secretKeyAES = KeyGenerator.getInstance("AES").generateKey();
                                secretKeyFieldAES.setText(decodeKey(secretKeyAES));
                            } else {

                                //If key exists, imports it form UI and encodes it.

                                secretKeyAES = encodeKeyAES(secretKeyFieldAES.getText());
                            }
                            //I cant be fucked with doing padding therefore plain AES
                            TextCrypto encrypt = new TextCrypto(secretKeyAES, "AES");
                            output.setText(encrypt.encrypt(input.getText()));

                        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            TextCrypto decrypt = new TextCrypto(secretKeyAES, "AES");
                            input.setText(decrypt.decrypt(output.getText()));
                        } catch (NoSuchPaddingException | NoSuchAlgorithmException | IllegalBlockSizeException | InvalidKeyException | BadPaddingException e) {
                            e.printStackTrace();
                        }
                    }
                }
                if (DESCipherRadio.isSelected()) {
                    if (!input.getText().equals("")) {
                        try {
                            if (secretKeyFieldDES.getText().equals("")) {

                                //Generates SecretKey and decodes it for UI print.

                                secretKeyDES = KeyGenerator.getInstance("DES").generateKey();
                                secretKeyFieldDES.setText(decodeKey(secretKeyDES));
                            } else {

                                //If key exists, imports it form UI and encodes it.

                                secretKeyDES = encodeKeyDES(secretKeyFieldDES.getText());
                            }
                            //I cant be fucked with doing padding therefore plain AES
                            TextCrypto encrypt = new TextCrypto(secretKeyDES, "DES");
                            output.setText(encrypt.encrypt(input.getText()));

                        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            TextCrypto decrypt = new TextCrypto(secretKeyDES, "DES");
                            input.setText(decrypt.decrypt(output.getText()));
                        } catch (NoSuchPaddingException | NoSuchAlgorithmException | IllegalBlockSizeException | InvalidKeyException | BadPaddingException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }
}

