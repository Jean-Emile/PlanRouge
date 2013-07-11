package org.daum.planrouge.server.adapter;

import org.json.JSONException;
import org.json.JSONObject;
import org.kevoree.planrouge.ContainerRoot;
import org.kevoree.planrouge.GpsPoint;
import org.kevoree.planrouge.PlanrougeFactory;

import java.util.Iterator;

/**
 * Created with IntelliJ IDEA.
 * User: cbriand
 * Date: 11/07/13
 * Time: 14:22
 * To change this template use File | Settings | File Templates.
 */
public class AdapterGpsPoint {

    private PlanrougeFactory planrougeFactory;
    private ContainerRoot containerRoot;


    public AdapterGpsPoint(PlanrougeFactory planrougeFactory, ContainerRoot containerRoot) {
        this.planrougeFactory = planrougeFactory;
        this.containerRoot = containerRoot;
    }

    // Parse JSONObject to GPSPoint
    public GpsPoint parseJsonToGPS(JSONObject jsonGPSPoint) throws JSONException {

        GpsPoint gpsPoint = this.planrougeFactory.createGpsPoint();
        //on crée un iterator pour l'objet identity
        Iterator iteratorNewObject = jsonGPSPoint.keys();
        while (iteratorNewObject.hasNext()) {
            String keyGPS = iteratorNewObject.next().toString();
            String value = jsonGPSPoint.getString(keyGPS);
            if (keyGPS.equals("latitude")) {
                gpsPoint.setLatitude(Integer.parseInt(value));
            } else if (keyGPS.equals("longitude")) {
                gpsPoint.setLongitude(Integer.parseInt(value));
            } else if (keyGPS.equals("precision")) {
                gpsPoint.setMode(Integer.parseInt(value));
            } else if (keyGPS.equals("heure")) {
                gpsPoint.setHorodatage(value);
            }
        }


        return gpsPoint;
    }

}
