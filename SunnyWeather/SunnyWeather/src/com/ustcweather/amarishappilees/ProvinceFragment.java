package com.ustcweather.amarishappilees;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.ustcweather.amarishappilees.db.DataBase;

import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;


public class ProvinceFragment  extends ListFragment{
	
	private static ExecutorService LIMITED_TASK_EXECUTOR  = (ExecutorService) Executors.newFixedThreadPool(7);
	private Cursor provinceCursor = null;
	private List<String> provinceData= new ArrayList<String>();
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_city_list, container, false);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DataBase dataBase = new DataBase();
		provinceCursor = dataBase.readDataBaseFromSDCard();
		getData();
//		setListAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, city));
	}
	
	class ProvinceAsyncTask extends AsyncTask<String, Integer, String[]> {

		@Override
		protected String[] doInBackground(String... params) {
			// TODO Auto-generated method stub	
			while (provinceCursor.moveToNext()) {
				String name =provinceCursor.getString(provinceCursor.getColumnIndex("name"));  
				provinceData.add(name);
			}
			provinceCursor.close();
			return params;
		}

		@Override
		protected void onPostExecute(String[] result) {
			// TODO Auto-generated method stub
			setListAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, provinceData));
		}
	}
	
	private  void getData() {
		ProvinceAsyncTask task = new ProvinceAsyncTask();
		task.executeOnExecutor(LIMITED_TASK_EXECUTOR);
	}
	
}
