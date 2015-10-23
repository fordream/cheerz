package com.acv.cheerz.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import z.lib.base.ImageLoaderUtils;
import z.lib.base.LogUtils;
import z.lib.base.callback.ExeCallBack;
import z.lib.base.callback.ResClientCallBack;
import z.lib.base.callback.RestClient;
import z.lib.base.callback.RestClient.RequestMethod;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.acv.cheerz.R;
import com.acv.cheerz.service.CheerzServiceCallBack;
import com.acv.cheerz.util.CheerzUtils;

public class HomeHeaderView extends LinearLayout {
	private View home_pager_main;
	private com.acv.cheerz.view.CheerzViewPager home_pager_gallery1;

	public HomeHeaderView(Context context) {
		super(context);
		((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.home_pager, this);

		home_pager_main = findViewById(R.id.home_pager_main);
		home_pager_gallery1 = (com.acv.cheerz.view.CheerzViewPager) findViewById(R.id.home_pager_gallery1);
	}

	public void setAdapter(PagerAdapter adapter) {
		((ViewPager) findViewById(R.id.home_pager_gallery1)).setAdapter(adapter);
	}

	public void visibility(boolean isShow) {
		home_pager_main.setVisibility(isShow ? View.VISIBLE : View.GONE);
	}

	public void getFeedGallery() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("page", "gallery");

		CheerzUtils.execute(getContext(), RequestMethod.GET, CheerzUtils.API.API_GET_FEED_GALLLEY, params, new CheerzServiceCallBack() {
			@Override
			public void onError(String message) {
				super.onError(message);
			}

			@Override
			public void onSucces(JSONObject jsonObject) {
				super.onSucces(jsonObject);
				try {
					JSONArray array = jsonObject.getJSONArray("data");
					for (int i = 0; i < array.length(); i++) {
						data.add(array.getJSONObject(i));
					}
				} catch (Exception exception) {
				}

				if (data.size() > 0) {
					// home_galery.setVisibility(View.VISIBLE);
				}
				PagerAdapter adapter = new PagerAdapter() {
					public int getCount() {
						return data.size();
					}

					public Object instantiateItem(View collection, final int position) {

						View view = ((LayoutInflater) collection.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.home_item_top, null);
						view.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
						ImageView imageView1 = (ImageView) view.findViewById(R.id.imageView1);
						String url = CheerzUtils.getString(data.get(position), "img");
						ImageLoaderUtils.getInstance(collection.getContext()).DisplayImage(url, imageView1, BitmapFactory.decodeResource(getResources(), R.drawable.noimage));
						String text = CheerzUtils.getString(data.get(position), "msg");
						if (CheerzUtils.isBlank(text)) {
							text = CheerzUtils.getString(data.get(position), "url_web");
						}
						((TextView) view.findViewById(R.id.header_title)).setText(text);
						((ViewPager) collection).addView(view, 0);

						view.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								callBackItem(data.get(position));
								String type = CheerzUtils.getString(data.get(position), "type");

								if ("1".equals(type)) {
									// image
									// callImage(CheerzUtils.getString(data.get(position),
									// "msg"));
								} else {
									// callWeb(CheerzUtils.getString(data.get(position),
									// "url_web"));
								}
							}

						});
						return view;
					}

					@Override
					public void destroyItem(View arg0, int arg1, Object arg2) {
						((ViewPager) arg0).removeView((View) arg2);
					}

					@Override
					public boolean isViewFromObject(View arg0, Object arg1) {
						return arg0 == ((View) arg1);
					}

					@Override
					public Parcelable saveState() {
						return null;
					}
				};
				visibility(true);
				if (data.size() > 0) {
					// gallery.setVisibility(View.VISIBLE);
					visibility(true);
					home_pager_gallery1.setAdapter(adapter);
				}
			}
		});

	}

	List<JSONObject> data = new ArrayList<JSONObject>();

	public void callBackItem(JSONObject jsonObject) {

	}
}
