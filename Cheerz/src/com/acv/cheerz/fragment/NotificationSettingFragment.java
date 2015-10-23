package com.acv.cheerz.fragment;

import java.util.HashMap;

import org.json.JSONObject;

import z.lib.base.DataStore;
import z.lib.base.callback.RestClient.RequestMethod;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class NotificationSettingFragment extends Fragment {
	private com.acv.cheerz.view.CheerzListView list;
	private ArrayAdapter<String> daAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View loginView = inflater.inflate(R.layout.notificationsetting, null);

		HeaderView headerView = (HeaderView) loginView.findViewById(R.id.headerView1);
		headerView.visibivityRight(false, R.drawable.transfer);
		headerView.visibivityLeft(true, R.drawable.btn_back);
		headerView.setTextHeader(R.string.notificationa);
		headerView.setiOnClickHeader(new IOnClickHeader() {
			@Override
			public void onClickRight() {

			}

			@Override
			public void onClickLeft() {
				getActivity().onBackPressed();
			}
		});

		list = (CheerzListView) loginView.findViewById(R.id.list);

		reload();
		return loginView;
	}

	private void reload() {
		list.setAdapter(daAdapter = new ArrayAdapter<String>(getActivity(), 0, getActivity().getResources().getStringArray(R.array.notification_setting)) {
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

		CheerzUtils.execute(getActivity(), RequestMethod.GET, API.API_UPDATE_SETTING, params, new CheerzServiceCallBack() {
			ProgressDialog progressDialog;

			@Override
			public void onStart() {
				super.onStart();
				progressDialog = ProgressDialog.show(getActivity(), null, null);
			}

			@Override
			public void onError(String message) {
				super.onError(message);
				CheerzUtils.showDialog(getActivity(), message);
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
