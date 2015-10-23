package com.acv.cheerz.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;
import android.widget.ScrollView;

//com.acv.cheerz.view.CheerzScrollView
public class CheerzScrollView extends ScrollView {

	public CheerzScrollView(Context context) {
		super(context);
	}

	public CheerzScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public CheerzScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
	}
}