

public class Table {
    private final int tableId;
    private final int capacity;
    private boolean occupied;

    public Table(int tableId, int capacity) {
        this.tableId = tableId;
        this.capacity = capacity;
        this.occupied = false;
    }
}
