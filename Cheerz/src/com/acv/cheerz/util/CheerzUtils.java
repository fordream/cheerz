package com.acv.cheerz.util;

import java.util.Map;
import java.util.Random;

import org.json.JSONException;
import org.json.JSONObject;

import z.lib.base.callback.RestClient.RequestMethod;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.IBinder;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.acv.cheerz.CheerzApplication;
import com.acv.cheerz.R;
import com.acv.cheerz.db.User;
import com.acv.cheerz.service.CheerzServiceCallBack;

public class CheerzUtils {

	public static class API {
		public static final String BASESERVER = "http://acvdev.com/anhlx/cheerz/";
		public static final String API_REGISTER = "user/register";
		public static final String API_LOGIN = "user/login";
		public static final String API_EDIT_PROFILE = "user/updateProfile";
		public static final String API_GET_FEED = "artist/getFeed";
		public static final String API_GET_FEED_GALLLEY = "artist/getFeedGallery";
		public static final String API_EDIT_FAVORITE = "user/favorite";
		public static final String API_LIST_RANKING = "artist/getListRanking";
		public static final String API_LIST_FAN_RANKING = "artist/getListFanRanking";
		public static final String API_DETAIL_ARTIST = "artist/getDetailArtist";
		public static final String API_GET_SETTING = "user/getSetting";
		public static final String API_UPDATE_SETTING = "user/updateSetting";
		public static final String API_LIST_FAVORITE = "user/getListFavorite";
		public static final String API_LIST_IMAGE_VOTE = "user/getListImageVote";
		public static final String API_SEARCH = "artist/search";
	}

	public static void execute(Context context, final RequestMethod method, final String api, final Map<String, String> params, final CheerzServiceCallBack cheerzServiceCallBack) {
		((CheerzApplication) context.getApplicationContext()).execute(method, api, params, cheerzServiceCallBack);
	}

	public static BitmapDrawable roundCornered(BitmapDrawable scaledBitmap, int i) {

		try {
			Bitmap bitmap = scaledBitmap.getBitmap();

			Bitmap result = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
			Canvas canvas = new Canvas(result);

			int color = 0xff424242;
			Paint paint = new Paint();
			Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
			RectF rectF = new RectF(rect);
			int roundPx = i;
			paint.setAntiAlias(true);
			canvas.drawARGB(0, 0, 0, 0);
			paint.setColor(Color.BLUE);
			canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

			paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
			canvas.drawBitmap(bitmap, rect, rect, paint);
			BitmapDrawable finalresult = new BitmapDrawable(result);
			return finalresult;
		} catch (Exception exception) {
			return scaledBitmap;
		}
	}

	public static Bitmap drawableToBitmap(Drawable drawable) {
		if (drawable instanceof BitmapDrawable) {
			return ((BitmapDrawable) drawable).getBitmap();
		}

		Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
		drawable.draw(canvas);

		return bitmap;
	}

	public static final String[] URL_IMG = new String[] { "http://i.imgur.com/m9nYfVe.jpg?1", //
			"http://img.tintuc.vietgiaitri.com/2014/3/31/hinh-gai-dep-co-cap-nguc-di-truoc-thoi-dai-0f5344.jpg",//
			"http://cogiaothaoblog.com/wp-content/uploads/2013/08/562965_582847681765918_389701627_n1.jpg",//
			"http://img.tintuc.vietgiaitri.com/2014/3/31/hinh-gai-dep-co-cap-nguc-di-truoc-thoi-dai-a21437.jpg",//
			"http://img.tintuc.vietgiaitri.com/2014/3/31/hinh-gai-dep-co-cap-nguc-di-truoc-thoi-dai-491a30.jpg",//
			"http://img.tintuc.vietgiaitri.com/2014/3/31/hinh-gai-dep-co-cap-nguc-di-truoc-thoi-dai-501c7b.jpg",//
			"http://img.tintuc.vietgiaitri.com/2014/3/31/hinh-gai-dep-co-cap-nguc-di-truoc-thoi-dai-c751ab.jpg",//
			"http://cogiaothaoblog.com/wp-content/uploads/2013/08/995451_582262391824447_1392930263_n1.jpg",//
			"http://cogiaothaoblog.com/wp-content/uploads/2013/04/1006332_582719521778734_667073631_n1.jpg" };

	public static String randomUrl() {
		return URL_IMG[new Random().nextInt(URL_IMG.length)];
	}

	public static final String BASESERVER = "http://192.168.2.5/anhlx/cheerz/";

	public static final String API_REGISTER = "user/register";

	public static final String API_LOGIN = "user/login";

	public static final String API_EDIT_PROFILE = "user/updateProfile";

	public static final String EMAIL_LOGIN = "account_login"; // email

	public static final String PASS_LOGIN = "pass_login"; // pass

	public static final String TOKEN = "token";

	public static final String AVATAR = "http://www.picgifs.com/clip-art/cartoons/scooby-doo/clip-art-scooby-doo-308243.jpg";

	public static final String PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAgEs5xj9fNBtfCLbdEgn/QAmWAggtB+vy6ZuxJk4VGjTjUh0Ur182nN/Oav7kwkq3T2Y4tJoDkxuiZ111TlJGQyvCdsMJa9wIjb4pfgSScnnrf451qP5OcIau9ii6eA5Whj0/5QNVQZGc1Q7VeScOvhr01qZVnzDcWDcoNUAD0ftVAG3MWuBJ2RjMiOwH1Y9x/Ha99KWOo15isQFELyl/N0qIB/c62TYOuL7pymnCkfe9pm9/Sw+bS4Ph2+0u6fV1UnufNbrIeP34wttxOD1WWhzZip4NrSNed7yC0+y2GgckfoT5xJwLEKKeFso+R5IGmZnv3E+So35301LiBewKSQIDAQAB";

	// public static void saveLogin(Context context, boolean status) {
	// DataStore.getInstance().init(context);
	// DataStore.getInstance().save("login_status", status);
	// }

	public static String getString(JSONObject item, String key) {
		try {
			return item.getString(key);
		} catch (JSONException e) {
			return "";
		}

	}

	public static boolean isBlank(String text) {
		return text == null || text != null && text.trim().equals("");
	}

	/**
	 * check network connected
	 * 
	 * @param ctx
	 * @return True | False
	 */
	public static boolean isNetworkConnected(Context ctx) {
		ConnectivityManager connect = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo infor = connect.getActiveNetworkInfo();
		if (infor != null && infor.isAvailable() && infor.isConnected()) {
			return true;
		} else {
			return false;
		}
	}

	public static void callWeb(Context context, String url) {
		try {
			Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
			context.startActivity(myIntent);
		} catch (ActivityNotFoundException e) {
			// Toast.makeText(context, "No application can handle this request."
			// + " Please install a webbrowser", Toast.LENGTH_LONG).show();
			showDialog(context, context.getString(R.string.weberror));
		}
	}

	public static final void streamView(Context context, String movieurl) {
		try {
			Intent tostart = new Intent(Intent.ACTION_VIEW);
			tostart.setDataAndType(Uri.parse(movieurl), "video/*");
			context.startActivity(tostart);
		} catch (Exception exception) {
			showDialog(context, context.getString(R.string.videoerror));
		}
	}

	public static void showDialog(Context context, String string) {
		Builder builder = new Builder(context);
		builder.setMessage(string);
		builder.setTitle(context.getString(R.string.app_name));
		builder.setPositiveButton(R.string.ok, null);
		builder.show();
	}

	public static String getToken(Context context) {
		String token = "";
		Cursor cursor = context.getContentResolver().query(User.CONTENT_URI, null, String.format("%s = '1'", User.status_login), null, null);
		if (cursor != null) {
			if (cursor.moveToNext()) {
				token = CheerzUtils.getString(cursor, User.token);
			}
			cursor.close();
		}
		return token;

	}

	public static boolean isLogin(Context context) {
		boolean token = false;
		String where = String.format("%s = '1'", User.status_login);
		Cursor cursor = context.getContentResolver().query(User.CONTENT_URI, null, where, null, null);

		if (cursor != null) {
			if (cursor.moveToNext()) {
				token = true;
			}
			cursor.close();
		}
		return token;
	}

	public static String getString(Cursor cursor, String token2) {
		return cursor.getString(cursor.getColumnIndex(token2));
	}

	public static String getUserId(Context activity) {
		String token = "";
		Cursor cursor = activity.getContentResolver().query(User.CONTENT_URI, null, String.format("%s = '1'", User.status_login), null, null);
		if (cursor != null) {
			if (cursor.moveToNext()) {
				token = CheerzUtils.getString(cursor, User.user_id);
			}
			cursor.close();
		}
		return token;
	}

	public static void setText(View findViewById, Cursor cursor, String column) {
		((TextView) findViewById).setText(cursor.getString(cursor.getColumnIndex(column)));
	}

	public static void toast(Context context, String string) {
		Toast.makeText(context, string, Toast.LENGTH_SHORT).show();
	}

	
	public static JSONObject formatJson(JSONObject json){
		json.toString().replace("null", "unKnown");
		return json;
	}



	public static <T extends View> T getView(View view, int searchMain) {
		T t = (T) view.findViewById(searchMain);
		return t;
	}

	public static void showKeyBoard(EditText keEditText) {
		keEditText.requestFocus();
		InputMethodManager imm = (InputMethodManager) keEditText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(keEditText, InputMethodManager.SHOW_IMPLICIT);

	}

	public static void hiddenKeyBoard(Activity activity) {
		try {
			String service = Context.INPUT_METHOD_SERVICE;
			InputMethodManager imm = null;
			imm = (InputMethodManager) activity.getSystemService(service);
			IBinder binder = activity.getCurrentFocus().getWindowToken();
			imm.hideSoftInputFromWindow(binder, 0);
		} catch (Exception e) {
		}
	}

	public static void hiddenKeyBoard(EditText editext) {
		// InputMethodManager imm = (InputMethodManager)
		// editext.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		// imm.showSoftInput(editext, InputMethodManager.HIDE_IMPLICIT_ONLY);
	}

	public static void showView(View search_right, boolean b) {
		if (search_right != null) {
			search_right.setVisibility(b ? View.VISIBLE : View.GONE);
		}
	}
}

