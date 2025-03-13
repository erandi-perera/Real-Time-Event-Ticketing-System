package com.coursework_backend.creatingbackend;

public class LogEntry {    //Class to handle the log entries
    private String dateTime;
    private String message;

    public LogEntry(String dateTime, String message) {
        this.dateTime = dateTime;
        this.message = message;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

