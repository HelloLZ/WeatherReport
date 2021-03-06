package com.ustcweather.amarishappilees;

import com.ustcweather.amarishappilees.db.DataBase;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBar;


public class CityActivity extends FragmentActivity implements CityFragment.CityDrawerCallbacks{
	

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.city_main);		
		restoreActionBar();
		initCityDB();
	} 
	
	public void initCityDB() {
		Context context = getApplicationContext();
		DataBase cityDB = new DataBase();
		cityDB.copyDataBase(context);
	}
	
	@Override
    public void onBackPressed() {
		Intent intent = new Intent(this,MainActivity.class);		
		startActivity(intent);
		finish();
    	return;
    }
    
	@Override
	public void onCityItemSelected(String myCityName) {
		// update the main content by replacing fragments
		Bundle bundle = new Bundle();
		bundle.putString("cityName", myCityName);
		CityActivity.this.setResult(RESULT_OK, this.getIntent().putExtras(bundle));
		CityActivity.this.finish();
	}
	
	public void restoreActionBar() {
		android.app.ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
		String title = "选择城市";
		actionBar.setTitle(title);
	}
}

