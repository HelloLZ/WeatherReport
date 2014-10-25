package com.ustcweather.amarishappilees;

import com.ustcweather.amarishappilees.ProvinceFragment.OnItemClickedListener;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;


public class CityActivity extends FragmentActivity implements OnItemClickedListener{

	FragmentManager fm;
	FragmentTransaction ft;
	CityFragment cf;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		fm = this.getSupportFragmentManager();		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onItemClickedListener(long id) {
		// TODO Auto-generated method stub
		ft = fm.beginTransaction();
		cf = CityFragment.newInstance(id);
		ft.replace(R.id.fragment1, cf);
		ft.commit();
	}
   
}

