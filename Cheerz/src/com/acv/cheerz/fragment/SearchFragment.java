package com.acv.cheerz.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import z.lib.base.callback.RestClient.RequestMethod;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.acv.cheerz.R;
import com.acv.cheerz.dialog.CheezProgressDialog;
import com.acv.cheerz.service.CheerzServiceCallBack;
import com.acv.cheerz.util.CheerzUtils;
import com.acv.cheerz.util.CheerzUtils.API;

public class SearchFragment extends BaseFragment implements TextWatcher, OnEditorActionListener {
	private EditText edtsearch;
	private View search_right;

	@Override
	public int getLayout() {
		return R.layout.search;
	}

	@Override
	public void init(View view) {
		CheerzUtils.getView(view, R.id.search_left).setOnClickListener(this);
		CheerzUtils.getView(view, R.id.search_right).setOnClickListener(this);

		search_right = CheerzUtils.getView(view, R.id.search_right);
		edtsearch = CheerzUtils.getView(view, R.id.edtsearch);

		edtsearch.setText(getArguments().getString("search"));
		edtsearch.addTextChangedListener(this);
		edtsearch.setOnEditorActionListener(this);
		CheerzUtils.showView(search_right, false);

		search(getArguments().getString("search"));
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);

		if (R.id.search_left == v.getId()) {
			getActivity().onBackPressed();
		} else if (R.id.search_right == v.getId()) {
			edtsearch.setText("");
		}
	}

	public SearchFragment() {
		super();
	}

	@Override
	public void afterTextChanged(Editable s) {

	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		CheerzUtils.showView(search_right, !CheerzUtils.isBlank(edtsearch.getText().toString()));
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {

	}

	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		if (actionId == KeyEvent.KEYCODE_ENDCALL || event == null || KeyEvent.KEYCODE_CALL == actionId) {
			// Conts.hiddenKeyBoard(getActivity());
			// moidichvuchonhieunguoi_add_plusOnCLick.onClick(null);

			search(edtsearch.getText().toString());
		}
		return false;
	}

	private void search(String text) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("search", text);
		params.put("current_page", "1");

		CheerzUtils.execute(getActivity(), RequestMethod.GET, API.API_SEARCH, params, cheerzServiceCallBack);
	}

	private CheerzServiceCallBack cheerzServiceCallBack = new CheerzServiceCallBack() {
		private CheezProgressDialog cheezProgressDialog;

		public void onStart() {
			if (cheezProgressDialog == null) {
				cheezProgressDialog = new CheezProgressDialog(getActivity());
				cheezProgressDialog.show();
			}
		};

		public void onError(String message) {
			if (cheezProgressDialog != null) {
				cheezProgressDialog.dismiss();
			}
		};

		public void onSucces(org.json.JSONObject jsonObject) {
			if (cheezProgressDialog != null) {
				cheezProgressDialog.dismiss();
			}

			if (getActivity() != null) {

			}
		}
	};
	
}
