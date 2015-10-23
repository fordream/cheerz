package com.acv.cheerz.dialog;

import java.util.HashMap;

import org.json.JSONObject;

import z.lib.base.DataStore;
import z.lib.base.LibsBaseAdialog;
import z.lib.base.callback.RestClient.RequestMethod;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.acv.cheerz.R;
import com.acv.cheerz.db.User;
import com.acv.cheerz.service.CheerzServiceCallBack;
import com.acv.cheerz.util.CheerzUtils;
import com.acv.cheerz.util.CheerzUtils.API;
import com.acv.cheerz.view.CheerzListView;
import com.acv.cheerz.view.HeaderView;
import com.acv.cheerz.view.HeaderView.IOnClickHeader;

public class NotificationSettingDialog extends LibsBaseAdialog {
	private com.acv.cheerz.view.CheerzListView list;
	private ArrayAdapter<String> daAdapter;

	public NotificationSettingDialog(Context context) {
		super(context);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		findViewById(R.id.main).startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.scale));
		
		HeaderView headerView = (HeaderView) findViewById(R.id.headerView1);
		headerView.visibivityRight(false, R.drawable.transfer);
		headerView.visibivityLeft(true, R.drawable.btn_back);
		headerView.setTextHeader(R.string.notificationa);
		headerView.setiOnClickHeader(new IOnClickHeader() {
			@Override
			public void onClickRight() {

			}

			@Override
			public void onClickLeft() {
				Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.scale_to_center);
				animation.setAnimationListener(new AnimationListener() {
					
					@Override
					public void onAnimationStart(Animation animation) {
						
					}
					
					@Override
					public void onAnimationRepeat(Animation animation) {
						
					}
					
					@Override
					public void onAnimationEnd(Animation animation) {
						dismiss();						
					}
				});
				findViewById(R.id.main).startAnimation(animation);
				
			}
		});

		list = (CheerzListView) findViewById(R.id.list);

		reload();
	}

	private void reload() {
		list.setAdapter(daAdapter = new ArrayAdapter<String>(getContext(), 0, getContext().getResources().getStringArray(R.array.notification_setting)) {
			@Override
			public View getView(final int position, View convertView, ViewGroup parent) {

				if (convertView == null) {
					convertView = ((LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.setting_item, null);
				}

				convertView.findViewById(R.id.checkbox).setVisibility(View.VISIBLE);
				convertView.findViewById(R.id.imageView_1).setVisibility(View.GONE);
				DataStore.getInstance().init(parent.getContext());

				final CheckBox box = (CheckBox) convertView.findViewById(R.id.checkbox);
				box.setOnCheckedChangeListener(null);
				box.setChecked(User.getSetting(getContext(), position));

				box.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

						checkChange(position, getItem(position), box.isChecked());
					}
				});

				((TextView) convertView.findViewById(R.id.tv1)).setText(getItem(position));
				return convertView;
			}
		});
	}

	@Override
	public int getLayout() {
		return R.layout.notificationsetting;
	}

	private void checkChange(int position, String key, boolean isChecked) {
		String type = User.type_1;
		if (position == 0) {
			type = User.type_1;
		} else if (position == 1) {
			type = User.type_2;
		} else if (position == 2) {
			type = User.type_3;
		} else if (position == 3) {
			type = User.type_4;
		}

		HashMap<String, String> params = new HashMap<String, String>();
		params.put(type, isChecked ? "1" : "0");

		CheerzUtils.execute(getContext(), RequestMethod.GET, API.API_UPDATE_SETTING, params, new CheerzServiceCallBack() {
			ProgressDialog progressDialog;

			@Override
			public void onStart() {
				super.onStart();
				progressDialog = ProgressDialog.show(getContext(), null, null);
			}

			@Override
			public void onError(String message) {
				super.onError(message);
				CheerzUtils.showDialog(getContext(), message);
				progressDialog.dismiss();
				reload();
			}

			@Override
			public void onSucces(JSONObject jsonObject) {
				super.onSucces(jsonObject);
				progressDialog.dismiss();
				reload();
			}
		});
	}
}
