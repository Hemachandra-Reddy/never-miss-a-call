package com.nevermissacall.utils;

import android.content.Context;
import android.widget.Toast;

public class ShowToasts {
	public static final boolean SHOW_TOASTS = false;
	public static void show(Context mContext, String msg) {
		if(SHOW_TOASTS) {
			Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();	
		}		
	}
}