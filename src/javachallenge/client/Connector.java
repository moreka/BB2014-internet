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
                    synchronized (lock) {
                        otherThreadMessage = serverMessage;
                    }
                    client.update(otherThreadMessage);
                    client.step();
                    ClientMessage message = client.end();
                    try {
                        out.writeObject(message);
                    } catch (IOException e) {
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
