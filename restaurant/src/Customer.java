/**
 * Represents a customer waiting or seated in the restaurant.
 */
public class Customer {

    // Unique identifier for the customer
    private final int id;

    // Customer name
    private final String name;

    // Number of people in the customer's party
    private final int partySize;

    // Time when the customer arrived (milliseconds)
    private final long arrivalTime;

    // Customer priority (VIP or REGULAR)
    private Priority priority;

    // Table number where the customer is seated (-1 means not seated)
    private int seatedTable = -1;

    /**
     * Constructs a new Customer.
     *
     * @param id        unique customer ID
     * @param name      customer name
     * @param partySize number of people in the party
     * @param priority  customer priority
     */
    public Customer(int id, String name, int partySize, Priority priority) {
        this.id = id;
        this.name = name;
        this.partySize = partySize;
        this.priority = priority;
        this.arrivalTime = System.currentTimeMillis();
    }

    /**
     * Returns the customer's name.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Returns the time the customer arrived.
     */
    public long getArrivalTime() {
        return this.arrivalTime;
    }

    /**
     * Returns the customer's priority.
     */
    public Priority getPriority() {
        return this.priority;
    }

    /**
     * Updates the customer's priority.
     */
    public void SetPriority(Priority p) {
        this.priority = p;
    }

    /**
     * Returns the size of the customer's party.
     */
    public int getPartySize() {
        return this.partySize;
    }

    /**
     * Sets the table where the customer is seated.
     */
    public void setSeatedTable(int tableId) {
        this.seatedTable = tableId;
    }

    /**
     * Returns the table number where the customer is seated.
     */
    public int getSeatedTable() {
        return this.seatedTable;
    }
}
