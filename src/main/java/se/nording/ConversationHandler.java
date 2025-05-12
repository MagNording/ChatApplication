package se.nording;

import java.io.*;
import java.net.Socket;

public class ConversationHandler extends Thread {
    private final Socket socket;
    private final ChatLogger chatLogger;
    private String name;
    private PrintWriter out;
    
    public ConversationHandler(Socket socket, ChatLogger chatLogger) {
        this.socket = socket;
        this.chatLogger = chatLogger;
    }
    
    @Override
    public void run() {
        try (socket;
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
            
            this.out = out;
            
            requestUniqueName(in, out);
            
            ChatServer.printWriters.add(out);
            broadcast("🔔 " + name + " has joined the chat.");
            
            String message;
            while ((message = in.readLine()) != null) {
                chatLogger.logMessage(name + ": " + message);
                broadcast(name + ": " + message);
            }
            
        } catch (IOException e) {
            e.printStackTrace(); // eller logga till fil
        } finally {
            if (name != null) {
                ChatServer.userNames.remove(name);
                ChatServer.printWriters.remove(out);
                broadcast("👋 " + name + " has left the chat.");
            }
        }
    }
    
    private void requestUniqueName(BufferedReader in, PrintWriter out) throws IOException {
        int attempt = 0;
        while (true) {
            out.println(attempt > 0 ? "NAMEALREADYEXISTS" : "NAMEREQUIRED");
            String proposedName = in.readLine();
            if (proposedName == null) return;
            
            synchronized (ChatServer.userNames) {
                if (!ChatServer.userNames.contains(proposedName)) {
                    name = proposedName;
                    ChatServer.userNames.add(name);
                    out.println("NAMEACCEPTED" + name);
                    break;
                }
            }
            attempt++;
        }
    }
    
    private void broadcast(String message) {
        synchronized (ChatServer.printWriters) {
            for (PrintWriter writer : ChatServer.printWriters) {
                writer.println(message);
            }
        }
    }
}
