package pl.pjatk.project;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
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

import static pl.pjatk.project.TextCrypto.*;

public class TextMenu extends Application {
    public AnchorPane root;
    public GridPane texts;
    public TextField input;
    public TextField output;
    public Button btn;

    private SecretKey secretKeyAES;
    private SecretKey secretKeyDES;


    private final GridPane radio = new GridPane();
    private final ToggleGroup groupCrypt = new ToggleGroup();
    private final Spinner<Integer> caesarSpinner = new Spinner<>();
    private final RadioButton caesarCipherRadio = new RadioButton("Caesar");
    private final RadioButton AESCipherRadio = new RadioButton("AES");
    private final TextField secretKeyFieldAES = new TextField();
    private final RadioButton DESCipherRadio = new RadioButton("DES");
    private final TextField secretKeyFieldDES = new TextField();

    public TextMenu() {
    }

    @Override
    public void start(Stage textStage) throws Exception {

        textStage.setTitle("Text Encryption");
        Parent root = FXMLLoader.load(getClass().getResource("TextMenu.fxml"));

        //Generate radio Pane
        Shared.genRadio(radio, groupCrypt, caesarCipherRadio, caesarSpinner, AESCipherRadio, secretKeyFieldAES, DESCipherRadio, secretKeyFieldDES);

        //Import copyright footer
        Shared.genCopyright((AnchorPane) root);

        Shared.setIcon(textStage);

        ((AnchorPane) root).getChildren().add(radio);
        Scene scene = new Scene(root, 600, 500);
        Shared.setStyling(scene);
        textStage.setScene(scene);
        textStage.show();

    }

    public void submit(ActionEvent actionEvent) {
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
                    //I cant be fucked with doing padding therefore plain DES
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
}
