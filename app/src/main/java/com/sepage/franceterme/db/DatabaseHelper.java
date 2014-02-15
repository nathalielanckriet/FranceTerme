package com.sepage.franceterme.db;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.sepage.franceterme.db.util.SQLUtil;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "franceterme.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        executeQueryOnTransactionMode(db, SQLUtil.CREATE_DATABASE_SCRIPT + SQLUtil.ANALYZE_DATABASE_SCRIPT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQLUtil.DROP_DATABASE_SCRIPT);
        onCreate(db);
    }


    public void executeQueryOnTransactionMode(SQLiteDatabase db, String query) {
        db.beginTransaction();
        try {
            db.execSQL("PRAGMA foreign_keys = ON;");
            db.execSQL(query);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    public void initializeSQLTablesAndInsertAllOnSeparateThread(SQLiteDatabase database) {

        final SQLiteDatabase db = database;
        Thread downloadThread = new Thread(new Runnable() {

            public void run() {

                db.beginTransaction();

//                try {
//                    InputStream input = url.openStream();
//                    CSVReader reader = new CSVReader(new InputStreamReader(input));
//
//                    String [] sched;
//                    while ((sched = reader.readNext()) != null) {
//                        if(sched[INDEX_CITY].equals("")) sched[INDEX_CITY]="OTHERS";
//                        try {
//
//                            db.addRow(sched[INDEX_SIN], sched[INDEX_CITY],
//                                    sched[INDEX_START_DATE], sched[INDEX_START_TIME],
//                                    sched[INDEX_END_DATE], sched[INDEX_END_TIME],
//                                    sched[INDEX_DETAILS], sched[INDEX_REASON]);
//                        } catch (IndexOutOfBoundsException e) {
//                            db.addRow(sched[INDEX_SIN], sched[INDEX_CITY],
//                                    sched[INDEX_START_DATE], sched[INDEX_START_TIME],
//                                    sched[INDEX_END_DATE], sched[INDEX_END_TIME],
//                                    "", sched[INDEX_REASON]);
//                            //e.printStackTrace();
//                        }
//                    }
//                    input.close();
//                    Log.i("dl", "finished");
//
//                } catch (MalformedURLException e) {
//                    e.printStackTrace();
//                    db.endTransaction();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    db.endTransaction();
//                }
//                Log.d("Count", ""+db.count());
//                db.setTransactionSuccessful();
//                db.endTransaction();
//
//                writeUploadDateInTextFile();
//
//            }

            }


        });
    }
}
