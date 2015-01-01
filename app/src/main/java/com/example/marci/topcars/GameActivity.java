package com.example.marci.topcars;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.example.marci.topcars.PlayerFragment.ViewIdHolder;
import com.example.marci.topcars.data.CarDataContract;
import com.example.marci.topcars.data.CarDataDbHelper;

import java.util.HashMap;


public class GameActivity extends ActionBarActivity {

    private static final String LOG_TAG = "Top Cars app";

    public GameSession mGameSession;
    public CarDataDbHelper carDataDbHelper;
    private Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_game);

        // Hide the action bar to use more space
        android.app.ActionBar actionBar = getActionBar();
        actionBar.hide();

        if (savedInstanceState == null) {

            mContext = this;

            carDataDbHelper = new CarDataDbHelper(this);
            mGameSession = new GameSession(carDataDbHelper.getCarsBase());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.play, menu);
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
        }
        return super.onOptionsItemSelected(item);
    }

    public void fieldClick(View view){
        int viewId = view.getId();
        view.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.field_click));
        mGameSession.compareFields(viewId);
    }
    public void buttonClick(View view){
        int buttonId = view.getId();
        switch (buttonId){
            case R.id.next_round:
                mGameSession.nextRound();
                break;
            case R.id.new_game:
                mGameSession.newGame();
                break;
        }
        view.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.field_click));
    }

    public class GameSession{

        private PlayerFragment mPlayer1;
        private PlayerFragment mPlayer2;

        // Stores the so far highest streak of the player
        private int mHighestStreak;

        private Cursor mUserProgress;

        // Stores the deck of cards
        private Cursor mPack;
        private int mPackLength;

        // Elements of UI with their integer id's being the key
        private TextView mUiScore;
        private TextView mUiAddScore;
        private TextView mUiStreak;
        private Button mUiNextRound;
        private Button mUiNewGame;

        // Currently active field
        private int mActiveField;

        public GameSession(Cursor cursor){

            mPlayer1 = (PlayerFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_player_1);
            mPlayer2 = (PlayerFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_player_2);

            mUserProgress = carDataDbHelper.getUserProgressBase();
            mHighestStreak = mUserProgress.getInt(CarDataContract.COL_USERPROGRESS_STREAK);
            Log.d(LOG_TAG, "Current UserProgress (cursor): " + mUserProgress.toString());
            Log.d(LOG_TAG, "Current streak: " + mHighestStreak);
            Log.d(LOG_TAG, "Current score: "  + mUserProgress.getInt(CarDataContract.COL_USERPROGRESS_SCORE));

            mPack = cursor;
            mPackLength = cursor.getCount();

            mUiScore        =  (TextView) findViewById(R.id.player_score);
            mUiAddScore     =  (TextView) findViewById(R.id.player_add_score);
            mUiStreak       =  (TextView) findViewById(R.id.player_streak);
            mUiNextRound    =  (Button) findViewById(R.id.next_round);
            mUiNewGame      =  (Button)  findViewById(R.id.new_game);

            mPlayer1.setAllowedToPickField(true);
            mPlayer1.pickNewCard(getRandomCardFromDeck());
            mPlayer1.showCard();
        }

        public HashMap<Integer, String> getRandomCardFromDeck(){
        // Returns a random card from the deck

            HashMap<Integer, String> retValues = new HashMap<Integer, String>();

            int random = (int )(Math.random() * mPackLength);

            mPack.moveToPosition(random);

            retValues.put(ViewIdHolder.P_CARD_NAME_VIEW,            mPack.getString(CarDataContract.COL_CARS_NAME));
            retValues.put(ViewIdHolder.P_CARD_IMAGE_VIEW,           mPack.getString(CarDataContract.COL_CARS_IMAGE));
            retValues.put(ViewIdHolder.P_CARD_SPEED_LAYOUT,         mPack.getString(CarDataContract.COL_CARS_SPEED));
            retValues.put(ViewIdHolder.P_CARD_POWER_LAYOUT,         mPack.getString(CarDataContract.COL_CARS_POWER));
            retValues.put(ViewIdHolder.P_CARD_TORQUE_LAYOUT,        mPack.getString(CarDataContract.COL_CARS_TORQUE));
            retValues.put(ViewIdHolder.P_CARD_ACCELERATION_LAYOUT,  mPack.getString(CarDataContract.COL_CARS_ACCELERATION));
            retValues.put(ViewIdHolder.P_CARD_WEIGHT_LAYOUT,        mPack.getString(CarDataContract.COL_CARS_WEIGHT));

            return retValues;
        }

        public void compareFields(int viewId){
        // Called when the user picked their field

            if (mPlayer1.getAllowedToPickField()){

                // Allow user to continue the game
                mPlayer1.setAllowedToRotate(true);

                // Disallow user to select another field
                mPlayer1.setAllowedToPickField(false);

                // Generate random cards for Guest
                mPlayer2.pickNewCard(getRandomCardFromDeck());
                mPlayer2.showCard();

                // Set the active field
                mActiveField = viewId;

                // Get and convert the picked values to double - because of acceleration
                double player1Field = Double.parseDouble(mPlayer1.getCurrentCard().get(viewId));
                double player2Field = Double.parseDouble(mPlayer2.getCurrentCard().get(viewId));

                // Compare players' cards' values, considering special cases where smaller value is better
                if (viewId == ViewIdHolder.P_CARD_WEIGHT_LAYOUT || viewId == ViewIdHolder.P_CARD_ACCELERATION_LAYOUT){
                    if (player1Field < player2Field){
                        Log.d(LOG_TAG, "Player 1 wins");
                        winAction(mPlayer1, mPlayer2);
                        givePoints(mPlayer1, calculatePoints(player1Field, player2Field, viewId));
                    }else if(player1Field > player2Field){
                        Log.d(LOG_TAG, "Player 2 wins");
                        loseAction(mPlayer1, mPlayer2);
                    }else{
                        Log.d(LOG_TAG, "Equal");
                        equalAction(mPlayer1, mPlayer2);
                    }
                }else{
                    if (player1Field > player2Field){
                        Log.d(LOG_TAG, "Player 1 wins");
                        winAction(mPlayer1, mPlayer2);
                        givePoints(mPlayer1, calculatePoints(player1Field, player2Field, viewId));
                    }else if(player1Field < player2Field){
                        Log.d(LOG_TAG, "Player 2 wins");
                        loseAction(mPlayer1, mPlayer2);
                    }else{
                        Log.d(LOG_TAG, "Equal");
                        equalAction(mPlayer1, mPlayer2);
                    }
                }
            }
        }

        private void winAction(PlayerFragment winner, PlayerFragment loser){
            int winnerStreak = winner.getStreak() +1;
            winner.setStreak(winnerStreak);
            mUiStreak.setText(Integer.toString(winnerStreak));

            // Colour fields
            winner.getCardView().mPlayerLayouts.get(mActiveField).setBackgroundResource(R.drawable.winning_field);
            loser.getCardView().mPlayerLayouts.get(mActiveField).setBackgroundResource(R.drawable.losing_field);
            mUiNextRound.setVisibility(View.VISIBLE);
        }

        private void loseAction(PlayerFragment loser, PlayerFragment winner){
            // Upload record into database
            mUiNewGame.setVisibility(View.VISIBLE);

            // Colour fields
            winner.getCardView().mPlayerLayouts.get(mActiveField).setBackgroundResource(R.drawable.winning_field);
            loser.getCardView().mPlayerLayouts.get(mActiveField).setBackgroundResource(R.drawable.losing_field);

            // If the new highscore is larger than the previous, update database
            if (mPlayer1.getStreak() > mHighestStreak){
                Log.d(LOG_TAG, "New high score greater than previous");
                mHighestStreak = mPlayer1.getStreak();
            }
            carDataDbHelper.uploadUserProgress(mPlayer1.getScore(), mPlayer1.getStreak());
        }

        private void equalAction(PlayerFragment player1, PlayerFragment player2){
            mUiNextRound.setVisibility(View.VISIBLE);

            // Colour fields
            player1.getCardView().mPlayerLayouts.get(mActiveField).setBackgroundResource(R.drawable.equal_field);
            player2.getCardView().mPlayerLayouts.get(mActiveField).setBackgroundResource(R.drawable.equal_field);
        }

        private void givePoints(PlayerFragment player, int points){
            int score = player.getScore();
            score += points;
            player.setScore(score);
            mUiScore.setText(Integer.toString(score));

            // Show acquired points under score
            mUiAddScore.setText("+ " + points);
            mUiAddScore.setAlpha(1.0f);
            AlphaAnimation animation = new AlphaAnimation(1.0f, 0f);
            animation.setDuration(200);
            animation.setFillAfter(true);
            animation.setStartOffset(1000);
            mUiAddScore.startAnimation(animation);

            Log.d(LOG_TAG, "Player1's current score: " + score);
        }

        private int calculatePoints(double winnerValue, double loserValue, int viewId){
            int retValue;

            if (viewId == ViewIdHolder.P_CARD_ACCELERATION_LAYOUT){
                retValue = 100 * (int) (loserValue - winnerValue);
            }else if(viewId == ViewIdHolder.P_CARD_WEIGHT_LAYOUT){
                retValue = (int) (loserValue - winnerValue);
            }else{
                retValue = (int) (winnerValue - loserValue);
            }

            Log.d(LOG_TAG, "Player1 gets points: " + retValue);
            return retValue;
        }

        public void nextRound(){
            if (mPlayer1.isAllowedToRotate()){
                mPlayer1.hideCard();
                mPlayer2.hideCard();
                mPlayer1.getCardView().mPlayerLayouts.get(mActiveField).setBackground(null);
                mPlayer2.getCardView().mPlayerLayouts.get(mActiveField).setBackground(null);
                mPlayer1.pickNewCard(mGameSession.getRandomCardFromDeck());
                mPlayer1.showCard();
                mUiNextRound.setVisibility(View.GONE);
                mPlayer1.setAllowedToRotate(false);
                mPlayer1.setAllowedToPickField(true);
            }
        }

        public void newGame(){
            if (mPlayer1.isAllowedToRotate()){
                mPlayer1.setStreak(0);
                mUiStreak.setText(Integer.toString(mPlayer1.getStreak()));
                mPlayer1.hideCard();
                mPlayer2.hideCard();
                mPlayer1.getCardView().mPlayerLayouts.get(mActiveField).setBackground(null);
                mPlayer2.getCardView().mPlayerLayouts.get(mActiveField).setBackground(null);
                mPlayer1.pickNewCard(mGameSession.getRandomCardFromDeck());
                mPlayer1.showCard();
                mPlayer1.setAllowedToRotate(false);
                mPlayer1.setAllowedToPickField(true);
                mUiNewGame.setVisibility(View.GONE);
            }
        }
    }
}
