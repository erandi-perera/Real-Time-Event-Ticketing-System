package com.coursework_backend.creatingbackend;

public class Vendor implements Runnable {
    private final TicketPool ticketPool;
    private final Configuration config;
    private static boolean isVendorRunning = true;
    private static final Object lock = new Object(); // Lock object for synchronization
    private LogController logController;

    public Vendor(Configuration config, TicketPool ticketPool, LogController logController) { // Vendor constructor
        this.ticketPool = ticketPool;
        this.config = config;
        this.logController = logController;
    }

    @Override
    public void run() { // Run method is executed when thread starts
        while (true) {
            synchronized (lock) {
                while (!isVendorRunning) {
                    try {
                        log(Thread.currentThread().getName() + " is paused.");
                        lock.wait(); // Wait until notified
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        log("Vendor thread interrupted: " + Thread.currentThread().getName());
                        logController.addLog("Vendor thread interrupted: " + Thread.currentThread().getName());
                        return;
                    }
                }
            }

            // Try to add a ticket to the pool
            if (!ticketPool.addTicket()) {
                log("Cannot add ticket. Pool has reached maximum capacity.");
                break; // Stop if the pool is full
            }
            log("A ticket has been added by " + Thread.currentThread().getName());
            logController.addLog("A ticket has been added by " + Thread.currentThread().getName());

            try {
                Thread.sleep(TicketingSystemService.configuration.getTicketReleaseRate());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log("Vendor thread interrupted: " + Thread.currentThread().getName());
                logController.addLog("Vendor thread interrupted: " + Thread.currentThread().getName());
                break;
            }
        }
        log("Vendor thread stopped: " + Thread.currentThread().getName());
        logController.addLog("Vendor thread stopped: " + Thread.currentThread().getName());
    }

    public static void setVendorRunning(boolean running) { // Method to update the running flag
        synchronized (lock) {
            isVendorRunning = running;
            if (running) {
                lock.notifyAll(); // Notify all paused threads to resume
            }
        }
    }

    private void log(String message) { // Method to log messages
        System.out.println(message);
    }
}
