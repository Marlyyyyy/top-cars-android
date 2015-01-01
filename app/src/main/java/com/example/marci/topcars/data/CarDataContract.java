package com.example.marci.topcars.data;

import android.provider.BaseColumns;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Marci on 18/08/2014.
 */
public class CarDataContract {

    private static final String LOG_TAG = "Top Cars app";

    // Specify the columns we need.
    private static final String[] CARS_COLUMNS = {
            CarsEntry.TABLE_NAME + "." + CarsEntry._ID,
            CarsEntry.COLUMN_NAME,
            CarsEntry.COLUMN_IMAGE,
            CarsEntry.COLUMN_SPEED,
            CarsEntry.COLUMN_POWER,
            CarsEntry.COLUMN_TORQUE,
            CarsEntry.COLUMN_ACCELERATION,
            CarsEntry.COLUMN_WEIGHT
    };

    // These indices are tied to CARS_COLUMNS.
    public static final int COL_CARS_ID     = 0;
    public static final int COL_CARS_NAME   = 1;
    public static final int COL_CARS_IMAGE  = 2;
    public static final int COL_CARS_SPEED  = 3;
    public static final int COL_CARS_POWER  = 4;
    public static final int COL_CARS_TORQUE = 5;
    public static final int COL_CARS_ACCELERATION = 6;
    public static final int COL_CARS_WEIGHT = 7;

    public static final int COL_USERPROGRESS_ID     = 0;
    public static final int COL_USERPROGRESS_DATE   = 1;
    public static final int COL_USERPROGRESS_NAME  = 2;
    public static final int COL_USERPROGRESS_SCORE  = 3;
    public static final int COL_USERPROGRESS_STREAK = 4;


    // Format used for storing dates in the database.  ALso used for converting those strings
    // back into date objects for comparison/processing.
    public static final String DATE_FORMAT = "yyyyMMdd";

    /**
     * Converts unix time to a string representation, used for easy comparison and database lookup.
     * @param date The input date
     * @return a DB-friendly representation of the date, using the format defined in DATE_FORMAT.
     */
    public static String getDbDateString(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        return sdf.format(date);
    }

    /**
     * Converts a dateText to a long Unix time representation
     * @param dateText the input date string
     * @return the Date object
     */
    public static Date getDateFromDb(String dateText) {
        SimpleDateFormat dbDateFormat = new SimpleDateFormat(DATE_FORMAT);
        try {
            return dbDateFormat.parse(dateText);
        } catch ( ParseException e ) {
            e.printStackTrace();
            return null;
        }
    }

    public static final class CarsEntry implements BaseColumns{

        public static final String TABLE_NAME = "cars";

        public static final String COLUMN_NAME      = "name";
        public static final String COLUMN_IMAGE     = "image";
        public static final String COLUMN_SPEED     = "speed";
        public static final String COLUMN_POWER     = "power";
        public static final String COLUMN_TORQUE    = "torque";
        public static final String COLUMN_ACCELERATION = "acceleration";
        public static final String COLUMN_WEIGHT    = "weight";
    }

    public static final class UserProgressEntry implements BaseColumns{

        public static final String TABLE_NAME = "user_progress";

        public static final String COLUMN_DATE      = "date";
        public static final String COLUMN_NAME      = "name";
        public static final String COLUMN_SCORE     = "score";
        public static final String COLUMN_STREAK    = "streak";
    }
}
