/*
 * Copyright 2013 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.acv.cheerz;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import z.lib.base.DataStore;
import z.lib.base.LogUtils;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParsePushBroadcastReceiver;
import com.parse.SaveCallback;

/**
 * Handling of GCM messages.
 */
public class GcmBroadcastReceiver extends ParsePushBroadcastReceiver {
	static final String TAG = "GCMDemo";

	@Override
	protected Notification getNotification(Context context, Intent intent) {
		Bundle extras = intent.getExtras();
		String jsonData = extras.getString("com.parse.Data");
		String title = "Cheerz";
		String message = "";
		if (jsonData != null) {
			try {
				JSONObject data = new JSONObject(jsonData);
				message = data.getString("alert");
			} catch (JSONException e) {
				Log.e(TAG, "Error parsing json data");
			}
		} else {
			Log.e(TAG, "cannot find notification data");
		}

		PendingIntent contentIntent = null;
		Intent notificationIntent = new Intent(context, MainActivity.class);
		contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
		Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
		// Vibrate for 300 milliseconds
		v.vibrate(300);
		NotificationCompat.Builder builder = new NotificationCompat.Builder(context).setSmallIcon(R.drawable.ic_launcher).setContentTitle(title).setContentText(message)
				.setContentIntent(contentIntent).setDefaults(Notification.DEFAULT_ALL).setAutoCancel(true).setStyle(new NotificationCompat.BigTextStyle().bigText(message));
		return builder.build();
	}

	public static void registerChannel(final Context context, final int type) {

		try {
			ParseInstallation installation = ParseInstallation.getCurrentInstallation();

			switch (type) {
			case 1:
				installation.addAllUnique("channels", Arrays.asList("CHANNEL_1"));
				break;
			case 2:
				installation.addAllUnique("channels", Arrays.asList("CHANNEL_2"));
				break;
			case 3:
				installation.addAllUnique("channels", Arrays.asList("CHANNEL_3"));
				break;
			case 4:
				installation.addAllUnique("channels", Arrays.asList("CHANNEL_4"));
				break;
			case 5:
				installation.addAllUnique("channels", Arrays.asList("CHANNEL_1", "CHANNEL_2", "CHANNEL_3", "CHANNEL_4"));
				break;
			}

			DataStore.getInstance().init(context);
			if (type == 5) {
				if (!DataStore.getInstance().get("register", false)) {
					installation.saveInBackground(new SaveCallback() {
						@Override
						public void done(ParseException e) {
							if (e == null) {
								// OK
								DataStore.getInstance().save("register", true);
								DataStore.getInstance().save("CHANNEL_1", true);
								DataStore.getInstance().save("CHANNEL_2", true);
								DataStore.getInstance().save("CHANNEL_3", true);
								DataStore.getInstance().save("CHANNEL_4", true);
							} else {
								// Err
								e.printStackTrace();
							}
						}
					});
				}
			} else {
				installation.saveInBackground(new SaveCallback() {
					@Override
					public void done(ParseException e) {
						if (e == null) {
							// OK
							if (type == 5) {
							}
						} else {
							// Err
							e.printStackTrace();
						}
					}
				});
			}
		} catch (Exception e2) {
			e2.printStackTrace();
		}
	}

	public static void unregisterChannel(final Context context, int type) {
		try {
			ParseInstallation installation = ParseInstallation.getCurrentInstallation();
			switch (type) {
			case 1:
				installation.removeAll("channels", Arrays.asList("CHANNEL_1"));
				break;
			case 2:
				installation.removeAll("channels", Arrays.asList("CHANNEL_2"));
				break;
			case 3:
				installation.removeAll("channels", Arrays.asList("CHANNEL_3"));
				break;
			case 4:
				installation.removeAll("channels", Arrays.asList("CHANNEL_4"));
				break;
			}
			installation.saveInBackground(new SaveCallback() {
				@Override
				public void done(ParseException e) {

					if (e == null) {

						// OK
					} else {
						// Err
						e.printStackTrace();
					}
				}
			});
		} catch (Exception e2) {
			e2.printStackTrace();
		}
	}

	public static final void executeChanel(final Context context, int position, final IexecuteChanel iexecuteChanel) {
		iexecuteChanel.onStart();
		ParseInstallation installation = ParseInstallation.getCurrentInstallation();
		DataStore.getInstance().init(context);
		List<String> chanels = new ArrayList<String>();

		for (int i = 0; i < 4; i++) {
			String name = "CHANNEL_" + (i + 1);

			if (DataStore.getInstance().get(name, false)) {
				chanels.add(name);
			}
		}

		boolean isRemove = false;
		final String channelName = "CHANNEL_" + (position + 1);
		if (chanels.contains(channelName)) {
			chanels.remove(channelName);
			isRemove = true;
		} else {
			chanels.add(channelName);
		}

		if (isRemove) {
			installation.removeAll("channels", Arrays.asList(channelName));
		} else {
			installation.addAllUnique("channels",  Arrays.asList(channelName));
		}

		installation.saveInBackground(new SaveCallback() {
			@Override
			public void done(ParseException e) {
				if (e == null) {
					// OK
					DataStore.getInstance().save(channelName, !DataStore.getInstance().get(channelName, false));
					iexecuteChanel.onSuccess();
				} else {
					// Err
					iexecuteChanel.onError();
					e.printStackTrace();
				}
			}
		});
	}

	public interface IexecuteChanel {

		public void onStart();

		public void onError();

		public void onSuccess();
	}
}