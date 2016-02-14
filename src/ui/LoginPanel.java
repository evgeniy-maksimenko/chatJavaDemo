package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginPanel extends JDialog{
    public boolean isResultOk = false;
    JTextField input_login;

    public LoginPanel(){
        setLayout(null);
        setBounds(200,200,400,200);

        JPanel myPanel = new JPanel();
        myPanel.setLayout(null);
        myPanel.setBounds(0,0,400,200);

        JLabel label_login = new JLabel("login:");
        label_login.setBounds(20,20,70,20);
        myPanel.add(label_login);
        input_login = new JTextField();
        input_login.setBounds(70,20,70,20);
        myPanel.add(input_login);

        JButton okButton = new JButton("Ok");
        okButton.setBounds(220,20,100,20);
        okButton.addActionListener(new okActionButton());
        myPanel.add(okButton);


        add(myPanel);
        setModal(true);
    }

    public void showDialog() {
        setVisible(true);
    }

    public String getLogin() {
        return input_login.getText();
    }

    private class okActionButton implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            isResultOk = true;
            setVisible(false);
        }
    }
}
