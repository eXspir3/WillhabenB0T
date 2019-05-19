package java_server;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.NoSuchPaddingException;

public class UserAuthentication {

	private Map<String, HashMap<String, String>> userConfig = new HashMap<String, HashMap<String, String>>();
	private HashMap<String, String> innerConfig = new HashMap<String, String>();
	private EncryptorAES fileEncryptor = null;
	private String key = null;
	private boolean loggedIn = false;
	private String loggedInUser = "";

	public UserAuthentication() throws AuthenticationException, InvalidKeyException, NoSuchAlgorithmException,
			NoSuchPaddingException, InvalidKeySpecException, ClassNotFoundException, IOException {
		this.key = "XvQ5IrJww7n9kC0S";
		this.fileEncryptor = new EncryptorAES();

	}

	/**
	 * Function used to authenticate a user with Username and Password Loads and
	 * Checks UserConfiguration that is saved to File
	 * 
	 * The Try and Catch Block is used to determine wether the User exists or not
	 * --> when a NullPointer Exception is thrown by
	 * userConfig.get(user).get("role") - the user does not Exist and a
	 * AuthenticationException is thrown
	 * 
	 * The If Else Block checks for password validity
	 * 
	 * @param user     Username
	 * @param password this Users Password
	 * @throws AuthenticationException
	 * @throws InvalidKeyException
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 * @throws InvalidKeySpecException
	 * @throws ClassNotFoundException
	 * @throws IOException
	 * @throws ValidationException
	 */
	public void login(String user, String password)
			throws AuthenticationException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidKeySpecException, ClassNotFoundException, IOException, ValidationException {
		this.validateUserPassword(user, password);
		this.logout();
		if (!this.existsUser(user)) {
			throw new AuthenticationException("Invalid Username or Password on Authentication", 2);
		}
		this.loadUserConfig();
		if (password.equals(userConfig.get(user).get("password"))) {
			this.loggedIn = true;
			this.loggedInUser = user;
			this.unloadUserConfig();
			System.out.println("Login Success!");
		} else {
			this.unloadUserConfig();
			throw new AuthenticationException("Invalid Username or Password on Authentication", 2);
		}
	}

	/**
	 * Function to add Users --> Only Users with Role "admin" can create new Users
	 * 
	 * @param user     new Username
	 * @param password new Password
	 * @throws AuthenticationException
	 * @throws InvalidKeyException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 * @throws IOException
	 * @throws NoSuchPaddingException
	 * @throws ClassNotFoundException
	 * @throws ValidationException
	 */
	public void addUser(String user, String password)
			throws AuthenticationException, InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException,
			IOException, NoSuchPaddingException, ClassNotFoundException, ValidationException {

		this.validateUserPassword(user, password);
		if (this.loggedIn && hasRole("admin")) {
			if (this.existsUser(user)) {
				throw new AuthenticationException("The User " + user + "already exists!", 3);
			}
			this.loadUserConfig();
			this.innerConfig = new HashMap<String, String>();
			this.innerConfig.put("password", password);
			this.innerConfig.put("role", "user");
			this.userConfig.put(user, this.innerConfig);
			this.saveUserConfig();
			System.out.println("addUser Success!");
		} else {
			this.unloadUserConfig();
			throw new AuthenticationException("User does not have Admin Permissions or is not logged in!", 3);
		}
	}

	/**
	 * Function to remove Users --> Only Users with Role "admin" can remove Users
	 * 
	 * @param user User to Remove
	 * @throws InvalidKeyException
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 * @throws InvalidKeySpecException
	 * @throws ClassNotFoundException
	 * @throws IOException
	 * @throws AuthenticationException
	 * @throws ValidationException
	 */
	public void removeUser(String user) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidKeySpecException, ClassNotFoundException, IOException, AuthenticationException, ValidationException {
		this.validateUserPassword(user, "nopassword");
		if (this.loggedIn && hasRole("admin") && !user.equals("admin")) {
			if (!this.existsUser(user)) {
				throw new AuthenticationException("User " + user + "does not exist!", 3);
			}
			this.loadUserConfig();
			this.userConfig.remove(user);
			this.saveUserConfig();
			System.out.println("removeUser Success!");
		} else {
			this.unloadUserConfig();
			throw new AuthenticationException("User does not have Admin Permissions or is not logged in!", 3);
		}

	}

	/**
	 * This Method Validates User and Password String input
	 * 
	 * @param user
	 * @param password
	 * @throws ValidationException
	 * @throws AuthenticationException
	 */
	private void validateUserPassword(String user, String password) throws AuthenticationException {
		ConfigValidator validator = new ConfigValidator();
		if (user == null || user.isEmpty() || password == null || password.isEmpty()) {
			throw new AuthenticationException("Username or Password was empty or null!", 1);
		}
		try {
			validator.validateString(user, Regex.userNameRegex);
		} catch (ValidationException e) {
			throw new AuthenticationException(
					"Username did not meet requirements 5-20 Allowed Charakters: 'a-z A-Z 0-9 _-' ", 1);
		}
		try {
			validator.validateString(password, Regex.passwordRegex);
		} catch (ValidationException e) {
			throw new AuthenticationException(
					"Password did not meet requirements 8-20 Allowed Charakters: 'a-z A-Z 0-9 _- !@#\\$%\\^&' ", 1);
		}
	}

	/**
	 * Checks if a user exists by trying to access its role parameters in Map
	 * 
	 * @param user User of which existance is checked
	 * @return
	 * @throws InvalidKeyException
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 * @throws InvalidKeySpecException
	 * @throws ClassNotFoundException
	 * @throws IOException
	 * @throws AuthenticationException
	 */
	private boolean existsUser(String user)
			throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException,
			ClassNotFoundException, IOException, AuthenticationException {
		this.loadUserConfig();
		try {
			this.userConfig.get(user).get("role");
		} catch (Exception e) {
			this.unloadUserConfig();
			return false;
		}
		this.unloadUserConfig();
		return true;
	}

	/**
	 * Checks if the currently loggedInUser has a given role
	 * 
	 * @param role The Role to check
	 * @return TRUE if the loggedInUser has the role, false if otherwise
	 * @throws InvalidKeyException
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 * @throws InvalidKeySpecException
	 * @throws ClassNotFoundException
	 * @throws IOException
	 * @throws AuthenticationException
	 */
	private boolean hasRole(String role) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidKeySpecException, ClassNotFoundException, IOException, AuthenticationException {
		this.loadUserConfig();
		if (this.loggedIn == true && userConfig.get(this.loggedInUser).get("role").equals(role)) {
			System.out.println("User: " + this.loggedInUser + " has Role: " + role);
			this.unloadUserConfig();
			return true;
		}
		this.unloadUserConfig();
		return false;
	}

	/**
	 * Reset loggedIn and LoggedInUser
	 */
	private void logout() {
		this.loggedIn = false;
		this.loggedInUser = "";
	}

	/**
	 * Method Loads UserConfig decrypted from File into HashMap
	 * 
	 * @throws IOException
	 * @throws InvalidKeyException
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 * @throws InvalidKeySpecException
	 * @throws ClassNotFoundException
	 * @throws AuthenticationException
	 */
	@SuppressWarnings("unchecked")
	private void loadUserConfig() throws IOException, InvalidKeyException, NoSuchAlgorithmException,
			NoSuchPaddingException, InvalidKeySpecException, ClassNotFoundException, AuthenticationException {
		File f = new File("auth.ween");
		if (f.isFile() && f.canRead()) {
			System.out.println("Loading Userconfig.....");
			ObjectInputStream obInputStream = new ObjectInputStream(fileEncryptor.decryptFile("auth.ween", this.key));
			this.userConfig = (Map<String, HashMap<String, String>>) obInputStream.readObject();
			System.out.println(userConfig);
			obInputStream.close();
		} else {
			this.unloadUserConfig();
			throw new AuthenticationException("Error loading UserConfiguration for Authentication", 1);
		}

	}

	/**
	 * Unloads the UserConfig by setting the Variables to null and making it ready
	 * for GC
	 */
	private void unloadUserConfig() {
		this.innerConfig = null;
		this.userConfig = null;
	}

	/**
	 * Function used to save UserConfig from Map to encrypted File
	 * 
	 * @throws IOException
	 * @throws InvalidKeyException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 */
	private void saveUserConfig()
			throws IOException, InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException {
		System.out.println("Saving Userconfig.....");
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(
				fileEncryptor.encryptFile("auth.ween", this.key));
		objectOutputStream.writeObject(this.userConfig);
		objectOutputStream.close();
		this.unloadUserConfig();
	}
}
