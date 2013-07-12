package org.daum.planrouge.server.connections;

import org.daum.planrouge.server.adapter.AdapterVictime;
import org.json.JSONArray;
import org.json.JSONException;
import org.kevoree.log.Log;
import org.kevoree.planrouge.ContainerRoot;
import org.kevoree.planrouge.PlanrougeFactory;
import org.webbitserver.WebSocketConnection;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: cbriand
 * Date: 12/07/13
 * Time: 10:00
 * To change this template use File | Settings | File Templates.
 */
public class Connections {

    private PlanrougeFactory planrougeFactory;
    private ContainerRoot containerRoot;
    private ConnectionAddVictim connectionAddVictim;
    private ConnectionGetVictim connectionGetVictim;
    private ConnectionGlobalInformations connectionGlobalInformations;

    public Connections(PlanrougeFactory planrougeFactory, ContainerRoot containerRoot){
        this.connectionAddVictim = new ConnectionAddVictim(planrougeFactory,containerRoot) ;
        this.connectionGetVictim = new ConnectionGetVictim(planrougeFactory,containerRoot) ;
        this.connectionGlobalInformations = new ConnectionGlobalInformations(planrougeFactory,containerRoot) ;
        this.planrougeFactory = planrougeFactory;
        this.containerRoot = containerRoot;
    }


    //SETTERS GETTERS
    public ConnectionGlobalInformations getConnectionGlobalInformations() {
        return connectionGlobalInformations;
    }

    public ConnectionAddVictim getConnectionAddVictim() {
        return connectionAddVictim;
    }

    public ConnectionGetVictim getConnectionGetVictim() {
        return connectionGetVictim;
    }

    public void setConnectionAddVictim(ConnectionAddVictim connectionAddVictim){
        this.connectionAddVictim = connectionAddVictim;
    }

    public void setConnectionGetVictim(ConnectionGetVictim connectionGetVictim){
        this.connectionGetVictim = connectionGetVictim;
    }

    public void setConnectionGlobalInformations(ConnectionGlobalInformations connectionGlobalInformations){
        this.connectionGlobalInformations = connectionGlobalInformations;
    }
}
