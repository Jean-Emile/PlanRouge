package org.daum.planrouge.server;

import org.daum.planrouge.server.adapter.model.AdapterFactory;
import org.daum.planrouge.server.web.EmbedHandler;
import org.daum.planrouge.server.websocket.GetGlobalInformationsHandler;
import org.daum.planrouge.server.websocket.HandlerWebSocket;
import org.kevoree.annotation.*;
import org.kevoree.framework.AbstractComponentType;
import org.kevoree.log.Log;
import org.kevoree.planrouge.ContainerRoot;
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
    private ContainerRoot containerRoot;
    private AdapterFactory adapterFactory;

    private HandlerWebSocket handlerWebSocketget;
    private HandlerWebSocket handlerWebSocketput;
    private GetGlobalInformationsHandler handlerGlobalInformations;
    private HandlerWebSocket handlerWebSocketgetAll;
    private HandlerWebSocket handlerWebSocketdelete;

    @Start
    public void start() {
        adapterFactory = AdapterFactory.getInstance();
        containerRoot = adapterFactory.getFactory().createContainerRoot();

        handlerWebSocketget = new HandlerWebSocket(adapterFactory,containerRoot, HandlerWebSocket.ACTION.GET);
        handlerWebSocketput = new HandlerWebSocket(adapterFactory,containerRoot, HandlerWebSocket.ACTION.PUT);
        handlerWebSocketgetAll = new HandlerWebSocket(adapterFactory,containerRoot, HandlerWebSocket.ACTION.GETALL);
        handlerWebSocketdelete = new HandlerWebSocket(adapterFactory,containerRoot, HandlerWebSocket.ACTION.DELETE);
        handlerGlobalInformations =  new GetGlobalInformationsHandler(adapterFactory,containerRoot);

        createWebServer();
        webServer.start();
        Log.info("Server running at " + webServer.getUri());

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
                .add("/add",handlerWebSocketput)
                .add("/get", handlerWebSocketget)
                .add("/getAll", handlerWebSocketgetAll)
                .add("/delete", handlerWebSocketdelete)
                .add("/getGlobalInformations", handlerGlobalInformations)
                .add(new StaticFileHandler("/web"))
                .add(new EmbedHandler());
    }

}
