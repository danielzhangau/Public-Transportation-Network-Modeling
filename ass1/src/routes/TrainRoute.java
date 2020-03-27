package routes;

/**
 * Represents a train route in the transportation network.
 * @author Daniel Zhang
 */
public class TrainRoute extends Route{
    /**
     * Creates a new TrainRoute with the given name and number.
     * Should meet the specification of Route.Route(String, int)
     *
     * @param name The name of the route.
     * @param routeNumber  The route number of the route.
     */
    public TrainRoute(String name, int routeNumber){
        super(name, routeNumber);
    }
    /**
     * Returns the type of this route.
     *
     * @return "train"
     */
    public String getType() {
        return "train";
    }
}

