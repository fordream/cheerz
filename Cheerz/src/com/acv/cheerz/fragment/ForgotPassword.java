package com.acv.cheerz.fragment;

import android.view.View;
import android.widget.TextView;

import com.acv.cheerz.R;
import com.acv.cheerz.activity.RootActivity;
import com.acv.cheerz.util.CheerzUtils;

public class ForgotPassword extends BaseFragment {
	private TextView login_edt_email_id;

	public ForgotPassword() {
	}

	@Override
	public int getLayout() {
		return R.layout.forgotpassword;
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.login_btn_confirm_id) {
			//

		}
	}

	@Override
	public void init(View view) {
		CheerzUtils.getView(view, R.id.login_btn_confirm_id).setOnClickListener(this);
		login_edt_email_id = CheerzUtils.getView(view, R.id.login_edt_email_id);
		CheerzUtils.getView(view, R.id.main).setOnClickListener(null);
		getheader(view);
		updateHeader(R.string.forgotpassword, R.drawable.btn_back, R.drawable.btn_home);
	}

	@Override
	public void onClickHeaderLeft() {
		super.onClickHeaderLeft();
		getActivity().onBackPressed();
	}

	@Override
	public void onClickHeaderRight() {
		super.onClickHeaderRight();

		((RootActivity) getActivity()).finishCustom();
	}

}
