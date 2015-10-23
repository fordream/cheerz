package com.acv.cheerz.view;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.acv.cheerz.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

//com.acv.cheerz.view.CheerzHomeListView
public class CheerzHomeListView extends CheerzListView {
	private long mHeight = 0;
	private MAdapter adapter;

	public long getmHeight() {
		return mHeight;
	}

	public void addItem(final int width, final int height, JSONObject json) {
		adapter.addItem(json);
		// adapter.notifyDataSetChanged();

		new Thread() {
			public void run() {
				int newWidth = getWidth();
				int newHeight = 0;
				newHeight = height * newWidth / width;
				mHeight += newHeight + getContext().getResources().getDimension(R.dimen.dimen_60dp);

				post(new Runnable() {
					public void run() {
						setListViewHeightBasedOnChildren();
					}
				});
			};
		}.start();

	}

	private void setListViewHeightBasedOnChildren() {
		ListView listView = this;
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}

		int totalHeight = listView.getPaddingTop() + listView.getPaddingBottom();
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			if (listItem instanceof ViewGroup) {
				listItem.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			}
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = (int) mHeight;

		// (totalHeight +
		// (listView.getDividerHeight() *
		// (listAdapter.getCount() - 1)));
		listView.setLayoutParams(params);
	}

	public CheerzHomeListView(Context context) {
		super(context);
		init();
	}

	public CheerzHomeListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public CheerzHomeListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		adapter = new MAdapter(getContext(), 0);
		setAdapter(adapter);
	}

	private class MAdapter extends ArrayAdapter<JSONObject> {
		private List<JSONObject> jsonObjects = new ArrayList<JSONObject>();

		@Override
		public int getCount() {
			return jsonObjects.size();
		}

		public void addItem(JSONObject json) {
			jsonObjects.add(json);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = new HomeItemBotomView(parent.getContext());
			}

			((HomeItemBotomView) convertView).setData(jsonObjects.get(position));
			((HomeItemBotomView) convertView).loadUrl(jsonObjects.get(position));
			return convertView;
		}

		public MAdapter(Context context, int resource) {
			super(context, resource);
		}

	}
}
