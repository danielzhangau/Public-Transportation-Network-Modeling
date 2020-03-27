package stops;


import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class RoutingTableTest {

  private Stop initialStop;
  private Stop stop1;
  private Stop stop2;
  private Stop stop3;
  private List<Stop> stops;
  private List<RoutingEntry> routingEntries;
  private RoutingTable routingTable;

  @Before
  public void setUp() throws Exception {
    initialStop = new Stop("st\nop\r", 0, 255);
    stop1 = new Stop("stop1", 1, 366);
    stop2 = new Stop("stop2", 2, 477);
    stop3 = new Stop("stop3", 3, 588);
    routingEntries = new ArrayList<>();
    stops = new ArrayList<>();
    stops.add(initialStop);
    routingEntries.add(new RoutingEntry(initialStop, 0));
    routingTable = new RoutingTable(initialStop);
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void addNeighbour() {
    routingTable.addNeighbour(initialStop);
    Assert.assertEquals(initialStop, routingTable.getStop());
    routingTable.addNeighbour(stop1);
    Assert.assertEquals(stop1, routingTable.getStop());
  }

  @Test
  public void addOrUpdateEntry() {
    boolean result = routingTable.addOrUpdateEntry
        (initialStop, initialStop.distanceTo(stop1), stop1);
    Assert.assertFalse(result);
    boolean result1 = routingTable.addOrUpdateEntry
        (stop1, stop1.distanceTo(stop2), stop3);
    Assert.assertTrue(result1);
  }

  @Test
  public void costTo() {
    int cost = routingTable.costTo(stop3);
    Assert.assertEquals(Integer.MAX_VALUE, cost);
    stops.add(stop1);
    routingTable.addOrUpdateEntry(stop1, 1, initialStop);
    int number = stops.indexOf(stop1);
    int cost1 = routingTable.costTo(stop1);
    int cost2 = routingEntries.get(number).getCost();
    Assert.assertEquals(cost1, cost2);
    Assert.assertEquals(Integer.MAX_VALUE, cost);
  }

  @Test
  public void getCosts() {
  }

  @Test
  public void getStop() {
    Assert.assertEquals(initialStop, routingTable.getStop());
  }

  @Test
  public void nextStop() {
    stops.add(stop1);
    stops.add(stop2);
    int number = stops.indexOf(stop3);
    Assert.assertNull(routingTable.nextStop(stop3));
    Assert.assertNull(routingTable.nextStop(null));
    Assert.assertEquals(stop3, routingEntries.get(number).getNext());
  }

  @Test
  public void transferEntries() {
    stop2.addNeighbouringStop(stop3);
    routingTable.addOrUpdateEntry(stop2, stop1.distanceTo(stop2), stop1);
    boolean result = routingTable.transferEntries(stop3);
    Assert.assertTrue(result);
  }

  @Test
  public void traverseNetwork() {
    List<Stop> stopList = routingTable.traverseNetwork();
    stop2.addNeighbouringStop(stop3);
    routingTable.addOrUpdateEntry(stop2, stop1.distanceTo(stop2), stop1);

    Assert.assertEquals(stopList.size(), 1);
  }
}