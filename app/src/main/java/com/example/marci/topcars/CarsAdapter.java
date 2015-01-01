package com.example.marci.topcars;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.marci.topcars.data.CarDataContract;

/**
 * Created by Marci on 18/08/2014.
 */
public class CarsAdapter extends CursorAdapter {

    public CarsAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {

        int layoutId = R.layout.listview_cars;

        View view = LayoutInflater.from(context).inflate(layoutId, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
    // Populate the UI

        ViewHolder viewHolder = (ViewHolder) view.getTag();

        // Read name from cursor and update view
        String nameString = cursor.getString(CarDataContract.COL_CARS_NAME);
        viewHolder.nameView.setText(nameString);

        // Read image source name from cursor and update view
        String imageString = cursor.getString(CarDataContract.COL_CARS_IMAGE);
        int imageId = context.getResources().getIdentifier(imageString , "drawable", context.getPackageName());
        viewHolder.imageView.setImageResource(imageId);

        Log.e("ImageId", Integer.toString(imageId));

        // Read speed from cursor and update view
        String speedString = cursor.getString(CarDataContract.COL_CARS_SPEED);
        viewHolder.speedView.setText(speedString + " km/h");

        // Read power from cursor and update view
        String powerString = cursor.getString(CarDataContract.COL_CARS_POWER);
        viewHolder.powerView.setText(powerString + " HP");

        // Read torque from cursor and update view
        String torqueString = cursor.getString(CarDataContract.COL_CARS_TORQUE);
        viewHolder.torqueView.setText(torqueString + " Nm");

        // Read acceleration from cursor and update view
        String accelerationString = cursor.getString(CarDataContract.COL_CARS_ACCELERATION);
        viewHolder.accelerationView.setText(accelerationString + " s");

        // Read weight from cursor and update view
        String weightString = cursor.getString(CarDataContract.COL_CARS_WEIGHT);
        viewHolder.weightView.setText(weightString + " kg");

        //Log.d("Looking for name", nameString);
    }

    /**
     * Cache of the children views for a forecast list item.
     */
    public static class ViewHolder {
        public final TextView nameView;
        public final ImageView imageView;
        public final TextView speedView;
        public final TextView powerView;
        public final TextView torqueView;
        public final TextView accelerationView;
        public final TextView weightView;

        public ViewHolder(View view) {
            nameView        = (TextView) view.findViewById(R.id.cars_name);
            imageView       = (ImageView) view.findViewById(R.id.cars_image);
            speedView       = (TextView) view.findViewById(R.id.cars_speed);
            powerView       = (TextView) view.findViewById(R.id.cars_power);
            torqueView      = (TextView) view.findViewById(R.id.cars_torque);
            accelerationView= (TextView) view.findViewById(R.id.cars_acceleration);
            weightView      = (TextView) view.findViewById(R.id.cars_weight);
        }
    }
}
