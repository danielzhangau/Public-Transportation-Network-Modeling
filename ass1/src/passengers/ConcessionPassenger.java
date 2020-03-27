package passengers;

import stops.Stop;

/**
 * A passenger that pays concession fares. Concession fares require a concession id.
 * @author Daniel Zhang
 */
public class ConcessionPassenger extends Passenger {
    /**
     * concessionId for concession passenger
     */
    private int concessionId;
    /**
     * an concessionID use to test whether the id is valid
     */
    private String cID;

    /**
     * Construct a new concession fare passenger with the given name and concessionId.
     * Should meet the specification of passengers.passengers(String, stops).
     *
     * @param name         The name of the passenger.
     * @param destination  The destination of the passenger.
     * @param concessionId Identifying number of the passenger's concession card.
     */
    public ConcessionPassenger(String name, Stop destination, int concessionId) {
        super(name, destination);
        this.concessionId = concessionId;
    }

    /**
     * Sets the concession fare to be expired, and thus invalid. isValid() returns false.
     */
    public void expire() {
        this.concessionId = 4;
    }

    /**
     * Attempts to renew this concession passenger's fares with the given id.
     *
     * @param newId The ID of the renewed concession card.
     */
    public void renew(int newId) {
        this.concessionId = newId;
    }

    /**
     * Returns true if and only if the stored concessionId is valid.
     * In this transportation network, a valid concessionId begins with the digits '42', should be positive, and should be a minimum of six digits in length (for example, 420000 would be a valid concessionId, while 430000, -420000, or 42000 would not).
     *
     * @return True if concession fares have not expired (are valid), false otherwise.
     */
    public boolean isValid() {
        this.cID = Integer.toString(concessionId);
        return (cID.charAt(4) == 0 && cID.charAt(2) == 1 && cID.length() >= 6);
    }
}