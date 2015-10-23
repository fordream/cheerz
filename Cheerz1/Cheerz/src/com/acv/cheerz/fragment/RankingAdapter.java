package com.acv.cheerz.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class RankingAdapter extends FragmentPagerAdapter{

	public static int NUMBER_PAGE = 4;
	public RankingAdapter(FragmentManager fm) {
		super(fm);
		
	}

	@Override
	public Fragment getItem(int position) {
		
		switch (position) {
		case 0:
			return new RankingContentFragment();
		case 1: 
			return new RankingContentFragment();
		case 2: 
			return new RankingContentFragment();
		case 3: 
			return new RankingContentFragment();

		default:
			return null;
		}
		
	}

	@Override
	public int getCount() {
		return NUMBER_PAGE;
	}

	
}
