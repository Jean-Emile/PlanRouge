package org.daum.planrouge.server.adapter.model;

import org.json.JSONException;
import org.json.JSONObject;
import org.kevoree.planrouge.ContainerRoot;
import org.kevoree.planrouge.GpsPoint;
import org.kevoree.planrouge.PlanrougeFactory;
import org.kevoree.planrouge.factory.MainFactory;

/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 15/07/13
 * Time: 10:33
 * To change this template use File | Settings | File Templates.
 */
public class Tester {


    public static  void main(String argv[]) {


        try {

            AdapterFactory adapterFactory = new AdapterFactory();


            JSONObject jsonGPS = adapterFactory.build(adapterFactory.getFactory().createGpsPoint());


            GpsPoint point =   adapterFactory.build(jsonGPS);

            System.out.println(point.getLatitude());



        } catch (JSONException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }


    }
}
