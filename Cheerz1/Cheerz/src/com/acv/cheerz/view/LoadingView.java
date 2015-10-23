package com.acv.cheerz.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.acv.cheerz.R;

//com.acv.cheerz.view.HeaderView
public class LoadingView extends LinearLayout {

	public LoadingView(Context context) {
		super(context);
		init();
	}

	public LoadingView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {
		((LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.loading, this);
		findViewById(R.id.loading_main).setVisibility(View.VISIBLE);
		findViewById(R.id.loading_main).setOnClickListener(null);
	}
}