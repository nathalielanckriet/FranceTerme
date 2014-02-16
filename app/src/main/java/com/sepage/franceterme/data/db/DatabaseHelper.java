package com.sepage.franceterme.data.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.sepage.franceterme.data.DataPool;
import com.sepage.franceterme.data.db.util.SQLUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "franceterme.db";
    private static String SQL_INSERT_SCRIPTS_LOCAL_PATH = "franceterme.inserts.sql";
    private static String FULL_DATABASE_PATH;
    private static final int DATABASE_VERSION = 1;
    private SQLiteDatabase database;
    private final Context context;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;

        if (android.os.Build.VERSION.SDK_INT >= 17) {
            FULL_DATABASE_PATH = context.getApplicationInfo().dataDir + "/databases/" + File.separator + DATABASE_NAME;
            SQL_INSERT_SCRIPTS_LOCAL_PATH = context.getApplicationInfo().dataDir + "/databases/" + File.separator + SQL_INSERT_SCRIPTS_LOCAL_PATH;
        } else {
            FULL_DATABASE_PATH = context.getFilesDir().getAbsolutePath().replace("files", "databases") + File.separator + DATABASE_NAME;
            SQL_INSERT_SCRIPTS_LOCAL_PATH = context.getFilesDir().getAbsolutePath().replace("files", "databases") + File.separator + SQL_INSERT_SCRIPTS_LOCAL_PATH;
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("Database Initialization", "trying to use local file");
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
    public void setupDatabase() {

        if (checkIfDatabaseExists()) {    // check if database exists, if not copy local file
            Log.d("Database Initialization", "Setting up database, Database exists");
        } else {            // create database and populate
            Log.d("Database Initialization", "Database doesn't exist. Attempting to copy local database file to system");

            this.getWritableDatabase();     // creates an empty database that we will later write over
            this.close();
            try {
                copyDatabaseFromLocalFileToSystem();        // copy from local file
                openDatabase();
            } catch (IOException e) {
                Log.d("Database Initialization", "COULD NOT USE LOCAL DATABASE FILE, NOW RUNNING BATCH INSERT SCRIPTS IN SEPARATE THREAD");
            } catch (SQLException e) {
                Log.d("Database Initialization", "Opening new database Failed");
            }
        }

        try {
            openDatabase();
        } catch (SQLException e) {
            Log.d("Database Initialization", "Could not open database");
        }

        if (!checkIfDatabaseHasData()) {        // check if theres data!
            Log.d("Database Initialization", "DATABASE doesnt have data");
            executeLargeQueryOnTransactionMode(database, SQLUtil.DROP_ALLTABLES_SCRIPT);      // drop tables
            executeLargeQueryOnTransactionMode(database, SQLUtil.CREATE_ALLTABLES_SCRIPT);      // create tables
            initializeSQLTablesAndInsertAllOnSeparateThread(SQL_INSERT_SCRIPTS_LOCAL_PATH);   // insert all data (will take a while)
            Log.d("Database Initialization", "WRITING DATA TO DATABASE SUCCESSFUL!!");
        }
    }


    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     *
     * @return true if it exists, false if it doesn't
     */
    public boolean checkIfDatabaseExists() {
//        SQLiteDatabase database = null;
//        try {
//            Log.d("Database Initialization", "Attempting to open database on " + FULL_DATABASE_PATH);
//            database = SQLiteDatabase.openDatabase(FULL_DATABASE_PATH, null, SQLiteDatabase.OPEN_READONLY); // important that it opens a second database connection
//        } catch (SQLiteException e) {
//            Log.d("Database Initialization", "SQL Database " + DATABASE_NAME + " doesn't exist yet.");
//        }
//        if (database != null) {    // if database object is null, the database doesnt exist
//            database.close();
//            return true;
//        }
//        return false;
        Log.d("Database Initialization", "Attempting to open database on " + FULL_DATABASE_PATH);
        File dbFile = new File(FULL_DATABASE_PATH);
        //Log.v("dbFile", dbFile + "   "+ dbFile.exists());
        return dbFile.exists();
    }

    public boolean checkIfDatabaseHasData() {
        boolean exists = false;
        Log.d("Database Initialization", "checking if database has data");
        Cursor result = null;
        if (database != null) {    // if database object is null, the database doesnt exist
            try {
                result = database.rawQuery("SELECT COUNT(_id) AS countid FROM TERM", null);      // check to see if anything exists in the Term table
                exists = result.moveToFirst();
                Log.d("Database Initialization", "does query have an answer? :" + exists);
                if (exists) {
                    exists = result.getInt(0) > 1;
                    Log.d("Database Initialization", "number of id rows in Term table returned: " + result.getInt(0));
                }
            } catch (Exception e) {
                Log.d("Database Initialization", "data check query failed: must reset data");
                exists = false;
            } finally {
                if (result != null) {
                    result.close();
                }
            }

        }
        // dont close database
        return exists;
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
        database = SQLiteDatabase.openDatabase(FULL_DATABASE_PATH, null, SQLiteDatabase.OPEN_READWRITE);
        DataPool.setDatabaseHelper(this);
        Log.d("Database Initialization", "Opened Read/Write database");
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
            db.execSQL(largeQuery);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    public void initializeSQLTablesAndInsertAllOnSeparateThread(String sqlScriptFilePath) {

        final SQLiteDatabase db = SQLiteDatabase.openDatabase(FULL_DATABASE_PATH, null, SQLiteDatabase.OPEN_READWRITE);
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
                    Log.i("Database Initialization", "Database Inserts completed successfully");

                } catch (IOException e) {
                    e.printStackTrace();
                    db.endTransaction();
                }
                db.setTransactionSuccessful();
                db.endTransaction();
                db.close();
            }
        }).run();
    }

    public Cursor findTermByID(String id) {
        return database.rawQuery(SQLUtil.getSELECTQueryForTermById(), new String[]{id});
    }

    public Cursor executeRawQuery(String query, String[] args) {
        return database.rawQuery(query, args);
    }
}
