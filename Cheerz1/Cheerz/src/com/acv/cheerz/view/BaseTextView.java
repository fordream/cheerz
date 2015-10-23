package com.acv.cheerz.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.acv.cheerz.CheerzApplication;

//com.acv.cheerz.view.BaseTextView
public class BaseTextView extends TextView {

	public BaseTextView(Context context) {
		super(context);
		// try {
		// setTypeface(((ACVbaseApplication) getContext()
		// .getApplicationContext()).getTypeface());
		// } catch (Exception exception) {
		//
		// } catch (Error error) {
		//
		// }
		init();
	}

	public BaseTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// try {
		// setTypeface(((ACVbaseApplication) getContext()
		// .getApplicationContext()).getTypeface());
		// } catch (Exception exception) {
		//
		// } catch (Error error) {
		//
		// }
		init();
	}

	public BaseTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// try {
		// setTypeface(((ACVbaseApplication) getContext()
		// .getApplicationContext()).getTypeface());
		// } catch (Exception exception) {
		//
		// } catch (Error error) {
		//
		// }
		init();
	}

	private void init() {
		try {
			setTypeface(((CheerzApplication) getContext().getApplicationContext()).getTypeface());
			
		} catch (Exception exception) {

		} catch (Error error) {

		}
	}
}