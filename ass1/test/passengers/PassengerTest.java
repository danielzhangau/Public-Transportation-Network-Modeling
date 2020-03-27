package passengers;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import stops.Stop;

import static org.junit.Assert.*;
/**
 * Test class of Passenger
 * @author Daniel Zhang
 */
public class PassengerTest {
    private Passenger passengerNull;
    private Passenger passenger;
    private Stop destination;
    private Stop newDestination;

    @Before
    public void setUp() throws Exception {
        passengerNull = new Passenger(null);
        destination = new Stop("testDestination", 0, 0);
        passenger = new Passenger("Aga\rtha\n", destination);
        newDestination = new Stop("testNewDestination", 123, 123);
    }
    @Test
    public void getName() {
        Assert.assertEquals("", passengerNull.getName());
        Assert.assertEquals("Agatha", passenger.getName());
    }
    @Test
    public void setDestination() {
        passenger.setDestination(newDestination);
        Assert.assertEquals(newDestination, passenger.getDestination());
    }
    @Test
    public void getDestination() {
        Assert.assertEquals(destination, passenger.getDestination());
        passenger.setDestination(null);
        Assert.assertNull(passenger.getDestination());
    }
    @Test
    public void testToString() {
        Assert.assertEquals("Anonymous passenger", passengerNull.toString());
        Assert.assertEquals(String.format("passengers named %s", "Agatha"), passenger.toString());
    }
    @After
    public void tearDown() throws Exception {
    }
}