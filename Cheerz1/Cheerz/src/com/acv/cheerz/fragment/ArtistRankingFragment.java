package com.acv.cheerz.fragment;

import com.acv.cheerz.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class ArtistRankingFragment extends Fragment{

	public ArtistRankingFragment() {
	
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rankingView = inflater.inflate(R.layout.ranking_layout, null);
		return rankingView;
	}
	
}
