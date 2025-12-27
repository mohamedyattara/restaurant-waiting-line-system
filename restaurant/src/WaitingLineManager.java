import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * Manages the restaurant waiting line, seating logic,
 * customer promotion, and table assignments.
 */
public class WaitingLineManager {

    // Queue for VIP customers (ordered by arrival time)
    private PriorityQueue<Customer> vipQueue;

    // Queue for regular customers (ordered by arrival time)
    private PriorityQueue<Customer> regularQueue;

    // Time after which a regular customer is promoted to VIP (30 minutes)
    private static final long PROMOTION_TIME_MS = 30 * 60 * 1000;

    // List of tables in the restaurant
    private List<Table> tables;

    // Map of seated customers and their table numbers
    private Map<String, Integer> seatedCustomers;

    /**
     * Constructs a WaitingLineManager and initializes all data structures.
     */
    public WaitingLineManager() {

        // VIP customers are served first, FIFO by arrival time
        this.vipQueue = new PriorityQueue<>(
                Comparator.comparingLong(Customer::getArrivalTime));

        // Regular customers are FIFO by arrival time
        this.regularQueue = new PriorityQueue<>(
                Comparator.comparingLong(Customer::getArrivalTime));

        this.tables = new ArrayList<>();
        this.seatedCustomers = new HashMap<>();
    }

    /**
     * Adds a customer to the appropriate queue based on priority.
     */
    public void addCustomer(Customer c) {
        Priority p = c.getPriority();

        if (p == Priority.VIP) {
            this.vipQueue.add(c);
        } else {
            this.regularQueue.add(c);
        }
    }

    /**
     * Removes and returns the next customer to be seated.
     */
    public Customer removeCustomer() {
        this.promoteWaitingCustomers();
        Customer c;

        if (!this.vipQueue.isEmpty()) {
            c = this.vipQueue.poll();
        } else {
            c = this.regularQueue.poll();
        }
        return c;
    }

    /**
     * Promotes regular customers to VIP if they have waited too long.
     */
    public void promoteWaitingCustomers() {
        long now = System.currentTimeMillis();

        while (!this.regularQueue.isEmpty()) {
            Customer c = this.regularQueue.peek();

            if (this.vipQueue.isEmpty()
                    || now - c.getArrivalTime() > PROMOTION_TIME_MS) {
                this.regularQueue.poll();
                c.SetPriority(Priority.VIP);
                this.vipQueue.add(c);
            } else {
                break;
            }
        }
    }

    /**
     * Returns a list of all waiting customers.
     */
    public List<Customer> getQueueState() {
        List<Customer> state = new ArrayList<>();
        state.addAll(this.vipQueue);
        state.addAll(this.regularQueue);
        return state;
    }

    /**
     * Adds a table to the restaurant.
     */
    public void addTable(Table table) {
        if (table == null) {
            return;
        }

        // Prevent duplicate table IDs
        for (Table t : this.tables) {
            if (t.getTableId() == table.getTableId()) {
                throw new IllegalArgumentException("Duplicate table ID");
            }
        }

        this.tables.add(table);
    }

    /**
     * Finds the first available table that can fit the party size.
     */
    private Table findBestAvailableTable(int partySize) {
        Table best = null;

        for (Table table : this.tables) {
            if (!table.isOccupied() && table.getCapacity() >= partySize) {
                if (best == null) {
                    best = table;
                }
            }
        }
        return best;
    }

    /**
     * Returns the next customer to be seated without removing them.
     */
    public Customer getNextCustomer() {
        this.promoteWaitingCustomers();
        Customer result = null;

        if (!this.vipQueue.isEmpty()) {
            result = this.vipQueue.peek();
        } else if (!this.regularQueue.isEmpty()) {
            result = this.regularQueue.peek();
        }
        return result;
    }

    /**
     * Seats the next customer at an available table.
     *
     * @return table ID or -1 if seating was not possible
     */
    public int seatNextCustomer() {
        Customer next = this.getNextCustomer();
        int tableId = -1;

        if (next != null) {
            Table table = this.findBestAvailableTable(next.getPartySize());

            if (table != null) {
                table.occupy();
                tableId = table.getTableId();

                // Remove customer from the correct queue
                if (next.getPriority() == Priority.VIP) {
                    this.vipQueue.poll();
                } else {
                    this.regularQueue.poll();
                }

                // Track seated customer
                this.seatedCustomers.put(next.getName(), tableId);
            }
        }
        return tableId;
    }

    /**
     * Returns the map of seated customers.
     */
    public Map<String, Integer> getSeatedCustomers() {
        return this.seatedCustomers;
    }
    /**
     * Releases a table by table ID.
     */
    public void releaseTable(int tableId) {
        for (Table t : this.tables) {
            if (t.getTableId() == tableId) {
                t.release();
            }
        }
    }

    /**
     * Checks out a customer and frees their table.
     */
    public boolean checkoutCustomer(String name) {
        if (this.seatedCustomers.containsKey(name)) {
            int tableId = this.seatedCustomers.get(name);
            this.releaseTable(tableId);
            this.seatedCustomers.remove(name);
            return true;
        } else {
            return false;
        }
    }

    
}

