package org.daum.planrouge.server;

import org.daum.planrouge.server.adapter.AdapterVictime;
import org.daum.planrouge.server.connections.Connections;
import org.json.JSONArray;
import org.json.JSONException;
import org.kevoree.log.Log;
import org.kevoree.planrouge.ContainerRoot;
import org.kevoree.planrouge.PlanrougeFactory;
import org.webbitserver.*;

/**
 * Created with IntelliJ IDEA.
 * User: cbriand
 * Date: 09/07/13
 * Time: 15:20
 * To change this template use File | Settings | File Templates.
 */
public class GetGlobalInformationsHandler extends BaseWebSocketHandler {
    private int connectionCount;

    private PlanrougeFactory planrougeFactory;
    private ContainerRoot containerRoot;
    private Connections connections;

    public GetGlobalInformationsHandler(PlanrougeFactory pPlanrougeFactory, ContainerRoot pContainerRoot, Connections connections) {
        this.planrougeFactory = pPlanrougeFactory;
        this.containerRoot = pContainerRoot;
        this.connections = connections;
    }

    public void onOpen(WebSocketConnection connection) {
        Log.debug("Nouvelle connexion");
        connections.getConnectionGlobalInformations().addConnections(connection);
        connection.send("GetGlobalInformationsHandler ::: " + connectionCount + " other connections active");
        connectionCount++;
    }

    public void onClose(WebSocketConnection connection) {
        Log.debug("GetGlobalInformationsHandler ::: Connexion fermée ");
        connections.getConnectionGlobalInformations().removeConnections(connection);
        connectionCount--;

    }

    public void onMessage(WebSocketConnection connection, String message) throws JSONException {
        Log.debug("GetGlobalInformationsHandler ::: ON_MESSAGE");

        AdapterVictime adapterVictime = new AdapterVictime(planrougeFactory, containerRoot);

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

    }


}
