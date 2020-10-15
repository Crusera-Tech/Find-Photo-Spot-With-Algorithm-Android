package com.example.findit;

import android.content.Context;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class DbHelper extends SQLiteAssetHelper{
    private static final String DATABASE_NAME = "all_db_spot.db";
    private static final int DATABASE_VERSION = 1;

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

}
