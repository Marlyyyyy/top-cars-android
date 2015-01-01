package com.example.marci.topcars.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.marci.topcars.data.CarDataContract.CarsEntry;
import com.example.marci.topcars.data.CarDataContract.UserProgressEntry;

/**
 * Created by Marci on 18/08/2014.
 */
public class CarDataDbHelper extends SQLiteOpenHelper {

    private static final String LOG_TAG = "Top Cars app";

    // if you change the database scheme, you should increment the Version number
    private static final int DATABASE_VERSION = 5;
    public static final String DATABASE_NAME = "topcars.db";

    private Context mContext;

    public CarDataDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_CARS_TABLE =
                "CREATE TABLE " + CarsEntry.TABLE_NAME + " (" +
                        CarsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +

                        CarsEntry.COLUMN_NAME         + " TEXT NOT NULL, "    +
                        CarsEntry.COLUMN_IMAGE        + " TEXT NOT NULL, "    +
                        CarsEntry.COLUMN_SPEED        + " INTEGER NOT NULL, " +
                        CarsEntry.COLUMN_POWER        + " INTEGER NOT NULL, " +
                        CarsEntry.COLUMN_TORQUE       + " INTEGER NOT NULL, " +
                        CarsEntry.COLUMN_ACCELERATION + " REAL NOT NULL, "    +
                        CarsEntry.COLUMN_WEIGHT       + " INTEGER NOT NULL, " +

                        // to assure the app has just one image per card
                        // create UNIQUE constraint with REPLACE strategy
                        " UNIQUE (" + CarsEntry.COLUMN_IMAGE + ") ON CONFLICT REPLACE);";

        final String SQL_CREATE_HIGHSCORE_TABLE =
                "CREATE TABLE " + UserProgressEntry.TABLE_NAME + " (" +
                        UserProgressEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +

                        UserProgressEntry.COLUMN_DATE         + " DATETIME DEFAULT CURRENT_TIMESTAMP, "    +
                        UserProgressEntry.COLUMN_NAME         + " TEXT DEFAULT 'Player 1', " +
                        UserProgressEntry.COLUMN_SCORE        + " INTEGER NOT NULL DEFAULT 0, "    +
                        UserProgressEntry.COLUMN_STREAK       + " INTEGER NOT NULL DEFAULT 0, "    +

                        // for now only storing one entry per user
                        " UNIQUE (" + UserProgressEntry.COLUMN_NAME + ") ON CONFLICT REPLACE);";

        sqLiteDatabase.execSQL(SQL_CREATE_CARS_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_HIGHSCORE_TABLE);

        Log.d(LOG_TAG, "CarDataDbHelper onCreate was called!");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + CarsEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public void updateCarsBase(){
        FetchCarDataTask fetchCarDataTask = new FetchCarDataTask(mContext);
        fetchCarDataTask.execute("test");
    }

    private void createUserProgressBase(){

        SQLiteDatabase db = new CarDataDbHelper(mContext).getWritableDatabase();

        // Create a new map of default values
        ContentValues values = new ContentValues();

        values.put(UserProgressEntry.COLUMN_NAME, "Player 1");
        values.put(UserProgressEntry.COLUMN_SCORE, 0);
        values.put(UserProgressEntry.COLUMN_STREAK, 0);

        db.insert(
             UserProgressEntry.TABLE_NAME,
             null,
             values
        );
    }

    public Cursor getCarsBase(){

        SQLiteDatabase db = new CarDataDbHelper(mContext).getReadableDatabase();

        // Sort order:  Ascending, by name.
        String sortOrder = CarsEntry.COLUMN_NAME + " ASC";

        // A cursor is your primary interface to the query results.
        Cursor carsCursor = db.query(
                CarsEntry.TABLE_NAME,  // Table to Query
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null, // columns to group by
                null, // columns to filter by row groups
                sortOrder  // sort order
        );

        // If there was no data in the database, then fetch and re-query
        if (!carsCursor.moveToNext()) {
            updateCarsBase();
            getCarsBase();
        }

        return carsCursor;
    }

    public Cursor getUserProgressBase(){
        SQLiteDatabase db = new CarDataDbHelper(mContext).getReadableDatabase();

        // Sort order:  Ascending, by name.
        String sortOrder = UserProgressEntry.COLUMN_SCORE + " DESC";

        // A cursor is your primary interface to the query results.
        Cursor userProgressCursor = db.query(
                UserProgressEntry.TABLE_NAME,  // Table to Query
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null, // columns to group by
                null, // columns to filter by row groups
                sortOrder  // sort order
        );

        Log.d(LOG_TAG, "CarDataDbHelper getUserProgressBase size of cursor: " + userProgressCursor.getCount());

        // If there was no data in the database, then fetch and re-query
        if (!userProgressCursor.moveToNext()) {
            createUserProgressBase();
            return getUserProgressBase();
        }
        return userProgressCursor;
    }

    public void uploadUserProgress(int score, int streak){

        // Gets the data repository in write mode
        SQLiteDatabase db = this.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();

        values.put(UserProgressEntry.COLUMN_NAME, "Player 1");
        values.put(UserProgressEntry.COLUMN_SCORE, score);
        values.put(UserProgressEntry.COLUMN_STREAK, streak);

        // Insert the new row, returning the primary key value of the new row
        long newRowId;
        newRowId = db.insert(
                    UserProgressEntry.TABLE_NAME,
                    null,
                    values);
    }
}
