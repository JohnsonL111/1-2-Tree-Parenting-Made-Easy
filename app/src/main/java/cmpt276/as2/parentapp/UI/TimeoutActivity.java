package cmpt276.as2.parentapp.UI;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;



import cmpt276.as2.parentapp.R;

//timer button= start and stop button
public class TimeoutActivity extends AppCompatActivity {
    private static final String INITIAL_TIME = "Initial Time";

    Button timerButton;
    Button resetButton;
    TextView timeout;
    boolean timerIsRunning;
    int initialTime;
    int timeLeft;
    CountDownTimer timer;

    public static Intent makeIntent(Context context, int initialTime){
        Intent intent = new Intent(context, TimeoutActivity.class);
        intent.putExtra(INITIAL_TIME,initialTime);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeout);
        timerButton = findViewById(R.id.StartStopButton);
        resetButton = findViewById(R.id.ResetButton);
        timerButton.setText("START");
        timerButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if(!timerIsRunning) {
                    timerButton.setText("STOP");
                    timerIsRunning=true;
                    startTimer();

                }
                else{
                    timerButton.setText("START");
                    timerIsRunning=false;
                    stopTimer();
                }
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timeLeft=initialTime;
            }
        });

    }

    private void stopTimer() {
        timer.cancel();
    }

    private void startTimer() {
        timer= new CountDownTimer(timeLeft,1000) {
            @Override
            public void onTick(long l) {
                updateTimer();
            }

            @Override
            public void onFinish() {
                //notification()
            }
        }.start();
    }

    private void updateTimer() {
        int minute=timeLeft/60;
        int second=timeLeft%60;
        String timerText=minute+":"+second;
        timeout.setText(timerText);
    }
}
