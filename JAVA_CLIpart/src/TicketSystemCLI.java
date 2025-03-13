import java.io.*;
import java.util.Scanner;

public class TicketSystemCLI {
    private static final String CONFIGURATION_FILE = "configuration.txt";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Configuration configuration = loadConfiguration(scanner);
        TicketPool ticketPool = new TicketPool(configuration);


        System.out.println("Welcome to the Real-Time Ticketing System CLI!");

        boolean isOperationsRunning = false;

        while (true) {
            // If operations are running, don't show the main menu again
            if (!isOperationsRunning) {


                System.out.println("\nMain Menu:");
                System.out.println("1. Configure System");
                System.out.println("2. Start Ticketing Operations");
                System.out.println("3. Stop Ticketing Operations");
                System.out.println("4. Exit");
                System.out.print("Enter your choice: ");

                int choice;
                try {
                    choice = Integer.parseInt(scanner.nextLine());
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input! Please enter a number.");
                    continue;
                }

                switch (choice) {
                    case 1:
                        configuration.configureSystem(scanner);
                        saveConfiguration(configuration); // Save the configuration after modifying
                        break;
                    case 2:
                        ticketPool.startOperations();// Start multiple vendor and customer threads
                        isOperationsRunning = true;  // Set flag to indicate operations have started
                        break;
                    case 3:
                        ticketPool.stopOperations();  // Stop all threads safely
                        isOperationsRunning = false;  // Set flag to indicate operations have stopped
                        break;
                    case 4:
                        System.out.println("Exiting the system. Goodbye!");
                        scanner.close();
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Invalid choice! Please select a valid option.");
                }
            }
            else {

                try {
                    Thread.sleep(1000); // Sleep for a second before checking again
                } catch (InterruptedException e) {
                    System.err.println("Error while waiting: " + e.getMessage());
                }
            }
        }
    }

    // Load previous configuration if available
    private static Configuration loadConfiguration(Scanner scanner) {
        File configFile = new File(CONFIGURATION_FILE);
        Configuration configuration = new Configuration();

        if (configFile.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(configFile))) {
                configuration.setTotalTickets(Integer.parseInt(reader.readLine()));
                configuration.setTicketReleaseRate(Integer.parseInt(reader.readLine()));
                configuration.setCustomerRetrievalRate(Integer.parseInt(reader.readLine()));
                configuration.setMaxTicketCapacity(Integer.parseInt(reader.readLine()));
                System.out.println("Configuration loaded successfully.");
            } catch (IOException | NumberFormatException e) {
                System.out.println("Error loading configuration: " + e.getMessage());
            }
        } else {
            System.out.println("No previous configuration found. Proceeding with new configuration.");
        }

        return configuration;
    }

    // Save current configuration to a file
    private static void saveConfiguration(Configuration configuration) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CONFIGURATION_FILE))) {
            writer.write(configuration.getTotalTickets() + "\n");
            writer.write(configuration.getTicketReleaseRate() + "\n");
            writer.write(configuration.getCustomerRetrievalRate() + "\n");
            writer.write(configuration.getMaxTicketCapacity() + "\n");
            System.out.println("Configuration saved successfully.");
        } catch (IOException e) {
            System.err.println("Error saving configuration: " + e.getMessage());
        }
    }
}
