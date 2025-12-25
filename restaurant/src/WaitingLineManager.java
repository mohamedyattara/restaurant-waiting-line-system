import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class WaitingLineManager {
    private PriorityQueue<Customer> vipQueue;
    private PriorityQueue<Customer> regularQueue;
    private static final long PROMOTION_TIME_MS = 30 * 60;

    public WaitingLineManager() {
        // VIP: FIFO using arrival time
        this.vipQueue = new PriorityQueue<>(
                Comparator.comparingLong(Customer::getArrivalTime));
        // Regular: FIFO using arrival time
        this.regularQueue = new PriorityQueue<>(
                Comparator.comparingLong(Customer::getArrivalTime));
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
}
