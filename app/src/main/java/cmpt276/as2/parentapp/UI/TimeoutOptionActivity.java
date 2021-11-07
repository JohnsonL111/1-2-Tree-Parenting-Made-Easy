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

public class TimeoutOptionActivity extends AppCompatActivity {

    private static final String DURATION_SETTING = "Duration Settings";
    private static final String DURATION_CHOICE = "Duration Choice";
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

            if (duration == getDuration(this) ){
                button.setChecked(true);
            }

        }

        customMinute = findViewById(R.id.CustomMinute);
        customSecond = findViewById(R.id.CustomSecond);
        //Set up radio buttons
        customButton.setText("custom duration");
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
        customSecond.addTextChangedListener(new TextWatcher() {
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
        customSecond = findViewById(R.id.CustomSecond);
        String  minuteString=customMinute.getText().toString();
        String  secondString=customSecond.getText().toString();
        int minute=0;
        int second=0;
        if (!minuteString.equals("")){
            minute = Integer.parseInt(minuteString);
        }
        if (!secondString.equals("")) {
            second  = Integer.parseInt(secondString);
        }
        int duration=minute*60+second;
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
        return prefs.getInt(DURATION_CHOICE,4);
    }

    private void saveDurationSetting(int duration) {
        SharedPreferences prefs = this.getSharedPreferences(DURATION_SETTING, MODE_PRIVATE);

        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(DURATION_CHOICE, duration);
        editor.apply();
    }
}