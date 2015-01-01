package com.example.marci.topcars.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.example.marci.topcars.data.CarDataContract.CarsEntry;

/**
 * Created by Marci on 17/08/2014.
 */
public class FetchCarDataTask extends AsyncTask<String, Void, Void> {

    private final String LOG_TAG = FetchCarDataTask.class.getSimpleName();
    private final Context mContext;
    private JSONArray carDataJson;

    // Database helper
    private CarDataDbHelper mOpenHelper;

    public FetchCarDataTask(Context context) {

        mContext = context;
        mOpenHelper = new CarDataDbHelper(mContext);
    }

    @Override
    protected Void doInBackground(String... params) {

        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String carDataJsonStr = null;

        try{
            // Build URL for szelesek.hu
            final String CAR_DATA_BASE_URL = "http://szelesek.hu/old/cardgame/source/data/card_game_source.php";

            Uri builtUri = Uri.parse(CAR_DATA_BASE_URL);

            URL url = new URL(builtUri.toString());

            // Create the request to OpenWeatherMap, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }

            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;

            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }

            carDataJsonStr = buffer.toString();

        }catch (IOException e) {

        Log.e(LOG_TAG, "Error ", e);
        // If the code didn't successfully get the weather data, there's no point in attemping
        // to parse it.
        return null;
        }finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }

        try {
            getCarDataFromJson(carDataJsonStr);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }

        // This will only happen if there was an error getting or parsing the data.
        return null;
    }

    private void getCarDataFromJson(String carDataJsonStr) throws JSONException{

        // These are the names of the JSON objects that need to be extracted.
        final String DATA_NAME         = "name";
        final String DATA_IMAGE        = "picture";
        final String DATA_SPEED        = "speed";
        final String DATA_POWER        = "power";
        final String DATA_TORQUE       = "torque";
        final String DATA_ACCELERATION = "acceleration";
        final String DATA_WEIGHT       = "mass";

        JSONArray carDataJson = new JSONArray(carDataJsonStr);

        // Load data into database

        // Gets the data repository in write mode
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        // Loop through each car item in the Json array
        for (int i = 0; i < carDataJson.length(); i++){

            String name;
            String image;
            int speed;
            int power;
            int torque;
            double acceleration;
            int weight;

            JSONObject carDataItemJson = new JSONObject(carDataJson.getString(i));
            name = carDataItemJson.getString(DATA_NAME);
            image = carDataItemJson.getString(DATA_IMAGE);
            speed = carDataItemJson.getInt(DATA_SPEED);
            power = carDataItemJson.getInt(DATA_POWER);
            torque = carDataItemJson.getInt(DATA_TORQUE);
            acceleration = carDataItemJson.getDouble(DATA_ACCELERATION);
            weight = carDataItemJson.getInt(DATA_WEIGHT);


            // Create a new map of values, where column names are the keys
            ContentValues values = new ContentValues();

            values.put(CarsEntry.COLUMN_NAME, name);
            values.put(CarsEntry.COLUMN_IMAGE, image);
            values.put(CarsEntry.COLUMN_SPEED, speed);
            values.put(CarsEntry.COLUMN_POWER, power);
            values.put(CarsEntry.COLUMN_TORQUE, torque);
            values.put(CarsEntry.COLUMN_ACCELERATION, acceleration);
            values.put(CarsEntry.COLUMN_WEIGHT, weight);


            //Log.d(LOG_TAG, carDataItemJson.toString());

            // Insert the new row, returning the primary key value of the new row
            long newRowId;
            newRowId = db.insert(
                    CarsEntry.TABLE_NAME,
                    null,
                    values);
            //Log.d(LOG_TAG, Long.toString(newRowId));
        }

        //Log.d(LOG_TAG, carDataJson.toString());


    }
}
