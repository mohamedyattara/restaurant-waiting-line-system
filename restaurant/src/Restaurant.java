import java.util.Map;
import java.util.Scanner;

/**
 * Main driver class for the Restaurant Waiting Line system.
 * Handles user interaction through a console menu.
 *
 * @author Mohamed Yattara
 */
public final class Restaurant {

   /**
     * Private constructor to prevent creating objects of this class.
     * This class is only used to run the program.
     */
    private Restaurant() {
    }

   /**
     * Main method that runs the restaurant waiting line system.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        
        // Manager that handles customers, tables, and seating logic
        WaitingLineManager manager = new WaitingLineManager();

        // Add tables to the restaurant (table number, capacity)
        manager.addTable(new Table(1, 2));
        manager.addTable(new Table(2, 3));
        manager.addTable(new Table(3, 2));
        manager.addTable(new Table(4, 5));
        
        // Scanner for user input
        Scanner scanner = new Scanner(System.in);
        
        // Used to assign unique IDs to customers
        int nextCustomerId = 0;

        System.out.println("=== Restaurant Waiting Line System ===");

         // Main menu loop
        while (true) {
            System.out.println("\nOptions:");
            System.out.println("1 - Add Customer");
            System.out.println("2 - Seat Next Customer");
            System.out.println("3 - checkout");
            System.out.println("4 - Show Queue");
            System.out.println("5 - Currently seat customer");
            System.out.println("0 - Exit");

            System.out.print("Select option: ");
            String option = scanner.nextLine();

            switch (option) {
                case "1":
                     // Add a new customer to the waiting line
                    System.out.print("Enter customer name: ");
                    String name = scanner.nextLine();

                    int partySize = 0;
                    boolean validInput = false;
                   
                    // Validate party size input
                    while (!validInput) {
                        System.out.print("Enter party size: ");
                        String input = scanner.nextLine();

                        try {
                            partySize = Integer.parseInt(input);
                            if (partySize <= 0) {
                                System.out.println("Party size must be > 0.");
                            } else {
                                validInput = true;
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid number, try again.");
                        }
                    }
                    
                    // Determine customer priority
                    System.out.print("Is this a VIP customer? (yes/no): ");
                    String vipInput = scanner.nextLine();
                    Priority priority;
                    if (vipInput.equals("yes")) {
                        priority = Priority.VIP;
                    } else {
                        priority = Priority.REGULAR;
                    }
                    
                    // Create and add customer
                    Customer customer = new Customer(nextCustomerId++, name, partySize,
                            priority);
                    manager.addCustomer(customer);

                    System.out.println("Customer added: " + name + " (" + priority + ")");
                    break;

                case "2":
                   // Seat the next available customer
                    Customer next = manager.getNextCustomer();
                    int table = manager.seatNextCustomer();
                    if (table != -1 && next != null) {
                        System.out.println(
                                "Seated: " + next.getName() + " (at Table" + table + ")");
                    } else {
                        System.out.println("No customer can be seated at the moment.");
                    }
                    break;

                case "3":
                     // Checkout a seated customer
                    System.out.println("what is your Name");
                    String n = scanner.nextLine();
                    if (manager.checkoutCustomer(n)) {
                        System.out.println("Thank you for coming, " + n + "!");
                    } else {
                        System.out.println(
                                "No customer with that name is currently seated.");
                    }
                    break;

                case "4":
                    // Display customers still waiting in the queue
                    System.out.println("Remaining waiting customers:");
                    for (Customer c : manager.getQueueState()) {
                        System.out.println(c.getName() + " (" + c.getPriority()
                                + ", Party of " + c.getPartySize() + ")");
                    }
                    break;

                case "5":
                    // Display currently seated customers and their tables
                    System.out.println("Currently seated customers:");
                    for (Map.Entry<String, Integer> entry : manager.getSeatedCustomers()
                            .entrySet()) {
                        System.out.println(
                                entry.getKey() + " is at table " + entry.getValue());
                    }
                    break;

                case "0":
                    // Exit the program
                    System.out.println("Exiting...");
                    scanner.close();
                    return;

                default:
                    // Handle invalid menu options
                    System.out.println("Invalid option, try again.");
            }
        }
    }

}

