package com.acv.cheerz.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lazyload.ImageLoader2;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import z.lib.base.callback.RestClient.RequestMethod;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.acv.cheerz.R;
import com.acv.cheerz.activity.RootActivity;
import com.acv.cheerz.service.CheerzServiceCallBack;
import com.acv.cheerz.util.CheerzUtils;
import com.acv.cheerz.view.HeaderView;
import com.acv.cheerz.view.MyPageItemView;
import com.acv.cheerz.view.HeaderView.IOnClickHeader;
import com.etsy.android.grid.StaggeredGridView;

public class MyPageFragment extends Fragment implements View.OnClickListener {
	private StaggeredGridView mGridView;
	public MyPageFragment() {
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	};

	List<JSONObject> list = new ArrayList<JSONObject>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.mypage, null);
		HeaderView headerView = (HeaderView) view.findViewById(R.id.headerView1);
		headerView.visibivityRight(false, R.drawable.transfer);
		headerView.visibivityLeft(true, R.drawable.btn_back);
		headerView.setTextHeader(R.string.activities_log);
		headerView.setiOnClickHeader(new IOnClickHeader() {

			@Override
			public void onClickRight() {

			}

			@Override
			public void onClickLeft() {
				((RootActivity) getActivity()).onBackPressed();
			}
		});
		imageLoader2 = new ImageLoader2(getActivity());
		mGridView = (StaggeredGridView) view.findViewById(R.id.grid_view);
		mGridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				JSONObject jsonObject = (JSONObject) parent.getItemAtPosition(position);
				callImage(CheerzUtils.getString(jsonObject, "image_id"));
			}
		});
		getListVote();
		return view;
	}

	@Override
	public void onClick(View v) {
		
	}

	private void getListVote() {
		Map<String, String> params = new HashMap<String, String>();
		CheerzUtils.execute(getActivity(), RequestMethod.GET, CheerzUtils.API.API_LIST_IMAGE_VOTE, params, new CheerzServiceCallBack() {
			@Override
			public void onError(String message) {
				super.onError(message);
				CheerzUtils.showDialog(getActivity(), message);
			}

			@Override
			public void onSucces(JSONObject jsonObject) {
				super.onSucces(jsonObject);
				try {
					if("1".equals(jsonObject.getString("status_code"))){
						try {
							JSONArray array = jsonObject.getJSONArray("data");

							for (int i = 0; i < array.length(); i++) {
								JSONObject x = array.getJSONObject(i);
								list.add(x);
							}
						} catch (Exception exception) {

						}

						addListVote();
					}else{
						String message = jsonObject.getString("msg_error");
						CheerzUtils.showDialog(getActivity(), message  );
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		});
	}

	private ImageLoader2 imageLoader2;

	protected void addListVote() {
		ArrayAdapter<JSONObject> adapter = new ArrayAdapter<JSONObject>(getActivity(), 0, list) {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				if (convertView == null) {
					convertView = new MyPageItemView(parent.getContext());
				}

				((MyPageItemView) convertView).setData(getItem(position));
				((MyPageItemView) convertView).loadUrl(getItem(position));

				final String url = CheerzUtils.getString(getItem(position), "img");

				ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView1);
				imageLoader2.DisplayImage(url, imageView);
				final String image_id = CheerzUtils.getString(getItem(position), "image_id");
				convertView.findViewById(R.id.main_mypage).setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						voteArtist(image_id);
					}

				});
				return convertView;
			}

		};

		mGridView.setAdapter(adapter);
	}

	private void voteArtist(String artist_id) {
		String message = "image_id=" + artist_id;
		CheerzUtils.showDialog(getActivity(), message );
	}

	private void callImage(String id) {
		((RootActivity) getActivity()).callImageDetail(id);
	}

	public boolean onBackPressed() {
		return false;
	}
}