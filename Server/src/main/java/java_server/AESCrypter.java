package java_server;

import javax.crypto.*;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

public class AESCrypter {
    private Cipher cipher;

    public AESCrypter() throws NoSuchAlgorithmException, NoSuchPaddingException {
        this.cipher = Cipher.getInstance("AES");
    }

    /**
     * Used to decrypt the handlerConfig.ini File with the given password
     *
     * @param fileName File to be decrypted
     * @param password Password used for AES-Key
     * @return CipherInputStream that is decrypted using the cipher instantiated with Password and salt from File
     */
    public CipherInputStream decryptFile(String fileName, String password)
            throws NoSuchAlgorithmException, InvalidKeyException, IOException, InvalidKeySpecException {
        byte[] salt = new byte[16];
        FileInputStream encryptedStream = new FileInputStream(fileName);
        int saltReadResult = encryptedStream.read(salt);
        if (saltReadResult < 16) throw new IOException("Could not read Salt from File");
        SecretKey decryptKey = this.getKey(password, salt);
        this.cipher.init(Cipher.DECRYPT_MODE, decryptKey);
        return new CipherInputStream(encryptedStream, this.cipher);
    }

    /**
     * Used to encrypt the handlerConfig.ini File with the given AES Key
     *
     * @param fileName File to be encrypted
     * @param password Password used for AES Key derivation
     * @return CipherOutputStream that encrypts the Data going through using password + secure random salt
     */
    public CipherOutputStream encryptFile(String fileName, String password) throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, IOException {
        byte[] salt = this.getSalt();
        SecretKey encryptKey = this.getKey(password, salt);
        this.cipher.init(Cipher.ENCRYPT_MODE, encryptKey);
        FileOutputStream unencryptedStream = new FileOutputStream(fileName);
        unencryptedStream.write(salt);
        unencryptedStream.close();
        return new CipherOutputStream(new FileOutputStream(fileName, true), this.cipher);
    }

    /**
     * Generates the SecretKey used in AES Encryption of File by using PBKDF2 + HMAC
     * + SHA512
     *
     * @param password the password the Key is derived from
     * @param salt     The Salt used for Key Derivation
     * @return AES SecretKey
     */
    private SecretKey getKey(String password, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        SecretKeyFactory secretKeyFactory;
        secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
        KeySpec passwordBasedEncryptionKeySpec = new PBEKeySpec(password.toCharArray(), salt, 10000, 256);
        SecretKey secretKeyFromPBKDF2 = secretKeyFactory.generateSecret(passwordBasedEncryptionKeySpec);
        return new SecretKeySpec(secretKeyFromPBKDF2.getEncoded(), "AES");
    }

    /**
     * Generates Pseudo Random Salt for SecretKey
     *
     * @return byte Array with salt
     */
    private byte[] getSalt() throws NoSuchAlgorithmException {
        SecureRandom sr = SecureRandom.getInstanceStrong();
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return salt;
    }
}
