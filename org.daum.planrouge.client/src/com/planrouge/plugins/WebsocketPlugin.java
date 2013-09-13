package com.planrouge.plugins;

import org.apache.cordova.api.CallbackContext;
import org.apache.cordova.api.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;

import android.util.Log;

import com.planrouge.adapter.AdapterFactory;
import com.planrouge.plugins.manager.ComManager;
import com.planrouge.websocket.ConsumerWebSocket;

public class WebsocketPlugin extends CordovaPlugin {

	private String ipAddress;

	@Override
	public boolean execute(String action, JSONArray data, CallbackContext callbackContext) throws JSONException {
		Log.e("WebSocketPlugin", "ipAddress");
		ComManager comManager = ComManager.getInstance();

		if (action.equalsIgnoreCase("ipAddress")) {
			ipAddress = data.getString(0);
			comManager.setConsumerWebSocketAdd(new ConsumerWebSocket(ipAddress, 8080, "add"));
			comManager.setConsumerWebSocketGet(new ConsumerWebSocket(ipAddress, 8080, "get"));
			comManager.setAdapterFactory(new AdapterFactory(comManager.getConsumerWebSocketAdd()));
			callbackContext.success();
		} else {
			// invalid action
			return false;
		}

		return false;

	}

}
