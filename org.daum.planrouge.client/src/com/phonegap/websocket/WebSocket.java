package com.phonegap.websocket;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

import org.apache.http.HttpException;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import android.widget.Toast;

import com.chariotsolutions.nfc.plugin.NfcPlugin;
import com.phonegap.plugins.nfc.Common;

public class WebSocket {

	WebSocketClient client;
	List<BasicNameValuePair> extraHeaders = Arrays.asList(new BasicNameValuePair("Cookie", "session=abcd"));
	private NfcPlugin nfcPlugin;

	public WebSocket(String address, int port, String handler, final NfcPlugin nfcPlugin) {
		this.nfcPlugin=nfcPlugin;
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
							if(jObject.get("result").equals("undefined")){
								// TODO notify AGENT
							//	nfcPlugin.sendGetAgent("false");
							}else {
								//nfcPlugin.sendGetAgent(jObject.toString());
							}
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
				
			}

			@Override
			public void onError(Exception error) {
				Log.e(TAG, "Error!", error);
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

}
