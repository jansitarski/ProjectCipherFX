/**
 * Parent class for encryption classes
 *
 * @author Jan Sitarski s20701
 */
package pl.pjatk.project;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class Crypto {
    protected SecretKey secretKey;
    protected Cipher cipher;

    /**
     * @param secretKey SecretKey used in AES or DES encryption.
     * @param transformation name of encryption algorithm (e.g AES/CBC/PKCS5Padding).
     * @throws NoSuchPaddingException checks if padding type is valid (e.g PKCS5Padding).
     * @throws NoSuchAlgorithmException checks if algorithm is valid (e.g AES).
     */
    Crypto(SecretKey secretKey, String transformation) throws NoSuchPaddingException, NoSuchAlgorithmException {
        this.secretKey = secretKey;
        this.cipher = Cipher.getInstance(transformation);
    }

    public Crypto() {
    }

    /**
     * @param secretKey needs encoded SecretKey.
     * @return decoded secretKey as String.
     */
    public static String decodeKey(SecretKey secretKey){
        byte[] keyBytes = secretKey.getEncoded();
        return new String(Base64.getEncoder().encode(keyBytes));
    }

    /**
     * @param deKey needs decoded key as String
     * @return encoded AES SecretKey.
     */
    public static SecretKey encodeKeyAES(String deKey){
        byte[] keyBytes = Base64.getDecoder().decode(deKey);
        return new SecretKeySpec(keyBytes, "AES");
    }

    /**
     * @param deKey needs decoded key as String
     * @return encoded DES SecretKey.
     */
    public static SecretKey encodeKeyDES(String deKey){
        byte[] keyBytes = Base64.getDecoder().decode(deKey);
        return new SecretKeySpec(keyBytes, "DES");
    }
}
