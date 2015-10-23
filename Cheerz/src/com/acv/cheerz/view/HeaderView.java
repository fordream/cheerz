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
public class HeaderView extends LinearLayout {
	private TextView text;
	private ImageButton left, right;

	public HeaderView(Context context) {
		super(context);
		init();
	}

	public HeaderView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}


	private void init() {
		((LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.header, this);
		try {
			text = (TextView) findViewById(R.id.header_title);
			left = (ImageButton) findViewById(R.id.header_btn_left);
			right = (ImageButton) findViewById(R.id.header_btn_right);

			left.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (iOnClickHeader != null) {
						iOnClickHeader.onClickLeft();
					}
				}
			});

			right.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (iOnClickHeader != null) {
						iOnClickHeader.onClickRight();
					}
				}
			});
		} catch (Exception exception) {

		}
	}

	public void visibivityLeft(boolean isVisivility, int resImg) {
		left.setVisibility(isVisivility ? VISIBLE : GONE);
		left.setImageResource(resImg);
	}

	public void visibivityRight(boolean isVisivility, int resImg) {
		right.setVisibility(isVisivility ? VISIBLE : GONE);
		right.setImageResource(resImg);
	}

	public void setTextHeader(String str) {
		text.setText(str);
	}

	public void setTextHeader(int str) {
		text.setText(str);
	}

	private IOnClickHeader iOnClickHeader;

	public void setiOnClickHeader(IOnClickHeader iOnClickHeader) {
		this.iOnClickHeader = iOnClickHeader;
	}

	public interface IOnClickHeader {
		public void onClickLeft();

		public void onClickRight();
	}
}