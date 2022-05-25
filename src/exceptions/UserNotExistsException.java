package exceptions;

@SuppressWarnings("serial")
public class UserNotExistsException extends Exception{
	public UserNotExistsException() {
		super("Ya existe este usuario.");
	}
}
