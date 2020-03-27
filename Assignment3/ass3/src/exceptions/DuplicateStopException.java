package exceptions;

/**
 * Exception thrown when a duplicate {@link stops.Stop}
 * (as defined by Stop.equals(Object)) is added to a {@link network.Network}.
 */
public class DuplicateStopException extends TransportException{
}
