package java_server;

import java.io.FileInputStream;	
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class EncryptorAES {
	private Cipher cipher = null;
	
	public EncryptorAES() throws NoSuchAlgorithmException, NoSuchPaddingException{
		this.cipher = Cipher.getInstance("AES");
	}
	
	/**
	 * Used to decrypt the handlerConfig.ini File with the given password
	 * 
	 * @param decryptKey
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 * @throws InvalidKeyException
	 * @throws IOException 
	 * @throws InvalidKeySpecException 
	 */
	public CipherInputStream decryptFile(String fileName, String password)
			throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IOException, InvalidKeySpecException {
		byte[] salt = new byte[16];
		FileInputStream unencryptedStream = new FileInputStream(fileName);
		unencryptedStream.read(salt);
		SecretKey decryptKey = this.getKey(password, salt);
		this.cipher.init(Cipher.DECRYPT_MODE, decryptKey);
		return new CipherInputStream(unencryptedStream, this.cipher);
	}

	/**
	 * Used to encrypt the handlerConfig.ini File with the given AES Key
	 * 
	 * @param encryptKey
	 * @return
	 * @throws InvalidKeyException
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 * @throws InvalidKeySpecException
	 * @throws IOException
	 */
	public CipherOutputStream encryptFile(String fileName, String password) throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, IOException {
		byte[] salt = new byte[16];
		salt = this.getSalt();
		SecretKey encryptKey = this.getKey(password, salt);
		this.cipher.init(Cipher.ENCRYPT_MODE, encryptKey);
		FileOutputStream unencryptedStream = new FileOutputStream(fileName);
		unencryptedStream.write(salt);
		unencryptedStream.close();
		return new CipherOutputStream(new FileOutputStream(fileName, true), this.cipher);
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
	private SecretKey getKey(String password, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
		SecretKeyFactory secretKeyFactory;
		secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
		KeySpec passwordBasedEncryptionKeySpec = new PBEKeySpec(password.toCharArray(), salt, 10000, 256);
		SecretKey secretKeyFromPBKDF2 = secretKeyFactory.generateSecret(passwordBasedEncryptionKeySpec);
		SecretKey secretKey = new SecretKeySpec(secretKeyFromPBKDF2.getEncoded(), "AES");
		return secretKey;
	}

	/**
	 * Generates Pseudo Random Salt for SecretKey
	 * 
	 * @return byte Array with salt
	 * @throws NoSuchAlgorithmException
	 */
	private byte[] getSalt() throws NoSuchAlgorithmException {
		SecureRandom sr = new SecureRandom();
		byte[] salt = new byte[16];
		sr.nextBytes(salt);
		return salt;
	}
}
