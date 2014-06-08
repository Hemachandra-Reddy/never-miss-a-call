package com.nevermissacall.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.nevermissacall.R;
import com.nevermissacall.adapters.ContactDetailArrayAdapter;
import com.nevermissacall.data.CallLogModel;
import com.nevermissacall.database.DatabaseHelper;
import com.nevermissacall.utils.Fonts;
import com.nevermissacall.utils.Globals;
import com.nevermissacall.utils.NeverMissLogs;

public class ContactDetailActivity extends Activity implements OnClickListener {
	private String TAG="ContactDetailActivity";
	private String phoneNo;
	private String name;
	public DatabaseHelper calldata;
	private ListView mainListView;
	private ArrayAdapter<CallLogModel> listAdapter;
	public ArrayList<CallLogModel> numberlist;
	private ImageView expandBtn;
	private TextView nameTxt,numberTxt,call,edit,delete;
	private LinearLayout optionsLayout;
	private boolean optionsVisible = false;
	private Animation a,b;
	private Handler handler = null;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			this.requestWindowFeature(Window.FEATURE_NO_TITLE);
			setContentView(R.layout.contact_detail);
			Intent intent = getIntent();
			name = intent.getStringExtra("Name");
			phoneNo = intent.getStringExtra("Number");
			NeverMissLogs.e(TAG, "NUMBER:" + phoneNo);
			mainListView = (ListView) findViewById(R.id.numberListID);
			expandBtn = (ImageView) findViewById(R.id.callImageViewID);
			call = (TextView) findViewById(R.id.callID);
			edit = (TextView) findViewById(R.id.editID);
			delete = (TextView) findViewById(R.id.deleteID);
			call.setTypeface(Fonts.BOOK_ANTIQUA);
			edit.setTypeface(Fonts.BOOK_ANTIQUA);
			delete.setTypeface(Fonts.BOOK_ANTIQUA);
			call.setOnClickListener(this);
			edit.setOnClickListener(this);
			delete.setOnClickListener(this);
			optionsLayout = (LinearLayout)findViewById(R.id.optionsLayoutID);
			optionsLayout.setOnClickListener(this);
			nameTxt = (TextView) findViewById(R.id.nameID);
			nameTxt.setTypeface(Fonts.BOOK_ANTIQUA, Typeface.BOLD);			
			numberTxt = (TextView) findViewById(R.id.numberID);
			numberTxt.setTypeface(Fonts.BOOK_ANTIQUA);
			nameTxt.setText(name);
			numberTxt.setText("" + phoneNo);
			expandBtn.setOnClickListener(this);
			mainListView.setSmoothScrollbarEnabled(true);
			getAllEntriesForNumber();
			a = AnimationUtils.loadAnimation(this, R.anim.slide_down);
			b = AnimationUtils.loadAnimation(this, R.anim.slide_up);
			handler = new Handler();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@SuppressWarnings("unchecked")
	private void getAllEntriesForNumber(){
		try {
			calldata = new DatabaseHelper(this);
			numberlist = calldata.getAllEntries(phoneNo);
			listAdapter = new ContactDetailArrayAdapter(this, numberlist);
			mainListView.setAdapter(listAdapter);
		} catch (Exception e) {
			e.getMessage();
		}
	}
	public void initElements() {
		try {
			listAdapter.notifyDataSetChanged();
		} catch (Exception e) {
			e.getMessage();
		}
	}
	@Override
	public void onClick(View v) {
		try {
			switch (v.getId()) {
			case R.id.callID:
				handleCallClick();
				break;
			case R.id.editID:
				handleEditClick();
				break;
			case R.id.deleteID:
				handleDeleteClick();
				break;
			case R.id.callImageViewID:
				if(optionsVisible){
					expandBtn.setImageResource(R.drawable.ic_down);
					a.reset();
					b.reset();
					optionsLayout.clearAnimation();
					optionsLayout.startAnimation(b);
					handler.postDelayed(new Runnable() {
						@Override
						public void run() {
							optionsLayout.setVisibility(View.GONE);
						}
					}, 250);

					optionsVisible = false;
				}else{
					expandBtn.setImageResource(R.drawable.ic_up);
					optionsLayout.setVisibility(View.VISIBLE);	
					a.reset();
					b.reset();
					optionsLayout.clearAnimation();
					optionsLayout.startAnimation(a);
					optionsVisible = true;
				}

				break;
			default:
				break;
			}
		} catch (Exception e) {
			e.getMessage();
		}
	}
	private void handleDeleteClick() {
		Uri allCalls = Uri.parse("content://call_log/calls");
		@SuppressWarnings("deprecation")
		Cursor c = managedQuery(allCalls, null, null, null, null);
		if(c.getCount() == 0) {
			finish();
		}
		
		while(c.moveToNext())
		{
			String numbString = "+"+phoneNo.trim();
			String queryString= "NUMBER='"+numbString+"' AND type=3" ;
			ContactDetailActivity.this.getContentResolver().delete(allCalls, queryString, null);
			Globals.isRecordDeleted = true;
			Globals.deletedNumber = phoneNo;
		}
		
		finish();
	}
	private void handleEditClick() {
		String phoneNumber = "tel:" + phoneNo;
		Intent intent = new Intent(Intent.ACTION_DIAL,Uri.parse(phoneNumber));
		startActivity(intent);
		if(true) {
			
		}
			
	}
	private void handleCallClick() {
		if(phoneNo.length() > 10) {
			int len = phoneNo.length();
			phoneNo = phoneNo.substring((len - 10), (len));
		}
		phoneNo = "0"+phoneNo;
		String phoneNumber = "tel:" + phoneNo;
		Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse(phoneNumber));
		startActivity(intent);
	}
}
