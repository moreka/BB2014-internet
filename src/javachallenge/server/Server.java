/**
 * Created by mohammad on 2/4/14.
 */

package javachallenge.server;

import javachallenge.client.Client;
//import javachallenge.graphics.FJframe;
//import javachallenge.graphics.FJpanel;
import javachallenge.graphics.FJframe;
import javachallenge.graphics.FJpanel;
import javachallenge.message.Action;
import javachallenge.message.Delta;
import javachallenge.message.InitialMessage;
import javachallenge.message.ServerMessage;
import javachallenge.util.Map;
import sun.misc.Cleaner;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.concurrent.ThreadPoolExecutor;

public class Server {

    public static int CYCLE_LENGTH = 1000;
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

        InitialMessage initialMessage = new InitialMessage();
        initialMessage.setMap(map);

        for (ClientConnection c : clientConnections) {
            c.getOut().writeObject(initialMessage);
            c.getOut().flush();
        }

        FJframe graphics = new FJframe(game, game.getMap().getSizeY(), game.getMap().getSizeX());
        FJpanel panel = graphics.getPanel();

/*        DummyGraphics graphics = new DummyGraphics(map);
        graphics.setVisible(true);*/

        int turn = 0;

        while (!game.isEnded()) {
            System.out.println("Turn: " + (++turn));

            ServerMessage serverMessage = new ServerMessage(game.getWallDeltasList(),
                    game.getMoveDeltasList(), game.getOtherDeltasList());

            for (ClientConnection c : clientConnections) {
                c.getOut().writeObject(serverMessage);
                c.getOut().flush();
            }

            for (ClientConnection c : clientConnections) {
                c.setClientMessage(null);
            }

            Thread.sleep(CYCLE_LENGTH);

            ArrayList<Action> actions = new ArrayList<Action>();

            for (ClientConnection c : clientConnections) {
                if (c.getClientMessage() != null)
                    actions.addAll(c.getClientMessage().getActions());
            }

            for (Action action : actions) {
                System.out.println(action);
            }

            game.initTurn(turn);
            game.handleActions(actions);
            graphics.repaint();
            game.endTurn();
            game.getMap().updateMap(game.getOtherDeltasList());
            //game.getMap().updateMap(game.getMoveDeltasList());
            //game.getMap().updateMap(game.getWallDeltasList());
            //game.getMap().updateMap(game.getOtherDeltasList());

            graphics.repaint();
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
