package org.daum.planrouge.server;

import org.kevoree.annotation.*;
import org.kevoree.framework.AbstractComponentType;
import org.kevoree.log.Log;
import org.kevoree.planrouge.ContainerRoot;
import org.kevoree.planrouge.Intervention;
import org.kevoree.planrouge.PlanrougeFactory;
import org.kevoree.planrouge.Victime;
import org.kevoree.planrouge.factory.MainFactory;
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
    private PlanrougeFactory planrougeFactory;
    private ContainerRoot containerRoot;
      private int i;
    @Start
    public void start() {

        planrougeFactory = new MainFactory().getPlanrougeFactory();
        containerRoot = planrougeFactory.createContainerRoot();

        Intervention intervention = planrougeFactory.createIntervention();
        intervention.setId("1");
        intervention.setDescription("un train est rentré dans un avion en plein vol");
        containerRoot.addInterventions(intervention);

        createWebServer();
        webServer.start();

        Log.debug("Server running at " + webServer.getUri());
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
        Log.debug("UPDATE :: Server running at " + webServer.getUri());
    }


    private void createWebServer() {
        webServer = WebServers.createWebServer(Integer.parseInt(getDictionary().get("port").toString()))
                .add("/addVictim", new AddVictimHandler(planrougeFactory, containerRoot, i))
                .add("/getVictim", new GetVictimHandler(planrougeFactory, containerRoot))
                .add("/getGlobalInformations", new GetGlobalInformationsHandler(planrougeFactory, containerRoot))
                .add(new StaticFileHandler("/web"));
    }

}
