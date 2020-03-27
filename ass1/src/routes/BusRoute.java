package routes;


/**
 * Represents a bus route in the transportation network.
 * @author Daniel Zhang
 */
public class BusRoute extends Route{
    /**
     * Creates a new BusRoute with the given name and number.
     * Should meet the specification of Route.Route(String, int)
     *
     * @param name The name of the route.
     * @param routeNumber  The route number of the route.
     */
    public BusRoute(String name, int routeNumber){
        super(name, routeNumber);
    }
    /**
     * Returns the type of this route.
     *
     * @return "bus"
     */
    public String getType() {
        return "bus";
    }
}
