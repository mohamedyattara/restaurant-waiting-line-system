import java.util.Map;
import java.util.Scanner;

/**
 * Put a short phrase describing the program here.
 *
 * @author Put your name here
 *
 */
public final class Restaurant {

    /**
     * No argument constructor--private to prevent instantiation.
     */
    private Restaurant() {
    }

    /**
     * Put a short phrase describing the static method myMethod here.
     */
    private static void myMethod() {
        /*
         * Put your code for myMethod here
         */
    }

    /**
     * Main method.
     *
     * @param args
     *            the command line arguments
     */
    public static void main(String[] args) {
        WaitingLineManager manager = new WaitingLineManager();

        manager.addTable(new Table(1, 2));
        manager.addTable(new Table(2, 3));
        manager.addTable(new Table(3, 2));
        manager.addTable(new Table(4, 5));

        Scanner scanner = new Scanner(System.in);
        int nextCustomerId = 0;

        System.out.println("=== Restaurant Waiting Line System ===");

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
                    // Add customer
                    System.out.print("Enter customer name: ");
                    String name = scanner.nextLine();

                    int partySize = 0;
                    boolean validInput = false;

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

                    System.out.print("Is this a VIP customer? (yes/no): ");
                    String vipInput = scanner.nextLine();
                    Priority priority;
                    if (vipInput.equals("yes")) {
                        priority = Priority.VIP;
                    } else {
                        priority = Priority.REGULAR;
                    }
                    Customer customer = new Customer(nextCustomerId++, name, partySize,
                            priority);
                    manager.addCustomer(customer);

                    System.out.println("Customer added: " + name + " (" + priority + ")");
                    break;

                case "2":
                    // Seat next customer
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
                    // Release a table
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
                    // Show remaining queue
                    System.out.println("Remaining waiting customers:");
                    for (Customer c : manager.getQueueState()) {
                        System.out.println(c.getName() + " (" + c.getPriority()
                                + ", Party of " + c.getPartySize() + ")");
                    }
                    break;

                case "5":
                    System.out.println("Currently seated customers:");
                    for (Map.Entry<String, Integer> entry : manager.getSeatedCustomers()
                            .entrySet()) {
                        System.out.println(
                                entry.getKey() + " is at table " + entry.getValue());
                    }
                    break;

                case "0":
                    System.out.println("Exiting...");
                    scanner.close();
                    return;

                default:
                    System.out.println("Invalid option, try again.");
            }
        }
    }

}
