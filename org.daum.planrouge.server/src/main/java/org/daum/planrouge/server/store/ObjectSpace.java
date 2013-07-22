package org.daum.planrouge.server.store;

import org.daum.planrouge.server.adapter.model.AdapterFactory;
import org.json.JSONException;
import org.json.JSONObject;
import org.kevoree.log.Log;
import org.kevoree.planrouge.ContainerRoot;
import org.kevoree.planrouge.Intervention;
import org.kevoree.planrouge.Victime;
import org.kevoree.planrouge.container.KMFContainer;
import org.kevoree.planrouge.events.ModelEvent;
import org.kevoree.planrouge.events.ModelTreeListener;
import org.kevoree.planrouge.impl.InterventionImpl;


import org.kevoree.planrouge.serializer.JSONModelSerializer;
import voldemort.client.*;
import voldemort.client.protocol.admin.AdminClient;
import voldemort.client.protocol.admin.AdminClientConfig;
import voldemort.cluster.Node;
import voldemort.cluster.failuredetector.FailureDetectorListener;
import voldemort.utils.ByteArray;
import voldemort.utils.Pair;
import voldemort.versioning.Versioned;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 16/07/13
 * Time: 11:44
 * To change this template use File | Settings | File Templates.
 */
public class ObjectSpace implements  Runnable {

    private  List<String> voldemort_ips = new ArrayList<String>();
    private StoreClientFactory factory    =null;
    private StoreClient store=null;
    private AdapterFactory factoryadapter = new AdapterFactory();
    private  Thread eventsthread=null;
    private String storename="";
    private ContainerRoot containerRoot;

    public  void open(String storename){
                 this.storename = storename;
        ClientConfig clientConfig=  new ClientConfig();
        clientConfig.setBootstrapUrls(voldemort_ips);

        factory = new SocketStoreClientFactory(clientConfig);
        store = factory.getStoreClient(storename);

        factory.getFailureDetector().addFailureDetectorListener(new FailureDetectorListener() {
            @Override
            public void nodeUnavailable(Node node) {
                System.out.println("nodeUnavailable " + node);
            }

            @Override
            public void nodeAvailable(Node node) {
                System.out.println("nodeAvailable " + node);

            }
        });

        eventsthread = new Thread(this);
       eventsthread.start();



    }

    public  void close(){
        if(eventsthread !=null){
            eventsthread.interrupt();
        }
        if(factory != null){
            factory.close();
        }
    }

    public ObjectSpace(ContainerRoot model){

        containerRoot = model;

        model.addModelTreeListener(new ModelTreeListener() {
            @Override
            public void elementChanged(ModelEvent evt) {


                if(factory != null){

                    switch (evt.getType()){

                        case add:

                            if(evt.getValue() instanceof KMFContainer){
                                KMFContainer container = (KMFContainer)evt.getValue();

                                try {

                                    store.put(container.path(), factoryadapter.build(container).toString());


                                } catch (JSONException e) {
                                    e.printStackTrace();
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
                                    e.printStackTrace();
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



    @Override
    public void run() {



        AdminClient adminClient = new AdminClient(voldemort_ips.get(0), new AdminClientConfig());
        Node node = adminClient.getAdminClientCluster().getNodeById(4);
        List partitionIds = node.getPartitionIds();


        while (Thread.currentThread().isAlive()){


                Iterator<Pair<ByteArray,Versioned<byte[]>>> iter = adminClient.fetchEntries(node.getId(), storename, partitionIds, null, false);
                while (iter.hasNext())
                {
                    Pair<ByteArray, Versioned<byte[]>> entry = iter.next();

                    String path =        new String(entry.getFirst().get());

                     if(containerRoot.findByPath(path) == null){
                         String json =  store.get(path).getValue().toString();

                         JSONObject obj = null;
                         try {
                             obj = new JSONObject(json);
                             Object bean =   factoryadapter.build(obj);
                             switch (factoryadapter.getType(bean)){


                                 case AdapterIntervention:

                                     containerRoot.addInterventions((Intervention) bean);
                                     break;


                                 case AdapterVictime:


                                     Intervention intervention = (Intervention) containerRoot.findByPath(path.split("/")[0]);
                                     if(intervention != null){
                                         Victime victime =    (Victime) bean;

                                         Log.debug("add vicitime "+victime.getNom()+" Intervention"+intervention.getId()) ;
                                         intervention.addVictimes(victime);
                                     }

                                     break;
                             }
                         } catch (JSONException e) {
                             e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                         }

                     }



            }
            try {
                Thread.sleep(2500);
            } catch (InterruptedException e) {

            }
        }
    }
}
