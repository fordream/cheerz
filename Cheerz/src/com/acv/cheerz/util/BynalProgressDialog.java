package com.acv.cheerz.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.acv.cheerz.R;

public class BynalProgressDialog extends ProgressDialog {

	public BynalProgressDialog(Context context) {
		super(context);
		setCancelable(false);
	}

	private int[] loadingRes = new int[] { R.string.loading_0, R.string.loading_1, R.string.loading_2, R.string.loading_3 };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loading);

		TextView loading = (TextView) findViewById(R.id.loading);
		loadding(0, loading);
	}

	private void loadding(final int index, final TextView loading) {

		loading.postDelayed(new Runnable() {

			@Override
			public void run() {
				if (isShowing()) {
					loading.setText(loadingRes[index]);

					if (index == loadingRes.length - 1) {
						loadding(0, loading);
					} else {
						loadding(index + 1, loading);
					}
				}
			}
		}, 300);
	}

}