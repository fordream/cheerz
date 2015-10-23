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
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.acv.cheerz.R;
import com.acv.cheerz.object.Artist;
import com.acv.cheerz.object.FanObject;
import com.acv.cheerz.service.CheerzServiceCallBack;
import com.acv.cheerz.util.CheerzUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class FanRankingFragment  extends Fragment implements OnClickListener{

	private String TAG = "FanRankingFragment";
	ListView listView;
	FanRankingAdapter fanRankingAdapter;
	private List<FanObject> arrayData = new ArrayList<FanObject>();
	DisplayImageOptions options;
	private ImageButton btnHome,btnBack;
	private TextView txtTitle;
	private int idArtist;
	
	public FanRankingFragment(int id) {
		options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.artist_temp)
		.showImageForEmptyUri(R.drawable.artist_temp)
		.showImageOnFail(R.drawable.artist_temp)
		.cacheInMemory(true)
		.cacheOnDisk(true)
		.considerExifParams(true)
		.displayer(new RoundedBitmapDisplayer(20))
		.build();
		this.idArtist = id;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		View fanRankingView = inflater.inflate(R.layout.fan_ranking_layout, null);
		listView = (ListView)fanRankingView.findViewById(R.id.fanranking_listview_id);
		btnHome = (ImageButton) fanRankingView.findViewById(R.id.header_btn_right);
		btnHome.setVisibility(View.INVISIBLE);
		
		btnBack = (ImageButton) fanRankingView.findViewById(R.id.header_btn_left);
		btnBack.setImageResource(R.drawable.btn_back);
		btnBack.setOnClickListener(this);
		txtTitle = (TextView) fanRankingView.findViewById(R.id.header_title);
		txtTitle.setText(getResources().getString(R.string.fan_ranking));
		txtTitle.setTextColor(getResources().getColor(R.color.White));
		
		//fanRankingAdapter = new FanRankingAdapter(getActivity(),arrayData);
		//listView.setAdapter(fanRankingAdapter);
		updateData(idArtist);
		return fanRankingView;
	}
	
	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.fanranking_listview_id:
			
			break;
		case R.id.header_btn_left:
			getActivity().finish();
			break;	
		default:
			break;
		}
	}

	private void updateData(int id){
		 Map<String, String> params = new HashMap<String, String>();
		 params.put("artist_id", String.valueOf(idArtist));
		
		CheerzUtils.execute(getActivity(), RequestMethod.POST, CheerzUtils.API.API_LIST_FAN_RANKING, params, new CheerzServiceCallBack(){
			@Override
			public void onStart() {
				super.onStart();
			}
			
			
			@Override
			public void onError(String message) {
				super.onError(message);
			}

			
			@Override
			public void onSucces(JSONObject jsonObj) {
				super.onSucces(jsonObj);
				
				try {
					JSONArray jsonArray = jsonObj.getJSONArray("data");
					arrayData.clear();
					for (int i = 0; i < jsonArray.length(); i++) {
						Log.i(TAG, "====> json Data parser");
						FanObject obj = new FanObject();
						JSONObject jsonObjI = jsonArray.getJSONObject(i);
						obj.setId(1);
						obj.setName(jsonObjI.getString("fan_name"));
						obj.setTotalVote(jsonObjI.getInt("total_vote"));
						obj.setUrlAvatar(jsonObjI.getString("fan_avatar"));
						arrayData.add(obj);
						
						Log.i(TAG, "arrayData size = " + arrayData.size());
					}
					fanRankingAdapter = new FanRankingAdapter(getActivity(),arrayData);
					listView.setAdapter(fanRankingAdapter);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		
	}
	
	
	/**
	 * fan ranking adapter
	 * @author dodanhhieu
	 *
	 */
	class FanRankingAdapter extends BaseAdapter{
		
		private Context context;
		private List<FanObject> Data;
		public FanRankingAdapter(Context ctx,List<FanObject> data) {
			this.context = ctx;
			this.Data = data;
			Log.i(TAG, "init adapter");
		}
		
		@Override
		public int getCount() {
			
			return arrayData.size();
		}

		@Override
		public Object getItem(int position) {
			
			return arrayData.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return arrayData.get(position).getId();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View rowItem = convertView;
			Log.i(TAG, "fan ranking adapter");
			HolderView viewHolder = null;
			if (rowItem == null) {
				LayoutInflater inflater = ((Activity)context).getLayoutInflater();
				rowItem = inflater.inflate(R.layout.row_fan_ranking, parent, false);
				viewHolder = new HolderView();
				viewHolder.txtRowId = (TextView)rowItem.findViewById(R.id.row_fr_id);
				viewHolder.imgAvatar = (ImageView)rowItem.findViewById(R.id.row_fr_iconfan_id);
				viewHolder.txtName = (TextView)rowItem.findViewById(R.id.row_fr_name_id);
				viewHolder.txtTotalVote = (TextView)rowItem.findViewById(R.id.row_fr_totalvote_id);
				viewHolder.iconTop = (ImageView)rowItem.findViewById(R.id.row_fr_icontop_id);
				rowItem.setTag(viewHolder);
			}else{
				viewHolder = (HolderView)rowItem.getTag();
			}
			
			// fill data to row
			FanObject fanObj = Data.get(position);
			viewHolder.txtRowId.setText(String.valueOf(position + 1));
			ImageLoader.getInstance().displayImage(fanObj.getUrlAvatar(), viewHolder.imgAvatar, options);
			viewHolder.txtName.setText(fanObj.getName());
			viewHolder.txtTotalVote.setText(String.valueOf(fanObj.getTotalVote()));
			if (position < 4) {
				viewHolder.iconTop.setVisibility(View.VISIBLE);
			}
			return rowItem;
		}
		
	}
	
	
	
	class HolderView{
		private TextView txtRowId;
		private ImageView imgAvatar;
		private TextView txtName;
		private TextView txtTotalVote;
		private ImageView iconTop;
	}
}
