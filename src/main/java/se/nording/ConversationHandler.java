package se.nording;

import java.io.*;
import java.net.Socket;

public class ConversationHandler extends Thread {
    private final Socket socket;
    private String name;
    private ChatLogger chatLogger;

    public ConversationHandler(Socket socket, ChatLogger chatLogger) throws IOException {
        this.socket = socket;
        this.chatLogger = chatLogger;
    }

    public void run() {
        try (socket;
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            int count = 0;
            while (true) {
                out.println(count > 0 ? "NAMEALREADYEXISTS" : "NAMEREQUIRED");

                name = in.readLine();
                if (name == null) {
                    return;
                }
                if (!ChatServer.userNames.contains(name)) {
                    ChatServer.userNames.add(name);
                    break;
                }
                count++;
            }
            out.println("NAMEACCEPTED" + name);
            ChatServer.printWriters.add(out);

            while (true) {
                String message = in.readLine();
                if (message == null) {
                    return;
                }
                chatLogger.logMessage(name + ": " + message);

                for (PrintWriter writer : ChatServer.printWriters) {
                    writer.println(name + ": " + message);
                }
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
