package com.acv.cheerz.fragment;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.regex.Pattern;

import org.json.JSONObject;

import z.lib.base.DataStore;
import z.lib.base.callback.ExeCallBack;
import z.lib.base.callback.ResClientCallBack;
import z.lib.base.callback.RestClient;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.acv.cheerz.R;
import com.acv.cheerz.util.CheerzDialog;
import com.acv.cheerz.util.CheerzUtils;

public class RegisterFragment extends Fragment implements OnClickListener {
	private String TAG = "RegisterFragment";
	private ImageView imgAvatar;
	private ImageButton btnBack;
	private ImageButton btnHome;
	private TextView txtTitle;
	private Button btnRegister;

	private EditText edtName;
	private EditText edtEmail;
	private EditText edtPassword;
	private EditText edtPasswordConfirm;

	private String name;
	private String email = "";
	private String password = "";
	private String passwordConfirm;

	private String avatarString = "";// base64 cua image avatar
	
	private int type = 0; //  0 is register,1 edit profile

	public final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile("[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" + "\\@" + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" + "(" + "\\." + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}"
			+ ")+");

	private int REQUEST_CODE_IMAGE = 99;

	public RegisterFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View registerView = inflater.inflate(R.layout.register_layout, null);

		Intent intentData = getActivity().getIntent();
		

		btnHome = (ImageButton) registerView.findViewById(R.id.header_btn_right);

		btnHome.setVisibility(View.INVISIBLE);

		btnBack = (ImageButton) registerView.findViewById(R.id.header_btn_left);
		btnBack.setImageResource(R.drawable.btn_back);
		btnBack.setOnClickListener(this);

		txtTitle = (TextView) registerView.findViewById(R.id.header_title);
		txtTitle.setText("REGISTER");
		txtTitle.setTextColor(getResources().getColor(R.color.ebe7ef));

		imgAvatar = (ImageView) registerView.findViewById(R.id.register_img_id);
		imgAvatar.setOnClickListener(this);

		btnRegister = (Button) registerView.findViewById(R.id.register_btn_confirm_id);
		btnRegister.setOnClickListener(this);

		edtName = (EditText) registerView.findViewById(R.id.register_edt_name_id);
		edtEmail = (EditText) registerView.findViewById(R.id.register_edt_email_id);
		edtPassword = (EditText) registerView.findViewById(R.id.register_edt_pass_id);
		edtPassword.setOnClickListener(this);
		edtPasswordConfirm = (EditText) registerView.findViewById(R.id.register_edt_pass_confirm_id);
		edtPasswordConfirm.setOnClickListener(this);

		edtName.setOnClickListener(this);
		edtEmail.setOnClickListener(this);
		edtPassword.setOnClickListener(this);
		edtPasswordConfirm.setOnClickListener(this);

		edtPassword.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// nothing

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				email = edtEmail.getText().toString();
				if (!verifyEmail(email)) {
					showDialog("VERIFY", "EMAIL ERROR,Please enter again");
				}
			}

			@Override
			public void afterTextChanged(Editable s) {
				// nothing

			}
		});

		if (intentData.getStringExtra("type").equals("edit_profile")) {
			type = 1;
			try {
				edtEmail.setText(DataStore.getInstance().get(CheerzUtils.EMAIL_LOGIN, "").toString());
				edtPassword.setText(DataStore.getInstance().get(CheerzUtils.PASS_LOGIN, "").toString());	
			} catch (Exception e) {
				
			}
			
			edtEmail.setFocusable(false); // disable input text
			edtEmail.setEnabled(false);
			edtEmail.setClickable(false); 
			txtTitle.setText("INFOMATION");
			btnRegister.setText("OK");
			
			new ImageDownloader(imgAvatar).execute(CheerzUtils.AVATAR);
			Log.i(TAG, "Disable Email");	
			
		}
		

		return registerView;
	}

	@Override
	public void onClick(View v) {
		// TODO handle action click on view
		int id = v.getId();
		switch (id) {
		case R.id.register_img_id:
			getAvatar();
			break;
		case R.id.register_btn_confirm_id:
//			if (CheerzUtils.isNetworkConnected(getActivity())) {
				email = edtEmail.getText().toString();
				password = edtPassword.getText().toString();
				passwordConfirm = edtPasswordConfirm.getText().toString();
				if (verifyEmail(edtEmail.getText().toString()) && verifyPassword(password, passwordConfirm)) {
					excRegisterAccount();
				} else if (!verifyPassword(password, passwordConfirm)) {
					new CheerzDialog(getActivity()).showDialog("ERROR", "passwrod conflic");
				}
//			} else {
//				new CheerzDialog(getActivity()).showDialog("ERROR", "connection error!");
//			}

			break;
		case R.id.header_btn_left:
			getActivity().finish();
			break;
		case R.id.register_edt_email_id:

			break;
		case R.id.register_edt_pass_id:

			break;
		default:
			break;
		}
	}

	private void excRegisterAccount() {
		ExeCallBack apiCallBack = new ExeCallBack();
		ResClientCallBack clientCallBack = new ResClientCallBack() {

			@Override
			public String getApiName() {

				return CheerzUtils.API.API_REGISTER;
			}

			@Override
			public void onError(String errorMessage) {
				Log.e(TAG, "[onError] " + errorMessage.toString());

				super.onError(errorMessage);
			}

			@Override
			public void onSuccess(String response) {
				Log.i(TAG, response.toString());
				super.onSuccess(response);
			}

			@Override
			public void onCallBack(Object object) {
				super.onCallBack(object);
				try {
					JSONObject jsonObj = new JSONObject(((RestClient) object).getResponse());
					JSONObject jsonData = jsonObj.getJSONObject("user");
					//String token = jsonObj.getString("token");
					//DataStore.getInstance().save(CheerzUtils.TOKEN, token);
					DataStore.getInstance().save(CheerzUtils.EMAIL_LOGIN, email);
					DataStore.getInstance().save(CheerzUtils.PASS_LOGIN, password);
					int status_code = jsonObj.getInt("status_code");
					if (status_code == 1) {
						// goto FEED screen
						getActivity().finish();
					} else {
						// do anything
						showDialog("REGISTER", "FAILED");
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		};
		if (type == 0) {
			clientCallBack.addParam("name", name);
			clientCallBack.addParam("email", email);
			clientCallBack.addParam("password", password);
			clientCallBack.addParam("avatar", avatarString);
		}else{
			clientCallBack.addParam("name", name);
			clientCallBack.addParam("token", DataStore.getInstance().get(CheerzUtils.TOKEN, ""));
			clientCallBack.addParam("password", password);
			clientCallBack.addParam("avatar", avatarString);
		}
		apiCallBack.executeAsynCallBack(clientCallBack);
	}

	/**
	 * this method pick image from album of device
	 */
	private void getAvatar() {
		Intent intentPickImage = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		startActivityForResult(intentPickImage, REQUEST_CODE_IMAGE);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_CODE_IMAGE && data.getData() != null) {
			Uri uriImage = data.getData();

			Bitmap bmp = null;
			try {
				bmp = decodeUri(getActivity(), uriImage, 350);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			bmp = getCroppedBitmap(bmp);
			imgAvatar.setImageBitmap(bmp);
			avatarString = base64ImageToString(bmp);
		}
	}

	/**
	 * this method crop image retangle to image circle
	 * 
	 * @param bitmap
	 * @return return new bitmap circle
	 */
	public Bitmap getCroppedBitmap(Bitmap bitmap) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), android.graphics.Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2, bitmap.getWidth() / 2, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		return output;
	}

	/**
	 * this method will decode uri to get bitmap and scale, return new bitmap
	 * 
	 * @param c
	 *            : context
	 * @param uri
	 *            : uri of image
	 * @param requiredSize
	 *            : new size request
	 * @return new bitmap
	 * @throws FileNotFoundException
	 */
	public static Bitmap decodeUri(Context c, Uri uri, final int requiredSize) throws FileNotFoundException {
		BitmapFactory.Options o = new BitmapFactory.Options();
		o.inJustDecodeBounds = true;
		BitmapFactory.decodeStream(c.getContentResolver().openInputStream(uri), null, o);

		int width_tmp = o.outWidth, height_tmp = o.outHeight;
		int scale = 1;

		while (true) {
			if (width_tmp / 2 < requiredSize || height_tmp / 2 < requiredSize)
				break;
			width_tmp /= 2;
			height_tmp /= 2;
			scale *= 2;
		}

		BitmapFactory.Options o2 = new BitmapFactory.Options();
		o2.inSampleSize = scale;
		return BitmapFactory.decodeStream(c.getContentResolver().openInputStream(uri), null, o2);
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

	/**
	 * verify passwrod before send to server
	 * 
	 * @param password
	 * @param passwordConfirm
	 * @return true | false
	 */
	private boolean verifyPassword(String password, String passwordConfirm) {
		if (password.equals(passwordConfirm)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * show dialog
	 * 
	 * @param title
	 * @param msg
	 */
	private void showDialog(String title, String msg) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(title);
		builder.setMessage(msg);
		builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// code

			}
		});

		Dialog dialog = builder.create();
		dialog.show();
	}

	
	private String base64ImageToString(Bitmap bmp){
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		bmp.compress(Bitmap.CompressFormat.PNG, 90, byteArrayOutputStream);
		byte[] byteArray = byteArrayOutputStream.toByteArray();
		String encodeString = Base64.encodeToString(byteArray, Base64.DEFAULT);
		Log.i(TAG, "base64 avatar = " + encodeString);
		return encodeString;
	}
	
	/**
	 * this class will download and show image avatar
	 * @author dodanhhieu
	 *
	 */
	class ImageDownloader extends AsyncTask<String, Void, Bitmap> {
		  ImageView bmImage;

		  public ImageDownloader(ImageView bmImage) {
		      this.bmImage = bmImage;
		  }

		  protected Bitmap doInBackground(String... urls) {
		      String url = urls[0];
		      Bitmap mIcon = null;
		      try {
		        InputStream in = new java.net.URL(url).openStream();
		        mIcon = BitmapFactory.decodeStream(in);
		      } catch (Exception e) {
		          Log.e("Error", e.getMessage());
		      }
		      return mIcon;
		  }

		  protected void onPostExecute(Bitmap result) {
		      
		      result = getCroppedBitmap(result);
		      imgAvatar.setImageBitmap(result);
		  }
		}

}
