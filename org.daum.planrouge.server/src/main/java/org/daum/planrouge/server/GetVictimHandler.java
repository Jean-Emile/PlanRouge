package org.daum.planrouge.server;

import org.json.JSONException;
import org.webbitserver.*;

/**
 * Created with IntelliJ IDEA.
 * User: cbriand
 * Date: 09/07/13
 * Time: 15:19
 * To change this template use File | Settings | File Templates.
 */
public class GetVictimHandler extends BaseWebSocketHandler {
    private int connectionCount;

    public void onOpen(WebSocketConnection connection) {
        System.out.println("Nouvelle connexion");
        connection.send("GetVictimHandler ::: " + connectionCount + " other connections active");
        connectionCount++;
    }

    public void onClose(WebSocketConnection connection) {
        System.out.println("GetVictimHandler ::: Connexion fermée ");
        connectionCount--;

    }

    public void onMessage(WebSocketConnection connection, String message) throws JSONException {
        System.out.println("GetVictimHandler :::  ON_MESSAGE");

        connection.send("Nombre de victimes :: "+"\n"); // echo back message in upper case

    }
}
