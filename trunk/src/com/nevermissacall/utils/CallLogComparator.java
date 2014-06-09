package com.nevermissacall.utils;

import java.util.Comparator;
import java.util.Date;

import com.nevermissacall.data.CallLogModel;

public class CallLogComparator implements Comparator<CallLogModel>{	 

	public int compare(CallLogModel record1, CallLogModel record2){    
    	long timestamp1, timestamp2;
    	timestamp1 = record1.getDate();
    	timestamp2 = record2.getDate();        
    	Date date1, date2;
    	date1 = new Date(timestamp1);
    	date2 = new Date(timestamp2);
    	
    	if(date1.before(date2)) {
    		return 1;
    	}
    	return -1;    		
    }
}