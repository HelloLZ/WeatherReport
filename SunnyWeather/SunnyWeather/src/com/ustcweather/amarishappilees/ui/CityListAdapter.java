package com.ustcweather.amarishappilees.ui;

import java.util.List;

import com.ustcweather.amarishappilees.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class CityListAdapter extends BaseAdapter {
	private List<MyCityEntity> mColl;
	private Context mCtx;
	private LayoutInflater myInflater;

	public CityListAdapter(Context mContext, List<MyCityEntity> mColl) {
		this.mCtx = mContext;
		this.mColl = mColl;
		this.myInflater = LayoutInflater.from(mContext);
	}

	public int getCount() {
		return mColl.size();
	}

	public Object getItem(int position) {
		return mColl.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View listView, ViewGroup parent) {
		MyCityEntity entity = mColl.get(position);
		myViewHolder viewHolder = null;
		if (listView == null) {
			listView = myInflater.inflate(R.layout.item_city_list, null);
			viewHolder = new myViewHolder();
			viewHolder.myCityName = (TextView) listView.findViewById(R.id.textView_mycity);
			viewHolder.myCityBox = (CheckBox) listView.findViewById(R.id.checkbox_delect_mycity);
		} else {
			viewHolder = (myViewHolder) listView.getTag();
		}
		if (viewHolder != null) {
			viewHolder.myCityName.setText(entity.getMyCityName());
			viewHolder.myCityBox.setVisibility(entity.getBoxVisible());
			viewHolder.myCityBox.setChecked(entity.getIsChecked());
		}
		return listView;
	}

	static class myViewHolder {
		public TextView myCityName;
		public CheckBox myCityBox;
	}
}
