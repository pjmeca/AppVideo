package exceptions;

@SuppressWarnings("serial")
public class UserAlreadyExistsException extends Exception{
	public UserAlreadyExistsException() {
		super("Ya existe este usuario.");
	}
}
