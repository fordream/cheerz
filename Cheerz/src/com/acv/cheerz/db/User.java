package com.acv.cheerz.db;

import java.util.Map;

import z.lib.base.LogUtils;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import com.acv.cheerz.util.CheerzUtils;

public class User {
	public static final String USER_TABLE_NAME = "users";

	public static final String _ID = "_id";
	public static final String user_id = "user_id";
	public static final String token = "token";
	public static final String avatar = "avatar";
	public static final String name = "name";
	public static final String email = "email";
	public static final String time_countdown = "time_countdown";

	public static final String type_1 = "type_1";
	public static final String type_2 = "type_2";
	public static final String type_3 = "type_3";
	public static final String type_4 = "type_4";

	/*
	 * remember_login 1, remember
	 */
	public static final String remember_login = "remember_login";
	/**
	 * status_login = 1 --> login status_login= 0 --> unlogin
	 */
	public static final String status_login = "status_login";

	public static final String CREATE_DB_TABLE() {
		StringBuilder builder = new StringBuilder();
		builder.append("CREATE TABLE ").append(USER_TABLE_NAME);
		builder.append("(");
		builder.append(_ID).append(" INTEGER PRIMARY KEY AUTOINCREMENT").append(",");
		String[] colums = new String[] {//
		user_id, avatar, name, email, status_login, time_countdown, token, remember_login, type_4, type_3, type_2, type_1 };
		for (int i = 0; i < colums.length; i++) {
			if (i < colums.length - 1) {
				builder.append(colums[i]).append(" TEXT  ").append(",");
			} else {
				builder.append(colums[i]).append(" TEXT  ");
			}
		}
		builder.append(")");

		return builder.toString();

	}

	/**
	 * 
	 * 
	 */

	public static final String URL = "content://" + DBProvider.PROVIDER_NAME + "/" + USER_TABLE_NAME;
	public static final Uri CONTENT_URI = Uri.parse(URL);

	public User() {
	}

	// matcher
	public static final int USER_MATCHER = 3;
	public static final int USER_MATCHER_ID = 4;

	public static final void addUriMatcher(UriMatcher uriMatcher, String PROVIDER_NAME) {
		uriMatcher.addURI(PROVIDER_NAME, USER_TABLE_NAME, USER_MATCHER);
		uriMatcher.addURI(PROVIDER_NAME, USER_TABLE_NAME + "/#", USER_MATCHER_ID);
	}

	public static final void getType(Map<Integer, String> mMap) {
		mMap.put(USER_MATCHER, "vnd.android.cursor.dir/com.acv.cheerz");
		mMap.put(USER_MATCHER_ID, "vnd.android.cursor.item/com.acv.cheerz");
	}

	public static final int update(int match, SQLiteDatabase db, Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		if (USER_MATCHER == match) {
			return db.update(USER_TABLE_NAME, values, selection, selectionArgs);
		} else if (USER_MATCHER_ID == match) {
			return db.update(USER_TABLE_NAME, values, _ID + " = " + uri.getPathSegments().get(1) + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""), selectionArgs);
		} else {
			return -2;
		}
	}

	public static int delete(int match, SQLiteDatabase db, Uri uri, String selection, String[] selectionArgs) {
		if (USER_MATCHER == match) {
			return db.delete(USER_TABLE_NAME, selection, selectionArgs);
		} else if (USER_MATCHER_ID == match) {
			String id = uri.getPathSegments().get(1);
			return db.delete(USER_TABLE_NAME, _ID + " = " + id + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""), selectionArgs);
		} else {
			return -2;
		}
	}

	public static Cursor query(int match, SQLiteDatabase db, Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		qb.setTables(USER_TABLE_NAME);
		if (USER_MATCHER == match) {
			return qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);
		} else if (USER_MATCHER_ID == match) {
			qb.appendWhere(_ID + "=" + uri.getPathSegments().get(1));
			return qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);
		} else {
			return null;
		}
	}

	public static Uri insert(int match, SQLiteDatabase db, Uri uri, ContentValues values) {

		if (USER_MATCHER == match) {

			// if (!isInsert) {
			// db.update(selection, values, selection, null);
			// } else {
			long rowID = db.insert(USER_TABLE_NAME, "", values);

			LogUtils.e("den day", "hi " + rowID);
			if (rowID > 0) {
				Uri _uri = ContentUris.withAppendedId(User.CONTENT_URI, rowID);
				return _uri;
			}
			// }
		} else if (USER_MATCHER_ID == match) {
			// if (!isInsert) {
			// db.update(selection, values, selection, null);
			// } else {
			long rowID = db.insert(USER_TABLE_NAME, "", values);
			if (rowID > 0) {
				Uri _uri = ContentUris.withAppendedId(User.CONTENT_URI, rowID);
				return _uri;
			}
			// }
		}

		return null;
	}

	public static boolean getSetting(Context context, int position) {

		String token = "";
		Cursor cursor = context.getContentResolver().query(User.CONTENT_URI, null, String.format("%s = '1'", User.status_login), null, null);
		if (cursor != null) {
			if (cursor.moveToNext()) {
				String[] lines = new String[] { User.type_1, User.type_2, User.type_3, User.type_4 };
				token = CheerzUtils.getString(cursor, lines[position]);
			}
			cursor.close();
		}
		return "1".equals(token);
	}

	public static void setUnLogin(Context context) {
		ContentValues contentValues = new ContentValues();
		contentValues.put(User.status_login, "0");
		context.getContentResolver().update(User.CONTENT_URI, contentValues, null, null);
	}
}
