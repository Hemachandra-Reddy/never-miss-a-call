package com.nevermissacall.utils;

import android.util.Log;

public class NeverMissLogs {
	public static final boolean PRINT_LOGS = false;
	public static void i(String tag, String message){
		if(PRINT_LOGS){
			Log.i(tag,message);
		}
	}
	public static void e(String tag, String message){
		if(PRINT_LOGS){
			Log.e(tag,message);
		}
	}
	public static void d(String tag, String message){
		if(PRINT_LOGS){
			Log.d(tag,message);
		}
	}
	public static void v(String tag, String message){
		if(PRINT_LOGS){
			Log.v(tag,message);
		}
	}
	public  static void w(String tag, String message){
		if(PRINT_LOGS){
			Log.w(tag,message);
		}
	}

}
