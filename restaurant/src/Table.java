
public class Table {
    private final int tableId;
    private final int capacity;
    private boolean occupied;

    public Table(int tableId, int capacity) {
        this.tableId = tableId;
        this.capacity = capacity;
        this.occupied = false;
    }

    public int getCapacity() {
        return this.capacity;
    }

    public boolean isOccupied() {
        return this.occupied;
    }

    public void occupy() {
        this.occupied = true;
    }

    public void release() {
        this.occupied = false;
    }

    public int getTableId() {
        return this.tableId;
    }
}
