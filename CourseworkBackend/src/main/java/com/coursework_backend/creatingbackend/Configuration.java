package com.coursework_backend.creatingbackend;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Configuration {   //Configuration class defined with the variables
    private int totalTickets;
    private int ticketReleaseRate;
    private int customerRetrievalRate;
    private int maxTicketCapacity;
    private int remainingTickets;

    public Configuration(int totalTickets, int ticketReleaseRate, int customerRetrievalRate, int maxTicketCapacity,
                         int remainingTickets) {  // Constructor for the configuration
        this.totalTickets = totalTickets;
        this.ticketReleaseRate = ticketReleaseRate;
        this.customerRetrievalRate = customerRetrievalRate;
        this.maxTicketCapacity = maxTicketCapacity;
        this.remainingTickets = totalTickets;
    }

    public int getTotalTickets() {   //Getter for the total tickets
        return totalTickets;
    }

    public void setTotalTickets(int totalTickets) {
        this.totalTickets = totalTickets;
    }

    public int getTicketReleaseRate() {  //Getter for the ticket release rate
        return ticketReleaseRate;
    }

    public void setTicketReleaseRate(int ticketReleaseRate) {
        this.ticketReleaseRate = ticketReleaseRate;
    }

    public int getCustomerRetrievalRate() { //Getter for the Customer retrieval rate
        return customerRetrievalRate;
    }

    public void setCustomerRetrievalRate(int customerRetrievalRate) {
        this.customerRetrievalRate = customerRetrievalRate;
    }

    public int getMaxTicketCapacity() { //Getter for the maximum ticket capacity
        return maxTicketCapacity;
    }

    public void setMaxTicketCapacity(int maxTicketCapacity) {
        this.maxTicketCapacity = maxTicketCapacity;
    }

    public int getRemainingTickets() { //Getter for the maximum ticket capacity
        return remainingTickets;
    }

    public void setRemainingTickets(int remainingTickets) {
        this.remainingTickets = remainingTickets;
    }

    // Save configuration information to a file
    public void saveToFile(String fileName) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write("totalTickets=" + totalTickets);
            writer.newLine();
            writer.write("ticketReleaseRate=" + ticketReleaseRate);
            writer.newLine();
            writer.write("customerRetrievalRate=" + customerRetrievalRate);
            writer.newLine();
            writer.write("maxTicketCapacity=" + maxTicketCapacity);
        }
    }

    // Load configuration from a file
    public static Configuration loadFromFile(String fileName) throws IOException {
        // Initialize variables to default invalid values
        int totalTickets = -1, ticketReleaseRate = -1, customerRetrievalRate = -1, maxTicketCapacity = -1;


        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line; // To hold each line read from the file

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("="); // Split the line into key-value pairs
                if (parts.length == 2) {          // Ensure the line has exactly two parts
                    String key = parts[0].trim();       // Trim whitespace from the key
                    int value; // To hold the parsed value

                    // Try to parse the value as an integer
                    try {
                        value = Integer.parseInt(parts[1].trim());
                    } catch (NumberFormatException e) {
                        throw new IOException("Invalid number format in configuration file: " + line);
                    }

                    // Match the key to the corresponding configuration variable
                    switch (key) {
                        case "totalTickets":
                            totalTickets = value;
                            break;
                        case "ticketReleaseRate":
                            ticketReleaseRate = value;
                            break;
                        case "customerRetrievalRate":
                            customerRetrievalRate = value;
                            break;
                        case "maxTicketCapacity":
                            maxTicketCapacity = value;
                            break;
                        default:

                            throw new IOException("Unknown configuration key: " + key);
                    }
                } else {
                    throw new IOException("Invalid configuration line: " + line);
                }
            }
        }

        // Validate that all configuration values have been properly set
        if (totalTickets < 0 || ticketReleaseRate < 0 || customerRetrievalRate < 0 || maxTicketCapacity < 0) {
            throw new IOException("Configuration file is missing required values or contains invalid data.");
        }

        // Return a new Configuration object with the loaded values
        return new Configuration(totalTickets, ticketReleaseRate, customerRetrievalRate, maxTicketCapacity, 0);
    }
}
