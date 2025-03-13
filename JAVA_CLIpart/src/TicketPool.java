import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;

public class TicketPool {
    private final Configuration configuration;
    private volatile int currentTickets = 0;
    private boolean isRunning = false;

    private final ReentrantLock lock = new ReentrantLock();
    private final Condition notEmpty = lock.newCondition();
    private final Condition notFull = lock.newCondition();

    private List<Thread> vendorThreads = new ArrayList<>();
    private List<Thread> customerThreads = new ArrayList<>();

    private static final String TICKET_POOL_FILE = "ticketPool.txt";

    public TicketPool(Configuration configuration) {
        this.configuration = configuration;
        loadTicketPool(); // Load previous ticket data if available
    }

    public void startOperations() {
        if (isRunning) {
            System.out.println("System is already running!");
            return;
        }
        isRunning = true;

        // Initialize current tickets with totalTickets from configuration
        currentTickets = configuration.getTotalTickets();
        System.out.println("Initial tickets added: " + currentTickets);

        // Create and start multiple vendor threads
        for (int i = 0; i < 3; i++) {  // Example: Start 3 vendors
            Thread vendorThread = new Thread(new Vendor(this,"Vendor " + (i + 1)));
            vendorThreads.add(vendorThread);
            vendorThread.start();
        }

        // Create and start multiple customer threads
        for (int i = 0; i < 5; i++) {  // Example: Start 3 customers
            Thread customerThread = new Thread(new Customer(this, "Customer " + (i + 1)));
            customerThreads.add(customerThread);
            customerThread.start();
        }

        System.out.println("Ticketing operations started!");
    }

    public int getCurrentTickets() {
        return currentTickets;
    }

    private boolean isWaiting = false;

    public void addTickets(int tickets) throws InterruptedException {
        lock.lock();
        try {
            while (currentTickets + tickets > configuration.getMaxTicketCapacity()) {
                if (!isWaiting) {
                    System.out.println("Ticket pool is full. Waiting to add!");
                    isWaiting = true; // Set the flag to prevent repeated messages
                }
                notFull.await(); // Wait until space is available in the ticket pool
            }
            // If space is available, add the tickets
            currentTickets += tickets;
            notEmpty.signalAll();  // Notify customer that tickets are available for purchase
            saveTicketPool(); // Save ticket pool data after addition
        } finally {
            lock.unlock();
        }
    }

    public void stopOperations() {
        if (!isRunning) {
            System.out.println("System is not running!");
            return;
        }
        isRunning = false;

        try {
            for (Thread vendorThread : vendorThreads) {
                vendorThread.join();
            }

            for (Thread customerThread : customerThreads) {
                customerThread.join();
            }
        } catch (InterruptedException e) {
            System.out.println("Error stopping threads: " + e.getMessage());
        }

        System.out.println("Ticketing operations stopped!");
    }

    private boolean customerWaitingMessageLogged = false; // Tracks "waiting to purchase" message

    public void removeTickets(int tickets) throws InterruptedException {
        lock.lock();
        try {
            while (currentTickets < tickets) {
                if (!customerWaitingMessageLogged) {
                    System.out.println("Not enough tickets available. Waiting to purchase!");
                    customerWaitingMessageLogged = true; // Log the message only once
                }
                notEmpty.await(); // Wait until tickets are added to the pool
            }
            // Reset the flag when tickets are available
            customerWaitingMessageLogged = false;

            currentTickets -= tickets;
            notFull.signalAll(); // Notify vendor that space is available
            saveTicketPool(); // Save ticket pool data after removal
        } finally {
            lock.unlock();
        }
    }



    public boolean isRunning() {
        return isRunning;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    // Save current ticket pool data to a file
    private void saveTicketPool() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(TICKET_POOL_FILE))) {
            writer.write(currentTickets + "\n");

        } catch (IOException e) {
            System.err.println("Error saving ticket pool: " + e.getMessage());
        }
    }

    // Load ticket pool data from a file
    private void loadTicketPool() {
        File file = new File(TICKET_POOL_FILE);
        if (!file.exists()) {
            return; // No previous ticket pool data
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine();
            if (line == null || line.trim().isEmpty()) {
                System.out.println("Ticket pool file is empty. Starting with default values.");
                return; // No valid data in the file
            }

            try {
                currentTickets = Integer.parseInt(line.trim());
            } catch (NumberFormatException e) {
                System.err.println("Invalid data in ticket pool file: " + line);
                currentTickets = 0; // Set a safe default value
            }
        } catch (IOException e) {
            System.err.println("Error loading ticket pool: " + e.getMessage());
        }
    }

}

