package com.acv.cheerz.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import z.lib.base.callback.RestClient.RequestMethod;

import com.acv.cheerz.R;
import com.acv.cheerz.activity.RootActivity;
import com.acv.cheerz.object.Artist;
import com.acv.cheerz.service.CheerzServiceCallBack;
import com.acv.cheerz.util.CheerzUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailArtistFragment extends Fragment implements OnClickListener {

	private int artistId;
	private ImageButton btnBack, btnHome;
	private TextView txtTitle;
	private TextView txtName, txtBirthday, txtHomeTown, txtNickName,
			txtFavorite, txtCharactistic, txtFacebook;
	private List<Artist> arrayData = new ArrayList<Artist>();
	private ImageView imgAvatar;
	DisplayImageOptions options;

	public DetailArtistFragment() {

	}

	/**
	 * 
	 * @param id
	 *            : id of artist
	 */
	public DetailArtistFragment(int id) {
		this.artistId = id;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View detailView = inflater.inflate(R.layout.detail_artist_layout, null);

		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.artist_temp)
				.showImageForEmptyUri(R.drawable.artist_temp)
				.showImageOnFail(R.drawable.artist_temp).cacheInMemory(true)
				.cacheOnDisk(true).considerExifParams(true)
				.displayer(new RoundedBitmapDisplayer(20)).build();

		btnHome = (ImageButton) detailView.findViewById(R.id.header_btn_right);
		btnHome.setVisibility(View.INVISIBLE);

		btnBack = (ImageButton) detailView.findViewById(R.id.header_btn_left);
		btnBack.setImageResource(R.drawable.btn_back);
		btnBack.setOnClickListener(this);

		txtTitle = (TextView) detailView.findViewById(R.id.header_title);
		txtTitle.setText("ARTIST");
		txtTitle.setTextColor(getResources().getColor(R.color.White));

		txtName = (TextView) detailView.findViewById(R.id.detail_name_id);
		txtBirthday = (TextView) detailView
				.findViewById(R.id.detail_artist_birthday_id);
		txtHomeTown = (TextView) detailView
				.findViewById(R.id.detail_artist_hometown_id);
		txtNickName = (TextView) detailView
				.findViewById(R.id.detail_artist_nickname_id);
		txtFavorite = (TextView) detailView
				.findViewById(R.id.detail_artist_favcolor_id);
		txtCharactistic = (TextView) detailView
				.findViewById(R.id.detail_artist_char_id);
		txtFacebook = (TextView) detailView
				.findViewById(R.id.detail_artist_facebook_id);

		imgAvatar = (ImageView) detailView
				.findViewById(R.id.detail_icon_artist_id);

		addData();
		return detailView;
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.header_btn_left:
			getActivity().finish();
			break;

		default:
			break;
		}

	}

	

	private void addData() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("artist_id", String.valueOf(artistId));
		CheerzUtils.execute(getActivity(), RequestMethod.POST,
				CheerzUtils.API.API_DETAIL_ARTIST, params,
				new CheerzServiceCallBack() {
					@Override
					public void onError(String message) {

						super.onError(message);
					}

					@Override
					public void onSucces(JSONObject jsonObject) {
						super.onSucces(jsonObject);
						try {
							JSONObject jsObj = jsonObject.getJSONObject("data");
							txtName.setText(jsObj.getString("artist_name"));
							txtBirthday.setText(jsObj.getString("artist_birthday"));
							txtNickName.setText(jsObj.getString("artist_nickname"));
							txtCharactistic.setText(jsObj.getString("characteristic"));
							txtFacebook.setText(jsObj.getString("artist_facebook"));
							txtFavorite.setText(jsObj.getString("favorite_color"));
							txtHomeTown.setText(jsObj.getString("home_town"));
							ImageLoader.getInstance().displayImage(jsObj.getString("artist_avatar"), imgAvatar, options);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}
				});
	}

}
