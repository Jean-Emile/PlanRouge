package org.daum.planrouge.server;

import org.daum.planrouge.server.adapter.AdapterVictime;
import org.daum.planrouge.server.adapter.model.AdapterFactory;
import org.daum.planrouge.server.connections.Connections;
import org.json.JSONException;
import org.json.JSONObject;
import org.kevoree.planrouge.*;
import org.kevoree.log.Log;
import org.webbitserver.*;

/**
 * Created with IntelliJ IDEA.
 * User: cbriand
 * Date: 09/07/13
 * Time: 13:39
 * To change this template use File | Settings | File Templates.
 */
public class AddVictimHandler extends BaseWebSocketHandler {

    private int connectionCount;
    private Connections connections ;
    private AdapterFactory adapterFactory;
    private ContainerRoot containerRoot;


    public AddVictimHandler(AdapterFactory adapterFactory, ContainerRoot containerRoot, Connections connections) {
        this.adapterFactory = adapterFactory;
        this.containerRoot = containerRoot;
        this.connections = connections;
    }

    public void onOpen(WebSocketConnection connection) {
        Log.debug("New connection");
        connections.getConnectionAddVictim().addConnections(connection);
        connection.send("AddVictimHandler ::: " + connectionCount + " other connections active");
        connectionCount++;
    }

    public void onClose(WebSocketConnection connection) {
        Log.debug("AddVictimHandler ::: Connection close ");
        connections.getConnectionAddVictim().removeConnections(connection);
        connectionCount--;

    }

    public void onMessage(WebSocketConnection connection, String message) throws JSONException {
        Log.debug("AddVictimHandler ::: ON_MESSAGE");

        JSONObject jsonVictime = new JSONObject(message);
        Victime victime = adapterFactory.build(jsonVictime);

        if (victime != null) {
         Victime vicmodel =   containerRoot.findInterventionsByID("1").findVictimesByID(victime.getId());
            if(vicmodel !=null){
                //todo jed
            } else {
                containerRoot.findInterventionsByID("1").addVictimes(victime);
            }

            connection.send("Victim added to database");
        } else {
            connection.send("Erreur");
        }

        connections.getConnectionGlobalInformations().refresh();
    }



}

