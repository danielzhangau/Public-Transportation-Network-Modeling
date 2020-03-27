package vehicles;

import routes.Route;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents a ferry in the transportation network.
 * @author Daniel Zhang
 */
public class Ferry extends PublicTransport{
    private String ferryType;
    /**
     * Creates a new Ferry object with the given id, capacity, route, and type.
     * Should meet the specification of PublicTransport.PublicTransport(int, int, Route), as well as extending it to include the following:
     *
     * If the given ferryType is null or empty, the string "CityCat" should be stored instead. If the ferry type contains any newline characters ('\n') or carriage returns ('\r'), they should be removed from the string before it is stored.\
     *
     * @param id The identifying number of the ferry.
     * @param capacity The maximum capacity of the ferry.
     * @param route The route this ferry is on.
     * @param ferryType The type of the ferry (e.g. CityCat).
     */
    public Ferry(int id, int capacity, Route route, String ferryType){
        super(id,capacity,route);
        if (ferryType == null || ferryType.isEmpty()){
            this.ferryType = "CityCat";
        }
        else {Pattern p = Pattern.compile("\n\r");
        Matcher m = p.matcher(ferryType);
        this.ferryType = m.replaceAll("");
        }
    }
    /**
     * Returns the type of this ferry.
     *
     * @return The type of the ferry.
     */
    public String getFerryType(){
        return ferryType;
    }
}
