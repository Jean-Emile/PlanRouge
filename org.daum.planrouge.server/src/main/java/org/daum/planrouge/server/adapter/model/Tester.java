package org.daum.planrouge.server.adapter.model;

import org.json.JSONException;
import org.json.JSONObject;
import org.kevoree.planrouge.*;
import org.kevoree.planrouge.factory.MainFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 15/07/13
 * Time: 10:33
 * To change this template use File | Settings | File Templates.
 */
public class Tester {



    public static  void merge(Object obj, Object update) throws IllegalAccessException {
        if(!obj.getClass().isAssignableFrom(update.getClass())){
            return;
        }

        Field[] fields = obj.getClass().getDeclaredFields();

        for(Field f: fields){
            f.setAccessible(true);
            if(f.get(obj) != null && f.get(update).toString().isEmpty() ){
                f.set(update, f.get(obj));
            }
        }
    }
    public static  void main(String argv[]) throws IllegalAccessException {




        AdapterFactory adapterFactory = new AdapterFactory();




        Victime victime = adapterFactory.getFactory().createVictime();

        victime.setPrenom("jed");
        victime.setAge(10);

        Victime victime2 = adapterFactory.getFactory().createVictime();
        victime2.setNom("dartois");
        victime.setPrenom("jed2");


        merge(victime, victime2);
        System.out.println(victime2.getPrenom()+" "+victime2.getNom()+" ");



    }
}
