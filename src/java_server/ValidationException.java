package java_server;

@SuppressWarnings("serial")
class ValidationException extends Exception {
	
	String message;
	int id;
	public ValidationException(String message, int id) {
		this.message = message;
		this.id = id;
	}
	
	@Override
	public String toString() {
		return "Exception occured - " + this.message;
	}
	@Override
	public String getMessage() {
		return  this.message;
	}
	public int getId() {
		return this.id;
	}
	
//	public String getSemantics(int id) {
//		return message;
//	}
}
