import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Vendor implements Runnable {
    private final TicketPool ticketPool;
    private final String vendorName;
    private static final String LOG_FILE = "ticketing_log.txt";

    public Vendor(TicketPool ticketPool, String vendorName) {
        this.ticketPool = ticketPool;
        this.vendorName = vendorName;
    }

    @Override
    public void run() {
        Configuration config = ticketPool.getConfiguration();
        while (ticketPool.isRunning()) {
            try {

                // Calculate the number of tickets to add based on the release rate
                int ticketsToAdd = 5;

                int releaseRate = config.getTicketReleaseRate(); // Milliseconds

                if (ticketsToAdd > 0) {
                    // Add tickets to the pool
                    ticketPool.addTickets(ticketsToAdd);
                    System.out.println(vendorName + " Added " + ticketsToAdd + " tickets. Current tickets: " + ticketPool.getCurrentTickets());
                    // Log ticket addition to a file
                    logTicketTransaction(vendorName + " Added " + ticketsToAdd + " tickets. Current tickets: " + ticketPool.getCurrentTickets());
                }

                // Simulate delay between adding tickets
                Thread.sleep(releaseRate); // Simulating delay

            } catch (InterruptedException e) {
                System.out.println(vendorName + "interrupted: " + e.getMessage());
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


