package org.daum.planrouge.server.adapter;

import org.json.JSONException;
import org.json.JSONObject;
import org.kevoree.planrouge.Categorie;
import org.kevoree.planrouge.ContainerRoot;
import org.kevoree.planrouge.PlanrougeFactory;

import java.util.Iterator;

/**
 * Created with IntelliJ IDEA.
 * User: cbriand
 * Date: 11/07/13
 * Time: 14:22
 * To change this template use File | Settings | File Templates.
 */
public class AdapterCategorie {
    private PlanrougeFactory planrougeFactory;
    private ContainerRoot containerRoot;


    public AdapterCategorie(PlanrougeFactory planrougeFactory, ContainerRoot containerRoot){
        this.planrougeFactory = planrougeFactory;
        this.containerRoot = containerRoot;
    }



    // Parse JSONObject to Categorie
    public Categorie parseJsonToCategorie(JSONObject jsonCategorie) throws JSONException {
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



}
