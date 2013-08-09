package com.phonegap.websocket;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

import org.apache.cordova.api.CallbackContext;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.phonegap.plugins.nfc.Common;

public class GetAgent extends Thread{

	List<BasicNameValuePair> extraHeaders = Arrays.asList(new BasicNameValuePair("Cookie", "session=abcd"));
	JSONArray data;
	CallbackContext callbackContext;
	ConsumerWebSocket consumerWebSocketGet;
	public void getAgent(JSONArray data, CallbackContext callbackContext, ConsumerWebSocket consumerWebSocketGet) throws JSONException{
		this.data = data;
		this.callbackContext= callbackContext;
		this.consumerWebSocketGet = consumerWebSocketGet;
		
		this.start();

		
	}
	
	public void run(){
		JSONObject jObject = new JSONObject();
		try {
			jObject.put("type", "AdapterAgent");
			jObject.put("matricule", data.get(0));
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		WebSocketClient client =new WebSocketClient(URI.create("http://" + "192.168.1.101" + ":" + "8080" + "/" + "get"), new WebSocketClient.Listener() {
			String TAG = "WebSocketClient";

			@Override
			public void onConnect() {
				Log.d(TAG, "Connected!");
			}

			@Override
			public void onMessage(String message) {
				Log.d(TAG, String.format("Got string message! %s", message));
				JSONObject jObject = null;
				try {
					jObject = new JSONObject(message);

					if (jObject.has("type")) {
						if (jObject.get("type").equals("undefined")) {
							callbackContext.error("Agent Inconnu");
						} else {
							callbackContext.success();
						}
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

			@Override
			public void onMessage(byte[] data) {
				Log.d(TAG, String.format("Got binary message! %s", Common.byte2HexString(data)));

			}

			@Override
			public void onDisconnect(int code, String reason) {
				Log.d(TAG, String.format("Disconnected! Code: %d Reason: %s", code, reason));
				callbackContext.error("Agent Inconnu");
			}

			@Override
			public void onError(Exception error) {
				Log.e(TAG, "Error!", error);
			}

		}, extraHeaders);
	
				
	client.send(jObject.toString());
	}
}
