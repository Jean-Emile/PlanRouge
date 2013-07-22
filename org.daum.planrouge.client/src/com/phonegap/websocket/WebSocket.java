package com.phonegap.websocket;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;

import android.util.Log;

import com.phonegap.plugins.nfc.Common;

public class WebSocket {

	WebSocketClient client;
	List<BasicNameValuePair> extraHeaders = Arrays.asList(new BasicNameValuePair("Cookie", "session=abcd"));
	
	public WebSocket(String address, int port, String handler) {

		client = new WebSocketClient(URI.create("http://192.168.1.101:8080/add"), new WebSocketClient.Listener() {
			String TAG = "WebSocketClient";

			@Override
			public void onConnect() {
				Log.d(TAG, "Connected!");
			}

			@Override
			public void onMessage(String message) {
				Log.d(TAG, String.format("Got string message! %s", message));
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
	
	public void connect(){
		client.connect();
	}
	
	public void disconnect(){
		client.disconnect();
	}
	
	public void send(String data){
		client.send(data);
	}

	public boolean isConnected(){
		return client.isConnected();
	}


}
