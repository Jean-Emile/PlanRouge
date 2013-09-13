package com.planrouge.plugins.manager;

import android.util.Log;

import com.planrouge.adapter.AdapterFactory;
import com.planrouge.api.nfc.NFC_Mifare_classic;
import com.planrouge.websocket.ConsumerWebSocket;

public class ComManager {

	private static ComManager singleton = null;
	
	private ConsumerWebSocket consumerWebSocketGet = null;
	private ConsumerWebSocket consumerWebSocketAdd = null;
	private boolean isWriteExecution = false;
	private NFC_Mifare_classic puceNFC = new NFC_Mifare_classic();
	
	private AdapterFactory adapterFactory = null;
	
	private ComManager(){}
	                   
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

	public boolean isWriteExecution() {
		return isWriteExecution;
	}

	public void setWriteExecution(boolean isWriteExecution) {
		this.isWriteExecution = isWriteExecution;
	}	
}
