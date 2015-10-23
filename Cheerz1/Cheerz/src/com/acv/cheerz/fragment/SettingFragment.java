package com.acv.cheerz.fragment;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.acv.cheerz.GcmBroadcastReceiver;
import com.acv.cheerz.R;
import com.acv.cheerz.activity.RootActivity;
import com.acv.cheerz.dialog.NotificationSettingDialog;
import com.acv.cheerz.view.CheerzListView;
import com.acv.cheerz.view.HeaderView;
import com.acv.cheerz.view.HeaderView.IOnClickHeader;

public class SettingFragment extends Fragment {
	com.acv.cheerz.view.CheerzListView list;

	public SettingFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.setting, null);
		try {
			PackageInfo pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
			((TextView) view.findViewById(R.id.tv1)).setText(Html.fromHtml(String.format("<u>Version %s</u>", pInfo.versionName)));
		} catch (Exception exception) {
		}
		HeaderView headerView = (HeaderView) view.findViewById(R.id.headerView1);
		headerView.visibivityRight(false, R.drawable.transfer);
		headerView.visibivityLeft(true, R.drawable.btn_back);
		headerView.setTextHeader(R.string.settinga);
		headerView.setiOnClickHeader(new IOnClickHeader() {

			@Override
			public void onClickRight() {

			}

			@Override
			public void onClickLeft() {
				((RootActivity) getActivity()).onBackPressed();
			}
		});

		list = (CheerzListView) view.findViewById(R.id.list);

		list.setAdapter(new ArrayAdapter<String>(getActivity(), 0, getActivity().getResources().getStringArray(R.array.settings)) {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {

				if (convertView == null) {
					convertView = ((LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.setting_item, null);
				}

				((TextView) convertView.findViewById(R.id.tv1)).setText(getItem(position));
				return convertView;
			}
		});

		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				if (position == 2) {
					new NotificationSettingDialog(getActivity()).show();

				}
			}
		});
		return view;
	}
}