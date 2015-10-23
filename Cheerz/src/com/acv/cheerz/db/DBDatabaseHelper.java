package com.acv.cheerz.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBDatabaseHelper extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "midb";

	private static final int DATABASE_VERSION = 2;

	public DBDatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(IDOL.CREATE_DB_TABLE());
		db.execSQL(User.CREATE_DB_TABLE());
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + IDOL.USER_TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + User.USER_TABLE_NAME);
		onCreate(db);
	}
}