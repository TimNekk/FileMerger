package timnekk.exceptions;

/*
 * Exception for when the file can not be written to.
 */
public class FileWritingException extends Exception {
    public FileWritingException(String message, Throwable cause) {
        super(message, cause);
    }
}
