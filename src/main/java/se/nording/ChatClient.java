package se.nording;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ChatClient {

    private final JFrame chatWindow = new JFrame("Chat Application");
    private final JTextArea chatArea = new JTextArea(22, 40);
    private final JTextField textField = new JTextField(40);
    private BufferedReader in;
    private PrintWriter out;
    private final JLabel nameLabel = new JLabel("        ");
    
    private final ActionListener sendAction = e -> {
        String msg = textField.getText().trim();
        if (!msg.isEmpty()) {
            out.println(msg);
            textField.setText("");
        }
    };
    
    ChatClient() {
        chatWindow.setLayout(new FlowLayout());

        chatWindow.add(nameLabel);
        chatWindow.add(new JScrollPane(chatArea));
        JLabel blankLabel = new JLabel("            ");
        chatWindow.add(blankLabel);
        chatWindow.add(textField);
        JButton sendButton = new JButton("Send");
        chatWindow.add(sendButton);

        chatWindow.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
        chatWindow.setSize(475, 500);
        chatWindow.setVisible(true);

        textField.setEditable(false);
        chatArea.setEditable(false);

        sendButton.addActionListener(sendAction);
        textField.addActionListener(sendAction);
    }

    // Startchat
    void startChat() throws IOException {
        String ipAddress = JOptionPane.showInputDialog(chatWindow, "Enter IP Address:",
                "IP Address Required!", JOptionPane.PLAIN_MESSAGE);

        Socket soc = new Socket(ipAddress, 9806);
        in = new BufferedReader(new InputStreamReader(soc.getInputStream(), StandardCharsets.UTF_8));
        
        out = new PrintWriter(
                new OutputStreamWriter(soc.getOutputStream(), StandardCharsets.UTF_8), true);

        while (true) {
            String str = in.readLine();
            if (str == null) {
                break;
            }
            if (str.equals("NAMEREQUIRED")) {
                String name = JOptionPane.showInputDialog(chatWindow, "Enter a unique name: ",
                        "Name Required!", JOptionPane.PLAIN_MESSAGE);
                out.println(name);

            } else if (str.equals("NAMEALREADYEXISTS")) {
                String name = JOptionPane.showInputDialog(chatWindow, "Enter another name: ",
                        "Name Already Exists!", JOptionPane.WARNING_MESSAGE);
                out.println(name);

            } else if (str.startsWith("NAMEACCEPTED")) {  // Använder startsWith() här
                textField.setEditable(true);
                nameLabel.setText("You are logged in as: " + str.substring(12));

            } else {
                chatArea.append(str + "\n");
            }
        }
    }
}
