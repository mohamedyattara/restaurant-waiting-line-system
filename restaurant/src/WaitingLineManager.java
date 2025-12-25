import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class WaitingLineManager {
    private PriorityQueue<Customer> vipQueue;
    private PriorityQueue<Customer> regularQueue;
    private static final long PROMOTION_TIME_MS = 30 * 60;
    private List<Table> tables;

    public WaitingLineManager() {
        // VIP: FIFO using arrival time
        this.vipQueue = new PriorityQueue<>(
                Comparator.comparingLong(Customer::getArrivalTime));
        // Regular: FIFO using arrival time
        this.regularQueue = new PriorityQueue<>(
                Comparator.comparingLong(Customer::getArrivalTime));
        this.tables = new ArrayList<>();
    }

    public void addCustomer(Customer c) {

        Priority p = c.getPriority();
        if (p == Priority.VIP) {
            this.vipQueue.add(c);
        } else {
            this.regularQueue.add(c);
        }
    }

    public Customer removeNextCustomer() {
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

            if (now - c.getArrivalTime() < PROMOTION_TIME_MS) {
                break;
            }

            this.regularQueue.poll();
            c.SetPriority(Priority.VIP);
            this.vipQueue.add(c);
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

    public int seatNextCustomer() {
        this.promoteWaitingCustomers();

        Customer next = null;
        Table table = null;

        if (!this.vipQueue.isEmpty()) {
            next = this.vipQueue.peek();
        } else if (!this.regularQueue.isEmpty()) {
            next = this.regularQueue.peek();
        }
        int tableId = -1;

        if (next != null) {
            table = this.findBestAvailableTable(next.getPartySize());

            if (table != null) {
                table.occupy();
                tableId = table.getTableId();

                if (next.getPriority() == Priority.VIP) {
                    this.vipQueue.poll();
                } else {
                    this.regularQueue.poll();
                }
            } else {
                next = null;
            }
        }

        return tableId;
    }

    public void releaseTable(int tableId) {
        for (Table t : this.tables) {
            if (t.getTableId() == tableId) {
                t.release();

            }
        }
    }

}
