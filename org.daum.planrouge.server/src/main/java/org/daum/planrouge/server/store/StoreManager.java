package org.daum.planrouge.server.store;

import org.daum.planrouge.server.adapter.model.AdapterFactory;
import org.json.JSONException;
import org.kevoree.planrouge.ContainerRoot;
import org.kevoree.planrouge.container.KMFContainer;
import org.kevoree.planrouge.events.ModelEvent;
import org.kevoree.planrouge.events.ModelTreeListener;
import org.kevoree.planrouge.impl.InterventionImpl;


import org.kevoree.planrouge.serializer.JSONModelSerializer;
import voldemort.client.ClientConfig;
import voldemort.client.SocketStoreClientFactory;
import voldemort.client.StoreClient;
import voldemort.client.StoreClientFactory;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 16/07/13
 * Time: 11:44
 * To change this template use File | Settings | File Templates.
 */
public class StoreManager {

    private   List<String> voldemort_ips = new ArrayList<String>();
    private StoreClientFactory factory    =null;
    private StoreClient store=null;
    private AdapterFactory factoryadapter = new AdapterFactory();

    public  void open(){
        factory = new SocketStoreClientFactory(new ClientConfig().setBootstrapUrls(voldemort_ips));
        store = factory.getStoreClient("kevoree");

    }

    public  void close(){
        if(factory != null){
            factory.close();
        }
    }

    public StoreManager(ContainerRoot model){

        model.addModelTreeListener(new ModelTreeListener() {
            @Override
            public void elementChanged(ModelEvent evt) {

                if(factory != null){


                    switch (evt.getType()){

                        case add:

                            if(evt.getValue() instanceof KMFContainer){
                                KMFContainer container = (KMFContainer)evt.getValue();

                                try {
                                    store.put(container.path(),factoryadapter.build(container));
                                } catch (JSONException e) {
                                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                                }


                            }

                            break;

                        case remove:


                            if(evt.getValue() instanceof KMFContainer){
                                KMFContainer container = (KMFContainer)evt.getValue();
                                store.delete(container.path());
                            }

                            break;


                        case set:
                            if(evt.getValue() instanceof KMFContainer){
                                KMFContainer container = (KMFContainer)evt.getValue();
                                try {
                                    store.put(container.path(),factoryadapter.build(container));
                                } catch (JSONException e) {
                                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                                }

                            }

                            break;
                    }


                }else {
                    // err
                }



            }
        });


    }

    public void addReplica(String ip){
        voldemort_ips.add(ip);
    }

    public void removeReplica(String ip){
        voldemort_ips.remove(ip);
    }


}
