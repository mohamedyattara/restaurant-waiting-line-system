import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class WaitingLineManager {
    private PriorityQueue<Customer> vipQueue;
    private PriorityQueue<Customer> regularQueue;
    private static final long PROMOTION_TIME_MS = 30 * 60 * 1000;
    private List<Table> tables;
    private Map<String, Integer> seatedCustomers;

    public WaitingLineManager() {
        // VIP: FIFO using arrival time
        this.vipQueue = new PriorityQueue<>(
                Comparator.comparingLong(Customer::getArrivalTime));
        // Regular: FIFO using arrival time
        this.regularQueue = new PriorityQueue<>(
                Comparator.comparingLong(Customer::getArrivalTime));
        this.tables = new ArrayList<>();
        this.seatedCustomers = new HashMap<>();

    }

    public void addCustomer(Customer c) {

        Priority p = c.getPriority();
        if (p == Priority.VIP) {
            this.vipQueue.add(c);
        } else {
            this.regularQueue.add(c);
        }
    }

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

    public void promoteWaitingCustomers() {
        long now = System.currentTimeMillis();

        while (!this.regularQueue.isEmpty()) {
            Customer c = this.regularQueue.peek();
            if (this.vipQueue.isEmpty() || now - c.getArrivalTime() > PROMOTION_TIME_MS) {
                this.regularQueue.poll();
                c.SetPriority(Priority.VIP);
                this.vipQueue.add(c);
            } else {
                break;
            }

        }
    }

    public List<Customer> getQueueState() {
        List<Customer> x = new ArrayList<>();
        List<Customer> state = new ArrayList<>();
        state.addAll(this.vipQueue);
        state.addAll(this.regularQueue);
        return state;
    }

    public void addTable(Table table) {
        if (table == null) {
            return;
        }

        for (Table t : this.tables) {
            if (t.getTableId() == table.getTableId()) {
                throw new IllegalArgumentException("Duplicate table ID");
            }
        }

        this.tables.add(table);
    }

    private Table findBestAvailableTable(int partySize) {
        Table best = null;
        for (Table table : this.tables) {
            if (!table.isOccupied() && table.getCapacity() == partySize) {
                if (best == null) {
                    best = table;
                }
            }
        }

        return best;
    }

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

    public int seatNextCustomer() {
        Customer next = this.getNextCustomer();
        int tableId = -1;

        if (next != null) {
            Table table = this.findBestAvailableTable(next.getPartySize());
            if (table != null) {
                table.occupy();
                tableId = table.getTableId();

                if (next.getPriority() == Priority.VIP) {
                    this.vipQueue.poll();
                } else {
                    this.regularQueue.poll();
                }
                this.seatedCustomers.put(next.getName(), tableId);

            }
        }

        return tableId;
    }

    public Map<String, Integer> getSeatedCustomers() {
        return this.seatedCustomers;
    }

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

    public void releaseTable(int tableId) {
        for (Table t : this.tables) {
            if (t.getTableId() == tableId) {
                t.release();

            }
        }
    }

}
