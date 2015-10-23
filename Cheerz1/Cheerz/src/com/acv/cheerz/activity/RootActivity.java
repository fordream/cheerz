package com.acv.cheerz.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import com.acv.cheerz.MainActivity;
import com.acv.cheerz.OverMainActivity;
import com.acv.cheerz.R;
import com.acv.cheerz.fragment.AFragment;
import com.acv.cheerz.fragment.EarnPointFragment;
import com.acv.cheerz.fragment.HomeFragment;
import com.acv.cheerz.fragment.ImgFragment;
import com.acv.cheerz.fragment.LoginFragment;
import com.acv.cheerz.fragment.RankingFragment;
import com.acv.cheerz.fragment.RegisterFragment;
import com.acv.cheerz.fragment.SearchFragment;
import com.acv.cheerz.fragment.SettingFragment;

public class RootActivity extends FragmentActivity {
	private String TAG = "RootActivity";
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.root);

		String type = getIntent().getStringExtra("type");
		Log.i(TAG, "type = " + type);
		if (type.equals("A")) {
			changeFragemt(R.id.root, new AFragment());
			// show AF
		} else if (type.equals("home")) {
			changeFragemt(R.id.root, new HomeFragment());
		} else if (type.equals("login")) {
			changeFragemt(R.id.root, new LoginFragment());
		} else if (type.equals("register")) {
			changeFragemt(R.id.root, new RegisterFragment());
		} else if (type.equals("search")) {
			changeFragemt(R.id.root, new SearchFragment());
		} else if (type.equals("img")) {
			changeFragemt(R.id.root, new ImgFragment());
		} else if (type.equals(getString(R.string.setting))) {
			changeFragemt(R.id.root, new SettingFragment());
		}else if (type.equals("edit_profile")) {
			changeFragemt(R.id.root, new RegisterFragment());
		}else if(type.equals(getString(R.string.earn_points))){
			changeFragemt(R.id.root, new EarnPointFragment());
		}else if(type.equals(getString(R.string.ranking))){
			changeFragemt(R.id.root, new RankingFragment());
		}

	}

	public void changeFragemt(int res, Fragment fragment) {
		FragmentManager fragmentManager = getSupportFragmentManager();
		fragmentManager.beginTransaction().replace(res, fragment).commit();
	}

	public void search() {
		Intent intent = new Intent(this, RootActivity.class);
		intent.putExtra("type", "search");
		startActivity(intent);
	}

	public void callImageDetail(String id) {
		Intent intent = new Intent(this, RootActivity.class);
		intent.putExtra("type", "img");
		intent.putExtra("id", id);
		startActivity(intent);
	}

	public void openMenuLeft() {
		try {
			((MainActivity) getParent()).openMenuLeft();
		} catch (Exception exception) {

		}
		try {
			((OverMainActivity) getParent()).openMenuLeft();
		} catch (Exception exception) {

		}
	}
}