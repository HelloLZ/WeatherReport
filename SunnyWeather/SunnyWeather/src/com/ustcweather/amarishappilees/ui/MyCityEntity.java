package com.ustcweather.amarishappilees.ui;

import android.view.View;

public class MyCityEntity {
	private String myCityName;
	private int buttonVisible = View.INVISIBLE;
	
	public MyCityEntity() {
		
	}
	
	public MyCityEntity(String myCityName, int buttonVisible) {
		super();
		this.myCityName = myCityName;
		this.buttonVisible = buttonVisible;
	}
	
	public void setMyCityName (String myCityName) {
		this.myCityName = myCityName;
	}
	
	public String getMyCityName() {
		return this.myCityName;
	}
	
	public void setButtonVisible(int buttonVisible) {
		this.buttonVisible = buttonVisible;
	}
	
	public int getButtonVisible() {
		return this.buttonVisible;
	}
}
