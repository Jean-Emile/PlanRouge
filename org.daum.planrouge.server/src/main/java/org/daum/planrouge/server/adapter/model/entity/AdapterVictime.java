package org.daum.planrouge.server.adapter.model.entity;

import org.daum.planrouge.server.adapter.model.AbstractAdapter;
import org.daum.planrouge.server.adapter.model.AdapterFactory;
import org.json.JSONException;
import org.json.JSONObject;
import org.kevoree.log.Log;
import org.kevoree.planrouge.*;
import org.kevoree.planrouge.container.KMFContainer;
import org.kevoree.planrouge.impl.VictimeImpl;

import java.util.Iterator;

/**
 * Created with IntelliJ IDEA.
 * User: cbriand
 * Date: 15/07/13
 * Time: 15:17
 * To change this template use File | Settings | File Templates.
 */
public class AdapterVictime extends AbstractAdapter {

    AdapterFactory adapterFactory;

    public AdapterVictime(AdapterFactory adapterFactory) {
        this.adapterFactory = adapterFactory;
    }

    @Override
    public JSONObject build(KMFContainer container) throws JSONException {

        JSONObject jsonVictime = new JSONObject();
        Victime victime = (Victime) container;
        jsonVictime.put("type", container.getClass().getName());
        //Identité
        JSONObject jsonIdentity = new JSONObject();

        jsonIdentity.put("nom", victime.getNom());
        jsonIdentity.put("prenom", victime.getPrenom());
        jsonIdentity.put("id", victime.getId());
        jsonIdentity.put("age", victime.getAge());
        jsonIdentity.put("dateNaissance", victime.getDateNaissance());

        jsonVictime.put("identite", jsonIdentity);


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
            JSONObject position = new JSONObject();

            if (victime.getPosRef() instanceof GpsPoint) {
                //Position GPS
                GpsPoint gpsPoint = (GpsPoint) victime.getPosDestination();
                JSONObject jGpsPoint = adapterFactory.build(gpsPoint);

                position.put("gpsPoint", jGpsPoint);

            } else if (victime.getPosRef() instanceof PositionCivil) {
                //Position Civile
                PositionCivil positionCivile = (PositionCivil) victime.getPosDestination();
                JSONObject jPositionCivile = adapterFactory.build(positionCivile);

                position.put("positionCivile", jPositionCivile);
            }

            jsonVictime.put("positionDestination", position);
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

        Victime victime =  adapterFactory.getFactory().createVictime();

              // todo refactor
        //iterarateur pour parcourir les différentes valeurs du message reçu
        Iterator iterator = json.keys();
        while (iterator.hasNext()) {
            // On récupère une clé du message
            String key = iterator.next().toString();
             // todo jed
            //Si la clé correspond à Identity
            if (key.equals("identity")) {
                // Get Identity Object
                JSONObject IdentityObject = null;
                try {
                    IdentityObject = json.getJSONObject(key);
                } catch (JSONException e) {
                    Log.debug(e.getMessage());

                    return null;
                }
                //on crée un iterator pour l'objet identity
                Iterator iteratorNewObject = IdentityObject.keys();
                while (iteratorNewObject.hasNext()) {
                    String keyIdentity = iteratorNewObject.next().toString();
                    String value = null;
                    try {
                        value = IdentityObject.getString(keyIdentity);
                    } catch (JSONException e) {
                        Log.debug(e.getMessage());
                        return null;
                    }
                    if (keyIdentity.equals("id")) {
                        victime.setId(value);
                    } else if (keyIdentity.equals("nom")) {
                        victime.setNom(value);
                    } else if (keyIdentity.equals("prenom")) {
                        victime.setPrenom(value);
                    } else if (keyIdentity.equals("age")) {
                        victime.setAge(Integer.parseInt(value));
                    } else if (keyIdentity.equals("sexe")) {
                        victime.setSexe(value);
                    } else if (keyIdentity.equals("dateNaissance")) {
                        victime.setDateNaissance(value);
                    }
                }
            } else if (key.equals("positionRef")) {

                JSONObject jsonPositionRef = null;
                try {
                    jsonPositionRef = json.getJSONObject(key);
                } catch (JSONException e) {
                    Log.debug(e.getMessage());
                    return null;
                }
                Iterator iteratorPositionRef = jsonPositionRef.keys();
                while (iteratorPositionRef.hasNext()) {
                    String keyPositionRef = iteratorPositionRef.next().toString();

                    if (keyPositionRef.equals("gpsPoint")) {
                        // On récupére l'objet JSON GPSPoint
                        JSONObject jsonGPSPoint = null;
                        try {
                            jsonGPSPoint = jsonPositionRef.getJSONObject(keyPositionRef);
                        } catch (JSONException e) {
                            Log.debug(e.getMessage());
                            return null;
                        }

                        GpsPoint gpsPoint = adapterFactory.build(jsonGPSPoint);

                        if (gpsPoint != null) {
                            victime.setPosRef(gpsPoint);
                        }
                    } else if (keyPositionRef.equals("positionCivile")) {
                        // On récupére l'objet JSON PositionCivile
                        JSONObject jsonGPSPoint = null;
                        try {
                            jsonGPSPoint = jsonPositionRef.getJSONObject(keyPositionRef);
                        } catch (JSONException e) {
                            Log.debug(e.getMessage());
                            return null;
                        }

                        PositionCivil positionCivile = adapterFactory.build(jsonGPSPoint);

                        if (positionCivile != null) {
                            victime.setPosRef(positionCivile);
                        }
                    }
                }
            } else if (key.equals("positionDestination")) {
                JSONObject jsonPositionDestination = null;
                try {
                    jsonPositionDestination = json.getJSONObject(key);
                } catch (JSONException e) {
                    Log.debug(e.getMessage());
                    return null;
                }
                Iterator iteratorpositionDestination = jsonPositionDestination.keys();
                while (iteratorpositionDestination.hasNext()) {
                    String keyPositionRef = iteratorpositionDestination.next().toString();
                    if (keyPositionRef.equals("gpsPoint")) {
                        // On récupére l'objet JSON GPSPoint
                        JSONObject jsonGPSPoint = null;
                        try {
                            jsonGPSPoint = jsonPositionDestination.getJSONObject(keyPositionRef);
                        } catch (JSONException e) {
                            Log.debug(e.getMessage());
                            return null;
                        }

                        Position gpsPoint = adapterFactory.build(jsonGPSPoint);
                        if (gpsPoint != null) {
                            victime.addPosDestination(gpsPoint);
                        }
                    } else if (keyPositionRef.equals("positionCivile")) {
                        // On récupére l'objet JSON PositionCivile
                        JSONObject jsonGPSPoint = null;
                        try {
                            jsonGPSPoint = jsonPositionDestination.getJSONObject(keyPositionRef);
                        } catch (JSONException e) {
                            Log.debug(e.getMessage());
                            return null;
                        }

                        Position positionCivile = adapterFactory.build(jsonGPSPoint);
                        if (positionCivile != null) {
                            victime.addPosDestination(positionCivile);
                        }
                    }
                }
            } else if (key.equals("categorie")) {
                // On récupére l'objet JSON Categorie
                JSONObject jsonPriorite = null;
                try {
                    jsonPriorite = json.getJSONObject(key);
                } catch (JSONException e) {
                    Log.debug(e.getMessage());
                    return null;
                }
                Categorie categorie = adapterFactory.build(jsonPriorite);
                if (categorie != null) {
                    victime.setPriorite(categorie);
                }
            } else if(key.equals("type")){

            } else {
                return null;
            }
        }

        if (victime.getId() == null || victime.getId().equals("")) {
            Log.info(victime.getId());
            return null;
        }

        return (T) victime;
    }

    @Override
    public AdapterFactory.Adapters getType() {
        return AdapterFactory.Adapters.AdapterVictime;
    }




}
