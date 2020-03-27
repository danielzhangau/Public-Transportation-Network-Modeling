package stops;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * The class should map destination stops to RoutingEntry objects.
 *
 * <p>The table is able to redirect passengers from their
 * current stop to the next intermediate stop which they
 * should go to in order to reach their final destination.
 */
public class RoutingTable {

  //all the stops in the network
  private List<Stop> stops;

  //all the entries for each route
  public List<RoutingEntry> routingEntries;


  /**
   * Creates a new RoutingTable for the given stop.
   *
   * <p>The routing table should be created with an entry for
   * its initial stop (i.e. a mapping from the stop to a
   * {@link RoutingEntry()} for that stop with a cost of zero (0).
   *
   * @param initialStop The stop for which this table will handle routing.
   */
  public RoutingTable(Stop initialStop) {
    stops = new ArrayList<>();
    stops.add(initialStop);
    this.routingEntries = new ArrayList<>();
    routingEntries.add(new RoutingEntry(initialStop, 0));
  }

  /**
   * Adds the given stop as a neighbour of the stop stored in this table.
   *
   * <p>A neighbouring stop should be added as a destination in this table,
   * with the cost to reach that destination simply being the Manhattan
   * distance between this table's stop and the given neighbour stop.
   *
   * <p>If the given neighbour already exists in the table, it should be updated
   * (as defined in {@link RoutingTable#addOrUpdateEntry(Stop, int, Stop)}).
   *
   * <p>The 'intermediate'/'next' stop between this table's stop and
   * the new neighbour stop should simply be the neighbour stop itself.
   *
   * <p>Once the new neighbour has been added as an entry, this table should
   * be synchronised with the rest of the network using the
   * {@link RoutingTable#synchronise()} method.
   *
   * @param neighbour The stop to be added as a neighbour.
   */
  public void addNeighbour(Stop neighbour) {
    for (Stop stop : stops) {
      stop.addNeighbouringStop(neighbour);
      if (addOrUpdateEntry(neighbour, stop.distanceTo(neighbour), stop)) {
        synchronise();
      }
      break;
    }
  }

  /**
   * If there is currently no entry for the destination in the table,
   * a new entry for the given destination should be added, with a RoutingEntry
   * for the given cost and next (intermediate) stop.
   *
   * <p>If there is already an entry for the given destination, and the newCost
   * is lower than the current cost associated with the destination, then the entry
   * should be updated to have the given newCost and next (intermediate) stop.
   *
   * <p>If there is already an entry for the given destination, but the newCost
   * is greater than or equal to the current cost associated with the destination,
   * then the entry should remain unchanged.
   *
   * @param destination The destination stop to add/update the entry.
   * @param newCost The new cost to associate with the new/updated entry
   * @param intermediate The new intermediate/next stop to associate with the new/updated entry
   * @return True if a new entry was added, or an existing one was updated,
   *     or false if the table remained unchanged.
   */
  public boolean addOrUpdateEntry(Stop destination, int newCost, Stop intermediate) {
    int number = stops.indexOf(destination);
    //add entry
    if (!stops.contains(destination)) {
      stops.add(destination);
      routingEntries.add(new RoutingEntry(intermediate, newCost));
      return true;
    }
    //update entry
    else if (stops.contains(destination)
        && newCost < routingEntries.get(number).getCost()) {
      routingEntries.set(number, new RoutingEntry(intermediate, newCost));
      return true;
    } else {
      return false;
    }
  }

  /**
   * Returns the cost associated with getting to the given stop.
   *
   * @param stop The stop to get the cost.
   * @return The cost to the given stop, or Integer.MAX_VALUE
   *     if the stop is not currently in this routing table.
   */
  public int costTo(Stop stop) {
    int number = stops.indexOf(stop);
    if (!stops.contains(stop)) {
      return Integer.MAX_VALUE;
    }
    return routingEntries.get(number).getCost();
  }

  /**
   * Maps each destination stop in this table to the cost
   * associated with getting to that destination.
   *
   * @return A mapping from destination stops to the costs associated with getting to those stops.
   */
  public Map<Stop, java.lang.Integer> getCosts() {
    Map<stops.Stop, Integer> mapping = new HashMap<>();
    //through every stops in the class
    for (int i = 0; i < stops.size(); i++) {
      mapping.put(stops.get(i), routingEntries.get(i).getCost()); //put(key, value)
    }
    return mapping;
  }

  /**
   * Return the stop for which this table will handle routing.
   *
   * @return the stop for which this table will handle routing.
   */
  public Stop getStop() {
    return stops.get(stops.size() - 1);
  }

  /**
   * Returns the next intermediate stop which passengers should be routed to
   * in order to reach the given destination. If the given stop is null or not
   * in the table, then return null
   *
   * @param destination The destination which the passengers are being routed.
   * @return The best stop to route the passengers to in order to reach the given destination.
   */
  public Stop nextStop(Stop destination) {
    int number = stops.indexOf(destination);
    if (stops.contains(destination)) {
      return routingEntries.get(number).getNext();
    } else {
      return null;
    }
  }

  /**
   * Synchronises this routing table with the other tables in the network.
   *
   * <p>In each iteration, every stop in the network which is reachable by this
   * table's stop (as returned by {@link RoutingTable#traverseNetwork()})
   * must be considered. For each stop x in the network, each of its
   * neighbours must be visited, and the entries from x must be transferred
   * to each neighbour (using the {@link RoutingTable#transferEntries(Stop)} method).
   *
   * <p>If any of these transfers results in a change to the table that the entries are
   * being transferred, then the entire process must be repeated again. These iterations
   * should continue happening until no changes occur to any of the tables in the network.
   *
   * <p>This process is designed to handle changes which need to be propagated
   * throughout the entire network, which could take more than one iteration.
   */
  public void synchronise() {
    // Traverse through each of the Stops in the table
    List<Stop> everyStop = this.traverseNetwork();
    boolean didChange = false;
    for (Stop stop : everyStop) {
      didChange = this.transferEntries(stop);
      // Traverse through each of the Stop's neighbour in the table
      for (Stop neighbour : stop.getNeighbours()) {
        didChange = stop.getRoutingTable().transferEntries(neighbour);
      }
    }
  }

  /**
   * Updates the entries in the routing table of the given other stop,
   * with the entries from this routing table.
   *
   * <p>If this routing table has entries which the other stop's table doesn't,
   * then the entries should be added to the other table (as defined in
   * {@link RoutingTable#addOrUpdateEntry(Stop, int, Stop))}
   * with the cost being updated to include the distance.
   *
   * <p>If this routing table has entries which the other stop's table does have,
   * and the new cost would be lower than that associated with its existing entry,
   * then its entry should be updated (as defined in
   * {@link RoutingTable#addOrUpdateEntry(Stop, int, Stop))}.
   *
   * <p>If this routing table has entries which the other stop's table does have,
   * but the new cost would be greater than or equal to that associated with its
   * existing entry, then its entry should remain unchanged.
   *
   * @param other The stop whose routing table this table's entries should be transferred.
   * @return True if any new entries were added to the other stop's table,
   * or if any of its existing entries were updated, or false
   * if the other stop's table remains unchanged.
   */
  public boolean transferEntries(Stop other) {
    boolean didChange = false;
    RoutingTable routingTable = other.getRoutingTable();
    Map<stops.Stop, Integer> mapping = routingTable.getCosts();

    if (!(getStop().getNeighbours().contains(other))) {
      return false;
    }

    for (int i = 0; i < this.stops.size(); i++) {
      Stop stop = stops.get(i);
      RoutingEntry entry = routingEntries.get(i);
      if (mapping.get(stop) == null) {
        if (addOrUpdateEntry(other, other.distanceTo(stop), stop)) {
          didChange = true;
        }
        continue;
      }
      if (mapping.get(stop) < entry.getCost()) {
        if (addOrUpdateEntry(stop, stop.distanceTo(other), other)) {
          didChange = true;
        }
      }
    }
    return didChange;
  }

  /**
   * Performs a traversal of all the stops in the network, and returns a list of
   * every stop which is reachable from the stop stored in this table.
   *  1.Firstly create an empty list of Stops and an empty Stack of Stops.
   *  Push the RoutingTable's Stop on to the stack.
   *  2.While the stack is not empty,
   *      1.pop the top Stop (current) from the stack.
   *      2.For each of that stop's neighbours,
   *           1.if they are not in the list, add them to the stack.
   *      3.Then add the current Stop to the list.
   *  3.Return the list of seen stops.
   *
   * @return All of the stops in the network which are reachable by the stop stored in this table.
   */
  public java.util.List<Stop> traverseNetwork() {
    List<Stop> stopList = new ArrayList<>();
    Stack<Stop> stopStack = new Stack<>();
    for (Stop stop : stops) {
      stopStack.push(stop);
    }
    while (!stopStack.isEmpty()) {
      Stop topStop = stopStack.pop();
      for (Stop neighbour : topStop.getNeighbours()) {
        if (!stopList.contains(neighbour)) {
          stopStack.push(neighbour);
        }
      }
      stopList.add(topStop);
    }
    return stopList;
  }
}
