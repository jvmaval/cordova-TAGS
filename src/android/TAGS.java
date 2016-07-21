package org.apache.cordova.tags;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.util.Log;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.NfcManager;
import android.nfc.Tag;
import android.nfc.tech.NfcA;
import android.nfc.tech.NfcV;

import java.io.IOException;
import java.util.Arrays;



public class TAGS extends CordovaPlugin {
	
	private PendingIntent pendingIntent = null;
	private CallbackContext shareTagCallback;
  

	@Override
	public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
		Log.w("myApp", "is this ever reached?");
		
		/*if (action.equals("readTAG")) {
			String message = args.getString(0);
			return true;
		}*/
		
		return false;
	}
	

	
	@Override
    public void onNewIntent(Intent intent){
		Log.w("myApp", "PROCESSING INTENT");
		setIntent(intent);
		super.onNewIntent(intent); 
		processNfcIntent(intent);
    }
	
	
	
	@Override
	public void onResume(boolean multitasking) {
		super.onResume(multitasking);
		createPendingIntent();
		NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(getActivity());
		if (nfcAdapter != null) {
			try {
				nfcAdapter.enableForegroundDispatch(getActivity(), getPendingIntent(), null, null);
			} catch (IllegalStateException e) {
				
				Log.w("myApp", "WITHIN CATCH");
			}
        }		
	}
	
	@Override
	public void onPause(boolean multitasking) {
		Log.w("myApp", "PROCESSING PAUSE");
		super.onPause(multitasking);
		NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(getActivity());
		if (nfcAdapter != null) {
			try {
				nfcAdapter.disableForegroundDispatch(getActivity());
			} catch (IllegalStateException e) {
				
				Log.w("myApp", "WITHIN CATCH");
			}
        }
    
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
	
	
	private void createPendingIntent() {
        if (pendingIntent == null) {
            Activity activity = getActivity();
            Intent intent = new Intent(activity, activity.getClass());
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            pendingIntent = PendingIntent.getActivity(activity, 0, intent, 0);
        }
    }
	
	private PendingIntent getPendingIntent() {
        return pendingIntent;
    }
	
	public void processNfcIntent(Intent intent){
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        if(tag != null){
            Log.w("myApp", "TAG NOT NULL");
            // set up read command buffer
            byte blockNo = 0; // block address
            byte[] id = tag.getId();
            byte[] readCmd = new byte[3 + id.length];
            readCmd[0] = 0x20; // set "address" flag (only send command to this tag)
            readCmd[1] = 0x20; // ISO 15693 Single Block Read command byte

            System.arraycopy(id, 0, readCmd, 2, id.length); // copy ID
            readCmd[2 + id.length] = blockNo; // 1 byte payload: block address

            NfcV tech = NfcV.get(tag);
            if (tech != null) {
                // send read command
                try {
                    tech.connect();
                    byte[] data = tech.transceive(readCmd);
					Log.w("myApp", data.toString());
                    String response = arraytoTag(data);
					Log.w("myApp", response);
					//PluginResult result = new PluginResult(PluginResult.Status.OK, response);
					//result.setKeepCallback(true);
					//callbackContext.sendPluginResult(result);
					
					this.webView.loadUrl("javascript:cordova.fireDocumentEvent('tagReceived',{tag:"+response+"});");
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        tech.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
	
	public static String arraytoTag(byte[] tag){
        String tempBuffer="";
        for(int i=0;i<tag.length;i++){
            int temp=(int)tag[i];
            //if(temp<0){temp=temp+256;}
            tempBuffer = tempBuffer+Integer.toHexString(temp);
        }
        return Integer.valueOf(tempBuffer,16).toString();

    }
		
	
}

