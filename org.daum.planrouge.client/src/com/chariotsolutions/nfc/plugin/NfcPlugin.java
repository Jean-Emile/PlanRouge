package com.chariotsolutions.nfc.plugin;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.cordova.api.CallbackContext;
import org.apache.cordova.api.CordovaPlugin;
import org.apache.cordova.api.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentFilter.MalformedMimeTypeException;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.TagLostException;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Toast;

import com.phonegap.adapter.AdapterFactory;
import com.phonegap.plugins.nfc.Common;
import com.phonegap.plugins.nfc.NFC_Mifare_classic;
import com.phonegap.plugins.nfc.TagActionException;
import com.phonegap.websocket.ConsumerWebSocket;

public class NfcPlugin extends CordovaPlugin {
	private static final String REGISTER_MIME_TYPE = "registerMimeType";
	private static final String REGISTER_NDEF = "registerNdef";
	private static final String REGISTER_NDEF_FORMATABLE = "registerNdefFormatable";
	private static final String REGISTER_DEFAULT_TAG = "registerTag";
	private static final String WRITE_TAG = "writeTag";
	private static final String ERASE_TAG = "eraseTag";
	private static final String SHARE_TAG = "shareTag";
	private static final String UNSHARE_TAG = "unshareTag";
	private static final String INIT = "init";

	private static final String NDEF = "ndef";
	private static final String NDEF_MIME = "ndef-mime";
	private static final String NDEF_FORMATABLE = "ndef-formatable";
	private static final String TAG_DEFAULT = "tag";

	private static final String STATUS_NFC_OK = "NFC_OK";
	private static final String STATUS_NO_NFC = "NO_NFC";
	private static final String STATUS_NFC_DISABLED = "NFC_DISABLED";
	private static final String STATUS_NDEF_PUSH_DISABLED = "NDEF_PUSH_DISABLED";

	private static final String TAG = "NfcPlugin";
	private final List<IntentFilter> intentFilters = new ArrayList<IntentFilter>();
	private final ArrayList<String[]> techLists = new ArrayList<String[]>();

	private NdefMessage p2pMessage = null;
	private PendingIntent pendingIntent = null;

	private Intent savedIntent = null;

	private static final String GET_ID = "getID";
	private static final String READ_VITAL_EMERGENCY = "read_vital_emergency";
	private String READ_BILAN_COMPLEMENTAIRE = "read_bilan_complementaire";
	private String WRITE_BILAN_COMPLEMENTAIRE = "write_bilan_complementaire";
	private String WRITE_DESTINATION = "write_destination";
	private String READ_DESTINATION = "read_destination";
	private String WRITE_TEXTFIELD = "write_champlibre";
	private String READ_TEXTFIELD = "read_champlibre";
	private String WRITE_LESION = "write_lesion";
	private String READ_LESION = "read_lesion";

	private byte[] key = new NFC_Mifare_classic().hexStringToByteArray("FFFFFFFFFFFF");
	private String ID;
	private NFC_Mifare_classic puceNFC = new NFC_Mifare_classic();
	private ConsumerWebSocket consumerWebSocket = new ConsumerWebSocket();

	private boolean isWriteExecution = false;
	
	public boolean isWriteExecution() {
		return isWriteExecution;
	}

	public void setWriteExecution(boolean isWriteExecution) {
		this.isWriteExecution = isWriteExecution;
	}

	@Override
	public boolean execute(String action, JSONArray data, CallbackContext callbackContext) throws JSONException {
		AdapterFactory adapterFactory = new AdapterFactory(consumerWebSocket);
		Log.d(TAG, "execute " + action);

		if (!getNfcStatus().equals(STATUS_NFC_OK)) {
			callbackContext.error(getNfcStatus());
			return true; // short circuit
		}
		// Vicitme vic1! createVictime(json);
		createPendingIntent();

		if (action.equalsIgnoreCase(REGISTER_MIME_TYPE)) {
			registerMimeType(data, callbackContext);

		} else if (action.equalsIgnoreCase(REGISTER_NDEF)) {
			registerNdef(callbackContext);

		} else if (action.equalsIgnoreCase(REGISTER_NDEF_FORMATABLE)) {
			registerNdefFormattable(callbackContext);

		} else if (action.equals(REGISTER_DEFAULT_TAG)) {
			registerDefaultTag(callbackContext);

		} else if (action.equalsIgnoreCase(WRITE_TAG)) {
			writeTag(data, callbackContext);

		} else if (action.equalsIgnoreCase(ERASE_TAG)) {
			eraseTag(callbackContext);

		} else if (action.equalsIgnoreCase(SHARE_TAG)) {
			shareTag(data, callbackContext);

		} else if (action.equalsIgnoreCase(UNSHARE_TAG)) {
			unshareTag(callbackContext);

		} else if (action.equalsIgnoreCase(INIT)) {
			init(callbackContext);

		} else if (action.equalsIgnoreCase(GET_ID)) {
			writeVitalEmergency(data, callbackContext);

		} else if (action.equalsIgnoreCase(READ_VITAL_EMERGENCY)) {
			readVitalEmergency(callbackContext);

		} else if (action.equalsIgnoreCase(READ_BILAN_COMPLEMENTAIRE)) {
			readBilanComplementaire(callbackContext);

		} else if (action.equalsIgnoreCase(WRITE_BILAN_COMPLEMENTAIRE)) {
			writeBilanComplementaire(data, callbackContext);

		} else if (action.equalsIgnoreCase(READ_DESTINATION)) {
			readDestination(callbackContext);

		} else if (action.equalsIgnoreCase(WRITE_DESTINATION)) {
			writeDestination(data, callbackContext);

		} else if (action.equalsIgnoreCase(READ_TEXTFIELD)) {
			readTextField(callbackContext);

		} else if (action.equalsIgnoreCase(WRITE_TEXTFIELD)) {
			writeTextField(data, callbackContext);

		} else if (action.equalsIgnoreCase(READ_LESION)) {
			readLesion(callbackContext);

		} else if (action.equalsIgnoreCase(WRITE_LESION)) {
			writeLesion(data, callbackContext);

		} else if (action.equalsIgnoreCase("read")) {
			Log.i("NfcPlugin", "read");
			adapterFactory.read(data, callbackContext, key, puceNFC);

		} else if (action.equalsIgnoreCase("write")) {
			isWriteExecution = true;
			Log.i("NfcPlugin", "write");
			adapterFactory.write(data, callbackContext, key, puceNFC,this);

		} else {
			// invalid action
			return false;
		}

		return true;
	}

	// LESION

	private void writeLesion(JSONArray data, CallbackContext callbackContext) throws JSONException {
		NFC_Mifare_classic puceNFC = new NFC_Mifare_classic();
		puceNFC.treatAsNewTag(savedIntent);

		String lesion = data.getString(0);
		String lesion1 = lesion.substring(0, 40);
		String lesion2 = lesion.substring(40, 80);
		lesion1 = Long.toString(Long.parseLong(lesion1, 2), 16);
		lesion2 = Long.toString(Long.parseLong(lesion2, 2), 16);

		while (lesion1.length() < 10) {
			lesion1 = "0" + lesion1;

		}
		while (lesion2.length() < 10) {
			lesion2 = "0" + lesion2;

		}

		lesion = lesion1 + lesion2;

		String bilan_comp = null;
		try {
			bilan_comp = puceNFC.readABlock(5, 1, key, false);
		} catch (TagActionException e1) {
			callbackContext.error(e1.getMessage());
			e1.printStackTrace();
		}

		String valeurBloc = lesion + bilan_comp.substring(20, 32);

		while (valeurBloc.length() < 32) {
			valeurBloc += "0";
		}

		// Traitement perte connaissance

		boolean result = false;
		try {
			result = puceNFC.writeInABlock(5, 1, valeurBloc, key, false);
		} catch (TagActionException e) {

			callbackContext.error(e.getMessage());
			e.printStackTrace();
		}

		callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, result));

	}

	private void readLesion(CallbackContext callbackContext) throws JSONException {
		NFC_Mifare_classic puceNFC = new NFC_Mifare_classic();
		puceNFC.treatAsNewTag(savedIntent);
		boolean error = false;
		// Traitement perte connaissance

		String result = "";

		try {
			result = puceNFC.readABlock(5, 1, key, false);

		} catch (TagActionException e) {
			callbackContext.error(e.getMessage());
			e.printStackTrace();
			error = true;
		}

		JSONArray array = new JSONArray();
		if (!error) {
			String result1 = Long.toBinaryString(Long.parseLong(result.substring(0, 10), 16));
			String result2 = Long.toBinaryString(Long.parseLong(result.substring(10, 20), 16));

			while (result1.length() < 40) {
				result1 = "0" + result1;
			}
			while (result2.length() < 40) {
				result2 = "0" + result2;
			}

			result = result1 + result2;

			array.put(0, result);
		}
		callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, array));
	}

	// CHAMP LIBRE

	private void writeTextField(JSONArray data, CallbackContext callbackContext) throws JSONException {
		NFC_Mifare_classic puceNFC = new NFC_Mifare_classic();
		puceNFC.treatAsNewTag(savedIntent);

		String textfield = puceNFC.toHex(data.getString(0));
		System.out.println(textfield + "HEXA HEXA");
		while (textfield.length() < 576) {
			textfield = textfield + "0";
		}
		System.out.println(textfield + "HEXA HEXA");
		try {

			puceNFC.writeInASector(10, textfield.substring(0, 96), key, false);
			puceNFC.writeInASector(11, textfield.substring(96, 192), key, false);
			puceNFC.writeInASector(12, textfield.substring(192, 288), key, false);
			puceNFC.writeInASector(13, textfield.substring(288, 384), key, false);
			puceNFC.writeInASector(14, textfield.substring(384, 480), key, false);
			puceNFC.writeInASector(15, textfield.substring(480, 576), key, false);

		} catch (TagActionException e) {
			callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.INVALID_ACTION, e.getMessage()));
			e.printStackTrace();
		}

		callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, true));

	}

	private void readTextField(CallbackContext callbackContext) throws JSONException {
		NFC_Mifare_classic puceNFC = new NFC_Mifare_classic();
		puceNFC.treatAsNewTag(savedIntent);

		String textfield = "";
		try {
			// ville
			textfield = (puceNFC.readASector(10, key, false)).substring(0, 96);
			textfield = textfield + (puceNFC.readASector(11, key, false)).substring(0, 96);
			textfield = textfield + (puceNFC.readASector(12, key, false)).substring(0, 96);
			textfield = textfield + (puceNFC.readASector(13, key, false)).substring(0, 96);
			textfield = textfield + (puceNFC.readASector(14, key, false)).substring(0, 96);
			textfield = textfield + (puceNFC.readASector(15, key, false)).substring(0, 96);

			textfield = puceNFC.hexToAscii(textfield);
		} catch (TagActionException e) {
			// TODO Auto-generated catch block
			callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.ERROR, e.getMessage()));
			e.printStackTrace();

		}

		JSONArray array = new JSONArray();
		array.put(0, textfield);

		callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, array));
	}

	// DESTINATION

	private void readDestination(CallbackContext callbackContext) throws JSONException {
		NFC_Mifare_classic puceNFC = new NFC_Mifare_classic();
		puceNFC.treatAsNewTag(savedIntent);

		//

		String evacuation = "";
		String destination = "";
		String ville = "";
		String code_postal = "";

		try {
			// ville
			ville = puceNFC.hexToAscii(puceNFC.readABlock(8, 2, key, false));

			// destination
			destination = puceNFC.hexToAscii(puceNFC.readABlock(8, 1, key, false));

			// Autres
			String destination_infos = puceNFC.hexToAscii(puceNFC.readABlock(8, 0, key, false));

			evacuation = destination_infos.substring(0, 1);
			code_postal = destination_infos.substring(1, 6);

		} catch (TagActionException e) {
			// TODO Auto-generated catch block
			callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.INVALID_ACTION, e.getMessage()));
			e.printStackTrace();

		}

		JSONArray array = new JSONArray();
		array.put(0, evacuation);
		array.put(1, destination);
		array.put(2, ville);
		array.put(3, code_postal);

		callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, array));

	}

	private void writeDestination(JSONArray data, CallbackContext callbackContext) throws JSONException {
		NFC_Mifare_classic puceNFC = new NFC_Mifare_classic();
		puceNFC.treatAsNewTag(savedIntent);

		String evacuation = puceNFC.toHex(data.getString(0));
		String destination = completeTheBlank(puceNFC.toHex(data.getString(1)));
		String ville = completeTheBlank(puceNFC.toHex(data.getString(2)));
		String code_postal = puceNFC.toHex(data.getString(3));

		String code_postal_evacutation = completeTheBlank(evacuation + code_postal);

		String data_destination = code_postal_evacutation + destination + ville;
		try {

			puceNFC.writeInASector(8, data_destination, key, false);
		} catch (TagActionException e) {
			callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.INVALID_ACTION, e.getMessage()));
			e.printStackTrace();
		}

		callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, true));

	}

	// BILAN COMPLEMENTAIRE
	private void writeBilanComplementaire(JSONArray data, CallbackContext callbackContext) throws JSONException {
		NFC_Mifare_classic puceNFC = new NFC_Mifare_classic();
		puceNFC.treatAsNewTag(savedIntent);

		String info = data.getString(0); // données du questionnaire
		String oxygene = Integer.toHexString(data.getInt(1));
		String chocs = Integer.toHexString(data.getInt(2));
		String immobilisation = data.getString(3);
		String extension = data.getString(4);
		String maladies = data.getString(5);
		String hospitalisations = data.getString(6);
		String traitements = data.getString(7);
		String allergies = data.getString(8);

		immobilisation = completeTheBlank(puceNFC.toHex(immobilisation));
		extension = completeTheBlank(puceNFC.toHex(extension));
		maladies = completeTheBlank(puceNFC.toHex(maladies));
		hospitalisations = completeTheBlank(puceNFC.toHex(hospitalisations));
		traitements = completeTheBlank(puceNFC.toHex(traitements));
		allergies = completeTheBlank(puceNFC.toHex(allergies));

		try {
			puceNFC.writeInASector(6, maladies + hospitalisations + traitements, key, false);
			puceNFC.writeInASector(7, extension + immobilisation + allergies, key, false);
		} catch (TagActionException e) {
			// TODO Auto-generated catch block
			callbackContext.error(e.getMessage());
			e.printStackTrace();

		}

		String lesion = null;
		try {
			lesion = puceNFC.readABlock(5, 1, key, false);
		} catch (TagActionException e1) {
			// TODO Auto-generated catch block
			callbackContext.error(e1.getMessage());
			e1.printStackTrace();
		}

		// Traitement données questionnaire
		if (info.length() % 2 != 0) {
			info = info + "0";
		}
		String valeurBloc = Integer.toString(Integer.parseInt(info, 2), 16);
		while (valeurBloc.length() < 7) {
			valeurBloc = "0" + valeurBloc;
		}
		while (oxygene.length() < 2) {
			oxygene = "0" + oxygene;
		}
		while (chocs.length() < 2) {
			chocs = "0" + chocs;
		}

		valeurBloc = lesion.substring(0, 20) + valeurBloc + oxygene + chocs;

		while (valeurBloc.length() != 32) {
			valeurBloc += "0";
		}

		// Traitement perte connaissance

		boolean result = false;
		try {
			result = puceNFC.writeInABlock(5, 1, valeurBloc, key, false);
		} catch (TagActionException e) {
			// TODO Auto-generated catch block
			callbackContext.error(e.getMessage());
			e.printStackTrace();

		}
		// TODO: PluginResult pResult = new PluginResult(Status.OK, data);

		callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, result));
		// callbackContext.success("Transmission r�ussi");
		System.out.println("ID TAG NFC :::: :: " + valeurBloc + " resutl  :: " + result);

	}

	private void readBilanComplementaire(CallbackContext callbackContext) throws JSONException {
		NFC_Mifare_classic puceNFC = new NFC_Mifare_classic();
		puceNFC.treatAsNewTag(savedIntent);
		boolean error = false;
		// Traitement perte connaissance

		String result = "";
		String maladies = "";
		String hospitalisations = "";
		String traitements = "";
		String extension = "";
		String immobilisation = "";
		String allergies = "";

		try {
			result = puceNFC.readABlock(5, 1, key, false);
			maladies = puceNFC.hexToAscii(puceNFC.readABlock(6, 0, key, false));
			hospitalisations = puceNFC.hexToAscii(puceNFC.readABlock(6, 1, key, false));
			traitements = puceNFC.hexToAscii(puceNFC.readABlock(6, 2, key, false));
			extension = puceNFC.hexToAscii(puceNFC.readABlock(7, 0, key, false));
			immobilisation = puceNFC.hexToAscii(puceNFC.readABlock(7, 1, key, false));
			allergies = puceNFC.hexToAscii(puceNFC.readABlock(7, 2, key, false));

		} catch (TagActionException e) {
			// TODO Auto-generated catch block
			callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.ERROR, e.getMessage()));
			e.printStackTrace();
			error = true;
		}

		JSONArray array = new JSONArray();

		if (!error) {
			array.put(0, (result.subSequence(20, 32)));
			array.put(1, 0);
			array.put(2, maladies);
			array.put(3, hospitalisations);
			array.put(4, traitements);
			array.put(5, extension);
			array.put(6, immobilisation);
			array.put(7, allergies);
		}
		callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, array));
		System.out.println(" resutl  :: " + result);

	}

	// URGENCE VITALE
	private void writeVitalEmergency(JSONArray data, CallbackContext callbackContext) throws JSONException {
		NFC_Mifare_classic puceNFC = new NFC_Mifare_classic();
		puceNFC.treatAsNewTag(savedIntent);

		String info = data.getString(0); // données du questionnaire
		String perte_Connaissance = Integer.toHexString(data.getInt(1));
		String freq_Respiratoire = Integer.toHexString(data.getInt(2));
		String saturation = Integer.toHexString(data.getInt(3));
		String freq_Cardiaque = Integer.toHexString(data.getInt(4));
		String pression_Arterielle = Integer.toHexString(data.getInt(5));

		// Traitement données questionnaire
		if (info.length() % 2 != 0) {
			info = info + "0";
		}
		String valeurBloc = Integer.toString(Integer.parseInt(info, 2), 16);
		while (valeurBloc.length() < 7) {
			valeurBloc = "0" + valeurBloc;
		}
		while (perte_Connaissance.length() < 2) {
			perte_Connaissance = "0" + perte_Connaissance;
		}
		while (freq_Respiratoire.length() < 2) {
			freq_Respiratoire = "0" + freq_Respiratoire;
		}
		while (saturation.length() < 2) {
			saturation = "0" + saturation;
		}
		while (freq_Cardiaque.length() < 2) {
			freq_Cardiaque = "0" + freq_Cardiaque;
		}
		while (pression_Arterielle.length() < 2) {
			pression_Arterielle = "0" + pression_Arterielle;
		}

		valeurBloc = valeurBloc + perte_Connaissance + freq_Respiratoire + saturation + freq_Cardiaque + pression_Arterielle;

		while (valeurBloc.length() != 30) {
			valeurBloc += "0";
		}

		valeurBloc = completeTheBlank(valeurBloc);
		// Traitement perte connaissance

		boolean result = false;
		try {
			result = puceNFC.writeInABlock(5, 0, valeurBloc, key, false);
		} catch (TagActionException e) {
			// TODO Auto-generated catch block
			callbackContext.error(e.getMessage());
			e.printStackTrace();

		}

		callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, result));
		// callbackContext.success("Transmission r�ussi");
		System.out.println("ID TAG NFC :::: :: " + valeurBloc + " resutl  :: " + result);

	}

	private void readVitalEmergency(CallbackContext callbackContext) throws JSONException {
		NFC_Mifare_classic puceNFC = new NFC_Mifare_classic();
		puceNFC.treatAsNewTag(savedIntent);

		// Traitement perte connaissance

		String result = "";
		try {
			result = puceNFC.readABlock(5, 0, key, false);
		} catch (TagActionException e) {
			callbackContext.error(e.getMessage());
			e.printStackTrace();

		}

		callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, result));
		// callbackContext.success("Transmission r�ussi");
		System.out.println(" resutl  :: " + result);

	}

	// COMPLETER AVEC DES "0" LES BLOCS NON COMPLET
	private String completeTheBlank(String hexString) {
		while (hexString.length() < 32) {
			hexString += "0";
		}
		return hexString;
	}

	// /////////////////////////////////////////////////////////////////////////////////////////////
	// ///////// ////////////////////
	// ///////// ////////////////////
	// /////////////////////////////////////////////////////////////////////////////////////////////
	private String getNfcStatus() {
		NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(getActivity());
		if (nfcAdapter == null) {
			return STATUS_NO_NFC;
		} else if (!nfcAdapter.isEnabled()) {
			return STATUS_NFC_DISABLED;
		} else {
			return STATUS_NFC_OK;
		}
	}

	private void registerDefaultTag(CallbackContext callbackContext) {
		addTagFilter();
		callbackContext.success();
	}

	private void registerNdefFormattable(CallbackContext callbackContext) {
		addTechList(new String[] { NdefFormatable.class.getName() });
		callbackContext.success();
	}

	private void registerNdef(CallbackContext callbackContext) {
		addTechList(new String[] { Ndef.class.getName() });
		callbackContext.success();
	}

	private void unshareTag(CallbackContext callbackContext) {
		p2pMessage = null;
		stopNdefPush();
		callbackContext.success();
	}

	private void init(CallbackContext callbackContext) {
		Log.d(TAG, "Enabling plugin " + getIntent());

		startNfc();
		if (!recycledIntent()) {
			parseMessage();
		}
		callbackContext.success();
	}

	private void registerMimeType(JSONArray data, CallbackContext callbackContext) throws JSONException {
		String mimeType = "";
		try {
			mimeType = data.getString(0);
			intentFilters.add(createIntentFilter(mimeType));
			callbackContext.success();
		} catch (MalformedMimeTypeException e) {
			callbackContext.error("Invalid MIME Type " + mimeType);
		}
	}

	// Cheating and writing an empty record. We may actually be able to erase some tag types.
	private void eraseTag(CallbackContext callbackContext) throws JSONException {
		Tag tag = savedIntent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
		NdefRecord[] records = { new NdefRecord(NdefRecord.TNF_EMPTY, new byte[0], new byte[0], new byte[0]) };
		writeNdefMessage(new NdefMessage(records), tag, callbackContext);
	}

	private void writeTag(JSONArray data, CallbackContext callbackContext) throws JSONException {
		if (getIntent() == null) { // TODO remove this and handle LostTag
			callbackContext.error("Failed to write tag, received null intent");
		}

		Tag tag = savedIntent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
		NdefRecord[] records = Util.jsonToNdefRecords(data.getString(0));
		writeNdefMessage(new NdefMessage(records), tag, callbackContext);
	}

	private void writeNdefMessage(final NdefMessage message, final Tag tag, final CallbackContext callbackContext) {
		cordova.getThreadPool().execute(new Runnable() {
			@Override
			public void run() {
				try {
					Ndef ndef = Ndef.get(tag);
					if (ndef != null) {
						ndef.connect();

						if (ndef.isWritable()) {
							int size = message.toByteArray().length;
							if (ndef.getMaxSize() < size) {
								callbackContext.error("Tag capacity is " + ndef.getMaxSize() + " bytes, message is " + size + " bytes.");
							} else {
								ndef.writeNdefMessage(message);
								callbackContext.success();
							}
						} else {
							callbackContext.error("Tag is read only");
						}
						ndef.close();
					} else {
						NdefFormatable formatable = NdefFormatable.get(tag);
						if (formatable != null) {
							formatable.connect();
							formatable.format(message);
							callbackContext.success();
							formatable.close();
						} else {
							callbackContext.error("Tag doesn't support NDEF");
						}
					}
				} catch (FormatException e) {
					callbackContext.error(e.getMessage());
				} catch (TagLostException e) {
					callbackContext.error(e.getMessage());
				} catch (IOException e) {
					callbackContext.error(e.getMessage());
				}
			}
		});
	}

	private void shareTag(JSONArray data, CallbackContext callbackContext) throws JSONException {
		NdefRecord[] records = Util.jsonToNdefRecords(data.getString(0));
		this.p2pMessage = new NdefMessage(records);

		startNdefPush(callbackContext);
	}

	private void createPendingIntent() {
		if (pendingIntent == null) {
			Activity activity = getActivity();
			Intent intent = new Intent(activity, activity.getClass());
			intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
			pendingIntent = PendingIntent.getActivity(activity, 0, intent, 0);
		}
	}

	private void addTechList(String[] list) {
		this.addTechFilter();
		this.addToTechList(list);
	}

	private void addTechFilter() {
		intentFilters.add(new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED));
	}

	private void addTagFilter() {
		intentFilters.add(new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED));
	}

	private void startNfc() {
		createPendingIntent(); // onResume can call startNfc before execute

		getActivity().runOnUiThread(new Runnable() {
			public void run() {
				NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(getActivity());

				if (nfcAdapter != null && getActivity().isFinishing() == false) {
					nfcAdapter.enableForegroundDispatch(getActivity(), getPendingIntent(), getIntentFilters(), getTechLists());

					if (p2pMessage != null) {
						nfcAdapter.setNdefPushMessage(p2pMessage, getActivity());
					}

				}
			}
		});
	}

	private void stopNfc() {
		Log.d(TAG, "stopNfc");
		getActivity().runOnUiThread(new Runnable() {
			public void run() {

				NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(getActivity());

				if (nfcAdapter != null) {
					nfcAdapter.disableForegroundDispatch(getActivity());
				}
			}
		});
	}

	private void startNdefPush(final CallbackContext callbackContext) {
		getActivity().runOnUiThread(new Runnable() {
			public void run() {

				NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(getActivity());

				if (nfcAdapter == null) {
					callbackContext.error(STATUS_NO_NFC);
					// isNdefPushEnabled would be nice, but requires android-17
					// } else if (!nfcAdapter.isNdefPushEnabled()) {
					// callbackContext.error(STATUS_NDEF_PUSH_DISABLED);
				} else {
					nfcAdapter.setNdefPushMessage(p2pMessage, getActivity());
					callbackContext.success();
				}
			}
		});
	}

	private void stopNdefPush() {
		getActivity().runOnUiThread(new Runnable() {
			public void run() {

				NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(getActivity());

				if (nfcAdapter != null) {
					nfcAdapter.setNdefPushMessage(null, getActivity());
				}

			}
		});
	}

	private void addToTechList(String[] techs) {
		techLists.add(techs);
	}

	private IntentFilter createIntentFilter(String mimeType) throws MalformedMimeTypeException {
		IntentFilter intentFilter = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
		intentFilter.addDataType(mimeType);
		return intentFilter;
	}

	private PendingIntent getPendingIntent() {
		return pendingIntent;
	}

	private IntentFilter[] getIntentFilters() {
		return intentFilters.toArray(new IntentFilter[intentFilters.size()]);
	}

	private String[][] getTechLists() {
		// noinspection ToArrayCallWithZeroLengthArrayArgument
		return techLists.toArray(new String[0][0]);
	}

	void parseMessage() {
		cordova.getThreadPool().execute(new Runnable() {
			@Override
			public void run() {
				Log.d(TAG, "parseMessage " + getIntent());
				Intent intent = getIntent();
				String action = intent.getAction();
				Log.d(TAG, "action " + action);
				if (action == null) {
					return;
				}

				Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
				Parcelable[] messages = intent.getParcelableArrayExtra((NfcAdapter.EXTRA_NDEF_MESSAGES));

				if (action.equals(NfcAdapter.ACTION_NDEF_DISCOVERED)) {
					Ndef ndef = Ndef.get(tag);
					fireNdefEvent(NDEF_MIME, ndef, messages);

				} else if (action.equals(NfcAdapter.ACTION_TECH_DISCOVERED)) {
					for (String tagTech : tag.getTechList()) {
						Log.d(TAG, tagTech);
						if (tagTech.equals(NdefFormatable.class.getName())) {
							fireNdefFormatableEvent(tag);
						} else if (tagTech.equals(Ndef.class.getName())) { //
							Ndef ndef = Ndef.get(tag);
							fireNdefEvent(NDEF, ndef, messages);
						}
					}
				}

				if (action.equals(NfcAdapter.ACTION_TAG_DISCOVERED)) {
					fireTagEvent(tag);
				}

				setIntent(new Intent());
			}
		});
	}

	private void fireNdefEvent(String type, Ndef ndef, Parcelable[] messages) {

		JSONObject jsonObject = buildNdefJSON(ndef, messages);
		String tag = jsonObject.toString();

		String command = MessageFormat.format(javaScriptEventTemplate, type, tag);
		Log.v(TAG, command);
		this.webView.sendJavascript(command);

	}

	private void fireNdefFormatableEvent(Tag tag) {

		String command = MessageFormat.format(javaScriptEventTemplate, NDEF_FORMATABLE, Util.tagToJSON(tag));

		Log.v(TAG, command);
		this.webView.sendJavascript(command);
	}

	private void fireTagEvent(Tag tag) {

		String command = MessageFormat.format(javaScriptEventTemplate, TAG_DEFAULT, Util.tagToJSON(tag));

		Log.v(TAG, command);
		this.webView.sendJavascript(command);
	}

	JSONObject buildNdefJSON(Ndef ndef, Parcelable[] messages) {

		JSONObject json = Util.ndefToJSON(ndef);

		// ndef is null for peer-to-peer
		// ndef and messages are null for ndef format-able
		if (ndef == null && messages != null) {

			try {

				if (messages.length > 0) {
					NdefMessage message = (NdefMessage) messages[0];
					json.put("ndefMessage", Util.messageToJSON(message));
					// guessing type, would prefer a more definitive way to determine type
					json.put("type", "NDEF Push Protocol");
				}

				if (messages.length > 1) {
					Log.wtf(TAG, "Expected one ndefMessage but found " + messages.length);
				}

			} catch (JSONException e) {
				// shouldn't happen
				Log.e(Util.TAG, "Failed to convert ndefMessage into json", e);
			}
		}
		return json;
	}

	private boolean recycledIntent() { // TODO this is a kludge, find real solution

		int flags = getIntent().getFlags();
		if ((flags & Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY) == Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY) {
			Log.i(TAG, "Launched from history, killing recycled intent");
			setIntent(new Intent());
			return true;
		}
		return false;
	}

	@Override
	public void onPause(boolean multitasking) {
		Log.d(TAG, "onPause " + getIntent());
		super.onPause(multitasking);
		if (multitasking) {
			// nfc can't run in background
			stopNfc();
		}
	}

	@Override
	public void onResume(boolean multitasking) {
		Log.d(TAG, "onResume " + getIntent());
		super.onResume(multitasking);
		startNfc();
	}

	@Override
	public void onNewIntent(Intent intent) {
		Log.d(TAG, "onNewIntent " + intent);
		Toast.makeText(getActivity(), "New Tag discovered", Toast.LENGTH_LONG).show();

		super.onNewIntent(intent);

		setIntent(intent);

		savedIntent = intent;
		puceNFC.treatAsNewTag(intent);
		if (!(isWriteExecution)){
			String url = nextActionToProcess(intent);
			super.webView.loadUrl(url);
		}

		parseMessage();

	}

	private String nextActionToProcess(Intent intent) {
		// NFC_Mifare_classic puceNFC = new NFC_Mifare_classic();
		// puceNFC.treatAsNewTag(intent);

		String vital_urgency = "";
		try {
			vital_urgency = puceNFC.readABlock(1, 0, key, false);
		} catch (TagActionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String category = "";
		if (vital_urgency.length() > 31) {
			category = vital_urgency.substring(31, 32);
		}
		if (category.equals("0")) {
			return "file:///android_asset/www/categorieVictime/index.html";
		}

		String firstname = "";
		String surname = "";
		String age = "";
		String age_unit = "";
		String birthday = "";
		String sexe = "";
		String identity_infos = "";
		try {
			// firstname
			firstname = puceNFC.hexToAscii(puceNFC.readABlock(0, 1, key, false));
			// surname
			surname = puceNFC.hexToAscii(puceNFC.readABlock(0, 2, key, false));

			// Autres
			identity_infos = puceNFC.hexToAscii(puceNFC.readABlock(1, 0, key, false));
			// age
			if (identity_infos.length() > 12) {
				birthday = identity_infos.substring(0, 8);
				age = identity_infos.substring(8, 11);
				age_unit = identity_infos.substring(11, 12);
				sexe = identity_infos.substring(12, 13);
			}
		} catch (TagActionException e) {
			// TODO Auto-generated catch block

			e.printStackTrace();

		}

		if (firstname.equals("") || surname.equals("") || identity_infos.equals("")) {
			Log.d("DEBUG PAGE", firstname + "   " + surname + "   " + identity_infos);
			return "file:///android_asset/www/identite/index.html";
		}

		return "file:///android_asset/www/index.html";
	}

	private Activity getActivity() {
		return this.cordova.getActivity();
	}

	private Intent getIntent() {
		return getActivity().getIntent();
	}

	private void setIntent(Intent intent) {
		getActivity().setIntent(intent);
	}

	String javaScriptEventTemplate = "var e = document.createEvent(''Events'');\n" + "e.initEvent(''{0}'');\n" + "e.tag = {1};\n"
			+ "document.dispatchEvent(e);";

}
