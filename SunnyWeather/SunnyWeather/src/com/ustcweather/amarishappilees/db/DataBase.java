package com.ustcweather.amarishappilees.db;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

public class DataBase {

	public DataBase() {

	}

public void copyDataBase(Context context) {
		
		// 每个应用都有一个数据库目录，他位于 /data/data/yourpackagename/databases/目录下
		String dbName = "ChinaCity.db";
		String dbPath = null;
		dbPath = Environment.getExternalStorageDirectory() + File.separator + dbName;

		if  (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
			return; // 未挂载外部存储，拷贝到内部不用判断
		}

		File dbFile = new File(dbPath);
		if (dbFile.exists()) {
			dbFile.delete();
		}
		try {
			dbFile.createNewFile();
		} catch (IOException e1) {
			e1.printStackTrace();
			return;
		}

		try {
			InputStream is = context.getResources().getAssets().open(dbName);
			OutputStream os = new FileOutputStream(dbPath);

			byte[] buffer = new byte[1024];
			int length = 0;
			while ((length = is.read(buffer)) > 0) {
				os.write(buffer, 0, length);
			}

			os.flush();
			os.close();
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Cursor readDataBaseFromSDCard() {
		String dbPath = Environment.getExternalStorageDirectory() + "/ChinaCity.db";
		
		File dbFile = new File(dbPath);
		if (!dbFile.exists()) {
			return null;
		}
		SQLiteDatabase db = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READWRITE);
		Cursor cursor = db.rawQuery("select * from china_provinces_code", null);
/* 	while (cursor.moveToNext()) {
			int id = cursor.getInt(cursor.getColumnIndex("_id"));  
	        String name = cursor.getString(cursor.getColumnIndex("name"));  
		}
		*/
		return cursor;
	}
}
