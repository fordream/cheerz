package com.acv.cheerz.fragment;

import com.acv.cheerz.R;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FavoriteFragment extends Fragment{
	private String TAG = "RankingFragment";
	public FavoriteFragment(){
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View rankingView = inflater.inflate(R.layout.ranking_layout, container, false);
		Log.i(TAG, "onCreateView");
		return rankingView;
	}
}
