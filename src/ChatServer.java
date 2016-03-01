import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.Timer;

public class ChatServer {
    Object broadcast = new Object();
    CopyOnWriteArrayList<NetClient> clients = new CopyOnWriteArrayList<NetClient>();

    void sendMsgToAll(NetClient nc) {
        try {
            String str = nc.in.readUTF();
            System.out.println("[Trace] in: " + str);

            String cmd = str.substring(0, str.indexOf(":"));
            String inf = str.substring(str.indexOf(":") + 1);
            String msg = "";

            switch (cmd) {
                case "Login":
                    msg = "Connected user " + inf;
                    nc.login = inf;
                    break;
                case "Msg":
                    msg = "Msg from " + nc.login + " => " + inf;
                    break;
                case "Exit":
                    msg = "CLient " + nc.login + " disconnected";
                    break;
            }

            System.out.println("[Trace] msg: " + msg);
            for (NetClient nn : clients) {
                if (nc != nn)
                    nn.out.writeUTF(msg);
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public ChatServer() throws IOException {
        ServerSocket ss = new ServerSocket(7777);


        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    synchronized (broadcast) {
                        try {
                            for (NetClient nc : clients) {
                                if (nc.in.available() > 0) {
                                    sendMsgToAll(nc);
                                }
                            }
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                        try {
                            broadcast.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).start();


        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    synchronized (broadcast) {
                        broadcast.notifyAll();
                    }
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        System.out.println("Server stated");

        new Thread(new AddNewConnect(ss)).start();
    }

    class AddNewConnect implements Runnable {
        ServerSocket ss ;
        public AddNewConnect(ServerSocket ss) {
            this.ss = ss;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    Socket cs = ss.accept();
                    NetClient nc = new NetClient(cs);
                    clients.add(nc);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }


    class NetClient {
        Socket cs = null;
        DataOutputStream out = null;
        DataInputStream in = null;
        public String login;

        public NetClient(Socket cs) throws IOException {
            this.cs = cs;
            out = new DataOutputStream(cs.getOutputStream());
            in = new DataInputStream(cs.getInputStream());
        }
    }
}
