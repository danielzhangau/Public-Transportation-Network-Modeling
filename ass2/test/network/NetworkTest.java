package network;


import exceptions.TransportException;
import exceptions.TransportFormatException;
import java.io.IOException;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import routes.BusRoute;
import routes.Route;
import stops.Stop;
import vehicles.Bus;
import vehicles.PublicTransport;

public class NetworkTest {

  Network network = new Network();
  private List<Route> routes;
  private List<Stop> stops;
  private List<Stop> addStops;
  private List<PublicTransport> publicTransports;
  private Route route;
  private Stop stop;
  private Stop stop1;
  private PublicTransport publicTransport;

  @Before
  public void setUp() throws Exception {
    Stop stop = new Stop("stop1", 0, 0);
    Stop stop1 = new Stop("stop2", 1, 1);
    Route route = new BusRoute("route", 1);
    route.addStop(stop);
    route.addStop(stop1);
    PublicTransport publicTransport = new Bus(0, 20, route,
        "BUS1");

    network.addStop(stop);
    network.addStop(stop1);
    network.addRoute(route);
    network.addVehicle(publicTransport);
  }

  @Test
  public void testInit() throws IOException, TransportException {
    Network network1 = new Network("correct");
    Assert.assertEquals(network.getRoutes(), network1.getRoutes());
    Assert.assertEquals(network.getStops(), network1.getStops());
    Assert.assertEquals(network.getVehicles().size(), network1.getVehicles().size());
  }

  @Test(expected = TransportFormatException.class)
  public void testInitError() throws IOException, TransportFormatException {
    new Network("error");
  }

  @Test
  public void addRoute() {
    network.addRoute(route);
    Assert.assertEquals(routes.get(0), route);
  }

  @Test
  public void addStop() {
    network.addStop(stop);
    Assert.assertEquals(stops.get(0), stop);
  }

  @Test
  public void addStops() {
    this.network.addStops(stops);
    Assert.assertEquals(stops.get(0), stop);
  }

  @Test
  public void addVehicle() {
    this.network.addVehicle(publicTransport);
    Assert.assertEquals(publicTransports.get(0), publicTransport);
  }

  @Test
  public void getRoutes() {
    Assert.assertTrue(network.getRoutes() == route);
  }

  @Test
  public void getStops() {
    Assert.assertTrue(network.getStops() == stop);
  }

  @Test
  public void getVehicles() {
    stop.transportArrive(publicTransport);
    Assert.assertEquals(network.getVehicles().get(0), publicTransport);
  }

  @Test
  public void save() throws IOException{
    network.save("correct");
  }
}