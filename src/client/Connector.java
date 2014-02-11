package javachallenge.client;

import javachallenge.client.teamcli.TeamClient;
import javachallenge.message.InitialMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Created by mohammad on 2/5/14.
 */
public class Connector {

    public Connector(String server, int port) throws IOException, ClassNotFoundException {
        Socket socket = new Socket(server, port);
        ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());

        InitialMessage initialMessage = (InitialMessage) in.readObject();

        Client client = new TeamClient();

        while (true) {
            // get server message
            // update map
            client.step();
            // send client.actionLis
        }
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
