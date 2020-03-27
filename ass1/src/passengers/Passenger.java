package passengers;

import java.util.regex.Pattern;
import java.util.regex.Matcher;
import stops.Stop;
/**
 * A base passenger in the transport network.
 * @author Daniel Zhang
 */
public class Passenger extends Object{
    /**
     passengers' destination
     */
    private Stop destination;
    /**
     passengers' name
     */
    private String name;
    /**
     * Construct a new base passenger with the given name, and without a destination.
     * If the given name is null, an empty string should be stored instead.
     *
     * If the given name contains any newline characters ('\n') or carriage returns ('\r'), they should be removed from the string before it is stored.
     *
     * @param name The name of the passenger.
     */
    public Passenger(String name) {
        //for (int i = 0; i < name.length(); i++) {
        //if (name.charAt(i) == "\n" || "\r") {
       if (name == null) {
            this.name = "";
        }
       else {
           Pattern p = Pattern.compile("\n|\r");
           Matcher m = p.matcher(name);
           this.name = m.replaceAll("");
       }
    }


    /**
     * Construct a new base passenger with the given name and destination.
     * Should meet the specification of passengers(String), as well as storing the given destination stop.
     *
     * @param name The name of the passenger.
     * @param destination  The destination of the passenger.
     */
    public Passenger(String name, Stop destination) {
        if (name == null) {
            this.name = "";
        }
        else {
            Pattern p = Pattern.compile("\n|\r");
            Matcher m = p.matcher(name);
            this.name = m.replaceAll("");
        }
        this.destination = destination;
    }
    /**
     * Returns the name of the passenger.
     *
     * @return The name of the passenger.
     */
    public String getName() {
        return name;
    }
    /**
     * Sets the destination of the passenger.
     * A value of null for the given stop simply indicates that the passenger has no destination.
     *
     * @param destination  The intended destination of the passenger, or null if the passenger has no destination.
     */
    public void setDestination(Stop destination) {
        if (destination == null){
            this.destination = null;
        }
        this.destination = destination;
    }
    /**
     * Gets the destination of the passenger.
     *
     * @return The intended destination of the passenger, or null if the passenger has no destination.
     */
    public Stop getDestination() {
        if (destination == null){
            return null;
        }
        return destination;
    }
    /**
     * Creates a string representation of the passenger in the format:
     * 'passengers named {name}'
     *
     * without surrounding quotes and with {name} replaced by the name of the passenger instance. For example:
     *
     * passengers named Agatha
     *
     * If the passenger's name is empty, the method should instead return the following:
     *
     * Anonymous passenger
     *
     * @Override toString in class Object
     * @return A string representation of the passenger.
     */
    @Override
    public String toString() {
        if (name.equals("")) {
            return ("Anonymous passenger");
        }
        else {
            return String.format("passengers named %s", this.name);
        }
    }
}
