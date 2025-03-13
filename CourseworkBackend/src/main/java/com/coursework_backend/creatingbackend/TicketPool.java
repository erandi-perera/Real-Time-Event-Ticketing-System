package com.coursework_backend.creatingbackend;

public class TicketPool {  //Class for the ticketpool
    private final int maxCapacity;
    private int ticketCount;


    public TicketPool(int maxCapacity) {  //Constructor to the ticketpool
        this.maxCapacity = maxCapacity;
        this.ticketCount = 0; // Initially, the pool is empty
    }

    // Synchronized method to add a ticket to the pool
    public synchronized boolean addTicket() {
        if (ticketCount < maxCapacity) {
            ticketCount++;
            TicketingSystemService.configuration.setRemainingTickets(TicketingSystemService.configuration.getTotalTickets() - ticketCount);
            log("Ticket added to the pool. Current ticket count: " + ticketCount); // Log message to the addition
            return true;
        } else {
            log("Cannot add ticket. Pool has reached maximum capacity.");
            return false;
        }
    }

    // Synchronized method to remove a ticket from the pool
    public synchronized boolean removeTicket() {
        if (ticketCount > 0) {  // Check if there are tickets available to remove
            ticketCount--;
            log("Ticket removed from the pool. Current ticket count: " + ticketCount); // Log the removal
            return true;
        } else {
            log("No tickets available to remove.");
            return false;
        }
    }

    // Method to check if the pool is empty
    public boolean isEmpty() {
        return ticketCount == 0;
    }

    // method to log messages
    private void log(String message) {
        System.out.println(message);
    }
}