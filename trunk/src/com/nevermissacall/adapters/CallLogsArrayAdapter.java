package com.nevermissacall.adapters;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.nevermissacall.R;
import com.nevermissacall.data.CallLogModel;
import com.nevermissacall.utils.Fonts;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CallLogsArrayAdapter extends ArrayAdapter<CallLogModel> {
	private LayoutInflater inflater;

	public CallLogsArrayAdapter(Context context, List<CallLogModel> callLogsList) {
		super(context, R.layout.list_item, R.id.nameTV, callLogsList);
		inflater = LayoutInflater.from(context);
	}

	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		try {
			final CallLogModel callLogModel = this.getItem(position);
			TextView nameTV;
			TextView numberTV;
			TextView dateTV;
			convertView = inflater.inflate(R.layout.list_item, null);
			nameTV = (TextView) convertView.findViewById(R.id.nameTV);
			nameTV.setTypeface(Fonts.BOOK_ANTIQUA, Typeface.BOLD);
			numberTV = (TextView) convertView.findViewById(R.id.numberTV);
			numberTV.setTypeface(Fonts.BOOK_ANTIQUA);
			dateTV = (TextView) convertView.findViewById(R.id.dateTV);
			dateTV.setTypeface(Fonts.BOOK_ANTIQUA);
			nameTV.setText(callLogModel.getName());
			numberTV.setText("" + callLogModel.getNumber());
			String displayDate = getDateTime(callLogModel.getDate());
			dateTV.setText(displayDate);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return convertView;
	}
	@SuppressLint("SimpleDateFormat")
	private String getDateTime(long milliseconds) {
		Date date = null;
		try {
			date = new Date(milliseconds);
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return new SimpleDateFormat("dd-MMM HH:mm").format(date);
	}
}
