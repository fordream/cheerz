package com.acv.cheerz.fragment;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import z.lib.base.DataStore;
import z.lib.base.callback.ExeCallBack;
import z.lib.base.callback.ResClientCallBack;
import z.lib.base.callback.RestClient;
import z.lib.base.callback.RestClient.RequestMethod;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.acv.cheerz.R;
import com.acv.cheerz.service.CheerzServiceCallBack;
import com.acv.cheerz.util.CheerzDialog;
import com.acv.cheerz.util.CheerzUtils;

public class LoginFragment extends Fragment implements OnClickListener {
	private TextView txtTitleView;
	private ImageButton btnBack;
	private ImageButton btnHome;
	private EditText edtEmail;
	private EditText edtPassword;
	private Button btnLogin;

	private String email;
	private String password;

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

		btnLogin = (Button) loginView.findViewById(R.id.login_btn_confirm_id);
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
					new CheerzDialog(getActivity()).showDialog("VERIFY", "EMAIL ERROR,Please enter again");
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
			// email = edtEmail.getText().toString();
			// if (!verifyEmail(email)) {
			// new CheerzDialog(getActivity()).showDialog("ERROR",
			// "email error!");
			// }

			break;
		case R.id.login_btn_confirm_id:
			if (CheerzUtils.isNetworkConnected(getActivity())) {
				password = edtPassword.getText().toString();
				if (password.length() > 7) {
					excuteLogin();
				} else {
					new CheerzDialog(getActivity()).showDialog("ERROR", "password or email error!");
				}
			} else {
				new CheerzDialog(getActivity()).showDialog("ERROR", "connection error!");
			}

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
			@Override
			public void onSucces(JSONObject jsonObject) {
				super.onSucces(jsonObject);

				String token = CheerzUtils.getString(jsonObject, "token");
				CheerzUtils.saveToken(getActivity(), token);

				CheerzUtils.saveLogin(getActivity(), true);

				try {
					CheerzUtils.saveUserId(getActivity(), CheerzUtils.getString(jsonObject.getJSONObject("user"), "user_id"));
				} catch (JSONException e) {
				}

				getActivity().finish();
			}

			public void onError(String message) {
				super.onError(message);
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
