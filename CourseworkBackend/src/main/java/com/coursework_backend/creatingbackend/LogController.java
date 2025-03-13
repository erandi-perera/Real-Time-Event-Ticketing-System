package com.coursework_backend.creatingbackend;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.MediaType;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@RestController
public class LogController {

    private final List<LogEntry> logs = new ArrayList<>();

    public void addLog(String message) {
        String dateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        logs.add(new LogEntry(dateTime, message));
    }

    // Method to send log updates
    @GetMapping(value = "/logs", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<List<LogEntry>> streamLogs() {
        return Flux.interval(Duration.ofSeconds(1)) // Emit every second
                .map(sequence -> new ArrayList<>(logs)); // Return a copy of the logs
    }

    // Method to clear logs
    @GetMapping("/logs/clear")
    public ResponseEntity<Response> clearLogs() {
        try {
            logs.clear();
            Response status = new Response("Logs cleared successfully!", true, null);
            return ResponseEntity.status(200).body(status);
        } catch (Exception e) {
            // Handle and return an error response
            Response status = new Response("Error clearing logs: " + e.getMessage(), false, null);
            return ResponseEntity.status(500).body(status);
        }
    }
}

