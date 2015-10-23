package com.acv.cheerz.fragment;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.acv.cheerz.R;
import com.acv.cheerz.activity.RootActivity;

public class RankingFragment extends Fragment implements OnClickListener{
	private String TAG = "RankingFragment";
	private RankingAdapter adapter;
	private ViewPager viewPaper;
	private TextView tlYesterday,tlToday,tlThisMonth,tlLastMoth;
	private ImageButton btnBack,btnHome;
	private TextView txtTitle;
	private ImageView chkPointYesterday,chkPointToday,chkPointLastMonth,chkPointThismonth;
	public int currentPage = 0;
	public int WIDTH_SCREEN,HEIGHT_SCREEN;
	public RankingFragment(){
		
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View rankingView = inflater.inflate(R.layout.ranking_layout, container, false);
		viewPaper = (ViewPager)rankingView.findViewById(R.id.ranking_viewpager_id);
		adapter = new RankingAdapter(getFragmentManager());
		viewPaper.setAdapter(adapter);
		getWidthHeightScreen();
		Log.i(TAG, "currentPage = " + RankingContentFragment.filter);
		// init title
		tlYesterday = (TextView)rankingView.findViewById(R.id.ranking_title_yesterday_id);
		tlYesterday.setSelected(true);
		
		tlToday = (TextView)rankingView.findViewById(R.id.ranking_title_today_id);
		tlToday.setSelected(true);
		
		tlThisMonth = (TextView)rankingView.findViewById(R.id.ranking_title_thismonth_id);
		tlThisMonth.setSelected(true);
		
		tlLastMoth = (TextView)rankingView.findViewById(R.id.ranking_title_lastmonth_id);
		tlLastMoth.setSelected(true);
		
		btnHome = (ImageButton) rankingView.findViewById(R.id.header_btn_right);
		btnHome.setVisibility(View.INVISIBLE);

		btnBack = (ImageButton) rankingView.findViewById(R.id.header_btn_left);
		btnBack.setImageResource(R.drawable.btn_back);
		btnBack.setOnClickListener(this);

		txtTitle = (TextView) rankingView.findViewById(R.id.header_title);
		txtTitle.setText("RANKING");
		txtTitle.setTextColor(getResources().getColor(R.color.White));
		
		chkPointYesterday = (ImageView)rankingView.findViewById(R.id.ranking_imgprogress_1);
		chkPointToday = (ImageView)rankingView.findViewById(R.id.ranking_imgprogress_2);
		chkPointLastMonth = (ImageView)rankingView.findViewById(R.id.ranking_imgprogress_3);
		chkPointThismonth = (ImageView)rankingView.findViewById(R.id.ranking_imgprogress_4);
		updateTitle(currentPage);
		Log.i(TAG, "onCreateView");
		
		// handler change page
		
		viewPaper.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				updateTitle(arg0);
				Log.i(TAG, "==> onPageSelected : " + arg0);
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
		return rankingView;
	}

	private void gotoPageZ(int setting) {
		Intent intent = new Intent(getActivity(), RootActivity.class);
		intent.putExtra("type", getString(setting));
		startActivity(intent);
	}
	
	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.header_btn_left:
			getActivity().finish();
			break;
		case R.id.header_btn_right:
			getActivity().finish();
		default:
			break;
		}
		
	}
	
	/**
	 * 
	 * @param titleId
	 * id = 0 : yesterday
	 * id = 1 : today
	 * id = 2 : last month
	 * id = 3 : this month
	 */
	private void updateTitle(int titleId){
		if (WIDTH_SCREEN == 720) {
			tlYesterday.setTextSize(getActivity().getResources().getDimension(R.dimen.text_size_screen720));
			tlToday.setTextSize(getActivity().getResources().getDimension(R.dimen.text_size_screen720));
			tlLastMoth.setTextSize(getActivity().getResources().getDimension(R.dimen.text_size_screen720));
			tlThisMonth.setTextSize(getActivity().getResources().getDimension(R.dimen.text_size_screen720));
		}
		
		if (titleId == 0) {
			tlYesterday.setTextColor(getResources().getColor(R.color.bb328a));
			tlToday.setTextColor(getResources().getColor(R.color.colorhint_title));
			tlLastMoth.setTextColor(getResources().getColor(R.color.colorhint_title));
			tlThisMonth.setTextColor(getResources().getColor(R.color.colorhint_title));
			chkPointYesterday.setVisibility(View.VISIBLE);
			chkPointToday.setVisibility(View.INVISIBLE);
			chkPointLastMonth.setVisibility(View.INVISIBLE);
			chkPointThismonth.setVisibility(View.INVISIBLE);
		}else if (titleId == 1) {
			tlYesterday.setTextColor(getResources().getColor(R.color.colorhint_title));
			tlToday.setTextColor(getResources().getColor(R.color.bb328a));
			tlLastMoth.setTextColor(getResources().getColor(R.color.colorhint_title));
			tlThisMonth.setTextColor(getResources().getColor(R.color.colorhint_title));
			chkPointYesterday.setVisibility(View.INVISIBLE);
			chkPointToday.setVisibility(View.VISIBLE);
			chkPointLastMonth.setVisibility(View.INVISIBLE);
			chkPointThismonth.setVisibility(View.INVISIBLE);
		}else if (titleId == 2){
			tlYesterday.setTextColor(getResources().getColor(R.color.colorhint_title));
			tlToday.setTextColor(getResources().getColor(R.color.colorhint_title));
			tlLastMoth.setTextColor(getResources().getColor(R.color.bb328a));
			tlThisMonth.setTextColor(getResources().getColor(R.color.colorhint_title));
			chkPointYesterday.setVisibility(View.INVISIBLE);
			chkPointToday.setVisibility(View.INVISIBLE);
			chkPointLastMonth.setVisibility(View.VISIBLE);
			chkPointThismonth.setVisibility(View.INVISIBLE);
		}else {
			tlYesterday.setTextColor(getResources().getColor(R.color.colorhint_title));
			tlToday.setTextColor(getResources().getColor(R.color.colorhint_title));
			tlLastMoth.setTextColor(getResources().getColor(R.color.colorhint_title));
			tlThisMonth.setTextColor(getResources().getColor(R.color.bb328a));
			chkPointYesterday.setVisibility(View.INVISIBLE);
			chkPointToday.setVisibility(View.INVISIBLE);
			chkPointLastMonth.setVisibility(View.INVISIBLE);
			chkPointThismonth.setVisibility(View.VISIBLE);
		}
	}
	
	private void getWidthHeightScreen(){
		Display display = getActivity().getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		WIDTH_SCREEN = size.x;
		HEIGHT_SCREEN = size.y;
	}
}
