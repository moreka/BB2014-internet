/**
 * Created by mohammad on 2/4/14.
 */

package javachallenge.server;

import javachallenge.graphics.FJframe;
import javachallenge.graphics.FJpanel;
import javachallenge.message.Delta;
import javachallenge.message.ServerMessage;
import javachallenge.util.Map;
import sun.misc.Cleaner;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.concurrent.ThreadPoolExecutor;

public class Server {

    public static int CYCLE_LENGTH = 750;
    public static int PORT = 20140;

    public void run() throws InterruptedException, IOException, ClassNotFoundException {
        int num_clients = 1;

        ClientConnection[] clientConnections = new ClientConnection[num_clients];

        ServerSocket serverSocket = new ServerSocket(PORT);

        for (int i = 0; i < num_clients; i++) {
            System.out.println("Waiting for player " + i + " to connect ...");
            clientConnections[i] = new ClientConnection(serverSocket.accept());
            System.out.println("Player " + i + " connected!");
        }

        Map map = Map.loadMap("test.map");
        Game game = new Game(map);

        FJframe graphics = new FJframe(game);
        FJpanel panel = graphics.getPanel();

        int cycle = 0;

        while (!game.isEnded()) {
            System.out.println("Cycle: " + (++cycle));

            ServerMessage serverMessage = new ServerMessage();

            for (ClientConnection c : clientConnections) {
                c.getOut().writeObject(serverMessage);
                c.getOut().flush();
            }

            for (ClientConnection c : clientConnections) {
                c.setClientMessage(null);
            }

            Thread.sleep(CYCLE_LENGTH);

//            game.initCycle(cycle);
//            game.handleActions(actions);
//            game.endTurn()

            //update graphics and our map
        }
    }

    public static void main(String[] args) {
        try {
            new Server().run();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
