package cmpt276.as2.parentapp.UI;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import cmpt276.as2.parentapp.R;
import cmpt276.as2.parentapp.model.BreathMenuAdapter;
import cmpt276.as2.parentapp.model.State.ReadyToStartState;
import cmpt276.as2.parentapp.model.State.State;

public class BreathActivity extends AppCompatActivity {

    public static String BREATH_TAG = "Deep Breath Setting";
    public static String NUM_OF_BREATH = "Number of breath set";
    private static int[] TIME_INTERVAL = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};

    private TextView showNumOfBreath;
    private int numOfBreathSet;
    private int numOfBreathLeft;
    private Button mainBtn;
    private TextView helpMessage;

    private State currentState;
    private MediaPlayer calmSounds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_breath);

        setUpButton();
        getNumOfBreath();
        setUpOption();

        setState(new ReadyToStartState(this));
    }

    private void setUpButton() {
        mainBtn = findViewById(R.id.breath_main_btn);
        mainBtn.setText(R.string.begin_button_text);
    }

    public void setUpOption() {
        showNumOfBreath = findViewById(R.id.breath_num_of_breath);
        showNumOfBreath.setText(getString(R.string.num_of_breath_set, numOfBreathSet));
        helpMessage = findViewById(R.id.breath_help_message);
        helpMessage.setText(R.string.intial_message_breath);

        /**
         * Currently always show up the menu, change to only show up the menu when not in cycle later.
         */
        showNumOfBreath.setOnClickListener(view -> showOptionMenu());
    }

    public void disableBreathsMenu() {
        showNumOfBreath.setOnClickListener(view -> {
        });
    }

    private void showOptionMenu() {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        View v = LayoutInflater.from(this).inflate(R.layout.breath_optin_list, null);
        RecyclerView optionList = v.findViewById(R.id.breath_option_recycle);
        optionList.setLayoutManager(mLayoutManager);

        BreathMenuAdapter breathMenuAdapter = new BreathMenuAdapter(this, TIME_INTERVAL);
        optionList.setAdapter(breathMenuAdapter);


        AlertDialog.Builder build = new AlertDialog.Builder(this).setView(v)
                .setTitle("Change Number of Breath")
                .setNegativeButton(R.string.cancel, (dialogInterface, i) -> dialogInterface.dismiss());

        Dialog dialog = build.create();
        dialog.show();

        BreathMenuAdapter.clickObserverOption obs = () ->
        {
            dialog.dismiss();
            int result = breathMenuAdapter.getResult();
            showNumOfBreath.setText(getString(R.string.num_of_breath_set, TIME_INTERVAL[result]));
            saveOption(result);
            getNumOfBreath();
        };

        breathMenuAdapter.registerOptionCallBack(obs);
    }

    private void saveOption(int result) {
        SharedPreferences prefs = this.getSharedPreferences(BREATH_TAG, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putInt(NUM_OF_BREATH, result);
        editor.apply();
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, BreathActivity.class);
    }

    private void getNumOfBreath() {

        SharedPreferences prefs = this.getSharedPreferences(BREATH_TAG, MODE_PRIVATE);

        if (!prefs.contains(NUM_OF_BREATH)) {
            /**
             * Default ...
             */
            numOfBreathSet = 3;
            numOfBreathLeft = 3;
        } else {
            /**
             * Default ...
             */
            numOfBreathSet = TIME_INTERVAL[prefs.getInt(NUM_OF_BREATH, 3)];
            numOfBreathLeft = TIME_INTERVAL[prefs.getInt(NUM_OF_BREATH, 3)];
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_coin_history, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.go_back_coin_history:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void setState(State state) {
        currentState = state;
        currentState.helpTextHandler(this);
        currentState.onClickHandler(this);
    }

    public void setText(String btnText, String helpText) {
        mainBtn.setText(btnText);
        helpMessage.setText(helpText);
    }

    public void updateDecreaseBreathsText() {
        numOfBreathLeft = numOfBreathLeft - 1;
        showNumOfBreath.setText(getString(R.string.num_of_breath_set, numOfBreathLeft));
    }

    public int getNumOfBreathLeft() {
        return numOfBreathLeft;
    }

    public void startInhaleBreatheSound() {
        calmSounds = MediaPlayer.create(this, R.raw.calm_forest_birds);
        calmSounds.start();
    }

    public void startExhaleBreatheSound() {
        calmSounds = MediaPlayer.create(this, R.raw.beach_sound);
        calmSounds.start();
    }

    public void stopBreatheSounds() {
        if (calmSounds != null) {
            calmSounds.stop();
            calmSounds.release();
            calmSounds = null;
        }
    }
    public void resetNumBreathLeft(){
        numOfBreathLeft = numOfBreathSet;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopBreatheSounds();
    }
}