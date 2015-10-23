package com.acv.cheerz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.acv.cheerz.CheerzApplication.IConfigApplication;

public class SplashActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		((CheerzApplication) getApplication()).init(application);
	}

	private CheerzApplication.IConfigApplication application = new IConfigApplication() {

		@Override
		public void onSuccess() {
			if (!isFinishing()) {
				startActivity(new Intent(SplashActivity.this, OverMainActivity.class));
				finish();
			}
		}

		@Override
		public void onStart() {

		}
	};

}
