package com.nevermissacall.adapters;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
 
public class CallListner extends BroadcastReceiver {
	public static boolean needRefresh = false;
	
	@Override
	public void onReceive(Context context, Intent intent) {
        TelephonyManager tmgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        MyPhoneStateListener PhoneListener = new MyPhoneStateListener();        
        // Register listener for LISTEN_CALL_STATE
        tmgr.listen(PhoneListener, PhoneStateListener.LISTEN_CALL_STATE);		
	}

	private class MyPhoneStateListener extends PhoneStateListener {
        public void onCallStateChanged(int state, String incomingNumber) {        	
        	needRefresh = true;
        }
    }
}
