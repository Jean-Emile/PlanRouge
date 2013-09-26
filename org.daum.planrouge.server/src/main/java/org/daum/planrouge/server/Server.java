package org.daum.planrouge.server;

import org.daum.planrouge.server.adapter.model.AdapterFactory;
import org.daum.planrouge.server.web.EmbedHandler;
import org.daum.planrouge.server.websocket.*;
import org.kevoree.annotation.*;
import org.kevoree.framework.AbstractComponentType;
import org.kevoree.log.Log;
import org.kevoree.planrouge.Agent;
import org.kevoree.planrouge.ContainerRoot;
import org.kevoree.planrouge.Intervention;
import org.webbitserver.BaseWebSocketHandler;
import org.webbitserver.WebServer;
import org.webbitserver.WebServers;
import org.webbitserver.handler.StaticFileHandler;

import java.util.HashMap;

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
@Provides({
        @ProvidedPort(name = "WS", type = PortType.SERVICE, className = WsHandler.class),
        @ProvidedPort(name = "Container", type = PortType.SERVICE, className = IContainerRoot.class),
        @ProvidedPort(name = "Adapter", type = PortType.SERVICE, className = IAdapterFavtoryWs.class)
})

@ComponentType
public class Server extends AbstractComponentType implements WsHandler, IContainerRoot, IAdapterFavtoryWs {

    private WebServer webServer;
    private ContainerRoot containerRoot;
    private AdapterFactory adapterFactory;
    private int port;
    private HashMap<String, BaseWebSocketHandler> wspages = new HashMap<String, BaseWebSocketHandler>();

    @Start
    public void start() {
        Log.info("Begin START Server");
        adapterFactory = AdapterFactory.getInstance();
        containerRoot = adapterFactory.getFactory().createContainerRoot();

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
        Log.info("UPDATE :: Server running at " + webServer.getUri());
    }


    private void createWebServer() {
        port = Integer.parseInt(getDictionary().get("port").toString());
        webServer = WebServers.createWebServer(port);
        webServer.add(new StaticFileHandler("/web"))
                .add(new EmbedHandler());
        for (String key : wspages.keySet()) {
            webServer.add(key,wspages.get(key));
        }
    }



    @Port(name = "WS", method = "addHandler")
    @Override
    public void addHandler(String name, BaseWebSocketHandler webSocketChannel) {
        Log.warn("Adding WS " + name);
        if (!wspages.containsKey(name)) {
            wspages.put(name, webSocketChannel);
            update();

        } else {
            Log.warn("Already added " + name);
        }
    }


    @Port(name = "WS", method = "removeHandler")
    @Override
    public void removeHandler(String name) {

        wspages.remove(name);
        Log.warn("Removing WS " + name);
        update();
    }

    @Port(name = "Container", method = "getContainer")
    @Override
    public ContainerRoot getContainer() {
        return containerRoot;
    }

    @Port(name = "Container", method = "getAgent")
    @Override
    public Agent getAgent(String id) {
       return containerRoot.findAgentsByID(id);
    }

    @Port(name = "Container", method = "getIntervention")
    @Override
    public Intervention getIntervention(String id) {
        return containerRoot.findInterventionsByID(id);
    }


    @Port(name = "Adapter", method = "getAdapterFactory")
    @Override
    public AdapterFactory getAdapterFactory() {
        return adapterFactory;
    }

}
