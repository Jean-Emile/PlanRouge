package org.daum.planrouge.server;


import org.daum.planrouge.server.adapter.model.AdapterFactory;
import org.daum.planrouge.server.cache.MemCache;
import org.daum.planrouge.server.websocket.GetGlobalInformationsHandler;
import org.daum.planrouge.server.websocket.IAdapterFavtoryWs;
import org.daum.planrouge.server.websocket.IContainerRoot;
import org.daum.planrouge.server.websocket.WsHandler;
import org.kevoree.annotation.*;
import org.kevoree.library.javase.webserver.AbstractPage;
import org.kevoree.library.javase.webserver.KevoreeHttpRequest;
import org.kevoree.library.javase.webserver.KevoreeHttpResponse;
import org.kevoree.log.Log;
import org.kevoree.planrouge.ContainerRoot;

/**
 * Created with IntelliJ IDEA.
 * User: cbriand
 * Date: 16/09/13
 * Time: 16:07
 * To change this template use File | Settings | File Templates.
 */
@Library(name = "PlanRouge")
@Requires({
        @RequiredPort(name = "WS", type = PortType.SERVICE, className = WsHandler.class, optional = false, needCheckDependency = true),
        @RequiredPort(name = "Adapter", type = PortType.SERVICE, className = IAdapterFavtoryWs.class, optional = false, needCheckDependency = true),
        @RequiredPort(name = "Container", type = PortType.SERVICE, className = IContainerRoot.class, optional = false, needCheckDependency = true)
})

@ComponentType
public class GlobalInformations extends AbstractPage{

     private GetGlobalInformationsHandler getGlobalInformationsHandler;

    @Override
    public KevoreeHttpResponse process(KevoreeHttpRequest kevoreeHttpRequest, KevoreeHttpResponse kevoreeHttpResponse) {
        Log.info("Begin START GlobalInformations process");
        String page = new String(MemCache.getRessource("visu2.html"));
        kevoreeHttpResponse.setContent(page);
        return kevoreeHttpResponse;
    }


    @Start
    public void start() {
        Log.info("Begin START GlobalInformations");
        super.startPage();
        AdapterFactory adapterFactory = getPortByName("Adapter", IAdapterFavtoryWs.class).getAdapterFactory();
        ContainerRoot containerRoot =  getPortByName("Container", IContainerRoot.class).getContainer();
        getGlobalInformationsHandler = new GetGlobalInformationsHandler(adapterFactory,containerRoot);
        getPortByName("WS", WsHandler.class).addHandler("/getGlobalInformations",getGlobalInformationsHandler );
    }

    @Stop
    public void stop(){
        getPortByName("WS", WsHandler.class).removeHandler("/getGlobalInformations");
        super.stopPage();

    }

    @Update
    public void update(){
        getPortByName("WS", WsHandler.class).removeHandler("/getGlobalInformations");
        AdapterFactory adapterFactory = getPortByName("Adapter", IAdapterFavtoryWs.class).getAdapterFactory();
        ContainerRoot containerRoot =  getPortByName("Container", IContainerRoot.class).getContainer();
        getGlobalInformationsHandler = new GetGlobalInformationsHandler(adapterFactory,containerRoot);
        getPortByName("WS", WsHandler.class).addHandler("/getGlobalInformations",getGlobalInformationsHandler );
       super.updatePage();
    }
}

