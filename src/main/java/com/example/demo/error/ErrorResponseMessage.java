package com.example.demo.error;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ErrorResponseMessage {

    private final int status;
    private final String message;
    private final String path;
    private final String timeStamp;

    public ErrorResponseMessage(int status, String message, String path) {
        this.status = status;
        this.message = message;
        this.path = path;
        this.timeStamp = formatDateTime(LocalDateTime.now());
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public String getPath() {
        return path;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    static private String formatDateTime(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        return dateTime.format(formatter);
    }
}
