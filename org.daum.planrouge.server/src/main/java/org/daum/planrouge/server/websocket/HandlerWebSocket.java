package org.daum.planrouge.server.websocket;

import org.daum.planrouge.server.adapter.model.AdapterFactory;
import org.json.JSONException;
import org.json.JSONObject;
import org.kevoree.log.Log;
import org.kevoree.planrouge.ContainerRoot;
import org.kevoree.planrouge.Victime;
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
    private AdapterFactory adapterFactory;
    private MessageHandler msg =null;

    public HandlerWebSocket(AdapterFactory adapterFactory,ContainerRoot root){
        peers = new Peers();
        this.adapterFactory = adapterFactory;
        msg = new MessageHandler(root);

    }
    public void onOpen(WebSocketConnection connection) {
        peers.addPeer(connection);
    }

    public void onClose(WebSocketConnection connection) {
        peers.removePeer(connection);
    }

    public void onMessage(WebSocketConnection connection, String message) throws JSONException {
        Log.debug("Message from peer");
        JSONObject jsonObject = new JSONObject(message);
        msg.process(connection,adapterFactory.build(jsonObject));
    }
}
