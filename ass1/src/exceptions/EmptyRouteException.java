package exceptions;

/**
 * Exception thrown when a PublicTransport is added to a Route with no Stops.
 * @author Daniel Zhang
 */
public class EmptyRouteException extends TransportException{
    public EmptyRouteException(){}
}

