package com.coursework_backend.creatingbackend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/ticket") // Base URL for all endpoints in this controller
public class TicketController {

    @Autowired
    private TicketingSystemService ticketingSystemService; // Service for ticketing system operations
    @Autowired
    private LogController logController;

    @GetMapping("/configuration")
    public ResponseEntity<Response> getConfiguration() {
        try {
            Object data = ticketingSystemService.getConfiguration();
            Response status = new Response("Loaded configurations successfully.", true, data);
            logController.addLog("Loaded configurations successfully.");
            return ResponseEntity.status(200).body(status);
        } catch (Exception e) {
            // Handle and return an error response
            Response status = new Response("Error getting configuration: " + e.getMessage(), false, null);
            logController.addLog("Error getting configuration: " + e.getMessage());
            return ResponseEntity.status(500).body(status);
        }
    }

    // Endpoint to start ticketing operations
    @GetMapping("/start-operations")
    public ResponseEntity<Response> startOperations() {
        try {
            logController.addLog("Starting operation.");
            ticketingSystemService.startOperations(); // Start operations using the service
            Response status = new Response("Operations started successfully", true, null);
            logController.addLog("Operations started successfully.");
            return ResponseEntity.status(200).body(status);
        } catch (Exception e) {
            Response status = new Response("Error starting operations: " + e.getMessage(), false, null);
            logController.addLog("Error starting operations: " + e.getMessage());
            return ResponseEntity.status(500).body(status);
        }
    }

    // Endpoint to stop ticketing operations
    @GetMapping("/stop-operations")
    public ResponseEntity<Response> stopOperations() {
        try {
            ticketingSystemService.stopOperations(); // Stop operations using the service
            Response status = new Response("Operations stopped successfully", true, null);
            logController.addLog("Operations stopped successfully");
            return ResponseEntity.status(200).body(status);
        } catch (Exception e) {
            Response status = new Response("Error stopping operations: " + e.getMessage(), false, null);
            logController.addLog("Error stopping operations: " + e.getMessage());
            return ResponseEntity.status(500).body(status);
        }
    }

    // Endpoint to reset ticketing operations
    @GetMapping("/reset-operations")
    public ResponseEntity<Response> resetOperations() {
        try {
            ticketingSystemService.resetOperations(); // Reset operations using the service
            Response status = new Response("Operations reset successfully", true, null);
            logController.addLog("Operations reset successfully");
            return ResponseEntity.status(200).body(status);
        } catch (Exception e) {
            Response status = new Response("Error resetting operations: " + e.getMessage(), false, null);
            logController.addLog("Error resetting operations: " + e.getMessage());
            return ResponseEntity.status(500).body(status);
        }
    }

    // Endpoint to update configuration settings
    @PutMapping("/update-configuration")
    public ResponseEntity<Response> updateConfiguration(@RequestBody Configuration config) {
        try {
            ticketingSystemService.updateConfiguration(config);
            Response status = new Response("Configuration saved successfully.", true, null);
            logController.addLog("Configuration saved successfully.");
            return ResponseEntity.status(200).body(status);
        } catch (Exception e) {
            Response status = new Response("Error updating configuration: " + e.getMessage(), false, null);
            logController.addLog("Error updating configuration: " + e.getMessage());
            return ResponseEntity.status(500).body(status);
        }
    }

    // Endpoint to get the ticketing system status
    @GetMapping("/status")
    public ResponseEntity<Response> ticketStatus() {
        try {
            Object data = ticketingSystemService.ticketStatus(); // Fetch the system status from the service
            Response status = new Response("Configuration saved successfully.", true, data);
            return ResponseEntity.status(200).body(status);
        } catch (Exception e) {
            Response status = new Response("Error retrieving status: " + e.getMessage(), false, null);
            return ResponseEntity.status(500).body(status);
        }
    }
}

