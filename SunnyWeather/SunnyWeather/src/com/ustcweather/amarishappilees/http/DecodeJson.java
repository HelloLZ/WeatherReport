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
			String status = jsonObj.getString("status");
			String date = jsonObj.getString("date");
			JSONArray results = jsonObj.getJSONArray("results");
			JSONObject results0 = results.getJSONObject(0);
			String currentCity = results0.getString("currentCity");
			String pm = results0.getString("pm25");
			WeatherRelativeInfo weatherReInfo = new WeatherRelativeInfo();
			weatherReInfo.setCurrentCity(currentCity);
			weatherReInfo.setDate(date);
			weatherReInfo.setPm(pm);
			return weatherReInfo;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
}
