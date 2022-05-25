package exceptions;

@SuppressWarnings("serial")
public class FilterNotExistsException extends Exception{
	public FilterNotExistsException() {
		super("Ya existe este usuario.");
	}
}
