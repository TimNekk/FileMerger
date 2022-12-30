package timnekk.exceptions;

/*
 * Exception for when the circular dependency is detected.
 */
public class CircularDependencyException extends Exception {
    public CircularDependencyException(String message) {
        super(message);
    }
}
