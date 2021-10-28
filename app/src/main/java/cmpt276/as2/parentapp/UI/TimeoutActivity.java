package cmpt276.as2.parentapp.UI;

import static cmpt276.as2.parentapp.model.TimerNotification.TIMER_CHANNEL_ID;

import android.app.Notification;
import android.app.NotificationManager;
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
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;


import cmpt276.as2.parentapp.R;

//timer button= start and stop button
public class TimeoutActivity extends AppCompatActivity {
    private static final String INITIAL_TIME = "Initial Time";
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
    private NotificationManagerCompat timerNotificationManager;

    public static Intent makeIntent(Context context){
        Intent intent = new Intent(context, TimeoutActivity.class);
        intent.putExtra(INITIAL_TIME,initialTime);
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
        timerNotificationManager = NotificationManagerCompat.from(this);
        getDuration();
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
                startTimer();
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


    }

    @Override
    protected void onResume() {
        super.onResume();
        getDuration();
    }

    private void stopTimer() {
        timer.cancel();
    }

    private void startTimer() {
        timer=new CountDownTimer(timeLeft*1000, 1000) {

            public void onTick(long secondsLeft) {
                timeLeft=((int)secondsLeft)/1000;
                updateTimer((int)secondsLeft / 1000);
            }
            public void onFinish() {
                //notification()
                sendTimerNotification();
                timeoutText.setText("done!");
            }
        }.start();
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

    public void sendTimerNotification() {
        String title = "Timer";
        String message = "Time is up!";
        Notification notification = new NotificationCompat.Builder(this, TIMER_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_baseline_timer_24)
                .setContentTitle(title)
                .setContentText(message)
                .build();
        timerNotificationManager.notify(1, notification);
    }

}
