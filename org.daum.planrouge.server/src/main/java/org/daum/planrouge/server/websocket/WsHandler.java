package org.daum.planrouge.server.websocket;

import org.webbitserver.BaseWebSocketHandler;

/**
 * Created with IntelliJ IDEA.
 * User: cbriand
 * Date: 16/09/13
 * Time: 10:43
 * To change this template use File | Settings | File Templates.
 */

public interface WsHandler {

    public void addHandler(String name,BaseWebSocketHandler webSocketChannel);



    public void removeHandler(String name);
}
