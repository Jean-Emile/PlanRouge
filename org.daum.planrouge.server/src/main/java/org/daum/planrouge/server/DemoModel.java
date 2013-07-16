package org.daum.planrouge.server;

import org.daum.planrouge.server.store.StoreManager;
import org.kevoree.planrouge.ContainerRoot;
import org.kevoree.planrouge.Intervention;
import org.kevoree.planrouge.PlanrougeFactory;
import org.kevoree.planrouge.Victime;
import org.kevoree.planrouge.factory.MainFactory;

/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 10/07/13
 * Time: 11:11
 * To change this template use File | Settings | File Templates.
 */
public class DemoModel {


    public static  void main(String argv[]) {
        PlanrougeFactory f = new MainFactory().getPlanrougeFactory();

        ContainerRoot containerRoot =f.createContainerRoot();

        StoreManager store = new StoreManager(containerRoot);
        store.addReplica("tcp://10.0.4.1:6666");

        store.open();


        Intervention intervention =  f.createIntervention();
        intervention.setId("393939");
        intervention.setDescription("un train est rentré dans un avion en plein vol");


        Victime victime =  f.createVictime();
        victime.setId("tagid");
        victime.setNom("briand");
        victime.setPrenom("clement");


        containerRoot.addInterventions(intervention);


        intervention.addVictimes(victime);
        store.close();


    }
}
