package vehicles;

import exceptions.EmptyRouteException;
import exceptions.OverCapacityException;
import passengers.Passenger;
import routes.Route;
import stops.Stop;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * A base public transport vehicle in the transportation network.
 * @author Daniel Zhang
 */
public abstract class PublicTransport extends Object{
    /**
     * ID of the public transport
     */
    private int id;
    /**
     * capacity of the public transport
     */
    private int capacity;
    /**
     * record the route of the public transport
     */
    private Route route;
    /**
     * the passengers on the public transport
     */
    private List<Passenger> passengers;
    /**
     * record the current stop of the public transport
     */
    private Stop currentStop;
    /**
     * Creates a new public transport vehicle with the given id, capacity, and route.
     * The vehicle should initially have no passengers on board, and should be placed at the beginning of the given route (given by Route.getStartStop()). If the route is empty, the current location should be stored as null.
     *
     * If the given capacity is negative, 0 should be stored as the capacity instead (meaning no passengers will be allowed on board this vehicle).
     *
     * @param id The identifying number of the vehicle.
     * @param capacity The maximum number of passengers allowed on board.
     * @param route The route the vehicle follows. Note that the given route should never be null (@require route != null), and thus will not be tested with a null value.
     */
    public PublicTransport(int id, int capacity, Route route){
        try {
            this.currentStop = route.getStartStop();
        }
        catch (EmptyRouteException e){
            this.currentStop = null;
        }
        this.route = Objects.requireNonNull(route);
        this.capacity = capacity;
        this.id = id;
        this.passengers = new ArrayList<>();
        if (capacity < 0){
            this.capacity = 0;
        }
    }
    /**
     * Returns the route this vehicle is on.
     *
     * @return The route this vehicle is on.
     */
    public Route getRoute(){
        return route;
    }
    /**
     * Returns the id of this route.
     *
     * @return The id of this route.
     */
    public int getId(){
        return id;
    }
    /**
     * Returns the current location of this vehicle.
     *
     * @return The stop this vehicle is currently located at, or null if it is not currently located at a stop.
     */
    public Stop getCurrentStop(){
        return currentStop;
    }
    /**
     * Returns the number of passengers currently on board this vehicle.
     *
     * @return The number of passengers in the vehicle.
     */
    public int passengerCount(){
        return passengers.size();
    }
    /**
     * Returns the number of passengers currently on board this vehicle.
     *
     * @return The number of passengers in the vehicle.
     */
    public int getCapacity(){
        return capacity;
    }
    /**
     * Returns the maximum number of passengers allowed on this vehicle.
     *
     * @return The maximum capacity of the vehicle.
     */
    public String getType(){
        return route.getType();
    }
    /**
     * Returns the passengers currently on-board this vehicle.
     * No specific order is required for the passenger objects in the returned list.
     *
     * Modifying the returned list should not result in changes to the internal state of the class.
     *
     * @return The passengers currently on the public transport vehicle.
     */
    public List<Passenger> getPassengers(){
        return passengers;
    }
    /**
     * Adds the given passenger to this vehicle.
     * If the passenger is null, the method should return without adding it to the vehicle.
     *
     * If the vehicle is already at (or over) capacity, an exception should be thrown and the passenger should not be added to the vehicle.
     *
     * @param passenger - The passenger boarding the vehicle.
     * @throws OverCapacityException If the vehicle is already at (or over) capacity.
     */
    public void addPassenger(Passenger passenger) throws OverCapacityException {
        if (passenger == null){
            return;
        }
        else if (passengers.size() >= capacity){
            throw new OverCapacityException();
        }
        else {
            passengers.add(passenger);
        }    }
    /**
     * Removes the given passenger from the vehicle.
     * If the passenger is null, or is not on board the vehicle, the method should return false, and should not have any effect on the passengers currently on the vehicle.
     *
     * @param passenger The passenger disembarking the vehicle.
     * @return True if the passenger was successfully removed, false otherwise (including the case where the given passenger was not on board the vehicle to begin with).
     */
    public boolean removePassenger(Passenger passenger){
        if (passenger == null || !passengers.contains(passenger)) {
            return false;
        }
        else {
            passengers.remove(passenger);
            return true;
        }    }
    /**
     * Empties the vehicle of all its current passengers, and returns all the passengers who were removed.
     * No specific order is required for the passenger objects in the returned list.
     *
     * If there are no passengers currently on the vehicle, the method just returns an empty list.
     *
     * Modifying the returned list should not result in changes to the internal state of the class.
     *
     * @return The passengers who used to be on the bus.
     */
    public List<Passenger> unload(){
        if (passengers.isEmpty()){
            return Collections.emptyList();
        }
        List<Passenger> unloadPassenger = new ArrayList<>(passengers);
        passengers.clear();
        return unloadPassenger;
    }
    /**
     * Updates the current location of the vehicle to be the given stop.
     * If the given stop is null, or is not on this public transport's route the current location should remain unchanged.
     *
     * @param stop The stop the vehicle has travelled to.
     */
    public void travelTo(Stop stop) {
        if (stop != null && route.getStopsOnRoute().contains(stop)) {
            this.currentStop = stop;
        }
    }
    /**
     * Creates a string representation of a public transport vehicle in the format:
     * '{type} number {id} ({capacity}) on route {route}'
     *
     * without the surrounding quotes, and where {type} is replaced by the type of the vehicle, {id} is replaced by the id of the vehicle, {capacity} is replaced by the maximum capacity of the vehicle, and {route} is replaced by the route number of the route the vehicle is on. For example:
     *
     * bus number 1 (30) on route 1
     *
     * @Override toString in class Object
     * @return A string representation of the vehicle.
     */
    @Override
    public String toString(){
        return String.format("%s number %s (%s) on route %s", getType(), id, capacity, route.getRouteNumber());
    }

}
