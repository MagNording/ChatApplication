package se.nording;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Listener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        ChatClient.out.println(ChatClient.textField.getText());
        ChatClient.textField.setText("");
    }
}
