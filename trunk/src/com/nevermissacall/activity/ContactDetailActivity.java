package com.nevermissacall.activity;

import java.util.ArrayList;

import com.nevermissacall.R;
import com.nevermissacall.adapters.ContactDetailArrayAdapter;
import com.nevermissacall.data.CallLogModel;
import com.nevermissacall.database.DatabaseHelper;
import com.nevermissacall.utils.Fonts;
import com.nevermissacall.utils.NeverMissLogs;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class ContactDetailActivity extends Activity implements OnClickListener {
	private String TAG="ContactDetailActivity";
	private String phoneNo;
	private String name;
	public DatabaseHelper calldata;
	private ListView mainListView;
	private ArrayAdapter<CallLogModel> listAdapter;
	public ArrayList<CallLogModel> numberlist;
	private Button callBtn,smsBtn;
	private TextView nameTxt,numberTxt;
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
			callBtn = (Button) findViewById(R.id.callBtnID);
			smsBtn = (Button) findViewById(R.id.smsBtnID);
			callBtn.setTypeface(Fonts.BOOK_ANTIQUA);
			smsBtn.setTypeface(Fonts.BOOK_ANTIQUA);
			nameTxt = (TextView) findViewById(R.id.nameID);
			nameTxt.setTypeface(Fonts.BOOK_ANTIQUA, Typeface.BOLD);			
			numberTxt = (TextView) findViewById(R.id.numberID);
			numberTxt.setTypeface(Fonts.BOOK_ANTIQUA);
			nameTxt.setText(name);
			numberTxt.setText("" + phoneNo);
			callBtn.setOnClickListener(this);
			smsBtn.setOnClickListener(this);
			mainListView.setSmoothScrollbarEnabled(true);
			getAllEntriesForNumber();
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
			case R.id.callBtnID:
				if(phoneNo.startsWith("91")) {
					phoneNo = "+" + phoneNo;
				}
					
				String phoneNumber = "tel:" + phoneNo;
				Intent intent = new Intent(Intent.ACTION_CALL,
						Uri.parse(phoneNumber));
				startActivity(intent);
				break;
			case R.id.smsBtnID:
				startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts(
						"sms", phoneNo.toString(), null)));
			default:
				break;
			}
		} catch (Exception e) {
			e.getMessage();
		}
	}
}
