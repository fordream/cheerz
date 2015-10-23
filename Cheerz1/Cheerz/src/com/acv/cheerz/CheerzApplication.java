package com.acv.cheerz;

import java.util.Map;

import z.lib.base.callback.RestClient.RequestMethod;
import android.app.Application;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.IBinder;
import android.util.Log;

import com.acv.cheerz.service.CheerzBin;
import com.acv.cheerz.service.CheerzService;
import com.acv.cheerz.service.CheerzServiceCallBack;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.SaveCallback;

public class CheerzApplication extends Application {

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate() {
		super.onCreate();
		String Parse_app_key = this.getResources().getString(R.string.parse_app_key);
		String Parse_client_key = this.getResources().getString(R.string.parse_client_key);
		Parse.initialize(this, Parse_app_key, Parse_client_key);
		ParseInstallation.getCurrentInstallation().saveInBackground(new SaveCallback() {
			@Override
			public void done(ParseException e) {
				if (e == null) {
					Log.e("regis_parse", "done");
				} else {
					e.printStackTrace();
					Log.e("regis_parse", "err:" + e.getMessage());
				}
			}
		});

	}

	public void CallAPI() {

	}

	private Typeface typeface, typefaceb;

	public Typeface getTypeface() {
		if (typeface == null) {
			AssetManager assertManager = getAssets();
			typeface = Typeface.createFromAsset(assertManager, "meiryo.ttc");
		}
		return typeface;
	}

	public Typeface getTypefaceBold() {
		if (typefaceb == null) {
			AssetManager assertManager = getAssets();
			typefaceb = Typeface.createFromAsset(assertManager, "meiryob.ttc");
		}
		return typefaceb;
	}

	/**
	 * service init
	 */

	private CheerzService cheerzService;

	public CheerzService getCheerzService() {
		return cheerzService;
	}

	public void initService(final CheerzServiceCallBack back) {
		if (cheerzService != null) {
			back.onInitSuccess();
			return;
		}
		Intent intent = new Intent(CheerzService.ACTION);
		bindService(intent, new ServiceConnection() {

			@Override
			public void onServiceDisconnected(ComponentName name) {
				cheerzService = null;
				back.onInitFail();
			}

			@Override
			public void onServiceConnected(ComponentName name, IBinder service) {
				cheerzService = ((CheerzBin) service).getCheerzService();
				back.onInitSuccess();
			}
		}, BIND_AUTO_CREATE);
	}

	public void execute(final RequestMethod method, final String api, final Map<String, String> params, final CheerzServiceCallBack cheerzServiceCallBack) {
		if (cheerzService != null) {
			cheerzService.execute(method, api, params, cheerzServiceCallBack);
		} else {
			Intent intent = new Intent(CheerzService.ACTION);
			bindService(intent, new ServiceConnection() {

				@Override
				public void onServiceDisconnected(ComponentName name) {
					cheerzService = null;
				}

				@Override
				public void onServiceConnected(ComponentName name, IBinder service) {
					cheerzService = ((CheerzBin) service).getCheerzService();
					cheerzService.execute(method, api, params, cheerzServiceCallBack);
				}
			}, BIND_AUTO_CREATE);
		}
	}

}