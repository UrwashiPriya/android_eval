package com.neuman.brutus.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class OffSyncDbHandler extends SQLiteOpenHelper {
    private String table = "OffSync";

    public OffSyncDbHandler(@Nullable Context context, int version) {
        super(context, "OffSync", null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_TABLE = "CREATE TABLE "+table+" (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "request TEXT, " +
                "response TEXT, " +
                "header TEXT, " +
                "type TEXT, " +
                "code TEXT, " +
                "account TEXT, " +
                "exec_later INTEGER, " +
                "encryption INTEGER, " +
                "last_attempt DATETIME DEFAULT CURRENT_TIMESTAMP)";
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { }

    public void pushOffSyncRequest(String request, String response, String headers, String type, Integer exec_later, Integer encryption, Integer max_reqs) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        if (max_reqs==null || (getOffSyncReqCount(request)<max_reqs)) {
            ContentValues contentValues = new ContentValues();

            System.out.println("1. " + request + " " + max_reqs);

            contentValues.put("request", request);
            contentValues.put("response", response);
            contentValues.put("header", headers);
            contentValues.put("type", type);
            contentValues.put("exec_later", exec_later);
            contentValues.put("encryption", encryption);

            long x = sqLiteDatabase.insert("OffSync", null, contentValues);
            System.out.println("RPWWWWWWWWW "+x);
        } else {
            sqLiteDatabase.rawQuery("UPDATE "+table+" SET response='"+response+"' WHERE request='"+request+"' AND id IN (SELECT MIN(id) FROM " + table + " WHERE request='"+request+"')", null);
        }

        sqLiteDatabase.close();
    }

    public void pushOffSyncRequest(String request, String response, String code, String account, String headers, String type, Integer exec_later, Integer encryption, Integer max_reqs) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        if (max_reqs==null || (getOffSyncReqCount(request)<max_reqs)) {
            ContentValues contentValues = new ContentValues();

            System.out.println("1. " + request + " " + max_reqs);

            contentValues.put("request", request);
            contentValues.put("response", response);
            contentValues.put("header", headers);
            contentValues.put("type", type);
            contentValues.put("code", code);
            contentValues.put("account", account);
            contentValues.put("exec_later", exec_later);
            contentValues.put("encryption", encryption);

            long x = sqLiteDatabase.insert("OffSync", null, contentValues);
            System.out.println("RPWWWWWWWWW "+x);
        } else {
            sqLiteDatabase.rawQuery("UPDATE "+table+" SET response='"+response+"' WHERE request='"+request+"' AND id IN (SELECT MIN(id) FROM " + table + " WHERE request='"+request+"')", null);
        }

        sqLiteDatabase.close();
    }

    public String readOffSyncRequest(String request) {
        String response = null;
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM "+table+" WHERE request='"+request+"'", null);
        while (cursor.moveToNext()) {
            response = cursor.getString(cursor.getColumnIndex("response"));
        }
        cursor.close();
        return response;
    }

    private Integer getOffSyncReqCount(String request) {
        Integer count = 0;
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM "+table+" WHERE request='"+request+"'", null);
        while (cursor.moveToNext()) { count++; }
        cursor.close();
        return count;
    }
}
