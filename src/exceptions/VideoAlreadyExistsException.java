package exceptions;

@SuppressWarnings("serial")
public class VideoAlreadyExistsException extends Exception{
	public VideoAlreadyExistsException() {
		super("Ya existe este vídeo.");
	}
}
