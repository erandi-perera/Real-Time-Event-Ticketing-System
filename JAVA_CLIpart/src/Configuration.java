import java.io.*;
import java.util.Scanner;

public class Configuration {
    private int totalTickets;
    private int ticketReleaseRate;
    private int customerRetrievalRate;
    private int maxTicketCapacity;

    private static final String CONFIG_FILE = "config.txt";

    public void configureSystem(Scanner scanner) {
        if (loadConfiguration()) {
            System.out.println("Previous configuration loaded successfully:");
            displayConfiguration();
            System.out.print("Do you want to modify the configuration? (yes/no): ");
            String choice = scanner.nextLine().trim().toLowerCase();
            if (!choice.equals("yes")) {
                System.out.println("Using existing configuration.");
                return;
            }
        }

        System.out.println("System Configuration:");

        // Configure total tickets
        this.totalTickets = promptForInt(scanner, "Enter total tickets: ", 1, Integer.MAX_VALUE);

        // Configure ticket release rate
        this.ticketReleaseRate = promptForInt(scanner, "Enter ticket release rate: ",1, 60000);

        // Configure customer retrieval rate
        this.customerRetrievalRate = promptForInt(scanner, "Enter customer retrieval rate: ", 1, 60000);

        // Configure max ticket capacity (ensure maxTicketCapacity >= totalTickets)
        do {
            this.maxTicketCapacity = promptForInt(scanner, "Enter max ticket capacity: ", totalTickets, Integer.MAX_VALUE);
            if (this.maxTicketCapacity < totalTickets) {
                System.out.println("Max ticket capacity must be equal to or greater than the total tickets.");
            }
        } while (this.maxTicketCapacity < totalTickets);

        System.out.println("Configuration complete!");
        saveConfiguration();
    }

    public int getTotalTickets() {
        return totalTickets;
    }

    public int getTicketReleaseRate() {
        return ticketReleaseRate;
    }

    public int getCustomerRetrievalRate() {
        return customerRetrievalRate;
    }

    public int getMaxTicketCapacity() {
        return maxTicketCapacity;
    }

    public void setTotalTickets(int totalTickets) {
        this.totalTickets = totalTickets;
    }

    public void setTicketReleaseRate(int ticketReleaseRate) {
        this.ticketReleaseRate = ticketReleaseRate;
    }

    public void setCustomerRetrievalRate(int customerRetrievalRate) {
        this.customerRetrievalRate = customerRetrievalRate;
    }

    public void setMaxTicketCapacity(int maxTicketCapacity) {
        this.maxTicketCapacity = maxTicketCapacity;
    }

    // Prompt the user for an integer within a specified range
    private int promptForInt(Scanner scanner, String message, int min, int max) {
        int value;
        while (true) {
            System.out.print(message);
            try {
                value = Integer.parseInt(scanner.nextLine());
                if (value >= min && value <= max) {
                    break;
                } else {
                    System.out.println("Value must be between " + min + " and " + max + ".");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a number.");
            }
        }
        return value;
    }

    // Save configuration to a file
    private void saveConfiguration() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CONFIG_FILE))) {
            writer.write(totalTickets + "\n");
            writer.write(ticketReleaseRate + "\n");
            writer.write(customerRetrievalRate + "\n");
            writer.write(maxTicketCapacity + "\n");

        } catch (IOException e) {
            System.err.println("Error saving configuration: " + e.getMessage());
        }
    }

    // Load configuration from a file
    private boolean loadConfiguration() {
        File file = new File(CONFIG_FILE);
        if (!file.exists()) {
            return false; // No existing configuration
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            totalTickets = Integer.parseInt(reader.readLine());
            ticketReleaseRate = Integer.parseInt(reader.readLine());
            customerRetrievalRate = Integer.parseInt(reader.readLine());
            maxTicketCapacity = Integer.parseInt(reader.readLine());
            return true; // Configuration loaded successfully
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error loading configuration: " + e.getMessage());
            return false; // Failed to load configuration
        }
    }

    // Display the current configuration
    private void displayConfiguration() {
        System.out.println("Total Tickets: " + totalTickets);
        System.out.println("Ticket Release Rate: " + ticketReleaseRate);
        System.out.println("Customer Retrieval Rate: " + customerRetrievalRate);
        System.out.println("Max Ticket Capacity: " + maxTicketCapacity);
    }


}
