package org.daum.planrouge.server;

import org.json.JSONException;
import org.webbitserver.*;

/**
 * Created with IntelliJ IDEA.
 * User: cbriand
 * Date: 09/07/13
 * Time: 15:20
 * To change this template use File | Settings | File Templates.
 */
public class GetGlobalInformationsHandler extends BaseWebSocketHandler{
    private int connectionCount;

    public void onOpen(WebSocketConnection connection) {
        System.out.println("Nouvelle connexion");
        connection.send("GetGlobalInformationsHandler ::: " + connectionCount + " other connections active");
        connectionCount++;
    }

    public void onClose(WebSocketConnection connection) {
        System.out.println("GetGlobalInformationsHandler ::: Connexion fermée ");
        connectionCount--;

    }

    public void onMessage(WebSocketConnection connection, String message) throws JSONException {
        System.out.println("GetGlobalInformationsHandler ::: ON_MESSAGE");

        MapDB mapDB = new MapDB("BD_Victime","password","Collection_Victime");
        int nbVictim = mapDB.getMap().size();
        String retour = "NOMBRE DE VICTIME ::: "+nbVictim+"\n";
        for (String mapKey : mapDB.getMap().keySet()) {
           retour += " clé :: "+mapKey+"  |  valeur :: "+mapDB.getMap().get(mapKey)+"\n";
        }
        mapDB.close();
        connection.send(retour); // echo back message in upper case

    }
}
