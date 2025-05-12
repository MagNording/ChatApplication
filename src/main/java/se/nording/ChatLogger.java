package se.nording;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ChatLogger {
    private final PrintWriter logWriter;
    private final DateTimeFormatter timestamp = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public ChatLogger(String logFile) {
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(new OutputStreamWriter(
                    new FileOutputStream(logFile, true), StandardCharsets.UTF_8), true);
            
        } catch (IOException e) {
            System.err.println("Kunde inte öppna loggfilen: " + e.getMessage());
            e.printStackTrace();
        }
        this.logWriter = writer;
    }

    public synchronized void logMessage(String message) {
        if (logWriter != null) {
            logWriter.println("[" + LocalDateTime.now().format(timestamp) + "] " + message);
        }
    }
    
    public synchronized void logError(Exception e) {
        if (logWriter != null) {
            logWriter.println("[" + LocalDateTime.now().format(timestamp) + "] ERROR: " + e.getMessage());
            e.printStackTrace(logWriter);
        }
    }
}
