package com.acv.cheerz.fragment;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.json.JSONObject;

import z.lib.base.DataStore;
import z.lib.base.callback.RestClient.RequestMethod;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.acv.cheerz.R;
import com.acv.cheerz.activity.RootActivity;
import com.acv.cheerz.dialog.CheezProgressDialog;
import com.acv.cheerz.service.CheerzServiceCallBack;
import com.acv.cheerz.util.CheerzDialog;
import com.acv.cheerz.util.CheerzUtils;

public class LoginFragment extends Fragment implements OnClickListener {
	private TextView txtTitleView;
	private ImageButton btnBack;
	private ImageButton btnHome;
	private EditText edtEmail;
	private EditText edtPassword;
	private TextView btnLogin;

	private String email;
	private String password;

	// save password
	private CheckBox checkBox;
	public final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile("[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" + "\\@" + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" + "(" + "\\." + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}"
			+ ")+");

	public LoginFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View loginView = inflater.inflate(R.layout.login_layout, null);
		checkBox = CheerzUtils.getView(loginView, R.id.checkbox);

		CheerzUtils.getView(loginView, R.id.textcheckbox).setOnClickListener(this);
		CheerzUtils.getView(loginView, R.id.forgotpassword).setOnClickListener(this);

		txtTitleView = (TextView) loginView.findViewById(R.id.header_title);
		txtTitleView.setText("LOGIN");
		txtTitleView.setTextColor(getResources().getColor(R.color.ebe7ef));

		// inivisible button right
		btnHome = (ImageButton) loginView.findViewById(R.id.header_btn_right);
		btnHome.setVisibility(View.INVISIBLE);

		btnBack = (ImageButton) loginView.findViewById(R.id.header_btn_left);
		btnBack.setImageResource(R.drawable.btn_back);
		btnBack.setOnClickListener(this);

		edtEmail = (EditText) loginView.findViewById(R.id.login_edt_email_id);
		edtEmail.setOnClickListener(this);
		edtPassword = (EditText) loginView.findViewById(R.id.login_edt_pass_id);
		edtPassword.setOnClickListener(this);

		btnLogin = (TextView) loginView.findViewById(R.id.login_btn_confirm_id);
		btnLogin.setOnClickListener(this);

		edtPassword.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// nothing

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				email = edtEmail.getText().toString();
				if (!verifyEmail(email)) {
					// new CheerzDialog(getActivity()).showDialog("VERIFY",
					// "EMAIL ERROR,Please enter again");
				}
			}

			@Override
			public void afterTextChanged(Editable s) {
				// nothing

			}
		});

		return loginView;
	}

	@Override
	public void onClick(View v) {
		// TODO handle click on view
		int id = v.getId();
		switch (id) {
		case R.id.header_btn_left:
			getActivity().finish();
			break;
		case R.id.login_edt_email_id:

			break;
		case R.id.login_edt_pass_id:

			break;
		case R.id.login_btn_confirm_id:

			password = edtPassword.getText().toString();
			if (password.length() > 7) {
				excuteLogin();
			} else {
				new CheerzDialog(getActivity()).showDialog("ERROR", "password or email error!");
			}

			break;

		case R.id.forgotpassword:
			// goto forgot password
			// TODO

			((RootActivity) getActivity()).gotoForgotPassword();
			break;

		case R.id.textcheckbox:
			checkBox.setChecked(!checkBox.isChecked());
			break;
		default:
			break;
		}
	}

	/**
	 * this method will execute login
	 */
	private void excuteLogin() {

		Map<String, String> params = new HashMap<String, String>();
		params.put("email", email);
		params.put("password", password);

		CheerzUtils.execute(getActivity(), RequestMethod.GET, CheerzUtils.API.API_LOGIN, params, new CheerzServiceCallBack() {
			private CheezProgressDialog cheezProgressDialog;

			@Override
			public void onStart() {
				super.onStart();
				if (cheezProgressDialog == null) {
					cheezProgressDialog = new CheezProgressDialog(getActivity());
					cheezProgressDialog.show();
				}
			}

			@Override
			public void onSucces(JSONObject jsonObject) {
				super.onSucces(jsonObject);
				if (cheezProgressDialog != null) {
					cheezProgressDialog.dismiss();
				}
				getActivity().finish();
			}

			public void onError(String message) {
				super.onError(message);

				if (cheezProgressDialog != null) {
					cheezProgressDialog.dismiss();
				}

				CheerzUtils.showDialog(getActivity(), message);
			}
		});
	}

	/**
	 * this method will verify email user input.
	 * 
	 * @param email
	 * @return true | false
	 */
	private boolean verifyEmail(String email) {
		return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
	}
}
