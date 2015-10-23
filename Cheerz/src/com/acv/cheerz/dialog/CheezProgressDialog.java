package com.acv.cheerz.dialog;

import com.acv.cheerz.R;

import android.content.Context;
import android.os.Bundle;

public class CheezProgressDialog extends android.app.ProgressDialog {

	public CheezProgressDialog(Context context) {
		super(context, android.R.style.Theme_Translucent_NoTitleBar);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loading);
	}
}
