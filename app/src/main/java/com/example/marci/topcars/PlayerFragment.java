package com.example.marci.topcars;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.marci.topcars.data.CarDataContract;
import com.example.marci.topcars.data.CarDataDbHelper;

import java.util.HashMap;

/**
 * Created by Marci on 22/08/2014.
 */
public class PlayerFragment extends Fragment {

    private static final String LOG_TAG = "Top Cars app";

    private String PACKAGE_NAME;

    // Defines whether it's an actual player or the computer
    private boolean mIsComputer;
    public boolean getIsComputer() {
        return mIsComputer;
    }
    public void setIsComputer(boolean mIsComputer) {
        this.mIsComputer = mIsComputer;
    }

    private Context mContext;
    private View mContainer;

    // Contains the name of the player
    private String mName;

    // Contains the current score of the player
    private int mScore;
    public int getScore() {
        return mScore;
    }
    public void setScore(int mScore) {
        this.mScore = mScore;
    }

    // Contains the current streak of the player
    private int mStreak;
    public int getStreak() {
        return mStreak;
    }
    public void setStreak(int mStreak) {
        this.mStreak = mStreak;
    }

    // Contains the views and layouts which belong to the player
    private ViewHolder mCardView;
    public ViewHolder getCardView() {
        return mCardView;
    }
    public void setCardView(ViewHolder mCardView) {
        this.mCardView = mCardView;
    }

    // Contains the card values the player is holding
    private HashMap<Integer, String> mCurrentCardValues;

    // True, if the player is allowed to pick a field
    private boolean mAllowedToPickField;
    public void setAllowedToPickField(boolean isAllowed){
        mAllowedToPickField = isAllowed;
    }
    public boolean getAllowedToPickField(){
        return mAllowedToPickField;
    }

    // True if the player is allowed to rotate new card
    private boolean mAllowedToRotate;
    public void setAllowedToRotate(boolean isAllowed){
        mAllowedToRotate = isAllowed;
    }
    public boolean isAllowedToRotate(){
        return mAllowedToRotate;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_player, container, false);

        mContainer = rootView;

        mCardView = new ViewHolder();

        PACKAGE_NAME = getActivity().getPackageName();

        mContext = getActivity();

        return rootView;
    }

    public void pickNewCard(HashMap<Integer, String> newCard){

        mCurrentCardValues = newCard;

        // Set the cards name
        String name = mCurrentCardValues.get(ViewIdHolder.P_CARD_NAME_VIEW).toString();
        mCardView.mPlayerViews.get(CarDataContract.COL_CARS_NAME).setText(name);

        Log.d(LOG_TAG, "Name received by pickNewCard is: " + name);

        // Set the cards image
        String image = mCurrentCardValues.get(ViewIdHolder.P_CARD_IMAGE_VIEW).toString();
        int imageId = getResources().getIdentifier(image , "drawable", PACKAGE_NAME);
        mCardView.mImageView.setImageResource(imageId);

        // Set the cards speed
        String speed = mCurrentCardValues.get(ViewIdHolder.P_CARD_SPEED_LAYOUT).toString();
        mCardView.mPlayerViews.get(CarDataContract.COL_CARS_SPEED).setText(speed);

        // Set the cards power
        String power = mCurrentCardValues.get(ViewIdHolder.P_CARD_POWER_LAYOUT).toString();
        mCardView.mPlayerViews.get(CarDataContract.COL_CARS_POWER).setText(power);

        // Set the cards torque
        String torque = mCurrentCardValues.get(ViewIdHolder.P_CARD_TORQUE_LAYOUT).toString();
        mCardView.mPlayerViews.get(CarDataContract.COL_CARS_TORQUE).setText(torque);

        // Set the cards acceleration
        String acceleration = mCurrentCardValues.get(ViewIdHolder.P_CARD_ACCELERATION_LAYOUT).toString();
        mCardView.mPlayerViews.get(CarDataContract.COL_CARS_ACCELERATION).setText(acceleration);

        // Set the cards weight
        String weight = mCurrentCardValues.get(ViewIdHolder.P_CARD_WEIGHT_LAYOUT).toString();
        mCardView.mPlayerViews.get(CarDataContract.COL_CARS_WEIGHT).setText(weight);
    }

    public HashMap<Integer, String> getCurrentCard(){
        return mCurrentCardValues;
    }

    public void showCard(){
        mCardView.mCardLayout.setVisibility(View.VISIBLE);
    }

    public void hideCard(){
        mCardView.mCardLayout.setVisibility(View.INVISIBLE);
    }

    public class ViewIdHolder{
    // Holds static elements' ids for the player's card

        // Player
        public static final int P_CARD = R.id.p_card;

        public static final int P_CARD_NAME_VIEW   = R.id.p_card_name;
        public static final int P_CARD_IMAGE_VIEW  = R.id.p_card_image;

        public static final int P_CARD_SPEED_LAYOUT     = R.id.p_card_speed_layout;
        public static final int P_CARD_SPEED_TEXT_VIEW  = R.id.p_card_speed_text;

        public static final int P_CARD_POWER_LAYOUT     = R.id.p_card_power_layout;
        public static final int P_CARD_POWER_TEXT_VIEW  = R.id.p_card_power_text;

        public static final int P_CARD_TORQUE_LAYOUT    = R.id.p_card_torque_layout;
        public static final int P_CARD_TORQUE_TEXT_VIEW = R.id.p_card_torque_text;

        public static final int P_CARD_ACCELERATION_LAYOUT     = R.id.p_card_acceleration_layout;
        public static final int P_CARD_ACCELERATION_TEXT_VIEW  = R.id.p_card_acceleration_text;

        public static final int P_CARD_WEIGHT_LAYOUT    = R.id.p_card_weight_layout;
        public static final int P_CARD_WEIGHT_TEXT_VIEW = R.id.p_card_weight_text;


    }

    public class ViewHolder{
    // Holds static elements for the player's card

        public ViewHolder(){

            mPlayerLayouts.put(ViewIdHolder.P_CARD_SPEED_LAYOUT, (LinearLayout) mContainer.findViewById(ViewIdHolder.P_CARD_SPEED_LAYOUT));
            mPlayerLayouts.put(ViewIdHolder.P_CARD_POWER_LAYOUT, (LinearLayout) mContainer.findViewById(ViewIdHolder.P_CARD_POWER_LAYOUT));
            mPlayerLayouts.put(ViewIdHolder.P_CARD_TORQUE_LAYOUT, (LinearLayout) mContainer.findViewById(ViewIdHolder.P_CARD_TORQUE_LAYOUT));
            mPlayerLayouts.put(ViewIdHolder.P_CARD_ACCELERATION_LAYOUT, (LinearLayout) mContainer.findViewById(ViewIdHolder.P_CARD_ACCELERATION_LAYOUT));
            mPlayerLayouts.put(ViewIdHolder.P_CARD_WEIGHT_LAYOUT, (LinearLayout) mContainer.findViewById(ViewIdHolder.P_CARD_WEIGHT_LAYOUT));

            mPlayerViews.put(CarDataContract.COL_CARS_NAME, (TextView)mContainer.findViewById(ViewIdHolder.P_CARD_NAME_VIEW));
            mPlayerViews.put(CarDataContract.COL_CARS_SPEED, (TextView)mContainer.findViewById(ViewIdHolder.P_CARD_SPEED_TEXT_VIEW));
            mPlayerViews.put(CarDataContract.COL_CARS_POWER, (TextView)mContainer.findViewById(ViewIdHolder.P_CARD_POWER_TEXT_VIEW));
            mPlayerViews.put(CarDataContract.COL_CARS_TORQUE, (TextView)mContainer.findViewById(ViewIdHolder.P_CARD_TORQUE_TEXT_VIEW));
            mPlayerViews.put(CarDataContract.COL_CARS_ACCELERATION, (TextView)mContainer.findViewById(ViewIdHolder.P_CARD_ACCELERATION_TEXT_VIEW));
            mPlayerViews.put(CarDataContract.COL_CARS_WEIGHT, (TextView)mContainer.findViewById(ViewIdHolder.P_CARD_WEIGHT_TEXT_VIEW));

            mImageView = (ImageView)mContainer.findViewById(ViewIdHolder.P_CARD_IMAGE_VIEW);

            mCardLayout = (LinearLayout) mContainer.findViewById(ViewIdHolder.P_CARD);

        }

        // Key is the column name inside the table
        public HashMap<Integer, LinearLayout> mPlayerLayouts = new HashMap<Integer, LinearLayout>();
        public  HashMap<Integer, TextView> mPlayerViews = new HashMap<Integer, TextView>();

        // Storing the ImageView separately
        public ImageView mImageView;

        // Storing the main card Layout separately
        public LinearLayout mCardLayout;

    }
}
