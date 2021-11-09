package cmpt276.as2.parentapp.UI;

import static cmpt276.as2.parentapp.model.TimerNotification.TIMER_CHANNEL_ID;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.io.IOException;
import java.sql.Time;

import cmpt276.as2.parentapp.R;
import cmpt276.as2.parentapp.model.NotificationReceiver;
import cmpt276.as2.parentapp.model.RingtonePlayService;
import cmpt276.as2.parentapp.model.TimerService;
/**
 * activity for start, stop, and reset the timer service
 */
//timer button= start and stop button
public class TimeoutActivity extends AppCompatActivity {
    private static final String TIMER_SITUATION = "timer situation";
    private static final String TIME_LEFT = "time left";
    private static final int BACKGROUND_SIZE = 7;
    private static final String INTENT_FILTER = "countdown";
    private static final String START = "START";
    private static final String STOP = "STOP";
    public static MediaPlayer beach_sound;

    ImageView timerAnimation;
    Button timerButton;
    Button resetButton;
    Button optionButton;
    TextView timeoutText;
    TableLayout animationLayout;
    ImageSwitcher background;
    boolean timerIsRunning;
    int initialTime;
    int timeLeft;
    int currentBackground;
//    boolean timerIsPaused;

    int backgroundList[];
    int counter = 0;

    public static Intent makeIntent(Context context) {
        Intent intent = new Intent(context, TimeoutActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

//        ActivityCompat.requestPermissions(this, FOREGR, PackageManager.PERMISSION_GRANTED);


        setUpMusic();

        initialTime = getDuration();
        timeLeft = initialTime;
        setContentView(R.layout.activity_timeout);

        timerButton = findViewById(R.id.StartStopButton);
        resetButton = findViewById(R.id.ResetButton);
        optionButton = findViewById(R.id.OptionButton);

        timeoutText = findViewById(R.id.timeoutText);
        timeoutText.setTextSize(40);
        updateButton();

        backgroundList = new int[9];
        fillBackgroundList();

        timerAnimation = new ImageView(this);
        timerAnimation.setImageResource(R.drawable.round);
        timerAnimation.setAlpha(0.5F);

        background = (ImageSwitcher) findViewById(R.id.background);
        background = findViewById(R.id.background);
        background.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView myView = new ImageView(getApplicationContext());
                myView.setScaleType(ImageView.ScaleType.FIT_XY);
                return myView;
            }
        });
        changeBackground();

        animationLayout = findViewById(R.id.animationLayout);
        animationLayout.addView(timerAnimation);

        currentBackground=-1;

        timerButton.setOnClickListener(new View.OnClickListener() {

            @SuppressLint("Range")
            @Override
            public void onClick(View view) {
                if (!timerIsRunning) {
                    startTimer();
                    timerIsRunning = true;
                    updateButton();

                } else {
                    stopTimer();
                    timerIsRunning = false;
                    updateButton();
                }
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timerIsRunning=false;
                beach_sound.pause();
                stopService(new Intent(TimeoutActivity.this, TimerService.class));
                initialTime=getDuration();
                timeLeft=initialTime;
                updateTimer(timeLeft);
                updateButton();
                updateAnimation(timeLeft);

            }
        });

        optionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = TimeoutOptionActivity.makeIntent(TimeoutActivity.this);
                startActivity(i);

            }
        });
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(INTENT_FILTER);
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int newTimeLeft = intent.getIntExtra(TIME_LEFT, -1);
                if(newTimeLeft!=-1){
                    timerIsRunning=true;
                    updateButton();
                    timeLeft=newTimeLeft;
                    SharedPreferences prefs = getSharedPreferences(TIMER_SITUATION, MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putInt(TIME_LEFT,timeLeft);
                    editor.putBoolean("timerIsRunning", true);
                    initialTime=getDuration();
                    updateTimer( newTimeLeft);
                    updateAnimation(newTimeLeft);
                    changeBackground();
                }
                if(newTimeLeft==0){
                    stopService(new Intent(TimeoutActivity.this, TimerService.class));
                    sendTimerNotification();
                    if(beach_sound.isPlaying()){
                        beach_sound.pause();
                    }
                    SharedPreferences prefs = getSharedPreferences(TIMER_SITUATION, MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    timerIsRunning=false;
                    editor.putBoolean("timerIsRunning", false);
                    editor.putInt(TIME_LEFT, initialTime);
                    editor.apply();
                    updateButton();
                    initialTime=getDuration();
                    timeLeft=initialTime;
                    updateTimer(timeLeft);
                }
            }
        };
        registerReceiver(broadcastReceiver, intentFilter);
    }

    private void updateAnimation(int newTimeLeft) {
        float angle = newTimeLeft * 360 / initialTime;
        rotate(angle);
    }

    private void changeBackground() {
        int current=(initialTime - timeLeft) * 8 / initialTime;
        if(current!=currentBackground){
            background.setImageResource(backgroundList[current]);
            Animation in = AnimationUtils.loadAnimation(TimeoutActivity.this, android.R.anim.fade_in);
            Animation out = AnimationUtils.loadAnimation(TimeoutActivity.this, android.R.anim.fade_out);
            background.setInAnimation(in);
            background.setOutAnimation(out);
        }
        currentBackground=current;


    }

    private void stopTimer() {
        timerIsRunning=false;
        updateButton();
        beach_sound.pause();
        stopService(new Intent(this, TimerService.class));
    }

    private void setUpMusic(){
        beach_sound = MediaPlayer.create(TimeoutActivity.this,R.raw.beach_sound);
    }

    private void startTimer() {
        beach_sound.start();
        updateButton();
        changeBackground();
        Intent timerIntent = new Intent(TimeoutActivity.this, TimerService.class);
        timerIntent.putExtra(TIME_LEFT,timeLeft);
        startService(timerIntent);
    }
    private void updateButton() {
        if(!timerIsRunning){
            timerButton.setText(START);
            optionButton.setAlpha(1);
        }
        else{
            timerButton.setText(STOP);
            optionButton.setAlpha(0);
        }
    }

    public void sendTimerNotification() {
        String title = getString(R.string.timerNotificationTitle);
        String message = getString(R.string.timerNotificationDescription);
        String actionButtonText = getString(R.string.notificationActionButtonText);
        int notificationID = 1;

        Intent notificationReceiverBroadcast = new Intent(this, NotificationReceiver.class);
        notificationReceiverBroadcast.putExtra("notification_id", notificationID);
        PendingIntent actionButtonIntent = PendingIntent.getBroadcast(this, 0,
                notificationReceiverBroadcast, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, TIMER_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_baseline_timer_24)
                .setContentTitle(title)
                .setColor(Color.GREEN)
                .setContentText(message)
                .addAction(R.drawable.ic_sharp_clear_24, actionButtonText, actionButtonIntent)
                .setFullScreenIntent(actionButtonIntent, true)
                .setDeleteIntent(actionButtonIntent)
                .setPriority(NotificationCompat.PRIORITY_MAX);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(notificationID, builder.build());

        Intent stopAlarmIntent = new Intent(TimeoutActivity.this, RingtonePlayService.class);
        TimeoutActivity.this.stopService(stopAlarmIntent);
        Intent startAlarmIntent = new Intent(TimeoutActivity.this, RingtonePlayService.class);
        TimeoutActivity.this.startService(startAlarmIntent);
    }

    public void fillBackgroundList() {
        backgroundList[0] = R.drawable.beach1;
        backgroundList[1] = R.drawable.beach2;
        backgroundList[2] = R.drawable.beach3;
        backgroundList[3] = R.drawable.beach4;
        backgroundList[4] = R.drawable.beach5;
        backgroundList[5] = R.drawable.beach6;
        backgroundList[6] = R.drawable.beach7;
        backgroundList[7] = R.drawable.beach8;
        backgroundList[8] = R.drawable.beach9;
    }

    private void rotate(float angle) {
        timerAnimation.setRotation(angle * -1);
    }

    private void updateTimer(int l) {
        int minute = l / 60;
        int second = l % 60;
        timeoutText.setText(getString(R.string.timerTextFormat, minute, second));
    }

    public int getDuration() {
        int newTime = TimeoutOptionActivity.getDuration(this);
        return newTime;
    }



    @Override
    protected void onStop() {
        super.onStop();
        beach_sound.pause();
        SharedPreferences prefs = getSharedPreferences(TIMER_SITUATION, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("timerIsRunning", timerIsRunning);
        editor.putInt("initialTime", initialTime);
        editor.putInt(TIME_LEFT,timeLeft);

        editor.apply();

    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMusic();
        SharedPreferences prefs = getSharedPreferences(TIMER_SITUATION, MODE_PRIVATE);
        timerIsRunning = prefs.getBoolean("timerIsRunning", false);
        if(timerIsRunning){
            beach_sound.start();
            updateButton();
            updateTimer(timeLeft);
            changeBackground();
        }
        else{
            timeLeft=prefs.getInt(TIME_LEFT,0);
            updateAnimation(timeLeft);
            updateTimer(timeLeft);
            updateButton();
            changeBackground();
        }
        if(getDuration()!=initialTime){
            initialTime=getDuration();
            timeLeft=getDuration();
            updateTimer(timeLeft);
            updateAnimation(timeLeft);
            changeBackground();

        }
    }


}
