package com.neuman.brutus.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.google.gson.JsonObject;

public class OffSyncDbHandler extends SQLiteOpenHelper {
    public OffSyncDbHandler(@Nullable Context context, int version) {
        super(context, "OffSync", null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_TABLE = "CREATE TABLE OffSync (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "request TEXT, " +
                "response TEXT, " +
                "header TEXT, " +
                "type TEXT, " +
                "exec_later INTEGER, " +
                "encryption INTEGER, " +
                "last_attempt DATETIME DEFAULT CURRENT_TIMESTAMP)";
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void pushOffsyncRequest(String request, String response, String headers, String type) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("request", request);
        contentValues.put("response", response);
        contentValues.put("header", headers);
        contentValues.put("type", type);

        sqLiteDatabase.insert("OffSync", null, contentValues);
        sqLiteDatabase.close();
    }

    public void pullOffsyncRequest() {

    }
}
