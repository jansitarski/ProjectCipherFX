/**
 * Main class for file based encryption/decryption
 * @author Jan Sitarski s20701
 */
package pl.pjatk.project;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import java.io.*;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class FileCrypto extends Crypto {

    public FileCrypto(SecretKey secretKey, String transformation) throws NoSuchPaddingException, NoSuchAlgorithmException {
        super(secretKey, transformation);
    }

    public FileCrypto() {
    }

    /**
     * @param is needs FileInputStream of original unencrypted file.
     * @param os needs FileOutputStream of file that will be generated and encrypted.
     * @throws IOException         If an output or input error occurred in
     *                             reader, fileOut and cipherOut.
     * @throws InvalidKeyException If secretKey is an invalid SecretKey.
     */
    public void encrypt(InputStream is, OutputStream os) throws IOException, InvalidKeyException {
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] iv = cipher.getIV();
        os.write(iv);
        os = new CipherOutputStream(os, cipher);
        writeData(is,os);
    }

    /**
     * @param is needs FileInputStream of previously encrypted file.
     * @param os needs FileOutputStream of file that will be generated and decrypted.
     * @throws IOException                        If an output or input error occurred in
     *                                            reader, fileIn and cipherIn.
     * @throws InvalidAlgorithmParameterException If wrong algorithm is given (not AES)
     * @throws InvalidKeyException                If secretKey is an invalid SecretKey.
     */
    public void decrypt(InputStream is, OutputStream os) throws IOException, InvalidAlgorithmParameterException, InvalidKeyException {
            byte[] fileIv = new byte[16];
            is.read(fileIv);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(fileIv));
            is = new CipherInputStream(is, cipher);
            writeData(is, os);
    }

    /**
     * @param is needs FileInputStream of previously encrypted file.
     * @param os needs FileOutputStream of file that will be generated and decrypted.
     * @throws IOException                        If an output or input error occurred in
     *                                            writeData().
     * @throws InvalidAlgorithmParameterException If wrong algorithm is given (not DES)
     * @throws InvalidKeyException                If secretKey is an invalid SecretKey.
     */
    public void decryptDES(InputStream is, OutputStream os) throws InvalidAlgorithmParameterException, InvalidKeyException, IOException {
        byte[] fileIv = new byte[8];
        is.read(fileIv);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(fileIv));
        is = new CipherInputStream(is, cipher);
        writeData(is, os);
    }

    /**
     * @param file needs File of unencrypted file.
     * @param n need number which Caesar will jump.
     * @throws IOException If an output or input error occurred in
     *                     Scanner or FileWriter.
     */
    public void encryptCaesar(File file, int n) throws IOException {
        StringBuilder sb = new StringBuilder();
        Scanner reader = new Scanner(file);
        FileWriter encodedFile = new FileWriter(file.toString() + ".caenc");
        while (reader.hasNextLine()) {
            sb.append(reader.nextLine());
        }
        char[] inputText = sb.toString().toUpperCase().toCharArray();
        sb = new StringBuilder();
        for (char letter : inputText) {
            if (letter == ' ') {
                sb.append('%');
            } else {
                if (letter < 65 || letter > 90) {
                    throw new InputMismatchException("Only letters permitted");
                }
                sb.append((char) ((letter - n + 65) % 26 + 65));
            }
        }
        encodedFile.write(sb.toString());
        encodedFile.close();
        System.out.println(sb.toString());
    }

    /**
     * @param file needs File of unencrypted file.
     * @param n need number which Caesar will jump.
     * @throws IOException If an output or input error occurred in Scanner.
     */
    public void decryptCaesar(File file, int n) throws IOException {
        StringBuilder sb = new StringBuilder();
        Scanner reader = new Scanner(file);
        FileWriter decodedFile = new FileWriter(new File(file.toString().substring(0, file.toString().length() - 6)));
        while(reader.hasNextLine()){
            sb.append(reader.nextLine());
        }

        char[] inputText = sb.toString().toUpperCase().toCharArray();
        sb = new StringBuilder();
        for (char letter : inputText) {
            if(letter=='%'){
                sb.append(' ');
            }else {
                sb.append((char) ((letter + n - 65) % 26 + 65));
            }

        }
        decodedFile.write(sb.toString());
        decodedFile.close();
        System.out.println(sb.toString());
    }

    /**
     * @param is needs FileInputStream of previously encrypted file.
     * @param os needs FileOutputStream of file that will be generated and decrypted.
     * @throws IOException                        If an output or input error occurred in read(), write() or close().
     */
    private static void writeData(InputStream is, OutputStream os) throws IOException {
        byte[] buf = new byte[1024];
        int numRead = 0;
        while ((numRead = is.read(buf)) >= 0) {
            os.write(buf, 0, numRead);
        }
        os.close();
        is.close();
    }
}

