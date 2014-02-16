package com.sepage.franceterme.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.sepage.franceterme.db.util.SQLUtil;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "franceterme.db";
    private static final String DATABASE_PATH = "/data/data/com.sepage.fr/databases/";
    private static final String SQL_INSERT_SCRIPTS_LOCAL_PATH = "resources/franceterme.inserts.sql";
    private static final String FULL_DATABASE_PATH = DATABASE_PATH + DATABASE_NAME;
    private static final int DATABASE_VERSION = 1;
    private SQLiteDatabase database;
    private final Context context;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("Database Intialization", "trying to use local file");
        boolean successInUsingLocalDatabase = migrateDatabaseFromLocalFile(); // try to use local database file
        if (!successInUsingLocalDatabase) {             // using local database file failed, will use insert scripts now
            Log.d("Database Intialization", "COULD NOT USE LOCAL DATABASE FILE, NOW RUNNING BATCH INSERT SCRIPTS IN SEPARATE THREAD");
            executeLargeQueryOnTransactionMode(db, SQLUtil.CREATE_ALLTABLES_SCRIPT);
            initializeSQLTablesAndInsertAllOnSeparateThread(db, SQL_INSERT_SCRIPTS_LOCAL_PATH);
            db.execSQL(SQLUtil.ANALYZE_DATABASE_SCRIPT);
        }
        database = db;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQLUtil.DROP_ALLTABLES_SCRIPT);
        onCreate(db);
    }

    /**
     * Creates a empty database on the system and rewrites it with your own database.
     */
    public boolean migrateDatabaseFromLocalFile() {

        if (checkIfDatabaseExists()) {
            return true;        // meh no need
        } else {

            //By calling this method and empty database will be created into the default system path
            //of your application so we are gonna be able to overwrite that database with our database.
            this.getReadableDatabase();

            try {
                copyDatabaseFromLocalFileToSystem();
            } catch (IOException e) {
                Log.d("Database Initialization", "Could not copy local database file to system path");
                return false;
            }
            return true;
        }
    }

    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     *
     * @return true if it exists, false if it doesn't
     */
    private boolean checkIfDatabaseExists() {

        SQLiteDatabase checkDB = null;
        try {
            checkDB = SQLiteDatabase.openDatabase(FULL_DATABASE_PATH, null, SQLiteDatabase.OPEN_READONLY);

        } catch (SQLiteException e) {
            Log.d("Database Initialization", "SQL Database " + DATABASE_NAME + " doesn't exist yet. Will create.");
        }

        if (checkDB != null) {
            checkDB.close();
        }

        return checkDB != null ? true : false;
    }

    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transfering bytestream.
     */
    private void copyDatabaseFromLocalFileToSystem() throws IOException {

        //Open your local db as the input stream
        InputStream input = context.getAssets().open(DATABASE_NAME);

        //Open the empty db as the output stream
        OutputStream output = new FileOutputStream(FULL_DATABASE_PATH);

        //transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = input.read(buffer)) > 0) {
            output.write(buffer, 0, length);
        }

        //Close the streams
        output.flush();
        output.close();
        input.close();
    }

    public void openDatabase() throws SQLException {
        //Open the database
        String myPath = DATABASE_PATH + DATABASE_NAME;
        database = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
    }

    @Override
    public synchronized void close() {
        if (database != null) {
            database.close();
        }
        super.close();
    }


    public void executeLargeQueryOnTransactionMode(SQLiteDatabase db, String largeQuery) {
        db.beginTransaction();
        try {
            db.execSQL("PRAGMA foreign_keys = ON;");
            db.execSQL(largeQuery);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    public void initializeSQLTablesAndInsertAllOnSeparateThread(SQLiteDatabase database, String sqlScriptFilePath) {

        final SQLiteDatabase db = database;
        final String dbScriptPath = sqlScriptFilePath;
        new Thread(new Runnable() {

            public void run() {

                db.beginTransaction();

                try {
                    BufferedReader reader = new BufferedReader(new FileReader(dbScriptPath));

                    String script;
                    while ((script = reader.readLine()) != null) {
                        db.execSQL(script);
                    }
                    reader.close();

                    Log.i("SQL BATCH INSERT", "Inserts completed successfully");


                } catch (IOException e) {
                    e.printStackTrace();
                    db.endTransaction();
                }
                db.setTransactionSuccessful();
                db.endTransaction();
            }
        }).run();
    }

    public Cursor findTermByID(String id) {
        return database.rawQuery(SQLUtil.getSELECTQueryForTermById(), new String[] {id});
    }

    public Cursor executeRawQuery(String query, String[] args) {
        return database.rawQuery(query,args);
    }
}
