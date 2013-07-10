package org.daum.planrouge.server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.webbitserver.*;

import java.util.Iterator;

/**
 * Created with IntelliJ IDEA.
 * User: cbriand
 * Date: 09/07/13
 * Time: 13:39
 * To change this template use File | Settings | File Templates.
 */
public class AddVictimHandler extends BaseWebSocketHandler {

    private int connectionCount;
    private int i=0;
    public void onOpen(WebSocketConnection connection) {
        System.out.println("Nouvelle connexion");
        connection.send("AddVictimHandler ::: " + connectionCount + " other connections active");
        connectionCount++;
    }

    public void onClose(WebSocketConnection connection) {
        System.out.println("AddVictimHandler ::: Connexion fermée ");
        connectionCount--;

    }

    public void onMessage(WebSocketConnection connection, String message) throws JSONException {
        System.out.println("AddVictimHandler ::: ON_MESSAGE");
     //   ServerPlanRouge serverPlanRouge = new ServerPlanRouge();

        MapDB mapDB = new MapDB("BD_Victime","password","Collection_Victime");
        mapDB.addInMap(String.valueOf(i++),message);
        mapDB.commit();

        int size = mapDB.getMap().size();
        mapDB.close();
        connection.send("Nombre de VICTIMES:: "+ size +"\n");
    }
}
