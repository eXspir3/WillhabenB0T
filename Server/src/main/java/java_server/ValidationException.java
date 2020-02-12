package java_server;

@SuppressWarnings("serial")
public class ValidationException extends Exception {
	
	String message;
	int id;
	public ValidationException(String message, int id) {
		this.message = message;
		this.id = id;
	}
	
	/**
	 * @return String representation of Exception
	 */
	@Override
	public String toString() {
		return "Exception occured - " + this.message;
	}
	
	/**
	 * Getter for the message set in throwing the Exception
	 * @return message
	 */
	@Override
	public String getMessage() {
		return  this.message;
	}
	
	/**
	 * Getter for the id set in throwing the Exception
	 * @return id
	 */
	public int getId() {
		return this.id;
	}
}
