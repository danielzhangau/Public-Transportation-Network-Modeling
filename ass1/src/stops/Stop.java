package stops;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import exceptions.NoNameException;
import passengers.Passenger;
import routes.Route;
import vehicles.PublicTransport;

/**
 * Represents a stop in the transportation network.
 * Stops are where public transport vehicles collect and drop off passengers, and are located along one or more routes.
 * @author Daniel Zhang
 */
public class Stop {
    /**
     * stop name
     */
    private String name;
    /**
     * x coordinate
     */
    private int x;
    /**
     * y coordinate
     */
    private int y;
    /**
     * a list of all neighbour stops
     */
    private List<Route> routes;
    /**
     * a list of all vehicles
     */
    private List<Stop> neighbours;
    /**
     * a list of all waiting passenger
     */
    private List<Passenger> waitingPassengers;
    /**
     * a list of all routes
     */
    private List<PublicTransport> vehicles;

    /**
     * Creates a new Stop object with the given name and coordinates.
     * A stop should be created with no passengers, routes, or vehicles.
     * <p>
     * If the given name contains any newline characters ('\n') or carriage returns ('\r'), they should be removed from the string before it is stored.
     *
     * @param name The name of the stop being created.
     * @param x    The x coordinate of the stop being created.
     * @param y    The y coordinate of the stop being created.
     * @throws NoNameException If the given name is null or empty.
     */
    public Stop(String name, int x, int y) {
        if (name == null || name.isEmpty()){
            throw new NoNameException();
        }
        else {
            Pattern p = Pattern.compile("\n|\r");
            Matcher m = p.matcher(name);
            this.name = m.replaceAll("");
        }
        this.x = x;
        this.y = y;
        this.routes = new ArrayList<>();
        this.neighbours = new ArrayList<>();
        this.waitingPassengers = new ArrayList<>();
        this.vehicles = new ArrayList<>();
    }
    /**
     * Returns the name of this stop.
     *
     * @return The name of the stop.
     */
    public String getName() {
        return name;
    }
    /**
     * Returns the x-coordinate of this stop.
     *
     * @return The x-coordinate of the stop.
     */
    public int getX() {
        return x;
    }
    /**
     * Returns the y-coordinate of this stop.
     *
     * @return The y-coordinate of the stop.
     */
    public int getY() {
        return y;
    }
    /**
     * Records that this stop is part of the given route. If the given route is null, it should not be added to the stop.
     *
     * @param  route The route to be added.
     */
    public void addRoute(Route route) {
        if (route != null){
            this.routes.add(route);
        }
    }
    /**
     * Returns the routes associated with this stop.
     * No specific order is required for the route objects in the returned list.
     *
     * Modifying the returned list should not result in changes to the internal state of the class.
     *
     * @return  The routes which go past the stop.
     */
    public List<Route> getRoutes() {
        return routes;
    }
    /**
     * Records the given stop as being a neighbour of this stop.
     * If the given stop is null, or if this stop is already recorded as a neighbour, it should not be added as a neighbour, and the method should return early.
     *
     * @param neighbour The stop to add as a neighbour.
     */
    public void addNeighbouringStop(Stop neighbour) {
        if (neighbour != null && !this.neighbours.contains(neighbour)){
            this.neighbours.add(neighbour);
        }    }
    /**
     * Returns all of the stops adjacent to this one on any routes.
     * No specific order is required for the stop objects in the returned list.
     *
     * Modifying the returned list should not result in changes to the internal state of the class.
     *
     * @return The neighbours of this stop.
     */
    public List<Stop> getNeighbours() {
        return neighbours;
    }
    /**
     * Places a passenger at this stop.
     * If the given passenger is null, it should not be added to the stop.
     *
     * @param passenger The passenger to add to the stop.
     */
    public void addPassenger(Passenger passenger) {
        if (passenger != null){
            this.waitingPassengers.add(passenger);
        }
    }
    /**
     * Returns the passengers currently at this stop.
     * The order of the passengers in the returned list should be the same as the order in which the passengers were added to the stop.
     *
     * Modifying the returned list should not result in changes to the internal state of the class.
     *
     * @return The passengers currently waiting at the stop.
     */
    public List<Passenger> getWaitingPassengers() {
        return waitingPassengers;
    }
    /**
     * Checks whether the given public transport vehicle is at this stop or not.
     *
     * @param transport The transport vehicle to check for.
     * @return True if the vehicle is at this stop, false otherwise..
     */
    public boolean isAtStop(PublicTransport transport) {
        return (transport != null && this.vehicles.contains(transport));
    }
    /**
     * Returns the vehicles currently at this stop.
     * No specific order is required for the public transport objects in the returned list.
     *
     * Modifying the returned list should not result in changes to the internal state of the class.
     *
     * @return The vehicles currently at the stop.
     */
    public List<PublicTransport> getVehicles() {
        return vehicles;
    }
    /**
     * Records a public transport vehicle arriving at this stop. There is no limit on the number of vehicles that can be at a stop simultaneously.
     * If the given vehicle is already at this stop, or if the vehicle is null, do nothing.
     *
     * Otherwise, unload all of the passengers on the arriving vehicle (using PublicTransport.unload()), and place them at this stop, as well as recording the vehicle itself at this stop.
     *
     * This method does not need to check whether this stop is on the given transport's route, or whether the transport's route is a route of this stop, and should also not update the location of the transport.
     *
     * @param transport The public transport vehicle arriving at this stop.
     */
    public void transportArrive(PublicTransport transport) {
        List<Passenger> unloadPassengers = transport.unload();
        if (transport != null && !this.vehicles.contains(transport)){
            this.waitingPassengers.addAll(unloadPassengers);
            this.vehicles.add(transport);
        }
    }
    /**
     * Records a public transport vehicle departing this stop and travelling to a new stop.
     * This method should also update the vehicle's location to be the next stop (using PublicTransport.travelTo(Stop)).
     *
     * If the given vehicle is not at this stop, or if the vehicle is null, or if the next stop is null, do nothing.
     *
     * @param transport The transport currently leaving this stop.
     */
    public void transportDepart(PublicTransport transport, Stop nextStop) {
        if(transport == null || nextStop == null || !this.vehicles.contains(transport)){
            return;
        }
        vehicles.remove(transport);
        transport.travelTo(nextStop);
        nextStop.transportArrive(transport);
    }
    /**
     * Returns the Manhattan distance between this stop and the given other stop.
     * Manhattan distance between two points, for example (x1, y1) and (x2, y2), is calculated using the following formula:
     * abs(x1 - x2) + abs(y1 - y2)
     * where abs is a method that calculates the absolute value of a number.
     *
     * @param stop The stop to calculate the Manhattan distance to.
     * @return The Manhattan distance between this stop and the given stop (or -1 if the given stop is null)
     */
    public int distanceTo(Stop stop) {
        if (stop == null) {
            return -1;
        }
        else {
            return Math.abs(this.x - stop.getX()) + Math.abs(this.y - stop.getY());
        }

    }
    /**
     * Compares this stop to the other object for equality.
     * Two stops are considered equal if they have the same name, x-coordinate, y-coordinate, and routes. Routes may be in any order, as long as all of this stop's routes are also associated with the other stop, and vice versa. Duplicates of routes do not need to be considered in determining equality (that is, if this stop has routes R1 and R2, and other has routes R1, R2, and R1 again, their routes can still be considered equal, ignoring duplicates).
     *
     * @override equals in class Object
     * @param other the other object to compare for equality.
     * @return True if the objects are equal (as defined above), false otherwise (including if other is null or not an instance of the Stop class.
     */
    @Override
    public boolean equals(Object other) {
        Stop stop = (Stop)other;
        if (other == null || getClass() != other.getClass()){
            return false;
        }
        else if (this == other){
            return true;
        }
        return x == stop.x && y == stop.y && name.equals(stop.name) && routes.equals(stop.routes);
    }
    /**
     * @override hashCode in class Object
     */
    @Override
    public int hashCode() {
        return Objects.hash(name, x, y, routes);
    }
    /**
     * Creates a string representation of a stop in the format:
     * '{name}:{x}:{y}'
     *
     * without the surrounding quotes, and where {name} is replaced by the name of the stop, {x} is replaced by the x-coordinate of the stop, and {y} is replaced by the y-coordinate of the stop.
     *
     * @override toString in class Object
     * @return A string representation of the stop.
     */
    @Override
    public String toString() {
        return this.name + ":" + this.x + ":" + this.y;
    }

}
