package com.phonegap.plugins.manager;

import android.util.Log;

import com.phonegap.adapter.AdapterFactory;
import com.phonegap.plugins.nfc.NFC_Mifare_classic;
import com.phonegap.websocket.ConsumerWebSocket;

public class ComManager {

	private static ComManager singleton = null;
	
	private ConsumerWebSocket consumerWebSocketGet = null;
	private ConsumerWebSocket consumerWebSocketAdd = null;
	
	private NFC_Mifare_classic puceNFC = new NFC_Mifare_classic();
	
	private AdapterFactory adapterFactory = null;
	
	private ComManager(){	}
	                   
	public static ComManager getInstance() {
        if (singleton  == null) {
            singleton = new ComManager();
            Log.e("singleton ComManager    ","null");
        }
        return singleton;
    }


	public NFC_Mifare_classic getPuceNFC() {
		return puceNFC;
	}



	public void setPuceNFC(NFC_Mifare_classic puceNFC) {
		this.puceNFC = puceNFC;
	}



	public AdapterFactory getAdapterFactory() {
		return adapterFactory;
	}



	public void setAdapterFactory(AdapterFactory adapterFactory) {
		this.adapterFactory = adapterFactory;
	}



	public ConsumerWebSocket getConsumerWebSocketGet() {
		return consumerWebSocketGet;
	}



	public void setConsumerWebSocketGet(ConsumerWebSocket consumerWebSocketGet) {
		this.consumerWebSocketGet = consumerWebSocketGet;
	}



	public ConsumerWebSocket getConsumerWebSocketAdd() {
		return consumerWebSocketAdd;
	}



	public void setConsumerWebSocketAdd(ConsumerWebSocket consumerWebSocketAdd) {
		this.consumerWebSocketAdd = consumerWebSocketAdd;
	}
	
	
}
