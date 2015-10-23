package com.acv.cheerz.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

import z.lib.base.LogUtils;
import z.lib.base.callback.ExeCallBack;
import z.lib.base.callback.ResClientCallBack;
import z.lib.base.callback.RestClient;
import z.lib.base.callback.RestClient.RequestMethod;
import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.IBinder;

import com.acv.cheerz.R;
import com.acv.cheerz.db.User;
import com.acv.cheerz.util.CheerzUtils;
import com.acv.cheerz.util.CheerzUtils.API;

public class CheerzService extends Service {
	public static final String ACTION = "com.acv.cheerz.service.CheerzService";

	private CheerzBin cheerzBin;

	public CheerzService() {
		super();
	}

	@Override
	public IBinder onBind(Intent intent) {
		return cheerzBin;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		cheerzBin = new CheerzBin(this);
	}

	public void execute(RequestMethod method, final String api, final Map<String, String> params, final CheerzServiceCallBack cheerzServiceCallBack) {
		if (cheerzServiceCallBack != null) {
			cheerzServiceCallBack.onStart();
		}

		ResClientCallBack resClientCallBack = new ResClientCallBack() {
			@Override
			public void onSaveInAsynTask(RestClient client) {
				super.onSaveInAsynTask(client);
				try {
					JSONObject jsonObject = new JSONObject(client.getResponse());
					String status_code = CheerzUtils.getString(jsonObject, "status_code");
					String msg_error = CheerzUtils.getString(jsonObject, "msg_error");

					String is_login = CheerzUtils.getString(jsonObject, "is_login");

					if ("1".equals(status_code)) {
						if (getApiName().equals(CheerzUtils.API.API_GET_FEED)) {
							// save data
						} else if (getApiName().equals(CheerzUtils.API.API_GET_FEED_GALLLEY)) {
							// save data
						} else if (getApiName().equals(CheerzUtils.API.API_LOGIN)) {
							// all for unlogin
							ContentValues values = new ContentValues();
							values.put(User.status_login, "0");
							getContentResolver().update(User.CONTENT_URI, values, null, null);

							// save data
							JSONObject user = jsonObject.getJSONObject("user");

							String email = CheerzUtils.getString(user, User.email);

							String selection = String.format("%s ='%s'", User.email, email);
							boolean isInsert = true;
							Cursor cursor = getContentResolver().query(User.CONTENT_URI, null, selection, null, null);
							if (cursor != null) {
								if (cursor.getCount() >= 1) {
									isInsert = false;
								}

								cursor.close();
							}

							ContentValues contentValues = new ContentValues();
							contentValues.put(User.token, CheerzUtils.getString(jsonObject, User.token));
							contentValues.put(User.avatar, CheerzUtils.getString(user, User.avatar));
							contentValues.put(User.email, CheerzUtils.getString(user, User.email));
							contentValues.put(User.user_id, CheerzUtils.getString(user, User.user_id));
							contentValues.put(User.name, CheerzUtils.getString(user, User.name));
							contentValues.put(User.time_countdown, CheerzUtils.getString(user, User.time_countdown));
							contentValues.put(User.status_login, "1");

							if (isInsert) {
								getContentResolver().insert(User.CONTENT_URI, contentValues);
							} else {
								getContentResolver().update(User.CONTENT_URI, contentValues, selection, null);
							}

						} else if (getApiName().equals(CheerzUtils.API.API_EDIT_FAVORITE)) {
							// save

						} else if (getApiName().equals(API.API_GET_SETTING)) {
							updateSetting(jsonObject);
						} else if (getApiName().equals(API.API_UPDATE_SETTING)) {
							updateSetting(params);
						} else if (getApiName().equals(API.API_LIST_FAVORITE)) {
							updateFavorite(jsonObject);
						}

					}

					if (!"true".equals(is_login)) {
						List<String> apis = new ArrayList<String>();
						apis.add(API.API_REGISTER);
						apis.add(API.API_LOGIN);
						apis.add(API.API_GET_FEED);
						apis.add(API.API_GET_FEED_GALLLEY);
						if (!apis.contains(getApiName())) {
//							User.setUnLogin(CheerzService.this);
						}
					}
				} catch (Exception exception) {

				}
			}

			@Override
			public String getApiName() {
				return api;
			}

			@Override
			public void onError(String errorMessage) {
				super.onError(errorMessage);
				if (cheerzServiceCallBack != null) {
					cheerzServiceCallBack.onError(errorMessage);
				}
			}

			@Override
			public void onSuccess(String response) {
				super.onSuccess(response);
				if (cheerzServiceCallBack != null) {
					try {
						JSONObject jsonObject = new JSONObject(response);
						String status_code = CheerzUtils.getString(jsonObject, "status_code");
						String msg_error = CheerzUtils.getString(jsonObject, "msg_error");

						if ("1".equals(status_code)) {
							cheerzServiceCallBack.onSucces(jsonObject);
						} else {
							if (CheerzUtils.isBlank(msg_error)) {
								onError(getString(R.string.error_connect_server));
							} else {
								onError(msg_error);
							}
						}
					} catch (JSONException e) {
						onError(getString(R.string.error_connect_server));
					}
				}

				if (getApiName().equals(CheerzUtils.API.API_LOGIN)) {
					updateSettingCallToServer(new CheerzServiceCallBack());

					getListFavorite(new CheerzServiceCallBack());
				}

			}
		};

		if (CheerzUtils.isLogin(this)) {
			if (!params.containsKey("token")) {
				params.put("token", CheerzUtils.getToken(this));
			}

			if (!params.containsKey("user_id")) {
				params.put("user_id", CheerzUtils.getUserId(this));
			}
		}

		Set<String> keys = params.keySet();

		for (String key : keys) {
			resClientCallBack.addParam(key, params.get(key));
		}

		ExeCallBack callBack = new ExeCallBack();
		callBack.executeAsynCallBack(resClientCallBack);
	}

	protected void updateFavorite(JSONObject jsonObject) {
		// TODO
	}

	protected void updateSetting(JSONObject jsonObject) {

		ContentValues contentValues = new ContentValues();
		contentValues.put(User.type_1, CheerzUtils.getString(jsonObject, User.type_1));
		contentValues.put(User.type_2, CheerzUtils.getString(jsonObject, User.type_2));
		contentValues.put(User.type_3, CheerzUtils.getString(jsonObject, User.type_3));
		contentValues.put(User.type_4, CheerzUtils.getString(jsonObject, User.type_4));

		String selection = String.format("%s ='%s'", User.user_id, CheerzUtils.getUserId(CheerzService.this));
		getContentResolver().update(User.CONTENT_URI, contentValues, selection, null);
	}

	protected void updateSetting(Map<String, String> params) {
		ContentValues contentValues = new ContentValues();
		if (params.containsKey(User.type_1))
			contentValues.put(User.type_1, params.get(User.type_1));

		if (params.containsKey(User.type_2))
			contentValues.put(User.type_2, params.get(User.type_2));

		if (params.containsKey(User.type_3))
			contentValues.put(User.type_3, params.get(User.type_3));

		if (params.containsKey(User.type_4))
			contentValues.put(User.type_4, params.get(User.type_4));

		String selection = String.format("%s ='%s'", User.user_id, CheerzUtils.getUserId(CheerzService.this));
		getContentResolver().update(User.CONTENT_URI, contentValues, selection, null);
	}

	protected void getListFavorite(CheerzServiceCallBack cheerzServiceCallBack) {
		execute(RequestMethod.GET, API.API_LIST_FAVORITE, new HashMap<String, String>(), cheerzServiceCallBack);
	}

	public void updateSettingCallToServer(CheerzServiceCallBack cheerzServiceCallBack) {
		execute(RequestMethod.GET, API.API_GET_SETTING, new HashMap<String, String>(), cheerzServiceCallBack);
	}
}