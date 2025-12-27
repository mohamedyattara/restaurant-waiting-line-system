/**
 * Represents a table in the restaurant.
 */
public class Table {

    // Unique identifier for the table
    private final int tableId;

    // Maximum number of people the table can seat
    private final int capacity;

    // Indicates whether the table is currently occupied
    private boolean occupied;

    /**
     * Constructs a new table.
     *
     * @param tableId  table number
     * @param capacity number of seats at the table
     */
    public Table(int tableId, int capacity) {
        this.tableId = tableId;
        this.capacity = capacity;
        this.occupied = false;
    }

    /**
     * Returns the table capacity.
     */
    public int getCapacity() {
        return this.capacity;
    }

    /**
     * Returns true if the table is occupied.
     */
    public boolean isOccupied() {
        return this.occupied;
    }

    /**
     * Marks the table as occupied.
     */
    public void occupy() {
        this.occupied = true;
    }

    /**
     * Releases the table (marks it as available).
     */
    public void release() {
        this.occupied = false;
    }

    /**
     * Returns the table ID.
     */
    public int getTableId() {
        return this.tableId;
    }
}
