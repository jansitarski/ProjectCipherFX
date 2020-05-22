/**
 * Main class for text based encryption/decryption
 * @author Jan Sitarski s20701
 */
package pl.pjatk.project;

import javax.crypto.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class TextCrypto extends Crypto {
    public TextCrypto(SecretKey secretKey, String transformation) throws NoSuchPaddingException, NoSuchAlgorithmException {
        super(secretKey, transformation);
    }

    public TextCrypto() {
    }

    /**
     * @param input needs plain text String to encrypt;
     * @return encrypted plain text encrypted String.
     * @throws InvalidKeyException wrong secretKey in cipher.init.
     * @throws BadPaddingException wrong padding in transformation.
     * @throws IllegalBlockSizeException wrong block size for selected cipher.
     */
    public String encrypt(String input) throws InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        return Base64.getEncoder().encodeToString(cipher.doFinal(input.getBytes()));
    }

    /**
     * @param input needs plain text encrypted String.
     * @return plain text decrypted String.
     * @throws InvalidKeyException wrong secretKey in cipher.init.
     * @throws BadPaddingException wrong padding in transformation.
     * @throws IllegalBlockSizeException wrong block size for selected cipher.
     */
    public String decrypt(String input) throws InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] original = cipher.doFinal(Base64.getDecoder().decode(input));
        return new String(original);
    }

    /**
     * @param text needs plain text String.
     * @param n needs number which to skip in Caesars Cipher
     * @return plain text encrypted String.
     */
    public String encryptCaesar(String text, int n) {
        StringBuilder sb = new StringBuilder();
        char[] inputText = text.toUpperCase().toCharArray();
        for (char letter : inputText) {
            sb.append((char) ((letter + n - 65) % 26 + 65));
        }
        return sb.toString();
    }

    /**
     * @param text needs plain text String.
     * @param n needs number which to skip in Caesars Cipher
     * @return plain text decrypted String.
     */
    public String decryptCaesar(String text, int n){
        StringBuilder sb = new StringBuilder();
        char[] inputText = text.toUpperCase().toCharArray();
        for (char letter : inputText) {
            sb.append((char) ((letter - n + 65) % 26 + 65));
        }
        return sb.toString();
    }
}
