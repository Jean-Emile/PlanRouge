package org.daum.planrouge.server.connections;

import org.daum.planrouge.server.adapter.AdapterVictime;
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
 * Time: 10:49
 * To change this template use File | Settings | File Templates.
 */
public class ConnectionGetVictim {

    private List<WebSocketConnection> connections;
    private PlanrougeFactory planrougeFactory;
    private ContainerRoot containerRoot;

    public ConnectionGetVictim(PlanrougeFactory planrougeFactory, ContainerRoot containerRoot) {
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

    }
}
