package com.ustcweather.amarishappilees.weatherinfo;

public class WeatherRelativeInfo {

	private String currentCity = null;
	private String date = null;
	private String pm = null;
	
	public WeatherRelativeInfo() {
		
	}
	
	public void setCurrentCity(String currentCity) {
		this.currentCity = currentCity;
	}
	
	public String getCurrentCity() {
		return this.currentCity;
	}
	
	public void setDate(String date) {
		this.date = date;
	}
	
	public String getdate() {
		return this.date;
	}
	
	public void setPm(String pm) {
		this.pm = pm;
	}
	
	public String getPm() {
		return this.pm;
	}
	
}
