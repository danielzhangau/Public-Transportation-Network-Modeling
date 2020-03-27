package stops;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import passengers.Passenger;
import routes.BusRoute;
import routes.Route;
import vehicles.Bus;
import vehicles.PublicTransport;

/**
 * Test class of Stop
 * @author Daniel Zhang
 */
public class StopTest {
    private Stop stop;
    private Stop stop1;
    private Stop stop2;
    private Route busRoute;
    private Route nullRoute;
    private Passenger passenger;
    private Passenger nullPassenger;
    private PublicTransport transport;
    private PublicTransport transport1;

    @Before
    public void setUp() throws Exception {
        stop = new Stop("st\nop\r", 0, 255);
        stop1 = new Stop("stop1", 1, 366);
        stop2 = new Stop("stop2", 2, 477);
        busRoute = new BusRoute("bus",1 );
        nullRoute = new BusRoute(null,1);
        nullPassenger = new Passenger(null);
        passenger = new Passenger("Agatha", stop);
        transport = new Bus(0, 10, busRoute, "number 1");
        transport1 = new Bus(1, 11, busRoute, "number 2");
        transport.addPassenger(passenger);
    }
    @Test
    public void getName() {
        Assert.assertEquals("stop", stop.getName());
        Assert.assertEquals("stop1", stop1.getName());
    }
    @Test
    public void getX() {
        Assert.assertEquals(0, stop.getX());
    }

    @Test
    public void getY() {
        Assert.assertEquals(366, stop1.getY());
    }

    @Test
    public void testAddRoute() {
        stop1.addRoute(busRoute);
        stop1.addRoute(nullRoute);
        Assert.assertEquals(busRoute, stop1.getRoutes().get(0));
        Assert.assertFalse(stop1.getRoutes().contains(null));
    }
    @Test
    public void getRoutes() {
        stop1.addRoute(busRoute);
        busRoute.addStop(stop);
        Assert.assertEquals(stop.getRoutes().get(0), busRoute);
    }
    @Test
    public void TestAddNeighbouringStop() {
        stop.addNeighbouringStop(null);
        Assert.assertEquals(0, stop.getNeighbours().size());
        stop.addNeighbouringStop(stop1);
        Assert.assertTrue(stop.getNeighbours().contains(stop1));
        stop.addNeighbouringStop(stop1);
        Assert.assertEquals(1, stop.getNeighbours().size());
        stop.addNeighbouringStop(stop2);
        Assert.assertEquals(2, stop.getNeighbours().size());
    }
    @Test
    public void getNeighbours() {
        stop.addNeighbouringStop(null);
        Assert.assertEquals(0, stop.getNeighbours().size());
        stop.addNeighbouringStop(stop1);
        Assert.assertTrue(stop.getNeighbours().contains(stop1));
        stop.addNeighbouringStop(stop1);
        Assert.assertEquals(1, stop.getNeighbours().size());
        stop.addNeighbouringStop(stop2);
        Assert.assertEquals(2, stop.getNeighbours().size());
        Assert.assertEquals(stop2, stop.getNeighbours().get(1));

    }
    @Test
    public void addPassenger() {
        stop.addPassenger(passenger);
        stop.addPassenger(nullPassenger);
        Assert.assertEquals(passenger, stop.getWaitingPassengers().get(0));
        Assert.assertFalse(stop.getWaitingPassengers().contains(null));
    }
    @Test
    public void getWaitingPassengers() {
        stop.addPassenger(passenger);
        stop.addPassenger(null);
        Assert.assertEquals(passenger, stop.getWaitingPassengers().get(0));
        Assert.assertEquals(1, stop.getWaitingPassengers().size());
        Assert.assertFalse(stop.getWaitingPassengers().contains(null));
    }
    @Test
    public void isAtStop() {
        Assert.assertFalse(stop.isAtStop(transport));
        stop.transportArrive(transport);
        Assert.assertTrue(stop.isAtStop(transport));
    }
    @Test
    public void getVehicles() {
        stop.transportArrive(transport);
        Assert.assertEquals(stop.getVehicles().get(0), transport);
    }
    @Test
    public void testTransportArrive() {
        stop.transportArrive(transport);
        Assert.assertEquals(1, stop.getVehicles().size());
        Assert.assertEquals(1, stop.getWaitingPassengers().size());
        stop.transportArrive(transport1);
        Assert.assertEquals(2, stop.getVehicles().size());
        Assert.assertTrue(stop.isAtStop(transport));
        Assert.assertTrue(stop.isAtStop(transport1));
    }

    @Test
    public void TestTransportDepart() {
        stop.transportArrive(transport);
        Assert.assertTrue(stop.isAtStop(transport));
        stop.transportDepart(transport, stop1);
        Assert.assertTrue(!stop.isAtStop(transport));
        Assert.assertTrue(stop1.isAtStop(transport));
        //Assert.assertEquals(transport.getCurrentStop(),stop1);
    }

    @Test
    public void distanceTo() {
        Assert.assertEquals(-1, stop.distanceTo(null));
        Assert.assertEquals(112,stop.distanceTo(stop1));
    }
    @Test
    public void equals() {
        Stop sameStop = new Stop("st\nop\r", 0, 255);
        Assert.assertTrue(stop.getName().equals(sameStop.getName())&&stop.getX()==sameStop.getX()&&stop.getY()==sameStop.getY());
        Assert.assertFalse(stop.getName().equals(stop1.getName())&&stop.getX()==stop1.getX()&&stop.getY()==stop1.getY());
        Assert.assertFalse(stop.getName().equals(stop2.getName())&&stop.getX()==stop2.getX()&&stop.getY()==stop2.getY());
    }
    @Test
    public void TestHashCode(){
        Assert.assertEquals(-1883498719, stop.hashCode());
    }

    @Test
    public void testToString() {
        Assert.assertEquals("stop:0:255",stop.toString());
    }

    @After
    public void tearDown() throws Exception {
    }
}