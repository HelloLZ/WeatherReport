package com.ustcweather.amarishappilees;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.ustcweather.amarishappilees.db.DataBase;

import android.app.Activity;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class CityFragment extends ListFragment{
	
	private static ExecutorService LIMITED_TASK_EXECUTOR  = (ExecutorService) Executors.newFixedThreadPool(1);
	private List<String> cityData = new ArrayList<String>();
	private List<String> bufferCityData = new ArrayList<String>();
	private ArrayAdapter<String> cityAdapter;
	private Cursor cityCursor;
	private CityDrawerCallbacks cityCallbacks;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		cityAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, cityData);
		setListAdapter(cityAdapter);
	}
	
	@Override  
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {  

        View view = inflater.inflate(R.layout.fragment_city_list, null);
        DataBase dataBase = new DataBase();

        Bundle bundle = this.getArguments();  
        if(bundle == null) {
        	return view;
        }
        cityCursor = dataBase.readCityDataBaseSDCard(bundle.getString("provinceName"));
        getData();  
        
        return view;  
    }  
	
	class CityAsyncTask extends AsyncTask<String, Integer, String[]> {

		@Override
		protected String[] doInBackground(String... params) {
			// TODO Auto-generated method stub	
//			cityData.clear();
			while (cityCursor.moveToNext()) {
				String name =cityCursor.getString(cityCursor.getColumnIndex("city"));  
//				cityData.add(name);
				bufferCityData.add(name);
			}
			cityCursor.close();			
			return params;
		}

		@Override
		protected void onPostExecute(String[] result) {
			// TODO Auto-generated method stub	
			cityData.clear();
			cityData.addAll(bufferCityData);
			cityAdapter.notifyDataSetChanged();
			bufferCityData.clear();
		}
	}
	
	private  void getData() {
		CityAsyncTask task = new CityAsyncTask();
		task.executeOnExecutor(LIMITED_TASK_EXECUTOR);
	}
	
    @Override  
    public void onListItemClick(ListView l, View v, int position, long id) {  
        super.onListItemClick(l, v, position, id);  
        String cityName =  cityAdapter.getItem(position);
        cityCallbacks.onCityItemSelected(cityName);
    } 
    
    public static interface CityDrawerCallbacks {

    	void onCityItemSelected(String myCityName);
    }
    
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            cityCallbacks = (CityDrawerCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement CityDrawerCallbacks.");
        }
    }
}
