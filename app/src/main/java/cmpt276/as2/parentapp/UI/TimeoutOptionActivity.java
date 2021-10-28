package cmpt276.as2.parentapp.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import cmpt276.as2.parentapp.R;

public class TimeoutOptionActivity extends AppCompatActivity {

    private static final String DURATION_SETTING = "Duration Settings";
    private static final String DURATION_CHOICE = "Duration Choice";

    public static Intent makeIntent(Context context){
        return new Intent(context, TimeoutOptionActivity.class);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeout_option);
        setUpRadioButton();
    }
    private void setUpRadioButton(){
        int[] durationList = getResources().getIntArray(R.array.duration);


        RadioGroup durationRadioGroup= findViewById(R.id.timerOptionRadioGroup);

        for(int i = 0; i< durationList.length; i++){
            final int duration=durationList[i];
            RadioButton button = new RadioButton(this);

            //Set up radio buttons
            button.setText(getString(R.string.durationOption,duration));
            button.setOnClickListener(view -> { saveDurationSetting(duration); });
            durationRadioGroup.addView(button);

            if (duration == getDuration(this) ){
                button.setChecked(true);
            }
        }


    }
    static public int getDuration(Context context){
        SharedPreferences prefs = context.getSharedPreferences(DURATION_SETTING, MODE_PRIVATE);
        return prefs.getInt(DURATION_CHOICE,4);
    }

    private void saveDurationSetting(int duration) {
        SharedPreferences prefs = this.getSharedPreferences(DURATION_SETTING, MODE_PRIVATE);

        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(DURATION_CHOICE, duration);
        editor.apply();
    }
}