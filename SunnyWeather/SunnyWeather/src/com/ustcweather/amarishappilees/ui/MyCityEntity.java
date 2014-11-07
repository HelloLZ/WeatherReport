package com.ustcweather.amarishappilees.ui;

import android.view.View;

public class MyCityEntity {
	private String myCityName;
	private int boxVisible = View.INVISIBLE;
	private boolean isChecked = false;
	
	public MyCityEntity() {
		
	}
	
	public MyCityEntity(String myCityName, int  boxVisible) {
		super();
		this.myCityName = myCityName;
		this. boxVisible =  boxVisible;
	}
	
	public void setMyCityName (String myCityName) {
		this.myCityName = myCityName;
	}
	
	public String getMyCityName() {
		return this.myCityName;
	}
	
	public void setBoxVisible(int buttonVisible) {
		this. boxVisible = buttonVisible;
	}
	
	public int getBoxVisible() {
		return this. boxVisible;
	}
	
	public void setCheckBoxIsChecked(boolean isChecked){
		this.isChecked = isChecked;
	}
	
	public boolean getIsChecked(){
		return isChecked;
	}
}
