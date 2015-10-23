package com.acv.cheerz.customviews.charting;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;

import com.acv.cheerz.CheerzApplication;
import com.acv.cheerz.R;

//com.acv.cheerz.customviews.charting.MEditText
public class MEditText extends android.widget.EditText {
	public MEditText(Context context, AttributeSet attrs) {
		super(context, attrs);

		TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.MTextView, 0, 0);
		int txtType = -1;

		try {
			txtType = a.getInteger(R.styleable.MTextView_txtFont, -1);
		} finally {
			a.recycle();
		}

		if (txtType > -1) {
			try {
				((CheerzApplication) getContext().getApplicationContext()).setFonts(txtType, this);
			} catch (Exception exception) {
			}
		}

	}
	// int txtType;

}