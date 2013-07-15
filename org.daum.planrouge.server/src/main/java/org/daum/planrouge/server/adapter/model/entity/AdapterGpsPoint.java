package org.daum.planrouge.server.adapter.model.entity;

import org.daum.planrouge.server.adapter.model.AbstractAdapter;
import org.json.JSONException;
import org.json.JSONObject;
import org.kevoree.log.Log;
import org.kevoree.planrouge.ContainerRoot;
import org.kevoree.planrouge.GpsPoint;
import org.kevoree.planrouge.PlanrougeFactory;
import org.kevoree.planrouge.container.KMFContainer;
import org.kevoree.planrouge.impl.GpsPointImpl;

import java.util.Iterator;

/**
 * Created with IntelliJ IDEA.
 * User: cbriand
 * Date: 11/07/13
 * Time: 14:22
 * To change this template use File | Settings | File Templates.
 */
public class AdapterGpsPoint extends AbstractAdapter {

    @Override
    public JSONObject build(KMFContainer container) throws JSONException {
        JSONObject jsonGps = new JSONObject();
        GpsPoint  gpsPoint =    (GpsPoint)  container;
        jsonGps.put("latitude", gpsPoint.getLatitude());
        jsonGps.put("longitude", gpsPoint.getLongitude());
        jsonGps.put("precision", gpsPoint.getMode());
        jsonGps.put("heure", gpsPoint.getHorodatage());
        jsonGps.put("type", container.getClass().getName());
        return jsonGps;
    }

    // Parse JSONObject to GPSPoint
    @Override
    public  <T> T  build(JSONObject json) {
        GpsPoint gpsPoint = getModelfactory().createGpsPoint();
        //on crée un iterator pour l'objet identity
        Iterator iteratorNewObject = json.keys();
        while (iteratorNewObject.hasNext()) {
            String keyGPS = iteratorNewObject.next().toString();
            String value = null;
            try {
                value = json.getString(keyGPS);
            } catch (JSONException e) {
                Log.debug(e.getMessage());
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                return null;
            }
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

        return (T) gpsPoint;
    }

    @Override
    public String getType() {
        return GpsPointImpl.class.getName();
    }

}
