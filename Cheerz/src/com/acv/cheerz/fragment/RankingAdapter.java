package com.acv.cheerz.fragment;

import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class RankingAdapter extends FragmentPagerAdapter{

	public static int NUMBER_PAGE = 4;
	public int PAGE_ID = 0;
	public RankingAdapter(FragmentManager fm) {
		super(fm);
		
	}

	@Override
	public Fragment getItem(int position) {
		
		switch (position) {
		case 0:
			PAGE_ID = 0;
			return new RankingContentFragment(1);
		case 1: 
			PAGE_ID = 1;
			return new RankingContentFragment(2);
		case 2: 
			PAGE_ID = 2;
			return new RankingContentFragment(3);
		case 3: 
			PAGE_ID = 3;
			return new RankingContentFragment(4);
		default:
			return null;
		}
		
	}

	@Override
	public int getCount() {
		return NUMBER_PAGE;
	}

	public int getCurrentPage(){
		return PAGE_ID;
	}
	
}
