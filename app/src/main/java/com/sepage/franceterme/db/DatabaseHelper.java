package com.sepage.franceterme.db;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.sepage.franceterme.db.util.DBUtil;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "franceterme.db";
    private static final int DATABASE_VERSION = 1;


    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        executeQueryOnTransactionMode(db, DBUtil.CREATE_DATABASE_SCRIPT+DBUtil.ANALYZE_DATABASE_SCRIPT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DBUtil.DROP_DATABASE_SCRIPT);
        onCreate(db);
    }


    public void executeQueryOnTransactionMode(SQLiteDatabase db, String query) {
        db.beginTransaction();
        try {
            db.execSQL(query);
            db.setTransactionSuccessful();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            db.endTransaction();
        }
    }
}
