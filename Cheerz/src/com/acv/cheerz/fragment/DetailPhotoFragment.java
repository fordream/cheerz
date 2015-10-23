package com.acv.cheerz.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class DetailPhotoFragment extends Fragment {

	private int photoId;
	
	public DetailPhotoFragment() {
	
	}
	
	public DetailPhotoFragment(int photoId){
		this.photoId = photoId;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		return super.onCreateView(inflater, container, savedInstanceState);
	}
}
