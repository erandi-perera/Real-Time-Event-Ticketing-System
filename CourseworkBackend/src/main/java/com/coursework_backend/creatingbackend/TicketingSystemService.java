package com.coursework_backend.creatingbackend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TicketingSystemService {

    private static final String CONFIG_FILE = "configuration.txt";

    @Autowired
    public static Configuration configuration;
    public boolean systemRunning;
    private static List<Thread> vendorThreads = new ArrayList<>();
    private static List<Thread> customerThreads = new ArrayList<>();

    @Autowired
    public LogController logController;

    public static Configuration loadOrPromptConfiguration() {
        Configuration config = null;
        try {
            // Try to load configuration from file
            config = Configuration.loadFromFile(CONFIG_FILE);
        } catch (IOException e) {
            throw new RuntimeException("No configuration settings found.");
        }
        return config;
    }

    public void startOperations() {
        if(!systemRunning) {
            if (vendorThreads.isEmpty() && customerThreads.isEmpty()) {
                // Load configuration from file
                configuration = loadOrPromptConfiguration();

                // Create a TicketPool with the maximum capacity from the configuration
                TicketPool ticketPool = new TicketPool(configuration.getMaxTicketCapacity());
                logController.addLog("Setting up ticket pool.");
                startTicketOperations(ticketPool);
            } else {
                resumeOperations();
            }
        }
        else{
            throw new IllegalStateException("Operations already running.");
        }
    }

    private void resumeOperations(){
        Vendor.setVendorRunning(true);
        Customer.setCustomerRunning(true);
        systemRunning = true;
    }

    private void startTicketOperations(TicketPool ticketPool) {
        // Create and start vendor threads
        for (int i = 0; i < 5; i++) {
            Vendor vendor = new Vendor(configuration, ticketPool, logController);
            Thread vendorThread = new Thread(vendor, "Vendor-" + (i + 1));
            logController.addLog("Created Vendor - "+ (i + 1));
            vendorThread.start();
            vendorThreads.add(vendorThread);
        }

        // Create and start customer threads
        for (int i = 0; i < 5; i++) {
            Customer customer = new Customer(configuration, ticketPool, logController);
            Thread customerThread = new Thread(customer, "Customer-" + (i + 1));
            logController.addLog("Created Customer - "+ (i + 1));
            customerThread.start();
            customerThreads.add(customerThread);
        }
        Vendor.setVendorRunning(true);
        Customer.setCustomerRunning(true);
        systemRunning = true;
    }

    public void stopOperations() {
        if(systemRunning) {
            Vendor.setVendorRunning(false);
            Customer.setCustomerRunning(false);
            systemRunning = false;
        }
        else{
            throw new IllegalStateException("Operations already stopped.");
        }
    }

    public void resetOperations() {
        if (systemRunning) {
            throw new IllegalStateException("Cannot reset operations when the operations are running.");
        }
        else {
            vendorThreads.clear();
            customerThreads.clear();
            configuration = loadOrPromptConfiguration();
        }
    }

    // Start a VIP customer
    public void startVipCustomer(String customerName) {
        if (!systemRunning) {
            throw new IllegalStateException("Cannot start VIP customer when the system is not running.");
        }

        int vipInterval = configuration.getCustomerRetrievalRate();
        System.out.println("VIP Customer " + customerName + " started with interval " + vipInterval + " ms.");
        new Thread(() -> {
            while (systemRunning) {
                try {
                    System.out.println("VIP Customer " + customerName + " retrieved a ticket.");
                    Thread.sleep(vipInterval); // Adjust VIP customer retrieval interval
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.out.println("VIP Customer " + customerName + " stopped.");
                }
            }
        }).start();
    }

    // Start a regular customer
    public void startRegularCustomer(String customerName) {
        if (!systemRunning) {

            throw new IllegalStateException("Cannot start regular customer when the system is not running.");
        }

        int regularInterval = configuration.getCustomerRetrievalRate();
        System.out.println("Regular Customer " + customerName + " started with interval " + regularInterval + " ms.");
        new Thread(() -> {
            while (systemRunning) {
                try {
                    System.out.println("Regular Customer " + customerName + " retrieved a ticket.");
                    Thread.sleep(regularInterval); // Adjust regular customer retrieval interval
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.out.println("Regular Customer " + customerName + " stopped.");
                }
            }
        }).start();
    }

    public Object ticketStatus() {
        Map<String, Object> dynamicResponse = new HashMap<>(); // Create a map to hold the dynamic object

        dynamicResponse.put("totalTickets", configuration.getTotalTickets());
        dynamicResponse.put("remainingTickets", configuration.getRemainingTickets());
        return dynamicResponse;
    }

    public void updateConfiguration(Configuration newConfig) {
        Configuration con = new Configuration(newConfig.getTotalTickets(), newConfig.getTicketReleaseRate(),
                newConfig.getCustomerRetrievalRate(), newConfig.getMaxTicketCapacity(), newConfig.getTotalTickets());
        try {
            con.saveToFile(CONFIG_FILE);
            configuration = loadOrPromptConfiguration();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Object getConfiguration(){
        Map<String, Object> dynamicResponse = new HashMap<>(); // Create a map to hold the dynamic object

        dynamicResponse.put("totalTickets", configuration.getTotalTickets());
        dynamicResponse.put("ticketReleaseRate", configuration.getTicketReleaseRate());
        dynamicResponse.put("customerRetrievalRate", configuration.getCustomerRetrievalRate());
        dynamicResponse.put("maxTicketCapacity", configuration.getMaxTicketCapacity());
        return dynamicResponse;
    }
}

