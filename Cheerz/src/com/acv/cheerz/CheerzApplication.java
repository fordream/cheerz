package com.acv.cheerz;

import java.util.HashMap;
import java.util.Map;

import z.lib.base.callback.RestClient.RequestMethod;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;
import android.widget.TextView;

import com.acv.cheerz.service.CheerzBin;
import com.acv.cheerz.service.CheerzService;
import com.acv.cheerz.service.CheerzServiceCallBack;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.SaveCallback;

public class CheerzApplication extends Application {

	public interface IConfigApplication {
		public void onStart();

		public void onSuccess();
	}

	public void init(final IConfigApplication configApplication) {
		configApplication.onStart();

		new AsyncTask<String, String, String>() {
			@Override
			protected String doInBackground(String... params) {

				for (String font : fonts) {
					if (!hashMap.containsKey(font)) {
						hashMap.put(font, Typeface.createFromAsset(getAssets(), font));
					}
				}

				return null;
			}

			@Override
			protected void onPostExecute(String result) {
				super.onPostExecute(result);
				configApplication.onSuccess();
			}
		}.execute("");
	}

	public HashMap<String, Typeface> hashMap = new HashMap<String, Typeface>();
	public static final String fonts[] = new String[] { 
			"KozGoPr6N-Bold.otf",//
			"KozGoPr6N-ExtraLight.otf",//
			"KozGoPr6N-Heavy.otf",//
//			"KozGoPr6N-Light.otf",//
			"kalinga.ttf",
			"KozGoPr6N-Medium.otf",//
			"KozGoPr6N-Regular.otf"//
	};

	public static void initImageLoader(Context context) {
		// This configuration tuning is custom. You can tune every option, you
		// may tune some of them,
		// or you can create default configuration by
		// ImageLoaderConfiguration.createDefault(this);
		// method.
	}

	@Override
	public void onCreate() {
		super.onCreate();

		initImageLoader(this);
		initImageLoaderZ(getApplicationContext());
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

	public static void initImageLoaderZ(Context context) {
		// This configuration tuning is custom. You can tune every option, you
		// may tune some of them,
		// or you can create default configuration by
		// ImageLoaderConfiguration.createDefault(this);
		// method.
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context).threadPriority(Thread.NORM_PRIORITY - 2).denyCacheImageMultipleSizesInMemory()
				.diskCacheFileNameGenerator(new Md5FileNameGenerator()).diskCacheSize(50 * 1024 * 1024) // 50
																										// Mb
				.tasksProcessingOrder(QueueProcessingType.LIFO).writeDebugLogs() // Remove
																					// for
																					// release
																					// app
				.build();
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);
	}

	public void setFonts(int txtType, TextView editText) {
		if (txtType <= fonts.length - 1) {
			editText.setTypeface(hashMap.get(fonts[txtType]));
		}
	}

}