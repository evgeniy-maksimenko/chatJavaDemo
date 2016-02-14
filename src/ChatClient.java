import com.sun.tools.javah.Util;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import javax.swing.Timer;

public class ChatClient {
    DataOutputStream out = null;
    DataInputStream in = null;

    public ChatClient() throws UnknownHostException, IOException {
        Socket cs = new Socket("localhost", 7777);
        out = new DataOutputStream(cs.getOutputStream());
        in = new DataInputStream(cs.getInputStream());

        Scanner sc = new Scanner(System.in);
        Timer tm = new Timer(50, new ActionRead());
        tm.start();

        System.out.println("Input login, please!");
        String str = sc.nextLine();
        out.writeUTF("Login: " + str);

        while (true) {
            str = sc.nextLine();
            out.writeUTF("Msg: " + str);

            if (str.equals("exit")) {
                out.writeUTF("Exit:");
                break;
            }

        }

    }

    class ActionRead implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                if (in.available() > 0) {
                    String str = in.readUTF();
                    System.out.println(str);
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }
}
