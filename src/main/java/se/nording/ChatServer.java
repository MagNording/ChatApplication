package se.nording;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChatServer {
    
    static List<String> userNames = Collections.synchronizedList(new ArrayList<>());
    static List<PrintWriter> printWriters = Collections.synchronizedList(new ArrayList<>());
    
    public static void main(String[] args) throws IOException {

        System.out.println("Waiting for clients...");

        try (ServerSocket ss = new ServerSocket(9806))  {
            // intentional inifinite loop
            while (true) {
                Socket soc = ss.accept();
                System.out.println("Connection established");

                ChatLogger chatLogger = new ChatLogger("logs/ChatServer-Logs.txt");
                ConversationHandler handler = new ConversationHandler(soc, chatLogger);
                handler.start();
            }
        }
    }
}