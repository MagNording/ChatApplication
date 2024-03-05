package se.nording;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;

public class ChatLogger {
    private PrintWriter logWriter;

    public ChatLogger(String logFile) {
        try {
            this.logWriter = new PrintWriter(new FileOutputStream(logFile, true));
        } catch (FileNotFoundException e) {
            System.err.println("Loggfilen kunde inte skapas: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public synchronized void logMessage(String message) {
        if (logWriter != null) {
            logWriter.println(message);
            logWriter.flush();
        }
    }
}
