package se.nording;

import java.io.*;
import java.net.Socket;

public class ConversationHandler extends Thread {
    final Socket socket;
    String name;
    PrintWriter pw;

    public ConversationHandler(Socket socket) throws IOException {
        this.socket = socket;
        FileWriter fw = new FileWriter("logs/ChatServer-Logs.txt", true);
        BufferedWriter bw = new BufferedWriter(fw);
        pw = new PrintWriter(bw, true);
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
                pw.println(name + ": " + message);

                for (PrintWriter writer : ChatServer.printWriters) {
                    writer.println(name + ": " + message);
                }
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
