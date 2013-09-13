package com.planrouge.websocket;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.planrouge.api.nfc.Common;
import com.planrouge.plugins.WebsocketPlugin;
import com.planrouge.utils.BlockingConcurrentHashMap;

public class WebSocket {

	private WebSocketClient client;
	private List<BasicNameValuePair> extraHeaders = Arrays.asList(new BasicNameValuePair("Cookie", "session=abcd"));

    private  BlockingConcurrentHashMap<String,JSONObject> responses = new  BlockingConcurrentHashMap<String,JSONObject>();

	public WebSocket(String address, int port, String handler) {

		client = new WebSocketClient(URI.create("http://" + address + ":" + port + "/" + handler), new WebSocketClient.Listener() {
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
						if (jObject.get("type").equals("getAgent")) {
							// TODO generate token ID 
							responses.put("agent", jObject);
						}
					}
				} catch (JSONException e) {
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
			}

			@Override
			public void onError(Exception error) {
				Log.e(TAG, "Error! "+error);
			}

		}, extraHeaders);
	}

	public void connect() {
		client.connect();
		
	}

	public void disconnect() {
		client.disconnect();
		
	}

	public void send(String data) {
		client.send(data);
	}

	public boolean isConnected() {
		return client.isConnected();
	}

	public JSONObject getResponse(String id) throws InterruptedException {
		return responses.getAndWait(id);
	}
	
}
