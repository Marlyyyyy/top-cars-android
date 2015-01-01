package com.example.marci.topcars;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import com.example.marci.topcars.data.CarDataContract;
import com.example.marci.topcars.data.CarDataDbHelper;

import java.io.File;

public class MainActivity extends ActionBarActivity {

    private CarDataDbHelper mCarDataDbHelper;
    private static final String LOG_TAG = "Top Cars app";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCarDataDbHelper = new CarDataDbHelper(this);

        // Check if cars database exists
        if(!checkIfDatabaseExists(this, mCarDataDbHelper.DATABASE_NAME)){
            Log.d(LOG_TAG, "MainActivity onCreate - Database doesn't exist yet, now creating...");
            mCarDataDbHelper.updateCarsBase();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }else if(id == R.id.action_drop_database){
            this.deleteDatabase(mCarDataDbHelper.DATABASE_NAME);
        }else if(id == R.id.action_create_database){
            mCarDataDbHelper.updateCarsBase();
        }
        return super.onOptionsItemSelected(item);
    }

    private static boolean checkIfDatabaseExists(Context context, String dbName){
    // Check if database exists
        File dbFile = context.getDatabasePath(dbName);
        return dbFile.exists();
    }

    public void buttonClick(View view){

        int viewId = view.getId();

        switch (viewId){
            case R.id.launch_game:
                launchActivity(view, GameActivity.class);
                break;
            case R.id.launch_cars:
                launchActivity(view, CarsActivity.class);
                break;
            case R.id.launch_stats:
                break;
        }
    }

    public void launchActivity(View clickedView, Class activityClass){

        final Class activity = activityClass;
        final Context context = this;
        AlphaAnimation animation = new AlphaAnimation(1.0f, 0f);
        animation.setDuration(200);
        animation.setFillAfter(false);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Intent intent = new Intent(context, activity);
                startActivity(intent);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        clickedView.startAnimation(animation);
    }
}
