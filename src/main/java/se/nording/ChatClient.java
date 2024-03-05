package se.nording;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ChatClient {

    static JFrame chatWindow = new JFrame("Chat Application");
    static JTextArea chatArea = new JTextArea(22, 40);
    static JTextField textField = new JTextField(40);
    static JLabel blankLabel = new JLabel("            ");
    static JButton sendButton = new JButton("Send");
    static BufferedReader in;
    static PrintWriter out;
    static JLabel nameLabel = new JLabel("        ");

    ChatClient() {
        chatWindow.setLayout(new FlowLayout());

        chatWindow.add(nameLabel);
        chatWindow.add(new JScrollPane(chatArea));
        chatWindow.add(blankLabel);
        chatWindow.add(textField);
        chatWindow.add(sendButton);

        chatWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        chatWindow.setSize(475, 500);
        chatWindow.setVisible(true);

        textField.setEditable(false);
        chatArea.setEditable(false);

        sendButton.addActionListener(new Listener());
        textField.addActionListener(new Listener());
    }

    // Startchat
    void startChat() throws IOException {
        String ipAddress = JOptionPane.showInputDialog(
                chatWindow,
                "Enter IP Address:",
                "IP Address Required!",
                JOptionPane.PLAIN_MESSAGE);
        Socket soc = new Socket(ipAddress, 9806);
        in = new BufferedReader(new InputStreamReader(soc.getInputStream()));
        out = new PrintWriter(soc.getOutputStream(), true);

        while (true) {
            String str = in.readLine();
            if (str == null) {
                break;
            }
            if (str.equals("NAMEREQUIRED")) {
                String name = JOptionPane.showInputDialog(
                        chatWindow,
                        "Enter a unique name: ",
                        "Name Required!",
                        JOptionPane.PLAIN_MESSAGE);
                out.println(name);
            } else if (str.equals("NAMEALREADYEXISTS")) {
                String name = JOptionPane.showInputDialog(
                        chatWindow,
                        "Enter another name: ",
                        "Name Already Exists!",
                        JOptionPane.WARNING_MESSAGE);
                out.println(name);
            } else if (str.startsWith("NAMEACCEPTED")) {  // Använder startsWith() här
                textField.setEditable(true);
                nameLabel.setText("You are logged in as: " + str.substring(12));
            } else {
                chatArea.append(str + "\n");
            }

        }
    }

    public static void main(String[] args) throws IOException {
        ChatClient client = new ChatClient();
        client.startChat();
    }
}
