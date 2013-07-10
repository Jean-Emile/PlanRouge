package org.daum.planrouge.server;

import org.json.JSONException;
import org.kevoree.log.Log;
import org.kevoree.planrouge.ContainerRoot;
import org.kevoree.planrouge.Intervention;
import org.kevoree.planrouge.PlanrougeFactory;
import org.webbitserver.*;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: cbriand
 * Date: 09/07/13
 * Time: 15:20
 * To change this template use File | Settings | File Templates.
 */
public class GetGlobalInformationsHandler extends BaseWebSocketHandler{
    private int connectionCount;

    private PlanrougeFactory planrougeFactory;
    private ContainerRoot containerRoot;

    public GetGlobalInformationsHandler(PlanrougeFactory pPlanrougeFactory, ContainerRoot pContainerRoot) {
        this.planrougeFactory = pPlanrougeFactory;
        this.containerRoot = pContainerRoot;
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

        //List victimes;

        int size = containerRoot.findInterventionsByID("1").getVictimes().size();

        String retour = "NOMBRE DE VICTIME ::: "+size+"\n";

        connection.send(retour); // echo back message in upper case

    }
}
