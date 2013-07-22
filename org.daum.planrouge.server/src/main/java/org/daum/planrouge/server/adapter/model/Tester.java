package org.daum.planrouge.server.adapter.model;


import org.daum.planrouge.server.store.ObjectSpace;
import org.kevoree.planrouge.*;


/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 15/07/13
 * Time: 10:33
 * To change this template use File | Settings | File Templates.
 */
public class Tester {



    public static  void main(String argv[]) throws IllegalAccessException, InterruptedException {

        AdapterFactory adapterFactory = new AdapterFactory();





        PlanrougeFactory factory = adapterFactory.getFactory();

        ContainerRoot root = factory.createContainerRoot();



        ObjectSpace objectSpace = new ObjectSpace(root);

        objectSpace.addReplica("tcp://10.0.4.1:6666");

        objectSpace.open("kevoree");





Thread.sleep(10000);




        objectSpace.close();






    }
}
