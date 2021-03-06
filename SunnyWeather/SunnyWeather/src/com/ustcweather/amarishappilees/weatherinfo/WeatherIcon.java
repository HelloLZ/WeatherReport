package com.ustcweather.amarishappilees.weatherinfo;

import com.ustcweather.amarishappilees.R;

public class WeatherIcon {

	public WeatherIcon() {

	}

	public int getWeatherIcon(String weather, boolean isDay) {
		String name = null;
		int position = weather.indexOf('ת');
		if (position != -1) {
			name = weather.substring(0, position);
		} else {
			name = weather;
		}
		switch (name) {
		case "��":
			if (isDay) {
				return R.drawable.d_01;
			} else {
				return R.drawable.n_01;
			}
		case "����":
			if (isDay) {
				return R.drawable.d_02;
			} else {
				return R.drawable.n_02;
			}
		case "��":
			if (isDay) {
				return R.drawable.d_03;
			} else {
				return R.drawable.n_03;
			}
		case "����":
			if (isDay) {
				return R.drawable.d_04;
			} else {
				return R.drawable.n_04;
			}
		case "��������":
			if (isDay) {
				return R.drawable.d_05;
			} else {
				return R.drawable.n_05;
			}
		case "��������б���":
			if (isDay) {
				return R.drawable.d_06;
			} else {
				return R.drawable.n_06;
			}
		case "���ѩ":
			if (isDay) {
				return R.drawable.d_07;
			} else {
				return R.drawable.n_07;
			}
		case "С��":
			if (isDay) {
				return R.drawable.d_08;
			} else {
				return R.drawable.n_08;
			}
		case "����":
			if (isDay) {
				return R.drawable.d_09;
			} else {
				return R.drawable.n_09;
			}
		case "����":
			if (isDay) {
				return R.drawable.d_10;
			} else {
				return R.drawable.n_10;
			}
		case "����":
			if (isDay) {
				return R.drawable.d_11;
			} else {
				return R.drawable.n_11;
			}
		case "����":
			if (isDay) {
				return R.drawable.d_12;
			} else {
				return R.drawable.n_12;
			}
		case "�ش���":
			if (isDay) {
				return R.drawable.d_13;
			} else {
				return R.drawable.n_13;
			}
		case "��ѩ":
			if (isDay) {
				return R.drawable.d_14;
			} else {
				return R.drawable.n_14;
			}
		case "Сѩ":
			if (isDay) {
				return R.drawable.d_15;
			} else {
				return R.drawable.n_15;
			}
		case "��ѩ":
			if (isDay) {
				return R.drawable.d_16;
			} else {
				return R.drawable.n_16;
			}
		case "��ѩ":
			if (isDay) {
				return R.drawable.d_17;
			} else {
				return R.drawable.n_17;
			}
		case "��ѩ":
			if (isDay) {
				return R.drawable.d_18;
			} else {
				return R.drawable.n_18;
			}
		case "��":
			if (isDay) {
				return R.drawable.d_19;
			} else {
				return R.drawable.n_19;
			}
		case "����":
			if (isDay) {
				return R.drawable.d_20;
			} else {
				return R.drawable.n_20;
			}
		case "ɳ����":
			if (isDay) {
				return R.drawable.d_21;
			} else {
				return R.drawable.n_21;
			}
		case "С��ת����":
			if (isDay) {
				return R.drawable.d_22;
			} else {
				return R.drawable.n_22;
			}
		case "����ת����":
			if (isDay) {
				return R.drawable.d_23;
			} else {
				return R.drawable.n_23;
			}
		case "����ת����":
			if (isDay) {
				return R.drawable.d_24;
			} else {
				return R.drawable.n_24;
			}
		case "����ת����":
			if (isDay) {
				return R.drawable.d_25;
			} else {
				return R.drawable.n_25;
			}
		case "����ת�ش���":
			if (isDay) {
				return R.drawable.d_26;
			} else {
				return R.drawable.n_26;
			}
		case "Сѩת��ѩ":
			if (isDay) {
				return R.drawable.d_27;
			} else {
				return R.drawable.n_27;
			}
		case "��ѩת��ѩ":
			if (isDay) {
				return R.drawable.d_28;
			} else {
				return R.drawable.n_28;
			}
		case "��ѩת��ѩ":
			if (isDay) {
				return R.drawable.d_29;
			} else {
				return R.drawable.n_29;
			}
		case "����":
			if (isDay) {
				return R.drawable.d_30;
			} else {
				return R.drawable.n_30;
			}
		case "��ɳ":
			if (isDay) {
				return R.drawable.d_31;
			} else {
				return R.drawable.n_31;
			}
		case "ǿɳ����":
			if (isDay) {
				return R.drawable.d_32;
			} else {
				return R.drawable.n_32;
			}
		case "��":
			if (isDay) {
				return R.drawable.d_01;
			} else {
				return R.drawable.n_01;
			}
		default:
			return R.drawable.d_01;
		}
	}
}
