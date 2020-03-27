package exceptions;

/**
 * Exception thrown when a Passenger is added to a PublicTransport which has reached capacity. (i.e. PublicTransport.passengerCount() >= PublicTransport.getCapacity())
 * @author Daniel Zhang
 */
public class OverCapacityException extends TransportException{
    public OverCapacityException(){}
}
