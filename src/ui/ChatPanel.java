package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;


public class ChatPanel extends JFrame {
    DataOutputStream out = null;
    DataInputStream in = null;
    String login;
    JTextField input_msg;
    JTextArea text;

    public ChatPanel(String login) {
        this.login = login;
        setTitle("Chat Panel");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBounds(200, 200, 400, 400);

        JPanel myPanel = new JPanel();
        myPanel.setLayout(null);
        myPanel.setBounds(0, 0, 400, 400);

        text = new JTextArea("", 5, 5);
        JScrollPane scroll = new JScrollPane(text, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setBounds(5, 5, 370, 100);
        myPanel.add(scroll);


        JLabel label_msg = new JLabel("msg:");
        label_msg.setBounds(20, 120, 70, 20);
        myPanel.add(label_msg);
        input_msg = new JTextField();
        input_msg.setBounds(70, 120, 150, 20);
        myPanel.add(input_msg);

        JButton okButton = new JButton("Ok");
        okButton.setBounds(250, 120, 100, 20);
        okButton.addActionListener(new okActionButton());
        myPanel.add(okButton);

        add(myPanel);

        try {
            Socket cs = new Socket("localhost", 7777);
            out = new DataOutputStream(cs.getOutputStream());
            in = new DataInputStream(cs.getInputStream());


            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        actionPerformed();
                    }
                }
            }).start();

            out.writeUTF("Login: " + login);


        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public String getMsg() {
        return input_msg.getText();
    }

    private class okActionButton implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                out.writeUTF("Msg: " + getMsg() + "\n");
                text.append("Me: " + getMsg() + "\n");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    public void showDialog() {
        setVisible(true);
    }


    public void actionPerformed() {
        try {
            if (in.available() > 0) {
                String str = in.readUTF();
                text.append(str);
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }


}
