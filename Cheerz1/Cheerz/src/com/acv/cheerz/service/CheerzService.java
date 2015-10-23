package com.acv.cheerz.service;

import java.util.Map;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

import z.lib.base.callback.ExeCallBack;
import z.lib.base.callback.ResClientCallBack;
import z.lib.base.callback.RestClient;
import z.lib.base.callback.RestClient.RequestMethod;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.acv.cheerz.R;
import com.acv.cheerz.util.CheerzUtils;

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

	public void execute(RequestMethod method, final String api, Map<String, String> params, final CheerzServiceCallBack cheerzServiceCallBack) {
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

					if ("1".equals(status_code)) {
						if (getApiName().equals(CheerzUtils.API.API_GET_FEED)) {
							// save data
						} else if (getApiName().equals(CheerzUtils.API.API_GET_FEED_GALLLEY)) {
							// save data
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
			}
		};

		if (CheerzUtils.isLogin(this)) {
			if (!params.containsKey("token")) {
				params.put("token", CheerzUtils.getToken(this));
			}
		}

		Set<String> keys = params.keySet();

		for (String key : keys) {
			resClientCallBack.addParam(key, params.get(key));
		}

		ExeCallBack callBack = new ExeCallBack();
		callBack.executeAsynCallBack(resClientCallBack);
	}
}