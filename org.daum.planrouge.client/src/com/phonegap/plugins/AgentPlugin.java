package com.phonegap.plugins;

import org.apache.cordova.api.CallbackContext;
import org.apache.cordova.api.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.phonegap.plugins.manager.ComManager;

import android.util.Log;

public class AgentPlugin extends CordovaPlugin {

	public boolean execute(String action, JSONArray data, CallbackContext callbackContext) throws JSONException {

		if (action.equalsIgnoreCase("getAgent")) {

			JSONObject jObject = new JSONObject();
			Log.i("AgentPlugin", "getAgent " + data.get(0));
			jObject.put("type", "AdapterAgent");
			jObject.put("matricule", data.get(0));

			if (data.get(0).toString().length() == 0) {
				callbackContext.success("false");
			} else {
//				boolean agentexist = checkAgent(jObject.toString());
//
//				if (agentexist) {
//					Log.e("NFCPlugin", "SUCCESS login AGENT");
					callbackContext.success("true");
//				} else {
//					Log.e("NFCPlugin", "ERROR login AGENT");
//					callbackContext.success("false");
//				}
			}
		} else {
			return false;

		}
		return true;
	}


	public boolean checkAgent(String json) {
		Log.d("AgentPlugin", "CHECK_AGENT 1");
		ComManager comManager = ComManager.getInstance();
		// Adding message to Client
		comManager.getConsumerWebSocketGet().addMessage(json);
		
		Log.d("AgentPlugin", "CHECK_AGENT");
		try {
			JSONObject response = comManager.getConsumerWebSocketGet().getClient().getResponse("agent");
			Log.d("AgentPlugin", "GET RESPONSE");
			if (response == null) {
				return false;
			} else {
				if (response.has("result")) {
					if (!response.get("result").toString().equals("undefined")) {
						return true;
					}

				}
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
