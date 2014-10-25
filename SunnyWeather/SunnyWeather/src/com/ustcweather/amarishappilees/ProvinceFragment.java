package com.ustcweather.amarishappilees;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ProvinceFragment  extends ListFragment{
	
	String[] item = { "����", "�Ĵ�" };
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		mListener.onItemClickedListener(id);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		setListAdapter(new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_list_item_1, item));
	}

	OnItemClickedListener mListener;

	public interface OnItemClickedListener {
		public void onItemClickedListener(long id);
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);

		try {
				mListener = (OnItemClickedListener) activity;
			} catch (ClassCastException e) {
				e.printStackTrace();
			}
		}
}