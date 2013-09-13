package com.planrouge.plugins;

import org.apache.cordova.api.CallbackContext;
import org.apache.cordova.api.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;

import android.util.Log;

import com.planrouge.api.nfc.NFC_Mifare_classic;
import com.planrouge.plugins.manager.ComManager;

public class ReadWritePlugin extends CordovaPlugin {

	private byte[] key = new NFC_Mifare_classic().hexStringToByteArray("FFFFFFFFFFFF");

	@Override
	public boolean execute(String action, JSONArray data, CallbackContext callbackContext) throws JSONException {
		Log.i("READ_WRITE_PLUGIN","EXECUTE");
		ComManager  comManager= ComManager.getInstance();


		if (action.equalsIgnoreCase("read")) { // read chip
			Log.i("READ_WRITE_PLUGIN", "read ");
			comManager.getAdapterFactory().read(data, callbackContext, key, comManager.getPuceNFC());

		} else if (action.equalsIgnoreCase("write")) { // write on chip
			comManager.setWriteExecution(true);
			Log.i("READ_WRITE_PLUGIN", "write");
			comManager.getAdapterFactory().write(data, callbackContext, key, comManager.getPuceNFC(),this);

		}  else if (action.equalsIgnoreCase("raz")) { //reset isWriteExecution
			comManager.setWriteExecution(false);
			Log.i("READ_WRITE_PLUGIN", "IS NOT WRITE EXECUTION");

		}else {
			return false;
			
		}
		return true;
		
	}
	
}
