package cmpt276.as2.parentapp.UI;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

import cmpt276.as2.parentapp.R;

/**
 * activity for changing the duration of the timer
 */

public class TimeoutOptionActivity extends AppCompatActivity {

    private static final String DURATION_SETTING = "Duration Settings";
    private static final String DURATION_CHOICE = "Duration Choice";
    private static final String CUSTOM_DURATION = "custom duration";
    EditText customMinute;
    EditText customSecond;


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
        RadioButton customButton = new RadioButton(this);

        for(int i = 0; i< durationList.length; i++){
            final int duration=durationList[i];
            RadioButton button = new RadioButton(this);

            //Set up radio buttons
            button.setText(getString(R.string.durationOption,duration));
            button.setOnClickListener(view -> { saveDurationSetting(duration*60); });
            durationRadioGroup.addView(button);


        }
        customMinute = findViewById(R.id.CustomMinute);
        //Set up radio buttons
        customButton.setText(CUSTOM_DURATION);
        customButton.setOnClickListener(view -> { SaveCustomDuration(); });
        durationRadioGroup.addView(customButton);
        customMinute.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                customButton.setChecked(true);
                SaveCustomDuration();
            }
        });

    }



    private void SaveCustomDuration() {
        SharedPreferences prefs = this.getSharedPreferences(DURATION_SETTING, MODE_PRIVATE);
        customMinute = findViewById(R.id.CustomMinute);
        String  minuteString=customMinute.getText().toString();
        int minute=0;
        if (!minuteString.equals("")){
            minute = Integer.parseInt(minuteString);
        }
        int duration=minute*60;
        SharedPreferences.Editor editor = prefs.edit();
        if(duration==0){

        }
        else{
            editor.putInt(DURATION_CHOICE, duration);
            editor.apply();
        }



    }


    static public int getDuration(Context context){
        SharedPreferences prefs = context.getSharedPreferences(DURATION_SETTING, MODE_PRIVATE);
        return prefs.getInt(DURATION_CHOICE,300);
    }

    private void saveDurationSetting(int duration) {
        SharedPreferences prefs = this.getSharedPreferences(DURATION_SETTING, MODE_PRIVATE);

        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(DURATION_CHOICE, duration);
        editor.apply();
    }
}