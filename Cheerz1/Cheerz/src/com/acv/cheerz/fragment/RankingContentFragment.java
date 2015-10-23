package com.acv.cheerz.fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import org.json.JSONArray;
import org.json.JSONObject;

import z.lib.base.callback.APICaller;
import z.lib.base.callback.ExeCallBack;
import z.lib.base.callback.ResClientCallBack;
import z.lib.base.callback.RestClient;

import com.acv.cheerz.R;
import com.acv.cheerz.object.Artist;
import com.acv.cheerz.util.CheerzUtils;

import android.app.Service;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class RankingContentFragment extends Fragment{

	private LinearLayout ll_listview;
	private List<Artist> arrayData = new ArrayList<Artist>();;
	String TAG = "RankingContentFragment";
	public RankingContentFragment() {
		updateData();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rankingContentView = inflater.inflate(R.layout.ranking_content_layout, null);
		ll_listview = (LinearLayout)rankingContentView.findViewById(R.id.rk_listview_id);
		
		//new GetRankingData().execute("4");
		//updateData();
		return rankingContentView;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		for (int i = 0; i < arrayData.size(); i++) {
			Log.i(TAG, "add data : "+ i);
			LayoutInflater inflater = null;
			inflater = (LayoutInflater)getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE);
			View rowItemView = inflater.inflate(R.layout.row_ranking, null);
			
			TextView txtArtistName = (TextView)rowItemView.findViewById(R.id.row_artist_name_id);
			txtArtistName.setText(arrayData.get(i).getName());
			
			ll_listview.addView(rowItemView);
			ll_listview.invalidate();
		}
		super.onActivityCreated(savedInstanceState);
	}
	
	private void updateData(){
		ExeCallBack apiCallBack = new ExeCallBack();
		ResClientCallBack clientCallBack = new ResClientCallBack() {
			
			@Override
			public String getApiName() {
				
				return CheerzUtils.API.API_LIST_RANKING;
			}
			
			@Override
			public void onError(String errorMessage) {
				//Log.i(TAG, errorMessage);
				super.onError(errorMessage);
			}
			
			@Override
			public void onSuccess(String response) {
				//Log.i(TAG, response);
				super.onSuccess(response);
			}
			
			
			@Override
			public void onCallBack(Object object) {
				try {
					JSONObject jsonObj = new JSONObject(((RestClient) object).getResponse());
//					JSONObject jsonData = jsonObj.getJSONObject("data");
					JSONArray jsonArray = jsonObj.getJSONArray("data");
					
					
					for (int i = 0; i < jsonArray.length(); i++) {
						Artist newArtist = new Artist();
						JSONObject jsonObjI = jsonArray.getJSONObject(i);
						newArtist.setId(jsonObjI.getInt("artist_id"));
						newArtist.setName(jsonObjI.getString("artist_name"));
						newArtist.setUrlAvatar(jsonObjI.getString("artist_avatar"));
						newArtist.setTotalVote(jsonObjI.getInt("total_vote"));
						newArtist.setUrlIconFan1(jsonObjI.getString("avatar_fans_0"));
						newArtist.setUrlIconFan2(jsonObjI.getString("avatar_fans_1"));
						newArtist.setUrlIconFan3(jsonObjI.getString("avatar_fans_2"));
						arrayData.add(newArtist);
						Log.i(TAG, "+ " +jsonObjI.getInt("artist_id"));
						Log.i(TAG, "+ " +jsonObjI.getString("artist_name"));
					}
					
				} catch (Exception e) {
					// TODO: handle exception
				}
				super.onCallBack(object);
			}
		};
		clientCallBack.addParam("type_ranking", "4");
		clientCallBack.addParam("token", CheerzUtils.getToken(getActivity()));
		apiCallBack.executeAsynCallBack(clientCallBack);
		
		
	}
	
	class GetRankingData extends AsyncTask<String, String, List<Artist>>{

		

		@Override
		protected List<Artist> doInBackground(String... params) {
			ExeCallBack apiCallBack = new ExeCallBack();
			ResClientCallBack clientCallBack = new ResClientCallBack() {
				
				@Override
				public String getApiName() {
					
					return CheerzUtils.API.API_LIST_RANKING;
				}
				
				@Override
				public void onError(String errorMessage) {
					//Log.i(TAG, errorMessage);
					super.onError(errorMessage);
				}
				
				@Override
				public void onSuccess(String response) {
					//Log.i(TAG, response);
					super.onSuccess(response);
				}
				
				@Override
				public void onCallBack(Object object) {
					try {
						JSONObject jsonObj = new JSONObject(((RestClient) object).getResponse());
//						JSONObject jsonData = jsonObj.getJSONObject("data");
						JSONArray jsonArray = jsonObj.getJSONArray("data");
						
						
						for (int i = 0; i < jsonArray.length(); i++) {
							Artist newArtist = new Artist();
							JSONObject jsonObjI = jsonArray.getJSONObject(i);
							newArtist.setId(jsonObjI.getInt("artist_id"));
							newArtist.setName(jsonObjI.getString("artist_name"));
							newArtist.setUrlAvatar(jsonObjI.getString("artist_avatar"));
							newArtist.setTotalVote(jsonObjI.getInt("total_vote"));
							newArtist.setUrlIconFan1(jsonObjI.getString("avatar_fans_0"));
							newArtist.setUrlIconFan2(jsonObjI.getString("avatar_fans_1"));
							newArtist.setUrlIconFan3(jsonObjI.getString("avatar_fans_2"));
							arrayData.add(newArtist);
							Log.i(TAG, "+ " +jsonObjI.getInt("artist_id"));
							Log.i(TAG, "+ " +jsonObjI.getString("artist_name"));
						}
						
					} catch (Exception e) {
						// TODO: handle exception
					}
					super.onCallBack(object);
				}
			};
			clientCallBack.addParam("type_ranking", "4");
			clientCallBack.addParam("token", CheerzUtils.getToken(getActivity()));
			apiCallBack.executeAsynCallBack(clientCallBack);
			
			return null;
		}
		
		@Override
		protected void onPostExecute(List<Artist> result) {
			for (int i = 0; i < arrayData.size(); i++) {
				Log.i(TAG, "add data : "+ i);
				LayoutInflater inflater = null;
				inflater = (LayoutInflater)getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE);
				View rowItemView = inflater.inflate(R.layout.row_ranking, null);
				
				TextView txtArtistName = (TextView)rowItemView.findViewById(R.id.row_artist_name_id);
				txtArtistName.setText(arrayData.get(i).getName());
				
				ll_listview.addView(rowItemView);
				ll_listview.invalidate();
			}
			super.onPostExecute(result);
		}
		
	}
	
	
}
