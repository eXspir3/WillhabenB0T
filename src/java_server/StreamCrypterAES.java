package java_server;

import java.io.FileInputStream;	
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class StreamCrypterAES {
	Cipher cipher = null;
	byte[] salt = null;
	
	public StreamCrypterAES() throws NoSuchAlgorithmException, NoSuchPaddingException {
		this.cipher = Cipher.getInstance("AES");
		this.salt = new String("12345678").getBytes();
	}
	
	/**
	 * Generates the SecretKey used in AES Encryption of File by using PBKDF2 + HMAC
	 * + SHA512, uses static salt because salt is not needed in this usecase
	 * 
	 * @param password the password the Key is derived from
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 */
	public SecretKey getKey(String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
		SecretKeyFactory secretKeyFactory;
		secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
		KeySpec passwordBasedEncryptionKeySpec = new PBEKeySpec(password.toCharArray(), this.salt, 10000, 256);
		SecretKey secretKeyFromPBKDF2 = secretKeyFactory.generateSecret(passwordBasedEncryptionKeySpec);
		SecretKey secretKey = new SecretKeySpec(secretKeyFromPBKDF2.getEncoded(), "AES");
		return secretKey;
	}

	/**
	 * Used to decrypt the handlerConfig.ini File with the given AES Key
	 * 
	 * @param decryptKey
	 * @return
	 * @throws FileNotFoundException
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 * @throws InvalidKeyException
	 */
	public CipherInputStream decryptFile(SecretKey decryptKey, String fileName)
			throws FileNotFoundException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
		this.cipher.init(Cipher.DECRYPT_MODE, decryptKey);
		return new CipherInputStream(new FileInputStream(fileName), this.cipher);
	}

	/**
	 * Used to encrypt the handlerConfig.ini File with the given AES Key
	 * 
	 * @param encryptKey
	 * @return
	 * @throws FileNotFoundException
	 * @throws InvalidKeyException
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 */
	public CipherOutputStream encryptFile(SecretKey encryptKey, String fileName)
			throws FileNotFoundException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException {
		this.cipher.init(Cipher.ENCRYPT_MODE, encryptKey);
		return new CipherOutputStream(new FileOutputStream(fileName), this.cipher);
	}

}
