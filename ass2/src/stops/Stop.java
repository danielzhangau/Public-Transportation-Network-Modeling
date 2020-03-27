package stops;

import exceptions.NoNameException;
import exceptions.TransportFormatException;
import passengers.Passenger;
import routes.Route;
import utilities.Writeable;
import vehicles.PublicTransport;

import java.util.*;

/**
 * Represents a stop in the transportation network.
 *
 * <p>Stops are where public transport vehicles collect and drop off passengers,
 * and are located along one or more routes.
 */
public class Stop extends Object implements Writeable {
    // the name of the stop
    private String name;

    // the passengers currently waiting at the stop
    private List<Passenger> passengers;

    // the routes which this stop is located on
    private List<Route> routes;

    // the vehicles currently at this stop
    private Set<PublicTransport> atStop;

    // the stops directly adjacent to this stop along routes
    private List<Stop> neighbours;

    // the x and y coordinates of this stop
    private int xCoordinate;
    private int yCoordinate;

    // the size of list of decode size
    private static final int DECODE_LIST_SIZE = 3;

    /**
     * Creates a new Stop object with the given name and coordinates.
     *
     * <p>A stop should be created with no passengers, routes, or vehicles.
     *
     * <p>If the given name contains any newline characters ('\n') or carriage returns
     * ('\r'), they should be removed from the string before it is stored.
     *
     * @param name The name of the stop being created.
     * @param x The x coordinate of the stop being created.
     * @param y The y coordinate of the stop being created.
     * @throws NoNameException If the given name is null or empty.
     */
    public Stop(String name, int x, int y) {
        if (name == null || name.isEmpty()) {
            throw new NoNameException();
        }
        this.name = name.replace("\n", "")
                .replace("\r", "");
        this.xCoordinate = x;
        this.yCoordinate = y;

        this.neighbours = new ArrayList<>();
        this.passengers = new ArrayList<>();
        this.routes = new ArrayList<>();
        this.atStop = new HashSet<>();
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
        return xCoordinate;
    }

    /**
     * Returns the y-coordinate of this stop.
     *
     * @return The y-coordinate of the stop.
     */
    public int getY() {
        return yCoordinate;
    }

    /**
     * Records that this stop is part of the given route.
     *
     * If the given route is null, it should not be added to the stop.
     *
     * @param route The route to be added.
     */
    public void addRoute(Route route) {
        if (route == null) {
            return;
        }
        routes.add(route);
    }

    /**
     * Returns the routes associated with this stop.
     *
     * <p>No specific order is required for the route objects in the returned list.
     *
     * <p>Modifying the returned list should not result in changes to the internal
     * state of the class.
     *
     * @return The routes which go past the stop.
     */
    public List<Route> getRoutes() {
        return new ArrayList<>(routes);
    }

    /**
     * Records the given stop as being a neighbour of this stop.
     *
     * <p>If the given stop is null, or if this stop is already recorded as a neighbour,
     * it should not be added as a neighbour, and the method should return early.
     *
     * @param neighbour The stop to add as a neighbour.
     */
    public void addNeighbouringStop(Stop neighbour) {
        if (neighbour == null || neighbours.contains(neighbour)) {
            return;
        }
        neighbours.add(neighbour);
    }

    /**
     * Returns all of the stops adjacent to this one on any routes.
     *
     * <p>No specific order is required for the stop objects in the returned list.
     *
     * <p>Modifying the returned list should not result in changes to the internal
     * state of the class.
     *
     * @return The neighbours of this stop.
     */
    public List<Stop> getNeighbours() {
        return new ArrayList<>(neighbours);
    }

    /**
     * Places a passenger at this stop.
     *
     * <p>If the given passenger is null, it should not be added to the stop.
     *
     * @param passenger The passenger to add to the stop.
     */
    public void addPassenger(Passenger passenger) {
        if (passenger == null) {
            return;
        }
        this.passengers.add(passenger);
    }

    /**
     * Returns the passengers currently at this stop.
     *
     * <p>The order of the passengers in the returned list should be the same as the
     * order in which the passengers were added to the stop.
     *
     * <p>Modifying the returned list should not result in changes to the internal
     * state of the class.
     *
     * @return The passengers currently waiting at the stop.
     */
    public List<Passenger> getWaitingPassengers() {
        return new ArrayList<>(passengers);
    }

    /**
     * Checks whether the given public transport vehicle is at this stop or not.
     *
     * @param transport The transport vehicle to check for.
     * @return True if the vehicle is at this stop, false otherwise.
     */
    public boolean isAtStop(PublicTransport transport) {
        return atStop.contains(transport);
    }

    /**
     * Returns the vehicles currently at this stop.
     *
     * <p>No specific order is required for the public transport objects in the
     * returned list.
     *
     * <p>Modifying the returned list should not result in changes to the internal
     * state of the class.
     *
     * @return The vehicles currently at the stop.
     */
    public List<PublicTransport> getVehicles() {
        return new ArrayList<>(atStop);
    }

    /**
     * Records a public transport vehicle arriving at this stop. There is no limit
     * on the number of vehicles that can be at a stop simultaneously.
     *
     * <p>If the given vehicle is already at this stop, or if the vehicle is null,
     * do nothing.
     *
     * <p>Otherwise, unload all of the passengers on the arriving vehicle (using
     * {@link PublicTransport#unload()}), and place them at this stop, as well as
     * recording the vehicle itself at this stop.
     *
     * <p>This method does not need to check whether this stop is on the given transport's
     * route, or whether the transport's route is a route of this stop, and should
     * also not update the location of the transport.
     *
     * @param transport The public transport vehicle arriving at this stop.
     */
    public void transportArrive(PublicTransport transport) {
        if (transport == null || isAtStop(transport)) {
            return;
        }

        List<Passenger> arriving = transport.unload();
        for (Passenger passenger : arriving) {
            addPassenger(passenger);
        }

        atStop.add(transport);
    }

    /**
     * Records a public transport vehicle departing this stop and travelling to a
     * new stop.
     *
     * <p>This method should also update the vehicle's location to be the next stop
     * (using {@link PublicTransport#travelTo(Stop)}).
     *
     * <p>If the given vehicle is not at this stop, or if the vehicle is null, or
     * if the next stop is null, do nothing.
     *
     * @param transport The transport currently leaving this stop.
     * @param nextStop The stop which the transport is travelling to.
     */
    public void transportDepart(PublicTransport transport, Stop nextStop) {
        if (!isAtStop(transport) || nextStop == null) {
            return;
        }
        transport.travelTo(nextStop);
        atStop.remove(transport);
    }

    /**
     * Returns the Manhattan distance between this stop and the given other stop.
     *
     * <p>Manhattan distance between two points, for example (x1, y1) and (x2, y2),
     * is calculated using the following formula:<br>
     * abs(x1 - x2) + abs(y1 - y2)<br>
     * where abs is a method that calculates the absolute value of a number.
     *
     * @param stop The stop to calculate the Manhattan distance to.
     * @return The Manhattan distance between this stop and the given stop (or -1
     *         if the given stop is null)
     */
    public int distanceTo(Stop stop) {
        if (stop == null) {
            return -1;
        }
        return Math.abs(getX() - stop.getX()) + Math.abs(getY() - stop.getY());
    }

    /**
     * Compares this stop to the other object for equality.
     *
     * <p>Two stops are considered equal if they have the same name, x-coordinate,
     * y-coordinate, and routes. Routes may be in any order, as long as all of this
     * stop's routes are also associated with the other stop, and vice versa. Duplicates
     * of routes do not need to be considered in determining equality (that is,
     * if this stop has routes R1 and R2, and other has routes R1, R2, and R1 again,
     * their routes can still be considered equal, ignoring duplicates).
     *
     * {@inheritDoc}
     *
     * @param other the other object to compare for equality.
     * @return True if the objects are equal (as defined above), false otherwise
     *         (including if other is null or not an instance of the {@link Stop}
     *         class.
     */
    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Stop)) {
            return false;
        }
        Stop otherStop = (Stop) other;
        return this.name.equals(otherStop.getName())
                && this.xCoordinate == otherStop.getX()
                && this.yCoordinate == otherStop.getY()
                && new HashSet<>(this.routes).equals(new HashSet<>(otherStop.getRoutes()));
    }

    @Override
    public int hashCode() {
        return this.name.hashCode();
    }

    /**
     * Creates a string representation of a stop in the format:
     *
     * <p>'{name}:{x}:{y}'
     *
     * <p>without the surrounding quotes, and where {name} is replaced by the name
     * of the stop, {x} is replaced by the x-coordinate of the stop, and {y} is
     * replaced by the y-coordinate of the stop.
     *
     * @return A string representation of the stop.
     */
    @Override
    public String toString() {
        return name + ":" + xCoordinate + ":" + yCoordinate;
    }

    /**
     * Creates a new stop object based on the given string representation.
     *
     * <p>The format of the string should match that returned by the encode() method.
     *
     * @param stopString - The string to decode.
     * @return The decoded stop object.
     * @throws TransportFormatException - If the given string is null, or the string is incorrectly formatted (according to the encode() representation). This includes, but is not limited to:
     * x- or y- coordinates that are not integer values.
     * Any extra delimiters (:) being encountered whilst parsing.
     * Any of the parts of the string being missing.
     */
    public static Stop decode(String stopString) throws TransportFormatException {
        if (stopString == null || stopString.isEmpty()) {
            throw new TransportFormatException();
        }
        String[] stopStrings  = stopString.split(":");
        if (stopStrings.length != DECODE_LIST_SIZE) {
            throw new TransportFormatException();
        }
        List<String> stopList = new ArrayList<>(Arrays.asList(stopStrings));
        String name = stopList.get(0);
        int x, y;
        try {
            x = Integer.valueOf(stopList.get(1));
            y = Integer.valueOf(stopList.get(2));
        } catch (NumberFormatException e) {
            throw new TransportFormatException();
        }
        return new Stop(name, x, y);
    }

    /**
     * Encodes this stop as a string in the same format as specified in {@link Route#toString()}).
     * Specified by:
     * encode in interface {@link Writeable}
     * @return This stop encoded as a string.
     */
    public String encode() {
        return this.toString();
    }
}
