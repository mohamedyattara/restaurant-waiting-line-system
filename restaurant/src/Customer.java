
public class Customer {
    private final int id;
    private final String name;
    private final int partySize;
    private final long arrivalTime;
    private Priority priority;

    public Customer(int id, String name, int partySize, Priority priority) {
        this.id = id;
        this.name = name;
        this.partySize = partySize;
        this.priority = priority;
        this.arrivalTime = System.currentTimeMillis();
    }

    public String getName() {
        return this.name;
    }

    public long getArrivalTime() {
        return this.arrivalTime;
    }

    public Priority getPriority() {
        return this.priority;
    }

    public void SetPriority(Priority p) {
        this.priority = p;
    }

    public int getPartySize() {
        return this.partySize;
    }

}
