package org.daum.planrouge.server;

import org.kevoree.annotation.*;
import org.kevoree.framework.AbstractComponentType;
import org.kevoree.log.Log;
import org.kevoree.planrouge.ContainerRoot;
import org.kevoree.planrouge.PlanrougeFactory;
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

    WebServer webServer;
    int port;
    static PlanrougeFactory f;
    static ContainerRoot containerRoot;

    @Start
    public void start() {
        createWebServer();
        webServer.start();

        f = new MainFactory().getPlanrougeFactory();
        containerRoot =f.createContainerRoot();

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
        createWebServer()   ;
        webServer.start();
        // TODO update webbit server port
    }


    private void createWebServer(){
        webServer = WebServers.createWebServer(Integer.parseInt(getDictionary().get("port").toString()))
                .add("/addVictim", new AddVictimHandler())
                .add("/getVictim", new GetVictimHandler())
                .add("/getGlobalInformations", new GetGlobalInformationsHandler())
                .add(new StaticFileHandler("/web"));
    }

    public static ContainerRoot getContainerRoot(){
        return containerRoot;
    }
    public static PlanrougeFactory getPlanrougeFactory(){
        return f;
    }
}
