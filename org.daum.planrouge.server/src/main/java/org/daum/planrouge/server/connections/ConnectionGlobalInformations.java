package org.daum.planrouge.server.connections;


import org.daum.planrouge.server.adapter.model.entity.AdapterVictime;
import org.json.JSONArray;
import org.json.JSONException;
import org.kevoree.planrouge.ContainerRoot;
import org.kevoree.planrouge.PlanrougeFactory;
import org.webbitserver.WebSocketConnection;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: cbriand
 * Date: 12/07/13
 * Time: 10:48
 * To change this template use File | Settings | File Templates.
 */
public class ConnectionGlobalInformations {

    private List<WebSocketConnection> connections;
    private PlanrougeFactory planrougeFactory;
    private ContainerRoot containerRoot;

    public ConnectionGlobalInformations(PlanrougeFactory planrougeFactory, ContainerRoot containerRoot) {
        this.planrougeFactory = planrougeFactory;
        this.containerRoot = containerRoot;
        this.connections = new ArrayList<WebSocketConnection>();
    }


    public List<WebSocketConnection> getConnections() {
        return connections;
    }

    public void addConnections(WebSocketConnection connection) {
        connections.add(connection);
    }

    public void removeConnections(WebSocketConnection connection) {
        connections.remove(connection);
    }

    public void sendMessage(String message) {
        for (int i = 0; i < connections.size(); i++) {
            connections.get(i).send(message);
        }
    }

    public void refresh() throws JSONException {
      /*  AdapterVictime adapterVictime = new AdapterVictime(planrougeFactory, containerRoot);

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

        for (int i = 0; i < connections.size(); i++) {
            connections.get(i).send(informations.toString());
        } */
    }
}
