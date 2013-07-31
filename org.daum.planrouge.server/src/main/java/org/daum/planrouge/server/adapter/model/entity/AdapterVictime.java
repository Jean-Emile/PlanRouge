package org.daum.planrouge.server.adapter.model.entity;

import org.daum.planrouge.server.adapter.model.AbstractAdapter;
import org.daum.planrouge.server.adapter.model.AdapterFactory;
import org.daum.planrouge.server.adapter.model.Entities;
import org.daum.planrouge.server.adapter.model.IAdapter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.kevoree.log.Log;
import org.kevoree.planrouge.*;
import org.kevoree.planrouge.container.KMFContainer;
import org.kevoree.planrouge.impl.VictimeImpl;

import java.util.Iterator;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: cbriand
 * Date: 15/07/13
 * Time: 15:17
 * To change this template use File | Settings | File Templates.
 */
public class AdapterVictime implements IAdapter {

    private AdapterFactory adapterFactory;

    public AdapterVictime() {
        this.adapterFactory = AdapterFactory.getInstance();
    }

    @Override
    public JSONObject build(KMFContainer container) throws JSONException {

        JSONObject jsonVictime = new JSONObject();
        Victime victime = (Victime) container;

      //  Intervention intervention = (Intervention) container.eContainer();

        jsonVictime.put("type", getType());
        jsonVictime.put("nom", victime.getNom());
        jsonVictime.put("prenom", victime.getPrenom());
        jsonVictime.put("id", victime.getId());
      //  jsonVictime.put("intervention", adapterFactory.build(intervention));
        jsonVictime.put("age", victime.getAge());
        jsonVictime.put("dateNaissance", victime.getDateNaissance());
        jsonVictime.put("sexe", victime.getSexe());
        if (victime.getIntervenants() != null) {
            JSONArray arrayAgents = new JSONArray();
            List<Agent> listAgents = victime.getIntervenants();
            for (int i = 0; i < listAgents.size(); i++) {
                arrayAgents.put(adapterFactory.build(listAgents.get(i)));

            }
            jsonVictime.put("agents", arrayAgents);
        }
        if (victime.getPosRef() != null) {
            //Poistion de Référence
            JSONObject position = new JSONObject();

            if (victime.getPosRef() instanceof GpsPoint) {
                //Position GPS
                GpsPoint gpsPoint = (GpsPoint) victime.getPosRef();
                JSONObject jGpsPoint = adapterFactory.build(gpsPoint);

                position.put("gpsPoint", jGpsPoint);

            } else if (victime.getPosRef() instanceof PositionCivil) {
                //Position Civile
                PositionCivil positionCivile = (PositionCivil) victime.getPosRef();

                JSONObject jPositionCivile = adapterFactory.build(positionCivile);

                position.put("positionCivile", jPositionCivile);
            }


            jsonVictime.put("posRef", position);
        }

        if (victime.getPosDestination() != null) {
            //Destination
            List<Position> listGpsPoint = victime.getPosDestination();
            JSONArray arrayGpsPoint = new JSONArray();

            for (int i = 0; i < listGpsPoint.size(); i++) {
                Position element = listGpsPoint.get(i);
                if (element instanceof GpsPoint) {
                    //Position GPS

                    JSONObject jGpsPoint = adapterFactory.build(element);

                    arrayGpsPoint.put(new JSONObject().put("gpsPoint", jGpsPoint));

                } else if (element instanceof PositionCivil) {
                    //Position Civile

                    JSONObject jPositionCivile = adapterFactory.build(element);

                    arrayGpsPoint.put(new JSONObject().put("positionCivile", jPositionCivile));
                }
            }


            jsonVictime.put("posDestination", arrayGpsPoint);

        }
        if (victime.getPriorite() != null) {
            //Priority
            JSONObject jPriorite = adapterFactory.build(victime.getPriorite());

            jsonVictime.put("categorie", jPriorite);
        }

        Log.debug("VICTIME to JSON " + jsonVictime);

        return jsonVictime;

    }

    @Override
    public <T> T build(JSONObject json) throws JSONException {

        Victime victime = adapterFactory.getFactory().createVictime();

        if (json.has("prenom")) {
            Log.info("has prenom");
            victime.setPrenom(json.getString("prenom").toString());
        }
        if (json.has("nom")) {
            Log.info("has nom");
            victime.setNom(json.getString("nom").toString());
        }
        if (json.has("id")) {
            Log.info("has id");
            victime.setId(json.getString("id").toString());
        } else {
            victime.setId("");
        }
        if (json.has("dateNaissance")) {
            Log.info("has dateNaissance");
            victime.setDateNaissance(json.getString("dateNaissance").toString());
        }
        if (json.has("sexe")) {
            Log.info("has sexe");
            victime.setSexe(json.getString("sexe").toString());
        }
        if (json.has("age")) {
            Log.info("has age");
            victime.setAge(json.getInt("age"));
        }

        if (json.has("categorie")) {
            Log.info("has category");
            victime.setPriorite((Categorie) adapterFactory.build(json.getJSONObject("categorie")));
        }

        if (json.has("posRef")) {
            Log.info("has positionRef");
            if (json.getJSONObject("posRef").has("gpsPoint")) {
                Log.info("has gpsPoint");
                victime.setPosRef((GpsPoint) adapterFactory.build(json.getJSONObject("posRef").getJSONObject("gpsPoint")));
            }
            if (json.getJSONObject("posRef").has("positionCivile")) {
                Log.info("has positionCivile");
                victime.setPosRef((PositionCivil) adapterFactory.build(json.getJSONObject("posRef").getJSONObject("positionCivile")));
            }

        }
        if (json.has("posDestination")) {
            Log.info("has positionDestination");
            JSONArray array = json.getJSONArray("posDestination");
            for (int i = 0; i < array.length(); i++) {
                if (array.getJSONObject(i).has("gpsPoint")) {
                    Log.info("has gpsPoint");
                    victime.addPosDestination((GpsPoint) adapterFactory.build(array.getJSONObject(i).getJSONObject("gpsPoint")));
                }
                if (array.getJSONObject(i).has("positionCivile")) {
                    Log.info("has positionCivile");
                    victime.addPosDestination((PositionCivil) adapterFactory.build(array.getJSONObject(i).getJSONObject("positionCivile")));
                }
            }
        }

//        if (json.has("intervention")) {
//            Log.info("has intervention");
//            victime.setIntervention((Intervention) adapterFactory.build(json.getJSONObject("intervention")));
//        }

        if (json.has("agent")) {
            Log.info("has agent");
            victime.addIntervenants((Agent) adapterFactory.build(json.getJSONObject("agent")));

        }
        return (T) victime;


    }

    @Override
    public Entities getType() {
        return Entities.AdapterVictime;
    }


}
