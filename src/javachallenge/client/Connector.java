package javachallenge.client;

import javachallenge.client.teamcli.TeamClient;
import javachallenge.message.ClientMessage;
import javachallenge.message.InitialMessage;
import javachallenge.message.ServerMessage;
import javachallenge.util.Cell;
import javachallenge.util.CellType;

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
        client = new TeamClient(0, 1000, new Cell(5, 5, CellType.SPAWN), new Cell(1, 1, CellType.DESTINATION));
        client.map = initialMessage.getMap();

        new Thread() {
            @Override
            public void run() {
                try {
                    while (true) {
                        ServerMessage tmp = (ServerMessage) in.readObject();
                        System.out.println("data recieved from server");
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
        }.start();

        new Thread() {
            @Override
            public void run() {
                try {
                    while (true) {
                        synchronized (lock) {
                            otherThreadMessage = serverMessage;
                        }
                        if (otherThreadMessage != null) {
                            synchronized (lock) {
                                serverMessage = null;
                            }
                            client.init();
                            client.update(otherThreadMessage);
                            client.step();
                            ClientMessage message = client.end();
                            System.out.println("Writing object to server ...");
                            out.writeObject(message);
                        }
                        else {
                            Thread.sleep(WAIT_TIME);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
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
