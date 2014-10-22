package com.ustcweather.amarishappilees.http;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ustcweather.amarishappilees.weatherinfo.WeatherRelativeInfo;

public class DecodeJson {

	public DecodeJson() {

	}

	public static WeatherRelativeInfo getJsonInfo(String json) {
		// JSONObject decodeObject = JSONObject.fromObject(json);
		JSONObject jsonObj;
		try {
			jsonObj = new JSONObject(json);

			if (jsonObj.getInt("error") != 0) {
				return null;
			}
//			String status = jsonObj.getString("status");
			String date = jsonObj.getString("date");
			
			JSONArray results = jsonObj.getJSONArray("results");
			JSONObject results0 = results.getJSONObject(0);
			String currentCity = results0.getString("currentCity");
			String pm = results0.getString("pm25");
			
			/*Today weather information*/
			JSONArray weatherData = results0.getJSONArray("weather_data");
			JSONObject weatherToday = weatherData.getJSONObject(0);
			String todayDate = weatherToday.getString("date");
			String todayWeather = weatherToday.getString("weather");
			String todayWind = weatherToday.getString("wind");
			String todayTempera = weatherToday.getString("temperature");
			
			/*Tomorrow weather information*/
			JSONObject weatherTomorrow = weatherData.getJSONObject(1);
			String tomorrowDate = weatherTomorrow.getString("date");
			String tomorrowWeather = weatherTomorrow.getString("weather");
			String tomorrowTempera = weatherTomorrow.getString("temperature");
			
			/*The day after tomorrow weather information*/
			JSONObject weatherAfTomo = weatherData.getJSONObject(2);
			String afTomoDate = weatherAfTomo.getString("date");
			String afTomoWeather = weatherAfTomo.getString("weather");
			String afTomoTempera = weatherAfTomo.getString("temperature");
			
			/*The last day weather information*/
			JSONObject weatherLast = weatherData.getJSONObject(3);
			String lastDate = weatherLast.getString("date");
			String lastWeather = weatherLast.getString("weather");
			String lastTempera = weatherLast.getString("temperature");
			
			/*Set main information*/
			WeatherRelativeInfo weatherReInfo = new WeatherRelativeInfo();
			weatherReInfo.setCurrentCity(currentCity);
			weatherReInfo.setDate(date);
			weatherReInfo.setPm(pm);
			
			/*Set today weather information*/
			weatherReInfo.setTodayDate(todayDate);
			weatherReInfo.setTodayTempera(todayTempera);
//			weatherReInfo.setTodayTemperaMore(todayTemperaMore);
			weatherReInfo.setTodayWeather(todayWeather);
			weatherReInfo.setTodayWind(todayWind);
			
			/*Set Tomorrow weather information*/
			weatherReInfo.setTomorrowDate(tomorrowDate);
			weatherReInfo.setTomorrowTempera(tomorrowTempera);
			weatherReInfo.setTomorrowWeather(tomorrowWeather);
			
			/*Set the day after tomorrow weather information*/
			weatherReInfo.setAfTomoDate(afTomoDate);
			weatherReInfo.setAfTomoTempera(afTomoTempera);
			weatherReInfo.setAfTomoWeather(afTomoWeather);
			
			/*Set the last day weather information*/
			weatherReInfo.setLastDate(lastDate);
			weatherReInfo.setLastTempera(lastTempera);
			weatherReInfo.setLastWeather(lastWeather);
			
			return weatherReInfo;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
}
