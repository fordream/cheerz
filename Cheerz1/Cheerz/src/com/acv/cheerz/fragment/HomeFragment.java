package com.acv.cheerz.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import z.lib.base.callback.ExeCallBack;
import z.lib.base.callback.ResClientCallBack;
import z.lib.base.callback.RestClient;
import z.lib.base.callback.RestClient.RequestMethod;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.acv.cheerz.MainActivity;
import com.acv.cheerz.OverMainActivity;
import com.acv.cheerz.R;
import com.acv.cheerz.activity.RootActivity;
import com.acv.cheerz.service.CheerzServiceCallBack;
import com.acv.cheerz.util.CheerzUtils;
import com.acv.cheerz.view.HeaderView;
import com.acv.cheerz.view.HeaderView.IOnClickHeader;
import com.acv.cheerz.view.HomeHeaderView;
import com.acv.cheerz.view.HomeItemBotomView;
import com.etsy.android.grid.StaggeredGridView;

public class HomeFragment extends Fragment {
	// private CheerzScrollView scroll;
	// private com.acv.cheerz.view.CheerzHomeListView list_left;
	// private com.acv.cheerz.view.CheerzHome2ListView list_right;
	// private com.acv.cheerz.view.CheerzViewPager gallery;
	// private View home_galery;
	private StaggeredGridView mGridView;
	private HomeHeaderView homeHeaderView;

	// private View viewpager;

	public HomeFragment() {
	}

	List<JSONObject> list = new ArrayList<JSONObject>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.home, null);

		homeHeaderView = new HomeHeaderView(getActivity()) {
			@Override
			public void callBackItem(JSONObject jsonObject) {
				super.callBackItem(jsonObject);

				String type = CheerzUtils.getString(jsonObject, "type");

				if ("1".equals(type)) {
					callImage(CheerzUtils.getString(jsonObject, "msg"));
				} else {
					CheerzUtils.callWeb(getActivity(), CheerzUtils.getString(jsonObject, "url_web"));
				}
			}
		};

		homeHeaderView.visibility(false);
		// viewpager = inflater.inflate(R.id.home_pager, null);
		mGridView = (StaggeredGridView) view.findViewById(R.id.grid_view);
		mGridView.addHeaderView(homeHeaderView);
		mGridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				JSONObject jsonObject = (JSONObject) parent.getItemAtPosition(position);
				callImage(CheerzUtils.getString(jsonObject, "image_id"));
			}
		});

		HeaderView headerView = (HeaderView) view.findViewById(R.id.headerView1);
		headerView.setiOnClickHeader(new IOnClickHeader() {

			@Override
			public void onClickRight() {
				((RootActivity) getActivity()).search();
			}

			@Override
			public void onClickLeft() {
				Activity activity = getActivity();
				((RootActivity) getActivity()).openMenuLeft();
			}
		});

		getFeed();
		homeHeaderView.getFeedGallery();
		return view;
	}

	private void getFeed() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("page", "feed");
		CheerzUtils.execute(getActivity(), RequestMethod.GET, CheerzUtils.API.API_GET_FEED, params, new CheerzServiceCallBack() {
			@Override
			public void onError(String message) {
				super.onError(message);
				CheerzUtils.showDialog(getActivity(), message);
			}

			@Override
			public void onSucces(JSONObject jsonObject) {
				super.onSucces(jsonObject);

				try {
					JSONArray array = jsonObject.getJSONArray("data");

					for (int i = 0; i < array.length(); i++) {
						JSONObject x = array.getJSONObject(i);
						list.add(x);
					}
				} catch (Exception exception) {

				}

				addFeed();
			}
		});
	}

	protected void addFeed() {
		ArrayAdapter<JSONObject> adapter = new ArrayAdapter<JSONObject>(getActivity(), 0, list) {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				if (convertView == null) {
					convertView = new HomeItemBotomView(parent.getContext());
				}

				((HomeItemBotomView) convertView).setData(getItem(position));
				((HomeItemBotomView) convertView).loadUrl(getItem(position));
				final String artist_id = CheerzUtils.getString(getItem(position), "artist_id");

				convertView.findViewById(R.id.imageView3).setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						likeArtist(artist_id);
					}

				});
				return convertView;
			}
		};

		mGridView.setAdapter(adapter);
	}

	private void likeArtist(String artist_id) {
		if (CheerzUtils.isLogin(getActivity())) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("artist_id", artist_id);
			map.put("user_id", CheerzUtils.getUserId(getActivity()));
			
			CheerzUtils.execute(getActivity(), RequestMethod.GET,CheerzUtils.API.API_EDIT_FAVORITE , map, new CheerzServiceCallBack(){
				
			});
		} else {
			// login
		}
	}

	private void callImage(String id) {
		((RootActivity) getActivity()).callImageDetail(id);
	}
}