package org.daum.planrouge.server.websocket;

import org.daum.planrouge.server.adapter.model.AdapterFactory;
import org.daum.planrouge.server.adapter.model.IAdapter;
import org.kevoree.planrouge.ContainerRoot;
import org.kevoree.planrouge.Victime;
import org.webbitserver.WebSocketConnection;

import java.lang.reflect.Field;

/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 17/07/13
 * Time: 13:59
 * To change this template use File | Settings | File Templates.
 */
public class MessageHandler {
    private ContainerRoot root;

    public MessageHandler(ContainerRoot root) {
        this.root= root;
    }

    public  void merge(Object obj, Object update)   {
        if(!obj.getClass().isAssignableFrom(update.getClass())){
            return;
        }
        Field[] fields = obj.getClass().getDeclaredFields();
        for(Field f: fields){
            f.setAccessible(true);
            try {
                if(f.get(obj) != null && (f.get(update).equals("") |f.get(update).equals(0))){
                    f.set(update, f.get(obj));
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }


    //todo 
    public void process(WebSocketConnection connection,Object obj) {

        IAdapter adapter = (IAdapter)obj;
        switch (adapter.getType()){

            case AdapterCategorie:


                break;

            case AdapterGpsPoint:

                break;


            case AdapterIntervention:


                break;


            case AdapterVictime:

                Victime victime = (Victime)obj;
                Victime vicmodel =   root.findInterventionsByID("1").findVictimesByID(victime.getId());

                if(vicmodel !=null)
                {
                    merge(vicmodel,vicmodel);
                } else
                {
                    root.findInterventionsByID("1").addVictimes(victime);
                }

                connection.send("ack");
                break;



        }
    }
}
