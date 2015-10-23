package com.acv.cheerz.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.acv.cheerz.R;
import com.acv.cheerz.util.CheerzUtils;
import com.acv.cheerz.view.HeaderView;
import com.acv.cheerz.view.HeaderView.IOnClickHeader;

public abstract class BaseFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener {

	public BaseFragment() {
		super();
	}

	@Override
	public void onClick(View v) {

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public final View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(getLayout(), null);
		init(view);
		return view;
	}

	private HeaderView headerView;

	public void getheader(View view) {
		headerView = CheerzUtils.getView(view, R.id.headerView1);

		headerView.setiOnClickHeader(new IOnClickHeader() {

			@Override
			public void onClickRight() {
				onClickHeaderRight();
			}

			@Override
			public void onClickLeft() {
				onClickHeaderLeft();
			}
		});
		;
	}

	/**
	 * 
	 * @param res
	 * @param restLeft
	 * @param resRight
	 */
	public void updateHeader(int res, int restLeft, int resRight) {
		if (headerView != null) {
			headerView.setTextHeader(res);

			headerView.visibivityRight(resRight != 0, resRight);
			headerView.visibivityLeft(restLeft != 0, restLeft);
		}
	}

	public void onClickHeaderLeft() {

	}

	public void onClickHeaderRight() {

	}

	public abstract int getLayout();

	public abstract void init(View view);
}
