package com.acv.cheerz.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.acv.cheerz.CheerzApplication;

//com.acv.cheerz.view.BaseTextBoldView
public class BaseTextBoldView extends TextView {

	public BaseTextBoldView(Context context) {
		super(context);
		init();
		// try {
		// setTypeface(((ACVbaseApplication) getContext()
		// .getApplicationContext()).getTypefaceBold());
		// } catch (Exception exception) {
		//
		// } catch (Error error) {
		//
		// }
	}

	public BaseTextBoldView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// try {
		// setTypeface(((ACVbaseApplication) getContext()
		// .getApplicationContext()).getTypefaceBold());
		// } catch (Exception exception) {
		//
		// } catch (Error error) {
		//
		// }
		init();
	}

	public BaseTextBoldView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// try {
		// setTypeface(((ACVbaseApplication) getContext()
		// .getApplicationContext()).getTypefaceBold());
		// } catch (Exception exception) {
		//
		// } catch (Error error) {
		//
		// }
		init();
	}

	private void init() {
		try {
			setTypeface(((CheerzApplication) getContext().getApplicationContext()).getTypefaceBold());
		} catch (Exception exception) {

		} catch (Error error) {

		}
	}
}