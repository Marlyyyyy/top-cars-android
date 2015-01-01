package com.example.marci.topcars;

/**
 * Created by Marci on 18/08/2014.
 */

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.marci.topcars.data.CarDataContract;
import com.example.marci.topcars.data.CarDataContract.CarsEntry;
import com.example.marci.topcars.data.CarDataDbHelper;
import com.example.marci.topcars.data.FetchCarDataTask;

public class CarsFragment extends Fragment{

    private CarsAdapter mCarsAdapter;

    public CarsFragment() {
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        CarDataDbHelper carDataDbHelper = new CarDataDbHelper(getActivity());

        mCarsAdapter = new CarsAdapter(getActivity(), carDataDbHelper.getCarsBase(), 0);

        Log.e("COUNT", "CarsFragment onCreateView");

        View rootView = inflater.inflate(R.layout.fragment_cars, container, false);

        // Get a reference to the ListView, and attach this adapter to it.
        ListView listView = (ListView) rootView.findViewById(R.id.listview_cars);
        listView.setAdapter(mCarsAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Cursor cursor = mCarsAdapter.getCursor();
                if (cursor != null && cursor.moveToPosition(position)) {

                }
            }
        });

        return rootView;
    }




}
