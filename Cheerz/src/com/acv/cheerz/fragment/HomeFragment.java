package com.acv.cheerz.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lazyload.ImageLoader2;

import org.json.JSONArray;
import org.json.JSONObject;

import z.lib.base.callback.RestClient.RequestMethod;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.acv.cheerz.OverMainActivity;
import com.acv.cheerz.R;
import com.acv.cheerz.activity.RootActivity;
import com.acv.cheerz.dialog.WebDialog;
import com.acv.cheerz.service.CheerzServiceCallBack;
import com.acv.cheerz.util.CheerzUtils;
import com.acv.cheerz.view.HeaderView;
import com.acv.cheerz.view.HeaderView.IOnClickHeader;
import com.acv.cheerz.view.HomeHeaderView;
import com.acv.cheerz.view.HomeItemBotomView;
import com.etsy.android.grid.StaggeredGridView;

public class HomeFragment extends Fragment implements View.OnClickListener {
	private StaggeredGridView mGridView;
	private HomeHeaderView homeHeaderView;

	private View search_main;
	private EditText edtsearch;

	public HomeFragment() {
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	};

	List<JSONObject> list = new ArrayList<JSONObject>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.home, null);
		initSearch(view);

		imageLoader2 = new ImageLoader2(getActivity());
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
				// ((RootActivity) getActivity()).search();
				openSearchController();
			}

			@Override
			public void onClickLeft() {
				Activity activity = getActivity();
				((RootActivity) getActivity()).openMenuLeft();
			}
		});

		getFeed();
		homeHeaderView.getFeedGallery();

		// WebDialog dialog = new WebDialog(getActivity());
		// dialog.setData("title",
		// "file:///android_asset/cheerz-term/cheerz-term.html");
		// dialog.show();
		return view;
	}

	private void openSearchController() {
		((RootActivity) getActivity()).enableMenu(false);
		edtsearch.setEnabled(true);
		search_main.setVisibility(View.VISIBLE);
		CheerzUtils.showKeyBoard(edtsearch);
	}

	private void initSearch(View view) {
		search_main = CheerzUtils.getView(view, R.id.search_main);
		edtsearch = CheerzUtils.getView(view, R.id.edtsearch);
		search_main.setOnClickListener(this);
		edtsearch.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId == KeyEvent.KEYCODE_ENDCALL || event == null || KeyEvent.KEYCODE_CALL == actionId) {
					edtsearch.setEnabled(false);
					CheerzUtils.hiddenKeyBoard(getActivity());
					CheerzUtils.hiddenKeyBoard(edtsearch);
					search_main.setVisibility(View.GONE);
					((RootActivity) getActivity()).search(edtsearch.getText().toString());
					((RootActivity) getActivity()).enableMenu(true);
				}
				return false;
			}
		});
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.search_main) {
			((RootActivity) getActivity()).enableMenu(true);
			edtsearch.setEnabled(false);
			CheerzUtils.hiddenKeyBoard(getActivity());
			CheerzUtils.hiddenKeyBoard(edtsearch);
			search_main.setVisibility(View.GONE);
		}
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

	private ImageLoader2 imageLoader2;

	protected void addFeed() {
		ArrayAdapter<JSONObject> adapter = new ArrayAdapter<JSONObject>(getActivity(), 0, list) {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				if (convertView == null) {
					convertView = new HomeItemBotomView(parent.getContext());
				}

				((HomeItemBotomView) convertView).setData(getItem(position));
				((HomeItemBotomView) convertView).loadUrl(getItem(position));

				final String url = CheerzUtils.getString(getItem(position), "img");

				ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView1);
				imageLoader2.DisplayImage(url, imageView);
				// ImageLoaderUtils.getInstance(getContext()).DisplayImageRound(url,
				// imageView, BitmapFactory.decodeResource(getResources(),
				// R.drawable.noimage));
				final String artist_id = CheerzUtils.getString(getItem(position), "artist_id");
				convertView.findViewById(R.id.like).setOnClickListener(new View.OnClickListener() {

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
			map.put("status_favorite", "1");
			CheerzUtils.execute(getActivity(), RequestMethod.POST, CheerzUtils.API.API_EDIT_FAVORITE, map, new CheerzServiceCallBack() {
				ProgressDialog progressDialog;

				@Override
				public void onStart() {
					super.onStart();
					progressDialog = ProgressDialog.show(getActivity(), null, null);
				}

				@Override
				public void onSucces(JSONObject jsonObject) {
					super.onSucces(jsonObject);
					progressDialog.dismiss();

					CheerzUtils.showDialog(getActivity(), jsonObject.toString());
				}

				@Override
				public void onError(String message) {
					super.onError(message);
					CheerzUtils.showDialog(getActivity(), message);

					progressDialog.dismiss();
				}
			});
		} else {
			// login
			((OverMainActivity) getActivity().getParent()).startLogin();
		}
	}

	private void callImage(String id) {
		((RootActivity) getActivity()).callImageDetail(id);
	}

	public boolean onBackPressed() {

		if (search_main.getVisibility() == View.VISIBLE) {
			onClick(search_main);
			return true;
		}
		return false;
	}
}