package com.acv.cheerz.dialog;

import z.lib.base.DataStore;
import z.lib.base.LibsBaseAdialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.acv.cheerz.GcmBroadcastReceiver;
import com.acv.cheerz.GcmBroadcastReceiver.IexecuteChanel;
import com.acv.cheerz.R;
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
				dismiss();
			}
		});

		list = (CheerzListView) findViewById(R.id.list);

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

				box.setChecked(DataStore.getInstance().get("CHANNEL_" + (position + 1), false));

				box.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						// DataStore.getInstance().save(getItem(position),
						// box.isChecked());

						checkChange(position, getItem(position));
					}
				});
				// DataStore.getInstance().get(key, _default);

				((TextView) convertView.findViewById(R.id.tv1)).setText(getItem(position));
				return convertView;
			}
		});
	}

	@Override
	public int getLayout() {
		return R.layout.notificationsetting;
	}

	private void checkChange(int position, String key) {
		if (position == 0) {

		} else if (position == 1) {

		} else if (position == 2) {

		} else if (position == 3) {

		}

		GcmBroadcastReceiver.executeChanel(getContext(), position, new IexecuteChanel() {
			ProgressDialog dialog;

			@Override
			public void onSuccess() {
				dialog.dismiss();
				daAdapter.notifyDataSetChanged();
			}

			@Override
			public void onStart() {
				dialog = ProgressDialog.show(getContext(), null, getContext().getString(R.string.loading));
				// daAdapter.notifyDataSetChanged();
			}

			@Override
			public void onError() {
				dialog.dismiss();

			}
		});
	}
}
