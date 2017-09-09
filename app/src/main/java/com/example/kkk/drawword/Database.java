package com.example.kkk.drawword;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by KKK on 2017-09-09.
 */

public class Database extends SQLiteOpenHelper {


    public Database(Context context, String user_db, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, user_db, factory, version);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table user_token(" +
                "iden INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "server_iden TEXT, " +
                "name TEXT," +
                "server_token TEXT);");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w("0000", "Upgrading db from version" + oldVersion + " to" +
                newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS data");
        onCreate(db);
    }
    public void insert(String query){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(query);
        db.close();
    }
    public void delete(String query){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(query);
        db.close();
    }
    public String show_id(String query, int position,int cur){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(query,null);
        cursor.moveToPosition(position);
        String str = cursor.getString(cur);
        return str;
    }
    public int show_column(String query, int position){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(query,null);
        cursor.moveToPosition(position);
        int iden = cursor.getInt(0);
        return iden;
    }
    public String show_last(String query){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(query,null);
        cursor.moveToLast();
        String str = cursor.getString(1);
        return str;
    }
    public String show_alarm(String query ,int cur){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(query,null);
        cursor.moveToLast();
        String str = cursor.getString(cur);
        return str;
    }
    public void showdata(String query){
        SQLiteDatabase db = getReadableDatabase();
        db.execSQL(query);
        db.close();
    }
    public int count(String query){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        int cnt = cursor.getCount();
        Log.d("count", String.valueOf(cnt));
        cursor.close();
        return cnt;
    }


}
