import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Customer implements Runnable {
    private final TicketPool ticketPool;
    private final String customerName;
    private static final String LOG_FILE = "ticketing_log.txt";

    public Customer(TicketPool ticketPool, String customerName) {
        this.ticketPool = ticketPool;
        this.customerName = customerName;
    }

    @Override
    public void run() {
        Configuration config = ticketPool.getConfiguration();
        while (ticketPool.isRunning()) {
            try {
                // Calculate the number of tickets the customer will purchase
                int ticketsToRemove = 2;
                int retrievalRate = config.getCustomerRetrievalRate();

                if (ticketsToRemove > 0) {
                    // Remove tickets from the pool (simulate ticket purchase)
                    ticketPool.removeTickets(ticketsToRemove);
                    System.out.println(customerName + " Purchased " + ticketsToRemove + " tickets. Remaining tickets: " + ticketPool.getCurrentTickets());
                    // Log the customer ticket purchase to a file
                    logTicketTransaction(customerName + " Purchased " + ticketsToRemove + " tickets. Remaining tickets: " + ticketPool.getCurrentTickets());
                }

                // Simulate delay between customer purchases
                Thread.sleep(retrievalRate); // Simulating delay

            } catch (InterruptedException e) {
                System.out.println(customerName + "interrupted: " + e.getMessage());
            }
        }
    }

    // Log ticket transactions to a file
    private synchronized void logTicketTransaction(String message) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOG_FILE, true))) {
            writer.write(message + "\n");
        } catch (IOException e) {
            System.err.println("Error writing to log file: " + e.getMessage());
        }
    }
}
