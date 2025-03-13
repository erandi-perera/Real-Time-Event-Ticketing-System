package com.coursework_backend.creatingbackend;

public class Customer implements Runnable {
    private final Configuration config;
    private final TicketPool ticketPool;
    public static boolean isCustomerRunning = true; // Shared flag to control thread execution
    private static final Object lock = new Object(); // Lock object for synchronization
    private LogController logController;

    public Customer(Configuration config, TicketPool ticketPool, LogController logController) { // Constructor for the Customer class
        this.config = config;
        this.ticketPool = ticketPool;
        this.logController = logController;
    }

    @Override
    public void run() { // Run method executes when thread starts
        while (true) {
            synchronized (lock) {
                while (!isCustomerRunning) { // Pause if the flag is false
                    try {
                        log(Thread.currentThread().getName() + " is paused.");
                        lock.wait(); // Wait until notified
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        log("Customer thread interrupted: " + Thread.currentThread().getName());
                        logController.addLog("Error:Customer thread interrupted: " + Thread.currentThread().getName());
                        return;
                    }
                }
            }

            // Perform ticket purchase
            synchronized (ticketPool) {
                if (!ticketPool.isEmpty()) { // Check if tickets are available in the pool
                    ticketPool.removeTicket();
                    log("A ticket has been purchased by Customer: " + Thread.currentThread().getName());
                    logController.addLog("A ticket has been purchased by Customer: " + Thread.currentThread().getName());
                } else {
                    log("No tickets available for Customer: " + Thread.currentThread().getName());
                    logController.addLog("Error:No tickets available for Customer: " + Thread.currentThread().getName());
                }
            }

            try {
                Thread.sleep(TicketingSystemService.configuration.getCustomerRetrievalRate());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log("Customer thread interrupted: " + Thread.currentThread().getName());
                logController.addLog("Error:Customer thread interrupted: " + Thread.currentThread().getName());
                break;
            }
        }
        log("Customer thread stopped: " + Thread.currentThread().getName());
        logController.addLog("Error:Customer thread stopped: " + Thread.currentThread().getName());
    }

    public static void setCustomerRunning(boolean running) { // Method to update the running flag
        synchronized (lock) {
            isCustomerRunning = running;
            if (running) {
                lock.notifyAll(); // Notify all paused threads to resume
            }
        }
    }

    private void log(String message) { // Log method to display messages
        System.out.println(message);
    }
}

