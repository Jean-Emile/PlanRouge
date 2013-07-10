package org.daum.planrouge.server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.kevoree.planrouge.*;
import org.kevoree.log.Log;
import org.webbitserver.*;

import java.util.Iterator;

/**
 * Created with IntelliJ IDEA.
 * User: cbriand
 * Date: 09/07/13
 * Time: 13:39
 * To change this template use File | Settings | File Templates.
 */
public class AddVictimHandler extends BaseWebSocketHandler {

    private int connectionCount;
    private int i = 0;
    private PlanrougeFactory planrougeFactory;
    private ContainerRoot containerRoot;


    public AddVictimHandler(PlanrougeFactory planrougeFactory, ContainerRoot containerRoot, int i) {
        this.planrougeFactory = planrougeFactory;
        this.containerRoot = containerRoot;
        this.i = i;
    }

    public void onOpen(WebSocketConnection connection) {
        System.out.println("Nouvelle connexion");
        connection.send("AddVictimHandler ::: " + connectionCount + " other connections active");
        connectionCount++;
    }

    public void onClose(WebSocketConnection connection) {
        System.out.println("AddVictimHandler ::: Connexion fermée ");
        connectionCount--;

    }

    public void onMessage(WebSocketConnection connection, String message) throws JSONException {
        Log.debug("AddVictimHandler ::: ON_MESSAGE");

        Victime victime = parseJsonToVictime(message);
        if (victime != null) {
            containerRoot.findInterventionsByID("1").addVictimes(victime);
            connection.send("Victime àjouté à la base de données");
        } else {
            connection.send("Erreur");
        }
    }

    private Victime parseJsonToVictime(String jsonMessage) throws JSONException {

        JSONObject jNewObject = new JSONObject(jsonMessage);
        Victime victime = this.planrougeFactory.createVictime();

        //iterarateur pour parcourir les différentes valeurs du message reçu
        Iterator iterator = jNewObject.keys();
        while (iterator.hasNext()) {
            // On récupère une clé du message
            String key = iterator.next().toString();
            //Si la clé correspond à Identity
            if (key.equals("identity")) {
                // On récupére l'objet JSON identity
                JSONObject IdentityObject = jNewObject.getJSONObject(key);
                //on crée un iterator pour l'objet identity
                Iterator iteratorNewObject = IdentityObject.keys();
                while (iteratorNewObject.hasNext()) {
                    String keyIdentity = iteratorNewObject.next().toString();
                    String value = IdentityObject.getString(keyIdentity);
                    if (keyIdentity.equals("id")) {
                        victime.setId(value);
                    } else if (keyIdentity.equals("nom")) {
                        victime.setNom(value);
                    } else if (keyIdentity.equals("prenom")) {
                        victime.setPrenom(value);
                    }
                }
            } else if (key.equals("positionRef")) {
                JSONObject jsonPositionRef = jNewObject.getJSONObject(key);
                Iterator iteratorPositionRef = jsonPositionRef.keys();
                while (iteratorPositionRef.hasNext()) {
                    String keyPositionRef = iterator.next().toString();
                    if (keyPositionRef.equals("GPSPoint")) {
                        // On récupére l'objet JSON identity
                        JSONObject jsonGPSPoint = jNewObject.getJSONObject(key);
                        GpsPoint gpsPoint = parseJsonToGPS(jsonGPSPoint);
                        if (gpsPoint != null) {
                            victime.setPosRef(gpsPoint);
                        }
                    } else if (keyPositionRef.equals("PositionCivile")) {
                        // On récupére l'objet JSON identity
                        JSONObject jsonGPSPoint = jNewObject.getJSONObject(key);
                        PositionCivil positionCivile = parseJsonToPositionCivile(jsonGPSPoint);
                        if (positionCivile != null) {
                            victime.setPosRef(positionCivile);
                        }
                    }
                }
            } else if (key.equals("positionDestination")) {
                JSONObject jsonpositionDestination = jNewObject.getJSONObject(key);
                Iterator iteratorpositionDestination = jsonpositionDestination.keys();
                while (iteratorpositionDestination.hasNext()) {
                    String keyPositionRef = iterator.next().toString();
                    if (keyPositionRef.equals("GPSPoint")) {
                        // On récupére l'objet JSON identity
                        JSONObject jsonGPSPoint = jNewObject.getJSONObject(key);
                        GpsPoint gpsPoint = parseJsonToGPS(jsonGPSPoint);
                        if (gpsPoint != null) {
                            victime.addPosDestination(gpsPoint);
                        }
                    } else if (keyPositionRef.equals("PositionCivile")) {
                        // On récupére l'objet JSON identity
                        JSONObject jsonGPSPoint = jNewObject.getJSONObject(key);
                        PositionCivil positionCivile = parseJsonToPositionCivile(jsonGPSPoint);
                        if (positionCivile != null) {
                            victime.addPosDestination(positionCivile);
                        }
                    }
                }
            } else if (key.equals("categorie")) {
                // On récupére l'objet JSON identity
                JSONObject jsonGPSPoint = jNewObject.getJSONObject(key);
                Categorie categorie = parseJsonToCategorie(jsonGPSPoint);
                if (categorie != null) {
                    victime.setPriorite(categorie);
                }
            } else {
                return null;
            }
        }


        return victime;
    }


    private Categorie parseJsonToCategorie(JSONObject jsonCategorie) throws JSONException {
        Categorie categorie = this.planrougeFactory.createCategorie();

        Iterator iteratorNewObject = jsonCategorie.keys();
        while (iteratorNewObject.hasNext()) {
            String keyPositionCivile = iteratorNewObject.next().toString();
            String value = jsonCategorie.getString(keyPositionCivile);
            if (keyPositionCivile.equals("code")) {
                categorie.setId(value);
            }
        }
        return categorie;
    }

    private PositionCivil parseJsonToPositionCivile(JSONObject jsonPositionCivile) throws JSONException {
        PositionCivil positionCivile = this.planrougeFactory.createPositionCivil();

        Iterator iteratorNewObject = jsonPositionCivile.keys();
        while (iteratorNewObject.hasNext()) {
            String keyPositionCivile = iteratorNewObject.next().toString();
            String value = jsonPositionCivile.getString(keyPositionCivile);
            if (keyPositionCivile.equals("cp")) {
                positionCivile.setCp(value);
            } else if (keyPositionCivile.equals("nomRue")) {
                positionCivile.setNomRue(value);
            } else if (keyPositionCivile.equals("numeroRue")) {
                positionCivile.setNumeroRue(value);
            } else if (keyPositionCivile.equals("pays")) {
                positionCivile.setPays(value);
            } else if (keyPositionCivile.equals("heure")) {
                positionCivile.setHorodatage(value);
            }
        }
        return positionCivile;
    }

    private GpsPoint parseJsonToGPS(JSONObject jsonGPSPoint) throws JSONException {

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

