package com.ustcweather.amarishappilees;

import com.ustcweather.amarishappilees.db.DataBase;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;


public class CityActivity extends FragmentActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.city_main);		
		initCityDB();
	} 
	
	public void initCityDB() {
		Context context = getApplicationContext();
		DataBase cityDB = new DataBase();
		cityDB.copyDataBase(context);
	}
}

