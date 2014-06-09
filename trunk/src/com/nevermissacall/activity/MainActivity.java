package com.nevermissacall.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.CallLog;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.nevermissacall.R;
import com.nevermissacall.adapters.CallListner;
import com.nevermissacall.adapters.CallLogsArrayAdapter;
import com.nevermissacall.ads.ToastAdListener;
import com.nevermissacall.data.CallLogModel;
import com.nevermissacall.database.DatabaseHelper;
import com.nevermissacall.utils.Fonts;
import com.nevermissacall.utils.Globals;
import com.nevermissacall.utils.NeverMissLogs;
import com.nevermissacall.utils.ProgressWheel;

public class MainActivity extends Activity implements OnItemClickListener{	

	public DatabaseHelper calldata;
	private ListView mainListView;
	private ArrayAdapter<CallLogModel> listAdapter;
	public ArrayList<CallLogModel> outgoingcalls;
	public ArrayList<CallLogModel> incomingcalls;
	public ArrayList<CallLogModel> missedcalls;
	public ArrayList<CallLogModel> finallist;
	public static final String TAG = "MainActivity";
	ProgressDialog dialog;
	private AdView mAdView;
	private TextView noRecords;	
	private ProgressWheel pw;

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		try {
			this.requestWindowFeature(Window.FEATURE_NO_TITLE);
			setContentView(R.layout.listview);
			CallListner.needRefresh = true;
			pw = (ProgressWheel) findViewById(R.id.progressBarTwo);
			mainListView = (ListView) findViewById(R.id.listViewID);
			noRecords = (TextView) findViewById(R.id.noRecordsTextViewID);
			noRecords.setTypeface(Fonts.BOOK_ANTIQUA);
			mainListView.setSmoothScrollbarEnabled(true);
			mainListView.setOnItemClickListener(this);
			calldata = new DatabaseHelper(this);
			mAdView = new AdView(this);
			mAdView.setAdUnitId(getResources().getString(R.string.ad_unit_id));
			mAdView.setAdSize(AdSize.BANNER);
			mAdView.setAdListener(new ToastAdListener(this));
			RelativeLayout layout = (RelativeLayout) findViewById(R.id.mainLayoutID);
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.MATCH_PARENT,
					RelativeLayout.LayoutParams.WRAP_CONTENT);
			layout.addView(mAdView, params);
			mAdView.loadAd(new AdRequest.Builder().build());	
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	private void showFinalList() {
		try {
			if (incomingcalls == null) {
				incomingcalls = new ArrayList<CallLogModel>();
				incomingcalls = calldata.getCallLog(0, false);
			}
			if (outgoingcalls == null) {
				outgoingcalls = new ArrayList<CallLogModel>();
				outgoingcalls = calldata.getCallLog(1, false);
			}
			if (missedcalls == null) {
				missedcalls = new ArrayList<CallLogModel>();
				missedcalls = calldata.getCallLog(2, false);
			}
			if (finallist == null) {
				finallist = new ArrayList<CallLogModel>();
				if ((incomingcalls == null) && (outgoingcalls == null)) {
					if (missedcalls != null) {
						for (CallLogModel calllog : missedcalls) {
							finallist.add(calllog);
						}
					}
				} else {
					finallist = calldata.uShouldcalback2();
				}
			}
			listAdapter = new CallLogsArrayAdapter(this, finallist);
			mainListView.setAdapter(listAdapter);
			if (finallist.size() == 0) {
				noRecords.setVisibility(View.VISIBLE);
			}
			else {
				noRecords.setVisibility(View.GONE);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}		


	private void readCallLogs() {

		try {
			Cursor callLogCursor = getContentResolver().query(
					android.provider.CallLog.Calls.CONTENT_URI, null, null,
					null, android.provider.CallLog.Calls.DEFAULT_SORT_ORDER);
			if (callLogCursor != null) {
				while (callLogCursor.moveToNext()) {
					int id = callLogCursor.getInt(callLogCursor
							.getColumnIndex(CallLog.Calls._ID));

					String name = callLogCursor.getString(callLogCursor
							.getColumnIndex(CallLog.Calls.CACHED_NAME));

					String number = callLogCursor.getString(callLogCursor
							.getColumnIndex(CallLog.Calls.NUMBER));

					long dateTimeMillis = callLogCursor.getLong(callLogCursor
							.getColumnIndex(CallLog.Calls.DATE));

					int duration = callLogCursor.getInt(callLogCursor
							.getColumnIndex(CallLog.Calls.DURATION));

					int callType = callLogCursor.getInt(callLogCursor
							.getColumnIndex(CallLog.Calls.TYPE));

					if (name == null)
						name = "Unknown";

					switch (callType) {
					case CallLog.Calls.INCOMING_TYPE:
						callType = 0;
						break;
					case CallLog.Calls.OUTGOING_TYPE:
						callType = 1;
						break;
					case CallLog.Calls.MISSED_TYPE:
						callType = 2;
						break;
					default:
						break;
					}

					CallLogModel callLogModel = new CallLogModel(id, name,
							number, duration, dateTimeMillis, callType);
					calldata.insertCallLogEntry(callLogModel);
				}
				callLogCursor.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void initElements() {
		try {
			listAdapter.notifyDataSetChanged();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Override
	public void onItemClick(AdapterView<?> listview, View v, int position,long id) {
		try {
			CallLogModel calllog = (CallLogModel) mainListView
					.getItemAtPosition(position);
			String nameString = calllog.getName();
			String numbString = calllog.getNumber();
			Intent intent = new Intent(this, ContactDetailActivity.class);
			intent.putExtra("Name", nameString);
			intent.putExtra("Number", numbString);
			startActivity(intent);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private class ReadLogs extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			try {
				super.onPreExecute();
				if(!pw.isShown()){
					pw.setVisibility(View.VISIBLE);
				}
				mainListView.setAlpha(.2f);
				noRecords.setAlpha(0f);
				pw.spin();
				pw.setText("Reading Call Logs...");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		protected Void doInBackground(Void... params) {
			readCallLogs();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			try {
				super.onPostExecute(result);
				if(pw.isShown()){
					pw.setVisibility(View.GONE);
					mainListView.setAlpha(1f);
					noRecords.setAlpha(1f);
				}
				pw.stopSpinning();
				pw.setVisibility(View.GONE);
				incomingcalls = null;
				outgoingcalls = null;
				missedcalls = null;
				finallist = null;
				showFinalList();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	@Override
	public boolean isFinishing() {
		try {
			if(pw.isShown()){
				pw.setVisibility(View.GONE);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return super.isFinishing();
	}
	@Override
	protected void onResume() {

		try {
			super.onResume();
			if(Globals.isRecordDeleted) {
				updateListAdapter(Globals.deletedNumber);
				Globals.isRecordDeleted = false;
				Globals.deletedNumber = null;
			}
				
			if(CallListner.needRefresh){
				CallListner.needRefresh = false;
				calldata.deleteTblData();
				new ReadLogs().execute();
				listAdapter.notifyDataSetChanged();
			}						
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Override
	public void onBackPressed() {
		try {
			calldata.deleteTblData();
			NeverMissLogs.e(TAG, "Tbl Deleted on backPress");
			finish();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void updateListAdapter(String number) {
		int index = 0;
		int count = listAdapter.getCount();
		
		for(;index < count; index++) {
			CallLogModel tmp = listAdapter.getItem(index);
			String tmpNumber = tmp.getNumber().trim();
			if(tmpNumber.equals(number)) {
				listAdapter.remove(listAdapter.getItem(index));
				listAdapter.notifyDataSetChanged();
				if (listAdapter.isEmpty()) {
					noRecords.setVisibility(View.VISIBLE);
				}
				return;
			}
		}
	}
}
