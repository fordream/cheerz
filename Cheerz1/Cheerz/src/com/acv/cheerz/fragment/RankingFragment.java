package com.acv.cheerz.fragment;

import com.acv.cheerz.R;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class RankingFragment extends Fragment{
	private String TAG = "RankingFragment";
	private RankingAdapter adapter;
	private ViewPager viewPaper;
	private TextView tlYesterday,tlToday,tlThisMonth,tlLastMoth;
	
	public RankingFragment(){
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View rankingView = inflater.inflate(R.layout.ranking_layout, container, false);
		viewPaper = (ViewPager)rankingView.findViewById(R.id.ranking_viewpager_id);
		adapter = new RankingAdapter(getFragmentManager());
		viewPaper.setAdapter(adapter);
		
		// init title
		tlYesterday = (TextView)rankingView.findViewById(R.id.ranking_title_yesterday_id);
		tlYesterday.setSelected(true);
		tlToday = (TextView)rankingView.findViewById(R.id.ranking_title_today_id);
		tlToday.setSelected(true);
		tlThisMonth = (TextView)rankingView.findViewById(R.id.ranking_title_thismonth_id);
		tlThisMonth.setSelected(true);
		tlLastMoth = (TextView)rankingView.findViewById(R.id.ranking_title_lastmonth_id);
		tlLastMoth.setSelected(true);
		
		Log.i(TAG, "onCreateView");
		return rankingView;
	}
}
