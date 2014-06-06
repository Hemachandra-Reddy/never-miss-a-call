package com.nevermissacall.adapters;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.nevermissacall.R;
import com.nevermissacall.data.CallLogModel;
import com.nevermissacall.utils.Fonts;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ContactDetailArrayAdapter extends ArrayAdapter<CallLogModel> {
	private LayoutInflater inflater;
	
	public ContactDetailArrayAdapter(Context context, List<CallLogModel> callLogsList) {
		super(context, R.layout.list_item, R.id.nameTV, callLogsList);
		inflater = LayoutInflater.from(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		try {
			final CallLogModel callLogModel = this.getItem(position);
			TextView dateTV,timeTV;
			convertView = inflater.inflate(R.layout.number_list_item, null);
			dateTV = (TextView) convertView.findViewById(R.id.dateTVID);
			timeTV = (TextView) convertView.findViewById(R.id.timeTVID);
			dateTV.setTypeface(Fonts.BOOK_ANTIQUA);
			timeTV.setTypeface(Fonts.BOOK_ANTIQUA);
			String displayDate = getDateTime(callLogModel.getDate(),true);
			String displayTime = getDateTime(callLogModel.getDate(),false);
			dateTV.setText(displayDate);
			timeTV.setText(displayTime);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return convertView;
	}
	@SuppressLint("SimpleDateFormat")
	private String getDateTime(long milliseconds,boolean giveDate) {
		Date date = null;
		try {
			date = new Date(milliseconds);
		} catch (Exception e) {
			e.printStackTrace();
		}		
		if(giveDate){
			return new SimpleDateFormat("dd-MMM-yy").format(date);
		}else{
			return new SimpleDateFormat("HH:mm:ss").format(date);	
		}
		
	}
}
