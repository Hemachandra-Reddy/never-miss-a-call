package com.nevermissacall.data;

public class CallLogModel {

	private int id;
	private String name;
	private String number;
	private int duration;
	private long date;
	private int calltype;

	public CallLogModel(int id, String name, String number, int duration, long date, int calltype)
	{
		this.name = name;
		this.number = number;
		this.duration = duration;
		this.date = date;
		this.id = id;
		this.calltype = calltype;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public long getDate() {
		return date;
	}

	public void setDate(long date) {
		this.date = date;
	}

	public int getCalltype() {
		return calltype;
	}

	public void setCalltype(int calltype) {
		this.calltype = calltype;
	}	
}
