package javachallenge.client;

import javachallenge.client.teamcli.TeamClient;
import javachallenge.message.ClientMessage;
import javachallenge.message.InitialMessage;
import javachallenge.message.ServerMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Created by mohammad on 2/5/14.
 */
public class Connector {

    private static final int WAIT_TIME = 50;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    private Object lock = new Object();
    ServerMessage serverMessage = null;
    ServerMessage otherThreadMessage = null;
    Client client;

    public Connector(String server, int port) throws IOException, ClassNotFoundException {
        Socket socket = new Socket(server, port);
        in = new ObjectInputStream(socket.getInputStream());
        out = new ObjectOutputStream(socket.getOutputStream());

        InitialMessage initialMessage = (InitialMessage) in.readObject();
        client = new TeamClient();

        new Thread() {
            @Override
            public void run() {
                try {
                    while (true) {
                        ServerMessage tmp = (ServerMessage) in.readObject();
                        synchronized (lock) {
                            serverMessage = tmp;
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }.run();

        new Thread() {
            @Override
            public void run() {
                while (true) {
                    try {
                        synchronized (lock) {
                            otherThreadMessage = serverMessage;
                        }
                        if (otherThreadMessage != null) {
                            synchronized (lock) {
                                serverMessage = null;
                            }
                            client.update(otherThreadMessage);
                            client.step();
                            ClientMessage message = client.end();
                            out.writeObject(message);
                        }
                        else {
                            Thread.sleep(WAIT_TIME);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.run();
    }

    public static void main(String[] args) {
        try {
            new Connector("127.0.0.1", 20140);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
