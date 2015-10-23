package com.acv.cheerz.db;

import java.util.HashMap;
import java.util.Map;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class DBProvider extends ContentProvider {
	public static final String PROVIDER_NAME = "com.acv.cheerz.db.DBProvider";

	private void openDB() {
		// closeDB();
		db = new DBDatabaseHelper(getContext()).getWritableDatabase();
	}

	private void closeDB() {
		if (db != null) {
			db.close();
		}
	}

	public DBProvider() {
		super();
	}

	private UriMatcher uriMatcher;

	private SQLiteDatabase db;

	@Override
	public boolean onCreate() {
		db = new DBDatabaseHelper(getContext()).getWritableDatabase();

		if (uriMatcher == null) {
			uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
			IDOL.addUriMatcher(uriMatcher, PROVIDER_NAME);
			User.addUriMatcher(uriMatcher, PROVIDER_NAME);
		}

		return (db == null) ? false : true;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// openDB();
		int match = uriMatcher.match(uri);
		Uri _uri = IDOL.insert(match, db, uri, values);

		if (_uri == null) {
			_uri = User.insert(match, db, uri, values);
		}
		if (_uri != null) {
			getContext().getContentResolver().notifyChange(_uri, null);
		}
		return _uri;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		// openDB();
		int match = uriMatcher.match(uri);
		Cursor c = IDOL.query(match, db, uri, projection, selection, selectionArgs, sortOrder);

		if (c == null) {
			c = User.query(match, db, uri, projection, selection, selectionArgs, sortOrder);
		}
		if (c != null) {
			c.setNotificationUri(getContext().getContentResolver(), uri);
		}

		if (c == null || match == UriMatcher.NO_MATCH) {
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

		return c;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// openDB();
		int count = 0;
		int match = uriMatcher.match(uri);

		count = IDOL.delete(match, db, uri, selection, selectionArgs);
		// count == -2 delete other
		if (count == -2) {
			count = User.delete(match, db, uri, selection, selectionArgs);
		}

		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		// openDB();
		int count = 0;

		int match = uriMatcher.match(uri);

		count = IDOL.update(match, db, uri, values, selection, selectionArgs);
		// count == -2 update other
		if (count == -2) {
			count = User.update(match, db, uri, values, selection, selectionArgs);
		}

		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

	@Override
	public String getType(Uri uri) {
		Map<Integer, String> mMap = new HashMap<Integer, String>();
		IDOL.getType(mMap);
		User.getType(mMap);
		String type = mMap.get(uriMatcher.match(uri));

		if (type == null) {
			throw new IllegalArgumentException("Unsupported URI: " + uri);
		} else {
			return type;
		}
	}
}