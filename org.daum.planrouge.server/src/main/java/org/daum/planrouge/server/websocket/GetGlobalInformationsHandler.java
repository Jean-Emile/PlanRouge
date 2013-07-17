package org.daum.planrouge.server.websocket;

import org.daum.planrouge.server.adapter.model.AdapterFactory;
import org.json.JSONException;
import org.kevoree.log.Log;
import org.kevoree.planrouge.ContainerRoot;
import org.webbitserver.BaseWebSocketHandler;
import org.webbitserver.WebSocketConnection;

/**
 * Created with IntelliJ IDEA.
 * User: cbriand
 * Date: 09/07/13
 * Time: 15:20
 * To change this template use File | Settings | File Templates.
 */
public class GetGlobalInformationsHandler extends BaseWebSocketHandler {
    private int connectionCount;

    private AdapterFactory adapterFactory;
    private ContainerRoot containerRoot;
    private Peers connections;

    public GetGlobalInformationsHandler(AdapterFactory adapterFactory, ContainerRoot pContainerRoot) {
        this.adapterFactory = adapterFactory;
        this.containerRoot = pContainerRoot;
        this.connections = connections;
    }

    public void onOpen(WebSocketConnection connection) {
        Log.debug("Nouvelle connexion");

        connection.send("GetGlobalInformationsHandler ::: " + connectionCount + " other connections active");
        connectionCount++;
    }

    public void onClose(WebSocketConnection connection) {
        Log.debug("GetGlobalInformationsHandler ::: Connexion fermée ");

        connectionCount--;

    }

    public void onMessage(WebSocketConnection connection, String message) throws JSONException {
        Log.debug("GetGlobalInformationsHandler ::: ON_MESSAGE");
        /*
        AdapterVictime adapterVictime = new AdapterVictime(adapterFactory.getFactory(), containerRoot);

        //List victimes;

        JSONArray informations = new JSONArray();
        informations.put(0, "graph");
        informations.put(1, adapterVictime.getNombreVictimes());
        informations.put(2, adapterVictime.getNombreVictimeParCategorie()[0]);
        informations.put(3, adapterVictime.getNombreVictimeParCategorie()[1]);
        informations.put(4, adapterVictime.getNombreVictimeParCategorie()[2]);
        informations.put(5, adapterVictime.getNombreVictimeParCategorie()[3]);
        informations.put(6, adapterVictime.getNombreVictimeParCategorie()[4]);
        informations.put(7, containerRoot.findInterventionsByID("1").getDescription());
        Log.debug(informations.toString());
        connection.send(informations.toString()); // echo back message in upper case
           */
    }


}