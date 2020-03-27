package routes;

/**
 * Represents a ferry route in the transportation network.
 * @author Daniel Zhang
 */
public class FerryRoute extends Route{
    /**
     * Creates a new FerryRoute with the given name and number.
     * Should meet the specification of Route.Route(String, int)
     *
     * @param name The name of the route.
     * @param routeNumber  The route number of the route.
     */
    public FerryRoute(String name, int routeNumber){
        super(name, routeNumber);
    }
    /**
     * Returns the type of this route.
     *
     * @return "ferry"
     */
    public String getType() {
        return "ferry";
    }
}
