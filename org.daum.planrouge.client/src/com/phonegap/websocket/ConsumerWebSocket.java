package com.phonegap.websocket;

import java.util.concurrent.LinkedBlockingQueue;

import org.apache.cordova.CordovaWebView;
import org.json.JSONObject;

import com.chariotsolutions.nfc.plugin.NfcPlugin;

import android.util.Log;

public class ConsumerWebSocket extends Thread {

	private WebSocket client;
	private LinkedBlockingQueue<String> queue;
	private Thread t;

	public ConsumerWebSocket(String address, int port, String handler, NfcPlugin nfcPlugin) {
		client = new WebSocket(address, port, handler, nfcPlugin);
		queue = new LinkedBlockingQueue<String>();
		t = this;
		this.t.start();
		Log.i("WEBSOCKET" , " Client Started : address : "+address);
	}

	public void connect(){
		client.connect();
	}

	public void disconnect(){
		client.disconnect();
	}

	public void addMessage(String message) {
		queue.offer(message);
		if(!t.isAlive()){
			Log.i("THREAD WS", "START");
			this.t.start();
		}
	}

	public String takeMessage() {
		String message = null;
		try {
			message = queue.take();
		} catch (InterruptedException e) {
			Log.i("THREAD WS", "STOP");
			this.t.interrupt();
			e.printStackTrace();
		}

		return message;
	}

	public void run() {
		while (Thread.currentThread().isAlive()) {
			Log.i("THREAD WS", " Boucle Thread");
			String message = takeMessage();
			if (message != null) {

				if(!client.isConnected()){
					client.connect();
				}
				while (!client.isConnected()) {
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					client.connect();
				}
				client.send(message);

			}
		}
	}

	public WebSocket getClient() {
		return client;
	}
	// NO HAVE TO BE HERE NEED TO CREATE A CLASSE
	public boolean checkAgent(String json)
	{
			// todo create method 
			addMessage(json);
			try {
				JSONObject response = 	client.getResponse("agent");
	
				if(response == null){
					return false;
				}else {
					if(response.has("result")){
						if(!response.get("result").toString().equals("undefined")){
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

	// Called by Consumer
	//  public synchronized String getMessage() throws InterruptedException {
	//      notify();
	//      while (messages.size() == 0) {
	//          wait();//By executing wait() from a synchronized block, a thread gives up its hold on the lock and goes to sleep.
	//      }
	//      
	//      return message;
	//  }
	//  
}
