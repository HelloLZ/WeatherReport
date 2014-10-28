package com.ustcweather.amarishappilees;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.ustcweather.amarishappilees.http.DecodeJson;
import com.ustcweather.amarishappilees.http.HttpOperate;
import com.ustcweather.amarishappilees.weatherinfo.WeatherRelativeInfo;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity implements
		NavigationDrawerFragment.NavigationDrawerCallbacks {

	/**
	 * Fragment managing the behaviors, interactions and presentation of the
	 * navigation drawer.
	 */
	private NavigationDrawerFragment mNavigationDrawerFragment;

	/**
	 * Used to store the last screen title. For use in
	 * {@link #restoreActionBar()}.
	 */
	private CharSequence mTitle;
	private String[] weatherInfo;

	private static TextView textTodayTempera;
	private static TextView textTodayWeather;
	private static TextView textTodayUp;
//	private static TextView textTodayDown;
	
//	private static TextView textWind;
//	private static TextView textPm;
//	private static TextView textWetValue;
	private static TextView textWindValue;
	private static TextView textPmValue;
	
	private static TextView textAAToday;
	private static TextView textAAAToday;
	private static TextView textTodayDate;
	private static TextView textATodayDate;
	private static TextView textAATodayDate;
	private static TextView textAAATodayDate;
	private static TextView textCTodayUp;
	private static TextView textCATodayUp;
	private static TextView textCAATodayUp;
	private static TextView textCAAATodayUp;
	private static TextView textCTodayDown;
	private static TextView textCATodayDown;
	private static TextView textCAATodayDown;
	private static TextView textCAAATodayDown;

	private String city = "苏州";
	private static ExecutorService LIMITED_TASK_EXECUTOR  = (ExecutorService) Executors.newFixedThreadPool(7);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager()
				.findFragmentById(R.id.navigation_drawer);
		mTitle = getTitle();
		// Set up the drawer.
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
				(DrawerLayout) findViewById(R.id.drawer_layout));
		
		weatherInfo = new String[23];
		getData();
	}
	
	class MyAsyncTask extends AsyncTask<String, Integer, String[]> {

		@Override
		protected String[] doInBackground(String... params) {
			// TODO Auto-generated method stub
			
			String httpGetString = HttpOperate.getWeatherInformation(city);
			WeatherRelativeInfo weatherReInfo = new WeatherRelativeInfo();
			weatherReInfo = DecodeJson.getJsonInfo(httpGetString);
			weatherInfo[0] = weatherReInfo.getTodayWeather(); 
			String todayDate = weatherReInfo.getTodayDate();
			weatherInfo[1] = todayDate.substring(14, 16);
			String todayTempRange = weatherReInfo.getTodayTempera();
			weatherInfo[2] = todayTempRange.substring(0, 2);
//			weatherInfo[3] = todayTempRange.substring(5, 7);
			
			weatherInfo[4] = weatherReInfo.getTodayWind();
			weatherInfo[5] = weatherReInfo.getPm();
			weatherInfo[6] = weatherReInfo.getAfTomoDate();
			weatherInfo[7] = weatherReInfo.getLastDate();
			
			weatherInfo[8] = weatherReInfo.getTodayWeather();
			weatherInfo[9] = weatherReInfo.getTodayWeather();
			weatherInfo[10] = weatherReInfo.getAfTomoWeather();
			weatherInfo[11] = weatherReInfo.getLastWeather();

			weatherInfo[12] = weatherReInfo.getTomorrowTempera().substring(0, 2) + "℃";
			weatherInfo[13] = weatherReInfo.getTomorrowTempera().substring(5, 8);
			weatherInfo[14] = weatherReInfo.getAfTomoTempera().substring(0, 2) + "℃";
			weatherInfo[15] = weatherReInfo.getAfTomoTempera().substring(5, 8);
			weatherInfo[16] = weatherReInfo.getLastTempera().substring(0, 2) + "℃";
			weatherInfo[17] = weatherReInfo.getLastTempera().substring(5, 8);
			
			return weatherInfo;
		}

		@Override
		protected void onPostExecute(String[] result) {
			// TODO Auto-generated method stub
			// super.onPostExecute(result);
			textTodayTempera.setText(weatherInfo[1]);
			textTodayWeather.setText(weatherInfo[0]);
			textTodayUp.setText(weatherInfo[2]);
//			textTodayDown.setText(weatherInfo[3]);
			
			textWindValue.setText(weatherInfo[4]);
			textPmValue.setText(weatherInfo[5]);
			
			textAAToday.setText(weatherInfo[6]);
			textAAAToday.setText(weatherInfo[7]);
			
			textTodayDate.setText(weatherInfo[8]);
			textATodayDate.setText(weatherInfo[9]);
			textAATodayDate.setText(weatherInfo[10]);
			textAAATodayDate.setText(weatherInfo[11]);
			
			textCTodayUp.setText(weatherInfo[2] + "℃");
			textCTodayDown.setText(weatherInfo[3] + "℃");
			textCATodayUp.setText(weatherInfo[12]);
			textCATodayDown.setText(weatherInfo[13]);
			textCAATodayUp.setText(weatherInfo[14]);
			textCAATodayDown.setText(weatherInfo[15]);
			textCAAATodayUp.setText(weatherInfo[16]);
			textCAAATodayDown.setText(weatherInfo[17]);
		}
	}
	
	private  void getData() {
		MyAsyncTask task = new MyAsyncTask();
		task.executeOnExecutor(LIMITED_TASK_EXECUTOR);
	}
	
	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onNavigationDrawerItemSelected(int position, String myCityName) {
		// update the main content by replacing fragments
		FragmentManager fragmentManager = getSupportFragmentManager();
		fragmentManager
				.beginTransaction()
				.replace(R.id.container,
						PlaceholderFragment.newInstance(position + 1)).commit();
		city = myCityName;
	}

	public void onSectionAttached(int number) {
		switch (1) {
		case 1:
//			mTitle = getString(R.string.title_section1);
			mTitle = city;
//			city = "苏州";
			getData();
			break;
		case 2:
			mTitle = getString(R.string.title_section2);
			city = "合肥";
			getData();
			break;
		case 3:
			mTitle = getString(R.string.title_section3);
			city = "成都";
			getData();
			break;
		}
	}

	public void restoreActionBar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(mTitle);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (!mNavigationDrawerFragment.isDrawerOpen()) {
			// Only show items in the action bar relevant to this screen
			// if the drawer is not showing. Otherwise, let the drawer
			// decide what to show in the action bar.
			getMenuInflater().inflate(R.menu.main, menu);
			restoreActionBar();
			return true;
		} 
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		private static final String ARG_SECTION_NUMBER = "section_number";

		/**
		 * Returns a new instance of this fragment for the given section number.
		 */
		public static PlaceholderFragment newInstance(int sectionNumber) {
			PlaceholderFragment fragment = new PlaceholderFragment();
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments(args);
			return fragment;
		}

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			textTodayTempera = (TextView) rootView.findViewById(R.id.textView_today_temperature);
			textTodayWeather = (TextView) rootView.findViewById(R.id.textView_today_weather);
			textTodayUp= (TextView) rootView.findViewById(R.id.textView_today_up);
//			textTodayDown = (TextView) rootView.findViewById(R.id.textView_today_down);
//			textWind = (TextView) rootView.findViewById(R.id.textView_wind);
//			textPm = (TextView) rootView.findViewById(R.id.textView_pm);
//			textWetValue = (TextView) rootView.findViewById(R.id.textView_wet_value);
			textWindValue = (TextView) rootView.findViewById(R.id.textView_wind_value);
			textPmValue = (TextView) rootView.findViewById(R.id.textView_pm_value);			
			textAAToday = (TextView) rootView.findViewById(R.id.textView_compare_aatoday);
			textAAAToday = (TextView) rootView.findViewById(R.id.textView_compare_aaatoday);
			textTodayDate = (TextView) rootView.findViewById(R.id.textView_compare_date);
			textATodayDate = (TextView) rootView.findViewById(R.id.textView_compare_adate);
			textAATodayDate = (TextView) rootView.findViewById(R.id.textView_compare_aadate);
			textAAATodayDate = (TextView) rootView.findViewById(R.id.textView_compare_aaadate);
			textCTodayUp = (TextView) rootView.findViewById(R.id.textView_up_today);
			textCATodayUp = (TextView) rootView.findViewById(R.id.textView_up_atoday);
			textCAATodayUp = (TextView) rootView.findViewById(R.id.textView_up_aatoday);
		    textCAAATodayUp = (TextView) rootView.findViewById(R.id.textView_up_aaatoday);
			textCTodayDown = (TextView) rootView.findViewById(R.id.textView_down_today);
			textCATodayDown = (TextView) rootView.findViewById(R.id.textView_down_atoday);
			textCAATodayDown = (TextView) rootView.findViewById(R.id.textView_down_aatoday);
			textCAAATodayDown = (TextView) rootView.findViewById(R.id.textView_down_aaatoday);

			return rootView;
		}

		@Override
		public void onAttach(Activity activity) {
			super.onAttach(activity);
			((MainActivity) activity).onSectionAttached(getArguments().getInt(
					ARG_SECTION_NUMBER));
		}
	}
}
