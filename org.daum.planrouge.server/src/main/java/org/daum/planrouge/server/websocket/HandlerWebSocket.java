package org.daum.planrouge.server.websocket;

import org.daum.planrouge.server.adapter.model.AdapterFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.kevoree.log.Log;
import org.kevoree.planrouge.ContainerRoot;
import org.kevoree.planrouge.Victime;
import org.kevoree.planrouge.events.ModelEvent;
import org.kevoree.planrouge.events.ModelTreeListener;
import org.webbitserver.BaseWebSocketHandler;
import org.webbitserver.WebSocketConnection;

/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 17/07/13
 * Time: 13:52
 * To change this template use File | Settings | File Templates.
 */
public  class HandlerWebSocket extends BaseWebSocketHandler {
    private Peers peers=null;
    private AdapterFactory adapterFactory=null;
    private MessageHandler msg =null;
    private  ACTION current;
    private int connexion;
    public enum ACTION {
        PUT,
        GET,
        GETALL,
        DELETE
    }

    public HandlerWebSocket(final AdapterFactory adapterFactory,final ContainerRoot root,ACTION action){
        peers = new Peers();
        this.adapterFactory = adapterFactory;
        msg = new MessageHandler(root);
        this.current=action;

        root.addModelTreeListener(new ModelTreeListener() {
            @Override
            public void elementChanged(ModelEvent modelEvent) {

                Log.info(modelEvent.toString()+"   "+modelEvent.getElementAttributeName()+"   "+modelEvent.getElementAttributeType());
                Log.info("AdapterIntervention ::: GETALL");
                JSONArray mJSONArray = new JSONArray();
                for (int i = 0; i<root.getInterventions().size();i++){
                    try {
                        mJSONArray.put(adapterFactory.build(root.getInterventions().get(i)));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                JSONObject jsonObject = new JSONObject();

                try {
                    jsonObject.put("type","AdapterIntervention");
                    jsonObject.put("arrayInterventions",mJSONArray);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                peers.broadcast(jsonObject.toString());
            }
        });

    }
    public void onOpen(WebSocketConnection connection) {
        //connection.send("Other connection :: "+connexion );
        connexion++;
        peers.addPeer(connection);
        Log.info("START SOCKET  :  "+current.name());
    }

    public void onClose(WebSocketConnection connection) {
        connexion--;
        peers.removePeer(connection);
    }

    public void onMessage(WebSocketConnection connection, String message) throws JSONException {
        Log.debug("Message from peer "+message);
        JSONObject jsonObject = new JSONObject(message);
        msg.process(connection,adapterFactory,jsonObject,current);
    }
}
