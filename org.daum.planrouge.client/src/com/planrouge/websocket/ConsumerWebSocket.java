package com.planrouge.websocket;

import java.util.concurrent.LinkedBlockingQueue;

import org.json.JSONObject;

import android.util.Log;

import com.planrouge.plugins.WebsocketPlugin;

public class ConsumerWebSocket extends Thread {

	private WebSocket client;
	private LinkedBlockingQueue<String> queue;
	private Thread t;

	public ConsumerWebSocket(String address, int port, String handler) {
		client = new WebSocket(address, port, handler);
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
	}
