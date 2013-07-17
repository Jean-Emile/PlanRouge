package org.daum.planrouge.server;

import org.daum.planrouge.server.adapter.model.AdapterFactory;
import org.daum.planrouge.server.websocket.Peers;
import org.kevoree.annotation.*;
import org.kevoree.framework.AbstractComponentType;
import org.kevoree.log.Log;
import org.kevoree.planrouge.ContainerRoot;
import org.kevoree.planrouge.Intervention;
import org.webbitserver.WebServer;
import org.webbitserver.WebServers;
import org.webbitserver.handler.StaticFileHandler;

/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 09/07/13
 * Time: 11:50
 * To change this template use File | Settings | File Templates.
 */
@Library(name = "PlanRouge")
@DictionaryType({
        @DictionaryAttribute(name = "port", defaultValue = "8080", optional = false)
})
@ComponentType
public class ServerPlanRouge extends AbstractComponentType {

    private WebServer webServer;
    private int port;

    private ContainerRoot containerRoot;
      private int i;
    private Peers peers;
    private AdapterFactory adapterFactory;
    @Start
    public void start() {
        adapterFactory = new AdapterFactory();
//        planrougeFactory = new MainFactory().getPlanrougeFactory();
        containerRoot = adapterFactory.getFactory().createContainerRoot();


        Intervention intervention = adapterFactory.getFactory().createIntervention();
        intervention.setId("1");
        intervention.setDescription("un train est rentré dans un avion en plein vol");
        containerRoot.addInterventions(intervention);
        peers = new Peers(adapterFactory.getFactory());
        createWebServer();
        webServer.start();

        Log.info("Server running at " + webServer.getUri());
        // start server
    }

    @Stop
    public void stop() {
        webServer.stop();
    }


    @Update
    public void update() {
        webServer.stop();
        createWebServer();
        webServer.start();
        // TODO update webbit server port
        Log.info("UPDATE :: Server running at " + webServer.getUri());
    }


    private void createWebServer() {
        webServer = WebServers.createWebServer(Integer.parseInt(getDictionary().get("port").toString()))
                .add("/addVictim", new AddVictimHandler(adapterFactory,containerRoot, peers))
                .add("/getVictim", new GetVictimHandler(adapterFactory,containerRoot, peers))
                .add("/getGlobalInformations", new GetGlobalInformationsHandler(adapterFactory,containerRoot, peers))
                .add(new StaticFileHandler("/web"));
    }

}
