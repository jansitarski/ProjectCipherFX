package pl.pjatk.project;

import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

abstract class Shared {
    public static void genCopyright(BorderPane root){
        Label copyright = new Label("\u00a9 Jan Sitarski s20701");
        root.setBottom(copyright);
        copyright.setOpacity(0.4);
    }

    public static void genCopyright(AnchorPane root){
        Label copyright = new Label("\u00a9 Jan Sitarski s20701");
        AnchorPane.setBottomAnchor(copyright,0.0);
        AnchorPane.setLeftAnchor(copyright,0.0);
        copyright.setOpacity(0.4);
        root.getChildren().add(copyright);
    }

    /**
     *
     * @param radio             GridPane which hosts RadioButtons.
     * @param groupCrypt        Group which buttons belong to.
     * @param caesarCipherRadio Radio button for Caesars Cipher.
     * @param caesarSpinner     Spinner for choosing n value for Caesar Cipher.
     * @param AESCipherRadio    Radio button for AES Encryption.
     * @param secretKeyFieldAES Secret key field for AES.
     * @param DESCipherRadio    Radio button for AES Encryption.
     * @param secretKeyFieldDES Secret key field for AES.
     */
    public static void genRadio(GridPane radio,ToggleGroup groupCrypt, RadioButton caesarCipherRadio,Spinner<Integer> caesarSpinner,
                                RadioButton AESCipherRadio, TextField secretKeyFieldAES, RadioButton DESCipherRadio, TextField secretKeyFieldDES){

        final SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 25);
        caesarCipherRadio.setToggleGroup(groupCrypt);
        caesarSpinner.setValueFactory(valueFactory);
        AESCipherRadio.setToggleGroup(groupCrypt);
        DESCipherRadio.setToggleGroup(groupCrypt);
        radio.setVgap(5);
        radio.setHgap(5);
        radio.setAlignment(Pos.CENTER_LEFT);
        radio.add(caesarCipherRadio, 0, 0);
        radio.add(caesarSpinner, 1, 0);
        radio.add(AESCipherRadio, 0, 1);
        radio.add(secretKeyFieldAES, 1, 1);
        radio.add(DESCipherRadio, 0, 2);
        radio.add(secretKeyFieldDES, 1, 2);

        AnchorPane.setTopAnchor(radio, 250.0);
        AnchorPane.setBottomAnchor(radio, 250.0);
        AnchorPane.setLeftAnchor(radio, 30.0);
    }
}
