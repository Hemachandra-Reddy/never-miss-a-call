package com.nevermissacall.database;

import java.util.ArrayList;
import java.util.Collections;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.nevermissacall.data.CallLogModel;
import com.nevermissacall.utils.*;

public class DatabaseHelper extends SQLiteOpenHelper {

	SQLiteDatabase db;
	
	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "CallLogManager";

	// Table Names
	private static final String CALL_LOG_TBL = "call_log_info";

	// Table column names
	private static final String ID = "id";
	private static final String NUMBER = "number";
	private static final String NAME = "name";
	private static final String DATE = "date";	
	private static final String DURATION = "duration";
	private static final String CALL_TYPE = "calltype";

	// Table Create Statements
	// Call_log_info table create statement
	private static final String CREATE_CALL_LOG_TBL = "CREATE TABLE IF NOT EXISTS " + CALL_LOG_TBL + "(" + ID + " INTEGER," + NUMBER + " LONG," + NAME + " TEXT," + DATE + " LONG," + DURATION + " INTEGER," + CALL_TYPE + " INTEGER" +")";	 
	
	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		try {
			NeverMissLogs.e("SQL Command", " Query " + CREATE_CALL_LOG_TBL);
			// creating required table
			db.execSQL(CREATE_CALL_LOG_TBL);
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		try {
			// on upgrade drop older tables
			db.execSQL("DROP TABLE IF EXISTS " + CALL_LOG_TBL);
			// create new tables
			onCreate(db);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void insertCallLogEntry(CallLogModel callmodel) {
		try {
			SQLiteDatabase db = this.getWritableDatabase();
			//		openDatabase();
			ContentValues values = new ContentValues();
			values.put(ID, callmodel.getId());
			String number = callmodel.getNumber();			
			values.put(NUMBER, number);
			values.put(NAME, callmodel.getName());
			values.put(DATE, callmodel.getDate());
			values.put(DURATION, callmodel.getDuration());
			values.put(CALL_TYPE, callmodel.getCalltype());
			// insert row
			db.insert(CALL_LOG_TBL, null, values);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	long formatNumber(long number)
	{
		String original = "" + number;
		int numlen = original.length();
		
		if(numlen <= 10)
		{
			return number;
		}
		else 
		{
			if(original.startsWith("+"))
			{
				return number;
			}
		}
		return number;
	}
	
	/*
	 * Retrieving all the calls of type "callType"
	 */
	@SuppressWarnings("rawtypes")
	public ArrayList getCallLog(int callType, boolean shouldGroup)
	{
		ArrayList<CallLogModel> calllist = null;
		try {
			calllist = new ArrayList<CallLogModel>();
			String tmpQuery = "SELECT MAX(id), * FROM " + CALL_LOG_TBL
					+ " WHERE " + CALL_TYPE + " = " + callType
					+ " GROUP BY number ORDER BY date desc";
			if (!shouldGroup) {
				tmpQuery = "SELECT * FROM " + CALL_LOG_TBL + " WHERE "
						+ CALL_TYPE + " = " + callType;
			}
			SQLiteDatabase db = this.getReadableDatabase();
			//		openDatabase();
			Cursor callLogCursor = db.rawQuery(tmpQuery, null);
			if (callLogCursor != null) {
				while (callLogCursor.moveToNext()) {
					int id = callLogCursor.getInt(callLogCursor
							.getColumnIndex(ID));
					String name = callLogCursor.getString(callLogCursor
							.getColumnIndex(NAME));
					long date = callLogCursor.getLong(callLogCursor
							.getColumnIndex(DATE));
					String number = callLogCursor.getString(callLogCursor
							.getColumnIndex(NUMBER));
					int duration = callLogCursor.getInt(callLogCursor
							.getColumnIndex(DURATION));

					if (name == null)
						name = "Unknown";

					CallLogModel callLogModel = new CallLogModel(id, name,
							number, duration, date, callType);
					calllist.add(callLogModel);
				}
				callLogCursor.close();
			}
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return calllist;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ArrayList uShouldcalback2()
	{
		ArrayList<CallLogModel> finallist = null;		
		try {
			int index = 0;
			String tmpNumber;
			long tmpDate;
			String tmpQuery;
			Cursor callLogCursor = null;
			ArrayList<CallLogModel> missedcalls = new ArrayList<CallLogModel>();
			ArrayList<CallLogModel> incomingcalls = new ArrayList<CallLogModel>();			
			finallist = new ArrayList<CallLogModel>();
			missedcalls = getCallLog(2, true);
			incomingcalls = getCallLog(0, true);			
			for (index = 0; index < incomingcalls.size(); index++) {
				CallLogModel tmpEntry = incomingcalls.get(index);				
				if (tmpEntry.getDuration() == 0) {
					missedcalls.add(tmpEntry);
				}
			}

			Collections.sort(missedcalls, new CallLogComparator());

			SQLiteDatabase db = this.getReadableDatabase();
			for (index = 0; index < missedcalls.size(); index++) {
				CallLogModel tmpEntry = missedcalls.get(index);
				tmpNumber = tmpEntry.getNumber();
				tmpDate = tmpEntry.getDate();
				long rowcount = Integer.MAX_VALUE;
				tmpQuery = "SELECT * FROM " + CALL_LOG_TBL
						+ " as c where c.calltype != 2 and c.number LIKE '%"
						+ tmpNumber + "%' and " + tmpDate
						+ " < c.date and c.duration > 0";
				callLogCursor = db.rawQuery(tmpQuery, null);
				callLogCursor.moveToFirst();
				rowcount = callLogCursor.getCount();
				if (rowcount == 0) {
					finallist.add(tmpEntry);
				}
			}

			callLogCursor.close();
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return finallist;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ArrayList getAllEntries(String number)
	{
		ArrayList<CallLogModel> finallist = null;
		try {
			int index = 0;
			ArrayList<CallLogModel> missedcalls = new ArrayList<CallLogModel>();
			ArrayList<CallLogModel> incomingcalls = new ArrayList<CallLogModel>();
			finallist = new ArrayList<CallLogModel>();
			missedcalls = getCallLog(2, false);
			incomingcalls = getCallLog(0, true);
			for (index = 0; index < incomingcalls.size(); index++) {
				CallLogModel tmpEntry = incomingcalls.get(index);				
				if ((number.equals(tmpEntry.getNumber())) && 
						(tmpEntry.getDuration() == 0)) {
					missedcalls.add(tmpEntry);
				}
			}

			Collections.sort(missedcalls, new CallLogComparator());
			
			for (index = 0; index < missedcalls.size(); index++) {
				CallLogModel tmpEntry = missedcalls.get(index);
				if(number.equals(tmpEntry.getNumber())) {
					finallist.add(tmpEntry);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return finallist;
	}	

	public void deleteTblData()
	{
		try {
			if (db != null && db.isOpen()) {
				db.close();
			}
			SQLiteDatabase db = this.getWritableDatabase();
			db.execSQL("DELETE FROM " + CALL_LOG_TBL);
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	
	public void openDatabase(){
		try {
			if (db != null && db.isOpen()) {
				db.close();
			}
			db = this.getReadableDatabase();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
