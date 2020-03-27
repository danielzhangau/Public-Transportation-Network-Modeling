package network;


import exceptions.TransportFormatException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import routes.Route;
import stops.Stop;
import vehicles.PublicTransport;

/**
 * Represents the transportation network, and manages all of the various components therein.
 */
public class Network extends Object {
    // the stops in transportation network
    private List<Stop> stops;

    // the vehicles in transportation network
    private List<PublicTransport> vehicles;

    // the routes in transportation network
    private List<Route> routes;

    /**
     * Creates a new empty Network with no stops, vehicles, or routes.
     */
    public Network() {
        this.stops = new ArrayList<>();
        this.vehicles = new ArrayList<>();
        this.routes = new ArrayList<>();
    }

    /**
     * Creates a new Network from information contained in the file indicated by the given filename.
     *
     * <p>
     *  The file should be in the following format:
     * {number_of_stops}
     * {stop0:x0:y0}
     * ...
     * {stopN:xN:yN}
     * {number_of_routes}
     * {type0,name0,number0:stop0|stop1|...|stopM}
     * ...
     * {typeN,nameN,numberN:stop0|stop1|...|stopM}
     * {number_of_vehicles}
     * {type0,id0,capacity0,routeNumber,extra}
     * ...
     * {typeN,idN,capacityN,routeNumber,extra}
     * where {number_of_stops}, {number_of_routes}, and {number_of_vehicles}
     * are the number of stops, routes,
     * and vehicles (respectively) in the network, and where {stop0,x0,y0}
     * is the encode() representation of a Stop,
     * {type0,name0,number0:stop0|stop1|...|stopM} is the encode() representation of a Route,
     * and {typeN,idN,capacityN,routeNumber,extra} is the encode()
     * representation of a PublicTransport.
     * </p>
     *
     * <p>
     * For example:
     * 4
     * stop0:0:1
     * stop1:-1:0
     * stop2:4:2
     * stop3:2:-8
     * 2
     * train,red,1:stop0|stop2|stop1
     * bus,blue,2:stop1|stop3|stop0
     * 3
     * train,123,30,1,2
     * train,42,60,1,3
     * bus,412,20,2,ABC123
     * The Network object created should have the stops, routes,
     * and vehicles contained in the given file.
     * </p>
     *
     * @param filename - The name of the file to load the network from.
     * @throws IOException - If any IO exceptions occur whilst trying to
     * read from the file, or if the filename is null.
     * @throws TransportFormatException -
     * If any of the lines representing stops, routes,
     * or vehicles are incorrectly formatted according to their respective decode methods
     * (i.e. if their decode method throws an exception).
     * If any of the integers are incorrectly formatted (i.e. cannot be parsed).
     * If the {number_of_stops} does not match the actual number of lines
     * representing stops present.
     * This also applies to {number_of_routes} and {number_of_vehicles}.
     * If there are any extra lines present in the file
     * (the file may end with a single newline character,
     * but there may not be multiple blank lines at the end of the file).
     * If any other formatting issues are encountered whilst parsing the file.
     */
    public Network(String filename) throws IOException, TransportFormatException {
        if (filename == null || filename.isEmpty()) {
            throw new IOException();
        }
        String string = null;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(new File(filename)));
            string = reader.readLine();
            if (string == null) {
                throw new IOException();
            }
            int stopNumber = Integer.parseInt(string);
            for (int i = 0; i < stopNumber; ++i) {
                string  = reader.readLine();
                this.addStop(Stop.decode(string));
            }
            string = reader.readLine();
            int routeNumber = Integer.parseInt(string);
            for (int i = 0; i < routeNumber; ++i) {
                string  = reader.readLine();
                this.addRoute(Route.decode(string, this.getStops()));
            }
            string = reader.readLine();
            int vehicleNUmber = Integer.parseInt(string);
            for (int i = 0; i < vehicleNUmber; ++i) {
                string  = reader.readLine();
                this.addVehicle(PublicTransport.decode(string, this.getRoutes()));
            }
            if (reader.readLine() != null) {
                throw new TransportFormatException();
            }
        } catch (FileNotFoundException e) {
            throw new IOException();
        } catch (NumberFormatException e1) {
            e1.printStackTrace();
            throw new TransportFormatException();
        }
    }

    /**
     * Adds the given route to the network.
     *
     * <p>If the given route is null, it should not be added to the network.
     *
     * @param route - The route to add to the network.
     */
    public void addRoute(Route route) {
        if (route == null) {
            return;
        }
        routes.add(route);
    }

    /**
     * Adds the given stop to the transportation network.
     *
     * <p>If the given stop is null, it should not be added to the network.
     *
     * @param stop - The stop to add to the network.
     */
    public void addStop(Stop stop) {
        if (stop == null) {
            return;
        }
        stops.add(stop);
    }

    /**
     * Adds multiple stops to the transport network.
     *
     * <p>If any of the stops in the given list are null, none of them should be added
     * (i.e. either all of the stops are added, or none are).
     *
     * @param stops - The stops to add to the network.
     */
    public void addStops(List<Stop> stops) {
        if (Objects.isNull(stops) || stops.isEmpty() || stops.contains(null)) {
            return;
        }
        this.stops.addAll(stops);
    }

    /**
     * Adds the given vehicle to the network.
     *
     * <p>If the given vehicle is null, it should not be added to the network.
     *
     * @param vehicle - The vehicle to add to the network.
     */
    public void addVehicle(PublicTransport vehicle) {
        if (vehicle == null) {
            return;
        }
        vehicles.add(vehicle);
    }

    /**
     * Gets all the routes in this network.
     *
     * <p>Modifying the returned list should not result in
     * changes to the internal state of the class.
     *
     * @return All the routes in the network.
     */
    public List<Route> getRoutes(){
        return routes;
    }

    /**
     * Gets all of the stops in this network.
     *
     * <p>Modifying the returned list should not result
     * in changes to the internal state of the class.
     *
     * @return All the stops in the network
     */
    public List<Stop> getStops(){
        return stops;
    }

    /**
     * Gets all the vehicles in this transportation network.
     *
     * <p>Modifying the returned list should not result
     * in changes to the internal state of the class.
     *
     * @return All the vehicles in the transportation network.
     */
    public List<PublicTransport> getVehicles()
    {
        return vehicles;
    }

    /**
     * Saves this network to the file indicated by the given filename.
     *
     * <p>The file should be written with the same format as described
     * in the Network(String) constructor.
     *
     * <p>The stops should be written to the file in the same order
     * in which they were added to the network.
     * This also applies to the routes and the vehicles.
     *
     * @param filename - The name of the file to save the network to.
     * @throws IOException - If there are any IO errors whilst writing to the file.
     */
    public void save(String filename) throws IOException
    {
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new OutputStreamWriter
                (new FileOutputStream(filename, true)));
            out.write(stops.size());
            for (Stop stop : stops) {
                out.write("\n" + stop.toString());
            }
            out.write("\n"+ routes.size());
            for (Route route : routes) {
                out.write("\n" + routes.toString());
            }
            out.write("\n"+ vehicles.size());
            for (PublicTransport transport : vehicles) {
                out.write("\n" + transport.toString());
            }
        } catch (FileNotFoundException e) {
            throw new IOException();
        } finally {
            if (out != null) {
                out.flush();
                out.close();
            }
        }
    }
}
