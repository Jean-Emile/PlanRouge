package org.daum.planrouge.server;

import org.daum.planrouge.server.adapter.model.AdapterFactory;
import org.daum.planrouge.server.websocket.IAdapterFavtoryWs;
import org.daum.planrouge.server.websocket.IContainerRoot;
import org.daum.planrouge.server.websocket.WsHandler;
import org.kevoree.annotation.*;
import org.kevoree.framework.AbstractComponentType;
import org.kevoree.planrouge.Agent;
import org.kevoree.planrouge.ContainerRoot;

/**
 * Created with IntelliJ IDEA.
 * User: cbriand
 * Date: 20/09/13
 * Time: 17:05
 * To change this template use File | Settings | File Templates.
 */


@Library(name = "PlanRouge")

@Requires({
        @RequiredPort(name = "Adapter", type = PortType.SERVICE, className = IAdapterFavtoryWs.class, optional = false, needCheckDependency = true)
})
@Provides({
        @ProvidedPort(name = "Container", type = PortType.SERVICE, className = IContainerRoot.class),
})

@ComponentType
public class ContainerComponent extends AbstractComponentType implements IContainerRoot {

    private AdapterFactory adapterFactory;
    private ContainerRoot containerRoot;

    @Start
    public void start() {
        adapterFactory = AdapterFactory.getInstance();
        containerRoot = adapterFactory.getFactory().createContainerRoot() ;
    }

    @Stop
    public void stop(){

    }

    @Update
    public void update(){
        adapterFactory = AdapterFactory.getInstance();
        containerRoot = adapterFactory.getFactory().createContainerRoot();
    }


    @Port(name = "Container", method = "getContainer")
    @Override
    public ContainerRoot getContainer() {
        return containerRoot;
    }

    @Port(name = "Container", method = "getAgent")
    @Override
    public Agent getAgent(String id) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
