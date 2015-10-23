package com.acv.cheerz.customviews.charting;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Html;
import android.util.AttributeSet;

import com.acv.cheerz.CheerzApplication;
import com.acv.cheerz.R;
import com.acv.cheerz.util.CheerzUtils;

//com.acv.cheerz.customviews.charting.MTextView
public class MTextView extends android.widget.TextView {
	public MTextView(Context context, AttributeSet attrs) {
		super(context, attrs);

		TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.MTextView, 0, 0);
		int txtType = -1;
		String txtString = null;
		try {
			txtType = a.getInteger(R.styleable.MTextView_txtFont, -1);
			txtString = a.getString(R.styleable.MTextView_txtString);
		} finally {
			a.recycle();
		}

		if (txtType > -1) {
			try {
				((CheerzApplication) getContext().getApplicationContext()).setFonts(txtType, this);
			} catch (Exception exception) {

			}
		}
		try {
			if (!CheerzUtils.isBlank(txtString)) {
				String str = String.format("<u>%s</u>", txtString);
				setText(Html.fromHtml(str));
			}
		} catch (Exception exception) {

		}
	}
	// int txtType;

}