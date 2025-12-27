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

    /
