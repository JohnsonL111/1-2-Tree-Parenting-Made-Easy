package cmpt276.as2.parentapp.UI;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;



import cmpt276.as2.parentapp.R;

//timer button= start and stop button
public class TimeoutActivity extends AppCompatActivity {
    private static final String DURATION_SETTING = "Duration Settings";
    private static final String DURATION_CHOICE = "Duration Choice";


    Button timerButton;
    Button resetButton;
    Button optionButton;
    TextView timeoutText;
    boolean timerIsRunning;
    int initialTime;
    int timeLeft;
    CountDownTimer timer;

    public static Intent makeIntent(Context context){
        Intent intent = new Intent(context, TimeoutActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getDuration();
        setContentView(R.layout.activity_timeout);
        timerButton = findViewById(R.id.StartStopButton);
        resetButton = findViewById(R.id.ResetButton);
        optionButton=findViewById(R.id.OptionButton);
        timeoutText=findViewById(R.id.timeoutText);

        timerButton.setText("START");
        timerButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if(!timerIsRunning) {
                    startTimer();
                    timerIsRunning=true;
                    timerButton.setText("STOP");


                }
                else{
                    stopTimer();
                    timerIsRunning=false;
                    timerButton.setText("START");

                }
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timer.cancel();
                timeLeft=initialTime;
                updateTimer(timeLeft);
                timerIsRunning=false;
                timerButton.setText("START");
            }
        });

        optionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!timerIsRunning){
                    Intent i = TimeoutOptionActivity.makeIntent(TimeoutActivity.this);
                    startActivity(i);
                }
                else{
                    Toast.makeText(TimeoutActivity.this, "A timer is running",Toast.LENGTH_SHORT);
                }
            }
        });
        timer=new CountDownTimer(timeLeft*1000, 1000) {

            public void onTick(long secondsLeft) {
                timeLeft=((int)secondsLeft)/1000;
                updateTimer((int)secondsLeft / 1000);
            }
            public void onFinish() {
                timeoutText.setText("done!");
            }
        };

    }

    @Override
    protected void onResume() {
        super.onResume();
        getDuration();
        updateTimer(timeLeft);
    }

    private void stopTimer() {
        timer.cancel();
    }

    private void startTimer() {
        timer.start();
    }
    private void updateTimer(int l) {
        int minute=l/60;
        int second=l%60;
        timeoutText.setText(getString(R.string.timerTextFormat,minute,second));
    }
    public void getDuration(){
        initialTime = TimeoutOptionActivity.getDuration(this)*60;
        Log.e("duration",initialTime+"");
        timeLeft=initialTime;
    }

}
