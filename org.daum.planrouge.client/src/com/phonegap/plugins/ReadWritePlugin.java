package com.phonegap.plugins;

import org.apache.cordova.api.CallbackContext;
import org.apache.cordova.api.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;

import android.util.Log;

import com.phonegap.api.nfc.NFC_Mifare_classic;
import com.phonegap.plugins.manager.ComManager;

public class ReadWritePlugin extends CordovaPlugin {

	private byte[] key = new NFC_Mifare_classic().hexStringToByteArray("FFFFFFFFFFFF");
	private static boolean isWriteExecution = false;


	public static boolean isWriteExecution() {
		return isWriteExecution;
	}

	public void setWriteExecution(boolean isWriteExecution) {
		ReadWritePlugin.isWriteExecution = isWriteExecution;
	}
	
	@Override
	public boolean execute(String action, JSONArray data, CallbackContext callbackContext) throws JSONException {
		Log.i("READ_WRITE_PLUGIN","EXECUTE");
		ComManager  comManager= ComManager.getInstance();


		if (action.equalsIgnoreCase("read")) { // read chip
			Log.i("READ_WRITE_PLUGIN", "read ");
			comManager.getAdapterFactory().read(data, callbackContext, key, comManager.getPuceNFC());

		} else if (action.equalsIgnoreCase("write")) { // write on chip
			isWriteExecution = true;
			Log.i("READ_WRITE_PLUGIN", "write");
			comManager.getAdapterFactory().write(data, callbackContext, key, comManager.getPuceNFC(),this);

		}  else if (action.equalsIgnoreCase("raz")) { //reset isWriteExecution
			isWriteExecution = false;
			Log.i("READ_WRITE_PLUGIN", "IS NOT WRITE EXECUTION");

		}else {
			return false;
			
		}
		return true;
		
	}
	
}
