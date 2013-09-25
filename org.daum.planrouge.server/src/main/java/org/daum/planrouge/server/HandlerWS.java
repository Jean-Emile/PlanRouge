package org.daum.planrouge.server;

import org.daum.planrouge.server.adapter.model.AdapterFactory;
import org.daum.planrouge.server.cache.MemCache;
import org.daum.planrouge.server.websocket.*;
import org.kevoree.annotation.*;
import org.kevoree.framework.AbstractComponentType;
import org.kevoree.library.javase.webserver.AbstractPage;
import org.kevoree.library.javase.webserver.KevoreeHttpRequest;
import org.kevoree.library.javase.webserver.KevoreeHttpResponse;
import org.kevoree.log.Log;
import org.kevoree.planrouge.ContainerRoot;

/**
 * Created with IntelliJ IDEA.
 * User: cbriand
 * Date: 19/09/13
 * Time: 09:56
 * To change this template use File | Settings | File Templates.
 */
@Library(name = "PlanRouge")
@DictionaryType({
        @DictionaryAttribute(name = "action", defaultValue = "GET", optional = false, vals = {"PUT", "GET", "GETALL", "DELETE"}),
        @DictionaryAttribute(name = "handler", defaultValue = "/get", optional = false, vals = {"/add", "/get", "/getAll", "/delete"})
})

@Requires({
        @RequiredPort(name = "WS", type = PortType.SERVICE, className = WsHandler.class, optional = false, needCheckDependency = true),
        @RequiredPort(name = "Adapter", type = PortType.SERVICE, className = IAdapterFavtoryWs.class, optional = false, needCheckDependency = true),
        @RequiredPort(name = "Container", type = PortType.SERVICE, className = IContainerRoot.class, optional = false, needCheckDependency = true)
})
@ComponentType
public class HandlerWS extends AbstractComponentType {

    HandlerWebSocket handlerWebSocket;
    String handler;
    String action;

    @Start
    public void start() {
        Log.info("Begin START GlobalInformations");
        handler = getDictionary().get("handler").toString();
        action = getDictionary().get("action").toString();
        AdapterFactory adapterFactory = getPortByName("Adapter", IAdapterFavtoryWs.class).getAdapterFactory();
        ContainerRoot containerRoot = getPortByName("Container", IContainerRoot.class).getContainer();

        handlerWebSocket = new HandlerWebSocket(adapterFactory, containerRoot, HandlerWebSocket.ACTION.valueOf(action));

        getPortByName("WS", WsHandler.class).addHandler(handler, handlerWebSocket);

    }

    @Stop
    public void stop() {
        getPortByName("serviceWS", WsHandler.class).removeHandler(handler);


    }

    @Update
    public void update() {
        getPortByName("serviceWS", WsHandler.class).removeHandler(handler);

        handler = getDictionary().get("handler").toString();
        action = getDictionary().get("action").toString();
        AdapterFactory adapterFactory = getPortByName("Adapter", IAdapterFavtoryWs.class).getAdapterFactory();
        ContainerRoot containerRoot = getPortByName("Container", IContainerRoot.class).getContainer();

        handlerWebSocket = new HandlerWebSocket(adapterFactory, containerRoot, HandlerWebSocket.ACTION.valueOf(action));

        getPortByName("WS", WsHandler.class).addHandler(handler, handlerWebSocket);

    }
}
