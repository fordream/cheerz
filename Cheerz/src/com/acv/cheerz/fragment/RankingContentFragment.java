package com.acv.cheerz.fragment;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import z.lib.base.callback.ExeCallBack;
import z.lib.base.callback.ResClientCallBack;
import z.lib.base.callback.RestClient;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.acv.cheerz.R;
import com.acv.cheerz.activity.RootActivity;
import com.acv.cheerz.object.Artist;
import com.acv.cheerz.util.CheerzUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class RankingContentFragment extends Fragment implements OnClickListener{

	private LinearLayout ll_listview;
	private List<Artist> arrayData = new ArrayList<Artist>();;
	String TAG = "RankingContentFragment";
	private ImageView imgArtTop1,imgArtTop2,imgArtTop3,imgArtTop4;
	View rankingContentView;
	public static int filter = 1;
				// filter = 1: yesterday
				// filter = 2: today
				// filter = 3: last month
				// filter = 4: this month
	DisplayImageOptions options;
	
	/**
	 * contructer
	 * @param filterType
	 */
	public RankingContentFragment(int filterType) {
		//updateData();
		filter = filterType;
		options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.artist_temp)
		.showImageForEmptyUri(R.drawable.artist_temp)
		.showImageOnFail(R.drawable.artist_temp)
		.cacheInMemory(true)
		.cacheOnDisk(true)
		.considerExifParams(true)
		.displayer(new RoundedBitmapDisplayer(20))
		.build();
		
		
	}
	
	public int getCurrentPage(){
		return filter;
	}
	
	public RankingContentFragment() {
		//updateData();
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		updateData(filter);
		rankingContentView = inflater.inflate(R.layout.ranking_content_layout, null);
		ll_listview = (LinearLayout)rankingContentView.findViewById(R.id.rk_listview_id);
		// add listenner
		// top4
		rankingContentView.findViewById(R.id.rk_icon_artist_1_id).setOnClickListener(this);
		rankingContentView.findViewById(R.id.rk_icon_artist_2_id).setOnClickListener(this);
		rankingContentView.findViewById(R.id.rk_icon_artist_3_id).setOnClickListener(this);
		rankingContentView.findViewById(R.id.rk_icon_artist_4_id).setOnClickListener(this);
		
		rankingContentView.findViewById(R.id.rk_row1_id).setOnClickListener(this);
		rankingContentView.findViewById(R.id.rk_row2_id).setOnClickListener(this);
		rankingContentView.findViewById(R.id.rk_row3_id).setOnClickListener(this);
		rankingContentView.findViewById(R.id.rk_row4_id).setOnClickListener(this);
				
		
		return rankingContentView;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		
		super.onActivityCreated(savedInstanceState);
	}
	
	
	private void updateData(int typeRanking){
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
					Log.i(TAG, "====> json Data = " +jsonObj.toString());
					JSONArray jsonArray = jsonObj.getJSONArray("data");
					arrayData.clear();
					
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
						addRowData();
					}
					
				} catch (Exception e) {
					// TODO: handle exception
				}
				super.onCallBack(object);
			}
			
		};
		// test with type_ranking = 3 : hardcode
		clientCallBack.addParam("type_ranking", "3");
		//clientCallBack.addParam("token", CheerzUtils.getToken(getActivity()));
		apiCallBack.executeAsynCallBack(clientCallBack);
		
	}
	
	private void addRowData(){
		for (int j = 0; j < arrayData.size(); j++) {
			if (j == 0) {
				imgArtTop1 = (ImageView)rankingContentView.findViewById(R.id.rk_icon_artist_1_id);
				ImageLoader.getInstance().displayImage(arrayData.get(j).getUrlAvatar(), imgArtTop1, options);
				((TextView)rankingContentView.findViewById(R.id.rk_txtName1_id)).setText(arrayData.get(j).getName());
				((TextView)rankingContentView.findViewById(R.id.rk_txtcountvote_id))
				.setText(String.valueOf(arrayData.get(j).getTotalVote()));
				// add icon fan
				ImageView imgF1 = (ImageView)rankingContentView.findViewById(R.id.rk_iconfan11_id);
				ImageView imgF2 = (ImageView)rankingContentView.findViewById(R.id.rk_iconfan12_id);
				ImageView imgF3 = (ImageView)rankingContentView.findViewById(R.id.rk_iconfan13_id);
				ImageLoader.getInstance().displayImage(arrayData.get(j).getUrlIconFan1(), imgF1, options);
				ImageLoader.getInstance().displayImage(arrayData.get(j).getUrlIconFan2(), imgF2, options);
				ImageLoader.getInstance().displayImage(arrayData.get(j).getUrlIconFan3(), imgF3, options);
			}else if (j == 1) {
				imgArtTop2 = (ImageView)rankingContentView.findViewById(R.id.rk_icon_artist_2_id);
				ImageLoader.getInstance().displayImage(arrayData.get(j).getUrlAvatar(), imgArtTop2, options);
				((TextView)rankingContentView.findViewById(R.id.rk_txtName2_id)).setText(arrayData.get(j).getName());
				((TextView)rankingContentView.findViewById(R.id.rk_txtcountvote2_id))
				.setText(String.valueOf(arrayData.get(j).getTotalVote()));
				// add icon fan
				ImageView imgF1 = (ImageView)rankingContentView.findViewById(R.id.rk_iconfan21_id);
				ImageView imgF2 = (ImageView)rankingContentView.findViewById(R.id.rk_iconfan22_id);
				ImageView imgF3 = (ImageView)rankingContentView.findViewById(R.id.rk_iconfan23_id);
				ImageLoader.getInstance().displayImage(arrayData.get(j).getUrlIconFan1(), imgF1, options);
				ImageLoader.getInstance().displayImage(arrayData.get(j).getUrlIconFan2(), imgF2, options);
				ImageLoader.getInstance().displayImage(arrayData.get(j).getUrlIconFan3(), imgF3, options);
			}else if (j == 2) {
				imgArtTop3 = (ImageView)rankingContentView.findViewById(R.id.rk_icon_artist_3_id);
				ImageLoader.getInstance().displayImage(arrayData.get(j).getUrlAvatar(), imgArtTop3, options);
				((TextView)rankingContentView.findViewById(R.id.rk_txtName3_id)).setText(arrayData.get(j).getName());
				((TextView)rankingContentView.findViewById(R.id.rk_txtcountvote3_id))
				.setText(String.valueOf(arrayData.get(j).getTotalVote()));
				// add icon fan
				ImageView imgF2 = (ImageView)rankingContentView.findViewById(R.id.rk_iconfan32_id);
				ImageView imgF1 = (ImageView)rankingContentView.findViewById(R.id.rk_iconfan31_id);
				ImageView imgF3 = (ImageView)rankingContentView.findViewById(R.id.rk_iconfan33_id);
				 
				ImageLoader.getInstance().displayImage(arrayData.get(j).getUrlIconFan1(), imgF1, options);
				ImageLoader.getInstance().displayImage(arrayData.get(j).getUrlIconFan2(), imgF2, options);
				ImageLoader.getInstance().displayImage(arrayData.get(j).getUrlIconFan3(), imgF3, options);
			}else if(j == 3){
				imgArtTop4 = (ImageView)rankingContentView.findViewById(R.id.rk_icon_artist_4_id);
				ImageLoader.getInstance().displayImage(arrayData.get(j).getUrlAvatar(), imgArtTop4, options);
				((TextView)rankingContentView.findViewById(R.id.rk_txtName4_id)).setText(arrayData.get(j).getName());
				((TextView)rankingContentView.findViewById(R.id.rk_txtcountvote4_id))
				.setText(String.valueOf(arrayData.get(j).getTotalVote()));
				// add icon fan
				
				ImageView imgF1 = (ImageView)rankingContentView.findViewById(R.id.rk_iconfan41_id);
				ImageView imgF2 = (ImageView)rankingContentView.findViewById(R.id.rk_iconfan42_id);
				ImageView imgF3 = (ImageView)rankingContentView.findViewById(R.id.rk_iconfan43_id);
				ImageLoader.getInstance().displayImage(arrayData.get(j).getUrlIconFan1(), imgF1, options);
				ImageLoader.getInstance().displayImage(arrayData.get(j).getUrlIconFan2(), imgF2, options);
				ImageLoader.getInstance().displayImage(arrayData.get(j).getUrlIconFan3(), imgF3, options);
				
			}else{
				LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE);
				View rowItemView = inflater.inflate(R.layout.row_ranking, null);
				TextView txtArtistName = (TextView)rowItemView.findViewById(R.id.row_artist_name_id);
				txtArtistName.setText(arrayData.get(j).getName());
				((TextView)rowItemView.findViewById(R.id.row_ranking_id)).setText(String.valueOf(j + 1));
				
				ImageView imgIcon = (ImageView)rowItemView.findViewById(R.id.row_rk_icon_id);
				new DownloadImageTask(imgIcon, 1).execute(arrayData.get(j).getUrlAvatar());
				
				((TextView)rowItemView.findViewById(R.id.row_artist_name_id))
				.setText(String.valueOf(arrayData.get(j).getName()));
				
				((TextView)rowItemView.findViewById(R.id.row_vote_id))
				.setText(String.valueOf(arrayData.get(j).getTotalVote()));
				
				ImageView imgF1 = (ImageView)rowItemView.findViewById(R.id.row_fan_top1_id);
				ImageLoader.getInstance().displayImage(arrayData.get(j).getUrlIconFan1(), imgF1, options);
				
				ImageView imgF2 = (ImageView)rowItemView.findViewById(R.id.row_fan_top2_id);
				ImageLoader.getInstance().displayImage(arrayData.get(j).getUrlIconFan2(), imgF2, options);
				
				ImageView imgF3 = (ImageView)rowItemView.findViewById(R.id.row_fan_top3_id);
				ImageLoader.getInstance().displayImage(arrayData.get(j).getUrlIconFan3(), imgF3, options);
				
				ll_listview.addView(rowItemView);
			
				// add listenner for row-item
				//rowItemView.findViewById(R.id.row_artist_ranking_id).setOnClickListener(this);
				//rowItemView.findViewById(R.id.row_fanranking_id).setOnClickListener(this);
				final int rowId = arrayData.get(j).getId();
				rowItemView.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						Log.i(TAG, "rowId = "+rowId);
						gotoPageZ(R.string.detail_artist,rowId);
					}
				});
				
				RelativeLayout rlRowFanRanking = (RelativeLayout)rowItemView.findViewById(R.id.row_fanranking_id);
				rlRowFanRanking.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						Log.i(TAG, "rowId-fanranking = "+rowId);
						gotoPageZ(R.string.fan_ranking,rowId);
					}
				});
			}
		}
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
						Log.i(TAG, "====>json = " + jsonObj.toString());
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
			clientCallBack.addParam("type_ranking", "3");
			clientCallBack.addParam("token", CheerzUtils.getToken(getActivity()));
			apiCallBack.executeAsynCallBack(clientCallBack);
							
			return null;
		}
		
		@Override
		protected void onPostExecute(List<Artist> result) {
			for (int i = 0; i < arrayData.size(); i++) {
				Log.i(TAG, "add data : "+ i);
//				LayoutInflater inflater = null;
//				inflater = (LayoutInflater)getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE);
//				View rowItemView = inflater.inflate(R.layout.row_ranking, null);
//				
//				TextView txtArtistName = (TextView)rowItemView.findViewById(R.id.row_artist_name_id);
//				txtArtistName.setText(arrayData.get(i).getName());
//				
//				ll_listview.addView(rowItemView);
//				ll_listview.invalidate();
			}
			super.onPostExecute(result);
		}
		
	}
	
	static class RowItem{
		private ImageView imgArtIcon;
		private ImageView imgFan1Icon;
		private ImageView imgFan2Icon;
		private ImageView imgFan3Icon;
		private ImageView imgLikeIcon;
		private TextView txtArtistName;
		private TextView txtArtistVote;
		private TextView txtArtistNumber; 
	}

	private void gotoPageZ(int setting,int id) {
		Intent intent = new Intent(getActivity(), RootActivity.class);
		intent.putExtra("type", getString(setting));
		intent.putExtra("id", id);
		startActivity(intent);
	}
	
	public void onClick(View v) {
		int key = v.getId();
		switch (key) {
		case R.id.rk_icon_artist_1_id:
			Log.i(TAG, "1");
			gotoPageZ(R.string.detail_artist,1);
			break;
		case R.id.rk_icon_artist_2_id:
			Log.i(TAG, "2");
			gotoPageZ(R.string.detail_artist,2);
			break;
		case R.id.rk_icon_artist_3_id:
			Log.i(TAG, "3");
			gotoPageZ(R.string.detail_artist,3);
			break;
		case R.id.rk_icon_artist_4_id:
			Log.i(TAG, "4");
			gotoPageZ(R.string.detail_artist,4);
			break;
		case R.id.row_artist_ranking_id:
			//Log.i(TAG, "5");
			break;
		case R.id.row_fanranking_id:
			//Log.i(TAG, "6");
			break;
		case R.id.rk_row1_id:
			Log.i(TAG, "ranking top1");
			gotoPageZ(R.string.fan_ranking,arrayData.get(0).getId());
			break;
		case R.id.rk_row2_id:
			Log.i(TAG, "ranking top2");
			gotoPageZ(R.string.fan_ranking,arrayData.get(1).getId());
			break;
		case R.id.rk_row3_id:
			Log.i(TAG, "ranking top3");
			gotoPageZ(R.string.fan_ranking,arrayData.get(2).getId());
			break;
		case R.id.rk_row4_id:
			Log.i(TAG, "ranking top4");
			gotoPageZ(R.string.fan_ranking,arrayData.get(3).getId());
			break;
		default:
			break;
		}
		
	}
	
	private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
	    ImageView bmImage;
	    int typeResource = 0;
	    /**
	     * 
	     * @param bmImage : ImageView
	     * @param type : 0 => setBackground,
	     * 				 1 => setResource	
	     * 
	     */
	    public DownloadImageTask(ImageView bmImage,int type) {
	        this.bmImage = bmImage;
	        typeResource = type;
	    }

	    protected Bitmap doInBackground(String... urls) {
	        String urldisplay = urls[0];
	        Bitmap mIcon11 = null;
	        try {
	            InputStream in = new java.net.URL(urldisplay).openStream();
	            mIcon11 = BitmapFactory.decodeStream(in);
	        } catch (Exception e) {
	            Log.e("Error", e.getMessage());
	            e.printStackTrace();
	        }
	        return mIcon11;
	    }

	    @SuppressLint("NewApi") protected void onPostExecute(Bitmap result) {
//	        bmImage.setImageBitmap(result);
	    	if (typeResource == 0) {
	    		Drawable dr = new BitmapDrawable(getActivity().getResources(), result);
		        bmImage.setBackground(dr);	
			}else{
				bmImage.setImageBitmap(result);
			}
			
	    }
	}
	
}
