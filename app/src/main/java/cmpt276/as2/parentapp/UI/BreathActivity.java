package cmpt276.as2.parentapp.UI;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import cmpt276.as2.parentapp.R;
import cmpt276.as2.parentapp.model.BreathMenuAdapter;
import cmpt276.as2.parentapp.model.CoinFlipMenuAdapter;

public class BreathActivity extends AppCompatActivity {

    public static String BREATH_TAG = "Deep Breath Setting";
    public static String NUM_OF_BREATH = "Number of breath set";
    private static int[] TIME_INTERVAL = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};

    private TextView showNumOfBreath;
    private int numOfBreathSet;
    private int numOfBreathLeft;
    private Button mainBtn;
    private TextView helpMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_breath);

        getNumOfBreath();
        setUpOption();
    }

    private void setUpOption()
    {
        showNumOfBreath = findViewById(R.id.breath_num_of_breath);
        showNumOfBreath.setText(getString(R.string.num_of_breath_set, numOfBreathSet));
        helpMessage = findViewById(R.id.breath_help_message);
        helpMessage.setText(R.string.intial_message_breath);

        /**
         * Currently always show up the menu, change to only show up the menu when not in cycle later.
         */
        showNumOfBreath.setOnClickListener(view -> showOptionMenu());
    }

    private void showOptionMenu()
    {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        View v = LayoutInflater.from(this).inflate(R.layout.coin_flip_change_order, null);
        RecyclerView optionList = v.findViewById(R.id.coin_flip_change_listview);
        optionList.setLayoutManager(mLayoutManager);
        DividerItemDecoration decoration = new DividerItemDecoration(optionList.getContext(), mLayoutManager.getOrientation());
        decoration.setDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.recyclerview_divider, null));
        optionList.addItemDecoration(decoration);

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
        };

        breathMenuAdapter.registerOptionCallBack(obs);
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, BreathActivity.class);
    }

    private void getNumOfBreath() {

        SharedPreferences prefs = this.getSharedPreferences(BREATH_TAG, MODE_PRIVATE);

        if(!prefs.contains(NUM_OF_BREATH)) {
            /**
             * Default ...
             */
            numOfBreathSet = 3;
        } else {
            /**
             * Default ...
             */
            prefs.getInt(NUM_OF_BREATH, 3);
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
}