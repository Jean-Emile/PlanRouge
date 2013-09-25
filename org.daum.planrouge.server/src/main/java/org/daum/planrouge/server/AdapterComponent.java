package org.daum.planrouge.server;

import org.daum.planrouge.server.adapter.model.AdapterFactory;
import org.daum.planrouge.server.websocket.IAdapterFavtoryWs;
import org.kevoree.annotation.*;
import org.kevoree.framework.AbstractComponentType;
import org.kevoree.log.Log;

/**
 * Created with IntelliJ IDEA.
 * User: cbriand
 * Date: 19/09/13
 * Time: 09:58
 * To change this template use File | Settings | File Templates.
 */
@Library(name = "PlanRouge")
@Provides({
        @ProvidedPort(name = "Adapter", type = PortType.SERVICE, className = IAdapterFavtoryWs.class)
})
@ComponentType
public class AdapterComponent extends AbstractComponentType implements IAdapterFavtoryWs {

    private AdapterFactory adapterFactory;

    @Start
    public void start() {
        adapterFactory = AdapterFactory.getInstance();

    }

    @Stop
    public void stop(){

    }

    @Update
    public void update(){
        adapterFactory = AdapterFactory.getInstance();
    }


    @Port(name = "Adapter", method = "getAdapterFactory")
    @Override
    public AdapterFactory getAdapterFactory() {
        return adapterFactory;
    }

}
