package timnekk.exceptions;

/*
 * Exception for when the dependencies can not be gotten.
 */
public class DependenciesGettingException extends Exception {
    public DependenciesGettingException(String message, Throwable cause) {
        super(message, cause);
    }
}
