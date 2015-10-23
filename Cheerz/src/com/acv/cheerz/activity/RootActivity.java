package com.acv.cheerz.activity;

import java.util.List;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Display;

import com.acv.cheerz.MainActivity;
import com.acv.cheerz.OverMainActivity;
import com.acv.cheerz.R;
import com.acv.cheerz.fragment.DetailArtistFragment;
import com.acv.cheerz.fragment.EarnPointFragment;
import com.acv.cheerz.fragment.FanRankingFragment;
import com.acv.cheerz.fragment.FavoriteFragment;
import com.acv.cheerz.fragment.ForgotPassword;
import com.acv.cheerz.fragment.HomeFragment;
import com.acv.cheerz.fragment.ImgFragment;
import com.acv.cheerz.fragment.LoginFragment;
import com.acv.cheerz.fragment.MyPageFragment;
import com.acv.cheerz.fragment.NotificationSettingFragment;
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
		int id = getIntent().getIntExtra("id", 1);
		if (type.equals("home")) {
			changeFragemt(R.id.root, new HomeFragment());
		} else if (type.equals("login")) {
			repalteFragemt(R.id.root, new LoginFragment(), false);
			// changeFragemt(R.id.root, new RankingFragment());
		} else if (type.equals("register")) {
			repalteFragemt(R.id.root, new RegisterFragment(), false);
		} else if (type.equals("search")) {
			Bundle extras = getIntent().getExtras();
			SearchFragment fragment = new SearchFragment();
			fragment.setArguments(extras);
			repalteFragemt(R.id.root, fragment, false);
		} else if (type.equals("img")) {
			changeFragemt(R.id.root, new ImgFragment());
		} else if (type.equals(getString(R.string.setting))) {
			repalteFragemt(R.id.root, new SettingFragment(), false);
		} else if (type.equals("edit_profile")) {
			changeFragemt(R.id.root, new RegisterFragment());
		} else if (type.equals(getString(R.string.earn_points))) {
			changeFragemt(R.id.root, new EarnPointFragment());
		} else if (type.equals(getString(R.string.ranking))) {
			changeFragemt(R.id.root, new RankingFragment());
		} else if (type.equals(getString(R.string.favorite))) {
			changeFragemt(R.id.root, new FavoriteFragment());
		} else if (type.equals(getString(R.string.fan_ranking))) {
			changeFragemt(R.id.root, new FanRankingFragment(id));
		} else if (type.equals(getString(R.string.detail_artist))) {
			changeFragemt(R.id.root, new DetailArtistFragment(id));
		} else if (type.equals(getString(R.string.activities_log))) {
			changeFragemt(R.id.root, new MyPageFragment());
		}

		getWidthHeightScreen();
	}

	public void changeFragemt(int res, Fragment fragment) {
		FragmentManager fragmentManager = getSupportFragmentManager();
		fragmentManager.beginTransaction().replace(res, fragment).commit();
	}

	public void repalteFragemt(int res, Fragment fragment, boolean haveAnimation) {
		FragmentManager fragmentManager = getSupportFragmentManager();
		android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction();

		if (haveAnimation) {
			transaction.setCustomAnimations(R.anim.scale, R.anim.scale, R.anim.scale_to_center, R.anim.scale_to_center);
		}
		transaction.add(res, fragment, "" + System.currentTimeMillis());
		transaction.addToBackStack(null);
		transaction.commit();
	}

	public void search(String str) {
		Intent intent = new Intent(this, RootActivity.class);
		intent.putExtra("type", "search");
		intent.putExtra("search", str);
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

	public void openNotificationSetting() {
		repalteFragemt(R.id.root, new NotificationSettingFragment(), true);
	}

	@Override
	public void onBackPressed() {
		FragmentManager fragmentManager = getSupportFragmentManager();
		int count = fragmentManager.getBackStackEntryCount();

		List<Fragment> fragments = fragmentManager.getFragments();

		if (fragments.get(fragments.size() - 1) instanceof HomeFragment) {
			if (((HomeFragment) fragments.get(fragments.size() - 1)).onBackPressed()) {
				return;
			}
		}
		if (count > 1) {
			fragmentManager.popBackStack();
		} else if (count == 1) {
			finish();
		} else {
			super.onBackPressed();
		}
	}

	private void getWidthHeightScreen() {
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int width = size.x;
		int height = size.y;
		Log.i(TAG, "Width-Screen = " + width);
		Log.i(TAG, "Height-Screen = " + height);
	}

	public void enableMenu(boolean b) {
		((OverMainActivity) getParent()).enableMenu(b);

	}

	public void gotoForgotPassword() {
		repalteFragemt(R.id.root, new ForgotPassword(), true);

	}

	public void finishCustom() {
		finish();
		overridePendingTransition(R.anim.a_nothing, R.anim.scale_to_center);
	}
}