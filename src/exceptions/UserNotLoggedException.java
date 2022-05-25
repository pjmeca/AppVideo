package exceptions;

@SuppressWarnings("serial")
public class UserNotLoggedException extends Exception{
	public UserNotLoggedException() {
		super("Ya existe este usuario.");
	}
}
