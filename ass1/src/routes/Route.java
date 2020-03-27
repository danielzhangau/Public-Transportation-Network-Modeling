package routes;

import exceptions.EmptyRouteException;
import exceptions.IncompatibleTypeException;
import stops.Stop;
import vehicles.PublicTransport;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents a route in the transportation network.
 * A route is essentially a collection of stops which public transport vehicles can follow.
 * @author Daniel Zhang
 */
public abstract class Route extends Object {
    /**
     * route name
     */
    private String name;
    /**
     * route number
     */
    private int routeNumber;
    /**
     * a list of all the stops on the route
     */
    private List<Stop>stopsOnRoute;
    /**
     * a list of all the transport
     */
    private List<PublicTransport> transports;

    /**
     * Creates a new Route with the given name and number.
     * The route should initially have no stops or vehicles on it.
     *
     * If the given name contains any newline characters ('\n') or carriage returns ('\r'), they should be removed from the string before it is stored.
     *
     * If the given name is null, an empty string should be stored in its place.
     *
     * @param name The name of the route.
     * @param routeNumber  The route number of the route.
     */
    public Route(String name, int routeNumber){
        if (name == null){
            this.name = "";
        }
        else {
            Pattern p = Pattern.compile("\n|\r");
            Matcher m = p.matcher(name);
            this.name = m.replaceAll("");
        }
        this.routeNumber = routeNumber;
        this.stopsOnRoute = new ArrayList<>();
        this.transports = new ArrayList<>();
    }
    /**
     * Returns the name of the route.
     *
     * @return The route name.
     */
    public String getName() {
        return name;
    }
    /**
     * Returns the number of the route.
     *
     * @return The route number.
     */
    public int getRouteNumber() {
        return routeNumber;
    }
    /**
     * Returns the stops which comprise this route.
     * The order of the stops in the returned list should be the same as the order in which the stops were added to the route.
     *
     * Modifying the returned list should not result in changes to the internal state of the class.
     *
     * @return The stops making up the route.
     */
    public List<Stop> getStopsOnRoute() {
        return stopsOnRoute;
    }
    /**
     * Returns the first stop of the route (i.e. the first stop to be added to the route).
     *
     * @return The start stop of the route.
     * @throws EmptyRouteException If there are no stops currently on the route
     */
    public Stop getStartStop() throws EmptyRouteException {
        if (stopsOnRoute.isEmpty()) {
            throw new EmptyRouteException();
        }
        return stopsOnRoute.get(0);
    }
    /**
     * Adds a stop to the route.
     * If the given stop is null, it should not be added to the route.
     *
     * If this is the first stop to be added to the route, the given stop should be recorded as the starting stop of the route. Otherwise, the given stop should be recorded as a neighbouring stop of the previous stop on the route (and vice versa) using the Stop.addNeighbouringStop(Stop) method.
     *
     * This route should also be added as a route of the given stop (if the given stop is not null) using the Stop.addRoute(Route) method.
     *
     * @param stop The stop to be added to this route.
     */
    public void addStop(Stop stop){
        if(stop == null){
            return;
        }
        else if (stopsOnRoute.isEmpty()){
            stopsOnRoute.add(stop);
        }
        else {
            Stop lastStop = stopsOnRoute.get(stopsOnRoute.size() - 1);
            stop.addNeighbouringStop(lastStop);
            lastStop.addNeighbouringStop(stop);
        }
        stop.addRoute(this);
    }
    /**
     * Returns the public transport vehicles currently on this route.
     * No specific order is required for the public transport objects in the returned list.
     *
     * Modifying the returned list should not result in changes to the internal state of the class.
     *
     * @return The vehicles currently on the route.
     */
    public List<PublicTransport> getTransports(){
        return transports;
    }
    /**
     * Adds a vehicle to this route.
     * If the given transport is null, it should not be added to the route.
     *
     * The method should check for the transport being null first, then for an empty route, and then for incompatible types (in that order).
     *
     * @param transport The vehicle to be added to the route.
     * @throws EmptyRouteException If there are not yet any stops on the route.
     * @throws IncompatibleTypeException If the given vehicle is of the incorrect type for this route. This depends on the type of the route, i.e. a BusRoute can only accept Bus instances.
     */
    public void addTransport(PublicTransport transport) throws EmptyRouteException, IncompatibleTypeException {
        if (transport == null){
            return;
        }
        else if (this.stopsOnRoute.isEmpty()){
            throw new EmptyRouteException();
        }
        else if (!this.getType().equals(transport.getType())){
            throw new IncompatibleTypeException();
        }
        transports.add(transport);
    }
    /**
     * Compares this stop with another object for equality. Two routes are equal if their names and route numbers are equal.
     *
     * @override equals in class Object
     * @param other The other object to compare for equality.
     * @return True if the objects are equal (as defined above), false otherwise (including if other is null or not an instance of the Route class.
     */
    @Override
    public boolean equals(Object other){
        if (other == null || !(other instanceof Route)){
            return false;
        }
        Route route = (Route)other;
        return (name.equals(route.name)  && routeNumber == route.routeNumber);
    }
    /**
     * public int hashCode()
     *
     * @override hashCode in class Object
     */
    @Override
    public int hashCode(){
        return Objects.hash(name, routeNumber);
    }
    /**
     * Returns the type of this route.
     *
     * @return The type of the route (see subclasses)
     */
    public abstract String getType();
    /**
     * Creates a string representation of a route in the format:
     * '{type},{name},{number}:{stop0}|{stop1}|...|{stopN}'
     *
     * without the surrounding quotes, and where {type} is replaced by the type of the route, {name} is replaced by the name of the route, {number} is replaced by the route number, and {stop0}|{stop1}|...|{stopN} is replaced by a list of the names of the stops stops making up the route. For example:
     *
     * bus,red,1:UQ Lakes|City|Valley
     *
     * @override toString in class Object
     * @return A string representation of the route.
     */
    @Override
    public String toString(){
        String stopsWithChar = stopsOnRoute.get(0).toString();
        for(int i = 0; i < stopsOnRoute.size(); ++i){
            stopsWithChar = stopsWithChar.concat("|") + stopsOnRoute.get(i).toString();
        }
        return String.format("%s,%s,%s:%s", getType(),name, routeNumber, stopsWithChar);
    }

// dont know how to present stops like this {stop0}|{stop1}|...|{stopN}'
}
