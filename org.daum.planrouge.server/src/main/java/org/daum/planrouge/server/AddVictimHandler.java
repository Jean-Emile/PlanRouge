package org.daum.planrouge.server;

import org.daum.planrouge.server.adapter.AdapterVictime;
import org.daum.planrouge.server.connections.Connections;
import org.json.JSONException;
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
    private PlanrougeFactory planrougeFactory;
    private ContainerRoot containerRoot;


    public AddVictimHandler(PlanrougeFactory planrougeFactory, ContainerRoot containerRoot, Connections connections) {
        this.planrougeFactory = planrougeFactory;
        this.containerRoot = containerRoot;
        this.connections = connections;
    }

    public void onOpen(WebSocketConnection connection) {
        Log.debug("Nouvelle connexion");
        connections.getConnectionAddVictim().addConnections(connection);
        connection.send("AddVictimHandler ::: " + connectionCount + " other connections active");
        connectionCount++;
    }

    public void onClose(WebSocketConnection connection) {
        Log.debug("AddVictimHandler ::: Connexion fermée ");
        connections.getConnectionAddVictim().removeConnections(connection);
        connectionCount--;

    }

    public void onMessage(WebSocketConnection connection, String message) throws JSONException {
        Log.debug("AddVictimHandler ::: ON_MESSAGE");

        AdapterVictime adapterVictime = new AdapterVictime(planrougeFactory,containerRoot);
        Victime victime = adapterVictime.parseJsonToVictime(message);

        if (victime != null) {
            containerRoot.findInterventionsByID("1").addVictimes(victime);
            adapterVictime.parseVictimeToJson(victime);
            connection.send("Victime ajouté à la base de données");
        } else {
            connection.send("Erreur");
        }

        connections.getConnectionGlobalInformations().refresh();
    }



}

