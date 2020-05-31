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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import java.io.*;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import static pl.pjatk.project.TextCrypto.*;

public class FileMenu extends Application {
    public AnchorPane root;
    public GridPane text;
    public Button btnFileChooser;
    public HBox buttons;
    public Button btnEncrypt;
    public Button btnDecrypt;
    private SecretKey secretKey;
    private File encryptedFile;
    private File selectedFile;


    private final GridPane radio = new GridPane();
    private final TextField fileChosen = new TextField();
    private final ToggleGroup groupCrypt = new ToggleGroup();
    private final FileChooser fileChooser = new FileChooser();
    private final RadioButton caesarCipherRadio = new RadioButton("Caesar");
    private final Spinner<Integer> caesarSpinner = new Spinner<>();
    private final RadioButton AESCipherRadio = new RadioButton("AES");
    private final TextField secretKeyFieldAES = new TextField();
    private final RadioButton DESCipherRadio = new RadioButton("DES");
    private final TextField secretKeyFieldDES = new TextField();
    private Stage externalStage;

    @Override
    public void start(Stage fileStage) throws IOException {
        fileStage.setTitle("File Encryption");
        Parent root = FXMLLoader.load(getClass().getResource("FileMenu.fxml"));

        //Generate radio Pane
        Shared.genRadio(radio, groupCrypt, caesarCipherRadio, caesarSpinner, AESCipherRadio, secretKeyFieldAES, DESCipherRadio, secretKeyFieldDES);

        //import copyright footer
        Shared.genCopyright((AnchorPane) root);

        Shared.setIcon(fileStage);

        ((AnchorPane) root).getChildren().add(radio);
        Scene scene = new Scene(root, 600, 500);
        Shared.setStyling(scene);
        fileStage.setScene(scene);
        fileStage.show();
        externalStage = fileStage;
    }


    public void chooseFile(ActionEvent actionEvent) {
        selectedFile = fileChooser.showOpenDialog(externalStage);
        fileChosen.setText(selectedFile.getAbsolutePath());
        btnEncrypt.setDisable(false);
        btnDecrypt.setDisable(false);
    }


    public void encryptBtn(ActionEvent actionEvent) {
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


    public void decryptBtn(ActionEvent actionEvent) {
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
}

