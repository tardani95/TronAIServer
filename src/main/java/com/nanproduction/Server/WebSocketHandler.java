package com.nanproduction.Server;

import com.nanproduction.GameElements.Game;
import com.nanproduction.GameElements.GameStateEnum;
import com.nanproduction.GameElements.Player;
import org.webbitserver.BaseWebSocketHandler;
import org.webbitserver.WebSocketConnection;

import static com.nanproduction.Main.restartServer;
import static com.nanproduction.Main.stopServerAndExit;

public class WebSocketHandler extends BaseWebSocketHandler {

    private Game game = Game.getInstance();


    @Override
    public void onOpen(WebSocketConnection connection) {
    }

    @Override
    public void onClose(WebSocketConnection connection) {
        Player player = game.getPlayer(connection);
        if (player == null) {
            game.removeWatcher(connection);
        } else {
            game.removePlayer(connection);
        }

        System.out.println("SomeOne Disconnected. Connections: "
                + game.getNumOfPlayers());
    }

    @Override
    public void onMessage(WebSocketConnection connection, String message) {
        //if the value of the players map is null, then adds the message, otherwise do nothing - first message must be the player name
        //players.putIfAbsent(connection, message);
        switch (message) {
            case "GUEST":
                game.addWatcher(connection);
                System.out.println("New Watcher");
                return;
            case "KILLAPPLICATION":
                stopServerAndExit();
                return;
            case "RESTARTFUN":
                restartServer();
                return;
            default:break;
        }


        Player player = game.getPlayer(connection);

        if (player == null) {
            if (game.getGameState() == GameStateEnum.WAITING_FOR_PLAYERS) {
                game.addNewPlayer(connection,message);

                connection.send("Welcome to the Arena!\n"
                        + "Total No. of racers: " + game.getNumOfPlayers() + ".");
                System.out.println("Someone connected. Connections: "
                        + game.getNumOfPlayers());
            } else {
                connection.send("Your game is ended!");
            }

        } else {
            if (game.getGameState() == GameStateEnum.WAITING_FOR_PLAYERS) {
                if (message.equals("READY")) {
                    player.setReady(true);
                }
                System.out.println("Player " + player.getId() + " is ready!");
            } else {
                System.out.println(player.getId() + ": " + message/*+" time:"+ System.currentTimeMillis()%10000*/);
                //Game.getInstance().addKeyCode(message);
                player.setKeyCode(message);
                //}
                //connection.send("The server has received the following message:"+ message);
            }
        }
    }

}
