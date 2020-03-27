package exceptions;

/**
 * Exception thrown when a subclass of PublicTransport is added to a Route for a different type of transport. i.e. Train added to BusRoute
 * @author Daniel Zhang
 */
public class IncompatibleTypeException extends TransportException{
    public IncompatibleTypeException(){}
}
